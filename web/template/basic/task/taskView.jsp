<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page session="false" %>


<html>
<head>
<%@ include file="/template/basic/common_head.jsp"%>
<title>任务详细显示</title>
<LINK href="style/test.css" type=text/css rel=stylesheet>
</head>
<BODY text=#000000 bgColor=#efefef scroll=yes>
<TABLE class=up cellSpacing=0 cellPadding=3 width="100%" border=0 background=images/title_back1.gif>
	<TR >
		<TD height=22>
			<IMG height=16 hspace=2 src="images/new.gif" width=16 align=absMiddle>任务浏览操作
		</TD>		
	</TR>
</TABLE>
  <TABLE cellSpacing=0 cellPadding=2 width="100%" border=1>
    <TR> 
      <TD noWrap align=right height=20 width="11%">任务名称<FONT color=#ff0000>*</FONT>：</TD>
      <TD noWrap align=left height=20 width="24%">&nbsp;<c:out value="${obj.taskname}"/></TD>
      <TD noWrap align=left height=20 width="14%"> 
        <div align="right">服务器名<font color=#ff0000>*</font>：</div>
      </TD>
      <TD noWrap align=left height=20 width="51%">&nbsp;<c:out value="${obj.servername}"/></TD>
    </TR>
    <TR> 
      <TD noWrap align=right height=25 width="11%">运行时间<font color=#ff0000>*</font>：</TD>
      <TD noWrap align=left height=25 width="24%">&nbsp;<c:out value="${obj.tasktime}"/></TD>
      <TD noWrap align=left height=25 width="14%"> 
        <div align="right">运行周期<font color=#ff0000>*</font>：</div>
      </TD>
      <TD noWrap align=left height=25 width="51%">&nbsp;<c:out value="${obj.taskcycle}"/></TD>
    </TR>
    <TR> 
      <TD noWrap align=right height=25 width="11%">调用日期<font color=#ff0000>*</font>：</TD>
      <TD noWrap align=left height=25 width="24%">&nbsp;<c:out value="${obj.taskdate}"/></TD>
      <TD noWrap align=left height=25 width="14%"> 
        <div align="right">最近一次运行<font color=#ff0000>*</font>：</div>
      </TD>
      <TD noWrap align=left height=25 width="51%">&nbsp;<c:out value="${obj.lastoperation}"/></TD>
    </TR>
    <TR> 
      <TD noWrap align=right height=25 width="11%">最近一次耗时<font color=#ff0000>*</font>：
      </TD>
      <TD noWrap align=left height=25 width="51%">&nbsp;<c:out value="${obj.consumtime}"/></TD>
      
      <TD noWrap align=left height=25 width="14%"> 
        <div align="right">是否激活<font color=#ff0000>*</font>：</div>
      </TD>
      <TD noWrap align=left height=25 width="51%">&nbsp;<c:out value="${obj.thisoperation}"/></TD>
    </TR>
    <TR>
    		<TD noWrap align=right height=25 width="11%">所在类名<font color=#ff0000>*</font>：</TD>
      <TD noWrap align=left height=25 width="24%">&nbsp;<c:out value="${obj.taskclass}"/></TD>
  	</TR>	
  <TABLE cellSpacing=0 cellPadding=10 width="100%" border=0>
  <TR> 
      <TD noWrap align=middle colSpan=6>
        <IMG style="cursor: hand" onclick="history.back(-1)" height=23 src="images/return.gif">
	 </TD>
  </TR>
  </TABLE>
</body>
</html>
