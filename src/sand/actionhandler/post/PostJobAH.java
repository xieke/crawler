package sand.actionhandler.post;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
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

import org.apache.commons.io.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.job.PostJob;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.InfoException;
import sand.mail.MailServer;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import tool.util.CachedResponseWrapper;

public class PostJobAH extends ActionHandler {
	private Configuration cfg; 

	public PostJobAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
        cfg = new Configuration();
        // - Templates are stoted in the WEB-INF/templates directory of the Web app.
        cfg.setServletContextForTemplateLoading(this._context  , "WEB-INF/templates");
		// TODO Auto-generated constructor stub
	}
	

	
	public void listPostJob() throws SQLException{
		//BizObject postjob = this.getBizObjectFromMap("postjob");
		//postjob.setMap("posttime")
		//this.setAttribute("objList", v);
		this.listObj("postjob");
		this._nextUrl="/template/post/listPostJob.jsp";		
	}
	


	public void showPostJob() throws SQLException, IOException, TemplateException{
		List v = new ArrayList();
		for(int i=0;i<100;i++){
			BizObject b= new BizObject();
			b.set("name", "屁"+i);
			b.set("id", "pi"+i);
			v.add(b);
		}
		
		this.setAttribute("tags",v);
		this.showObj("postjob",this._objId);
		this._nextUrl="/template/post/showPostJob.jsp";
	}
	

	public void save() throws SQLException{
		BizObject b = this.getBizObjectFromMap("postjob");
		this.checkParam(b);
		this.getJdo().addOrUpdate(b);
		this._nextUrl="/template/post/showPostJob.jsp";
	}
	
}
