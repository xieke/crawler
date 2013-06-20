var menuObject="";
var mainOverBgColor ="#FF9966";
var mainOutBgColor ="#004d81";
var mainOverColor ="#000000";
var mainOutColor ="#F7F6F6";
var subOverColor ="#000000";
var subOutColor ="#F7F6F6";
var subOverBgColor ="#FF9966";
var subOutBgColor ="#004d81";
var overColor ="#000000";
var outColor ="#F7F6F6";
var overBgColor ="#FF9966";
var outBgColor ="#85B5E4";

function writePreciveprincipalMenu(){
    var menuString="<table><tr><td><DIV style=\"PADDING-LEFT: 10px;FONT-WEIGHT: normal; FONT-SIZE: 9pt; Z-INDEX: 2; LEFT: "+StartLeft+"px; VISIBILITY: visible; WIDTH: 992px; FONT-STYLE: normal; FONT-FAMILY: arial,comic sans ms,technical; POSITION: relative; TOP: "+StartLeft+"px; HEIGHT: 21px; BACKGROUND-COLOR: #004d81\" OrgTop=\"0\" OrgLeft=\"0\" bgColor=\"#004D81\" >\r\n";
	for (var i=1;i<=MenuLineCount;i++){
		menuObject = eval("Menu"+i) ;
		if(menuObject[0]==""){
            menuString+="<DIV style=\" LEFT: "+menuObject[5]*(i-1)+"px; WIDTH: "+menuObject[5]+"px; \" class=\"MAIN_DIV_MENU\"></DIV>\r\n";
		}
		else{
			 menuString+="<DIV style=\" LEFT: "+menuObject[5]*(i-1)+"px;  OVERFLOW: hidden; WIDTH: "+menuObject[5]+"px  \" class=\"MAIN_DIV_MENU\"  value="+menuObject[0]+"\"   onmouseover =mainDivMouseOver(this) onmouseout =mainDivMouseOut(this) onclick=changeSubMenu(\"Menu"+i+"\")>"+menuObject[0]+"</DIV>\r\n";
		}
	}
	menuString+="</DIV></td></tr></table>";
	return menuString;
}


function writeSubordinateMenu(){
var subMenuString="";
for (var i=1;i<=subMenuCount ;i++ ){
    var mainMenuName = "SubMenu"+i;
    subMenuString+="<div id=\"DivSubMenu"+i+"\" style=\"width:"+subMenuWidth+"px;background-color:#FFFFCC;height:500px;display:"+"none"+"\" >";
    var mainMenu =eval(mainMenuName);
	for (var j= 1;j<=mainMenu[3];j++) {
        menuObject=eval(mainMenuName+"_"+j);
		if (menuObject[3]!=0){
			subMenuString +=  "<div id=\"sub_menu"+i+"_"+"_"+j+"\" class=\"SUB_DIV_MENU\" onmouseover =subMouseOver(this) onmouseout =subMouseOut(this) onclick=javascript:displayDiv(\"menuChild_"+i+"_"+j+"\") >"+menuObject[0]+"</div>";
		  subMenuString +="<div id=\"menuChild_"+i+"_"+j+"\" style=\"display:"+display+"\">";
			for (var k=1;k<=menuObject[3];k++) {
				var subMenuObject=eval(mainMenuName+"_"+j+"_"+k);
				subMenuString +="<a href=\""+subMenuObject[1]+"\"class=\"A_SUBMENU\" style=\"\" onmouseover =mouseOver(this) onmouseout =mouseOut(this) target=\""+menuObject[2]+"\">"+subMenuObject[0]+"</a>";
			}
           subMenuString +="</div>";
		}
		else {
        subMenuString += "<div id=\"sub_menu"+"_"+i+"_"+j+"\" ><a href=\""+menuObject[1]+"\" class=\"SUB_DIV_MENU\" onmouseover =subMouseOver(this) onmouseout =subMouseOut(this) target=\""+menuObject[2] +"\"> "+menuObject[0]+"</a></div>";
		}
	}
	subMenuString  +="</div>";
   }
     return subMenuString;
}

function displayDiv(menuName){
	var showDiv = document.getElementsByName(menuName);
    showDiv=eval(menuName);
	if  (showDiv.style.display =='none') {
		showDiv.style.display ='';
	}
	else {showDiv.style.display ='none';}
}

function setSubDivToTop(){
	for (var i=1;i<=subMenuCount ;i++ ){
		var subDiv =eval("DivSubMenu"+i);
	 window.top.document.body.setAttribute("DivSubMenu"+i,subDiv);
	}
}

function changeSubMenu(menuName){
for (var i=1;i<=subMenuCount ;i++ ){
	var subDiv =window.top.document.body.getAttribute("DivSubMenu"+i);
    subDiv.style.display ="none";
}
	window.top.document.body.getAttribute("DivSub"+menuName).style.display="block";
}

function subMouseOver(menu){
  menu.style.cursor="hand";
  menu.style.backgroundColor = subOverBgColor;
  menu.style.color=subOverColor;
}

function subMouseOut(menu){
   menu.style.color=subOutColor;
  menu.style.backgroundColor = subOutBgColor;
}

function mouseOver(menu){
  menu.style.cursor="hand";
  menu.style.backgroundColor = overBgColor;
  menu.style.color=overColor;
}

function mouseOut(menu){
   menu.style.color=outColor;
  menu.style.backgroundColor = outBgColor;
}

function mainDivMouseOut(menu){
  menu.style.color=mainOutColor;
  menu.style.backgroundColor =mainOutBgColor;

}

function mainDivMouseOver(menu){
  menu.style.color=mainOverColor;
  menu.style.cursor="hand";
  menu.style.backgroundColor=mainOverBgColor;
}

function writeMenu(flag){
	if (flag=="main"){
        document.write(writePreciveprincipalMenu());
	}
	else if (flag=="sub"){
       document.write(writeSubordinateMenu());
	}
}

function initSubMenu(){
	setSubDivToTop();
changeSubMenu("Menu1");
}

