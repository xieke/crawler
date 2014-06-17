package sand.depot.job;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.news.GpMailAH;
import sand.actionhandler.news.NewsActionHandler;
import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.SystemKit;
import sand.mail.MailSender;
import sand.mail.MailServer;
import sand.service.basic.service.TagService;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.JDO;
import tool.dao.QueryFactory;
import tool.dao.UidGenerator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class PostJob extends BaseJob {

	
	public static List<BizObject>  postList;
	
	static Logger logger = Logger.getLogger(PostJob.class);

	public PostJob(){
		super.setMultiRun(true); //可以并行执行
	}
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
					tag_sql ="(  tag_ids like '%"+t.trim()+"%' ";	
				else
					tag_sql=tag_sql+" or "+" tag_ids like '%"+t.trim()+"%' ";
			}
			
		}
		if(!tag_sql.equals("")) 
			tag_sql=tag_sql+")";
		else
			tag_sql = " tag_ids is not null ";
		
		return tag_sql;
	}
	
	public static  Map<String ,List> getPostNews(int i) throws SQLException{
		BizObject  s = PostJob.parseMailList().get(i);
		s.set("posttime", getLastPostTime(i));
		Map<String ,List> v = PostJob.getQryPostNews(s);
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
	
	public static String parseTags(String tags,String lang){
		String [] ts = tags.split(",");
		String ret="";
		for(String s:ts ){
			
			if(s.equals("")) continue;
			if(ret.equals("")){
				ret=ret+"["+parseTag(s,lang)+"]";
			}
			else{
				ret=ret+",["+parseTag(s,lang)+"]";
			}
				
		}
		return ret;
	}
	public static String parseTag(String tag,String lang){
		String[] s=tag.split("/");
		if(s.length!=2)
			return tag;
		System.out.println(s[0]+"          "+s[1]);
		
		if(lang.equals("e")){
			
			if(s[0].indexOf('(')>=0){
				return s[0]+")";
			}
			else
				return s[0];
					
		}
		if(lang.equals("c")){
			
			if(s[0].indexOf('(')>=0){
				return s[0].substring(0,s[0].indexOf('(')+1)+s[1];
			}
			else{
				return s[1];
			}
					
		}
		return tag;
		
	}
	private  String parseFont(String font){
		   String result = "";
		       
		       String regex = "<\\s*font\\s+([^>]*)\\s*>"; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
		     //  String regex2 = "([a-z]+)\\S*=\\s*\"([^\"]+)\""; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
		       Pattern p1 = Pattern.compile(regex);
		     //  Pattern p2 = Pattern.compile(regex2);
		       Matcher m1 = p1.matcher(font);
		       while (m1.find()) {
		          result = m1.group(1);
		        //  parseFont2(result);
		           System.out.println("it is "+result);
		       
		       }
		       //String replacement = pocessImgWidth(img);
		       //restult = restult.replaceAll(img, replacement);       
		       return result;   

		}
	
	
	//static Map<String,Object> renderMap = new HashMap();
	/**
	 * 取待发送的post列表。 每次限取20条
	 * 按重要度排序，列表数应该有一个最大的限制
	 * 一篇文章，一个客户，应该只发一次
	 * 
	 * 那每次发送文章都要记录客户email.防止重复发送
	 * @return
	 * @throws SQLException
	 */
	public static  Map<String,List> getQryPostNews(BizObject rule) throws SQLException{
		
		//BizObject post;
		String tagids=rule.getString("tag_ids");
		//String cycle=post.getString("cycle");
		String urgent=rule.getString("urgent");
		String importance = rule.getString("importance");
		String limit = rule.getString("limits");
		String lang= rule.getString("lang");
		String lastposttime=rule.getString("lastposttime");
		//String address = post.getString("address");
		
		String sql="select * from news where issue=1  ";
		int now = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		String key = tagids+lastposttime+importance+urgent+ limit+lang+now;
		log("key is "+key+"   ");
		
		//Map<String,Object> renderMap = new HashMap();
		//if(renderMap.get(key)==null){
			
			//Map<String ,List> m=new HashMap();			
			
			String tagsql=constructTagSql(tagids);
			
			if(!tagsql.equals("")) sql=sql+" and "+tagsql;
			
			if(StringUtils.isNotBlank(urgent))
				sql=sql+" and "+" urgent>="+urgent.trim();
			
			if(StringUtils.isNotBlank(importance))
				sql=sql+" and "+" importance>="+importance.trim();
			
			if(StringUtils.isNotBlank(lastposttime))
				sql=sql+(" and updatedate>=STR_TO_DATE('")+(lastposttime)+("','%Y-%m-%d %H:%i:%s')");
			
			
			sql=sql+"  order by  importance asc , updatedate desc limit 0,"+limit;
			log("sql is "+sql);
			List<BizObject> allv = QueryFactory.executeQuerySQL(sql);
			
			Map<String,List> tagsMap = renderMap(allv,tagids,lang);
			
			//log("total is "+total );
			//renderMap.put(key,tagsMap);
			return tagsMap;
		
//	}
//		else{
//			logger.info("return from cache ... ");
//			return (Map<String,List>) renderMap.get(key);	
//		}
		
	}
	
	public static void parseTags(BizObject b,String lang){
		String tags=b.getString("tags");		
		String tags2 = PostJob.parseTags(tags, lang);
		b.set("tags2", tags2);
	}
	public static void parseSummary(BizObject b,String lang){
		String summary = b.getString("summary");
		String c_summary=b.getString("c_summary");
//		logger.info("lang is "+lang+ "  summary is "+summary+"  c_summary is "+c_summary);
		if(lang==null||lang.equals("c")||lang.equals("")){
			b.set("summary",c_summary);
		}
		else if(lang.equals("e")){
			b.set("summary",summary);
		}
		else if(lang.equals("ce")){
			b.set("summary",c_summary+"<br>"+summary);
		}
		else if(lang.equals("ec")){
			b.set("summary",summary+"<br>"+c_summary);
		}

	}
	
	public static Map renderMap(List<BizObject> allv, String tagids,String lang) throws SQLException{
		
		Map<String,List> tagsMap = new LinkedHashMap ();
		if(tagids.trim().equals("")||tagids.trim().equals(",")) 
			tagids=GpMailAH.expTagIds();
		
		logger.info("tags is "+tagids);
		String tagid[]=tagids.split(",");

		for(String tid:tagid){
			
			if(tid.equals("")) continue;
			
			
			
			BizObject tag= new BizObject("tag");
			BizObject ptag= new BizObject("tag");
			String tagname="";
			String ptagname="";
			String oldptagname="";
			tag.setID(tid);
			tag.refresh();
			
			tagname=tag.getString("name");						
			tagname=parseTag(tagname, lang);
			ptag=tag.getBizObj("parent_id");
			if(ptag!=null){
				ptagname=ptag.getString("name");
				ptagname=parseTag(ptagname, lang);
				
			}
			
			//logger.info("tagname is "+tagname+"  parent tagname is "+ptagname+"  oldptagname is "+oldptagname);
			
			List<BizObject> onetags = new ArrayList();
			
			for(int i=0 ; i<allv.size();i++){
				
				
				BizObject b=allv.get(i);					
			//	b.set("allids", allids);
				//log("lastid "+b.getString("lastid")+"  nextid "+b.getString("nextid"));
				/**
				 * 根据配置文件决定文章归在哪一类tag,并决定语言显示summary
				 */
				if(b.getString("tag_ids").indexOf(tid)>=0){
					//logger.info("begin process tag id "+tid);
					
					parseSummary(b,lang);
					parseTags(b,lang);
					
					//summary为空，不发邮件
					if(b.getString("summary").equals("")) {
						//logger.info("issue 为 1 的记录  summar  却为  空！！！！！！！！！！！！！！！！！！！！！！！   "+b.getString("title"));
						continue;
					}
					else{
						//logger.info("add  summary: "+b.getString("summary"));
						onetags.add(b);
						allv.remove(b);
						i--;						
					}
				}
			}	
			//log("put "+s+"  "+onetags.size());
			if(onetags.size()>0){
				if(!ptagname.equals("")){
					logger.info("put parent tag name  "+ptagname);
					if(tagsMap.get(ptagname)==null)
						tagsMap.put(ptagname, new ArrayList()); //放一个父标签
				}
					
				//total = total +onetags.size();
				tagsMap.put(tagname, onetags);
				logger.info("put "+tagname+"   "+onetags.size());
			}
			
		
		
		//return v;
		//return post.getQF().query(post,pv);
		
	}
		return tagsMap;
	}
	public  static String render(Map<String ,List> postv, String greeting, String ending,String email,String jobid) throws IOException, TemplateException, SQLException{
		
		 Configuration cfg; 
	        cfg = new Configuration();
	        // - Templates are stoted in the WEB-INF/templates directory of the Web app.
	        cfg.setServletContextForTemplateLoading(ActionHandler._context  , "WEB-INF/templates");
	       // cfg.set
	       // this._dispatched=true;
			// Build the data-model
	        Map root = new HashMap();
	       // root.put("message", "Hello World!");
	        root.put("greeting", greeting);
	        root.put("ending",ending);
	        root.put("email",email);
	      //  root.put("senddate",new Date());
	        root.put("jobid", jobid);
	        //this.render();
	        root.put("www_url", SystemKit.getParamById("system_core","www_url"));
	        root.put("objList", postv);
	       // this.renderHtml();
	         //2                      bv 
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
	private static TagService tagService;
	public static TagService getTagService() {
		if(tagService == null) 
			tagService = (TagService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("tagService");
		return tagService;
	}
	

	public static void main(String args[]){
		System.out.println(new PostJob().parseTag("Vipshop/唯品会", "c"));
	}
	
	public static void main2(String args[]){
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
	
	/**
	 * 检查时间间隔，不能小于23个小时
	 * @param postjob
	 * @return
	 */
	private static boolean checkDateDiv(BizObject postjob){
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.HOUR, -23);
		
		Calendar c2 = Calendar.getInstance();
		if(postjob.getDate("lastposttime")== null){
			c1.add(Calendar.HOUR, -2);
			postjob.set("lastposttime", c1.getTime());
			return true;
		}
			
		
		c2.setTime(postjob.getDate("lastposttime"));
		
		if(c1.before(c2)){
			log( "时间间隔太短"+",上次发送时间 "+postjob.getDate("lastposttime")+"  --- now   "+new Date());
			//postjob.set("memo",postjob.getString("memo")+"时间间隔太短"+",上次发送时间 "+postjob.getDate("lastposttime")+"  --- now   "+new Date());
			
			//memo=memo+"时间间隔太短"+",上次发送时间 "+postjob.getDate("lastposttime")+"  --- now   "+new Date();
			return false;
		}
		else
			return true;
		
	}
	
	/**
	 * 日期是否合法
	 * @param rule
	 * @return
	 */
	private  boolean checkDateValid(BizObject rule){
		String w1 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+"";
		String w2 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+"";
		String w3 =minute+"";
		
		String cycle = rule.getString("cycle");
		String posthour= rule.getString("posthour");
		String postminute= rule.getString("postminute");
		int w = Integer.parseInt(w1);
		w=w-1;
		if (w==0) w=7;
		log("check date valid "+w+" =  "+cycle+"   ,    "+w2+" = "+posthour+" , "+w3+" = "+postminute);
		if (cycle.indexOf(w+"")>=0&&posthour.equals(w2)&&postminute.equals(w3)){
			return true;
		}		
		else{
			log("日期不符合  ，exit");
			return false;
		}
			
	}
	/*
	 * 取map中所有newsid
	 */
	public static String getAllNewsId(Map<String,List> m){
		String newsids="";
		//int newscount=0;
		for(String o : m.keySet()){ 
		   List<BizObject>  list=m.get(o); 
			for(BizObject b:list){
				//newscount++;
				//NewsActionHandler.clickIt(email.getString("bcc"), b.getId(), "send",_jdo);
				if(newsids.equals(""))
					newsids=b.getId();
				else
					newsids=newsids+","+b.getId();
			}
			
		} 
		return newsids;
	}

	
	private static String getSelectedTagTree(String ruleid) throws SQLException{
		List<String> tagids = getTagService().getTagIdsByRuleId(ruleid);
		List<BizObject> tagtree = getTagService().getSelectdTagsTree(tagids);
		
		String tree="";
		for(BizObject b:tagtree){
			tree=tree+"<br>"+b.getString("level")+""+b.getString("name");
		}
		return tree;
		
	}
	private static String getAllTagTree() throws SQLException{
		List<BizObject> list = getTagService().openAllWithSelectedTag("");
	//	List<String> tagids = getTagService().getTagIdsByRuleId(ruleid);
		//List<BizObject> tagtree = getTagService().getUnSelectdTagsTree(tagids);
		
		String tree="";
		for(BizObject b:list){
			tree=tree+"<br>"+b.getString("level")+""+b.getString("name");
		}
		return tree;
		
	}	
	private static String getUnSelectedTagTree(String ruleid) throws SQLException{
		List<String> tagids = getTagService().getTagIdsByRuleId(ruleid);
		List<BizObject> tagtree = getTagService().getUnSelectdTagsTree(tagids);
		
		String tree="";
		for(BizObject b:tagtree){
			tree=tree+"<br>"+b.getString("level")+""+b.getString("name");
		}
		return tree;
		
	}		
	
	/**
	 * 处理单个邮件发送任务
	 * @param job
	 * @param jdo
	 * @return
	 * @throws SQLException
	 * @throws TemplateException
	 */
	public static String processJob(BizObject job,JDO jdo) throws SQLException, TemplateException{

		String result ="";
		
		BizObject rule =job.getBizObj("ruleid");
		rule.set("limits", job.getString("limits"));
		//log("post time is "+ job.getString("posttime"));
		//rule.set("posttime", job.getString("posttime"));
		
		//if(!checkDateValid(rule)) return;

		//if(!checkDateDiv(job)) return;				

		//if(job.getString("mailserver").equals("")) return;
		
		rule.set("lastposttime", job.getDate("lastposttime"));
		
		//开始处理单个任务
		Map<String ,List> m = getQryPostNews(rule);	
		
		if(m.isEmpty()){
			
			
			job.set("executetime", new Date());
			job.set("memo","没有符合的记录，退出");					
			//如果发送成功，才更新最后发送时间
//			if(success)
//				job.set("lastposttime", new Date());
			jdo.update(job);
			//job.set("newsids", newsids);
			//result = result+" 发送文章："+newscount+" , ";
			//job.set("content", content_posted);
			job.resetObjType("posted");
			job.set("lang", rule.getString("lang"));
			job.set("postjobid", job.getId());
			job.setID("");//这里是新的postid
			//job.set("executetime", new Date());
			job.set("result", "success");
			jdo.add(job);   
			logger.info("add job "+job);
			logger.info("没有符合的记录，退出");
			return "没有符合的记录，退出";
		}
		
		logger.info("map  size is "+m.size());
		for(String key:m.keySet()){
			List v =m.get(key);
			logger.info("key is "+key+"  size is  "+v.size());
		}
		String c_ids[]=job.getString("customers").split(",");
		String content_posted="";
		String postid=UidGenerator.getUUId();
		
		String memo="";

		String selectTree=getSelectedTagTree(rule.getId());
		String uselectTree=getUnSelectedTagTree(rule.getId());
		String allTree=getAllTagTree();
		boolean success=false;
		for(String cid:c_ids){
			if(cid.equals("")) continue;
			BizObject c = new BizObject("customers");
			c.setID(cid);
			c.refresh();
			log("begin 处理 客户 "+c.getString("name"));
			
			if(c.getString("status").equals("0")){
				log(" 客户 "+c.getString("name")+"  已被删除，忽略");
				continue;
			}
			String emailaddress=c.getString("email");
			String greeting=rule.getString("head").replaceAll("@name",c.getString("name"));
			
			greeting = greeting.replaceAll("@subscribedTags", selectTree);			
			greeting = greeting.replaceAll("@unsubscribedTags", uselectTree);
			greeting = greeting.replaceAll("@allTags", allTree);
			
			String ending=rule.getString("foot").replaceAll("@date", DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDD));
			ending=ending.replaceAll("@name", c.getString("name"));
			
			
			ending = ending.replaceAll("@subscribedTags", selectTree);			
			ending = ending.replaceAll("@unsubscribedTags", uselectTree);
			ending = ending.replaceAll("@allTags", allTree);
			
			String subject=rule.getString("title").replaceAll("@name",c.getString("name")).replaceAll("@date", DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDD));
			String content="";
		
			try {
				
				content=render(m,greeting,ending,emailaddress,postid);
				content_posted=content;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log("error , ",e);
				e.printStackTrace();
				job.set("memo", job.getString("memo")+","+e.getMessage());
			}
			
			BizObject email = new BizObject("email");
			// address=mailinfo[1];
			email.set("toaddr", emailaddress);
			//email.set("bcc",emailaddress);
			email.set("content",content);
			//	log("title "+subject);
			//email.set("title", title);
			email.set("subject", subject);
			
			log("begin send mail ,content: "+email.getString("content").length());
			//boolean success =MailServer.sendMailSyn(email);//.sendMailSyn(email);
			
			
			if(!job.getString("mailserver").equals("")){
				MailSender mailSender = new MailSender(job.getString("mailserver"));	
				success=mailSender.sendMailSyn(email);
				result=result + emailaddress +"  发送结果：  "+success+" , ";
				//每次发完邮件，休息一秒钟
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				success=false;
			
			memo=memo+" ,\r\n"+c.getString("name")+"-"+c.getString("email")+"-"+new Date()+"-"+success;
					
			
			//boolean success =true;
								
		}
		
		String newsids = getAllNewsId(m);

		job.set("memo",memo);
		job.set("executetime", new Date());
		
		//如果发送成功，才更新最后发送时间
		if(success){
			job.set("lastposttime", new Date());
			job.set("result", "success");
		}
		else
			job.set("result", "fail");
			
		
		jdo.update(job);
		job.set("newsids", newsids);
		//result = result+" 发送文章："+newscount+" , ";
	//	job.set("result", success);
		job.set("content", content_posted);
		job.resetObjType("posted");
		job.set("postjobid", job.getId());
		job.set("lang", rule.getString("lang"));
		//job.set("executetime", new Date());
		job.setID("");//这里是新的postid
		jdo.add(job);   
						
		return result;
	}	
	
	int minute=0;
	
	public String run() {
		//super.setMultiRun(true);
		minute = Calendar.getInstance().get(Calendar.MINUTE);
		//super.setSynRun(false);  //本任务可以同时执行
		String ret;
		BizObject postjob = new BizObject("postjob");
		try {
			
			postjob.set("status", 1);  //  1 是 激活

			List<BizObject> v = postjob.getQF().query(postjob);
			String memo="";
			for(BizObject job:v){
				log("begin 处理  job "+job.getString("name"));
				BizObject rule =job.getBizObj("ruleid");
				//rule.set("limits", job.getString("limits"));
				//log("post time is "+ job.getString("posttime"));
				rule.set("posthour", job.getString("posthour"));
				rule.set("postminute", job.getString("postminute"));
				
				if(!checkDateValid(rule)) continue;

				if(!checkDateDiv(job)) continue;				

				if(job.getString("mailserver").equals("")) continue;				
				
				processJob(job, _jdo);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log("",e);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log("",e);
		}
		
		return super.OK+","+postjob.getString("memo");
		
	}
	

	
//	public String run2() throws Exception {
//		
//		String ret="";
//		
//		MailConfig mc=null;
//		
////		this.addOldTags();
////		if(1==1) return "ok";
//		//List<String> mailv = mc.readTxtFile("/root/erp.upload/maillist");
//		int i=0;
//		
//		List<BizObject> mailv=parseMailList();
//
//		for(BizObject s:mailv){
//
//			BizObject email = new BizObject("email");
//			// address=mailinfo[1];
//			email.set("bcc", s.getString("address"));
//			//Map<String ,List> v = this.getQryPosts(s.getString("tags"), s.getString("cycle"), s.getString("urgent"), s.getString("limit"),s.getString("lang"));
//			Map<String ,List> v  = this.getPostNews(i);
//			String greeting=s.getString("greeting").replaceAll("@name",s.getString("name"));
//			String ending=s.getString("ending").replaceAll("@date", DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS));
//			
//			String content=this.render(v,greeting,ending,"");
//			email.set("content",content);
//			String subject=s.getString("subject").replaceAll("@name",s.getString("name"));
//		//	log("title "+subject);
//			//email.set("title", title);
//			email.set("subject", subject);
//			
//			
//
//			boolean success =MailServer.sendMailSyn(email);//.sendMailSyn(email);
//			
//			BizObject post = new BizObject("post");
//			//post.set("postmail", o)
//			post.set("postmail", s.getString("address"));
//			post.set("posted", "0");
//			
//			post.set("tags", s.getString("tags"));
//			
//			post.set("posttime", new Date());
//			
//			String newsids="";
//			
//			for(String o : v.keySet()){ 
//			   List<BizObject>  list=v.get(o); 
//				for(BizObject b:list){
//					
//					
//					
//					if(newsids.equals(""))
//						newsids=b.getId();
//					else
//						newsids=newsids+","+b.getId();
//				}
//
//			} 
//			post.set("newsids", newsids);
//			post.set("success", success); //是否成功发送
//			post.set("name", s.getString("name"));
//			
//			this._jdo.add(post);
//			
//			i++;
//		}
// 		
//		return super.OK+"，共发送 "+i+"封邮件 "+ret;
//	}

}
