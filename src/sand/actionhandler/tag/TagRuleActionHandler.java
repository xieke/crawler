package sand.actionhandler.tag;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.ErrorException;
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
	
	public void add() throws SQLException{
		BizObject tag_rule = this.getBizObjectFromMap("tag_rule");
		if(StringUtils.isBlank(tag_rule.getString("type"))) throw new ErrorException("条件域不能为空,请重新操作!");
		if(StringUtils.isBlank(tag_rule.getString("keyword"))) throw new ErrorException("关键字不能为空,请重新操作!");
		if(StringUtils.isBlank(tag_rule.getString("tag_id"))) throw new ErrorException("自动要打的标签不能为空,请重新操作!");
		
		this.getJdo().addOrUpdate(tag_rule);
		this.clearQueryParam();
		this.list();
	}
	
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
	
	public TagService getTagService() {
		if(tagService == null) 
			tagService = (TagService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("tagService");
		return tagService;
	}
}
