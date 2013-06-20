<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
			document.form1.action="/basic.ItemAH.saveDic";
			//document.form1.objId.value='';
			document.form1.submit();	
	}
	function  delit(){
			 //document.form1.objId=pid;
 			document.form1.action="/basic.ItemAH.delDic";
			document.form1.submit();	
	}

</script>
<body>
<form name="form1" action="<c:url value='/basic.ItemAH.saveDic'/>" method="post">
<div class="submenu">
键值：<input type="text" name="dictionary$id" value="${obj.id}"/>
说明：<input type="text" name="dictionary$name" value="${obj.name}"/>
<input type="hidden" name="dictionary$isview" value="${obj.isview}"/>
<a href="javascript:void(0)" onclick="saveit();"/>保存</a>
<a href="javascript:void(0)" onclick="delit();"/>删除</a>
<m:global>gender.1</m:global>-------------<m:tool>meeting_type</m:tool>
<c:out value="${param.tipInfo}"/>
<c:if test="${obj.isview!=1}">


<a href="javascript:void(0)" onclick="if(${obj.id==null||obj.id==''?'true':'false'})alert('请先选择左边的数据字典列表!');else window.location='<c:url value="/template/basic/itemOper.jsp?dicId=${obj.id}&reqType=basic.ItemAH.add"/>'">添加子键</a>
</c:if>
</div>

<style type="text/css">
table.effect td{ line-height:14px; padding:8px 2px 8px 6px; text-align:left}
table.effect td.op{ padding:0; text-align: center}

</style>
<table border="1" cellspacing="0" cellpadding="0" class="ui effect">
  <tr>
  <th width="20"></th>
    <th>用户名</th>
    <th>登录时间</th>
    <th>登录ip</th>
	<th>踢出</th>
    
  </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${detail.username} </td>
    <td style="width:350px">${detail.lastlogindate}</td>
    <td>${obj.loginip}</td>
	
    <td class="op"><a class="ico_button ico_del" href='javascript:void(0)' onclick="if(confirm('你确定要踢出用户名为：${detail.user} 的家伙吗？（一般不建议，除非完全确认该键无效）'))window.location='/basic.ItemAH.delete?dicid=${obj.id}&itemid=${detail.id}'">kick out</a></td>
	
  </tr>
</c:forEach>
${obj.dicid==null||obj.dicid==''?'<tr><td colspan="6">当前没有在线用户</td></tr>':''}
</table>


<div class="remarks">注意：每个用户很重要，踢出前务必确认该用户已不在使用，否则将导致部分功能出现异常！</div>


</form>

</body>
</html>
