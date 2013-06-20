package sand.test;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;



public class APITest extends Thread {

	 protected static String memberCenterUrl = "http://10.38.128.105/router";//会员中心测试地址
	 //protected static String memberCenterUrl = "https://my.bhecard.com/router?";//会员中心测试地址
	 
	protected static String APP_KEY = "00001";
     protected static String APP_SECRET = "abcdeabcdeabcdeabcdeabcde";
    
  
     public static String mapToJSON(HashMap<String,String> hashMap){
    	 if(hashMap==null){
    		 return null;
    	 }
    	 StringBuffer bs=new StringBuffer("{");
    	 for(Iterator<Map.Entry<String, String>> it=hashMap.entrySet().iterator();it.hasNext();){
    		 Map.Entry<String, String> e=it.next();
    		 bs.append("\"").append(e.getKey()).append("\"").append(":\"").append(e.getValue()).append("\",");
    	 }
    	 if(bs.length()>1){
    		 bs.deleteCharAt(bs.length()-1);
    	 }
    	 bs.append("}");
    	 return bs.toString();
     }
    
     public static String testUserGet(String method,String version,String messageFormat,String locale,String entityParam ){
         TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
         apiparamsMap.put("messageFormat", messageFormat);
         apiparamsMap.put("method", method);
         apiparamsMap.put("appKey",APP_KEY);
         apiparamsMap.put("v", version);
         apiparamsMap.put("locale", locale);
         apiparamsMap.put("deal", entityParam);
         //生成签名
         String sign = Util.md5Signature(apiparamsMap,APP_SECRET,"SHA1");
         apiparamsMap.put("sign", sign);
         StringBuilder param = new StringBuilder();
         for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();) {
             Map.Entry<String, String> e = it.next();
             param.append("&").append(e.getKey()).append("=").append(e.getValue());
         }
         return param.toString().substring(1);
     }

     public static void main(String[] args) {
    		 for(int i=0;i<200;i++){
    			 new APITest().start();
    		 }
     }
     public void run(){
    	 for(int j=0;j<100;j++){
    		 threadTest();
    	 }
    	 
     }
     public  void threadTest(){
    	 long begin=System.currentTimeMillis();
    	 HashMap<String,String> map=new HashMap<String,String>();
    	 map.put("cardNo", "2902210001000008010");
    	 map.put("dealDate", "1");
    	 map.put("serialNo", "1");
    	 map.put("issueOrg", "1");
    	 map.put("dealType", "1");
    	 map.put("sum", "1");
    	 map.put("targetAccount", "");
    	 map.put("memo", "1");
    	 map.put("status", "1");
    	 String param =testUserGet("user.sendSms","1.0","json","zh_CN",mapToJSON(map));
    	 String result = Util.getResult(memberCenterUrl,"application/x-www-form-urlencoded",param);
         long end=System.currentTimeMillis();
    	 System.out.println(Thread.currentThread().getId()+","+result+",cost:"+(end-begin)+"ms");
     }
     
     
}
