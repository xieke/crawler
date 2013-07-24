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

// 哦耶~~~任何人可用的
public class KeyWord {

	
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
	
	private static Logger logger = Logger.getLogger(KeyWord.class);

	
	
	/**
	 * 显示最热关键词
	 * @param mchid
	 * @param page
	 * @return
	 * @throws SQLException
	 */
	public static List<BizObject> getHotest(String type)
			throws SQLException {

		BizObject whatyoudo = new BizObject("keyword");
		
		if(!type.equals(""))
			whatyoudo.set("type", type);

		//logger.info("sql is "+sql);
		QueryFactory qf = whatyoudo.getQF();
		qf.setOrderBy("rank desc");
		PageVariable pv = new PageVariable(15);
		//pv.setNpage(page);
		List wv = qf.query(whatyoudo,pv );

		return wv;

	}	

	/**
	 * 增加热度
	 * @param keyword
	 * @throws SQLException 
	 */
	public static void addRank(String key) throws SQLException{
		BizObject wyd = new BizObject("keyword");
		if(key.equals("")) return;
		wyd.set("name", key);
		
		if(wyd.getQF().getOne(wyd)!=null){
			wyd= wyd.getQF().getOne(wyd);
			wyd.set("rank", wyd.getInt("rank", 0)+1);
			
		}
		ActionHandler.currentSession().addOrUpdate(wyd);//(wyd);
	
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
			return whatyoudo.getString("name")+",";
		else
			return "";

	}	

	/**
	 * 添加一个关键字，如果关键字已存在,增加关键字的热度
	 */
	public static void addKeyword(String mid, String keyword)
			throws SQLException {
		BizObject wyd = new BizObject("keyword");
		BizObject rewyd = new BizObject("keyword_re");
		
		rewyd.set("mid", mid);
		
		
		String keys[]=keyword.split(",");
		
		for(String key:keys){
			logger.info("key is "+key);
			wyd.clear();
			if(key.equals("")) continue;
			wyd.set("name", key);
			
			if(wyd.getQF().getOne(wyd)!=null){
				wyd= wyd.getQF().getOne(wyd);
				wyd.set("rank", wyd.getInt("rank", 0)+1);
				
			}
			
			ActionHandler.currentSession().addOrUpdate(wyd);//(wyd);
			
		}
		
		
		if(rewyd.getQF().getOne(rewyd)!=null){
			
			rewyd= rewyd.getQF().getOne(rewyd);
			//rewyd.set("rank", wyd.getInt("rank", 0)+1);
			rewyd.set("name", keyword);
		}
		ActionHandler.currentSession().addOrUpdate(rewyd);//(wyd);

		
	}	


}