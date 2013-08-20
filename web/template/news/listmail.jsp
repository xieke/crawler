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
			form1.action="/news.NewsActionHandler.render2";
			form1.submit();
		
		}
		function sendmail(){
			//alert("abcd");
			form1.action="/news.NewsActionHandler.sendMail";
			form1.submit();
		
		}
		function switchit(id){
			//alert("abcd");
			form1.objId.value=id;
			form1.action="/news.NewsActionHandler.switchIssue";
			form1.submit();
		
		}
	$(function(){
		//refreshTags();
	});
	

	function openit(id){
		//alert("afdadf");
		window.open("/news.NewsActionHandler.show?objId="+id+"&from=mail");
	}
	
	
	function show(id){
		$("#reqType").attr("value","news.NewsActionHandler.show");
		$("#objId").attr("value",id);
		$("#form1").submit();
	}
	function querylist(){
		$("#reqType").attr("value","news.NewsActionHandler.listMail");
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



<form action="/news.NewsActionHandler.listMail" method="post" name="form1" id="form1">
<m:token/>
<input type="hidden" id="tag_ids" name="tag_ids2" value="${tag_ids2}" />
<input type="hidden" id="objId" name="objId" value="" />
<input type="hidden" id="orderby" name="orderby" value="${orderby}"/>
<input type="hidden" id="order" name="order" value="${order}"/>

<table class="ui edit">
<tr class="title"><td colspan="4">查询条件</td></tr>
    <tr>
        <td width="10%">发布时间：</td>
        <td><input type="text" id="s_time" name="startDate" value="${startDate}" readonly="readonly" plugin="date2" start="start" />
<span class="newfont06">-</span>
<input type="text" id="e_time" name="endDate" value="${endDate}" readonly="readonly" plugin="date2" end="start" /></td>
        <td>状态：</td>
        <td><m:radio type="news_status_all" name="status" value="${status}" /></td>
    </tr>
    <tr>
        <td width="10%">最后更新时间：</td>
        <td><input type="text" id="s_time1" name="startDate1" value="${startDate1}" readonly="readonly" plugin="date2" start="start1" />
	<span class="newfont06">-</span>
	<input type="text" id="e_time1" name="endDate1" value="${endDate1}" readonly="readonly" plugin="date2" end="start1" /></td>
        <td></td>
        <td></td>
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

        <td><input type="hidden" id="tags" name="tags2" value="${param.tags2}" />
<ul id="tags_result" class="tags_result select_label"></ul>
<c:set var="tagsList_pf" value="${tags}" />
<%@ include file="/template/tag/tag_plugin.jsp"%>
        </td>


        <td>文章分类：</td>
        <td><m:radio type="sort_all" name="sort" value="${sort}" /></td>
    </tr>
    <tr>
        <td>排序：</td>
        <td><input type="text" id="orderby" name="orderby" value=" importance asc , posttime desc " /></td>

		<td>输出文章数：</td>
        <td><input type="text" id="pagesize" name="pagesize" value="${pagesize}" /></td>

    </tr>
    <tr>
        <td>摘要:</td>
        <td>中文非空<m:checkbox name="summary" value="notnull" checkValue="${summary}" /> &nbsp;
		英文非空<m:checkbox name="esummary" value="notnull" checkValue="${esummary}" />
		</td>

		<td>发布：</td>
        <td><m:radio type="news_status_all" name="issue" value="${issue}" /></td>

    </tr>
    <tr>
    	        <td>语言：</td>
        <td><m:radio type="lang" name="lang" value="${lang}" /></td>

        <td>标签:</td>
        <td><input type="text" id="tag" name="tag" value=" 非空 " /></td>
    </tr>
    <tr>
    	        <td></td>

        <td colspan="3"><input type="button" onclick="querylist()" value="查询" class="right-button08" />
        <input type="button" id="resetb" value="重填" /></td>
    </tr>

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
        <th><a href="javascript:setorderby('posttime')" id="posttime_orderby" class="order_ 
        	<c:if test="${orderby=='posttime' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='posttime' && order=='asc'}">order_down</c:if>
            ">最后更新时间</a></th>
        <th><a href="javascript:setorderby('createdate')" id="createdate_orderby" class="order_ 
        	<c:if test="${orderby=='createdate' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='createdate' && order=='asc'}">order_down</c:if>
            ">采集时间</a></th>
            <!-- 
        <th>分类</th>
        <th><a href="javascript:setorderby('urgent')" id="urgent_orderby" class="order_ 
        	<c:if test="${orderby=='urgent' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='urgent' && order=='asc'}">order_down</c:if>
            ">客户重要度</a></th>
        <th><a href="javascript:setorderby('importance')" id="importance_orderby" class="order_ 
        	<c:if test="${orderby=='importance' && order=='desc'}">order_up</c:if>
            <c:if test="${orderby=='importance' && order=='asc'}">order_down</c:if>
            ">GP重要度</a></th>
         -->
        <th>发布操作</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <tr class="effect">
  	<td><input type="checkbox" value="${detail.id}" name="outids"/></td>
    <td>${status.index+1}</td>
    <td><a href="javascript:void(0)" onclick="javascript:openit('${detail.id}')" >${detail.title}</a></td>
    <td>英：<m:out value="${detail.summary}" maxSize="20" /><br>中：<m:out value="${detail.c_summary}" maxSize="20" />  </td>
    <td>${detail.tags} </td>
    <td>${detail.author}</td>
    
    <td><fmt:formatDate value="${detail.modifydate}" pattern="yyyy-MM-dd HH:mm"/></td>
    <td><fmt:formatDate value="${detail.createdate}" pattern="yyyy-MM-dd HH:mm"/></td>
    <!-- 
    <td><c:if test="${detail.sort=='1'}">I</c:if><c:if test="${detail.sort=='0'}">K</c:if></td>
    <td><m:out type="urgent" value="${detail.urgent}" /></td>
    <td><m:out type="importance" value="${detail.importance}" /></td>
     -->
    <td>${detail.issue=="1"?"<font color=red>Y</font>":"N"}<input type=button id=isissue onclick="javascript:switchit('${detail.id}')" value='切 换'/></td>
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
<br></br>
邮件标题：<input  id="subject" name="subject"  size=80 value="Goldpebble Research Customized News" />
<br></br>
        邮箱地址：<input  id="email" name="email" value="" />         
        <m:radio name="mailserver" value="" type="mailservers"/>
        <input type="button" class="button" id="dels"  onclick="javascript:render();" value="预览邮件" />
        <input type="submit" onclick="javascript:sendmail();" value="发 送 " />
        </td>
    </tr>
</table>


</form>
</body>
</html>