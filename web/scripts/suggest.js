

/***********************************************
 * Global variables
 ***********************************************/
var maxResults = 10;	// max # of results to display控制最大显示数
var ignoreKeys = "";

/***********************************************
 * Prototype for populating data
 ***********************************************/
function get_data() {}

/***********************************************
 * Find search keys in data set
 ***********************************************/
function suggest(keywords,key)
{
	var results = document.getElementById("results");
	
	if(keywords != "")
	{
		var terms = get_data(); // sort? -- data should be alphabetical for best results

		var ul = document.createElement("ul");
		var li;
		var a;
		
		if ((key.keyCode == '40' || key.keyCode == '38' || key.keyCode == '13'))
		{
			navigate(key.keyCode);
		}
		else
		{
			var kIndex = -1;
		
			for(var i = 0; i < terms.length; i++)
			{	
				kIndex = terms[i].activity.toLowerCase().indexOf(keywords.toLowerCase());
				
				if(kIndex >= 0) 
				{
					li = document.createElement("li");
					
					// setup the link to populate the search box
					a = document.createElement("a");
					a.href = "javascript://"; 
					
					a.setAttribute("rel",terms[i].val);
					a.setAttribute("rev", getRank(terms[i].activity.toLowerCase(), keywords.toLowerCase()));
					
					if(!document.all) a.setAttribute("onclick","populate(this);");
					else a.onclick = function() { populate(this); }
					
					a.appendChild(document.createTextNode(""));
					
					if(keywords.length == 1) 
					{
						var kws = terms[i].activity.toLowerCase().split(" ");
						var firstWord = 0;
						
						for(var j = 0; j < kws.length; j++)
						{
						 //	if(kws[j].toLowerCase().charAt(0) == keywords.toLowerCase()) {
								
								ul.appendChild(li);
								
								if(j != 0) {
									kIndex = terms[i].activity.toLowerCase().indexOf(" " + keywords.toLowerCase());
									kIndex++;
								}
								
								break;
						//	}
						}
					}
					else if(keywords.length > 1) {
						ul.appendChild(li);
					}
					else continue;

					
					var before = terms[i].activity.substring(0,kIndex);
					var after = terms[i].activity.substring(keywords.length + kIndex, terms[i].activity.length);
	
					a.innerHTML = before + "<strong>" + keywords.toLowerCase() + "</strong>" + after;
	
					li.appendChild(a);

				}
			}		
			
			if(results.hasChildNodes()) results.removeChild(results.firstChild);
			
			// position the list of suggestions
			var s = document.getElementById("s");
			var xy = findPos(s);
	
			results.style.left = xy[0] + "px";
			results.style.top = xy[1] + s.offsetHeight + "px";
			results.style.width = s.offsetWidth + "px";
			
			// if there are some results, show them
			if(ul.hasChildNodes()) {
				results.appendChild(filterResults(ul));
				
				if(results.firstChild.childNodes.length == 1) results.firstChild.firstChild.getElementsByTagName("a")[0].className = "hover";
				
			}

		}
	}
	else
	{
		if(results.hasChildNodes()) results.removeChild(results.firstChild);
	}
}

/***********************************************
 * Rank results - used for sorting result sets
 * 0 if entire row starts with kw
 * 0 < i < 1 if any word in row starts with kw (1k char max)
 * i > 1 if row contains kw anywhere else
 ***********************************************/
function getRank(activity, keywords)
{
	var ret = -1;
	var kIndex = activity.indexOf(keywords);
	
	ret = (activity.charAt(kIndex - 1) == " ") ? kIndex : (200*kIndex);
	
	return ret;	
}

/***********************************************
 * Sort the result suggestion sets
 ***********************************************/
function filterResults(s)
{
	var sorted = new Array();
	
	for(var i = 0; i < s.childNodes.length; i++)
	{
		sorted.push(s.childNodes[i]);
	}
	
	var ul = document.createElement("ul");
	var lis = sorted.sort(sortIndex);
	
	for(var j = 0; j < lis.length; j++)
	{
		if(j < maxResults) ul.appendChild(lis[j]);
		else break;
	}

	return ul;
	
}

function sortIndex(a,b)
{
	// wow thats ugly -- need logic around grouped items getting out of order
	return (a.getElementsByTagName("a")[0].rev - b.getElementsByTagName("a")[0].rev);
}


/***********************************************
 * Navigate using up/down and submit with 'Enter'
 ***********************************************/
function navigate(key)
{
	var results = document.getElementById("results");
	var keyIndex = document.getElementById("keyIndex");
	
	var i = keyIndex.value;
	
	if(i == "" || !i) i = -1;
	else i = parseFloat(i);

	var ul = results.childNodes[0];

	if(ul)
	{
		if(key == '40') // DOWN
		{
			i++;
			if(i > ul.childNodes.length-1) i = ul.childNodes.length-1;
			
			keyIndex.value = i;
			
			try {
				ul.childNodes[i].getElementsByTagName("a")[0].className = "hover";
				ul.childNodes[i-1].getElementsByTagName("a")[0].className = "";
			}
			catch(e) {}
		}
		else if(key == '38') // UP
		{
			i--;
			if(i <= 0) i = 0;
			
			keyIndex.value = i;
			
			try {
				ul.childNodes[i].getElementsByTagName("a")[0].className = "hover";
				ul.childNodes[i+1].getElementsByTagName("a")[0].className = "";
			}
			catch(e) {}
		}
		else if(key == '13' || key == '9') // ENTER/TAB -- POPULATE
		{
			if(i == -1) i = 0;
			populate(ul.childNodes[i].getElementsByTagName("a")[0]);
		}
		else return;	
	}
}

/***********************************************
 * Allow for using tab onkeydown
 ***********************************************/
function tabfix(keywords, key)
{
	if(key.keyCode == '9') {
		navigate(key.keyCode);
		return false;
	}
	else return true;
}

/***********************************************
 * Populate hidden fields via onclick on 'Enter'
 ***********************************************/
function populate(a)
{
	var ul = document.getElementById("results").childNodes[0];
	
	try {
		var pick = a.innerHTML.replace("<strong>","").replace("</strong>","");
		
		// IE6 converts HTML elements to uppercase -- could be done with RegExp
		if(document.all) pick = a.innerHTML.replace("<STRONG>","").replace("</STRONG>","");
		
		document.getElementById("s").value = pick;
	}
	catch(e) {}
	
	clearSuggest();
}

/***********************************************
 * Find an elements position on the screen
 ***********************************************/
function findPos(obj) 
{
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft
		curtop = obj.offsetTop
		while (obj = obj.offsetParent) {
			curleft += obj.offsetLeft
			curtop += obj.offsetTop
		}
	}
	return [curleft,curtop];
}

/***********************************************
 * Helper to preserve onclick on suggestions
 ***********************************************/
function clearSuggest()
{
	// need a timeout so the onclick event is captured before results are hidden
	setTimeout("hideSuggest()",200);  //time set选择时间设置
}

/***********************************************
 * Hide the suggestions list and remove from DOM
 ***********************************************/
function hideSuggest()
{
	var results = document.getElementById("results");
	if(results.hasChildNodes()) results.removeChild(results.firstChild);
	
	document.getElementById("keyIndex").value = "-1"; // reset the suggestions index
}
