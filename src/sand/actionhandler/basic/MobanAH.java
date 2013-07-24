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

public class MobanAH extends ActionHandler {
	
	//private Logger logger = Logger.getLogger("wyc.logger");
	public MobanAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType ="flow_case_fl";
		this._moduleId="basic";
		// TODO Auto-generated constructor stub
	}
	
	BizObject _moban = new BizObject("moban");
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
				_moban.setID(_objId);
				_moban.refresh();
				this.setAttribute("moban", _moban);
			}
			
			logger.info("moban is "+_moban);

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
	public void listMoban() throws ServletException, SQLException {
    
		this.setPageSize(-1);
		super.listObj("moban");
		this._nextUrl = "/template/basic/moban/mbList.jsp";
	}
 
	/**
	 * 显示一个 模版 
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showMoban() throws ServletException, SQLException {
    
		//super.listObj("moban_detail");
		List<BizObject> v = _moban.getList("moban_detail","serialno");
		this.setAttribute("objList", v);
		this.setAttribute("moban", _moban);
		this._nextUrl = "/template/basic/moban/mobanEdit.jsp";
	}
	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void showMobanList() throws ServletException, SQLException {
    
		//super.listObj("moban_detail");
		List<BizObject> v = _moban.getList("moban_list","serialno");
		this.setAttribute("objList", v);
		this.setAttribute("moban", _moban);
		this._nextUrl = "/template/basic/moban/mobanListEdit.jsp";
	}	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseMoban() throws ServletException, SQLException {
    
		//super.listObj("moban_detail");
//		BizObject moban=new BizObject("moban");
//		moban=moban.getQF().getByID(_objId);
		logger.info("objid is "+_objId);
		this.setAttribute("obj", _moban);
		this.listMoban();
		//ActionHandler.currentActionHandler().
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}

	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseMobanList() throws ServletException, SQLException {
    
		//super.listObj("moban_detail");
//		BizObject moban=new BizObject("moban");
//		moban=moban.getQF().getByID(_objId);
		logger.info("objid is "+_objId);
		this.setAttribute("obj", _moban);
		this.listMoban();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}
	String _objDetailId="";  //objdetail id
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseMobanDetail() throws ServletException, SQLException {
    
		//super.listObj("moban_detail");
		BizObject mobandetail=new BizObject("moban_detail");
		
		mobandetail=mobandetail.getQF().getByID(_objDetailId);
		addFkTableInfo(mobandetail);
		logger.info("obj is "+mobandetail);
		this.setAttribute("obj", mobandetail);
		
		this.showMoban();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void chooseMobanListDetail() throws ServletException, SQLException {
    
		//super.listObj("moban_detail");
		BizObject mobandetail=new BizObject("moban_list");
		
		mobandetail=mobandetail.getQF().getByID(_objDetailId);
		addFkTableInfo(mobandetail);
		logger.info("obj is "+mobandetail);
		this.setAttribute("obj", mobandetail);
		
		this.showMobanList();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}
	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void saveMoban() throws ServletException, SQLException {
    
		//super.listObj("moban_detail");
		BizObject moban=this.getBizObjectFromMap("moban");
		//moban.set("description", "{type:'add',uid:'-----',truename:'中文',sex:'man'}");
		moban.setID(_objId);
		this.checkParam(moban);
		this.getJdo().addOrUpdate(moban);
		this.setAttribute("obj", moban.duplicate());
		logger.info("objid is "+_objId+" moban is "+moban);
		this.clearQueryParam();
		this.listMoban();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
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
	
	public void addFkTableInfo(BizObject mobandetail){
		String field = mobandetail.getString("field");
		String tablename=_moban.getString("tablename");
		//logger.info("moban is ")
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
			mobandetail.set("fktable", op.getFkTable());
			//mobandetail.set("fktable", op.getFkTable());
//			if(mobandetail.getString("fkfield").equals("")){
//				throw new ControllableException(field+"是外键，必须填写外键关联关系");
//			}
		}

	}
	
	/**
	 * 保存模版明细
	 * @throws ServletException
	 * @throws SQLException
	 */
	
	public void saveMobanDetail() throws ServletException, SQLException {
	    
		//super.listObj("moban_detail");
		BizObject mobandetail=this.getBizObjectFromMap("moban_detail");
		logger.info("moban detail is "+mobandetail);
		mobandetail.setFk("iddd", "moban_detail");
		logger.info("iddd is "+mobandetail.getBizObj("iddd"));
		mobandetail.setID(_objDetailId);
		logger.info("objid is "+_objId);
		//mobandetail.setID(_objId);
		this.checkParam(mobandetail);
		addFkTableInfo(mobandetail);		
//		logger.info("des op is "+"  "+op);
			
		
		try {
			this.getJdo().addOrUpdate(mobandetail);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(e.getErrorCode()==1062)
				throw new AlarmException("重复的序列号");
			else
				throw e;
			//e.printStackTrace();
		}
		//this.setAttribute("obj", mobandetail.duplicate());
		this.showMoban();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}
	
	/**
	 * 保存模版列表明细
	 * @throws ServletException
	 * @throws SQLException
	 */
	
	public void saveMobanListDetail() throws ServletException, SQLException {
	    
		//super.listObj("moban_detail");
		BizObject mobandetail=this.getBizObjectFromMap("moban_list");
		logger.info("moban list detail is "+mobandetail);
//		mobandetail.setFk("iddd", "moban_detail");
//		logger.info("iddd is "+mobandetail.getBizObj("iddd"));
		mobandetail.setID(_objDetailId);
		logger.info("objid is "+_objId);
		//mobandetail.setID(_objId);
		this.checkParam(mobandetail);
		addFkTableInfo(mobandetail);		
//		logger.info("des op is "+"  "+op);
			
		//mobandetail.getColumns()
	try{	
		this.getJdo().addOrUpdate(mobandetail);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		if(e.getErrorCode()==1062)
			throw new AlarmException("重复的序列号");
		else
			throw e;
		//e.printStackTrace();
	}
		//this.setAttribute("obj", mobandetail.duplicate());
		this.showMobanList();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}
	
	private static String getMobanDetailValue(BizObject detail,BizObject showobj) throws SQLException{
		
		
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
	 * @param mobanid  模板id
	 * @param showobj  要显示的打印对象
	 * @throws SQLException
	 */
	public  static void showTemplate(String mobanid,BizObject showobj,List<BizObject> objList) throws SQLException{
		BizObject moban=new BizObject("moban");
		moban.setID(mobanid);
		moban.refresh();
		
		List<BizObject> mobanlist=moban.getList("moban_detail","serialno");
		for(BizObject detail:mobanlist){
			detail.set("value", getMobanDetailValue(detail,showobj));
		}
		
		
		
		List listv=new ArrayList();
		
		for(BizObject b:objList){
			
			List<BizObject> mobanlist2=moban.getList("moban_list","serialno");	
			for(BizObject detail:mobanlist2){
				detail.set("value", getMobanDetailValue(detail,b));
				
			}
			listv.add(mobanlist2);
		}
		
		currentActionHandler().setAttribute("obj", moban);
		currentActionHandler().setAttribute("objList", mobanlist);
		currentActionHandler().setAttribute("objListV", listv);
		currentActionHandler()._nextUrl="/template/basic/moban/template.jsp";
	}


	public void  testMoban() throws SQLException{
		
		BizObject m = new BizObject("moban");
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
	public void delMoban() throws ServletException, SQLException {
    
		BizObject b = new BizObject("moban");
		b.setID(_objId);
		try {
			this.getJdo().delete(b);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(e.getErrorCode()==1451)
				throw new AlarmException("存在子记录,不能删除");
			//e.printStackTrace();
		}
		this.listMoban();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}	
	/**
	 * 删除模版字段明细
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void delMobanDetail() throws ServletException, SQLException {
    
		BizObject b = new BizObject("moban_detail");
		b.setID(_objDetailId);
		this.getJdo().delete(b);
		this.showMoban();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}	
	/**
	 * 列表显示用户
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void delMobanListDetail() throws ServletException, SQLException {
    
		BizObject b = new BizObject("moban_list");
		b.setID(_objDetailId);
		this.getJdo().delete(b);
		this.showMobanList();
		//this._nextUrl = "/template/basic/moban/mbList.jsp";
	}

}
