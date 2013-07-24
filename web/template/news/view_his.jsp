<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>采集内容管理</title>
<link href="/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/plugin/jquery-1.5.1.min.js"></script>

<!--日期控件 开始-->
<script type="text/javascript" src="/plugin/jquery.datepick.package-4.0.5/jquery.datepick.js"></script>
<link href="/plugin/jquery.datepick.package-4.0.5/jquery.datepick-adobe.css" rel="stylesheet" type="text/css" />
<!--日期控件 结束-->

</head>


<body>
<form action="GeneralHandleSvt" method="post" name="form1" id="form1">
<input type="hidden" id="reqType" name="reqType" value="news.NewsActionHandler.list">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="30">      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="62" background="/images/nav04.gif">
            
		   <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
		    <tr>
			  <td><input name="Submit3" type="button" onclick="window.history.back()" class="right-button07" value="返回" /></td>	
		    </tr>
          </table></td>
        </tr>
    </table></td></tr>
  <tr>
    <td><table id="subtree1" style="DISPLAY: " width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" class="font42">
				<table width="100%" border="0" cellpadding="4" cellspacing="1" bgcolor="#464646" class="newfont03">
				 <tr class="CTitle" >
                    	<td height="22" colspan="7" align="center" style="font-size:16px">查看采集原文</td>
                  </tr>
                  <tr bgcolor="#EEEEEE">
    <th width="100">标题：</th>
    <th>${obj.title}</th>
                  </tr>
  <tr bgcolor="#FFFFFF">
  	<td align="center">内容：</td>
    <td>${obj.content}</td>
  </tr>
  <tr bgcolor="#FFFFFF">
  	<td align="center">作者：</td>
    <td>${obj.author}</td>
  </tr>
  <tr bgcolor="#FFFFFF">
  	<td align="center">采集时间：</td>
    <td>${obj.posttime}</td>
  </tr>
  <tr bgcolor="#FFFFFF">
  	<td align="center">来源网站：</td>
    <td>${obj.copyfrom}</td>
  </tr>
  <tr bgcolor="#FFFFFF">
  	<td align="center">原始链接：</td>
    <td><a href="${obj.copyfromurl}" target="_blank">${obj.copyfromurl}</a></td>
  </tr>
            </table></td>
        </tr>
      </table>
      </td>
  </tr>
</table>
</form>
</body>
</html>
