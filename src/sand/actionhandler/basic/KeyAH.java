package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.tool.system.AlarmException;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import tool.dao.util.ObjectProperty;
import tool.workflow.service.FlowCaseService;
import tool.workflow.service.impl.FlowCaseServiceImpl;

public class KeyAH extends ActionHandler {
	
	//private Logger logger = Logger.getLogger("wyc.logger");
	public KeyAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType ="message";
		this._moduleId="basic";
		// TODO Auto-generated constructor stub
	}
	
	BizObject _message = new BizObject("message");
	/**
	 * 一个ActionHandler要初始化运行的代码就放在这个方法中（供子类用）
	 * 
	 * 注意，如果你重载了这个方法，除非调用 super.initial() 这个方法里的语句将不会运行
	 * 
	 * @param bizName
	 * @return
	 * @throws SQLException 
	 */
	protected void initial()  {

		// 保存初始obj type值
		// logger.info("super initial ");

		// if (!this.getParameter("objId").equals(""))
		//_objId = this.getParameter("objId");
		super.initial();
		
		logger.info("objid is "+_objId);
		try {
			if(!_objId.equals("")){
				_message.setID(_objId);
				_message.refresh();
				
				this.setAttribute("msg", _message);
			}
			
			logger.info("msg is "+_message);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("",e);
		}


	}
	
	/**
	 * 关键字
	 * @throws SQLException
	 */
	public void listKeyword() throws SQLException{
		
		List hv = KeyWord.getHotest(this.getParameter("type"));
		
		String keyword = KeyWord.getKeyword(this.getParameter("mid"));
		this.setAttribute("hv", hv);
		this.setAttribute("keyword", keyword);
		this._nextUrl="/template/basic/edit2.jsp";
	}
	
	
	
	/**
	 * 关键字
	 * @throws SQLException
	 */
	@Ajax
	public String getKey() throws SQLException{
		return KeyWord.getKeyword(this.getParameter("mid"));

	}
	/**
	 * 关键字
	 * @throws SQLException
	 */
	@Ajax
	public String getHotest() throws SQLException{
		
		List<BizObject> hv = KeyWord.getHotest(this.getParameter("type"));
		
//		List mkv = KeyWord.getKey(this.getParameter("mid"));
//		this.setAttribute("hv", hv);
//		this.setAttribute("mkv", mkv);
//		this._nextUrl="/template/basic/edit2.jsp";
		
		String ret="";
		for(BizObject b:hv){
			ret=ret+b.getString("name")+",";
		}
		return ret;
	}	
	
	@Ajax
	public String addKey() throws SQLException{
		KeyWord.addKeyword(this.getParameter("mid"), this.getParameter("keyword"));
		return "添加关键字成功";
	}
	
	private   Logger logger = Logger.getLogger(this.getClass());



	/**
	 * 列表显示模版 
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void listOutbox() throws  SQLException {}
	
	/**
	 * 列表显示模版 
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void send() throws ServletException, SQLException {}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void read() throws SQLException{}
	/**
	 * 显示一个 模版 
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showExcp() throws ServletException, SQLException {}
	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showexceptionList() throws ServletException, SQLException {}	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseMsg() throws ServletException, SQLException {}

	
	private void prepareUsers() throws SQLException{
		
		String users=_message.getString("users");
		String userids[] = users.split(",");
		ArrayList usersv = new ArrayList();
		for(String userid:userids){
			BizObject user= new BizObject("employee");
			user.setID(userid);
			user.refresh();
			usersv.add(user);			
		}
		this.setAttribute("userList", usersv);
		
	}

}
