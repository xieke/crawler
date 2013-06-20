package sand.actionhandler.basic;

import java.io.File;



import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.annotation.TokenCheck;
import sand.chart.BarChartDemo;
import sand.chart.PieChartDemo;
import sand.depot.business.document.Document;
import sand.depot.business.system.Employee;
import sand.depot.business.system.Role;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.InfoException;
import sand.depot.tool.system.SystemKit;
import sand.mail.MailServer;

//import sand.rop.RopSampleClient;
//import sand.rop.UserServiceClient;
import tool.basic.Utility;
import tool.crypto.Crypto;
import tool.dao.BizObject;
import tool.dao.JDO;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;
import tool.ntlm.NTLM;

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
 * Company: ivt
 * </p>
 * 
 * @author wang.yc
 * @version 1.0
 */
public class UserAH extends ActionHandler {
	
	//private DTaskService dTaskService;
	
	private static Logger logger = Logger.getLogger(UserAH.class);

	private int flag = 0;// 表明调动ERP用户的修改页面还是门禁用户的修改页面
	//private TaskMemberService taskMemberService;
	/**
	 * 构造方法
	 * 
	 * @param req
	 * @param res
	 */
	public UserAH(HttpServletRequest req, HttpServletResponse res) {

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
		this.pushLocation("密码修改","/basic.UserActionHandler.modifyPwd");
		BizObject userBiz = this.getBizObjectFromMap("EMPLOYEE");
		// UserData user = new UserData();
		// user.setUserId(userBiz.getString("userId"));
		// user.setPassword(userBiz.getString("password"));

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
	/**
	 * 修改密码
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void setInnerMail() throws ServletException, SQLException {

		BizObject userBiz = this.getBizObjectFromMap("EMPLOYEE");		
		BizObject user = this._curuser;
		
		user.set("innermail", userBiz.getString("innermail"));
		user.set("innermailpass", userBiz.getString("innermailpass"));
		
		getJdo().update(user);
		
		this._tipInfo = "内邮修改成功!";
		this._nextUrl = "/setting/setEmail.jsp";
	}
	
	public void sendTestMail(){
		MailServer.sendMail(_curuser, "", "測試", "測試");
		this._tipInfo="";
		this._nextUrl = "/success.jsp";
	}
	
    public  void send() throws IOException {
    	NTLM.disconnect();
    	MailServer.sendMail("xie.k@sand.com.cn", "", "ddd", "dd","");
    	logger.info("first send return");
    	
            URL url = new URL("http://mail.sand.com.cn");  
            URLConnection connection = url.openConnection();  
            InputStream in = connection.getInputStream();  
            byte[] data = new byte[1024];  
             while(in.read(data)>0)  
             {  
            	 logger.info(data);
            	 System.out.println(data);
                     //do something for data  
             }  
             in.close();  
     }  
    public  void send2() throws IOException {
//    	boolean a=MailSend.sendMail("xie.k@sand.com.cn", "", "ddd", "dd","");
//    	logger.info("first send return "+a);
    	//NTLM.init();
    	MailServer.sendMail("xie.k@sand.com.cn", "", "ddd", "dd","");
    	logger.info("second send return ");
            URL url = new URL("http://mail.sand.com.cn");  
            URLConnection connection = url.openConnection();  
            InputStream in = connection.getInputStream();  
            byte[] data = new byte[1024];  
             while(in.read(data)>0)  
             {  
            	 logger.info(data);
            	 System.out.println(data);
                     //do something for data  
             }  
             in.close();  
     } 
	/**
	 * 修改密码
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void setOutMail() throws ServletException, SQLException {

		BizObject userBiz = this.getBizObjectFromMap("EMPLOYEE");
		
		BizObject user = this._curuser;
		Crypto crypto = new Crypto();
		user.set("outmail", userBiz.getString("outmail"));
		user.set("outmailpass",crypto.des(userBiz.getString("outmailpass")) );
		
		getJdo().update(user);
		this._tipInfo = "外邮修改成功!";
		this._nextUrl = "/setting/setEmail.jsp";
	}

	/**
	 * 
	 * 退出重新登陆
	 */
	public void exit2() {
		_request.getSession().setAttribute("userInfo", null);
		this._nextUrl = "/template/basic/login.jsp";

	}

	/**
	 * 删除一个用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("usersave")
	public void delUser() throws ServletException, SQLException {

		// this.validateCanDo("usersave");

		// JanitorController jc = new JanitorController(this._curuser,
		// this._jdo);
		// jc.refreshById(objId);
		// jc.removeAllDoors();
		
		System.out.println("删除方法");

		Employee employee = new Employee(this._curuser, this.getJdo());
		employee.refreshById(objId);

		employee.delete();
		this.listUser();

	}

	// 传来的objId
	String objId = "";

	// 初始化
	protected void initial() {

		this.pushLocation("用户中心","/basic.UserActionHandler.showUserCenter");
		if (this._request.getParameter("objId") != null)
			objId = this._request.getParameter("objId");

	}

	/**
	 * 查看一个用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void viewUser() throws ServletException, SQLException {
		this.pushLocation("查看用户","/basic.UserActionHandler.viewUser");
		Employee employee = new Employee(this._curuser, this.getJdo());
		employee.refreshById(objId);
		BizObject obj=employee.getEmployee();
		//String depts=this.findDeptsOfUser(obj.getId());
		//obj.set("depts",depts);
		this._request.setAttribute("obj",obj);
		//this.showUserRecord(employee.getEmployee().getId());
		List myroles = employee.getRoles();

		System.out.println("objId=============="
				+ employee.getEmployee().getId());

		this._request.setAttribute("roles", myroles);
		this._nextUrl = "/template/basic/admin/userView.jsp";

	}
	
	@CandoCheck("accessBill")
	public void accessBill() throws SQLException{	
		BizObject obj=new QueryFactory("TASK_BILL").getByID(this.getParameter("objId"));
		obj.set("STATE", 1);
		obj.set("EDATE", new Date());
		this.getJdo().update(obj);
		
		this.showMyBill();
	}
	
	public void accessBillBack() throws SQLException{	
		BizObject obj=new QueryFactory("TASK_BILL").getByID(this.getParameter("objId"));
		obj.set("STATE", 1);
		obj.set("EDATE", new Date());
		this.getJdo().update(obj);
		
		this.showMyBillBack();
	}
	
	public void searchBill() throws SQLException{	
		this.setAttribute("TASKID", this.getParameter("TASK_BILL$TASKID"));
		
		this._orderBy="ODATE desc";
		this.listObj("TASK_BILL");	
		

		if(this._employee.canDo("accessBill")==false)
			this.setAttribute("cando",0); 
		else
			this.setAttribute("cando",1);
		
		this._nextUrl="/task/taskmember/billBack.jsp";
	}
	
	
	public void showMyBillBack() throws SQLException{
		BizObject obj=new BizObject("TASK_BILL");
		
		if(this._employee.canDo("accessBill")==false)
			this.setAttribute("cando",0); //不显示受理
		else
			this.setAttribute("cando",1);
	
		obj.setOrderBy("ODATE desc");
		List l=new QueryFactory("TASK_BILL").query(obj,this.preparePageVar());
		this.setAttribute("objList", l);
		this._nextUrl="/task/taskmember/billBack.jsp";
	}
	
	
	public void showMyBill() throws SQLException{
		BizObject obj=new BizObject("TASK_BILL");
		
		if(this._employee.canDo("accessBill")==false)
		{
			obj.set("ASKID", this._curuser.getId());
			this.setAttribute("cando",0); //不显示受理
		}
		else
			this.setAttribute("cando",1);
		obj.setOrderBy("ODATE desc");
		List l=new QueryFactory("TASK_BILL").query(obj,this.preparePageVar());
		this.setAttribute("objList", l);
		this._nextUrl="/user/company_bill.jsp";	
	}
	

	
	/**
	 * 显示当前用户
	 * @throws SQLException
	 */
	public void showMe() throws SQLException{
		logger.info(this+" curuser id "+this._curuser.getId());
		showOne(_curuser.getId());
		
		//this._curuser.set("myessayCnt", 100);
		
	//	this.setAttribute("wydList", Whatyoudo.getMywyd(_curuser.getId(), this.getParameter("type")));
	//	this._nextUrl="/user/user_center_index.jsp";
		
//		/**
//		 * 如果是企业用户
//		 */
//		if(_curuser.getString("type").equals("1")||this.getParameter("company").equals("1"))
//			this._nextUrl="/user/company_index.jsp";			
//		else{
////			List<BizObject> tasks = getTaskMemberService().queryForMemberReward(TaskMemberVo.create("3", _curuser.getId(),null,null), new PageVariable(10));
////			this._request.setAttribute("tasks", tasks);
//			this._nextUrl="/user/user_center_index.jsp";
//		}
	
	}
	/**
	 * 显示一个用户信息
	 * @throws SQLException
	 */
	public void showOne() throws SQLException{
		String userid=this.getParameter("userid");
		this.showOne(userid);
		this.showOneFriends(userid);
		this.showOneTeams(userid);
		
		//this.setAttribute("otheressay",basicAssisant.getMyTotalEssay(userid));
		//this.setAttribute("otherrepase",basicAssisant.getMyTotalRepase(userid));
		
		this._nextUrl="/user/user_center_homepage.jsp";
	}
	
	public void showOne(String userid) throws SQLException{
		//Employee employee = this._employee;
		//String userid=this.getParameter("userid");
		
		
		
		BizObject obj=new BizObject("employee");
		obj.setID(userid);
		obj.refresh();
		
		Employee emp = new Employee();
		emp.setEmployee(_curuser);
		
		//obj.set("joined",emp.isMyFriend(obj.getId()));
		
		//this.setAttribute("wydList", Whatyoudo.getOnewyd(userid, this.getParameter("type")));
//		if(obj.getString("photoid").equals(""))
//			obj.set("photoid", "ANNE14514412");
//		
//		if(obj.getString("signature").equals(""))
//			obj.set("signature", "太纠结.太矛盾.太复杂...不好说.不好说吖！");
		
		//List<BizObject> taskClasses = getMyTaskClass();
		
		//logger.info("显示数量为："+taskClasses.size());
		//_request.setAttribute("classes", taskClasses);
		
		//this._request.setAttribute("mall", MallTransAH.getMyTotalListById(userid));
		this._request.setAttribute("obj", obj);
		
//		this._request.setAttribute("issuedtask", this.getDTaskService().queryIssueDTaskByUserId(userid));
//		this._employee.refreshUserInfo("dtaskcnt");
		/**
		 * 如果是企业用户
		 */
		if(_curuser.getString("type").equals("1")){
//			//设置用户任务数
//			_request.getSession().setAttribute(TaskContext.SESSION_COMPANY_TASK_COUNT_KEY, getTaskService().queryIssueTaskCount(userid));
			//查询发布的达任务
		//	List<BizObject> dtasks = this.getDTaskService().getConcernDTaskByUserId(userid,this.preparePageVar(4));
			//this._request.setAttribute("dtasks", dtasks);
			this._nextUrl="/html/theme/index.html";		
		}
		else{
//			//设置用户任务数
//			_request.getSession().setAttribute(TaskContext.SESSION_USER_TASK_COUNT_KEY, getTaskService().queryTaskCount(userid));
			this._nextUrl="/user/user_center_homepage.jsp";
		}
			
		//this._nextUrl="/user/user_center_info.jsp";
	}

	/**
	 * 取任务类别
	 * @return
	 * @throws SQLException 
	 */
	public List<BizObject> getMyTaskClass() throws SQLException{
		List<BizObject> v = SystemKit.getCachePickList("parenttclass");
		
		List<BizObject> concerns = this._curuser.getList("concern");
		
		List strconcerns = new ArrayList();
		Map concernmap = new HashMap();
		for(BizObject concern:concerns){
			//1concernmap.put(concern.getString("taskclassid"), arg1)
			strconcerns.add(concern.getString("taskclassid"));
		}
		logger.info("get my list concern "+strconcerns.size());
		for(BizObject b:v){
			List<BizObject> children=SystemKit.getCachePickList(b.getString("id"));
			//logger.info(b.getId()+"parentid"+children.size());
			for(BizObject bb:children){
				if(strconcerns.contains(bb.getID())){
					bb.set("checked", bb.getId());
					logger.info("set checked "+bb.getId());
				}
				else
					bb.set("checked", "");
			}
			b.set("children", children);
		}
		return v;
	}
	/**
	 * 申请添加一个好友
	 * @throws SQLException
	 */
	public void addFriend() throws SQLException{
		this.getJdo().beginTrans();
		BizObject ref = this.getBizObjectFromMap("re_friend");
		BizObject friend=ref.getBizObj("userid");
		if(ref==null)
			throw new ControllableException("好友为空");
		if(friend.getId().equals(this._curuser.getId()))
			throw new ControllableException("不能添加自己为好友");
		
		ref.set("friendid", this._curuser.getId());
		logger.info("ref us "+ref+"  "+ref.getQF().query(ref).size());
		if(ref.getQF().query(ref).size()==0){
			ref.set("state", 0);
			this.getJdo().add(ref);			
			//BizObject friend=ref.getBizObj("friendid");
//			Whatyoudo.addUserWyd(_curuser.getId(), "申请和 <a href=/basic.UserActionHandler.showOne?userid="+friend.getId()+"> "+friend.getString("username")+"</a>做好友", Whatyoudo.TYPE_FRIENDSHIP);
//			Whatyoudo.addUserWyd(friend.getId(),_curuser.getString("username")+ "希望和你成为好友， <a href=/basic.UserActionHandler.acceptFriend?friendid="+friend.getId()+"> 同意 </a> <a href=/basic.UserActionHandler.refuseFriend?friendid="+friend.getId()+"> 拒绝 </a> ", Whatyoudo.TYPE_SELF);
		}
		else{
			ref=(BizObject) ref.getQF().query(ref).get(0);
			ref.set("state", 0);
			logger.info("update ref "+ref);
			this.getJdo().update(ref);			
			
			
			
		}
		
		Whatyoudo.addUserWyd(_curuser.getId(), "申请和 <a href=/basic.UserActionHandler.showOne?userid="+friend.getId()+"> "+friend.getString("username")+"</a>做好友", Whatyoudo.TYPE_FRIENDSHIP);
		//Whatyoudo.addUserWyd(friend.getId(),_curuser.getString("username")+ "希望和你成为好友， <a href=/basic.UserActionHandler.listMyFriendApplys?friendid="+this._curuser.getId()+"> 处理 </a> <a href=/basic.UserActionHandler.refuseFriend?friendid="+friend.getId()+"> 拒绝 </a> ", Whatyoudo.TYPE_SELF);
		Whatyoudo.addUserWyd(friend.getId(),_curuser.getString("username")+ "希望和你成为好友， <a href=/basic.UserActionHandler.listMyFriendApplys?friendid="+this._curuser.getId()+"> 处理 </a>  ", Whatyoudo.TYPE_SELF);
		//this.showMyFriends();
		//BizObject e = new BizObject("employee");
		//e.set("username", friend.getString("username"));
		BizObject fparam = new BizObject("employee");
		fparam.set("userid", friend.getId());
		this.setQueryParam(fparam);
		this.search();
	}

	/**
	 * 拒绝好友申请
	 * @throws SQLException 
	 */
	public void refuseFriend() throws SQLException{
		String friendid = this.getParameter("friendid");
		BizObject ref = new BizObject("re_friend");

		ref.set("friendid", friendid);
		ref.set("userid", this._curuser.getId());
		//this._curuser.set("ddd", 11);
//		ref.set("friendid", this._curuser.getId());
//		ref.set("userid", friendid);
		
		ref = ref.getQF().getOne(ref);
		
		 if(ref!=null){
			 ref.set("state", -1);
			 this.getJdo().update(ref);
			 Whatyoudo.addUserWyd(friendid,"<a href=/basic.UserActionHandler.showOne?userid="+_curuser.getId()+"> "+_curuser.getString("username")+ "</a> 拒绝了你的好友申请", Whatyoudo.TYPE_SELF);
		 }
		 this.listMyFriendApplys();
	}
	
	/**
	 * 拒绝团队邀请
	 * @throws SQLException 
	 */
	public void refuseTeam() throws SQLException{
		String teamid = this.getParameter("teamid");
		BizObject ref = new BizObject("re_team_user");

		ref.set("teamid", teamid);
		ref.set("userid", this._curuser.getId());
		//this._curuser.set("ddd", 11);
//		ref.set("friendid", this._curuser.getId());
//		ref.set("userid", friendid);
		
		ref = ref.getQF().getOne(ref);
		
		 if(ref!=null){
			 ref.set("state", -1);
			 this.getJdo().update(ref);
			 Whatyoudo.addTeamWyd(teamid,"<a href=/basic.UserActionHandler.showOne?userid="+_curuser.getId()+"> "+_curuser.getString("username")+ "</a> 拒绝加入本团队", Whatyoudo.TYPE_TEAM);
		 }
		 this.listMyTeamInvites();
	}	
	/**
	 * 接受好友申请
	 * @throws SQLException 
	 */
	public void acceptFriend() throws SQLException{
		String friendid = this.getParameter("friendid");
		BizObject ref = new BizObject("re_friend");
		ref.set("friendid", friendid);
		ref.set("userid", this._curuser.getId());
		
		ref = ref.getQF().getOne(ref);
		logger.info("ref is "+ref);
		 if(ref!=null){
			 ref.set("state", 1);
			 this.getJdo().update(ref);
			 Whatyoudo.addUserWyd(friendid,"<a href=/basic.UserActionHandler.showOne?userid="+_curuser.getId()+"> "+_curuser.getString("username")+ "</a> 同意了你的好友申请", Whatyoudo.TYPE_SELF);
		 }
		 
		 /**
		  * 他成为我的好友，我也成为他的好友
		  */
		 BizObject ref2 = new BizObject("re_friend");
			ref2.set("friendid", this._curuser.getId());
			ref2.set("userid", friendid);
			logger.info("ref2 "+ref2);
			//ref2 = ref2.getQF().getOne(ref2);
			if (ref2.getQF().query(ref2).size()==0){
				 ref2.set("state", 1);
				 this.getJdo().add(ref2);
				
			}

			
		//	this._request.
			this.listOneFriendApplys(this._curuser.getId(),"");
			this._employee.refreshUserInfo(Employee.MY_FRIENDS);
			this._employee.refreshUserInfo(Employee.MY_FRIENDAPPLYS);
	}

	/**
	 * 接受团队邀请
	 * @throws SQLException 
	 */
	public void acceptTeam() throws SQLException{
		String teamid = this.getParameter("teamid");
		BizObject ref = new BizObject("re_team_user");
		ref.set("teamid", teamid);
		ref.set("userid", this._curuser.getId());
		
		ref = ref.getQF().getOne(ref);
		logger.info("ref is "+ref);
		 if(ref!=null){
			 ref.set("state", 1);
			 this.getJdo().update(ref);
			 Whatyoudo.addTeamWyd(teamid,"高手 <a href=/basic.UserActionHandler.showOne?userid="+_curuser.getId()+"> "+_curuser.getString("username")+ "</a> 同意加入本团队", Whatyoudo.TYPE_TEAM);
		 }
		 
			
		//	this._request.
			this.listMyTeamInvites();
			this._employee.refreshUserInfo(Employee.MY_TEAMS);
			//this._employee.refreshUserInfo(Employee.MY_FRIENDAPPLYS);
	}
	
	/**
	 * 等待审核列表
	 * @throws SQLException
	 */
	public void listWaitVerify() throws SQLException{
		this.setHardcoreFilter(" type=1 and state=0 ");
		List v = this.listObj("employee");
		this.setAttribute("waitverify", v.size());
		this._nextUrl = "/basic/userList.jsp";
	}
	
	/**
	 * 删除我的好友
	 * @throws SQLException 
	 */
	public void delMyFriend() throws SQLException{
		//BizObject ref = this.getBizObjectFromMap("re_friend");
		BizObject ref = new BizObject("re_friend");//this.getBizObjectFromMap("re_friend");
		ref.set("friendid", this.getParameter("friendid"));
		ref.set("userid", this._curuser.getId());
		logger.info("qry ref is "+ref);
		ref= ref.getQF().getOne(ref);
		logger.info("ref is "+ref);
		if(ref!=null)
			this.getJdo().delete(ref);
		
		//Whatyoudo.addUserWyd(_curuser.getId(), "申请和 <a href=/basic.UserActionHandler.showOne?userid="+friend.getId()+"> "+friend.getString("username")+"</a>做好友", Whatyoudo.TYPE_FRIENDSHIP);
		//Whatyoudo.addUserWyd(friend.getId(),_curuser.getString("username")+ "希望和你成为好友， <a href=/basic.UserActionHandler.listMyFriendApplys?friendid="+this._curuser.getId()+"> 处理 </a> <a href=/basic.UserActionHandler.refuseFriend?friendid="+friend.getId()+"> 拒绝 </a> ", Whatyoudo.TYPE_SELF);
		//Whatyoudo.addUserWyd(friend.getId(),_curuser.getString("username")+ "希望和你成为好友， <a href=/basic.UserActionHandler.listMyFriendApplys?friendid="+this._curuser.getId()+"> 处理 </a>  ", Whatyoudo.TYPE_SELF);
		//this.showMyFriends();
		//BizObject e = new BizObject("employee");
		//e.set("username", friend.getString("username"));
//		BizObject fparam = new BizObject("employee");
//		fparam.set("userid", friend.getId());
//		this.setQueryParam(fparam);
		this.showMyFriends();
		//this.search();
		
	}
	
	/**
	 * 离开某个团队
	 * @throws SQLException 
	 */
	public void leaveTeam() throws SQLException{
		BizObject re = new BizObject("re_team_user");
		
		re.set("teamid", this.getParameter("teamid"));
		re.set("userid", this._curuser.getId());
		re= re.getQF().getOne(re);
		
		if (re.getBizObj("teamid").getString("leader").equals(this._curuser))
			throw new ControllableException("对不起，你是该团队团长，退团前请先指定其他人做团长");
		this.getJdo().delete(re);
		this.showMyTeams();
	}	
	
	public void saveMyState() throws SQLException, ServletException{
		BizObject user=this.getBizObjectFromMap("employee");
		if(user!=null) {
			user.set("userid", this._curuser.getId());
			this.getJdo().update(user);
			this._curuser.refresh();	
		}
		
		if(!user.getString("signaturebefore").equals(user.getString("signature")))			
			Whatyoudo.addUserWyd(user.getId(), "的新签名："+user.getString("signature"), Whatyoudo.TYPE_SIGNATURE);
		
		this.showMe();
	}
	
	/**
	 * 保存当前用户
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void saveMe() throws ServletException, SQLException {
		
		//this.save();
		BizObject user=this.getBizObjectFromMap("employee");
		//user.getQF().query(this.preparePageVar())
		
		if(user!=null) {
			
			if(user.getString("password").equals("")){
				user.set("password", null);  //防止修改密码
			}
			else
				user.set("password", new Crypto().des(user.getString("password")));
			
//			logger.info("emp is " + emp);
//			//this.clearEmptyParam(emp);
//			System.out.println("v size is " + v.size());
//			if (emp.get("status")!=null&&emp.getString("status").equals("")) {
//				emp.set("status", 0);
//			}
//			if (emp.getString("status").equals("1")) {
//				emp.set("stoptime", new Date());
//			}	
//			employee.setRoles(v);
//			employee.setEmployee(emp);
//			employee.save();
			
			logger.info("update user "+user);
			this.getJdo().update(user);
			this._curuser.refresh();	
		}
		//this._curuser.set("signature",user.getString("signature"));
		
		List<File> files = this.getUploadFiles();
		File file = null;
		if (files.size()>0){
			
			BizObject annex=new BizObject("annex");
			List<BizObject> v =annex.getQF().query("bizid",_curuser.getId());
			if(v.size()>0)
				annex=v.get(0);
			file = files.get(0);			
			//BizObject annex=new BizObject("annex");
			annex.set("bizid", objId);
			annex.set("type", Document.TYPE_USERPIC);
			Document.updateAnnex(annex, file,1);			
			_curuser.set("photoid", annex.getId());
			
		}
		

//		List<BizObject> v = this.getBizObjectWithType("concern");
//
//		if(v!=null){
//			// 删除所有擅长
//			this.getJdo().resetObjType("concern");
//			int i=this.getJdo().delete("userid", this._curuser.getId());
//			logger.info("delete "+i);
//		}
//
//		String concernstr="";
//		logger.info("v size is "+v.size());
//		for(BizObject concern :v){
//			concern.set("userid", this._curuser.getId());
//			concernstr=concernstr+concern.getString("taskclassid")+",";
//			this.getJdo().addOrUpdate(concern);
//		}
//		this._curuser.set("concern", concernstr);
		this.getJdo().update(_curuser);
//		if(!user.getString("signaturebefore").equals(user.getString("signature"))){
//			
//			Whatyoudo.addUserWyd(user.getId(), "的新签名："+user.getString("signature"), Whatyoudo.TYPE_SIGNATURE);
//		}
//		
		//this.getJdo().commit();
		this.showUserInfo();
	}
	
	//public void showMyTeam
	
	/**
	 * 显示当前用户图片
	 * @throws SQLException 
	 */
	public void showMyPic() throws SQLException{
		this.showUserPic(this._curuser.getString("photoid"));		
	}
	/**
	 * 显示指定用户图片
	 * @throws SQLException 
	 */
	public void showUserPic() throws SQLException{
		//this._dispatched=true;
		String pid= this.getParameter("photoid");
		this.showUserPic(pid);
	}

	/**
	 * 显示指定用户图片
	 * @throws SQLException 
	 */
	public void showUserPic(String pid) throws SQLException{
		//this._dispatched=true;
		//String pid= this.getParameter("photoid");
		this._dispatched=true;
		logger.info("pid"+pid);
		if(pid==null||pid.equals(""))
			pid="ANNE14514412";
		DocumentAH.download(pid, _response);
	}

	/**
	 * 显示一个用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showUser() throws ServletException, SQLException {

		Employee employee = new Employee(this._curuser, this.getJdo());
		employee.refreshById(objId);
		
		
		//得到该用户部门,一个用户多个部门
		BizObject obj=employee.getEmployee();
		if(obj!=null){
			String userid=obj.getId();
			//obj.set("depts",this.findDeptsOfUser(userid));
		}

		this._request.setAttribute("obj", employee.getEmployee());

		List<BizObject> myroles =employee.getRoles();
		List myroleIds =new ArrayList();
		for(BizObject b:myroles)
			myroleIds.add(b.getId());

//		BizObject change = new BizObject("wealthlog");
//		change.set("userid", objId);
//		change.setOrderBy("createdate desc");
//		List v = change.getQF().query(change);
//		this.setAttribute("changelist", v);
		// 需返回所有角色，并且用remark字段来区分是否checked
		QueryFactory roleQF = new QueryFactory("role");
		List roles = roleQF.query();
		// logger.debug("size is "+roles.size());
		logger.info("my roles is "+myroleIds);

		for (int i = 0; i < roles.size(); i++) {
			BizObject role = (BizObject) roles.get(i);
			//logger.info("role "+role.getId());
			if (myroleIds.contains(role.getId())){
				logger.info("role "+role.getId());
				
				role.set("creator", role.getID());
			}
			else{
				if(role.getId().equals(Role.DEFAULT_ID)){
					role.set("creator", role.getId());
				}
			}
				
		}
		List annexs = this.queryAnnex(objId);
		logger.info("annexs size is "+annexs.size());
		if(annexs.size()>0)
			this._request.setAttribute("annexs", annexs);

		System.out.println("objId==============" + objId);
		//this.showUserRecord(objId);
		this._request.setAttribute("roles", roles);
		this._request.setAttribute("dept_tree",DeptAH.getCascadeDept());
		this._nextUrl = "/template/basic/admin/userOper.jsp";

	}

	
	public void edtMailBox() throws SQLException{
		this.pushLocation("收件箱", "/basic.UserActionHandler.edtMailBox");
	
		List<BizObject> v = this._employee.getOneFriends(_curuser.getId(),new PageVariable(-1));
		logger.info(" friends size "+v.size());
		ActionHandler.currentRequest().setAttribute("friendList", v);
		
		this._nextUrl="/user/user_center_edtmsg.jsp";
	}
	
	public void showMyFriends() throws SQLException{
		this.pushLocation("我的好友","/basic.UserActionHandler.showMyFriends");
		this.showMe();
		this.showOneFriends(_curuser.getId());
	}
	/**
	 * 显示 加我为好友的申请
	 * @throws SQLException
	 */
	public void listMyFriendApplys() throws SQLException{
		this.pushLocation("我的好友申请","/basic.UserActionHandler.showMyFriends");
		//this.showMe();
		this.listOneFriendApplys(_curuser.getId(),this.getParameter("friendid"));
	}	
	/**
	 * 显示 加我为团队邀请
	 * @throws SQLException
	 */
	public void listMyTeamApplys() throws SQLException{
		this.pushLocation("我的好友申请","/basic.UserActionHandler.listMyTeamApplys");
		//this.showMe();
		this.listOneFriendApplys(_curuser.getId(),this.getParameter("friendid"));
	}
	/**
	 * 显示 加我为好友的申请
	 * @throws SQLException
	 */
	public void listMyTeamInvites() throws SQLException{
		this.pushLocation("我的团队邀请","/basic.UserActionHandler.listMyTeamInvites");
		//this.showMe();
		List<BizObject> v =  this._employee.getOneTeamInvites(this._curuser.getId());//reteam.getQF().query(reteam);
		
		this.setAttribute("inviteList", v);
		
		//this._nextUrl="/user/user_center_teams.jsp";
		this._nextUrl="/user/team_invite_audit.jsp";	

	}	
	/**
	 * 取得我的团队列表
	 * @throws SQLException 
	 */
	public void showMyTeams() throws SQLException{
		this.pushLocation("我的团队","/basic.UserActionHandler.showMyTeams");
		this.showMe();
//		BizObject reteam=new BizObject("re_team_user");
//		reteam.set("userid", _curuser.getId());
//		reteam.set("state", "1");
//		List<BizObject> v = reteam.getQF().query(reteam);
		
		List<BizObject> v = this._employee.getOneTeams(this._curuser.getId(),this.preparePageVar());
		this.setAttribute("teamList", v);
		
		this._nextUrl="/user/user_center_teams.jsp";
	}	

	
	public void showOneFriends(String userid) throws SQLException{
		//this.pushLocation("我的好友","/basic.UserActionHandler.showOneFriends");
		//this.showOne(userid);
//		BizObject refriend=new BizObject("re_friend");
//		refriend.set("userid", userid);
//		refriend.set("state", 1);
//		List<BizObject> v = refriend.getQF().query(refriend);
		
		List<BizObject> v = this._employee.getOneFriends(userid,this.preparePageVar());
		logger.info(" friends size "+v.size());
		ActionHandler.currentRequest().setAttribute("friendList", v);
		ActionHandler.currentActionHandler()._nextUrl="/user/user_center_friends.jsp";
	//	return v.size();
		
	}
	public void listOneFriendApplys(String userid,String friendid) throws SQLException{
		//this.pushLocation("我的好友","/basic.UserActionHandler.showOneFriends");
		//this.showOne(userid);
		BizObject refriend=new BizObject("re_friend");
		refriend.set("userid", userid);
		if(!friendid.equals(""))
			refriend.set("friendid", this.getParameter("friendid"));
		refriend.set("state", "0");
		logger.info("re_friend is "+refriend);
		List<BizObject> v = refriend.getQF().query(refriend);
		
		logger.info(" friends size "+v.size()+"  "+refriend.getQF().getSql());
		this.setAttribute("friendList", v);
		this._nextUrl="/user/friends_audit.jsp";	
		
	}
	public void showOneTeams(String userid) throws SQLException{
		//this.pushLocation("我的好友","/basic.UserActionHandler.showOneFriends");
		//this.showOne(userid);
		BizObject reteam=new BizObject("re_team_user");
		reteam.set("userid", userid);
		reteam.set("state", 1);
		List<BizObject> v = reteam.getQF().query(reteam);
		this.setAttribute("teamList", v);
		
		this._nextUrl="/user/user_center_teams.jsp";
		
	}	
	/**
	 * 取得我的好友列表
	 */
	public void getMyFriends(){
		
	}
	/**
	 * 删除某个物料对应的某一附件
	 */
	public void deleteAnnex() throws SQLException, ServletException {
		BizObject annex = this.getBizObjectFromMap("annex");
		try {
//			String id = annex.getString("objId");
//			String bizid = annex.getString("bizid");
//			String type = this.getParameter("annex$type");
//			ActionHandler.currentRequest().setAttribute("type", type);
//			annex.set("type", type);
			
			// 删除某个指定附件
			Document.deleteAnnex(annex.getId());
			annex.set("id", null);
			this._tipInfo = "删除成功！";
			// 显示某个物料对应的附件列表
			
		} catch (SQLException sqle) {
			logger.error("error",sqle);
			if (sqle.getErrorCode() == 2292) {
				this._tipInfo = "无法删除，此附件被使用！";
			}
			else
				throw sqle;
		}
		this.showUser();
	}
	
	public List<BizObject> queryAnnex(String BizId) throws SQLException {
		QueryFactory anQF = new QueryFactory("annex");
		BizObject annex = new BizObject("annex");
		annex.set("BizID", BizId);
		annex.set("history", "1");
		annex.set("deleted", "is null");
	//	annex.set("suffix", ".jpg");
		
		List<BizObject> ans = anQF.query(annex, ActionHandler.currentSession().getCon());
		return ans;
	}

	/**
	 * 显示用户中心页面
	 * @throws SQLException 
	 * @throws ServletException 
	 */
	public void showUserCenter() throws ServletException, SQLException{	
		this.showMe();
		//this.setAttribute("title", "usercenter");

	}
	
//	public UTaskService getTaskService() {
//		return (UTaskService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("uda.taskService");
//	}

	/**
	 * 显示用户信息修改页面
	 * @throws SQLException 
	 * @throws ServletException 
	 */
	public void showUserInfo() throws ServletException, SQLException{
		this.pushLocation("修改用户信息","/basic.UserActionHandler.showUserInfo");
		this.showMe();
		//this.setAttribute("title", "usercenter");
//		/**
//		 * 如果是企业用户
//		 */
//		if(_curuser.getString("type").equals("1"))
//			this._nextUrl="/user/company_info.jsp";			
//		else
//			this._nextUrl="/user/user_center_info.jsp";
//		//this._nextUrl="/user/user_center_info.jsp";
		
		this._nextUrl="/template/user_center/account_settings.jsp";
	}
	/**
	 * 
	 * 退出重新登陆
	 */
	public void exit() {
		_request.getSession().removeAttribute("user");
		_request.getSession().removeAttribute("employee");
		_request.getSession().removeAttribute("module");
		
		// 当前用户(已转换成BizObject对象,以供内部资源网页面使用)

		_request.getSession().removeAttribute("curuser");		
		_request.getSession().removeAttribute("userInfo");
		
		_request.getSession().invalidate(); //彻底销毁session
		
		//logger.info("curuser is "+_request.getSession().getAttribute("curuser"));
		this._nextUrl = "/";

	}
	
	/**
	 * 显示我的桌面
	 * @throws SQLException 
	 */
	public  void listMyDesktop() throws SQLException{
		this.setAttribute("objList", getMyDesktop());
		this._nextUrl="/system.track/desktop2.jsp";
	}
	
	public static List getMyDesktop() throws SQLException{
		BizObject d = new BizObject("Desktop_Save");
		d.set("users", ActionHandler.currentUser().getEmployee().getId());
		return  d.getQF().query(d);
		
	}
	
	/**
	 * 取所有的function
	 * @return
	 * @throws SQLException
	 */
	public static List getAllDesktop() throws SQLException{
		BizObject d = new BizObject("desktop_item");
		//d.set("users", ActionHandler.currentUser().getEmployee().getId());
		List<BizObject> v = d.getQF().query();
		List<BizObject> userv = getMyDesktop();
		for(BizObject b:v){
			for(BizObject b2:userv){
				if (b.getString("id").equals(b2.getString("function"))){
					b.set("show", b2.getString("show"));
					b.set("ico", b2.getString("ico"));
				}
					
			}
		}
		return v;
		
	}
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void listUser() throws ServletException, SQLException {
    	super.listObj();
    	
		this._nextUrl = "/template/basic/userList.jsp";
	}
	

	/**
	 * 签到
	 * @throws SQLException 
	 */
	public void signIn() throws SQLException{
		insert(this._curuser,"0");
		throw new InfoException("上班签到成功！   签到时间："+Utility.getFormatedDate(new Date()));
	}
	/**
	 * 签出
	 * @throws SQLException 
	 */
	public void signOut() throws SQLException{
		insert(this._curuser,"1");
		throw new InfoException("下班签到成功！   签到时间："+Utility.getFormatedDate(new Date()));
	}
	
	/**
	 * 成功返回 true 失败返回 false
	 * 
	 * @param result
	 * @param jdo
	 * @throws SQLException 
	 */
	public  boolean insert(BizObject user,String inout) throws SQLException {
		boolean ret = false;
		
//		if (_jdo == null)
//			_jdo = new JDO("rollbook");

		
		//try {
			
			BizObject rollbook = new BizObject("rollbook");

			Calendar c = Calendar.getInstance();
			rollbook.set("day", c.get(Calendar.DAY_OF_MONTH));
			rollbook.set("month", c.get(Calendar.MONTH)+1);
			rollbook.set("year", c.get(Calendar.YEAR));
			
			rollbook.set("departid", user.getString("deptid"));
			rollbook.set("time", c.getTime());
			rollbook.set("userid", user.getId());
			rollbook.set("effect", "1");
			rollbook.set("doorid", 0);
			rollbook.set("cardno", user.getString("cardno"));
			
			rollbook.set("hour", c.get(Calendar.HOUR_OF_DAY));
			rollbook.set("minute",  c.get(Calendar.MINUTE));
			   
			rollbook.set("inout", inout); //上班还是下班
			 //周几        
			int hour=c.get(Calendar.DAY_OF_WEEK); 
//			 规定的上班时间
			int ontime = 9 * 60;	
			//中午12点
			int ontimetw=12*60;
			//迟到十五分钟
			int ontimefif=9*60+15;
			//迟到30分钟
			int ontimethir=9*60+30;
			
//			实际打卡时间
			int ontime1=c.get(Calendar.HOUR_OF_DAY)*60+c.get(Calendar.MINUTE);
//			 规定的下班时间
			int offtime1 = 17 * 60 + 45;
			//早退一个小时
			int offtime2 = 16 * 60 + 45;
			String day=c.get(c.YEAR) + "-" + (c.get(c.MONTH) + 1) + "-"
			+ (c.get(c.DAY_OF_MONTH));
			QueryFactory hqf=new QueryFactory("holiday");
			hqf.setHardcoreFilter(" dates='"+day+"' ");
			List holidayList=hqf.query();
			String sign="1";
			if(holidayList.size()>0){
				BizObject holiday=(BizObject) holidayList.get(0);
				sign=holiday.getString("state");
			}
			if(sign.equals("1")){
				if(ontime1<=ontime){
					//正常上班
					rollbook.set("am",  0);
				}else if(ontime1>ontime&&ontime1<=ontimefif){
					//迟到十五分钟内
					rollbook.set("am",  1);
				}else if(ontime1>ontimefif&&ontime1<=ontimethir){
					//迟到十五至三十分钟
					rollbook.set("am",  2);
				}
				else if(ontime1>ontimethir&&ontime1<=ontimetw){
					//迟到三十分钟以上
					rollbook.set("am",  3);
				}else if(ontime1>ontimetw&&ontime1<offtime1){
					//早退
					BizObject rollbook1 = new BizObject("rollbook");
					QueryFactory qf=new QueryFactory("rollbook");
					qf.setHardcoreFilter("year='"+c.get(Calendar.YEAR)+"' and month='"+(c.get(Calendar.MONTH)+1)+"' and day='"+c.get(Calendar.DAY_OF_MONTH)+"'" +
							" and hour<12 and userid='"+user.getId()+"'");
					List list=qf.query();
					if(list.size()>0){
						//上午打卡，下午早退
						if(ontime1>=offtime2){
							//早退一小时以内
							rollbook.set("pm",  1);
						}else{
							//早退一小时以上
							rollbook.set("pm",  2);
						}
						
					}else{
						//上午未打卡，下午打卡（早退）
						if(ontime1>=offtime2){
//							//早退一小时以内
							rollbook.set("pm",  3);
						}else{
//							//早退一小时以上
							rollbook.set("pm",  4);
						}
					}
					
				}else if(ontime1>=offtime1){
					//正常下班
					rollbook.set("pm",  0);
				}
				
			}
			
			//BizObject rollBook = parse(result.trim());
			

			//System.out.println("add rollbook " + rollbook);
			
			this.getJdo().add(rollbook);
			
			ret = true;
			//return true;
			// i++;

//		} catch (Exception e) {
//			// 出错后马上退出，把损失减到最小，因为芯片里的数据读了就没有了
//			e.printStackTrace();
//			logger.error(e);//JDO.getStackTrace(e);
//			ret = false;
//			// break;
//		}

		return ret;

	}
	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void search() throws  SQLException {
       // this.setHardcoreFilter("SCRAP !='0'  or  SCRAP  is  null ");
		//logger.info("user"+this.getBizObjectFromMap("employee")+this.getParameter("employee$username"));
		BizObject employee = this.getBizObjectFromMap("employee");
		logger.info("search friend param "+employee);
		//employee.set("state", )
		List<BizObject> v=super.listObj("employee");
		for(BizObject b:v){			
			logger.info(b.getId()+"  "+this._employee.isMyFriend(b.getId()));
			b.set("joined", this._employee.isMyFriend(b.getId()));
		}
		if(!this.getParameter("taskId").equals(null)) 
			this.setAttribute("taskId", this.getParameter("taskId"));
		this._nextUrl = "/user/user_center_search.jsp";
	}
	
	public void RecommendTask()throws SQLException{	
		System.out.println("??"+this.getParameter("taskId")+"  "+this.getParameter("userid")+"  "+ActionHandler.currentUser().getEmployee().getId());
		
//		this.getTaskMemberService().addRecommendTask(this.getParameter("taskId"),
//											  this.getParameter("userid"),
//											  ActionHandler.currentUser().getEmployee().getId(),
//											  this.getJdo());
		
		System.out.println("??");
		
		this._nextUrl = "/user/user_center_recmdsuccess.jsp";
	}
	
//	public TaskMemberService getTaskMemberService() {
//		if(taskMemberService == null) 
//			taskMemberService = (TaskMemberService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("uda.taskMemberService");
//		return taskMemberService;
//	}
//	
//	public void setTaskMemberService(TaskMemberService taskMemberService) {
//		this.taskMemberService = taskMemberService;
//	}
	
	/**
	 * 
	 * 
	 */
	public void delCard(String cardno) {

	}

	// 保存user
	public void save() throws ServletException, SQLException {
		this.getJdo().beginTrans();
		
		System.out.println("调用save()");
		// this.validateCanDo("usersave");
		String birthday = this._request.getParameter("EMPLOYEE$birthday");
		logger.info("birthday " + birthday);
		BizObject emp = this.getBizObjectFromMap("EMPLOYEE");
		this.checkParam(emp);		
		List v = this.getBizObjectWithType("role");
		Employee employee = new Employee(this._curuser, this.getJdo());
		// if (employee.refreshById(emp.getId())!=null)

		logger.info("emp is " + emp);
		this.clearEmptyParam(emp);
		System.out.println("v size is " + v.size());
		employee.setRoles(v);
		employee.setEmployee(emp);
		employee.save();
		this.objId = employee.getEmployee().getID();
		
		List<File> files = this.getUploadFiles();
		File file = null;
		if (files.size()>0){
			
			file = files.get(0);
			BizObject annex=new BizObject("annex");
			annex.set("bizid", objId);
			Document.updateAnnex(annex, file);			
			emp.set("photoid", annex.getId());
			this.getJdo().update(emp);
		}
		
		this.objId = employee.getEmployee().getID();
		
		
		//得到替换部门的id
//		String deptId=this.getParameter("EMPLOYEE$deptid");
//		if(!deptId.equals("")){
//			this.replaceDeptsOfUser(this.objId, deptId);
//		}
//		
		this.clearQueryParam();
		// this.listUser();
		this._tipInfo = "操作成功";
		this.showUser();

	}

//	public void paintChart(JFreeChart chart) throws IOException {
//		this._dispatched = true;
////		ChartUtilities.writeChartAsJPEG(this._response.getOutputStream(), 1,
////				chart, 400, 300, null);
//	}

	public void showPic() throws IOException {
		BarChartDemo bd = new BarChartDemo();
		// bd.write(this._response.getOutputStream());
//		this.paintChart(bd.createChart());
	}

	public void showPic2() throws IOException {
		PieChartDemo bd = new PieChartDemo();
		// bd.write(this._response.getOutputStream());
		//this.paintChart(bd.createChart());
	}
	
//	public void test1(){
//		
//		BizObject role = new BizObject("erplog");
//		//JDO ro = new JDO(role);
//		
//
//		//QueryFactory qf = new QueryFactory(role);			
//			Runnable openquery = new Runnable() {
//				public void run() {
//					//JDO jdo = new JDO("reportStatus");
//					
//					BizObject role = new BizObject("erplog");
//					JDO ro = new JDO(role);
//					QueryFactory qf = new QueryFactory(role);
//					try {
//						String id ="ERPL7077183";
//						int ci =2000;
//						long l = System.currentTimeMillis();					
//						for(int i=0;i<ci;i++){
//							
//							ro.getByID(id);
//						}
//						System.out.println();
//						System.out.println("jdbc haoshi "+(System.currentTimeMillis()-l));
//						 l = System.currentTimeMillis();
//						ro.beginTrans();
//						for(int i=0;i<ci;i++){
//							
//							ro.getByID(id);
//						}
//						System.out.println();
//						System.out.println("Jdbc2 haoshi "+(System.currentTimeMillis()-l));
//						
//						ro.commit();
//						l = System.currentTimeMillis();
//					
//						for(int i=0;i<ci;i++){
//							
//							qf.getByID(id);
//						}
//
//						
//
//						System.out.println();
//						System.out.println("cache jcs haoshi "+(System.currentTimeMillis()-l));
//						
//						
//						
//						l = System.currentTimeMillis();
//						Global.mcc.set(id, ro.getByID(id));
//						for(int i=0;i<ci;i++){
//							Global.mcc.get(id);
//							//ro.getByID("ERPL7024904");
//						}
//
//						
//
//						System.out.println();
//						System.out.println("cache mem haoshi "+(System.currentTimeMillis()-l));
//						
//					} catch (Exception e) {} finally {
//						
//						ro.close();
//					}
//				}
//			};
//			for(int x=0;x<10;x++){
//				Thread splashThread = new Thread(openquery, "SplashThread");
//				splashThread.start();					
//			}
//		
//	}

	/**
	 * 模糊查询
	 * @throws SQLException
	 * @throws ServletException
	 */
	public void listUser4() throws SQLException, ServletException {
		List v = null;
		PageVariable pv = new PageVariable();
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {

			long start = System.currentTimeMillis();

			QueryFactory qf34 = new QueryFactory("material");
			//qf34.disableNestQuery();
			v = qf34.mquery(new BizObject("material"));

			System.out.println(v.size() + " use "
					+ (System.currentTimeMillis() - start));
		}
		System.out.println(v.size() + " use "
				+ (System.currentTimeMillis() - start1));

	}
	/**
	 * 精确查询带con
	 * @throws SQLException
	 * @throws ServletException
	 */
	public void listUser5() throws SQLException, ServletException {
		List v = null;
		PageVariable pv = new PageVariable();
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {

			long start = System.currentTimeMillis();

			QueryFactory qf34 = new QueryFactory("materialverify");
			//qf34.disableNestQuery();
			v = qf34.query(new BizObject("materialverify"),getJdo().getCon());

			System.out.println(v.size() + " use "
					+ (System.currentTimeMillis() - start));
		}
		System.out.println(v.size() + " use "
				+ (System.currentTimeMillis() - start1));

}

	/**
	 * 模糊查询不带con
	 * @throws SQLException
	 * @throws ServletException
	 */
	public void listUser3() throws SQLException, ServletException {
			List v = null;
			PageVariable pv = new PageVariable();
			long start1 = System.currentTimeMillis();
			for (int i = 0; i < 100; i++) {

				long start = System.currentTimeMillis();

				QueryFactory qf34 = new QueryFactory("materialverify");
				//qf34.disableNestQuery();
				v = qf34.mquery(new BizObject("materialverify"));

				System.out.println(v.size() + " use "
						+ (System.currentTimeMillis() - start));
			}
			System.out.println(v.size() + " use "
					+ (System.currentTimeMillis() - start1));

	}

	public void listUser2() throws SQLException, ServletException {

		//	
		// QueryFactory qf = QueryFactory.getInstance("employee");
		// QueryFactory qf2 = QueryFactory.getInstance("employee");
		String type = this.getParameter("type");
		System.out.println("type " + type);
		// qf.BIZCACHEABLE=true;
		// qf.setBIZCACHEABLE(true);

		try {
			// JDO jdo = new JDO("employee");
			List v = null;
			PageVariable pv = new PageVariable();
			long start1 = System.currentTimeMillis();
			for (int i = 0; i < 30; i++) {

				long start = System.currentTimeMillis();

				if (type.equals("1")) {
					// qf.enableNestQuery();
					// qf2.disableNestQuery();
					// v =qf.query(jdo.getCon());
				}

				if (type.equals("2"))
					// v =qf.executeQuerySQL("select * from basic.employee",jdo.
					// getCon());
					if (type.equals("3")) {
						// qf.disableNestQuery();
						// v =qf.query(jdo.getCon());
					}
				if (type.equals("4")) {
					// qf.disableNestQuery();
					// v =qf.query(new BizObject("employee"),jdo.getCon());
				}
				if (type.equals("5")) {
					QueryFactory qf34 = new QueryFactory("materialverify");
				//	qf34.disableNestQuery();
					v = qf34.mquery(new BizObject("materialverify"));
				}
				if (type.equals("6")) {
					QueryFactory qf34 = new QueryFactory("employee");
					v = qf34.mquery(new BizObject("employee"));
				}

				// v
				// =qf.executeQuerySQL("select * from basic.employee",jdo.getCon
				// ());

				System.out.println(v.size() + " use "
						+ (System.currentTimeMillis() - start));
				// start = System.currentTimeMillis();
				// System.out.println(qf.executeQuerySQL(
				// "select * from  basic.employee",
				// jdo.getCon()).size()+" use "+
				// (System.currentTimeMillis()-start));

				// Thread.sleep(100);
			}
			System.out.println(v.size() + " use "
					+ (System.currentTimeMillis() - start1));
			// System.out.println(v.get(0));

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		// jdo.close();

	}

	/**
	 *添加用户对应的履历信息（培训内容，培训人及时间等）
	 * 
	 *@author xiangtiesha
	 * 
	 */
	public void addUserRecord() throws SQLException, ServletException {
		if (objId == "") {
			throw new ControllableException("请先对用户信息进行保存");
		}
		this._request.setAttribute("objId", objId);
		this._nextUrl = "/basic/newUserRecord.jsp";

	}

	/**
	 * 显示一个用户履历信息
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showUserRecord2(String objId) throws ServletException,
			SQLException {
		QueryFactory userrecordQF = new QueryFactory("userrecord");
		// String userid = this._request.getParameter("userrecord$userid");
		// if(objId==null||objId.equals("")){
		// this._nextUrl= "/basic/userOper.jsp";
		// }else{
		List records = userrecordQF.query("userid", objId);

		this._request.setAttribute("record", records);
		System.out.println("返回" + records.size());
		// }
	}
	
	public static void main(String[] args) {
		Crypto cry = new Crypto();
		System.out.println(cry.deDes("ff3fe2e028a2f1b5"));
	}

	/**
	 *保存用户对应的履历信息（培训内容，培训人及时间等）
	 * 
	 *@author xiangtiesha
	 * 
	 */
	public void saveUserRecord() throws SQLException, ServletException {

		BizObject userrecord = this.getBizObjectFromMap("userrecord");

		BizObject recorder = new BizObject("userrecord");
		recorder.set("userid", userrecord.getString("userid"));
		if (userrecord.getString("name") == ""
				|| userrecord.getString("trainer") == ""
				|| userrecord.getString("startdate") == ""
				|| userrecord.getString("enddate") == "") {
			throw new ControllableException("请完整填写用户履历信息");
		}
		// System.out.println("用户id"+userrecord.getString("userid"));
		// System.out.println("履历名称="+userrecord.getString("name"));
		// System.out.println("培训人="+userrecord.getString("trainer"));
		// System.out.println("时间="+userrecord.getString("startdate"));
		// System.out.println("时间2="+userrecord.getString("enddate"));
		this.getJdo().add(userrecord);
		this.showUser();
	}

	/**
	 *删除用户对应的履历信息（培训内容，培训人及时间等）
	 * 
	 *@author xiangtiesha
	 * 
	 */
	public void deleteUserRecord() throws SQLException, ServletException {

		String detailId = this._request.getParameter("detailObjId2");
		// String objid = this._request.getParameter("objId");
		BizObject userre = new BizObject("userrecord");
		userre.setID(detailId);
		this.getJdo().delete(userre);
		this._tipInfo = "删除成功！";
		this.showUser();
		// this.showObj(this._objType, objId);
		// this.showUserRecord(objId);
		// this._nextUrl = "/basic/userOper.jsp" ;
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
	

	//查询此人所属部门
	public String findDeptsOfUser2(String userid) throws SQLException, ServletException {
		String sql="select d.deptname as deptname from basic.re_user_dept r,basic.employee e,basic.department d " +
    		"where r.deptid=d.deptid and e.userid=r.userid and e.userid='"+userid+"' " +
    		"and (d.scrap<>'0' or d.scrap is null) and r.status='1'";
		List list=QueryFactory.executeQuerySQL(sql);
		String depts="";
		for(int j=0;j<list.size();j++){
			depts+=" "+((BizObject)list.get(j)).getString("deptname");
		}
		return depts;
	}
	
	//替换此人部门操作
	public void replaceDeptsOfUser(String userid,String replaceDeptid) throws SQLException, ServletException {
		QueryFactory qf=new QueryFactory("re_user_dept");
		qf.setHardcoreFilter("status='1' and userid='"+userid+"'");
		List list=qf.query();
		
		//更新以前的数据
		for(int i=0;i<list.size();i++){
			BizObject reuserdept=(BizObject)list.get(i);
			reuserdept.set("enddate",new Date());
			reuserdept.set("status","0");
			this.update(reuserdept);
		}
		
		//添加新的数据
		BizObject obj=new BizObject("re_user_dept");
		obj.set("userid",userid);
		obj.set("deptid",replaceDeptid);
		obj.set("startdate",new Date());
		obj.set("status","1");
		this.add(obj);
	}
	
	public void testMem() throws SQLException{
		

		//Mrp.test();


		this._tipInfo="测试完成";
		this._nextUrl="/success.jsp";
	}
	
	@Ajax
	public String queryUserOrDept() throws SQLException{
		String name = this.getParameter("name");
		String type = this.getParameter("type");
		String sql = "";
		if(type.equals("0")){//部门
			sql = "select e.USERID,e.USERNAME,d.DEPTID,d.DEPTNAME from basic.employee e INNER JOIN basic.department d " +
					"on e.DEPTID=d.DEPTID where d.deptname like '%"+name+"%'";
		}else if(type.equals("1")){//人员
			sql = "select e.USERID,e.USERNAME,e.telno as deptid,e.email as deptname from basic.employee e";

			if(!name.equals("")){
				sql =sql +
						" where e.USERNAME like '%"+name+"%'";
				
			}
		}
		logger.info("sql is "+sql);
		return QueryFactory.executeQuerySQL(sql.toString()).toString();
	}
	
	//	public DTaskService getDTaskService() {
//		if(dTaskService == null) 
//			dTaskService = (DTaskService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("uda.dTaskService");
//		return dTaskService;
//	}
//
//	public void setDTaskService(DTaskService dTaskService) {
//		this.dTaskService = dTaskService;
//	}

	//@Ajax
//	public String testLogout(){
//		RopSampleClient rsc = new RopSampleClient("1","1231431431");	
//		rsc.logout();//.logon("xieke","qjdble");
//		return "ok";
//	}
	
	
	/**
	 * 数据迁移，原持卡人门户数据迁入
	 */
	public void  impMy() {
		

		BizObject bb = new BizObject("cardholder");
		System.out.println(bb.toXml(""));
		System.out.println(bb.colsInfo);
		
		//JDO jdo = new JDO("employee");
		
			
			BizObject cardholder = new BizObject("cardholder");
			
			//List<BizObject> chlist;
			try 
			{
				this.getJdo().beginTrans();
				List<BizObject> chlist = cardholder.getQF().query();
				System.out.println(chlist.size());
				int i=0;
				
				for(BizObject u:chlist){
					BizObject emp=new BizObject("employee");
					emp.set("IDENTIFIER", u.getString("IDENTIFIER"));
					emp.set("username", u.getString("name"));
					emp.set("loginname", u.getString("loginname"));
					emp.set("password", u.getString("password"));
					emp.set("TELNO", u.getString("CELLPHONE"));
					emp.set("pwdtype", "md5") ;  //密码验证方式为md5
					emp.set("createdate", u.getDate("createtime"));
					emp.set("status", 0);
					logger.info(emp);
					this.getJdo().add(emp);
					i++;
				}
				
				BizObject admin = new BizObject("admin");			
				List<BizObject> adminlist = admin.getQF().query();
				System.out.println(adminlist.size());							
				
				for(BizObject u:adminlist){
					BizObject emp=new BizObject("employee");
					emp.set("IDENTIFIER", u.getString("IDENTIFIER"));
					emp.set("username", u.getString("name"));
					emp.set("loginname", u.getString("loginname"));
					emp.set("password", u.getString("password"));
					emp.set("TELNO", u.getString("CELLPHONE"));
					emp.set("pwdtype", "md5") ;  //密码验证方式为md5
					emp.set("isadmin", "1");
					emp.set("createdate", u.getDate("createtime"));
					emp.set("status", 0);
					logger.info(emp);
					this.getJdo().add(emp);
					i++;
				}
				this._tipInfo="数据迁移成功！！！ 共迁移数据 "+i+"条" ;
				

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.getJdo().rollback();
				this._tipInfo="出现错误 , 已经回滚！！！"+e.getMessage();
			}
			this._nextUrl="/success.jsp";
	}
}