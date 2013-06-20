package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sand.actionhandler.system.ActionHandler;
import tool.dao.BizObject;



public class TemplateAH extends ActionHandler {

	public TemplateAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}
	
	public void showTemplate() throws SQLException{
		BizObject b = new BizObject("template");
		b.setID(_objId);
		b.refresh();
		this.setAttribute("obj", b);
		this._nextUrl="";
	}
	
	public void listTemplate() throws SQLException{
		BizObject b = this.getBizObjectFromMap("template");
		List<BizObject> v = b.getQF().query(b);
		this.setAttribute("objList", v);
		this._nextUrl="";
	}

}
