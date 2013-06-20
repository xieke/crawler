<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/template/basic/common_head.jsp"%>
<title></title>
<style type="text/css">
div.menu{top:10px; right:0; bottom:10px; left:10px; background:#D0D3D6; position:absolute; overflow:auto}
table.ui td{ padding:6px 0px}
table.ui tr{ background:#F1F1F1}
table.ui tr:hover,table.ui tr.select{ background:#F5F8FB; cursor:default}
</style>
<script type="text/javascript">
//<a target="dic_body" href="/basic.ItemAH.listItemsOfDic?ITEM$dicId=">
$(function(){
	$("tr[did]").click(function(){
			$("tr[did].select").removeClass("select");
			$(this).addClass("select");
			window.parent.dic_body.location.href="/basic.ItemAH.listItemsOfDic?dicid="+$(this).attr("did")+"&isview="+$(this).attr("isview");
	});
	$("tr[did]:first").click();
});
</script>
<jsp:include page="/inc.jsp" flush="false" />
</head>
<c:if test="${1==1}">
<body style="padding-right:0">

<div class="menu">
  <table border="1" cellspacing="0" cellpadding="0" class="ui">
    <tr>
      <th><a href="/basic.DicAH.listDic"  <c:if test="${param.select=='dic'}"> style="color:#FF0000" </c:if>>字典</a>|<a href="/basic.DicAH.listViewDic" <c:if test="${param.select=='view'}"> style="color:#FF0000" </c:if>>视图</a> |   
	  <a href="javascript:void(0)" onclick="javascript:window.parent.dic_body.location.href='/basic.ItemAH.listItemsOfDic';">添加</a></th>
    </tr>
<c:forEach var="obj" items="${objList}">
    <tr did="${obj.dicid}" isview="${obj.isview}">
      <td>${obj.name}</td>
    </tr>
</c:forEach>
  </table>
</div>
</body>
</c:if>
</html>




