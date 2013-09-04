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



<form method="post" action="/GeneralHandleSvt" id="form1" name="form1">
<input type="hidden" id="reqType" name="reqType" value="customer.RulesActionHandler.list" />
<input type="hidden" id="id" name="tag$id" value="" />

<table class="ui edit">
<tr class="title"><td colspan="4">查询条件</td></tr>
    <tr>
        <td>客户姓名：</td>
        <td><input type="text" name="customers$name" value="${param.customers$name}" /></td>
        <td>客户地址：</td>
        <td><input type="text" name="customers$address" value="${param.customers$address}" /></td>
    </tr>
    <tr>
        <td>创建时间：</td>
        <td colspan="3"><input type="text" id="s_time" name="startDate" value="${param.startDate}" readonly="readonly" plugin="date" start="start" />
<span class="newfont06">-</span><input type="hidden" name="dateColumn" value="createdate" />
<input type="text" id="e_time" name="endDate" value="${param.endDate}" readonly="readonly" plugin="date" end="start" /></td>
    </tr>
    <tr>
    	<td></td>
        <td colspan="3"><input type="button" onclick="querylist()" value="查询" class="right-button08" />
        <input type="button" id="resetb" value="重填" /></td>
    </tr>
</table>

<table class="ui list">
	<tr class="title"><td class="title" colspan="7">客户/组</td><td class="title" style="text-align:right" ><a style=" color: #FFFFFF; " href="/customer.CustomerActionHandler.show?job=default" >Add客户/组</a></td></tr>
    <tr class="effect">
        <th>选择</th>
        <th>序号</th>
        <th>客户姓名</th>
        <th>邮箱地址</th>
        <th>创建日期</th>
        <th>当前任务</th>
        <th>最后邮件时间</th>
        <th>Feedback(点击More)</th>
        <th>操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
    <tr class="no_padding effect">
        <td><input type="checkbox" value="${detail.id}" name="delid"/></td>
        <td>${status.index+1}</td>
        <td>${detail.name}</td>
        <td><m:out type="" value="${detail.address}" maxSize="10" /></td>
        <td><fmt:formatDate value="${detail.createdate}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td><m:out type="" value="${detail.jobs}" maxSize="15" /></td>
        <td>${detail.lastposttime}</td>
        <td><a href="/customer.CustomerActionHandler.feedbackList?userclicks$customerid=${detail.id}&operator=open" >邮件more</a><a href="/customer.CustomerActionHandler.feedbackList?userclicks$customerid=${detail.id}&operator=dis_like" >(dis)like</a></td>
        <td><a href="/customer.CustomerActionHandler.show?objId=${detail.id}" ><img alt="点击编辑" src="/images/button_edit.png" /></a></td>
    </tr>
</c:forEach>
	<tr class="edit">
    	<td colspan="7">
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
<script type="text/javascript">
	function querylist(){
		$("#reqType").attr("value","customer.CustomerActionHandler.list");
		$("#form1").submit();
	}

	$(function(){
		$("#dels").click(function(){
			if(confirm('你确实要删除吗?')){
				$("#reqType").attr("value","customer.CustomerActionHandler.deletes");
				$("#form1").submit();
			}
		});
		
	})
</script>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td height="6"><img src="/images/spacer.gif" width="1" height="1" /></td>
    </tr>
    <tr>
      <td height="33"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="right-font08">
          <tr>
            <td width="150">共 <span class="right-text09">${pageVariable.totalpage}</span> 页 | 第 <span class="right-text09">${pageVariable.npage+1}</span> 页</td>
            <td align="right"><m:page action="/customer.CustomerActionHandler.list" size="30" /></td>
          </tr>
      </table></td>
    </tr>
</table>

</form>
</body>
</html>