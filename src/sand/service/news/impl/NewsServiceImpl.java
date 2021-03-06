package sand.service.news.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import sand.actionhandler.system.ActionHandler;
import sand.depot.job.BaseJob;
import sand.service.basic.service.TagRuleService;
import sand.service.basic.service.TagService;
import sand.service.news.NewsService;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import basic.BasicContext;

public class NewsServiceImpl implements NewsService {

	@Resource(name="tagRuleService")
	TagRuleService tagRuleService;
	
	@Resource(name="tagService")
	TagService tagService;
	
	public List<BizObject> copyfromNews() throws SQLException{
	
		String sql = "select * from ben.origin_news limit 0,500";
		List<BizObject> list = QueryFactory.executeQuerySQL(sql);
		return list;
	}
	
	public List<String> getTagIdsByNewsId(String news_id) throws SQLException{
		List<String> tag_id_lists = new ArrayList<String>();
		BizObject news = QueryFactory.getInstance(NewsService.TABLE_NAME).getByID(news_id);
		if(news==null||StringUtils.isBlank(news.getString("tag_ids"))) return tag_id_lists;
		String[] tag_ids = news.getString("tag_ids").split(",");
		for(String s : tag_ids) tag_id_lists.add(s);
		return tag_id_lists;
	}


	/**
	 * 自动打标签
	 * 
	 * @throws SQLException
	 */
//	private void hitTagging() throws SQLException{
//		List<BizObject> tagRuleList = tagRuleService.queryTagRules();
//		
//		List<BizObject> originNewsList = QueryFactory.executeQuerySQL("select * from ben.origin_news where status='0' or status is null ");
//		
//		for(BizObject originNews : originNewsList){
//			for(BizObject tagRule : tagRuleList){
//					if(originNews.getString("title").indexOf(tagRule.getString("keyword"))!=-1 
//							|| originNews.getString("contents").indexOf(tagRule.getString("keyword"))!=-1
//							|| originNews.getString("copyfromurl").indexOf(tagRule.getString("keyword"))!=-1)
//						originNews.set("tags", originNews.getString("tags")+","+tagRule.getString("tag_id"));
//				
//			}
//			QueryFactory.executeUpdateSQL
//			("update ben.origin_news set tags='"+originNews.getString("tags")+"',status='1' where id='"+originNews.getId()+"'");
//		}
//	}
	
	@Override
	public void addNews(List<BizObject> copyfromNews) throws SQLException {
		BizObject news = null;
		BizObject his_news = null;
		
		for(BizObject biz : copyfromNews){
			if(StringUtils.isNotBlank(biz.getString("title").trim()) && StringUtils.isNotBlank(biz.getString("content").trim())){
				//当标签和内容其中一项为空时，则不采集入库
				//添	加到历史新闻表中,以便后面查询原文所用
				his_news = biz.duplicate();
				his_news.resetObjType(NewsService.HIS_TABLE_NAME);
				his_news.setID("");
				ActionHandler.currentSession().add(his_news);
				
				//添	加到新闻表中
				news = biz.duplicate();
				news.resetObjType(NewsService.TABLE_NAME);
				news.setID("");
				news.set("status", BasicContext.STATUS_DISPOSE_NO);
				news.set("issue", BasicContext.ISSUE_NO);
				news.set("hits", 0);
				news.set("isrecommend", BasicContext.IS_RECOMMEND_NO);
				news.set("istop", BasicContext.ISTOP_NO);
				news.set("isautotag", BasicContext.ISAUTOTAG_NO);
				news.set("his_news_id", his_news.getId());
				news.set("updatedate", new Date());
				ActionHandler.currentSession().add(news);
				//自动打标签
				this.hitTagging(news,news.getString("content"));
			}
			QueryFactory.executeUpdateSQL("delete from ben.origin_news where  id="+biz.getId());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void addNews(BizObject news) throws SQLException {
		
		//添	加到历史新闻表中,以便后面查询原文所用
		 BizObject his_news = news.duplicate();
		his_news.resetObjType(NewsService.HIS_TABLE_NAME);
//		his_news.setID("");
		BaseJob.currentSession().add(his_news);
		/**暂时先不用news_content表,先不移出去
		BizObject news_content = new BizObject(NewsService.NEWS_CONTENT);
		news_content.set("content", news.getString("content"));
		**/
		//添	加到新闻表中
		news.setID(his_news.getId());
		news.set("status", BasicContext.STATUS_DISPOSE_NO);
		news.set("issue", BasicContext.ISSUE_NO);
		news.set("hits", 0);
		news.set("isrecommend", BasicContext.IS_RECOMMEND_NO);
		news.set("istop", BasicContext.ISTOP_NO);
		news.set("isautotag", BasicContext.ISAUTOTAG_NO);
		news.set("his_news_id", his_news.getId());
		news.set("updatedate", new Date());
		/**暂时先不用news_content表,先不移出去
//		news.set("content", "");
		**/

		BaseJob.currentSession().add(news);
		/**暂时先不用news_content表,先不移出去
		news_content.set("news_id", news.getId());
		ActionHandler.currentSession().add(news_content);
		
		//自动打标签
		this.hitTagging(news,news_content.getString("content"));
		**/
		
		this.hitTagging(news, news.getString("content"));

		
	}
	
	/**
	 * 自动打标签
	 * 
	 * @throws SQLException
	 */
	private void hitTagging(BizObject news,String content) throws SQLException{
		List<BizObject> tagRuleList = tagRuleService.queryTagRules();
		
		for(BizObject tagRule : tagRuleList){
			
			if(this.validateCopyfrom(news, tagRule) && this.validateKeyword(news, tagRule, content)){
				this.updateNewsByTagRule(news, tagRule);
			}
//			if(news.getString("isweibo").equals("1")){
//				String[] uids = tagRule.getString("weibo_uid").split(",");
//				for(String uid : uids){
//					if(uid.equals("")) continue;
//					
//					if(news.getString("author_id").indexOf(uid)>=0){
//						String[] tag_ids = tagRule.getString("tag_id").split(",");
//						for(String s : tag_ids){
//							if(news.getString("tag_ids").indexOf(s)==-1) {
//								news.set("tags", (StringUtils.isBlank(news.getString("tags"))?"":news.getString("tags"))+tagService.getById(s).getString("name")+",");
//								news.set("tag_ids", (StringUtils.isBlank(news.getString("tag_ids"))?"":news.getString("tag_ids"))+s+",");
//							}
//						}
//						BaseJob.currentSession().update(news);
//					}
//				}
//			}
			
			
			
		}
	}
	
	private boolean validateCopyfrom(BizObject news,BizObject tagRule){
		boolean b = false;
		if(StringUtils.isBlank(tagRule.getString("weibo_uid"))
				&&StringUtils.isBlank(tagRule.getString("copyfromurl"))
				&&StringUtils.isBlank(tagRule.getString("task_no"))
				&&StringUtils.isBlank(tagRule.getString("copyfrom_lat")))
			return true;
		if(this.validateWeiboUid(news, tagRule) || this.validateUrl(news, tagRule) || this.validateTaskNo(news, tagRule))
			b = true;
		return b;
	}
	
	/**
	 * 验证weibo_uid是否满足条件(只要有一个满足即可)
	 * 
	 * @param news
	 * @param tagRule
	 * @return
	 */
	private boolean validateWeiboUid(BizObject news,BizObject tagRule){
		boolean b = false;
		String[] uids = tagRule.getString("weibo_uid").split(",");
		for(String uid : uids){
			if(uid.equals("")) continue;
			if(news.getString("author").indexOf(uid)>=0) return true;
		}
		return b;
	}
	
	/**
	 * 验证url是否满足条件(只要有一个满足即可)
	 * 
	 * @param news
	 * @param tagRule
	 * @return
	 */
	private boolean validateUrl(BizObject news,BizObject tagRule){
		boolean b = false;
		String[] uids = tagRule.getString("copyfromurl").split(",");
		for(String uid : uids){
			if(uid.equals("")) continue;
			if(news.getString("copyfromurl").indexOf(uid)>=0) return true;
		}
		return b;
	}
	
	/**
	 * 验证taskno是否满足条件(只要有一个满足即可)
	 * 
	 * @param news
	 * @param tagRule
	 * @return
	 */
	private boolean validateTaskNo(BizObject news,BizObject tagRule){
		boolean b = false;
		String[] uids = tagRule.getString("task_no").split(",");
		for(String uid : uids){
			if(uid.equals("")) continue;
			if(news.getString("task_no").indexOf(uid)>=0) return true;
		}
		return b;
	}
	
	public boolean validateKeyword(BizObject news,BizObject tagRule,String content){
		boolean b = false;//false:打标签;true:不打标签
		if(StringUtils.isBlank(tagRule.getString("keyword"))) return true;
		String[] keywords = tagRule.getString("keyword").split(",");
		//循环关键字
		for(String keyword : keywords){
			//如果当前的关键字不为空
			if(StringUtils.isNotBlank(keyword)){
				//如果标题或者内容包含当前的关键字
				if(news.getString("title").indexOf(keyword)!=-1 || content.indexOf(keyword)!=-1){
					news.set("content", content.replace(keyword, "<span style=\"background-color:#CF3\">"+keyword+"</span>"));
					if(tagRule.getString("conditions").equals("or")){//如果当前的条件是or,也就是只要包含一个关键字就打标签,则直接打上
						return true;//this.updateNewsByTagRule(news, tagRule);
					}else b=true;//否则就设置当前的条件为true
				}else b=false;//否则就设置当前的条件为false
			}
		}
		return b;
//			if(tagRule.getString("conditions").equals("and")){//如果当前的条件是and,则看当前文章是否都包含所有的关键字
//				 if(b) this.updateNewsByTagRule(news, tagRule);
//			}
//		}else {
//			String[] keywords = tagRule.getString("keyword").split(",");
//			for(String keyword : keywords){
//				if(StringUtils.isNotBlank(keyword)){
//					if(news.getString("copyfromurl").indexOf(keyword)!=-1){
//						news.set("content", content.replace(keyword, "<span style=\"background-color:#CF3\">"+keyword+"</span>"));
//						if(tagRule.getString("conditions").equals("or")){//如果当前的条件是or,也就是只要包含一个关键字就打标签,则直接打上
//							this.updateNewsByTagRule(news, tagRule);
//						}else b=true;//否则就设置当前的条件为true
//					}else b=false;//否则就设置当前的条件为false
//				}
//			}
//			if(tagRule.getString("conditions").equals("and")){//如果当前的条件是and,则看当前文章是否都包含所有的关键字
//				 if(b) this.updateNewsByTagRule(news, tagRule);
//			}
//		}
	}
	
	private void updateNewsByTagRule(BizObject news,BizObject tagRule) throws SQLException{
		String[] tag_ids = tagRule.getString("tag_id").split(",");
		for(String s : tag_ids){
			if(news.getString("tag_ids").indexOf(s)==-1) {
				news.set("tags", (StringUtils.isBlank(news.getString("tags"))?"":news.getString("tags"))+tagService.getById(s).getString("name")+",");
				news.set("tag_ids", (StringUtils.isBlank(news.getString("tag_ids"))?"":news.getString("tag_ids"))+s+",");
				news.set("urgent", tagRule.getString("urgent"));
				news.set("importance", tagRule.getString("importance"));
			}
//						tagService.addReBillTag(s, news.getId());
		}
		BaseJob.currentSession().update(news);
	}
	
	/**
	 * 手动标签
	 * 
	 * @param tagRuleIds
	 * @param startDate
	 * @param endDate
	 * @throws SQLException
	 */
	public void doHittingTag(String tagRuleIds,String startDate,String endDate) throws SQLException{
		String[] tag_rule_ids = tagRuleIds.split(",");
		BizObject tag_rule = null;
		QueryFactory qf = new QueryFactory("tag_rule");
		for(String s : tag_rule_ids){
			tag_rule = qf.getByID(s);
			if(tag_rule!=null){
				for(String tag_id : tag_rule.getString("tag_id").split(",")){
					if(StringUtils.isNotBlank(tag_id))
						this.updateTag(tag_id,tag_rule,startDate,endDate);
				}
			}
		}
	}
	
	//暂时先不用news_content表,先不移出去
	public void updateTag(String tag_id,BizObject tag_rule,String startDate,String endDate) throws SQLException{
		StringBuilder update_sql = new StringBuilder("");
		StringBuilder select_sql = new StringBuilder("");
		StringBuilder count_sql = new StringBuilder("");
			
		String[] keywords = tag_rule.getString("keyword").split(",");
		for(String keyword : keywords){
			
			if(StringUtils.isNotBlank(keyword)){
				while(true){
					update_sql = new StringBuilder("");
					select_sql = new StringBuilder("");
					count_sql = new StringBuilder("");
					
					update_sql.append("update basic.news news,");
					select_sql.append(" (select n.id from basic.news n where 1=1 ");
					count_sql.append(" select count(id) cnt from basic.news n where 1=1 ");
					
					if(tag_rule.getString("type").equals(BasicContext.TAGRULE_TYPE_CONTENT)){
						select_sql.append(" and (n.title like '%").append(keyword).append("%' or n.content like '%").append(keyword).append("%')");
						
						count_sql.append(" and (n.title like '%").append(keyword).append("%' or n.content like '%").append(keyword).append("%')");
					}else{
						select_sql.append(" and n.copyfromurl like '%").append(keyword).append("%'");
						count_sql.append(" and n.copyfromurl like '%").append(keyword).append("%'");
					}
					
					if(StringUtils.isNotBlank(startDate)){
						select_sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d')");
						count_sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d')");
					}
					if(StringUtils.isNotBlank(endDate)){
						select_sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate+" 23:59:59").append("','%Y-%m-%d %H:%i:%s')");
						count_sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate+" 23:59:59").append("','%Y-%m-%d %H:%i:%s')");
					}
					select_sql.append(" and (tag_ids not like '%").append(tag_id).append("%' or tag_ids is null) limit 0,100) nc ");
					count_sql.append(" and (tag_ids not like '%").append(tag_id).append("%' or tag_ids is null)");
					
					update_sql.append(select_sql).append("set news.tags=CONCAT(case when news.tags is null || news.tags='' then '' else news.tags end ,'").append(tagService.getById(tag_id).getString("name"))
						.append(",'),news.tag_ids=CONCAT(case when news.tag_ids is null || news.tag_ids='' then ',' else news.tag_ids end ,'").append(tag_id).append(",') where news.id=nc.id");
					 
					System.out.println("统计的sql:"+count_sql.toString());
					System.out.println("修改的sql:"+update_sql.toString());
					QueryFactory.executeUpdateSQL(update_sql.toString());
					List<BizObject> countList = QueryFactory.executeQuerySQL(count_sql.toString());
					if(countList==null || countList.get(0).getInt("cnt", 0)<=0) break;
				}
			}
		}
			
	}
	
	//暂时先不用news_content表,先不移出去,此方法暂时不用
	public void updateTag_no(String tag_id,BizObject tag_rule,String startDate,String endDate) throws SQLException{
		StringBuilder update_sql = new StringBuilder("");
		StringBuilder select_sql = new StringBuilder("");
		StringBuilder count_sql = new StringBuilder("");
			
		while(true){
		
			update_sql.append("update basic.news news,");
			select_sql.append(" (select n.id from basic.news n,basic.news_content c where n.id=c.news_id ");
			count_sql.append(" select count(id) cnt from basic.news n,basic.news_content c where n.id=c.news_id ");
			
			if(tag_rule.getString("type").equals(BasicContext.TAGRULE_TYPE_CONTENT)){
				select_sql.append(" and (n.title like '%").append(tag_rule.getString("keyword")).append("%' or c.content like '%")
					.append(tag_rule.getString("keyword")).append("%')");
				
				count_sql.append(" and (n.title like '%").append(tag_rule.getString("keyword")).append("%' or c.content like '%")
					.append(tag_rule.getString("keyword")).append("%')");
			}else{
				select_sql.append(" and n.copyfromurl like '%").append(tag_rule.getString("keyword")).append("%'");
				count_sql.append(" and n.copyfromurl like '%").append(tag_rule.getString("keyword")).append("%'");
			}
			
			if(StringUtils.isNotBlank(startDate)){
				select_sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d')");
				count_sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d')");
			}
			if(StringUtils.isNotBlank(endDate)){
				select_sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate).append("','%Y-%m-%d')");
				count_sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate).append("','%Y-%m-%d')");
			}
			select_sql.append(" and tag_ids not like '%").append(tag_id).append("%' limit 0,100) nc ");
			count_sql.append(" and tag_ids not like '%").append(tag_id).append("%' ");
			
			update_sql.append(select_sql).append("set news.tags=CONCAT(case when news.tags is null || news.tags='' then ',' else news.tags end ,'").append(tagService.getById(tag_id).getString("name"))
			.append(",'),news.tag_ids=CONCAT(case when news.tag_ids is null || news.tag_ids='' then ',' else news.tag_ids end ,'").append(tag_id).append(",') where news.id=nc.id");
			 
			System.out.println("统计的sql:"+count_sql.toString());
			System.out.println("修改的sql:"+update_sql.toString());
			QueryFactory.executeUpdateSQL(update_sql.toString());
			List<BizObject> countList = QueryFactory.executeQuerySQL(count_sql.toString());
			if(countList==null || countList.get(0).getInt("cnt", 0)<=0) break;
		}
			
	}

	
	/**
	 * 手动标签
	 * 
	 * @param tagRuleIds
	 * @param startDate
	 * @param endDate
	 * @throws SQLException
	 */
	public void doHittingTag2(String tagRuleIds,String startDate,String endDate) throws SQLException{
		String[] tag_rule_ids = tagRuleIds.split(",");
		BizObject tag_rule = null;
		String[] tag_ids = null;
		QueryFactory qf = new QueryFactory("tag_rule");
		StringBuilder sql = new StringBuilder("");
		StringBuilder update_sql = new StringBuilder("");
		StringBuilder update_sql_tagsId = new StringBuilder("");
		for(String s : tag_rule_ids){
			tag_rule = qf.getByID(s);
			tag_ids = tag_rule.getString("tag_id").split(",");
			for(String tag_id : tag_ids){
				sql = new StringBuilder("");
				update_sql = new StringBuilder("");
				update_sql_tagsId = new StringBuilder("");
				
				sql.append("insert into basic.re_bill_tag(bill_id,tag_id,id,createdate,modifydate) select n.id bill_id,'").append(tag_id).append("' tag_id,uuid()")
				   .append(",SYSDATE(),SYSDATE() from basic.news n where ");
				
				update_sql.append("update basic.news n set tags=CONCAT(tags,',").append(tagService.getById(tag_id).getString("name")).append("') where ");
				
				update_sql_tagsId.append("update basic.news n set tag_ids=CONCAT(tag_ids,',").append(tag_id).append("') where ");
				
				if(tag_rule.getString("type").equals(BasicContext.TAGRULE_TYPE_CONTENT)){
					sql.append(" (n.title like '%").append(tag_rule.getString("keyword")).append("%' or n.content like '%")
						.append(tag_rule.getString("keyword")).append("%')");
					
					update_sql.append(" (n.title like '%").append(tag_rule.getString("keyword")).append("%' or n.content like '%")
						.append(tag_rule.getString("keyword")).append("%')");
					
					update_sql_tagsId.append(" (n.title like '%").append(tag_rule.getString("keyword")).append("%' or n.content like '%")
					.append(tag_rule.getString("keyword")).append("%')");
				}else{
					sql.append(" n.copyfromurl like '%").append(tag_rule.getString("keyword")).append("%'");
					
					update_sql.append(" n.copyfromurl like '%").append(tag_rule.getString("keyword")).append("%'");
					
					update_sql_tagsId.append(" n.copyfromurl like '%").append(tag_rule.getString("keyword")).append("%'");
				}
				
				if(StringUtils.isNotBlank(startDate)){
					sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d')");
					update_sql.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d')");
					update_sql_tagsId.append(" and n.posttime>=STR_TO_DATE('").append(startDate).append("','%Y-%m-%d')");
				}
				if(StringUtils.isNotBlank(endDate)){
					sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate).append("','%Y-%m-%d')");
					update_sql.append(" and n.posttime<=STR_TO_DATE('").append(endDate).append("','%Y-%m-%d')");
					update_sql_tagsId.append(" and n.posttime<=STR_TO_DATE('").append(endDate).append("','%Y-%m-%d')");
				}
				
				sql.append(" and n.id not in (select bill_id from basic.re_bill_tag where bill_id=n.id and tag_id='").append(tag_id).append("')");
//				sql.append(" and tag_ids not like '%").append(tag_id).append("%')");
				update_sql.append(" and tags not like '%").append(tagService.getById(tag_id).getString("name")).append("%'");
				update_sql_tagsId.append(" and tag_ids not like '%").append(tag_id).append("%'");
				 
				System.out.println("插入的sql:"+sql.toString());
				System.out.println("修改的sql:"+update_sql.toString());
				QueryFactory.executeUpdateSQL(sql.toString());
				QueryFactory.executeUpdateSQL(update_sql.toString());
				QueryFactory.executeUpdateSQL(update_sql_tagsId.toString());
				
			}
		}
	}
	
	public int moveNews() throws SQLException{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -NewsService.history_dates);

		String sql = "select * from basic.news where createdate<=STR_TO_DATE('"+DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMMDD)+" 23:59:59','%Y-%m-%d %H:%i:%s') limit 0,100";
		System.out.println("sql is :"+sql);
		List<BizObject> list = null;
		BizObject bak = new BizObject("news_his_bak");
		int i=0;
		while(true){
			list = QueryFactory.executeQuerySQL(sql);
			if(list==null || list.size()<=0) break;
			i+=list.size();
			for(BizObject biz : list){
				
				bak = biz.duplicate();
				bak.resetObjType("news_his_bak");
				
				BaseJob.currentSession().addOrUpdate(bak);
				BaseJob.currentSession().delete(biz);
			}
		}
		return i;
	}

	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -7);
		System.out.println(c.getTime());
	}
	
	@Override
	public void deleteCopyforms() throws SQLException {
		String sql = "delete from ben.origin_news where 1=1";
		QueryFactory.executeUpdateSQL(sql);
	}

}
