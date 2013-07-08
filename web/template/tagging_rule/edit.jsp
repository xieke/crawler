<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>
<!--日期控件 开始-->
<script type="text/javascript" src="/plugin/jquery.datepick.package-4.0.5/jquery.datepick.js"></script>
<link href="/plugin/jquery.datepick.package-4.0.5/jquery.datepick-adobe.css" rel="stylesheet" type="text/css" />
<!--日期控件 结束-->
<title>TAG</title>
</head>

<body>
<div class="ui_head"></div>



<form method="post" action="/GeneralHandleSvt" id="form1" name="form1">
<input type="hidden" id="reqType" name="reqType" value="tag.TagRuleActionHandler.add" />
<input type="hidden" id="id" name="tag$id" value="" />
<input type="hidden" id="tag_ids" name="tag_rule$tag_id" value="" />
<table class="ui edit">
<tr class="title"><td colspan="2">新建TAG标签</td></tr>
    <tr>
        <td width="75">条件域：</td>
        <td><input type="radio" name="tag_rule$type" value="0" checked="checked" />来源URL &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tag_rule$type" value="1" />正文或标题</td>
    </tr>
    <tr>
        <td>包含关键字：</td>
        <td><input type="text" name="tag_rule$keyword" /></td>
    </tr>
    <tr>
        <td>标签：</td>
        <td>
        
<input type="text" size="50" id="tags" name="tag_rule$tag_name" value="" />
<c:set var="tagsList_pf" value="${tagsList}" />
<%@ include file="/template/tag/tag_plugin.jsp"%>
        </td>
    </tr>
    <tr>
    	<td></td>
        <td><input type="submit" value="创建" class="right-button08" /></td>
    </tr>
</table>

<table class="ui list" id="rules">
	<tr class="title"><td class="title" colspan="6">编辑TAG标签库</td></tr>
    <tr class="effect">
    	<th width="15">选择</th>
    	<th width="15">序号</th>
        <th width="29%">条件域</th>
        <th width="30%">包含关键字</th>
        <th width="11%">标签</th>
        <th width="11%">操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
    <tr class="effect">
        <td><input type="checkbox" value="${detail.id}" name="delid"/></td>
        <td>${status.index+1}</td>
        <td><c:if test="${detail.type=='0'}">来源URL</c:if><c:if test="${detail.type=='1'}">正文或标题</c:if></td>
        <td>${detail.keyword}</td>
        <td align="center">${detail.tag_name}</td>
        <td align="center"><a href="/tag.TagRuleActionHandler.show?job=edit&objId=${detail.id}" onclick="return confirm('你确实要编辑吗?');"><img alt="点击编辑" src="/images/button_edit.png" /></a><a href="/tag.TagRuleActionHandler.delete?job=delete&id=${detail.id}" onclick="return confirm('你确实要删除吗?');"><img alt="点击删除" src="/images/del_icon2.gif" /></a></td>
    </tr>
</c:forEach>
	<tr class="edit">
    	<td colspan="6">
                <a class="select_all" href="javascript:void(0)">全选</a>/<a class="unselect_all" href="javascript:void(0)">反选</a>
<script type="text/javascript">
$(".select_all").click(function(){
	$("input[name='delid']").attr("checked",true);
});
$(".unselect_all").click(function(){
	$("input[name='delid']").attr("checked",false);
});
</script>
                <input type="button" class="button" id="dels" value="批量删除" />&nbsp;&nbsp;
                <input type="text" id="s_time" name="startDate" value="" readonly="readonly" plugin="date" start="start" />
				<span class="newfont06">-</span>
				<input type="text" id="e_time" name="endDate" value="" readonly="readonly" plugin="date" end="start" />
                <input type="button" class="button" id="do_tag" value="手动标签" />
				<input type="hidden"  id="tagRuleIds" name="tagRuleIds" value="手动标签" />
        </td>
    </tr>
</table>
<script type="text/javascript">
	$(function(){
		$("#dels").click(function(){
			if(confirm('你确实要删除吗?')){
				$("#reqType").attr("value","tag.TagRuleActionHandler.deletes");
				$("#form1").submit();
			}
		});
		
		$("#do_tag").click(function(){
			var startDate = $("#s_time").val();
			var endDate = $("#e_time").val();
			if(startDate==null || startDate=='') {
				alert("请选择开始时间,目前只支持两天之内的手动标签!");
				return false;	
			}
			if(endDate==null || endDate=='') {
				alert("请选择结束时间,目前只支持两天之内的手动标签!");
				return false;	
			}
			
			var tagRuleIds = "";
			$("#rules :checked").each(function(){
				tagRuleIds += $(this).attr("value")+",";
			});
			$("#tagRuleIds").val(tagRuleIds);
			$("#reqType").attr("value","news.NewsActionHandler.doHitting");
			$("#form1").submit();
		})

	})
</script>

<div class="pages_bar">
<div class="pages_left">共 <span class="orange">${pageVariable.totalpage}</span> 页 | 第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="tag.TagRuleActionHandler.list" size="30" /></div>
</div>
</form>
</body>
</html>