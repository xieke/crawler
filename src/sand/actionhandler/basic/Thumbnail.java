package sand.actionhandler.basic;

///Thumbnail.java Java生成缩略图 -javacode

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import sand.depot.business.document.Document;
import sand.image.ImageOperation;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/** */
/**
 * 
 * <p>
 * Title: Thumbnail
 * </p>
 * 
 * <p>
 * Description: Picture Thumbnail
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 54powerman@163.com 2005
 * </p>
 * 
 * <p>
 * Company: http://blog.sina.com.cn/u1055000490
 * </p>
 * 
 * @author 54powerman
 * @version 1.0
 */
public class Thumbnail {
	private String srcFile;
	private String destFile;
	private int width;
	private int height;
	private Image img;

	boolean watermark=false;
	
	public boolean isWatermark() {
		return watermark;
	}

	public void setWatermark(boolean watermark) {
		this.watermark = watermark;
	}


	private static Logger logger = Logger.getLogger(Thumbnail.class);
	public static void main(String[] args) throws Exception {
		Thumbnail thum = new Thumbnail("d:/myimg.png");
		thum.resizeFix(100, 100);
		//System.out.println("success!!!");
	}

	public Thumbnail(String fileName) throws IOException {
		File _file = new File(fileName); // 读入文件
		this.srcFile = _file.getAbsolutePath();
		this.destFile = this.srcFile
				.substring(0, this.srcFile.lastIndexOf(".")) + "_s.jpg";
		//System.out.println(this.destFile);
		img = javax.imageio.ImageIO.read(_file); // 构造Image对象
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
	}
	public Thumbnail(File f) throws IOException {
		File _file = f; // 读入文件
		this.srcFile = _file.getAbsolutePath();
		this.destFile = this.srcFile
				.substring(0, this.srcFile.lastIndexOf(".")) + "_s.jpg";
		System.out.println(this.destFile);
		img = javax.imageio.ImageIO.read(_file); // 构造Image对象
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
		logger.info("width:"+width+" height:"+height);
	}
	public void resize(int w, int h) throws IOException {
		BufferedImage _image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		_image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		//logger.info(destFile);
		//_image.
		if(watermark){
			logger.info("begin watermark .......");
			ImageOperation.Imagese(_image, destFile,  (float)0.9);
		}
			
		else{
			FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(_image); // 近JPEG编码
			out.close();			
		}
		
		
		
	}

	public void resize(double t) throws IOException {
		int w = (int) (width * t);
		int h = (int) (height * t);
		resize(w, h);
	}

	public void resizeByWidth(int w) throws IOException {
		//logger.info("resize by w");
		int h = (int) (height * w / width);
		logger.info("resize by w "+w+" "+h);
		resize(w, h);
	}

	public void resizeByHeight(int h) throws IOException {
		//logger.info("resize by h");
		int w = (int) (width * h / height);
		logger.info("resize by h "+w+" "+h);
		resize(w, h);
	}

	public void resizeFix(int w, int h) throws IOException {
		if (width / height > w / h) {
			resizeByWidth(w);
		} else {
			resizeByHeight(h);
		}
	}
	public void resizeFix(int w, int h,int i) throws IOException {
		//this.srcFile = _file.getAbsolutePath();
		//this.watermark=watermark;
		this.destFile = this.srcFile
				 + "s"+i+".jpg";

		
		if (width / height > w / h) {
			resizeByWidth(w);
		} else {
			resizeByHeight(h);
		}
	}
	public void setDestFile(String fileName) throws Exception {
		if (!fileName.endsWith(" .jpg ")) {
			throw new Exception(" Dest File Must end with \" .jpg\" . ");
		}
		destFile = fileName;
	}

	public String getDestFile() {
		return destFile;
	}

	public int getSrcWidth() {
		return width;
	}
	
	
	/**
	 * 忽略处理的资源扩展名
	 */
	protected static final Set<String> extNameExcludes = new HashSet<String>();
	static {
		extNameExcludes.add("jpg");
		extNameExcludes.add("jpeg");
		extNameExcludes.add("gif");
		extNameExcludes.add("png");
		extNameExcludes.add("bmp");
		extNameExcludes.add("ico");
		
	}
	/**
	 * 是否是图片
	 * @return
	 */
	public static boolean isPicture(String filename){
		String _uri = filename.trim().toLowerCase();
		int index = _uri.lastIndexOf(".");
		if (index != -1) {
			String ext_name = _uri.substring(index + 1);
			if (extNameExcludes.contains(ext_name)) {
				//logger.info("ignored ... ");
				//filterChain.doFilter(hRequest, hResponse);
				return true;
			}
		}
		return false;
		
		
	}
	public int getSrcHeight() {
		return height;
	}
	
	
}