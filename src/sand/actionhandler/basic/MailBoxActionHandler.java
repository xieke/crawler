package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.List;import java.util.ArrayList;
import java.util.List;import java.util.ArrayList;
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
import tool.dao.QueryFactory;





@AccessControl("no")
// 哦耶~~~任何人可用的
public class MailBoxActionHandler extends ActionHandler {

	private static Logger logger = Logger.getLogger(MailBoxActionHandler.class);
	
	public MailBoxActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
		this._objType = "MAILBOX"; // 本操作针对表EMPLOYEE
		this._moduleId = "system"; // 本模块为基本设置
	}




	
	

	/*
	 * 我的桌面：设置图标
	 * p1:功能名	[p2:图标值1-48] | [p3:是否显示0-1] | [p4:排序]
	 * */
	@Ajax
	public String setshow() throws ServletException, SQLException {
		String userid=this._curuser.getId();
		String p1=this.getParameter("p1");
		String p2=this.getParameter("p2");
		String p3=this.getParameter("p3");
		String p4=this.getParameter("p4");
		
		
		if(p4!=null&&p4!=""){
			//更新排序
			StringTokenizer st = new StringTokenizer(p4, ";");
			while (st.hasMoreTokens()) {
				String function = (String) st.nextElement();
				String sql="update desktop_save t set t.sort='"+st.countTokens()+"' where t.id in (select s.id from desktop_save s where s.function='"+function+"' and s.users='"+userid+"')";
				System.out.println("sql="+sql);
				logger.info("sql="+sql);
				int i = QueryFactory.executeUpdateSQL(sql);
			}
			return "ok";
		}
		
		
		List<BizObject> list = QueryFactory.executeQuerySQL("select * from desktop_save t where t.users='"+userid+"' AND function='"+p1+"'");
		if(list.size()==0){
			BizObject desktop = new BizObject("desktop_save");
			desktop.set("users", userid);
			desktop.set("function", p1);
			if(p2!=null&&p2!=""){
				desktop.set("ico", p2);
				desktop.set("sort", "12");
				desktop.set("show", "0");
			}
			if(p3!=null&&p3!=""){
				desktop.set("show", p3);
				desktop.set("ico", "1");
				desktop.set("sort", "12");
			}
			this.add(desktop);
		}else{
			if(p2!=null&&p2!=""){
				list.get(0).set("ico", p2);
			}
			if(p3!=null&&p3!=""){
				list.get(0).set("show", p3);
			}
			getJdo().update(list.get(0));
		}
		return "ok";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*
	 * AJAX查询部门及对应的ID
	 * */
	@Ajax
	public String getDepotMember() throws ServletException, SQLException {
		String username="";
		QueryFactory qf = new QueryFactory("re_user_dept");
		String userid=this.getParameter("employee$depot");
		
		qf.setHardcoreFilter("deptid='"+userid+"'");	
		List<BizObject> list=qf.query();
		
		
		
		
		
		for(BizObject bb:list){
			String username2 = bb.getBizObj("userid").getString("username"); 
			username+="<option value='"+bb.getString("userid")+"'>"+username2+"</option>";
		}
		return username;
		
			//["green","grey","gold"]
	}
	
/*
 * 写新邮件界面
 */
	public void mailWrite() throws ServletException, SQLException {
		QueryFactory qf = new QueryFactory("department");
		List<BizObject> list=qf.query();
		
		String userid="";
		userid=this.getParameter("userid");
		if(userid==null||userid.equals("")){
			this.setAttribute("reciverid", "0");
			this.setAttribute("recivername","0");
		}else{
			this.setAttribute("reciverid", userid);
			BizObject obj=new QueryFactory("employee").getByID(userid);
			String username2="";
			if (obj!=null)
				username2=obj.getString("username");
			this.setAttribute("recivername", username2);
		}
		
		this.setAttribute("objList", list);
		this._nextUrl = "/basic/mailWrite.jsp";
	}

	
	
/*
 * 写一条新的短消息
 * */
public void write() throws ServletException, SQLException {
	BizObject biz = this.getBizObjectFromMap("MAILBOX");
	String userId=this._curuser.getId();
		biz.set("sendtime",new Date());
		biz.set("sender",userId);
		biz.set("reply","0");
		biz.set("deleted","0");
		this.add(biz);
		this._nextUrl = "/basic/mailMsg.jsp";
}


/*
 * 标记短消息(标记是否为旧消息、已回复、已删除)
 * */
public void tag() throws ServletException, SQLException {
	BizObject biz=new BizObject("MAILBOX");
	biz.setID(_objId);
	biz.set("reply",this.getParameter("flow$creator"));
	this.update(biz);
	this.setAttribute("jjj",1);
	this._nextUrl = "/basic/mailboxajax.jsp";
}

/*
 * 删除短消息
 * */
public void deleted() throws ServletException, SQLException {
	String msgid=this.getParameter("msgid");
	String j=this.getParameter("j");
	BizObject obj=new QueryFactory("MAILBOX").getByID(msgid);
	String del2=obj.getString("deleted");
	if(del2.equals("0")){obj.set("deleted", j);}
	else if(del2.equals("1")&&j.equals("2")){obj.set("deleted", 3);}
	else if(del2.equals("2")&&j.equals("1")){obj.set("deleted", 3);}
	this.update(obj);
}

/*
 * 查询收发短信箱
 * */
public void getList() throws ServletException, SQLException {
	QueryFactory qf = new QueryFactory("MAILBOX");
	String wherebox=this.getParameter("where");

	String userid=this._curuser.getId();
	if(wherebox.equals("inbox")){
		qf.setHardcoreFilter("reciver='"+userid+"' and (deleted='0' or deleted='2')");
		this.setAttribute("wherebox","收件箱");
	}else{
		qf.setHardcoreFilter("sender='"+userid+"' and (deleted='0' or deleted='1')");
		this.setAttribute("wherebox","发件箱");
	}
	qf.setOrderBy("sendtime desc");
	List<BizObject> list=qf.query(preparePageVar());
	
	this.setAttribute("objList", list);
	this._nextUrl = "/basic/mailBoxList.jsp";
}


/*
 * AJAX用的统计收件箱数量
 * */
public static int getMsgNum() throws ServletException, SQLException {
	QueryFactory qf = new QueryFactory("MAILBOX");
	String userid=currentUser().getEmployee().getId();
	
	qf.setHardcoreFilter("reciver='"+userid+"'");
	List<BizObject> list=qf.query();
	return list.size();
}





/*
 * TEST
 * */
@Ajax
public String benTest() throws ServletException, SQLException {
	String no[] = this.getParameters("s");//request.getParameterValues("s");
	return Integer.toString(no.length);//"ok";//no.length;
}























}