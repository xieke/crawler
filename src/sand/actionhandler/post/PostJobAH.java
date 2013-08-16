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
	

	
	public void list() throws SQLException{
		//BizObject postjob = this.getBizObjectFromMap("postjob");
		//postjob.setMap("posttime")
		//this.setAttribute("objList", v);
		this.listObj("postjob");
		this._nextUrl="/template/post/listPostJob.jsp";		
	}
	
	
	public void listPosted() throws SQLException{
		//BizObject postjob = this.getBizObjectFromMap("postjob");
		//postjob.setMap("posttime")
		//this.setAttribute("objList", v);
		this.setOrderBy(" lastposttime desc ");
		this.listObj("posted");
		this._nextUrl="/template/post/listPosted.jsp";		
	}
	

	private List<BizObject> getRules(BizObject pj) throws SQLException{
		BizObject rule = new BizObject("rules");
		List<BizObject> v = rule.getQF().query();
		for(BizObject b:v){
			if(b.getId().equals(pj.getString("ruleid")))
				b.set("checked", "checked");
		}
		return v;
		
	}
	private List<BizObject> getCustomers(BizObject pj) throws SQLException{
		BizObject csts = new BizObject("customers");
		csts.setHardcoreFilter(" status=1 ");
		List<BizObject> v = csts.getQF().query(csts);
		for(BizObject b:v){
			b.set("name",b.getString("name")+"("+b.getString("mail")+")");
			if(pj.getString("customers").indexOf(b.getId())>=0)
				b.set("checked", "checked");
		}
		return v;	
		}
	private String getCustomerInfos(String customer_ids,String info) throws SQLException{
//		BizObject csts = new BizObject("customers");
//		List<BizObject> v = csts.getQF().query();
		String customer_id[]=customer_ids.split(",");
		String names="";
		BizObject b  = new BizObject ("customers"); 
		for(String  id:customer_id){
			if(!b.equals("")){
				b.setID(id);
				b.refresh();
				if(names.equals(""))
					names =b.getString(info);
				else
					names =names+","+b.getString(info);
			}
		}
		return names;	
		}	
	public void show() throws SQLException{

		BizObject b = new BizObject("postjob");
		b.setID(this._objId);
		b.refresh();
		
		if(b.getString("lastposttime").equals("")){
			
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.DAY_OF_YEAR, -2);
			b.set("lastposttime", c1.getTime());
		}
		
		//this.showObj("postjob",this._objId);
		this.setAttribute("rules",this.getRules(b));
		this.setAttribute("customers",this.getCustomers(b));
		
		this.setAttribute("obj", b);		
		this._nextUrl="/template/post/showPostJob.jsp";
	}

	public void showPosted() throws SQLException{
		
			super.showObj("posted",this._objId);		
		this._nextUrl="/template/post/showPosted.jsp";
	}
//	public String transToMail(String customer_ids){
//		String customer_id[]=customer_ids.split(",");
//		
//	}
	
	public void save() throws SQLException{
		String customer_ids = this.getParameter("custom_ids");
		String tactics_ids = this.getParameter("tactics_ids");
		
		BizObject b = this.getBizObjectFromMap("postjob");
		b.set("customers",customer_ids);
		b.set("ccounts",customer_ids.split(",").length);
		b.set("cnames",getCustomerInfos(b.getString("customers"),"name"));
		b.set("cemails",getCustomerInfos(b.getString("customers"),"email"));
		b.set("ruleid", tactics_ids);
		
		this.checkParam(b);
		this.getJdo().addOrUpdate(b);
		this._objId=b.getId();
		this.clearQueryParam();
		this.list();
		//this._nextUrl="/template/post/showPostJob.jsp";
	}
	
	public void delete() throws SQLException{
		BizObject b = new BizObject("postjob");
		b.setID(this._objId);
		this.getJdo().delete(b);
		this.clearQueryParam();
		this.list();

	}
	public void deleteAll() throws SQLException{
		String ids[]=this.getParameters("outids");
		for(String id:ids){
			BizObject b = new BizObject("postjob");
			b.setID(id);
			this.getJdo().delete(b);
			
		}
		this.clearQueryParam();
		this.list();

	}	
	public void sendAll() throws SQLException, TemplateException{
		String ids[]=this.getParameters("outids");
		String result="";
		for(String id:ids){
			BizObject b = new BizObject("postjob");
			b.setID(id);
			b.refresh();
			result=result+PostJob.processJob(b, this.getJdo());
			//this.getJdo().delete(b);			
		}
		this.clearQueryParam();
		this.list();
		this.setTipInfo(result);
	}	

	public void disable() throws SQLException{
		BizObject b = new BizObject("postjob");
		b.setID(this._objId);
		b.refresh();
		if(b.getString("status").equals("0"))
			b.set("status", 1);
		else
			b.set("status", 0);
		
		this.getJdo().update(b);
		this.clearQueryParam();
		this.list();
	}
	public void changeAll(String status) throws SQLException{
		
		String ids[]=this.getParameters("outids");
		for(String id:ids){
			BizObject b = new BizObject("postjob");
			b.setID(id);
			//b.refresh();
//			if(b.getString("status").equals("0"))
//				b.set("status", 1);
//			else
				b.set("status", status);
			
			this.getJdo().update(b);
			
		}
		
		this.clearQueryParam();
		this.list();
	}	
	public void disableAll() throws SQLException{
		this.changeAll("0");
	}	
	public void enableAll() throws SQLException{
		this.changeAll("1");
	}	

}
