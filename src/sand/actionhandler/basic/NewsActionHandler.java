package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.depot.tool.system.ErrorException;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

@AccessControl("no")
public class NewsActionHandler extends ActionHandler {

	public NewsActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType="article";
		this._moduleId = "basic";
		// TODO Auto-generated constructor stub
	}
	

	public void viewArticle() throws SQLException{
		QueryFactory qf = new QueryFactory("article");
		if(StringUtils.isBlank(this.getParameter("articleid"))) throw new ErrorException("查看的文章错误,请重新操作!");
		BizObject article = qf.getByID(this.getParameter("articleid"));
		if(article!=null){
			article.set("times", article.getInt("times", 100)+1);
			this.getJdo().update(article);
		}
		this._request.setAttribute("article", article);
		if(article!=null){
//			BizObject biz = new BizObject("item");
//			biz.set("ikey", article.getString("article_type"));
//			biz = QueryFactory.getInstance("item").getOne(biz);
//			if(StringUtils.isBlank(biz.getString("memo")))
//				this._nextUrl = "/template/article/content_help.jsp";
			this._request.setAttribute("article_type", article.getString("article_type"));
		}else this._request.setAttribute("article_type", this.getParameter("article_type"));
		
		this._nextUrl = "/template/article/content_help.jsp";//this._nextUrl = "/template/article/"+biz.getString("memo")+".jsp";
	}
	
	public void listArticle() throws SQLException{
		if(StringUtils.isBlank(this.getParameter("template"))) throw new ErrorException("操作错误,请选择文章版块!");
		String article_type = this.getParameter("article_type");
		String keyword = this.getParameter("keyword");
		if(StringUtils.isBlank(article_type)){
			BizObject biz = new BizObject("item");
			biz.set("memo", this.getParameter("template"));
			List<BizObject> list = QueryFactory.getInstance("item").query(biz);
			String article_types = "('"+this.getParameter("template")+"'";
			for(int i=0;i<list.size();i++) article_types += ",'"+list.get(i).getString("ikey")+"'";
			article_types +=")";
			
			super.setHardcoreFilter("(title like '%"+keyword+"%' or content like '%"+keyword+"%')" +
					" and ispublish='1' and article_type in "+article_types);
		}else {
			super.setHardcoreFilter("(title like '%"+keyword+"%' or content like '%"+keyword+"%')" +
					" and article_type='"+article_type+"' and ispublish='1'");
		}
	
		super.setOrderBy("PUBLISH_DATE desc");
		super.listObj("article");
		System.out.println(super._sql);
		this._request.setAttribute("article_type", article_type);
		this._request.setAttribute("keyword", this.getParameter("keyword"));
		//跳转到本版块的列表页
		this._nextUrl = "/template/article/help.jsp";
		
	}
	
	/**
	 * 得到首页显示的2条网站公告
	 * @return
	 */
	@Ajax
	public String getWZGG(){
		try{
			QueryFactory qf = new QueryFactory("article");
			qf.setHardcoreFilter("article_type='WZGG' and ispublish='1'");
			qf.setOrderBy("PUBLISH_DATE desc");
			List list = qf.query(new PageVariable(2));
			return list.toString();
		}catch(Exception e){
			return "[]";
		}
	}

}
