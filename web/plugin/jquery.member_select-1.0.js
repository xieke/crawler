/*
 * JQuery tag Select core 1.0
 *
 * Copyright (c) 2012 Suliangben (ben)
 *
 * Licensed same as jquery - MIT License
 * http://www.opensource.org/licenses/mit-license.php
 *
 * email: wzcom@126.com
 * Date: 2012-03-10
 */
 var tag_select_fn;
 var tag_select_html="<div id=\"tag_select_dialog\" style=\"display:none\" title=\"Tag标签库\"><div class=\"tag_select_left\"><div class=\"tag_radio_button\"><a href=\"javascript:void(0)\" class=\"select\" index=\"0\">标签树</a></div></div><div class=\"tag_select_right\"><p>已选择：</p><div class=\"tag_select_plugin_list select_label\"></div><button id=\"tag_select_ok\">完成选择</button></div></div>";
 
 
 
function tag_select(fn){
	tag_select_fn=fn;
	if($("#tag_select_dialog").length==0){//首次初始
		$("body").append(tag_select_html);//置入html
		$( "#tag_select_dialog" ).dialog({//初始对话框
				modal:true,
				resizable:false,
				width:700,
				height:400,
				create: function(event, ui){
						$.fn.zTree.init($("#tag_tree"), setting);//初始组织架构树
						$("div.tag_radio_button a").click(function(){//初始绑定组织架构和高级搜索切换按钮点击事件
							$("div.tag_radio_button a").removeClass("select");
							$(this).addClass("select");
							$("div.tag_select_search").hide().eq($(this).attr("index")).show();
							if($(this).attr("index")=='0'){//处理超时后重新点击切换回组织架构树时，节点为空则重新加载
								if($.fn.zTree.getZTreeObj("tag_tree").getNodes.length==0)//若树的节点为0，则重载（可能因超时）
								$.fn.zTree.getZTreeObj("tag_tree").reAsyncChildNodes(null, "refresh");//异步重载所有节点
							}
						});
						
						$( "#tag_select_ok" ).button().click(function(){//初始完成选择按钮
							$.fn.zTree.getZTreeObj("tag_tree").reAsyncChildNodes(null, "refresh");//异步重载所有节点
							$("#tag_select_dialog").dialog( "close" );//关闭选择人员窗口
							if($("div.tag_select_plugin_list a.mb").length==0)return;//未选择人员直接关闭
							var json="[";//构建json
							$("div.tag_select_plugin_list a.mb").each(function(){
								json+="{uid:'"+$(this).attr("uid")+"',truename:'"+$(this).attr("truename")+"',gid:'"+$(this).attr("groupid")+"',group:'"+$(this).attr("group")+"'},"
							});
							json=json.substring(0,json.length-1)+"]";
							json=eval(json);
							eval(fn+"(json)");//将josn做为参数并回调函数
							$("div.tag_select_plugin_list a.mb").remove();//清空已选择人员
							$("tr.tag_search_value").detach();//清空高级搜索列表
							$("table.tag_search_table tr").after("<tr class='tag_search_value'><td colspan='3' height='30' style='color:#CCC'>可以按姓名或所在部门进行搜索！</td></tr>");
						});
				},
				close: function(event, ui){
							//if($.fn.zTree.getZTreeObj("tag_tree").getNodes.length==0)//打开窗口后若树的节点为0，则重载（可能因超时）
							//	$.fn.zTree.getZTreeObj("tag_tree").reAsyncChildNodes(null, "refresh");//异步重载所有节点
							//else{
								//$.fn.zTree.getZTreeObj("tag_tree").checkAllNodes(false);//取消选中组织架构树
								//$.fn.zTree.getZTreeObj("tag_tree").expandAll(false);//收缩组织架构树
							//}
							
				}
		});
	}else{//重新打开选人
		$("#tag_select_dialog").dialog( "open" );
	}
}


// 选择人员控件 - 删除结点函数
function tag_del_parent_node(obj){
	$(obj).parent().fadeOut(function(){$(this).remove();})
	$("tr.tag_search_value input[uid='"+$(obj).parent().attr("uid")+"']").attr("checked",false);
}
//选择人员管件 - 添加或删除选择
function tag_add_or_remove_select(is_add,uid,truename,groupid,group){
	if(is_add){
		//if($("a[uid='"+uid+"']").length==0){
			$("div.tag_select_plugin_list").append("<a style='display:none' truename='"+truename+"' uid='"+uid+"' group='"+group+"' groupid='"+groupid+"' href='#' class='mb' title='"+group+"'>"+truename+"<span class='del' onclick='tag_del_parent_node(this)'></span></a>");
			$("a[uid='"+uid+"']").fadeIn();
		//}
	}else if(!is_add){
		$("a[uid='"+uid+"'] span.del").click();
	}
}
//选择人员控件 - 高级查询并刷新列表
function tag_search_from(){
	var keywords=$("#tag_search_form_keyword").val();
	$.post("/basic.UserActionHandler.queryUserOrDept",{name:keywords,type:type}, function(data) {
			if(data=="expired"){
				alert("登录超时，请登录后进行操作！");
				reutrn;
			}
			var json=eval(data);
			var exprot_html="";
			$.each(json, function(i, field){
				if($("div.tag_select_plugin_list a[uid='"+field.userid+"']").length==0)this_checked=""; else this_checked="checked";
				exprot_html+="<tr class='tag_search_value'><td><input "+this_checked+" uid='"+field.userid+"' type='checkbox' onclick='tag_add_or_remove_select(this.checked,&quot;"+field.userid+"&quot;,&quot;"+field.username+"&quot;,&quot;"+field.deptid+"&quot;,&quot;"+field.deptname+"&quot;)' /></td><td>"+field.username+"</td><td>"+field.deptname+"</td></tr>";
			});
			$("tr.tag_search_value").detach();
			if(exprot_html==""){
				$("table.tag_search_table tr").after("<tr class='tag_search_value'><td colspan='3' height='30' style='color:#CCC'>没有查到记录，请更改搜索条件后重试！</td></tr>");
			}else{
				$("table.tag_search_table tr").after(exprot_html);
			}
	});
	
}


		//选择人员控件 - 组织架构树配置
		var setting = {
			async: {
				enable: true,
				url:"/basic.DeptAH.deptAndUserTree",
				autoParam:["id", "name=n", "level=lv"],
				otherParam:{"otherParam":"zTreeAsyncTest"},
				dataFilter: filter
			},
			check: {
				enable: true
			},
			callback: {
				onCheck: onCheck
			}
		};
		//选择人员控件 - 组织架构树 - 数据过滤处理
		function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
			return childNodes;
		}
		function onCheck(e, treeId, treeNode) {
			var trees=$.fn.zTree.getZTreeObj(treeId);
			//alert( treeNode.getParentNode().id+"____"+treeNode.getParentNode().name+"_____"+treeNode.id+"____"+treeNode.name );
			var nodes_select=trees.getChangeCheckedNodes(true);
			var nodes_length=nodes_select.length;
			for(var i=0;i<nodes_length;i++){
				if(nodes_select[i].isParent==false)
				//循环选择插入
				tag_add_or_remove_select(
						nodes_select[i].checked,
						nodes_select[i].id,
						nodes_select[i].name,
						nodes_select[i].getParentNode().id,
						nodes_select[i].getParentNode().name
					);
			}
			clearCheckedOldNodes(treeId);
		}
		function clearCheckedOldNodes(treeId) {
			var zTree = $.fn.zTree.getZTreeObj(treeId),
			nodes = zTree.getChangeCheckedNodes();
			for (var i=0, l=nodes.length; i<l; i++) {
				nodes[i].checkedOld = nodes[i].checked;
			}
		}
