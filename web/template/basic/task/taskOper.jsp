<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.util.*"%>
<%@ page import="sand.depot.tool.system.*"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="<c:out value='/images/style.css'/>">
<script type="text/javascript" src="<c:out value='/plugin/jquery-1.6.2.min.js'/>"></script> 
<title>添加定时任务</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<%@ include file="/template/basic/common_head.jsp"%>
<script type="text/javascript">
function checkform(){
	var taskdate=document.getElementById('TASK$TASKDATE').value;
	var taskcycle=document.getElementById('TASK$TASKCYCLE').value;
	if(taskcycle=="每天"){
		if(taskdate<1||taskdate>24){
			alert("请在1-24之间选择");
			document.form1.TASK$TASKDATE.focus();
			return false;
		}	
	}
	if(taskcycle=="每周"){
		if(taskdate<1||taskdate>7){
			alert("请在1-7之间选择");
			document.form1.TASK$TASKDATE.focus();
			return false;
		}	
	}
	if(taskcycle=="每月"){
		if(taskdate<1||taskdate>30){
			alert("请在1-30之间选择");
			document.form1.TASK$TASKDATE.focus();
			return false;
		}	
	}
	document.form1.submit();
}
</script>
<body>


<!--建议以上不要写代码-->
<div class="uitable">
<!--标题部分开始-->
<div class="div_title"><span><img height="13" align="absmiddle" width="15" src="<c:out value='/images/gp.gif'/>"/>&nbsp;&nbsp;添加定时任务</span><span class="spanB" op="border1s"></span><div style="clear: both;"/></div></div>
<!--标题部分结束-->
<div class="border2"><div id="border1s">
<form action="<c:url value='/GeneralHandleSvt'/>" method="post" name="form1">
<m:token/>
<input type="hidden" name="reqType" value="task.TaskAH.addOrUpdateTask" />
<input type=hidden name="task$id" value=${obj.id}>

<!--按钮区开始,如果不要，就删除掉-->
<div class="toolbar" style="margin-bottom:5px"><ul>
<li><a href="javascript:history.go(-1);"><span>返回</span></a></li>
<li><a href="javascript:checkform()"><span>保存</span></a></li>
</ul></div>
<!--按钮区结束-->



<!--表头信息区开始,如果不要，就删除掉-->
<table border="0" cellspacing="0" cellpadding="0" class="infotable">
    <tr>
      <td class="z">任务名称：</td>
      <td><input maxlength="25" size="20" name="TASK$TASKNAME" id="TASK$TASKNAME" value="<c:out value='${obj.TASKNAME}'/>"></td>     
    <tr>
    </tr>
      <td class="z">服务器名：</td>
      <td><input value="${obj.servername}" type="servername" name="task$servername"/></td>      
    </tr>
    <tr>
      <td class="z">运行周期：</td>
      <td><m:select value="${obj.taskcycle}" type="taskcycle" name="TASK$TASKCYCLE" id="TASK$TASKCYCLE"/></td>
    <tr>
    </tr>
      <td class="z">日期(1-31)：</td>
      <td><input maxlength="25" size="20" name="TASK$TASKDATE" id="TASK$TASKDATE" value="<c:out value='${obj.TASKDATE}'/>"> ( 运行周期为每月时填写,也可以是逗号隔开的多个值)</td>
    </tr>

	</tr>
      <td class="z">星期(1-7)：</td>
      <td><input maxlength="25" size="20" name="TASK$TASKweek" id="TASK$TASKweek" value="<c:out value='${obj.TASKweek}'/>"> ( 运行周期为每周时填写,也可以是逗号隔开的多个值 )</td>
    </tr>
    <tr> 
      <td class="z">小时(0-23)：</td>
      <td><input maxlength="25" size="20" name=TASK$TASKhour id="TASK$TASKhour" value="<c:out value='${obj.TASKhour}'/>">(运行周期为每天时填写，也可以是逗号隔开的多个值)</td>
    <tr>
    <tr> 
      <td class="z">分钟(0-59)：</td>
      <td><input maxlength="25" size="20" name=TASK$TASKminute id="TASK$TASKminute" value="<c:out value='${obj.TASKminute}'/>">(运行周期为每小时时填写，也可以是逗号隔开的多个值)</td>
    <tr>
    <tr> 
      <td class="z">秒(0-59)：</td>
      <td><input maxlength="25" size="20" name=TASK$TASKsecond id="TASK$TASKsecond" value="<c:out value='${obj.TASKsecond}'/>">(运行周期为每分钟时填写，也可以是逗号隔开的多个值)</td>
    <tr>

	</tr>
      <td class="z">所在类名：</td>
      <td><input value="${obj.taskclass}" name="TASK$TASKCLASS" id="TASK$TASKCLASS" type="taskclass"/></td>
    </tr>
	
    <tr>
      <td class="z">运行日期：</td>
      <td><input maxlength="25" size="20" name="TASK$runDATE" id="TASK$runDATE" onFocus="WdatePicker({startdate:'%y-%M-%1',dateFmt:'yyyy-MM-dd',alwaysUseStartdate:true})" value='${obj.rundate}'> (只运行一次时填写)</td>
    <tr>
    </tr>
      <td class="z">是否激活：</td>
      <td><m:select value="${obj.active}" name="TASK$active" id="TASK$active" type="active"/></td>
  	</tr>
    </tr>
      <td class="z"></td>
      <td>如果在运行周期中选择"每天"，则"1-24"代表小时；选择"每周"，则"1-7"代表星期；选择"每月"，则"1-30"代表天数。</td>
  	</tr>
</table>













<!--页码开始-->
<div id="pagination"></div>
<!--页码结束-->



</form>
</div></div></div>
</body>
</html>