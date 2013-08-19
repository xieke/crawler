<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>
<!--日期控件 开始-->
<script type="text/javascript" src="/plugin/jquery.datepick.package-4.0.5/jquery.datepick.js"></script>
<link href="/plugin/jquery.datepick.package-4.0.5/jquery.datepick-adobe.css" rel="stylesheet" type="text/css" />
<!--日期控件 结束-->
<title>TAG</title>
</head>

<body>
<div class="ui_head"></div>


<div class="submenu">
		<span class="button">
                <a href="javascript:void(0)" onclick="history.back(-1)">返回上一级</a>
		</span>
</div>
<form method="post" action="/GeneralHandleSvt" name="form1" id="form1">
<input type="hidden" id="reqType" name="reqType" value="customer.CustomerActionHandler.feedbackList" />
<input type="hidden" id="operator" name="operator" value="${operator}" />
<input type="hidden" id="userclicks_customerid" name="userclicks$customerid" value="${param.userclicks$customerid}" />

<table class="ui edit" style="display:none">
<tr class="title"><td colspan="4">查询条件</td></tr>
    <tr>
        <td>状态：</td>
        <td><input type="text" name="customers$name" value="${param.customers$name}" /></td>
        <td>客户地址：</td>
        <td><input type="text" name="customers$address" value="${param.customers$address}" /></td>
    </tr>
    <tr>
        <td>发送邮件时间：</td>
        <td colspan="3"><input type="text" id="s_time" name="startDate" value="${param.startDate}" readonly="readonly" plugin="date" start="start" />
<span class="newfont06">-</span><input type="hidden" name="dateColumn" value="senddate" />
<input type="text" id="e_time" name="endDate" value="${param.endDate}" readonly="readonly" plugin="date" end="start" /></td>
    </tr>
    <tr>
    	<td></td>
        <td colspan="3"><input type="button" onclick="querylist()" value="查询" class="right-button08" />
        <input type="button" id="resetb" value="重填" /></td>
    </tr>
</table>

<table class="ui list">
	<tr class="title"><td class="title" colspan="8">feedback文章列表</td></tr>
    <tr class="effect">
        <th>序号</th>
        <th>客户名称</th>
        <th>文章标题</th>
        <th>文章标签</th>
        <th>发布时间</th>
        <th>发邮件时间</th>
        <th>操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
    <tr class="no_padding effect">
        <td>${status.index+1}</td>
        <td>${detail.customerid.name}</td>
        <td><a target="_blank" href="/news.NewsActionHandler.showIt?objId=${detail.newsid.id}&email=${detail.email}" ><m:out type="" value="${detail.newsid.title}" maxSize="15" /></a></td>
        <td><m:out type="" value="${detail.newsid.tag}" maxSize="15" /></td>
        <td><fmt:formatDate value="${detail.newsid.posttime}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td><fmt:formatDate value="${detail.newsid.senddate}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td><c:if test="${detail.operator=='open'}">点击</c:if><c:if test="${detail.operator=='like'}">喜欢</c:if><c:if test="${detail.operator=='dislike'}">不喜欢</c:if></td>
    </tr>
</c:forEach>
</table>
<script type="text/javascript">
	function querylist(){
		$("#reqType").attr("value","customer.CustomerActionHandler.feedbackList");
		$("#form1").submit();
	}

</script>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td height="6"><img src="/images/spacer.gif" width="1" height="1" /></td>
    </tr>
    <tr>
      <td height="33"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="right-font08">
          <tr>
            <td width="150">共 <span class="right-text09">${pageVariable.totalpage}</span> 页 | 第 <span class="right-text09">${pageVariable.npage+1}</span> 页</td>
            <td align="right"><m:page action="customer.CustomerActionHandler.feedbackList" size="30" /></td>
          </tr>
      </table></td>
    </tr>
</table>

</form>
</body>
</html>