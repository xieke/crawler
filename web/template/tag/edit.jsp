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
<title>TAG</title>
</head>

<body>
<div class="ui_head"></div>



<form method="post" action="/GeneralHandleSvt" id="form1">
<input type="hidden" id="reqType" name="reqType" value="basic.TagActionHandler.save" />
<input type="hidden" id="id" name="tag$id" value="" />
<table class="ui edit">
<tr class="title"><td colspan="2">新建TAG标签</td></tr>
    <tr>
        <td width="70">TAG名称：</td>
        <td><input type="text" name="tag$name" />(可使用逗号 , 分隔批量添加)</td>
    </tr>
    <tr>
        <td>TAG描述：</td>
        <td><input type="text" name="tag$description" /></td>
    </tr>
    <tr>
        <td>上层分类：</td>
        <td>
            <select name="tag$parent_id">
            		<option value="">根</option>
                <c:forEach var="detail" items="${objList}" varStatus="status">
                    <option value="${detail.id}">${detail.level}${detail.name}</option>
                </c:forEach>
            </select>(不选择将成为一级分类) 
        </td>
    </tr>
    <tr>
    	<td></td>
        <td><input type="submit" value="创建" class="right-button08" /></td>
    </tr>
</table>

<table class="ui list">
	<tr class="title"><td class="title" colspan="5">编辑TAG标签库</td></tr>
    <tr class="effect">
        <th>TAG名称</th>
        <th>描述</th>
        <th width="11%">排序</th>
        <th width="9%">编辑</th>
        <th width="11%">删除</th>
    </tr>
<c:forEach var="detail" items="${objList}" varStatus="status">
    <tr class="no_padding effect">
        <td><input type="checkbox" value="${detail.id}" name="delid"/> ${detail.level}-【${detail.name}】</td>
        <td>${detail.description==""||detail.description==null?"-":detail.description}<input type="hidden" name="${status.index}$tags$id" id="${status.index}_tags_id" value="${detail.id}" /></td>
        <td align="center"><input type="text" size="2" name="${status.index}$tags$orderby" value="${detail.orderby}" /></td>
        <td align="center"><a href="/basic.TagActionHandler.show?job=edit&objId=${detail.id}"><img src="/images/button_edit.png" /></a></td>
        <td align="center"><a href="/basic.TagActionHandler.delete?job=delete&id=${detail.id}" onclick="return confirm('你确实要删除吗?子节点将同时删除');"><img alt="点击删除" src="/images/del_icon2.gif" /></a></td>
    </tr>
</c:forEach>
	<tr class="edit">
    	<td colspan="5">
                <a class="select_all" href="javascript:void(0)">全选</a>/<a class="unselect_all" href="javascript:void(0)">反选</a>
<script type="text/javascript">
$(".select_all").click(function(){
	$("input[name='delid']").attr("checked",true);
});
$(".unselect_all").click(function(){
	$("input[name='delid']").attr("checked",false);
});
</script>
                <input type="button" class="button" id="dels" value="批量删除" />
                <input type="button" class="button" id="saveOrderbys" value="更改标签排序" />
        </td>
    </tr>
</table>
<script type="text/javascript">
	$(function(){
		$("#dels").click(function(){
			if(confirm('你确实要删除吗?子节点将同时删除')){
				$("#reqType").attr("value","basic.TagActionHandler.deletes");
				$("#form1").submit();
			}
		});
		
		$("#saveOrderbys").click(function(){
				$("#reqType").attr("value","basic.TagActionHandler.saveOrderBy");
				$("#form1").submit();
		});
	})
</script>
<!--
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td height="6"><img src="/images/spacer.gif" width="1" height="1" /></td>
    </tr>
    <tr>
      <td height="33"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="right-font08">
          <tr>
            <td width="150">共 <span class="right-text09">${pageVariable.totalpage}</span> 页 | 第 <span class="right-text09">${pageVariable.npage+1}</span> 页</td>
            <td align="right"><m:page action="basic.TagActionHandler.list" size="30" /></td>
          </tr>
      </table></td>
    </tr>
</table>
-->
</form>
</body>
</html>