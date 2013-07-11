<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<input type="button" value="选择策略" id="select_tactics" class="button" style="display:inline; float:none" /><input type="button" value="全部清除" id="clear_tag" class="button" style="display:inline; float:none" />
<script type="text/javascript">
$(function(){
	$("#select_tactics").click(function(){
		if($(this).val()=="选择策略"){
			$(this).val("完成选择").css("color","red");
			$("#tactics_text").slideDown();
		}else{
			$(this).val("选择策略").css("color","#174B73");;
			$("#tactics_text").slideUp();
		}
	});
	
	$("#tactics_text div.result input").click(function(){
		refreshTags_tactics();
	}).dblclick(function(){
		select_under_level($(this).attr("level"),$(this),$(this).attr("checked")?false:true);
	});
	function select_under_level(level,obj,ischecked){//选择或取消下级策略
		obj.attr("checked",ischecked);//当前赋值
		var next=obj.parent().next().find("input");
		if(next.attr("level")>level){
			select_under_level(level,next,ischecked);
		}else refreshTags_tactics();
	}
	
	$("#clear_tag").click(function(){
		$("#tactics").attr("value","");
		$("#tactics_text :checked").attr("checked","");
		refreshTags_tactics();
	});
	
	$("#search_tactics").keyup(function(){
		search_tactics($(this));
	}).blur(function(){
		search_tactics($(this));
	}).focus(function(){
		search_tactics($(this));
	});
	//删除策略内容
	$("#tactics").blur(function(){
		$("#tactics_text :checked").attr("checked","");
		$("[tagsname='"+$("#tactics").val().replace(new RegExp(",","gm"),"'],[tagsname='")+"']").attr("checked","checked");
		refreshTags_tactics();
	})
});
function search_tactics(obj){
	if(obj.val()!=""){
		$("#tactics_text .result label").hide();
		$("#tactics_text .result label:contains("+obj.val().toLowerCase()+")").show();
	}else
		$("#tactics_text .result label").show()
}
function refreshTags_tactics(){
	var tagsname = "";
	var tagsid = "";
	var i=0;
	$("#tactics_text :checked").each(function(){
		if(i>0){
			tagsname += ",";
			tagsid += ","
		}
		tagsname += $(this).attr("tagsname");
		tagsid += $(this).attr("tagsid");
		++i;
	});	
	$("#tag_ids").attr("value",tagsid);
	$("#tactics").val(tagsname);
}
</script>
<div id="tactics_text" class="tag_plugin">
	<div class="search"> &nbsp;模糊搜索：<input id="search_tactics" type="text" /> &nbsp;<a href="javascript:void(0)" onclick="$('#search_tactics').val('').focus()">重填</a></div>
    <div class="result">
    <c:forEach var="detail" items="${tagsList_pf}" varStatus="status">
		<label title="${detail.name}"><input level="${fn:length(detail.level)}" type="radio" id="${detail.id}" tagsid="${detail.id}" tagsname="${detail.name}" name="tactics" value="${detail.id}" ${detail.checked} /><span class="dx">${detail.level}${fn:toLowerCase(detail.name)}</span>${detail.level}<m:out maxSize="20" value="${detail.name}" /></label>
	</c:forEach>
    </div>
</div>