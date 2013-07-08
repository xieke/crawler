package sand.service.basic.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import sand.actionhandler.system.ActionHandler;
import sand.service.basic.service.TagService;
import sand.service.news.NewsService;
import tool.basic.BasicContext;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class TagServiceImpl implements TagService {

	@Resource(name="newsService")
	NewsService newsService;
	
	public BizObject getById(String id) throws SQLException{
		QueryFactory qf = new QueryFactory(TagService.TABLE_NAME);
		BizObject biz = qf.getByID(id);
		if(biz==null) biz = new BizObject(TagService.TABLE_NAME);
		return biz;
	}
	
	public List<BizObject> openAllTag() throws SQLException{
		List<BizObject> list= new ArrayList<BizObject>();

		this.getAll("root",list,"");
		return list;
	}
	
	public List<BizObject> queryTagsByBillId(String billId) throws SQLException{
		QueryFactory qf = new QueryFactory(TagService.BILL_TABLE_NAME);
		List<BizObject> list = new ArrayList<BizObject>();
		for(BizObject biz : qf.query("bill_id",billId)) list.add(biz.getBizObj("tag_id"));
		return list;
	}
	
	public List<BizObject> openAllWithSelectedTag(String billId) throws SQLException{
		List<BizObject> list= new ArrayList<BizObject>();

		this.getAll("root",list,"");
		this.setChecked(billId, list);
		return list;
	}
	
	/**
	 * 取得所有的标签,并且根据传进来的标签id集合,给传进来的标签id打个选中的标示
	 *
	 * @param tagIds  字段格式,中间用","号隔开,如:   标签1,标签2,...
	 * @return
	 * @throws SQLException
	 */
	public List<BizObject> openAllWithSelectedTagByTagIds(String tagIds) throws SQLException{
		List<BizObject> list= new ArrayList<BizObject>();

		this.getAll("root",list,"");
		String[] tag_ids = tagIds.split(",");
		List<String> tag_id_lists = new ArrayList<String>();
		for(String s : tag_ids) tag_id_lists.add(s);
		for(BizObject biz : list)
			if(tag_id_lists.contains(biz.getId())) biz.set("checked", "checked");
		return list;
	}

	private void setChecked(String bill_id,List<BizObject> list) throws SQLException{
//		List<String> strs = this.queryTagsByBillIdToString(bill_id);
		List<String> strs = this.newsService.getTagIdsByNewsId(bill_id);
		for(BizObject biz : list)
			if(strs.contains(biz.getId())) biz.set("checked", "checked");
	}
	
	/**
	 * 
	 * @param parent_id
	 * @param list
	 * @param tc	页面显示树形状的填充空格
	 * @throws SQLException
	 */
	public void getAll(String parent_id,List<BizObject> list,String tc) throws SQLException{
		QueryFactory qf = new QueryFactory(TagService.TABLE_NAME);
		qf.setHardcoreFilter("isvalid='"+BasicContext.IS_AUDIT_YES+"' and parent_id='"+parent_id+"'");
		qf.setOrderBy("orderby asc");
		List<BizObject> qflist = qf.query();
		for(BizObject biz : qflist){
			biz.set("level", tc+"|-");
			list.add(biz);
			getAll(biz.getId(),list,tc+"&nbsp;&nbsp;&nbsp;");
		}
	}
	
	private List<String> queryTagsByBillIdToString(String billId) throws SQLException{
		QueryFactory qf = new QueryFactory(TagService.BILL_TABLE_NAME);
		List<BizObject> list = qf.query("bill_id",billId);
		List<String> str = new ArrayList<String>();
		for(BizObject biz : list) str.add(biz.getString("tag_id"));
		return str;
	}
	
	public void deleteReBillTagsByBillId(String bill_id) throws SQLException{
		String sql = "delete from "+TagService.BILL_TABLE_NAME + " where bill_id='"+bill_id+"'";
		QueryFactory.executeUpdateSQL(sql);
//		QueryFactory qf = new QueryFactory(TagService.BILL_TABLE_NAME);
//		List<BizObject> list = qf.query("bill_id",bill_id);
//		for(BizObject biz : list)
//			ActionHandler.currentSession().delete(biz);
	}
	
	public void addReBillTags(String[] tag_ids,String bill_id) throws SQLException{
		BizObject biz = new BizObject(TagService.BILL_TABLE_NAME);
		for(String s : tag_ids){
			biz.setID("");
			biz.set("tag_id", s);
			biz.set("bill_id", bill_id);
			ActionHandler.currentSession().add(biz);
		}
	}

	/**
	 * 根据tag id 返回tagname
	 * @param tag_ids
	 * @return
	 * @throws SQLException
	 */
	public List<String> getTagNames(String[] tag_ids) throws SQLException{
		BizObject biz = new BizObject(TagService.TABLE_NAME);
		List<String> v=new ArrayList();
		//int i=0;
		for(String s : tag_ids){
			biz.setID(s);
			biz.refresh();
			//if(biz.getBizObj("tag_id")!=null)
				//tagNames[i]=biz.getBizObj("tag_id").getString("name");
			v.add(biz.getString("name"));
		//	ActionHandler.currentSession().add(biz);
		}
		return v;
	}
	public void addReBillTag(String tag_id,String bill_id) throws SQLException{
		QueryFactory qf = new QueryFactory(TagService.BILL_TABLE_NAME);
		qf.setHardcoreFilter("bill_id='"+bill_id+"' and tag_id='"+tag_id+"'");
		if(qf.query().size()>0) return ;
		BizObject biz = new BizObject(TagService.BILL_TABLE_NAME);
		biz.setID("");
		biz.set("tag_id", tag_id);
		biz.set("bill_id", bill_id);
		ActionHandler.currentSession().add(biz);
	}
}
