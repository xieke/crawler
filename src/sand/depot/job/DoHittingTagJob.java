package sand.depot.job;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.system.ActionHandler;
import sand.service.news.NewsService;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class DoHittingTagJob extends BaseJob {
	Logger logger = Logger.getLogger(DoHittingTagJob.class);
	
	@Override
	public String run() throws Exception {
		this._task.set("nexturl", "");
		NewsService newsService = (NewsService) WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("newsService");
		BizObject doHitting = QueryFactory.getInstance("re_general").getByID("doHittingTag");

		newsService.doHittingTag(doHitting.getString("value"), doHitting.getString("aid"), doHitting.getString("bid"));
		logger.info("成功啦~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		return BaseJob.OK;
	}

}
