<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<c:url value='/theme/adobe/index.css'/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/plugin/jquery-1.5.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/plugin/jquery.cookies.2.2.0.min.js'/>"></script>
<title><m:tool>sys_name</m:tool>  <m:tool>sys_edition</m:tool></title>
</head>
<body>
<div class="top">
  <div class="logo"></div>
  <!--
        <dd><a target="main" href="<c:url value='/basic.HolidayActionHandler.show'/>">节假日设定</a></dd>
        <dd><a target="main" href="<c:url value='/basic.project.GradeRateActionHandler.list'/>">等级费率设置</a></dd>
        <dd><a target="main" href="<c:url value='/basic.project.TemplateActionHandler.list?bill_type=0'/>">任务模板设置</a></dd>
        <dd><a target="main" href="<c:url value='/GeneralHandleSvt?reqType=test.TestAH.listCaseType'/>">测试案例类型</a></dd>
        <dd><a target="main" href="<c:url value='/GeneralHandleSvt?reqType=buflow.BuFlowAH.listFlow'/>">业务流列表</a></dd>
        <dd><a target="main" href="<c:url value='/GeneralHandleSvt?reqType=taskta.WaitVerifyExecutantAH.listWaitExecutantVerify'/>">执行人审核</a></dd>
        <dd><a target="main" href="<c:url value='/GeneralHandleSvt?reqType=taskta.WaitVerifyExecutantAH.listExecutantVerify'/>">执行人已审核</a></dd>
        <dd><a target="main" href="<c:url value='/GeneralHandleSvt?reqType=taskta.WaitVerifyExecutantAH.listTaskVerify'/>">任务已验收</a></dd>
        <dd><a target="main" href="<c:url value='/GeneralHandleSvt?reqType=taskta.ValidateTaskAH.listWaitValidateTask'/>">任务待验收</a></dd>
        <dd><a target="main" href="<c:url value='/GeneralHandleSvt?reqType=taskta.DelayExecutantAH.listDelayExecutant'/>">延期单列表</a></dd>
-->
  <ul id="lists">
    <li class="single"><a href="#nogo" class="tl single"><u></u>系统管理
      <!--[if IE 7]><!--></a><!--<![endif]-->
      <!--[if lte IE 6]><table><tr><td><![endif]-->
      <div class="pos2">

        <dl>
                <dt><a href="nogo">会员管理</a></dt>
                <dd><a target="main" href="<c:url value='/basic.UserAH.listUser'/>">会员资料管理</a></dd>
                <dd><a target="main" href="<c:url value='/basic.UserAH.showUser'/>">添加新用户</a></dd>
                <dd><a target="main" href="<c:url value='/admin.AdminCardActionHandler.listCard'/>">会员卡列表</a></dd>
        </dl>
         <dl>
                <dt><a href="nogo">认证管理</a></dt>
                <dd><a target="main" href="<c:url value='/real.RealActionHandler.prelistReal'/>">待审核实名认证</a></dd>
                <dd><a target="main" href="<c:url value='/real.RealActionHandler.listReal'/>">实名认证列表</a></dd>
        </dl>
        <dl>
                <dt><a href="nogo"><fmt:message key="core_set"/></a></dt>
                <dd><a target="main" href="<c:url value='/template/basic/dictionary.jsp'/>">全局参数设置</a></dd>
                <dd><a target="main" href="<c:url value='/basic.ModuleAH.listModule'/>">模块管理</a></dd>
		        <dd><a target="main" href="<c:url value='/basic.ExcpAH.listExcp'/>">异常管理</a></dd>
                <dd><a target="main" href="<c:url value='/task.TaskAH.listTask'/>">定时任务</a></dd>
		        <dd><a target="main" href="<c:url value='/basic.GlobalAH.listOnlineUser'/>">当前在线用户</a></dd>
                <dd><a target="main" href="<c:url value='/basic.RoleAH.listRole'/>">角色管理</a></dd>
                <dd><a target="main" href="<c:url value='/card.AccInformationServiceAH.showInfoRate'/>">信息服务费率设置</a></dd>
                <dd><a target="main" href="<c:url value='/basic.LinkDesktopActionHandler.list'/>">桌面应用设置</a></dd>
        
        </dl>
        <dl>
          <dt><a href="#"><fmt:message key="developer_tools"/></a></dt>

        <dd><a target="main" href="<c:url value='/system.AdminAH.clearCache'/>">清除缓存</a></dd>
        <dd><a target="main" href="<c:url value='/system.AdminAH.clearBizCache'/>">清除Biz缓存</a></dd>
        <dd><a target="main" href="<c:url value='/system.AdminAH.unLock'/>">数据库解锁</a></dd>
        <dd><a target="main" href="<c:url value='/system.AdminAH.statCache'/>">查看缓存状态</a></dd>
        <dd><a target="main" href="<c:url value='/system.AdminAH.viewConn'/>">查看连接池状态</a></dd>
		<dd><a target="main" href="<c:url value='/system.AdminAH.debugOn'/>">打开debug</a></dd>
		<dd><a target="main" href="<c:url value='/system.AdminAH.debugOff'/>">关闭debug</a></dd>
        <dd><a target="main" href="<c:url value='/basic.GlobalAH.ant'/>">系统ANT</a></dd>
        <dd><a target="main" href="<c:url value='/card.ArticleAH.list'/>">文章管理</a></dd>
        </dl>
      </div>
      <!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
    <li class="single"><a href="#nogo" class="tl single"><u></u>帮助
      <!--[if IE 7]><!--></a><!--<![endif]-->
      <!--[if lte IE 6]><table><tr><td><![endif]-->
      <div class="pos4">
        <dl>

				<dt><a href="#nogo">版权信息</a></dt>
         <dd><a target="main" href="<c:url value='/template/basic/reg.jsp'/>">产品注册</a></dd>
				<dd><a target="main" href="<c:url value='/basic.GlobalAH.version'/>">关于<m:tool>sys_name</m:tool></a></dd>
        </dl>
      </div>
      <!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
  </ul>
  <div class="user_op">
  	<a href="<c:url value='/basic.GlobalAH.version'/>" target="main" class="homepage" title="进入个人主页">首页</a>
    <ul id="user_op">
      <li class="single"><a target="main" href="/basic.GlobalAH.version" class="tl single"><u></u>${sessionScope.curuser.userName}
        <!--[if IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]-->
        <div class="pos4">
          <dl>
            <dt><a target="main" href="<c:url value='/basic.HomeAH.index'/>">
              <fmt:message key="user_center"/>
              </a></dt>
            <dd><a target="main" href="/basic.MsgAH.listInbox">
              <fmt:message key="message"/>
              </a></dd>

			<dd><a target="main" href="<c:url value='/basic.UserCenter.accountSettings'/>">

			  <fmt:message key="account_settings"/>
              </a></dd>
            <dd><a target="main" href="/basic.UserAH.signIn">
					上班签到
              </a></dd>
			<dd><a target="main" href="/basic.UserAH.signOut">
			下班签到
				</a></dd>

			<dd><a href="<c:url value='/basic.LoginAH.exit'/>">
              <fmt:message key="exit"/>
              </a></dd>
          </dl>
        </div>
        <!--[if lte IE 6]></td></tr></table></a><![endif]-->
      </li>
    </ul>
    <a href="<c:url value='/basic.LoginAH.exit'/>" class="exit_sys" title="<fmt:message key='exit'/>">
    <fmt:message key="exit"/>
    </a> </div>
</div>
<div class="body">
  <iframe name="main" id="main" src="/basic.GlobalAH.version" width="100%" height="100%" frameborder="0"></iframe>
  <script type="text/javascript">if($.cookies.get('iframe_main'+location.port))$("#main").attr("src",$.cookies.get('iframe_main'+location.port));</script>
</div>
<!--[if IE 7]>
<script type="text/javascript">
$(function(){
    window.onresize = function(){
        var heights = document.documentElement.clientHeight-39;
        $("#main").height(heights);
    }
    window.onresize();
});
</script>
<![endif]-->
</body>
</html>
