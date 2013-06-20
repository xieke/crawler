

// 
// 此文件包含一些公共的js方法，以及和html相关的方法
//

//=========================> 公共js方法 <=========================

//去除字符串前后的空格
function trim(str){
	if(str!=null){
		rex=/^ +/;
		rex2=/ +$/;
		return str.replace(rex,"").replace(rex2,"");
	}
	return "";
}

//替换字符串
//srcStr原始字符串  sourStr被替换字符串 destStr替换的字符串
function replaceAll(srcStr,sourStr,destStr){
	while(srcStr.indexOf(sourStr)!=-1)
		srcStr=srcStr.replace(sourStr,destStr);
	return srcStr;
}

//检查是否为数字
function isNum(str){
	rex=/^\d+$/
	return str.match(rex)==str;
}

//产生随机字符串
function randomStr(){
	var d=new Date();
	s = ""+d.getHours()+d.getMinutes()+d.getSeconds()+d.getMilliseconds();
	s = "z_"+parseInt(""+(Math.random()*10000))+"_z_"+s;
	return s;
}

//表格排序相关方法
/**function sort_(obj,fieldName,q_){
	var keystr=obj.className;
	var nx=keystr.indexOf("_")+1;
	var sortKey=(nx!=keystr.length&&keystr.substring(nx)=="asc")?"desc":"asc";
	var obs=document.getElementsByName("orderBy");
	if(obs==null||obs.length<1){
		alert("排序发生错误,没有找到HTML页面中的name=\"orderBy\"的控件);
		return;
	}
	obs[0].value=fieldName+" "+sortKey;
	q_();
}**/


//=========================> Html相关的js方法 <=========================

//
//   <input type="checkbox" 控件相关的方法
// 

// 全选 复选框
// 参数：
// allCheckboxObj 必须 触发全选的checkbox控件对象
// langFilter 可选 通过<input type="checkbox" lang="abc"中的abc进行分组过滤
function checkedAll(allCheckboxObj,langFilter){
	var ips=document.getElementsByTagName("input");
	var isChecked=allCheckboxObj.checked;
	for(var i = 0; i < ips.length;i++)
		if(ips[i].type=="checkbox" && (langFilter==null || ips[i].lang==langFilter))
			if(ips[i]!=allCheckboxObj&&!ips[i].disabled)
				ips[i].checked=isChecked;
}

// 返回checkbox选择的数量
// 参数：
// langFilter 可选 通过<input type="checkbox" lang="abc"中的abc进行分组过滤
function getCheckedCount(langFilter){
	var ips=document.getElementsByTagName("input");
	var nn=0;
	for(var i = 0; i < ips.length; i++)
		if(ips[i].type=="checkbox" && (langFilter==null||ips[i].lang==langFilter) && ips[i].checked)
			nn++;
	return nn;
}

// 返回checkbox中第一个选择的值
// 参数：
// langFilter 可选 通过<input type="checkbox" lang="abc"中的abc进行分组过滤
function getCheckedValue(langFilter){
	var ips=document.getElementsByTagName("input");
	for(var i = 0; i < ips.length; i++)
		if(ips[i].type=="checkbox" && (langFilter==null||ips[i].lang==langFilter) && ips[i].checked)
			return ips[i].value;
	return null;
}

// 返回checkbox中选择的所有值，用逗号,分隔返回
// 参数：
// langFilter 可选 通过<input type="checkbox" lang="abc"中的abc进行分组过滤
function getCheckedValues(langFilter){
	var ips=document.getElementsByTagName("input");
	var res="";
	for(i = 0; i < ips.length;i++)
		if(ips[i].type=="checkbox" && (langFilter==null||ips[i].lang==langFilter) && ips[i].checked)
			res=res+","+ips[i].value;
	return res.length>0?res.substring(1):res;
}

//
//    <select 控件相关的方法
// 

//设置select框的选中值
function setSelectedValue(selObj,value){
	for(i = 0; i < selObj.options.length;i++)
		selObj.options[i].selected=selObj.options[i].value==value;
}

//获取select框中被选中的Option对象
function getSelectedOption(selObj){
	return selObj.options[selObj.selectedIndex];	
}


//
//  公共的html相关方法 
//
//根据ID获取对象
function gi(id){
	return document.getElementById(id);
}

//根据Name获取对象
function gn(name){
	es=document.getElementsByName(name);
	return es!=null?es[0]:null;
}

//获取http数据
function getHttpData(url){
	try{
		var xmlhttp = new ActiveXObject("Msxml2.XMLHTTP.3.0")
		xmlhttp.open("GET", url, false);
		xmlhttp.send();
		return xmlhttp.responseText;
	}catch(e){
		return e;	
	}
}

//
// 此方法废弃，建议使用window.js.jsp中的openDialog方法
//
//打开模式窗口
//参数：
//uri 打开的页面的uri，以/开始，例如： /abc/d.do?goto=aaa
//width 窗口宽度
//heigth 窗口高度
//argsObj 传递参数的包装对象var argsObj=new Object(); argsObj.abc="dddd"
//dir 相对于/ 的路径，比如引用是在/abc/ddd/abc.htm中，则dir应该为../../两级目录，默认为../一级目录
//返回 argsObj包装对象
function showModelDialog(uri,width,height,argsObj,dir) {
	if(argsObj==null) argsObj=new Object();
	argsObj.modalDialogSrc = ".."+uri;
	argsObj.modalDialogWidth = width;
	argsObj.modalDialogHeight = height;
	argsObj.location=location;
	dir=dir==null?"../":dir;
	return window.showModalDialog(dir+"js/modelDialog.html",argsObj,"dialogHeight:"+height+"px;dialogWidth:"+width+"px;location=no;");
}

//新开窗口
function openWindow(url,x,y){
	x=x==null?"800":x;
	y=y==null?"600":y;
	window.open(url,"´Ԑ´°¿ی���","scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,top="+((screen.height-y)/2)+",left="+((screen.width-x)/2)+",width="+x+",height="+y);
}


//给table添加一行
// tabObj table对象
// celTemplate 列模板，用Array包装的列数据，其中的#index会被替换为行索引值
// 				例：两列的模板var testTemplate=new Array("第#index行","数据"); 
// countObj 记录总行数的隐藏控件对象，其中新增的  行号=countObj+1
// trPrefix 行标识字符串，每行的id格式为:  trPrefix_row_index
// startRow 开始计算序号的行号,第一行为0
// fieldIndex 需要计算下标的列号 第一列为0
//
// 返回: 增加的行对象
function addRow(tabObj,celTemplate,countObj,trPrefix,startRow,fieldIndex){
	row=tabObj.insertRow(-1);
	theIndex=parseInt(countObj.value)+1;
	row.id=((trPrefix!=null)?trPrefix:"")+"_row_"+theIndex;
	for(i=0;i<celTemplate.length;i++){
		
		{
			cel = row.insertCell(i);
    	cel.innerHTML = replaceAll(celTemplate[i],"#index",theIndex);   
    }
	}
	countObj.value=theIndex;
	//计算序号
	if(startRow!=null&&fieldIndex!=null)
		reInitIndex(tabObj,startRow,fieldIndex);
	return row;
}

//删除一行
function delRow(trObj,tableObj,startRow,fieldIndex){
	trObj.parentNode.removeChild(trObj);
	//计算序号
	if(tableObj!=null&&startRow!=null&&fieldIndex!=null)
		reInitIndex(tableObj,startRow,fieldIndex);
}

//计算表格的下标列
function reInitIndex(tableObj,startRow,fieldIndex){
	if(startRow==null) startRow=0;
	if(fieldIndex==null) fieldIndex=0;
	trs=tableObj.rows;
	for(x=1;startRow<trs.length;startRow++,x++){
		tds=trs[startRow].cells;
		if(tds.length>fieldIndex)
			tds[fieldIndex].innerHTML=x;
	}
}

//获取对象集合
// formObj form对象
// langFilter 在控件上的lang属性值
// 返回Array
function getObjArray(formObj,langFilter){
	var res=new Array();
	for(i = 0; i < formObj.elements.length;i++){
		obj = formObj.elements[i];
		if(obj.lang==langFilter)
			res[res.length]=obj;
	}
	return res;	
}

//标签方法
function clickTab(tabIds,boxIds,activeTabId){
	for(i=0;i<tabIds.length;i++){
		isActive=(activeTabId==tabIds[i]);
		if(gi(tabIds[i])==null || gi(boxIds[i])==null)
			alert("ȫ՚±뇩Р¹صètml֮º󶸓ô˷½·¨");
		gi(tabIds[i]).className=isActive?"tab_on":"tab";
		gi(boxIds[i]).style.display=isActive?"":"none";
	}
}


//选择公司的小窗口（不限定公司）
// nameObj （必须）名称对象
// idObj   （必须）id对象
// level   限定公司级别
// dir		相对路径，默认为../../
function company(nameObj,idObj,level,dir){
	var args=new Object();
	url="/common/index.do?goto=selectCompany2";
	if(level!=null)
		url=url+"&level="+level;
	if(dir==null)
		dir="../../";
	showModelDialog(url,400,300,args,dir);
	if(args.id!=null){
		nameObj.value=args.name;
		idObj.value=args.id;
	}
}