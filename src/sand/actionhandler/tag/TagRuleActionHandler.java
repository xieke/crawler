package sand.actionhandler.tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
import sand.service.news.NewsService;
import tool.basic.DateUtils;
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
		List<BizObject> list = this.getTagService().openAllWithSelectedTag(this.getParameter("objId"));
		String[] str = this.getParameter("tag_ids2").split(",");
		if(str!=null && str.length>0){
			List<String> tagList = new ArrayList<String>();
			for(String s : str) tagList.add(s);
			for(BizObject biz : list) if(tagList.contains(biz.getId())) biz.set("checked", "checked");
		}

		this._request.setAttribute("tags2", this.getParameter("tags2"));
		this._request.setAttribute("tagsList", list);
		this._request.setAttribute("objList", exeQuerySQL());
		this._nextUrl = "/template/tagging_rule/edit.jsp";
	}
	
	private List<BizObject> exeQuerySQL() throws SQLException{
		String name = this.getParameter("name");
		String keyword = this.getParameter("keyword");
		String urgent = this.getParameter("urgent");
		String importance = this.getParameter("importance");
		String copyfrom_lat = this.getParameter("copyfrom_lat");
		String copyfrom_lat0 = this.getParameter("copyfrom_lat0");
		String copyfrom_lat1 = this.getParameter("copyfrom_lat1");
		String copyfrom_lat2 = this.getParameter("copyfrom_lat2");
		String tag_ids2 = this.getParameter("tag_ids2");
		String tags2 = this.getParameter("tags2");
		String[] tag_ids = null;
		if(StringUtils.isNotBlank(tag_ids2))
			tag_ids = tag_ids2.split(",");
		
		StringBuilder sql = new StringBuilder("");
		sql.append("select * from basic.tag_rule t where 1=1");
			
		List<Object> s = new ArrayList<Object>();
			
		if(tag_ids!=null && tag_ids.length>0){
			StringBuilder sql2 = new StringBuilder("");
			for(int i=0;i<tag_ids.length;i++){
				if(StringUtils.isNotBlank(tag_ids[i])){
					if(StringUtils.isNotBlank(sql2)) sql2.append(" or ");
					sql2.append(" tag_id like ? ");
					s.add("%"+tag_ids[i]+"%");
				}
			}
			if(StringUtils.isNotBlank(sql2)) sql.append(" and (").append(sql2).append(")");
		}
		
		if(StringUtils.isNotBlank(name)) {
			sql.append(" and t.name like ?");
			s.add("%"+name+"%");
		}
		if(StringUtils.isNotBlank(urgent)) {
			sql.append(" and t.urgent=?");
			s.add(urgent);
		}
		if(StringUtils.isNotBlank(importance)) {
			sql.append(" and t.importance=?");
			s.add(importance);
		}
		if(StringUtils.isNotBlank(keyword)) {
			sql.append(" and t.keyword like ?");
			s.add("%"+keyword+"%");
		}
		if(StringUtils.isNotBlank(copyfrom_lat)) {
			String[] s_lat = copyfrom_lat.split(",");
			if(s_lat != null && s_lat.length>0){
				StringBuilder sql2 = new StringBuilder("");
				for(String str : s_lat){
					if(StringUtils.isNotBlank(str)){
						if(StringUtils.isNotBlank(sql2)) sql2.append(" or ");
						sql2.append(" copyfrom_lat like ? ");
						s.add("%"+str+"%");
					}
				}
				if(StringUtils.isNotBlank(sql2)) sql.append(" and (").append(sql2).append(")");
			}
		}
		sql.append(" order by modifydate desc");
		log("392 sql is "+sql.toString());
		
		List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString(), s, this.preparePageVar(), this.getJdo().getCon());
		
		this._request.setAttribute("tags2", tags2);
		this._request.setAttribute("name",name);
		this._request.setAttribute("keyword",keyword);
		this._request.setAttribute("urgent",urgent);
		this._request.setAttribute("importance",importance);
		this._request.setAttribute("copyfrom_lat",copyfrom_lat);
		this._request.setAttribute("copyfrom_lat0",copyfrom_lat0);
		this._request.setAttribute("copyfrom_lat1",copyfrom_lat1);
		this._request.setAttribute("copyfrom_lat2",copyfrom_lat2);
		this._request.setAttribute("tag_ids2",tag_ids2);
		
		return list;
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
//		if(StringUtils.isBlank(tag_rule.getString("type"))) throw new ErrorException("条件域不能为空,请重新操作!");
//		if(StringUtils.isBlank(tag_rule.getString("keyword"))) throw new ErrorException("关键字不能为空,请重新操作!");
		if(StringUtils.isBlank(tag_rule.getString("keyword"))
				&&StringUtils.isBlank(tag_rule.getString("weibo_uid"))
				&&StringUtils.isBlank(tag_rule.getString("copyfromurl"))
				&&StringUtils.isBlank(tag_rule.getString("task_no")))
			throw new ErrorException("文章来源纬度中的微博ID、来源URL、火车头任务编号，以及关键字纬度中的关键字四者必须有一个不能为空,请重新操作!");
		
		if(StringUtils.isBlank(tag_rule.getString("tag_id")) 
				&& StringUtils.isBlank(tag_rule.getString("urgent")) 
				&& StringUtils.isBlank(tag_rule.getString("importance"))) 
			throw new ErrorException("自动要打的标签、GP重要度、客户重要度，三者必须有一个不能为空,请重新操作!");
		
		if(StringUtils.isBlank(tag_rule.getString("weibo_uid"))) tag_rule.set("weibo_uid", "");
		if(StringUtils.isBlank(tag_rule.getString("copyfromurl"))) tag_rule.set("copyfromurl", "");
		if(StringUtils.isBlank(tag_rule.getString("task_no"))) tag_rule.set("task_no", "");
		if(StringUtils.isBlank(tag_rule.getString("copyfrom_lat"))) tag_rule.set("copyfrom_lat", "");
		this.getJdo().addOrUpdate(tag_rule);
		this.clearQueryParam();
		this.list();
	}
	
	public void show() throws SQLException{
		if(StringUtils.isBlank(this._objId)){
			List<BizObject> list = this.getTagService().openAllWithSelectedTag(this.getParameter("objId"));
			this._request.setAttribute("tagsList", list);
			this._nextUrl = "/template/tagging_rule/edit2.jsp";
			return;
		}
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
	
	public void copy() throws SQLException{
		if(StringUtils.isBlank(this._objId)){
			throw new ErrorException("要复制的记录为空,请重新操作!");
		}
		QueryFactory qf = new QueryFactory("tag_rule");
		BizObject tag_rule = qf.getByID(this._objId);
		List<BizObject> list = this.getTagService().openAllWithSelectedTagByTagIds(tag_rule.getString("tag_id"));
		
		tag_rule.setID("");
		this._request.setAttribute("tagsList", list);
		this._request.setAttribute("obj", tag_rule);
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
