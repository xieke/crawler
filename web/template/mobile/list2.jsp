<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<c:forEach var="detail" items="${objList}" varStatus="status">
  <div class="post">
    <div class="archive-top">
    <script type="text/javascript">
					$wpt(document).ready(function(){
						$wpt("a#aa${detail.id}").bind( touchStartOrClick, function(e) {
							$wpt(this).toggleClass("post-arrow-down");
							$wpt('#bb${detail.id}').wptouchFadeToggle(500);
						});	
					 });					
				</script> 
      <div class="archive-top-right"> <a id="aa${detail.id}" class="post-arrow" href="javascript:void(0);"></a> </div>
      <div class="archive-top-left month-05"> <fmt:formatDate value="${detail.posttime}" pattern="yyyy-MM-dd HH:mm"/> </div>
    </div>
    <a class="h2" href="<m:out value='www_url' type='system_core'/>/news.NewsActionHandler.showIt?jobid=${param.jobid}&email=${param.email}&objId=${detail.id}">${detail.title}</a>
    <div class="post-author"><!-- <span class="lead">By</span> ${detail.author}<br />-->
      <span class="lead">Tags:</span> <a href="#" title="信息技术" rel="category">${detail.tags}</a><br />
    </div>
    <div class="clearer"></div>
    <div id="bb${detail.id}" style="display:none" class="mainentry left-justified">
      ${detail.summary}      
      <a class="read-more" href="<m:out value='www_url' type='system_core'/>/news.NewsActionHandler.showIt?jobid=${param.jobid}&email=${param.email}&objId=${detail.id}" onclick="">Read This Post</a> </div>
  </div>
</c:forEach>
<c:if test="${pageVariable.hasNextPage}">
<div id="page${param.page}" class="ajax-load-more">
  <div id="s${param.page}" class="spin"	 style="display:none"></div>
  <a class="ajax" href="javascript:return false;" onClick="$wpt('#s${param.page}').fadeIn(200); $wpt('#a${param.page}').load('/news.NewsActionHandler.listPhone?jobid=${param.jobid}&email=${param.email}&page=${param.page+1}', {}, function(){ $wpt('#page${param.page}').fadeOut();});"> 载入更多新闻... </a> </div>
<div id="a${param.page}"></div>
</c:if>