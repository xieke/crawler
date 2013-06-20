<%@ page contentType="text/html;charset=utf-8"%> <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%> <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> <html xmlns="http://www.w3.org/1999/xhtml"> <head> <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> <%@ include file="/template/basic/common_head.jsp"%>

<!--表单验证控件引入文件 开始-->
<script type="text/javascript" src="<c:url value='/plugin/jquery.easyvalidator/js/easy_validator.pack.js'/>"></script>
<link href="<c:url value='/plugin/jquery.easyvalidator/css/validate.css'/>" rel="stylesheet" type="text/css" />
<!--表单验证控件引入文件 结束-->


<!--日期控件引入文件 开始-->
<script type="text/javascript" src="<c:url value='/plugin/jquery.selectsearch-4.0/jquery.selectseach.min.js'/>"></script>
<!--日期控件引入文件 结束-->


<title>编辑用户信息</title>
</head>

<body>
<div class="ui_head"></div>

<%@ include file="menu.jsp"%>

<form action="/basic.UserAH.save" method="post" id="post_form">
<m:token/>
	<input type="hidden" name="objId" value="${obj.userid}" />
<table class="ui edit">
	<tr class="title">
      <td colspan="2">${obj.username} 个人信息</td>
    </tr>
    <tr> 
      <td width="20%">登录名：</td>
      <td><input type="text" class="mybutton" name="EMPLOYEE$loginname"  value="<c:out value='${obj.loginname}'/>" />
		  	<input type="hidden" name="EMPLOYEE$userid"  value="<c:out value='${obj.userid}'/>" />
		  	<input type="hidden" name="EMPLOYEE$isadmin"  value="<c:out value='1'/>" />
		  	<input type="hidden" name="EMPLOYEE$status"  value="<c:out value='0'/>" />
		  	</td>
    </tr>
    <tr> 
      <td>真实姓名：</td>
      <td><input type="text" name="EMPLOYEE$username" value="<c:out value='${obj.username}'/>" /></td>
    </tr>
    <tr> 
      <td>性别：</td>
      <td><m:select name="EMPLOYEE$gender"  value="${obj.gender}" type="gender"/></td>
    </tr>
	
    <tr> 
      <td>邮箱：</td>
      <td><c:out value="${obj.email}" /></td>
    </tr>
    <tr> 
      <td>手机号：</td>
      <td><c:out value="${obj.telno}" /></td>
    </tr>
    <tr> 
      <td>上次登录时间：</td>
      <td>${obj.lastlogindate}</td>
    </tr>
    <tr> 
      <td>帐号是否注销：</td>
      <td>${obj.scrap=='0'?"<font color=red>已注销</font>":"未注销"}</td>
    </tr>
<tr>
  <td>备注：</td>
  <td><input type="text" name="EMPLOYEE$remark" value="${obj.remark}" /></td>
</tr>
<tr>
	<td>角色权限：</td>
    <td><c:forEach var="role" items="${roles}"  varStatus="status">
        <div style="width:100px; float:left"><m:checkbox name="${status.index}$role$roleId" value="${role.roleid}" checkValue="${role.creator}"/> ${role.rolename}</div>
</c:forEach></td>
</tr>
	<tr>
      <td></td>
      <td>
      <input type="button" value=" 保 存 " onclick="$('#post_form').submit()" />
      <input onclick="history.go(-1);" type="button" value="返回上页" /></td>
    </tr>
</table>
</form>








</body>
</html>
