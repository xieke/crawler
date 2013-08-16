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
<title>新建发送任务</title>

<!--日期控件 **带时分** 开始-->
<script type="text/javascript" src="/plugin/jquery-calendar.js"></script>
<!--日期控件 **带时分** 结束-->

</head>
<script type="text/javascript">
	$(function(){
		$("#modify").click(function(){
			//editor.sync();
			$("#reqType").attr("value","news.GpMailAH.listModify");

			$("#form1").submit();
		});
		$("#sendmail").click(function(){
			//editor.sync();
			$("#reqType").attr("value","news.GpMailAH.sendMail");
			$("#form1").submit();
		});
	});

</script>
		

<body>
<div class="ui_head"></div>
<%@ include file="menu.jsp"%>

<form action="/post.PostJobAH.save" method="post" name="form1" id="form1">

<input type="hidden" id="objId" name="objId" value="${obj.id}">
<input type="hidden" id="postjobid" name="postJob$id" value="${obj.id}">
<input type="hidden" id="tactics_ids" name="tactics_ids" value="${obj.ruleid.id}">
<input type="hidden" id="custom_ids" name="custom_ids" value="${obj.customers}">
<table class="ui edit">

<tr class="title"><td colspan="2">${obj.id==''||obj.id==null?"新建":"修改"}发送任务</td></tr>
				  <tr>
					<td width="90">任务编号：</td>
                    <td><input type="text"  id="no" name="postjob$no" value="${obj.no}" /></td>
				  </tr>

				  <tr>
					<td>任务名称：</td>
                    <td><input type="text"  id="name" name="postjob$name" value="${obj.name}" /></td>
				  </tr>
				  <tr>
					<td>任务策略：</td>
                    <td><input type="text" size="50" id="tactics" name="ruleid" value="${obj.ruleid.name}" />
<c:set var="tagsList_pf" value="${rules}" />
<%@ include file="tactics_plugin.jsp"%></td>
				  </tr>
				  <tr>
					<td>客户：</td>
                    <td><input type="text" size="50" id="custom" name="postjob$customers" value="${obj.cnames}" />
<c:set var="tagsList_pf" value="${customers}" />
<%@ include file="custom_plugin.jsp"%></td>
				  </tr>
				  <tr>
					<td>预定时间：</td>
                    <td>
                    <m:select name="postjob$posthour" value="${obj.posthour}" type="hours" />
                    <m:select name="postjob$postminute" value="${obj.postminute}" type="minutes" />
					&nbsp; &nbsp; 
                    <jsp:useBean id="now" class="java.util.Date" />
					上次发送时间 <input style="border-color:#FFF;color: #005AFF;text-decoration: underline;" type="text" name="postjob$lastposttime" value='<fmt:formatDate value="${obj.lastposttime}" pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly" plugin="date2" start="" end="" /></td>
				  </tr>
				  <tr>
					<td>生效状态：</td>
                    <td>
					<m:radio name="postjob$status" value="${obj.status==null?1:obj.status}" type="postjob_status"/>
					</td>
				  </tr>
				  <tr>
					<td>发送文章上限：</td>
                    <td><input type="text"  id="limits" name="postjob$limits" value="${obj.limits}" />
					</td>
				  </tr>
				  <tr>
					<td>邮件服务器：</td>
                    <td>
					<m:radio name="postjob$mailserver" value="${obj.mailserver}" type="mailservers"/>
					</td>
				  </tr>

				  <tr>
					<td></td>
                    <td><input type="submit" id="save" name="save" class="button" value="提交" />
                    <input type="button" value="放弃保存" onclick="window.history.go(-1);" /></td>
				  </tr>
</table>
</form>
</body>
</html>
