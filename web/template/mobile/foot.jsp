<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<div id="footer">
  <!--<center>
    <div id="wptouch-switch-link"> 
      <script type="text/javascript">function switch_delayer() { window.location = "/"}</script>Mobile Version <a id="switch-link" onclick="wptouch_switch_confirmation();" href="javascript:return false;"></a> </div>
  </center>-->
  <p><a href="<m:out value='www_url' type='system_core'/>/news.NewsActionHandler.listPhone?jobid=${param.jobid}&email=${param.email}">All News</a> | <a href="/template/mobile/feedback.jsp?email=${param.email}">Feedback</a></p>
  <p>Goldpebble Research Copyright &copy; All Rights Reserved</p>
  <p><a href="http://www.goldpebble.com/terms-of-service/" target="_blank">Terms of Service</a></p>
  <script type='text/javascript' src='/images/mobile/jquery.form.min.js?ver=2.73'></script> 
</div>
</body>
</html>