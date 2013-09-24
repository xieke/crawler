package sand.service.news;

import java.sql.SQLException;
import java.util.List;

import tool.dao.BizObject;

public interface NewsService {
	public static final String TABLE_NAME = "news";
	public static final String HIS_TABLE_NAME = "his_news";
	public static final String NEWS_CONTENT = "news_content";
	public static final String COPYFORM_TABLE_NAME = "ben.origin_news";
	
	public static final int history_dates = 90;
	
	/**
	 * 获取源新闻
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<BizObject> copyfromNews() throws SQLException;
	
	public List<String> getTagIdsByNewsId(String news_id) throws SQLException;
	
	public void addNews(List<BizObject> copyfromNews) throws SQLException;
	
	public void addNews(BizObject news) throws SQLException ;
	
	/**
	 * 删除新闻采集表
	 * 
	 * @throws SQLException
	 */
	public void deleteCopyforms() throws SQLException;
	
	/**
	 * 手动标签
	 * 
	 * @param tagRuleIds
	 * @param startDate
	 * @param endDate
	 * @throws SQLException
	 */
	public void doHittingTag(String tagRuleIds,String startDate,String endDate) throws SQLException;
	
	public int moveNews() throws SQLException;
}
