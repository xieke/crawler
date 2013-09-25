package sand.depot.job;

import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.system.ActionHandler;
import sand.service.news.NewsService;

public class MoveNews extends BaseJob {
	private NewsService newsService;

	@Override
	public String run() throws Exception {
		int i=this.getNewsService().moveNews();
		return super.OK+"，共移库 "+i+"条";
	}
	
	public NewsService getNewsService() {
		if(newsService == null) 
			newsService = (NewsService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("newsService");
		return newsService;
	}
}
