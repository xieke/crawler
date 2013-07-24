package sand.actionhandler.basic;



import java.io.File;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import sand.actionhandler.system.ActionHandler;

import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.annotation.TokenCheck;
import sand.basic.Global;
import sand.chart.BarChartDemo;
import sand.chart.PieChartDemo;
import sand.depot.business.document.Document;
import sand.depot.business.system.Employee;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.InfoException;
import sand.depot.tool.system.SystemKit;
import sand.mail.MailServer;
import tool.crypto.Crypto;
import tool.dao.BizObject;
import tool.dao.JDO;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;
import tool.ntlm.NTLM;
import tool.taglib.html.TokenTag;

/**
 * <p>
 * 
 * Title: 数据字典操作处理
 * </p>
 * <p>
 * Description: 数据字典操作处理
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SAND
 * </p>
 * 
 * @author wang.yc
 * @version 1.0
 */
public class UserCenter extends ActionHandler {
	
	//private DTaskService dTaskService;
	
	private static Logger logger = Logger.getLogger(UserCenter.class);

	private int flag = 0;// 表明调动ERP用户的修改页面还是门禁用户的修改页面
	//private TaskMemberService taskMemberService;
	/**
	 * 构造方法
	 * 
	 * @param req
	 * @param res
	 */
	public UserCenter(HttpServletRequest req, HttpServletResponse res) {

		super(req, res);
		this._objType = "EMPLOYEE"; // 本操作针对表EMPLOYEE
		this._moduleId = "system"; // 本模块为基本设置
	}
	
	

	/**
	 * 修改密码
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void modifyPwd() throws ServletException, SQLException {
		this.pushLocation("密码修改","/basic.UserCenter.modifyPwd");
		BizObject userBiz = this.getBizObjectFromMap("EMPLOYEE");

		if (Employee.isValidUserDes(userBiz).equals(Employee.VALID)) {
			Crypto crypto = new Crypto();
			try {
				userBiz.set("password", crypto.des(this._request
						.getParameter("newPwd")));
			} catch (Exception e) {
				e.printStackTrace();
				throw new ControllableException("加密过程出现错误");
			}



			this.update(userBiz);
			this.getCurUser().set("PASSWORD",
					this._request.getParameter("newPwd"));
		} else
			throw new ControllableException("旧密码错误！");
		this._tipInfo = "密码修改成功！";
		this._nextUrl = "/template/basic/msg.jsp";
	}
	

//	/**
//	 * 账号设置
//	 * 
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	public void accountSettings() throws ServletException, SQLException {
//		this.setAttribute("obj",_curuser);
//		this._nextUrl = "/template/user_center/account_settings.jsp";
//	}

	public void showOne() throws SQLException{
		
		BizObject obj=new BizObject("employee");
		obj.setID(this._curuser.getId());
		obj.refresh();	
		this._request.setAttribute("employee", obj);
		this._request.setAttribute("from",this.getParameter("from"));
		logger.info("from is "+this.getParameter("from"));
		this._nextUrl="/template/basic/userOper.jsp";

	}
	//Validate validate=new Validate();	
	// 保存user
	
	public void save() throws ServletException, SQLException {
		
	
		BizObject emp = this.getBizObjectFromMap("EMPLOYEE");
		String  str="";
		System.out.println("emp:"+emp);
		if(!emp.getString("telno").equals("")){
			//验证手机号	
			 // str=validate.validateTel2(emp.getString("telno"));
			  System.out.println("str:"+str);
			  if(!str.equals("3003"))
				  throw new InfoException("手机号格式不对，请核对！");
		}
		if(!emp.getString("email").equals("")){
			//验证email
			//  str=validate.validateEmail2(emp.getString("email"));
			  if(!str.equals("4003"))
				  throw new InfoException("邮箱格式不对，请核对！");
		}
		
				  
		
		    
				  System.out.println("str:"+str);

		List<File> files = this.getUploadFiles();
		File file = null;
		this.getJdo().beginTrans();
		if (files.size()>0){
			
			BizObject annex=new BizObject("annex");
			List<BizObject> v =annex.getQF().query("bizid",_curuser.getId());
			if(v.size()>0)
				annex=v.get(0);
			file = files.get(0);			
			//BizObject annex=new BizObject("annex");
			annex.set("bizid",this._objId);
			annex.set("type", Document.TYPE_USERPIC);
			Document.updateAnnex(annex, file,1);			
			emp.set("photoid", annex.getId());
			
		}
		
			
		this.checkParam(emp);
		this.clearEmptyParam(emp);
		//this
		this.getJdo().addOrUpdate(emp);
		
	
		this._objId = emp.getId();
		this.getJdo().commitAll();
		
		//得到替换部门的id
//		String deptId=this.getParameter("EMPLOYEE$deptid");
//		if(!deptId.equals("")){
//			this.replaceDeptsOfUser(this.objId, deptId);
//		}
//		
		this.clearQueryParam();
		// this.listUser();
		this.showOne();

	}
	@Ajax
	public String saveUser() throws ParseException, SQLException{
		
		
		BizObject emp = this.getBizObjectFromMap("EMPLOYEE");
		System.out.println("file:"+this.getParameter("FILE(0)"));
		List<File> files = this.getUploadFiles();
		File file = null;
		this.getJdo().beginTrans();
		if (files.size()>0){
			System.out.println("11111111111111:"+emp);
			BizObject annex=new BizObject("annex");
			List<BizObject> v =annex.getQF().query("bizid",_curuser.getId());
			if(v.size()>0)
				annex=v.get(0);
			file = files.get(0);			
			//BizObject annex=new BizObject("annex");
			annex.set("bizid",this._objId);
			annex.set("type", Document.TYPE_USERPIC);
			Document.updateAnnex(annex, file,1);			
			emp.set("photoid", annex.getId());
			
		}
		this.checkParam(emp);
		this.clearEmptyParam(emp);
		this.getJdo().addOrUpdate(emp);
		
	
		this._objId = emp.getId();
		this.getJdo().commitAll();
		return "[{respCode:'0000', respMsg:'修改用户个人信息成功'}]";
		
		
		
	}



	
}