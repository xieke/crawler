<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.util.*"%>
<%@ page session="true" %>
<%@ page import="sand.depot.tool.system.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>易生会员中心</title>
<link href="/css/base.css" rel="stylesheet" type="text/css" />
<link href="/css/member_style.css" rel="stylesheet" type="text/css" />
<link type="/text/css" rel="stylesheet" href="/images/style.css" />
   
 <script type="text/javascript">
    function test(){
             alert("test");
             setReqType("basic.UserActionHandler.test");
             alert("submit");
             document.form1.submit();
    }
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
			document.form1.submit();
	}
</script>

</head>

<body>


<div class="wrapper">
<!-- header -->
		<jsp:include page="/template/basic/header1.jsp" flush="false" />
<!-- content-->
	<div class="content clearfix">
		<div class="wzh clearfix">
<!-- sidebar-->
			<div class="wzhl fl">
				<jsp:include page="/template/center/sidebar.jsp" flush="false" />
			</div>
<!-- zhanghu-->
	<div class="wzhr fr">
				<div class="mt10 clearfix">
			<div class="vip_top">登录密码修改</div>
			 <form action="<c:url value='/GeneralHandleSvt'/>" method="post" name="form1">
				  <m:token/>
				  <input type=hidden name="reqType" value="basic.PublicActionHandler.modifyPwd">
				  <input type=hidden name="EMPLOYEE$loginname"  value="${sessionScope.curuser.showname}">
			<ul class="edit_zcone clearfix">
				<li>
					<font>旧密码：</font><label><input type="password" name="EMPLOYEE$password" class="loinput wl" /></label>
				</li>
				<li>
					<font>新密码：</font><label><input type="password" name="newPwd" class="loinput wl" /></label>
				</li>
				<li>
					<font>新密码确认：</font><label><input type="password" name="newPwd2" class="loinput wl" /></label>
				</li>
			</ul>
			<div class="dyb clearfix"><span class="b-button"><a href="#" onClick="return checkform()">提交</a></span></div>
		</div>
	</div>
	<!--end edit_password-->
  </div>
    </div>
	  </div>
  <!--end content-->
<!-- footer-->
<jsp:include page="/template/basic/footer.jsp" flush="false" />
</div>
</body>
</html>
