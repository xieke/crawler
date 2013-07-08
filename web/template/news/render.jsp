<!DOCTYPE html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<title><m:tool>sys_name</m:tool> <m:tool>sys_edition</m:tool></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<!-- Bootstrap -->
    <link href="<m:tool>www_url</m:tool>/css/bootstrap.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="<m:tool>www_url</m:tool>/css/theme.css" />
    <link rel="stylesheet" type="text/css" href="<m:tool>www_url</m:tool>/css/blog.css" />

</head>
<script>
	function preview(){
//		$("#form1").attr("action","news.NewsActionHandler.render2");
		form1.action="news.NewsActionHandler.render2";
		form1.submit();
	}
</script>
<body>
    <a href="#" class="scrolltop">
        <span>up</span>
    </a>
	<div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          	<a class="brand" href="<m:tool>www_url</m:tool>/">
                <img src="<m:tool>www_url</m:tool>/img/logo.png" alt="logo" />
            </a>
		  	<div class="nav-collapse collapse">
                <ul class="nav pull-right">
                    <li><a class="btn-header" href="<m:tool>www_url</m:tool>/">访问GpCore</a></li>
                </ul>
	        </div>
        </div>
      </div>
    </div>
<form action="/news.NewsActionHandler.sendMail" method="post" name="form1" id="form1" onsumit="" >

    <div id="blog_wrapper">
        <div class="container">
            <div class="row">
            
语言：<input  id="lang" name="lang" value="${lang}" />
	</div>

            <div class="row">
            
标签：<input  id="tags" name="tags" value="${tags}" />
	</div>
	<div class="row">
标题：<input  id="subject" name="subject" value="Goldpebble Research Customized News" />
	</div>
	<div class="row">

欢迎语：<input  id="greeting" name="greeting" value="" />
	</div>
	<div class="row">

结束语：<input  id="ending" name="ending" value="" />
	</div>
	<div class="row">

			邮箱地址：<input  id="email" name="email" value="" /> 
			<input type="submit" value="发送" />
			<input type="button" onclick="preview();" value="预览邮件" />
</div>
                <div class="span8" style="width:100%">

                    <h1 class="header">
                        最新信息
                        <hr />
                    </h1>

<c:forEach var="item" items="${objList}">   
${item.key}<br>   

	<c:if test="${fn:length(item.value)==0}">
                    <div class="post nopic">
                        <div class="row">
                            <div class="span4 info">
							没有文章！
                                </div>
                            </div>
                        </div>

	</c:if>
<c:forEach var="detail" items="${item.value}" varStatus="status">
<input type=hidden name="outids" value="${detail.id}"/>
                    <div class="post nopic">
                        <div class="row">
                            <div class="span4 info">
                                <a href="/news.NewsActionHandler.showIt?objId=${detail.id}">
                                    <h3>${detail.title}</h3>
                                </a>
                                <p>${detail.summary}</p>
                                <div class="post_info">
                                    <div class="author">${detail.author}${detail.copyfrom}</div>
                                    <div class="date"><fmt:formatDate value="${detail.posttime}" pattern="yyyy-MM-dd HH:mm"/></div>
                                    <a href="/news.NewsActionHandler.showIt?objId=${detail.id}" class="btn">more</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    
  </c:forEach>



</c:forEach>  


</form>

                

            </div>
        </div>
    </div>

    <div id="footer">
        <div class="container">
            <div class="row copyright">
                <div class="span5">
                    <h3>GpCore</h3>
                </div>
                <div class="span2 offset5 copy">
                    <p>2013 - GoldPebble.com</p>
                </div>
            </div>
        </div>
    </div>

</body>
</html>