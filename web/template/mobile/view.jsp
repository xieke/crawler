<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ include file="head.jsp"%>
<div class="content single">
  <div class="post"> <a class="sh2" href="#" rel="bookmark" title="${obj.title}">${obj.title}</a>
    <div class="single-post-meta-top">${obj.copyfrom} &rsaquo; ${obj.author}</div>
  </div>
  <div class="clearer"></div>
  <div class="post" id="post-572">
    <div id="singlentry" class="left-justified">${obj.content}</div>
    <div class="single-post-meta-bottom"> 所属标签： <a href="#" title="信息技术">信息技术</a> , <a href="#" title="商业和经济" rel="category">商业和经济</a> </div>
    <ul id="post-options">
      <li><a href="#" id="oprev" title="上一篇"></a></li>
      <li><a href="javascript:dislike('${obj.id}','XPhone@moto.com')" id="omail" title="喜欢"></a></li>
      <li><a href="javascript:like('${obj.id}','XPhone@moto.com')" id="otweet" title="不喜欢"></a></li>
      <li><a href="#" id="onext" title="下一篇"></a></li>
    </ul>
<form action="GeneralHandleSvt" method="post" name="post_form" id="post_form" onsumit="" >
	<input type="hidden" id="s_time" name="startDate" value="${startDate}" readonly plugin="date" start="start" />
	<input type="hidden" id="e_time" name="endDate" value="${endDate}" readonly plugin="date" end="start" />
    <input type="hidden" name="status" value="${status}" />
    <input type="hidden" id="title" name="title" value="${title}" />
    <input type="hidden" name="urgent" value="${urgent}" />
    <input type="hidden" id="author" name="author" value="${author}" />
    <input type="hidden" name="newsid" value="" />
    <input type="hidden" name="mail" value="" />
    <input type="hidden" name="sort" value="${sort}" />
    <input type="hidden" name="tag_ids2" value="${tag_ids2}" />
	<input type="hidden" id="orderby" name="orderby" value="${orderby }"/>
	<input type="hidden" id="order" name="order" value="${order }"/>
</form>
<script type="text/javascript">
	function like(id,mail){
		post_form.newsid.value=id;
		post_form.mail.value=mail;
		$.get("/news.NewsActionHandler.like",$("#post_form").serialize(),function(result){
			alert(result);
		
		});	
	}
	function dislike(id,mail){
		post_form.newsid.value=id;
		post_form.mail.value=mail;		
		$.get("/news.NewsActionHandler.dislike",$("#post_form").serialize(),function(result){
			alert(result);		
		});	
	}
</script>
  </div>
</div>
<%@ include file="foot.jsp"%>