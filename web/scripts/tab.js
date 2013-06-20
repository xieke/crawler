function  setstyletab(id,curi){
	var tab;
	var ele;
	for (i=0;i<30;i++)
	{
		tab = id+"_"+i+"_A";
		ele=document.getElementById(tab);
		if (ele != null)	
		ele.style.color = "white";
	}
		tab = id+"_"+curi+"_A";
		ele=document.getElementById(tab);
		if (ele != null)	
		ele.style.color = "black";

}
function  setstyletabSelect(id){
	var tab;
	var ele;

		tab = id+"_A";
		ele=document.getElementById(tab);
		if (ele != null)	
		ele.style.color = "black";

}
function  setstyletabNonSelect(id){
	var tab;
	var ele;
	for (i=0;i<30;i++)
	{
		tab = id+"_"+i+"_A";
		ele=document.getElementById(tab);
		if (ele != null)	
		ele.style.color = "white";
	}

}


function setstyle(id,curi,allleng){	
	var tab;
	var ele;
	for (i=0;i<allleng;i++)
	{
		tab = id+"_"+i+"_A";
		ele=document.getElementById(tab);
		if (ele != null)	
		ele.style.color = "white";
	}
		tab = id+"_"+curi+"_A";
		ele=document.getElementById(tab);
		if (ele != null)	
		ele.style.color = "black";
}

function setActivePane(ele){	
	ele.style.visibility = "visible";
	ele.style.position = "static";	
	ele.selected = "true";
	activeChildren(ele,true);
}

function deActivePane(ele){	
	ele.style.visibility = "hidden";
	ele.style.position = "absolute";
	ele.selected = "false";
	activeChildren(ele,false);
}

function activeChildren(ele,visible){
	for(var i=0;i<ele.children.length;i++){
		var curChildren=ele.children[i];

		if( curChildren.tagName=="div" || curChildren.tagName=="DIV" ){			
			if( curChildren.id && curChildren.selected=="true"){
				if( visible==true ) {
					curChildren.style.visibility = "visible";
					curChildren.style.position = "static";
				}
				else{
					curChildren.style.visibility = "hidden";
					curChildren.style.position = "absolute";
				}
				
			}
			
		
			if( curChildren.children.length >0 ) activeChildren(curChildren,visible);
		}
	}
}

function setActiveTab(cell){
	setCellBackground(cell,active_img);

	var row=getRowByCell(cell);
	
	var prev=getCellByIndex(row,cell.cellIndex-1);	

	var next=getCellByIndex(row,cell.cellIndex+1);

	
	if( cellIsFirstTab(cell) )
		setCellChildImgSrc(prev,0,active_start_img);
	else
		setCellChildImgSrc(prev,0,active_left_img);
	
	if( cellIsLastTab(cell) )
		setCellChildImgSrc(next,0,active_end_img);
	else
		setCellChildImgSrc(next,0,active_right_img);
}

function deActiveTab(cell){
	setCellBackground(cell,deactive_img);

	var row=getRowByCell(cell);
	
	var prev=getCellByIndex(row,cell.cellIndex-1);	
	var next=getCellByIndex(row,cell.cellIndex+1);
		
	
	if( cellIsFirstTab(cell) )
		setCellChildImgSrc(prev,0,deactive_start_img);
	else
		setCellChildImgSrc(prev,0,deactive_left_img);
	
	if( cellIsLastTab(cell) )
		setCellChildImgSrc(next,0,deactive_end_img);
	else
		setCellChildImgSrc(next,0,deactive_right_img);	
}

function getRowByCell(cell){
	return cell.parentElement;
}

function getCellByIndex(row,index){
	return row.cells[index];
}

function setCellBackground(cell,img_url){
	cell.background=img_url;	
}

function setCellChildImgSrc(cell,child_index,img_url){
	if( cell.children.length>child_index ){
		cell.children[child_index].src = img_url;
	}
}

function cellIsLastTab(cell){
	var row = getRowByCell(cell);
	var _cell = getCellByIndex(row,cell.cellIndex+2);
	if( _cell.id=="last" ) return true;
	else return false;
}

function cellIsFirstTab(cell){
	var row = getRowByCell(cell);
	var _cell = getCellByIndex(row,cell.cellIndex-2);
	if( !_cell ) return true;
	
	else return false;	
}
