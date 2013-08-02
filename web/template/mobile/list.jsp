<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ include file="head.jsp"%>
<div class="content">
  <div class="result-text">亲爱的德华，您好！</div>


  <tr class="effect">
  	<td><input type="checkbox" value="${detail.id}" name="delid"/></td>
    <td></td>
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
    <td><a href="javascript:void(0)" onclick="show('${detail.id}','${pageVariable.npage*pageVariable.pagesize}')" ><img src="/images/button_edit.png" /></a> <c:if test="${detail.his_news_id!='' && detail.his_news_id!=null}">| <a href="/news.NewsActionHandler.showHis?objId=${detail.his_news_id}" >原文</a></c:if></td>
  </tr>


<c:forEach var="detail" items="${objList}" varStatus="status">
  <div class="post">
    <div class="archive-top">
      <div class="archive-top-right"> <a class="post-arrow" href="javascript:void(0);"></a> </div>
      <div class="archive-top-left month-05"> <fmt:formatDate value="${detail.posttime}" pattern="yyyy-MM-dd HH:mm"/> </div>
    </div>
    <a class="h2" href="javascript:void(0)" onclick="show('${detail.id}')">${detail.title}</a>
    <div class="post-author"> <span class="lead">By</span> ${detail.author}<br />
      <span class="lead">Tags:</span> <a href="#" title="信息技术" rel="category">${detail.tags}</a><br />
    </div>
    <div class="clearer"></div>
    <div id="entry-671" style="display:none" class="mainentry left-justified">
      ${detail.content}
      <a class="read-more" href="javascript:void(0)" onclick="show('${detail.id}'">Read This Post</a> </div>
  </div>
</c:forEach>
  <div id="call_list" class="ajax-load-more">
    <div id="more_list" class="spin" style="display:none"></div>
    <a class="ajax" href="javascript:return false;" onclick="$wpt('#more_list').fadeIn(200); $wpt('#ajax_list').load('/?paged=2', {}, function(){ $wpt('#call_list').fadeOut();});"> 载入更多新闻... </a> </div>
  <div id="ajax_list"></div>
</div>
<%@ include file="foot.jsp"%>