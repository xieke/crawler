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

/**
 * 
 * @author xieke
 *
 */
public class Message {

	
	final static String TYPE_FRIENDSHIP = "friendship";
	final static String TYPE_TEAM = "team";
	final static String TYPE_SIGNATURE = "status";
	final static String TYPE_BLOG = "blog";
	final static String TYPE_REFERED = "refered";
	public final static String TYPE_TASK = "task";
	public final static String TYPE_SELF = "myself"; // 自己才能看的动态
	
	public final static String TYPE_ALL = "";
	public final static String TYPE_PRODUCT="product";
	public final static String TYPE_PROJECT="project";
	public final static String TYPE_REQUIRE="require";
	public final static String TYPE_PROBLEM="problem";
	public final static String TYPE_BUG="bug";
	
	
	public final static String USER_TYPE_ALL="";
	public final static String USER_TYPE_FQ="fq";
	public final static String USER_TYPE_ZR="zr";
	public final static String USER_TYPE_ZX="zx";
	public final static String USER_TYPE_CY="cy";
	
	private static Logger logger = Logger.getLogger(Message.class);

	
	/**
	 * 取消息数目
	 * @param userid
	 * @param read
	 * @return
	 * @throws SQLException 
	 */
	public static int getMsgCount(String userid,boolean read) throws SQLException{
		BizObject msg = new BizObject("message");
		msg.set("receiver", userid);
		
		if(read)
			msg.set("readed", "0");
		
		msg.set("instate", 0);
		//logger.info("sql is "+sql);
		QueryFactory qf = msg.getQF();
		qf.setOrderBy("createdate desc");
		PageVariable pv = new PageVariable(-1);
		//、、pv.setNpage(page);
		List wv = qf.mquery(msg,pv);
		return wv.size();
		
	}
	
	
	public void send(String sender,String receiver,String title,String content) throws SQLException{
		BizObject b = new BizObject("message");
		b.set("sender", sender);
		b.set("receiver", receiver);
		b.set("content", content);
		b.set("title", title);
		ActionHandler.currentSession().add(b);
		
	}

	/**
	 * 取发件箱
	 * @param mchid
	 * @param page
	 * @param read 只取未读   true   取全部  false   
	 * @return
	 * @throws SQLException
	 */
	public static List<BizObject> getOutbox(String userid,boolean read,PageVariable pv)
			throws SQLException {

		BizObject msg = new BizObject("message");
		msg.set("sender", userid);
		
		if(read)
			msg.set("readed", "0");
		
		msg.set("outstate", 0);
		
		//logger.info("sql is "+sql);
		QueryFactory qf = msg.getQF();
		qf.setOrderBy("createdate desc");
		//PageVariable pv = new PageVariable(15);
		//pv.setNpage(page);
		List wv = qf.mquery(msg,pv);

		return wv;

	}	
	/**
	 * 取收件箱
	 * @param mchid
	 * @param page
	 * @param read 只取未读   true   取全部  false   
	 * @return
	 * @throws SQLException
	 */
	public static List<BizObject> getInbox(String userid,boolean read,PageVariable pv)
			throws SQLException {

		BizObject msg = new BizObject("message");
		msg.set("receiver", userid);
		
		if(read)
			msg.set("readed", "0");
		
		msg.set("instate", 0);
		
		//logger.info("sql is "+sql);
		QueryFactory qf = msg.getQF();
		qf.setOrderBy("createdate desc");
		//PageVariable pv = new PageVariable(15);
		//pv.setNpage(page);
		List wv = qf.mquery(msg,pv);

		return wv;

	}	

	/**
	 * 显示最热关键词
	 * @param mchid
	 * @param page
	 * @return
	 * @throws SQLException
	 */
	public static String getKeyword(String mid)
			throws SQLException {

		BizObject whatyoudo = new BizObject("keyword_re");
		whatyoudo.set("mid", mid);

		//logger.info("sql is "+sql);
		QueryFactory qf = whatyoudo.getQF();
		//qf.setOrderBy("rank desc");
		//PageVariable pv = new PageVariable(15);
		//pv.setNpage(page);
		whatyoudo = qf.getOne(whatyoudo);

		if(whatyoudo!=null)
			return whatyoudo.getString("name");
		else
			return "";

	}	

	/**
	 * 添加一个关键字，如果关键字已存在,增加关键字的热度
	 */
	public static void addKeyword(String mid, String keyword)
			throws SQLException {
		BizObject wyd = new BizObject("keyword");
		
		String keys[]=keyword.split(",");
		
		for(String key:keys){
			wyd.set("name", key);
			
			if(wyd.getQF().getOne(wyd)!=null){
				wyd= wyd.getQF().getOne(wyd);
				wyd.set("rank", wyd.getInt("rank", 0)+1);
				
			}
			ActionHandler.currentSession().addOrUpdate(wyd);//(wyd);
			
		}
		
		BizObject rewyd = new BizObject("keyword_re");
		
		rewyd.set("mid", mid);
		
		
		if(rewyd.getQF().getOne(rewyd)!=null){
			rewyd= wyd.getQF().getOne(wyd);
			//rewyd.set("rank", wyd.getInt("rank", 0)+1);
			rewyd.set("name", keyword);
		}
		ActionHandler.currentSession().addOrUpdate(rewyd);//(wyd);

		
	}	


}