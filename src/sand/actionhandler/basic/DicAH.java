package sand.actionhandler.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.sql.SQLException;
import java.io.IOException;
import java.util.List;import java.util.ArrayList;

import sand.actionhandler.system.ActionHandler;
import tool.dao.*;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: 数据字典操作处理
 * </p>
 * <p>
 * Description: 数据字典操作处理
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
public class DicAH extends ActionHandler {
	private Logger logger = Logger.getLogger(DicAH.class);

	/**
	 * 构造方法
	 * 
	 * @param req
	 * @param res
	 */
	public DicAH(HttpServletRequest req,
			HttpServletResponse res) {

		super(req, res);
		this._objType = "DICTIONARY";// 本操作针对表DICTIONARY
		this._moduleId = "basic";// 本模块为基本设置
	}

	/**
	 * 列出所需数据字典
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void list(String belong) throws ServletException, SQLException {
		BizObject obj = (BizObject) this._paramMap.get("DICTIONARY");
		if (obj == null) {
			obj = new BizObject("DICTIONARY");
			obj.set("belong", belong);
		}
		QueryFactory qf = new QueryFactory(obj);
		List list = qf.query(obj);
		// logger.debug("sql is "+qf.getSql());
		this._request.setAttribute("objList", list);
		// this._nextUrl="/template/basic/dict_menu.jsp";
	}

	/**
	 * 列出所有数据字典
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listDepot() throws ServletException, SQLException {
		list("0");
		this._nextUrl = "/template/basic/dict_menu.jsp";
	}
	/*
	 * 这里不需要过滤关键字
	 * (non-Javadoc)
	 * @see sand.actionhandler.system.ActionHandler#initial()
	 */
	protected void initial(){
		super.initial();
		//super._keywords_filter_enabled=false;
	}
	/**
	 * 列出所有数据字典
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void list() throws ServletException, SQLException {
		BizObject obj = (BizObject) this._paramMap.get("DICTIONARY");
		if(obj==null)
			obj = new BizObject("dictionary");
		QueryFactory qf = new QueryFactory(obj);
		List list = qf.query(obj);
		// logger.debug("sql is "+qf.getSql());
		this._request.setAttribute("objList", list);

		this._nextUrl = "/template/basic/dict_menu.jsp";
	}
	

	/**
	 * 列表显示字典
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void listDic() throws ServletException, SQLException {
    

	    
		//this.setPageSize(-1);
		//super.listObj("moban");
		
		BizObject obj = new BizObject("dictionary");
		List<BizObject> v = obj.getQF().query();
		for(int i=0;i<v.size();i++){
			BizObject b = v.get(i);
			
			if(ItemAH.isView(b.getString("dicid"))){
				v.remove(b);
				i--;
				
			}
		}
		this.setAttribute("objList", v);
		
		this._nextUrl = "/template/basic/dict_menu.jsp?select=dic";
	
		//this.setPageSize(-1);
		//super.listObj("moban");
		
		}	

	
	/**
	 * 列表显示字典
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void listViewDic() throws ServletException, SQLException {

		List v2 = new ArrayList();
		BizObject obj = new BizObject("dictionary");
		List<BizObject> v = obj.getQF().query();
		for(BizObject b:v){
			if(ItemAH.isView(b.getString("dicid"))){
				b.set("isview", 1);
				v2.add(b);
			}
				
		}		

		
		BizObject dic = new BizObject("v_dictionary");
		String sql = "select DISTINCT dicid as dicid, dicid as name from v_dictionary where dicid not in (select id from dictionary)";
		List<BizObject> v3=QueryFactory.executeQuerySQL(sql);
		
		for(int i=0;i<v3.size();i++){
			BizObject b = v3.get(i);
		
			if(b.getString("dicid").length()>=36){
				continue;
			}
			else{
				b.set("isview", 1);
				v2.add(b);
			}
				
			
		}
		
		//v.addAll(v2);
		this.setAttribute("objList", v2);
		
		this._nextUrl = "/template/basic/dict_menu.jsp?select=view";
		
	}	

	
	/**
	 * 列出所有数据字典
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listTrack() throws ServletException, SQLException {
		list("1");
		this._nextUrl = "/template/basic/dict_menu.jsp";
	}

}