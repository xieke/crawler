package sand.actionhandler.customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.CandoCheck;
import sand.depot.tool.system.ErrorException;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class CustomerActionHandler extends ActionHandler {

	public CustomerActionHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType="customers";
		this._moduleId = "customer";
	}

	@CandoCheck("editCustmoer")
	public void list() throws SQLException{
		super.setHardcoreFilter("status=1");
		super.listObj();
		List<BizObject> objList = (ArrayList)this._request.getAttribute("objList");
		for(BizObject biz : objList){
			String sql = "select GROUP_CONCAT(p.name) jobs from basic.postjob p where status='1' and customers like '%"+biz.getId()+"%'";
			List<BizObject> list = QueryFactory.getInstance("postjob").executeQuerySQL(sql);
			if(list.size()>0) biz.set("jobs", list.get(0).getString("jobs"));
			
			sql = "select lastposttime from basic.postjob p where status='1' and customers like '%"+biz.getId()+"%' order by lastposttime desc limit 0,1";
			list = QueryFactory.getInstance("postjob").executeQuerySQL(sql);
			if(list.size()>0) biz.set("lastposttime", list.get(0).getString("lastposttime"));
		}
		this._nextUrl = "/template/customer/list.jsp";
	}
	
	@CandoCheck("editCustmoer")
	public void show() throws SQLException{
		if(StringUtils.isNotBlank(this._objId)){
			String sql = "select GROUP_CONCAT(p.name) jobs from basic.postjob p where status='1' and customers like '%"+this._objId+"%'";
			List<BizObject> list = QueryFactory.getInstance("postjob").executeQuerySQL(sql);
			if(list.size()>0) this._request.setAttribute("jobs", list.get(0).getString("jobs"));
		}
		BizObject customer = new BizObject("customers");
		customer.setID(this._objId);
		customer.refresh();
		this._request.setAttribute("obj", customer);
		this._nextUrl = "/template/customer/edit.jsp";
	}
	
	@CandoCheck("editCustmoer")
	public void save() throws SQLException{
		BizObject customers = this.getBizObjectFromMap("customers");
		if(StringUtils.isBlank(customers.getId()))customers.set("status", 1);
		this.getJdo().addOrUpdate(customers);
		String postjobId = this.getParameter("postjobId");
		if(StringUtils.isNotBlank(postjobId)){
			QueryFactory qf = new QueryFactory("postjob");
			BizObject job = qf.getByID(postjobId);
			if(job.getString("customers").indexOf("customers.getId()")==-1){
				job.set("customers", (StringUtils.isBlank(job.getString("customers"))?"":(job.getString("customers")+","))+customers.getId());
				job.set("cnames", (StringUtils.isBlank(job.getString("customers"))?"":(job.getString("customers")+","))+customers.getString("name"));
				this.getJdo().update(job);
			}
		}
		this._objId = customers.getId();
		this.show();
	}
	
	@CandoCheck("editCustmoer")
	public void deletes()throws SQLException{
		String[] ids = this.getParameters("delid");
		for(String s : ids) this.delete(s);
		this.list();
	}
	
	public void delete(String id) throws SQLException{
		BizObject customer = new BizObject("customers");
		customer.setID(id);
		customer.refresh();
		if(customer==null) throw new ErrorException("要删除的记录不存在 ,请重新操作!");

		//更新任务调度表
		QueryFactory qf = new QueryFactory("postjob");
		List<BizObject> list = qf.mquery("customers", customer.getId());
		for(BizObject biz : list){
			biz.set("customers", biz.getString("customers").replaceAll(customer.getId(), ""));
			this.getJdo().update(biz);
		}
		
		customer.set("status", 0);
		this.getJdo().update(customer);
	}
	
	public void feedbackList() throws SQLException{
		String operator = this.getParameter("operator");
		if(operator.equals("open"))
			super.setHardcoreFilter("operator='open'");
		else if(operator.equals("dis_like"))
			super.setHardcoreFilter("(operator='dislike' or operator='like')");
		super.listObj("userclicks");
		List<BizObject> list = (List<BizObject>) this._request.getAttribute("objList");
		for(BizObject biz : list) biz.setFk("newsid", "news");
		this._request.setAttribute("operator", operator);
		this._nextUrl = "/template/customer/feedbackList.jsp";
	}
}
