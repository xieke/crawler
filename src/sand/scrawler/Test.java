package sand.scrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test 类
 * 
 * @author Ma rulin
 * 
 * @version 1.0
 * 
 */
public class Test {

	/**
	 * 主程序入口
	 * 
	 * @param args
	 *            输入参数数组
	 */
	public static void main(String[] args) {
		System.out.println("beging...");
		DownLoadPages("http://edu.qq.com/job/jlgc_more.htm", "e:/webcraw");
//		String s ="<tr><td height=\"24\" class=\"font14px\"><div align=\"left\">·<a target=\"_blank\" class=\"blackul\" href=\"{url=NO\"}\">{title=NO<}</a>";;
//		Pattern		p1 = Pattern.compile("/\\{(.*?)\\}/is");
//		Test.isContentRelevant(s, p1);
//		System.out.println("end.");
	}

	/**
	 * 下载网页
	 * 
	 * @param urlStr
	 *            网页地址 比如: http://www.163.com
	 * @param outPath
	 *            文件输出路径
	 */
	public static void DownLoadPages(String urlStr, String outPath) {
		/** 读入输入流的数据长度 */
		int chByte = 0;

		/** 网络的url地址 */
		URL url = null;

		/** http连接 */
		HttpURLConnection httpConn = null;

		/** 输入流 */
		InputStream in = null;

		/** 文件输出流 */
		FileOutputStream out = null;

		try {
			url = new URL(urlStr);
			httpConn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");

			in = httpConn.getInputStream();
			out = new FileOutputStream(new File(outPath));

			chByte = in.read();
			while (chByte != -1) {
				out.write(chByte);
				// System.out.println(chByte);
				chByte = in.read();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				httpConn.disconnect();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	
	public static boolean isContentRelevant(String content,     	Pattern regexpPattern) {     
	    boolean retValue = false;     
	    if (content != null) {     
	        //是否符合正则表达式的条件     
	        Matcher m = regexpPattern.matcher(content.toLowerCase());     
	        retValue = m.find();     
	        System.out.println(retValue);
	    }     
	    return retValue;     
	}    
}
