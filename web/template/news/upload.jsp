<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=gbk" %>
<%@ page import="java.util.*"%>
<%@ page import="tool.dao.*"%>
<%@ page import="sand.depot.tool.system.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<META http-equiv=pragma content=no-cache>
<LINK href="<c:url value='/style/test.css'/>" rel=stylesheet type=text/css>
<script language="javascript" src="<c:url value='/scripts/public.js'/>"></script>
<script language="javascript" src="<c:url value='/scripts/date.js'/>"></script>
<script>

//
function addRecord()
{
	form1.submit();
}

function delRecord(a)
{
	form1.detailObjId.value = a;
	setReqType('basic.UserActionHandler.deleteDetail');
	form1.submit();
}
//
function saveRecord()
{

	setReqType('basic.UserActionHandler.save');
	form1.submit();
}
</script>
<META content="MSHTML 6.00.2800.1106" name=GENERATOR>

</HEAD>
<BODY text=#000000 bgColor=#efefef scroll=yes leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<TABLE  background=images/title_back1.gif  class=up cellSpacing=0 cellPadding=3 width="100%" border=0>
  <TBODY>
  <TR>
    <TD height=22><IMG height=16
      hspace=2 src="images/new.gif" width=16
    align=absMiddle></TD>
  <td align=right>
    <FONT color=#ffffff>
<c:if test="${!(empty param.tipInfo)}">
  <c:out value="${param.tipInfo}"/>
</c:if>

    </FONT>
    </td>
  </TR></TBODY></TABLE>


<p>&nbsp;</p>
<p align="center"></p>

<FORM action=GeneralHandleSvt method=post name=form1 style="MARGIN: 0px" ENCTYPE="multipart/form-data">

	<input type=hidden name="reqType" value="news.GpMailAH.doUpload">
	<input type=hidden name="objId" value=${obj.userid}>
	<input type=hidden name="detailObjId" value=>

<input type="hidden" name="TEST" value="good">
  <table width="75%" border="1" align="center">
    <tr> 
      <td><div align="center">邮件配置文件(UTF-8格式)：
          <input type="FILE" name="FILE1" size="30">
        </div></td>
    </tr>

    <tr> 
      <td><div align="center">
          <input type="submit" name="Submit" value="上传">
        </div></td>
    </tr>
  </table>
</FORM>


</BODY></HTML>