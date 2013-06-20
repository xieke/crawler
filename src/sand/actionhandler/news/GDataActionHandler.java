package sand.actionhandler.news;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.basic.MailAH;
import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.service.basic.service.TagService;
import sand.service.news.NewsService;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

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
	
	@Ajax
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
					// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
			RUNNING=false;
			logger.info("采集失败:   ---   "+i+" "+e1.getMessage());
			return "采集失败:"+e1.getMessage();
		}
		finally{
			
			RUNNING=false;
			return "采集成功了吗?";
		}
		
		
		
		
		//this._request.setAttribute("msg_type", "SUCCESS");
		//this._nextUrl = super._msgUrl;
		//RUNNING=false;
		
		
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
