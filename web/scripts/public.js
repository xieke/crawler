//*************** 行选择改变颜色****************************
	var flag="-1";
	var param = 0;
	
	function changeTrColor(id)
	{
	if(!(flag=="-1"))
		 eval("document.getElementById('"+flag+"').bgColor='#efefef'");
	eval("document.getElementById('"+id+"').bgColor='#ffffff'");
	flag = id;
	param = flag.substring(1,flag.length);	
	}
	

	
	//检查一段字符串是否全由数字组成
function jtrim(str)
{     while (str.charAt(0)==" ")
          {str=str.substr(1);}      
     while (str.charAt(str.length-1)==" ")
         {str=str.substr(0,str.length-1);}
     return(str);
}

//函数名：fucCheckNUM
//功能介绍：检查是否为数字
//参数说明：要检查的数字
//返回值：1为是数字，0为不是数字

function IsNumber(string,sign)
{ 
var number; 
if (string==null) return false; 
if ((sign!=null) && (sign!='-') && (sign!='+')) 
{ 
alert('IsNumber(string,sign)的参数出错： sign为null或"-"或"+"'); 
return false; 
} 
number = new Number(string); 
if (isNaN(number)) 
{ 
return false; 
} 
else if ((sign==null) || (sign=='-' && number<0) || (sign=='+' && number>0)) 
{ 
return true; 
} 
else 
return false; 
} 

//*************** 行选择改变颜色****************************	  
	
	function LTrimString(StringIn)
	{
		var num=StringIn.length; 
		while(num!=0 && StringIn.substr(0,1)==" ")
		{
			StringIn=StringIn.substr(1,num-1);
			num=StringIn.length;
		}
		return StringIn
	}

//去掉字符串后面的空格，函数：RTrimString(StringIn)
	function RTrimString(StringIn)
	{
		var num=StringIn.length;
	
		while(num!=0 && StringIn.substr(num-1,1)==" ")
		{
			StringIn=StringIn.substr(0,num-1);
			num=StringIn.length;
		}
		return StringIn
	}
	
	//去掉字符串前后的空格，函数：TrimString(StringIn)
	
	function trim(StringIn)
	{
		return RTrimString(LTrimString(StringIn));
	} 

	<!--设置当前选中的objId------------------------------>
	function setObjId(id)
	{	
		//alert(id);	
		//alert(document.forms["form1"].elements["objId"]);

		document.form1.elements["objId"].value = id;
	}
	<!--检查当前是否选中objId------------------------------>
	function validObjId()
	{		
		if (document.form1.elements["objId"].value == '') {
			alert("请选中要编辑的记录");
			return false;
		}
		return true;
	}

	<!--设置当前的reqType------------------------------>
	function setReqType(reqType)
	{
		document.form1.elements["reqType"].value = reqType;
		//alert(document.form1.elements["reqType"].value);
	}
	
	<!---设置当前页数，提交----------------------------->
	function  setpage(a)	{ 
	
	   var form1 = window.form1;					
	   
	   document.form1.elements["page"].value = a;    	   
	   
	   document.form1.submit();

	   return false;
	}


/**
 * 获得字符串的字节数
 *
 * 示例: "test测试".getBytes() 值为 8
 *
 * @return字节数
*/
function getBytes() {
	var intCount = 0;
	for(var i = 0;i < this.length;i ++)
	{
		// Ascii 码大于 255 是双字节的字符
		if (this.charCodeAt(i) > 255) {
			intCount += 2;
		}
		else {
			intCount += 1;
		}
	}
	return intCount;
}
String.prototype.getBytes = getBytes;

/**
 * 检查对象数据, 根据输入参数弹出相应提示信息
 * @param p_CheckObject 要检查的对象名称
 * @param p_AlertText 对象数据检查没通过时的提示信息,
 * 格式必须为 "选择XX", 或者 "填写XX", 假如为空则不提示信息
 * @param p_Must 该对象是否必须选择或填写, 值为 "Y" 表示必须选择或填写,
 * 会强制检查; 其他值则不会强制检查, 但假如对象数据超过限制仍会进行提示
 * 并返回 false
 * @param p_Length 该项数据的长度限制, 按双字节计算,
 * 即为数据库字段定义的长度
 * @return 检查通过返回 true, 否则返回 false.
 */
function checkLength(p_CheckObject,p_AlertText,p_Must)
{
	var result = false;
	var formName = "form1";
	if (window.document[formName] != null)
	{
		var checkObject = window.document[formName][trim(p_CheckObject)];
		if (checkObject != null)
		{
			if (checkObject.value.length == 0 && p_Must == "Y")
			{
				if (trim(p_AlertText).length > 0) { //非空才提示
					alert("请" + trim(p_AlertText));
				}
				checkObject.focus();
			}
			else
			{
				//去掉回车
				checkObject.value = covertCRLFToBR(checkObject.value);
				result = true;
			}
		}
		else
		{
			alert("参数错误，对象 p_CheckObject: " + p_CheckObject + " 不存在。")
		}
	}
	else
	{
		alert("参数错误，表单 " + formName + " 不存在。")
	}
	return result;
}


//去掉字串左边的空格 
function lTrim(str) 
{ 
	if (str.charAt(0) == " ") 
	{ 
		//如果字串左边第一个字符为空格 
		str = str.slice(1);//将空格从字串中去掉 
		//这一句也可改成 str = str.substring(1, str.length); 
		str = lTrim(str); //递归调用 
	} 
	return str; 
} 

//去掉字串右边的空格 
function rTrim(str) 
{ 
	var iLength; 

	iLength = str.length; 
	if (str.charAt(iLength - 1) == " ") 
	{ 
		//如果字串右边第一个字符为空格 
		str = str.slice(0, iLength - 1);//将空格从字串中去掉 
		//这一句也可改成 str = str.substring(0, iLength - 1); 
		str = rTrim(str); //递归调用 
	} 
	return str; 
} 

//去掉字串两边的空格 
function trim(str) 
{ 
	return lTrim(rTrim(str)); 
} 



/**
 * 取当前日期时间,返回指定格式的日期时间到指定的对象
 * @param p_TimeStyle 时间日期的格式
 * @return 时间日期
 */
function getDateTime(p_TimeStyle) {
	var date = new Date();
	var year = date.getFullYear();		//年
	var month = date.getMonth() + 1;	//月
	var day = date.getDate();			//日
	//月份小于10时显示为'0X'
	if (month < 10) {
		month = "0" + month;
	}
	//天数小于10时显示为'0X'
	if (day < 10) {
		day = "0" + day;
	}
	var hour = date.getHours();			//小时
	var minute = date.getMinutes();		//分钟
	var second = date.getSeconds();		//秒
	var colon = ":";
	var dash = "-";
	var result = "";
	if (p_TimeStyle != null && p_TimeStyle != "") {
		if (p_TimeStyle == "") {
			result = year + dash + month + dash + day;
		}
		else if (p_TimeStyle == "yyyy-MM-dd") {
			result = year + dash + month + dash + day;
		}
		else if (p_TimeStyle == "yyyy-MM-dd HH:mm") {
			result = year + dash + month + dash + day + " "
				+ hour + colon + minute;
		}
		else if (p_TimeStyle == "yyyy-MM-dd HH:mm:ss") {
			result = year + dash + month + dash + day + " " 
				+ hour + colon + minute + colon + second;
		}
		else {
			alert("参数错误: \np_TimeStyle: *" + p_TimeStyle + "*\n\n" +
			"*使用方法举例:\n" + "getDateTime(aaa,'yyyy-MM-dd HH:mm');");
		}
	}
	else {
		result = year + dash + month + dash + day;
	}
	return result;
}

/**
 * 取当前日期时间,返回指定格式的日期时间到指定的对象
 * @param p_Object 指定的返回对象
 */
function setCurrentDate(p_Object) {
	if (p_Object != null) {
		p_Object.value = getDateTime("yyyy-MM-dd");
	}
	else {
		alert("参数错误: \np_Object: *" + p_Object + "*\n\n" +
			"*使用方法举例:\n" + "setCurrentTime(aaa,'yyyy-MM-dd');");
	}
}

/**
 * 取当月第一天,返回指定格式的日期时间到指定的对象
 * @param p_Object 指定的返回对象
 */
function setStartDate(p_Object) {
	var tempStr = "";
	if (p_Object != null) {
		tempStr = getDateTime("yyyy-MM-dd");
		p_Object.value = tempStr.substr(0,8) + "01";
	}
	else {
		alert("参数错误: \np_Object: *" + p_Object + "*\n\n" +
			"*使用方法举例:\n" + "setStartDate(aaa,'yyyy-MM-dd');");
	}
}

/**
 * 取当前日期时间,返回指定格式的日期时间到指定的对象
 * @param p_Object 指定的返回对象
 * @param p_TimeStyle 时间日期的格式
 */
function setCurrentTime(p_Object,p_TimeStyle) {
	if (p_Object != null) {
		if (p_TimeStyle != null) {
			p_Object.value = getDateTime(p_TimeStyle);
		}
		else {
			p_Object.value = getDateTime("");
		}
	}
	else {
		alert("参数错误: \np_Object: *" + p_Object + "*\n\n" +
			"*使用方法举例:\n" + "setCurrentTime2(aaa,'yyyy-MM-dd HH:mm:ss');");
	}
}

/**
 * 从文本框中逐个添加多值到某下拉列表
 * @param p_FromObj 数值来源的对象
 * @param p_Value 要添加的数值
 * @param p_ToObj 数值添加到的对象
 */
function AppendItem3(p_FromObj,p_DefaultValue,p_ToObj)
{
	var n = p_ToObj.length;
	var theValue;
	if (p_DefaultValue != null && p_DefaultValue != "")
	{
		theValue = p_DefaultValue + "";
	}
	else
	{
		theValue = p_FromObj.value;
	}

	if (theValue != null && theValue.length > 0)
	{
		var dataExist = false;
		//检查想要添加的值是否已被添加
		for (i = 0; i < n; i++)
		{
			if (p_ToObj.options[i].value == theValue) //已被添加
			{
				dataExist = true;
				break;
			}
			else
			{
				dataExist = false;
			}
		}
		if (!dataExist) //还未被添加才添加
		{
			p_ToObj.options[n] = new Option(theValue, theValue);
			p_FromObj.value = "";
		}
		else
		{
			alert("该数值 " + theValue + " 已被添加");
		}
	}
	else
	{
		alert("没有任何字符,无法进行添加");
	}
	p_FromObj.focus();
}

/**
 * 从下拉列表中删除数据
 * @param p_Obj 要进行操作的对象
 */
function RemoveItem3(p_Obj)
{
	var selectedItemCount = 0;
	for (i = p_Obj.length-1; i >= 0; i--)
	{
		if (p_Obj.options[i].selected)
		{
			if (p_Obj.options.length == 1) {
				p_Obj.options[i].value = "";
				p_Obj.options[i].text = "";
			}
			else {
				p_Obj.options[i] = null;
			}
			selectedItemCount++;
		}
	}
	if (selectedItemCount == 0)
	{
		alert("请先选择想要删除的数据再进行删除");
	}
}

/**
 * 初始化本窗口的大小和位置，窗口位置默认在屏幕中间
 * 假如窗口的宽、高太大太小或比例失调则设置为默认大小 640 * 480
 * @param p_Width 窗口宽度
 * @param p_Height 窗口高度
 */
function resizeWindow(p_Width,p_Height)
{
	var width = 0;
	var height = 0;
	var defWidth = screen.width; //默认窗口宽度
	var defHeight = screen.height; //默认窗口高度
	var minWidth = 300; //最小窗口宽度
	var maxWidth = 800; //最大窗口宽度
	var minHeight = 200; //最小窗口高度
	var maxHeight = 600; //最大窗口高度
	var goodProportion = true;
	if ((p_Width / p_Height > 2) || (p_Height / p_Width > 2)) {
		goodProportion = false;
	}
	if (p_Width > minWidth && p_Width <= maxWidth && p_Height >
		minHeight && p_Height <= maxHeight && goodProportion) { //大小合适
		width = p_Width;
		height = p_Height;
	}
	else { //设为默认值
		width = defWidth;
		height = defHeight;
	}
	//alert("width:" + width + ",height:" + height + ",goodProportion:" + goodProportion);
	window.resizeTo(width,height); //将窗口伸缩成指定大小
	var originX = (screen.width - width) / 2;
	var originY = (screen.height - height) /2;
	window.moveTo(originX,originY); //将窗口移动到屏幕中间
}

/**
 * 客户端检查非法字符, 不得含有 ""
 * @param p_String 待检查的字符串
 * @return 合法字符串返回 true, 否则返回 false
 */
function isValidString(p_String) {
	var returnValue = true;
	if (p_String != null) {
		//if (/[\W]/g.test(p_String)) { 检查很多非法字符
		if (/[\\{}\'\[\]\"|<>#$^&`~]/.test(p_String)) {
			alert("不得含有非法字符: \n" + p_String);
			returnValue = false;
		}
	}
	else {
		alert("参数错误: \np_String: *" + p_String + "*\n\n");
		returnValue = false;
	}
	return returnValue;
}

/**
 * 客户端把字符串中的回车去掉
 * @param p_String 待替换的字符串
 * @return 返回替换后的字符串
 */
function covertCRLFToBR(p_String)
{
	var i;
	var str = p_String;
	while(str.indexOf("\r\n") > 0) 
	{
		i = str.indexOf("\r\n");
		str = str.substring(0, i) + "" + str.substring(i + 2, str.length);
	}
	return str;
}

/**
*定义物料类
*@param id 物料id
*@param name 物料名
*@param amount 物料数量
*/
function material(id,name,amount)
{
	this.id=id;
	this.name=name;
	this.amount=amount;
}

//判断值是否未空（包括全空格）
function isnull(p)
{
  if(p==null)
  {
    return true;
  }
  var regStr=/\S/;
  if(p.search(regStr)==-1)
  {
    return true;
  }
  else
  {
    return false;
  }
}
//判断是否数值,包括负点数
function isNumber(value)
{
	if(isnull(value))
	{
		return false;
	}
	if(isNaN(value))
	{
		return false;
	}
	else
	{
		return true;
	}
}

//本本增加把参数文本存入内容。
function bencopy(txt) {   
     if(window.clipboardData) {   
             window.clipboardData.clearData();   
             window.clipboardData.setData("Text", txt);   
     } else if(navigator.userAgent.indexOf("Opera") != -1) {   
          window.location = txt;   
     } else if (window.netscape) {   
          try {   
               netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");   
          } catch (e) {   
               alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");   
          }   
          var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);   
          if (!clip)   
               return;   
          var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);   
          if (!trans)   
               return;   
          trans.addDataFlavor('text/unicode');   
          var str = new Object();   
          var len = new Object();   
          var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);   
          var copytext = txt;   
          str.data = copytext;   
          trans.setTransferData("text/unicode",str,copytext.length*2);   
          var clipid = Components.interfaces.nsIClipboard;   
          if (!clip)   
               return false;   
          clip.setData(trans,null,clipid.kGlobalClipboard);   
          alert("复制成功！")   
     }   
}
