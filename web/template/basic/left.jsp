
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>GpCore后台管理系统</title>
<style type="text/css">
<!--
body {
	margin: 0px;
	background-image: url(/images/left.gif);
}
table.left-table02{ display:none}
-->
</style>
<link href="/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/plugin/jquery-1.5.1.min.js"></script>

</head>
<body>

<table width="198" border="0" cellpadding="0" cellspacing="0" class="left-table01">
  <tr>
    <TD>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		  <tr>
			<td width="207" height="55" background="/images/nav01.gif">
				<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
				  <tr>
					<td width="25%" rowspan="2"><img src="/images/ico02.gif" width="35" height="35" /></td>
					<td width="75%" height="22" class="left-font01">您好，<span class="left-font02">${sessionScope.curuser.loginname}</span></td>
				  </tr>
				  <tr>
					<td height="22" class="left-font01">
						[&nbsp;<a href="/basic.LoginAH.exit" target="_top" class="left-font01">退出</a>&nbsp;]</td>
				  </tr>
				</table>
			</td>
		  </tr>
		</table>
<script type="text/javascript">
$(function(){
	$(".class_1").click(function(){
		var sub_menu=$(this).closest(".left-table03").next();
		sub_menu.toggle();
		if(sub_menu.css("display")=="none")
		$(this).parent().parent().find("img").attr("src","/images/ico04.gif");
		else
		$(this).parent().parent().find("img").attr("src","/images/ico03.gif");
	});
	$(".class_2").click(function(){
		$("img.c2_img").attr("src","/images/ico06.gif");
		$(this).parent().parent().find("img").attr("src","/images/ico05.gif");
	});
})
</script>


		<!--  分类1统开始    -->
		<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" class="left-table03">
          <tr>
            <td height="29">
				<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td width="8%"><img class="class_ico" src="/images/ico04.gif" width="8" height="11" /></td>
						<td width="92%">
								<a href="javascript:void(0)" target="mainFrame" class="left-font03 class_1">数据处理模块</a></td>
					</tr>
				</table>
			</td>
          </tr>		  
        </TABLE>
		<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="left-table02">
				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/basic.TagActionHandler.list" target="mainFrame" class="left-font03 class_2">Tag标签库</a></td>
				</tr>
				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/tag.TagRuleActionHandler.list" target="mainFrame" class="left-font03 class_2">自动Tag规则</a></td>
				</tr>
				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/tag.TagRuleActionHandler.showDupRule" target="mainFrame" class="left-font03 class_2">去重规则</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/news.GpMailAH.expTags" target="mainFrame" class="left-font03 class_2">导出所有tag列表</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/news.NewsActionHandler.toList?job=default" target="mainFrame" class="left-font03 class_2">手动数据处理</a></td>
				</tr>
                
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/news.NewsActionHandler.toList_bak?job=his_bak" target="mainFrame" class="left-font03 class_2">手动数据-历史</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/news.NewsActionHandler.listHistory?his_news$status=2" target="mainFrame" class="left-font03 class_2">已删除的数据</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/news.ErrWebSiteActionHandler.list?err_web_site$status=0" target="mainFrame" class="left-font03 class_2">出错的源信息</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/weibo.WeiBoAH.listWeibo" target="mainFrame" class="left-font03 class_2">微博数据</a></td>
				</tr>
	  </table>
		<!--  分类1结束    -->

		<!--  分类1统开始    -->
		<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" class="left-table03">
          <tr>
            <td height="29">
				<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td width="8%"><img class="class_ico" src="/images/ico04.gif" width="8" height="11" /></td>
						<td width="92%">
								<a href="javascript:void(0)" target="mainFrame" class="left-font03 class_1">邮件发送模块</a></td>
					</tr>
				</table>
			</td>
          </tr>		  
        </TABLE>
		<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="left-table02">
        
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="<m:out value='/customer.CustomerActionHandler.list' />" id="customer_id" target="mainFrame" class="left-font03 class_2">客户管理</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="<m:out value='/rules.RulesActionHandler.list' />" target="mainFrame" class="left-font03 class_2">自动邮件策略配置</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/news.NewsActionHandler.toListMail?job=default" target="mainFrame" class="left-font03 class_2">邮件输出</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/news.GpMailAH.listPost" target="mainFrame" class="left-font03 class_2">邮件发送审核</a></td>
				</tr>

				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/news.GpMailAH.upload" target="mainFrame" class="left-font03 class_2">上传邮件配置文件</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="<m:out value='/post.PostJobAH.list' />" target="mainFrame" class="left-font03 class_2">邮件发送任务</a></td>
				</tr>
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/post.MailServerAH.list" target="mainFrame" class="left-font03 class_2">邮件服务器配置</a></td>
				</tr>
				<tr>

				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/post.PostJobAH.listPosted" target="mainFrame" class="left-font03 class_2">邮件发送记录</a></td>
				</tr>
      </table>
		<!--  分类1结束    -->






		<!--  分类1统开始    -->
		<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" class="left-table03">
          <tr>
            <td height="29">
				<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td width="8%"><img class="class_ico" src="/images/ico04.gif" width="8" height="11" /></td>
						<td width="92%">
								<a href="javascript:void(0)" target="mainFrame" class="left-font03 class_1">系统管理模块</a></td>
					</tr>
				</table>
			</td>
          </tr>		  
        </TABLE>
		<table width="80%" border="0" align="center" cellpadding="0" 
				cellspacing="0" class="left-table02">
				<tr>
				  <td width="9%" height="20" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/basic.UserAH.listUser" target="mainFrame" class="left-font03 class_2">用户管理</a></td>
				</tr>
				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/basic.ModuleAH.listModule" target="mainFrame" class="left-font03 class_2">模块配置</a></td>
				</tr>

				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/system.AdminAH.viewConn" target="mainFrame" class="left-font03 class_2">查看数据库连接</a></td>
				</tr>
						
				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/system.AdminAH.clearCache" target="mainFrame" class="left-font03 class_2">清除缓存</a></td>
				</tr>
				
				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/basic.RoleAH.listRole" target="mainFrame" class="left-font03 class_2">权限配置</a></td>
				</tr>
				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/template/basic/dictionary.jsp" target="mainFrame" class="left-font03 class_2">全局参数设置</a></td>
				</tr>

				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/task.TaskAH.listTask" target="mainFrame" class="left-font03 class_2">任务调度</a></td>
				</tr>
				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/businesslog" target="mainFrame" class="left-font03 class_2">系统日志</a></td>
				</tr>

				<tr>
				  <td width="9%" height="21" ><img class="c2_img" src="/images/ico06.gif" width="8" height="12" /></td>
				  <td width="91%"><a href="/template/basic/pwdModify.jsp" target="mainFrame" class="left-font03 class_2">修改密码</a></td>
				</tr>
      </table>
		<!--  分类1结束    -->
	  </TD>
  </tr>
  
</table>
</body>
</html>
