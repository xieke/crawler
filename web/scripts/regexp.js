/*
-------------------------------------------------------------------------------
文件名称：regexp.js
说    明：JavaScript脚本，正则表达式校验
版    本：1.0
修改纪录:
---------------------------------------------------------------------------
时间		修改人		说明
2005-4-30	zhouming		创建
------------------------------------------------------------------------------- 	
*/

/*
用途：正则表达式校验
输入：str：被校验的字符串；pattern：表达式 flag: 标记
返回：如果通过验证返回true,否则返回false；	
*/
function checkRegexp(str,pattern,flag) {
 var result=false;
 var flagReg=/^[igm]*$/g;
if(!flagReg.test(flag)) {
	alert("正则表达式标记错误");
    return result;
}
 var regexp = new RegExp(pattern,flag);
 result = regexp.test(str);
 return result;
} 
