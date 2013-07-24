<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>




<title>${empty obj?'新建':'修改'}模块</title>
</head>

<body>
<div class="ui_head"></div>


<%@ include file="module_menu.jsp"%>


<form method="post" action="/basic.ModuleAH.save" id="post_form">
<m:token/>
<input type="hidden" name="objId" value="${obj.moduleid}" />
<table class="ui edit">
<tr class="title"><td colspan="2">${empty obj?"创建新模块":"修改模块"}</td></tr>
    <tr>
        <td width="8%">模块编码</td>
        <td><input reg="^.+$" tip="请填写模块编码，请使用字母数字组合" type="text" maxlength="25" ${obj.moduleid!=null?"readonly":""} name="Module$moduleid" value="<c:out value='${obj.moduleid}'/>" /></td>
    </tr>
    <tr>
        <td>模块名</td>
        <td><input type="text" reg="^.+$" tip="请填写模块名，请使用字母数字组合" name="Module$modulename" value="${obj.modulename}" /></td>
    </tr>
    <tr>
        <td>描述： </td>
        <td><input type="text" class=mybutton maxlength=25 size=20 name="Module$description" value="<c:out value='${obj.description}'/>" ></td>
    </tr>




<c:if test="${!empty obj}">
    <tr>
    	<td>模块功能：</td>
        <td style="padding:10px">
<table width="100%" border="1" cellspacing="1" cellpadding="1" class="inner_table">
    <tr>
      <th>功能代码</th>
      <th>功能名称</th>
      <th>描述</th>
      <th>操作</th>
    </tr>
<c:forEach var="function" items="${functions}"  varStatus="status">
    <tr>
      <td><c:out value="${function.functionid}"/></td>
      <td><c:out value="${function.functionname}"/>${function.functionname==''||function.functionname==null?'-':''}</td>
      <td><c:out value="${function.remark}"/>${function.remark==''||function.remark==null?'-':''}</td>
      <td><a href="/basic.ModuleAH.delFunction?detailObjId=${function.functionid}" onclick="return confirm('你确实要删除吗?不可恢复');"><img src="/images/del_icon2.gif" /></a></td>
    </tr>
</c:forEach>
    <tr>
      <td><input type="text" name="function$functionId" maxlength="25" /></td>
      <td><input type="text" name="function$functionname" maxlength="25" /></td>
      <td><input type="text" name="function$remark" maxlength="25" />
		  <input type="hidden" name="this$function$moduleid" value="${obj.moduleid}" />
      </td>
      <td><a id="post_new" href="javascript:void(0)"><img src="/images/button_edit.png" /></a></td>
    </tr>
</table>
		</td></tr>
<script type="text/javascript">
$(function(){
	$("#post_new").click(function(){
			$("#post_form").submit();//attr("action","/basic.ModuleAH.addFunction").
	});
});
</script>
</c:if>

    <tr>
    	<td></td>
        <td>      
            <input type="submit" value="提交保存" />
            <input type="button" onclick="window.location.href='/basic.ModuleAH.listModule'" value="取消返回" /></div>
        </td>
    </tr>
</table>
</form>








</body>
</html>