<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US">
<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>${obj.title}</title>
<link rel="apple-touch-icon" href="/images/mobile/Default.png" />
<link rel="stylesheet" href="/images/mobile/style.css" type="text/css" media="screen" />
<style type="text/css">
#headerbar, #wptouch-login, #wptouch-search {
	background: #000000 url(/images/mobile/head-fade-bk.png);
}
#headerbar-title, #headerbar-title a {
	color: #eeeeee;
}
#wptouch-menu-inner a:hover {
	color: #006bb3;
}
#catsmenu-inner a:hover {
	color: #006bb3;
}
#drop-fade {
	background: #333333;
}
a, h3#com-head {
	color: #006bb3;
}
a.h2, a.sh2, .page h2 {
	font-family: 'Helvetica Neue';
}
a.h2 {
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden;
}
</style>
<script type='text/javascript' src='/images/mobile/jquery.js?ver=1.8.3'></script>
<script type='text/javascript' src='/images/mobile/core.js?ver=1.9.x'></script>
<script type="text/javascript">
// Hides the addressbar on non-post pages
function hideURLbar() { window.scrollTo(0,1); }
addEventListener('load', function() { setTimeout(hideURLbar, 0); }, false );

					$wpt(document).ready(function(){
						$wpt("a.post-arrow").bind( touchStartOrClick, function(e) {
							$wpt(this).toggleClass("post-arrow-down");
							$wpt(this).parent().parent().parent().find(".mainentry")
								.wptouchFadeToggle(500).show();
						});	
					 });					
</script>
</head>
<body class="grid-wptouch-bg ">
<!-- New noscript check, we need js on now folks -->
<noscript>
<div id="noscript-wrap">
  <div id="noscript">
    <h2>Notice</h2>
    <p>JavaScript for Mobile Safari is currently turned off.</p>
    <p>Turn it on in <em>Settings &rsaquo; Safari</em><br />
      to view this website.</p>
  </div>
</div>
</noscript>
<!-- Prowl: if DM is sent, let's tell the user what happened --> 

<!-- #start The Search Overlay -->
<div id="wptouch-search">
  <div id="wptouch-search-inner">
    <form method="get" id="searchform" action="?">
      <input type="text" placeholder="Search..." name="s" id="s" />
      <input name="submit" type="submit" tabindex="1" id="search-submit" placeholder="Search..."  />
      <a href="javascript:return false;"><img class="head-close" src="/images/mobile/head-close.png" alt="close" /></a>
    </form>
  </div>
</div>
<div id="wptouch-menu" class="dropper">
  <div id="wptouch-menu-inner">
    <div id="menu-head">
      <div id="tabnav"> <a href="#head-pages">菜单</a> <a href="#head-tags">标签</a> <a href="#head-cats">关于</a> </div>
      <ul id="head-pages">
        <li><a href="http://hct1.dingweila.com:8080/news.NewsActionHandler.listPhone"><img src="/images/mobile/Default.png" alt=""/>首页</a></li>
        <li><a href="http://hct1.dingweila.com:8080/news.NewsActionHandler.listPhone/?feed=rss2"><img src="/images/mobile/RSS.png" alt="" />订阅</a></li>
        <li><a href="mailto:calvinsh@gmail.com"><img src="/images/mobile/Mail.png" alt="" />设置邮箱</a></li>
      </ul>
      <ul id="head-cats">
        <ul>
          <li><a href="#">子菜单一 <span>(0)</span></a></li>
          <li><a href="#">子菜单二 <span>(0)</span></a></li>
          <li><a href="#">子菜单三 <span>(0)</span></a></li>
        </ul>
      </ul>
      <ul id="head-tags">
        <li>
          <ul>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</div>
<div id="headerbar">
  <div id="headerbar-title"> 
    <!-- This fetches the admin selection logo icon for the header, which is also the bookmark icon --> 
    <img id="logo-icon" src="/images/mobile/Default.png" alt="GpHiden" /> <a href="http://hct1.dingweila.com:8080/news.NewsActionHandler.listPhone">GpHiden</a> </div>
  <div id="headerbar-menu"> <a href="javascript:return false;"></a> </div>
</div>

<div id="drop-fade"> <a idsssss="searchopen" class="top feedback" href="/template/mobile/feedback.jsp">Feedback</a></div>