<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>易生会员中心</title>
<link href="css/base.css" rel="stylesheet" type="text/css" />
<link href="css/member_style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/tabs.js"></script>
<script type="text/javascript">
$(function(){
	$(".layer1").Tabs({event:'click'});
	$(".layer2").Tabs({event:'click'});
	$(".layer3").Tabs({event:'click'});
	var _cur=0;
	$(".change_bar").click(function(){
		var _$len=$(".tab0 li").length;
		var _$index=$(".tab0 li.select").index();
		_cur=_$index+1;
		if(_cur>_$len||_cur==_$len){
			_cur=0;
		}
		$(".tab0 li").eq(_cur).trigger("mouseover");

	});
});
</script>

   
<script language="javascript">
<!--
function senfe(o,a,b,c){
var t=$(o).find("tr");
for(var i=0;i<t.length;i++){
t[i].style.backgroundColor=(t[i].sectionRowIndex%2==0)?a:b;
t[i].onmouseover=function(){
if(this.x!="1")this.style.backgroundColor=c;
}
t[i].onmouseout=function(){
if(this.x!="1")this.style.backgroundColor=(this.sectionRowIndex%2==0)?a:b;
}
}
}
-->
</script>

</head>

<body>
<div class="wrapper">
<!-- header -->
	<jsp:include page="/template/basic/header1.jsp" flush="false" />
<!-- content-->
	<div class="content clearfix">
		<div class="wzh clearfix">
<!-- sidebar-->
			<div class="wzhl fl">
				<jsp:include page="/template/center/sidebar.jsp" flush="false" />
			</div> 
<!-- zhanghu-->
<div style="height:700px" class="wzhr fr">
<div class="tit_2 layer1"><h3>个人资料</h4>
	         ${empty from?'':'<font color=red>您的登录名现在为身份证号，请修改您的登录名</font>'}
	
 <form action="/GeneralHandleSvt"  method="post" id="post_form" name="post_form" ENCTYPE="multipart/form-data">
    <m:token/>
    <input type="hidden" id="reqType" name="reqType" value="basic.UserCenter.save">
    <input type="hidden" name="objId" id="objId" value="${employee.userid}"/>
		 
	  <ul class="buc clearfix">
	  	<li>
			<font>用户头像</font><label><img style="height: 148px;width: 148px;" src="${employee.photoid.url}?load" onerror="this.src='images/pic.jpg'" class="bg fl" />
			</label>
		</li>
		<br/><br/><br/><br/><br/><br/><br/><br/>
		<li>
			<font>上传头像</font><label><input type='FILE' name='FILE(0)' size='35' value="上传图片" />
        (最佳尺寸：148px×148px) 
			</label>
		</li>
		<li>
			<font>家庭地址</font><label><input name="employee$familyaddress" type="text" id="employee$familyaddress" value="${employee.familyaddress}" class="loinput info_wz400" />
			<input name="employee$userid" type="hidden" id="employee$userid" value="${employee.userid}" class="loinput info_wz400" />
			</label>
		</li>
		<li>
			<font>登录名</font><label><input name="employee$loginname" type="text" id="employee$loginname" value="${employee.loginname}" class="loinput info_wz400" /></label>
  
		</li>
		<li>
			<font>工作地址</font><label><input name="employee$address" type="text" id="employee$address" value="${employee.address}" class="loinput info_wz400" /></label>
		</li>
		<li>
			<font>联系电话</font><label><input name="employee$telno2" type="text" id="employee$telno2" value="${employee.telno2}" class="loinput info_wz400" /></label>
		</li>
		<li>
			<font>QQ/MSN</font><label><input name="employee$qqno" type="text" id="employee$qqno" value="${employee.qqno}" class="loinput info_wz400" /></label>
		</li>
		<li>
			<font>工作单位</font><label><input name="employee$company" type="text" id="employee$company" value="${employee.qqno}" class="loinput info_wz400" /></label>
		</li>
		<li style="height:100px;">
			<font>个人简介</font>
			<label><textarea name="employee$remark" class="loinput info_wz400 info_hg100">${employee.remark}</textarea></label>
		</li>
	  </ul>
	  <div  class="info_bntdiv clearfix">
		<span class="c-button mr20 clearfix"><a href="#"onclick="$('#post_form').submit()">保&nbsp;存</a></span><span class="f-button clearfix"><a href="<c:url value='/basic.HomeAH.index'/>">取&nbsp;消</a></span>
	  </div></form></div>
</div>
	</div>
	</div>
<!-- footer-->
	<jsp:include page="/template/basic/footer.jsp" flush="false" />
</div>
</body>
</html>
