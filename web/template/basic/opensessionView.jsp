<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML><HEAD><TITLE>查看连接数!</TITLE>
<META content="text/html; charset=utf-8" http-equiv=Content-Type>
<META content=no-cache http-equiv=pragma>
<LINK 
href="style/test.css" rel=stylesheet type=text/css>
<META content="MSHTML 5.00.3103.1000" name=GENERATOR><jsp:include page="/inc.jsp" flush="false" />
</head>
<BODY bgColor=#efefef scroll=no text=#000000>
<TABLE border=0 cellPadding=0 cellSpacing=0 height="150" 
width="100%">
 
  <TR>
    <TD align=middle vAlign=top>
      <TABLE border=0 cellPadding=3 cellSpacing=0 width="100%">
    
        <TR>
          <TD background=images/title_back1.gif height=22 
            noWrap><FONT color=#000000><IMG align=absMiddle height=16 hspace=2 
            src="images/xp1.gif" 
      width=16>&nbsp;查看连接</FONT></TD>
        </TR></TABLE>

    </TD>
    </TR>
    </TABLE>
    <table>
    <tr>
    	<TD  align=center>最大连接数：<a href="/system/AdminAH/viewSnapConn">${maxopen}</a></TD>
    </tr>
    <tr>

	<TD  align=center>当前连接数：${Opensession}</TD>
    </tr>
    <tr>
    <TD  align=center>当前激活连接数：${activenum}</TD>
    </tr>
    <tr>
    <TD  align=center>当前空闲连接数：${idlenum}</TD>
    </tr>
    <!--
    <TD  align=center>回收检测空闲连接数：${evictionnum}</TD>-->
	<c:forEach var="con" items="${cons}"  varStatus="prop">
	<tr>
		<td align=left >
	${prop.index+1}	&nbsp;&nbsp;&nbsp; ${con.key}
   <br>
		${con.value}
		<p>
    </td>

	</tr>
</c:forEach>	
	<tr>
	<TD align=center heigh=5 ><input type=button value=" 返 回 "  style="CURSOR: hand" onclick=history.back(-1) height=23 src="images/return.gif" width=75> 
    </td>
	</tr>
	

	</TABLE></BODY></HTML>
