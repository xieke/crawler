/*
-------------------------------------------------------------------------------
文件名称：progress.js
说    明：JavaScript脚本，进度条
版    本：1.0
修改纪录:
---------------------------------------------------------------------------
时间        修改人      说明
2005-5-8   zhouming        增加了简单的进度条
-------------------------------------------------------------------------------     
*/

/*
用途：显示进度条
*/
var i=0 , stepTime=0;	
	function showWaiting(time) {
		if(time==null) time=10000;
    stepTime=time/100;
    waiting.style.visibility="visible";
    cover.style.visibility="visible";
	progress.style.visibility="visible";
	word.style.visibility="visible";
     timedIterations(); 
	}

function timedIterations()
{
   if (i<=100)
   {
        progress.style.width=i*3.5;
      setTimeout("timedIterations();", stepTime);
      i++;   
   }
}

	function hideWaiting() {
		waiting.style.visibility="hidden";
		cover.style.visibility="hidden";
		progress.style.visibility="hidden";
		word.style.visibility="hidden";
	 }

	function initProgress() {
		document.write("<div id=\"progress\" class=\"PROGRESS\"></div>");
		document.write("<div id=\"waiting\" class=\"WAITING\">&nbsp;");
		document.write("</div><div id=word class=\"WORD\"><TABLE WIDTH=100% height=30 BORDER=0  CELLPADDING=0><TR><td  align=center >处理中，请稍候...</td></tr></TABLE></div>");
		document.write("<div id=\"cover\" class=\"COVER\">");
		document.write("<TABLE WIDTH=100% height=600 BORDER=0 CELLSPACING=0 CELLPADDING=0><TR><TD align=center><br></td></tr></table></div>");
	}

	initProgress();
