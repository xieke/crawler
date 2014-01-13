<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld"%>
<%@ page contentType="text/html;charset=utf-8" %>
<style type="text/css">
/*选择人员控件*/
.select_label{ line-height:26px; padding:5px; overflow-y:auto; overflow-x:hidden;border: 1px solid #ABADB3;float: left;height: 40px;margin-right: 20px;width: 50%;}
.select_label .mb{ background:#eaf5ff; border:1px solid #bfe0ff; text-decoration:none; padding:0 20px 0 10px; position:relative; margin:2px; display:block; float:left}
.select_label .mb_holder{ background:#fff; border:1px dashed #ccc; padding:0 20px 0 10px; position:relative; margin:2px; display:block; float:left}
.select_label .mb:hover{ background:#b5daff; border:1px solid #9acdff;}
.select_label .mb span.del{ width:17px; height:17px; background:url(images/label_close.gif) no-repeat; position:absolute; top:0; right:0;display:block; margin:1px}
.select_label .mb:hover span.del{ background-position:0 -17px}
.select_label .mb:hover span.del:hover{ background-position:0 bottom}
.tags_single_line{float: none;height: 80px;width: 660px;}
.full_winodws{ width:auto; height:auto; margin:0; position: absolute; left:0; right:0; top:0; bottom:0; background:#fff}
</style>
<input type="button" value="选择标签" id="select_tag" class="button" style="display:inline; float:none" /><input type="button" value="全部清除" id="clear_tags" class="button" style="display:inline; float:none" />
<script type="text/javascript" src="/plugin/jquery.dragsort-0.4.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#select_tag").click(function(){
		if($(this).val()=="选择标签"){
			$(this).val("完成选择").css("color","red");
			$("#tag_text").slideDown();
			$("#tags_result").addClass("full_winodws");
		}else{
			$(this).val("选择标签").css("color","#174B73");;
			$("#tag_text").slideUp();
			$("#tags_result").removeClass("full_winodws");
		}
	});
        $(".tag_plugin .search").mousedown(function(e){//e鼠标事件  
            $(this).css("cursor","move");//改变鼠标指针的形状  
            var offset = $(this).offset();//DIV在页面的位置  
            var x = e.pageX - offset.left;//获得鼠标指针离DIV元素左边界的距离  
            var y = e.pageY - offset.top;//获得鼠标指针离DIV元素上边界的距离  
            $(document).bind("mousemove",function(ev)//绑定鼠标的移动事件，因为光标在DIV元素外面也要有效果，所以要用doucment的事件，而不用DIV元素的事件  
            {  
                $(".tag_plugin .search").stop();//加上这个之后  
                var _x = ev.pageX - x;//获得X轴方向移动的值  
                var _y = ev.pageY - y;//获得Y轴方向移动的值  
                $(".tag_plugin").animate({left:_x+"px",top:_y+"px"},10);  
            });
        });  
        $(document).mouseup(function()  
        {  
            $(".tag_plugin .search").css("cursor","default");  
            $(this).unbind("mousemove");  
        })
	$("#tag_text div.result input").click(function(){
		tags_add_or_del(this);//添加或删除标签
	}).dblclick(function(){
		if($.browser.msie){
			ischecked=$(this).attr("checked")?"checked":"";
		}else{
			ischecked=$(this).attr("checked")?"":"checked";
		}
		select_under_level($(this).attr("level"),$(this),ischecked);
	});

	$("#clear_tags").click(function(){
		$("#tags").attr("value","");
		$("#tag_text :checked").attr("checked","");
		$("#tags_result").html("");
	});
	
	$("#search_tag").keyup(function(){
		search_tag($(this));
	}).blur(function(){
		search_tag($(this));
	}).focus(function(){
		search_tag($(this));
	});
	$("#tags_result").dragsort({
			placeHolderTemplate: "<li class='mb_holder'></li>",
			scrollSpeed: 5
	});
	//页面初加载还原标签中原值
	if($("#tag_ids").val()!=""){
		temp_val="#tag_text [tagsid='"+$("#tag_ids").val().replace(/,/g,"'],#tag_text [tagsid='")+"']";
		//alert(temp_val);
		var temp_arr=temp_val.split(",")
		//objs=$(temp_val);
		//$(temp_val).each(function(){
			//tags_add_or_del(this);
		//})
		
		//alert(objs.length);
		for(i=0;i<temp_arr.length;i++){
			tags_add_or_del($(temp_arr[i]));
		}
	}
	//自动绑定FORM提交时，将标签值保存
	$("#tags").closest("form").submit(function(){
			temp_val="";
			temp_val2="";
			$("#tags_result li").each(function(i){
				if(i==0){
					temp_val+=$(this).attr("tagsid");
					temp_val2+=$(this).attr("tagsname");
				}else{
					temp_val+=","+$(this).attr("tagsid");
					temp_val2+=","+$(this).attr("tagsname");
				}
			})
			$("#tag_ids").val(temp_val+",");
			$("#tags").val(temp_val2+",");
	})
});
function select_under_level(level,obj,ischecked){//选择或取消下级标签
	obj.attr("checked",ischecked);//当前赋值
	tags_add_or_del(obj);//添加或删除标
	var next=obj.parent().next().find("input");
	if(next.attr("level")>level){
		select_under_level(level,next,ischecked);
	}
}
function search_tag(obj){
	if(obj.val()!=""){
		$("#tag_text .result label").hide();
		$("#tag_text .result label:contains("+obj.val().toLowerCase()+")").show();
	}else
		$("#tag_text .result label").show()
}
function tags_add_or_del(obj){
	if($(obj).attr("checked")){
		$("#tags_result").html($("#tags_result").html()+'<li tagsname="'+$(obj).attr("tagsname")+'" tagsid="'+$(obj).attr("tagsid")+'" class="mb">'+$(obj).attr("tagsname")+'<span class="del" onMouseDown="del_parent_node(this)"></span></li>');
	}else{
		$("#tags_result").find("li[tagsid='"+$(obj).attr("tagsid")+"']").remove();
	}
}
// 删除结点函数
function del_parent_node(obj){
	//window.Event.stopPropagation(); 
	$(obj).parent().fadeOut(function(){$(this).remove();})
	$("#tag_text input[tagsid='"+$(obj).parent().attr("tagsid")+"']").attr("checked",false);
}
</script>
<div id="tag_text" class="tag_plugin">
	<div class="search">搜索：<input id="search_tag" type="text" /> &nbsp;<a href="javascript:void(0)" onclick="$('#search_tag').val('').focus()">重填</a> <a href="javascript:void(0)" onclick="$('#select_tag').click()">关闭</a></div>
    <div class="result">
    <c:forEach var="detail" items="${tagsList_pf}" varStatus="status">
		<label title="${detail.name}"><span class="dx">${detail.level}${fn:toLowerCase(detail.name)}</span>${detail.level}<m:out maxSize="20" value="${detail.name}" /><input level="${fn:length(detail.level)}" type="checkbox" id="${detail.id}" tagsid="${detail.id}" tagsname="${detail.name}" name="tag$id" value="${detail.id}" ${detail.checked} /></label>
	</c:forEach>
    </div>
</div>