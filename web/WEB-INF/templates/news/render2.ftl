<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>GpCore</title>
<style>
body{ background:url(http://www.pcwcn.com/gpcore---bg---nopost.jpg) #eee;font-family:Verdana, Geneva, sans-serif; margin:0; padding:0}
.tags{
	font-size:140%; font-weight:bolder; font-family:'黑体';

  background-color: #F5F5F5;
  color:#000;
  text-shadow: 0 1px 1px rgba(255, 255, 255, 0.75);
  background-image: -moz-linear-gradient(top, #f1f1f2, #E6E6E6);
  background-image: -ms-linear-gradient(top, #f1f1f2, #E6E6E6);
  background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#f1f1f2), to(#E6E6E6));
  background-image: -webkit-linear-gradient(top, #f1f1f2, #E6E6E6);
  background-image: -o-linear-gradient(top, #f1f1f2, #E6E6E6);
  background-image: linear-gradient(top, #f1f1f2, #E6E6E6);
  background-repeat: repeat-x;
  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#'f1f1f2, endColorstr='#'e6e6e6, GradientType=0);

margin:2% 1% 0 1%;clear:both; padding:1% 3%; border:1px solid #ccc; border-bottom:0; overflow:auto;-moz-border-radius:4px 4px 0 0; -webkit-border-radius:4px 4px 0 0; border-radius:4px 4px 0 0;}
.list{ margin:0 1% 2% 1%;clear:both; padding:2% 3%; border:1px solid #ccc; background:#fff; overflow:auto;-moz-border-radius:0 0 4px 4px; -webkit-border-radius:0 0 4px 4px; border-radius:0 0 4px 4px;}
h3{ font-size:120%; font-weight:bolder; font-family:'黑体'; margin:2% 0; padding:0}
a{text-decoration:none; color:#87a46e}
.green{color:#87a46e}
a:hover{ text-decoration:underline; color:#F60}
.post_info{ overflow:auto; height:auto;}
.date{ overflow:auto; height:auto;}
.more{float:right; font-size:14px; font-weight:bolder}
.fl{ float:left; font-size:80%}
.fr{ float:right; font-size:80%}
.author{ font-size:12px; color:#ccc}
.posttime{ float:left;color:#ccc;font-size:12px}
.copyright{ overflow:auto; margin:1% 2%}
p{ font-size:14px}
.logo{margin:4% 0 1% 2%; overflow:auto;border:0}
.head{width:100%;overflow:auto;}
.user{ float:right;padding:4% 2% 0 0}
.start{margin:2% 1%;clear:both; padding:2% 3%; border:1px solid #ccc; background:#fff; overflow:auto;-moz-border-radius:4px; -webkit-border-radius:4px; border-radius:4px;}
.end{margin:2% 1%;clear:both; padding:2% 3%; border:1px solid #ccc; background:#fff; overflow:auto;-moz-border-radius:4px; -webkit-border-radius:4px; border-radius:4px;}
.hr{border-bottom:1px dotted #ccc; padding:0; margin:2% 0 0 0;height:1px; overflow:hidden;clear:both}
</style>
</head>

<body>

<div class="head" test='email:${email!"null"};mail:${mail!"null"};name:${name!"null"}'>
<a href="${www_url}" target="_blank" class="fl"><img border="0" class="logo" src="http://www.pcwcn.com/gpcore---logo---email.png" alt="GpCore" /></a>
<div class="user"><strong class="green">欢迎订阅 GpCore</strong>  - 2013-06-19</div>
</div>


<div class="start">${greeting!""}</div>



            <#if objList?exists>
                <#list objList?keys as key>
					<#assign item = objList[key]>
					<#if item?size!=0 > 
                        <div class="tags">${key}</div>
						<div class="list">
							<#assign hr = 1>
							<#list item as e>
								<#if hr!=1 ><div class="hr">&nbsp;</div></#if>
								<#assign hr = 2>
								<h3><a href="${www_url}/news.NewsActionHandler.showIt?objId=${e.id}">${e.title}</a></h3>
								<p>${(e.c_summary)?default("N/A")} </p>
								<div class="post_info">
									<div class="author">${e.posttime}</div>
									<div class="date">
											<span class='posttime'>${(e.author)?default("N/A")} - ${e.copyfrom!""}</span>
											<a href="${www_url}/news.NewsActionHandler.showIt?objId=${e.id}" class="more" target="_blank">more</a>
									</div>
								</div>
							</#list>
						</div>
					</#if>
                </#list>
            </#if>





<div class="end">${ending!""}</div>


<div class="copyright">
	<span class='fl'><strong>GpCore</strong> v1.2</span>
	<span class='fr'>2013 - <a href="http://www.GoldPebble.com" target="_blank">GoldPebble.com</a></span>
</div>





</body>
</html>