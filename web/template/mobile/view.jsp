<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ include file="head.jsp"%>
<div class="content single">
	<div class="single-post-meta-bottom" style="margin:10px; border-left:1px solid #ADADAD; border-right:1px solid #ADADAD;-webkit-border-radius: 8px;-moz-border-radius: 8px;border-radius: 8px;"> 所属标签：${obj.tags}</div>
  <div class="post"> <a class="sh2" href="#" rel="bookmark" title="${obj.title}">${obj.title}</a>
    <div class="single-post-meta-top">${obj.copyfrom} &rsaquo; ${obj.author}</div>
  </div>
  <div class="clearer"></div>
  <div class="post">
    <div id="singlentry" class="left-justified" style="padding-bottom: 12px;">${obj.summary==''||obj.summary==null?'暂无内容':obj.summary}</div>
    <ul id="post-options">
      <li><a href="javascript:last()" id="oprev" title="上一篇"></a></li>
      <li id="like" ><a href="javascript:like('${obj.id}','${param.email}')" id="omail" title="喜欢"></a></li>
      <li id="dislike" ><a href="javascript:dislike('${obj.id}','${param.email}')" id="otweet" title="不喜欢"></a></li>
      <li><a href="javascript:next()" id="onext" title="下一篇"></a></li>
    </ul>
  </div>
  <div class="post">
    <div id="singlentry" class="left-justified" style="padding-bottom: 12px;">${obj.content}</div>
    <ul id="post-options">
      <li><a href="javascript:last()" id="oprev" title="上一篇"></a></li>
      <li id="like" ><a href="javascript:like('${obj.id}','${param.email}')" id="omail" title="喜欢"></a></li>
      <li id="dislike" ><a href="javascript:dislike('${obj.id}','${param.email}')" id="otweet" title="不喜欢"></a></li>
      <li><a href="javascript:next()" id="onext" title="下一篇"></a></li>
    </ul>
  </div>
<form action="GeneralHandleSvt" method="post" name="post_form" id="post_form" onsumit="" >
	<input type="hidden" id="s_time" name="startDate" value="${startDate}" readonly plugin="date" start="start" />
	<input type="hidden" id="e_time" name="endDate" value="${endDate}" readonly plugin="date" end="start" />
    <input type="hidden" name="status" value="${status}" />
    <input type="hidden" id="title" name="title" value="${title}" />
    <input type="hidden" name="urgent" value="${urgent}" />
    <input type="hidden" id="author" name="author" value="${author}" />
    <input type="hidden" name="newsid" value="" />
    <input type="hidden" name="jobid" value="${param.jobid}" />
    <input type="hidden" name="email" value="${param.email}" />
    <input type="hidden" name="sort" value="${sort}" />
	<input type="hidden" name="objId" value="${obj.id}" />
    <input type="hidden" name="tag_ids2" value="${tag_ids2}" />
	<input type="hidden" id="orderby" name="orderby" value="${orderby }"/>
	<input type="hidden" id="order" name="order" value="${order }"/>
</form>
<script type="text/javascript">
	function last(){
		post_form.action="/news.NewsActionHandler.last";
		$wpt("#post_form").submit();
	}
	function next(){
		post_form.action="/news.NewsActionHandler.next";
		$wpt("#post_form").submit();
	}
	function like(id,mail){
		post_form.newsid.value=id;
		post_form.email.value=mail;
		$wpt.get("/news.NewsActionHandler.like",$wpt("#post_form").serialize(),function(result){
		if(result=='ok')
				$wpt("#like").addClass("ahover");
				$wpt("#like2").addClass("ahover");
		});	
	}
	function dislike(id,mail){
		post_form.newsid.value=id;
		post_form.email.value=mail;		
		$wpt.get("/news.NewsActionHandler.dislike",$wpt("#post_form").serialize(),function(result){
		if(result=='ok')
				$wpt("#dislike").addClass("ahover");
				$wpt("#dislike2").addClass("ahover");
		});	
	}
</script>
</div>

<%@ include file="foot.jsp"%>