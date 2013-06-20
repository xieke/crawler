package sand.actionhandler.basic;

import sand.actionhandler.system.*;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.*;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


import tool.dao.*;

/**
 * @author liustone
 * 
 * creatdate :2004-5-26 company :sand
 */
public class DeptAH extends ActionHandler {

	private static final String DEPT_ROOT_NAME = "root"; 
	
	static Logger logger = Logger.getLogger(DeptAH.class);

	/**
	 * 构造方法
	 * 
	 * @param p_Req
	 * @param p_Res
	 */
	public DeptAH(HttpServletRequest req,
			HttpServletResponse res) {
		super(req, res);
		this._objType = "DEPARTMENT"; // 本操作针对表DEPARTMENT
		this._moduleId = "system"; // 本模块针对ADMIN
	}

	/**
	 * 列表显示部门查询结果
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void listDepartment() throws ServletException, SQLException {
		this.setHardcoreFilter("SCRAP !='0'  or  SCRAP  is  null ");
		super.listObj();
		this._nextUrl = "/basic/departmentList.jsp";
	}

	
	@Ajax
	public String openDept() throws SQLException{
		String pid=this.getParameter("pid");
		BizObject dept = new BizObject("department");
		List<BizObject> subdepts= new ArrayList();
		if(pid.equals("")){
			dept.set("parentid", "is null");
			subdepts=dept.getQF().query(dept);
		}
			
		else{
			dept.set("parentid", pid);
			subdepts=dept.getQF().query(dept);
			
//			subdepts = dept.getList("parentid");	
		}		
		
//		 {id:'root',isLeaf:false,dataObject:{depotName:'世博集团'},userObject:{isGroup:true}}
//		 isLeaf:false,userObject:{isGroup:true}
//		 
		int i=0;
		String ret="[";
		for(BizObject sub:subdepts){
			if(i>0){
				ret=ret+",";
			}
			if (sub.getList("department").size()>0)
				ret=ret+"{id:'"+sub.getId()+"',pid:'"+pid+"',dataObject:{depotName:'"+sub.getString("deptname")+"',code:'"+sub.getString("code")+"'},userObject:{isGroup:true},isLeaf:false}";
			else
				ret=ret+"{id:'"+sub.getId()+"',pid:'"+pid+"',dataObject:{depotName:'"+sub.getString("deptname")+"',code:'"+sub.getString("code")+"'},userObject:{isGroup:true},isLeaf:true}";
			
			i++;
		}			
		ret=ret+"]";
		return ret;
	}	
	@Ajax
	@CandoCheck("departsave")
	public String addDept() throws SQLException{
		String deptname=this.getParameter("deptname");
		String pid=this.getParameter("pid");
		
		BizObject dept = new BizObject("department");
		dept.set("deptname", deptname);
		dept.set("parentid", pid);
		
		BizObject parent = dept.getQF().getByID(pid);
		//部门编码规则:除根公司第一层公司是三位编码以外(海航集团001,三位编码),其它的子公司或者部门都是上层公司+二位编码流水
		StringBuilder sql;
		
		if(parent==null){
			parent=new BizObject("department");
			parent.setID("root");
			parent.set("deptname", "root");
			parent.set("code", "00");
			this.getJdo().add(parent);
		}
		if(parent.getId().equals("root")){
			sql = new StringBuilder("select max(code) code from basic.department where code like '___' order by code desc");
			List<BizObject> list = QueryFactory.rawExecuteQuerySQL(sql.toString(), null);
			if(list.size()<=0 || StringUtils.isBlank(list.get(0).getString("code"))) dept.set("code", parent.getString("code")+"001");
			else {
				String str =  new Integer(list.get(0).getString("code")).intValue() + 1 + "";
				dept.set("code", this.checkFormat(str, 3));
			}
		}else{
			sql = new StringBuilder("select max(code) code from basic.department where code like '").append(parent.getString("code"))
					.append("__' order by code desc");
			List<BizObject> list = QueryFactory.rawExecuteQuerySQL(sql.toString(), null);
			if(list.size()<=0 || StringUtils.isBlank(list.get(0).getString("code"))) dept.set("code", parent.getString("code")+"01");
			else {
				String str = list.get(0).getString("code").substring(list.get(0).getString("code").length()-2, list.get(0).getString("code").length());
				str = new Integer(str).intValue() + 1 + "";
				dept.set("code", parent.getString("code")+this.checkFormat(str, 2));
			}
		}
		
		
		int i=parent.getInt("depth", 0);
		
		dept.set("depth", i+1);
		this.getJdo().add(dept);
		SystemKit.removeCache("department");
		return dept.getId()+"|"+dept.getString("code");
	}
	
	public String checkFormat(String no ,int len) {
		String str = "";
		for (int i = 0; i < len-no.length(); i++) {
			str = str.concat("0");
		}
		no = str + no;
		return no;
	}
	

	@Ajax
	@CandoCheck("departsave")
	public String delDept() throws SQLException{
		String pid=this.getParameter("pid");
		BizObject dept = new BizObject("department");
		dept.setID(pid);
//		dept.set("deptname", deptname);
//		dept.set("parentid", pid);
		if(dept.getList("department").size()>0){
			throw new ControllableException("存在子部门，不能删除");
		}
		if(dept.getList("employee").size()>0){
			throw new ControllableException("部门下存在员工，不能删除");
		}

		this.getJdo().delete(dept);
		SystemKit.removeCache("department");
		return "ok";
	}	
	
	@Ajax
	@CandoCheck("departsave")
	public String editDept() throws SQLException{

		String pid=this.getParameter("node_id");
		BizObject dept = new BizObject("department");
		dept.setID(pid);
//		dept.set("deptname", deptname);
//		dept.set("parentid", pid);
		dept.set("deptname", this.getParameter("new_name"));
		this.getJdo().update(dept);
		SystemKit.removeCache("department");
		return "ok";
	}	

	/**
	 * 取嵌套部门树
	 * @return
	 * @throws SQLException 
	 * 
	 */
	public static String getCascadeDept() throws SQLException{

		BizObject dept = new BizObject("department");
		
		//String sql = "select * from department where depth=1";
		dept.setID("root");
		dept.refresh();
		
		return getSubDepts(dept);
	}	
		
	public static String  getSubDeptTree(BizObject p) throws SQLException{
		
		String ret="";
		if(p==null)
			return "";
		List<BizObject> v = p.getList("department");
		if(v.size()>0){
			
			if(p.getId().equals("root"))
				 ret="{id : 'root' , data: '"+p.getString("deptname")+"',children:[";
			else
				ret="children:[";
		}
			
		int i=0;
		for(BizObject b:v){
			i++;
			//isLeaf
			String subtree=getSubDeptTree(b);
			
			if(!subtree.equals(""))
				ret=ret+"{id : '"+b.getId()+"' , data: '"+b.getString("deptname")+"',"+subtree+"}";
			else
				ret=ret+"{id : '"+b.getId()+"' , data: '"+b.getString("deptname")+"',isLeaf:true}";
				
				
			
			if(i<v.size())
				ret=ret+",";
			//ret=ret+"<li rel='"+b.getId()+"'> "+b.getString("deptname")+getSubDepts(b)+"</li>";
		}
		if(!ret.equals("")){
			if(p.getId().equals("root"))
				 ret=ret+"]}";
			else
				ret=ret+"]";
			
		}			
		
		return ret;
	}	

	@Ajax
	public static String getCascadeDeptTree() throws SQLException{

		BizObject dept = new BizObject("department");
		
		//String sql = "select * from department where depth=1";
		dept.setID("root");
		dept.refresh();
		
		return getSubDeptTree(dept);
	}		
	public static String getSubDepts(BizObject p) throws SQLException{
		
		String ret="";
		if(p==null)
			return "";
		List<BizObject> v = p.getList("department");
		if(v.size()>0){
			
			if(p.getId().equals("root"))
				 ret="<ul id=deptid_data class=mcdropdown_menu>";
			else
				ret="<ul>";
		}
			
		
		for(BizObject b:v){
			ret=ret+"<li rel='"+b.getId()+"'> "+b.getString("deptname")+getSubDepts(b)+"</li>";
		}
		if(!ret.equals(""))
			ret=ret+"</ul>";
		
		return ret;
	}
	
	/**
	 * 确显示一个部门
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */

	public void viewDepartment() throws ServletException, SQLException {
		super.showObj();
		// 目标页
		this._nextUrl = "/basic/departmentView.jsp";
	}

	/**
	 * 确定添加/修改一个部门
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */

	public void showDepartment() throws ServletException, SQLException {
		super.showObj();
//		QueryFactory qf = QueryFactory.getInstance("re_user_dept");
//		qf.setHardcoreFilter("status='1' and deptid='"+this._objId+"'");
//		List v =qf.query();
//		this.setAttribute("users", v);
		//this._objId
		// 目标页
		this._nextUrl = "/basic/departmentOper.jsp";
		//this._nextUrl = "/basic/index4_dep.jsp";
	}

	/**
	 * 处理添加或修改，由 objId 的值来决定
	 * 
	 * @param aso
	 */
	@CandoCheck("departsave")
	public void addOrUpdate() throws ServletException, SQLException {
		//this.validateCanDo("departsave");
		// this.validateCanAdd();
		// 取出参数
		_objId = this._request.getParameter("objId");
		BizObject obj = this.getBizObjectFromMap(this._objType);
		//logger.info("我的天啊"+obj);
		// 如果此对象未被组装，生成一个新的对象
		if (obj == null) {
			obj = new BizObject(this._objType);

			// 如果是添加
		}
		getJdo().beginTrans();

		if ((_objId == null) || _objId.equals("")) {
			logger.debug("add");
			this.add(obj);
			_objId = obj.getId();
		}
		// 如果是修改
		else {
			logger.debug("update");
			obj.setID(_objId);
			this.update(obj);
		}
		//不添加经理和分管领导
		/*
		if (!obj.getString("leader").equals("")){
			this.addUser(_objId,obj.getString("leader"));
		}
		
		if (!obj.getString("manager").equals("")){
			this.addUser(_objId,obj.getString("manager"));
		}
		if (!obj.getString("manager2").equals("")){
			this.addUser(_objId,obj.getString("manager2"));
		}
		if (!obj.getString("manager3").equals("")){
			this.addUser(_objId,obj.getString("manager3"));
		}
		*/
		getJdo().commit();
		// 返回添加\修改页
		//super.clearQueryParam();
		sand.depot.tool.system.SystemKit.removeCache("department");//清空缓存
		this.showDepartment();
		

//		this.listDepartment();
	}


	/**
	 * 删除一个部门成员
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("departsave")
	public void delUser() throws ServletException, SQLException {


		//BizObject detail = new BizObject("mntpart");
		//detail.setID(_detailId);
		BizObject d= this.getBizObjectFromMap("re_user_dept");
		BizObject user=new BizObject("re_user_dept");
		user.setID(d.getId());
		user.set("enddate",new Date());
		user.set("status","0");
		this.update(user);
		this.showDepartment();

	}	
	/**
	 * 添加一个部门成员
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("departsave")
	public void addUser() throws ServletException, SQLException {



		BizObject d = this.getBizObjectFromMap("re_user_dept");

		if (d.getString("userid").equals(""))
			throw new ControllableException("请选择用户");
		
		d.set("deptid", this._objId);
		BizObject k=new BizObject("re_user_dept");
		k.set("userid",d.getString("userid"));
		k.set("deptid",d.getString("deptid"));
		k.set("status","1");
		logger.info("d2 is "+d);
	    

		List v = new QueryFactory("re_user_dept").query(k);

		if(v.size()>0){
			throw new ControllableException("同样的用户已选择");
		}
			
		try {
			//改变以前所在的部门
			this.updDeptsOfUser(d.getString("userid"));
			//添加新的部门
			k.set("startdate",new Date());
			this.getJdo().add(k);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			if (sqle.getErrorCode() == 2291)
				_tipInfo = "无法添加，无此用户 ";
			else
				throw sqle;

		}
		this.showDepartment();

	}	
	/**
	 * 处理删除，
	 * 
	 * @param aso
	 */
	@CandoCheck("departsave")
	public void delete() throws ServletException, SQLException {
		// this.validateCanRemove();
//		try {
//			String objId = this._request.getParameter("objId");
//			this.delete(objId);
//			this._tipInfo = "删除成功！";
//		} catch (SQLException sqle) {
//			logger.error(JDO.getStackTrace(sqle));
//			logger.error(""+sqle.getErrorCode());
//			if (sqle.getErrorCode() == 2292) {
//				this._tipInfo = "无法删除该部门，已经被使用！";
//			}
//		}
		
		
		
		//删除部门 ,不是真正 删除,而是 置个状态标志位,查看 的时候 屏蔽 掉
		String objId = this._request.getParameter("objId");
		QueryFactory qf=new QueryFactory("DEPARTMENT");
		BizObject  department = (BizObject)qf.getByID(objId);
		department.set("scrap", "0");
		this.getJdo().update(department);
		
		//删除该 部门 下所有人员 和该 部门 的关系
		this._objType="re_user_dept";
		this.setHardcoreFilter("deptid='" + objId + "' and status='1'");
		logger.info("objid:" + objId );
		this.listObj();
		List<BizObject> objList=(List) this._request.getAttribute("objList");
		for(int i=0 ;i< objList.size();i++ ){
			BizObject reuserdept=(BizObject)objList.get(i);
			reuserdept.set("endate",new Date());
			reuserdept.set("status","0");
			this.update(reuserdept);
		}
		
		
		sand.depot.tool.system.SystemKit.removeCache("department");//清空缓存
		this._objType="DEPARTMENT";
		this.listDepartment();
	}
	
	//更新此人员相关的部门
	public void updDeptsOfUser(String userid) throws SQLException, ServletException {
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
	}
	
	@Ajax
	public String deptAndUserTree() throws SQLException{
		String deptId = this.getParameter("id");
		if(StringUtils.isBlank(deptId)){
			deptId = this.DEPT_ROOT_NAME;
		}
		String sql = "select d.deptid id,d.DEPTNAME name,'true' isParent from basic.department d where d.parentid='"+deptId+"' UNION " +
				"select e.USERID id,e.USERNAME name,'false' isParent from basic.employee e where e.DEPTID='"+deptId+"'";
		List<BizObject> list = QueryFactory.rawExecuteQuerySQL(sql, null);
		int i=0;
		String str="[";
		for(BizObject biz : list){
			if(i>0)  str += ",";
			str = str + "{isParent:"+biz.getString("isparent")+",id:'"+biz.getString("id")+"',name:'"+biz.getString("name")+"'}";
			i++;
		}
		str = str+"]";
		return str;
	}
	
	public static void main(String args[])throws SQLException{
//		InitialConn.initialTrue();
//		QueryFactory qf = QueryFactory.getInstance("employee");
//		List<BizObject> v = qf.query();
//		JDO jdo = new JDO("re_user_dept");
//		for(BizObject b:v){
//			System.out.println(b);
//			BizObject rud = new BizObject("re_user_dept");
//			rud.set("userid",b.getId());
//			rud.set("deptid", b.getString("deptid"));
//			jdo.add(rud);
//			
//			
//		}
		String str = "0010102";
		String no = str.substring(str.length()-2, str.length());
		System.out.println(no);
		
		
	}
}