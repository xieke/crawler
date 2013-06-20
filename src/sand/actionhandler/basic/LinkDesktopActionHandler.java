package sand.actionhandler.basic;

import java.io.File;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.business.document.Document;
import sand.depot.tool.system.ErrorException;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class LinkDesktopActionHandler extends ActionHandler {

	public LinkDesktopActionHandler(HttpServletRequest req,
			HttpServletResponse res) {
		super(req, res);
		this._objType = "link_desktop";
		this._moduleId = "basic";
	}

	public void list() throws SQLException{
		this.pushLocation("快捷桌面设置列表", "/basic.LinkDesktopActionHandler.list");
		super.listObj();
		super.setOrderBy("createdate desc");
		this._nextUrl = "/template/basic/link_desktop/list.jsp";
	}
	
	public void show() throws SQLException{
		this.pushLocation("快捷桌面设置列表", "/basic.LinkDesktopActionHandler.list");
		this.pushLocation("快捷桌面设置", "/basic.LinkDesktopActionHandler.show?objId="+this._objId);
		BizObject biz = new BizObject(this._objType);
		biz.setID(this._objId);
		biz.refresh();
		this._request.setAttribute("obj",biz);
		this._nextUrl = "/template/basic/link_desktop/edit.jsp";
	}
	
	public void save() throws SQLException{
		this.getJdo().beginTrans();
		BizObject biz = this.getBizObjectFromMap("link_desktop");
		this.getJdo().addOrUpdate(biz);
		List<File> files = this.getUploadFiles();
		File file = null;
		if (files.size()>0){
			
			BizObject annex=new BizObject("annex");
			List<BizObject> v =annex.getQF().query("bizid",biz.getId());
			if(v.size()>0)
				annex=v.get(0);
			file = files.get(0);			
			annex.set("bizid", biz.getId());
			annex.set("type", Document.TYPE_USERPIC);
			Document.updateAnnex(annex, file,1);		
			
			biz.set("pic", annex.getId());
			this.getJdo().addOrUpdate(biz);
		}
		this.getJdo().commit();
		this._tipInfo = "保存成功！";
		this._request.setAttribute("nextUrl", "/basic.LinkDesktopActionHandler.show?objId="+biz.getId());
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}
	
	public void delete() throws SQLException{
		this.getJdo().beginTrans();
		BizObject biz = new BizObject("re_user_link_desktop");
		biz.set("link_desktop_id", this._objId);
		this.getJdo().delete(biz);
		
		BizObject link = new BizObject("link_desktop");
		link.setID(this._objId);
		link.refresh();
		if(StringUtils.isNotBlank(link.getString("pic")))
			Document.deleteAnnex(link.getString("pic"));
		this.getJdo().delete(link);
		this.getJdo().commit();
		
		this._tipInfo = "删除成功！";
		this._request.setAttribute("nextUrl", "/basic.LinkDesktopActionHandler.list");
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}
	
	public void listUserLink() throws SQLException{
		
		//全部应用
		StringBuilder sql = new StringBuilder
			("select l.*,r.orderby,r.id rid from link_desktop l left JOIN (select * from re_user_link_desktop rr where rr.user_id='");
		sql.append(this._curuser.getId()).append("') r on l.id=r.link_desktop_id order by r.orderby,l.name desc");
		
		//System.out.println(sql);
		
		List<BizObject> list = QueryFactory.executeQuerySQL(sql.toString());
		this.setAttribute("objList", list);
		
		this._nextUrl = "/template/basic/link_desktop/user_list.jsp";
	}
	
	public void saveUserLink() throws SQLException{
		this.getJdo().beginTrans();
		List<BizObject> list = this.getBizObjectWithType("re_user_link_desktop");
		StringBuilder sql = new StringBuilder("delete from re_user_link_desktop where user_id='");
		sql.append(this._curuser.getId()).append("'");
		QueryFactory.executeUpdateSQL(sql.toString());
		
		int i=0;
		for(BizObject biz : list){
			biz.resetObjType("re_user_link_desktop");
			
			if(biz.getString("checkbox").equals("1")){
				++i;
				if(i>5) throw new ErrorException("对不起,应用最多设置5个,请重新操作!");
				biz.set("user_id", this._curuser.getId());
				this.getJdo().addOrUpdate(biz);
			}
		}
		this.getJdo().commit();
		this._tipInfo = "设置成功！";
		this._request.setAttribute("nextUrl", "/basic.LinkDesktopActionHandler.listUserLink");
		this._request.setAttribute("msg_type", "SUCCESS");
		this._nextUrl = super._msgUrl;
	}
	
	
	/**
	 * 更新用户应用
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Ajax
	public String updateUserLink() throws SQLException{
		
		String linkid=this._request.getParameter("linkid");
		String chvalue=this._request.getParameter("chvalue");
		this.getJdo().beginTrans();
		if(chvalue.equals("1"))//添加
		{
			BizObject obj=new BizObject ("re_user_link_desktop");
			obj.set("user_id",this._curuser.getId() );
			obj.set("link_desktop_id", linkid);
			List<BizObject> list =QueryFactory.getInstance("re_user_link_desktop").query(obj);
			if(list.size()==0)//防止重复添加
			{
				this.getJdo().add(obj);
			}
		}else //删除
		{
			String sql = "delete from re_user_link_desktop where user_id='"+this._curuser.getId()+"' and  link_desktop_id='"+linkid+"'";	
			QueryFactory.executeUpdateSQL(sql);
		}
		this.getJdo().commit();
		return "1";

	}
	
	
	
	
	
}
