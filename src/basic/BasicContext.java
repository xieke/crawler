package basic;

public class BasicContext {
	//是否有效
	public static final String ISVALID_YES = "1";//有效
	public static final String ISVALID_NO = "0";//无效
	
	//状态 处理
	public static final String STATUS_DISPOSE_NO = "0";//等待处理N
	public static final String STATUS_DISPOSE_YES = "1";	//已处理Y
	public static final String STATUS_DISPOSE_DELETE = "2";//删除D
	
	//是否发布
	public static final String ISSUE_YES = "1";//发布
	public static final String ISSUE_NO = "0";//未发布
	
	//是否置顶
	public static final String ISTOP_YES = "1";//置顶
	public static final String ISTOP_NO = "0";//未置顶
	
	//是否已自动标签
	public static final String ISAUTOTAG_YES = "1";//是自动标签
	public static final String ISAUTOTAG_NO = "0";//未自动标签
	
	//是否推荐新闻
	public static final String IS_RECOMMEND_YES = "1";//是
	public static final String IS_RECOMMEND_NO = "0";//否
	
	//自动标签规则的类型
	public static final String TAGRULE_TYPE_CONTENT = "1";	//自动标签规则:根据标题和内容过滤
	public static final String TAGRULE_TYPE_URL = "0";		//自动标签规则:根据URL过滤
	
}
