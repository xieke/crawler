<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统注册表管理</title>
<jsp:include page="/inc.jsp" flush="false" />
</head>
<frameset cols="180,*" frameborder="no" border="0" framespacing="0">
  <frame src="<c:url value='/basic.DicAH.listDic' />" name="dic_menu" scrolling="No" noresize="noresize" id="dic_menu" title="注册表分项">
  <frame src="" name="dic_body" id="dic_body" title="注册分项内容">
</frameset>
<noframes><body>你的浏览器不支持Frameset，请更换浏览器或联系管理员！
</body></noframes>
</html>