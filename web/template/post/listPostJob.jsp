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
<title>邮件发送任务管理</title>
<!--日期控件 **带时分** 开始-->
<script type="text/javascript" src="/plugin/jquery-calendar.js"></script>
<!--日期控件 **带时分** 结束-->
<script type="text/javascript">

		function render(){
			//alert("abcd");
			form1.action="/news.NewsActionHandler.render";
			form1.submit();
		
		}



	function openit(id){
		//alert("afdadf");
		window.open("/news.NewsActionHandler.show?objId="+id);
	}
	
	
	function show(id){
		$("#form1").attr("action","post.PostJobAH.showPostJob");
		$("#objId").attr("value",id);
		$("#form1").submit();
	}
	function querylist(){
		$("#reqType").attr("value","news.NewsActionHandler.list");
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
<%@ include file="menu.jsp"%>

<form action="/news.GpMailAH.listPostConfig" method="post" name="form1" id="form1">

<input type="hidden" id="tag_ids" name="tag_ids2" value="${tag_ids2}" />
<input type="hidden" id="objId" name="objId" value="" />
<input type="hidden" id="orderby" name="orderby" value="${orderby}"/>
<input type="hidden" id="order" name="order" value="${order}"/>

<table class="ui edit">
<tr class="title"><td colspan="6">查询条件</td></tr>
    <tr>
        <td>任务编号：</td>
        <td><input type="text" id="title" name="postjob$no" value="${obj.no}" /></td>

		<td>执行时间：</td>
        <td><input type="text" id="s_time" name="postjob$posttime" value="${obj.posttime}"  />
		</td>

        <td>生效状态：</td>
        <td><m:radio type="postjob_status" name="postjob$status" value="${obj.status}" /></td>
        
    </tr>
    <tr>
        <td>任务名称：</td>
        <td colspan=3><input type="text" size=80  id="author" name="postjob$name" value="${obj.name}" /></td>
        <td>客户邮件：</td>
        <td><input type="text" id="author" name="author" value="${author}" /></td>

	</tr>
    <tr>
    	        <td></td>

        <td colspan="3"><input type="button" onclick="querylist()" value="查询" class="right-button08" /><input type="button" id="resetb" value="重填" /></td>
		    	        <td></td>    	        <td></td>
    </tr>

</table>
<table class="ui list">
	<tr class="title"><td class="title" colspan="13">任务列表</td></tr>
    <tr class="effect">

        <th>任务编号</th>
        <th>任务名称</th>
        <th>预定执行时间</th>
        <th>状态</th>
        <th>执行的邮件策略</th>
        <th>最近执行时间</th>
        <th>客户数</th>
        <th>操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr class="effect">
  	<td><input type="checkbox" value="${detail.id}" name="outids"/>${detail.no}</td>
    <td><a href="/post.PostJobAH.show?objId=${detail.id}" >${detail.name}</a></td>
    <td>${detail.posttime}</td>
    <td>${detail.status} </td>
    <td>${detail.ruleid}</td>
    <td><fmt:formatDate value="${detail.lastposttime}" pattern="yyyy-MM-dd HH:mm"/></td>
    <td>${detail.customers}</td>
    <td><a href="javascript:void(0)" onclick="javascript:openit('${detail.id}')" ><img src="/images/button_edit.png" /></a>  | <a href="/news.NewsActionHandler.showHis?objId=${detail.his_news_id}" >原文</a></td>
  </tr>
</c:forEach>
	<tr class="edit">
    	<td colspan="13">
                <a class="select_all" href="javascript:void(0)">全选</a>/<a class="unselect_all" href="javascript:void(0)">反选</a>
<script type="text/javascript">
$(".select_all").click(function(){
	$("input[name='outids']").attr("checked",true);
});
$(".unselect_all").click(function(){
	$("input[name='outids']").attr("checked",false);
});
</script>
                <input type="button" class="button" id="dels"  onclick="javascript:render();" value="sendnow" />
                <input type="button" class="button" id="dels"  onclick="javascript:render();" value="enable" />
                <input type="button" class="button" id="dels"  onclick="javascript:render();" value="disable" />
                <input type="button" class="button" id="dels"  onclick="javascript:render();" value="delete" />
        </td>
    </tr>
</table>


</form>
</body>
</html>