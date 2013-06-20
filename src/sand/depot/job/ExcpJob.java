package sand.depot.job;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sand.depot.tool.system.AlarmException;
import sand.mail.MailServer;
import tool.dao.BizObject;
import tool.dao.DBPool;
import tool.dao.QueryFactory;
import tool.dao.mongodb.MongoDB;

public class ExcpJob extends BaseJob {


	private static Logger logger = Logger.getLogger(ExcpJob.class);
	
	public String run()  {
		
		BizObject excp = new BizObject("exception");
	//	try {
			List<BizObject> v;
			try {
				v = excp.getQF().query();
				for(BizObject e:v){
					String csql=e.getString("csql");
					List<BizObject> ev=null;
					try {
						ev = QueryFactory.executeQuerySQL(csql);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						logger.error("",e1);
						e1.printStackTrace();
						return "wrong sql:"+csql+"   "+e1.getMessage();
						//throw new AlarmException("wrong sql:"+csql+"   "+e1.getMessage());
					}
					
					String users=e.getString("users");
					String userids[] = users.split(",");
					List<BizObject> usersv = new ArrayList();
					for(String userid:userids){
						BizObject user= new BizObject("employee");
						user.setID(userid);
						user.refresh();
						usersv.add(user);			
					}
					if(ev==null)
						return "  sql  query result is null ";
					
					
					String moban=e.getString("moban");
					for(BizObject ed:ev){
						for(BizObject user:usersv){
							MailServer.sendMail(user, "",e.getString("mailtitle"), render(moban,ed));	
						}
						
					}
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				logger.error("",e2);
				e2.printStackTrace();
				return "error "+e2.getMessage();
			}
			

		
	
		return BaseJob.OK;
		// TODO Auto-generated method stub
		
	
		//return false;
	}
	
	private String render(String moban , BizObject b){
		//Render 
		Pattern p = Pattern.compile("<(.*?)>");
		Matcher m = p.matcher(moban);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			//m.
		    System.out.println(m.group(1));
		    logger.info(m.group(1)+"  -》  "+b.getString(m.group(1)));
		    m.appendReplacement(sb, b.getString(m.group(1)));//="aaaaaa";//		    m.re
		}
		
		 
		
		m.appendTail(sb);
		logger.info("after render :"+sb.toString());
		return sb.toString();
	} 
	private void insertNewlog(int no){

		BizObject log=new BizObject("joblog");

		if (BaseJob.currentUser() != null) {
			log.set("username", this.currentUser().getString("username"));
			log.set("userid", currentUser().getId());
		}

		String jobid=Calendar.getInstance().get(Calendar.MINUTE)+"  "+no;
		log.set("jobid", jobid);
//		log.set("servername", _request.getServerName()); // 服务器名
//		log.set("USEMETHOD", this._curMethod);
//		log.set("MESSAGE", this._tipInfo);
		log.set("usetype", 0);
//		// 如果调用时间大于1秒，或者时不可控异常
//		if ((usetime > 50 || _log.getInt("usetype", 0) == 2)) {
		MongoDB mdb = new MongoDB("joblog");
			if (mdb == null)
				mdb = new MongoDB("joblog");
			mdb.insert(log);
			logger.info("insert job log "+jobid);
		}
	public static void main(String args[]){
//		String s = "adfadf$<YLPY-F>adfadf<XLPY-F>$<PY-3>adfdaf<RHP-6>adfadf$<SDY-1>dafadf<LXBY-6><ZBL-P810>";
//		Pattern p = Pattern.compile("$<(.*?)>");
//		Matcher m = p.matcher(s);
//		StringBuffer sb = new StringBuffer();
//		while (m.find()) {
//			//m.
//		    System.out.println(m.group(1));
//		    m.appendReplacement(sb, "abcdefg");//="aaaaaa";//		    m.re
//		}
//		
//		 
//		
//		m.appendTail(sb);
//		 System.out.println("调用 m.appendTail(sb) 后 sb 的最终内容是 :"+ 
//					sb.toString());
	String moban= "请注意： 问题通报 <topic> 已经超期 <delay> 天"; 
		Pattern p = Pattern.compile("<(.*?)>");
		Matcher m = p.matcher(moban);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			//m.
		    System.out.println(m.group(1));
		   // logger.info(m.group(1)+"  -》  "+b.getString(m.group(1)));
		    m.appendReplacement(sb,m.group(1)+"***");//="aaaaaa";//		    m.re
		}
		
		 
		
		m.appendTail(sb);
		System.out.println("after render :"+sb.toString());
	}
	
	 /**
	  * @param args
	  * @return 
	  * 4
	  * 1240
	  * 124067
	  */
	 public static void main1(String[] args) {
	  String string="1234567";
	  Matcher matcher = Pattern.compile("3(4)5").matcher(string);
	  if(matcher.find()){
	   System.out.println(matcher.group(1));
	   StringBuffer sb = new StringBuffer();
	   matcher.appendReplacement(sb, matcher.group(1)+"0");  //替换的是整个group()
	   matcher.appendReplacement(sb, "$0"+"$1"+"_recycle/");

	   System.out.println(sb);
	   matcher.appendTail(sb);
	   System.out.println(sb.toString());
	   
	  }
	 
	}	
}



