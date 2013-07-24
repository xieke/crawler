package sand.actionhandler.basic;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.sql.SQLException;
import java.io.IOException;
import tool.dao.*;
import tool.dao.util.Base64;
import tool.dao.util.Encoder;
import tool.crypto.*;
import sand.depot.tool.system.*;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.depot.business.system.*;

import java.util.*;


import org.apache.log4j.Logger;

/**
 * <p>
 * Title: 一些公用功能
 * </p>
 * <p>
 * Description: 一些公用功能
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
@AccessControl("no") //不需要访问控制
public class PublicActionHandler extends ActionHandler {
	private Logger logger = Logger.getLogger(UserAH.class);

	/**
	 * 构造方法
	 * 
	 * @param req
	 * @param res
	 */
	public PublicActionHandler(HttpServletRequest req, HttpServletResponse res) {

		super(req, res);
		this._objType = "EMPLOYEE"; // 本操作针对表DICTIONARY
		this._moduleId = "all"; // 本模块为任意权限
	}

	/**
	 * 修改密码
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void modifyPwd() throws ServletException, SQLException {

		BizObject user = this.getBizObjectFromMap("EMPLOYEE");
		//UserData user = new UserData();
		//user.setUserId(userBiz.getString("userId"));
		//user.setPassword(userBiz.getString("password"));

		if (Employee.isValidUserDes(user).equals(Employee.VALID)) {
			Crypto crypto = new Crypto();
			try {
				
				String pass = crypto.des(this._request.getParameter("newPwd"));
				user.set("pwdtype","des");
				user.set("password", pass);
				
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new ControllableException("加密过程出现错误");
			}

			
			getJdo().update(user);
			
			
			logger.info("updated user "+user);
			this.getCurUser().set("password",
					this._request.getParameter("newPwd"));
		} else
			throw new ControllableException("旧密码错误！");
//		this._tipInfo = "密码修改成功！";
//		this._nextUrl = "success.jsp";
		this._request.setAttribute("nextUrl","basic.HomeAH.index");
		throw new SuccessException("密码修改成功!");
	}

	/**
	 * 
	 * 退出重新登陆
	 */
	public void exit() {
		_request.getSession().setAttribute("userInfo", null);
		_request.getSession().setAttribute("user", null);
		this._nextUrl = "domainlogin.jsp";

	}

	public void test() {
		try {
			logger.debug("to index.jsp");
			_request.getRequestDispatcher("index.jsp").forward(_request,
					_response);
			logger.debug("after index");
			// this.wait(3000);
			for (int i = 0; i < 50; i++) {
				logger.debug("i " + i);
				// this.wait(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}