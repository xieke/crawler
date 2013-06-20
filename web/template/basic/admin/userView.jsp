<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>

<!--表单验证控件引入文件 开始-->
<script type="text/javascript" src="<c:url value='/plugin/jquery.easyvalidator/js/easy_validator.pack.js'/>"></script>
<link href="<c:url value='/plugin/jquery.easyvalidator/css/validate.css'/>" rel="stylesheet" type="text/css" />
<!--表单验证控件引入文件 结束-->


<!--日期控件引入文件 开始-->
<script type="text/javascript" src="<c:url value='/plugin/jquery.selectsearch-4.0/jquery.selectseach.min.js'/>"></script>
<!--日期控件引入文件 结束-->


<title>view user</title>
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
      <td>所属部门(主)：</td>
      <td>${obj.deptid.deptname}</td>
    </tr>
    <tr> 
      <td>当前头像：</td>
      <td>
		<div class="userFace"><img height="148" src="${obj.photoid.url}" onerror="this.src='<c:url value='/theme/adobe/noface.gif'/>'" /></div>
      </td>
    </tr>
    <tr> 
      <td>性别：</td>
      <td><m:out  name=""  value="${obj.gender}" type="gender"/></td>
    </tr>
	<tr>
      <td>卡号：</td>
      <td>${obj.cardno}</td>
    </tr>
    <tr> 
      <td>内部邮箱：</td>
      <td>${obj.INNERMAIL}</td>
    </tr>
    <tr> 
      <td>外部邮箱：</td>
      <td>${obj.outmail}</td>
    </tr>
    <tr> 
      <td>手机号：</td>
      <td>${obj.telno}</td>
    </tr>
    <tr> 
      <td>人员费率：</td>
      <td>${obj.grade_rate_id.rate}</td>
    </tr>
    <tr> 
      <td>连续登录天数：</td>
      <td>${obj.logincount}</td>
    </tr>
    <tr> 
      <td>上次登录时间：</td>
      <td>${obj.lastlogindate}</td>
    </tr>
    <tr> 
      <td>工号：</td>
      <td>${obj.schoolno}</td>
    </tr>
    <tr> 
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
			${role.rolename} &nbsp; 
	</c:forEach></td>
	</tr>
   <tr>
    	<td></td>
        <td><input type="button" value="返回" onclick="window.back();" class="right-button08" /></td>
    </tr>
</table>


</body>
</html>
