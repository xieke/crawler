<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>
<!--日期控件 开始-->
<script type="text/javascript" src="/plugin/jquery.datepick.package-4.0.5/jquery.datepick.js"></script>
<link href="/plugin/jquery.datepick.package-4.0.5/jquery.datepick-adobe.css" rel="stylesheet" type="text/css" />
<!--日期控件 结束-->
<title>TAG</title>
</head>

<body>
<div class="ui_head"></div>



<form method="post" action="/GeneralHandleSvt" id="form1" name="form1">
<input type="hidden" id="reqType" name="reqType" value="tag.TagRuleActionHandler.saveDupRule" />
<input type="hidden" id="id" name="dup_rule$id" value="${obj.id}" />
<table class="ui edit">
<tr class="title"><td colspan="2">去重规则</td></tr>
    <tr>
        <td>GP重要度：</td>
        <td id="importance_text">
        	<c:forEach var="im" items="${importanceList}"  varStatus="status">
            	<m:checkbox name="${statux.index}$im$importance" value="${im.id}" checkValue="${im.checkValue}"/>${im.name}
            </c:forEach>
            <input type="hidden" name="dup_rule$importance" id="importance_hidden" value="${obj.importance}" />
        </td>
    </tr>
    <tr>
        <td colspan="2" style="left:50px"><br /><font color="#FF0000">友情提示：选中即代表需要去重</font></td>
    </tr>
    <tr>
    	<td></td>
        <td><input type="submit" value="保存" class="right-button08" /></td>
    </tr>
</table>


<script type="text/javascript">
	$(function(){
		
		$("#importance_text [type='checkbox']").click(function(){
			var importance="";
			$("#importance_text :checked").each(function(){
				importance+=$(this).val()+",";
			})
			$("#importance_hidden").val(importance);
		})
	})
</script>
</form>
</body>
</html>