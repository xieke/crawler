package sand.actionhandler.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.sql.SQLException;
import java.io.IOException;
import java.util.List;import java.util.ArrayList;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.CandoCheck;
import tool.dao.*;
import sand.depot.tool.system.*;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: 数据字典子项操作处理
 * </p>
 * <p>
 * Description: 数据字典子项操作处理
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SAND
 * </p>
 * 
 * @author wang.yc
 * @version 1.0
 */
public class ItemAH extends ActionHandler {
	private Logger logger = Logger.getLogger("wyc.logger");

	/**
	 * 构造方法
	 * 
	 * @param req
	 * @param res
	 */
	public ItemAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "ITEM";// 本操作针对表ITEM
		this._moduleId = "basic";// 本模块为基本设置
	}
	/**
	 * 列出某个数据字典的子项
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 */
	private void listItemsOfDic(String dicid) throws SQLException {
		BizObject obj = new BizObject("item");
		//String dicid=_request.getParameter("dicid");
		obj.set("dicid", dicid);
		obj.setOrderBy("odb");
		//obj.set("disabled", "is null");
//		QueryFactory qf = new QueryFactory(obj);
//		List list = qf.query(obj);
		
		BizObject dic= new BizObject("dictionary");
		dic.set("dicid",dicid);
		
		if(dic.getQF().getOne(dic)!=null)
			dic=dic.getQF().getOne(dic);
		
		if(dic.getId().equals("")){
			dic.setID(dicid);
			dic.set("isview", this.getParameter("isview"));
		}
		
		
		//if(!this.getParameter("isview").equals(""))
		
		
		//logger.info("list size is "+list.size()+qf.getSql());
		//if(list.size()==0){
			String sql="select id ,id as ikey,name,dicid from v_dictionary where dicid = '"+dicid+"' order by odb ";
			 List<BizObject> list = QueryFactory.executeQuerySQL(sql);			
			 

			 BizObject item =new BizObject("item");
			 
			 
			 for(BizObject b:list){
				 
				 item.clear();
				 item.set("dicid", dicid);
				 item.set("ikey", b.getString("ikey"));
				 item.set("name", b.getString("name"));
				 
				 
				 if(item.getQF().getOne(item)!=null){
					 
					 item=item.getQF().getOne(item);
					 b.set("isview", 0); //不是来自视图
					 b.setID(item.getId());
					 b.set("memo", item.getString("memo"));
				 }
					 
					 
			 }
			
		//}
		
		this.setAttribute("obj", dic);

		this.setAttribute("objList", list);
		_request.setAttribute("dicId", obj.getString("dicId"));
		this._nextUrl = "/template/basic/dictItemsList.jsp";
	}

	/**
	 * 列出某个数据字典的子项
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 */
	public void listItemsOfDic() throws SQLException {
		this.listItemsOfDic(this.getParameter("dicid"));		
		
	}

	public static List getSubItems(String dicid) throws SQLException{
		BizObject item = new BizObject("item");
		item.set("dicid", dicid);
		return item.getQF().query(item);
		
	}
	
	public static boolean isView(String dicid) throws SQLException{
		
		BizObject dic = new BizObject("dictionary");
		dic.setID(dicid);
		
		if(SystemKit.getNoCachePickList(dicid).size()>getSubItems(dicid).size()){
			return true;
		}
		else
			return false;
			
	}
	
	public void saveDic() throws SQLException{
		BizObject biz = this.getBizObjectFromMap("dictionary");
		
		BizObject bizold = new BizObject("dictionary");
		
		//bizold.set("name", o);
		
		if(isView(biz.getId())){ //如果视图中存在此 dicid，那么标识一下
			biz.set("isview",1);
		}
		else
			biz.set("isview",0);
		
		
		
		this.getJdo().addOrUpdate(biz);
		//this._dispatchType=ActionHandler.
		this._tipInfo="保存成功<script>window.parent.dic_menu.location.href=window.parent.dic_menu.location.href</script>";
		
		this.listItemsOfDic(biz.getId());
		//this._nextUrl="/basic.ItemAH.listItemsOfDic?dicid="+biz.getId();
	}	

	public void delDic() throws SQLException{
		BizObject biz = this.getBizObjectFromMap("dictionary");
		
		if(SystemKit.getNoCachePickList(biz.getId()).size()>0)
			throw new ControllableException("还存在子项，不能删除");
		this.getJdo().delete(biz);
		this._tipInfo="删除成功<script>window.parent.dic_menu.reload()</script>";
		//this._dispatchType=ActionHandler.
		this.listItemsOfDic("deleted");
		//this._nextUrl="/basic.ItemAH.listItemsOfDic?dicid=deleted";
	}

	/**
	 * 添加数据字典子项
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 */
	@CandoCheck("itemedit")
	public void add() throws SQLException, ServletException {
		// this.validateCanAdd();
		//this.validateCanDo("depotbsop");
		// 取出参数
		BizObject obj = this.getBizObjectFromMap(this._objType);
		if(obj.getQF().query(obj).size()>0)
			throw new ControllableException("该值已存在");
		if(SystemKit.getCachePickMap(obj.getString("dicid")).containsKey(obj.getString("key")))
			throw new ControllableException("已存在"+obj.getString("key"));

		//List v = SystemKit.getCachePickList(type);
		super.add(obj);
		//sand.depot.tool.system.SystemKit.removePickListCache(obj.getString("dicid"));
		sand.depot.tool.system.SystemKit.removeCache(obj.getString("dicid"));
		clearKeyWordsCache(obj);
		
		// 返回添加页
		this.listItemsOfDic(obj.getString("dicid"));
		// 提示信息
		this._tipInfo = "添加完成";
	}
	/*
	 * 这里不需要过滤关键字
	 * (non-Javadoc)
	 * @see sand.actionhandler.system.ActionHandler#initial()
	 */
	protected void initial(){
		super.initial();
		super.hexie();
		//super._keywords_filter_enabled=true;
	}
	
	private void clearKeyWordsCache(BizObject obj){
		if(obj.getString("dicid").equals("crabforbidden")||obj.getString("dicid").equals("politeforbidden"))
			SystemKit.keywords=null;

	}

	/**
	 * 删除数据字典子项
	 */
	@CandoCheck("itemedit")
	public void delete() throws SQLException, ServletException {
		//this.validateCanDo("depotbsop");
		BizObject obj = new BizObject("item");
		String dicid="";
		try {
			obj.setID(this.getParameter("itemid"));
			obj.refresh();
			dicid=obj.getString("dicid");
			//obj.set("disabled", 1);
			this.getJdo().delete(obj);
			//super.delete(obj);
			// super.delete(obj);
			_tipInfo = "删除成功";
			
			sand.depot.tool.system.SystemKit.removeCache(obj.getString("dicid"));
			//clearKeyWordsCache(obj);
		} catch (SQLException sqle) {
			if (sqle.getErrorCode() == 2292)
				_tipInfo = "无法删除，这个数据字典项已经被使用";

		}

		// 返回添加页
		this.listItemsOfDic(dicid);
	}

	/**
	 * 修改记录前查询记录
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 */
	@CandoCheck("itemedit")
	public void preUpdate() throws SQLException, ServletException {
		// this.validateCanModify();
		BizObject obj = this.getBizObjectFromMap(this._objType);
		QueryFactory qf = new QueryFactory(obj);
		List r = qf.query(obj);
		if (r == null || r.size() == 0) {
			throw new ControllableException("没有相应的记录！");
		}

		_request.setAttribute("obj", r.get(0));

		// 返回修改页
		this._nextUrl = "/template/basic/itemOper.jsp?reqType=basic.ItemActionHandler.update";
	}

	/**
	 * 修改记录
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 */
	@CandoCheck("itemedit")
	public void update() throws SQLException, ServletException {
		//this.validateCanDo("depotbsop");
		BizObject obj = this.getBizObjectFromMap(this._objType);
		logger.info("obj is "+obj);
//		if(SystemKit.getCachePickMap(obj.getString("dicid")).containsKey(obj.getString("key")))
//			throw new ControllableException("已存在"+obj.getString("key"));

		super.update(obj);
		// 返回列表页
		
		sand.depot.tool.system.SystemKit.removeCache(obj.getString("dicid"));
		//SystemKit.removePickMapCache();
		//SystemKit.removePickListCache();
		clearKeyWordsCache(obj);
		this.listItemsOfDic(obj.getString("dicid"));
		// 提示信息
		
		this._tipInfo = "修改完成";
	}
}