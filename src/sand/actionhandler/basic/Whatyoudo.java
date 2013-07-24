package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.depot.business.oa.Job;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;
import tool.dao.JDO;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

// 哦耶~~~任何人可用的
public class Whatyoudo {

	//建议这些动态类型和工作流的单据类型一样的,用表名,如果不是表名也希望是一样的类型名
	final static String TYPE_FRIENDSHIP = "friendship";
	final static String TYPE_TEAM = "team";
	final static String TYPE_SIGNATURE = "status";
	final static String TYPE_BLOG = "blog";
	final static String TYPE_REFERED = "refered";
	public final static String TYPE_TASK = "testtask_qa";
	public final static String TYPE_SELF = "myself"; // 自己才能看的动态
	
	public final static String TYPE_ALL = "";
	public final static String TYPE_PRODUCT="product_pt";
	public final static String TYPE_PROJECT="project_pj";
	public final static String TYPE_REQUIRE="require_rq";
	public final static String TYPE_PROBLEM="problem";
	public final static String TYPE_BUG="bug";
	public final static String TYPE_TEST="test_qa";
	
	
	public final static String USER_TYPE_ALL="";
	public final static String USER_TYPE_FQ="fq";
	public final static String USER_TYPE_ZR="zr";
	public final static String USER_TYPE_ZX="zx";
	public final static String USER_TYPE_CY="cy";
	
	private static Logger logger = Logger.getLogger(Whatyoudo.class);

	/**
	 * 显示我的动态
	 * @param mchid
	 * @param page
	 * @return
	 * @throws SQLException
	 */
	public static List<BizObject> getMywyd(String[] mchid,String type,PageVariable page)
			throws SQLException {

		BizObject whatyoudo = new BizObject("whatyoudo");
		whatyoudo.set("mid", mchid);
		if(StringUtils.isNotBlank(type))
			whatyoudo.set("type", type);

		//logger.info("sql is "+sql);
		QueryFactory qf = whatyoudo.getQF();
		qf.setOrderBy("createdate desc");
		if(page!=null)
			return qf.query(whatyoudo,page );
		else return qf.query(whatyoudo);

	}	
	public static List<BizObject> getMywyd(String userid, String type,int page)
			throws SQLException {
		// String type = this.getParameter("type");
		// String userid=this._curuser.getId();
		BizObject refriend = new BizObject("re_friend");
		refriend.set("userid", userid);
		refriend.set("state", 1);
		List<BizObject> v = refriend.getQF().query(refriend);
		// List result=new ArrayList();
		// String[] users=new String[v.size()+1];
		String users = "''";
		// users[0]=ActionHandler.currentUser().getEmployee().getId();
		// int i=1;
		for (BizObject b : v) {
			users = users + ",'" + b.getString("friendid") + "'";
		}
		BizObject whatyoudo = new BizObject("whatyoudo");
		// whatyoudo.set("userid", users);
		// if(type.equals(""))
		// type=null;
		// logger.info("type is "+type);
		// whatyoudo.set("type", type);
		// whatyoudo.setOrderBy("modifydate desc");
		BizObject reteam = new BizObject("re_team_user");
		reteam.set("userid", userid);
		List<BizObject> tv = reteam.getQF().query(reteam);
		String teams = "''";// new String[tv.size()];

		for (BizObject b : tv) {
			teams = teams + ",'" + b.getString("teamid") + "'";
		}

		String sql = "select  * from basic.whatyoudo where ( userid in("
				+ users
				+ ") or teamid in ("
				+ teams
				+ ") ) and type <> 'myself' and type like '%"+type+"' or (userid='"
				+ ActionHandler.currentUser().getEmployee().getId()
				+ "' and type='myself' and type like '%"+type+"') order by modifydate desc";
		logger.info("sql is "+sql);
		
		PageVariable pv = new PageVariable(15);
		pv.setNpage(page);

		
		List wv = whatyoudo.getQF().executeQuerySQL(sql, pv);
		// logger.info(message)

		return wv;
		// this.setAttribute("objList", wv.addAll(wvt));

	}

	/**
	 * 
	 * @param userid
	 * @param type
	 * @param usertype
	 * @param page
	 * @return
	 * @throws SQLException
	 */
	public static List<BizObject> getPlmWyd(String userid, String type,String usertype,String sort,String startdate,String enddate,PageVariable page)
			throws SQLException {
		// String type = this.getParameter("type");
		// String userid=this._curuser.getId();
		
		String cysql="cyids like +'%"+userid+"%'";
		String zrsql="zrid like +'%"+userid+"%'";
		String zxsql="zxid like +'%"+userid+"%'";
		String fqsql="fqid like +'%"+userid+"%'";
		
		String usersql="";
		if(usertype.equals(USER_TYPE_CY)){
			usersql=cysql;
		}
		if(usertype.equals(USER_TYPE_ZR)){
			usersql=zrsql;
		}		
		if(usertype.equals(USER_TYPE_ZX)){
			usersql=zxsql;
		}		
		if(usertype.equals(USER_TYPE_FQ)){
			usersql=fqsql;
		}		
		if(usertype.equals(USER_TYPE_ALL)){
			usersql=cysql+" or "+zrsql+" or "+zxsql+" or "+fqsql;
		}		
		
		
		String typesql = " and type like'%"+type+"'";
		
		String datesql="";
		if(!startdate.equals("")){
			datesql=" and createdate >= '"+startdate+"'";
		}
		if(!enddate.equals("")){
			datesql=" and createdate <= '"+enddate+"'";
		}
		String order=" createdate desc";
		
		if(!sort.equals("")){
			order = sort;
		}
		
		
		
		BizObject whatyoudo = new BizObject("whatyoudo");

		String sql = "select  * from basic.whatyoudo where ("+usersql+")"+typesql+datesql+" order by "+order;
		logger.info("sql is "+sql);
		
//		PageVariable pv = new PageVariable(15);
//		pv.setNpage(page);

		
		List wv = whatyoudo.getQF().executeQuerySQL(sql, page);
		
		//List wv = whatyoudo.getQF().executeQuerySQL(sql, pv);
		// logger.info(message)

		return wv;
		// this.setAttribute("objList", wv.addAll(wvt));

	}
	
	/**
	 * 显示某个好友动态
	 * 
	 * @throws SQLException
	 */
	public static List<BizObject> getOnewyd(String userid, String type)
			throws SQLException {
		// String userid=this.getParameter("userid");
		BizObject b = new BizObject("whatyoudo");
		// b.set("type", this.getParameter("type"));
		b.set("userid", userid);
		if(!"".equals(type))
			b.set("type", type);
		b.setOrderBy("modifydate desc");
		QueryFactory qf = b.getQF();
		qf.setHardcoreFilter(" type!='"+Whatyoudo.TYPE_SELF+"'");
		
		List v = qf.query(b, new PageVariable(15));
		logger.info("sql is "+b.getQF().getSql());
		return v;
		// this.setAttribute("objList", v);
	}

	/**
	 * <p>
	 * Title: 发送系统信息
	 * </p>
	 * <p>
	 * Parameter: 用户id,信息内容,信息链接
	 * </p>
	 */
	public static boolean send_msg_to(String userid, String message,
			String url, JDO jdo) throws ServletException, SQLException {
		BizObject biz = new BizObject("SYSTEM_MSG");

		biz.set("userid", userid);
		biz.set("msg", message);
		biz.set("url", url);
		biz.set("posttime", new Date());

		jdo.add(biz);
		return true;
	}
//	
//	public static void addWyd(String mchid, String dowhat, String type)
//			throws SQLException {
//		BizObject wyd = new BizObject("whatyoudo");
//		wyd.set("mid", mchid);
//		wyd.set("dowhat", dowhat);
//		// wyd.set("url", url);
//		wyd.set("type", type);
//		ActionHandler.currentSession().add(wyd);
//	}
//	
	/**
	 * @param faqiid  发起人 id 
	 * @param zrid   责任人 id
	 * @param zxid   执行人 id
	 * @param cyids   参与人id   ,可为多个，逗号隔开
	 * @param mid   业务 id
	 * @param dowhat   
	 * @param type  业务类型 ：   product(产品)  project(项目)  require(需求)  problem(问题)   bug
	 * @throws SQLException
	 */
	public static void addPlmWyd(String fqid, String zrid, String zxid, String cyids, String mid, String dowhat ,String type)
			throws SQLException {
		BizObject wyd = new BizObject("whatyoudo");
		wyd.set("fqid", fqid);
		wyd.set("zrid", zrid);
		wyd.set("zxid", zxid);
		wyd.set("cyids", cyids);
		wyd.set("mid", mid);
		wyd.set("dowhat", dowhat);
		wyd.set("type", type);
		ActionHandler.currentSession().add(wyd);
	}	
	public static void addUserWyd(String userid, String dowhat, String type)
			throws SQLException {
		BizObject wyd = new BizObject("whatyoudo");
		wyd.set("userid", userid);
		wyd.set("dowhat", dowhat);
		// wyd.set("url", url);
		wyd.set("type", type);
		ActionHandler.currentSession().add(wyd);
	}

	public static void addTeamWyd(String teamid, String dowhat, String type)
			throws SQLException {
		BizObject wyd = new BizObject("whatyoudo");
		wyd.set("teamid", teamid);
		wyd.set("dowhat", dowhat);
		// wyd.set("url", url);
		wyd.set("type", type);
		ActionHandler.currentSession().add(wyd);
	}

}