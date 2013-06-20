package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.tool.system.AlarmException;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.InfoException;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import tool.dao.util.ObjectProperty;
import tool.workflow.service.FlowCaseService;
import tool.workflow.service.impl.FlowCaseServiceImpl;

public class WydAH extends ActionHandler {
	
	//private Logger logger = Logger.getLogger("wyc.logger");
	public WydAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType ="whatyoudo";
		this._moduleId="basic";
		// TODO Auto-generated constructor stub
	}
	
	BizObject _message = new BizObject("message");
	/**
	 * 一个ActionHandler要初始化运行的代码就放在这个方法中（供子类用）
	 * 
	 * 注意，如果你重载了这个方法，除非调用 super.initial() 这个方法里的语句将不会运行
	 * 
	 * @param bizName
	 * @return
	 * @throws SQLException 
	 */
	protected void initial()  {}
	
	
	public void listWYD() throws SQLException{
		BizObject b = new BizObject("re_general");
		b.setID("wydconfig");
		//b.set("aid", "wydconfig");
		b.refresh();
		
		String ajaxconfig=b.getString("value");
		BizObject config =this.getConfigBiz(ajaxconfig);
		
		
		String type=config.getString("wydtype");
		//this.setAttribute("wydtype", type);
		String usertype=config.getString("wydusertype");
		//this.setAttribute("wydusertype", usertype);
		
		String sort=config.getString("sort");
		String startdate=config.getString("startdate");
		String enddate=config.getString("enddate");
		String page=this.getParameter("page");


		this.setAttribute("wydList",Whatyoudo.getPlmWyd(_curuser.getId(),type,usertype,sort,startdate,enddate,this.preparePageVar(8)));
		this._nextUrl = "/template/basic/wydList.jsp";
				
	}	
	public void addWYD() throws SQLException{
		String type=this.getParameter("wydtype");
		this.setAttribute("wydtype", type);
		String usertype=this.getParameter("wydusertype");
		this.setAttribute("wydusertype", usertype);
		String page=this.getParameter("page");
		String a="ad950a68-d25d-42fa-8811-4ee7842352f2";
		Whatyoudo.addPlmWyd(a, a, a, a, a, "做了什么事", "project");

		

		//this.setAttribute("wydList",Whatyoudo.getPlmWyd(_curuser.getId(),type,usertype,this.preparePageVar(8)));
		this._nextUrl = "/template/basic/wydList.jsp";
				
	}
	
	
	private BizObject getConfigBiz(String ajax){
		String ajaxconfig=ajax;
		if(ajaxconfig.equals(""))
			ajaxconfig="{wydconfig}";
		logger.info(ajaxconfig);

		BizObject config = new BizObject();
		try {
			
			JSONObject jsonObj = new JSONObject(ajaxconfig).getJSONObject("wydconfig");;
			
			Iterator it=jsonObj.keys();

			while (it.hasNext()){
				String key = it.next().toString();
			//	logger.
				config.set(key, jsonObj.get(key));
			}
			System.out.println("config is "+config);
			logger.info("config is "+config);
			//av.add(b);
			//return config;
			
		} catch (JSONException e) {
			
			logger.error(ajax+"   ",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return config;

	}
	public void config() throws SQLException{
		BizObject b = new BizObject("re_general");
		b.setID("wydconfig");
		//b.set("aid", "wydconfig");
		b.refresh();
		
		String ajaxconfig=b.getString("value");
		BizObject config =this.getConfigBiz(ajaxconfig);
		this.setAttribute("obj", config);
		
		this._nextUrl="/template/basic/wyd_config.jsp";
		

	}
	/**
	 * 关键字
	 * @throws SQLException
	 */
	public void saveConfig() throws SQLException{
		
		BizObject config=new BizObject("wydconfig");
		config.set("wydtype", this.getParameter("wydtype"));
		config.set("wydusertype", this.getParameter("wydusertype"));
		config.set("sort", this.getParameter("sort"));
		config.set("startdate", this.getParameter("startdate"));
		config.set("enddate", this.getParameter("enddate"));
		
		
		BizObject b = new BizObject("re_general");
		b.setID("wydconfig");
		//b.set("aid", "wydconfig");
		b.set("value", config.toString());
		this.setAttribute("obj", config);
		this.getJdo().addOrUpdate(b);
		
		this._request.setAttribute("nextUrl", "/basic.WydAH.config");
		throw new InfoException("个人动态保存成功");
		
		
	}
	
	
	public static void main(String args[]){
		BizObject config=new BizObject("wydconfig");
		config.set("wydtype", "aaa");
		config.set("wydusertype", "aaa");
		config.set("sort", "aaa");
		config.set("startdate", "aaa");
		config.set("enddate", "aaa");
		List<BizObject> objList = new ArrayList<BizObject>();
		objList.add(config);
		
		System.out.println("config is "+config);
		
//		BizObject b = new BizObject("re_general");
//		b.set("aid", "wydconfig");
//		b.set("value", config.toString());
		
		try {
			JSONObject jsonObj = new JSONObject(config.toString()).getJSONObject("wydconfig");
			BizObject b = new BizObject();
			Iterator it=jsonObj.keys();
			System.out.println("jsonObj is "+jsonObj);
			while (it.hasNext()){
				String key = it.next().toString();
				System.out.println("key is "+key);
				b.set(key, jsonObj.get(key));
			}
			System.out.println("b is "+b);
			//av.add(b);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private   Logger logger = Logger.getLogger(this.getClass());




}
