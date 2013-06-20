package sand.actionhandler.basic;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.management.Query;

import org.apache.log4j.Logger;

import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class MailConfig {

	private static Logger logger = Logger.getLogger(MailConfig.class);

	static List status_valid = new ArrayList();
	static List<String> superAv = new ArrayList();
	static Properties properties = new Properties();
	static {

		InputStream in = MailConfig.class.getClassLoader()
				.getResourceAsStream("maillist");
		try {
			logger.info("in is "+in);
			if(in!=null){
				properties.load(in);	
			}
				
			if(in!=null)
				in.close();
			// properties.c

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static void readTxtFile(String filePath){
        try {
                String encoding="UTF-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        System.out.println(lineTxt);
                    }
                    read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
     
    }
     

	public static String readProperty(String name) {
		String value=properties.getProperty(name);
		logger.info("read property "+name+"  value: "+value);
		return value;
	}
	// 写入properties信息
	public static void writeProperty(String name, String value) {
		// Properties prop = new Properties();
		try {
			logger.info("write property "+name);
			// fis = new FileInputStream(filePath);
			// 从输入流中读取属性列表（键和元素对）
			// prop.load(fis);
			// 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			// OutputStream fos = new FileOutputStream(filePath);
			// 只针对windows的取路径方法
			String uri = MailConfig.class.getClassLoader()
					.getResource("config.properties").toExternalForm()
					.substring(5);
			System.out.print("path is " + uri);
			logger.info("begin write propery path is " + uri);

			OutputStream fos = new FileOutputStream(uri);
			properties.setProperty(name, value);
			// 以适合使用 load 方法加载到 Properties表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			properties.store(fos, "Update '" + name + "' value ");
			fos.close();
		} catch (Exception e) {
			logger.error("error is ",e);
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws SQLException {
		MailConfig.writeProperty("afd", "adfadf");
	}
}
