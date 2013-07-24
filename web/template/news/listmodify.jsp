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
	
	function show(){
		alert('');
		$("#reqType").attr("value","news.GpMailAH.saveModify");
		//$("#objId").attr("value",id);
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



<form action="/news.GpMailAH.saveModify" method="post" name="form1" id="form1">

<input type="hidden" id="tag_ids" name="tag_ids2" value="${tag_ids2}" />
<input type="hidden" id="objId" name="objId" value="${objid}" />
<input type="hidden" id="orderby" name="orderby" value="${orderby}"/>
<input type="hidden" id="order" name="order" value="${order}"/>

<table class="ui edit">

</table>
<table class="ui list">
	<tr class="title"><td class="title" colspan="13">信息手动处理</td></tr>
    <tr class="effect">
        <th>选择</th>
        <th>序号</th>
        <th>标题</th>
        <th>summary</th>
        <th>标签</th>
        <th>作者</th>
        <th>发布时间</th>
        <th>采集时间</th>
        <th>分类</th>
        <th>客户重要度</th>
        <th>GP重要度</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
	<c:forEach var="item" items="${objList}">   
		<c:if test="${fn:length(item.value)>0}">
		  <tr class="effect">
		  <td colspan=13 >${item.key}</td>
		  </tr>
		  </c:if>
	  <c:forEach var="detail" items="${item.value}" varStatus="status">
		
  <tr class="effect">
  	<td><input type="checkbox" value="${detail.id}" ${detail.checked=="true"?"checked":""} name="outids"/></td>
    <td>${status.index+1}</td>
    <td><a href="/news.NewsActionHandler.show?objId=${detail.id}" >${detail.title}</a></td>
    <td><m:out value="${detail.summary}" maxSize="20" /> </td>
    <td>${detail.tags} </td>
    <td>${detail.author}</td>
    <td><fmt:formatDate value="${detail.posttime}" pattern="yyyy-MM-dd HH:mm"/></td>
    <td><fmt:formatDate value="${detail.createdate}" pattern="yyyy-MM-dd HH:mm"/></td>
    <td><c:if test="${detail.sort=='1'}">I</c:if><c:if test="${detail.sort=='0'}">K</c:if></td>
    <td><m:out type="urgent" value="${detail.urgent}" /></td>
    <td><m:out type="importance" value="${detail.importance}" /></td>
    <td><m:out type="news_status" value="${detail.status}" /></td>
    <td><a href="javascript:void(0)" onclick="javascript:openit('${detail.id}')" ><img src="/images/button_edit.png" /></a> </td>
  </tr>
		
		</c:forEach>  
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
                <input type="button" class="button" id="dels"  onclick="javascript:show();" value="修改完成" />
        </td>
    </tr>
</table>


</form>
</body>
</html>