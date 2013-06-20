
// Defaults
var menuname= "TOP";
var menuclass= "";
var menuheight= 18;
var menuindent= 5;
var menutarget= "contents";
var menuplusimg= "fold.gif";
var menuminusimg= "open.gif";
var menublankimg= "fold.gif";
var menuexpand= "Expand";
var menucollapse= "Collapse";
var menu= [];


// Internal variables

// Current state of menu lines:
//  bit 1..0:
//    00 bottom level (img blank)
//    01 top level (no img)
//    10 collapsible collapsed (img plus)
//    11 collapsible expanded (img minus)
//  bit 2:
//     0 hidden
//     1 visible
var menustate= [];




// General utils

// Find object by name or id
function menuobj(id) {
  var i, x;
  x= document[id];
  if (!x && document.all) x= document.all[id];
  for (i=0; !x && i<document.forms.length; i++) x= document.forms[i][id];
  if (!x && document.getElementById) x= document.getElementById(id);
  return(x);
}


// Set cookie name to value
function menusetcookie(name, value){
  document.cookie=name+"="+escape(value);
}


// Get value of cookie name
function menugetcookie(name) {
  var s= document.cookie+";";
  b= s.indexOf(name+"=");
  if (b>=0) {
    b+= name.length+1;
    e= s.indexOf(";", b);
    return(unescape(s.substring(b, e)));
  }
  return("");
}




// Browser detection

// Global variables
var browserversion=0.0;
var browsertype=0; // 0: unknown; 1:MSIE; 2:NN

// Return true if MSIE or NN detected
function browserdetect() {
  var agt= navigator.userAgent.toLowerCase();
  var appVer= navigator.appVersion.toLowerCase();
  browserversion= parseFloat(appVer);
  var iePos= appVer.indexOf('msie');
  if (iePos!=-1) browserversion= parseFloat(appVer.substring(iePos+5, appVer.indexOf(';',iePos)));
  var nav6Pos = agt.indexOf('netscape6');
  if (nav6Pos!=-1) browserversion= parseFloat(agt.substring(nav6Pos+10))
  browsertype= (iePos!=-1) ? 1 : (agt.indexOf('mozilla')!=-1) ? 2 : 0;
}

browserdetect();




// Menu specific utilities

// Get menu level
function menulevel(m) {
  return(parseInt(menu[m].charAt(0)));
}


// Show menu line
function menushow(m) {
  var o= menuobj("menu"+m);
  if (o) {
    if (o.style) {
      o.style.visibility= "visible";
    } else {
      o.visibility= "show";
    }
  }
}


// Hide menu line
function menuhide(m) {
  var o= menuobj("menu"+m);
  if (o) {
    if (o.style) {
      o.style.visibility= "hidden";
    } else {
      o.visibility= "hide";
    }
  }
}


// Move menu line
function menumove(m, x, y) {
  var o= menuobj("menu"+m);
  if (o) {
    if (o.style) {
      o.style.left= x;
      o.style.top= y;
    } else {
      o.left= x;
      o.top= y;
    }
  }
}


// Get height of menu line
function menugetheight(m) {
  var o= menuobj("menu"+m);
  if (o && o.offsetHeight) return(o.offsetHeight);
  return(menuheight);
}


// Set menu state (menustate[m]=state; + reload image)
function menusetstate(m, state) {
  var pic= menuobj("menug"+m);
  if (menustate[m]!=state) {
    menustate[m]= state;
    if ((state&4)!=0) switch (state&3) {
      case 0: pic.src= menublankimg; break;
      case 1: break;
      case 2: pic.src= menuplusimg; break;
      case 3: pic.src= menuminusimg; break;
    }
  }
}


// Return expand/collapse info
function menustatus(m) {
  return((menustate[m]&1)!=0 ? menucollapse : menuexpand);
}


// Resize menu to reflect changes
function menuupdate() {
  var y= 0;
  for (i=0; i<menu.length; i++) {
    if ((menustate[i]&4)!=0) {
      menuhide(i);
      ilevel= menulevel(i);
      menumove(i, ilevel>1 ? (ilevel-1)*menuindent : 0, y);
      menushow(i);
      y+= menugetheight(i);
    } else {
      menuhide(i);
    }
  }
  menuobj("menulayer").style.height= y;
  menusavestate();
}


// Expand/collapse
function menuclick(m) {
  
  var i, level, ilevel;
  switch (menustate[m]) {
    case 7: // Collapse
      menusetstate(m, 6);
      level= menulevel(m);
      for (i=m+1; i<menu.length; i++) {
        ilevel= menulevel(i);
	if (ilevel>level) {
	  switch (menustate[i]&3) {
	    case 0:  menusetstate(i, 0); break;
	    case 1:  menusetstate(i, 1); break;
	    default: menusetstate(i, 2);
	  }
	} else break;
      }
      break;
    case 6: // Expand
      menusetstate(m, 7);
      level= menulevel(m);
      for (i=m+1; i<menu.length; i++) {
        ilevel= menulevel(i);
	if (ilevel>level) {
	  if (ilevel==level+1) switch (menustate[i]&3) {
	    case 0:  menusetstate(i, 4); break;
	    case 1:  menusetstate(i, 5); break;
	    default: menusetstate(i, 6);
	  }
	} else break;
      }
      break;
  }
  menuupdate();
}




// Load, save and init

// Save menustate array
function menusavestate() {
  var i, s;
  if (menuname.length>0) {
    s="";
    for (i=0; i<menu.length; i++) {
      s+= menustate[i];
    }
    menusetcookie(menuname, s);
  }
}


// Load menustate array
function menuloadstate() {
  var i, s;
  if (menuname.length>0) {
    s= menugetcookie(menuname);
    if (s.length==menu.length) {
      for (i=0; i<s.length; i++) {
	menustate[i]= parseInt(s.charAt(i));
      }
      return(true);
    }
  }
  return(false);
}


// Construct menustate array
function menuinitstate() {
  var i;

  for (i=0; i<menu.length; i++) {
    level= menulevel(i);
    switch(level) {
      case 0:  menustate[i]= 5; break;
      //case 1:  menustate[i]= 6; break;  // this  replace by   xieke 08.11.4
	  case 1:
	  	if ((i+1==menu.length) || (menulevel(i+1)<=level)) {
			//alert(i);
			//alert(level);
          menustate[i]= 4;
		} else {
			menustate[i]= 6;
		}
		break;
      default:
	if ((i+1==menu.length) || (menulevel(i+1)<=level)) {
          menustate[i]= 0;
	} else {
	  menustate[i]= 2;
	}
    }
  }
}




// Main

// Create menu
function menuwrite() {
  var i, j, k, y;
  var ilevel, istate;
  var target;
  var text, link, status;
  var wr;

  // Construct/load menustate array
  if (!menuloadstate()) menuinitstate();

  // Fallback
  if (browserversion<5.0) { menuoldwrite(); return }

  // Write all
  wr="<div id='menulayer' style='position:relative; top: 0; left: 0'>";
  document.write(wr);
  y= 0;
  for (i=0; i<menu.length; i++) {
    wr= "";

    // Split menu string into hasclass, text, link and status
    j=menu[i].indexOf('|',2);
    if (j>=0) {
      k=menu[i].indexOf('|',j+1);
      if (k>=0) {
	text=menu[i].substring(2,j);
	link=menu[i].substring(j+1,k);
	status=menu[i].substring(k+1);
      } else {
	text=menu[i].substring(2,j);
	link=menu[i].substring(j+1);
	status="";
      }
    } else {
      text=menu[i].substring(2);
      link="";
      status="";
    }
    switch (menu[i].charAt(1)) {
      case ' ': link='javascript:void(0)'; target='_self'; break;
      case 't': target='_top'; break;
      case 's': target='_self'; break;
      case 'p': target='_parent'; break;
      default: target=menutarget; break;
    }

    // One menu item
    ilevel=menulevel(i);
    istate=menustate[i];
    wr+="<div id='menu"+i+"' "+
          "class='"+menuclass+"' "+
          "style='position: absolute; "+
	    "top: "+y+"; "+
	    "left: "+(ilevel>1 ? (ilevel-1)*menuindent : 0)+"; "+
	    "visibility: "+((istate&4)!=0 ? "visible" : "hidden")+
	    " '"+
	">";
    if ((istate&2)!=0) {
      wr+="<a href=\"javascript: void(0)\">"+
         "<img name='menug"+i+"' border=0 style='cursor:hand' "+
            "src='"+((istate&1)!=0 ? menuminusimg : menuplusimg)+"' "+
	    "onMouseOver=\"window.status=menustatus("+i+"); return(true)\" "+
	    "onMouseDown=\"menuclick("+i+"); return(true)\" "+
	    "onMouseUp=\"window.status=menustatus("+i+"); return(true)\" "+
	    "onMouseOut=\"window.status=''; return(true)\""+
	 "></a>";
    } else if ((istate&3)==0) {
      wr+="<img name='menug"+i+"' src='"+menublankimg+"' border=0>";
    }
	//谢珂修改
	//如果链接是一个javascript,则不需要 target	
	//********************
	var targetStr=" target='"+target+"' ";	
	var index = link.indexOf("javascript");
	//alert(link);
	//alert(index);
	if (index != -1) {
		targetStr=" ";	}
	//********************	
    
    //alert(link);
    wr+=link!="" ?
      //"<a href='"+link+"' target='"+target+"' "+
      //谢珂修改      
      "<a href="+link+targetStr+
        "onMouseDown=\"window.status=''; return(true)\" "+
        "onMouseOver=\"window.status='"+status+"'; return(true)\" "+
	"onMouseOut=\"window.status=''; return(true)\">"+text+"</a>" :
      text;
    wr+="</div>\n";
    //alert(wr);
    document.write(wr);
    if ((istate&4)!=0) {
      y+=menugetheight(i);
    }
  }
  wr="</div>";
  document.write(wr);
  menuobj("menulayer").style.height= y;
}




// Fallback (no layer, no style)


// Expand/collapse
function menuoldclick(m) {
  var i, level, ilevel;
  switch (menustate[m]) {
    case 7: // Collapse
      dw("collapse");
      menustate[m]= 6;
      level= menulevel(m);
      for (i=m+1; i<menu.length; i++) {
        ilevel= menulevel(i);
	if (ilevel>level) {
	  switch (menustate[i]&3) {
	    case 0:  menustate[i]= 0; break;
	    case 1:  menustate[i]= 1; break;
	    default: menustate[i]= 2;
	  }
	} else break;
      }
      break;
    case 6: // Expand
      menustate[m]= 7;
      level= menulevel(m);
      for (i=m+1; i<menu.length; i++) {
        ilevel= menulevel(i);
	if (ilevel>level) {
	  if (ilevel==level+1) switch (menustate[i]&3) {
	    case 0:  menustate[i]= 4; break;
	    case 1:  menustate[i]= 5; break;
	    default: menustate[i]= 6;
	  }
	} else break;
      }
      break;
  }
  menusavestate();
  location.reload();
}


// Create menu
function menuoldwrite() {
  var i, j, k, y;
  var ilevel, istate;
  var target;
  var text, link, status;
  var wr;

  // Construct/load menustate array
  if (!menuloadstate()) menuinitstate();

  // Write all
  for (i=0; i<menu.length; i++) {
    wr= "";

    // Split menu string into hasclass, text, link and status
    j=menu[i].indexOf('|',2);
    if (j>=0) {
      k=menu[i].indexOf('|',j+1);
      if (k>=0) {
	text=menu[i].substring(2,j);
	link=menu[i].substring(j+1,k);
	status=menu[i].substring(k+1);
      } else {
	text=menu[i].substring(2,j);
	link=menu[i].substring(j+1);
	status="";
      }
    } else {
      text=menu[i].substring(2);
      link="";
      status="";
    }
    switch (menu[i].charAt(1)) {
      case ' ': link='javascript:void(0)'; target='_self'; break;
      case 't': target='_top'; break;
      case 's': target='_self'; break;
      case 'p': target='_parent'; break;
      default: target=menutarget; break;
    }

    // One menu item
    ilevel=menulevel(i);
    istate=menustate[i];
    if ((istate&4)!=0) {
      wr+="<div class='"+menuclass+"'>";
      for (j=1; j<ilevel; j++) wr+="&nbsp;";
      if ((istate&2)!=0) {
	wr+="<a href=\"javascript:menuoldclick("+i+")\" "+
	      "onMouseOver=\"window.status=menustatus("+i+"); return(true)\" "+
	      "onMouseOut=\"window.status=''; return(true)\""+
	      "onMouseUp=\"window.status=menustatus("+i+"); return(true)\" "+
	    ">"+
	   "<img name='menug"+i+"' border=0 "+
	      "src='"+((istate&1)!=0 ? menuminusimg : menuplusimg)+"' "+
	   "></a>";
      } else if ((istate&3)==0) {
	wr+="<img name='menug"+i+"' src='"+menublankimg+"' border=0>";
      }
	
	//谢珂修改
	//如果链接是一个javascript,则不需要 target	
	//********************
	var targetStr=" target='"+target+"' ";	
	var index = link.indexOf("javascript");
	alert(link);
	alert(index);
	while (index != -1) {
		targetStr=" ";	}
	//********************	
		      
      wr+=link!="" ?
	//"<a href='"+link+"' target='"+target+"' "+
	//谢珂修改		
	"<a href="+link+targetStr+
	  "onMouseDown=\"window.status=''; return(true)\" "+
	  "onMouseOver=\"window.status='"+status+"'; return(true)\" "+
	  "onMouseOut=\"window.status=''; return(true)\">"+text+"</a>" :
	text;
      wr+="</div>\n";
      document.write(wr);
    }
  }
}