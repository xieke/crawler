/**使用方法：1、子表tbody id="tbline"
*2、隐藏表使用tbody id=tbhidden
*3、tr 加事件 onKeyDown="processLine(this);" onclick="selectRow(this)"
*4、调用initCurrent()初始化当前选择行；
*5、输入元素紧跟td
*/
function initCurrent(){
		var currentRow=null;
		if(tbline.rows.length>0){			
			selectEdit(tbline.rows[0]);
		}
	}
function selectEdit(otr){
	if(typeof(currentRow)!="undefined"&&currentRow!=null){
		currentRow.className="fb_add_content";
	}
	otr.className="boss_result_content";
	currentRow=otr;	
	var ccells=otr.cells;
	for(var i=0;i<ccells.length;i++){
		//alert(ccells[i].firstChild.type+ccells[i].firstChild.readOnly);
		if((ccells[i].firstChild.type=="text"
		||ccells[i].firstChild.type=="textarea"
		||ccells[i].firstChild.type=="select-one"
		||ccells[i].firstChild.type=="select-multiple"
		||ccells[i].firstChild.type=="file"
		||ccells[i].firstChild.type=="textArea")
		&&(ccells[i].firstChild.readOnly!=true)){	
			ccells[i].firstChild.setActive();		
			ccells[i].firstChild.focus();			
			break;
		}
	}
}		
function processLine(obj){		
	var iKeyCode = window.event.keyCode;
	//alert(iKeyCode);		
	if(window.event.ctrlKey==true&&iKeyCode == 13) { //回车
		addTr();
		return false;
	}
	else if(window.event.ctrlKey==true&&iKeyCode == 46){ //删除一行(按DEL)			
		deleteTr(obj);
		return false;
	}
} 
function selectRow(obj){
	currentRow=obj;		
}
function addRow(){
	addTr();		
}
function deleteRow(){
	if(currentRow!=null&&tbline.rows.length>0){
		deleteTr(currentRow);
	}
}
function addTr(){
	var otr=tbhidden.rows[0];	
	var itr=otr.cloneNode(true);
	tbline.appendChild(itr);
	var crow=tbline.rows.length-1;
	selectEdit(tbline.rows[crow]);
}
//
function deleteTr(otr){
	if(!confirm("你确实要删除这一行吗？")){
		return;
	}
	var otbody = otr.parentNode;
	var lastrow=tbline.rows.length-2;
	for(var i=0;i<tbline.rows.length-1;i++){
		if(otr==tbline.rows[i]){
			lastrow=i;
			break;
		}
	}		
	otbody.removeChild(otr);		
	if(tbline.rows.length==0){
		currentRow=null;
	}else{
		selectEdit(tbline.rows[lastrow]);
	}
	
}
//多行日期控件
function calendar_add(obj,format){
	var txt=obj.parentNode.firstChild;
	calendar(txt,format);
}