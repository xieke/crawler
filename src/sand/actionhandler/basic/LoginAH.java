package sand.actionhandler.basic;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;



import sand.actionhandler.system.ActionHandler;
import sand.actionhandler.weibo.WeiBoAH;

import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.basic.Global;
import sand.depot.business.system.Employee;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.LoginException;
import sand.depot.tool.system.SystemKit;
import sand.image.RandomGraphic;
import sand.mail.MailServer;

import tool.crypto.Crypto;
import tool.dao.BizObject;
import tool.dao.JDO;
import tool.dao.QueryFactory;
import tool.dao.UidGenerator;
import tool.taglib.html.TokenTag;
import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.http.AccessToken;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

//import tool.uda.task.inter.imp.UTaskServiceImpl;

//import org.json.*;
//import net.sf.json.*;
/**
 * <p>
 * Title: 工具处理类
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
public class LoginAH extends ActionHandler {

    public static String SSOServiceURL= "";  
    public static String SSOLoginPage= "";  


	/**
	 * 构造方法
	 * 
	 * @param p_Req
	 * @param p_Res
	 */

	public LoginAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "MODULE"; // 随便写一个了，没有还不行
		this._moduleId = "basic"; // 本模块暂时未定
	}

	static Logger logger = Logger.getLogger(LoginAH.class);

	private static final String JEPASS = "erplawson";

	// 验证码图片的宽度。
	private int width = 80;

	// 验证码图片的高度。
	private int height = 30;

	// 验证码字符个数
	private int codeCount = 4;

	private int x = 0;

	// 字体高度
	private int fontHeight;

	private int codeY;

	char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	// 初始化
	protected void initial() {

		
		try {
			SSOServiceURL=SystemKit.getCacheParamById("system_core", "rop_url")+"/router";
			SSOLoginPage=SystemKit.getCacheParamById("system_core", "www_url");
			
			logger.info("SSOServiceURL  "+SSOServiceURL);
			logger.info("SSOLoginPage  "+SSOLoginPage);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 从web.xml中获取初始信息
		// 宽度
		String strWidth = this.getParameter("width");
		// 高度
		String strHeight = this.getParameter("height");
		// 字符个数
		String strCodeCount = this.getParameter("codeCount");

		// 将配置的信息转换成数值
		try {
			if (strWidth != null && strWidth.length() != 0) {
				width = Integer.parseInt(strWidth);
			}
			if (strHeight != null && strHeight.length() != 0) {
				height = Integer.parseInt(strHeight);
			}
			if (strCodeCount != null && strCodeCount.length() != 0) {
				codeCount = Integer.parseInt(strCodeCount);
			}
		} catch (NumberFormatException e) {
		}

		x = width / (codeCount + 1);
		fontHeight = height - 2;
		codeY = height - 4;
	}

	/**
	 * 返回[from,to)之间的一个随机整数
	 * 
	 * @param from
	 *            起始值
	 * @param to
	 *            结束值
	 * @return [from,to)之间的一个随机整数
	 */
	protected int randomInt(int from, int to) {
		Random r = new Random();
		return from + r.nextInt(to - from);
	}

	/**
	 * 生成检验图片
	 * 
	 * @throws Exception
	 */
	public void createVerifyImage() throws Exception {

		this._dispatched = true;

		// // 定义图像buffer
		// BufferedImage buffImg = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_RGB);
		// Graphics2D g = buffImg.createGraphics();
		//
		// // 创建一个随机数生成器类
		// Random random = new Random();
		//
		// // 将图像填充为白色
		// g.setColor(Color.WHITE);
		// g.fillRect(0, 0, width, height);
		//
		// // 创建字体，字体的大小应该根据图片的高度来定。
		// Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// // 设置字体。
		// g.setFont(font);
		//
		// // 画边框。
		// g.setColor(Color.BLACK);
		// g.drawRect(0, 0, width - 1, height - 1);
		//
		// // 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
		// g.setColor(Color.BLACK);
		// for (int i = 0; i < 60; i++) {
		// int x = random.nextInt(width);
		// int y = random.nextInt(height);
		// int xl = random.nextInt(12);
		// int yl = random.nextInt(12);
		// g.drawLine(x, y, x + xl, y + yl);
		// }

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		// StringBuffer randomCode = new StringBuffer();
		// int red = 0, green = 0, blue = 0;
		//
		// // 随机产生codeCount数字的验证码。
		// for (int i = 0; i < codeCount; i++) {
		// // 得到随机产生的验证码数字。
		// String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
		// // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
		// red = random.nextInt(255);
		// green = random.nextInt(255);
		// blue = random.nextInt(255);
		//
		// // 用随机产生的颜色将验证码绘制到图像中。
		// //g.setColor(new Color(red, green, blue));
		//
		// //int ypos = randomInt(height+fontHeight,height+fontHeight*2);
		// //g.drawString(strRand, (i + 1) * x, ypos);
		//
		// // 将产生的四个随机数组合在一起。
		// randomCode.append(strRand);
		// }

		RandomGraphic rg = RandomGraphic.createInstance(5);
		String charValue = rg.randAlpha();

		// 将四位数字的验证码保存到Session中。
		HttpSession session = _request.getSession();
		session.setAttribute("validateCode", charValue);

		// 禁止图像缓存。
		_response.setHeader("Pragma", "no-cache");
		_response.setHeader("Cache-Control", "no-cache");
		_response.setDateHeader("Expires", 0);

		_response.setContentType("image/jpeg");

		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = _response.getOutputStream();
		rg.drawAlpha(charValue, RandomGraphic.GRAPHIC_PNG, sos);
		// ImageIO.write(buffImg, "jpeg", sos);
		sos.close();
	}

	@Ajax
	public String verifyCode() {

		// _response.setContentType("text/html;charset=utf-8");
		String validateC = (String) _request.getSession().getAttribute(
				"validateCode");

		/**
		 * 如果 session 中没有验证码信息，说明没有生成验证码图片自动放行
		 */
		if (validateC == null || validateC.equals("")) {
			logger.info("did not create validate code img");
			return "ok";
		}
		String veryCode = "";
		if (this.getBizObjectFromMap("employee") != null)
			veryCode = this.getBizObjectFromMap("employee").getString(
					"verifycode");
		// PrintWriter out = _response.getWriter();
		String result = "";
		logger.info("vercode is " + veryCode + "  validateC is " + validateC);
		if (veryCode == null || "".equals(veryCode)) {
			result = "验证码为空";
		} else {
			if (validateC.equals(veryCode)) {
				result = "ok";
			} else {
				result = "验证码错误";
			}
		}
		if (!result.equals("ok")) {
			//throw new ControllableException(result);
		}
		return result;
		// BigDecimal b ;
		// b.intValue()
	}

	public void active() throws SQLException {

		BizObject user = new BizObject("employee");
		user.set("activecode", this.getParameter("code"));
		List<BizObject> v = user.getQF().query(user);
		if (v.size() == 1) {
			user = v.get(0);
			if (user.getString("state").equals("-1")) {

				/**
				 * 如果是个人用户，无须审核
				 */
				if (user.getString("type").equals("0")) {
					user.set("state", 1);
				}
				/**
				 * 如果是企业用户，未审核
				 */
				if (user.getString("type").equals("1")) {
					user.set("state", 0);
				}
				this.getJdo().update(user);

				this._tipInfo = "你已成功激活uda账号";
				this._nextUrl = "/template/basic/msg.jsp";

			} else {
				throw new ControllableException("该用户已经激活");
			}
		} else {
			throw new ControllableException("无效的激活码");
		}

	}


	
	public void createEnterpriseUser() throws SQLException {
		this.createUser();
		// List<BizObject> v = GlobalAH.getTaskClass();
		// logger.info("显示数量为："+v.size());
		// _request.setAttribute("classes", v);

		this._nextUrl = "/user/reg_enterprise.jsp";
	}

	public void createUser() throws SQLException {
		List<BizObject> v = GlobalAH.getTaskClass();
		logger.info("显示数量为：" + v.size());
		_request.setAttribute("classes", v);

		this._nextUrl = "/user/reg.jsp";
	}

	/**
	 * 注册
	 * 
	 * @throws SQLException
	 */
	public void register() throws SQLException {
		BizObject user = this.getBizObjectFromMap("employee");
		if (this.verifyCode().equals("1")) {
			BizObject u = new BizObject("employee");
			u.set("loginname", user.getString("loginname"));
			QueryFactory qf = u.getQF();
			//设置过滤条件，非注销用户
			qf.setHardcoreFilter("scrap!=0 or scrap is null ");
			if (qf.query(u).size() > 0)
				throw new ControllableException("对不起,"
						+ user.getString("loginname") + "已经被人注册了！");
			u.clear();
			u.set("email", user.getString("email"));
			if (qf.query(u).size() > 0)
				throw new ControllableException("对不起,"
						+ user.getString("email") + "已经被人注册了！");

			user.set("state", "-1");// 未激活
			user.set("activecode", TokenTag.generateToken(user
					.getString("loginname")));
			user.set("photoid", "ANNE14514412");

			user.set("UDAGOLD",0);
			user.set("UDASCORE",0);
			user.set("LOGINCOUNT",0);
			user.set("TOTALTASK",0);
			user.set("SUCCESSTASK",0);
			user.set("GOODCOMMENT",0);
			user.set("TOTALSUM",0);
			user.set("FOOTSTEP",0);
			user.set("TOTALPUBTASK",0);
			user.set("UDAABILITY",0);
			user.set("RANKING",0);
			
			Employee employee = new Employee();
			employee.setEmployee(user);
			List<BizObject> roles = new ArrayList();
			BizObject role = new BizObject("role");

			/**
			 * 如果是个人用户，无须审核
			 */
			if (user.getString("type").equals("0")) {
				// user.set("state", 1);
				role.setID("person");
				roles.add(role);
			}
			/**
			 * 如果是企业用户，未审核
			 */
			if (user.getString("type").equals("1")) {

				role.setID("enterprise");
				roles.add(role);
			}

			employee.setRoles(roles);
			employee.save();

			List<BizObject> v = this.getBizObjectWithType("concern");
			logger
					.info("v size is " + v.size() + " user id is "
							+ user.getId());
			if (v != null) {
				// 删除所有擅长
				this.getJdo().resetObjType("concern");
				int i = this.getJdo().delete("userid", user.getId());
				logger.info("delete " + i);
			}

			String concernstr = "";
			logger
					.info("v size is " + v.size() + " user id is "
							+ user.getId());

			for (BizObject concern : v) {
				concern.set("userid", user.getId());
				concernstr = concernstr + concern.getString("taskclassid")
						+ ",";
				this.getJdo().addOrUpdate(concern);
			}
			user.set("concern", concernstr);
			this.getJdo().update(user);

			MailServer.sendMail(user, null, "完成  注册", "请完成注册",
					"<a href='http://116.236.224.52:7989/basic.LoginAH.active?code="
							+ user.getString("activecode")+"'>激活账号</a>");// .sendMail(user,
			// "", "完成uda网注册",
			// "");
			logger.info("active code is " + user.getString("activecode"));
			// this.getJdo().add(user);

		}

		else
			throw new ControllableException("您的验证码不正确");

		String mail=user.getString("email");
		String mailaddress="http://mail."+mail.substring(mail.indexOf('@')+1);
		this._tipInfo = "注册确认信已经发往您的邮箱，请点击<a href='"+mailaddress+"'> 这里  </a>查收";
		this._nextUrl = "/template/basic/msg.jsp?showtime=3";
	}

	public void showAdminCenter() throws SQLException {
		this.setAttribute("channels", DocumentAH.getChannel());
		this._request
				.setAttribute(
						"waitverify",
						new QueryFactory("employee")
								.executeQuerySQL(
										"select * from basic.employee where type=1 and state=0")
								.size());
		this._nextUrl = "/plm.admin/menu.jsp";
	}

	/**
	 * 丢失密码
	 * 
	 * @throws SQLException
	 */
	public void lostPasswd() throws SQLException {
		BizObject user = this.getBizObjectFromMap("employee");
		if (this.verifyCode().equals("1")) {
			List a=user.getQF().query(user);
			if(a.size()>0)
			{
			user = (BizObject) user.getQF().query(user).get(0);
			// MailServer.sendMail(user, cc, subject, message, attaches);
			if (user != null)
			{
				Crypto crypto = new Crypto();
				String ln=user.getString("loginname");
			//	System.out.println("ln    "+ln+" l "+ln.length()+"  "+user.getString("EMAIL"));
				String b=ln.substring(0,1);
				String e=ln.substring(ln.length()-1,ln.length());
				String t="*";
				for(int i=0;i<ln.length()-2;i++)
					t+="*";
				
				MailServer.sendMail(user, "", "plm密码提醒", "您的登录账号是 "+b+t+e+"\n密码是 "+crypto.deDes(user.getString("Password"))+"\n请注意保管好您的密码");
				this._tipInfo = "您的密码已经发送到你的信箱里去了";
			
			}
			else
				this._tipInfo = "对不起，您的账户没有找到";
			}
			else
				this._tipInfo = "对不起，您的账户没有找到";
			this._nextUrl = "/template/basic/msg.jsp";
			// int i = loginImpl(user);
		}

	}

	public void index() {
		this._nextUrl = "/demo_index.html";
	}

	/**
	 *  登陆
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @ajax
	 */
	// @Ajax
	public void login() throws SQLException, IOException {

		_nextUrl = "/basic.UserActionHandler.showUserCenter";
		_nextUrl = "/basic.HomeAH.index";
		BizObject user = this.getBizObjectFromMap("employee");
		//String i = loginImpl(user);
		String result = loginImpl(user);
		
		logger.info("user  is "+user);
		
		logger.info("~~~~~~~~~~~~~~~~~~~~~ result is "+result);
		//user.set("cookieid", sign);
		if(!result.equals(Employee.VALID)){
			_tipInfo=result;
			_nextUrl = "/login.jsp";
			return;
		}
		else
			_nextUrl = "/basic.HomeAH.index";
		
		/*
		if (user != null) {
			if (this.verifyCode().equals("1")) {

				int i = loginImpl(user);
			} else
				throw new ControllableException("验证码不正确~~");

		}
		*/

	}

	//static private ConcurrentMap SSOIDs ;  
//	public static String CookieName="WangYuDesktopSSOID";
	 //String cookiename="WangYuDesktopSSOID"; 
	 String domainname="sand";
	    
//	 public void ssoEntrance() throws SQLException{
//		 _nextUrl="/";
//		 if(this._curuser!=null)
//			 return;
//		 
//			SSOServiceClient ssoc = new SSOServiceClient(SSOServiceURL);    	
//			logger.info("cookied is "+this.getParameter("cookieid"));
//			SSOResponse ssoresponse = ssoc.auth(this.getParameter("cookieid"));//httpget.getResponseBodyAsString();  
//	        
//	        String result="nobody";
//	        if(ssoresponse!=null)
//	        	result=ssoresponse.getLoginname();
//	        
//	            //检查认证结果
//	            System.out.println("result is "+result);
//	            if (result.equals("nobody")) { //效验失败或没有找到cookie，则需要登录  
//	                throw new ControllableException("对不起，单点登录失败");  
//	                
//	            } else   {//效验成功  
//	              //  request.setAttribute("SSOUser",result);
//	            	BizObject user =new BizObject("employee");
//	            	user.set("loginname", result);
//	            	user = user.getQF().getOne(user);
//	            	//this.writeSession(req, user, jdo)
//	            	result = writeSession(_request,user,this.getJdo());
//	            	//ssoresponse.setCookieid(cookieid);
//	            	//写入session,转为已登录状态
//	            	//this.loginImpl(user);
//	            	
//	            }
//
//	 }
	 
	 /**后台管理**/
	 @CandoCheck("session")
	 public void admin(){
		 if(_curuser.getString("isadmin").equals("1")||_curuser.getString("loginname").equals("system")){
			 this._nextUrl="/template/basic/admin/index.jsp";
			 
		 }
		 else
			 throw new ControllableException("对不起，您没有管理员权限");
		 
	 }
	 
	 
 /**
  * 接受微博授权
 * @throws WeiboException 
 * @throws IOException 
 * @throws ServletException 
  */
 //@Ajax
 public void weibo() throws WeiboException, ServletException, IOException{
	 	Oauth oauth = new Oauth();
	 	String code=this.getParameter("code");
	 	if(code.equals("")) throw new ErrorException("empty code !!!!");
	 	WeiBoAH.accesstoken =oauth.getAccessTokenByCode(code);
	 	//System.out.println();
	 	this.dispatch("/weibo.WeiBoAH.readWeibo");
	 	//return accesstoken.getAccessToken();
 }
	    /**
	     * sso 跳转
	     */
//	 public void ssoRedirect(){
//		 String url = this.getParameter("url");
//     	SSOServiceClient ssoc = new SSOServiceClient(SSOServiceURL,_curuser.getString("cookieid"));
//     	String ticket = ssoc.getTicket();
//     	String target=this.getParameter("url");
//     	//response.sendRedirect();
//     	this._nextUrl=target+"?cookieid="+ticket;
//     	this._dispatchType=this.DISPATCH_TYPE_REDIRECT;
//     	return; 
//		 
//	 }
	 
	 
	// public String _username;
	 public String _password;
//	public void ssoLogin() throws SQLException {
//		
//		if(_request.getServerPort()==9999){
//
//			//HttpServletRequest request = ServletActionContext.getRequest();  
//	       // HttpServletResponse response=ServletActionContext.getResponse();  
//	        
//			this._nextUrl="http://10.38.128.105";
//			this._nextUrl=SystemKit.getParamById("system_core","ssl_url");
//			
//			this._dispatchType=this.DISPATCH_TYPE_REDIRECT;
//			logger.info("server port is "+_request.getServerPort()+"  , not allowed login");
//			return;
//		}
//
//		Cookie cookie = new Cookie("JSESSIONID", _request.getSession().getId());  
//        _response.addCookie(cookie);  
//
//		//this.PAGE_SCHEMA="hna";
//		this.deCrypt();
//		
//		BizObject user = this.getBizObjectFromMap("employee");
//		String gotoUrl =this.getParameter("gotoUrl");
//		//logger.info("goto url is "+this.getParameter("gotoUrl"));
//		
//		if(gotoUrl.equals("")){
//			gotoUrl=user.getString("gotourl");
//		}
//		else
//			user.set("gotourl", gotoUrl);
//		
//		this.setAttribute("user",user);
//		
//		if (!this.verifyCode().equals("ok")) {
//			throw new LoginException("验证码不正确~~");
//		}
//
//		if(SSOAuthService.SSOIDs==null)
//			SSOAuthService.SSOIDs = new ConcurrentHashMap();  
//
//		if(SSOAuthService.SSOUSERs==null)
//			SSOAuthService.SSOUSERs = new ConcurrentHashMap();  
//
//		
//		//_nextUrl="http://10.38.128.105:5577/basic.HomeAH.index";
//		
//		if(_request.getServerPort()==9443){
//			_nextUrl = SystemKit.getParamById("system_core","www_url");	
//		}
//
//		_nextUrl = "/";
//		//this._dispatchType=this.
//			
//		logger.info("next url is "+_nextUrl);
//		
//		
//		
//		user.set("loginname",this.getParameter("username"));
//		user.set("password", _password);
//		String result = loginImpl(user);
//		
//		logger.info("user  is "+user);
//		
//		logger.info("~~~~~~~~~~~~~~~~~~~~~ result is "+result);
//		//user.set("cookieid", sign);
//		if(!result.equals(Employee.VALID)){
//			_tipInfo=result;
//			return;
//		}
//		else
//			_nextUrl = "/basic.HomeAH.index";
//		//user.refresh();
//		logger.info("next url is "+_nextUrl);
//		//String gotoURL = _request.getParameter("gotoUrl");          
//
////        Map<String,String> idMap = new HashMap();
////        idMap.put("cookieid",newID);
//        
//       // String secret =new  SampleAppSecretManager().getSecret("00001");
//   //     String sign=RopUtils.sign(idMap, secret);
//		String sign = UidGenerator.getUUId();  
//        SSOAuthService.SSOIDs.put(sign, user.getString(user.getString("logincolumn")));  
//        SSOAuthService.SSOUSERs.put(user.getString(user.getString("logincolumn")), user);
//        
//        System.out.println("SSOIDs "+SSOAuthService.SSOIDs);
//        String url=this.getParameter("gotoUrl");
//         
//        if(url.equals("")){  //是给自己用的
//        	try{
//             	SSOServiceClient ssoc = new SSOServiceClient(SSOServiceURL);         	
//             	SSOResponse ssoresponse =ssoc.auth(sign);  //自己认证一下自己，在 sso服务器生成session
//             	
//             	this.getCurUser().set("cookieid", ssoresponse.getCookieid());//把sessionid 保存起来
//        		
//				//BizObject user=(BizObject)request.getSession(false).getAttribute("curuser");
//        	}
//        	catch(Exception e){
//        		e.printStackTrace();
//        		logger.error("error",e);
//        	}
//        	logger.info("loginname length ......................................................................... "+this.getCurUser().getString("loginname").length());
//        	
//        	if(this.getCurUser().getString("logincolumn").toLowerCase().equals("identifier")){   //身份证做登录名的要去完善用户名
//        		_nextUrl="/basic.UserCenter.showOne?from=login";	
//        	}
////			if(this.getCurUser().getString("loginname").length()==18){ 
////				
////			}             	
//        	
//        	
//        }else
//		if(!url.equals("")){
//			//if (url.indexOf("?") != -1) // 是否url中有?号，根据这个条件判断 tipInfo
//			if(url.substring(url.length()-1,url.length()).equals("/")){
//				_nextUrl=url.substring(0,url.length()-1)+"?cookieid="+sign;
//			}				
//			else if(url.indexOf("?")!=-1){
//				//if (_nextUrl.indexOf("?") != -1) // 是否url中有?号，根据这个条件判断 tipInfo
//				
//					_nextUrl=url+"&cookieid="+sign;
//			}
//			else
//				_nextUrl=url+"?cookieid="+sign;
//			
//			this._dispatchType=this.DISPATCH_TYPE_REDIRECT;
//		}        
//        System.out.println(url +"   login success, goto back url:" + _nextUrl+"&cookieid="+sign+"   "+this._dispatchType);  
//
//
//
//	}
	@Ajax
	public String ajaxLogin() throws SQLException, IOException {

		_nextUrl = "/basic.UserActionHandler.showUserCenter";
		BizObject user = this.getBizObjectFromMap("employee");
		String i = loginImpl(user);
		logger.info("return login "+i);
		return i+"";
		/*
		if (user != null) {
			if (this.verifyCode().equals("1")) {

				int i = loginImpl(user);
			} else
				throw new ControllableException("验证码不正确~~");

		}
		*/

	}
	
	/**
	 * 这里的user2参数是作为出参用的
	 * @param user2
	 * @return
	 * @throws SQLException
	 */
	public String loginImpl(BizObject user2) throws SQLException {

		// logger.info("url is 1");
		System.out.println("in Login");

		String result = "0";

		QueryFactory userQuery = new QueryFactory("employee");
		// BizObject user = new BizObject();
		// // logger.info("url is 2");
		// user.set("loginname", userId);
		// user.set("Password", password);
		String url = null;
		String msg = null;
		// System.out.println("loginname " + userId);
		// System.out.println("password " + password);

		// _request.setAttribute("domainuser", domainuser);

		BizObject user=user2;
		result = Employee.isValidUserDes(user);
		if (result.equals(Employee.VALID) ) {
			// if (Employee.isValidUser(user)) {
//			_nextUrl = "/basic.UserActionHandler.showUserCenter?userid="
//					+ user.getId();
			// this.
			//String cookieid=user.getString("cookieid");
//			user = (BizObject) userQuery.query("loginname",
//					user.getString("loginname")).get(0);
		//	user.set("cookieid", cookieid);
			//Employee.getRank(user);
		//	user2.setID(user.getId());
		//	user2.du
			// logger.info("user footstep is "+user.getString("footstep"));
			result = writeSession(_request,user,this.getJdo());

		} else {
			logger.info("result is " + result);
			if (result.equals(Employee.ERROR_APPLY)) {
				_tipInfo = "请点击发送到您邮箱的地址来激活帐号";
			} else if (result.equals(Employee.ERROR_FIRED)) {
				_tipInfo = "该用户已经注销";
			} else
				_tipInfo =result;
			// 用户名或密码错误，应提示重新登录
			//_nextUrl = "/ssologin.jsp";

			//logger.info(_tipInfo);
			//System.out.println(msg);
			//logger.info("url is " + url);
			//result = Employee.ERROR_NOVALID;
		}
		_request.setAttribute("errMsg", msg);
		return result;
		// _request.getRequestDispatcher(url).forward(_request, _response);

	}

	public static String writeSession(HttpServletRequest req,BizObject user,JDO jdo) throws SQLException {
		if (user == null) {
			return Employee.ERROR_NOBUND;// 未绑定erp
		} else if (user.getString("scrap").equals("0")) {
			return Employee.ERROR_FIRED;// 该用户已经离职
		} else {

		//	Crypto crypto = new Crypto();

			// user.set("fieldpasswd", crypto.des(password));
			// .getDay();
			/**
			 * 计算 连续登陆天数
			 */
			Calendar c = Calendar.getInstance();

			if (user.getDate("lastlogindate") != null) {
				c.setTime(user.getDate("lastlogindate"));
				Calendar c2 = Calendar.getInstance();
				c.add(c.DATE, 1);
				logger.info("----------------------lastlogin " + c.get(c.DATE)
						+ "  thislogin " + c2.get(c.DATE));
				logger.info("footstep is " + user.getString("footstep"));
				if (c.get(c.DATE) == c2.get(c.DATE))
					user.set("logincount", user.getInt("logincount", 0) + 1);

			}

			user.set("lastlogindate", new Date());
			if(!user.getString("username").equals(""))
				user.set("showname", user.getString("username"));
			else
				user.set("showname", user.getString(user.getString("logincolumn")));
			
			if(user.getId().equals("")){
				user.setID("anonymous");
				jdo.add(user);
			}
			else{
				jdo.update(user);// 保存域密码，以便将来统一域帐户和erp帐户	
			}
			
			// System.out.println("update "+user);
			Employee employee = new Employee(user, jdo);
			employee.refreshById(user.getID());
			employee.setEmployee(user); // 为了保证Action的 curuser 和

			//employee.refreshUserInfo(employee.MY_ALL);

			/**
			 * 如果是客户，不允许登陆
			 */
			if (employee.isRole("KEHU")) {
				return Employee.ERROR_KEHU;
			}

			
			// System.out.println("begin");
			// _request.getSession().setAttribute("user", user);
			
			if(Global.onlineMap.size()>=Global.maxonline){
				logger.info("Global.onlineMap is "+Global.onlineMap);
				return Employee.ERROR_MAX+Global.maxonline;
			}
			req.getSession().setAttribute("employee", employee);
			// _request.getSession().setAttribute("module",
			// employee.get_module());
			// 当前用户(已转换成BizObject对象,以供内部资源网页面使用)

			BizObject curuser = (BizObject) user;
			Global.onlineMap.put(curuser.getId(), curuser);
			// curuser.set("password", crypto.deDes(curuser
			// .getString("password")));
			req.getSession().setAttribute("curuser", curuser);
			// employee.canDo(funId)
			// System.out.println("登陆类中的输出："+employee.get_module());
			System.out.println("set session " + employee.getEmployee().getId());
			logger.info("write session  " + curuser);
			// _//request.getRequestDispatcher("main.jsp").forward(request,
			// response);

			// 设置用户任务数
			// _request.getSession().setAttribute(TaskContext.SESSION_USER_TASK_COUNT_KEY,
			// getTaskService().queryTaskCount(employee.getEmployee().getId()));
			// 楼上的,你在session里设置，这更新可是要退出才能看到的呀,先抄来用用
			// _request.getSession().setAttribute(basicAssisant.SESSION_USER_CLUB_KEY,basicAssisant.allCnt(employee.getEmployee().getId()));
			return Employee.VALID;
		}

		// return Employee.UNKNOW;
	}

	/**
	 * 
	 * 退出重新登陆
	 * @throws SQLException 
	 */
	public void exit() throws SQLException {
		
		if(Global.onlineMap!=null&&_curuser!= null){
			Global.onlineMap.remove(_curuser.getId());
	        System.out.println(_curuser.getString("username")+"已经退出！");  
			
		}

		_request.getSession().removeAttribute("user");
		_request.getSession().removeAttribute("employee");
		_request.getSession().removeAttribute("module");

		// 当前用户(已转换成BizObject对象,以供内部资源网页面使用)

		_request.getSession().removeAttribute("curuser");
		_request.getSession().removeAttribute("userInfo");

		_request.getSession().invalidate(); // 彻底销毁session

		// logger.info("curuser is "+_request.getSession().getAttribute("curuser"));
		
		//以下为本本添加，支持退出转向next_url，为空自动转到根目录
//		String next_url = this.getParameter("next_url");
//			_dispatched=true;
//			String ssl_login=SystemKit.getParamById("system_core", "ssl_url");
//			_response.sendRedirect(next_url.equals("")?ssl_login:next_url);
			
			//if(this.getParameter(//"next_url"))

		if(_request.getServerPort()==9999)
			_nextUrl=SystemKit.getParamById("system_core", "ssl_url");
		else
			_nextUrl="/";
		//this._nextUrl = "/";
	}
	
	/**
	 * 
	 * 退出重新登陆
	 */
	public void center() {
		this._nextUrl = "/template/basic/center.jsp";
	}
	
	//验证用户邮箱并添加或者修改用户邮箱
	public void activemail() throws SQLException {
			
			BizObject validatecode = new BizObject("validatecode");
			validatecode.set("activecode", this.getParameter("activecode"));
			List<BizObject> v = QueryFactory.getInstance("validatecode").query(validatecode);
			String msg="";
				
			String server2 =SystemKit.getParamById("server2", "serverurl2");//国民旅游网登录地址
			String server =SystemKit.getParamById("system_core", "www_url");//服务器地址
			if(StringUtils.isBlank(server))
			{
				msg=msg+"服务器地址未设置\n";
			}else
			{
				
			if (v.size() == 1) {
				BizObject user = v.get(0).getBizObj("userid");
				if (!user.getString("cacheemail").equals("")) {
						user.set("email", user.getString("cacheemail"));//更新邮箱
						user.set("cacheemail", "");//删除激活码
					this.getJdo().update(user);
					msg = "您已成功更新邮箱";
				}
				this.setAttribute("loginname", user.getString("loginname"));
				this.setAttribute("flag", 1);
					
			} else {
				msg = "无效的激活码";
			}
				
			}
				
				
			this.setAttribute("msg", msg);
				
			this._request.setAttribute("server", server);
			this._request.setAttribute("server2", server2);
			this._nextUrl = "/template/real/updateEmail.jsp";

		}	
	public void deCrypt(){
		
	     String local_network = null;
	     String local_disk =null;
	     String local_nic =null;
	     String network=null;
	     String disk=null;
	     String nic=null;
	     String backpage=null;
	     
	     String mcrypt_key_1=(String)_request.getSession(false).getAttribute("mcrypt_key");
	     String username1=_request.getParameter("username").trim();
	     String password1 = _request.getParameter("password").trim();
	     
	   //  _request.set
	     backpage=_request.getParameter("backpage");
	     local_network = _request.getParameter("local_network").trim();//加密后的客户端网卡和MAC信息;    
	     local_disk = _request.getParameter("local_disk").trim();//获取加密后的客户端硬盘序列号;
	     local_nic = _request.getParameter("local_nic").trim();//获取加密后的客户端cpuid号;  
	   
//	   if(mcrypt_key_1 ==null || mcrypt_key_1.equals("") || username1.equals("") || username1 ==null || password1.equals("") || password1 ==null  )
//	     {
//		   throw new LoginException("用户名或密码不可以为空，请重新登录!");
//	       //System.out.print("<script>alert('用户名或密码不可以为空，请重新登录!');window.location.href='./login.jsp';</script>");
//	     //  return;
//	    }
	   
	     if(password1.equals("")||password1.equals("undefined")){
	    	 _password=this.getParameter("password2");
	     }
	     else{
	    	 logger.info("password 1 is   "+password1);
	    	// _password=AESWithJCE.getResult(mcrypt_key_1,password1);//调用解密接口。mcrypt_key_1为获取的32位随机数，password1为密码的密文；	 
	     }
	   
	     logger.info("password is "+_password);

	 //   _username=AESWithJCE.getResult(mcrypt_key_1,username1);//调用解密接口。mcrypt_key_1为获取的32位随机数，username1为用户名密文；
	    
	   
//	    if(local_network!=null) network=AESWithJCE.getResult(mcrypt_key_1,local_network);//调用解密接口.获取网卡信息;
//	    if(local_disk!=null)  disk=AESWithJCE.getResult(mcrypt_key_1,local_disk);//调用解密接口.获取硬盘序列号信息;
//	    if(local_nic!=null)  nic=AESWithJCE.getResult(mcrypt_key_1,local_nic);//调用解密接口.获取cpuid号信息;    
//	        
	//    this._request.getSession(false).invalidate();//清除session;

	}
}