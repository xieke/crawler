<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="submenu">
		<span class="button">
                <a href="<c:url value='/basic.UserAH.listUser'/>"><fmt:message key="member_list"/></a>
                <a href="<c:url value='/basic.UserAH.showUser'/>"><fmt:message key="add_member"/></a>
		</span>
</div>