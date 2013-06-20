package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.depot.business.oa.Job;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;
import tool.dao.JDO;
import tool.dao.QueryFactory;

@AccessControl("no")
// 哦耶~~~任何人可用的
public class SystemMsgAH extends ActionHandler {

	private static Logger logger = Logger.getLogger(SystemMsgAH.class);

	public SystemMsgAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
		this._objType = "SYSTEM_MSG"; // 本操作针对表EMPLOYEE
		this._moduleId = "system"; // 本模块为基本设置
	}

	/**
	 * <p>Title: 发送系统信息</p>
	 * <p>Parameter: 用户id,信息内容,信息链接</p>
	 */
	public static boolean send_msg_to(String userid, String message, String url,JDO jdo) throws ServletException, SQLException {
		BizObject biz = new BizObject("SYSTEM_MSG");

		biz.set("userid", userid);
		biz.set("msg", message);
		biz.set("url",url);
		biz.set("posttime", new Date());
		
		jdo.add(biz);
		return true;
	}
	
	
	
	
	/**
	 * <p>Title: 发送系统信息（AJAX方法）</p>
	 * <p>Parameter: 用户id,信息内容,信息链接</p>
	 */
	 @Ajax
	public String send_msg_to_ajax() throws ServletException, SQLException {
    String userid = this.getParameter("userid");
    String message = this.getParameter("message");
    String url = this.getParameter("url");
    
		BizObject biz = new BizObject("SYSTEM_MSG");

		biz.set("userid", userid);
		biz.set("msg", message);
		biz.set("url",url);
		biz.set("posttime", new Date());
		
		this.add(biz);
		return "ok";
	}
	
	
	
	

	/**
	 * <p>Title: 系统通知列表</p>
	 * <p>Parameter: 无</p>
	 */
	public void my_msg_list() throws ServletException, SQLException {
		QueryFactory qf = new QueryFactory("SYSTEM_MSG");
		String userid = this._curuser.getId();

		qf.setHardcoreFilter("userid='" + userid+ "' and deleted is null");

		qf.setOrderBy("posttime desc");
		//List<BizObject> list = qf.query(preparePageVar());
		List<BizObject> list = qf.query();

		this.setAttribute("objList", list);
		this._nextUrl = "/basic/system_msg_list.jsp";
	}

	/**
	 * <p>Title: 删除某条系统通知</p>
	 * <p>Parameter: 信息id:Get方式获取</p>
	 */
	@Ajax
	public String delete_msg() throws ServletException, SQLException {
		String msgid = this.getParameter("msgid");
		BizObject obj = new QueryFactory("SYSTEM_MSG").getByID(msgid);
		String del2 = obj.getString("deleted");
		obj.set("deleted", "0");
		this.update(obj);
		return "ok";
	}


}