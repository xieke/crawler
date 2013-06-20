
var xmlHttp=false;
try {
   xmlHttp = new XMLHttpRequest();
} catch (trymicrosoft) {
     try {
       xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
     } catch (othermicrosoft) {
       try {
         xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
          
       }  
     }
   }
function callServer(smallLei){
    var url="GeneralHandleSvt?reqType=utility.UtilActionHandler.loadMaterial&class=" + smallLei;
    xmlHttp.open("get",url,"true");
    xmlHttp.onreadystatechange =upsmlei;
    xmlHttp.send(null);
	alert("sended "+url);
}
function upsmlei(){
    if(xmlHttp.readyState == 4){
        if(xmlHttp.status==200){
			alert("come on");
            var smlei = document.getElementById("smallLei");
            smlei.options.length=0;
            var arr=xmlHttp.responseText.split("|");
			smlei.options.add(new Option('a','b'));
			
            for(var i=0;i<arr.length-1;i++){
				//alert(arr[i]);
                smlei.options.add(new Option(arr[i],arr[i]));
            }
			//smanPromptList(arr,"aspx");
			//smanPromptList(arr,"aspx2");
			smanPromptList(arr,"inputer");
		}
    }
}
