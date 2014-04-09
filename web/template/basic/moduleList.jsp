<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/template/basic/common_head.jsp"%>
</head>

<body>
<div class="ui_head"></div>

<%@ include file="module_menu.jsp"%>


<form name="form1" action="?" method="post">
<table class="ui edit">
<tr class="title"><td colspan="2">查询条件</td></tr>
    <tr>
      <td width="8%">模块名称：</td>
      <td><input type="text" name="module$modulename" value="${param.module$modulename}" /></td>
    </tr>
    <tr>
      <td></td>
      <td><input type="submit" value="<fmt:message key='search'/>" />
        <input type="button" id="resetb" value="<fmt:message key='reset'/>" /></td>
    </tr>
  </table>
</form>

<table class="ui list">
  <tr class="title"><td class="title" colspan="7">模块管理</td></tr>
  <tr class="effect">
            <th>优先级别</th>
            <th>模块ID</th>
            <th>模块名称</th>
            <th>所属系统</th>
            <th>描述</th>
            <th>修改</th>
            <th>删除</th>
  </tr>
<c:forEach var="obj" items="${objList}" varStatus="status">
<tr class="effect">
            <td>${obj.priority==''||obj.priority==null?'-':obj.priority}</td>
            <td>${obj.Moduleid}</td>
            <td>${obj.Modulename}</td>
            <td><m:out name="" value="${obj.systemid}" type="application"/>${obj.systemid==''||obj.systemid==null?'-':''}</td>
            <td>${obj.description==''||obj.description==null?'-':obj.description}</td>
            <td align="center"><a title="修改" class="ico_button ico_edit" href="/basic.ModuleAH.showModule?objId=${obj.Moduleid}"><img src="/images/button_edit.png" /></a></td>
            <td align="center"><a title="删除" class="ico_button ico_del" href="/basic.ModuleAH.delModule?objId=${obj.Moduleid}" onclick="return confirm('你确实要删除吗?不可恢复');"><img alt="点击删除" src="/images/del_icon2.gif" /></a></td>
</tr>
</c:forEach>

<div class="pages_bar">
<div class="pages_left">共 <span class="orange">${pageVariable.totalpage}</span> 页 | 第 <span class="orange">${pageVariable.npage+1}</span> 页</div>
<div class="pages_right"><m:page action="basic.UserAH.listUser" /></div>
</div>

</form>

</table>










</body>
</html>