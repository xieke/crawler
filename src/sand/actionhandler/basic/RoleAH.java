package sand.actionhandler.basic;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.sql.SQLException;
import java.io.IOException;
import tool.dao.*;
import tool.basic.Utility;
import tool.crypto.*;
import sand.depot.tool.system.*;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.CandoCheck;
import sand.depot.business.system.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * <p>
 * Title: 角色操作处理
 * </p>
 * <p>
 * Description: 角色字典操作处理
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
public class RoleAH extends ActionHandler {
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 构造方法
	 * 
	 * @param req
	 * @param res
	 */
	public RoleAH(HttpServletRequest req, HttpServletResponse res) {

		super(req, res);
		this._objType = "role"; // 本操作针对表DICTIONARY
		this._moduleId = "system"; // 本模块为基本设置
	}

	/**
	 * 删除一个用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("rolesave")
	public void delRole() throws ServletException, SQLException {

		Role role = new Role(this._curuser, this.getJdo());
		role.refreshById(objId);
		role.delete();
		this.listRole();

	}

	// 传来的objId
	String objId = "";

	// 初始化
	protected void initial() {
		this.pushLocation("角色列表", "/basic.RoleAH.listRole");
		if (this._request.getParameter("objId") != null)
			objId = this._request.getParameter("objId");

	}

	/**
	 * 显示一个用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showRole() throws ServletException, SQLException {
		
		this.pushLocation("角色编辑", "/basic.RoleAH.showRole?objId="+objId);
		List<BizObject> plugins=Utility.get_configs("home_plugin");//plugin - 取插件列表
		this._request.setAttribute("plugins", current_role_plugins(plugins,objId));//plugin -传递到页面

		Role role = new Role(this._curuser, this.getJdo());
		role.refreshById(objId);
		this._request.setAttribute("obj", role.getRole());

		List<BizObject> myFunctions = role.getFunctions();
		List myFunctionIds = new ArrayList();
		for(BizObject b:myFunctions){
			myFunctionIds.add(b.getId());
		}

		// 需返回所有function，并且用remark字段来区分是否checked
		QueryFactory funcQF = new QueryFactory("function");
		funcQF.setOrderBy("createdate");
		List funcs = funcQF.query();
		HashMap modules = new HashMap();

		// logger.debug("size is "+funcs.size());
		// logger.debug("my functions "+ myFunctions);
		for (int i = 0; i < funcs.size(); i++) {

			BizObject func = (BizObject) funcs.get(i);
			// logger.debug("func "+ func);
			// 做checked标记
			if (myFunctionIds.contains(func.getId())) {
				// logger.debug(" contains !!!");
				func.set("creator", func.getID());
			}

			String moduleId = func.getString("moduleId");

			if (modules.containsKey(moduleId)) {
				List v = (List) modules.get(moduleId);
				v.add(func);
			} else {
				List v = new ArrayList();
				v.add(func);
				modules.put(moduleId, v);
			}

		}

		this._request.setAttribute("funcview", modules);
		this._nextUrl = "/template/basic/roleOper.jsp";

	}

	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void listRole() throws ServletException, SQLException {
		
		super.listObj();
		this._nextUrl = "/template/basic/roleList.jsp";
	}

	// 保存user
	@CandoCheck("rolesave")
	public void save() throws ServletException, SQLException {
		save_home_plugins(this.getParameter("Role$RoleID"),this.getParameters("home_plugins"));//保存角色下插件选择信息
		String obj_id=this.getParameter("objId");
		String obj_name=this.getParameter("Role$rolename");
		
		
		
		
		getJdo().beginTrans();
		//this.validateCanDo("rolesave");
		BizObject r = this.getBizObjectFromMap("role");
		//this.
		List v = this.getBizObjectWithType("function");
		Role role = new Role(this._curuser, this.getJdo());
		logger.info("add role v "+v.size());

		role.setFunctions(v);
		role.setRole(r);

		role.save();
		
		this.objId = role.getRole().getID();
		
		this._tipInfo = "操作成功";
		getJdo().commit();  //这里不commit的话，showRole显示还是老数据
		
		
		String buttons="<input onclick='window.location.href=\"/basic.RoleAH.showRole\"'" +
				  " type='button' class='w100' value='创建新角色' />"+
				  "<input onclick='window.location.href=\"/basic.RoleAH.listRole\"'" +
				  " type='button' class='w100' value='返回角色列表' />"+
				  "<input onclick='window.location.href=\"/basic.RoleAH.showRole?objId="+this.objId+"\"'" +
				  " type='button' class='w100' value='继续修改' />";
		this._request.setAttribute("buttons",buttons);
		throw new SuccessException((obj_id.equals("")?"新建":"修改")+"角色成功，角色名称："+obj_name);
		
		
		//this.showRole();

	}
	
			/**
			 * @title	获取一个会议的消息列表
			 */
			public List<BizObject> current_role_plugins(List<BizObject> result,String role_id) throws SQLException{
				Role role = new Role(this._curuser, this.getJdo());
				if("".equals(role_id)){
					return result;
				}
				role.refreshById(role_id);
				String home_plugin=role.getRole().getString("home_plugins")+",";//取当前角色选中的权限
				for(BizObject biz: result){
					//if(){}
					if(home_plugin.indexOf(biz.getId()+",")>-1){
						biz.set("checked", "checked");
					}
				}
				return result;
			}
			
			
	//保存角色下插件选择信息
	public void save_home_plugins(String roleid,String[] select_plugins) throws ServletException, SQLException {
		//
		
		String home_plugins="";
		if(select_plugins!=null && select_plugins.length>0)home_plugins=StringUtils.join(select_plugins,",");
		
		
		
		QueryFactory qf = new QueryFactory("role");
		qf.setHardcoreFilter("roleid='"+roleid+"'");
		List<BizObject> list = qf.query();
		BizObject obj;
		if(list.size()>0){
			
				obj = list.get(0);
				obj.set("home_plugins", home_plugins);
				ActionHandler.currentSession().update(obj);
				//throw new ControllableException("1111！"+home_plugins);
		}
		//else throw new ControllableException("2222！");
		
	}
	
	

	public void test() {
		try {
			// logger.debug("to index.jsp");
			_request.getRequestDispatcher("index.jsp").forward(_request,
					_response);
			// logger.debug("after index");
			// this.wait(3000);
			for (int i = 0; i < 50; i++) {
				// logger.debug("i " + i);
				// this.wait(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}