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
<title>邮件发送服务器配置</title>
<!--日期控件 **带时分** 开始-->
<script type="text/javascript" src="/plugin/jquery-calendar.js"></script>
<!--日期控件 **带时分** 结束-->
<script type="text/javascript">

		function render(){
			//alert("abcd");
			form1.action="/news.NewsActionHandler.render";
			form1.submit();
		
		}



	function editit(id){
		//alert("afdadf");
		//window.open("/post.PostJobAH.show?objId="+id);
		$("#form1").attr("action","post.MailServerAH.show");
		$("#objId").attr("value",id);
		$("#form1").submit();

	}
	
	
	function deleteit(id){
		$("#form1").attr("action","post.MailServerAH.delete");
		$("#objId").attr("value",id);
		$("#form1").submit();
	}
	function deleteall(){
		$("#form1").attr("action","post.MailServerAH.deleteAll");
//		$("#objId").attr("value",id);
		$("#form1").submit();
	}	
	function disableit(id){
		$("#form1").attr("action","post.MailServerAH.disable");
		$("#objId").attr("value",id);
		$("#form1").submit();
	}
	function disableall(){
		$("#form1").attr("action","post.MailServerAH.disableAll");
//		$("#objId").attr("value",id);
		$("#form1").submit();
	}

	function sendall(){
		$("#form1").attr("action","post.MailServerAH.sendAll");
//		$("#objId").attr("value",id);
		$("#form1").submit();
	}
	function querylist(){
		$("#reqType").attr("value","news.MailServerAH.list");
		$("#form1").submit();
	}
	
	function setorderby(orderType) {
		$('#orderby').attr('value',orderType);
		if($('#'+orderType+'_orderby').hasClass("order_up")){
			//向上
			
			$('#order').attr('value',"asc");
		}else{
			//向下	
			$('#order').attr('value',"desc");
		}
		querylist();
		
		
	}
</script>
</head>

<body>
<div class="ui_head"></div>
<%@ include file="menu2.jsp"%>

<form action="/post.PostJobAH.list" method="post" name="form1" id="form1">

<input type="hidden" id="tag_ids" name="tag_ids2" value="${tag_ids2}" />
<input type="hidden" id="objId" name="objId" value="" />
<input type="hidden" id="orderby" name="orderby" value="${orderby}"/>
<input type="hidden" id="order" name="order" value="${order}"/>


<table class="ui list">
	<tr class="title"><td class="title" colspan="13">配置列表</td></tr>
    <tr class="effect">


        <th>名称</th>
        <th>发送服务器</th>
        <th>接收服务器</th>
        <th>用户名</th>
        <th>显示名称</th>
        <th>发送地址</th>
        <th>操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr class="effect">

    <td><a href="/post.MailServerAH.show?objId=${detail.id}" >${detail.name}</a></td>
    <td>${detail.smtp}</td>
    <td>${detail.pop3}</td>
    <td>${detail.username}</td>
    <td>${detail.showname}</td>
    <td>${detail.fromaddr}</td>
    <td><a href="javascript:void(0)" onclick="javascript:editit('${detail.id}')" > edit </a>  |  
	
  
	<a href="javascript:void(0)" onclick="javascript:deleteit('${detail.id}')" > delete </a>
	</td>
  </tr>
</c:forEach>
	<tr class="edit">
    	<td colspan="13">

<script type="text/javascript">
$(".select_all").click(function(){
	$("input[name='outids']").attr("checked",true);
});
$(".unselect_all").click(function(){
	$("input[name='outids']").attr("checked",false);
});
</script>

        </td>
    </tr>
</table>

<div class="pages_bar">
<div class="pages_left">共 <span class="orange">${pageVariable.totalpage}</span> 页 | 第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="post.MailServerAH.list" size="30" /></div>
</div>


</form>
</body>
</html>