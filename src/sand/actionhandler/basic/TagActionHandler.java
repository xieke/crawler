package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.SystemKit;
import sand.service.basic.service.TagService;
import tool.basic.BasicContext;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class TagActionHandler extends ActionHandler {
	
	private TagService tagService;

	public TagActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "tag";
		this._moduleId = "basic";
	}
	
	public void list() throws SQLException{
		QueryFactory qf = new QueryFactory("tag");
		
		if(qf.query().size()<=0){
			BizObject tt = new BizObject("tag");
			tt.set("name", "标签库");
			tt.set("isvalid", BasicContext.IS_AUDIT_YES);
			tt.set("id", "root");
			this.getJdo().add(tt);
		}
		
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
//		SystemKit.page(list, this.preparePageVar());
		this._request.setAttribute("objList", list);
		this._nextUrl = "/template/tag/edit.jsp";
	}

	@Ajax
	public String openTag() throws SQLException{
		QueryFactory qf = new QueryFactory("tag");
		
		if(qf.query().size()<=0){
			BizObject tt = new BizObject("tag");
			tt.set("name", "标签库");
			tt.set("isvalid", BasicContext.IS_AUDIT_YES);
			tt.set("id", "root");
			this.getJdo().add(tt);
		}
		
		String pid=this.getParameter("pid");
		BizObject tag = new BizObject("tag");
		List<BizObject> subdepts= new ArrayList();
		if(pid.equals("")){
			tag.set("parent_id", "is null");
			tag.set("isvalid", BasicContext.IS_AUDIT_YES);
			subdepts=tag.getQF().query(tag);
		}else{
			tag.set("parent_id", pid);
			tag.set("isvalid",  BasicContext.IS_AUDIT_YES);
			subdepts=tag.getQF().query(tag);
		}		
		
		int i=0;
		String ret="[";
		for(BizObject sub:subdepts){
			if(i>0){
				ret=ret+",";
			}
			if (sub.getList("tag").size()>0)
				ret=ret+"{id:'"+sub.getId()+"',pid:'"+pid+"',dataObject:{name:'"+sub.getString("name")+"'," +
						"description:'"+sub.getString("description")+"',orderby:'"+sub.getString("orderby")+"'}," +
								"userObject:{isGroup:true},isLeaf:false}";
			else
				ret=ret+"{id:'"+sub.getId()+"',pid:'"+pid+"',dataObject:{name:'"+sub.getString("name")+"'," +
						"description:'"+sub.getString("description")+"',orderby:'"+sub.getString("orderby")+"'}," +
								"userObject:{isGroup:true},isLeaf:true}";
			
			i++;
		}			
		ret=ret+"]";
		return ret;
	}	
	
	public void save() throws SQLException{
		this.getJdo().beginTrans();
		BizObject tag = this.getBizObjectFromMap("tag");
//		if(StringUtils.isBlank(tag.getString("name"))) throw new ErrorException("要添加的标签名不能为空,请重新操作!");
		String[] str = tag.getString("name").split(",");
		if(str.length<=0) throw new ErrorException("要添加的标签名不能为空,请重新操作!");
		QueryFactory qf = new QueryFactory("tag");
		
		for(String s : str){
			if(StringUtils.isNotBlank(s.trim())){
				qf.setHardcoreFilter("name='"+s.trim()+"' and isvalid='1'");
				if(qf.query().size()>0) throw new ErrorException("要添加的标签名:'"+s.trim()+"'已经存在,不能重复添加!");
				
				tag.setID("");
				tag.set("name", s.trim());
				tag.set("isvalid", BasicContext.IS_VALID_YES);
				if(StringUtils.isBlank(tag.getString("orderby")))
					tag.set("orderby", 9999);
				if(StringUtils.isBlank(tag.getString("parent_id")))
					tag.set("parent_id", "root");
				this.getJdo().addOrUpdate(tag);
			}
		}
		this.getJdo().commit();
		this.clearQueryParam();
		this.list();
	}
	
	public void edit() throws SQLException{
		this.getJdo().beginTrans();
		BizObject tag = this.getBizObjectFromMap("tag");
		
		QueryFactory qf = new QueryFactory("tag");
		qf.setHardcoreFilter("name='"+tag.getString("name")+"' and isvalid='1' and id!='"+tag.getId()+"'");
		if(qf.query().size()>0) throw new ErrorException("要添加的标签名:'"+tag.getString("name")+"'已经存在,不能重复添加!");
		
		String old_name = tag.getString("old_name");
		
		if(StringUtils.isBlank(tag.getString("parent_id")))
			tag.set("parent_id", "root");
		this.getJdo().addOrUpdate(tag);
		
		if(!old_name.equals(tag.getString("name"))){
//			this.updateRules_tags(tag.getString("name"),old_name);
//			this.updateTagRule_tags(tag.getString("name"),old_name);
//			this.updateNews_tags(tag.getString("name"),old_name);
		}
		this.getJdo().commit();
		this.clearQueryParam();
		this.list();
	}
	
	public void updateRules_tags(String name,String old_name) throws SQLException{
		QueryFactory qf = new QueryFactory("rules");
		qf.setHardcoreFilter("tags like '%,"+old_name+",%'");
		List<BizObject> list = qf.query();
		for(BizObject biz : list){
			biz.set("tags", biz.getString("tags").replace(","+old_name+",", ","+name+","));
			this.getJdo().update(biz);
		}
	}
	
	public void updateTagRule_tags(String name,String old_name) throws SQLException{
		QueryFactory qf = new QueryFactory("tag_rule");
		qf.setHardcoreFilter("tag_name like '%,"+old_name+",%'");
		List<BizObject> list = qf.query();
		for(BizObject biz : list){
			biz.set("tag_name", biz.getString("tag_name").replace(","+old_name+",", ","+name+","));
			this.getJdo().update(biz);
		}
	}
	
	public void updateNews_tags(String name,String old_name) throws SQLException{
		QueryFactory qf = new QueryFactory("news");
		qf.setHardcoreFilter("tags like '%,"+old_name+",%'");
		List<BizObject> list = qf.query();
		for(BizObject biz : list){
			biz.set("tags", biz.getString("tags").replace(","+old_name+",", ","+name+","));
			this.getJdo().update(biz);
		}
	}
	
	public void saveOrderBy() throws SQLException{
		List<BizObject> list = this.getBizObjectWithType("tags");
		this.getJdo().beginTrans();
		for(BizObject biz : list){
			biz.resetObjType("tag");
			this.getJdo().update(biz);
		}
		this.getJdo().commit();
		this.list();
	}
	
	public void show() throws SQLException{
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		this._request.setAttribute("objList", list);
		
		String objId = this.getParameter("objId");
		if(StringUtils.isBlank(objId)) throw new ErrorException("要编辑的标签不存在,请重新操作!");
		
		BizObject biz = QueryFactory.getInstance("tag").getByID(objId);
		this._request.setAttribute("obj", biz);
		this._nextUrl = "/template/tag/edit2.jsp";
	}
	
	public void delete() throws SQLException{
		String id = this.getParameter("id");
		if(StringUtils.isBlank(id)) throw new ErrorException("要删除的标签不存在,请重新操作!");
		this.getJdo().beginTrans();
		this.del(id);
		this.getJdo().commit();
		this.list();
	}
	
	private void del(String id) throws SQLException{
		BizObject tag = new BizObject("tag");
		tag.setID(id);
		tag.set("isvalid", BasicContext.IS_VALID_NO);
		this.getJdo().update(tag);
		
		List<BizObject> list = QueryFactory.getInstance("re_bill_tag").query("tag_id",id);
		for(BizObject biz : list){
			this.getJdo().delete(biz);
		}
		
		this.deleteCascade(tag.getId());
	}
	
	public void deletes() throws SQLException{
		this.getJdo().beginTrans();
		String[] ids = this.getParameters("delid");
		for(String s : ids){
			this.del(s);
		}
		this.getJdo().commit();
		this.list();
	}
	
	public void deleteCascade(String parent_id) throws SQLException{
		QueryFactory qf = new QueryFactory("tag");
		qf.setHardcoreFilter("isvalid='"+BasicContext.IS_VALID_YES+"' and parent_id='"+parent_id+"'");
		List<BizObject> list = qf.query();
		
		for(BizObject biz : list){
			biz.set("isvalid", BasicContext.IS_VALID_NO);
			this.getJdo().update(biz);
			
			this.deleteCascade(biz.getId());
		}
	}
	
	public TagService getTagService() {
		if(tagService == null) 
			tagService = (TagService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("tagService");
		return tagService;
	}
	
	public static void main(String[] args) {
		String old_name="BIDU (Baidu/百度)";
		String name="BIDU (Baidu/百度啊)";
		String str = ",BIDU (Baidu/百度),";
		System.out.println(str.replace(old_name, name));
	}
}
