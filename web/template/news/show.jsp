<!DOCTYPE html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<head>
	<title>${obj.title} - <m:tool>sys_name</m:tool> <m:tool>sys_edition</m:tool></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<!-- Bootstrap -->
    <link href="<m:tool>www_url</m:tool>/css/bootstrap.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="<m:tool>www_url</m:tool>/css/theme.css" />
    <link rel="stylesheet" type="text/css" href="<m:tool>www_url</m:tool>/css/blog.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head>

<script language="javascript" src="/js/jquery-1.8.2.js"></script>


<script>

	function like(id,mail){
		
		post_form.newsid.value=id;
		post_form.mail.value=mail;
		$.get("/news.NewsActionHandler.like",$("#post_form").serialize(),function(result){
			//var result2=eval(result);
			alert(result);
		
		});	
	}

	function dislike(id,mail){
		post_form.newsid.value=id;
		post_form.mail.value=mail;		
		$.get("/news.NewsActionHandler.dislike",$("#post_form").serialize(),function(result){
//			var result2=eval(result);
			alert(result);
		
		});	
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

    <div id="blog_wrapper">
        <div class="container">
            <div class="row">

                <div class="span8" style="width:100%">




                    <div class="post nopic">
                        <div class="row">
                            <div class="span4 info">

                                <div style="text-align:center">
                                	<h3 style="color:#87a46e">${obj.title}</h3>
                                    <div>${obj.author} &nbsp; ${obj.copyfrom} &nbsp; <fmt:formatDate value="${obj.posttime}" pattern="yyyy-MM-dd HH:mm"/> </div>
                                </div>
                                <hr/>
                                <p>${obj.content}</p>
                                <div class="post_info">
                                    <div class="date">来源：${obj.copyfrom}
                                     &nbsp; 源文地址：<a href="${obj.copyfromurl}" target="_blank">${obj.copyfromurl}</a></div>
                                    <a style="margin-left:10px" href="javascript:void(0)" onClick="javascript:dislike('${obj.id}','XPhone@moto.com')" class="btn">不喜欢</a>
                                    <a href="javascript:void(0)" onClick="javascript:like('${obj.id}','XPhone@moto.com')" class="btn">喜欢</a>
                                </div>
                            </div>
                        </div>
                    </div>


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

                </div>

                

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