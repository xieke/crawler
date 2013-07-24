/*
-------------------------------------------------------------------------------
文件名称：select.js
说    明：JavaScript脚本，一些和下拉筐有关的脚本
版    本：1.0
修改纪录:
---------------------------------------------------------------------------
时间		修改人		说明
2005-4-21	zhouming		创建
2005-4-29	zhouming		修改 selectOptionsChange的选第一个option移动所有option 错误
2005-8-10   zhouming		修改 多选的Select框中使用selectOptionsChange函数时，如果全部选择则改功能错误
------------------------------------------------------------------------------- 	
*/

/*
用途：根据"参数值"选中对应的select的option（支持多选的select）
输入：selName：下拉框名或ID；
            selValue：需要选中的Value值 可以是数组
			formName：所在的form名称；
返回：Void  
*/
function selectOptionSelect( selName,selValue,formName){
	var form = document.forms[0];
	if (formName!=null){
		form = document.forms[formName];
	}
	var valueArray = new Array();
    valueArray=valueArray.concat(selValue);
	var sel = form.all(selName,0);
	for (var i=0;i<valueArray.length ; i++){
		var value = valueArray[i];
        for (var j=0;j<sel.options.length ; j++){
			if (value==sel.options[j].value){
				sel.options[j].selected = true;
			}
        }
	}
}

/*
用途：获得select对象选中的option的值或值数组
输入：selName：下拉框名或ID；
			formName：所在的form名称；
返回：单选：option的值
            多选：option的值组成的数组
*/
function getSelectedValues( selName,formName){
	var form = document.forms[0];
	if (formName!=null){
		form = document.forms[formName];
	}
	var valueArray = new Array();
	var sel = form.all(selName,0);
	var cnt =0;
        for (var j=0;j<sel.options.length ; j++){
			if (sel.options[j].selected == true){
				valueArray[cnt] = sel.options[j].value;
				cnt++;
			}
	}
	if(sel.multiple!=true) return valueArray[0];
	return valueArray;
}

/*
用途：获得select对象选中的option的显示值或显示值数组
输入：selName：下拉框名或ID；
			formName：所在的form名称；
返回：单选：option的显示值
            多选：option的显示值组成的数组
*/
function getSelectedTexts( selName,formName){
	var form = document.forms[0];
	if (formName!=null){
		form = document.forms[formName];
	}
	var valueArray = new Array();
	var sel = form.all(selName,0);
	var cnt =0;
        for (var j=0;j<sel.options.length ; j++){
			if (sel.options[j].selected == true){
				valueArray[cnt] = sel.options[j].text;
				cnt++;
			}
	}
	if(sel.multiple!=true) return valueArray[0];
	return valueArray;
}

/*
用途：左右选择框之间的增加和删除操作
输入：selNameFrom：删除选项的下拉框名或ID；
			selNameTo：增加选项的下拉框名或ID；
			formName：所在的form名称；
返回：Void
*/
function selectOptionsChange( selNameFrom,selNameTo,formName){
	var form = document.forms[0];
	if (formName!=null){
		form = document.forms[formName];
	}
	var sel1 = form.all(selNameFrom,0);
	var sel2 = form.all(selNameTo,0);
        for (var j=0;j<sel1.options.length ; j++){
			if (sel1.options[j].selected == true){
				var opt = sel1.options[j];
				sel1.options[j] = null;
				sel2.options[sel2.options.length] = opt;
				//防止单选时,把整个下拉的值增加和删除
				if (j==0&&sel1.options[j]!=null&&sel1.options[j].selected == true&&sel1.multiple!=true) break;
				j=j-1;
			}
	}
}

/*
用途：选中所有的多选select中的值
输入：selName：下拉框名或ID；
			formName：所在的form名称；
返回：Void
*/
function selectOptionSelectAll( selName,formName){
	var form = document.forms[0];
	if (formName!=null){
		form = document.forms[formName];
	}
	var sel = form.all(selName,0);
        for (var j=0;j<sel.options.length ; j++){
            sel.options[j].selected = true;
	}
}

/*
用途：判断某个值是否在选择框中
输入：aSelect：下拉框ID；
	  value：值；
返回：true：在
      false：不在
*/
function isInSelect(aSelect, value)
{
 var obj = document.getElementById(aSelect);
 for (var i=0; i<obj.options.length; i++) {
  if (obj.options[i].value == value)
   return true;
 }
 return false;
}
 

/*
用途：选中所有的多选select中的值
输入：aSelect：下拉框ID；
返回：Void
*/
function multiSelectAll(aSelect)
{
 var obj = document.getElementById(aSelect);
 for (var j=0; j<obj.options.length; j++) {
  obj.options[j].selected = true;
 }
}
 
/*
用途：全不选所有的多选select中的值
输入：aSelect：下拉框ID；
返回：Void
*/
function multiDeSelectAll(aSelect)
{
 var obj = document.getElementById(aSelect);
 for (var j=0; j<obj.options.length; j++) {
  obj.options[j].selected = false;
 }
}
 
/*
用途：删除选中的
输入：aSelect：下拉框ID；
返回：Void
*/
function removeSelected(aSelect)
{
 var obj = document.getElementById(aSelect);
 for (var j=obj.options.length-1; j>=0; j--) {
  if (obj.options[j].selected)
   obj.remove(j);
 }
}
 
/*
用途：删除select中所有的值
输入：aSelect：下拉框ID；
返回：Void
*/
function removeAllSelect(aSelect)
{
 var obj = document.getElementById(aSelect);
 for (var j=obj.options.length-1; j>=0; j--) {
  obj.remove(j);
 }
}
 

/*
用途：源和目的选择框之间选中的增加和删除操作
输入：src：源下拉框的ID；
	  dest：目的下拉框ID；
      remove：是否删除源下拉框中选中的；
返回：Void
*/
function multiSelectSend(src, dest, remove)
{
 var fromSelect = document.getElementById(src);
 var toSelect = document.getElementById(dest);
 var isRemove = true;
 if (remove == null || remove == false) {
  isRemove = false;
 }
 for (var i=0; i<fromSelect.options.length; i++) {
  var opt = fromSelect.options[i];
  if (opt.selected) {
   if (!isInSelect(dest, opt.value)) {
    var oOption = new Option(opt.text, opt.value);
    toSelect.add(oOption);
   }
  }
 }
 if (isRemove) {
  removeSelected(src);
 }
}
 
/*
用途：源和目的选择框之间增加和删除操作(包括没选中的)
输入：src：源下拉框的ID；
	  dest：目的下拉框ID；
      remove：是否删除源下拉框中选中的；
返回：Void
*/
function multiSelectSendAll(src, dest, remove)
{
 var fromSelect = document.getElementById(src);
 var toSelect = document.getElementById(dest);
 var isRemove = true;
 if (remove == null || remove == false) {
  isRemove = false;
 }
 for (var i=0; i<fromSelect.options.length; i++) {
  var opt = fromSelect.options[i];
  if (!isInSelect(dest, opt.value)) {
   var oOption = new Option(opt.text, opt.value);
   toSelect.add(oOption);
  }
 }
 if (isRemove) {
  removeAllSelect(src);
 }
}
