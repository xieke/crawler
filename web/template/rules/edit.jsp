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
<!--UI套装 开始-->
<link type="text/css" href="/plugin/jquery-ui-1.8.13/css/ui-lightness/jquery-ui-1.8.13.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="/plugin/jquery-ui-1.8.13/js/jquery-ui-1.8.13.custom.min.js"></script>
<!--UI套装 结束-->

<script type="text/javascript">
	jQuery(document).ready(function($){ //fire on DOM ready
		$("[insert_text][insert_text_obj]").click(function(){//插件支持
			var textarea_obj=$($(this).attr("insert_text_obj"));
			insert_text(textarea_obj[0],$(this).attr("insert_text"));
		});
		
		$("#cycle_text [type='checkbox']").click(function(){
			var cycle="";
			$("#cycle_text :checked").each(function(){
				cycle+=$(this).val()+",";
			})
			$("#cycle_hidden").val(cycle);
		})
	});
	
	function insert_text(obj, str) {//texarea插入粘贴内容
		obj.focus();
		if (document.selection) {
			var sel = document.selection.createRange();
			sel.text = str;
		} else if (typeof obj.selectionStart == 'number' && typeof obj.selectionEnd == 'number') {
			var startPos = obj.selectionStart,
			endPos = obj.selectionEnd,
			cursorPos = startPos,
			tmpStr = obj.value;
			obj.value = tmpStr.substring(0, startPos) + str + tmpStr.substring(endPos, tmpStr.length);
			cursorPos += str.length;
			obj.selectionStart = obj.selectionEnd = cursorPos;
		} else {
			obj.value += str;
		}
	}

</script>

</head>

<body>
<div class="ui_head"></div>



<form method="post" action="/GeneralHandleSvt">
<input type="hidden" id="reqType" name="reqType" value="rules.RulesActionHandler.save" />
<input type="hidden" id="id" name="rules$id" value="${obj.id}" />

<table class="ui edit">
<tr class="title"><td colspan="2">编辑策略</td></tr>
    <tr>
        <td width="8%">策略编号</td>
        <td><input type="text" name="rules$no" value="${obj.no}"/></td>
    </tr>
    <tr>
        <td>策略名称</td>
        <td><input type="text" name="rules$name" value="${obj.name}"/></td>
    </tr>
    <tr>
        <td colspan="2">标签规则（包含以下标签的触发）</td>
    </tr>
    <tr>
        <td></td>
        <td>
			<input type="hidden" id="tag_ids" name="tag_ids" value="${obj.tag_ids}" />
            <input type="hidden" id="tags" name="tags" />
			<ul style="float: none;height: 150px;width: 660px;" id="tags_result" class="tags_result select_label"></ul>
            <c:set var="tagsList_pf" value="${tags}" />
            <%@ include file="/template/tag/tag_plugin.jsp"%>
        </td>
        
    </tr>
    <tr>
    	<td>GP重要度</td>
        <td><input type="text" name="rules$importance" value="${obj.importance}" size="2" maxlength="2" onkeyup="value=value.replace(/[^\d]/g,'')" tip="只允许数字" />大于该数值时触发，空白为不限制</td>
    </tr>
    <tr>
    	<td>客户重要度</td>
        <td><input type="text" name="rules$urgent" value="${obj.urgent}" size="2" maxlength="2" onkeyup="value=value.replace(/[^\d]/g,'')" tip="只允许数字" />大于该数值时触发，空白为不限制</td>
    </tr>
    <tr>
    	<td>发送周期</td>
        <td id="cycle_text">
            <c:forEach var="detail" items="${cycleList}" varStatus="status">
                <label><input type="checkbox" id="${detail.id}" name="cycle" value="${detail.id}" ${detail.checked} />${detail.name}</label>
            </c:forEach>
            <input type="hidden" name="rules$cycle" id="cycle_hidden" value="${obj.cycle}" />
    </tr>
    <tr>
    	<td>语言</td>
        <td><input type="radio" name="rules$lang" value="c" <c:if test="${obj.lang=='c'||obj.lang==''||obj.lang==null}">checked="checked"</c:if> />中文&nbsp;<input type="radio" name="rules$lang" value="e" <c:if test="${obj.lang=='e'}">checked="checked"</c:if> />英文&nbsp;<input type="radio" name="rules$lang" value="ce" <c:if test="${obj.lang=='ce'}">checked="checked"</c:if> />中英文&nbsp;</td>
    </tr>
    <tr>
    	<td>邮件标题</td>
        <td><input type="text" id="title" name="rules$title" value="${obj.title}" size="57" />&nbsp;<input type="button" title="插入Date变量=当时日期" insert_text="@date" insert_text_obj="#title" value="插入Date变量" style="display:inline; float:none" /></td>
    </tr>
    <tr>
    	<td>邮件头部</td>
        <td>
<div style="float:left; width:450px"><textarea style="width:430px" type="text" id="head" name="rules$head" cols="50" rows="3">${obj.head}</textarea></div>
<div style="float:left">
<input type="button" title="插入name变量" insert_text="@name" insert_text_obj="#head" value="插入name变量" style="margin:3px 20px 5px 0; float:none" />
<input type="button" title="插入固定短语" insert_text="${head_txt}" insert_text_obj="#head" value="插入固定短语" item_id="${head_id}" id="head_txt" style="margin:3px 5px 5px 0;float:none; display:inline" />
<a title="编辑对以后有效,当前无效" style="text-decoration:underline ; color:#0000FF; " href="javascript:void(0)" onclick="editItem('head','#head_txt')" >编辑</a>
</div>
		</td>
    </tr>
    <tr>
    	<td>邮件尾部</td>
        <td>
<div style="float:left; width:450px"><textarea style="width:430px" type="text" id="foot" name="rules$foot" cols="50" rows="3">${obj.foot}</textarea></div>
<div style="float:left">
<input type="button" title="插入Date变量=当时日期" insert_text="@date" insert_text_obj="#foot" value="插入Date变量" style="margin:3px 20px 5px 0; float:none" />
<input type="button" title="插入固定短语" insert_text="${foot_txt}" insert_text_obj="#foot" value="插入固定短语" item_id="${foot_id}" id="foot_txt" style="margin:3px 5px 5px 0; float:none; display:inline" />
<a title="编辑对以后有效,当前无效" style="text-decoration:underline ; color:#0000FF; " href="javascript:void(0)" onclick="editItem('foot','#foot_txt')" >编辑</a>
</div>
		</td>
    </tr>
    <tr>
    	<td></td>
        <td><input type="submit" value="提交"  />
        <input type="button" value="放弃保存" onclick="window.history.go(-1);" /></td>
    </tr>
</table></form>


<div id="item_content" style="display:none">
	<table>
    	<tr><td id="content_title"></td></tr>
        <tr>
        	<td>
            	<!--富文本编辑控件引入文件 开始-->
<script charset="utf-8" src="/plugin/kindeditor-4.1.2/kindeditor-min.js"></script>
<script charset="utf-8" src="/plugin/kindeditor-4.1.2/lang/zh_CN.js"></script>
<script>
				    var editor=false;
					var editor_k;
					KindEditor.ready(function(K) {
							
							editor_k=K;

					});
</script>
                <!--富文本编辑控件引入文件 结束-->
            	<textarea id="content_txt" rows="9" cols="52" name="content_txt"></textarea>
                <input type="hidden" id="item_type" value="" />
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
	function editItem(src,obj){
		var title;
		if(src=='head') title= "编辑邮件头部信息";
		if(src=='foot') title= "编辑邮件尾部信息";
		//$("#content_txt").attr("value",$(obj).attr("insert_text"));
		//alert($("#content_txt").val());
		//alert($(obj).attr("insert_text"));
		if(editor!=false)
			editor.html($(obj).attr("insert_text"));
		$("#item_content" ).dialog({
			title:title,
			width:500,
			height:350,
			modal:true,
			resizable:false,
			autoOpen: true,
			open: function( event, ui ){
				//$("#content_txt").attr("value",$(obj).attr("insert_text"));
				if(editor!=false)return;
				editor = editor_k.create('#content_txt', {
					resizeType : 1,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					autoHeightMode : true,
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link']
				});
			},
			buttons: {
				"确定": function() {	
					editor.sync();
					$.post("/rules.RulesActionHandler.editItem",{item_type:src,item_id:$(obj).attr("item_id"),item_content:$("#content_txt").val()},function(result){
						var result2=eval(result);
						var json=result2[0];
	
						if(json.respCode=='0000'){
							$(obj).attr("insert_text",$("#content_txt").val());
							$(obj).attr("item_id",json.item_id);
							alert("编辑成功！");
						}else {
							alert("编辑失败！失败原因:"+json.respMsg);
						}
					})
					
					$( this ).dialog( "close" );
				},
				"取消": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}
</script>
</body>
</html>