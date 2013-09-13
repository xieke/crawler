/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package sand.actionhandler.weibo;

import java.io.File;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;


import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import sand.actionhandler.system.ActionHandler;
import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

/**
 * Demonstration of the use of protocol interceptors to transparently
 * modify properties of HTTP messages sent / received by the HTTP client.
 * <p/>
 * In this particular case HTTP client is made capable of transparent content
 * GZIP compression by adding two protocol interceptors: a request interceptor
 * that adds 'Accept-Encoding: gzip' header to all outgoing requests and
 * a response interceptor that automatically expands compressed response
 * entities by wrapping them with a uncompressing decorator class. The use of
 * protocol interceptors makes content compression completely transparent to
 * the consumer of the {@link org.apache.http.client.HttpClient HttpClient}
 * interface.
 */
public class UdaClient {
	
	static Logger logger = Logger.getLogger(UdaClient.class);
	//static String SERVER="http://116.236.224.52:7989/";
	static String SERVER="http://www.udaspace.com/";
	static String DD_SERVER="http://timg3.ddmap.com/";
	 
	private  static DefaultHttpClient createHttpClient(){
		DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(
                    final HttpRequest request,
                    final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }
            }

        });

        httpclient.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process(
                    final HttpResponse response,
                    final HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                Header ceheader = entity.getContentEncoding();
                if (ceheader != null) {
                    HeaderElement[] codecs = ceheader.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(
                                    new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }

        });		
        return httpclient;
	}
	public static String downloadImage(String url){
		return download(SERVER, url);
	}
	
	public static String downloadDDImage(String url){
		return download(DD_SERVER, url);
	}
	
	//public static String _windowsLocation="C:/Program Files/Apache Software Foundation/Tomcat 6.0/";
	
	public static String _windowsLocation="E:/udaclient/build/";
	
	public static String downloadImageImpl(String server,String url){
		boolean ret=false;
		
		
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
        	if (url.charAt(0)=='/')
        		url = url.substring(1);
        	File storeFile = new File("/root/udaclient/build/"+url);
        	
        	if(ActionHandler.OS_TYPE.equalsIgnoreCase("windows")){        		
        		 storeFile = new File(_windowsLocation+url);
        	}
            //File storeFile = new File("c:/root/udaclient/build/"+url);
            //如果文件已经存在，不下载
            if(storeFile.exists()){
            	return "exists";
            }

        	httpclient = createHttpClient();

            HttpGet httpget = new HttpGet(server+url);

            // Execute HTTP request
           // System.out.println("executing request " + httpget.getURI());
            //logger.info("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);
            

            
            HttpEntity entity = response.getEntity();
           
            if(entity.getContentType().getValue().indexOf("text/html")>=0){
            	return "error";
            }
            
            //
            org.apache.commons.io.FileUtils.touch(storeFile);
            FileOutputStream fileOutputStream = new FileOutputStream(storeFile);
            FileOutputStream output = fileOutputStream;      
            
            entity.writeTo(output);
            output.close();
            ret=true;
                

        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		return "success";
		
	}
	
	public static String getUpPath() {

		String pathStr = "";
		String currYear = Calendar.getInstance().get(Calendar.YEAR) + "";
		String currDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + "";
		//BaseJob.
		pathStr=ActionHandler._context.getRealPath("/")+"erp.upload/"+ currYear + "/" + currDay + "/";
		logger.info("path str is "+pathStr);
		
		File f = new File(pathStr);
		if (!f.exists()) {
			f.mkdirs();
		}
		
		return pathStr;
	}

	public static String download(String url){
		boolean ret=false;
		
		if(url==null||url.equals("")){
			return "";
		}
		
        DefaultHttpClient httpclient = new DefaultHttpClient();
        
        
        String filepath = getUpPath();
        try {
//        	if (url.charAt(0)=='/')
//        		url = url.substring(1);
        	logger.info("url "+url);
    	//	String _uri = uri.trim().toLowerCase();
        	String pic_name=""; 
    		int index = url.lastIndexOf("/");
    		if (index != -1) {
    			pic_name = url.substring(index + 1);
    			int index2 = pic_name.lastIndexOf(";");
    			if(index2!=-1){
    				pic_name = pic_name.substring(0,index2);
    				 //logger.info("ext_name "+ext_name);
    			}

    		}

    		
    		filepath = filepath+pic_name;
    		logger.info("file path is "+filepath);
        	File storeFile = new File(filepath);
        	System.out.println(ActionHandler.OS_TYPE+"...................................");
        	if(ActionHandler.OS_TYPE.equalsIgnoreCase("windows")){        		
        		 //storeFile = new File(filepath);
        		 storeFile = new File(_windowsLocation+filepath);
        	}
            //File storeFile = new File("c:/root/udaclient/build/"+url);
        	//logger.info(url+filepath);
          //  如果文件已经存在，不下载
            if(storeFile.exists()){
            	return filepath;
            }

        	httpclient = createHttpClient();

            HttpGet httpget = new HttpGet(url);

            // Execute HTTP request
//            System.out.println("executing request " + httpget.getURI());
//            logger.info("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);
            
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(response.getLastHeader("Content-Encoding"));
            System.out.println(response.getLastHeader("Content-Length"));
            System.out.println("----------------------------------------");

            
            HttpEntity entity = response.getEntity();
            //if(entity.get)
            System.out.println(entity.getContentType());
//            logger.info(entity.getContentType());            
            if(entity.getContentType().getValue().indexOf("text/html")>=0){
            	return "error";
            }
            org.apache.commons.io.FileUtils.touch(storeFile);
            FileOutputStream fileOutputStream = new FileOutputStream(storeFile);
            FileOutputStream output = fileOutputStream;      
            
            entity.writeTo(output);
            
            output.close();
            ret=true;
                
//            if (entity != null) {
//                content = EntityUtils.toString(entity);
//                System.out.println(content);
//                System.out.println("----------------------------------------");
//                System.out.println("Uncompressed size: "+content.length());
//            }

        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            
            
        }
		return filepath;
		
	}	
	public static String download(String url,String filepath){
		boolean ret=false;
		
		
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
        	if (url.charAt(0)=='/')
        		url = url.substring(1);
        	File storeFile = new File(filepath);
        	System.out.println(ActionHandler.OS_TYPE+"...................................");
        	if(ActionHandler.OS_TYPE.equalsIgnoreCase("windows")){        		
        		 //storeFile = new File(filepath);
        		 storeFile = new File(_windowsLocation+filepath);
        	}
            //File storeFile = new File("c:/root/udaclient/build/"+url);
        	//logger.info(url+filepath);
          //  如果文件已经存在，不下载
            if(storeFile.exists()){
            	return "exists";
            }

        	httpclient = createHttpClient();

            HttpGet httpget = new HttpGet(url+filepath);

            // Execute HTTP request
//            System.out.println("executing request " + httpget.getURI());
//            logger.info("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);
            
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(response.getLastHeader("Content-Encoding"));
            System.out.println(response.getLastHeader("Content-Length"));
            System.out.println("----------------------------------------");

            
            HttpEntity entity = response.getEntity();
            //if(entity.get)
            System.out.println(entity.getContentType());
//            logger.info(entity.getContentType());            
            if(entity.getContentType().getValue().indexOf("text/html")>=0){
            	return "error";
            }
            org.apache.commons.io.FileUtils.touch(storeFile);
            FileOutputStream fileOutputStream = new FileOutputStream(storeFile);
            FileOutputStream output = fileOutputStream;      
            
            entity.writeTo(output);
            
            output.close();
            ret=true;
                
//            if (entity != null) {
//                content = EntityUtils.toString(entity);
//                System.out.println(content);
//                System.out.println("----------------------------------------");
//                System.out.println("Uncompressed size: "+content.length());
//            }

        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            
        }
		return "success";
		
	}	
	
	public static void addCookie(CookieStore cookieStore){
		   //设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        
        // 如果需要填充cookie
        BasicClientCookie cookie = new BasicClientCookie("SINAGLOBAL", "4384152551131.761.1366874745139");
        //cookie.setVersion(0);
       // cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        
        
        cookie = new BasicClientCookie("ULV", "1366874745145:1:1:1:4384152551131.761.1366874745139:");
        //cookie.setVersion(0);
       // cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);      
        
        cookie = new BasicClientCookie("ALF", "1369466789");
        //cookie.setVersion(0);
       // cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);      

        
        
        cookie = new BasicClientCookie("un", "tarzon@21cn.com");
        //cookie.setVersion(0);
       // cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);     

        
        cookie = new BasicClientCookie("SUE", "es%3D888879b5c6579ed58ce44764fc70789a%26ev%3Dv1%26es2%3Dea414c75258d739dc29dac83a32e13d7%26rs0%3D0Pfpg7NGhPKa7WCtgXEyb7rf8eycrrUksec83xOU6kkZfPW7NSVM01dl0YwSxn5Yg5qgCFI4CTJdZCaUdS1xI7GO%252BOiXaUVZo6XI3DZMBslgxtYYLiA5ho8BwzAzaj5N4mHmd6yubMcNRiEK49TmeQ2BsQKujgfqRIh4i3Qc27c%253D%26rv%3D0");
        //cookie.setVersion(0);
       // cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);     
        
        
        
        cookie = new BasicClientCookie("SUP", "cv%3D1%26bt%3D1366943072%26et%3D1366944871%26d%3Dc909%26i%3D957b%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3D1694607674%26user%3Dtarzon%254021cn.com%26ag%3D4%26name%3Dtarzon%254021cn.com%26nick%3Dtarzon%26fmp%3D%26lcp%3D2013-02-27%252014%253A02%253A16");
        //cookie.setVersion(0);
       // cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);     
        
        
        cookie = new BasicClientCookie("SUS", "SID-1694607674-1366943072-JA-4hcq3-2172dd350b7313b3544937abc38f3e72");
        //cookie.setVersion(0);
       // cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);           
	}
	public static String syn(String method,String machineno,String params){
		String content="";
		
		
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
        	httpclient = createHttpClient();
        	//		HttpGet httpget = new HttpGet("http://www.broken-server.com/");
        			// 对这个请求覆盖默认策略
        		
        			HttpGet httpget = new HttpGet(SERVER+method+"?no="+machineno+"&"+params);

                	httpclient.getParams().setParameter(
                			ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);

        			httpget.getParams().setParameter(
                			ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
              
//           httpget.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            // Execute HTTP request
            //System.out.println("executing request " + httpget.getURI());
           
           
          // DefaultHttpClient httpclient = new DefaultHttpClient();
        // 创建一个本地的cookie store实例
           CookieStore cookieStore = new BasicCookieStore();
           
           addCookie(cookieStore);
        // 设置存储
        httpclient.setCookieStore(cookieStore);
        
        
           
            logger.info("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);
            
//            System.out.println("----------------------------------------");
//            System.out.println(response.getStatusLine());
//            System.out.println(response.getLastHeader("Content-Encoding"));
//            System.out.println(response.getLastHeader("Content-Length"));
//            System.out.println("----------------------------------------");

            HttpEntity entity = response.getEntity();
            //System.out.println(entity.getContentType());
            
            if (entity != null) {
                content = EntityUtils.toString(entity);
//                System.out.println(content);
//                System.out.println("----------------------------------------");
//                System.out.println("Uncompressed size: "+content.length());
            }

        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		
		return content;
	}


	public static String syn(String url){
		String content="";
		
		
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
        	httpclient = createHttpClient();

            HttpGet httpget = new HttpGet(url);

            // Execute HTTP request
            //System.out.println("executing request " + httpget.getURI());
            //logger.info("executing request " + httpget.getURI());
            
//            System.out.println("----------------------------------------");
//            System.out.println(response.getStatusLine());
//            System.out.println(response.getLastHeader("Content-Encoding"));
//            System.out.println(response.getLastHeader("Content-Length"));
//            System.out.println("----------------------------------------");

            //System.out.println(entity.getContentType());
            
         	httpclient.getParams().setParameter(
        			ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);

			httpget.getParams().setParameter(
        			ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

	           CookieStore cookieStore = new BasicCookieStore();
	           
	           addCookie(cookieStore);
	        // 设置存储
	        httpclient.setCookieStore(cookieStore);
	        //httpclient.ex
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                content = EntityUtils.toString(entity);
                System.out.println(content);
                System.out.println("----------------------------------------");
                System.out.println("Uncompressed size: "+content.length());
            }

        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		
		return content;
	}
	


    static class GzipDecompressingEntity extends HttpEntityWrapper {

        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent()
            throws IOException, IllegalStateException {

            // the wrapped entity's getContent() decides about repeatability
            InputStream wrappedin = wrappedEntity.getContent();

            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() {
            // length of ungzipped content is not known
            return -1;
        }

    }

    
//    public static AccessToken refreshToken(){ 
//        Properties props = new Properties(); 
//        try { 
//                props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("sina_account.properties")); 
//                String url = props.getProperty("url");/*模拟登录的地址，https://api.weibo.com/oauth2/authorize*/ 
//                PostMethod postMethod = new PostMethod(url); 
//                postMethod.addParameter("client_id", props.getProperty("client_id"));//your client id 
//                postMethod.addParameter("redirect_uri", props.getProperty("redirect_uri"));//your url 
//                postMethod.addParameter("userId", props.getProperty("userId"));//需要获取微薄的use id 
//                postMethod.addParameter("passwd", props.getProperty("passwd")); 
//                postMethod.addParameter("isLoginSina", "0"); 
//                postMethod.addParameter("action", "submit"); 
//                postMethod.addParameter("response_type", props.getProperty("response_type"));//code 
//                HttpMethodParams param = postMethod.getParams(); 
//                param.setContentCharset("UTF-8"); 
//                List<Header> headers = new ArrayList<Header>(); 
//                
//               // PostMethod.
//                postMethod.addRequestHeader(header);
//                h//ttpclient.e
//                postMethod.execute(state, conn);
//                headers.add(new Header("Referer", "https://api.weibo.com/oauth2/authorize?client_id=your_client_id&redirect_uri=your_redirect_url&from=sina&response_type=code"));//伪造referer 
//                headers.add(new Header("Host", "api.weibo.com")); 
//                headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0")); 
//                
//                postMethod.execute(state, conn)
//                HttpClient client  = new HttpClient(); 
//                client.ex
//                client.getHostConfiguration().getParams().setParameter("http.default-headers", headers); 
//                client.exex.executeMethod(postMethod); 
//                int status = postMethod.getStatusCode(); 
//                if(status != 302){ 
//                        LOG.error("refresh token failed"); 
//                        return null; 
//                } 
//                Header location = postMethod.getResponseHeader("Location"); 
//                if(location != null){ 
//                        String retUrl = location.getValue(); 
//                        int begin = retUrl.indexOf("code="); 
//                        if(begin != -1){ 
//                                int end = retUrl.indexOf("&", begin); 
//                                if(end == -1) 
//                                        end = retUrl.length(); 
//                                String code = retUrl.substring(begin+5, end); 
//                                if(code != null){ 
//                                                AccessToken token = oauth.getAccessTokenByCode(code); 
//                                                Oauth oauth = new Oauth(); 
//                                                return token; 
//                                } 
//                        } 
//                } 
//        } catch (FileNotFoundException e) { 
//                LOG.error("error" + e); 
//        } catch (IOException e) { 
//                LOG.error("error" + e); 
//        } 
//        LOG.error("refresh token failed"); 
//        return null; 
//}     
    
    public static String REDIRECT_URI="http://60.172.229.19:18080/basic.LoginAH.weibo";
    
	public static AccessToken getToken(String userid,String password) throws Exception{
		
		//https://api.weibo.com/oauth2/authorize?client_id=2517196057&redirect_uri=http://116.236.101.196:18080/weibo.WeiBoAH.weibo&response_type=code&state=&scope=
		
		String url="https://api.weibo.com/oauth2/authorize";
		String content="";
		
		
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
        	httpclient = createHttpClient();

            HttpPost httppost = new HttpPost(url);

        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1); 
        	nameValuePairs.add(new BasicNameValuePair("client_id", "2517196057")); 
        	nameValuePairs.add(new BasicNameValuePair("redirect_uri",REDIRECT_URI)); 
        	nameValuePairs.add(new BasicNameValuePair("userId",userid)); 
        	nameValuePairs.add(new BasicNameValuePair("passwd", password)); 
        	nameValuePairs.add(new BasicNameValuePair("isLoginSina", "")); 
        	nameValuePairs.add(new BasicNameValuePair("action", "submit"));
        	nameValuePairs.add(new BasicNameValuePair("response_type", "code"));
        	nameValuePairs.add(new BasicNameValuePair("withOfficalFlag", "0"));
        	nameValuePairs.add(new BasicNameValuePair("ticket", ""));
        	nameValuePairs.add(new BasicNameValuePair("'regCallback'", ""));
        	nameValuePairs.add(new BasicNameValuePair("state", ""));
        	nameValuePairs.add(new BasicNameValuePair("from", ""));
        	
        	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
        	
//          headers.add(new Header("Referer", "https://api.weibo.com/oauth2/authorize?client_id=your_client_id&redirect_uri=your_redirect_url&from=sina&response_type=code"));//伪造referer 
//          headers.add(new Header("Host", "api.weibo.com")); 
//          headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0")); 
        	
            httppost.addHeader("Referer","https://api.weibo.com/oauth2/authorize?client_id=2517196057&redirect_uri="+REDIRECT_URI+"&from=sina&response_type=code");
            httppost.addHeader("Host","api.weibo.com");
            httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0");
            
         	httpclient.getParams().setParameter(
        			ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);

			httppost.getParams().setParameter(
        			ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

	           CookieStore cookieStore = new BasicCookieStore();
	           
	           addCookie(cookieStore);
	        // 设置存储
	        httpclient.setCookieStore(cookieStore);
	        //httpclient.ex
            HttpResponse response = httpclient.execute(httppost);
            
            Header location = response.getFirstHeader("Location"); 
            logger.info("location "+location);
          if(location != null){ 
        	  String retUrl = location.getValue(); 
        	  System.out.println(retUrl);
        	  logger.info("retUrl  "+retUrl);
            //String reU = location.getValue(); 
            int begin = retUrl.indexOf("code="); 
            if(begin != -1){ 
                    int end = retUrl.indexOf("&", begin); 
                    if(end == -1) 
                            end = retUrl.length(); 
                    String code = retUrl.substring(begin+5, end); 
                    if(code != null){ 
                    	Oauth oauth = new Oauth(); 
                    			System.out.println("code is "+code);
                                    AccessToken token = oauth.getAccessTokenByCode(code); 
                                    
                                    return token; 
                    } }
        	  
          }
//            HttpEntity entity = response.getEntity();
//
//            if (entity != null) {
//                content = EntityUtils.toString(entity);
//                System.out.println(content);
//                System.out.println("----------------------------------------");
//                System.out.println("Uncompressed size: "+content.length());
//            }

        } catch  (Exception e) {
			// TODO Auto-generated catch block
        	logger.error("error",e);
			e.printStackTrace();
			throw e;
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		
		return null;
	}
	public final static void main(String[] args) throws Exception {
	    //	UdaClient.downloadImage("erp.upload/2010/xiek/ANNE14514412.gif");
	    	//UdaClient.downloadDDImage("coupon/100/1312461527094.jpg");
			//String xml=UdaClient.syn("http://coupon.ddmap.com/api/api.xml");
			//System.out.println(xml);
			//UdaClientJob.parserXml(xml);
		AccessToken ac = UdaClient.getToken("tarzon@21cn.com","qjdble597969");
		System.out.println(ac.getAccessToken());
			
	    }
}

