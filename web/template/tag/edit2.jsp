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
</head>

<body>
<div class="ui_head"></div>



<form method="post" action="/GeneralHandleSvt">
<input type="hidden" id="reqType" name="reqType" value="basic.TagActionHandler.edit" />
<input type="hidden" id="id" name="tag$id" value="${obj.id}" />
<input type="hidden" name="tag$old_name" value="${obj.name}"/>
<table class="ui edit">
<tr class="title"><td colspan="2">修改TAG标签</td></tr>
    <tr>
        <td width="8%">TAG名称：</td>
        <td><input type="text" name="tag$name" value="${obj.name}"/></td>
    </tr>
    <tr>
        <td>TAG描述：</td>
        <td><input type="text" name="tag$description" value="${obj.description}"/></td>
    </tr>
    <tr>
        <td>上层分类：</td>
        <td>
            <select name="tag$parent_id">
            		<option value="" >根</option>
                <c:forEach var="detail" items="${objList}" varStatus="status">
                    <option value="${detail.id}" <c:if test="${obj.parent_id.id==detail.id}">selected="selected"</c:if> >${detail.level}${detail.name}</option>
                </c:forEach>
            </select>(不选择将成为一级分类) 
        </td>
    </tr>
    <tr>
    	<td></td>
        <td><input type="submit" value="提交"  />
        <input type="button" value="放弃保存" onclick="window.history.go(-1);" /></td>
    </tr>
</table></form>



</body>
</html>