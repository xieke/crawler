/*
-------------------------------------------------------------------------------
文件名称：openWin.js
说    明：JavaScript脚本，用于网页中弹出窗口的处理
版    本：1.0
修改纪录:
---------------------------------------------------------------------------
时间			修改人		说明
2002-8-29	libo		创建
2004-6-21	YuanYi		修改(多次点击只创建一个窗口、模式窗口的返回值)
2004-6-21	zhou ming		修改(模式窗口和父窗口的数据交换)
2004-6-21	zhou ming		showModal增加了两个参数winLeft,winTop
2004-6-21	zhou ming		openNewWindow增加了两个参数winLeft,winTop
2005-12-19  chenchun	修改showModal,使得默认弹出窗口居中
------------------------------------------------------------------------------- 	
*/

/*
/*
用途：弹出模式窗口
	此功能只能在IE5.0以上浏览器使用。
	弹出窗口的风格为居中，没有状态栏，没有IE按钮，菜单,地址栏
输入：
	strUrl：  	弹出窗口内显示的网页的地址
	winWidth：	弹出窗口的宽度，单位为px
	winHeight:	弹出窗口的高度，单位为px
	winLeft:	弹出窗口的左坐标，单位为px
	winTop:	    弹出窗口的顶坐标，单位为px
返回：
	如果通过验证返回true,否则返回false	
*/
function showModal( strUrl,winWidth,winHeight){
	var left = "", top = "";
//	alert(arguments[3] + ":" + arguments[4]);
	if(arguments[3] != null) left = "dialogLeft:" + arguments[3] + "px;"
	if(arguments[4] != null) top = "dialogTop:" + arguments[4] + "px;"
	return window.showModalDialog(strUrl,
									window,
									"dialogWidth:"+ winWidth + "px;" + "dialogHeight:"+winHeight + "px;" 
									+ left + top 
									+ "directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
}


/*用途：弹出窗口
输入：
	strUrl：  	弹出窗口内显示的网页的地址
	winWidth：	弹出窗口的宽度，单位为px
	winHeight:	弹出窗口的高度，单位为px
	winLeft:	弹出窗口的左坐标，单位为px
	winTop:	    弹出窗口的顶坐标，单位为px
	type :      是否有菜单栏
返回：
	如果通过验证返回true,否则返回false	
*/
function openNewWindow( strUrl,winWidth,winHeight,winLeft,winTop,type){
	if (type==null) type=false;
	if(type==true||type=="true")
	{
		newwin = window.open(	strUrl,
								"popupnav",
								"width="+ winWidth + ","
								+ "height="+winHeight + ","
								+ "left="+winLeft+","
								+ "top="+winTop+","
								+ "status=no,menubar=yes,scrollbars=yes");

		newwin.focus();
	}
	else
	{
		newwin = window.open(	strUrl,
								"popupnav",
								"width="+ winWidth + ","
								+ "height="+winHeight + ","
								+ "left="+winLeft+","
								+ "top="+winTop+","
								+ "status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
		newwin.focus();
	}
}