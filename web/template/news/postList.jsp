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
<title>邮件配置管理</title>
<!--日期控件 **带时分** 开始-->
<script type="text/javascript" src="/plugin/jquery-calendar.js"></script>
<!--日期控件 **带时分** 结束-->
<script type="text/javascript">

		function render(){
			//alert("abcd");
			form1.action="/news.NewsActionHandler.render";
			form1.submit();
		
		}


	
	function show(id){
		//alert('abcd');
		$("#form1").attr("action","news.GpMailAH.showPost");
		$("#objId").attr("value",id);
		$("#form1").submit();
	}
	function querylist(){
		$("#reqType").attr("value","news.NewsActionHandler.list");
		$("#form1").submit();
	}
	

</script>
</head>

<body>
<div class="ui_head"></div>



<form action="/news.NewsActionHandler.listMail" method="post" name="form1" id="form1">

<input type="hidden" id="tag_ids" name="tag_ids2" value="${tag_ids2}" />
<input type="hidden" id="objId" name="objId" value="" />
<input type="hidden" id="orderby" name="orderby" value="${orderby}"/>
<input type="hidden" id="order" name="order" value="${order}"/>


<table class="ui list">
	<tr class="title"><td class="title" colspan="12">邮件配置手动处理</td></tr>
    <tr class="effect">

        <th>序号</th>
        <th>subject</th>
        <th>name</th>
        <th>发送周期</th>
        <th>文章数</th>
        <th>问候语</th>
        <th>结束语</th>
        <th>状态</th>
        <th>重要度</th>
        <th>语言</th>
        <th>联系电话</th>
        <th>上次发送时间</th>
        <th>操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr class="effect">

    <td>${detail.serialno}</td>
    <td>${detail.subject}</td>
    <td>${detail.name}  ${detail.address}</td>
    <td>${detail.cycle} </td>
    <td>${detail.limit}</td>
    <td>${detail.greeting}</td>
    <td>${detail.ending}</td>
    <td>${detail.enable}</td>
    <td>${detail.urgent}</td>
    <td>${detail.lang}</td>
    <td>${detail.telphone}</td>
    <td><fmt:formatDate value="${detail.posttime}" pattern="yyyy-MM-dd HH:mm"/></td>
    <td><a href="javascript:void(0)" onclick="javascript:show('${status.index}')" ><img src="/images/button_edit.png" /></a></td>
  </tr>
</c:forEach>
	<tr class="edit">
    	<td colspan="12">


        </td>
    </tr>
</table>


</form>
</body>
</html>