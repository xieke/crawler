<#ftl encoding="UTF-8" /> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>GpCore</title>
</head>

<body>



${greeting!""}<br><br>



            <#if objList?exists>
                <#list objList?keys as key>
					<#assign item = objList[key]>
					<#if item?size!=0 > 
[${key}]<br>
							<#list item as e>
${(e.summary)?default("")}    <a href="${www_url}/news.NewsActionHandler.showIt?objId=${e.id}&email=${email}&jobid=${(jobid)?default('')}" class="more" target="_blank">more</a><br><br>
							</#list>
                            <br>
					</#if>
                </#list>
            </#if>





${ending!""}<br>



</body>
</html>