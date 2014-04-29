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
<input type="hidden" id="tag_ids" name="tag_ids2" value="${tag_ids2}" />

<table class="ui edit">
<tr class="title"><td colspan="4">查询条件</td></tr>
    <tr>
        <td width="10%">规则名称：</td>
        <td width="40%"><input type="text" name="name" value="${name}" /></td>
        <td width="10%">关键字：</td>
        <td width="40%"><input type="text" name="keyword" value="${keyword}" /></td>
    </tr>
     <tr>
     	<td width="10%">来源：</td>
    	<td colspan="3" id="copyfrom_lat_text">
        	<input type="hidden" name="copyfrom_lat" id="copyfrom_lat" value="${copyfrom_lat}" />
            
    		<input type="checkbox" id="weibo_checkbox" name="copyfrom_lat0" value="0" <c:if test="${copyfrom_lat0!=null && copyfrom_lat0!='' }">checked="checked"</c:if> />&nbsp;微博UID&nbsp;&nbsp;&nbsp;&nbsp;
            
            <input type="checkbox" id="url_checkbox" name="copyfrom_lat1" value="1" <c:if test="${copyfrom_lat1!=null && copyfrom_lat1!='' }">checked="checked"</c:if>/>&nbsp;来源url&nbsp;&nbsp;&nbsp;&nbsp;
            
            <input type="checkbox" id="task_checkbox" name="copyfrom_lat2" value="2" <c:if test="${copyfrom_lat2!=null && copyfrom_lat2!=''}">checked="checked"</c:if>/>&nbsp;火车头任务编号
        </td>
	</tr>
    <tr>
    	<td>客户重要度：</td>
        <td><m:radio type="urgent_all" name="urgent" value="${urgent}" /></td>
        <td>GP重要度：</td>
        <td><m:radio type="importance_all" name="importance" value="${importance}" /></td>
    </tr>
    <tr>
        <td>标签：</td>
        <td colspan="3">
<input type="hidden" id="tags" name="tags2" value="${tags2}" />
<ul id="tags_result" class="tags_result select_label"></ul>
<c:set var="tagsList_pf" value="${tagsList}" />
<%@ include file="/template/tag/tag_plugin.jsp"%>
<style type="text/css">
#tag_text{ margin-left:0}
</style>
<script type="text/javascript">
$(function(){
	$("#dels").click(function(){
		if(confirm('你确定要删除吗?')){
			$("#reqType").attr("value","news.NewsActionHandler.deletes");
			
			$("#rownum").attr("value",rownum);
			//$("#page").attr("value","");
			$("#form1").submit();
		}
	});
});
</script>
        </td>
        
    </tr>
    <tr>
    	<td></td>
        <td colspan="3"><input type="button" onclick="querylist()" value="查询" class="right-button08" />
        <input type="button" id="resetb" value="重填" /></td>
    </tr>
</table>

<table class="ui list" id="rules">
	<tr class="title"><td class="title" colspan="8">编辑TAG标签库</td><td><a style=" color: #FFFFFF" href="/tag.TagRuleActionHandler.show?job=default" >Add规则</a></td></tr>
    <tr class="effect">
    	<th width="15">选择</th>
    	<th width="15">序号</th>
        <th width="9%">名称</th>
        <th width="5%">条件</th>
        <th width="20%">包含关键字</th>
        <th width="18%">标签</th>
        <th width="10%">客户重要度</th>
        <th width="10%">GP重要度</th>
        <th width="8%">操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
    <tr class="effect">
        <td><input type="checkbox" value="${detail.id}" name="delid"/></td>
        <td>${status.index+1}</td>
        <td>${detail.name}</td>
        <td>${detail.conditions}</td>
        <td>${detail.keyword}</td>
        <td align="center">${detail.tag_name}</td>
        <td align="center"><m:out type="urgent" value="${detail.urgent}" /></td>
        <td align="center"><c:if test="${obj.importance==null || obj.importance==''}"></c:if><m:out type="importance" value="${detail.importance}" /></td>
        <td align="center"><a href="/tag.TagRuleActionHandler.show?job=edit&objId=${detail.id}" onclick="return confirm('你确实要编辑吗?');"><img title="点击编辑" alt="点击编辑" src="/images/button_edit.png" /></a>&nbsp;<a href="/tag.TagRuleActionHandler.copy?job=edit&objId=${detail.id}" onclick="return confirm('你确实要复制吗?');"><img title="点击复制" src="/images/copy.png" /></a>&nbsp;<a href="/tag.TagRuleActionHandler.delete?job=delete&id=${detail.id}" onclick="return confirm('你确实要删除吗?');"><img title="点击删除" src="/images/del_icon2.gif" /></a></td>
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
		
		$("#copyfrom_lat_text [type='checkbox']").click(function(){
			var v='' ;
			$("#copyfrom_lat_text :checked").each(function(){
				if(v=='') v=$(this).val();
				else v=v+","+$(this).val();
			})
			$("#copyfrom_lat").val(v);
		})

	})
	
	function querylist(){
		$("#reqType").attr("value","tag.TagRuleActionHandler.list");
		$("#form1").submit();
	}
</script>

<div class="pages_bar">
<div class="pages_left"> 第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="tag.TagRuleActionHandler.list" size="30" /></div>
</div>
</form>
</body>
</html>