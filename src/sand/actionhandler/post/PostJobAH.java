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
	
	/**
	 * 保存做的修改
	 * @throws SQLException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void saveModify() throws SQLException, IOException, TemplateException{
		int i = Integer.parseInt(this._objId);
		BizObject  s = PostJob.parseMailList().get(i);
		
		BizObject post = new BizObject("post");
		
		post.set("mailid",i);
		post.set("posted", 0);
		if(post.getQF().getOne(post)!=null){
			post=post.getQF().getOne(post);
		}
		
		post.set("tags", s.getString("tags"));
		post.set("postmail", s.getString("address"));
		post.set("posttime", new Date());
		
		String newsids[]=this.getParameters("outids");
		String ids="";
		for(String newsid:newsids){
			if(ids.equals("")) ids=newsid;
			else
				ids=ids+","+newsid;
		}
		

		post.set("newsids", ids);
		//post.set("success", success); //是否成功发送
		post.set("name", s.getString("name"));
		
		this.getJdo().addOrUpdate(post);
		this.showPost();
		//this.listPost();
	}
	

	public void  listModify() throws SQLException{
		int i = Integer.parseInt(this._objId);
		this.setAttribute("objid",i);

		Map v=PostJob.getPosts(i);
		this.setAttribute("objList",v);
		this._nextUrl="/template/news/listmodify.jsp";
		
	}

	public void showPost() throws SQLException, IOException, TemplateException{
		int i = Integer.parseInt(this._objId);
		BizObject  s = PostJob.parseMailList().get(i);
		BizObject email = new BizObject("email");
		email.set("toaddr", s.getString("address"));
		email.set("name",s.getString("name"));
		email.set("id", i);
		
		//Map<String ,List> v = PostJob.getPosts(s.getString("tags"), s.getString("cycle"), s.getString("urgent"), s.getString("limit"),s.getString("lang"));
		
		Map<String ,List> v = PostJob.getPosts(i);
		
		String greeting=s.getString("greeting").replaceAll("@name",s.getString("name"));
		String ending=s.getString("ending").replaceAll("@date", DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS));
		
		String content=PostJob.render(v,greeting,ending);
		email.set("content",content);
		String subject=s.getString("subject").replaceAll("@name",s.getString("name"));
	//	log("title "+subject);
		//email.set("title", title);
		email.set("subject", subject);
		
		this.setAttribute("obj", email);
		this._nextUrl="/template/news/showPost.jsp";

		//boolean success =MailServer.sendMailSyn(email);//.sendMailSyn(email);
		
		
	}
	

}
