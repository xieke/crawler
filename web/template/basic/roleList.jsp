<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/template/basic/common_head.jsp"%>
<!--表格表头悬浮控件 开始-->
<script type="text/javascript" src="/plugin/jquery.fixedtableheader-1.0.2.min.js"></script>
<!--表格表头悬浮控件 结束-->
<title></title>
<jsp:include page="/inc.jsp" flush="false" />
</head>

<body>
<div class="ui_head"></div>

<%@ include file="role_menu.jsp"%>


<form name="form1" action="?" method="post">
  <table class="ui edit">
    <tr class="title">
      <td colspan="2">查询条件</td>
    </tr>
    <tr>
      <td width="20%">角色名称：</td>
      <td><input type="text" name="role$rolename" value="${param.role$rolename}" /></td>
    </tr>
    <tr>
      <td></td>
      <td><input type="submit" value="查询" />
        <input type="button" id="resetb" value="重填" /></td>
    </tr>
  </table>
</form>

<table class="ui list effect">
  <tr class="title">
            <th>角色代号</th>
            <th>角色名</th>
            <th>备注</th>
            <th>修改</th>
            <th>删除</th>
  </tr>
<c:forEach var="obj" items="${objList}" varStatus="status">
<tr>
            <td><c:out value="${obj.roleid}"/></td>
            <td><c:out value="${obj.rolename}"/></td>
            <td><c:out value="${obj.remark}"/></td>
            <td><a title="修改" class="ico_button ico_edit" href="/basic.RoleAH.showRole?objId=${obj.roleid}">修改</a></td>
            <td><a title="删除" class="ico_button ico_del" href="/basic.RoleAH.delRole?objId=${obj.roleid}" onclick="return confirm('你确实要删除吗?不可恢复');">删除</a></td>
</tr>
</c:forEach>
</table>








</body>
</html>