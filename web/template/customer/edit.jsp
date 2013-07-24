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
<title>TAG</title>
<!--UI套装 开始-->
<link type="text/css" href="/plugin/jquery-ui-1.8.13/css/ui-lightness/jquery-ui-1.8.13.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="/plugin/jquery-ui-1.8.13/js/jquery-ui-1.8.13.custom.min.js"></script>
<!--UI套装 结束-->


</head>

<body>
<div class="ui_head"></div>


<form method="post" action="/GeneralHandleSvt">
<input type="hidden" id="reqType" name="reqType" value="customer.CustomerActionHandler.save" />
<input type="hidden" id="id" name="customers$id" value="${obj.id}" />

<table class="ui edit">
<tr class="title"><td colspan="4">编辑客户/组</td></tr>
    <tr>
        <td width="8%">客户姓名</td>
        <td colspan="3"><input type="text" name="customers$name" value="${obj.name}"/></td>
    </tr>
    <tr>
    	<td>客户级别</td>
        <td colspan="3"><m:select type="customer_level" name="customers$level" value="${obj.level}"/></td>
    </tr>
    <tr>
        <td>邮箱地址</td>
        <td colspan="3"><input type="text" name="customers$email" value="${obj.email}"/></td>
    </tr>
    <tr>
        <td>联系电话</td>
        <td colspan="3"><input type="text" name="customers$telephone" value="${obj.telephone}"/></td>
    </tr>
    <tr>
        <td>联系地址</td>
        <td colspan="3"><input type="text" name="customers$address" value="${obj.address}"/></td>
    </tr>
    <tr>
        <td>创建时间</td>
        <td colspan="3"><fmt:formatDate value="${obj.createdate}" pattern="yyyy-MM-dd HH:mm"/></td>
    </tr>
    <tr>
        <td>发送任务</td>
        <td colspan="3"><m:select type="postjobs" name="postjobId" value=""/></td>
    </tr>
    <tr>
    	<td>备注信息</td>
        <td colspan="3"><textarea type="text" name="customers$remark" cols="50" rows="3">${obj.remark}</textarea></td>
    </tr>
    <tr>
    	<td>当前生效规则</td>
        <td colspan="3">${jobs}</td>
    </tr>
    <tr>
    	<td></td>
        <td><input type="submit" value="提交"  />
        <input type="button" value="放弃保存" onclick="window.history.go(-1);" /></td>
    </tr>
</table></form>


</body>
</html>