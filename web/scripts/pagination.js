/**
 * 分页javascript控件，使用类方式建立分页程序
 * 在分页的页面中需要一个分页的FORM，在定义FORM后面增加下面HTML代码：

<form name="testPage" method="POST" action="BNLOG.BNLOG_testPage.do" onsubmit="return false;">
<html:hidden property="PageCond/begin" />
<html:hidden property="PageCond/length" />
<html:hidden property="PageCond/count" />
<script>
var myPage = new page();
myPage.init("testPage", "BNLOG.BNLOG_testPage.do","PageCond/begin", "PageCond/length", "PageCond/count");
</script>

其中：
    PageCond/begin 是开始记录，如果送到分页的BL方法中 begin 不存在默认值为 0
    PageCond/length 是每页记录数，如果送到分页的BL方法中 length 不存在默认值为 10，
                    也可以用<html:input property="PageCond/length" />作为输入栏输入每页的记录数
    PageCond/count 是查询结果的总记录数，如果总记录数不存在或者小于1，分页的BL方法会自动计算查询结果
                   的总记录数，如果 count 是 'noCount' 或者大于 1，分页的BL方法不会计算结果总记录数
                   对于复杂查询，并且结果数非常大的时候，建议采用'noCount'，不计算总记录数来提高速度
对上述三个变量初始化后，就可以创建分页的对象了，
分页对象创建后需要调用 init 函数进行初始化，
初始化参数就是分页的FORM对象的名称和begin,length,count三个TEXT对象的名称
注意：初始化分页对象需要在三个变量(begin,length,count)初始化后

初始化成功后就可以使用分页对象了

<a href="javascript:myPage.firstPage();" >首页</a>&nbsp;
－－ myPage.firstPage() 跳转到第一页

<a href="javascript:myPage.previousPage();" >上页</a>&nbsp;
－－ myPage.firstPage() 跳转到上一页

<a href="javascript:myPage.nextPage();" >下页</a>&nbsp;
－－ myPage.firstPage() 跳转到下一页

<a href="javascript:myPage.lastPage();" >尾页</a>
－－ myPage.firstPage() 跳转到最后一页

<script>if (!myPage.noCount) document.write('总共' + myPage.count + '条');</script>
－－使用script语句显示查询结果的总条数

第<input type="text" name="pageno" size=2 value=<script>document.write(myPage.current)</script> >页 if (!myPage.noCount)<script>document.write('/共'+myPage.total+'页')</script>
－－ myPage.current 当前页号
－－ myPage.total   总页数 （如果没有计算出总记录数，就没有总页数）


<input type="text" name="pageno" size=2 > <input type="button" onclick="myPage.goPage('pageno');" value="go!" name="gopage">
－－ myPage.goPage('pageno') 跳转到指定的页号，pageno是输入页号的TEXT对象名称
---------------------------------------------------------------------------
时间		    修改人		说明
2005-4-14   陈春         增加中文字符串按拼音或者笔画排序
--------------------------------------------------------------------------- 

 *
 */
function page() 
{
    this.frm;           //分页查询FORM的对象
    this.beginTxt;      //分页查询FORM中的begin TEXT对象（可以使隐藏的对象）
    this.lengthTxt;     //分页查询FORM中的length TEXT对象（可以使隐藏的对象）
    this.countTxt;      //分页查询FORM中的count TEXT对象（可以使隐藏的对象）

    this.begin;         //分页查询开始记录位置
    this.length;        //每页显示记录数
    this.count;         //查询结果总记录数
    this.current;   	//当前页码
    this.total;     	//总共页数
    this.noCount;       //分页程序没有总记录数
	this.action;
    
    this.init = initPage;
    this.nextPage = nextPage;
    this.previousPage = previousPage;
    this.firstPage = firstPage;
    this.lastPage = lastPage;
    this.goPage = goPage;
    this.go = go;
	this.printPageCode = printPageCode;		//3.3翻页风格
	this.printPageCode51 = printPageCode51;		//5.1翻页风格
    this.printPageCode1 = printPageCode1;	//5.1翻页风格一
    this.printPageCode2 = printPageCode2;	//5.1翻页风格二
	this.printPageCode3 = printPageCode3;	//5.1翻页风格三

    /*
     * 分页查询的构造函数
     * @param frmName 分页查询的FORM的名称
     * @param beginText FORM中begin TEXT对象名称
     * @param lengthText FORM中length TEXT对象名称
     * @param countText FORM中count TEXT对象名称
     */
    function initPage(frmName, frmAction, beginText, lengthText, countText)
    {
        this.noCount = false;
        this.frm = document.forms[frmName];
        this.frm.action = frmAction;		
		this.action = frmAction;
		this.beginTxt = this.frm.elements[beginText];
        this.lengthTxt = this.frm.elements[lengthText];
        this.countTxt = this.frm.elements[countText];
        
        if(this.beginTxt.value==null ||this.beginTxt.value==""){
        	this.begin = 0;
        }else{
        	this.begin = parseInt(this.beginTxt.value);
    	}
        if(this.lengthTxt.value==null ||this.lengthTxt.value==""){
        	this.length=10;
        }else{
        	this.length = parseInt(this.lengthTxt.value);
    	}
        	
        if(this.countTxt.value==null ||this.countTxt.value==""){
        	this.count = 0;
        }else{
        	this.count = parseInt(this.countTxt.value);
    	}

        if (this.countTxt.value == "noCount") {
            this.noCount = true;
            this.total = "";
            this.count = -2;
        }
	
        if (this.count < 1) {
            this.noCount = true;
            this.total = 1;
            this.count = -2;
        }
	
        if (!this.noCount) {
            this.total = Math.floor(this.count/this.length);
            if (this.count%this.length != 0) {
                this.total++;
            }
        }
		
        this.current = Math.floor(this.begin/this.length) + 1;
    }
	
	//转到下一页
    function nextPage()
    {
        this.go(this.current + 1);
    }
	//转到上一页    
    function previousPage()
    {
        this.go(this.current - 1);
    }
	//转到第一页
    function firstPage()
    {
        this.go(1);
    }
	//转到最后一页
    function lastPage()
    {
        this.go(this.total);
    }	
	// 跳到输入的页号，pageNo是输入页码的输入框的名称 
    function goPage(pageNo) 
    {
        if (!isNumber(this.frm.elements[pageNo].value)){
        	alert("请正确输入跳转的页码！")
        	this.frm.elements[pageNo].select();
        	return;
        }
        var page = parseInt(this.frm.elements[pageNo].value);
        if(page==NaN || page==undefined)
        	this.count=0;
        this.go(page);
    }
	//转到指定页
    function go(page)
    {
    	if (!isNumber(this.lengthTxt.value)) {
    		alert("每页条数错误！请重新输入大于0的数字")
        	this.lengthTxt.select();
    		return;
    	}
        var inputLen = parseInt(this.lengthTxt.value);
        
        if (inputLen < 1) {
            alert("每页记录数错误！请重新输入大于0的数字")
            return;
        }
    
        if (inputLen != this.length) {
            this.length = inputLen;
            if (this.noCount) { //如果改变了每页显示记录数，且没有统计出总记录数，记录从0开始查询
                this.beginTxt.value = 0;
                this.frm.submit();
                return;
            }
            this.total = Math.floor(this.count/this.length);
            if (this.count%this.length != 0) {
                this.total++;
            }
        }
    
        var gono = page;
        if (gono<1)
            gono=1;
        if (!this.noCount) {
            if (gono>this.total)
                gono=this.total;
        }
            
        this.beginTxt.value = (gono - 1) * this.length;		
		this.frm.action = this.action;
		this.frm.target = "_self";		
        this.frm.submit();
    }
	//3.3翻页风格
	function printPageCode(currRowCount)
    {
        var htmltxt="";
        var currCount=currRowCount;
        if(currCount==NaN || currCount==undefined || currCount == null){
        	return;	
        }

        if(this.current>1){
        	htmltxt += "【<a href='javascript:myPage.firstPage();' >首页</a>】";
        	htmltxt += "【<a href='javascript:myPage.previousPage();' >上页</a>】";
        }
		else	
		{
        	htmltxt += "【首页】";
        	htmltxt += "【上页】";
		}
        
        if (!this.noCount){
	        if (this.current<this.total ) {
	            htmltxt += "【<a href='javascript:myPage.nextPage();' >下页</a>】";
        		htmltxt += "【<a href='javascript:myPage.lastPage();' >尾页</a>】";
	        }
			else	
			{
				htmltxt += "【下页】";
				htmltxt += "【尾页】";
			}	
    	}else{
    		if (this.length<=currCount){
    			htmltxt += "【<a href='javascript:myPage.nextPage();' >下页</a>】";
    		}
			else	
			{
				htmltxt += "【下页】";
			}
    	}		
        
        htmltxt += "第<input type='text' name='pageno' size=2 value='"+this.current+"'>页";
		htmltxt += "<input type='button' onclick=myPage.goPage('pageno') value='go' name='gopage'>";
        
        if (!this.noCount){
        	htmltxt += "/共"+this.total+"页";
        	htmltxt += "共"+myPage.count + "条";
        }
        	
        document.write( htmltxt);
    }
	
	//5.1翻页风格
	function printPageCode51(pageControlStyle,currRowCount,groupNum,txClass,btClass)
    {
		switch(pageControlStyle)
		{					
			case "link": this.printPageCode1(currRowCount,txClass);	break;
			case "button": this.printPageCode2(currRowCount,txClass,btClass); break;
			case "group": this.printPageCode3(currRowCount,groupNum,txClass); break;
			default	: break;
		}
	}

	/*
	* 链接式翻页
	*/
	function printPageCode1(currRowCount,txClass)
    {
        var htmltxt="";
        var currCount=currRowCount;
        if(currCount==NaN || currCount==undefined || currCount == null){
        	return;	
        }
		
		if (!this.noCount){
        	htmltxt = htmltxt+this.current+"/"+this.total;
        }

		// 如果当前页数大于1，则显示"首页"、"上页"的链接；否则显示普通文字提示		
        if(this.current>1){
        	htmltxt += "<a href='javascript:myPage.firstPage();' > 首页</a>";
        	htmltxt += "<a href='javascript:myPage.previousPage();' > 上页</a>";
        }
		else
		{
			htmltxt += " 首页";
        	htmltxt += " 上页";
		}
        
		//如果PageCond/count的值不为"noCount"，则要统计总记录条数，可显示“尾页”；否则没有“尾页”选项
        if (!this.noCount){
			//如果当前页小于总页数，则显示“下页”，“尾页”链接，否则显示普通文字提示
	        if (this.current<this.total ) {
	            htmltxt += "<a href='javascript:myPage.nextPage();' > 下页</a>";
        		htmltxt += "<a href='javascript:myPage.lastPage();' > 尾页</a>";
	        }
			else
			{
				htmltxt += " 下页";
        		htmltxt += " 尾页";
			}
    	}else{
			//如果当前页小于总页数，则显示“下页”链接，否则显示普通文字提示
    		if (this.length<=currCount){
    			htmltxt += "<a href='javascript:myPage.nextPage();' > 下页</a>";
    		}
			else
			{
				htmltxt += " 下页";
			}
    	}		
        
        htmltxt += " 转到第<input type='text' name='pageno' size=2 value='"+ this.current +"' class='"+ txClass +"'>页 ";
                      	
        document.write( htmltxt);
    }

	function printPageCode2(currRowCount,txClass,btClass)
    {
        var htmltxt="";
        var currCount=currRowCount;
        if(currCount==NaN || currCount==undefined || currCount == null){
        	return;	
        }
		
		if (!this.noCount){
        	htmltxt += "第"+this.current+"页,共"+this.total+"页";
        }

		// 如果当前页数大于1，则显示"首页"、"上页"的按钮；否则显示不可用按钮
        if(this.current>1){
        	htmltxt += " <input type='button' onclick='javascript:myPage.firstPage();' value='首页' class='"+ btClass +"'/>";
        	htmltxt += " <input type='button' onclick='javascript:myPage.previousPage();' value='上页' class='"+ btClass +"'/>";
        }
		else
		{
			htmltxt += " <input type='button' value='首页' disabled class='"+ btClass +"'/>";
        	htmltxt += " <input type='button' value='上页' disabled class='"+ btClass +"'/>";
		}
        
		//如果PageCond/count的值不为"noCount"，则要统计总记录条数，可显示“尾页”；否则没有“尾页”选项
        if (!this.noCount){
			//如果当前页小于总页数，则显示“下页”，“尾页”的按钮；否则显示不可用按钮
	        if (this.current<this.total ) {
	            htmltxt += " <input type='button' value='下页' onclick='javascript:myPage.nextPage();'  class='"+ btClass +"'/>";
        		htmltxt += " <input type='button' value='尾页' onclick='javascript:myPage.lastPage();'  class='"+ btClass +"'/>";
	        }
			else
			{
				htmltxt += " <input type='button' value='下页' disabled class='"+ btClass +"'/>";
        		htmltxt += " <input type='button' value='尾页' disabled class='"+ btClass +"'/>";
			}
    	}else{
			//如果当前页小于总页数，则显示“下页”的按钮；否则显示不可用按钮
    		if (this.length<=currCount){
    			htmltxt += " <input type='button' value='下页' onclick='javascript:myPage.nextPage();'  class='"+ btClass +"'/>";
    		}
			else
			{
				htmltxt += " <input type='button' value='下页' disabled class='"+ btClass +"'/>";
			}
    	}		
        
        htmltxt += " 转到第<input type='text' name='pageno' size=2 value='"+ this.current +"' class='"+ txClass +"'>页";
                      	
        document.write( htmltxt);
    }
	//使用此风格时，PageCond/count的值不能为"noCount"
	function printPageCode3(currRowCount,showPageCount,txClass)
    {
        var htmltxt="";
        var currCount=currRowCount;
		var i=0;			//起始页号
		var totalGroup=0;	//分组总数
		var currGroup=0;	//当前组号
		if (!this.noCount){
			if(this.total%showPageCount==0)
			{
				totalGroup=Math.floor(this.total/showPageCount);
			}
			else
			{
				totalGroup=Math.floor(this.total/showPageCount)+1;
			}
		}

		if(this.current%showPageCount==0)
		{
			currGroup=Math.floor(this.current/showPageCount);
		}
		else
		{
			currGroup=Math.floor(this.current/showPageCount)+1;
		}

		i=showPageCount*(currGroup-1)+1;

		if(currCount==NaN || currCount==undefined || currCount == null){
        	return;	
        }
		
		if (!this.noCount){
        	htmltxt += "共"+this.total+"页";	 //"第"+this.current+"页"
        }

		//判断"<<"
		if(currGroup==1)
		{
			htmltxt += " <<";
		}
		else
		{
			htmltxt += " <a href='javascript:myPage.go("+ (1 + showPageCount*(currGroup-2)) +");'> <<</a>";
		}
		//alert(this.total);
		//列出选择页号
		while(i<=showPageCount*currGroup)
		{
			if (!this.noCount){
				if(i>this.total)  break;
			}
			if(i!=this.current)
			{
				htmltxt += " <a href='javascript:myPage.go("+i+");'>"+i+"</a>";
			}
			else
			{
				htmltxt += " <font color='red'>"+i+"</font>";
			}
			i++;
		}
		//判断">>"
		if (!this.noCount){
			if(currGroup==totalGroup)
			{
				htmltxt += " >>";
			}
			else
			{
				htmltxt += " <a href='javascript:myPage.go("+(showPageCount*currGroup+1)+");'> >></a>";
			}
		}
		else
		{
			if (this.length<=currCount){
    			htmltxt += " <a href='javascript:myPage.go("+(showPageCount*currGroup+1)+");'> >></a>";
    		}
			else
			{
				htmltxt += " >>";
			}
		}

		htmltxt += " 转到第<input type='text' name='pageno' size=2 value='"+ this.current +"' class='"+ txClass +"'>页";

        document.write( htmltxt);
    }
	
}
//判断是否为数字，是则返回true,否则返回false
function isNumber( s ){   
	var regu = "^[0-9]+$";
	var re = new RegExp(regu);
	if (s.search(re) != -1) {
	   return true;
	} else {
	   return false;
	}
}
//调式时用来输出结果
function print(s)
{
	document.write(s);
}