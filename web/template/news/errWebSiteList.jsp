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
<title>采集出错的源</title>
<!--日期控件 **带时分** 开始-->
<script type="text/javascript" src="/plugin/jquery-calendar.js"></script>
<!--日期控件 **带时分** 结束-->
<script type="text/javascript">
	function querylist(){
		$("#reqType").attr("value","news.ErrWebSiteActionHandler.list");
		$("#form1").submit();
	}
	$(function(){
		$("#dels").click(function(){
			if($("input[type=checkbox]:checked").length<1){
				alert("您未选择要处理的记录!");
				return false;	
			}
			
			if(confirm('你确定所有的都要处理吗?')){
				$("#reqType").attr("value","news.ErrWebSiteActionHandler.disposes");
				$("#form1").submit();
			}
		});
	});
	
</script>
</head>

<body>
<div class="ui_head"></div>



<form action="/GeneralHandleSvt" method="post" name="form1" id="form1">
<input type="hidden" id="reqType" name="reqType" value="news.ErrWebSiteActionHandler.list">

<m:token/>

<table class="ui edit">
<tr class="title"><td colspan="4">查询条件</td></tr>
    <tr>
        <td width="10%">处理状态：</td>
        <td width="40%"><m:radio type="news_status_all" name="err_web_site$status" value="${param.err_web_site$status}" /></td>
    </tr>
    <tr>
    	<td></td>
        <td colspan="3"><input type="button" onclick="querylist()" value="查询" class="right-button08" />
        <input type="button" id="resetb" value="重填" /></td>
    </tr>
</table>
<table class="ui list">
	<tr class="title"><td class="title" colspan="9">采集出错的源信息</td></tr>
    <tr class="effect">
        <th>选择</th>
        <th>序号</th>
        <th>来源网站</th>
        <th>url</th>
        <th>出错的原因</th>
        <th>频道标签</th>
        <th>状态</th>
        <th>采集时间</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr class="effect">
  	<td><c:if test="${detail.status!='1'}"><input type="checkbox" value="${detail.id}" name="delid"/></c:if><c:if test="${detail.status=='1'}">-</c:if></td>
    <td>${status.index+1}</td>
    <td>${detail.copyfrom}</td>
    <td>${detail.copyfromurl}</td>
    <td>${detail.reson}</td>
    <td>${detail.catalog}</td>
    <td><m:out type="news_status" value="${detail.status}" /></td>
    <td><fmt:formatDate value="${detail.createdate}" pattern="yyyy-MM-dd HH:mm"/></td>
  </tr>
</c:forEach>
	<tr class="edit">
    	<td colspan="13">
                <a class="select_all" href="javascript:void(0)">全选</a>/<a class="unselect_all" href="javascript:void(0)">反选</a>
<script type="text/javascript">
$(".select_all").click(function(){
	$("input[name='delid']").attr("checked",true);
});
$(".unselect_all").click(function(){
	$("input[name='delid']").attr("checked",false);
});
</script>
                <input type="button" class="button" id="dels" value="批量处理" />
        </td>
    </tr>
</table>

<div class="pages_bar">
<div class="pages_left"> 第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="news.ErrWebSiteActionHandler.list" size="30" /></div>
</div>

</form>
</body>
</html>