package sand.actionhandler.post;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.CandoCheck;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;

public class MailServerAH extends ActionHandler{

	public MailServerAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}
	
	@CandoCheck("serverconfig")
	public void list() throws SQLException{
		//BizObject postjob = this.getBizObjectFromMap("postjob");
		//postjob.setMap("posttime")
		//this.setAttribute("objList", v);
		this.listObj("mailserver");
		this._nextUrl="/template/post/listMailServer.jsp";		
	}
	
	@CandoCheck("serverconfig")
	public void save() throws SQLException{
	
		BizObject b = this.getBizObjectFromMap("mailserver");

		this.checkParam(b);
		this.getJdo().addOrUpdate(b);
		SystemKit.removeCache("mailservers");
		this._objId=b.getId();
		this.clearQueryParam();
		this.list();
		//this._nextUrl="/template/post/showPostJob.jsp";
	}
	@CandoCheck("serverconfig")
	public void delete() throws SQLException{
		BizObject b = new BizObject("mailserver");
		b.setID(this._objId);
		this.getJdo().delete(b);
		SystemKit.removeCache("mailservers");
		this.clearQueryParam();
		this.list();

	}	
	@CandoCheck("serverconfig")
	public void show() throws SQLException{

		BizObject b = new BizObject("mailserver");
		b.setID(this._objId);
		b.refresh();
		
		
		this.setAttribute("obj", b);		
		this._nextUrl="/template/post/showMailServer.jsp";
	}	
}
