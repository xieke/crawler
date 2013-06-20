package sand.service.basic.service;

import java.sql.SQLException;
import java.util.List;

import tool.dao.BizObject;

public interface TagRuleService {
	public static final String TABLE_NAME = "tag_rule";
	
	public List<BizObject> queryTagRules() throws SQLException;
}
