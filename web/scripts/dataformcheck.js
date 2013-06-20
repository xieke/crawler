/******************************************************/
/* 文件名:DataFormCheck.js                            */
/* 功  能:基于自定义属性的Javascript校验函数库			  */
/* 作  者:陈春										  */
/* 日  期:2005-5-11 10:46							  */
/******************************************************/

/**
* 目前所支持的校验类型有：
+-----------------------+---------------------------------------+-------------------------------------------------------+
|		类	型			|		校验函数							|		描	述											|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|number					|f_check_number(obj)					|数字													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|naturalnumber			|f_check_naturalnumber(obj)				|自然数													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|integer				|f_check_integer(obj)					|整数													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|float					|f_check_float(obj)						|实数													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|zh						|f_check_zh(obj)						|汉字													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|lowercase				|f_check_lowercase(obj)					|小写英文字母												|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|uppercase				|f_check_uppercase(obj)					|大写英文字母												|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|letter					|f_check_letter(obj)					|英文字母													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|ZhOrNumOrLett			|f_check_ZhOrNumOrLett(obj)				|汉字、字母、数字组成的字符串								|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|IP						|f_check_IP(obj)						|计算机的IP地址											|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|port					|f_check_port(obj)						|计算机的IP地址的端口号									|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|URL					|f_check_URL(obj)						|网页地址													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|email					|f_check_email(obj)						|电子邮件地址												|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|mobile					|f_check_mobile(obj)					|移动电话号码												|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|phone					|f_check_phone(obj)						|电话号码													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|zipcode				|f_check_zipcode(obj)					|邮政编码													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|userID					|f_check_userID(obj)					|用户ID，可以为数字、字母、下划线的组合，第一个字符不能为数字,	|
|						|										|且总长度不能超过20。										|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|IDno					|f_check_IDno(obj)						|身份证													|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|formatStr(reg)			|f_check_formatStr(obj)					|符合某正则表达式的字符串。指定的字符串中不要包含"//",也不需要引号.	|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|date(yyyyMMdd)			|f_check_date(obj)						|指定格式的日期数据，目前支持yyyy年MM月dd日,yyyy-MM-dd,		|
|						|										|yyyy/MM/dd,yyyyMMdd									|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|time(yyyyMMddHHmmss)	|f_check_time(obj)						|指定格式的时间数据，目前支持yyyy年MM月dd日HH时mm分ss秒，		|
|						|										|yyyy-MM-dd HH:mm:ss,yyyy/MM/dd HH:mm:ss，yyyyMMddHHmmss	|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|double(len,prc)		|f_check_double(obj)					|带长度和精度的数字										|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|interval(min,max)		|f_check_interval(obj)					|在某区间内的数字，"-"代表负无穷，"+"代表正无穷。				|
+-----------------------+---------------------------------------+-------------------------------------------------------+
|						|checkIntervalObjs(obj1 , obj2)			|校验2个表单域中的值是否满足obj1小于obj2						|
+-----------------------+---------------------------------------+-------------------------------------------------------+

*/

/**
* 取得字符串的字节长度 
*/
function strlen(str)
{
	var i;
	var len;
	
	len = 0;
	for (i=0;i<str.length;i++)
	{
		if (str.charCodeAt(i)>255) len+=2; else len++;
	}
	return len;
}

/*
* 判断是否为数字，是则返回true,否则返回false
*/
function f_check_number(obj)
{   	
	if (/^\d+$/.test(obj.value))
	{
	   return true;
	} 
	else 
	{
	   f_alert(obj,"请输入数字");
	   return false;
	}
}

/*
* 判断是否为自然数，是则返回true,否则返回false
*/
function f_check_naturalnumber(obj)
{   	
	var s = obj.value;
	if (/^[0-9]+$/.test( s ) && (s > 0))
	{
	   return true;
	} 
	else 
	{
		f_alert(obj,"请输入自然数");
	    return false;
	}
}

/*
* 判断是否为整数，是则返回true,否则返回false
*/
function f_check_integer(obj)
{   	
	if (/^(\+|-)?\d+$/.test( obj.value )) 
	{
	   return true;
	} 
	else 
	{
		f_alert(obj,"请输入整数");
	    return false;
	}
}

/*
* 判断是否为实数，是则返回true,否则返回false
*/
function f_check_float(obj)
{   	
	if (/^(\+|-)?\d+($|\.\d+$)/.test( obj.value )) 
	{
	   return true;
	} 
	else 
	{
		f_alert(obj,"请输入实数");
	   return false;
	}
}

/*
* 校验数字的长度和精度
*/
function f_check_double(obj){
	alert("com on");
	var numReg;
	var value = obj.value;
	if(Trim(value).length != 0){
		if(!f_check_float(obj)) return;
	}	
	var strValueTemp, strInt, strDec;	
	var dtype = obj.eos_datatype;
	var pos_dtype = dtype.substring(dtype.indexOf("(")+1,dtype.indexOf(")")).split(",");
	var len = pos_dtype[0], prec = pos_dtype[1];
	try
	{		
		numReg =/[\-]/;
		strValueTemp = value.replace(numReg, "");
		numReg =/[\+]/;
		strValueTemp = strValueTemp.replace(numReg, "");
		//整数
		if(prec==0){
			numReg =/[\.]/;
			if(numReg.test(value) == true){
				f_alert(obj, "输入必须为整数类型");
				return false;	
			}			
		}		
		if(strValueTemp.indexOf(".") < 0 ){
			if(strValueTemp.length >( len - prec)){
				f_alert(obj, "整数位不能超过"+ (len - prec) +"位");
				return false;
			}		
		}else{
			strInt = strValueTemp.substr( 0, strValueTemp.indexOf(".") );		
			if(strInt.length >( len - prec)){
				f_alert(obj, "整数位不能超过"+ (len - prec) +"位");
				return false;
			}
			strDec = strValueTemp.substr( (strValueTemp.indexOf(".")+1), strValueTemp.length );	
			if(strDec.length > prec){
				f_alert(obj, "小数位不能超过"+  prec +"位");
				return false;
			}		
		}		
		return true;
	}catch(e){
		alert("in f_check_double = " + e);
		return false;
	}	
}

/*
* 校验数字的最小最大值
* 返回bool
*/
function f_check_interval(obj)
{
	var value = parseFloat(obj.value);

	var dtype = obj.eos_datatype;
	var pos_dtype = dtype.substring(dtype.indexOf("(")+1,dtype.indexOf(")")).split(",");
	
	var minLimit = pos_dtype[0];
	var maxLimit = pos_dtype[1];
	var minVal = parseFloat(pos_dtype[0]);
	var maxVal = parseFloat(pos_dtype[1]); 
	
	if(isNaN(value))
	{
		f_alert(obj, "值必须为数字");
		return false;
	}
	if((isNaN(minVal) && (minLimit != "-")) || (isNaN(maxVal) && (maxLimit != "+")))
	{
		f_alert(obj, "边界值必须为数字或-、+");
		return false;
	}

	if(minLimit == "-" && !isNaN(maxVal))
	{
		if(value > maxVal)
		{
			f_alert(obj, "值不能超过" + maxVal);
			return false;
		}
	}
	
	if(!isNaN(minVal) && maxLimit == "+")
	{		
		if(value < minVal)
		{
			f_alert(obj, "值不能小于" + minVal);
			return false;
		}
	}
	
	if(!isNaN(minVal) && !isNaN(maxVal))
	{
		if(minVal > maxVal)
		{
			f_alert(obj, "起始值" + minVal + "不能大于终止值" + maxVal);
		}else
		{
			if(!(value <= maxVal && value >= minVal))
			{
				f_alert(obj, "值应该在" + minVal + "和" + maxVal + "之间");
				return false;
			}
		}
	}
	return true;
}

/*
用途：检查输入字符串是否只由汉字组成
如果通过验证返回true,否则返回false	
*/
function f_check_zh(obj){
	if (/^[\u4e00-\u9fa5]+$/.test(obj.value)) {
	  return true;
	}
	f_alert(obj,"请输入汉字");
	return false;
}

/*
* 判断是否为小写英文字母，是则返回true,否则返回false
*/
function f_check_lowercase(obj)
{   	
	if (/^[a-z]+$/.test( obj.value )) 
	{
	   return true;
	} 
	f_alert(obj,"请输入小写英文字母");
    return false;
}

/*
* 判断是否为大写英文字母，是则返回true,否则返回false
*/
function f_check_uppercase(obj)
{   	
	if (/^[A-Z]+$/.test( obj.value )) 
	{
	   return true;
	} 
	f_alert(obj,"请输入大写英文字母");
	return false;
}

/*
* 判断是否为英文字母，是则返回true,否则返回false
*/
function f_check_letter(obj)
{   	
	if (/^[A-Za-z]+$/.test( obj.value )) 
	{
	   return true;
	} 
	f_alert(obj,"请输入英文字母");
	return false;
}

/*
用途：检查输入字符串是否只由汉字、字母、数字组成
输入：
	value：字符串
返回：
	如果通过验证返回true,否则返回false	
*/
function f_check_ZhOrNumOrLett(obj){    //判断是否是汉字、字母、数字组成
	var regu = "^[0-9a-zA-Z\u4e00-\u9fa5]+$";   
	var re = new RegExp(regu);
	if (re.test( obj.value )) {
	  return true;
	}
	f_alert(obj,"请输入汉字、字母或数字");
	return false;
}

/*
用途：校验ip地址的格式
输入：strIP：ip地址
返回：如果通过验证返回true,否则返回false；	
*/
function f_check_IP(obj) 
{ 
    var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/; //匹配IP地址的正则表达式
	if(re.test( obj.value ))
	{
		if( RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256) return true;
	}
	f_alert(obj,"请输入合法的计算机IP地址");
	return false; 
}

/*
用途：检查输入对象的值是否符合端口号格式
输入：str 输入的字符串
返回：如果通过验证返回true,否则返回false	
*/
function f_check_port(obj)
{
	if(!f_check_number(obj))
		return false;
	if(obj.value < 65536)
		return true;
	f_alert(obj,"请输入合法的计算机IP地址端口号");
	return false; 
}

/*
用途：检查输入对象的值是否符合网址格式
输入：str 输入的字符串
返回：如果通过验证返回true,否则返回false	
*/
function f_check_URL(obj){  
	var myReg = /^((http:[/][/])?\w+([.]\w+|[/]\w*)*)?$/; 
	if(myReg.test( obj.value )) return true; 
	f_alert(obj,"请输入合法的网页地址");
	return false; 
}

/*
用途：检查输入对象的值是否符合E-Mail格式
输入：str 输入的字符串
返回：如果通过验证返回true,否则返回false	
*/
function f_check_email(obj){  
	var myReg = /^([-_A-Za-z0-9\.]+)@([_A-Za-z0-9]+\.)+[A-Za-z0-9]{2,3}$/; 
	if(myReg.test( obj.value )) return true; 
	f_alert(obj,"请输入合法的电子邮件地址");
	return false; 
}

/*
要求：一、移动电话号码为11或12位，如果为12位,那么第一位为0
	 二、11位移动电话号码的第一位和第二位为"13"
	 三、12位移动电话号码的第二位和第三位为"13"
用途：检查输入手机号码是否正确
输入：
	s：字符串
返回：
	如果通过验证返回true,否则返回false	
*/
function f_check_mobile(obj){   
	var regu =/(^[1][3][0-9]{9}$)|(^0[1][3][0-9]{9}$)/;
	var re = new RegExp(regu);
	if (re.test( obj.value )) {
	  return true;
	}
	f_alert(obj,"请输入正确的手机号码");
	return false;	
}

/*
要求：一、电话号码由数字、"("、")"和"-"构成
	 二、电话号码为3到8位
	 三、如果电话号码中包含有区号，那么区号为三位或四位
	 四、区号用"("、")"或"-"和其他部分隔开
用途：检查输入的电话号码格式是否正确
输入：
	strPhone：字符串
返回：
	如果通过验证返回true,否则返回false	
*/
function f_check_phone(obj) 
{
	var regu =/(^([0][1-9]{2,3}[-])?\d{3,8}(-\d{1,6})?$)|(^\([0][1-9]{2,3}\)\d{3,8}(\(\d{1,6}\))?$)|(^\d{3,8}$)/; 
	var re = new RegExp(regu);
	if (re.test( obj.value )) {
	  return true;
	}
	f_alert(obj,"请输入正确的电话号码");
	return false;
}

/* 判断是否为邮政编码 */
function f_check_zipcode(obj)
{
	if(!f_check_number(obj))
		return false;
	if(obj.value.length!=6)
	{
		f_alert(obj,"邮政编码长度必须是6位");
		return false;
	}
	return true;
}

/*
用户ID，可以为数字、字母、下划线的组合，
第一个字符不能为数字,且总长度不能超过20。
*/
function f_check_userID(obj)
{
	var userID = obj.value;
	if(userID.length > 20)
	{
		f_alert(obj,"ID长度不能大于20");
		return false;
	}

	if(!isNaN(userID.charAt(0)))
	{
		f_alert(obj,"ID第一个字符不能为数字");
		return false;
	}
	if(!/^\w{1,20}$/.test(userID)) 
	{
		f_alert(obj,"ID只能由数字、字母、下划线组合而成");
		return false;
	}
	return true;
}

/*
功能：验证身份证号码是否有效
提示信息：未输入或输入身份证号不正确！
使用：f_check_IDno(obj)
返回：bool
*/
function f_check_IDno(obj)
{ 
	var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
 
	var iSum = 0;
	var info = "";
	var strIDno = obj.value;
	var idCardLength = strIDno.length;  
	if(!/^\d{17}(\d|x)$/i.test(strIDno)&&!/^\d{15}$/i.test(strIDno)) 
	{
		f_alert(obj,"非法身份证号");
		return false;
	}
 
	//在后面的运算中x相当于数字10,所以转换成a
	strIDno = strIDno.replace(/x$/i,"a");

	if(aCity[parseInt(strIDno.substr(0,2))]==null)
	{
		f_alert(obj,"非法地区");
		return false;
	}
	
	if (idCardLength==18)
	{
		sBirthday=strIDno.substr(6,4)+"-"+Number(strIDno.substr(10,2))+"-"+Number(strIDno.substr(12,2));
		var d = new Date(sBirthday.replace(/-/g,"/"))
		if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))
		{		
			f_alert(obj,"非法生日");
			return false;
		}

		for(var i = 17;i>=0;i --)
			iSum += (Math.pow(2,i) % 11) * parseInt(strIDno.charAt(17 - i),11);

		if(iSum%11!=1)
		{
			f_alert(obj,"非法身份证号");
			return false;
		}
	}
	else if (idCardLength==15)
	{
		sBirthday = "19" + strIDno.substr(6,2) + "-" + Number(strIDno.substr(8,2)) + "-" + Number(strIDno.substr(10,2));
		var d = new Date(sBirthday.replace(/-/g,"/"))
		var dd = d.getFullYear().toString() + "-" + (d.getMonth()+1) + "-" + d.getDate();   
		if(sBirthday != dd)
		{
			f_alert(obj,"非法生日");
			return false;
		}
	}
	return true; 
}

/*
* 判断字符串是否符合指定的正则表达式
*/
function f_check_formatStr(obj)
{
	var str = obj.value;
	var dtype = obj.eos_datatype;
	var regu = dtype.substring(dtype.indexOf("(")+1,dtype.indexOf(")"));	//指定的正则表达式
	var re = new RegExp(regu);
	if(re.test(str))
		return true;
	f_alert(obj , "不符合指定的正则表达式要求");
	return false;	
}

/*
功能：判断是否为日期(格式:yyyy年MM月dd日,yyyy-MM-dd,yyyy/MM/dd,yyyyMMdd)
提示信息：未输入或输入的日期格式错误！
使用：f_check_date(obj)
返回：bool
*/
function f_check_date(obj)
{
	var date = Trim(obj.value);
	if(date.length==0) return true;
	var dtype = obj.eos_datatype;
	if(dtype.indexOf("(")==-1) {f_alert(obj, "请指定日期格式"); return false; }
	var format = dtype.substring(dtype.indexOf("(")+1,dtype.indexOf(")"));	//日期格式
	var year,month,day,datePat,matchArray;
	
	if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(format))
		datePat = /^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
	else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)$/.test(format))
		datePat = /^(\d{4})年(\d{1,2})月(\d{1,2})日$/;
	else if(format=="yyyyMMdd")
		datePat = /^(\d{4})(\d{2})(\d{2})$/;
	else
	{
		f_alert(obj,"日期格式不对");
		return false;
	}
	matchArray = date.match(datePat);
	if(matchArray == null) 
	{
		f_alert(obj,"日期长度不对,或日期中有非数字符号");
		return false;
	}
	if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2})$/.test(format))
	{
		year = matchArray[1];
		month = matchArray[3];
		day = matchArray[4];
	} else
	{
		year = matchArray[1];
		month = matchArray[2];
		day = matchArray[3];
	}
	if (month < 1 || month > 12)
	{			  
		f_alert(obj,"月份应该为1到12的整数");
		return false;
	}
	if (day < 1 || day > 31)
	{
		f_alert(obj,"每个月的天数应该为1到31的整数");
		return false;
	}     
	if ((month==4 || month==6 || month==9 || month==11) && day==31)
	{
		f_alert(obj,"该月不存在31号");
		return false;
	}     
	if (month==2)
	{
		var isleap=(year % 4==0 && (year % 100 !=0 || year % 400==0));
		if (day>29)
		{				
			f_alert(obj,"2月最多有29天");
			return false;
		}
		if ((day==29) && (!isleap))
		{				
			f_alert(obj,"闰年2月才有29天");
			return false;
		}
	}
	return true;
}

/*
功能：校验的格式为yyyy年MM月dd日HH时mm分ss秒,yyyy-MM-dd HH:mm:ss,yyyy/MM/dd HH:mm:ss,yyyyMMddHHmmss
提示信息：未输入或输入的时间格式错误
使用：f_check_time(obj)
返回：bool
*/
function f_check_time(obj)
{
	var time = Trim(obj.value);
	if(time.length==0) return true;
	var dtype = obj.eos_datatype;
	if(dtype.indexOf("(")==-1) {f_alert(obj,"请指定时间格式"); return false; }
	var format = dtype.substring(dtype.indexOf("(")+1,dtype.indexOf(")"));	//日期格式
	var datePat,matchArray,year,month,day,hour,minute,second;

	if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2}) (HH:mm:ss)$/.test(format))
		datePat = /^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
	else if(/^(y{4})(年)(M{1,2})(月)(d{1,2})(日)(HH时mm分ss秒)$/.test(format))
		datePat = /^(\d{4})年(\d{1,2})月(\d{1,2})日(\d{1,2})时(\d{1,2})分(\d{1,2})秒$/;
	else if(format == "yyyyMMddHHmmss")
		datePat = /^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/;
	else
	{
		f_alert(obj,"日期格式不对");
		return false;
	}
	matchArray = time.match(datePat);
	if(matchArray == null) 
	{
		f_alert(obj,"日期长度不对,或日期中有非数字符号");
		return false;
	}
	if(/^(y{4})(-|\/)(M{1,2})\2(d{1,2}) (HH:mm:ss)$/.test(format))
	{
		year = matchArray[1];
		month = matchArray[3];
		day = matchArray[4];
		hour = matchArray[5];
		minute = matchArray[6];
		second = matchArray[7];
	} else
	{
		year = matchArray[1];
		month = matchArray[2];
		day = matchArray[3];
		hour = matchArray[4];
		minute = matchArray[5];
		second = matchArray[6];
	}
	if (month < 1 || month > 12)
	{			  
		f_alert(obj,"月份应该为1到12的整数");
		return false;
	}
	if (day < 1 || day > 31)
	{			
		f_alert(obj,"每个月的天数应该为1到31的整数");
		return false;
	}     
	if ((month==4 || month==6 || month==9 || month==11) && day==31)
	{		  
		f_alert(obj,"该月不存在31号");
		return false;
	}     
	if (month==2)
	{
		var isleap=(year % 4==0 && (year % 100 !=0 || year % 400==0));
		if (day>29)
		{				
			f_alert(obj,"2月最多有29天");
			return false;
		}
		if ((day==29) && (!isleap))
		{				
			f_alert(obj,"闰年2月才有29天");
			return false;
		}
	}
	if(hour<0 || hour>23)
	{
		f_alert(obj,"小时应该是0到23的整数");
		return false;
	}
	if(minute<0 || minute>59)
	{
		f_alert(obj,"分应该是0到59的整数");
		return false;
	}
	if(second<0 || second>59)
	{
		f_alert(obj,"秒应该是0到59的整数");
		return false;
	}
    return true;
}
/*判断当前对象是否可见*/
function isVisible(obj){
	var visAtt,disAtt;
	try{
		disAtt=obj.style.display;
		visAtt=obj.style.visibility;
	}catch(e){}
	if(disAtt=="none" || visAtt=="hidden")
		return false;
	return true;
}
/*判断当前对象及其父对象是否可见*/
function checkPrVis(obj){
	var pr=obj.parentNode;
	do{
		if(pr == undefined || pr == "undefined") return true;
		else{
			if(!isVisible(pr)) return false;
		}
	}while(pr=pr.parentNode);
	return true;
}
/* 弹出警告对话框，用户点确定后将光标置于出错文本框上， 并且将原来输入内容选中。*/
function f_alert(obj,alertInfo)
{
	var caption = obj.getAttribute("eos_displayname");
	var type = obj.getAttribute("type");
	if(caption == null) 
		caption = "";
	alert(caption + "：" + alertInfo + "！");
	if(type != null){
		if(type == "text" || type == "TEXT" || type == "textarea" || type == "TEXTAREA")
			obj.select();
	}
	if(isVisible(obj) && checkPrVis(obj))
		obj.focus();
}

/**
* 检测字符串是否为空 
*/
function isnull(str)
{
    var i;
    if(str.length == 0)
		return true;
    for (i=0;i<str.length;i++)
    {
        if (str.charAt(i)!=' ') 
			return false;
    }
    return true;
}

/**
* 检测指定文本框输入是否合法。
* 如果用户输入的内容有错，则弹出提示对话框，
* 同时将焦点置于该文本框上，并且该文本框前面
* 会出现一个警告图标(输入正确后会自动去掉)。
*/
function checkInput(object)
{
	var image;
	var i;
	var length;

	if(object.eos_maxsize + "" != "undefined") length = object.eos_maxsize;
	else length = 0;

	//if (object.eos_isnull=="true" && isnull(object.value))	return true;
      if (isnull(object.value))	return true;//modify by ton
	/* 长度校验 */
	if(length != 0 && strlen(object.value) > parseInt(length)) {
			f_alert(object, "超出最大长度" + length);
			return false;
	} 
	/* 数据类型校验 */
	else {
		if (object.eos_datatype + "" != "undefined")
		{		

			var dtype = object.eos_datatype;
			var objName = object.name;
			//如果类型名后面带有括号，则视括号前面的字符串为校验类型
			if(dtype.indexOf("(") != -1)
				dtype = dtype.substring(0,dtype.indexOf("("));
			//根据页面元素的校验类型进行校验
			try{
				if(eval("f_check_" + dtype + "(object)") != true)
					return false;
			}catch(e){return true;}
			/*	如果form中存在name前半部分相同，并且同时存在以"min"和"max"结尾的表单域，
				那么视为按区间查询。即"min"结尾的表单域的值要小于等于"max"结尾的表单域的值。	*/
			if(objName.substring((objName.length-3),objName.length)=="min")
			{
				var objMaxName = objName.substring(0, (objName.length-3)) + "max";
				if(document.getElementById(objMaxName) != undefined && document.getElementById(objMaxName) != "undefined" )
				{
					if(checkIntervalObjs(object, document.getElementById(objMaxName)) != true)
						return false;					
				}
			}			
		}
	}
	return true;
}

/* 检测表单中所有输入项的正确性，一般用于表单的onsubmit事件 */
function checkForm(myform)
{
	var i;
	for (i=0;i<myform.elements.length;i++)
	{
	    /* 非自定义属性的元素不予理睬 */		
		if (myform.elements[i].eos_displayname + "" == "undefined") continue;
		/* 非空校验 */
		if (myform.elements[i].eos_isnull=="false" && isnull(myform.elements[i].value)){
			f_alert(myform.elements[i],"不能为空");
			return false;
		}		
		/* 数据类型校验 */
		if (checkInput(myform.elements[i])==false)
			return false;				
	}
	return true;
}

/**
* 校验两个表单域数据的大小，目前只允许比较日期和数字。
* @param obj1 小值表单域
* @param obj2 大值表单域
*/
function checkIntervalObjs(obj1 , obj2)
{	
	var caption1 = obj1.eos_displayname;
	var caption2 = obj2.eos_displayname;
	var val1 = parseFloat(obj1.value);
	var val2 = parseFloat(obj2.value);
	var dtype1 = obj1.eos_datatype;
	var dtype2 = obj2.eos_datatype;
	
	// 非自定义属性的元素不予理睬
	if (obj1.eos_displayname + "" == "undefined" || obj2.eos_displayname + "" == "undefined") {
		return false;
	}
	// 日期类型的比较
	if((dtype1.indexOf("date")!=-1 && dtype2.indexOf("date")!=-1) ||
		(dtype1.indexOf("time")!=-1 && dtype2.indexOf("time")!=-1)){
		if(Trim(obj1.value.length)==0 || Trim(obj2.value.length)==0) return true;
		var format1 = dtype1.substring(dtype1.indexOf("(")+1,dtype1.indexOf(")"));	//日期时间格式
		var format2 = dtype2.substring(dtype2.indexOf("(")+1,dtype2.indexOf(")"));	//日期时间格式
		val1 = getDateByFormat(obj1.value, format1);
		val2 = getDateByFormat(obj2.value, format2);
		if(val1 > val2){
			obj2.select();
			if(isVisible(obj2) && checkPrVis(obj2))	obj2.focus();
			alert(caption1 + "的起始日期不能大于其终止日期！");
			return false;
		}
	}	
	// 数字类型的比较
	if((isNaN(val1) && !isnull(val1)) || (isNaN(val2) && !isnull(val2))){
		alert(caption1 + "的值不全为数字则不能比较！");
		return false;
	}
	if(val1 > val2){
		obj2.select();
		if(isVisible(obj2) && checkPrVis(obj2))
			obj2.focus();
		alert(caption1 + "的起始值不能大于其终止值！");
		return false;
	}
	return true;
}
//校验表单并提交
function check(frm){
	if(checkForm(frm) == false) return;
	else frm.submit();    	
}

//校验表单并提交
function checkfrm(frm,action){
	if(checkForm(frm) == false) return;
	else {
		frm.reqType.value=action;
		frm.submit();    	
		}
}
//表单数据重置
function resetData(frm){
	frm.reset();
}

/*根据日期格式，将字符串转换成Date对象。
格式：yyyy-年，MM-月，dd-日，HH-时，mm-分，ss-秒。
（格式必须写全，例如:yy-M-d，是不允许的，否则返回null；格式与实际数据不符也返回null。）
默认格式：yyyy-MM-dd HH:mm:ss,yyyy-MM-dd。*/
function getDateByFormat(str){
	var dateReg,format;
	var y,M,d,H,m,s,yi,Mi,di,Hi,mi,si;
	if((arguments[1] + "") == "undefined") format = "yyyy-MM-dd HH:mm:ss";
	else format = arguments[1];
	yi = format.indexOf("yyyy");
	Mi = format.indexOf("MM");
	di = format.indexOf("dd");
	Hi = format.indexOf("HH");
	mi = format.indexOf("mm");
	si = format.indexOf("ss");
	if(yi == -1 || Mi == -1 || di == -1) return null;
	else{
		y = parseInt(str.substring(yi, yi+4),10);
		M = parseInt(str.substring(Mi, Mi+2),10);
		d = parseInt(str.substring(di, di+2),10);
	}
	if(isNaN(y) || isNaN(M) || isNaN(d)) return null;
	if(Hi == -1 || mi == -1 || si == -1) return new Date(y, M-1, d);
	else{
		H = str.substring(Hi, Hi+4);
		m = str.substring(mi, mi+2);
		s = str.substring(si, si+2);
	}
	if(isNaN(parseInt(y)) || isNaN(parseInt(M)) || isNaN(parseInt(d))) return new Date(y, M-1, d);
	else return new Date(y, M-1, d,H, m, s);
}

/*LTrim(string):去除左边的空格*/
function LTrim(str){
    var whitespace = new String(" \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*RTrim(string):去除右边的空格*/
function RTrim(str){
    var whitespace = new String(" \t\n\r");
    var s = new String(str); 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*Trim(string):去除字符串两边的空格*/
function Trim(str){
    return RTrim(LTrim(str));
}