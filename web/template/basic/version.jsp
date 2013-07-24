<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>
<title></title>
<jsp:include page="/inc.jsp" flush="false" />
</head>
<script type="text/javascript">


	function  saveit(){
			document.form1.action="/basic.GlobalAH.createAuthorFile";
			//document.form1.objId.value='';
			document.form1.submit();	
	}
	function  regit(){
			 //document.form1.objId=pid;
 			document.form1.action="/basic.GlobalAH.reg";
			document.form1.submit();	
	}

</script>
<body>
<form name="form1" action="<c:url value='/basic.ItemAH.saveDic'/>" method="post">

<div class="submenu">

是否注册：<c:if test='${obj.registed!=1}'>否 </c:if><c:if test='${obj.registed==1}'>是 </c:if>
<br>
系统版本：<m:tool>sys_edition</m:tool>
<br>
版权所有人：${obj.regname}   
<br>
注册日期：<fmt:formatDate value="${obj.regdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
<br>
最大同时在线数：${obj.maxonline}
<br>
激活模块：${obj.modules}


<br /><br />

<m:tool>sys_name</m:tool> <m:tool>sys_edition</m:tool><br />
版权所有 © 2006-2012 <m:tool>company</m:tool> 保留所有权利。<br />
<a href="#">服务条款</a>


</div>
<style type="text/css">
table.effect td{ line-height:14px; padding:8px 2px 8px 6px; text-align:left}
table.effect td.op{ padding:0; text-align: center}
</style>



</form>

</body>
</html>
