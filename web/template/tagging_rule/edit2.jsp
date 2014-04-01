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
<script type="text/javascript">
	$(function(){
		//refreshTags();	
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
<body>
<div class="ui_head"></div>



<form method="post" action="/GeneralHandleSvt" id="form1" name="form1">
<input type="hidden" id="reqType" name="reqType" value="tag.TagRuleActionHandler.add" />
<input type="hidden" id="id" name="tag$id" value="" />
<input type="hidden" id="tag_ids" name="tag_rule$tag_id" value="${obj.tag_id}" />
<input type="hidden" id="tid" name="tag_rule$id" value="${obj.id}" />
<table class="ui edit">
<tr class="title"><td colspan="2">修改自动Tag规则</td></tr>
    <tr>
        <td width="75">条件域：</td>
        <td>
        	<input type="radio" name="tag_rule$type" value="0" <c:if test="${obj.type=='0'}">checked="checked"</c:if> />来源URL &nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="radio" name="tag_rule$type" value="1" <c:if test="${obj.type=='1'}">checked="checked"</c:if> />正文或标题</td>
    </tr>
    <tr>
        <td>条件：</td>
        <td><input type="radio" name="tag_rule$conditions" value="or" <c:if test="${obj.conditions=='or' || obj.conditions==''}">checked="checked"</c:if> />or &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tag_rule$conditions" value="and" <c:if test="${obj.conditions=='and'}">checked="checked"</c:if> />and</td>
    </tr>
    <tr>
        <td>包含关键字：</td>
        <td><input type="text" name="tag_rule$keyword" value="${obj.keyword}" /></td>
    </tr>
    <tr>
        <td>博主uid：</td>
        <td><input type="text" name="tag_rule$weibo_uid" value="${obj.weibo_uid}"/>&nbsp;&nbsp;&nbsp;&nbsp;不受条件设置的影响,一律是or</td>
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
            	<m:checkbox name="${statux.index}$ur$urgent" value="${ur.id}" checkValue="${ur.checkValue}"/>${ur.name}
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
            	<m:checkbox name="${statux.index}$im$importance" value="${im.id}" checkValue="${im.checkValue}"/>${im.name}
            </c:forEach>
            <input type="hidden" name="tag_rule$importance" id="importance_hidden" value="${obj.importance}" />
        </td>-->
    </tr>  
    <tr>
    	<td></td>
        <td><input type="submit" value="提交" class="right-button08" />
        <input type="button" value="放弃保存" onclick="window.history.go(-1);" /></td>
    </tr>
</table>


</form>
</body>
</html>