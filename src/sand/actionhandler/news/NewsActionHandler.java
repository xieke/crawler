package sand.actionhandler.news;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.depot.job.QuartzManager;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.SystemKit;
import sand.mail.MailServer;
import sand.service.basic.service.TagService;
import sand.service.news.NewsService;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import tool.util.CachedResponseWrapper;
import basic.BasicContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

//private Configuration cfg; 

@AccessControl("no")
public class NewsActionHandler extends ActionHandler {
	
	private TagService tagService;
	private NewsService newsService;

	public NewsActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "news";
		this._moduleId = "news";
	}
	
	public void render() throws SQLException {
		log("begin render ...............................");
		String[] outids = this.getParameters("outids");
		List<BizObject> objList = queryList();
		
		List<BizObject> v= new ArrayList();
		if(outids==null) throw new ErrorException("没有文章");
		for(String id:outids){
			BizObject b = new BizObject("news");
			b.setID(id);
			b.refresh();
			v.add(b);
		}

		
		
		
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		String[] str = this.getParameter("tag_ids2").split(",");
		String tagstr="";
		
		
		
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			
			
			for(BizObject biz : list){
				if(tagList.contains("'"+biz.getId()+"'")) {
					
					biz.set("checked", "checked");
					String tagname=biz.getString("name");
					tagstr=tagstr+","+tagname;
					

				}
			}
			
		}
		Map<String,List> newtags = new HashMap();
		
		if(tagstr.equals("")) tagstr=this.getParameter("tags");
		
		//log("ok  ,  tags is "+tagstr);
		
		if(tagstr.equals("")) throw new ErrorException("您没有选择 tag");
		String tag[] = tagstr.split(",");
		for(String s:tag){
			if(s.equals("")) continue;
			
			List<BizObject> onetags = new ArrayList();
			
			for(int i=0 ; i<v.size();i++){
				BizObject b=v.get(i);
				//log("tags is "+b.getString("tags"));
				if(b.getString("tags").indexOf(s)>=0){
					b.set("tag", s);
					onetags.add(b);
					v.remove(b);
					i--;
				}
			}	
			log("put "+s+"  "+onetags.size());
			newtags.put(s, onetags);
		}
		this._request.setAttribute("objList",newtags);
		
	//	this._request.setAttribute("taglist", tagList);
		this.setParam();
		//this.log("tagstr is "+tagstr);
		this._request.setAttribute("tags", tagstr);
		
		this._nextUrl = "/template/news/render.jsp";
	}
	

	@CandoCheck("session")
	public void listMail() throws SQLException{
		log("list mail tag "+this.getParameter("tags2"));
		List<BizObject> objList = queryList();
		String[] str = this.getParameter("tag_ids2").split(",");

		this._request.setAttribute("objList", objList);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
	//	String tagstr="";
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			
			for(BizObject biz : list){
				if(tagList.contains("'"+biz.getId()+"'")){
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
	@CandoCheck("session")
	public void list() throws SQLException{
		List<BizObject> objList = queryList();
		String[] str = this.getParameter("tag_ids2").split(",");

		this._request.setAttribute("objList", objList);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			for(BizObject biz : list){
				if(tagList.contains("'"+biz.getId()+"'")) biz.set("checked", "checked");
			}
		}
		
		this.setParam();
		this._request.setAttribute("tags", list);
		this._nextUrl = "/template/news/list.jsp";
	}
	@CandoCheck("session")
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
	
	@CandoCheck("session")
	public void deletes() throws SQLException{
		this.getJdo().beginTrans();
		String[] ids = this.getParameters("delid");
		for(String s : ids) this.delete(s);
		this.getJdo().commit();
		this.list();
	}
	
	@CandoCheck("session")
    public void listHistory() throws SQLException{
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
	private List<BizObject> queryList() throws SQLException{
		String job = this.getParameter("job");
		String startDate = this.getParameter("startDate");
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
		String issue = this.getParameter("issue");
		
		if(StringUtils.isNotBlank(job) && job.equals("default")){
			//如果是从最外层点进去的列表链接,则是查询默认近两天的日期的记录
			endDate = DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -2);
			startDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
		}
		
		StringBuilder sql = new StringBuilder("");
		if(StringUtils.isBlank(tag_ids2)) sql.append("select DISTINCT n.id,n.title,n.posttime,n.fname,n.author,n.copyfrom,n.copyfromurl," +
				"n.category_id,n.status,n.issue,n.hits,n.isrecommend,n.istop,n.isautotag,n.summary,n.c_summary,n.importance," +
				"n.urgent,n.sort,n.his_news_id,n.createdate from basic.news n where 1=1 ");
		else sql.append("select DISTINCT n.id,n.title,n.posttime,n.fname,n.author,n.copyfrom,n.copyfromurl," +
				"n.category_id,n.status,n.issue,n.hits,n.isrecommend,n.istop,n.isautotag,n.summary,n.c_summary,n.importance," +
				"n.urgent,n.sort,n.his_news_id,n.createdate from basic.news n left join basic.re_bill_tag r on n.id=r.bill_id where r.tag_id in (")
				.append(tag_ids2).append(")");
		
		//STR_TO_DATE('2013-05-27','%Y-%m-%d')>=posttime
		
		if(StringUtils.isNotBlank(startDate)) sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d %H:%i:%s')");
		if(StringUtils.isNotBlank(endDate)) sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate).append("','%Y-%m-%d %H:%i:%s')");
		if(StringUtils.isNotBlank(status)) sql.append(" and n.status='").append(status).append("'");
		if(StringUtils.isNotBlank(title)) sql.append(" and n.title like '%").append(title).append("%'");
		if(StringUtils.isNotBlank(urgent)) sql.append(" and n.urgent='").append(urgent).append("'");
		if(StringUtils.isNotBlank(author)) sql.append(" and n.author like '%").append(author).append("%'");
		if(StringUtils.isNotBlank(importance)) sql.append(" and n.importance='").append(importance).append("'");
		if(StringUtils.isNotBlank(sort)) sql.append(" and n.sort='").append(sort).append("'");
		if(StringUtils.isNotBlank(summary)) sql.append(" and n.c_summary is not null "); //输出html的时候，没有summary无意义
		if(StringUtils.isNotBlank(issue)) sql.append(" and n.issue ='").append(issue).append("'"); //输出html的时候，没有summary无意义
		
		if(StringUtils.isNotBlank(orderby)) sql.append(" order by ").append(orderby);
		else sql.append(" order by n.posttime");
		if(StringUtils.isNotBlank(order)) sql.append(" ").append(order);
		else sql.append(" desc");

		System.out.println(sql.toString());
		//PageVariable pv 
		List<BizObject> objList = QueryFactory.executeQuerySQL(sql.toString(),this.preparePageVar());
		
		return objList;
	}
	
	private void setParam() throws SQLException{
		String job = this.getParameter("job");
		String startDate = this.getParameter("startDate");
		String endDate = this.getParameter("endDate");
		String status = this.getParameter("status");
		String title = this.getParameter("title");
		String urgent = this.getParameter("urgent");
		String author = this.getParameter("author");
		String importance = this.getParameter("importance");
		String tags2 = this.getParameter("tags2");
		String sort = this.getParameter("sort");
		String tag_ids2 = this.getParameter("tag_ids2");
		String orderby = this.getParameter("orderby");
		String order = this.getParameter("order");
		
		if(StringUtils.isBlank(orderby)) orderby="posttime";
		if(StringUtils.isBlank(order)) order="desc";
		
		if(StringUtils.isNotBlank(job) && job.equals("default")){
			//如果是从最外层点进去的列表链接,则是查询默认近两天的日期的记录
			endDate = DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -2);
			startDate = DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDDHHMMSS);
		}
		
		this._request.setAttribute("startDate", startDate);
		this._request.setAttribute("endDate", endDate);
		this._request.setAttribute("status", status);
		this._request.setAttribute("title", title);
		this._request.setAttribute("urgent", urgent);
		this._request.setAttribute("author", author);
		this._request.setAttribute("importance", importance);
		this._request.setAttribute("tags2", tags2);
		this._request.setAttribute("sort", sort);
		this._request.setAttribute("tag_ids2", tag_ids2);
		this._request.setAttribute("orderby", orderby);
		this._request.setAttribute("order", order);
	}
	
	public void list2(int i) throws SQLException{
		String[] str = null;
		if(i==0){//==0表示是需要标签作为查询条件的,==1是不需要标签条件的,因为是保存等方法进行的调用
			String tag_ids = this.getParameter("tag_ids");//这个参数的值为:   'ssss','dddd','dddd','ddd'
			str = tag_ids.split(",");
			if(StringUtils.isNotBlank(tag_ids))
				this.setHardcoreFilter(" id in (select bill_id from basic.re_bill_tag where tag_id in ("+tag_ids+"))");
		}
		this.setOrderBy("createdate desc");
		this.listObj("news");
		
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		
		if(str!=null && str.length>0){
			List<String> tags = new ArrayList<String>();
			for(String s : str){
				tags.add(s);
			}
			for(BizObject biz : list){
				if(tags.contains("'"+biz.getId()+"'"))
					biz.set("checked", "checked");
			}
		}
		this._request.setAttribute("tags", list);
		this._nextUrl = "/template/news/list.jsp";
	}
	
	public void showIt() throws SQLException{
		QueryFactory qf = new QueryFactory("news");
		BizObject biz = qf.getByID(this._objId);
		
		biz.set("hits",biz.getInt("hits",0)+1);
		String mail = this.getParameter("mail");
		if(!mail.equals("")){
			biz.set("clickmails", biz.getString("clickmails")+","+mail);
		}
		this.getJdo().update(biz);
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
		this._nextUrl = "/template/news/show.jsp";
	}
	
	@CandoCheck("session")
	public void show() throws SQLException{
		QueryFactory qf = new QueryFactory("news");
		BizObject biz = qf.getByID(this._objId);
		
		List<BizObject> list = this.getTagService().openAllWithSelectedTag(this._objId);
//		List<BizObject> retags = this.getTagService().queryTagsByBillId(this._objId);
		
		biz.set("content", biz.getString("content").replaceAll("'","&#039;"));
		biz.set("content", biz.getString("content").replaceAll("&","&amp;"));
		biz.set("content", biz.getString("content").replaceAll("\"","&quot;"));
		biz.set("content", biz.getString("content").replaceAll("<","&lt;"));
		biz.set("content", biz.getString("content").replaceAll(">","&gt;"));
		
		this._request.setAttribute("obj", biz);
//		this._request.setAttribute("retags", retags);
		this._request.setAttribute("objList", list);
		this._request.setAttribute("commentsList", this.getComments(this._objId));
		this.setParam();
		this._nextUrl = "/template/news/edit.jsp";
	}
	
	/**
	 * 显示添加页面
	 * 
	 * @throws SQLException
	 */
	@CandoCheck("session")
	public void showAdd() throws SQLException{
		List<BizObject> list = this.getTagService().openAllWithSelectedTag(this._objId);
		this._request.setAttribute("objList", list);
		this._nextUrl = "/template/news/edit.jsp";
	}
	
	@CandoCheck("session")
	public void showHis() throws SQLException{
		QueryFactory qf = new QueryFactory("his_news");
		BizObject biz = qf.getByID(this.getParameter("objId"));
		this._request.setAttribute("obj", biz);
		this._nextUrl = "/template/news/view_his.jsp";
	}
	@CandoCheck("session")
	public void save() throws SQLException{
		this.getJdo().beginTrans();
		String tagId = this.getParameter("tag_ids");
		BizObject news = this.getBizObjectFromMap("news");
//		news.set("status", BasicContext.STATUS_DISPOSE_YES);
		this.log("tags is "+this.getParameter("tags"));
		news.set("tags", this.getParameter("tags"));
		news.set("tag_ids", tagId);
		if(StringUtils.isBlank(news.getId())){
			news.set("posttime", new Date());
			news.set("issue", BasicContext.ISSUE_NO);
			news.set("hits", 0);
			news.set("isrecommend", BasicContext.IS_RECOMMEND_NO);
			news.set("istop", BasicContext.ISTOP_NO);
			news.set("isautotag", BasicContext.ISAUTOTAG_NO);
		}
		this.getJdo().addOrUpdate(news);
		
		//先删除已标置的标签
		if(StringUtils.isNotBlank(news.getId()))
			this.getTagService().deleteReBillTagsByBillId(news.getId());		
		
		this.log("tagIds is "+tagId);
		String[] tagIds = tagId.split(",");
		this.getTagService().addReBillTags(tagIds, news.getId());
		
//		List<String> tagNames = this.getTagService().getTagNames(tagIds);
//		
//		String tags="";
//		for(String s:tagNames){
//			if(tags.equals(""))
//				tags=s;
//			else
//				tags=tags+","+s;
//
//		}
		
//		this.log("tags is "+tags);
////		if(!tags.equals(""))
//		news.set("tags", this.getParameter("tags"));
//		news.set("tag_ids", tagId);
//		this.getJdo().addOrUpdate(news);
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
		
		this.getJdo().commit();
		this.clearQueryParam();
		
//		QueryFactory qf = new QueryFactory("news");
//		qf.setHardcoreFilter("status='"+ BasicContext.STATUS_DISPOSE_NO+"' and " +
//				"DATE_FORMAT(createdate,'%Y-%m-%d HH:mm:ss')>='"+DateUtils.formatDate(news.getDate("createdate"), DateUtils.PATTERN_YYYYMMDDHHMMSS)+"' ");
//		qf.setOrderBy("createdate desc");
//		List<BizObject> list = qf.query(new PageVariable(1));
//		if(list.size()>0){
//			this._objId = list.get(0).getId();
//			this.show();
//		}else
//			this._nextUrl = "/news.NewsActionHandler.list";
		this.list();
	}
	
	@CandoCheck("session")
	public void addComments() throws SQLException{
		this.getJdo().beginTrans();
		BizObject biz = this.getBizObjectFromMap("NEWCOMMENTS");
		if(StringUtils.isNotBlank(biz.getString("content"))){
			biz.set("aid", this.getParameter("objId"));
			biz.set("posttime", new Date());
			biz.set("userid", this._curuser.getId());
			// 添加一条备忘记录
			this.add(biz);
			this.getJdo().commit();
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
	@CandoCheck("session")
	public void delComments() throws SQLException{
		BizObject biz = new BizObject("NEWCOMMENTS");
		biz.setID(this.getParameter("comId"));
		this.getJdo().beginTrans();
		this.delete(biz);
		this.getJdo().commit();
		this.show();
	}

	public void gData() throws SQLException{
		List<BizObject> list = this.getNewsService().copyfromNews();
		this.getJdo().beginTrans();
		if(list.size()>0){
			this.getNewsService().addNews(list);
			this.getNewsService().deleteCopyforms();
		}
		this.getJdo().commit();
	}
	
	@CandoCheck("session")
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
	
	@Ajax
	public String like() throws SQLException{
		
		return click("likecount");
		
	}
	
	@Ajax
	public String dislike() throws SQLException{

		return click("dislikecount");
		
	
	}
	

	public String click(String likeordislike) throws SQLException{

		
		String mail=this.getParameter("mail");
		String newsid = this.getParameter("newsid");
		BizObject b = new BizObject("news");
		b.setID(newsid);
		b.refresh();
		//logger.
		if(b.getString("clickmails").indexOf(mail)>=0)
			return "您已经点过了";
		b.set("clickmails", b.getString("clickmails")+","+mail+"|"+likeordislike.substring(0,1));
		
		b.set(likeordislike, b.getInt(likeordislike,0)+1);
		b.set("content", null);
		this.getJdo().update(b);
		return "投票成功";
		
	
	}
	
	
	public void preview(){
		
	}
	/**
	 * 发送邮件
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 * @throws TemplateException
	 */
	public void sendMail() throws IOException, ServletException, SQLException, TemplateException{
		
		BizObject email = new BizObject("email");

		String address=this.getParameter("email");
		email.set("toaddr", address);
		
		int i=address.indexOf("@");
		if(i<=0)
			throw new ControllableException("错误的邮件地址");
		
		//String sfix = address.substring(i+1);
		//logger.info("sfix is "+sfix);
		// email.set("fromaddr", "xieke3@hnair.com");

		String content=this.renderHtml2(this.getParameter("tags"),this.getParameter("subject"),this.getParameter("greeting"),this.getParameter("ending"));
		email.set(
				"content",content);
		email.set("subject", "gpcore 友情赠送！");

		boolean result = false;
	//	if(mailExcludes.contains(sfix)){
			result=MailServer.sendMailSyn(email);
			
			

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
	public  void render2() throws IOException, ServletException, SQLException, TemplateException{
	   
		log("ok  ,  tags is "+this.getParameter("tags"));
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
	private  String renderHtml2(String tags,String subject,String greeting,String ending) throws IOException, ServletException, SQLException, TemplateException{
		 Configuration cfg; 
        cfg = new Configuration();
        // - Templates are stoted in the WEB-INF/templates directory of the Web app.
        cfg.setServletContextForTemplateLoading(this._context  , "WEB-INF/templates");
        
       // this._dispatched=true;
		// Build the data-model
        Map root = new HashMap();
        root.put("message", "Hello World!");

        this.render();
        root.put("www_url", "http://192.168.36.33:18080");
        root.put("objList", _request.getAttribute("objList"));
        root.put("tags",tags);
        root.put("subject", subject);
        root.put("greeting", greeting);
        root.put("ending", ending);
       // this.renderHtml();
        
        // Get the templat object
        Template t = cfg.getTemplate("news/render.ftl");
        
        // Prepare the HTTP response:
        // - Use the charset of template for the output
        // - Use text/html MIME-type
//        _response.setContentType("text/html; charset=" + t.getEncoding());
//        Writer out = _response.getWriter();
        String s =FreeMarkerTemplateUtils.processTemplateIntoString(t, root);
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

	@Ajax
	public String post() throws SQLException{
		BizObject news = this.getBizObjectFromMap("news");
		log("insert news "+news);
		//this.getJdo().add(news);
		return "ok";
		
	}

	public static void main(String[] args) {
		String sql = "aabbccdd";
		System.out.println(sql.indexOf("bec"));
	}
}
