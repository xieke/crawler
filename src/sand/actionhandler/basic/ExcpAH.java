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
import sand.depot.tool.system.AlarmException;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import tool.dao.util.ObjectProperty;
import tool.workflow.service.FlowCaseService;
import tool.workflow.service.impl.FlowCaseServiceImpl;

public class ExcpAH extends ActionHandler {
	
	//private Logger logger = Logger.getLogger("wyc.logger");
	public ExcpAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType ="exception";
		this._moduleId="basic";
		// TODO Auto-generated constructor stub
	}
	
	BizObject _exception = new BizObject("exception");
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
		_objDetailId = this.getParameter("objDetailId");
		logger.info("objid is "+_objId);
		logger.info("objdetailid is "+_objDetailId);
		try {
			if(!_objId.equals("")){
				_exception.setID(_objId);
				_exception.refresh();
				
				this.setAttribute("exception", _exception);
			}
			
			logger.info("exception is "+_exception);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("",e);
		}


	}
	private   Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 列表显示模版 
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void listExcp() throws ServletException, SQLException {
    
		this.setPageSize(-1);
		prepareUsers();
		super.listObj("exception");
		this._nextUrl = "/template/basic/exception/excpList.jsp";
	}
 
	/**
	 * 显示一个 模版 
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showExcp() throws ServletException, SQLException {
    
		//super.listObj("exception_detail");
		List<BizObject> v = _exception.getList("exception_detail","serialno");
		this.setAttribute("objList", v);
		this.setAttribute("exception", _exception);
		this._nextUrl = "/template/basic/exception/excepEdit.jsp";
	}
	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showexceptionList() throws ServletException, SQLException {
    
		//super.listObj("exception_detail");
		List<BizObject> v = _exception.getList("exception_list","serialno");
		this.setAttribute("objList", v);
		this.setAttribute("exception", _exception);
		this._nextUrl = "/template/basic/exception/exceptionListEdit.jsp";
	}	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseExcp() throws ServletException, SQLException {
    
		//super.listObj("exception_detail");
//		BizObject exception=new BizObject("exception");
//		exception=exception.getQF().getByID(_objId);
		logger.info("objid is "+_objId);
		this.setAttribute("obj", _exception);
		
		
		
		this.listExcp();
		//ActionHandler.currentActionHandler().
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}

	
	private void prepareUsers() throws SQLException{
		
		String users=_exception.getString("users");
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
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseexceptionList() throws ServletException, SQLException {
    
		//super.listObj("exception_detail");
//		BizObject exception=new BizObject("exception");
//		exception=exception.getQF().getByID(_objId);
		logger.info("objid is "+_objId);
		this.setAttribute("obj", _exception);
		this.listExcp();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}
	String _objDetailId="";  //objdetail id
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseexceptionDetail() throws ServletException, SQLException {
    
		//super.listObj("exception_detail");
		BizObject exceptiondetail=new BizObject("exception_detail");
		
		exceptiondetail=exceptiondetail.getQF().getByID(_objDetailId);
		addFkTableInfo(exceptiondetail);
		logger.info("obj is "+exceptiondetail);
		this.setAttribute("obj", exceptiondetail);
		
		this.showExcp();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseexceptionListDetail() throws ServletException, SQLException {
    
		//super.listObj("exception_detail");
		BizObject exceptiondetail=new BizObject("exception_list");
		
		exceptiondetail=exceptiondetail.getQF().getByID(_objDetailId);
		addFkTableInfo(exceptiondetail);
		logger.info("obj is "+exceptiondetail);
		this.setAttribute("obj", exceptiondetail);
		
		this.showexceptionList();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}
	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void saveExcp() throws ServletException, SQLException {
    
		//super.listObj("exception_detail");
		_exception=this.getBizObjectFromMap("exception").duplicate();
		//exception.set("description", "{type:'add',uid:'-----',truename:'中文',sex:'man'}");
		_exception.setID(_objId);
		this.checkParam(_exception);
		this.getJdo().addOrUpdate(_exception);
		this.setAttribute("obj", _exception);
		
		
		
		logger.info("objid is "+_objId+" exception is "+_exception);
		this.clearQueryParam();
		this.listExcp();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}
	/**
	 * 取嵌套metalist树
	 * @return
	 * @throws SQLException 
	 * 
	 */
	public static String getCascadeField(String pid) throws SQLException{

		return getSubFields(pid,"");
	}	
	
	public static String getSubFields(String tablename,String fenge) throws SQLException{
		
		String ret="";
		fenge=fenge+"_";
		// p.getList("department");
		List<BizObject> v = SystemKit.getCachePickList(tablename+fenge+"metalist");
		System.out.println(tablename+"  "+v.size());
		if(v.size()>0){
			
			if(tablename.equals("root"))
				 ret="<ul id=fieldid_data class=mcdropdown_menu>";
			else
				ret="<ul>";
		}
			
		
		for(BizObject b:v){
			//logger.info();
			ret=ret+"<li rel='"+tablename+"."+b.getString("id")+"'>"+b.getString("name")+getSubFields(b.getString("id"),fenge)+"</li>";
		}
		if(!ret.equals(""))
			ret=ret+"</ul>";
		
		return ret;
	}
	
	public void addFkTableInfo(BizObject exceptiondetail){
		String field = exceptiondetail.getString("field");
		String tablename=_exception.getString("tablename");
		//logger.info("exception is ")
		logger.info("tablename  is "+tablename);
		logger.info("field is "+field+"  ");
		BizObject des = new BizObject(tablename);
		logger.info("des is "+"  "+des);
		
		ObjectProperty  op = (ObjectProperty)des.colsInfo.get(field.toLowerCase());
		//logger.info("des colsinfo is "+"  "+des.colsInfo);
		
		logger.info("des op is "+"  "+op);
		if(op==null)
			throw new AlarmException ("表和字段不匹配");
		if(op.isFk()){
			exceptiondetail.set("fktable", op.getFkTable());
			//exceptiondetail.set("fktable", op.getFkTable());
//			if(exceptiondetail.getString("fkfield").equals("")){
//				throw new ControllableException(field+"是外键，必须填写外键关联关系");
//			}
		}

	}
	
	/**
	 * 保存模版明细
	 * @throws ServletException
	 * @throws SQLException
	 */
	
	public void saveExcpDetail() throws ServletException, SQLException {
	    
		//super.listObj("exception_detail");
		BizObject exceptiondetail=this.getBizObjectFromMap("exception_detail");
		logger.info("exception detail is "+exceptiondetail);
		exceptiondetail.setFk("iddd", "exception_detail");
		logger.info("iddd is "+exceptiondetail.getBizObj("iddd"));
		exceptiondetail.setID(_objDetailId);
		logger.info("objid is "+_objId);
		//exceptiondetail.setID(_objId);
		this.checkParam(exceptiondetail);
		addFkTableInfo(exceptiondetail);		
//		logger.info("des op is "+"  "+op);
			
		
		try {
			this.getJdo().addOrUpdate(exceptiondetail);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(e.getErrorCode()==1062)
				throw new AlarmException("重复的序列号");
			else
				throw e;
			//e.printStackTrace();
		}
		//this.setAttribute("obj", exceptiondetail.duplicate());
		this.showExcp();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}
	
	/**
	 * 保存模版列表明细
	 * @throws ServletException
	 * @throws SQLException
	 */
	
	public void saveexceptionListDetail() throws ServletException, SQLException {
	    
		//super.listObj("exception_detail");
		BizObject exceptiondetail=this.getBizObjectFromMap("exception_list");
		logger.info("exception list detail is "+exceptiondetail);
//		exceptiondetail.setFk("iddd", "exception_detail");
//		logger.info("iddd is "+exceptiondetail.getBizObj("iddd"));
		exceptiondetail.setID(_objDetailId);
		logger.info("objid is "+_objId);
		//exceptiondetail.setID(_objId);
		this.checkParam(exceptiondetail);
		addFkTableInfo(exceptiondetail);		
//		logger.info("des op is "+"  "+op);
			
		//exceptiondetail.getColumns()
	try{	
		this.getJdo().addOrUpdate(exceptiondetail);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		if(e.getErrorCode()==1062)
			throw new AlarmException("重复的序列号");
		else
			throw e;
		//e.printStackTrace();
	}
		//this.setAttribute("obj", exceptiondetail.duplicate());
		this.showexceptionList();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}
	
	private static String getexceptionDetailValue(BizObject detail,BizObject showobj) throws SQLException{
		
		
		String value="";
		String field=detail.getString("field");
		Object o = showobj.get(field);
		System.out.println("field is "+ field+"   "+o);
		if(o==null)
			return "";
		if(!detail.getString("fkfield").equals("")){
			value = ((BizObject)o).getString(detail.getString("fkfield"));
		}
		else
			value=o.toString();

		if(!detail.getString("optiontype").equals(""))
			value=SystemKit.getCacheParamById(detail.getString("optiontype"), value);
		
		return value;
		
	}
	
	/**
	 * 显示模板
	 * 
	 * @param exceptionid  模板id
	 * @param showobj  要显示的打印对象
	 * @throws SQLException
	 */
	public  static void showTemplate(String exceptionid,BizObject showobj,List<BizObject> objList) throws SQLException{
		BizObject exception=new BizObject("exception");
		exception.setID(exceptionid);
		exception.refresh();
		
		List<BizObject> exceptionlist=exception.getList("exception_detail","serialno");
		for(BizObject detail:exceptionlist){
			detail.set("value", getexceptionDetailValue(detail,showobj));
		}
		
		
		
		List listv=new ArrayList();
		
		for(BizObject b:objList){
			
			List<BizObject> exceptionlist2=exception.getList("exception_list","serialno");	
			for(BizObject detail:exceptionlist2){
				detail.set("value", getexceptionDetailValue(detail,b));
				
			}
			listv.add(exceptionlist2);
		}
		
		currentActionHandler().setAttribute("obj", exception);
		currentActionHandler().setAttribute("objList", exceptionlist);
		currentActionHandler().setAttribute("objListV", listv);
		currentActionHandler()._nextUrl="/template/basic/exception/template.jsp";
	}


	public void  testexception() throws SQLException{
		
		BizObject m = new BizObject("exception");
		m.setID(_objId);
		m.refresh();

		
		BizObject b= new BizObject(m.getString("tablename"));
		b = (BizObject) b.getQF().query().get(0);
		logger.info(b);
		
		List v = new QueryFactory(m.getString("dtablename")).query(this.preparePageVar());
		this.showTemplate(_objId, b,v);
	}	
	public void  testTmp() throws SQLException{
		BizObject b= new BizObject("employee");
		b = (BizObject) b.getQF().query().get(0);
		logger.info(b);
		List v = b.getQF().query(this.preparePageVar());
		this.showTemplate("d976434e-8da4-4dfd-b0d4-8d3d27888979", b,v);
	}
	/**
	 * 删除模版
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void delExcp() throws ServletException, SQLException {
    
		BizObject b = new BizObject("exception");
		b.setID(_objId);
		try {
			this.getJdo().delete(b);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(e.getErrorCode()==1451)
				throw new AlarmException("存在子记录,不能删除");
			//e.printStackTrace();
		}
		this.listExcp();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}	
	/**
	 * 删除模版字段明细
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void delexceptionDetail() throws ServletException, SQLException {
    
		BizObject b = new BizObject("exception_detail");
		b.setID(_objDetailId);
		this.getJdo().delete(b);
		this.showExcp();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void delexceptionListDetail() throws ServletException, SQLException {
    
		BizObject b = new BizObject("exception_list");
		b.setID(_objDetailId);
		this.getJdo().delete(b);
		this.showexceptionList();
		//this._nextUrl = "/template/basic/exception/mbList.jsp";
	}

}
