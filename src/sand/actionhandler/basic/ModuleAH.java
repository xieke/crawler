package sand.actionhandler.basic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.io.IOException;
import tool.dao.*;
import tool.crypto.*;
import sand.depot.tool.system.*;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.CandoCheck;
import sand.annotation.TokenCheck;
import sand.depot.business.system.*;

import java.util.*;
import org.apache.log4j.Logger;

/**
 * <p>
 * Title: 模块操作处理
 * </p>
 * <p>
 * Description: 模块操作处理
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SAND
 * </p>
 * 
 * @author xie.ke
 * @version 1.0
 */
public class ModuleAH extends ActionHandler {
	private Logger logger = Logger.getLogger("ModuleActionHandler.logger");

	/**
	 * 构造方法
	 * 
	 * @param req
	 * @param res
	 */
	public ModuleAH(HttpServletRequest req, HttpServletResponse res) {

		super(req, res);
		this._objType = "Module"; // 本操作针对表DICTIONARY
		this._moduleId = "system"; // 本模块为基本设置
	}

	/**
	 * 删除一个模块
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("modulesave")
	public void delModule() throws ServletException, SQLException {
		//this.validateCanDo("moduleoperator");
		Module module = new Module(this._curuser, this.getJdo());
		module.refreshById(objId);
		module.delete();
		this.listModule();

	}

	/**
	 * 添加一个功能点
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("modulesave")
	public void addFunction() throws ServletException, SQLException {
		//this.validateCanDo("moduleoperator");
		BizObject fun = this.getBizObjectFromMap("function");
		fun.set("moduleid", this.objId);
		this.getJdo().add(fun, this._curuser);
		this.showModule();
	}

	/**
	 * 删除一个function
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("modulesave")
	@TokenCheck
	public void delFunction() throws ServletException, SQLException {

		//this.validateCanDo("moduleoperator");
		String funcId = this._request.getParameter("detailObjId");
		logger.debug("funcId is " + funcId);
		BizObject func = new BizObject("function");
		func.setID(funcId);
		func.refresh();
		objId=func.getString("moduleid");
		this.getJdo().delete( func);
		this.showModule();
	}

	// 初始化
	// 传来的objId
	String objId = "";

	protected void initial() {

		if (this._request.getParameter("objId") != null)
			objId = this._request.getParameter("objId");

	}

	/**
	 * 显示一个模块
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showModule() throws ServletException, SQLException {

		Module module = new Module(this._curuser, this.getJdo());
		module.refreshById(objId);
		logger.info("module is "+module.getModule());
		this._request.setAttribute("obj", module.getModule());

		List myFunctions = module.getFunctions();

		this._request.setAttribute("functions", myFunctions);
		this._nextUrl = "/template/basic/moduleOper.jsp";

	}

	/**
	 * 列表显示模块
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void listModule() throws ServletException, SQLException {
		this.setOrderBy("priority");
		this.listObj();

		this._nextUrl = "/template/basic/moduleList.jsp";
	}

	/**
	 * 保存
	 */
	@CandoCheck("modulesave")
	@TokenCheck
	public void save() throws ServletException, SQLException {

		//this.validateCanDo("moduleoperator");
		
		BizObject r = this.getBizObjectFromMap("module");
		this.checkParam(r);
        String str = r.getString("priority");
		if(str.length()==1){
			str="0"+str;
			r.set("priority", str);
		}
			
		List v = this.getBizObjectWithType("function");
		Module module = new Module(this._curuser, this.getJdo());
		module.setFunctions(v);


		module.setModule(r);

		module.save();
		this.objId = module.getModule().getID();
		this.showModule();
		this._tipInfo = "操作成功";

	}

	/**
	 * 测试
	 * 
	 */
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