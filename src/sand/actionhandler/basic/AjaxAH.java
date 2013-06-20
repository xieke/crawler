package sand.actionhandler.basic;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import sand.actionhandler.system.ActionHandler;

import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.depot.tool.system.SystemKit;
import sand.mail.MailServer;
import sand.mail.ReceiveMail;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;
import tool.handlestring.StringFormat;


@AccessControl("session")
public class AjaxAH extends ActionHandler {

	static Logger logger = Logger.getLogger(AjaxAH.class);
	static int i = 0;
	
	static	int orderMonth = 0;       
	static	int orderDay = 0;         
	static	int orderDelay = 0;       
	static	int orderNumMonth = 0;    
	static	int orderNumDay = 0;      
	static	int orderNumDelay = 0;    
	                            
	static	int planMonth = 0;        
	static	int waixieMonth = 0;      
	static	int unPlanOrder = 0;      
	static	int planDay = 0;          
	static	int waixieDay = 0;        
	static	int buyapply = 0;         
	static	int mrpruntime = 0;     
	static  int excepmanu = 0;
	                            
	static	int pactmonth = 0;        
		                          
	static	int pactday = 0;          
	/**                       
	static	 * 此方法太慢,暂时注?     
	static	 
	*/                       
	static	int unapply =0;// Pac     
	/**                       
	static	 * 此方法太慢,暂时注?     
	static	 
	*/                       
	static	int delayPact = 0;        
		                          
	static	int Urunapply = 0;        
	static	int iqcBack = 6; // i     
	                          
	static	int manuMonth = 0;        
	static	int manuDay = 0;          
	static	int manuDelay = 6; //     
	static	int numsMonth = 60;//     
	static	int numsDay = 30;// ?     
	static	int numsDelay = 30;//     
	static	int numsCompleteMonth =0 ;
	static	int numsCompleteDay = 0 ; 
	static	int manuOnline = 10;      
	static	int numsOnline = 20;      
	static	int zhitongMonth = 30;    
	static	int zhitongDay = 20;      
	
	static long stamp=0;
	/**
	 * 构造方法
	 * 
	 * @param p_Req
	 * @param p_Res
	 */

	public AjaxAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "MODULE"; // 随便写一个了，没有还不行
		this._moduleId = "system"; // 本模块暂时未定
	}

	/**
	 * 取本周内新增紅頭文件數量
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getAnnex(String name) throws SQLException {
		BizObject annex = new BizObject("annex");
		annex.set("bizId", name);
		//annex.set("isvip", 1);暂时去除此限定条件
		
		// annex.setOrderBy("modifydate desc");
		Calendar c = Calendar.getInstance();
		c.roll(Calendar.WEEK_OF_YEAR, -1); // 查询一周内的新增部门问题报告
		// logger.info("c is " + c.getTime().toLocaleString());
		
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);
		
		annex.setMinValue("modifydate", c.getTime());

		return new QueryFactory(annex).mquery(annex).size();
	}

	/**
	 * 返回 erp 首页所需要的ajax数据
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 */
	@Ajax
	public String getErpHomeData() throws ServletException, SQLException {return "";}

	/**
	 * 返回 mis 首页所需要的ajax数据
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 */
	@Ajax
	public String getMisHomeLeftData() throws ServletException, SQLException {return "";}

	@Ajax
	public String getAdminCnt() {
		String json = "{auditamt:'"+this._request.getSession().getAttribute("auditamt")+"',audithamt:'"+this._request.getSession().getAttribute("audithamt")+"'" +
				",utaskdisposalamt:'"+this._request.getSession().getAttribute("utaskdisposalamt")+"',dtaskdisposalamt:'"+this._request.getSession().getAttribute("dtaskdisposalamt")+"'}";
		logger.info("ajax is "+json);
		return json;
	}
	
	
	/**
	 * 取郵件列表
	 */
	public void listMail() {

//		List v =MailServer.receive(); 
//		logger.info("recieved mail  "+v);
//		this.setAttribute("objList", SystemKit.page(v
//				, this.preparePageVar()));
//		this._nextUrl = "/basic/mailList.jsp";
//		if (v!=null)
			this._nextUrl = this._nextUrl+"?isbound=1";
			this._nextUrl="/success.jsp";
			
	}

	private List getList(String objtype) throws SQLException {

		BizObject dptpbm = new BizObject(objtype);
		QueryFactory rsQF = new QueryFactory(dptpbm);
		rsQF.setOrderBy("modifydate desc");
		if (objtype.equals("DEPTPROBLEM"))
			rsQF.setHardcoreFilter("type is null");
		Calendar c = Calendar.getInstance();
		c.roll(Calendar.WEEK_OF_YEAR, -1); // 查询一周内的新增问题报告
		c.set(Calendar.MINUTE, 0); //防止太多次刷新缓存
		// logger.info("c is " + c.getTime().toLocaleString());
		dptpbm.setMinValue("modifydate", c.getTime());

		List v = (List) rsQF.query(dptpbm);
		// logger.info("sql is "+rsQF.getSql());
		return v;

	}

	public String getJsonList(String objtype) throws SQLException {

		List v = getList(objtype);
		// logger.info("sql is " + rsQF.getSql());

		// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		String xml = constructJson(v);

		// logger.info("xml is " + xml); // 纪录日志

		return htmEncode(xml);

	}

	/**
	 * 質量問題ajax列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Ajax
	public String getQualityProblemList() throws SQLException {
		if (1==1) return null;
		return getJsonList("QUALITYCOMMUNICATIONS");
	}

	/**
	 * 体系文件ajax列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Ajax
	public String getAnnexList() throws SQLException {
		return getJsonList("annex");
	}

	/**
	 * ajax方法，返回最近一周的部門問題
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 * @throws IOException
	 */

	@Ajax
	public String getDeptProblemList() throws ServletException, SQLException,
			IOException {

		if (1==1) return null;
		List<BizObject> v = getList("DEPTPROBLEM");
		for (BizObject b : v) {
			//logger.info("getdeptproblemlist "+b);
			
			b.set("deptid", b.getBizObj("deptid").getString("deptname"));
		}
		String xml = constructJson(v);
		// logger.info("xml is " + xml); // 纪录日志

		return htmEncode(xml);
		// return getJsonList("DEPTPROBLEM");

	}

	/**
	 * ajax方法，返回最近一周的新闻
	 * 
	 * @throws ServletException
	 * @throws SQLException
	 * @throws IOException
	 */

	@Ajax
	public String getNewsList() throws ServletException, SQLException,
			IOException {
		
		BizObject annex = new BizObject("annex");
		String[] bizid = { "新闻", "通知", "红头文件", "规章制度", "会议纪要" };
		annex.set("bizid", bizid);
		annex.set("isvip", 1);
		List<BizObject> v = new QueryFactory("annex").mquery(annex);
		// logger.info("sql is "+);
		List<BizObject> v2 = new ArrayList();
		for (BizObject b : v) {
			BizObject b2 = new BizObject("annex");

			b2.set("id", b.getId());
			b2.set("title", b.getString("title"));
			b2.set("bizid", b.getString("bizid"));
			v2.add(b2);
		}
		// logger.info("sql is " + rsQF.getSql());

		// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		String xml = constructJson(v2);

		// logger.info("xml is " + xml); // 纪录日志

		return htmEncode(xml);

	}

	/**
	 * html 编码
	 * 
	 * @param s
	 * @return
	 */
	String htmEncode(String s) {
		StringBuffer stringbuffer = new StringBuffer();
		int j = s.length();
		for (int i = 0; i < j; i++) {
			char c = s.charAt(i);
			switch (c) {
			case 60:
				stringbuffer.append("&lt;");
				break;
			case 62:
				stringbuffer.append("&gt;");
				break;
			case 38:
				stringbuffer.append("&amp;");
				break;
			case 34:
				stringbuffer.append("&quot;");
				break;
			case 169:
				stringbuffer.append("&copy;");
				break;
			case 174:
				stringbuffer.append("&reg;");
				break;
			case 165:
				stringbuffer.append("&yen;");
				break;
			case 8364:
				stringbuffer.append("&euro;");
				break;
			case 8482:
				stringbuffer.append("&#153;");
				break;
			case 13:
				if (i < j - 1 && s.charAt(i + 1) == 10) {
					stringbuffer.append("<br>");
					i++;
				}
				break;
			case 32:
				if (i < j - 1 && s.charAt(i + 1) == ' ') {
					stringbuffer.append(" &nbsp;");
					i++;
					break;
				}
			default:
				stringbuffer.append(c);
				break;
			}
		}
		return new String(stringbuffer.toString());
	}

	/**
	 * 根據 BizObject List 組裝 json
	 * 
	 * @param v
	 * @return
	 */
	public static String constructJsonOld(List<BizObject> v) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <root>";

		for (BizObject biz : v) {

			// System.out.println(biz);
			if (biz != null) {

				xml += "<" + biz.getName() + ">";
				for (String column : biz.getColumns()) {
					xml += "<" + column + ">" + biz.getString(column) + "</"
							+ column + ">";
				}
				xml += "</" + biz.getName() + ">";

			}
		}

		xml += "</root>";
		return xml;
	}

	/**
	 * 根據 BizObject List 組裝 json
	 * 
	 * @param v
	 * @return
	 */
	public static String constructXml(List<BizObject> v) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <root>";

		for (BizObject biz : v) {

			// System.out.println(biz);
			if (biz != null) {

				xml += "<" + biz.getName() + ">";
				for (String column : biz.getColumns()) {
					xml += "<" + column + ">" + biz.getString(column) + "</"
							+ column + ">";
				}
				xml += "</" + biz.getName() + ">";

			}
		}

		xml += "</root>";
		return xml;
	}

	public static String constructJson(List<BizObject> v) {

		if (v == null || v.size() == 0)
			return "{}";
		BizObject biz = v.get(0);
		// StringBuilder s = new StringBuilder("{"+biz.getName()+":[");
		StringBuilder s = new StringBuilder("");
		for (int i = 0; i < v.size(); i++) {
			BizObject bizO = v.get(i);
			s.append("{");
			for (int j = 0; j < bizO.getColumns().length; j++) {
				String column = bizO.getColumns()[j];
				s.append(column + ":'").append(StringFormat.toHTMLString(bizO.getString(column))).append(
						"'");
				if (j != bizO.getColumns().length - 1)
					s.append(",");

			}
			s.append("}");
			//s.append("{id:'").append(bizO.get("id")).append("',value:'").append
			// (bizO.get("name")).append("'}");
			if (i != v.size() - 1)
				s.append(",");
		}
		// s.append("]}");

		return s.toString();
	}
	
	/**
	 * 页面层显示详细内容
	 * @return
	 * @throws SQLException
	 */
	@Ajax
	public String showProMes() throws SQLException{
		String type = this.getParameter("type");
		String id = this.getParameter("id");
		String tablename = this.getParameter("tname");

		QueryFactory QF= new QueryFactory(tablename);
		BizObject biz = QF.getByID(id);
		
		String message = "";
		
		if(type.equals("method")){
			message += "<B>处理过程为：  </B><BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+biz.getString("method");
		}else if (type.equals("project")){
			message += "<B>解决方案为：  </B><BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+biz.getString("project");
		}else if(type.equals("end")){
			message += "<B>解决方案汇总为：  </B><BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+biz.getString("ENDCONTENT");
		}

		return message;
	}
	
	/**
	 * 获得产品出库的所有产品
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 */
	@Ajax
	public String getProductexData() throws ServletException, SQLException {
		String pl=this.getParameter("productserial$PL","");
		String sn=this.getParameter("productserial$SN","");
		if(!pl.equals("")||!sn.equals("")){
			String sql="select * from productserial where state='6' ";
			if(!pl.equals(""))
				sql+="and pl='"+pl+"' ";
			if(!sn.equals(""))
				sql+="and sn='"+sn+"' ";
			List<BizObject> v=QueryFactory.executeQuerySQL(sql);

			List<BizObject> v2 = new ArrayList();
			for (BizObject b : v) {
				BizObject b2 = new BizObject("productserial");
				b2.set("pl", b.getString("pl"));
				b2.set("sn", b.getString("sn"));
				b2.set("id", b.getId());
				v2.add(b2);
			}
		// logger.info("sql is " + rsQF.getSql());

		// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			String xml = constructJson(v2);

		//	logger.info("xml is " + xml); // 纪录日志			
			return htmEncode(xml);
		}
		
		return "{}";
	}
	

	public static void main(String args[]) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar
				.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar
				.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar
				.getActualMinimum(Calendar.SECOND));

		System.out.println(calendar.getTime().toGMTString());
	}
}
