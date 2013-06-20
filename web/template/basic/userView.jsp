<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>

<title>user view</title>
<jsp:include page="/inc.jsp" flush="false" />
</head>

<body>
<div class="ui_head"></div>

<%@ include file="menu.jsp"%>


  <table class="ui edit">
    <tr class="title">
      <td colspan="2">${obj.username} 的个人信息</td>
    </tr>
    <tr> 
      <td width="22%">用户名：</td>
      <td>${obj.loginname}</td>
    </tr>
    <tr> 
      <td>真实姓名：</td>
      <td>${obj.username}</td>
    </tr>
	
    <tr> 
      <td>当前头像：</td>
      <td>
		<div class="userFace"><img height="148" src="${obj.photoid.url}" onerror="this.src='<c:url value='/theme/adobe/noface.gif'/>'" /></div>
      </td>
    </tr>
    <tr> 
      <td class="bt">性别：</td>
      <td><m:out  name=""  value="${obj.gender}" type="gender"/></td>
    </tr>
   
    <tr> 
      <td>邮箱：</td>
      <td><input name="EMPLOYEE$email" value="${obj.email}" /></td>
    </tr>
    <tr> 
      <td>手机号：</td>
      <td>${obj.telno}</td>
    </tr>
   
  
    <tr> 
      <td>上次登录时间：</td>
      <td>${obj.lastlogindate}</td>
    </tr>
    <tr style="display:none"> 
      <td>帐号是否注销：</td>
      <td>${obj.scrap=='0'?"<font color=red>已注销</font>":"未注销"}</td>
    </tr>
	<tr>
	  <td>备注：</td>
	  <td>${obj.remark}</td>
	</tr>
	<tr>
		<td>角色权限：</td>
		<td><c:forEach var="role" items="${roles}"  varStatus="status">
			<div style="width:100px; float:left"><m:checkbox name="${status.index}$role$roleId" value="${role.roleid}" checkValue="${role.creator}"/> ${role.rolename}</div>
	</c:forEach></td>
	</tr>
</table>





</body>
</html>
