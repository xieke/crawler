<<<<<<< HEAD
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
<title>新建邮件服务器配置</title>

<!--日期控件 **带时分** 开始-->
<script type="text/javascript" src="/plugin/jquery-calendar.js"></script>
<!--日期控件 **带时分** 结束-->

</head>
<script type="text/javascript">
	$(function(){
		$("#modify").click(function(){
			//editor.sync();
			$("#reqType").attr("value","news.GpMailAH.listModify");

			$("#form1").submit();
		});
		$("#sendmail").click(function(){
			//editor.sync();
			$("#reqType").attr("value","news.GpMailAH.sendMail");
			$("#form1").submit();
		});
	});

</script>
		

<body>
<div class="ui_head"></div>
<%@ include file="menu.jsp"%>

<form action="/post.MailServerAH.save" method="post" name="form1" id="form1">

<input type="hidden" id="objId" name="objId" value="${obj.id}">
<input type="hidden" id="postjobid" name="mailserver$id" value="${obj.id}">
<table class="ui edit">

<tr class="title"><td colspan="2">${obj.id==''||obj.id==null?"新建":"修改"}邮件服务器配置</td></tr>
				  <tr>
					<td width="90">名称：</td>
                    <td><input type="text"  id="no" name="mailserver$name" value="${obj.name}" /></td>
				  </tr>

				  <tr>
					<td>发送服务器：</td>
                    <td><input type="text"  id="name" name="mailserver$smtp" value="${obj.smtp}" /></td>
				  </tr>
				  <tr>
					<td>接收服务器：</td>
                    <td>
					<input type="text"  id="name" name="mailserver$pop3" value="${obj.pop3}" />
					</td>
				  </tr>
				  <tr>
					<td>用户名：</td>
                    <td>
					<input type="text"  id="name" name="mailserver$username" value="${obj.username}" />
					</td>
				  </tr>
				  <tr>
					<td>密码：</td>
                    <td>
					<input type="text"  id="name" name="mailserver$password" value="${obj.password}" />
					</td>
				  </tr>
				  <tr>
					<td>超时时间：</td>
                    <td>
						<input type="text"  id="name" name="mailserver$overtime" value="${obj.overtime}" />

					</td>
				  </tr>
				  <tr>
					<td>发送地址：</td>
                    <td><input type="text"  id="limits" name="mailserver$fromaddr" value="${obj.fromaddr}" />
					</td>
				  </tr>
                  <tr>
					<td></td>
                    <td><input type="submit" id="save" name="save" class="button" value="提交" />
                    <input type="button" value="放弃保存" onclick="window.history.go(-1);" /></td>
				  </tr>
</table>
</form>
</body>
</html>
=======
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
<title>新建邮件服务器配置</title>

<!--日期控件 **带时分** 开始-->
<script type="text/javascript" src="/plugin/jquery-calendar.js"></script>
<!--日期控件 **带时分** 结束-->

</head>
<script type="text/javascript">
	$(function(){
		$("#modify").click(function(){
			//editor.sync();
			$("#reqType").attr("value","news.GpMailAH.listModify");

			$("#form1").submit();
		});
		$("#sendmail").click(function(){
			//editor.sync();
			$("#reqType").attr("value","news.GpMailAH.sendMail");
			$("#form1").submit();
		});
	});

</script>
		

<body>
<div class="ui_head"></div>
<%@ include file="menu.jsp"%>

<form action="/post.MailServerAH.save" method="post" name="form1" id="form1">

<input type="hidden" id="objId" name="objId" value="${obj.id}">
<input type="hidden" id="postjobid" name="mailserver$id" value="${obj.id}">
<table class="ui edit">

<tr class="title"><td colspan="2">${obj.id==''||obj.id==null?"新建":"修改"}邮件服务器配置</td></tr>
				  <tr>
					<td width="90">名称：</td>
                    <td><input type="text"  id="no" name="mailserver$name" value="${obj.name}" /></td>
				  </tr>

				  <tr>
					<td>发送服务器：</td>
                    <td><input type="text"  id="name" name="mailserver$smtp" value="${obj.smtp}" /></td>
				  </tr>
				  <tr>
					<td>接收服务器：</td>
                    <td>
					<input type="text"  id="name" name="mailserver$pop3" value="${obj.pop3}" />
					</td>
				  </tr>
				  <tr>
					<td>用户名：</td>
                    <td>
					<input type="text"  id="name" name="mailserver$username" value="${obj.username}" />
					</td>
				  </tr>
				  <tr>
					<td>密码：</td>
                    <td>
					<input type="text"  id="name" name="mailserver$password" value="${obj.password}" />
					</td>
				  </tr>
				  <tr>
					<td>超时时间：</td>
                    <td>
						<input type="text"  id="name" name="mailserver$overtime" value="${obj.overtime}" />

					</td>
				  </tr>
				  <tr>
					<td>发送地址：</td>
                    <td><input type="text"  id="limits" name="mailserver$fromaddr" value="${obj.fromaddr}" />
					</td>
				  </tr>
                  <tr>
					<td></td>
                    <td><input type="submit" id="save" name="save" class="button" value="提交" />
                    <input type="button" value="放弃保存" onclick="window.history.go(-1);" /></td>
				  </tr>
</table>
</form>
</body>
</html>
>>>>>>> 啊对发发呆
