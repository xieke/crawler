<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<c:if test="${!(empty sessionScope.curuser)}">
<meta http-equiv="refresh" content="0;URL='/basic.HomeAH.index'" /><!--若已登录，则自动转到管理首页-->
</c:if>
<c:if test="${empty sessionScope.curuser}">
<script type="text/javascript">
if(window.self!=window.top)//防止登录超时后，存在子框架则刷新父框架
window.top.location=window.self.location;
</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>欢迎登录GpCore后台管理系统</title>
<link href="css/css.css" rel="stylesheet" type="text/css" />
</head>

<body><form id="login_form" action="basic.LoginAH.login">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="147" background="images/top02.gif"><img src="images/top03.gif" /></td>
  </tr>
</table>
<table width="562" border="0" align="center" cellpadding="0" cellspacing="0" class="right-table03">
  <tr>
    <td width="221"><table width="95%" border="0" cellpadding="0" cellspacing="0" class="login-text01">
      
      <tr>
        <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="login-text01">
          <tr>
            <td align="center"><img src="images/ico13.gif" width="107" height="97" /></td>
          </tr>
          <tr>
            <td height="40" align="center">&nbsp;</td>
          </tr>
          
        </table></td>
        <td><img src="images/line01.gif" width="5" height="292" /></td>
      </tr>
    </table></td>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="31%" height="35" class="login-text02">用户名称：<br /></td>
        <td width="69%"><input name="employee$loginname" type="text" size="30" /></td>
      </tr>
      <tr>
        <td height="35" class="login-text02">密　码：<br /></td>
        <td><input name="employee$password" type="password" size="30" /></td>
      </tr>
      <tr>
        <td height="35" class="login-text02">验证图片：<br /></td>
        <td><img src="images/pic05.gif" width="80" height="30" /> <a href="#" class="login-text03">看不清楚，换张图片</a></td>
      </tr>
      <tr>
        <td height="35" class="login-text02">请输入验证码：</td>
        <td><input name="textfield3" type="text" size="30" /></td>
      </tr>
      <tr>
        <td height="35">&nbsp;</td>
        <td><input name="Submit2" type="submit" class="right-button01" value="确认登陆" />
          <input name="Submit232" type="reset" class="right-button02" value="重 置" />
          <div style="color:#F00; font-size:12px; margin-top:10px">${tipInfo}</div></td>
      </tr>
    </table></td>
  </tr>
</table></form>
</body>
</html>