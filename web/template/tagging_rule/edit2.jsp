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
		
		$("#copyfrom_lat_text [type='checkbox']").click(function(){
			//if($("#copyfrom_lat_text :checked").length<=0) alert("请先选中");
			$("#copyfrom_text").find("input:text").each(function(){
				$(this).attr("disabled",true);
				//$(this).attr("value","");
			})

			var v='' ;
			$("#copyfrom_lat_text :checked").each(function(){
				$("#copyfrom"+$(this).val()).attr("disabled",false);
				if(v=='') v=$(this).val();
				else v=v+","+$(this).val();
			})
			$("#copyfrom_lat").val(v);
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
<input type="hidden" name="tag_rule$type" value="1" />


<table class="ui edit" id="copyfrom_text">
	<tr class="title"><td colspan="2">文章来源纬度</td></tr>
    <tr>
    	<td colspan="2" id="copyfrom_lat_text">
        	<input type="hidden" name="tag_rule$copyfrom_lat" id="copyfrom_lat" value="${obj.copyfrom_lat}" />
            
    		<input type="checkbox" id="weibo_checkbox" name="copyfrom_lat0" value="0" <c:if test="${(obj.copyfrom_lat==null || obj.copyfrom_lat=='') || (obj.weibo_uid!=null && obj.weibo_uid!='') }">checked="checked"</c:if> />&nbsp;微博UID&nbsp;&nbsp;&nbsp;&nbsp;
            
            <input type="checkbox" id="url_checkbox" name="copyfrom_lat1" value="1" <c:if test="${(obj.copyfrom_lat==null || obj.copyfrom_lat=='') || (obj.copyfromurl!=null && obj.copyfromurl!='') }">checked="checked"</c:if>/>&nbsp;来源url&nbsp;&nbsp;&nbsp;&nbsp;
            
            <input type="checkbox" id="task_checkbox" name="copyfrom_lat2" value="2" <c:if test="${(obj.copyfrom_lat==null || obj.copyfrom_lat=='') || (obj.task_no!=null && obj.task_no!='') }">checked="checked"</c:if>/>&nbsp;火车头任务编号
        </td>
	</tr>
	<tr>
        <td width="75">微博UID：</td>
        <td><input type="text" name="tag_rule$weibo_uid" id="copyfrom0" value="${obj.weibo_uid}" size="60" />&nbsp;<span style="color:#CCC;">多个值之间,以逗号分隔</span></td>
    </tr>
    <tr>
        <td width="75">来源URL：</td>
        <td><input type="text" name="tag_rule$copyfromurl" id="copyfrom1" value="${obj.copyfromurl}" size="60" />&nbsp;<span style="color:#CCC;">多个值之间,以逗号分隔</span></td>
    </tr>
    <tr>
        <td width="75">任务编号：</td>
        <td><input type="text" name="tag_rule$task_no" id="copyfrom2" value="${obj.task_no}" size="60" />&nbsp;<span style="color:#CCC;">多个值之间,以逗号分隔</span></td>
    </tr>
</table>

<table class="ui edit">
<tr class="title"><td colspan="2">关键字纬度</td></tr>
	<tr>
        <td width="75">条件：</td>
        <td><input type="radio" name="tag_rule$conditions" value="or" <c:if test="${obj.conditions=='or' || obj.conditions=='' || obj.conditions==null}">checked="checked"</c:if> />or &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tag_rule$conditions" value="and" <c:if test="${obj.conditions=='and'}">checked="checked"</c:if> />and</td>
    </tr>
    <tr>
        <td width="75">包含关键字：</td>
        <td><input type="text" name="tag_rule$keyword" value="${obj.keyword}" size="60"/>&nbsp;<span style="color:#CCC;">多个值之间,以逗号分隔</span></td>
    </tr>
</table>

<table class="ui edit">
<tr class="title"><td colspan="2">自动Tag</td></tr>
	<tr>
        <td width="75">规则名称：</td>
        <td><input type="text" name="tag_rule$name" value="${obj.name}" size="60" /></td>
    </tr>
    <tr>
        <td width="75">标签：</td>
        <td>
<input type="hidden" id="tags" name="tag_rule$tag_name" />
<ul id="tags_result" class="tags_result select_label"></ul>
<c:set var="tagsList_pf" value="${tagsList}" />
<%@ include file="/template/tag/tag_plugin.jsp"%>
        </td>
    </tr>
    <tr>
        <td width="75">客户重要度：</td>
        <td><input type="radio" name="tag_rule$urgent" value="" <c:if test="${obj.urgent==null || obj.urgent==''}">checked="checked"</c:if>/>无<m:radio type="urgent" name="tag_rule$urgent" value="${obj.urgent}" /></td>
    </tr>
    <tr>
        <td width="75">GP重要度：</td>
        <td><input type="radio" name="tag_rule$importance" value="" <c:if test="${obj.importance==null || obj.importance==''}">checked="checked"</c:if>/>无<m:radio type="importance" name="tag_rule$importance" value="${obj.importance}" /></td>
    </tr>  
</table>
<div><table class="ui edit" style="border:0">
		<tr>
        	<td width="75"></td>
        	<td><input type="submit" value="提交" class="right-button08" /><input type="button" value="放弃保存" onclick="window.history.go(-1);" /></td>
        </tr>
      </table>
</div>

</form>
</body>
</html>