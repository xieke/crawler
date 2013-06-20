<!DOCTYPE html>
<html lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<head>

	<!-- start: Meta -->
	<meta charset="utf-8" />
	<title>GpCore</title> 

    <link href="css/bootstrap.css" rel="stylesheet" type="text/css" />
    <link href="css/bootstrap-responsive.css" rel="stylesheet" type="text/css" />
	<link href="css/style_2.css" rel="stylesheet" type="text/css" />
	<link href="css/layerslider.css" rel="stylesheet" type="text/css" />
	
	<!--[if lt IE 9 ]>
	  <link href="css/styleIE.css" rel="stylesheet">
	<![endif]-->
	
	<!--[if IE 9 ]>
	  <link href="css/styleIE9.css" rel="stylesheet">
	<![endif]-->
	
	<!-- end: CSS -->

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head>
<body>
	
	<!--start: Header -->
	<header>
		
		<!--start: Container -->
		<div class="container">
			
			<!--start: Navigation -->
			<div class="navbar navbar-inverse">
	    		<div class="navbar-inner">
	          		<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            		<span class="icon-bar"></span>
	            		<span class="icon-bar"></span>
	            		<span class="icon-bar"></span>
	          		</a>
					<a class="brand" href="javascript:void(0)">GpCore</a>

	          		<div class="nav-collapse collapse">
	            		<ul class="nav">
							<li class="active"><a href="javascript:void(0)">收听中</a></li>
	              			<li><a href="javascript:void(0)">欢迎您使用！</a></li>
	            		</ul>
	          		</div>
	        	</div>
	      	</div>	
			<!--end: Navigation -->
			
		</div>
		<!--end: Container-->			
			
	</header>
	<!--end: Header-->
	<!--start: Container -->
	<div class="container">
				
		<!--start: Wrapper-->
		<div id="wrapper">
		
			<!-- start: Wall -->
			<div id="wall" class="row-fluid">












<c:forEach var="obj" items="${objList}">
				<div class="span3 item">
					<div class="picture">
					<c:if test="${!empty (obj.picurl)}">
						<img src="${obj.picurl}" alt="" />
						</c:if>
						<div class="description">
							<p>${obj.text}</p>
						</div>
						<div class="meta">
							<span><i class="fa-icon-calendar"></i>${obj.createdat}</span>
							<span><i class="fa-icon-user"></i> <a href="#">来自：${obj.username}</a></span>							
							<span><i class="fa-icon-user"></i> <a href="#">转发：${obj.repostsCount}</a></span>
							<span><i class="fa-icon-heart-empty"></i>评论： ${obj.commentsCount}</span>

						</div>
					</div>	
				</div>
</c:forEach>



















			</div>
			<!-- end: Wall -->				
					
		</div>
		<!--end: Container-->
	
	</div>
	<!-- end: Wrapper  -->			



<!-- start: Java Script -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="js/jquery-1.8.2.js"></script>
<script src="js/isotope.js"></script>
<script src="js/jquery.imagesloaded.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/flexslider.js"></script>
<script src="js/carousel.js"></script>
<script src="js/jquery.cslider.js"></script>
<script src="js/slider.js"></script>
<script src="js/fancybox.js"></script>
<script src="js/twitter.js"></script>
<script src="js/jquery.placeholder.min.js"></script>

<script src="js/jquery-easing-1.3.js"></script>
<script src="js/layerslider.kreaturamedia.jquery.js"></script>

<script src="js/excanvas.js"></script>
<script src="js/jquery.flot.js"></script>
<script src="js/jquery.flot.pie.min.js"></script>
<script src="js/jquery.flot.stack.js"></script>
<script src="js/jquery.flot.resize.min.js"></script>

<script defer src="js/modernizr.js"></script>
<script defer src="js/retina.js"></script>
<script defer src="js/custom.js"></script>
<!-- end: Java Script -->

    <form name="form1" action="<c:url value='/weibo.WeiBoAH.listWeibo'/>" method="post"/>
    <div class="ui_showpage"><m:page action="weibo.WeiBoAH.listWeibo" /></div>
</body>
</html>