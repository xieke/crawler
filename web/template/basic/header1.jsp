<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<div class="wrapper">
<!-- header-->
	<div class="header_bg1">
		<div class="header clearfix">
			<div class="logos"><a href="<c:url value='/basic.HomeAH.index'/>" target="_parent" title="易生会员"></a></div>
			<div class="tops fr">
<!-- top_nav-->
				<ul class="top_nav clearfix">
					<li><a href="<c:url value='/basic.NewsActionHandler.listArticle?template=help&article_type=XSZN'/>" target="_parent"><span class="ts3"></span>帮助中心</a></li>
					<li><span></span><a href="/basic.LoginAH.exit" title="注销">[退出]</a></li>
					<li><span></span>欢迎您${sessionScope.curuser.showname}</li>
				</ul>
<!-- menu-->
				<div id="nowDiv">
					<ul id="sddm">
						<li class="xx"><a href="<c:url value='/real.SecureActionHandler.showCenter'/>" target="_parent">安全中心</a></li>
						<li class="xx"><a href="/template/station/station.jsp?menu=0" target="_parent">会员直通站</a></li>
						<li class="xx"><a href="/template/basic/pay.jsp" target="_parent">支付生活</a></li>
						<li class="xx"><a href="<c:url value='/basic.HomeAH.index'/>" target="_parent">我的账户</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
