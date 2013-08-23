package sand.actionhandler.rules;

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
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class RulesActionHandler extends ActionHandler {

	private TagService tagService;
	
	public RulesActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "rules";
	}
	
	public void show() throws SQLException{
		BizObject rules = QueryFactory.getInstance("rules").getByID(this._objId);
		List<BizObject> list = this.getTagService().openAllWithSelectedTag("");
		List<BizObject> cyclelist = SystemKit.getCachePickList("cycle");
		if(rules!=null){
			String[] str = rules.getString("tag_ids").split(",");
			
			if(str!=null && str.length>0){
				List<String> tagList = new ArrayList<String>();
				for(String s : str) {
					if(StringUtils.isNotBlank(s))
						tagList.add(s);
				}
				for(BizObject biz : list){
					if(tagList.contains(biz.getId())) biz.set("checked", "checked");
				}
			}
			this._request.setAttribute("tags_order", str);
			this._request.setAttribute("head_id", "head_content");
			String head_txt=SystemKit.getParamById("system_core", "head_content");
			head_txt=head_txt.replaceAll("\"","&quot;");
			this._request.setAttribute("head_txt", head_txt);
			
			String foot_txt=SystemKit.getParamById("system_core", "foot_content");
			foot_txt=foot_txt.replaceAll("\"","&quot;");
			this._request.setAttribute("foot_id", "foot_content");
			this._request.setAttribute("foot_txt",foot_txt);
			
			String[] cycle = rules.getString("cycle").split(",");
			if(cycle!=null && cycle.length>0){
				List<String> cycles = new ArrayList<String>();
				for(String s:cycle) {
					if(StringUtils.isNotBlank(s))
						cycles.add(s);
				}
				log("已经选中的:"+cycles);
				for(BizObject biz : cyclelist){
					if(cycles.contains(biz.getId())) biz.set("checked", "checked");
					else biz.set("checked", "");
				}
				log("结果:"+cyclelist);
			}
		}
		this._request.setAttribute("obj", rules);		
		this._request.setAttribute("tags", list);
		this._request.setAttribute("cycleList", cyclelist);
		this._nextUrl = "/template/rules/edit.jsp";
	}
	
	public void save() throws SQLException{
		BizObject rules = this.getBizObjectFromMap("rules");
		rules.set("tags", this.getParameter("tags"));
		rules.set("tag_ids", this.getParameter("tag_ids"));
		if(StringUtils.isBlank(rules.getId())) rules.set("status", 1);
		this.getJdo().addOrUpdate(rules);
		this.clearQueryParam();
		this.list();
	}

	@Ajax
	public String editItem() {
		try{
			String item_type = this.getParameter("item_type");
			String item_id = this.getParameter("item_id");
			String item_content = this.getParameter("item_content");
			if(StringUtils.isBlank(item_id)) item_id = item_type+"_content";
			
			QueryFactory qf = new QueryFactory("item");
			
			BizObject biz = new BizObject("item");
			biz.set("dicid", "system_core");
			biz.set("ikey", item_id);
			
			List<BizObject> list = qf.query(biz);
			
			biz.set("name", item_content);

			log("biz is "+biz);
			log("list "+list.size());
			if(list!=null && list.size()>0) {
				biz.setID(list.get(0).getId());
//				this.getJdo().update(biz);
			}
			
			else {
				biz.setID("");
//				this.getJdo().add(biz);
			}
			this.getJdo().addOrUpdate(biz);
			
			return "[{respCode:'0000', respMsg:'编辑成功',item_id:'"+item_id+"'}]";
		}catch(Exception e){
			e.printStackTrace();
			return "[{respCode:'9999', respMsg:'"+e.getMessage().replaceAll("'", "&nbsp;")+"'}]";
		}
	}
	
	public void deletes()throws SQLException{
		String[] ids = this.getParameters("delid");
		for(String s : ids) this.delete(s);
		this.list();
	}
	
	public void delete(String id) throws SQLException{
		BizObject rules = new BizObject("rules");
		rules.setID(id);
		rules.refresh();
		if(rules==null) throw new ErrorException("要删除的记录不存在 ,请重新操作!");
		rules.set("status", 0);
		this.getJdo().update(rules);
	}
	
	public void list() throws SQLException{
		super.setHardcoreFilter("status=1");
		super.listObj();
		this._nextUrl = "/template/rules/list.jsp";
	}

	public TagService getTagService() {
		if(tagService == null) 
			tagService = (TagService)WebApplicationContextUtils.getWebApplicationContext(ActionHandler._context).getBean("tagService");
		return tagService;
	}
	
	public static void main(String[] args) {
		String str = "[Properties/地产]<br>Guangzhou is seeking public opinion for a new land regulation which requires land buyers " +
				"to pay 20% of contractual prices if they keep the plot idle for over a year.    " +
				"<a href=\"http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=0fae5a728acd413389ec69b95cdff8aa\" " +
				"class=\"more\" target=\"_blank\">more</a><br><br><br>[700 HK (Tencent/腾讯)]<br>Ma Zhengqi, the vice chief " +
				"of the State Administration of Industry of Commerce and former vice mayor of Chongqing, was accused of malfeasance by " +
				"a journalist of Guangzhou-based newspaper New Express Daily.    " +
				"<a href=\"http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=080b18d83ac74158a8fa4ed065f60efd\" " +
				"class=\"more\" target=\"_blank\">more</a><br><br>";
		int start = str.indexOf("http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=0fae5a728acd413389ec69b95cdff8aa");
		int end = str.indexOf("http://10.38.128.105:16666/news.NewsActionHandler.showIt?objId=", start);
		System.out.println(str.substring(start+end, 32));
	}
	
}
