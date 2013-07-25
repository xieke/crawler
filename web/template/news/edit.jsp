<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!--maxlength控件的start-->
<script type="text/javascript" src="/js/jquery.maxlength.js"></script>
<!--maxlength控件的end-->

<title>编辑新闻</title>
<%@ include file="/template/basic/common_head.jsp"%>
<script type="text/javascript">
	$(function(){
		$("#save").click(function(){
			editor.sync();
			$("#reqType").attr("value","news.NewsActionHandler.save");
			$("#form1").submit();
		});
		
		$("#save_next").click(function(){
			editor.sync();
			$("#operator").attr("value","next");
			$("#reqType").attr("value","news.NewsActionHandler.save");
			$("#form1").submit();
		});
		
		$("#save_last").click(function(){
			editor.sync();
			$("#operator").attr("value","last");
			$("#reqType").attr("value","news.NewsActionHandler.save");
			$("#form1").submit();
		});
		
		$("#commit").click(function(){
			editor.sync();
			$("#reqType").attr("value","news.NewsActionHandler.addComments");
			$("#form1").submit();
		});
		
		$("#merger").click(function(){
			var merger_text=$("#merger_c_summary").val();
			$("#merger_text :checked").each(function(){
				if(merger_text!='') merger_text+="\r\n";
				merger_text+= $(this).attr("value");
			})
			$("#c_summary").val(merger_text);

		})
		
		//refreshTags();
		
		
		
		$("#tag_text").css("margin","-7px 0 0 0")
		
	});
</script>
</head>

<body>
<div class="ui_head"></div>
<form action="GeneralHandleSvt" method="post" name="form1" id="form1" onsumit="" >
			<input type="hidden" id="tag_ids" name="tag_ids" value="${obj.tag_ids}" />
            <input type="hidden" id="reqType" name="reqType" value="" />
            <input type="hidden" id="objId" name="objId" value="${obj.id}" />
            <input type="hidden" id="news_id" name="news$id" value="${obj.id}" />
            <input type="hidden" id="merger_c_summary" name="merger_c_summary" value="${obj.c_summary}" />

            


<table class="ui edit">
<tr class="title"><td colspan="2">内容编辑</td></tr>
    <tr>
        <td valign="top" style="background:#f1f1f1">
<div style="padding:5px">新闻标题：<input type="text" style="width:420px" id="title" name="news$title" value="${obj.title}" /></div>
<div style="padding:5px">&nbsp;&nbsp;&nbsp;&nbsp;作者：<input type="text" style="width:80px" id="news_author" name="news$author" value="${obj.author}" />&nbsp;&nbsp;&nbsp;原文地址：<input type="text" id="news_copyfrom" name="news$copyfrom" value="${obj.copyfrom}" /></div>
<div style="padding:5px">原文URL：&nbsp;<input type="text" id="news_copyfromurl" name="news$copyfromurl" value="${obj.copyfromurl}" style="width:420px"  /></div>
<c:if test="${obj.his_news_id!='' && obj.his_news_id!=null}">
	<div style="padding:5px">查看原文：<a href="${obj.copyfromurl}" target="_blank" >${obj.copyfromurl}</a></div>
</c:if>
<div>
    	<!--富文本编辑控件引入文件 开始-->
<script charset="utf-8" src="/plugin/kindeditor-4.1.2/kindeditor-min.js"></script>
<script charset="utf-8" src="/plugin/kindeditor-4.1.2/lang/zh_CN.js"></script>
<script>
    var editor;
    KindEditor.ready(function(K) {
        editor = K.create('#content', {
            resizeType : 1,
            allowPreviewEmoticons : false,
            allowImageUpload : false,
			autoHeightMode : true,
            items : [
                'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                'insertunorderedlist', '|', 'emoticons', 'image', 'link']
        });
    });
</script>
                <!--富文本编辑控件引入文件 结束-->
        <textarea rows="1" cols="1" id="content" name="news$content" style="width: 100%; height: 650px; visibility: hidden;">${obj.content}</textarea>
        <input type="hidden" name="news_content$id" value="${news_content.id}" />
</div>
        </td>
        <td width="300" valign="top" class="content_side">
<div class="title">分类标签</div>
<input type="hidden" id="tags" name="tags" value="${obj.tags}" />
<ul style="height: 120px;margin: 0;width: 288px; background:#fff" id="tags_result" class="tags_result select_label"></ul>
<c:set var="tagsList_pf" value="${objList}" />
<%@ include file="/template/tag/tag_plugin.jsp"%>


<div style="margin-top:5px" class="title">参数设置</div>
<table class="item_sa">
<tr>
	<td width="75">状态：</td>
    <td><m:radio type="news_status" name="news$status" value="${obj.status}" defaultValue="0" /></td>
</tr>
<tr>
	<td>是否发邮件：</td>
    <td><label><input type="radio" id="issue" name="news$issue" <c:if test="${obj.issue=='1'}">checked</c:if> value="1" />Y</label>&nbsp;&nbsp;<label><input type="radio" id="issue" name="news$issue" <c:if test="${obj.issue=='0'||obj.issue==null}">checked</c:if> value="0" />N</label></td>
</tr>
<tr>
	<td>客户重要度：</td>
    <td><m:radio type="urgent" name="news$urgent" value="${obj.urgent}" defaultValue="0" /></td>
    <!--<td>
    	<c:forEach var="detail" items="${urgentList}" varStatus="status">
        	<m:checkbox name="news$urgent" value="${detail.id}" checkValue="${detail.checkedValue}"/>${detail.name}
        </c:forEach>-->
    </td>
</tr>
<tr>
	<td>GP重要度：</td>
    <td><m:radio type="importance" name="news$importance" value="${obj.importance}" defaultValue="0" /></td>
 <!--   <td>
    	<c:forEach var="detail" items="${importanceList}" varStatus="status">
        	<m:checkbox name="news$importance" value="${detail.id}" checkValue="${detail.checkedValue}"/>${detail.name}
        </c:forEach>
    </td>-->
</tr>
<tr>
	<td>文章分类：</td>
    <td><m:radio type="sort" name="news$sort" value="${obj.sort}" defaultValue="1" /></td>
</tr>
</table>
<div class="title">英文摘要</div>
<div><textarea style="height:150px" id="summary" name="news$summary" data-maxsize="1000">${obj.summary}</textarea></div>
<div class="title">中文摘要</div>
<div><textarea style="height:150px" id="c_summary" name="news$c_summary" data-maxsize="500">${obj.c_summary}</textarea></div>
<div class="title">备忘</div>
<div>
	<c:forEach var="detail" items="${commentsList}" varStatus="status">
		<div style="padding:4px; border-bottom:1px dotted #ccc; margin:4px; overflow:auto">
        <div style="background:#ccc; padding:1px 4px; overflow:auto"><span style="float:left">${detail.userid.username}</span><span style="float:right"><fmt:formatDate value="${detail.posttime}" pattern="yyyy-MM-dd HH:mm"/></span></div>
        <div id="merger_text" style="padding:4px"><input style="display:none" type="checkbox" name="merger_content" value="${detail.content}" />${detail.content}</div></div>
    </c:forEach>
    <textarea id="content" name="newcomments$content" ></textarea>
    <input style="margin-left:10px" type="button" id="commit" name="commit" value="Commit" />
  <!--  <input style="margin-left:10px" style="display:none" type="button" id="merger" name="merger" value="Merger" />-->
</div>
        </td>
    </tr>
    <tr>
    	<td colspan="2" style="border-top:1px solid #ccc; text-align:right">
        	<input type="button" id="save" name="save" class="button" value="完成处理_list" />　
        	<input type="button" id="save_next" name="save_next" class="button" value="完成处理_next" />　
            <input type="button" id="save_last" name="save_last" class="button" value="完成处理_last" />　
			<input type="button" name="Submit2" value="放弃保存" class="button" onclick="window.history.go(-1);"/></td>
    </tr>
</table>



<div>
	<input type="hidden" id="s_time" name="startDate" value="${startDate}" readonly="readonly" plugin="date" start="start" />
	<input type="hidden" id="e_time" name="endDate" value="${endDate}" readonly="readonly" plugin="date" end="start" />
    <input type="hidden" name="status" value="${status}" />
    <input type="hidden" id="title" name="title" value="${title}" />
    <input type="hidden" name="urgent" value="${urgent}" />
    <input type="hidden" id="author" name="author" value="${author}" />
    <input type="hidden" name="importance" value="${importance}" />
    <input type="hidden" name="tags2" value="${tags2}" />
    <input type="hidden" name="sort" value="${sort}" />
    <input type="hidden" name="tag_ids2" value="${tag_ids2}" />
	<input type="hidden" id="orderby" name="orderby" value="${orderby }"/>
	<input type="hidden" id="order" name="order" value="${order }"/>
	<input type="hidden" id="rownum" name="rownum" value="${rownum}" />
	<input type="hidden" id="operator" name="operator" value="" />
	<input type="hidden" id="pagesize" name="pagesize" value="${pagesize}" />
	<input type="hidden" id="page" name="page" value="${page}" />
	<input type="hidden" id="job" name="job" value="${job}" />
	<input type="hidden" id="from" name="from" value="${param.from}" />
</div>





</form>
</body>
</html>