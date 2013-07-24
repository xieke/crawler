/*
-------------------------------------------------------------------------------
文件名称：checkBox.js
说    明：JavaScript脚本，一些和复选筐有关的脚本
版    本：1.0
修改纪录:
---------------------------------------------------------------------------
时间		修改人		说明
2005-4-20	zhouming		创建
------------------------------------------------------------------------------- 	
*/

/*
用途：得到复选框被选中的数目
输入：checkboxID：复选框的名称或者ID
返回：返回该复选框中被选中的数目	
*/	
function checkBoxSelectCount( checkboxID ) {
	var check = 0;
	if( document.all(checkboxID) == null)
		return check;
        check=countSelNum(checkboxID);
	return check;
}

/*
用途：改变复选框的全选状态
输入：checkboxID：复选框的名称或者ID , 
            status : true 全选中
			         false 全未选中
返回：	
*/	
function checkBoxSelectAll( checkboxID,status )	{
	if( document.all(checkboxID) == null)
		return;

	if( document.all(checkboxID).length > 0 ){ 
		for(  i=0; i<document.all(checkboxID).length; i++ )	{
			document.all(checkboxID).item( i ).checked = status;
		}
	} else {
		document.all(checkboxID).checked = status;
	}
}



/********************************************************************************************************
* added by Chen Chun 
* checkBoxSelectAll,checkBoxSelectCount 两个函数的简洁版本 checkAll,countSelNum
*********************************************************************************************************/

//改变复选框的全选状态
function checkAll(target)
{
	var cks = document.getElementsByName(target);
	var ck = window.event.srcElement;

	if(ck.checked==true)
	{
		for(var i=0;i<cks.length;i++)
		{
			if(cks[i].checked == false) cks[i].checked = true;
		}
	}
	else
	{
		for(var i=0;i<cks.length;i++)
		{
			if(cks[i].checked == true) cks[i].checked = false;
		}
	}
}
//被选中复选框个数
function countSelNum(target)
{
	var cks = document.getElementsByName(target);
	var sel = 0;
	for(var i=0;i<cks.length;i++)
	{
		if(cks[i].checked == true) sel++;
	}
	return sel;
}

//添加记录的提交函数
function prepareInsert(form, action, subtarget){
	var frm = form;
	frm.action = action;
	if (subtarget != null) frm.target=subtarget;
	frm.submit();
}

//修改记录的提交函数
function prepareUpdateRow(form, action, target, subtarget){
	var frm = form;
	if(checkBoxSelectCount(target) < 1){
	alert("必须选择一行！");
	return ;
	}else if(checkBoxSelectCount(target) > 1){
	alert("只能选择一行, 不允许多选！");
	return ;
	}
	frm.action = action;
	if (subtarget != null) frm.target=subtarget;
	frm.submit();    
}

//删除记录的提交函数
function deleteRows(form, action, target, subtarget){
	var frm = form;
	if(checkBoxSelectCount(target)<1){
	alert("至少必须选择一行！");
	return ;
	}

	if(confirm("是否确定删除指定记录？")==false){
	return ;
	}      

	frm.action = action;
	if (subtarget != null) frm.target=subtarget;
	frm.submit();
}
