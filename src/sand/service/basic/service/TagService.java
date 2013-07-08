package sand.service.basic.service;

import java.sql.SQLException;
import java.util.List;
import tool.dao.BizObject;

public interface TagService {
	public static final String TABLE_NAME = "tag";
	public static final String BILL_TABLE_NAME = "re_bill_tag";
	
	public BizObject getById(String id) throws SQLException;
	
	public List<BizObject> openAllTag() throws SQLException;
	
	public List<BizObject> openAllWithSelectedTag(String billId) throws SQLException;
	
	/**
	 * 取得所有的标签,并且根据传进来的标签id集合,给传进来的标签id打个选中的标示
	 *
	 * @param tagIds  字段格式,中间用","号隔开,如:   标签1,标签2,...
	 * @return
	 * @throws SQLException
	 */
	public List<BizObject> openAllWithSelectedTagByTagIds(String tagIds) throws SQLException;
	
	public List<BizObject> queryTagsByBillId(String billId) throws SQLException;
	
	public void deleteReBillTagsByBillId(String bill_id) throws SQLException;
	
	public List<String> getTagNames(String tag_ids[]) throws SQLException;
	
	public void addReBillTags(String[] tag_ids,String bill_id) throws SQLException;
	
	public void addReBillTag(String tag_id,String bill_id) throws SQLException;
	
}
