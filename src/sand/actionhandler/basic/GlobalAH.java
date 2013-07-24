package sand.actionhandler.basic;

import java.math.BigDecimal;




import java.net.InetAddress;

import java.net.URL;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;




import org.apache.log4j.*;
import org.json.JSONException;




import sand.actionhandler.basic.AjaxAH;
import sand.actionhandler.basic.DocumentAH;

import sand.actionhandler.system.*;
import sand.depot.servlet.system.GeneralHandleSvt;
import sand.depot.tool.system.*;

import sand.depot.business.product.ProductVersion;
import sand.depot.business.system.Employee;
import tool.crypto.Crypto;
import tool.crypto.DiscuzPassport;
import tool.crypto.Encryption;
import tool.dao.*;
import tool.handlestring.StringFormat;
import tool.project.ProjectContext;
import tool.project.service.ProjectUserService;
import tool.taglib.html.TokenTag;
//import tool.uda.task.inter.imp.UTaskServiceImpl;
import sand.actionhandler.*;
import sand.annotation.AccessControl; //import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.export;

import javax.servlet.*;
import javax.servlet.http.*;

import sand.image.RandomGraphic;
import sand.mail.MailServer;
import sand.security.MyEclipseGen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sand.annotation.Log2DB;
import sand.basic.Global;
import sand.basic.GlobalConfig;

//import org.json.*;
//import net.sf.json.*;
/**
 * <p>
 * Title: 全局工具处理类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SAND
 * </p>
 * 
 * @author liustone
 * @version 1.0
 */
@AccessControl("no")
// 不需要访问控制
public class GlobalAH extends ActionHandler {

	/**
	 * 构造方法
	 * 
	 * @param p_Req
	 * @param p_Res
	 */

	public GlobalAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "MODULE"; // 随便写一个了，没有还不行
		this._moduleId = "basic"; // 本模块暂时未定
	}

	static Logger logger = Logger.getLogger(GlobalAH.class);


	// 初始化
	protected void initial() {
	}
	
	@Ajax
	public String getNews(){
		return "outman和灰姐快来实现吧！！！！！！！";
	}

	public void listOnlineUser(){
		this.setAttribute("objList", map2List(Global.onlineMap));
		this._nextUrl="/template/basic/onlineList.jsp";
	}
	
	/**
	 * 注册
	 */
	public void reg(){
		//String path=ActionHandler._context.getRealPath("/");
		if(!this.getParameter("des").equals("")){
			GlobalConfig.writeProperty("des", this.getParameter("des"));	
			throw new InfoException("注册信息已经导入，重启tomcat后生效");
		}
		else
			throw new AlarmException("注册信息未正确填写");
		
	}
	
	public void version(){
		BizObject b= new BizObject();
		b.set("registed", Global.registed);
		b.set("regname", Global.regname);
		b.set("regdate", Global.regdate);
		b.set("maxonline", Global.maxonline);
		String modules="";
		if(Global.moduleMap!=null){
			for(String s:Global.modules){
				if(Global.moduleMap.get(s)!=null){
					modules=modules+s+"   ";
				}
				
			}
			
		}
		b.set("modules", modules);
		
		this.setAttribute("obj", b);
		this._nextUrl="/template/basic/version.jsp";
	}
	
	
	public void createAuthorFile(){
		String authorstr="1";
		if(!this.getParameter("maxonline").equals("")){
			authorstr=this.getParameter("maxonline");
		}
		authorstr=authorstr+",";
		for(String s:Global.modules){
			if(this.getParameter(s).equals("1")){
				//moduleMap.put(s, 1);
				authorstr=authorstr+"1";
			}
			else
				authorstr=authorstr+"0";
		}
		logger.info("author str is "+authorstr);
		BizObject b= new BizObject();
		String regname=this.getParameter("regname");
		String regcode=MyEclipseGen.getSerial(regname, "4");
		b.set("maxonline", this.getParameter("maxonline"));
		b.set("regname", regname);
		b.set("regcode", regcode);
		
		authorstr=authorstr+","+System.currentTimeMillis()+","+regname+","+regcode;
		
		Crypto c = new Crypto();
		
		
		String des=c.des(authorstr);
		//this.setAttribute("des", des);
		b.set("des", des);
		this.setAttribute("obj", b);
		logger.info("str is "+c.deDes(des));
		this.showAuthor();
	}
	//授权界面
	public void showAuthor(){
		this._nextUrl="/template/basic/author.jsp";
	}
	public void kickout(){
		
		String userid=this.getParameter("userid");
        HttpSession session = this._request.getSession();  
        ServletContext application = session.getServletContext();  
        Map<String, HttpSession> sessionMap = (Map<String, HttpSession>)application.getAttribute("sessionMap");  
        HttpSession otherSession = (HttpSession)sessionMap.get(userid);  
        //otherSession.removeAttribute("username");  
        otherSession.invalidate();  
        sessionMap.remove(userid);  
        
        this.listOnlineUser();
	}	
	//map转换成lisy
		public static List  map2List(Map map) {
			List list = new ArrayList();
			Iterator iter = map.entrySet().iterator();  //获得map的Iterator
			while(iter.hasNext()) {
				Entry entry = (Entry)iter.next();
				list.add(entry.getValue());
			}
			return list;
		}
	/**
	 * svn up && ant
	 */
	public void ant(){
		String commandLine = " svn up /root/plm/";
		String commandLine2 = "ant -buildfile /root/plm/build.xml";  
		Runtime runTime = Runtime.getRuntime();  
		Process process;
		int code=-1;
		String ret="";
		try {
			process = runTime.exec(commandLine);
			InputStream fis=process.getInputStream();
			//用一个读输出流类去读
			InputStreamReader isr=new InputStreamReader(fis);
			//用缓冲器读行
			BufferedReader br=new BufferedReader(isr);
			String line=null;
			//直到读完为止
			while((line=br.readLine())!=null) {
				System.out.println(line);
				ret=ret+line+"\r\n";
			
			}
			logger.info(ret);
			process = runTime.exec(commandLine2);
			fis=process.getInputStream();
			//用一个读输出流类去读
			isr=new InputStreamReader(fis);
			//用缓冲器读行
			br=new BufferedReader(isr);
			line=null;
			//直到读完为止
			while((line=br.readLine())!=null) {
				System.out.println(line);
				ret=ret+line+"\r\n";
			
			}
			fis.close();
			isr.close();
			br.close();
			code = process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}  
		this._tipInfo = "Ant 指令运行成功！返回结果为 \r\n"+ret;
		this._nextUrl="/template/basic/msg.jsp?showtime=20";
		  
		
	}

	/**
	 * svn up && ant
	 */
	public void ant2(){}
	
	/**
	 * 取任务类别
	 * @return
	 * @throws SQLException 
	 */
	public  static List<BizObject> getTaskClass() throws SQLException{
		List<BizObject> v = SystemKit.getCachePickList("parenttclass");
		
		//BizObject team = (BizObject)this._request.getAttribute("obj");		
		//List<BizObject> concerns = new ArrayList();		
//		if(team!=null)
//			concerns = team.getList("concern");		
	//	List strconcerns = new ArrayList();
//		Map concernmap = new HashMap();
//		for(BizObject concern:concerns){
//			//1concernmap.put(concern.getString("taskclassid"), arg1)
//			strconcerns.add(concern.getString("taskclassid"));
//		}
		//logger.info("get my list concern "+strconcerns.size());
		for(BizObject b:v){
			List<BizObject> children=SystemKit.getCachePickList(b.getString("id"));
			//logger.info(b.getId()+"parentid"+children.size());
//			for(BizObject bb:children){
//				if(strconcerns.contains(bb.getID())){
//					bb.set("checked", bb.getId());
//					logger.info("set checked "+bb.getId());
//				}
//				else
//					bb.set("checked", "");
//			}
			b.set("children", children);
		}
		return v;
	}
			
	/**
	 * 网站导航
	 * @throws SQLException 
	 */
	public void navigate() throws SQLException{
		List<BizObject> v = SystemKit.getCachePickList("channel");		
		this.setAttribute("channels", v);
		logger.info("channels size is "+v.size());
		List<BizObject> v2 = SystemKit.getCachePickList("parenttclass");
		for(BizObject b:v2){
			List<BizObject> children=SystemKit.getCachePickList(b.getString("id"));
			b.set("children", children);
		}		
		this.setAttribute("classes", v2);
		
		
		this._nextUrl="/inc/sitemap.jsp";
	}

	public void config(){
		throw new InfoException("无需设置");
	}
	
	

	/**
	 * 产品动态
	 * @throws SQLException
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void load() throws SQLException, ParseException, JSONException{
		
		String page=this.getParameter("page");
		//ProjectUserServiceImpl.getProjectIdsByUserId(String user_id)

		//sand.depot.business.product.ProductVersion.queryProduct(String userid)  根据用户取得产品
		List<BizObject> v =ProductVersion.queryProduct(_curuser.getId());
		int ipage=1;
		if(!page.equals("")){
			ipage=Integer.parseInt("page");
		}
		String ptids[]= new String[v.size()];
		int i=0;
		for(BizObject b:v){
			ptids[i]=b.getId();
			i++;					
		}
				
//		this.setAttribute("wydList",Whatyoudo.getMywyd(ptids,"",ipage));
		this._nextUrl = "/template/basic/plugin_load.jsp";
	}
	
//    private ApplicationContext getApplicationContext(ServletConfig servletConfig) {
//        return (ApplicationContext) servletConfig.getServletContext().getAttribute(
//                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
//    }
	/**
	 * 开发动态
	 * @throws SQLException
	 * @throws ParseException
	 * @throws JSONException
	 */
//	public void load1() throws SQLException, ParseException, JSONException{
//		
//		//ApplicationContext a =this.getApplicationContext(this._config);
//		ProjectUserService projectUserService = (ProjectUserService) a.getBean("projectUserService");
//		//ProjectUserServiceImpl.getProjectIdsByUserId(String user_id)
//		List<BizObject> v = projectUserService.getProjectIdsByUserId(_curuser.getId());
//		//sand.depot.business.product.ProductVersion.queryProduct(String userid)  根据用户取得产品
//		String ptids[]= new String[v.size()];
//		int i=0;
//		for(BizObject b:v){
//			ptids[i]=b.getId();
//			i++;					
//		}
//
////		this.setAttribute("wydList",Whatyoudo.getMywyd(ptids,"",ipage));
//		this._nextUrl = "/template/basic/plugin_load.jsp";
//	}	
}