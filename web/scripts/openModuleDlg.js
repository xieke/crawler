//######################################################################
// 文件名：openModuleDlg.js
// Copyright (c) 2001-2002 上海杉德金卡系统发展有限公司 多媒体和终端事业部
// 作者：Rage Luo
// 创建日期：2002-3-14
// 修改日期：
// 描述：创建无边框对话框
// 版本：1.0
// 改进：
//######################################################################
/*
  函数原形：openModuleDlg(theURL,wname,posx,posy,bCenter,w,h,windowTIT)
  参数：    theURL       窗口内显示的网页地址
            wname       窗口名字，可以随便起一个，但不能为空
            posx        窗口左上角x轴坐标（bCenter为false时有效）
            posy        窗口左上角y轴坐标（bCenter为false时有效）
            bCenter     是否居中显示
            w           窗口的高（如果设为0则表示用默认宽度）
            h           窗口的高（如果设为0则表示用默认高度）
            windowTIT   标题栏图片文件名（如果设为空则表示用默认图片）
  返回值：  无反回值
  
  注意：    1. theURL中如果有多个参数必须用 ~ 来替代 &
               比如 1.jsp?a=1&b=2 必须写成 1.jsp?a=1~b=2
            
            2. theURL必须包含模块所在的目录
               比如 1.jsp 在 Datum 目录中，那么应该写成 ../Datum/1.jsp
            
  例子：
        onclick="openModuleDlg('myModule.jsp','myModule',20,20,false,0,0,'')";
*/




function openModuleWindow(theURL, wname, W, H, posx, posy, windowTIT, windowREALtit ,
										windowBORDERCOLOR, windowBORDERCOLORsel, windowTITBGCOLOR, windowTITBGCOLORsel,
											bCenter, sFontFamily, sFontSize, sFontColor) {
	
		var windowX = 0,windowY = 0;
		if(bCenter){	
		    windowX = Math.ceil( (window.screen.width  - W) / 2 );
		    windowY = Math.ceil( (window.screen.height - H) / 2 );
	    } else {
		    windowX = posx;
		    windowY = posy;
	    }
		H=H+20; W=W+2;
		
		splashWin1 = window.open( "../Public/ModuleFrame.jsp?u="+theURL+"&t="+windowTIT , wname, "fullscreen=1,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0");
		//splashWin1 = window.open( "../Public/ModuleFrame.jsp?u="+theURL+"&t="+windowTIT , wname, "fullscreen=1,toolbar=1,location=1,directories=1,status=1,menubar=1,scrollbars=1,resizable=1");

		splashWin1.resizeTo( Math.ceil( W )       , Math.ceil( H ) );
		splashWin1.moveTo  ( Math.ceil( windowX ) , Math.ceil( windowY ) );
	splashWin1.focus();
}                                                                               


function openModuleDlg(theURL,wname,posx,posy,bCenter,w,h,windowTIT)
{
  var height,width;
  if (h == 0)
    height = 381;
  else
    height = h;
  
  if (w == 0)
    width = 600;
  else
    width = w;
    
  openModuleWindow(theURL,wname,width,height,posx,posx,windowTIT, '#000000', '#000000', '#999988', '#808040' ,'',bCenter,'Arial, Helvetica, sans-serif', '1','#000000');
}