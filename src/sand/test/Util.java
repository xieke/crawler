package sand.test;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.TreeMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
 

public class Util {

         /**
         * 签名算法:参数列表按字母顺序排列, 对请求数据进行签名:签名算法(MD5、SHA1等)(<secret> <paramName1><paramValue><secret>)
         * @param params 请求参数
         * @param secret 分配的APP_SECRET
         * @param signMethod 算法 MD5,SHA1
         */

         public static String md5Signature(TreeMap<String, String> params, String secret,String signMethod) {
        	 	 if(params==null){
        	 		 return null;
        	 	 }
                 String result = null;
                 StringBuffer orgin = new StringBuffer(secret);
                 Iterator<String> iter = params.keySet().iterator();
                 while (iter.hasNext()) {
                         String name = (String) iter.next();
                         orgin.append(name).append(params.get(name));
                 }
                 orgin.append(secret);
                 try {
                         MessageDigest md = MessageDigest.getInstance(signMethod);
                         byte [] di=md.digest(orgin.toString().getBytes("utf-8"));
                         result = byte2hex(di);
                 } catch (Exception e) {
                         throw new java.lang.RuntimeException("sign error !");
                 }
             return result;
         }

         /**
         * 二进制转字符串
         */
         private static String byte2hex(byte[] b) {
                 StringBuffer hs = new StringBuffer();
                 String stmp = "";
                 for (int n = 0; n < b.length; n++) {
                         stmp = (java.lang.Integer.toHexString(b[n]&0xFF));
                         if (stmp.length() == 1)
                                 hs.append("0").append(stmp);
                         else
                                 hs.append(stmp);
                 }
                 return hs.toString().toUpperCase();
         }

         /**
          * 发送http或https请求,https请求只支持单向认证
          * @param urlStr 请求的url
          * @param contentType 上下文类型
          * @param content 发送内容，如果为空使用get请求否则使用post请求
          * @return String
          */

         public static String getResult(String urlStr, String contentType,String content) {
        	 URL url = null;
        	 URLConnection connection = null;
        	 boolean isHttps=false;
             try {
                     url = new URL(urlStr);
                     connection =  url.openConnection();
                     connection.setDoOutput(true);
                     connection.setDoInput(true);
                     isHttps= connection instanceof HttpsURLConnection; //是否为HTTPS请求
                     if(isHttps){
                         TrustManager [] trustAllCerts=new   TrustManager[]{//信任所有证书
                        		 new X509TrustManager() {
        							@Override
        							public void checkClientTrusted(
        									X509Certificate[] arg0, String arg1)
        									throws CertificateException {
        							}
        							@Override
        							public void checkServerTrusted(
        									X509Certificate[] arg0, String arg1)
        									throws CertificateException {
        							}
        							@Override
        							public X509Certificate[] getAcceptedIssuers() {
        								return null;
        							}}
                         };
                         SSLContext sc = SSLContext.getInstance("SSL");
                         sc.init(null, trustAllCerts, new SecureRandom());
                         ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
                       
                         ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier(){
         					@Override
         					public boolean verify(String hostname, SSLSession session) {//信任所有主机
         						return true;
         					}
                          });
                     }
                    
                     connection.setUseCaches(false);
                     connection.setConnectTimeout(15000);//连接15秒超时
                     connection.setReadTimeout(15000);//读返回结果15秒超时
                     connection.setRequestProperty("Content-Type",contentType);
                     connection.connect();
                    if(content!=null&&!content.trim().equals("")){//向服务器发送数据
                     DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                     out.write(content.getBytes("utf-8"));
                     out.flush();
                     out.close();
                    }
                     int responseCode=0;//HTTP状态码
                     if(isHttps){
                    	 responseCode=((HttpsURLConnection) connection).getResponseCode(); 
                     }else{
                    	 responseCode=((HttpURLConnection) connection).getResponseCode();
                     }
                     InputStream input=null;//读返回结果流
                     if(responseCode==HttpURLConnection.HTTP_OK){
                     	input=connection.getInputStream();
                     }else{
                    	 if(isHttps){
                    		 input=((HttpsURLConnection) connection).getErrorStream();
                    	 }else{
                    		 input=((HttpURLConnection) connection).getErrorStream();
                    	 }
                     	
                     }
                     StringBuffer buffer = new StringBuffer();
                     if(input!=null){
                     BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
                     String line = "";
                     while ((line = reader.readLine()) != null) {
                             buffer.append(line);
                     }
                     reader.close();
                     }
                     return buffer.toString();
             } catch (Throwable e) {
                     e.printStackTrace();
             } finally {
                     if (connection != null) {
                    	 if(isHttps){
                    		 ((HttpsURLConnection) connection).disconnect();
                    	 }else{
                    		 ((HttpURLConnection) connection).disconnect();
                    		 System.out.println("释放连接");
                    	 }
                     }
             }
             return null;

         }
}

