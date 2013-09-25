<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/template/basic/common_head.jsp"%>


<!--表单验证控件引入文件 开始-->
<script type="text/javascript" src="/plugin/jquery.easyvalidator/js/easy_validator.pack.js"></script>
<link href="/plugin/jquery.easyvalidator/css/validate.css" rel="stylesheet" type="text/css" />
<!--表单验证控件引入文件 结束-->




<title>${empty obj?'新建':'修改'}角色</title>
<jsp:include page="/inc.jsp" flush="false" />
</head>

<body>
<div class="ui_head"></div>

<%@ include file="role_menu.jsp"%>


<form method="post" action="/basic.RoleAH.save" id="post_form">
<input type="hidden" name="objId" value="${obj.roleid}" />
<input type="hidden" name="role$roleid" value="${obj.roleid}" />
  <table class="ui edit">
    <tr class="title">
      <td colspan="2">
<c:if test="${empty obj}">创建新角色</c:if>
<c:if test="${!empty obj}">修改角色</c:if>
      </td>
    </tr>
    <tr> 
      <td width="20%">角色名称：</td>
      <td><input type="text" name="Role$rolename" reg="^.+$" tip="请填写角色名称" value="${obj.rolename}" /></td>
    </tr>
    <tr> 
      <td>备注说明：</td>
      <td><input type="text" name="Role$remark" value="<c:out value='${obj.remark}'/>" /></td>
    </tr>
    <tr> 
      <td>模块权限：</td>
      <td style="padding:10px">
<table width="100%" border="1" cellspacing="1" cellpadding="1" class="inner_table">
    <tr>
      <th>系统模块</th>
      <th>权限设置</th>
    </tr>
<c:forEach var="funcs" items="${funcview}"  varStatus="prop">
    <tr>
      <td width="160">${funcs.key} <m:out value="${funcs.key}" type="module.modulename"/></td>
      <td style="text-align:left">
      <c:forEach var="function" items="${funcs.value}"  varStatus="status">
      	<div style="min-width:140px; float:left">&nbsp;<m:checkbox name="${funcs.key}${status.index}$function$functionId" value="${function.functionid}" checkValue="${function.creator}"/>
       	${function.functionname}</div>
	  </c:forEach>
      </td>
    </tr>
</c:forEach>
</table>
      </td>
    </tr>
    <tr> 
      <td>主页插件：</td>
      <td style="padding:10px">
<table width="100%" border="1" cellspacing="1" cellpadding="1" class="inner_table">
    <tr>
        <th>个人主页插件</th>
        <th>方法名</th>
        <th>说明</th>
        <th>权限设置</th>
    </tr>
<c:forEach var="plugin" items="${plugins}" varStatus="plugin_index">
    <tr>
        <td>${plugin.ikey}</td>
        <td>${plugin.name}</td>
        <td>${plugin.memo}</td>
        <td><input type="checkbox" ${plugin.checked} name="home_plugins" value="${plugin.id}" /></td>
    </tr>
</c:forEach>
</table>
      </td>
    </tr>
    <tr>
    <td></td>
    <td>    <c:if test="${empty obj}">
    				<input type="submit" value="提交保存" />
                    <input type="button" onclick="history.back()" value="取消返回" />
    </c:if>
    <c:if test="${!empty obj}">
    				<input type="submit" value="保存修改" />
                    <input type="button" onclick="history.back()" value="取消返回" />
    </c:if></td>
    </tr>
</table>
</form>








</body>
</html>