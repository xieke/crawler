<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page session="false" %>


<html>
<head>
<title>任务历史详细显示</title>
<LINK href="style/test.css" type=text/css rel=stylesheet>
</head>
<BODY text=#000000 bgColor=#efefef scroll=yes>
<TABLE class=up cellSpacing=0 cellPadding=3 width="100%" border=0 background=images/title_back1.gif>
	<TR >
		<TD height=22>
			<IMG height=16 hspace=2 src="images/new.gif" width=16 align=absMiddle>任务历史详细
		</TD>		
	</TR>
</TABLE>
  <TABLE cellSpacing=0 cellPadding=2 width="100%" border=1>
    <TR> 
      <TD noWrap align=right height=20 width="11%">任务名称<FONT color=#ff0000>*</FONT>：</TD>
      <TD noWrap align=left height=20 width="24%">&nbsp;<c:out value="${obj.taskid.taskname}"/></TD>
      <TD noWrap align=left height=20 width="14%"> 
        <div align="right">运行结果<font color=#ff0000>*</font>：</div>
      </TD>
      <TD noWrap align=left height=20 width="51%">&nbsp;<c:out value="${obj. result}"/></TD>
    </TR>
    <TR> 
      <TD noWrap align=right height=25 width="11%">运行时间<font color=#ff0000>*</font>：</TD>
      <TD noWrap align=left height=25 width="24%">&nbsp;<c:out value="${obj.rundate}"/></TD>
      <TD noWrap align=left height=25 width="14%"> 
        <div align="right">耗时<font color=#ff0000>*</font>：</div>
      </TD>
      <TD noWrap align=left height=25 width="51%">&nbsp;<c:out value="${obj.usetime}"/></TD>
    </TR>
    <TR> 
      <TD noWrap align=right height=25 width="11%" >备注<font color=#ff0000>*</font>：</TD>
      <TD noWrap align=left height=25 width="24%" colspan="3">&nbsp;<c:out value="${obj.remark}"/></TD>
     
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
