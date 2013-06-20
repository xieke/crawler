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
		
		for(String s : str){
			if(StringUtils.isNotBlank(s.trim())){
				tag.setID("");
				tag.set("name", s.trim());
				tag.set("isvalid", BasicContext.IS_VALID_YES);
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
		if(StringUtils.isBlank(tag.getString("parent_id")))
			tag.set("parent_id", "root");
		this.getJdo().addOrUpdate(tag);
		this.getJdo().commit();
		this.clearQueryParam();
		this.list();
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
}
