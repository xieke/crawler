/*
-------------------------------------------------------------------------------
文件名称：string.js
说    明：JavaScript脚本，处理一些和字符有关的
版    本：1.0
修改纪录:
---------------------------------------------------------------------------
时间		修改人		说明
2005-4-20	zhouming		创建
------------------------------------------------------------------------------- 	
*/

/*
用途：查找str1中是否包含str2
输入：str1：字符串；str2：被包含的字符串
返回：若包含，则返回str1中从str2后一字符开始的所有字符串；否则返回空字符串e；	
*/
function substringAfter( str1,  str2) { 
   var index = str1.indexOf(str2);
   if(index==-1) return "";
   return str1.substring(index+str2.length);
} 

/*
用途：查找str1中是否包含str2
输入：str1：字符串；str2：被包含的字符串
返回：若包含，则返回str1中str2前的所有字符串；否则返回空字符串。
*/
function substringBefore( str1,  str2) { 
   var index = str1.indexOf(str2);
   if(index==-1) return "";
   return str1.substring(0,index);
} 

/*
用途：获得字符串的字节数
输入：str：字符串；
返回：返回字符串的字节数
*/function charLength(str) {
    if( str == null || str ==  "" ) return 0;
    var totalCount = 0;
    for (i = 0; i< str.length; i++) {
        if (str.charCodeAt(i) > 127) 
            totalCount += 2;
        else
            totalCount++ ;
    }
    return totalCount;
}

/*
用途：判断是否含有非ASCII码字符
输入：s：字符串
返回：  如果通过验证返回true,否则返回false  
*/
function containsNOASC( s) {
    if( s == null || s ==  "" ) return false;
    for (i = 0; i< s.length; i++) {
        if (s.charCodeAt(i) > 127) 
             return true;
    }
    return false;
}

/*
用途：去掉字符串左边空格函数
输入：str：字符串
返回： 去掉空格的字符串  
*/
function lTrim(str){
    var i = 0;
        while(str.charCodeAt(i) <=32 )
        {
            ++i;
        }
        str = str.substring(i);
		return str;
}

/*
用途：去掉字符串右边空格函数
输入：str：字符串
返回： 去掉空格的字符串
*/
function rTrim(str){
    var i = str.length-1;
        while(str.charCodeAt(i) <=32)
        {
            --i;
        }
        str = str.substring(0,i+1);
		return str;
}

/*
用途：去掉字符串左右两边空格函数
输入：str：字符串
返回： 去掉空格的字符串
*/
function trim(str){
		return lTrim(rTrim(str));
}

/*
用途：去掉字符串左右两边还有中间空格函数
输入：str：字符串
返回： 去掉空格的字符串
*/
function allTrim(str){
		return str.replace(/\s/g,"");

}
/*
用途：把字符串中的"<",">","&"," "用xml转义符表示;
输入：str：字符串
返回： 转义后的字符串
*/
function conversion(str)
{
	var newstr="";
		for(var i=0;i<str.length;i++)
	{
		switch(str.charAt(i))
		{
		case '<':
		    newstr=newstr+"&lt;";
			break;
		case ">":
            newstr=newstr+"&gt;";
            break;
		case "&":
            newstr=newstr+"&amp;";
			break;
		//case " ":  空格不需要转义
        //    newstr=newstr+"&nbsp;";
		//	break;
		default:
		newstr=newstr+str.charAt(i);
		break;

		}
		
		}
		return newstr;
	}

