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
<script type="text/javascript">
function do_submit(){
	$("#form1").submit();
}
</script>
<title>定时任务列表</title>
</head>

<body>
<div class="ui_head"></div>



<form action="GeneralHandleSvt" method="post" id="form1" name="form1">
    <input type="hidden" name="page" id="page"/>
    <input type="hidden" name="pagesize" id="pagesize" value="${pageVariable.pagesize}"/>
	<input type="hidden" name="orderBy" id="orderBy" value="${orderBy}"/>



<div class="submenu">
		<span class="button">
                <a href="/task.TaskAH.listTask">任务调度</a>
                <a href="<c:url value='/task.TaskAH.showTask'/>">添加定时任务</a>
		</span>
</div>
 













  <table class="ui edit">
    <tr class="title">
      <td colspan="4">查询条件</td>
    </tr>
  <tr>
    <td style="text-align:right">任务名称：</td>
    <td><input type="text" name="TASK$TASKNAME" value="${param.TASK$TASKNAME}" /></td>
    <td style="text-align:right">激活与否：</td>
    <td><label><input type="radio" name="reqType" ${param.reqType=="task.TaskAH.listActiveTask"?'checked="checked"':""} value="task.TaskAH.listActiveTask"/>激活</label> &nbsp; <label><input type="radio" ${param.reqType!="task.TaskAH.listActiveTask"?'checked="checked"':""} name="reqType" value="task.TaskAH.listTask"/>全部(含激活与未激活)</label></td>
  </tr>
  <tr><td></td>
  <td><input type="button" value="查询" onclick="do_submit()" /></td>
  </tr>
</table>




<!--查询区结束-->

<table class="ui list effect">
  <tr class="title">
            <th>任务名称</th>
            <th>服务器名</th>
            <th>运行周期</th>
            <th>运行日</th>
            <th>时间</th>
            <th>运行状态</th>
            <th>累计运行时间</th>
            <th>运行进度</th>
            <th>运行结果</th>
            <th>上次耗时</th>
            <th>上次运行时间</th>
            <th>是否激活</th>
            <th>备注</th>
            <th>操作</th>
  </tr>
<c:if test="${fn:length(objList)==0||objList==null}">
  <tr><td colspan="14">没有任何记录</td></tr>
</c:if>
<c:forEach var="obj" items="${objList}">
    <tr>
            <td><a href="/task.TaskAH.viewTask?objId=${obj.id}"><c:out value="${obj.taskname}" /></a></td>
            <td><c:out value="${obj.servername}" /></td>
            <td><m:out value="${obj.taskcycle}" type="taskcycle" /></td>
            <td><c:out value="${obj.taskdate}" /></td>
            <td><c:out value="${obj.tasktime}"/></td>
            <td><m:out value="${obj.runstatus}" type="runstatus"/></td>
            <td><c:out value="${obj.runtime}"/></td>
            <td><c:out value="${obj.percent}"/>%</td>
            <td><m:out value="${obj.result}" type="result"/></td>
            <td><c:out value="${obj.consumtime}"/></td>
            <td>${obj.rundate}</td>
            <td><m:out value="${obj.active}" type="active"/></td>
            <td><c:out value="${obj.memo}"/></td>
            <td>
                <a href="/task.TaskAH.listTaskLog?taskid=${obj.id}">执行日志</a>&nbsp;|&nbsp; 
                <a href="/task.TaskAH.showTask?objId=${obj.id}">修改</a>&nbsp;|&nbsp;
            	<a href="javascript:void(0)" onClick="if(window.confirm('确定要删除定时任何【${obj.taskname}】吗？(不可恢复)'))window.location='/task.TaskAH.deleteTask?objId=${obj.id}'">删除</a>
            </td>
    </tr>
</c:forEach> 
</table>



<div class="pages_bar">
<div class="pages_left">共 <span class="orange">${pageVariable.totalpage}</span> 页 | 第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="task.TaskAH.listTask" /></div>
</div>


</form>



</body>
</html>