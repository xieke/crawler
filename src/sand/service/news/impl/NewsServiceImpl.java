package sand.service.news.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import sand.actionhandler.system.ActionHandler;
import sand.service.basic.service.TagRuleService;
import sand.service.basic.service.TagService;
import sand.service.news.NewsService;
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
				ActionHandler.currentSession().add(news);
				//自动打标签
				this.hitTagging(news);
			}
			QueryFactory.executeUpdateSQL("delete from ben.origin_news where  id="+biz.getId());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 自动打标签
	 * 
	 * @throws SQLException
	 */
	private void hitTagging(BizObject news) throws SQLException{
		List<BizObject> tagRuleList = tagRuleService.queryTagRules();
		
		for(BizObject tagRule : tagRuleList){
			if(tagRule.getString("type").equals(BasicContext.TAGRULE_TYPE_CONTENT)){
				if(news.getString("title").indexOf(tagRule.getString("keyword"))!=-1 
						|| news.getString("content").indexOf(tagRule.getString("keyword"))!=-1){
					String[] tag_ids = tagRule.getString("tag_id").split(",");
					for(String s : tag_ids){
						if(news.getString("tags").indexOf(s)==-1) {
							news.set("tags", news.getString("tags")+","+tagService.getById(s).getString("name"));
							news.set("tag_ids", news.getString("tag_ids")+","+s);
							ActionHandler.currentSession().update(news);
						}
						tagService.addReBillTag(s, news.getId());
					}
				}
			}else {
				if(news.getString("copyfromurl").indexOf(tagRule.getString("keyword"))!=-1){
					String[] tag_ids = tagRule.getString("tag_id").split(",");
					for(String s : tag_ids){
						if(news.getString("tags").indexOf(s)==-1) {
							news.set("tags", news.getString("tags")+","+tagService.getById(s).getString("name"));
							news.set("tag_ids", news.getString("tag_ids")+","+s);
							ActionHandler.currentSession().update(news);
						}
						tagService.addReBillTag(s, news.getId());
					}
				}
			}
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
	public void doHittingTag(String tagRuleIds,String startDate,String endDate) throws SQLException{
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

	@Override
	public void deleteCopyforms() throws SQLException {
		String sql = "delete from ben.origin_news where 1=1";
		QueryFactory.executeUpdateSQL(sql);
	}

}
