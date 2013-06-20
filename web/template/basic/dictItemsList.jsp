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
<input type=hidden name="dictionary$id" value="${obj.id}" />
键值：<input type="text" name="dictionary$dicid" value="${obj.dicid}"/>
说明：<input type="text" name="dictionary$name" value="${obj.name}"/>
<input type="hidden" name="dictionary$isview" value="${obj.isview}"/>
<a href="javascript:void(0)" onclick="saveit();"/>保存</a>
<c:if test="${obj.isview!='1'}">
<a href="javascript:void(0)" onclick="delit();"/>删除</a>
</c:if>


${param.tipInfo}


<span class="button">
<a href="javascript:void(0)" onclick="if(${obj.dicid==null||obj.dicid==''?'true':'false'})alert('请先选择左边的数据字典列表!');else window.location='<c:url value="/template/basic/itemOper.jsp?dicId=${obj.dicid}&reqType=basic.ItemAH.add"/>'">添加子键</a>
</span>

</div>

<style type="text/css">
table.effect td{ line-height:14px; padding:8px 2px 8px 6px; text-align:left}
table.effect td.op{ padding:0; text-align: center}

</style>
<table border="1" cellspacing="0" cellpadding="0" class="ui effect">
  <tr>
  <th width="20"></th>
    <th>键名(N)</th>
    <th>数值数据(V)</th>
    <th>备注</th>

    <th>修改</th>
    <th>删除</th>
  </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${detail.ikey}</td>
    <td style="width:350px">${detail.name}</td>
    <td>${detail.memo}</td>
	<c:if test="${detail.isview==0}">
    <td class="op"><a class="ico_button ico_edit" href='/basic.ItemAH.preUpdate?ITEM$id=${detail.id}'>Edit</a></td>
    <td class="op"><a class="ico_button ico_del" href='javascript:void(0)' onclick="if(confirm('你确定要删除键名为：${detail.ikey} 的参数吗？（一般不建议删除，除非完全确认该键无效）'))window.location='/basic.ItemAH.delete?dicid=${obj.id}&itemid=${detail.id}'">Del</a></td>
	</c:if>
	<c:if test="${detail.isview!=0}">
	<td></td>
	<td></td>
	</c:if>
  </tr>
</c:forEach>
${obj.id==null||obj.id==''?'<tr><td colspan="6">请先选择左边的数据字典列表</td></tr>':''}
</table>


<div class="remarks">注意：每个子键都很重要，删除前务必确认该子键已不在使用，否则将导致部分功能出现异常！</div>


</form>

</body>
</html>
