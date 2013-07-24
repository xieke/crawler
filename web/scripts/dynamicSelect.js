/*
-------------------------------------------------------------------------------
文件名称：dynamicSelect.js
存放路径：/pageComponent/resources/scripts/dynamicSelect.js
说    明：JavaScript脚本，用于动态生成级联Select框
版    本：1.0
依    赖：
<SCRIPT language="jscript" src="/pageComponent/resources/scripts/processCaller.js"></SCRIPT>
<SCRIPT language="jscript" src="/pageComponent/resources/scripts/string.js"></SCRIPT>
修改纪录:
---------------------------------------------------------------------------
时间		   修改人		    说明
2005-6-29	 yangyaoli		创建
2005-8-11  yangyaoli    功能修改
2005-12-28 wangkq       解决在最新版IE中不能正确运行的BUG
------------------------------------------------------------------------------- 	
*/

//刷新子Select中的数据
function refreshChild_DS(parentSelect) {
	if (parentSelect.childID == null || parentSelect.childID == '') {
		return;
	}
	var child = document.getElementById(parentSelect.childID);
	var allParament = "";//级联参数构造
	if (parentSelect.mainSelectID == null || parentSelect.mainSelectID =="") {//主Select刷新时
		allParament = "<root><data><"+parentSelect.entityName+" number=\"0\">"
									+"<"+parentSelect.textField+">"
									+conversion(parentSelect.options[parentSelect.selectedIndex].text)
									+"</"+parentSelect.textField+">"
									+"<"+parentSelect.valueField+">"
									+conversion(parentSelect.value)
									+"</"+parentSelect.valueField+">"
									+"</"+parentSelect.entityName+">"
									+parentSelect.paramXML+"</data></root>";//加入用户自定义的参数
	} else {//所有从Select刷新时
		var tempSel = document.getElementById(parentSelect.mainSelectID);
		var j = 0
		allParament = "<root><data>";
		while (tempSel != parentSelect) {//直到当前的Select才退出
			allParament = allParament + "<"+tempSel.entityName+" number=\""+j+"\">"
										+"<"+tempSel.textField+">"
										+conversion(tempSel.options[tempSel.selectedIndex].text)
										+"</"+tempSel.textField+">"
										+"<"+tempSel.valueField+">"
										+conversion(tempSel.value)
										+"</"+tempSel.valueField+">"
										+"</"+tempSel.entityName+">"
										+tempSel.paramXML;//加入用户自定义的参数
			j++;
			tempSel = document.getElementById(tempSel.childID);
		}
		allParament = allParament + "<"+ parentSelect.entityName+" number=\""+j+"\">"
									+"<"+parentSelect.textField+">"
									+conversion(parentSelect.options[parentSelect.selectedIndex].text)
									+"</"+parentSelect.textField+">"
									+"<"+parentSelect.valueField+">"
									+conversion(parentSelect.value)
									+"</"+parentSelect.valueField+">"
									+"</"+parentSelect.entityName+">"
									+parentSelect.paramXML+"</data></root>";//加入用户自定义的参数
	}
	//alert(allParament);
	var strResponse = callBizAction(child.bizAction,allParament);
	if (strResponse == false) {//调用错误，直接返回
		return;
	}
	var xmlDom = new ActiveXObject("Msxml2.DOMDocument");
	xmlDom.async = false;
	xmlDom.resolveExternals = false;
	xmlDom.loadXML(strResponse);
	var xmlList = xmlDom.selectSingleNode("/root/data/"+child.listXpath);
	if (xmlList == null) {
		alert("在业务逻辑返回的数据中，没有找到listXpath属性中所设置的路径！请检查代码。");
		return;
	}
	
	removeAllSelectOption(child);
//	child.length = null;
	//处理Default Option
	if (child.nullLable != null && child.nullLable == "true") {
        var vv = "";
        var tt = "";
        if (child.nullLableText != null) {
            tt = child.nullLableText;
        }
        if (child.nullLableValue != null) {
            vv = child.nullLableValue;
        }
        var loption = new Option(tt, vv);
        child.options.add(loption);
	}
	
	var entityList = xmlList.selectNodes(child.entityName);
	if ((entityList == null)||(entityList.length < 1)) {
		if (child.options.length > 0)
			refreshChild_DS(child);
		return;
	}
	
	for (var i=0;i<entityList.length;i++) {
		var vv = "";
		var tt = "";
		var optionText = null;
		var optionValue = null;
  		optionValue = entityList.item(i).selectSingleNode(child.valueField);
  		if (optionValue != null) {//取到valueField中的值
  			vv = optionValue.text;
  		}
  		optionText = entityList.item(i).selectSingleNode(child.textField);
  		if (optionText != null) {//取到textField中的值
  			tt = optionText.text;
	  	}
  		var toption = new Option(tt, vv);
  		child.options.add(toption);
	}
	refreshChild_DS(child);
}

//对数据进行初始化查询操作
function initSel_DS(aSelect) {
	var strResponse = callBizAction(aSelect.bizAction,"<root><data>"+aSelect.paramXML+"</data></root>");
	if (strResponse == false) {//调用错误，直接返回
		return;
	}
	var xmlDom = new ActiveXObject("Msxml2.DOMDocument");
	xmlDom.async = false;
	xmlDom.resolveExternals = false;
	xmlDom.loadXML(strResponse);
	var xmlList = xmlDom.selectSingleNode("/root/data/"+aSelect.listXpath);
	if (xmlList == null) {
		alert("在业务逻辑返回的数据中，没有找到listXpath属性中所设置的路径！请检查代码。");
		return;
	}
	//处理Default Option
	if (aSelect.nullLable != null && aSelect.nullLable == "true") {
    	var vv = "";
    	var tt = "";
        if (aSelect.nullLableText != null) {
            tt = aSelect.nullLableText;
        }
        if (aSelect.nullLableValue != null) {
            vv = aSelect.nullLableValue;
        }
        var loption = new Option(tt, vv);
        aSelect.options.add(loption);
	}
	
	var entityList = xmlList.selectNodes(aSelect.entityName);
	if ((entityList == null) || (entityList.length < 1)) {
		if (aSelect.options.length > 0)
			refreshChild_DS(aSelect);
		return;
	}
	
	for (var i=0;i<entityList.length;i++) {
		var vv = "";
		var tt = "";
		var optionText = null;
		var optionValue = null;
  		optionValue = entityList.item(i).selectSingleNode(aSelect.valueField);
  		if (optionValue != null) {//取到valueField中的值
  			vv = optionValue.text;
  		}
  		optionText = entityList.item(i).selectSingleNode(aSelect.textField);
  		if (optionText != null) {//取到textField中的值
  			tt = optionText.text;
	  	}
  		var toption = new Option(tt, vv);
  		aSelect.options.add(toption);
	}
	refreshChild_DS(aSelect);
}

function removeAllSelectOption(obj)
{
    for (var j = obj.options.length - 1; j >= 0; j--)
    {
        obj.remove(j);
    }
}
