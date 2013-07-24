/*
-------------------------------------------------------------------------------
文件名称：print.js
说    明：JavaScript脚本，处理打印事务
版    本：1.0
修改纪录:
---------------------------------------------------------------------------
时间        修改人      说明
2005-4-25   zhouming        创建
-------------------------------------------------------------------------------     
*/

/*
用途：对显示的表格进行打印，去除一定的格式
输入：tableId：打印的表格ID；title：打印主题
返回：
*/
var htmltext;
function printTable( tableId,title) { 
    //检验浏览器版本
    var recPerPage = 15;
    var printWindow;
	var winWidth=750;
	var winHeight=530;

   printCheck();
    openPrintWindow(winWidth,winHeight);
    var printTable = window.document.getElementById(tableId);
	if (printTable==null) alert("表格不存在");

    var titleRow = title;
    if( titleRow==null) titleRow= "";


    var headRow = printTable.rows(0).innerHTML;
    headRow = headRow.replace(/选择/,"序号");

     htmltext = "<table  width=98% border=0 style='border-collapse:collapse' cellpadding=2 >";
    htmltext += "<tr><td align='center'><b>";
    htmltext += titleRow;
    htmltext += "</b></td/></tr></table>";
    htmltext += "<table  width=98% border=1 style='border-collapse:collapse' cellpadding=2 >";
    htmltext += "<tr>"+headRow.toString()+"</tr>";
    var tmpText = "";

    for(var i=1;i<printTable.rows.length;i++)
    {
        if (i%recPerPage==0){
            htmltext += "</table><table  width=98% border=1 style='border-collapse:collapse;page-break-before:always' cellpadding=2 >";
            htmltext += "<tr>"+headRow.toString()+"</tr>";
        }
        
        htmltext += "<tr>";
        tmpText = printTable.rows(i).innerHTML;
        //tmpText = tmpText.replace(/(< *INPUT TYPE=CHECK [^>]*>)/gi,i);
//        tmpText = tmpText.replace(/(< *INPUT [^>]*>)/,i);
        htmltext += tmpText;
        htmltext += "</tr>";        
                
    }
    htmltext += "</table>";
    
    re=/(< *a [^>]*>)|(< *\/ *a *>)/gi;   //去除文字上面的链接
    re1 = /<SPAN class=arrow>5<\/span>/gi; //去除页面排序时产生的箭头
    re2 = /<SPAN class=arrow>6<\/span>/gi; //去除页面排序时产生的箭头
    re3=/onClick=sort\(\)/gi;
    re4=/title=点击排序/gi;
    re5=/class=reshead/gi;
    re6=/(< *IMG [^>]*>)/gi
    htmltext=htmltext.replace(re,"");
    htmltext=htmltext.replace(re1,"");
    htmltext=htmltext.replace(re2,"");
    htmltext=htmltext.replace(re3,"");
    htmltext=htmltext.replace(re4,"");
    htmltext=htmltext.replace(re5,"");
    htmltext=htmltext.replace(re6,"");
    printReport();
} 

    /*
    用途：取得当前浏览器的版本，只取第一个数字
    输入：空
    返回：版本号的第一个数字
    */
function GetIEversion() {
        var version = navigator.appVersion;
        var pos = version.indexOf("MSIE");
        var intVersion = version.substr(pos+5,1);
        return intVersion;
    }



    /*
    用途：检验当前浏览器版本是否是IE6
    输入：空
    返回：空
    */
    function printCheck() {
        if(GetIEversion()<6)
        {
            alert("您使用的浏览器版本过低，不能支持分页打印功能，请升级浏览器版本至6.0！");
            window.close();
        }
    }

    //显示打印表格
    function printReport(){
        var mm=printWindow.document.getElementById("ss");
        mm.innerHTML=htmltext;  
    }

//打开新窗口
function openPrintWindow(winWidth,winHeight) {
    printWindow = window.open("", "printWin", "width="+ winWidth + ","  + "height="+winHeight + ","+ "status=no,menubar=yes,scrollbars=yes");
    printWindow.document.write("<html>");
    printWindow.document.write("<title>报表打印</title>");
    printWindow.document.write("<body bgcolor='#FFFFFF' text='#000000'>");
    printWindow.document.write("<div id='ss'>");
    printWindow.document.write("</div>");
    printWindow.document.write("</body>");
    printWindow.document.write("</html>");
}