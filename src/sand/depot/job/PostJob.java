package sand.depot.job;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import sand.actionhandler.news.GpMailAH;
import sand.actionhandler.system.ActionHandler;
import sand.actionhandler.system.AdminAH;
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
	public static List<BizObject>  postList;
	
	static Logger logger = Logger.getLogger(PostJob.class);

	private static  String constructCycleSql(String cycle){
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
	private  static String constructTagSql(String tags){
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
		if(!tag_sql.equals("")) 
			tag_sql=tag_sql+")";
		else
			tag_sql = " tags is not null ";
		
		return tag_sql;
	}
	
	public static  Map<String ,List> getPosts(int i) throws SQLException{
		BizObject  s = PostJob.parseMailList().get(i);
		s.set("posttime", getLastPostTime(i));
		Map<String ,List> v = PostJob.getQryPosts(s);
		//this.setAttribute("objid",i);
		BizObject post = new BizObject("post");
		post.set("mailid", i);
		post.set("posted", 0);
		post=post.getQF().getOne(post);
		
		if(post!=null)
			log(" newsids "+post.getString("newsids"));
			//Map m = new HashMap(); 
			for(Object o : v.keySet()){ 
			    List<BizObject> l=v.get(o);
			    String newsids="";
				
			    List <BizObject> deletel= new ArrayList();
			    for(BizObject b:l){
			    	
			    	if(post!=null){
			    		log(b.getId()+"    "+post.getString("newsids").indexOf(b.getId()));
				    	if(post.getString("newsids").indexOf(b.getId())>=0)
				    		b.set("checked", "true");
				    	else{
				    		
				    		b.set("checked", "false");
				    		deletel.add(b);
				    	}				    					    		
			    	}
			    	else
			    		b.set("checked", "true");			    		
			    }
			    l.removeAll(deletel);
			} 
			return v;
	}
	
	/**
	 * 取最后发送时间
	 * @param i
	 * @return
	 * @throws SQLException
	 */
	public static String getLastPostTime(int i) throws SQLException{
		BizObject  s = PostJob.parseMailList().get(i);
		String email = s.getString("address");
		String date;
		BizObject post = new BizObject("post");
		post.set("postmail", email);
		post.set("posted", "1");
		post.setOrderBy("posttime desc");
		post =post.getQF().getOne(post);
		
		if(post==null){
			//if(StringUtils.isNotBlank(job) && job.equals("default")){
				//如果是从最外层点进去的列表链接,则是查询默认近两天的日期的记录
				//endDate = DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR, -2);
				date = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
			//}

		}
		else
			date=DateUtils.formatDate(post.getDate("posttime"), DateUtils.PATTERN_YYYYMMDDHHMMSS);
		return date;
		
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
	public static  Map<String,List> getQryPosts(BizObject post) throws SQLException{
		//BizObject post;
		String tags=post.getString("tags");
		String cycle=post.getString("cycle");
		String urgent=post.getString("urgent");
		String limit = post.getString("limit");
		String lang= post.getString("lang");
		String posttime=post.getString("posttime");
		String address = post.getString("address");
		
		String sql="select * from news where status=1  and issue=1  ";
		int now = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		String key = tags+cycle+urgent+ limit+lang+now;
		//log("key is "+key+"   ");
		
		
		if(renderMap.get(key)==null){
			
			//Map<String ,List> m=new HashMap();			
			
			String tagsql=constructTagSql(tags);
			
			if(!tagsql.equals("")) sql=sql+" and "+tagsql;
			
			String cyclesql=constructCycleSql(cycle);
			
			if(!cyclesql.equals("")) sql=sql+" and "+cyclesql;
			//sql=sql+" and "+constructCycleSql(cycle);
			if(StringUtils.isNotBlank(urgent))
				sql= sql +" and  posted not like '%"+address+"%'";
			
			if(StringUtils.isNotBlank(urgent))
				sql=sql+" and "+" urgent>="+urgent.trim();
			
			if(StringUtils.isNotBlank(posttime))
				sql=sql+(" and posttime>=STR_TO_DATE('")+(posttime)+("','%Y-%m-%d %H:%i:%s')");
			
			
			sql=sql+"  order by  importance asc , posttime desc limit 0,"+limit;
			//log("sql is "+sql);
			List<BizObject> v = QueryFactory.executeQuerySQL(sql);
			
//			for(BizObject b:v){
//				b.refresh();
//			}
			
			
			if(tags.trim().equals("")) tags=GpMailAH.expTags();
			
			//log("tags is "+tags);
			String tag[]=tags.split(",");
			
			Map<String,List> newtags = new LinkedHashMap ();
			
			
			
			//if(tagstr.equals("")) tagstr=this.getParameter("tags");
			//String tag[] = tagstr.split(",");
			for(String s:tag){
				if(s.equals("")) continue;
				List<BizObject> onetags = new ArrayList();
				
				for(int i=0 ; i<v.size();i++){
					BizObject b=v.get(i);
					
					/**
					 * 根据配置文件决定文章归在哪一类tag,并决定语言显示summary
					 */
					if(b.getString("tags").indexOf(s)>=0){
						b.set("tag", s);
						if(lang==null||lang.equals("c")||lang.equals("")){
							b.set("summary",b.getString("c_summary"));
						}
						else if(lang.equals("e")){
							b.set("summary",b.getString("summary"));
						}
						else if(lang.equals("ce")){
							b.set("summary",b.getString("c_summary")+"<br>"+b.getString("summary"));
						}
						else if(lang.equals("ec")){
							b.set("summary",b.getString("summary")+"<br>"+b.getString("c_summary"));
						}
						
						//log("put  "+s+"  "+);
						onetags.add(b);
						v.remove(b);
						i--;
					}
				}	
				//log("put "+s+"  "+onetags.size());
				newtags.put(s, onetags);
			
			
			//return v;
			//return post.getQF().query(post,pv);
			
		}
			renderMap.put(key,newtags);
			return newtags;
		
	}
		else
		return (Map<String,List>) renderMap.get(key);
	}
	public  static String render(Map<String ,List> postv, String greeting, String ending) throws IOException, TemplateException, SQLException{
		
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
	        root.put("www_url", SystemKit.getParamById("system_core","www_url"));
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
	
	public static  List<BizObject> parseMailList(){
		
		
		if(postList!=null)  return postList;
		
		MailConfig mc=null;
		List<String> mailv = mc.readTxtFile("/root/erp.upload/maillist");
		int i=0;

		List<BizObject> v = new ArrayList();
		for(String s:mailv){
			if(s.indexOf("#")>=0) continue;
			logger.info(s);
			String[] mailinfo=s.split(";");
			if(mailinfo.length!=13){
				logger.info(i+" txt解析错误 "+mailinfo[0]);
				//ret=ret+i+" txt解析错误 "+mailinfo[0];
				continue;
			}
			String serialno=mailinfo[0].trim();
			String name=mailinfo[1].trim();
			String address=mailinfo[2].trim();
			String tags=mailinfo[3].trim();
			String cycle=mailinfo[4].trim();
			String limit=mailinfo[5].trim();
			String enable=mailinfo[6].trim();
			String subject=mailinfo[7].trim();
			String greeting=mailinfo[8].trim();
			String ending=mailinfo[9].trim();
			String urgent=mailinfo[10].trim();
			String telphone=mailinfo[11].trim();
			String lang=mailinfo[12].trim();
			
			BizObject email = new BizObject("email");
			email.set("serialno", serialno);
			email.set("name", name);
			email.set("address", address);
			email.set("cycle", cycle);
			email.set("limit", limit);
			email.set("enable", enable);
			email.set("subject", subject);
			email.set("greeting", greeting);
			email.set("ending", ending);
			email.set("urgent", urgent);
			email.set("telphone", telphone);
			email.set("lang", lang.toLowerCase());
			v.add(email);
		}
		
		postList=v;
		return postList;
	}
	
	@Override
	public String run() throws Exception {
		
		String ret="";
		
		MailConfig mc=null;
		
//		this.addOldTags();
//		if(1==1) return "ok";
		//List<String> mailv = mc.readTxtFile("/root/erp.upload/maillist");
		int i=0;
		
		List<BizObject> mailv=parseMailList();



		for(BizObject s:mailv){

			
			BizObject email = new BizObject("email");

			// address=mailinfo[1];
			email.set("toaddr", s.getString("address"));
			//Map<String ,List> v = this.getQryPosts(s.getString("tags"), s.getString("cycle"), s.getString("urgent"), s.getString("limit"),s.getString("lang"));
			Map<String ,List> v  = this.getPosts(i);
			String greeting=s.getString("greeting").replaceAll("@name",s.getString("name"));
			String ending=s.getString("ending").replaceAll("@date", DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS));
			
			String content=this.render(v,greeting,ending);
			email.set("content",content);
			String subject=s.getString("subject").replaceAll("@name",s.getString("name"));
		//	log("title "+subject);
			//email.set("title", title);
			email.set("subject", subject);
			
			

			boolean success =MailServer.sendMailSyn(email);//.sendMailSyn(email);
			
			BizObject post = new BizObject("post");
			//post.set("postmail", o)
			post.set("postmail", s.getString("address"));
			post.set("posted", "0");
			
			post.set("tags", s.getString("tags"));
			
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
			post.set("name", s.getString("name"));
			
			this._jdo.add(post);
			
			i++;
		}
 		
		return super.OK+"，共发送 "+i+"封邮件 "+ret;
	}

}
