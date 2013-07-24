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
<title>采集内容管理</title>
<!--日期控件 开始-->
<script type="text/javascript" src="/plugin/jquery.datepick.package-4.0.5/jquery.datepick.js"></script>
<link href="/plugin/jquery.datepick.package-4.0.5/jquery.datepick-adobe.css" rel="stylesheet" type="text/css" />
<!--日期控件 结束-->
<script type="text/javascript">

	function querylist(){
		$("#reqType").attr("value","news.NewsActionHandler.listHistory");
		$("#form1").submit();
	}
	
</script>
</head>

<body>
<div class="ui_head"></div>



<form action="GeneralHandleSvt" method="post" name="form1" id="form1">
<input type="hidden" id="reqType" name="reqType" value="news.NewsActionHandler.listHistory">
<input type="hidden" id="objId" name="objId" value="" />
<input type="hidden" id="status" name="his_news$status" value="2" />

<table class="ui edit">
<tr class="title"><td colspan="4">查询条件</td></tr>
    <tr>
        <td width="10%">发布时间：</td>
        <td><input type="text" id="s_time" name="startDate" value="${param.startDate}" readonly="readonly" plugin="date" start="start" />
<span class="newfont06">-</span>
<input type="hidden" id="dateColumn" name="dateColumn" value="posttime" />
<input type="text" id="e_time" name="endDate" value="${param.endDate}" readonly="readonly" plugin="date" end="start" /></td>
    </tr>
    <tr>
        <td>标题：</td>
        <td><input type="text" id="title" name="his_news$title" value="${param.title}" /></td>
        
    </tr>
    <tr>
        <td>作者：</td>
        <td><input type="text" id="author" name="his_news$author" value="${param.author}" /></td>
        
    </tr>
    <tr>
    	<td></td>
        <td colspan="3"><input type="button" onclick="querylist()" value="查询" class="right-button08" />
        <input type="button" id="resetb" value="重填" /></td>
    </tr>
</table>
<table class="ui list">
	<tr class="title"><td class="title" colspan="12">信息手动处理</td></tr>
    <tr class="effect">
        <th>序号</th>
        <th>标题</th>
        <th>作者</th>
        <th>来源</th>
        <th><a href="javascript:setorderby('posttime')" id="posttime_orderby" class="order_ 
        	<c:if test="${orderby=='posttime' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='posttime' && order=='asc'}">order_down</c:if>
            ">发布时间</a></th>
        <th>操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr class="effect">
    <td>${status.index+1}</td>
    <td><a href="javascript:void(0)" onclick="show('${detail.id}')" ><m:out type="" value="${detail.title}" maxSize="12" /></a></td>
    <td>${detail.author}</td>
    <td>${detail.copyfrom}</td>
    <td><fmt:formatDate value="${detail.posttime}" pattern="yyyy-MM-dd HH:mm"/></td>
    <td><a href="/news.NewsActionHandler.showHis?objId=${detail.id}" >详细</a></td>
  </tr>
</c:forEach>
</table>

<div class="pages_bar">
<div class="pages_left">第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="news.NewsActionHandler.listHistory" type="simple" size="30" /></div>
</div>

</form>
</body>
</html>