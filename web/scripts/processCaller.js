/*
 *用户需要隐含提交数据时，在javascript中接调用callBizAction( bizAction, bizParam )函数
 *此函数通过XMLHttp对Server端的/internet/scripts/pageComponent/proxy.jsp发出请求，proxy.jsp完成对业务自动机的调用
 *参数说明：
 *bizAction：调用的业务自动机的名字
 *bizParam：调用业务自动机时传递的参数（XML格式）
 *返回值：如果发生错误（returnCode<0）返回false；成功，返回调用业务自动机执行后的返回值（XML格式）
 *
 *注：此函数依赖/internet/scripts/pageComponent/string.js中的trim()函数
 *
 *
 *例：callBizAction( "ROLE.biz.ROLE_B_addRoleOperator", "<root><data><EOSOperator><operatorID>12</operatorID></EOSOperator></data></root>");
*/

function callBizAction( bizAction, bizParam )
{
	function processReturnError( oReq )
	{	
		var retDom = new ActiveXObject("MSXML2.DOMDocument");
		retDom.async=false;
		retDom.loadXML( trim( oReq.responseText ) );
		
		var retCodeNode = retDom.selectSingleNode( "root/data/return/code" );
		if( !retCodeNode ) return true;
		
		var retCode = retCodeNode.text;
		
		if( !retCode ) return true;
		retCode = parseInt( retCode );
		
		//如果返回值小于0，说明业务逻辑处理发生错误，提示出错信息
		if( retCode < 0 )
		{
			var msg = retDom.selectSingleNode( "root/data/return/message" ).text;
			if( msg ) alert( msg );
			
			if( retCode == -99001 )	//超时，到登陆页面
			{
				var timeoutPage = retDom.selectSingleNode( "root/data/timeoutPage" ).text;
				var url = window.location.protocol+"//"+window.location.host+"//"+ timeoutPage;
				window.open( url, "_top");
			}
			return false;
		}
		//如果返回值等于0，说明业务逻辑处理成功，提示信息
		if(retCode==0)
		{
			var msg = retDom.selectSingleNode( "root/data/return/message" ).text;
			if( msg ) alert( msg );
		}
		
		return true;
	}
	//校验参数是否是正确的xml结构，如果不正确，默认参数为<root><data></data></root>
	var paramDom = new ActiveXObject("MSXML2.DOMDocument");
	try
	{
		paramDom.async=false;
		paramDom.loadXML( trim(bizParam) );
	}catch( e )
	{
		bizParam = "<root><data></data></root>";
	}
	//------------
	
	var proxyURL = window.location.protocol+"//"+window.location.host+"/bizService.call";
	proxyURL += "?bizAction=" + bizAction;
	
	var oReq  = new ActiveXObject("Microsoft.XMLHTTP");
	try{
		oReq.open("POST", proxyURL, false);
		oReq.send(escape(bizParam));
		
		//检查处理业务逻辑是否成功，若失败，提示出错信息
		if( !processReturnError( oReq ) ) return false;
	}catch( e )
	{
		return false;
		return "<root><data><return><code>-9900</code></return></data></root>";
	}	
	
	return trim( oReq.responseText );
}
