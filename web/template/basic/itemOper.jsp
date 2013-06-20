<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.util.*"%>
<%@ page session="false" %>
<%@ page import="org.apache.log4j.Logger.*"%>
<%@ page import="org.apache.log4j.xml.DOMConfigurator.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/template/basic/common_head.jsp"%>


<!--表单验证控件引入文件 开始-->
<script type="text/javascript" src="/plugin/jquery.easyvalidator/js/easy_validator.pack.js"></script>
<link href="/plugin/jquery.easyvalidator/css/validate.css" rel="stylesheet" type="text/css" />
<!--表单验证控件引入文件 结束-->



<title>${!empty param.ITEM$id?'修改':'添加'}子键</title>
<jsp:include page="/inc.jsp" flush="false" />
</head>

<body>



<form action="${!empty param.ITEM$id?'/basic.ItemAH.update':'/basic.ItemAH.add'}" method="post">
<c:if test="${empty (param.dicId)}">
<input type="hidden" name="ITEM$id" value='${obj.id}' />
<input type="hidden" name="ITEM$dicId" value='${obj.dicid}' />
</c:if>
<c:if test="${!empty (param.dicId)}">
<input type="hidden" name="ITEM$dicId" value='${param.dicId}' />
</c:if>
<div class="tab_title"><div id="tab_title">
	<div>${!empty param.ITEM$id?'修改':'添加'}子键</div>
	<div><input type="submit" value="提交保存" /><input onclick="history.back()" type="button" value="取消返回" /></div>
</div></div>
<table border="1" cellspacing="0" cellpadding="0" class="ui op">
    <tr> 
      <td class="bt">键名(N)：</td>
      <td><input style="width:120px" reg="^.+$" tip="键名不能为空" type="text" value="${obj.ikey}" name="ITEM$ikey" /></td>
    </tr>
    <tr> 
      <td class="bt">数值数据(V)：</td>
      <td><input style="width:220px" reg="^.+$" tip="数值数据不能为空" type="text" value="${obj.name}" name="ITEM$name" /></td>
    </tr>
    <tr> 
      <td>备注：</td>
      <td><input style="width:220px" type="text" value="${obj.memo}" name="ITEM$memo" /></td>
    </tr>
</table>
</form>


<div class="remarks">
1、每个注册表的参数项控制了一个用户的功能或者系统的功能。<br />
2、全局参数设置是PLM中的一个重要的数据库，用于存储系统和模块的设置信息。
</div>





</body>
</html>