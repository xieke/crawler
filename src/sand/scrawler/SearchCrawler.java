package sand.scrawler;

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;

// 搜索Web爬行者
public class SearchCrawler implements Runnable{
 
/* disallowListCache缓存robot不允许搜索的URL。 Robot协议在Web站点的根目录下设置一个robots.txt文件,
  *规定站点上的哪些页面是限制搜索的。 搜索程序应该在搜索过程中跳过这些区域,下面是robots.txt的一个例子:
 # robots.txt for http://somehost.com/
   User-agent: *
   Disallow: /cgi-bin/
   Disallow: /registration # /Disallow robots on registration page
   Disallow: /login
  */

	
  private HashMap< String,ArrayList< String>> disallowListCache = new HashMap< String,ArrayList< String>>(); 
  
  ArrayList< String> titleUrlList= new ArrayList< String>();//列表页
  ArrayList< String> errorList= new ArrayList< String>();//错误信息 
  ArrayList< String> result=new ArrayList< String>(); //搜索到的结果 
  String startUrl;//开始搜索的起点
  int maxUrl;//最大处理的url数
  String searchString;//要搜索的字符串(英文)
  boolean caseSensitive=false;//是否区分大小写
  boolean limitHost=false;//是否在限制的主机内搜索
  
  public SearchCrawler(String startUrl,int maxUrl,String searchString){
   this.startUrl=startUrl;
   this.maxUrl=maxUrl;
   this.searchString=searchString;
  }

   public ArrayList< String> getResult(){
       return result;
   }

  public void run(){//启动搜索线程
      
     //  crawl(startUrl,maxUrl, searchString,limitHost,caseSensitive);
	  //String url="";
	  String titleurl="http://edu.qq.com/job/jlgc_more.htm";
	  //List<String> urls = this.getTitleUrlList();
	  List<String> urls = this.getTitleUrlList();
	  //String rule="<tr><td height=\"24\" class=\"font14px\"><div align=\"left\">·<a target=\"_blank\" class=\"blackul\" href=\"{url=NO\"}\">{title=NO<}</a>";
	  //String rule="<img";// src=images/{NO\"}";//" border=0>";
	  
	  String rule="<img src=images/{*} border=0><a href=\"{url=NO\"}\" target=_blank>{title=NO<}</a>";
	// String a ="<a href=\"{url=NO\"}\" target=_blank>{title=NO<}</a>";
	 List<Map> v=  crawlTitle(urls,rule);
	 //String contentRule="<div id=\"ArticleCnt\">{content=*}</div><div id=\"copyweb\">";
	 String contentRule="<div align=\"center\" class=\"s3\">作者: {author=NO\"}　</div>{*}<tr><td><p class=s11>&nbsp;&nbsp;{content=*}</td></tr>";
	  crawlContent(v,contentRule);
  }
   
  /**
   * 取得title list列表
   */
  private List<String>getTitleUrlList(){
	  List v =new ArrayList();
	  String s="可变页码";
	  if("固定地址".equals(s)){
		  v.add("http://edu.qq.com/job/jlgc_more.htm");
		  v.add("http://edu.qq.com/job/jlgc_more1.htm");
		  v.add("http://edu.qq.com/job/jlgc_more2.htm");
	  }
	  if("可变页码".equals(s)){
		  v=RegexUtil.parsePageUrl("http://wind.yinsha.com/ashow.php?sid=5&%20size=20&page=[page]", 1, 2);
	  }
	return v;
	  
  }

    //检测URL格式
  private URL verifyUrl(String url) {
    // 只处理HTTP URLs.
    if (!url.toLowerCase().startsWith("http://"))
      return null;

    URL verifiedUrl = null;
    try {
      verifiedUrl = new URL(url);
    } catch (Exception e) {
      return null;
    }

    return verifiedUrl;
  }

  // 检测robot是否允许访问给出的URL.
 private boolean isRobotAllowed(URL urlToCheck) { 
    String host = urlToCheck.getHost().toLowerCase();//获取给出RUL的主机 
    //System.out.println("主机="+host);

    // 获取主机不允许搜索的URL缓存 
    ArrayList< String> disallowList =disallowListCache.get(host); 

    // 如果还没有缓存,下载并缓存。 
    if (disallowList == null) { 
      disallowList = new ArrayList< String>(); 
      try { 
        URL robotsFileUrl =new URL("http://" + host + "/robots.txt"); 
        BufferedReader reader =new BufferedReader(new InputStreamReader(robotsFileUrl.openStream())); 

        // 读robot文件，创建不允许访问的路径列表。 
        String line; 
        while ((line = reader.readLine()) != null) { 
          if (line.indexOf("Disallow:") == 0) {//是否包含"Disallow:" 
            String disallowPath =line.substring("Disallow:".length());//获取不允许访问路径 

            // 检查是否有注释。 
            int commentIndex = disallowPath.indexOf("#"); 
            if (commentIndex != - 1) { 
              disallowPath =disallowPath.substring(0, commentIndex);//去掉注释 
            } 
             
            disallowPath = disallowPath.trim(); 
            disallowList.add(disallowPath); 
           } 
         } 

        // 缓存此主机不允许访问的路径。 
        disallowListCache.put(host, disallowList); 
      } catch (Exception e) { 
              return true; //web站点根目录下没有robots.txt文件,返回真
      } 
    } 

     
    String file = urlToCheck.getFile(); 
    //System.out.println("文件getFile()="+file);
    for (int i = 0; i < disallowList.size(); i++) { 
      String disallow = disallowList.get(i); 
      if (file.startsWith(disallow)) { 
        return false; 
      } 
    } 

    return true; 
  } 

	/**
	 * 下载网页
	 * 
	 * @param urlStr
	 *            网页地址 比如: http://www.163.com
	 * @param outPath
	 *            文件输出路径
	 */
	public static String downloadPages(String urlStr) {
		/** 读入输入流的数据长度 */
		int chByte = 0;

		/** 网络的url地址 */
		URL url = null;

		/** http连接 */
		HttpURLConnection httpConn = null;

		/** 输入流 */
		InputStream in = null;

		/** 文件输出流 */
		//FileOutputStream out = null;
	//	System.out.println("url is "+urlStr);
		try {
			url = new URL(urlStr);
			httpConn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");

			in = httpConn.getInputStream();
		//	out = new FileOutputStream(new File(outPath));

            StringBuffer buffer = new StringBuffer();
            if(in!=null){
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"gbk"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                    buffer.append(line);
            }
            reader.close();
            }
            return buffer.toString();

//			chByte = in.read();
//			while (chByte != -1) {
//				out.write(chByte);
//				// System.out.println(chByte);
//				chByte = in.read();
//			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
			//	out.close();
				if(in!=null) in.close();
				httpConn.disconnect();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return "";

	}
 
  private String downloadPage(URL pageUrl) {
     try {
        // Open connection to URL for reading.
        BufferedReader reader =
          new BufferedReader(new InputStreamReader(pageUrl.openStream(), "gb2312"));


        // Read page into buffer.
        String line;
        StringBuffer pageBuffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
          pageBuffer.append(line);
        }
        
        return pageBuffer.toString();
     } catch (Exception e) {
     }

     return null;
  }

  // 从URL中去掉"www"
  private String removeWwwFromUrl(String url) {
    int index = url.indexOf("://www.");
    if (index != -1) {
      return url.substring(0, index + 3) +
        url.substring(index + 7);
    }

    return (url);
  }

  // 解析页面并找出链接
  private ArrayList< String> retrieveLinks(URL pageUrl, String pageContents, HashSet crawledList,
    boolean limitHost)
  {
    // 用正则表达式编译链接的匹配模式。
    Pattern p =Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(pageContents);

    
    ArrayList< String> linkList = new ArrayList< String>();
    while (m.find()) {
      String link = m.group(1).trim();
      //System.out.println("link:"+link);
      if (link.length() < 1) {
        continue;
      }

      // 跳过链到本页面内链接。
      if (link.charAt(0) == '#') {
        continue;
      }

      
      if (link.indexOf("mailto:") != -1) {
        continue;
      }
     
      if (link.toLowerCase().indexOf("javascript") != -1) {
        continue;
      }

      if (link.indexOf("://") == -1){
        if (link.charAt(0) == '/') {//处理绝对地  
          link = "http://" + pageUrl.getHost()+":"+pageUrl.getPort()+ link;
        } else {         
          String file = pageUrl.getFile();
          if (file.indexOf('/') == -1) {//处理相对地址
            link = "http://" + pageUrl.getHost()+":"+pageUrl.getPort() + "/" + link;
          } else {
            String path =file.substring(0, file.lastIndexOf('/') + 1);
            link = "http://" + pageUrl.getHost() +":"+pageUrl.getPort()+ path + link;
          }
        }
      }

      int index = link.indexOf('#');
      if (index != -1) {
        link = link.substring(0, index);
      }

      link = removeWwwFromUrl(link);

      URL verifiedLink = verifyUrl(link);
      if (verifiedLink == null) {
        continue;
      }

      /* 如果限定主机，排除那些不合条件的URL*/
      if (limitHost &&
          !pageUrl.getHost().toLowerCase().equals(
            verifiedLink.getHost().toLowerCase()))
      {
        continue;
      }

      // 跳过那些已经处理的链接.
      if (crawledList.contains(link)) {
        continue;
      }

       linkList.add(link);
    }

   return (linkList);
  }

 // 搜索下载Web页面的内容，判断在该页面内有没有指定的搜索字符串

  private boolean searchStringMatches(String pageContents, String searchString, boolean caseSensitive){
       String searchContents = pageContents; 
       if (!caseSensitive) {//如果不区分大小写
          searchContents = pageContents.toLowerCase();
       }

    
    Pattern p = Pattern.compile("[\\s]+");
    String[] terms = p.split(searchString);
   // System.out.println("term :"+terms.length);
    for (int i = 0; i < terms.length; i++) {
    	//System.out.println(terms[i]);
      if (caseSensitive) {
        if (searchContents.indexOf(terms[i]) == -1) {
          return false;
        }
      } else {
        if (searchContents.indexOf(terms[i].toLowerCase()) == -1) {
          return false;
        }
      }     }

    return true;
  }

  public void crawlContent(List<Map> titleList,String rule){
	  
	  
	  String ruleregex=RegexUtil.getRuleRegex(rule);
	  System.out.println("content rule regex "+ruleregex);
	  for(Map<String,String> m:titleList){
		  //Map m = new HashMap();
		     String pageContents =downloadPages(m.get("url"));		  
			 // System.out.println(ruleregex);
		     System.out.println("page content size is "+pageContents.length());
		   //  System.out.println(pageContents);
		  RegexUtil.parseContent(ruleregex,pageContents,m);		  
		  System.out.println(m.get("content"));

	  }
  }
  
  /**
   * 先搜  title  列表
   * @param regex
   */
  public List<Map> crawlTitle(List<String> titleurls,String rule){

		//  URL titleUrl = verifyUrl(titleurl);
		  //String pageContents = downloadPage(titleUrl);
	  List<Map> paramv = new ArrayList();
	  for(String titleurl:titleurls){
		  String pageContents =downloadPages(titleurl);
		  System.out.println(titleurl+"  page size "+pageContents.length());
		  List<String> titlev = RegexUtil.parseRule(rule);
		  for(String s:titlev){
			  System.out.println(s);			  
		  }
		  String ruleregex=RegexUtil.getRuleRegex(rule);
		  System.out.println(ruleregex);
		  //System.out.println("pageContents is :"+pageContents);
		  
		  String url = RegexUtil.getUrl(titleurl);
		  paramv.addAll(RegexUtil.parseTitle(url,ruleregex,pageContents,titlev));
		  
		  for(Map m:paramv){
			  System.out.println(m);			 
		  }
		  
		  
		  //System.out.println(url);
		  
	  }
		  return paramv;
	  
  }
  
  //执行实际的搜索操作
  public ArrayList< String> crawl(String startUrl, int maxUrls, String searchString,boolean limithost,boolean caseSensitive )
  { 
    
    System.out.println("searchString="+searchString);
    HashSet< String> crawledList = new HashSet< String>();
    LinkedHashSet< String> toCrawlList = new LinkedHashSet< String>();

     if (maxUrls < 1) {
        errorList.add("Invalid Max URLs value.");
        System.out.println("Invalid Max URLs value.");
      }
  
    
    if (searchString.length() < 1) {
      errorList.add("Missing Search String.");
      System.out.println("Missing search String");
    }

    
    if (errorList.size() > 0) {
      System.out.println("err!!!");
      return errorList;
      }

    
    // 从开始URL中移出www
    startUrl = removeWwwFromUrl(startUrl);

    
    toCrawlList.add(startUrl);
    while (toCrawlList.size() > 0) {
      
      if (maxUrls != -1) {
        if (crawledList.size() == maxUrls) {
          break;
        }
      }

      // Get URL at bottom of the list.
      String url =  toCrawlList.iterator().next();

      // Remove URL from the to crawl list.
      toCrawlList.remove(url);

      // Convert string url to URL object.
      URL verifiedUrl = verifyUrl(url);

      // Skip URL if robots are not allowed to access it.
      if (!isRobotAllowed(verifiedUrl)) {
        continue;
      }

    
      // 增加已处理的URL到crawledList
      crawledList.add(url);
      String pageContents = downloadPage(verifiedUrl);
      System.out.println(verifiedUrl+" page content size : "+pageContents.length());

      
      if (pageContents != null && pageContents.length() > 0){
        // 从页面中获取有效的链接
        ArrayList< String> links =retrieveLinks(verifiedUrl, pageContents, crawledList,limitHost);
        System.out.println("links :"+links.size());
        toCrawlList.addAll(links);

        if (searchStringMatches(pageContents, searchString,caseSensitive))
        {
          result.add(url);
          System.out.println(url);
        }
     }

    
    }
   return result;
  }

  // 主函数
  public static void main(String[] args) {
//     if(args.length!=3){
//        System.out.println("Usage:java SearchCrawler startUrl maxUrl searchString");
//        return;
//     }
//    int max=Integer.parseInt(args[1]);
    SearchCrawler crawler = new SearchCrawler("http://www.163.com",10,"网易");
    Thread  search=new Thread(crawler);
    System.out.println("Start searching...");
    System.out.println("result:");
    search.start();
   
  }
}