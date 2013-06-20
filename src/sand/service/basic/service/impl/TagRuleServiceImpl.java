package sand.service.basic.service.impl;

import java.sql.SQLException;
import java.util.List;

import sand.service.basic.service.TagRuleService;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class TagRuleServiceImpl implements TagRuleService {

	@Override
	public List<BizObject> queryTagRules() throws SQLException {
		QueryFactory qf = new QueryFactory(TagRuleService.TABLE_NAME);
		qf.setOrderBy("type");
		return qf.query();
	}
	
}
