package sand.actionhandler.news;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.service.basic.service.TagService;
import sand.service.news.NewsService;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import basic.BasicContext;

@AccessControl("no")
public class GDataActionHandler extends ActionHandler {
	static Logger logger = Logger.getLogger(GDataActionHandler.class);
	private TagService tagService;
	private NewsService newsService;

	public GDataActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "news";
		this._moduleId = "news";
	}
	private static boolean RUNNING=false;
	private static int i=0;
	
	/**
	 * 已经不用了
	 * @return
	 */
	@SuppressWarnings("finally")
	@Ajax
	@CandoCheck("session")
	public String gData() {
		if(RUNNING==true){
			logger.info("上次采集还未完成   "+i);
			return "上次采集还未完成 -- "+i;
		} 
		else RUNNING=true;
		
		//删除不满足要求的数据
		try {
			QueryFactory.executeUpdateSQL("delete from ben.origin_news where title is null or content is null or title ='' or content =''");
			//更新posttime为0000记录为当前时间
			String sql = "update ben.origin_news set posttime=SYSDATE() where posttime='0000-00-00 00:00:00'";
			QueryFactory.executeUpdateSQL(sql);
			
			List<BizObject> objList = null;
			List<BizObject> list = null;
			i=0;
			while(true){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				objList = QueryFactory.executeQuerySQL("select count(*) cnt from ben.origin_news");
				if(objList==null || objList.get(0).getInt("cnt", 0)<=0)
					break;
				
				list = this.getNewsService().copyfromNews();
//				this.getJdo().beginTrans();
				if(list.size()>0){
					this.getNewsService().addNews(list);
					i=i+list.size();
		//			this.getNewsService().deleteCopyforms();
				}
//				this.getJdo().commit();
				
			}
			logger.info("采集成功   ---   "+i);
			return  "采集成功!  --- "+i;

		} catch (SQLException e1) {
			e1.printStackTrace();
			RUNNING=false;
			logger.info("采集失败:   ---   "+i+" "+e1.getMessage());
			return "采集失败:"+e1.getMessage();
		}finally{
			RUNNING=false;
			return "采集成功了吗?";
		}
		
		
		
		
		//this._request.setAttribute("msg_type", "SUCCESS");
		//this._nextUrl = super._msgUrl;
		//RUNNING=false;
		
		
	}
	
	/**
	 * 去重的方法
	 * 
	 * @return
	 */
	@Ajax
	public String nlpirProcess() {
//		logger.info("-------------------------");
//		logger.info("要处理的文章ids:"+this.getParameter("dels"));
		
		try{
			String dels = this.getParameter("dels");
			if(StringUtils.isBlank(dels)){
				logger.info("要处理的文章ids为空,直接返回");
				return "null";
			}
			String[] del_ids = dels.split(",");
			logger.info("要处理的文章ids:"+this.getParameter("dels")+",要处理的文章ids的大小为:"+del_ids.length);
			int i=0;
			int j=0;
			
//			QueryFactory qf = new QueryFactory("news");
			
			if(del_ids.length<=0) return "no";
			for(String id : del_ids){
				i++;
//				logger.info("开始处理第"+i+"个:"+id);
				BizObject news = new BizObject("news");
				news.setID(id);
				news.refresh();
				if(StringUtils.isNotBlank(news.getString("summary"))||StringUtils.isNotBlank(news.getString("c_summary"))){
					logger.info("第"+i+"个:"+id+"文章编辑过,不能删除");
					continue;
				}
//				news = qf.getOne(news);
				if(StringUtils.isBlank(id) || news.isEmpty() ) {
					logger.info("第"+i+"个:"+id+"  对应的文章不存在,或者已处理过,继续下一个");
				}else{
					j++;
					if(StringUtils.isNotBlank(news.getString("his_news_id"))){
						BizObject his = new BizObject("his_news");
						his.setID(news.getString("his_news_id"));
						his.set("status", BasicContext.STATUS_DISPOSE_DELETE);
						this.getJdo().update(his);
					}
					
					//删除新闻
					this.getJdo().delete(news);
//					logger.info("第"+i+"个:"+id+"  对应的文章处理完成");
				}
			}
			logger.info("本次处理请求共处理"+j+"个,"+(i-j)+"篇文章不存在或者已删除,总数是:"+i);
			return "ok";
		}catch(SQLException s){
			s.printStackTrace();
			logger.info(s.getMessage());
			return "no";
		}
	}
	
	/**
	 * 自动打标签
	 * 
	 * @throws SQLException
	 */
	@Ajax
	public String hitTagging() {
		String id = this.getParameter("id");
		String tag_rules = this.getParameter("tag_rules");
		logger.info("自动打标签id is :"+id);
		logger.info("自动打标签tags is :"+tag_rules);
		if(StringUtils.isBlank(id)) return "id is null";
		if(StringUtils.isBlank(tag_rules)) return "tag_rules is null";
		try{
			BizObject biz = new BizObject("news");
			biz.setID(id);
			biz.refresh();
			if(biz==null) return "id error";
			String[] tag_ids = tag_rules.split(",");
			String str ="";
			for(int i=0;i<tag_ids.length;i++) {
				if(i>0) str+=",";
				str+="'"+tag_ids[i]+"'";
			}
			
			String sql = "select GROUP_CONCAT(tag_id) tag_ids,GROUP_CONCAT(tag_name) tag_names from basic.tag_rule where id in ("+str+")";
			List<BizObject> list = QueryFactory.executeQuerySQL(sql);
			if(list==null || list.size()<=0) return "tags error";
			biz.set("tags", ","+list.get(0).getString("tag_names")+",");
			biz.set("tag_ids", ","+list.get(0).getString("tag_ids")+",");
			
			this.getJdo().update(biz);
			logger.info("标签的名字:"+biz.toString());
		}catch(SQLException s){
			s.printStackTrace();
			logger.info(s.getMessage());
			return "no";
		}
		return "ok";
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
	
}
