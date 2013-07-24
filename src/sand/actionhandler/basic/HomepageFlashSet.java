package sand.actionhandler.basic;

import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import sand.actionhandler.basic.UserAH;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.depot.business.oa.Job;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

@AccessControl("session")
public class HomepageFlashSet extends ActionHandler {

	static Logger logger = Logger.getLogger(HomepageFlashSet.class);

	public HomepageFlashSet(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
		this._objType = "homepageflash"; // 本操作针对表EMPLOYEE
		this._moduleId = "system"; // 本模块为基本设置

	}


	/*
	 * 后台页面
	 */
	public void setting() throws ServletException, SQLException {
		if (this.getParameter("job", "").equals("update")) {
			List list = this.getBizObjectWithType("homepageflash");
			for (int i = 0; i < list.size(); i++) {
				this.update((BizObject) list.get(i));
			}

		}
		QueryFactory qf = new QueryFactory("homepageflash");
		qf.setOrderBy("id asc");
		List<BizObject> list = qf.query();
		logger.info("sql is " + qf.getSql());
		this.setAttribute("objList", list);
		this._nextUrl = "/basic/homepage_flash_set.jsp";
	}

}