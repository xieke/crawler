/*
*
*tree数据结构说明：
*　　与树相对应的数据结构为一多组数组　
*　　数组对应位意义为：
*　　　a [0] =树结点显示的名称　
*      a [1] =链接的目标地址 
*      a [2] =目标窗口名称
*      a [3] =服务器传来的参数，其结构为二维数组．a[3][0][0]固定为entity的名字，a[3][1][0]为field名称，a[3][1][1]为field的值
*      a [4] =服务器传来的树结点的图标．
*      a [5] =checkbox的标志位．0为不选中，1为选中，2为中间态．
*       
*
*/
var close_icon='/pageComponent/resources/images/t_plus.gif'
var open_icon='/pageComponent/resources/images/t_minus.gif';
var check_1="/pageComponent/resources/images/t_check1.gif"
var check_2="/pageComponent/resources/images/t_check2.gif"
function createTree(tree1,id){
	if (tree1.length!=start) {
		if (id.indexOf("[")==-1) {
			document.write("<IMG id='"+id+"plus' valign='top' border='0' width=16 src="+open_icon+"  onclick='show(this)'><IMG valign='top' border='0'  src="+tree1[4]+"><INPUT id='"+id+"'  name='treemenu_parent' class='PG_STREE_T_CHECKBOX'  readonly  type='text'  onfocus='nodechicked(eval(this.id),this.id)'><SPAN valign='top' id='"+id+"text' onclick='show1(this);'onmouseover='mouse_over(this)' onmouseout='mouse_out(this)' class='PG_STREE_ITEM'><a href='"+tree1[1]+"'onclick='if(this.nameProp==\"eos_no_action\") return false;' target='"+tree1[2]+"' class='PG_STREE_LINK' >"+tree1[0]+"</a></span><DIV nowrap style='margin-left:16;'><SPAN  id='"+id+"sub'  onclick=''>");
			
		} else{
			document.write("<IMG id='"+id+"plus' valign='top' border='0' width=16 src="+close_icon+"  onclick='show(this)'><IMG valign='top' border='0' src="+tree1[4]+"><INPUT id='"+id+"'  name='treemenu_parent' class='PG_STREE_T_CHECKBOX'  readonly  type='text'  onfocus='nodechicked(eval(this.id),this.id)'><SPAN  id='"+id+"text' onclick='show1(this);'onmouseover='mouse_over(this)' onmouseout='mouse_out(this)' class='PG_STREE_ITEM'><a href='"+tree1[1]+"' onclick='if(this.nameProp==\"eos_no_action\") return false;' target='"+tree1[2]+"' class='PG_STREE_LINK' >"+tree1[0]+"</a></span><DIV nowrap style='margin-left:16;'><SPAN  id='"+id+"sub' style='display:none' onclick=''>");
			
		}
		for (var i=start;i<tree1.length;i++) {
			createTree(tree1[i],id+"["+i+"]");
		}
		document.write("</span></div>");
	} else{
		document.write( "<DIV nowrap ><IMG valign='top' border='0' src="+tree1[4]+"><INPUT id='"+id+"' name='treemenu_checkbox'  readonly type='checkbox' onclick='nodechicked(eval(this.id),this.id)'  ><SPAN  id='"+id+"text' onclick='show1(this);'onmouseover='mouse_over(this)' onmouseout='mouse_out(this)' class='PG_STREE_ITEM' ><a href='"+tree1[1]+"' onclick='if(this.nameProp==\"eos_no_action\") return false;' target='"+tree1[2]+"'class='PG_STREE_LINK' >"+tree1[0]+"</a></span></div>");
		
	}
}
function createTreeNotCheck(tree1,id){
	if (tree1.length!=start) {
		if (id.indexOf("[")==-1) {
			document.write("<IMG id='"+id+"plus' valign='top' border='0' width=16 src="+open_icon+" onclick='show(this)'><IMG valign='top' border='0'  src="+tree1[4]+"><SPAN  id='"+id+"text' onclick='show1(this);'onmouseover='mouse_over(this)' onmouseout='mouse_out(this)' class='PG_STREE_ITEM'><a href='"+tree1[1]+"' onclick='if(this.nameProp==\"eos_no_action\") return false;' target='"+tree1[2]+"'class='PG_STREE_LINK' >"+tree1[0]+"</a></span><DIV nowrap style='margin-left:16;'><SPAN  id='"+id+"sub' onclick=''>")
			
		} else{
			document.write("<IMG id='"+id+"plus' valign='top' border='0' width=16 src="+close_icon+" onclick='show(this)'><IMG valign='top' border='0'  src="+tree1[4]+"><SPAN  id='"+id+"text' onclick='show1(this);'onmouseover='mouse_over(this)' onmouseout='mouse_out(this)' class='PG_STREE_ITEM'><a href='"+tree1[1]+"' onclick='if(this.nameProp==\"eos_no_action\") return false;' target='"+tree1[2]+"'class='PG_STREE_LINK' >"+tree1[0]+"</a></span><DIV nowrap style='margin-left:16;'><SPAN  id='"+id+"sub' style='display:none' onclick=''>")
			
		}
		for (var i=start;i<tree1.length;i++) {
			createTreeNotCheck(tree1[i],id+"["+i+"]");
		}
		document.write("</span></div>");
	} else{
		document.write( "<DIV nowrap ><IMG valign='top' border='0'  src="+tree1[4]+"><SPAN  id='"+id+"text' onclick='show1(this);' onmouseover='mouse_over(this)' onmouseout='mouse_out(this)' class='PG_STREE_ITEM'><a href='"+tree1[1]+"' onclick='if(this.nameProp==\"eos_no_action\") return false;' target='"+tree1[2]+"'class='PG_STREE_LINK' >"+tree1[0]+"</a></span></div>");
		//document.write( "<DIV nowrap > ");
		//document.write("<IMG valign='top' border='0'  src="+tree1[4]+"><SPAN  id='"+id+"text' onclick='show1(this);' onmouseover='mouse_over(this)' onmouseout='mouse_out(this)' class='stree_item'><a href='"+tree1[1]+"' target='"+tree1[2]+"'class='PG_STREE_LINK' onmouseover='return true'>"+tree1[0]+"</a></span>");
		//document.write("</div>");
	}
}
function show(s){
	event.cancelBubble=true;
	if (document.all(s.id.substr(0,s.id.length-4)+"sub").style.display=="none") {
		document.all(s.id.substr(0,s.id.length-4)+"sub").style.display=""
		document.all(s.id).src=open_icon;
	} else{
		document.all(s.id.substr(0,s.id.length-4)+"sub").style.display="none";
		document.all(s.id).src=close_icon;
	}
}
function show1(s1){
	event.cancelBubble=true;
}
function getUrlField(a){
	return a[1];
}
function nodechicked(tree1,id){
	if (document.all(id).type=="text") 
		document.all(id).blur();
	
	if (tree1[start-1]==0) {
		allsonchecked(tree1,id);
	} else{
		allsonnotchecked(tree1,id);
	}
	if (id.indexOf("[")!=-1) 
		processfatherstate( eval(id.substr(0,id.lastIndexOf("["))),id.substr(0,id.lastIndexOf("[")));
}
function allsonchecked(tree,id){
	if (tree.length!=start) {
		tree[start-1]=1;
		document.all(id).style.backgroundImage="url("+check_2+")";
		for (var i=start;i<tree.length;i++) 
			allsonchecked(tree[i],id+"["+i+"]");
	} else{
		tree[start-1]=1;
		document.all(id).checked=true;
	}
}
function allsonnotchecked(tree2,id){
	if (tree2.length!=start) {
		tree2[start-1]=0;
		document.all(id).style.backgroundImage="";
		for (var i=start;i<tree2.length;i++) {
			allsonnotchecked(tree2[i],id+"["+i+"]");
		}
	} else{
		tree2[start-1]=0;
		document.all(id).checked=false;
	}
}
function processfatherstate(tree2,id){
	for (var i=start;i<tree2.length;i++) {
		if (tree2[i][start-1]==2) {
			tree2[start-1]=2;
			document.all(id).style.backgroundImage='url('+check_1+')';
			if (id.indexOf("[")!=-1) 
				processfatherstate(eval(id.substr(0,id.lastIndexOf("["))),id.substr(0,id.lastIndexOf("[")));
			return;
		}
		if (tree2[start][start-1]!=tree2[i][start-1]) {
			tree2[start-1]=2;
			document.all(id).style.backgroundImage='url('+check_1+')';
			if (id.indexOf("[")!=-1) 
				processfatherstate(eval(id.substr(0,id.lastIndexOf("["))),id.substr(0,id.lastIndexOf("[")));
			return;
		}
	}
	tree2[start-1]=tree2[start][start-1];
	if (tree2[start-1]==0) 
		document.all(id).style.backgroundImage='';
	else
		document.all(id).style.backgroundImage='url('+check_2+')';

	if (id.indexOf("[")!=-1) 
		processfatherstate(eval(id.substr(0,id.lastIndexOf("["))),id.substr(0,id.lastIndexOf("[")));
	return
}
function mouse_over(o){
	o.className="PG_STREE_ITEM_MOUSEOVER";
}
function mouse_out(o){
	o.className="PG_STREE_ITEM";
}
function getCheckedNodes(tree1,key){
	var select=new Array();
	getAllChecked(tree1,select,key);
	return select;
}
function getAllChecked(tree1,select,key){
	if (tree1[3].length!=0) 
		if(tree1[3][0].length!=0)
	if (tree1[3][0][0]==key) {
		if (tree1[start-1]==1) 
			select[select.length]=tree1;
		return ;
	}
	for (var i=start;i<tree1.length;i++) {
		getAllChecked(tree1[i],select,key);
	}
}
function getNodeValue(tree1,key){
	for (var i=0;i<tree1[3].length;i++) {
		if (tree1[3][i][0]==key) 
			return tree1[3][i][1];
	}
	return null;
}
function getAllCheckedList(treeid,mutiltable){
	var tree_array=new Array();
	var str="<?xml version='1.0' encoding='GB2312'?><root>";
	if(mutiltable==true)
	tree_getAllCheckedList(treeid,tree_array,0);
	else
	tree_getAllCheckedList_single(treeid,tree_array,0);

		
	for (var i=0;i<tree_array.length;i++) {
		str=str+"<list type='"+tree_array[i][0]+"'>";
		for (var j=1;j<tree_array[i].length;j++) 
			str=str+"<"+tree_array[i][0]+">"+tree_array[i][j]+"</"+tree_array[i][0]+">";
		str=str+"</list>";
	}
	str=str+"</root>";
	if(str=="<?xml version='1.0' encoding='GB2312'?><root></root>") str="<?xml version='1.0' encoding='GB2312'?><root><list/></root>";
	return str;
}
function tree_getAllCheckedList(treeid,tree_array,layer){
	for ( var i=start;i<treeid.length;i++) {
		var tmp_tree=treeid[i]
		if (tmp_tree[start-1]!=0) {
			if (!tree_array[layer]) {
				tree_array[layer]=new Array();
				// alert("laryer"+layer);
				tree_array[layer][0]=tmp_tree[3][0][0];
			}
			var tmp_str="";
			for (var j=1;j<tmp_tree[3].length;j++) {
				tmp_str=tmp_str+"<"+tmp_tree[3][j][0]+">"+tmp_tree[3][j][1]+"</"+tmp_tree[3][j][0]+">";
			}
			tree_array[layer][tree_array[layer].length]=tmp_str;
		}
		tree_getAllCheckedList(tmp_tree,tree_array,layer+1);
	}
}

function tree_getAllCheckedList_single(treeid,tree_array,layer){
	for ( var i=start;i<treeid.length;i++) {

		var tmp_tree=treeid[i]
        var curr_layer=-1;
		if (tmp_tree[start-1]!=0) {
		

			for(var j=0;j<tree_array.length;j++)
		  {
				
				
				if (tree_array[j][0][0] == tmp_tree[3][0][0] )
                   {
					 curr_layer=j;
				      break;
				  }
				  }

			   
                    
              	if (curr_layer==-1) 
					{	curr_layer=tree_array.length;
						tree_array[tree_array.length]=new Array();

				// alert("laryer"+layer);
				
				tree_array[layer][0]=tmp_tree[3][0][0];
				
				}

			 
		    	var tmp_str="";
			 

			for (var j=1;j<tmp_tree[3].length;j++) {
			    
				tmp_str=tmp_str+"<"+tmp_tree[3][j][0]+">"+tmp_tree[3][j][1]+"</"+tmp_tree[3][j][0]+">";
			}
			
			tree_array[curr_layer][tree_array[curr_layer].length]=tmp_str;
			
			
			
		   }
			tree_getAllCheckedList_single(tmp_tree,tree_array,layer+1);
	     }
		
	}


function getCheckedList(treeid,layerName){
	var tree_array=new Array();
	tree_array[0]=new Array();
	var str="<?xml version='1.0' encoding='GB2312'?><root>";
	tree_getCheckedList(treeid,tree_array,layerName)
	if (!tree_array[0][0]) 
		str=str+"<list type='"+layerName+"'>";
	else
		str=str+"<list type='"+tree_array[0][0]+"'>";

	for (var j=1;j<tree_array[0].length;j++) 
		str=str+"<"+tree_array[0][0]+">"+tree_array[0][j]+"</"+tree_array[0][0]+">";
	str=str+"</list>";
	str=str+"</root>";
	if(str=="<?xml version='1.0' encoding='GB2312'?><root></root>") str="<?xml version='1.0' encoding='GB2312'?><root><list/></root>";
	return str;
}
function tree_getCheckedList(treeid,tree_array,layerName){
	for ( var i=start;i<treeid.length;i++) {
		var tmp_tree=treeid[i]
		if (tmp_tree[start-1]!=0) 
			if(tmp_tree[3][0][0]==layerName){
		tree_array[0][0]=tmp_tree[3][0][0];
		var tmp_str="";
		for (var j=1;j<tmp_tree[3].length;j++) {
			tmp_str=tmp_str+"<"+tmp_tree[3][j][0]+">"+tmp_tree[3][j][1]+"</"+tmp_tree[3][j][0]+">";
		}
		tree_array[0][tree_array[0].length]=tmp_str;
	}
	tree_getCheckedList(tmp_tree,tree_array,layerName);
}

}

function initCheckbox(tree1,id)
{
  var nodestatus=3;
  if(tree1.length!=start)
	{
	  for (var i=start;i<tree1.length;i++) {
			
			var status=initCheckbox(tree1[i],id+"["+i+"]");
            
　          
		 if(nodestatus==3)
			      nodestatus=status;
             else
		  {
				
			  if(status==2)
	    	       {
				nodestatus=2;
					}
		   else
     	         {    
          	  if(nodestatus!=status)
				  nodestatus=2;
                 }
		  }
		  
	  }
      
		
		if(nodestatus==1) 
		{document.all(id).style.backgroundImage='url('+check_2+')';
	   　　
		}
	   if(nodestatus==2)
　　　　　　{document.all(id).style.backgroundImage='url('+check_1+')';}　　　
          tree1[start-1]=nodestatus;　
		   return nodestatus;
			}
  else
		{
 
       if(tree1[start-1]==1)
			{
			 document.all(id).checked=true;
	         return 1;
			 }
			 else
             return 0;


	    }

}

