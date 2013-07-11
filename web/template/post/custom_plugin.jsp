<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<input type="button" value="选择客户" id="select_custom" class="button" style="display:inline; float:none" /><input type="button" value="全部清除" id="clear_tag" class="button" style="display:inline; float:none" />
<script type="text/javascript">
$(function(){
	$("#select_custom").click(function(){
		if($(this).val()=="选择客户"){
			$(this).val("完成选择").css("color","red");
			$("#select_text").slideDown();
		}else{
			$(this).val("选择客户").css("color","#174B73");;
			$("#select_text").slideUp();
		}
	});
	
	$("#select_text div.result input").click(function(){
		refreshTags();
	}).dblclick(function(){
		select_under_level($(this).attr("level"),$(this),$(this).attr("checked")?false:true);
	});
	function select_under_level(level,obj,ischecked){//选择或取消下级客户
		obj.attr("checked",ischecked);//当前赋值
		var next=obj.parent().next().find("input");
		if(next.attr("level")>level){
			select_under_level(level,next,ischecked);
		}else refreshTags();
	}
	
	$("#clear_tag").click(function(){
		$("#custom").attr("value","");
		$("#select_text :checked").attr("checked","");
		refreshTags();
	});
	
	$("#search_custom").keyup(function(){
		search_custom($(this));
	}).blur(function(){
		search_custom($(this));
	}).focus(function(){
		search_custom($(this));
	});
	//删除客户内容
	$("#custom").blur(function(){
		$("#select_text :checked").attr("checked","");
		$("[tagsname='"+$("#custom").val().replace(new RegExp(",","gm"),"'],[tagsname='")+"']").attr("checked","checked");
		refreshTags();
	})
});
function search_custom(obj){
	if(obj.val()!=""){
		$("#select_text .result label").hide();
		$("#select_text .result label:contains("+obj.val().toLowerCase()+")").show();
	}else
		$("#select_text .result label").show()
}
function refreshTags(){
	var tagsname = "";
	var tagsid = "";
	var i=0;
	$("#select_text :checked").each(function(){
		if(i>0){
			tagsname += ",";
			tagsid += ","
		}
		tagsname += $(this).attr("tagsname");
		tagsid += $(this).attr("tagsid");
		++i;
	});	
	$("#tag_ids").attr("value",tagsid);
	$("#custom").val(tagsname);
}
</script>
<div id="select_text" class="tag_plugin">
	<div class="search"> &nbsp;模糊搜索：<input id="search_custom" type="text" /> &nbsp;<a href="javascript:void(0)" onclick="$('#search_custom').val('').focus()">重填</a></div>
    <div class="result">
    <c:forEach var="detail" items="${tagsList_pf}" varStatus="status">
		<label title="${detail.name}"><span class="dx">${detail.level}${fn:toLowerCase(detail.name)}</span>${detail.level}<m:out maxSize="20" value="${detail.name}" /><input level="${fn:length(detail.level)}" type="checkbox" id="${detail.id}" tagsid="${detail.id}" tagsname="${detail.name}" name="tactics" value="${detail.id}" ${detail.checked} /></label>
	</c:forEach>
    </div>
</div>