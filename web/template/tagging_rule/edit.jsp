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
<tr class="title"><td colspan="2">新建自动Tag规则</td></tr>
    <tr>
        <td width="75">条件域：</td>
        <td><input type="radio" name="tag_rule$type" value="0" checked="checked" />来源URL &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tag_rule$type" value="1" />正文或标题</td>
    </tr>
    <tr>
        <td>条件：</td>
        <td><input type="radio" name="tag_rule$conditions" value="or" checked="checked" />or &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tag_rule$conditions" value="and" />and</td>
    </tr>
    <tr>
        <td>包含关键字：</td>
        <td><input type="text" name="tag_rule$keyword" /></td>
    </tr>
    <tr>
        <td>博主uid：</td>
        <td><input type="text" name="tag_rule$weibo_uid" />不受条件设置的影响,一律是or</td>
    </tr>
    <tr>
        <td>标签：</td>
        <td>
        <input type="hidden" id="tags" name="tag_rule$tag_name" />
        <ul id="tags_result" class="tags_result select_label"></ul>
        <c:set var="tagsList_pf" value="${tagsList}" />
		<%@ include file="/template/tag/tag_plugin.jsp"%>
        </td>
    </tr>
    <tr>
        <td>客户重要度：</td>
        <td><input type="radio" name="tag_rule$urgent" value="" <c:if test="${obj.urgent==null || obj.urgent==''}">checked="checked"</c:if>/>无<m:radio type="urgent" name="tag_rule$urgent" value="${obj.urgent}" /></td>
        <!--
        <td id="urgent_text">
        	<c:forEach var="ur" items="${urgentList}"  varStatus="status">
            	<m:checkbox name="${statux.index}$ur$urgent" value="${ur.id}" checkValue=""/>${ur.name}
            </c:forEach>
            <input type="hidden" name="tag_rule$urgent" id="urgent_hidden" value="${obj.urgent}" />
        </td>-->
    </tr>
    <tr>
        <td>GP重要度：</td>
        <td><input type="radio" name="tag_rule$importance" value="" <c:if test="${obj.importance==null || obj.importance==''}">checked="checked"</c:if>/>无<m:radio type="importance" name="tag_rule$importance" value="${obj.importance}" /></td>
        <!--
        <td id="importance_text">
        	<c:forEach var="im" items="${importanceList}"  varStatus="status">
            	<m:checkbox name="${statux.index}$im$importance" value="${im.id}" checkValue=""/>${im.name}
            </c:forEach>
            <input type="hidden" name="tag_rule$importance" id="importance_hidden" value="${obj.importance}" />
        </td>-->
    </tr>
    <tr>
    	<td></td>
        <td><input type="submit" value="创建" class="right-button08" /></td>
    </tr>
</table>

<table class="ui list" id="rules">
	<tr class="title"><td class="title" colspan="9">编辑TAG标签库</td></tr>
    <tr class="effect">
    	<th width="15">选择</th>
    	<th width="15">序号</th>
        <th width="10%">条件域</th>
        <th width="10%">条件</th>
        <th width="30%">包含关键字</th>
        <th width="11%">标签</th>
        <th width="10%">客户重要度</th>
        <th width="10%">GP重要度</th>
        <th width="8%">操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
    <tr class="effect">
        <td><input type="checkbox" value="${detail.id}" name="delid"/></td>
        <td>${status.index+1}</td>
        <td><c:if test="${detail.type=='0'}">来源URL</c:if><c:if test="${detail.type=='1'}">正文或标题</c:if></td>
        <td>${detail.conditions}</td>
        <td>${detail.keyword}</td>
        <td align="center">${detail.tag_name}</td>
        <td align="center"><m:out type="urgent" value="${detail.urgent}" /></td>
        <td align="center"><c:if test="${obj.importance==null || obj.importance==''}"></c:if><m:out type="importance" value="${detail.importance}" /></td>
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
		
		$("#importance_text [type='checkbox']").click(function(){
			var importance="";
			$("#importance_text :checked").each(function(){
				importance+=$(this).val()+",";
			})
			$("#importance_hidden").val(importance);
		})
		
		$("#urgent_text [type='checkbox']").click(function(){
			var urgent="";
			$("#urgent_text :checked").each(function(){
				urgent+=$(this).val()+",";
			})
			$("#urgent_hidden").val(urgent);
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