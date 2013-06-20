package sand.depot.job;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import sand.actionhandler.system.ActionHandler;
import sand.actionhandler.weibo.UdaClient;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.SystemKit;
import sand.mail.MailServer;
import sand.service.basic.service.TagService;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.JDO;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;
import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.http.AccessToken;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class PostJob extends BaseJob {

	private static Map<String,Object> renderMap = new HashMap();
	

	private String constructCycleSql(String cycle){
		String sql="";
		if(cycle.trim().equals("D")){
			sql=" posted=0";
					}
		else
			if(cycle.trim().equals("W")){
				sql=" weekposted=0";
			}
		return sql;

	}
	/**
	 * 构造tag sql
	 * @param tags
	 * @return
	 */
	private String constructTagSql(String tags){
		String tag[]=tags.split(",");
		
		String tag_sql="";
		for(String t:tag){
			if(!t.equals("")){
				if(tag_sql.equals(""))
					tag_sql ="(  tags like '%"+t.trim()+"%' ";	
				else
					tag_sql=tag_sql+" or "+" tags like '%"+t.trim()+"%' ";
			}
			
		}
		if(!tag_sql.equals("")) tag_sql=tag_sql+")";
		
		return tag_sql;
	}
	/**
	 * 取待发送的post列表。 每次限取20条
	 * 按重要度排序，列表数应该有一个最大的限制
	 * 一篇文章，一个客户，应该只发一次
	 * 
	 * 那每次发送文章都要记录客户email.防止重复发送
	 * @return
	 * @throws SQLException
	 */
	private Map<String,List> getPosts(String tags,String cycle,String urgent,String limit ) throws SQLException{
		
		String sql="select id from news where (1=1) ";
		String key = tags+cycle+urgent+ limit;
		
		
		
		if(renderMap.get(key)==null){
			
			Map<String ,List> m=new HashMap();
			
			
			String tagsql=constructTagSql(tags);
			
			if(!tagsql.equals("")) sql=sql+" and "+tagsql;
			
			String cyclesql=constructCycleSql(cycle);
			
			if(!cyclesql.equals("")) sql=sql+" and "+cyclesql;
			//sql=sql+" and "+constructCycleSql(cycle);
			
			sql=sql+" and "+" urgent>="+urgent.trim();
			sql=sql+"  order by  importance asc , posttime desc limit 0,"+limit;
			log("sql is "+sql);
			List<BizObject> v = QueryFactory.executeQuerySQL(sql);
			
			for(BizObject b:v){
				b.refresh();
			}
			

			Map<String,List> newtags = new HashMap();
			
			String tag[]=tags.split(",");
			//if(tagstr.equals("")) tagstr=this.getParameter("tags");
			//String tag[] = tagstr.split(",");
			for(String s:tag){
				if(s.equals("")) continue;
				List<BizObject> onetags = new ArrayList();
				
				for(int i=0 ; i<v.size();i++){
					BizObject b=v.get(i);
					log("tags is"+b.getString("tags"));
					if(b.getString("tags").indexOf(s)>=0){
						b.set("tag", s);
						onetags.add(b);
						v.remove(b);
						i--;
					}
				}	
				log("put "+s+"  "+onetags.size());
				newtags.put(s, onetags);
			
			
			//return v;
			//return post.getQF().query(post,pv);
			
		}
			renderMap.put(key,newtags);
			return newtags;
		
	}
		return (Map<String,List>) renderMap.get(key);
	}
	private String render(Map<String ,List> postv, String greeting, String ending) throws IOException, TemplateException{
		
		 Configuration cfg; 
	        cfg = new Configuration();
	        // - Templates are stoted in the WEB-INF/templates directory of the Web app.
	        cfg.setServletContextForTemplateLoading(ActionHandler._context  , "WEB-INF/templates");
	       // cfg.set
	       // this._dispatched=true;
			// Build the data-model
	        Map root = new HashMap();
	        root.put("message", "Hello World!");
	        root.put("name", greeting);
	        root.put("date",ending);
	        //this.render();
	        root.put("www_url", "http://192.168.36.33:18080");
	        root.put("objList", postv);
	       // this.renderHtml();
	        
	        // Get the templat object
	        Template t = cfg.getTemplate("news/render.ftl");
	        
	        // Prepare the HTTP response:
	        // - Use the charset of template for the output
	        // - Use text/html MIME-type
//	        _response.setContentType("text/html; charset=" + t.getEncoding());
//	        Writer out = _response.getWriter();
	        String s =FreeMarkerTemplateUtils.processTemplateIntoString(t, root);
	        return s;

	}
	
	private void addOldTags() throws SQLException{
		String sql="select id from news where posted is null  and tags is null limit 0,100";
		
		
		while(true){
			
			List<BizObject> v=QueryFactory.executeQuerySQL(sql);
			if(v.size()==0) break;
			
			for(BizObject b:v){
				String s="";
				//log("b id is "+b.getId());
				List<BizObject> tagids=this.getTagService().queryTagsByBillId(b.getId());	
				for(BizObject tag:tagids){
					if(tag==null) continue;
					//log("tag is "+tag);
					if(s.equals(""))
						s=tag.getString("name");
					else
						s=s+","+tag.getString("name");
				}
				if(!s.equals("")){
					b.set("tags", s);
					log("update  s "+s);
				}
				log(".");
				b.set("posted", "1");
				this._jdo.update(b);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		//this.getTagService().
		
	}
	private TagService tagService;
	public TagService getTagService() {
		if(tagService == null) 
			tagService = (TagService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("tagService");
		return tagService;
	}
	

	public void insertStatus(Status s) throws SQLException{}
	
	public static void main(String args[]){
		String s= "@name agfadsfadf";
		s=s.replaceAll("@name", "xxxx");
		System.out.print(s);
		}
	
	@Override
	public String run() throws Exception {
		
		String ret="";
		
		MailConfig mc=null;
		
//		this.addOldTags();
//		if(1==1) return "ok";
		List<String> mailv = mc.readTxtFile("/root/erp.upload/maillist");
		int i=0;



		for(String s:mailv){
			if(s.indexOf("#")>=0) continue;
			log(s);
			String[] mailinfo=s.split(";");
			if(mailinfo.length!=13){
				this.log(i+" txt解析错误 "+mailinfo[0]);
				ret=ret+i+" txt解析错误 "+mailinfo[0];
				continue;
			}
			String serialno=mailinfo[0];
			String name=mailinfo[1];
			String address=mailinfo[2];
			String tags=mailinfo[3];
			String cycle=mailinfo[4];
			String limit=mailinfo[5];
			String enable=mailinfo[6];
			String title=mailinfo[7];
			String greeting=mailinfo[8];
			String ending=mailinfo[9];
			String urgent=mailinfo[10];
			String telphone=mailinfo[11];
			String lang=mailinfo[12];
			
			BizObject email = new BizObject("email");

			// address=mailinfo[1];
			email.set("toaddr", address);
			Map<String ,List> v = this.getPosts(tags, cycle, urgent, limit);
			
			greeting=greeting.replaceAll("@name",name);
			ending=ending.replaceAll("@date", DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS));
			
			String content=this.render(v,name,DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS));
			email.set("content",content);
			title=title.replaceAll("@name",name);
			log("title "+title);
			//email.set("title", title);
			email.set("subject", title);
			
			

			boolean success =MailServer.sendMailSyn(email);//.sendMailSyn(email);
			
			BizObject post = new BizObject("post");
			post.set("tags", tags);
			post.set("postmail", address);
			post.set("posttime", new Date());
			
			String newsids="";
			
			for(String o : v.keySet()){ 
			   List<BizObject>  list=v.get(o); 
				for(BizObject b:list){
					if(newsids.equals(""))
						newsids=b.getId();
					else
						newsids=newsids+","+b.getId();
				}

			} 
			post.set("newsids", newsids);
			post.set("success", success); //是否成功发送
			post.set("name", name);
			
			this._jdo.add(post);
			
			i++;
		}
 		
		return super.OK+"，共发送 "+i+"封邮件 "+ret;
	}

}
