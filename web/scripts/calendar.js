/**
* calendar.js author:Chen Chun
*
* 功能描述:本日历控件(以下简称控件)采用隐藏层的实现方式，避免了弹出窗口式可能被浏览器拦截的麻烦。
* 控件的调出函数被触发后，在指定位置会弹出控件面板。向文本框输入数据时，先要选择日期，然后用鼠标
* 点击面板上某一天的单元格。这时，日历控件会把你选择的时间自动填到文本框里。控件面板最上面显示的是
* 年和月。可以按住键盘上的↑,↓,←,→来分别调整年和月；也可以点击面板最下面一栏，"Today"按钮两侧
* 的<<,<,>,>>来调整年和月。如果点击"Today"按钮，则会把当前时间传给文本框。从上至下第二栏的"日，
* 一，二，三，四，五，六"表示的是星期，不做日期输入，只用来提示时间；如果用鼠标按住此栏，可以托拽
* 控件到页面任意位置。星期栏下面是控件主操作区，用鼠标点击某一天，即可把选中的时间传给指定文本框。
* 主操作区下面显示的是时分秒，用鼠标点击时分秒其中任意一个数字均会显示相应的微调按钮，以便调整时间。
* 回填给文本框的日期，目前支持以下几种：yyyyMMddHHmmss,yyyyMMdd,yyyy-MM-dd HH:mm:ss,yyyy-MM-dd,
* yyyy/MM/dd HH:mm:ss,yyyy/MM/dd,yyyy年MM月dd日HH时mm分ss秒,yyyy年MM月dd日
*
* 调用方法: 
* 方法一:calendar()，该函数适合直接由文本框调用。
* 方法二:calendar(obj,format)，该函数适合由任意指定的页面元素调用。其中参数obj为输出的目标，format
* 为输出的日期格式。
* 
* 因为本日历控件的图片使用的是相对路径，所以因页面所在文件夹的不同有可能图片显示不出来，
* 这时请修改图片相对路径: this.imageRelativePath = "/pageComponent/resources/images/calendar";
*
*/

<!--
document.write("<div id=eosCalendarLayer style='position: absolute; z-index: 9999; width: 185; height: 233; display: none'>");
document.write("<iframe src='scripts/blank.htm' name=eosCalendarIframe scrolling=no frameborder=0 width=100% height=100%></iframe></div>");

//日期控件高度 19+20+120+19+20
function writeIframe(parent)	//对iframe进行初始化
{
	var strIframe = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=gb2312'><style>"+
    "*{font-size: 12px; font-family: 宋体}"+
	".operate { cursor: hand; }"+
	".textarea{	FONT-FAMILY: Tahoma;font-size:8pt;height: 17px;padding-right:2px;text-align:right;}"+
    ".bg{ color: "+ parent.lightColor +"; cursor: default; background-color: "+ parent.darkColor +";}"+
	"table#tableHead td{ font-size:14px; font-family:宋体; font-weight:bold; color:#FFFFFF; }"+
    "table#tableMain{ width:180; height:198;}"+
    "table#tableWeek td{ font-size:12px; font-family:宋体; font-weight:bold; color:#00008B; "+
						"background-color:"+ parent.darkColor +"; border:0px; }"+
    "table#tableDay td{ font-size: 12px; font-family:宋体; font-color: #000000;border: 0px solid;}"+
	"table#tableTime td{ cursor: hand; font-size: 12px; font-family:宋体; font-color: #000000;border: 0px solid;}"+
    "td#meizzYearHead, td#meizzYearMonth{color: "+ parent.wordColor +"}"+
    ".out { text-align: center; border-top: 1px solid "+ parent.DarkBorder +"; border-left: 1px solid "+ 
			parent.DarkBorder +"; border-right: 1px solid "+ parent.lightColor +"; border-bottom: 1px solid "+ parent.lightColor +"; }"+
    ".over{ text-align: center; border-top: 1px solid #FFFFFF; border-left: 1px solid #FFFFFF;"+
    "border-bottom: 1px solid "+ parent.DarkBorder +"; border-right: 1px solid "+ parent.DarkBorder +"}"+
    "</style></head><body onselectstart='return false' style='margin: 0px' oncontextmenu='return false'><form name=meizz>";

    if (parent.drag)
	{ 
		strIframe += "<script language=javascript>"+
			"var drag=false, cx=0, cy=0, o = parent.WebCalendar.calendar; function document.onmousemove(){"+
			"if(parent.WebCalendar.drag && drag){if(o.style.left=='')o.style.left=0; if(o.style.top=='')o.style.top=0;"+
			"o.style.left = parseInt(o.style.left) + window.event.clientX-cx;"+
			"o.style.top  = parseInt(o.style.top)  + window.event.clientY-cy;}}"+
			"function document.onkeydown(){ switch(window.event.keyCode){  case 88 : parent.hiddenCalendar(); break;"+
			"case 37 : parent.prevM(); break; case 38 : parent.prevY(); break; case 39 : parent.nextM(); break; case 40 : parent.nextY(); break;"+
			"case 84 : document.forms[0].today.click(); break;} window.event.keyCode = 0; window.event.returnValue= false;}"+
			"function dragStart(){cx=window.event.clientX; cy=window.event.clientY; drag=true;}"+
			"function scrolltextarea(obj,min,max){ var textareaMin = min; var textareaMax = max; if (obj.scrollTop==0)"+
			"{ obj.value = (parseInt(obj.value) > textareaMax-1)?textareaMin:(parseInt(obj.value)+1); }	else if (obj.scrollTop==2)"+
			"{ obj.value = (parseInt(obj.value) < textareaMin+1)?textareaMax:(parseInt(obj.value)-1);} obj.scrollTop = 1; }</scr"+"ipt>"
	}

	/**
	* 年份、月份、时、分、秒的选择控件,
	* 以及在日期控件上的位置。
	*/
    strIframe += "<select name=tmpYearSelect  onblur='parent.hiddenSelect(this)' style='z-index:1;position:absolute;top:5;left:33;display:none'"+
    " onchange='parent.WebCalendar.thisYear =this.value; parent.hiddenSelect(this); parent.writeCalendar();'></select>"+
    "<select name=tmpMonthSelect onblur='parent.hiddenSelect(this)' style='z-index:1; position:absolute;top:5;left:95;display:none'"+
    " onchange='parent.WebCalendar.thisMonth=this.value; parent.hiddenSelect(this); parent.writeCalendar();'></select>";

	if(parent.timeShow == true)
		strIframe += "<textarea onscroll=\"scrolltextarea(this,0,23)\"  class=\"textarea\" rows=\"1\" name=\"tmpHourSelect\" cols=\"3\""+ 
			"onblur='parent.WebCalendar.thisHour=this.value; parent.hiddenTextarea(this); parent.writeCalendar();' style='z-index:1; position:absolute;top:176;left:7;display:none'"+
			" onchange='parent.hiddenTextarea(this); parent.writeCalendar();'></textarea>"+
			"<textarea onscroll='scrolltextarea(this,0,59)'  class='textarea' rows='1' name='tmpMinuteSelect' cols='3'"+ "onblur='parent.WebCalendar.thisMinute=this.value; parent.hiddenTextarea(this); parent.writeCalendar();' style='z-index:1; position:absolute;top:176;left:66;display:none'"+
			" onchange='parent.hiddenTextarea(this); parent.writeCalendar();'></textarea>"+
			"<textarea onscroll='scrolltextarea(this,0,59)'  class='textarea' rows='1' name='tmpSecondSelect' cols='3'"+ "onblur='parent.WebCalendar.thisSecond=this.value; parent.hiddenTextarea(this); parent.writeCalendar();' style='z-index:1; position:absolute;top:176;left:127;display:none'"+
			" onchange='parent.hiddenTextarea(this); parent.writeCalendar();'></textarea>";

	strIframe += 
    "<div><table id=tableMain cellspacing=0 cellpadding=0 style='background-color:#F6F6F6;border:1px solid #0054E3;'>"+
    "<tr><td>"+
    "<table width=180 height=30 id=tableHead border=0 cellspacing=0 cellpadding=0 background='"+ parent.imageRelativePath +"/TaskBarBG.gif' bgcolor='#255FDC'><tr align=center>"+
	"<td><image class='operate' src='"+ parent.imageRelativePath +"/_calprevyr.gif' title='向前翻 1 年&#13;快捷键：↑' onclick='parent.prevY()' onfocus='this.blur()' style='meizz:expression(this.disabled=parent.WebCalendar.thisYear==1000)'></td>" + 
	"<td><image class='operate' src='"+ parent.imageRelativePath +"/_calprevmm.gif' onfocus='this.blur()' title='向前翻 1 月&#13;快捷键：←' onclick='parent.prevM()'>"+
    "</td>"+
    "<td width=60 class='operate' id=meizzYearHead  title='点击此处选择年份' onclick='parent.funYearSelect(parseInt(this.innerText, 10))'"+
	"</td><td width=15>年</td>"+
    "<td width=60 class='operate' id=meizzYearMonth title='点击此处选择月份' onclick='parent.funMonthSelect(parseInt(this.innerText, 10))'"+
	"</td><td width=15>月</td>"+
	"<td align=right><image class='operate' src='"+ parent.imageRelativePath +"/_calnextmm.gif' title='向后翻 1 月&#13;快捷键：→' onclick='parent.nextM()' onfocus='this.blur()'></td>"+
	"<td><image class='operate' src='"+ parent.imageRelativePath +"/_calnextyr.gif' title='向后翻 1 年&#13;快捷键：↓' onclick='parent.nextY()' onfocus='this.blur()' style='meizz:expression(this.disabled=parent.WebCalendar.thisYear==9999)'></td></tr>"+
	"<tr><td colspan=8 width=180 height=2 background='"+ parent.imageRelativePath +"/progressBar.gif'></td></tr>"+
	"</table></td></tr>"+
	"<tr><td height=20><table id=tableWeek width=180 cellspacing=1 cellpadding=0 background-color:#CCCCCC ";

    if(parent.drag)
		strIframe += "onmousedown='dragStart()' onmouseup='drag=false' onmouseout='drag=false'";

	strIframe += "><tr align=center><td height=20>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr></table>"+
		"</td></tr><tr><td valign=top width=180 bgcolor='"+ parent.lightColor +"'>"+
	    "<table id=tableDay height=120 width=180 cellspacing=1 cellpadding=1 style='background-color:#CCCCCC;border:0px;'>";
         for(var x=0; x<5; x++){ strIframe += "<tr>";
         for(var y=0; y<7; y++)  strIframe += "<td class=out id='meizzDay"+ (x*7+y) +"'></td>"; strIframe += "</tr>";}
         strIframe += "<tr>";
         for(var x=35; x<42; x++) strIframe += "<td class=out id='meizzDay"+ x +"'></td>";
	
	strIframe += "</tr></table></td></tr>";
	
	/**
	* 时、分、秒选择栏。当鼠标落在上面时，相应区域变色，点击后可以见到时分秒的调节框。
	*/
	if(parent.timeShow) 	
		strIframe += "<tr><td width=180 height=25>"+
			"<table width='100%' height='100%' id=tableTime border=0 cellspacing=0 cellpadding=0 bgcolor='"+ parent.lightColor +"'><tr align=center>"+
			"<td width=59 id=hourTD align='right'></td>"+
			"<td width=59 id=minuteTD align='right'></td>"+
			"<td width=59 id=secondTD align='right'></td></tr></table></td></tr>";
	
	strIframe +="<tr><td height=30 width=180>"+
		"<table width='100%' height='100%' border=0 cellpadding=0 cellspacing=0 bgcolor='"+ parent.darkColor + "'><tr><td align=center width=60>"+
		"<a href='javascript:parent.returnCurDate();' onfocus='this.blur()' title='当前日期&#13;快捷键：T'>"+
		"<font color='#444444' style='font-size:14px; font-weight:bold;'>今天</font></a></td>"+
		"<td align=center  width=60'><a href='javascript:parent.clsCalendar();' onfocus='this.blur()' title='关闭快捷键:X'>"+
		"<font color='#444444' style='font-size:14px;'>清除</font></a></td>"+
		"<td align=center  width=60><a href='javascript:parent.hiddenCalendar();' onfocus='this.blur()' title='关闭快捷键:X'>"+
		"<font color='#444444' style='font-size:14px;'>取消</font></a></td>"+
		"</tr></table></td></tr><table></div></form></body></html>";

    with(parent.iframe)
    {
        document.writeln(strIframe); document.close();
        for(var i=0; i<42; i++)
        {
            WebCalendar.dayObj[i] = eval("meizzDay"+ i);
            WebCalendar.dayObj[i].onmouseover = dayMouseOver;
            WebCalendar.dayObj[i].onmouseout  = dayMouseOut;
            WebCalendar.dayObj[i].onclick     = returnDate;
        }
    }
}

function WebCalendar() //初始化日历的设置
{
    this.daysMonth  = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    this.day        = new Array(42);				//定义日历展示用的数组
    this.dayObj     = new Array(42);				//定义日期展示控件数组
    this.dateStyle  = null;							//保存格式化后日期数组
    this.iframe     = window.frames("eosCalendarIframe");						//日历的 iframe 载体
    this.calendar   = getObjectById("eosCalendarLayer");						//日历的层

	this.objExport  = null;							//日历回传的显示控件
    this.eventSrc   = null;							//日历显示的触发控件
    
	this.inputDate  = null;							//转化外的输入的日期(d/m/yyyy)
    this.thisYear   = new Date().getFullYear();		//定义年的变量的初始值
    this.thisMonth  = new Date().getMonth()+ 1;		//定义月的变量的初始值
    this.thisDay    = new Date().getDate();			//定义日的变量的初始值
    this.thisHour   = new Date().getHours();		//定义时的变量的初始值
    this.thisMinute = new Date().getMinutes();      //定义分的变量的初始值
    this.thisSecond = new Date().getSeconds();      //定义秒的变量的初始值
    this.today      = this.thisDay +"/"+ this.thisMonth +"/"+ this.thisYear;    //今天(d/m/yyyy)
    
    this.dateReg    = "";           //日历格式验证的正则式
	//以下属性用户可定制
    this.yearFall   = 50;           //定义年下拉框的年差值
    this.format     = "yyyy-MM-dd"; //*回传日期的格式(目前支持yyyy-MM-dd,yyyy/MM/dd)	
    this.timeShow   = true;         //*是否返回时间	
    this.drag       = true;         //是否允许拖动
    this.darkColor  = "#B3D7FF";	//控件的暗色
    this.lightColor = "#FFFFFF";    //控件的亮色
    this.wordColor  = "#000000";    //控件的文字颜色
    this.wordDark   = "#C0C0C0";    //控件的暗文字颜色(当前月之外的日期数字的颜色)
    this.dayBgColor = "FFFFFF";		//日期数字背景色
    this.todayColor = "#FFFFCC";    //今天在日历上的标示背景色
	this.inputColor = "#B0C4DE";	//选中的日期在日历上的标示背景
    this.DarkBorder = "#CCCCCC";	//日期显示的立体表达色
	this.isOpen = false;			//日历是否已打开
	this.imageRelativePath = "images/calendar";
} 

var WebCalendar = new WebCalendar();

//用户的主调函数
function calendar() 
{	
	if(WebCalendar.isOpen != true)
	{	
		if(arguments[1] == undefined || arguments[1] == "undefined")
			WebCalendar.format = "yyyy-MM-dd HH:mm:ss";
		else
			WebCalendar.format = arguments[1];

		if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format))
		{
			WebCalendar.dateReg = /^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
			WebCalendar.timeShow = false;
		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(WebCalendar.format))
		{
			WebCalendar.dateReg = /^(\d{4})年(\d{1,2})月(\d{1,2})日$/;
			WebCalendar.timeShow = false;
		}
		else if(WebCalendar.format=="yyyyMMdd")
		{
			WebCalendar.dateReg = /^(\d{4})(\d{2})(\d{2})$/;
			WebCalendar.timeShow = false;
		}
		else if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2}) (HH:mm:ss)$/.test(WebCalendar.format))
		{
			WebCalendar.dateReg = /^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
			WebCalendar.timeShow = true;
		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/.test(WebCalendar.format))
		{
			WebCalendar.dateReg = /^(\d{4})年(\d{1,2})月(\d{1,2})日(\d{1,2})时(\d{1,2})分(\d{1,2})秒$/;
			WebCalendar.timeShow = true;
		}
		else if(WebCalendar.format=="yyyyMMddHHmmss")
		{	
			WebCalendar.dateReg = /^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/;
			WebCalendar.timeShow = true;
		}
		else
		{
			WebCalendar.format = "yyyy-MM-dd HH:mm:ss";
			WebCalendar.dateReg = /^(\d{4})(-|\/)(\d{2})\2(\d{2}) (\d{2}):(\d{2}):(\d{2})$/;
			WebCalendar.timeShow = true;
		}

		var e = window.event.srcElement;
		writeIframe(WebCalendar);
		var o = WebCalendar.calendar.style;
		WebCalendar.eventSrc = e;
		if (arguments.length == 0) 
		  WebCalendar.objExport = e;
		else 
		  WebCalendar.objExport = eval(arguments[0]);
		WebCalendar.iframe.tableWeek.style.cursor = WebCalendar.drag ? "move" : "default";
		
		funBindTo(WebCalendar.objExport);	//将日历显示位置绑至某一控件下

		o.display = ""; 	
		WebCalendar.iframe.document.body.focus();
		
		var cw = WebCalendar.calendar.clientWidth, ch = WebCalendar.calendar.clientHeight;
		var dw = document.body.clientWidth, dl = document.body.scrollLeft, dt = document.body.scrollTop;
	 
		try{
			if (WebCalendar.objExport.value.trim() != ""){
				WebCalendar.dateStyle = WebCalendar.objExport.value.trim().match(WebCalendar.dateReg);
				if (WebCalendar.dateStyle == null)
				{
					WebCalendar.thisYear   = new Date().getFullYear();
					WebCalendar.thisMonth  = new Date().getMonth()+ 1;
					WebCalendar.thisDay    = new Date().getDate(); 
					WebCalendar.thisHour   = new Date().getHours();	
					WebCalendar.thisMinute = new Date().getMinutes();
					WebCalendar.thisSecond = new Date().getSeconds(); 
					writeCalendar(); return false;
				}
				else
				{
					if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format) || 
						/^(y{4})(-|\/)(M{1,2})\2(d{1,2}) (HH:mm:ss)$/.test(WebCalendar.format))
					{
						WebCalendar.thisYear   = parseInt(WebCalendar.dateStyle[1], 10);
						WebCalendar.thisMonth  = parseInt(WebCalendar.dateStyle[3], 10);
						WebCalendar.thisDay    = parseInt(WebCalendar.dateStyle[4], 10);
						WebCalendar.thisHour   = parseInt(WebCalendar.dateStyle[5], 10);
						WebCalendar.thisMinute = parseInt(WebCalendar.dateStyle[6], 10);
						WebCalendar.thisSecond = parseInt(WebCalendar.dateStyle[7], 10);
					}
					else
					{
						WebCalendar.thisYear   = parseInt(WebCalendar.dateStyle[1], 10);
						WebCalendar.thisMonth  = parseInt(WebCalendar.dateStyle[2], 10);
						WebCalendar.thisDay    = parseInt(WebCalendar.dateStyle[3], 10);
						WebCalendar.thisHour   = parseInt(WebCalendar.dateStyle[4], 10);
						WebCalendar.thisMinute = parseInt(WebCalendar.dateStyle[5], 10);
						WebCalendar.thisSecond = parseInt(WebCalendar.dateStyle[6], 10);
					}
					WebCalendar.inputDate  = parseInt(WebCalendar.thisDay, 10) +"/"+ parseInt(WebCalendar.thisMonth, 10) +"/"+ 
											parseInt(WebCalendar.thisYear, 10); 
					writeCalendar(); 
					WebCalendar.isOpen = true;
				}
			}  else { 
				WebCalendar.thisYear   = new Date().getFullYear();		
				WebCalendar.thisMonth  = new Date().getMonth()+ 1;		
				WebCalendar.thisDay    = new Date().getDate();			
				WebCalendar.thisHour   = new Date().getHours();		
				WebCalendar.thisMinute = new Date().getMinutes();      
				WebCalendar.thisSecond = new Date().getSeconds();      
				writeCalendar(); WebCalendar.isOpen = true; }
		}  catch(e){ writeCalendar();WebCalendar.isOpen = true; }
	}
	else
	{
		hiddenCalendar(); 
	}
}

function funBindTo(target)	//将日历绑至某一控件下
{
	var t = target.offsetTop,  h = target.clientHeight, l = target.offsetLeft, p = target.type;
	var o = WebCalendar.calendar.style;

	while (target = target.offsetParent)
	  {t += target.offsetTop; l += target.offsetLeft;}

	var cw = parseInt(o.width), ch = parseInt(o.height);
    var dw = document.body.clientWidth, dl = document.body.scrollLeft, dt = document.body.scrollTop, dh = document.body.clientHeight;

	if (dh + dt - t - h >= ch) 
	  o.top = (p=="image")? t + h : t + h + 6;
    else 
	  o.top  = (t - dt < ch) ? ((p=="image")? t + h : t + h + 6) : (t - ch);
    if (dw + dl - l >= cw) 
	  o.left = l; 
	else 
	  o.left = (dw >= cw) ? dw - cw + dl : dl;
}

function funMonthSelect() //月份的下拉框
{
    var m = isNaN(parseInt(WebCalendar.thisMonth, 10)) ? new Date().getMonth() + 1 : parseInt(WebCalendar.thisMonth);
    var e = WebCalendar.iframe.document.forms[0].tmpMonthSelect;
    for (var i=1; i<13; i++) e.options.add(new Option(i, i));// +"月"
    e.style.display = ""; e.value = m; e.focus(); window.status = e.style.top;//??
}

function funYearSelect() //年份的下拉框
{
    var n = WebCalendar.yearFall;
    var e = WebCalendar.iframe.document.forms[0].tmpYearSelect;
    var y = isNaN(parseInt(WebCalendar.thisYear, 10)) ? new Date().getFullYear() : parseInt(WebCalendar.thisYear);
        y = (y <= 1000)? 1000 : ((y >= 9999)? 9999 : y);
    var min = (y - n >= 1000) ? y - n : 1000;
    var max = (y + n <= 9999) ? y + n : 9999;
        min = (max == 9999) ? max-n*2 : min;
        max = (min == 1000) ? min+n*2 : max;
    for (var i=min; i<=max; i++) e.options.add(new Option(i, i));// +"年"
    e.style.display = ""; e.value = y; e.focus();
}

function funHourSelect() //小时的滚动按钮
{    
	if(WebCalendar.timeShow) 
	{ 
		var h = isNaN(parseInt(WebCalendar.thisHour, 10)) ? new Date().getHours() : parseInt(WebCalendar.thisHour);
		var e = WebCalendar.iframe.document.forms[0].tmpHourSelect;
		e.style.display = ""; e.value = h; e.focus(); 
	}
}

function funMinuteSelect() //分钟的滚动按钮
{
	if(WebCalendar.timeShow) 
	{
		var mi = isNaN(parseInt(WebCalendar.thisMinute, 10)) ? new Date().getMinutes() : parseInt(WebCalendar.thisMinute);
		var e = WebCalendar.iframe.document.forms[0].tmpMinuteSelect;
		e.style.display = ""; e.value = mi; e.focus();
	}
}

function funSecondSelect() //秒的滚动按钮
{
	if(WebCalendar.timeShow) 
	{
		var s = isNaN(parseInt(WebCalendar.thisSecond, 10)) ? new Date().getSeconds : parseInt(WebCalendar.thisSecond);
		var e = WebCalendar.iframe.document.forms[0].tmpSecondSelect;
		e.style.display = ""; e.value = s; e.focus();
	}
}

function prevM()  //往前翻月份
{
    WebCalendar.thisDay = 1;
    if (WebCalendar.thisMonth==1)
    {
        WebCalendar.thisYear--;
        WebCalendar.thisMonth=13;
    }
    WebCalendar.thisMonth--; 
	writeCalendar();
}

function nextM()  //往后翻月份
{
    WebCalendar.thisDay = 1;
    if (WebCalendar.thisMonth==12)
    {
        WebCalendar.thisYear++;
        WebCalendar.thisMonth=0;
    }
    WebCalendar.thisMonth++; 
	writeCalendar();
}

//往前翻 Year
function prevY()
{WebCalendar.thisDay = 1; WebCalendar.thisYear--; writeCalendar();}

//往后翻 Year
function nextY()
{WebCalendar.thisDay = 1; WebCalendar.thisYear++; writeCalendar();}

//隐藏下拉列表
function hiddenSelect(e)
{for(var i=e.options.length; i>-1; i--)e.options.remove(i); e.style.display="none";}

//隐藏微调按钮
function hiddenTextarea(e)
{e.style.display="none";}

//获取对象
function getObjectById(id)
{ 
  if(document.all) 
    return(eval("document.all."+ id)); 
  return(eval(id));
}

//隐藏日历
function hiddenCalendar()
{
  getObjectById("eosCalendarLayer").style.display = "none";
  WebCalendar.isOpen = false;
}


//日期自动补零程序
function appendZero(n)
{return(("00"+ n).substr(("00"+ n).length-2));}

//覆盖字符串的去空格函数
function String.prototype.trim()
{return this.replace(/(^\s*)|(\s*$)/g,"");}

//鼠标移到某天时的事件
function dayMouseOver()
{
    this.className = "over";
    this.style.backgroundColor = WebCalendar.darkColor;
    if(WebCalendar.day[this.id.substr(8)].split("/")[1] == WebCalendar.thisMonth)
    this.style.color = WebCalendar.lightColor;
	this.style.cursor = "hand";
}

//鼠标移出某天时的事件
function dayMouseOut()
{
    this.className = "out"; 
	var d = WebCalendar.day[this.id.substr(8)], a = d.split("/");
    this.style.removeAttribute('backgroundColor');
    if(a[1] == WebCalendar.thisMonth && d != WebCalendar.today)
    {
        if(WebCalendar.dateStyle && a[0] == parseInt(WebCalendar.dateStyle[4], 10))
        this.style.color = WebCalendar.lightColor;
        this.style.color = WebCalendar.wordColor;
    }
}

//对日历显示的数据的处理程序
function writeCalendar() 
{
    var y = WebCalendar.thisYear;
    var m = WebCalendar.thisMonth; 
    var d = WebCalendar.thisDay;
	var h = WebCalendar.thisHour;
	var mi = WebCalendar.thisMinute;
	var s = WebCalendar.thisSecond;
    WebCalendar.daysMonth[1] = (0==y%4 && (y%100!=0 || y%400==0)) ? 29 : 28;
    if (!(parseInt(y, 10)<=9999 && parseInt(y, 10) >= 1000 && parseInt(m, 10)>=0 && parseInt(m, 10)<=12 && parseInt(d, 10)>=0
		 && parseInt(h, 10)<=23 && parseInt(h, 10)>=0 && parseInt(mi, 10)<=59 && parseInt(mi, 10)>=0
		 && parseInt(s, 10)<=59 && parseInt(s, 10)>=0))
	{
		//alert("对不起，你输入了错误的日期！");
        WebCalendar.thisYear     = new Date().getFullYear();
        WebCalendar.thisMonth    = new Date().getMonth()+ 1;
        WebCalendar.thisDay		 = new Date().getDate(); 
		WebCalendar.thisHour	 = new Date().getHours(); 
		WebCalendar.thisMinute   = new Date().getMinutes(); 
		WebCalendar.thisSecond   = new Date().getSeconds(); 
	}
    WebCalendar.iframe.meizzYearHead.innerText  = y;
    WebCalendar.iframe.meizzYearMonth.innerText = m;
	if(WebCalendar.timeShow)
	{
		WebCalendar.iframe.hourTD.innerText = h +" 时";
		WebCalendar.iframe.hourTD.title = '点击此处选择时';
		WebCalendar.iframe.hourTD.onclick = funHourSelect;
		WebCalendar.iframe.minuteTD.innerText = mi +" 分";
		WebCalendar.iframe.minuteTD.title='点击此处选择分';
		WebCalendar.iframe.minuteTD.onclick = funMinuteSelect;
		WebCalendar.iframe.secondTD.innerText = s +" 秒";
		WebCalendar.iframe.secondTD.title='点击此处选择秒';
		WebCalendar.iframe.secondTD.onclick = funSecondSelect;
	}
	WebCalendar.daysMonth[1] = (0==y%4 && (y%100!=0 || y%400==0)) ? 29 : 28; //闰年二月为29天
    var w = new Date(y, m-1, 1).getDay();	//星期值0-6 -> 周日-周一
    var prevDays = m==1  ? WebCalendar.daysMonth[11] : WebCalendar.daysMonth[m-2];	//当前月上一个月天数
	//这三个 for 循环为日历赋数据源（数组 WebCalendar.day）格式是 d/m/yyyy
	for(var i=(w-1); i>=0; i--)	//补齐上个月日期
    {
        WebCalendar.day[i] = prevDays +"/"+ (parseInt(m, 10)-1) +"/"+ y;
        if(m==1) WebCalendar.day[i] = prevDays +"/"+ 12 +"/"+ (parseInt(y, 10)-1);
        prevDays--;
    }
    for(var i=1; i<=WebCalendar.daysMonth[m-1]; i++) WebCalendar.day[i+w-1] = i +"/"+ m +"/"+ y;	//本月日期
    for(var i=1; i<42-w-WebCalendar.daysMonth[m-1]+1; i++)	//下月日期
    {
        WebCalendar.day[WebCalendar.daysMonth[m-1]+w-1+i] = i +"/"+ (parseInt(m, 10)+1) +"/"+ y;
        if(m==12) WebCalendar.day[WebCalendar.daysMonth[m-1]+w-1+i] = i +"/"+ 1 +"/"+ (parseInt(y, 10)+1);
    }
    for(var i=0; i<42; i++)    //这个循环是根据源数组写到日历里显示
    {
        var a = WebCalendar.day[i].split("/");
        WebCalendar.dayObj[i].innerText    = a[0];
        WebCalendar.dayObj[i].title        = a[2] +"-"+ appendZero(a[1]) +"-"+ appendZero(a[0]);
        WebCalendar.dayObj[i].bgColor      = WebCalendar.dayBgColor;
        WebCalendar.dayObj[i].style.color  = WebCalendar.wordColor;
        if ((i<10 && parseInt(WebCalendar.day[i], 10)>20) || (i>27 && parseInt(WebCalendar.day[i], 10)<12))
            WebCalendar.dayObj[i].style.color = WebCalendar.wordDark;
        if (WebCalendar.inputDate==WebCalendar.day[i])    //设置输入框里的日期在日历上的颜色
        {
			WebCalendar.dayObj[i].bgColor = WebCalendar.inputColor; 
			WebCalendar.dayObj[i].style.color = WebCalendar.wordColor;
			WebCalendar.dayObj[i].style.fontWeight = "bold";
		}
        if (WebCalendar.day[i] == WebCalendar.today)      //设置今天在日历上反映出来的颜色
        {
			WebCalendar.dayObj[i].bgColor = WebCalendar.todayColor; 
			WebCalendar.dayObj[i].style.color = WebCalendar.wordColor;
			WebCalendar.dayObj[i].style.fontWeight = "bold";
		}
    }
}

/*
* 根据日期格式等返回用户选定的日期
* 目前支持:yyyyMMdd,yyyy/MM/dd,yyyy-MM-dd,yyyy年MM月dd日
* yyyyMMddHHmmss,yyyy/MM/dd HH:mm:ss,yyyy-MM-dd HH:mm:ss,yyyy年MM月dd日HH时mm分ss秒
*/
function returnDate() 
{
    if(WebCalendar.objExport)
    {
        var returnValue;
		var a = WebCalendar.day[this.id.substr(8)].split("/");
		
		if(WebCalendar.format == "yyyyMMdd" || WebCalendar.format == "yyyyMMddHHmmss")
		//eos标准格式
		{			
			returnValue = a[2] + appendZero(a[1]) + appendZero(a[0]);
			if(WebCalendar.timeShow == true)
			{
				var h = WebCalendar.thisHour, m = WebCalendar.thisMinute, s = WebCalendar.thisSecond;
				returnValue += appendZero(h) + appendZero(m) + appendZero(s);
			}
		}
		else if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format) || 
			/^(y{4})(-|\/)(M{1,2})(-|\/)(d{1,2}) HH:mm:ss$/.test(WebCalendar.format))
		//"-","/"间隔格式
		{
			var d;
			if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format))
				d = WebCalendar.format.match(/^(\w{4})(-|\/)(\w{1,2})\2(\w{1,2})$/);
			else
				d = WebCalendar.format.match(/^(\w{4})(-|\/)(\w{1,2})\2(\w{1,2}) HH:mm:ss$/);
			if(d==null)
			{
			  alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
			  return false;
			}
			var flag = d[3].length==2 || d[4].length==2; //判断返回的日期格式是否要补零
			returnValue = flag ? a[2] +d[2]+ appendZero(a[1]) +d[2]+ appendZero(a[0]) : a[2] +d[2]+ a[1] +d[2]+ a[0];			
			if(WebCalendar.timeShow == true)
			{
				var h = WebCalendar.thisHour, m = WebCalendar.thisMinute, s = WebCalendar.thisSecond;
				returnValue += flag ? " "+ appendZero(h) +":"+ appendZero(m) +":"+ appendZero(s) : " "+  h  +":"+ m +":"+ s;
			}			
		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(WebCalendar.format) ||
			/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/.test(WebCalendar.format))
		//中文格式
		{
			var d;
			if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(WebCalendar.format))
				d = WebCalendar.format.match(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/);
			else
				d = WebCalendar.format.match(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/);
			if(d==null)
			{
			  alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
			  return false;
			}
			var flag = d[3].length==2 || d[5].length==2; //判断返回的日期格式是否要补零
			returnValue = flag ? a[2] + "年" + appendZero(a[1]) + "月" + appendZero(a[0]) + "日" : a[2] + "年" + a[1] + "月" + a[0] + "日";
			if(WebCalendar.timeShow == true)
			{
				var h = WebCalendar.thisHour, m = WebCalendar.thisMinute, s = WebCalendar.thisSecond;
				returnValue += flag ? appendZero(h) +"时"+ appendZero(m) + "分" + appendZero(s) + "秒" : h +"时"+ m + "分" + s + "秒";
			}
		}
		else
		//默认的返回格式
		{	
			if(/^(y{4})(-)(M{1,2})\2(d{1,2}) HH:mm:ss$/.test(WebCalendar.format))	
			{
				var h = WebCalendar.thisHour, m = WebCalendar.thisMinute, s = WebCalendar.thisSecond;
				returnValue = a[2] + "-" + appendZero(a[1]) + "-" + appendZero(a[0]);
				returnValue += " " + appendZero(h) +":"+ appendZero(m) + ":" + appendZero(s);
			}
			else
				alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
		}
        WebCalendar.objExport.value = returnValue;
        hiddenCalendar();
    }
	else
	{
		alert("你设定的日期输出位置不对！\r\n\r\n请重新定义 WebCalendar.objExport ！");
	}
}

function returnCurDate() //根据日期格式等返回当前日期
{
	var yy = new Date().getFullYear(),mm = new Date().getMonth() +1,dd = new Date().getDate();
	var h = new Date().getHours(), m = new Date().getMinutes(), s = new Date().getSeconds();
    if(WebCalendar.objExport)
    {
        var returnValue;
		if(WebCalendar.format == "yyyyMMdd" || WebCalendar.format == "yyyyMMddHHmmss")
		//eos标准格式
		{
			returnValue = yy + appendZero(mm) + appendZero(dd);
			if(WebCalendar.timeShow == true)
			{
				returnValue += appendZero(h) + appendZero(m) + appendZero(s);
			}
		}
		else if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format) || 
			/^(y{4})(-|\/)(M{1,2})(-|\/)(d{1,2}) HH:mm:ss$/.test(WebCalendar.format))
		//"-","/"间隔格式
		{
			var d;
			if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format))
				d = WebCalendar.format.match(/^(\w{4})(-|\/)(\w{1,2})\2(\w{1,2})$/);
			else
				d = WebCalendar.format.match(/^(\w{4})(-|\/)(\w{1,2})\2(\w{1,2}) HH:mm:ss$/);

			if(d==null)
			{
			  alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
			  return false;
			}
			var flag = d[3].length==2 || d[4].length==2; //判断返回的日期格式是否要补零
			returnValue = flag ? yy +d[2]+ appendZero(mm) +d[2]+ appendZero(dd) : yy +d[2]+ mm +d[2]+ dd;
			if(WebCalendar.timeShow == true)
			{			
				returnValue += flag ? " "+ appendZero(h) +":"+ appendZero(m) +":"+ appendZero(s) : " "+  h  +":"+ m +":"+ s;
			}
		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(WebCalendar.format) ||
			/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/.test(WebCalendar.format))
		//中文格式
		{
			var d;
			if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(WebCalendar.format))
				d = WebCalendar.format.match(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/);
			else
				d = WebCalendar.format.match(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/);
			if(d==null)
			{
			  alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
			  return false;
			}
			var flag = d[3].length==2 || d[5].length==2; //判断返回的日期格式是否要补零
			returnValue = flag ? yy + "年" + appendZero(mm) + "月" + appendZero(dd) + "日" : yy + "年" + mm + "月" + dd + "日";
			if(WebCalendar.timeShow == true)
			{
				var h = WebCalendar.thisHour, m = WebCalendar.thisMinute, s = WebCalendar.thisSecond;
				returnValue += flag ? appendZero(h) +"时"+ appendZero(m) + "分" + appendZero(s) + "秒" : h +"时"+ m + "分" + s + "秒";
			}
		}
		else
		//默认的返回格式
		{	
			if(/^(y{4})(-)(M{1,2})\2(d{1,2}) HH:mm:ss$/.test(WebCalendar.format))	
			{
				var h = WebCalendar.thisHour, m = WebCalendar.thisMinute, s = WebCalendar.thisSecond;
				returnValue = yy + "-" + mm + "-" + ss;
				returnValue += " " + appendZero(h) +":"+ appendZero(m) + ":" + appendZero(s);
			}
			else
				alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
		}
        WebCalendar.objExport.value = returnValue;
        hiddenCalendar();
    }
	else
	{
		alert("你设定的日期输出位置不对！\r\n\r\n请重新定义 WebCalendar.objExport ！");
	}
}
function clsCalendar()
{
 	WebCalendar.objExport.value ='';
    hiddenCalendar();
}
function document.onclick()
{
    if(WebCalendar.eventSrc != window.event.srcElement) 
		hiddenCalendar();
}

/**
　*此函数把yyyMMdd或yyyyMMddHHmmss转成各种支持的格式
　*参数说明：
　*　　　　data:待转换的日期　
　*　　　　format:需要转换成的日期格式　
　*
　*/
function formatSpecial(data,format)
{ 
	

	if(WebCalendar.isOpen != true)
	{	
		if(arguments[1] == undefined || arguments[1] == "undefined")
			WebCalendar.format = "yyyy-MM-dd HH:mm:ss";
		else
			WebCalendar.format = arguments[1];

		if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format))
		{
			WebCalendar.dateReg = /^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
			WebCalendar.timeShow = false;
		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(WebCalendar.format))
		{
			WebCalendar.dateReg = /^(\d{4})年(\d{1,2})月(\d{1,2})日$/;
			WebCalendar.timeShow = false;
		}
		else if(WebCalendar.format=="yyyyMMdd")
		{
			WebCalendar.dateReg = /^(\d{4})(\d{2})(\d{2})$/;
			WebCalendar.timeShow = false;
		}
		else if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2}) (HH:mm:ss)$/.test(WebCalendar.format))
		{
			WebCalendar.dateReg = /^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
			WebCalendar.timeShow = true;
		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/.test(WebCalendar.format))
		{
			WebCalendar.dateReg = /^(\d{4})年(\d{1,2})月(\d{1,2})日(\d{1,2})时(\d{1,2})分(\d{1,2})秒$/;
			WebCalendar.timeShow = true;
		}
		else if(WebCalendar.format=="yyyyMMddHHmmss")
		{	
			WebCalendar.dateReg = /^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/;
			WebCalendar.timeShow = true;
		}
		else
		{
			WebCalendar.format = "yyyy-MM-dd HH:mm:ss";
			WebCalendar.dateReg = /^(\d{4})(-|\/)(\d{2})\2(\d{2}) (\d{2}):(\d{2}):(\d{2})$/;
			WebCalendar.timeShow = true;
		}
        var returnValue;
		//alert(this.id.substr(8));
		//alert(WebCalendar.day[this.id.substr(8)]);
		//var a = WebCalendar.day[this.id.substr(8)].split("/");
		var a=new Array();
		a[2]=data.substr(0,4);
		a[1]=data.substr(4,2);
		a[0]=data.substr(6,2);
        
		
		if(WebCalendar.format == "yyyyMMdd" || WebCalendar.format == "yyyyMMddHHmmss")
		//eos标准格式
		{		
			returnValue = a[2] + appendZero(a[1]) + appendZero(a[0]);
			if(WebCalendar.timeShow == true)
			{
				var h = data.substr(8,2);
				var  m = data.substr(10,2);
				var s = data.substr(12,2);
				returnValue += appendZero(h) + appendZero(m) + appendZero(s);
			}
		}
		else if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format) || 
			/^(y{4})(-|\/)(M{1,2})(-|\/)(d{1,2}) HH:mm:ss$/.test(WebCalendar.format))
		//"-","/"间隔格式
		{
			var d;
			if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(WebCalendar.format))
				d = WebCalendar.format.match(/^(\w{4})(-|\/)(\w{1,2})\2(\w{1,2})$/);
			else
				d = WebCalendar.format.match(/^(\w{4})(-|\/)(\w{1,2})\2(\w{1,2}) HH:mm:ss$/);
			if(d==null)
			{
			 // alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
			  return data;
			}
			var flag = d[3].length==2 || d[4].length==2; //判断返回的日期格式是否要补零
			returnValue = flag ? a[2] +d[2]+ appendZero(a[1]) +d[2]+ appendZero(a[0]) : a[2] +d[2]+ a[1] +d[2]+ a[0];			
			if(WebCalendar.timeShow == true)
			{
				var h = data.substr(8,2);
				var  m = data.substr(10,2);
				var s = data.substr(12,2);
				returnValue += flag ? " "+ appendZero(h) +":"+ appendZero(m) +":"+ appendZero(s) : " "+  h  +":"+ m +":"+ s;
			}
		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(WebCalendar.format) ||
			/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/.test(WebCalendar.format))
		//中文格式
		{
			var d;
			if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(WebCalendar.format))
				d = WebCalendar.format.match(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/);
			else
				d = WebCalendar.format.match(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/);
			if(d==null)
			{
			  //alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
			  return data;
			}
			var flag = d[3].length==2 || d[5].length==2; //判断返回的日期格式是否要补零
			returnValue = flag ? a[2] + "年" + appendZero(a[1]) + "月" + appendZero(a[0]) + "日" : a[2] + "年" + a[1] + "月" + a[0] + "日";
			//returnValue =  a[2] + "年" + a[1] + "月" + a[0] + "日";
			if(WebCalendar.timeShow == true)
			{
				var h = data.substr(8,2);
				var  m = data.substr(10,2);
				var s = data.substr(12,2);
				returnValue += flag ? appendZero(h) +"时"+ appendZero(m) + "分" + appendZero(s) + "秒" : h +"时"+ m + "分" + s + "秒";
			//returnValue +=  h +"时"+ m + "分" + s + "秒";
			
			
			}
		}
		else
		//默认的返回格式
		{	
			if(/^(y{4})(-)(M{1,2})\2(d{1,2}) HH:mm:ss$/.test(WebCalendar.format))	
			{
				var h = data.substr(8,2);
				var  m = data.substr(10,2);
				var s = data.substr(12,2);
				returnValue = a[2] + "-" + appendZero(a[1]) + "-" + appendZero(a[0]);
				returnValue += " " + appendZero(h) +":"+ appendZero(m) + ":" + appendZero(s);
			}
			else
			 return data;	
			//alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！");
		}
		
       return returnValue;
      
    }


	}

/**
　*此函数把各种支持的格式转成yyyMMdd或yyyyMMddHHmmss
　*参数说明：
　*　　　　data:待转换的日期　
　*　　　　format:待转换日期的格式　
　*
　*/

	function formatDefault(data,format)
	{             
		


		if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(format))
		{
			return data.substr(0,4)+data.substr(5,2)+data.substr(8,2);

		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(format))
		{
			return data.substr(0,4)+data.substr(5,2)+data.substr(8,2);
		}
		else if(WebCalendar.format=="yyyyMMdd")
		{
			return data;
			
		}
		else if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2}) (HH:mm:ss)$/.test(format))
		{return data.substr(0,4)+data.substr(5,2)+data.substr(8,2)+data.substr(11,2)+data.substr(14,2)+data.substr(17,2);

			WebCalendar.dateReg = /^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
			WebCalendar.timeShow = true;
		}
		else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/.test(format))
		{
			return data.substr(0,4)+data.substr(5,2)+data.substr(8,2)+data.substr(11,2)+data.substr(14,2)+data.substr(17,2);

			
		}
		else if(format=="yyyyMMddHHmmss")
		{	return data
			
		}
		else
		{
			
            return data.substr(0,4)+data.substr(5,2)+data.substr(8,2)+data.substr(11,2)+data.substr(14,2)+data.substr(17,2);

			
		}

				}
//-->