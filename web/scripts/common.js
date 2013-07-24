
/***
	通用JS函数库:
		Dialog 系列函数：ShowWarnDlg(msg)警告对话框；ShowErrorDlg(msg)错误对话框;ShowInputDlg()输入对话框;ShowMessageDlg(msg)信息对话框;
		Valid  系列函数：IsValidInteger(exp)整数判断；IsValidDate(date,content)检查输入的日期是否符合yyyy-mm-dd的标准；IsValidEmail(exp)信箱地址判断；IsValidFloat(exp)浮点数判断；
		String 系列函数：FormatInteger(exp,S)格式化整数;Format(exp,N)指定小数位数的数字；FormatNum(exp,N,s)指定小数位数的有间隔符的数字；Trim(exp)去掉两端空格的字符串；
		                 CheckLength( Str, MinLen, MaxLen )字符串是否在指定的范围内; IsEmpty( Str )字符串是否为空;IsLetter( c )指定的字符是否是字母;
			         CheckLetter( Str )检查指定的字符串是否是字母组成;CheckString( SrcStr, DestStr )检查源字符串里是否包含目标字符串里的字符;
				 CDate(dateyyyymmdd,content) 转换日期为指定格式；
***/


/*
        函数原形：
                ChangeChar(Str);

        说明：
                转换特殊字符

        参数：
                Str 要转换的字符串

        返回值：
                经过转换的字符串
*/
function ChangeChar(Str)
{
  var ReValue;
  ReValue=Str;
  if(Str!=null)
  {
	  for(var i=0;i<Str.length;i++)
	  {
	  	ReValue=ReValue.replace(String.fromCharCode(13),"\\n");
	  	ReValue=ReValue.replace(String.fromCharCode(0),"");
		ReValue=ReValue.replace('%','');
		ReValue=ReValue.replace('#','');
		ReValue=ReValue.replace('&','');
	        ReValue=ReValue.replace('"','“');
	        ReValue=ReValue.replace('"','”');
	        ReValue=ReValue.replace("'","‘");
	        ReValue=ReValue.replace("'","’");
	  }
  }
  return ReValue;
}
/**
	Dialog 系列函数 开始
**/
/*
        函数原形：
                showWarnDlg(msg);

        说明：
                输出警告页面

        参数：
                msg 警告信息字符串

        返回值：
                true 点击是
                false 点击否
*/
function showWarnDlg(msg)
{
  ans = window.showModalDialog("../public/dlgWarn.htm?param="+msg,"null","center:yes;dialogWidth:14.75;dialogHeight:10.3em;help:no;status:no");

  if ((ans != null) && (ans == "Yes"))
    return true
  else
    return false
}
/*
        函数原形：
                ShowErrorDlg(msg);

        说明：
                输出错误页面

        参数：
                msg 错误信息字符串

        返回值：无

*/
function showErrorDlg(msg)
{
   window.showModalDialog("../public/dlgError.jsp?param="+msg,"null","center:yes;dialogWidth:14.75;dialogHeight:10.3em;help:no;status:no");
}
/*
        函数原形：
                ShowInputDlg(msg,Value);

        说明：
                输出输入页面

        参数：
                msg 显示输入提示信息、Value要修改的信息

        返回值：
		点击是:ans 输入信息
                点击否:无

*/
function showInputDlg(msg,value)
{
  if(msg==null) msg="请输入：";
  if(value==null) value="";
  ans = window.showModalDialog("../public/dlgInput.jsp?param="+msg+"&value="+value,"null","center:yes;dialogWidth:14.75;dialogHeight:10.3em;help:no;status:no");

  if (ans != null)
    return ans
}
/*
        函数原形：
                ShowMessageDlg(msg);

        说明：
                输出显示信息页面

        参数：
                msg 显示信息字符串

        返回值：无

*/
function showMessageDlg(msg)
{
  window.showModalDialog("../public/dlgMessage.jsp?param="+msg,"null","center:yes;dialogWidth:14.75;dialogHeight:10.3em;help:no;status:no");
}

/**
	Dialog 系列函数 结束
**/

/**
	Valid 系列函数 开始
**/
/*
        函数原形：
                IsValidInteger(exp);

        说明：
                判断是否是整数

        参数：
                exp 要判断的值

        返回值：
		ture 为真，是
                false 为假，否

*/
// 根据是否有 “.”，来判断是否是整数。
function IsValidInteger(exp)
{
        for (i=0; i<exp.length; i++)
        {
                n = exp.substr(i, 1)
                if (!(IsNumber(n)))
                {
                        return false;
                }
        }

        return true;
}


/*
        函数原形：
                IsValidDate(date,content);

        说明：
                判断是否是日期

        参数：
                date 日期格式
		content 内容

        返回值：
		true 为真，是
                false 为假，否

*/
function IsValidDate(date,content)
{
  Digitals = "01234567879";
  for (var CharIndex=0; CharIndex<date.length; CharIndex++) {
    IndexChar = date.substr(CharIndex,1);
    if ((IndexChar != '-') && (Digitals.indexOf(IndexChar) == -1)) {
      MyAlert(content + "中有非法字符！例:2000-01-01");
      return false;
      break;
    }
  }

  var yearvalue,monthvalue,dayvalue;
  Today = new Date();
  Today.getDate();

  if (date.indexOf("-")!=4) {
    MyAlert(content + "的年份不正确！例:2000-01-01");
    return false;
  }
  yearvalue=date.substr(0,date.indexOf("-"));
  //判断不能超过1900<year<2100
  if (yearvalue < Today.getYear() -100 || yearvalue > Today.getYear()+100) {
    MyAlert(content + "的年份不正确！例:2000-01-01");
    return false;
  }

  date = date.substr(date.indexOf("-")+1,100);
  if ((date.indexOf("-")!=1) && (date.indexOf("-")!=2)) {
    MyAlert(content + "的月份不正确！例:2000-01-01");
    return false;
  }
  monthvalue=date.substr(0,date.indexOf("-"));
  if ((monthvalue == 0) || (monthvalue > 12)) {
    MyAlert(content + "的月份不正确！例:2000-01-01");
    return false;
  }

  dayvalue = date.substr(date.indexOf("-")+1,100);

  var Leap;
  Leap = false;
  Months= new Array(31,28,31,30,31,30,31,31,30,31,30,31);
  if ((yearvalue % 4 == 0) && ((yearvalue % 100 != 0) || (yearvalue % 400 == 0))) Leap = true;

  if ((dayvalue == 0) || (dayvalue > 31)) {
    MyAlert(content + "的日数不对！");
    return false;
  }
  if ((dayvalue > Months[monthvalue-1]) && !((monthvalue == 2) && (dayvalue > 28))) {
    MyAlert(content + "的日数不对！");
    return false;
  }
  if (!(Leap) && (monthvalue == 2) && (dayvalue > 28)) {
    MyAlert(content + "的日数不对！");
    return false;
  }
  if ((Leap) && (monthvalue == 2) && (dayvalue > 29)) {
    MyAlert(content + "的日数不对！");
    return false;
  }
  return true;
}
/*
        函数原形：
                IsValidEmail(exp);

        说明：
                检查Email地址的格式是否符合标准

        参数：
                Str 待校验的Email地址字符串

        返回值：
                true 符合标准
                false 不符合标准
*/

function IsValidEmail(exp)
{
        var myReg = /^[_a-z0-9]+@([_a-z0-9]+\.)+[a-z0-9]{2,3}$/;

        if(myReg.test(exp) || Trim(exp)=="")
                return true;

        return false;

}
/*
        函数原形：
                IsValidFloat(exp);

        说明：
                检查是否是浮点数

        参数：
                exp 待检查的数

        返回值：
                true 符合标准
                false 不符合标准
*/

function IsValidFloat(exp)
{
        var myReg=/^[0-9]+(\.)+[0-9]+$/;
	var myRegs=/^[0-9]+$/;
        if(myReg.test(exp) || myRegs.test(exp))
                return true;

        return false;

}
/**
	Valid 系列函数 结束
**/

/**
	String 系列函数 开始
**/
/*
        函数原形：
                FormatInteger(exp,s);

        说明：
                格式整数

        参数：
                exp 待格式化的数字
		s 间隔符号
        返回值：
                Str 格式化的整数
*/

function FormatInteger(exps,s)
{
	var exp=exps.value;
	if(IsValidInteger(exp)){
		var Str,StrB;
		exps.title=exp;
		StrB=exp.length;
		var k=parseInt(StrB/3);
		if(StrB==3*k) k=k-1;
		for(var i=1;i<=k;i++){
	  		exp=exp.substr(0,StrB-3*i)+s+exp.substr(StrB-3*i,StrB);
		}
		Str=exp;
		return Str;
	}
	exps.value=exps.title;
}
/*
        函数原形：
                FormatNum(exp,N,s);

        说明：
                指定小数位数的数字

        参数：
                exp 待格式化的数字
		N 小数位数
		s 间隔符号
        返回值：
                Str 格式化的数字
*/

function FormatNum(exps,N,s)
{
	var exp=exps.value;
	if(IsValidFloat(exp)){
		var Str,StrB;
		exps.title=exp;
		StrB=exp.indexOf(".");
		if(StrB>0) {
	  	for(var i=0;i<N;i++) exp=exp+"0";
	    		exp=exp.substr(0,StrB+N+1);
		}else{
	  	exp=exp+".";
	  	for(var i=0;i<N;i++) exp=exp+"0";
		}
		var k=parseInt(StrB/3);
		if(StrB==3*k) k=k-1;
		for(var i=1;i<=k;i++){
          		var L=exp.length;
	  		exp=exp.substr(0,StrB-3*i)+s+exp.substr(StrB-3*i,L);
		}
		Str=exp;
		return Str;
	}
	exps.value=exps.title;
}
/*
        函数原形：
                Format(exp,N);

        说明：
                指定小数位数的数字

        参数：
                exp 待格式化的数字
		N 小数位数
        返回值：
                Str 格式化的数字
*/

function Format(exp,N)
{
	var Str,StrB;
	StrB=exp.indexOf(".");
	if(StrB>0) {
	for(var i=0;i<N;i++) exp=exp+"0";
	exp=exp.substr(0,StrB+N+1);
	}else{
	exp=exp+".";
	for(var i=0;i<N;i++) exp=exp+"0";
	}
	Str=exp;
	return Str;
}
/*
        函数原形：
                Trim(exp);

        说明：
                去掉字符串两端的空格

        参数：
                exp 待格式化的字符串

        返回值：
                Str 去掉两端空格的字符串
*/

function Trim(exp)
{
	var Str,StrB,StrE;
	do{
	  StrB=exp.indexOf(" ");
	  if(StrB==0) exp=exp.substr(1,exp.length);
	}while(StrB==0)
	//do{
	  StrE=exp.lastIndexOf(" ");
	  if(StrE==(exp.length-1)) exp=exp.substr(0,exp.length-1);
	  //StrE=exp.lastIndexOf(" ")+1;
	//}while(StrE==exp.length)
	Str=exp;
	return Str;
}

/**
	String 系列函数 结束
**/
  //-------------------
// common.js
// 常用表单校验函数
//-------------------

//-------------------------------------------------------------------------

/*
        函数原形：
                CheckLength( Str, MinLen, MaxLen );

        说明：
                字符串是否在指定的范围内

        参数：
                Str 待校验字符串
                MinLen 指定最小长度
                MaxLen 指定最大长度

        返回值：
                ture 在指定范围内
                false 不在指定范围内
*/

function CheckLength( Str, MinLen, MaxLen )
{
        if ( (Str.length >= MinLen) && (Str.length <= MaxLen) )
                return true;
        else
                return false;
}

//-------------------------------------------------------------------------

/*
        函数原形：
                IsEmpty( Str );

        说明：
                字符串是否为空

        参数：
                Str 待校验的字符串

        返回值：
                true 字符串为空
                false 字符串不为空
*/

function IsEmpty( Str,name )
{
        if ( Str=="" )
        {
          MyAlert(name+"不能为空！");
          return true;
        }
        else
        {
          return false;
        }
}
//-------------------------------------------------------------------------

/*
        函数原形：
                IsLetter( c );

        说明：
                指定的字符是否是字母

        参数：
                c 待检验的字符

        返回值：
                true 是字母
                false 不是字母
*/

function IsLetter( c )
{
        if (((c>='a') && (c<='z')) || ((c>='A') && (c<='Z')))
                return true
        else
                return false
}

//-------------------------------------------------------------------------

/*
        函数原形：
                CheckLetter( Str );

        说明：
                检查指定的字符串是否是字母组成

        参数：
                Str 待校验的字符串

        返回值：
                true 是字母组成
                false 不是字母组成
*/

function CheckLetter( Str )
{
        for (i=0; i<Str.length; i++)
        {
                c = Str.substr(i, 1)
                if (!(IsLetter(c)))
                {
                        return false;
                }
        }
        return true;
}

//-------------------------------------------------------------------------

/*
        函数原形：
                IsNumber( c );

        说明：
                检查指定的字符是否是数字

        参数：
                c 待校验的字符

        返回值：
                true 是数字
                false 不是数字
*/

function IsNumber( c )
{
        if ((c>='0') && (c<='9'))
                return true;
        else
                return false;
}

//-------------------------------------------------------------------------

/*
        函数原形：
                CheckNumber( Str );

        说明：
                检查指定的字符串是否是数字

        参数：
                Str 待校验的字符串

        返回值：
                true 是数字
                false 不是数字
*/

function CheckNumber( Str )
{
        for (i=0; i<Str.length; i++)
        {
                n = Str.substr(i, 1)
                if (!(IsNumber(n)))
                {
                        return false;
                }
        }
        return true;
}

//-------------------------------------------------------------------------

/*
        函数原形：
                CheckString( SrcStr, DestStr );

        说明：
                检查源字符串里是否包含目标字符串里的字符

        参数：
                SrcStr 源字符串
                DestStr 目标字符串

        返回值：
                true 包含
                false 不包含
*/

function CheckString( SrcStr, DestStr )
{
        for ( i = 0; i < SrcStr.length; i++ )
        {
                for ( j = 0; j < DestStr.length; j++ )
                {
                        if ( SrcStr.substr( i, 1 ) == DestStr.substr( j, 1 ) )
                                return true;
                }
        }

        return false;
}

//-------------------------------------------------------------------------

/*
        函数原形：
                CheckEmail( Str );

        说明：
                检查Email地址的格式是否符合标准

        参数：
                Str 待校验的Email地址字符串

        返回值：
                true 符合标准
                false 不符合标准
*/

function CheckEmail( Str )
{
        var myReg = /^[_a-z0-9]+@([_a-z0-9]+\.)+[a-z0-9]{2,3}$/;

        if(myReg.test(Str))
                return true;

        return false;

}

/*
        函数原形：
                checkdateyyyymmdd(dateyyyymmdd,content);

        说明：
                检查输入的日期是否符合yyyy-mm-dd的标准

        参数：
                dateyyyymmdd 待校验的日期字符串
                content 名称

        返回值：

*/

function CheckDate(date,content)
{
  Digitals = "01234567879";
  for (var CharIndex=0; CharIndex<date.length; CharIndex++) {
    IndexChar = date.substr(CharIndex,1);
    if ((IndexChar != '-') && (Digitals.indexOf(IndexChar) == -1)) {
      MyAlert(content + "中有非法字符！例:2000-01-01");
      return false;
      break;
    }
  }

  var yearvalue,monthvalue,dayvalue;
  Today = new Date();
  Today.getDate();

  if (date.indexOf("-")!=4) {
    MyAlert(content + "的年份不正确！例:2000-01-01");
    return false;
  }
  yearvalue=date.substr(0,date.indexOf("-"));
  if (yearvalue < Today.getYear() -100 || yearvalue > Today.getYear()+100) {
    MyAlert(content + "的年份不正确！例:2000-01-01");
    return false;
  }

  date = date.substr(date.indexOf("-")+1,100);
  if ((date.indexOf("-")!=1) && (date.indexOf("-")!=2)) {
    MyAlert(content + "的月份不正确！例:2000-01-01");
    return false;
  }
  monthvalue=date.substr(0,date.indexOf("-"));
  if ((monthvalue == 0) || (monthvalue > 12)) {
    MyAlert(content + "的月份不正确！例:2000-01-01");
    return false;
  }

  dayvalue = date.substr(date.indexOf("-")+1,100);

  var Leap;
  Leap = false;
  Months= new Array(31,28,31,30,31,30,31,31,30,31,30,31);
  if ((yearvalue % 4 == 0) && ((yearvalue % 100 != 0) || (yearvalue % 400 == 0))) Leap = true;

  if ((dayvalue == 0) || (dayvalue > 31)) {
    MyAlert(content + "的日数不对！");
    return false;
  }
  if ((dayvalue > Months[monthvalue-1]) && !((monthvalue == 2) && (dayvalue > 28))) {
    MyAlert(content + "的日数不对！");
    return false;
  }
  if (!(Leap) && (monthvalue == 2) && (dayvalue > 28)) {
    MyAlert(content + "的日数不对！");
    return false;
  }
  if ((Leap) && (monthvalue == 2) && (dayvalue > 29)) {
    MyAlert(content + "的日数不对！");
    return false;
  }
  return true;
}

function CDate(dateyyyymmdd,content)
{
  if (dateyyyymmdd.length != 10){
    MyAlert(content + "格式不正确！例:2000-01-01");
    return false;}
  var IndexChar;
  var Digitals;
  Digitals = "01234567879";
  for (var CharIndex=0; CharIndex<10; CharIndex++){
    IndexChar = dateyyyymmdd.substr(CharIndex,1);
    if (IndexChar == "-"){
      if ((CharIndex != 4) && (CharIndex != 7)){
        MyAlert(content + "的格式不正确！");
        return false;}
    }
    else{
      if ((CharIndex == 4) || (CharIndex == 7)){
        MyAlert(content + "的格式不正确！");
        return false;}
      else{
        if (Digitals.indexOf(IndexChar) == -1){
          MyAlert(content + "的格式不正确！");
          return false;}
      }
    }
  }
  var yearvalue;
  var monthvalue;
  var dayvalue;
  yearvalue = dateyyyymmdd.substr(0, 4);
  monthvalue = dateyyyymmdd.substr(5, 2);
  dayvalue = dateyyyymmdd.substr(8, 2);
  Today = new Date();
  Today.getDate();
  if (yearvalue < Today.getYear() -150 || yearvalue > Today.getYear()){
    MyAlert(content + "的年份不对！");
    return false;}
  if ((monthvalue == 0) || (monthvalue > 12)){
    MyAlert(content + "的月份不对！");
    return false;}
  var Leap;
  Leap = false;
  Months= new Array(31,28,31,30,31,30,31,31,30,31,30,31);
  if ((yearvalue % 4 == 0) && ((yearvalue % 100 != 0) || (yearvalue % 400 == 0)))
    Leap = true;
  if ((dayvalue == 0) || (dayvalue > 31)){
    MyAlert(content + "的日数不对！");
    return false;}
  if ((dayvalue > Months[monthvalue-1]) && !((monthvalue == 2) && (dayvalue > 28))){
    MyAlert(content + "的日数不对！");
    return false;}
  if (!(Leap) && (monthvalue == 2) && (dayvalue > 28)){
    MyAlert(content + "的日数不对！");
    return false;}
  if ((Leap) && (monthvalue == 2) && (dayvalue > 29)){
    MyAlert(content + "的日数不对！");
    return false;}
  return true;
}

function MyAlert(msg)
{
  window.showModalDialog("DlgError.jsp?Param="+msg,"null","center:yes;dialogWidth:16.75;dialogHeight:10em;help:no;status:no");
}

//johney

var uptimer,downtimer
function ups(fNM,mNM)
{
	var f=fNM;
	var m=mNM;
	clearTimeout(uptimer);
	eval(f+"."+m+"."+"doScroll('up');");
	eval("uptimer=setTimeout(\"ups('"+f+"','"+m+"')\",50);");
}

function downs(fNM,mNM)
{
	var f=fNM;
	var m=mNM;
	clearTimeout(downtimer);
	eval(f+"."+m+"."+"doScroll('down');");
	eval("uptimer=setTimeout(\"downs('"+f+"','"+m+"')\",50);");
}

function stop()
{
	clearTimeout(uptimer);
	clearTimeout(downtimer);
}

function radio_onMouseOver(img_id,radio_id,over_c,over_unc) {
	if(radio_id.checked == true){
		img_id.src = over_c;
	} else {
		img_id.src = over_unc;
	}
}

function radio_onMouseOut(img_id,radio_id,out_c,out_unc) {
	if(radio_id.checked == true){
		img_id.src = out_c;
	} else {
		img_id.src = out_unc;
	}
}

function radio_onMouseUp(radio_name,img_id,radio_id,up_other,up_self,up_disabled) {
	radio_id.click();

	var img_id;
	var r_id;

	images = document.images(radio_name);

	for(i=0;i<images.length;i++) {
		image=images.item(i);

		if(image.id && image.id != ""){
			img_id=image.id;
			r_id=img_id.substring(4,img_id.length);
		}

                if(document.getElementById(r_id).disabled == true)
                  image.src=up_disabled;
                else
		  image.src=up_other;
	}
	img_id.src=up_self;
}

function checkBox_onMouseOver(img_id,checkBox_id,over_c,over_unc) {
	if(checkBox_id.checked == true){
		img_id.src = over_c;
	} else {
		img_id.src = over_unc;
	}
}

function checkBox_onMouseOut(img_id,checkBox_id,out_c,out_unc) {
	if(checkBox_id.checked == true){
		img_id.src = out_c;
	} else {
		img_id.src = out_unc;
	}
}

function checkBox_onMouseUp(img_id,checkBox_id,c_up,unc_up) {
	checkBox_id.click();
	if(checkBox_id.checked == true) {
		img_id.src=c_up;
	} else {
		img_id.src=unc_up;
	}
}

function bDown(fNM,selfLayer,cNM,isInFrame)
{
	var str="";
	str+=fNM+"."+cNM+"_t.blur()";
	str+="\n"+"if("+fNM+"."+cNM+"_b.value == 6){";
		str+="\n"+fNM+"."+cNM+"_b.value = 5;\n";
		if(isInFrame=="true")
			str+="parent.";
		str+=cNM+"_mL.style.top = ";
		if(isInFrame=="true"){
			//str+="parent."+cNM+"_tL.offsetTop+";
			str+="parent."+selfLayer+".offsetTop+";
			str+="getAbsY("+fNM+"."+cNM+"_t)+19;\n";
		}else{
			str+="getAbsY("+fNM+"."+cNM+"_t)+19;\n";
		}
		if(isInFrame=="true")
			str+="\n"+"parent.";
		str+=cNM+"_mL.style.left= ";
		if(isInFrame=="true"){
			//str+="parent."+cNM+"_tL.offsetLeft+";
			str+="parent."+selfLayer+".offsetLeft+";
			str+="getAbsX("+fNM+"."+cNM+"_t);\n";
		}else{
			str+="getAbsX("+fNM+"."+cNM+"_t);\n";
		}
		str+=fNM+"."+cNM+"_t.select();\n";
		if(isInFrame=="true")
			str+="parent.";
		str+=cNM+"_mL.style.visibility='visible';\n}";
	str+="\n"+"else{";
		str+=fNM+"."+cNM+"_b.value = 6;\n";
		if(isInFrame=="true")
			str+="parent.";
		str+=cNM+"_mL.style.visibility='hidden';\n";
		str+=fNM+"."+cNM+"_t.blur();\n}";
	eval(str);
}

function menuH(fNM,cNM,isInFrame)
{
	var str="";
	str+="if ("+fNM+"."+cNM+"_b.value == 5){";
		if(isInFrame=="true")
			str+="\n"+"parent.";
		str+=cNM+"_mL.style.visibility='hidden';";
		str+="\n"+fNM+"."+cNM+"_b.value = 6;}";
	eval(str);
}

function setBlur(fNM,cNM)
{
	var str="";
	str+="if ("+fNM+"."+cNM+"_b.value == 6) {";
		str+="\n"+fNM+"."+cNM+"_t.blur();";
	str+="}";
	eval(str);
}

function getAbsX(obj, offsetObj){
	var _offsetObj=(offsetObj)?offsetObj:document.body;
	var x=obj.offsetLeft;
	var tmpObj=obj.offsetParent;

	while ((tmpObj!=_offsetObj) && tmpObj){
		x+=tmpObj.offsetLeft+tmpObj.clientLeft-tmpObj.scrollLeft;
		tmpObj=tmpObj.offsetParent;

	}
	return x;
}

function getAbsY(obj, offsetObj){
	var _offsetObj=(offsetObj)?offsetObj:document.body;
	var y=obj.offsetTop;
	var tmpObj=obj.offsetParent;

	while ((tmpObj!=_offsetObj) && tmpObj){
		y+=tmpObj.offsetTop+tmpObj.clientTop-tmpObj.scrollTop;
		tmpObj=tmpObj.offsetParent;
	}
	return y;
}

function addSpin(fNM,bID){
      var str =  "";
      str += bID+"_Layer.style.left=parseInt((getAbsX("+fNM+"."+bID+")) + "+fNM+"."+bID+".clientWidth - 14);";
      str += bID+"_Layer.style.top=parseInt(getAbsY("+fNM+"."+bID+"));";
      str += bID+"_Layer.style.visibility='visible';";
      eval(str);
}

function remSpin(fNM,bID){
	eval(bID+"_Layer.style.visibility='hidden';");
}
