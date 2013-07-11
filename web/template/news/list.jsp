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
	function GetDateStr(AddDayCount){
		var dd = new Date(); 
		dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
		var y = dd.getFullYear(); 
		var m = dd.getMonth()+1;//获取当前月份的日期 
		if(m<10)m="0"+m;
		var d = dd.getDate(); 
		if(d<10)d="0"+d;
		return y+"-"+m+"-"+d; 
	} 
	
	function show(id,rownum){
		$("#reqType").attr("value","news.NewsActionHandler.show");
		$("#objId").attr("value",id);
		$("#rownum").attr("value",rownum);
		$("#page").attr("value",$("#ttpages").val()-1);
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



<form action="/GeneralHandleSvt" method="post" name="form1" id="form1">
<input type="hidden" id="reqType" name="reqType" value="news.NewsActionHandler.list">
<input type="hidden" id="tag_ids" name="tag_ids2" value="${tag_ids2}" />
<input type="hidden" id="objId" name="objId" value="" />
<input type="hidden" id="orderby" name="orderby" value="${orderby}"/>
<input type="hidden" id="order" name="order" value="${order}"/>
<input type="hidden" id="rownum" name="rownum" value=""/>
<input type="hidden" id="job" name="job" value="${param.job}"/>
<m:token/>

<table class="ui edit">
<tr class="title"><td colspan="4">查询条件</td></tr>
    <tr>
        <td width="10%">发布时间：</td>
        <td width="40%"><input type="text" id="s_time" name="startDate" value="${startDate}" readonly="readonly" plugin="date2" start="start" />
<span class="newfont06">-</span>
<input type="text" id="e_time" name="endDate" value="${endDate}" readonly="readonly" plugin="date2" end="start" /></td>
        <td width="10%">状态：</td>
        <td width="40%"><m:radio type="news_status_all" name="status" value="${status}" /></td>
    </tr>
    <tr>
        <td>标题：</td>
        <td><input type="text" id="title" name="title" value="${title}" /></td>
        <td>客户重要度：</td>
        <td><m:radio type="urgent_all" name="urgent" value="${urgent}" /></td>
    </tr>
    <tr>
        <td>作者：</td>
        <td><input type="text" id="author" name="author" value="${author}" /></td>
        <td>GP重要度：</td>
        <td><m:radio type="importance_all" name="importance" value="${importance}" /></td>
    </tr>
    <tr>
        <td>标签：</td>
        <td>
<input type="hidden" id="tags" name="tags2" value="${param.tags2}" />
<ul style="float: none;height: 80px;width: 330px;" id="tags_result" class="tags_result select_label"></ul>
<c:set var="tagsList_pf" value="${tags}" />
<%@ include file="/template/tag/tag_plugin.jsp"%>
<style type="text/css">
#tag_text{ margin-left:0}
</style>
<script type="text/javascript">
$(function(){
	$("#dels").click(function(){
		if(confirm('你确定要删除吗?')){
			$("#reqType").attr("value","news.NewsActionHandler.deletes");
			
			$("#rownum").attr("value",rownum);
			$("#page").attr("value",$("#ttpages").val()-1);
			$("#form1").submit();
		}
	});
});
</script>
        </td>
        <td>文章分类：</td>
        <td><m:radio type="sort_all" name="sort" value="${sort}" /></td>
    </tr>
    <tr>
    	<td></td>
        <td colspan="3"><input type="button" onclick="querylist()" value="查询" class="right-button08" />
        <input type="button" id="resetb" value="重填" /></td>
    </tr>
</table>
<table class="ui list">
	<tr class="title"><td class="title" colspan="12">信息手动处理</td><td class="title" ><a style=" color: #FFFFFF" href="/news.NewsActionHandler.showAdd?job=default" >Add文章</a></td></tr>
    <tr class="effect">
        <th>选择</th>
        <th>序号</th>
        <th>标题</th>
        <th>中文summary</th>
        <th>作者</th>
        <th>来源</th>
        <th><a href="javascript:setorderby('posttime')" id="posttime_orderby" class="order_ 
        	<c:if test="${orderby=='posttime' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='posttime' && order=='asc'}">order_down</c:if>
            ">发布时间</a></th>
        <th><a href="javascript:setorderby('createdate')" id="createdate_orderby" class="order_ 
        	<c:if test="${orderby=='createdate' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='createdate' && order=='asc'}">order_down</c:if>
            ">采集时间</a></th>
        <th>分类</th>
        <th width="7px"><a href="javascript:setorderby('urgent')" id="urgent_orderby" class="order_ 
        	<c:if test="${orderby=='urgent' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='urgent' && order=='asc'}">order_down</c:if>
            ">客户重要度</a></th>
        <th width="7px"><a href="javascript:setorderby('importance')" id="importance_orderby" class="order_ 
        	<c:if test="${orderby=='importance' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='importance' && order=='asc'}">order_down</c:if>
            ">GP重要度</a></th>
        <th>状态</th>
        <th>操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr class="effect">
  	<td><input type="checkbox" value="${detail.id}" name="delid"/></td>
    <td>${status.index+1}</td>
    <td><a href="javascript:void(0)" onclick="show('${detail.id}')" ><m:out type="" value="${detail.title}" maxSize="12" /></a></td>
    <td><m:out type="" value="${detail.c_summary}" maxSize="12" /></td>
    <td>${detail.author}</td>
    <td>${detail.copyfrom}</td>
    <td><fmt:formatDate value="${detail.posttime}" pattern="yyyy-MM-dd HH:mm"/></td>
    <td><fmt:formatDate value="${detail.createdate}" pattern="yyyy-MM-dd HH:mm"/></td>
    <td><c:if test="${detail.sort=='1'}">I</c:if><c:if test="${detail.sort=='0'}">K</c:if></td>
    <td><m:out type="urgent" value="${detail.urgent}" /></td>
    <td><m:out type="importance" value="${detail.importance}" /></td>
    <td><m:out type="news_status" value="${detail.status}" /></td>
    <td><a href="javascript:void(0)" onclick="show('${detail.id}','${pageVariable.npage*pageVariable.pagesize+status.index}')" ><img src="/images/button_edit.png" /></a> <c:if test="${detail.his_news_id!='' && detail.his_news_id!=null}">| <a href="/news.NewsActionHandler.showHis?objId=${detail.his_news_id}" >原文</a></c:if></td>
  </tr>
</c:forEach>
	<tr class="edit">
    	<td colspan="12">
                <a class="select_all" href="javascript:void(0)">全选</a>/<a class="unselect_all" href="javascript:void(0)">反选</a>
<script type="text/javascript">
$(".select_all").click(function(){
	$("input[name='delid']").attr("checked",true);
});
$(".unselect_all").click(function(){
	$("input[name='delid']").attr("checked",false);
});
</script>
                <input type="button" class="button" id="dels" value="批量删除" />
        </td>
    </tr>
</table>

<div class="pages_bar">
<div class="pages_left">共 <span class="orange">${pageVariable.totalpage}</span> 页 | 第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="news.NewsActionHandler.list" size="30" /></div>
</div>

</form>
</body>
</html>