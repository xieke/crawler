<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/images/style.css'/>">
<script type="text/javascript" src="<c:url value='/plugin/jquery-1.6.2.min.js'/>"></script>
<!--日期控件 开始-->
<script type="text/javascript" src="<c:url value='/plugin/jquery.datepick.package-4.0.5/jquery.datepick.js'/>"></script>
<link href="<c:url value='/plugin/jquery.datepick.package-4.0.5/jquery.datepick-adobe.css'/>" rel="stylesheet" type="text/css" />
<!--日期控件 结束-->
<script type="text/javascript">
function do_submit(){
	$("#form1").submit();
}
</script>
<title>执行日志</title>
</head>

<body>


<!--建议以上不要写代码-->
<div class="uitable">
<!--标题部分开始-->
<div class="div_title"><span><img height="13" align="absmiddle" width="15" src="<c:url value='/images/gp.gif'/>"/>&nbsp;&nbsp;执行日志</span><span class="spanB" op="border1s"></span><div style="clear: both;"/></div></div>
<!--标题部分结束-->
<div class="border2"><div id="border1s">
<form action="task.TaskAH.listTaskLog" method="post" id="form1" name="form1">
    <input type="hidden" name="page" id="page"/>
    <input type="hidden" name="pagesize" id="pagesize" value="${pageVariable.pagesize}"/>
	<input type="hidden" name="orderBy" id="orderBy" value="${orderBy}"/>
    
    <input type="hidden" name="taskid" id="taskid" value="${param.taskid}"/>



    <!--按钮区开始,如果不要，就删除掉-->
    <div class="toolbar" style="margin-bottom:5px"><ul>
    <li><a href="<c:url value='/task.TaskAH.listTask'/>"><span>返回上级</span></a></li>
    <li><a href="javascript:void(0)" onclick="do_submit()"><span>查询</span></a></li>
    </ul></div>
    <!--按钮区结束-->   





<!--查询区开始,如果不要，就删除掉-->
<div class="searchform">
<div class="search_title"><span style="background:url(<c:url value='/images/toolbar_search.png'/>) no-repeat 9px 5px" class="title">查询条件：</span><span class="spanB" op="search_content"></span><div style="clear: both;"/></div></div>
<div id="search_content">








<table border="0" cellspacing="0" cellpadding="0" class="searchtable">
  <tr>
    <td style="text-align:right">运行日期：</td>
    <td><input type="text" name="rundate" value="${param.rundate}" plugin="date"  /></td>
  </tr>
</table>


</div>




</div>

<!--查询区结束-->

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ui effect">
  <tr>
            <th>任务名称</th>
        	<th>运行结果</th>
        	<th>运行日期</th>
        	<th>耗时</th>
        	<th>备注</th>
  </tr>
<c:if test="${fn:length(objList)==0||objList==null}">
  <tr><td colspan="5">没有任何记录</td></tr>
</c:if>
<c:forEach var="obj" items="${objList}">
    <tr>
            <td><c:out value="${obj.taskid.taskname}" /></td>
            <td><c:out value="${obj.result}" /></td>
            <td><fmt:formatDate value="${obj.rundate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            <td><c:out value="${obj.usetime}" /></td>
            <td><c:out value="${obj.remark}" /></td>
    </tr>
</c:forEach> 
</table>


<!--页码开始-->
<div id="pagination">
  <c:if test="${pageVariable.npage==0}"> <span class="span_arrow page_home_1"></span> </c:if>
  <c:if test="${pageVariable.npage!=0}"> <span class="span_arrow page_home" onClick="pages('0')"></span> </c:if>
  <c:if test="${pageVariable.npage==0}"> <span class="span_arrow page_up_1"></span> </c:if>
  <c:if test="${pageVariable.npage>0}"> <span class="span_arrow page_up" onClick="pages('${pageVariable.npage-1}')"></span> </c:if>
  <span class="pagenumber">
  <c:forEach var="i" begin="${pageVariable.npage}" end="${pageVariable.npage+10}" step="1">
    <c:if test="${i-5>=0}">
      <c:if test="${i-5<=pageVariable.totalpage-1}"> <a href="javascript:void(0)" onclick="pages('<c:out value="${i-5}" />')"
        <c:if test="${pageVariable.npage==i-5}">class="a_acticve"</c:if>
        >
        <c:out value="${i-4}" />
        </a> </c:if>
    </c:if>
  </c:forEach>
  </span>
  <c:if test="${pageVariable.npage>=pageVariable.totalpage-1}"> <span class="span_arrow page_dn_1"></span> </c:if>
  <c:if test="${pageVariable.npage<pageVariable.totalpage-1}"> <span class="span_arrow page_dn" onClick="pages('${pageVariable.npage+1}')"></span> </c:if>
  <c:if test="${pageVariable.npage==pageVariable.totalpage-1||pageVariable.totalpage==0}"> <span class="span_arrow page_end_1"></span> </c:if>
  <c:if test="${pageVariable.npage!=pageVariable.totalpage-1&&pageVariable.totalpage!=0}"> <span class="span_arrow page_end" onClick="pages('${pageVariable.totalpage-1}')"></span> </c:if>
  <span class="page_text">共<strong>
  <c:out value="${pageVariable.totalpage}"/>
  </strong>页&nbsp;&nbsp;<strong>
  <c:out value="${pageVariable.rowcount}"/>
  </strong>条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每页显示&nbsp;
  <input size="3" maxlength="4" id="ppsize" name="" value="${pageVariable.pagesize}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" />
  &nbsp;条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;到第&nbsp;
  <input size="3" maxlength="3" id="ttpages" name="" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" />
  &nbsp;页&nbsp;&nbsp;
  <input class="gopages" value="转到" onclick="document.getElementById('pagesize').value=document.getElementById('ppsize').value;if(document.getElementById('ttpages').value==''){pages(0);}else{pages(document.getElementById('ttpages').value-1);}" name="" type="button" />
  </span>
  <div></div>
</div>
<!--页码结束-->

</form>


</div></div></div>
</body>
</html>