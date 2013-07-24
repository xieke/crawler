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
<title>修改密码</title>
<script type="text/javascript">
	function checkform(){
		if (document.form1.EMPLOYEE$password.value==""){
			alert ("请输入旧密码");
			return false;
		}
		if (document.form1.newPwd.value==''){
			alert ("请输入新密码");
			return false;
		}
		if (document.form1.newPwd.value==document.form1.EMPLOYEE$password.value){
			alert ("新密码不能与原密码相同");
			return false;
		}
		if (document.form1.newPwd.value!=document.form1.newPwd2.value){
			alert ("两次输入密码不符");
			return false;
		}
		
		else
			return true;
	}
</script>
</head>

<body>
<div class="ui_head"></div>



<form action="<c:url value='/GeneralHandleSvt'/>" method="post" name="form1" onsubmit="return checkform();">
<m:token/>
<input type=hidden name="reqType" value="basic.PublicActionHandler.modifyPwd">
<input type=hidden name="EMPLOYEE$loginname"  value="${sessionScope.curuser.showname}">
<table class="ui edit">
<tr class="title"><td colspan="2">登录密码修改</td></tr>
    <tr>
        <td width="90">旧密码：</td>
        <td><input type="password" name="EMPLOYEE$password" /> （为了您的密码安全请设置相对复杂的密码）</td>
    </tr>
    <tr>
        <td>新密码：</td>
        <td><input type="password" name="newPwd" /></td>
    </tr>
    <tr>
        <td>新密码确认：</td>
        <td><input type="password" name="newPwd2" /></td>
    </tr>
    <tr>
    	<td></td>
        <td><input type="submit" value="确定修改" class="right-button08" /></td>
    </tr>
</table>
</form>
</body>
</html>