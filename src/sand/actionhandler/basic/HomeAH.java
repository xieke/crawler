package sand.actionhandler.basic;
import java.io.File;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import sand.actionhandler.system.ActionHandler;

import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.depot.business.document.Document;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.InfoException;
import sand.depot.tool.system.SystemKit;
import tool.basic.Utility;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import tool.workflow.FlowContext;
import tool.workflow.service.WorkFlowEngineService;


/**
 * <p>
 * 
 * Title: 系统首页 - 个人中心
 * </p>
 * <p>
 * Description: 系统首页 - 个人中心含插件、动态、快捷功能、用户信息等
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @author suliangben
 * @version 1.0
 */
public class HomeAH extends ActionHandler {
	
	private WorkFlowEngineService workFlowEngineService;
	//private TaskTa taskTa;
	
	private static Logger logger = Logger.getLogger(HomeAH.class);
	private int flag = 0;// 表明调动ERP用户的修改页面还是门禁用户的修改页面
	/**
	 * 构造方法
	 * 
	 * @param req
	 * @param res
	 */
	public HomeAH(HttpServletRequest req, HttpServletResponse res) {

		super(req, res);
		this._objType = "EMPLOYEE"; // 本操作针对表EMPLOYEE
		this._moduleId = "system"; // 本模块为基本设置
	}
	
	public void index() throws SQLException{
//		QueryFactory qf = new QueryFactory("re_user_role");//取当前用户拥有的插件列表
//		qf.setHardcoreFilter("USERID LIKE '"+this._curuser.getId()+"'");
//		List<BizObject> result = qf.query();
//		String my_plugins="";
//		for(BizObject biz : result){
//			my_plugins=my_plugins+biz.getBizObj("ROLEID").getString("home_plugins")+",";
//		}
//		//String where=" id in ('"+my_plugins.replace(",","','")+"good boy') ";
//		//qf = new QueryFactory("item");
//		//qf.setHardcoreFilter(where);
//		//this._request.setAttribute("my_plugins", qf.query());
//		
//		String sql = "SELECT m.*,i.ikey,i.name,i.id FROM item i LEFT JOIN (SELECT * FROM basic.`my_plugin_config`  WHERE  " +
//		" owns='"+this._curuser.getId()+"' ) m ON i.`ID`=m.`plugin_id` WHERE i.dicid ='home_plugin' AND" +
//		" i.id IN ('"+my_plugins.replace(",","','")+"good boy') ORDER BY m.`sort` DESC";
//		
////		String sql="SELECT m.*,i.ikey,i.name,i.id FROM item i right JOIN basic.`my_plugin_config`  m ON i.`ID`=m.`plugin_id` WHERE i.dicid ='home_plugin' " +
////		"AND i.id IN ('"+my_plugins.replace(",","','")+"good boy') AND m.closes<>'1' AND m.owns='"+this._curuser.getId()+"' ORDER BY m.`sort` DESC";
//		//this._request.setAttribute("my_plugins22",sql);
//		result=QueryFactory.executeQuerySQL(sql);
//		this._request.setAttribute("my_plugins",result);
//		
		
		this._nextUrl = "/index.jsp";
	}
	
	public void config() throws SQLException{
		String job = this.getParameter("job");
		if(job.equals("save")){
			String str = "delete from basic.my_plugin_config where owns='"+this._curuser.getId()+"'";
			QueryFactory.executeUpdateSQL(str);
			List<BizObject> list = this.getBizObjectWithType("my_plugin_config");
			for(int i=0;i<list.size();i++){
					BizObject biz = list.get(i);
					biz.set("owns", this._curuser.getId());
					biz.set("closes", list.get(i).getString("checkbox").equals("1")?"0":"1");
					this.getJdo().addOrUpdate(biz);
			}
		}
		
		String plugins_list="";
		String sql = "SELECT A.`home_plugins` FROM re_user_role R right JOIN role A ON R.`ROLEID`= A.`ROLEID` WHERE R.`USERID` ='"+this._curuser.getId()+"' AND home_plugins IS NOT NULL";//取当前用户所有角色所拥有的控件id
		List<BizObject> result=QueryFactory.executeQuerySQL(sql);
		for(BizObject biz: result){
			plugins_list+=biz.getString("home_plugins")+",";
		}
		//sql="SELECT * FROM item WHERE dicid ='home_plugin' AND id IN ('"+plugins_list.replace(",","','")+"good boy')";
		
		sql = "SELECT m.*,i.ikey,i.name,i.id FROM item i LEFT JOIN (SELECT * FROM basic.`my_plugin_config`  WHERE  " +
				" owns='"+this._curuser.getId()+"' ) m ON i.`ID`=m.`plugin_id` WHERE i.dicid ='home_plugin' AND" +
				" i.id IN ('"+plugins_list.replace(",","','")+"good boy') ORDER BY m.`sort` DESC";
		
//		sql="SELECT m.*,i.ikey,i.id FROM item i LEFT JOIN basic.`my_plugin_config`  m ON i.`ID`=m.`plugin_id` WHERE i.dicid ='home_plugin' " +
//				"AND i.id IN ('"+plugins_list.replace(",","','")+"good boy') and owns='"+this._curuser.getId()+"' ORDER BY m.`sort` DESC";
		result=QueryFactory.executeQuerySQL(sql);
		this._request.setAttribute("objList", result);//取有权限的控件列表
//		logger.info("插件objList is :"+result);
		//读取当前用户的控件配置
		
		
		
		this._nextUrl = "/template/basic/my_plugin_config.jsp";
	}
	
	public void my_plugins() throws SQLException{
		this._nextUrl = "/template/basic/my_plugins.jsp";
	}
	
	/**
	 * 个人中心正中上方的指派给我(任务,bug,测试等)
	 * 
	 * @return
	 * @throws SQLException
	 */
	//@Ajax
//	public String getDesignateToMeDom() throws SQLException{
//		int i=0;
//		//指派给我的任务
//		//String sql =MyTaskAH.getBasicSql(this._curuser.getID());
//	//	List <BizObject>list =new QueryFactory("re_task_executant_ta").executeQuerySQL(sql);
//		i+=list.size();
//		
//		//往i上加指派给我的任务,bug,测试等的数量
//		return i+"";
//	}
	
	/**
	 * 个人中心指派给我的明细
	 * 
	 * @throws SQLException
	 */
//	public void listDesignateToMe() throws SQLException{
//		//指派给我的任务
//		String sql =MyTaskAH.getBasicSql(this._curuser.getID());
//	    sql =sql+" order by  ex.task_id";
//		List <BizObject> rList =new QueryFactory("re_task_executant_ta").executeQuerySQL(sql);
//		
//		for(BizObject obj:rList)
//		{
//			obj.setFk("user_id", "employee");
//			obj.setFk("task_id", "testtask_qa");
//		}
//		this._request.setAttribute("rList", rList);
//		
//			
//		//取任务,bug,测试等的列表
//		
//		this._nextUrl = "/template/basic/home_designatetome_list.jsp";
//	}
	
	
	/**
	 * 个人中心正中上方的的通知信息(包括审核的通知和会议的通知)
	 * 
	 * @return
	 * @throws SQLException
	 */
//	@Ajax
//	public String getNoticeMyDom() throws SQLException{
//		List<BizObject> list=this.getWorkFlowEngineService().getToDoJobLists(FlowContext.JOB_SORT_NOTICE,"", this._curuser.getId(),null);
//		int i=0;
////		for (BizObject biz : list) i+=biz.getInt("cnt", 0);
//		i+=list.size();
//		//在此加上会议通知,把会议通知的数量加到i上
//		return i+"";
//	}
	
	/**
	 * 个人中心正中上方的待处理(包括审核的事项和待分配的需求,待指派的任务,待处理的任务等)
	 * 
	 * @return
	 * @throws SQLException
	 */
	//@Ajax
//	public String getTodoMyDom() throws SQLException{
//		List<BizObject> list = this.getWorkFlowEngineService().getToDoJobLists("", "", this._curuser.getId(), null);
//		int i=0;
//		i+=list.size();
//		
//		//待指派的任务
//	//	List <BizObject> taskList =this.getTaskTa().getTaskAssignListByPrincipal(this._curuser.getId(), "", "", "", "", null,"");
//
//		i=i+taskList.size();
//		
//		
//		//在此加上等处理(待分配的需求,待指派的任务等)的数量加到i上
//		return i+"";
//	}
	
	/**
	 * 个人中心的待处理的明细
	 * 
	 * @throws SQLException
	 */
//	public void listMyTodo() throws SQLException{
//		List<BizObject> workflow_todo_list = this.getWorkFlowEngineService().getToDoJobLists("", "", this._curuser.getId(), null);
//		this._request.setAttribute("workflow_todo_list", workflow_todo_list);
//		
//		//待指派的任务
//		List <BizObject> taskList =this.getTaskTa().getTaskAssignListByPrincipal(this._curuser.getId(), "", "", "", "", null,"0");
//	    for(BizObject obj:taskList)
//		{
//			obj.setFk("user_id", "employee");
//			obj.setFk("task_id", "testtask_qa");
//		}
//		this._request.setAttribute("taskList", taskList);
//		
//		
//		this._nextUrl = "/template/basic/home_mytodo_list.jsp";
//	}
	
	/**
	 * 个人中心待处理的通知信息
	 * 
	 * @throws SQLException
	 */
//	public void listMyNotice() throws SQLException{
//		List<BizObject> workflow_noice_list=this.getWorkFlowEngineService().getToDoJobLists(FlowContext.JOB_SORT_NOTICE,"", this._curuser.getId(),null);
//		this._request.setAttribute("workflow_noice_list", workflow_noice_list);
//		
//		this._nextUrl = "/template/basic/home_mynotice_list.jsp";
//	}
	
	/**
	 * 获取当前用户的快捷方式
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Ajax
	public String getLink() throws SQLException{
		StringBuilder sql = new StringBuilder("select l.`name`,l.link,(select url from basic.annex a where a.id=l.pic) url " +
				"from basic.link_desktop l inner JOIN basic.re_user_link_desktop r on l.id=r.link_desktop_id where r.user_id='");
		sql.append(this._curuser.getId()).append("' order by r.`orderby` desc");
		String str = QueryFactory.executeQuerySQL(sql.toString()).toString();
		return str;
	}

//	public WorkFlowEngineService getWorkFlowEngineService() {
//		if(workFlowEngineService == null) 
//			workFlowEngineService = (WorkFlowEngineService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("workFlowEngineService");
//		return workFlowEngineService;
//	}
	
//	public TaskTa getTaskTa() {
//		if (taskTa == null)
//			taskTa = (TaskTa) WebApplicationContextUtils
//					.getWebApplicationContext(ActionHandler._context).getBean(
//							"taskTa");
//		return taskTa;
//	}
}