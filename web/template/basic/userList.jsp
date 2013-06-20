<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>

<!--日期控件 开始-->
<script type="text/javascript" src="<c:url value='/plugin/jquery.datepick.package-4.0.5/jquery.datepick.js'/>"></script>
<link href="<c:url value='/plugin/jquery.datepick.package-4.0.5/jquery.datepick-adobe.css'/>" rel="stylesheet" type="text/css" />
<!--日期控件 结束-->
<!--表格表头悬浮控件 开始-->
<script type="text/javascript" src="<c:url value='/plugin/jquery.fixedtableheader-1.0.2.min.js'/>"></script>
<!--表格表头悬浮控件 结束-->
<title></title>
<jsp:include page="/inc.jsp" flush="false" />
</head>

<body>
<div class="ui_head"></div>


<%@ include file="menu.jsp"%>

<form name="form1" action="<c:url value='/basic.UserAH.listUser'/>" method="post">
  <table class="ui edit">
    <tr class="title">
      <td colspan="6">查询条件</td>
    </tr>
    <tr>
      <td width="8%"><fmt:message key="username"/>：</td>
      <td><input name="Employee$loginname" type="text" value="${param.Employee$loginname}" /></td>
      <td><fmt:message key="create_date"/>：</td>
      <td><input readonly="readonly" plugin="date" name="Employee$createdate" type="text" value="${param.Employee$createdate}" /></td>
      <td><fmt:message key="sex"/>：</td>
      <td><m:radio name="Employee$gender" type="gender" value="" />
      </td>
    </tr>
	<tr>
	  <td><fmt:message key="truename"/>：</td>
      <td><input name="Employee$username" type="text" value="${param.Employee$username}" /></td>
      <td><fmt:message key="department"/>：</td>
      <td><m:select  name="employee$deptid" type="department" value="" /></td>
      <td><fmt:message key="is_cancellation"/>：</td>
      <td>
      		<label><input name="Employee$scrap" type="radio" value="" checked="checked" /><fmt:message key="all"/></label>
            <label><input name="Employee$scrap" type="radio" value="0" /><fmt:message key="dimission"/></label>
      </td>
    </tr>
    <tr>
    	<td></td>
      <td colspan="5"><input type="submit" value="<fmt:message key='search'/>" />
        <input type="button" id="resetb" value="<fmt:message key='reset'/>" /></td>
    </tr>
  </table>

<table class="ui list effect">
  <tr class="title">
    <th><fmt:message key="username"/></th>
    <th><fmt:message key="department"/></th>
    <th><fmt:message key="truename"/></th>
    <th><fmt:message key="sex"/></th>
    <th><fmt:message key="create_date"/></th>
    <th><fmt:message key="is_cancellation"/></th>
    <th><fmt:message key="operation"/></th>
  </tr>
<c:if test="${fn:length(objList)==0||objList==null}">
  <tr><td colspan="7"><fmt:message key="no_record"/></td></tr>
</c:if>
<c:forEach var="obj" items="${objList}">
  <tr>
    <td><c:out value="${obj.loginname}"/></td>
    <td>${obj.deptid.deptname}</td>
    <td><c:out value="${obj.username}"/></td>
    <td><m:out value="${obj.gender}" type="gender"/></td>
    <td><fmt:formatDate value="${obj.createdate}" pattern="yyyy-MM-dd"/></td>
    <td>
    	<c:if test="${obj.scrap=='0'}"><fmt:message key="dimission"/></c:if>
        <c:if test="${obj.scrap!='0'}"><fmt:message key="inthesaddle"/></c:if>
    </td>
    <td>
<a href="<c:url value='/basic.UserAH.viewUser?objId=${obj.userid}'/>"><fmt:message key="view"/></a> | 
<a href="<c:url value='/basic.UserAH.showUser?objId=${obj.userid}'/>"><fmt:message key="edit"/></a> | 
<a href="javascript:if(window.confirm('<fmt:message key="delete_alert"/>'))window.location='<c:url value='/basic.UserAH.delUser?objId=${obj.userid}'/>'"><fmt:message key="delete"/></a>
    </td>
  </tr>
</c:forEach>
</table>


<div class="pages_bar">
<div class="pages_left">共 <span class="orange">${pageVariable.totalpage}</span> 页 | 第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="basic.UserAH.listUser" /></div>
</div>

</form>







</body>
</html>