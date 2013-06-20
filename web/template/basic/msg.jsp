<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>
<title>PLMWeb v1.0</title>
<style type="text/css">
body {
	background-color: #b7b7b7;
}
div.alert {width:80%;min-height:350px;margin:6% auto 0 auto;}
div.alert_title{background: no-repeat right 0 url(<c:url value='/theme/adobe/alert_title.gif'/>); height:39px; line-height:38px; padding-left:20px; font-size:14px; font-weight:bold}
div.alert_content{ border-bottom:3px solid #979797;border-right:3px solid #979797; background:#eeeeee; padding:20px}
div.alert_op{margin:10px auto; padding-top:10px; text-align:center; background:url(<c:url value='/theme/adobe/side_bg_line.gif'/>) repeat-x}
</style>
</head>

<body>
<div class="alert">
<div class="alert_title">系统提示</div>
  <div class="alert_content">
${tipInfo==''||tipInfo==null?param.tipInfo:tipInfo}
<div class="alert_op">
<SCRIPT LANGUAGE="JavaScript">
	
<!--
var nexturl="${nextUrl==''?param.nextUrl:nextUrl}";
<!-- istab 是指的要返回上一级的标签页,以防标签页重复 -->

<c:if test="${istab==''||istab==null}">
var action=nexturl==""?"history.back(-1);":"window.location.href='"+nexturl+"'";
</c:if>
<c:if test="${istab!=''&&istab!=null}">
var action=nexturl==""?"history.back(-1);":"window.parent.location.href='"+nexturl+"'";
</c:if>


if(document.referrer==''&&window.self==window.top){
	document.write('<input type="button" name="Submit" value="点击关闭本网页" class="post_bt" onclick="window.self.close();"> <input type="button" name="Submit" value="返回网站首页" class="post_bt" onclick="top.location.href=\'/\';"> ');
}else{
	<c:if test="${buttons==''||buttons==null}">
	document.write('<input type="button" name="Submit" value="确 定" class="post_bt" onclick="'+action+'" />');
	</c:if>
}
//-->
</SCRIPT>
	${buttons}

</div>
  </div>
</div>
</body>
</html>
