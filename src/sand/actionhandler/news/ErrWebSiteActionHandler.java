package sand.actionhandler.news;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import basic.BasicContext;

import sand.actionhandler.system.ActionHandler;
import tool.dao.BizObject;

public class ErrWebSiteActionHandler extends ActionHandler {

	public ErrWebSiteActionHandler(HttpServletRequest req,HttpServletResponse res) {
		super(req, res);
		this._objType="err_web_site";
		this._moduleId = "news";
	}
	
	public void list() throws SQLException{
		super.listObj();
		this._nextUrl = "/template/news/errWebSiteList.jsp";
	}
	
	public void dispose(String id) throws SQLException{
		BizObject err_web_site = new BizObject("err_web_site");
		err_web_site.setID(id);
		err_web_site.refresh();
		if(err_web_site.getString("status").equals(BasicContext.STATUS_DISPOSE_NO)){
			err_web_site.set("status", BasicContext.STATUS_DISPOSE_YES);
			this.getJdo().update(err_web_site);
		}
//		this.list();
	}
	
	public void disposes() throws SQLException{
		String[] ids = this.getParameters("delid");
		for(String s : ids) this.dispose(s);
		this.list();
	}
}
