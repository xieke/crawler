<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="/theme/adobe/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/plugin/jquery.ztree-3.1/css/demo.css" type="text/css">
<link rel="stylesheet" href="/plugin/jquery.ztree-3.1/css/zTreeStyle/zTreeStyle.css" type="text/css">

<script type="text/javascript" src="/plugin/jquery.ztree-3.1/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="/plugin/jquery.ztree-3.1/js/jquery.ztree.core-3.1.js"></script>
<script type="text/javascript" src="/plugin/jquery.ztree-3.1/js/jquery.ztree.excheck-3.1.js"></script>
<script type="text/javascript" src="/plugin/jquery.ztree-3.1/js/jquery.ztree.exedit-3.1.js"></script>

<!--日期控件 开始-->
<script type="text/javascript" src="/plugin/jquery.datepick.package-4.0.5/jquery.datepick.js"></script>
<link href="/plugin/jquery.datepick.package-4.0.5/jquery.datepick-adobe.css" rel="stylesheet" type="text/css" />
<!--日期控件 结束-->

<!--表格表头悬浮控件 开始-->
<script type="text/javascript" src="/plugin/jquery.fixedtableheader-1.0.2.min.js"></script>
<!--表格表头悬浮控件 结束-->
<script type="text/javascript" src="/plugin/jquery.selectsearch-4.0/jquery.selectseach.min.js"></script>

<title>无标题文档</title>

<script type="text/javascript">

var setting = {
			async: {
				enable: true,
				url:"/basic.TagActionHandler.openTag",
				autoParam:["id=pid", "name=n", "level=lv"],
				otherParam:{"otherParam":"zTreeAsyncTest"},
				dataFilter: filter
			},
			check: {
				enable: true,
				chkStyle: "radio",
				radioType: "all"
			},
			view: {
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onClick,
				onCheck: onCheck
			}
		};

		function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				childNodes[i].name = childNodes[i].dataObject.name.replace(/\.n/g, '.');
				childNodes[i].isParent = childNodes[i].userObject.isGroup;
			}
			return childNodes;
		}
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.checkNode(treeNode, !treeNode.checked, null, true);
			return false;
		}

		function onCheck(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getCheckedNodes(true),
			v = "";
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			var cityObj = $("#category_id");
			cityObj.attr("value", v);
		}

		function showMenu() {
			var cityObj = $("#category_id");
			var cityOffset = $("#category_id").offset();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$("body").bind("mousedown", onBodyDown);
		}
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "category_id" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}

		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting);
		});
</script>

</head>

<body>
<!--
<div class="head">系统财务记录信息</div>
-->

<form action="GeneralHandleSvt" method="post" name="form1" id="form1">
	<input type="hidden" id="reqType" name="reqType" value="news.NewsActionHandler.list">
	

  <table border="1" cellspacing="0" cellpadding="0" class="ui" >
    <tr>
      <th colspan="6">搜索信息</th>
    </tr>
    <tr>
		<td class="oz_l">标题：</td>
        <td class="oz_r"><input type="text" id="title" name="news$title" value="${param.news$title}" /></td>
        <td class="oz_l">作者：</td>
        <td class="oz_r"><input type="text" id="author" name="news$author" value="${param.news$author}" /></td>
        <td class="oz_l">分类：</td>
        <td class="oz_r"><input type="hidden" id="category_id" name="news$category_id" value="${param.news$category_id}" onclick="showMenu();" otherhtml="m='search'" /></td>
    </tr>
    <tr>
    	<td class="oz_l">状态：</td>
        <td class="oz_r"><m:select type="product" id="status" name="news$status" value="${param.news$status}" otherhtml="m='search'" /></td>
        <td class="oz_l">计划开始时间段：</td>
        <td class="oz_r" colspan="3"><input type="text" id="s_time" name="startDate" value="${param.startDate}" readonly="readonly" plugin="date" start="start" />-<input type="text" id="e_time" name="endDate" value="${param.endDate}" readonly="readonly" plugin="date" end="start" /><input type="hidden" name="dateColumn" value="time" /></td>
	</tr>
    <tr>
      <td colspan="6" class="op"><input type="submit" value="查询" /><input type="button" id="resetb" value="重填" /></td>
    </tr>
  </table>

<table border="1" cellspacing="0" cellpadding="0" class="ui effect">
  <tr>
    <th>序号</th>
    <th>标题</th>
    <th>作者</th>
    <th>时间</th>
    <th>分类</th>
    <th>状态</th>
    <th>操作</th>
  </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr>
  	<td>${status.index+1}</td>
    <td>${detail.title}</td>
    <td>${detail.author}</td>
    <td>${detail.time}</td>
    <td>${detail.category_id.name}</td>
    <td>${detail.status}</td>
    <td><a href="/news.NewsActionHandler.show?objId=${detail.id}" >处理</a><a href="/news.NewsActionHandler.showHis?objId=${detail.his_news_id.id}" >原文</a></td>
  </tr>
</c:forEach>
</table>
<m:page action="news.NewsActionHandler.list" size="30" />
<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
<ul id="treeDemo" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
</form>
</body>
</html>
