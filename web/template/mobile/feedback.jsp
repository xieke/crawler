<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ include file="head.jsp"%>
<div class="content single">
  <div id="comment_wrapper">
    <ol class="commentlist" id="commentlist">
      
      <!-- If comments are open, but there are no comments. -->
      <li id="hidelist" style="display:none"></li>
    </ol>
    
    <!--end comment status-->
    
    <div id="textinputwrap">
      <div id="refresher" style="display:none;"> <img src="http://www.shengchao.name/blog/wp-content/plugins/wptouch/images/good.png" alt="checkmark" />
        <h3>Send Success!</h3>
        &rsaquo; Thank you</div>
      <form action="/news.GpMailAH.sendMail" method="post" id="commentform">
        <h3 id="respond">Feedback</h3>
        <p>
          <input type="text" name="author" id="author" value="" size="22" tabindex="1" />
          <label for="author">Name *</label>
        </p>
        <p>
          <input name="email" id="email" type="email" value="" size="22" tabindex="2" />
          <label for="email">Mail *</label>
        </p>
        <div id="errors" style="display:none">There was an error posting your comment. Maybe it was too short?</div>
        <p>
          <textarea name="content" id="comment" tabindex="4"></textarea>
        </p>
        <p>
          <input name="submit" type="submit" id="submit" tabindex="5" value="Send" />
        <div id="loading" style="display:none"> <img src="http://www.shengchao.name/blog/wp-content/plugins/wptouch/themes/core/core-images/comment-ajax-loader.gif" alt="" />
          <p>Send...</p>
        </div>
        </p>
      </form>
    </div>
    <!--textinputwrap div--> 
  </div>
  <script type="text/javascript">
jQuery(document).ready( function() {
// Ajaxify '#commentform'
var formoptions = { 
	beforeSubmit: function() {
		$wpt("#loading").fadeIn(400);
	},
	success:  function() {
		$wpt("#commentform").hide();
		$wpt("#loading").fadeOut(400);
		$wpt("#refresher").fadeIn(400);
		}, // end success 
	error:  function() {
		$wpt('#errors').show();
		$wpt("#loading").fadeOut(400);
		} //end error
	} 	//end options
$wpt('#commentform').ajaxForm(formoptions);
}); //End onReady
</script>
</div>
<%@ include file="foot.jsp"%>