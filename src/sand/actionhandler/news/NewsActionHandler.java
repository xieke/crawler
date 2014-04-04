package sand.actionhandler.news;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.annotation.TokenCheck;
import sand.depot.job.BaseJob;
import sand.depot.job.PostJob;
import sand.depot.job.QuartzManager;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.InfoException;
import sand.depot.tool.system.SystemKit;
import sand.mail.MailSender;
import sand.service.basic.service.TagService;
import sand.service.news.NewsService;
import sand.util.HttpRequestDeviceUtils;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;
import tool.dao.UidGenerator;
import tool.util.CachedResponseWrapper;
import basic.BasicContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@AccessControl("no")
public class NewsActionHandler extends ActionHandler {
	
	static Logger logger = Logger.getLogger(NewsActionHandler.class);
	private TagService tagService;
	private NewsService newsService;

	public NewsActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "news";
		this._moduleId = "news";
	}
	public void render3(){
		
	}
	
	public void render() throws SQLException {
		log("in render  begin render ...............................");
		String[] outids = this.getParameters("outids");
		//List<BizObject> objList = queryList();
		log(" lang is "+this.getParameter("lang"));
		String lang=this.getParameter("lang");
		
		List<BizObject> v= new ArrayList();  
		if(outids==null) throw new ErrorException("没有文章");
		for(String id:outids){
			BizObject b = new BizObject("news");
			b.setID(id);
			b.refresh();
			v.add(b);
		}
		//至此，v代表所有要显示的新闻列表
		
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		String tagids=this.getParameter("tag_ids2");

		Map<String,List> newtags = PostJob.renderMap(v, tagids, lang);
		

		this._request.setAttribute("objList",newtags);
		
		this.setParam();
		
		this._nextUrl = "/template/news/render.jsp";
	}
	

	@CandoCheck("session")
	public void toListMail() throws SQLException{
		//this.setPageSize(2000);
		//this.setAttribute("pagesize", 2000);
		log("163  list mail tag "+this.getParameter("tags2"));
		//List<BizObject> objList = queryList();
		String[] str = this.getParameter("tag_ids2").split(",");

		//this._request.setAttribute("objList", objList);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
	//	String tagstr="";
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			
			for(BizObject biz : list){
				if(tagList.contains(biz.getId())){
					biz.set("checked", "checked");
					//tagstr=tagstr+"  "+biz.getString("name");
					
				} 
			}
		}
		//this.setAttribute("tagstr", tagstr);
		this._request.setAttribute("tags", list);
		this.setParam();
		//this._request.setAttribute("tags", list);
		this._nextUrl = "/template/news/listmail.jsp";
	}
	@CandoCheck("listmail")
	@TokenCheck
	public void listMail() throws SQLException{
		//this.list();
		//this.setPageSize(2000);
		log("163  list mail tag "+this.getParameter("tags2"));
		List<BizObject> objList = queryList("basic.news");
		String[] str = this.getParameter("tag_ids2").split(",");

		this._request.setAttribute("objList", objList);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
	//	String tagstr="";
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			
			for(BizObject biz : list){
				if(tagList.contains(biz.getId())){
					biz.set("checked", "checked");
					//tagstr=tagstr+"  "+biz.getString("name");
					
				} 
			}
		}
		//this.setAttribute("tagstr", tagstr);
		this._request.setAttribute("tags", list);
		this.setParam();
		//this._request.setAttribute("tags", list);
		this._nextUrl = "/template/news/listmail.jsp";
	}
	
	
	public void listPhone() throws SQLException{
		//String newsids = this.getPosted().getString("newsids");
		String allids =this.getPosted().getString("newsids");
		logger.info(allids);
		String ids[]=allids.split(",");
		log("1 objid is "+_objId);
		List v=new ArrayList();
		
		for(int i=0;i<ids.length;i++){
			if(ids[i].equals("")) continue;
			BizObject b = new BizObject("news");
			b.setID(ids[i]);
			b.refresh();
			v.add(b);
		}
		PageVariable pv = this.preparePageVar();
		
		List v2=SystemKit.page(v,this.preparePageVar());
		
		this.setAttribute("objList", v2);
		//this.list();
		if (pv.npage>0)
			this._nextUrl="/template/mobile/list2.jsp";
		else
			this._nextUrl="/template/mobile/list.jsp";
	}

	@CandoCheck("session")
	@TokenCheck
	public void list() throws SQLException{
		this.setPageSize(200);
		List<BizObject> objList = queryList("basic.news");
		String[] str = this.getParameter("tag_ids2").split(",");

		this._request.setAttribute("objList", objList);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			for(BizObject biz : list){
				if(tagList.contains(biz.getId())) biz.set("checked", "checked");
			}
		}
		
		this.setParam();
		this._request.setAttribute("tags", list);
		this._nextUrl = "/template/news/list.jsp";
	}
	
	@CandoCheck("session")
	@TokenCheck
	public void list_bak() throws SQLException{
		this.setPageSize(200);
		List<BizObject> objList = queryList("basic.news_his_bak");
		String[] str = this.getParameter("tag_ids2").split(",");

		this._request.setAttribute("objList", objList);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			for(BizObject biz : list){
				if(tagList.contains(biz.getId())) biz.set("checked", "checked");
			}
		}
		
		this.setParam();
		this._request.setAttribute("tags", list);
		this._nextUrl = "/template/news/bak_list.jsp";
	}
	
	@CandoCheck("session")
	public void toList_bak() throws SQLException{
//		List<BizObject> objList = queryList();
		String[] str = this.getParameter("tag_ids2").split(",");

//		this._request.setAttribute("objList", objList);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			for(BizObject biz : list){
				if(tagList.contains(biz.getId())) biz.set("checked", "checked");
			}
		}
		
		this.setParam();
		this._request.setAttribute("tags", list);
		this._nextUrl = "/template/news/bak_list.jsp";
	}
	
	@CandoCheck("session")
	public void toList() throws SQLException{
//		List<BizObject> objList = queryList();
		String[] str = this.getParameter("tag_ids2").split(",");

//		this._request.setAttribute("objList", objList);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			for(BizObject biz : list){
				if(tagList.contains(biz.getId())) biz.set("checked", "checked");
			}
		}
		
		this.setParam();
		this._request.setAttribute("tags", list);
		this._nextUrl = "/template/news/list.jsp";
	}
	
	@CandoCheck("editNews")
	public void delete(String id) throws SQLException{
		BizObject news = new BizObject("news");
		news.setID(id);
		news.refresh();
		if(news==null) throw new ErrorException("要删除的记录不存在 ,请重新操作!");
		
		if(StringUtils.isNotBlank(news.getString("his_news_id"))){
			BizObject his = new BizObject("his_news");
			his.setID(news.getString("his_news_id"));
			his.set("status", BasicContext.STATUS_DISPOSE_DELETE);
			this.getJdo().update(his);
		}
		//删除文章对应的标签
		this.getTagService().deleteReBillTagsByBillId(news.getId());
		
		//删除新闻
		this.getJdo().delete(news);
	}
	
	@CandoCheck("editNews")
	public void deletes() throws SQLException{
	//	this.getJdo().beginTrans();
		String[] ids = this.getParameters("delid");
		for(String s : ids) this.delete(s);
		//this.getJdo().commit();
		this.list();
	}
	
	@CandoCheck("session")
//	@TokenCheck
    public void listHistory() throws SQLException{
		PageVariable pv=this.preparePageVar();
		pv.type="simple";
		super.listObj("his_news");
		System.out.println(this._sql);
		this._nextUrl = "/template/news/history_list.jsp";
	}
    
//	public  static  List<BizObject> queryList2(String job,String startDate,String endDate,String status,String title,String urgent,String author,String importance,String sort,String tag_ids2,String summary,String issue,String orderby,String order  ) throws SQLException{
////		String job = this.getParameter("job");
////		String startDate = this.getParameter("startDate");
////		String endDate = this.getParameter("endDate");
////		String status = this.getParameter("status");
////		String title = this.getParameter("title");
////		String urgent = this.getParameter("urgent");
////		String author = this.getParameter("author");
////		String importance = this.getParameter("importance");
////		String sort = this.getParameter("sort");
////		String tag_ids2 = this.getParameter("tag_ids2");
////		String orderby = this.getParameter("orderby");
////		String order = this.getParameter("order");
////		String summary = this.getParameter("summary");
////		String issue = this.getParameter("issue");
//		
//		if(StringUtils.isNotBlank(job) && job.equals("default")){
//			//如果是从最外层点进去的列表链接,则是查询默认近两天的日期的记录
//			endDate = DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
//			Calendar c = Calendar.getInstance();
//			c.add(Calendar.DAY_OF_YEAR, -20);
//			startDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
//		}
//		
//		StringBuilder sql = new StringBuilder("");
//		if(StringUtils.isBlank(tag_ids2)) sql.append("select DISTINCT n.id,n.title,n.posttime,n.fname,n.author,n.copyfrom,n.copyfromurl," +
//				"n.category_id,n.status,n.issue,n.hits,n.isrecommend,n.istop,n.isautotag,n.summary,n.c_summary,n.importance," +
//				"n.urgent,n.sort,n.his_news_id,n.createdate from basic.news n where 1=1 ");
//		else sql.append("select DISTINCT n.id,n.title,n.posttime,n.fname,n.author,n.copyfrom,n.copyfromurl," +
//				"n.category_id,n.status,n.issue,n.hits,n.isrecommend,n.istop,n.isautotag,n.summary,n.c_summary,n.importance," +
//				"n.urgent,n.sort,n.his_news_id,n.createdate from basic.news n left join basic.re_bill_tag r on n.id=r.bill_id where r.tag_id in (")
//				.append(tag_ids2).append(")");
//		
//		//STR_TO_DATE('2013-05-27','%Y-%m-%d')>=posttime
//		
//		if(StringUtils.isNotBlank(startDate)) sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d')");
//		if(StringUtils.isNotBlank(endDate)) sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate+" 23:59:59").append("','%Y-%m-%d %H:%i:%s')");
//		if(StringUtils.isNotBlank(status)) sql.append(" and n.status='").append(status).append("'");
//		if(StringUtils.isNotBlank(title)) sql.append(" and n.title like '%").append(title).append("%'");
//		if(StringUtils.isNotBlank(urgent)) sql.append(" and n.urgent='").append(urgent).append("'");
//		if(StringUtils.isNotBlank(author)) sql.append(" and n.author like '%").append(author).append("%'");
//		if(StringUtils.isNotBlank(importance)) sql.append(" and n.importance='").append(importance).append("'");
//		if(StringUtils.isNotBlank(sort)) sql.append(" and n.sort='").append(sort).append("'");
//		if(StringUtils.isNotBlank(summary)) sql.append(" and n.c_summary is not null "); //输出html的时候，没有summary无意义
//		if(StringUtils.isNotBlank(issue)) sql.append(" and n.issue ='").append(issue).append("'"); //输出html的时候，没有summary无意义
//		
//		if(StringUtils.isNotBlank(orderby)) sql.append(" order by ").append(orderby);
//		else sql.append(" order by n.posttime");
//		if(StringUtils.isNotBlank(order)) sql.append(" ").append(order);
//		else sql.append(" desc");
//
//		System.out.println(sql.toString());
//		//PageVariable pv 
//		List<BizObject> objList = QueryFactory.executeQuerySQL(sql.toString(),this.preparePageVar());
//		
//		return objList;
//	}	
	private List<BizObject> queryList(String tableName) throws SQLException{
		
		//QueryFactory.setPageType("simple");
		//PageVariable pv 
		PageVariable pv=this.preparePageVar();
		pv.type="simple";
		//pv.setPagesize(200);
		List<BizObject> objList = QueryFactory.executeQuerySQL(this.getQuerySQL(tableName),pv);

		return objList;
	}
	
	private String getQuerySQL(String tableName) throws SQLException{
		String job = this.getParameter("job");
		String startDate = this.getParameter("startDate");
		String startDate1 = this.getParameter("startDate1");
		String endDate1 = this.getParameter("endDate1");
		String endDate = this.getParameter("endDate");
		String status = this.getParameter("status");
		String title = this.getParameter("title");
		String urgent = this.getParameter("urgent");
		String author = this.getParameter("author");
		String importance = this.getParameter("importance");
		String sort = this.getParameter("sort");
		String tag_ids2 = this.getParameter("tag_ids2");
		String orderby = this.getParameter("orderby");
		String order = this.getParameter("order");
		String summary = this.getParameter("summary");
		String esummary = this.getParameter("esummary");
		String issue = this.getParameter("issue");
		String limit = this.getParameter("limit");
		String lang=this.getParameter("lang");
		String isweibo=this.getParameter("isweibo");
		String[] tag_ids = null;
		if(StringUtils.isNotBlank(tag_ids2))
			tag_ids = tag_ids2.split(",");
		String tag = this.getParameter("tag");
		
		if(StringUtils.isNotBlank(job) && job.equals("default")){
			//如果是从最外层点进去的列表链接,则是查询默认近两天的日期的记录
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, -20);
			endDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
			c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -2);
			startDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
		}else if(StringUtils.isNotBlank(job) && job.equals("his_bak")){
			//如果是从最外层点进去的列表链接,则是查询默认近两天的日期的记录
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -NewsService.history_dates);
			endDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
			c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, (-NewsService.history_dates-2));
			startDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
		}
		
		StringBuilder sql = new StringBuilder("");
//		if(StringUtils.isBlank(tag_ids2)) 
			sql.append("select DISTINCT n.id,n.title,n.posttime,n.fname,n.author,n.copyfrom,n.copyfromurl," +
				"n.category_id,n.status,n.issue,n.hits,n.isrecommend,n.istop,n.isautotag,n.summary,n.c_summary,n.importance," +
				"n.urgent,n.sort,n.his_news_id,n.tags,n.createdate,n.updatedate from ").append(tableName).append(" n where 1=1 ");
//		else sql.append("select DISTINCT n.id,n.title,n.posttime,n.fname,n.author,n.copyfrom,n.copyfromurl," +
//				"n.category_id,n.status,n.issue,n.hits,n.isrecommend,n.istop,n.isautotag,n.summary,n.c_summary,n.tags,n.importance," +
//				"n.urgent,n.sort,n.his_news_id,n.createdate from basic.news n left join basic.re_bill_tag r on n.id=r.bill_id where r.tag_id in (")
//				.append(tag_ids2).append(")");
			
		if(tag_ids!=null && tag_ids.length>0){
//			sql.append(" and (");
			StringBuilder sql2 = new StringBuilder("");
			for(int i=0;i<tag_ids.length;i++){
				if(StringUtils.isNotBlank(tag_ids[i])){
					if(StringUtils.isNotBlank(sql2)) sql2.append(" or ");
					sql2.append("tag_ids like '%").append(tag_ids[i]).append("%'");
				}
			}
			if(StringUtils.isNotBlank(sql2)) sql.append(" and (").append(sql2).append(")");
//			sql.append(")");
		}
//		else sql.append("select DISTINCT n.id,n.title,n.posttime,n.fname,n.author,n.copyfrom,n.copyfromurl," +
//				"n.category_id,n.status,n.issue,n.hits,n.isrecommend,n.istop,n.isautotag,n.summary,n.c_summary,n.tags,n.importance," +
//				"n.urgent,n.sort,n.his_news_id,n.createdate from basic.news n left join basic.re_bill_tag r on n.id=r.bill_id where r.tag_id in (")
//				.append(tag_ids2).append(")");
		
		//STR_TO_DATE('2013-05-27','%Y-%m-%d')>=posttime
		if(StringUtils.isNotBlank(startDate1)) sql.append(" and n.updatedate>=STR_TO_DATE('").append(startDate1).append("','%Y-%m-%d %H:%i:%s')");
		if(StringUtils.isNotBlank(endDate1)) sql.append(" and n.updatedate<=STR_TO_DATE('").append(endDate1).append("','%Y-%m-%d %H:%i:%s')");
		if(StringUtils.isNotBlank(startDate)) sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d %H:%i:%s')");
		if(StringUtils.isNotBlank(endDate)) sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate).append("','%Y-%m-%d %H:%i:%s')");
		if(StringUtils.isNotBlank(status)) sql.append(" and n.status='").append(status).append("'");
		if(StringUtils.isNotBlank(title)) sql.append(" and n.title like '%").append(title).append("%'");
		if(StringUtils.isNotBlank(urgent)) sql.append(" and n.urgent='").append(urgent).append("'");
		if(StringUtils.isNotBlank(author)) sql.append(" and n.author like '%").append(author).append("%'");
		if(StringUtils.isNotBlank(importance)) sql.append(" and n.importance='").append(importance).append("'");
		if(StringUtils.isNotBlank(sort)) sql.append(" and n.sort='").append(sort).append("'");
		
		if(StringUtils.isNotBlank(summary)){
		//	if(lang.equals("c"))
				sql.append(" and (n.c_summary is not null) "); //输出html的时候，没有summary无意义
			//else if(lang.equals("e"))
			//	sql.append(" and (n.summary is not null) "); //输出html的时候，没有summary无意义
			//else //if(lang.equals("ce"))
				//sql.append(" and (n.c_summary is not null or n.summary is not null) "); //输出html的时候，没有summary无意义
			//else
		} 
		if(StringUtils.isNotBlank(esummary)){
		//	if(lang.equals("c"))
				//sql.append(" and (n.c_summary is not null) "); //输出html的时候，没有summary无意义
			//else if(lang.equals("e"))
				sql.append(" and (n.summary is not null) "); //输出html的时候，没有summary无意义
			//else //if(lang.equals("ce"))
				//sql.append(" and (n.c_summary is not null or n.summary is not null) "); //输出html的时候，没有summary无意义
			//else
		} 
			
			
		if(StringUtils.isNotBlank(issue)) sql.append(" and n.issue ='").append(issue).append("'"); //输出html的时候，没有summary无意义
		if(StringUtils.isNotBlank(tag)) sql.append(" and n.tags is not null "); //输出html的时候，没有summary无意义
		if(StringUtils.isNotBlank(isweibo)) sql.append(" and n.isweibo ='1' "); //输出html的时候，没有summary无意义
		if(StringUtils.isNotBlank(orderby)) sql.append(" order by ").append(orderby);
		else sql.append(" order by n.posttime");
		if(StringUtils.isNotBlank(order)) sql.append(" ").append(order);
		else sql.append(" desc");

		//if(StringUtils.isNotBlank(limit)) sql.append(" limit 0, ").append(limit);
		//System.out.println(sql.toString());
		log("392 sql is "+sql.toString());
		//PageVariable pv 
		
		return sql.toString();
	}
	
	private void setParam() throws SQLException{
		String job = this.getParameter("job");
		String startDate = this.getParameter("startDate");
		String startDate1 = this.getParameter("startDate1");
		String endDate = this.getParameter("endDate");
		String endDate1 = this.getParameter("endDate1");
		String status = this.getParameter("status");
		String title = this.getParameter("title");
		String urgent = this.getParameter("urgent");
		String author = this.getParameter("author");
		String importance = this.getParameter("importance");
		String tags2 = this.getParameter("tags2");
		String sort = this.getParameter("sort");
		String lang = this.getParameter("lang");
		String limit = this.getParameter("limit");
		String pagesize = this.getParameter("pagesize");
		if(pagesize.equals("") ) pagesize="2000";
		String tag_ids2 = this.getParameter("tag_ids2");
		String orderby = this.getParameter("orderby");
		String order = this.getParameter("order");
		String rownum = this.getParameter("rownum");
		String page = this.getParameter("page");
		String summary=this.getParameter("summary");
		String esummary=this.getParameter("esummary");
		String isweibo=this.getParameter("isweibo");
		if(StringUtils.isBlank(orderby)) orderby="posttime";
		if(StringUtils.isBlank(order)) order="desc";
		
		if(StringUtils.isNotBlank(job) && job.equals("default")){
			//如果是从最外层点进去的列表链接,则是查询默认近两天的日期的记录
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, -20);
			endDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
			c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -2);
			startDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
		}else if(StringUtils.isNotBlank(job) && job.equals("his_bak")){
			//如果是从最外层点进去的列表链接,则是查询默认近两天的日期的记录
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -NewsService.history_dates);
			endDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
			c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, (-NewsService.history_dates-2));
			startDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
		}
		
		this._request.setAttribute("startDate", startDate);
		this._request.setAttribute("startDate1", startDate1);
		this._request.setAttribute("endDate", endDate);
		this._request.setAttribute("endDate1", endDate1);
		this._request.setAttribute("status", status);
		this._request.setAttribute("title", title);
		this._request.setAttribute("urgent", urgent);
		this._request.setAttribute("author", author);
		this._request.setAttribute("importance", importance);
		this._request.setAttribute("tags2", tags2);
		this._request.setAttribute("sort", sort);
		this._request.setAttribute("lang", lang);
		this._request.setAttribute("limit", limit);
		this._request.setAttribute("pagesize", pagesize);
		this._request.setAttribute("tag_ids2", tag_ids2);
		this._request.setAttribute("orderby", orderby);
		this._request.setAttribute("order", order);
		this._request.setAttribute("rownum", rownum);
		this._request.setAttribute("page", page);
		this._request.setAttribute("summary", summary);
		this._request.setAttribute("esummary", esummary);
		this._request.setAttribute("isweibo", isweibo);
	}
	
	public void switchIssue() throws SQLException{
		BizObject b = new BizObject("news");
		b.setID(this._objId);
		b.refresh();
		if(b.getString("issue").equals("1"))
			b.set("issue","0");
		else
			b.set("issue", "1");
		
		this.getJdo().update(b);
		log("切换状态的ID:"+b.getId()+",标题为:"+b.getString("title"));
		this.listMail();
			
	}
	
	private BizObject getPosted() throws SQLException{
		String jobid=this.getParameter("jobid");
		BizObject posted =new BizObject("posted");
		posted.setID(jobid);
		posted.refresh();
		return posted;
	}

	public void last() throws SQLException{
		
		String allids =this.getPosted().getString("newsids");
		logger.info(allids);
		String ids[]=allids.split(",");
		log("1 objid is "+_objId);
		for(int i=0;i<ids.length;i++){
			if(this._objId.equals(ids[i])){
				if(i>0)
					this._objId=ids[i-1];
				else
					this._objId=ids[i];
				break;
			}
		}
		log("2 objid is "+_objId);
		this.showIt();
	}
	public void next() throws SQLException{
		String allids =this.getPosted().getString("newsids");
		String ids[]=allids.split(",");
		log("1 objid is "+_objId);
		for(int i=0;i<ids.length;i++){
			if(this._objId.equals(ids[i])){
				if(i<ids.length-1)
					this._objId=ids[i+1];
				else
					this._objId=ids[i];
				break;
			}
		}
		log("2 objid is "+_objId);
		this.showIt();
	}

	public void showIt() throws SQLException{
				
		boolean result = HttpRequestDeviceUtils.isMobileDevice(_request);
		log("is mobile device ?   "+result);
		QueryFactory qf = new QueryFactory("news");
		BizObject biz = qf.getByID(this._objId);
		if(biz==null) throw new InfoException("没有文章了");
		biz.set("hits",biz.getInt("hits",0)+1);
		String tags = biz.getString("tags");
		//b.set("senddate",this.getPosted().getDate("lastposttime"));
		//BizObject posted = this.getPosted();
		String lang="";
		if(this.getPosted().getBizObj("ruleid")!=null)
			lang= this.getPosted().getBizObj("ruleid").getString("lang");
		else
			lang=this.getPosted().getString("lang");
		
		String tags2 = PostJob.parseTags(tags, lang);
		PostJob.parseSummary(biz, lang);
		//tags.substring(tags.length()-1,tags.length())
		if(tags.charAt(tags.length()-1)==','){
			tags=tags.substring(0,tags.length()-1);
			biz.set("tags", tags);
		}
//		String email = this.getParameter("email");
		String email=this.getParameter("email");

		this.clickIt("open");

		//this.getJdo().update(biz);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag(this._objId);
		List<BizObject> retags = this.getTagService().queryTagsByBillId(this._objId);
		
//		biz.set("content", biz.getString("content").replaceAll("'","&#039;"));
//		biz.set("content", biz.getString("content").replaceAll("&","&amp;"));
//		biz.set("content", biz.getString("content").replaceAll("\"","&quot;"));
//		biz.set("content", biz.getString("content").replaceAll("<","&lt;"));
//		biz.set("content", biz.getString("content").replaceAll(">","&gt;"));
		
		this._request.setAttribute("obj", biz);
		this._request.setAttribute("retags", retags);
		this._request.setAttribute("objList", list);
		this._request.setAttribute("commentsList", this.getComments(this._objId));
		this.setParam();
		//if (result)
			this._nextUrl = "/template/mobile/view.jsp";
//		else
//			this._nextUrl = "/template/news/show.jsp";
	}
	
	@CandoCheck("session")
	public void show() throws SQLException{
		QueryFactory qf = new QueryFactory("news");
		BizObject biz = qf.getByID(this._objId);
//		BizObject news_content = new BizObject("news_content");
//		news_content.set("news_id", biz.getId());
//		news_content = QueryFactory.getInstance("news_content").getOne(news_content);
		
		List<BizObject> list = this.getTagService().openAllWithSelectedTag(this._objId);
		
		if(biz!=null){
			biz.set("content", biz.getString("content").replaceAll("'","&#039;"));
			biz.set("content", biz.getString("content").replaceAll("&","&amp;"));
			biz.set("content", biz.getString("content").replaceAll("\"","&quot;"));
			biz.set("content", biz.getString("content").replaceAll("<","&lt;"));
			biz.set("content", biz.getString("content").replaceAll(">","&gt;"));
		}
		
		this._request.setAttribute("obj", biz);
//		this._request.setAttribute("news_content", news_content);
//		this._request.setAttribute("retags", retags);
		this._request.setAttribute("objList", list);
		this._request.setAttribute("commentsList", this.getComments(this._objId));

//		this._request.setAttribute("importanceList", this.queryImportances(biz.getString("importance")));
//		this._request.setAttribute("urgentList", this.queryUrgents(biz.getString("urgent")));
		this.setParam();
		this._nextUrl = "/template/news/edit.jsp";
	}
	
	public void show_operator() throws SQLException{
		long millinow = Calendar.getInstance().getTimeInMillis();
		this.log("进入show_operator:"+millinow);
		String operator = this.getParameter("operator");
		String rownum = this.getParameter("rownum");
		String from = this.getParameter("from");
		log("from is :"+from);
		
		int rows = 0;
		
		try{
			rows = Integer.parseInt(rownum);
		}catch(Exception e){
			e.printStackTrace();
			rows = 0;
		}
		if(from.equals("mail")){
			this._tipInfo = "编辑成功,请关闭当前页面！";
			this._request.setAttribute("msg_type", "SUCCESS");
			//this._request.setAttribute("nextUrl", "account.ReturnActionHandler.listMchReturnList");
			this._nextUrl = super._msgUrl;
			long endmillinow = Calendar.getInstance().getTimeInMillis();
	        long usetime = endmillinow - millinow;
	        this.log("结束show_operator  mail:"+usetime);
			return;
		}
		if(operator.equals("next")){
			rows += 1;
		}else if(operator.equals("last")){
			if(rows>0) rows -= 1;
			else {
				this.list();
				long endmillinow = Calendar.getInstance().getTimeInMillis();
		        long usetime = endmillinow - millinow;
		        this.log("结束 show_operator no last:"+usetime);
				return;
			}
		}else{
			this.list();
			long endmillinow = Calendar.getInstance().getTimeInMillis();
	        long usetime = endmillinow - millinow;
	        this.log("结束 show_operator list:"+usetime);
			return;
		}
		log("行与："+rows);
		StringBuilder sql = new StringBuilder("");
		sql.append(this.getQuerySQL("basic.news"));
		sql.append(" limit ").append(rows).append(",1");
		log("打印sql:"+sql.toString());
		
		QueryFactory qf = new QueryFactory("news");
		List<BizObject> rowslist = qf.executeQuerySQL(sql.toString());
		if(rowslist.size()>0){
			BizObject biz = qf.getByID(rowslist.get(0).getString("id"));
	//		BizObject news_content = new BizObject("news_content");
	//		news_content.set("news_id", biz.getId());
	//		news_content = QueryFactory.getInstance("news_content").getOne(news_content);
			
			List<BizObject> list = this.getTagService().openAllWithSelectedTag(biz.getId());
			
			if(biz!=null){
				biz.set("content", biz.getString("content").replaceAll("'","&#039;"));
				biz.set("content", biz.getString("content").replaceAll("&","&amp;"));
				biz.set("content", biz.getString("content").replaceAll("\"","&quot;"));
				biz.set("content", biz.getString("content").replaceAll("<","&lt;"));
				biz.set("content", biz.getString("content").replaceAll(">","&gt;"));
			}
			
			this._request.setAttribute("obj", biz);
	//		this._request.setAttribute("news_content", news_content);
			this._request.setAttribute("objList", list);
			this._request.setAttribute("commentsList", this.getComments(this._objId));
			this.setParam();
			
//			this._request.setAttribute("importanceList", this.queryImportances(biz.getString("importance")));
//			this._request.setAttribute("urgentList", this.queryUrgents(biz.getString("urgent")));
			this._request.setAttribute("rownum", rows);
			this._nextUrl = "/template/news/edit.jsp";
		}else this.list();
		
		long endmillinow = Calendar.getInstance().getTimeInMillis();
        long usetime = endmillinow - millinow;
        this.log("结束 show_operator:"+usetime);
	}
	
	@CandoCheck("resumeNews")
	public void resume() throws SQLException{
		BizObject biz = new BizObject("his_news");
		biz.setID(this._objId);
		biz.refresh();
		
		if(biz==null) throw new ErrorException("当前要恢复的原文章不存在,请重复操作!");

		biz.set("status", BasicContext.STATUS_DISPOSE_NO);
		this.getJdo().update(biz);
		
		BizObject news = biz.duplicate();
		news.resetObjType("news");
		news.set("status", BasicContext.STATUS_DISPOSE_NO);
		news.set("issue", BasicContext.ISSUE_NO);
		news.set("hits", 0);
		news.set("isrecommend", BasicContext.IS_RECOMMEND_NO);
		news.set("istop", BasicContext.ISTOP_NO);
		news.set("isautotag", BasicContext.ISAUTOTAG_NO);
		news.set("his_news_id", news.getId());
		this.getJdo().addOrUpdate(news);
		this._tipInfo = "恢复成功！";
		this._request.setAttribute("msg_type", "SUCCESS");
		this._request.setAttribute("nextUrl", "news.NewsActionHandler.listHistory?his_news$status=2");
		this._nextUrl = super._msgUrl;
	}
	
	@CandoCheck("moveNews")
	public void moveNewsHisToNews() throws SQLException{
		BizObject bak = new BizObject("news_his_bak");
		bak.setID(this._objId);
		bak.refresh();

		BizObject news  = bak.duplicate();
		news.resetObjType("news");
		BaseJob.currentSession().addOrUpdate(news);
		BaseJob.currentSession().delete(bak);

		this._tipInfo = "移动成功！";
		this.list_bak();
	}
	
//	private List<BizObject> queryUrgents(String news_urgent) throws SQLException{
//		String[] types = news_urgent.split(",");
//		List<BizObject> urgentList = SystemKit.getNoCachePickList("urgent");
//		List<String> typeList = new ArrayList<String>();
//		for(String type:types)typeList.add(type);
//		for(BizObject urgent : urgentList){
//			if(typeList.contains(urgent.getId())) urgent.set("checkedValue", urgent.getId());
//		}
//		return urgentList;
//	}
//	
//	private List<BizObject> queryImportances(String news_importance) throws SQLException{
//		String[] types = news_importance.split(",");
//		List<BizObject> importanceList = SystemKit.getNoCachePickList("importance");
//		List<String> typeList = new ArrayList<String>();
//		for(String type:types)typeList.add(type);
//		for(BizObject importance : importanceList){
//			if(typeList.contains(importance.getId())) importance.set("checkedValue", importance.getId());
//		}
//		return importanceList;
//	}
	
	/**
	 * 显示添加页面
	 * 
	 * @throws SQLException
	 */
	@CandoCheck("editNews")
	public void showAdd() throws SQLException{
		List<BizObject> list = this.getTagService().openAllWithSelectedTag(this._objId);
		this._request.setAttribute("objList", list);
		this.setParam();
		this._request.setAttribute("job", "default");
//		this._request.setAttribute("importanceList", this.queryImportances(""));
//		this._request.setAttribute("urgentList", this.queryUrgents(""));
		this._nextUrl = "/template/news/edit.jsp";
	}
	
	@CandoCheck("session")
	public void showHis() throws SQLException{
		QueryFactory qf = new QueryFactory("his_news");
		BizObject biz = qf.getByID(this.getParameter("objId"));
		this._request.setAttribute("obj", biz);
		this._nextUrl = "/template/news/view_his.jsp";
	}
	
	@CandoCheck("editNews")
	public void save() throws SQLException{
		long millinow = Calendar.getInstance().getTimeInMillis();
		this.log("进入save:"+millinow);
		String tagId = this.getParameter("tag_ids");
		BizObject news = this.getBizObjectFromMap("news");
		this.log("tags is "+this.getParameter("tags"));
		news.set("tags", this.getParameter("tags"));
		news.set("tag_ids", tagId);
		
		if(StringUtils.isBlank(news.getId())){
			news.set("posttime", new Date());
			
			BizObject his_news = news.duplicate();
			his_news.resetObjType("his_news");
			this.getJdo().addOrUpdate(his_news);
			news.setID(his_news.getId());
			news.set("his_news_id", his_news.getId());
			news.set("hits", 0);
			news.set("isrecommend", BasicContext.IS_RECOMMEND_NO);
			news.set("istop", BasicContext.ISTOP_NO);
			news.set("isautotag", BasicContext.ISAUTOTAG_NO);
			news.set("updatedate", new Date());
		}
		if(this.getParameter("update_date").equals("1")){
			news.set("updatedate", new Date());
		}
		this.getJdo().addOrUpdate(news);
		
		/**暂时先不用news_content表,先不移出去
		BizObject news_content = this.getBizObjectFromMap("news_content");
		if(StringUtils.isBlank(news_content.getString("news_id")))
			news_content.set("news_id", news.getId());
		this.getJdo().addOrUpdate(news_content);
		**/
		
		//保存备忘
		BizObject biz = this.getBizObjectFromMap("NEWCOMMENTS");
		if(StringUtils.isNotBlank(biz.getString("content"))){
			biz.set("aid", news.getId());
			biz.set("posttime", new Date());
			biz.set("userid", this._curuser.getId());
			// 添加一条备忘记录
			this.add(biz);
			this.getJdo().commit();
		}
		
		long endmillinow = Calendar.getInstance().getTimeInMillis();
        long usetime = endmillinow - millinow;
        this.log("编辑的文章id:"+news.getId()+",编辑的文章标签:"+news.getString("title"));
        this.log(news.toString());
        this.log("结束save:"+usetime);
		this.clearQueryParam();
		this.show_operator();
	}
	
	@CandoCheck("editNews")
	public void addComments() throws SQLException{
	//	this.getJdo().beginTrans();
		BizObject biz = this.getBizObjectFromMap("NEWCOMMENTS");
		if(StringUtils.isNotBlank(biz.getString("content"))){
			biz.set("aid", this.getParameter("objId"));
			biz.set("posttime", new Date());
			biz.set("userid", this._curuser.getId());
			// 添加一条备忘记录
			this.add(biz);
		//	this.getJdo().commit();
		}
		
		this.show();
	}
	
	public List<BizObject> getComments(String partnerId) throws SQLException{
		QueryFactory qf = new QueryFactory("NEWCOMMENTS");
		qf.setOrderBy("posttime desc");
		List<BizObject> objList = qf.query("aid",partnerId);
		return objList;
	}
	
	/**
	 * 删除备忘
	 * @throws SQLException
	 */
	@CandoCheck("editNews")
	public void delComments() throws SQLException{
		BizObject biz = new BizObject("NEWCOMMENTS");
		biz.setID(this.getParameter("comId"));
		//this.getJdo().beginTrans();
		this.delete(biz);
		//this.getJdo().commit();
		this.show();
	}

	public void gData() throws SQLException{
		List<BizObject> list = this.getNewsService().copyfromNews();
	//	this.getJdo().beginTrans();
		if(list.size()>0){
			this.getNewsService().addNews(list);
			this.getNewsService().deleteCopyforms();
		}
	//	this.getJdo().commit();
	}
	
	@CandoCheck("hittingTag")
	public void doHitting()	throws SQLException, ParseException{
		String startDate = this.getParameter("startDate");
		String endDate = this.getParameter("endDate");
		if(StringUtils.isBlank(startDate)) throw new ErrorException("请选择开始时间,目前只支持两天之内的手动标签");
		if(StringUtils.isBlank(endDate)) throw new ErrorException("请选择结束时间,目前只支持两天之内的手动标签");
		
		Date start = DateUtils.parse(startDate, DateUtils.PATTERN_YYYYMMDD);
		Date end = DateUtils.parse(endDate, DateUtils.PATTERN_YYYYMMDD);
		Calendar cstart = Calendar.getInstance();
		cstart.setTime(start);
		
		Calendar cend = Calendar.getInstance();
		cend.setTime(end);
		
		cstart.add(Calendar.DAY_OF_YEAR, 1);
		if(cstart.before(cend)) throw new ErrorException("目前只支持两天之内的手动标签!");
		
		BizObject biz = new BizObject("re_general");
		biz.set("id", "doHittingTag");
		biz.set("value", this.getParameter("tagRuleIds"));
		biz.set("aid", this.getParameter("startDate"));
		biz.set("bid",this.getParameter("endDate"));
		this.getJdo().addOrUpdate(biz);
		
//		this.getNewsService().doHittingTag(this.getParameter("tagRuleIds"), this.getParameter("startDate"), this.getParameter("endDate"));
//		this.list();
//
		BizObject task = new BizObject("TASK");
		task.setID("6774d070-1a94-4d88-8e58-44e62c2a61ce");
		task.set("TASKNAME", "手动标签"+DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDD));
		task.set("SERVERNAME", ActionHandler.SERVERNAME);
		task.set("TASKCYCLE", 6);
		task.set("ACTIVE", "1");
		task.set("TASKTIME", "16:30:00");
		task.set("runDATE", DateUtils.formatDate(new Date(),DateUtils.PATTERN_YYYYMMDD));
		task.set("TASKCLASS", "DoHittingTagJob");
		this.getJdo().addOrUpdate(task);
		
		QuartzManager.addTask(task,new HashMap());
		
		this._orderBy = "createdate desc";
		this.listObj("task");
		this._nextUrl = "/template/basic/task/taskList.jsp";
		
	}
	
	public TagService getTagService() {
		if(tagService == null) 
			tagService = (TagService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("tagService");
		return tagService;
	}
	
	public NewsService getNewsService() {
		if(newsService == null) 
			newsService = (NewsService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("newsService");
		return newsService;
	}
	
	public  String clickIt(String operator) throws SQLException{
		
		String email=this.getParameter("email");
		String newsid=this.getParameter("newsid");
		String senddate=this.getParameter("senddate");
		if(newsid.equals("")) newsid=this._objId;
		logger.info("newsid is "+newsid);

		if(!email.equals("")&&!newsid.equals("")){
			
			BizObject b = new BizObject("userclicks");
			b.set("email", email);
			b.set("newsid",newsid);
			b.set("operator", operator);
			
			
			if(b.getQF().getOne(b)!=null){
				return "ok";
			}
			
			b.set("senddate",this.getPosted().getDate("lastposttime"));
			
			BizObject customer = new BizObject("customers");
			customer.set("email",email);			
			customer = customer.getQF().getOne(customer);

			if(customer!=null){
					b.set("customerid", customer.getId());
					//customer.set("operate", customer.getString(operat)+","+this._objId);
			}

			b.set("operator", operator);
			b.set("clickdate",new Date());
			this.getJdo().addOrUpdate(b);
			return "ok";

		}
		logger.info("email "+email+"  newsid "+newsid);
		return "错误的参数";		
	}

	
	@Ajax
	public String like() throws SQLException{

		return clickIt("like");
		
	}
	
	@Ajax
	public String dislike() throws SQLException{

		return clickIt("dislike");
		
	
	}
	

	public String click(String likeordislike) throws SQLException{

		
		String email=this.getParameter("email");
		String newsid = this.getParameter("newsid");
		BizObject b = new BizObject("news");
		b.setID(newsid);
		b.refresh();
		//logger.
		if(b.getString("clickmails").indexOf(email)>=0)
			return "您已经点过了";
		b.set("clickmails", b.getString("clickmails")+","+email+"|"+likeordislike.substring(0,1));
		
		b.set(likeordislike, b.getInt(likeordislike,0)+1);
		b.set("content", null);
		this.getJdo().update(b);
		return "投票成功";
		
	
	}
	
	
	public void preview(){
		
	}
	
	public void sendAll(){
		
	}
	/**
	 * 发送邮件
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 * @throws TemplateException
	 */
	@CandoCheck("sendmail")
	public void sendMail() throws IOException, ServletException, SQLException, TemplateException{
		
		BizObject email = new BizObject("email");

		String address=this.getParameter("email");
		//email.set("toaddr", address);
		email.set("bcc", address);
		
		int i=address.indexOf("@");
		if(i<=0)
			throw new ControllableException("错误的邮件地址");
		
		//String sfix = address.substring(i+1);
		//logger.info("sfix is "+sfix);
		// email.set("fromaddr", "xieke3@hnair.com");

		String content=this.renderForSendMail(address,this.getParameter("tags"),this.getParameter("subject"),this.getParameter("greeting"),this.getParameter("ending"));
		email.set(
				"content",content);
		
//		Map<String,List> map = (Map<String, List>) _request.getAttribute("objList");


		if(this.getParameter("subject").equals(""))
			email.set("subject", "Goldpebble Research Customized News");
		else
			email.set("subject", this.getParameter("subject"));

		boolean result = false;
	//	if(mailExcludes.contains(sfix)){
		
		MailSender mailSender = new MailSender(this.getParameter("mailserver"));
		
		result=mailSender.sendMailSyn(email);
		//result=MailServer.sendMailSyn(email);
			

		if (result)
			_tipInfo="发送成功";
		else
			_tipInfo="发送失败";
		this._nextUrl="/template/basic/msg.jsp";
		
	

	}
	
	/**
	 * 渲染html
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws SQLException 
	 */
	
	private String renderHtml() throws IOException, ServletException, SQLException{
		//_dispatchType == DISPATCH_TYPE_DEFAULT
		_cachedResponse = new CachedResponseWrapper(_response);
		this.render();
		_request.getRequestDispatcher("/template/news/render.jsp").forward(_request,
				_cachedResponse);
		
		//this.dispatch("/template/news/render.jsp");
		//ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte [] b = new byte[5000];
		b=_cachedResponse.getResponseData();
		log("output is .................."+new String(b));
		return new String(b);
	}
	@CandoCheck("preview")
	public  void render2() throws IOException, ServletException, SQLException, TemplateException{
	   
		log("in render 2   ok  ,  tags is "+this.getParameter("tags2"));

		String[] str = this.getParameter("tag_ids2").split(",");
			
		
		String tree="";
		List<String> tagList = new ArrayList<String>();
		if(str!=null && str.length>0){
			
			for(String s : str) tagList.add(s);
			
		}
		List<BizObject> tagtree = getTagService().getSelectdTagsTree(tagList);
		for(BizObject b:tagtree){
			tree=tree+"<br>"+b.getString("level")+""+b.getString("name");
		}
		
		String utree="";
		tagtree = getTagService().getUnSelectdTagsTree(tagList);
		for(BizObject b:tagtree){
			utree=utree+"<br>"+b.getString("level")+""+b.getString("name");
		}


		String atree="";
		tagtree = getTagService().openAllWithSelectedTag("");

		for(BizObject b:tagtree){
			atree=atree+"<br>"+b.getString("level")+""+b.getString("name");
		}
		
//		List<String> tagids = getTagService().getTagIdsByRuleId(rule.getId());
		
		this.render();
		Configuration cfg; 
       cfg = new Configuration();
       // - Templates are stoted in the WEB-INF/templates directory of the Web app.
       cfg.setServletContextForTemplateLoading(this._context  , "WEB-INF/templates");
       
       this._dispatched=true;
		// Build the data-model
       Map root = new HashMap();
       root.put("message", "Hello World!");

       
       root.put("www_url", SystemKit.getParamById("system_core", "www_url"));
       root.put("objList", _request.getAttribute("objList"));
       root.put("tags",this.getParameter("tags"));
       root.put("email", this.getParameter("email"));
       root.put("tree",tree);
       root.put("utree",utree);
       root.put("atree",atree);
      // root.put("senddate",new Date());
       root.put("subject", this.getParameter("subject"));
       root.put("greeting", this.getParameter("greeting"));
       root.put("ending", this.getParameter("ending"));       
       // Get the templat object
       Template t = cfg.getTemplate("news/render.ftl");
       
       // Prepare the HTTP response:
       // - Use the charset of template for the output
       // - Use text/html MIME-type
       _response.setContentType("text/html; charset=" + t.getEncoding());
       Writer out = _response.getWriter();
       String s =FreeMarkerTemplateUtils.processTemplateIntoString(t, root);
      // String s;
     //  s.to
       // Merge the data-model and the template
       try {
           t.process(root, out);
       } catch (TemplateException e) {
           throw new ErrorException(
                   "Error while processing FreeMarker template"+e.getMessage());
       }

		//return _SUBMIT_TYPE;
	}
	private  String renderForSendMail(String email,String tags,String subject,String greeting,String ending) throws IOException, ServletException, SQLException, TemplateException{
		 Configuration cfg; 
        cfg = new Configuration();
        
        // - Templates are stoted in the WEB-INF/templates directory of the Web app.
        cfg.setServletContextForTemplateLoading(this._context  , "WEB-INF/templates");
        cfg.setDefaultEncoding("UTF-8");
       // this._dispatched=true;
		// Build the data-model

        this.render();
        
        String postid=UidGenerator.getUUId();
        //posted.set("content", content);
        
        Map<String,List> map = (Map<String, List>) _request.getAttribute("objList");

        Map root = new HashMap();
        root.put("message", "Hello World!");

        root.put("www_url", SystemKit.getParamById("system_core", "www_url"));
        root.put("objList", map);
        root.put("tags",tags);
        root.put("subject", subject);
        root.put("email",email);
        root.put("jobid",postid);
        root.put("greeting", greeting);
        root.put("ending", ending);
       // this.renderHtml();
        
        // Get the templat object
        Template t = cfg.getTemplate("news/render.ftl","UTF-8");
        
        // Prepare the HTTP response:
        // - Use the charset of template for the output
        // - Use text/html MIME-type
//        _response.setContentType("text/html; charset=" + t.getEncoding());
//        Writer out = _response.getWriter();
        String s =FreeMarkerTemplateUtils.processTemplateIntoString(t, root);
        
        String newsids= PostJob.getAllNewsId(map);
        BizObject posted = new BizObject("posted");
        posted.set("newsids", newsids);
        posted.setID(postid);
        //\result=result + emailaddress +"  发送结果：  "+success+" , ";
        posted.set("content", s);
        posted.set("memo", email);
        posted.set("lastposttime", new Date());
        String lang=this.getParameter("lang");
        posted.set("lang", lang);
      //  posted.set("posttime", new Date());
        posted.set("no","manual");
        posted.set("limits", 0);
        posted.set("name", "手动发邮件");
        this.getJdo().add(posted);

        
        return s;
       // String s;
      //  s.to
        // Merge the data-model and the template
//        try {
//            t.process(root, out);
//        } catch (TemplateException e) {
//            throw new ErrorException(
//                    "Error while processing FreeMarker template"+e.getMessage());
//        }

		//return _SUBMIT_TYPE;
	}

	/**
	 * 最新的post的采集入库
	 * @return
	 */
	@Ajax
	public String post() {
		BizObject news = this.getBizObjectFromMap("news");
		
		if(StringUtils.isBlank(news.getString("title")) ){
			BizObject err_web_site = new BizObject("err_web_site");
			try {
				err_web_site.set("reson", "标题为空");
				err_web_site.set("status", BasicContext.STATUS_DISPOSE_NO);
				err_web_site.set("copyfrom",news.getString("web_site"));
				err_web_site.set("copyfromurl",news.getString("copyfromurl"));
				err_web_site.set("catalog",news.getString("catalog"));
				
				this.getJdo().add(err_web_site);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			log("标题为空,不入库:"+news);
			return "ok";
		}else if (StringUtils.isBlank(news.getString("content"))){
			BizObject err_web_site = new BizObject("err_web_site");
			try {
				err_web_site.set("reson", "内容为空");
				err_web_site.set("status", BasicContext.STATUS_DISPOSE_NO);
				err_web_site.set("copyfrom",news.getString("web_site"));
				err_web_site.set("copyfromurl",news.getString("copyfromurl"));
				err_web_site.set("catalog",news.getString("catalog"));
				
				this.getJdo().add(err_web_site);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			log("内容为空,不入库:"+news);
			return "ok";
		}
		
		try{
			if(StringUtils.isBlank(news.getString("posttime")))
				news.set("posttime", new Date());
			news.getDate("posttime");
		}catch(Exception e){
			e.printStackTrace();
			log(e.getMessage());
			news.set("posttime", new Date());
		}
		//log("insert news "+news);
		
		news.set("copyfrom", news.getString("web_site"));
		news.set("web_summary", news.getString("summary"));
		news.set("summary", "");
		
		try {
		//	Thread.sleep(200);
			this.checkParam(news);
			this.getNewsService().addNews(news);
		} catch (SQLException e){
			log("",e);
			log("错误记录:"+news);
			e.printStackTrace();
		}
		return "ok";
		
	}
	
	public void reset() throws SQLException{
		while(true){
			String sql = "select * from basic.his_news where status='2' and createdate>=STR_TO_DATE('2013-08-23 22:00:00','%Y-%m-%d %H:%i:%s') " +
					"and createdate<=STR_TO_DATE('2013-08-26 10:00:00','%Y-%m-%d %H:%i:%s') limit 0,100";
			List<BizObject> list = QueryFactory.executeQuerySQL(sql);
			if(list==null || list.size()<=0) break;
			for(BizObject biz : list){
				biz.set("status", 0);
				this.getJdo().update(biz);
				
				BizObject news = biz.duplicate();
				news.resetObjType("news");
				news.set("status", BasicContext.STATUS_DISPOSE_NO);
				news.set("issue", BasicContext.ISSUE_NO);
				news.set("hits", 0);
				news.set("isrecommend", BasicContext.IS_RECOMMEND_NO);
				news.set("istop", BasicContext.ISTOP_NO);
				news.set("isautotag", BasicContext.ISAUTOTAG_NO);
				news.set("his_news_id", news.getId());
				this.getJdo().addOrUpdate(news);
			}
		}
	}

	public static void main(String[] args) throws ParseException {
		String str = "[Properties/地产]<br>Guangzhou is seeking public opinion for a new land regulation which requires land buyers " +
				"to pay 20% of contractual prices if they keep the plot idle for over a year.    " +
				"<a href=\"http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=0fae5a728acd413389ec69b95cdff8aa\" " +
				"class=\"more\" target=\"_blank\">more</a><br><br><br>[700 HK (Tencent/腾讯)]<br>Ma Zhengqi, the vice chief " +
				"of the State Administration of Industry of Commerce and former vice mayor of Chongqing, was accused of malfeasance by " +
				"a journalist of Guangzhou-based newspaper New Express Daily.    " +
				"<a href=\"http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=080b18d83ac74158a8fa4ed065f60efd\" " +
				"class=\"more\" target=\"_blank\">more</a><br><br>";
		String ss = "http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=0fae5a728acd413389ec69b95cdff8aa";
		String ssend = "http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=";

		String ss_last = "http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=080b18d83ac74158a8fa4ed065f60efd";
		
		System.out.println("start:0fae5a728acd413389ec69b95cdff8aa; find : 080b18d83ac74158a8fa4ed065f60efd");
		int start = str.indexOf(ss);
		int end = str.indexOf(ssend, start+ss.length());
		System.out.println(str.length()+":"+start+":"+end);
		System.out.println(str.substring(end+ssend.length(), end+ssend.length()+32));
		
		str = str.substring(0,str.indexOf(ss_last));
		int index = str.lastIndexOf(ssend);
		System.out.println(str.substring(index+ssend.length(), index+ssend.length()+32));
		
	}
}
