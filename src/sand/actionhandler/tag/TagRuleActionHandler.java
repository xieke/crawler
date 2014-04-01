package sand.actionhandler.tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.CandoCheck;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.SystemKit;
import sand.service.basic.service.TagService;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class TagRuleActionHandler extends ActionHandler {
	
	private TagService tagService;

	public TagRuleActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "tag_rule";
		this._moduleId = "tag";
	}
	
	public void list() throws SQLException{
		this.clearQueryParam();
		super.listObj();
		List<BizObject> list = this.getTagService().openAllWithSelectedTag(this.getParameter("objId"));
		
//		for(BizObject biz : list){
//			biz.set("tag_name", this.setTagName(biz.getString("tag_id").split(",")));
//		}

		this._request.setAttribute("urgentList", SystemKit.getNoCachePickList("urgent"));
		this._request.setAttribute("importanceList", SystemKit.getNoCachePickList("importance"));
		this._request.setAttribute("tagsList", list);
		this._nextUrl = "/template/tagging_rule/edit.jsp";
	}

	public String setTagName(String[] tag_ids) throws SQLException{
		if(tag_ids.length<=0) return "";
		StringBuilder sql = new StringBuilder("select name from basic.tag where id in(");
		
		for(int i=0;i<tag_ids.length;i++){
			if(StringUtils.isNotBlank(tag_ids[i])){
				if(i==0) sql.append("'").append(tag_ids[i]).append("'");
				else sql.append(",'").append(tag_ids[i]).append("'");
			}
		}
		sql.append(")");
		List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString());
		StringBuilder str = new StringBuilder("");
		for(BizObject biz : list){
			str.append(biz.getString("name")).append(",");
		}
		return str.toString();
	}
	
	@CandoCheck("editTagRule")
	public void add() throws SQLException{
		BizObject tag_rule = this.getBizObjectFromMap("tag_rule");
		if(StringUtils.isBlank(tag_rule.getString("type"))) throw new ErrorException("条件域不能为空,请重新操作!");
		if(StringUtils.isBlank(tag_rule.getString("keyword"))) throw new ErrorException("关键字不能为空,请重新操作!");
		if(StringUtils.isBlank(tag_rule.getString("tag_id")) 
				&& StringUtils.isBlank(tag_rule.getString("urgent")) 
				&& StringUtils.isBlank(tag_rule.getString("importance"))) 
			throw new ErrorException("自动要打的标签、GP重要度、客户重要度，三者必须有一个不能为空,请重新操作!");
		
		this.getJdo().addOrUpdate(tag_rule);
		this.clearQueryParam();
		this.list();
	}
	
	public void show() throws SQLException{
		QueryFactory qf = new QueryFactory("tag_rule");
		BizObject tag_rule = qf.getByID(this._objId);
		List<BizObject> list = this.getTagService().openAllWithSelectedTagByTagIds(tag_rule.getString("tag_id"));

//		List<BizObject> urgentList = SystemKit.getNoCachePickList("urgent");
//		List<BizObject> importanceList = SystemKit.getNoCachePickList("importance");
//		String[] urgents = tag_rule.getString("urgent").split(",");
//		String[] importances = tag_rule.getString("importance").split(",");
//		List<String> urs = new ArrayList<String>();
//		List<String> ims = new ArrayList<String>();
//		if(urgents!=null && urgents.length>0) for(String s : urgents) if(StringUtils.isNotBlank(s)) urs.add(s);
//		if(importances!=null && importances.length>0) for(String s : importances) if(StringUtils.isNotBlank(s)) ims.add(s);
//		
//		for(BizObject urgent : urgentList) if(urs.contains(urgent.getId())) urgent.set("checkValue", urgent.getId());
//		for(BizObject importance : importanceList) if(ims.contains(importance.getId())) importance.set("checkValue", importance.getId());
		
		this._request.setAttribute("tagsList", list);
		this._request.setAttribute("obj", tag_rule);
//		this._request.setAttribute("urgentList", urgentList);
//		this._request.setAttribute("importanceList", importanceList);
		this._nextUrl = "/template/tagging_rule/edit2.jsp";
	}
	
	@CandoCheck("editTagRule")
	public void delete() throws SQLException{
		String id = this.getParameter("id");
		if(StringUtils.isBlank(id)) throw new ErrorException("要删除的记录不存在,请重新操作!");
		this.getJdo().beginTrans();
		BizObject tag_rule = new BizObject("tag_rule");
		tag_rule.setID(id);
		this.getJdo().delete(tag_rule);
		this.getJdo().commit();
		this.list();
	}
	
	@CandoCheck("editTagRule")
	public void deletes() throws SQLException{
		this.getJdo().beginTrans();
		String[] ids = this.getParameters("delid");
		BizObject tag_rule = new BizObject("tag_rule");
		
		for(String s : ids){
			tag_rule.setID(s);
			this.getJdo().delete(tag_rule);
		}
		this.getJdo().commit();
		this.list();
	}
	
	public void saveDupRule() throws SQLException{
		this.getJdo().addOrUpdate(this.getBizObjectFromMap("dup_rule"));
		this.showDupRule();
	}
	
	public void showDupRule() throws SQLException{
		QueryFactory qf = new QueryFactory("dup_rule");
		List<BizObject> list = qf.query();
		BizObject biz = null;
		if(list.size()>0){
			biz = list.get(0);
		}
		
		List<BizObject> importanceList = SystemKit.getNoCachePickList("importance");
		if(biz!=null){
			String[] importances = biz.getString("importance").split(",");
			List<String> ims = new ArrayList<String>();
			if(importances!=null && importances.length>0) for(String s : importances) if(StringUtils.isNotBlank(s)) ims.add(s);
			
			for(BizObject importance : importanceList) if(ims.contains(importance.getId())) importance.set("checkValue", importance.getId());
		}
		this._request.setAttribute("obj", biz);
		this._request.setAttribute("importanceList", importanceList);
		this._nextUrl = "/template/dup_rule/edit.jsp";
	}
	
	public TagService getTagService() {
		if(tagService == null) 
			tagService = (TagService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("tagService");
		return tagService;
	}
}
