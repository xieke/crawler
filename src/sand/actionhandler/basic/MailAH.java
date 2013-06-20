package sand.actionhandler.basic;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import org.json.JSONException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.TokenCheck;
import sand.depot.business.document.Document;
import sand.depot.job.MailConfig;

import sand.depot.tool.system.BillNoGenerator;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.InfoException;
import sand.depot.tool.system.SystemKit;
import sand.mail.MES;
import sand.mail.MailServer;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import tool.dao.mongodb.MongoDB;
import tool.handlestring.StringFormat;

@AccessControl("no")
public class MailAH extends ActionHandler {

	static Logger logger = Logger.getLogger(MailAH.class);

	public MailAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}

	public void listMail() {

		logger.info("user is " + ActionHandler.currentUser());
		BizObject mail = this.getBizObjectFromMap("email");
		if (mail == null)
			mail = new BizObject("email");

		mail.setOrderBy("senddate desc");

		MongoDB mqf = new MongoDB(MongoDB.T_MAIL);
		// logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+erplog.getOrderBy());
	//	List v = mqf.mfind(mail, this.preparePageVar());
	//	this.setAttribute("objList", v);

		this._nextUrl = "/oa/mail/mailList.jsp";
	}

	public void delete() {
		MongoDB mdb = new MongoDB(MongoDB.T_MAIL);
		DBObject obj = mdb.findOne(_objId);
		if (obj != null)
			mdb.remove(obj);
		listMail();

	}

	public void deleteAll() {
		MongoDB mdb = new MongoDB(MongoDB.T_MAIL);
		mdb.removeAll();
		listMail();

	}

	// @TokenCheck
	public void saveMail() throws SQLException {

		BizObject mail = this.getBizObjectFromMap("email");
		logger.info("_objId is " + this.getParameter("objId"));

		logger.info("_objId is " + _objId);
		if (!_objId.equals(""))
			mail.put("_id", new ObjectId(_objId));
		// logger.info("mail is "+mail);
		this.saveMail(mail);
		this.showMail(mail.getString("_id"));
	}

	// @TokenCheck
	private void saveMail(BizObject mail) throws SQLException {

		// BizObject mail = this.getBizObjectFromMap("email");
		MongoDB mdb = new MongoDB(MongoDB.T_MAIL);
		if (mail.getString("mailno").equals("")) {
			mail.set("mailno", BillNoGenerator.getBillNo("SANDMAIL"));
		}

		mdb.save(mail);
		List<File> files = this.getUploadFiles();
		logger.info("add annex :" + files.size());
		for (File file : files) {
			BizObject annex = new BizObject("annex");
			annex.set("bizid", _objId);
			Document.updateAnnex(annex, file);
			logger.info("add annex :" + annex);
		}

	}

	// @TokenCheck
	public void sendMail() throws SQLException {

		BizObject mail = this.getBizObjectFromMap("email");
		this.saveMail(mail);

		if (mail.getString("sent").equals(""))
			MES.createMail(mail);
		else
			throw new ControllableException("该邮件已被发送");

		this.listMail();
	}
	
	/**
	 * 海航专用发送邮件
	 */
	public static boolean sendMail(BizObject email){


		String address = email.getString("toaddr");
		int i=address.indexOf("@");
		if(i<=0)
			throw new ControllableException("错误的邮件地址");
		
		String sfix = address.substring(i+1);
		logger.info("sfix is "+sfix);

		boolean result = false;
	//	if(mailExcludes.contains(sfix)){
			result=MailServer.sendMailSyn(email);
//		}
//		else
//			result=MailServer.sendOutMailSyn(email);
			
		return result;
	
	}
	
	public void test2(){
		BizObject email = new BizObject("email");

		String address=this.getParameter("email");
		email.set("toaddr", address);
		
		email.set("fromaddr","tarzon@21cn.com");
		MailAH.sendMail(email);
	}

	public void test() {
		BizObject email = new BizObject("email");

		String address=this.getParameter("email");
		email.set("toaddr", address);
		
		int i=address.indexOf("@");
		if(i<=0)
			throw new ControllableException("错误的邮件地址");
		
		String sfix = address.substring(i+1);
		logger.info("sfix is "+sfix);
		// email.set("fromaddr", "xieke3@hnair.com");

		email.set(
				"content",
				"会员中心注册 <a href='http://116.236.224.52:7989/basic.LoginAH.active?activecode=111111'>激活账号</a>");
		email.set("subject", "comeon 操！！！！");

		boolean result = false;
	//	if(mailExcludes.contains(sfix)){
			result=MailServer.sendMailSyn(email);
			
			MailConfig.readTxtFile("/root/crawler/src/maillist");
		//}
//		else
//			result=MailServer.sendOutMailSyn(email);
//			
		if (result)
			throw new InfoException("发送成功");
		else
			throw new InfoException("发送失败");
	}

	// public void exbill() throws SQLException, IOException{
	//
	// //String arg = this.getParameter("arg");
	//
	// String
	// args[]={"employee","department","storehouse","materialclass","material"};
	//
	// BizObject dbd = new BizObject("exbill");
	// dbd.setID("LL11053000014");
	// dbd.refresh();
	// List<BizObject> v = dbd.getList("exbilldetail");
	// //SystemKit.getCacheParamById("storehouse",dbd.getString("storehouseid"))
	// dbd.set("storehouseid", dbd.getBizObj("storehouseid").getString("code"));
	// //dbd.set("storehouseid2",
	// dbd.getBizObj("storehouseid2").getString("code"));
	// //dbd.set("intype", "调拨");
	// dbd.set("outtype", "出库");
	// dbd.set("deptcode", "1234");
	// //dbd.set("outdept", "2341");
	// dbd.set("checker", "xieke");
	// dbd.set("projectname","某某项目");
	// dbd.set("projectcode","1234");
	// dbd.set("manubatchno","TS11243444");
	//
	// for(BizObject b:v){
	// b.set("materialid", b.getBizObj("materialid").getString("coding"));
	// }
	// Document.writeBizObjXml("exbill", dbd,v);
	// }

	// public void basic() throws SQLException{
	//
	// this.material();
	// this.employee();
	// this.department();
	//
	// }

	/**
	 * 物料用友
	 * 
	 * @throws SQLException
	 */
	// public void material() throws SQLException{
	//
	// String args[]={"material"};
	//
	// for(String s:args){
	// BizObject b = new BizObject(s);
	// List<BizObject> v = b.getQF().query();
	// for(BizObject obj:v){
	// if(obj.getString("coding").equals(""))
	// continue;
	// String etype = SystemKit.getParamById("unit", obj.getString("unitid"));
	// obj.set("unitid", etype);
	// obj.set("spec", StringFormat.toHTMLString(obj.getString("spec")));
	// obj.set("name", StringFormat.toHTMLString(obj.getString("name")));
	// obj.set("partdesc",
	// StringFormat.toHTMLString(obj.getString("partdesc")));
	// obj.set("description",
	// StringFormat.toHTMLString(obj.getString("description")));
	// obj.set("symbol", StringFormat.toHTMLString(obj.getString("symbol")));
	// Document.writeBizObjXml(s, obj);
	// }
	// }
	//
	// }

	/**
	 * 用户用友
	 * 
	 * @throws SQLException
	 */
	// public void employee() throws SQLException{
	//
	// String args[]={"employee"};
	//
	// for(String s:args){
	// BizObject b = new BizObject(s);
	// List<BizObject> v = b.getQF().query();
	// for(BizObject obj:v){
	// if(obj.getString("scrap").equals("0"))
	// continue;
	// else{
	// int len = obj.getString("deptid").length();
	// if (len >12)
	// len=12;
	// obj.set("deptcode", obj.getString("deptid").substring(0,len));
	// Document.writeBizObjXml(s, obj);
	// }
	//
	// }
	// }
	//
	// }

	/**
	 * 物料用友
	 * 
	 * @throws SQLException
	 */
	// public void department() throws SQLException{
	//
	// String args[]={"department"};
	//
	// for(String s:args){
	// BizObject b = new BizObject(s);
	// List<BizObject> v = b.getQF().query();
	// for(BizObject obj:v){
	// if(obj.getString("scrap").equals("0"))
	// continue;
	// else{
	// //obj.set("deptcode", obj.getString("deptid"));
	// Document.writeBizObjXml(s, obj);
	// }
	//
	// }
	// }
	//
	// }
	/**
	 * 解决用户没有部门的问题
	 * 
	 * @throws SQLException
	 */
	public void useradddept() throws SQLException {

		String args[] = { "employee" };

		for (String s : args) {
			BizObject b = new BizObject(s);
			List<BizObject> v = b.getQF().query();

			for (BizObject obj : v) {

				if (obj.getString("deptid").equals("")) {
					// select * from basic.re_user_dept
					BizObject re = new BizObject("re_user_dept");
					re.set("userid", obj.getId());
					List<BizObject> v2 = re.getQF().query(re);
					if (v2.size() > 0) {
						re = v2.get(0);
						obj.set("deptid", re.getString("deptid"));
						logger.info("update b " + obj);
						this.getJdo().update(obj);
					}
				}

			}
		}

	}

	// public void otherexbill() throws SQLException, IOException{
	//
	// //String arg = this.getParameter("arg");
	//
	// String
	// args[]={"employee","department","storehouse","materialclass","material"};
	//
	// BizObject dbd = new BizObject("exbill");
	// dbd.setID("LL11062100019");
	// dbd.refresh();
	// List<BizObject> v = dbd.getList("exbilldetail");
	// //SystemKit.getCacheParamById("storehouse",dbd.getString("storehouseid"))
	// dbd.set("storehouseid", dbd.getBizObj("storehouseid").getString("code"));
	// //dbd.set("storehouseid2",
	// dbd.getBizObj("storehouseid2").getString("code"));
	// //dbd.set("intype", "调拨");
	// dbd.set("outtype", "材料出库");
	// dbd.set("deptcode", "1234");
	// //dbd.set("outdept", "2341");
	// dbd.set("checker", "xieke");
	// dbd.set("projectname","某某项目");
	// dbd.set("projectcode","1234");
	// dbd.set("manubatchno","TS11243444");
	//
	// for(BizObject b:v){
	// b.set("materialid", b.getBizObj("materialid").getString("coding"));
	// }
	// Document.writeBizObjXml("otherexbill", dbd,v);
	// }

	// public void doexbill() throws SQLException{
	// BizObject exbill = new BizObject("exbill");
	// exbill.set("status", ExbillControl.STATUS_EFFECT);
	// Calendar c = Calendar.getInstance();
	// c.roll(Calendar.MONTH, -1);
	// exbill.setMinValue("createdate", c.getTime());
	// List<BizObject> v = exbill.getQF().query(exbill);
	// for(BizObject b:v){
	//
	// ExbillControl.createXML(b.getId());
	// }
	// //String billno=this.getParameter("billno");
	//
	// }
	// public void diaobo() throws SQLException, IOException{
	//
	// //String arg = this.getParameter("arg");
	//
	// String billno=this.getParameter("billno");
	// String
	// args[]={"employee","department","storehouse","materialclass","material"};
	//
	// BizObject dbd = new BizObject("exbill");
	// dbd.setID(billno);
	// dbd.refresh();
	// List<BizObject> v = dbd.getList("exbilldetail");
	// //SystemKit.getCacheParamById("storehouse",dbd.getString("storehouseid"))
	// dbd.set("storehouseid", dbd.getBizObj("storehouseid").getString("code"));
	// dbd.set("storehouseid2",
	// dbd.getBizObj("storehouseid2").getString("code"));
	// dbd.set("intype", "调拨");
	// dbd.set("outtype", "调拨");
	// dbd.set("indept", "1234");
	// dbd.set("outdept", "2341");
	// dbd.set("checker", "xieke");
	// dbd.set("projectname","某某项目");
	// dbd.set("projectcode","1234");
	// dbd.set("manubatchno","TS11243444");
	//
	// for(BizObject b:v){
	// b.set("materialid", b.getBizObj("materialid").getString("coding"));
	// }
	// Document.writeBizObjXml("diaobo", dbd,v);
	// // for(String arg:args){
	// //
	// // BizObject u = new BizObject(arg);
	// // List<BizObject> l = u.getQF().query(this.preparePageVar());
	// // //logger.info(XmlConstructor.toXml(l, "employee"));
	// //
	// //
	// // for(BizObject b:l){
	// //
	// // }
	// //
	// // }
	// }
	public void quanxian() throws SQLException {

		BizObject employee = new BizObject("employee");
		List<BizObject> v = employee.getQF().query();

		for (BizObject b : v) {
			if (b.getString("scrap").equals("0"))
				continue;
			BizObject re = new BizObject("re_user_role");
			re.set("userid", b.getId());
			re.set("roleid", "service");
			if (re.getQF().query(re).size() == 0)
				this.getJdo().add(re);
		}
	}

	public void makeamount3() throws SQLException {
		// String
		// sql="select batch,inbillamount,amount from manufacture manu where manu.batch like 'TS%' and manu.inbillamount < manu.amount and manu.status in (1,2,3)";
		// String
		// sql="select *  from exbill e where  e.createdate >=to_date('2011-07-09 17:45:00','YYYY-MM-DD HH24:MI:SS') and  e.statusid=2 and e.billno like 'XN%' ";//
		// and e.expatch in (select id from manufacture manu where manu.batch
		// like 'TS%' and manu.inbillamount < manu.amount and manu.status in (1,
		// 2, 3))";
		String sql = "select *  from exbill e where  e.statusid=3 and e.billno like 'XN%'   and e.expatch in (select id from manufacture manu where manu.batch like 'TS%'  and manu.inbillamount < manu.amount and manu.status in (1, 2, 3))";
		Map<String, Map<String, Double>> storehouse = new HashMap();
		Map<String, Double> result = null;
		BizObject exbill = new BizObject("exbill");
		// String sql =
		// "select id,amount, amount-inbillamount as ddd from manufacture where status in (1,2,3) and batch like 'TS%'";
		List<BizObject> v = exbill.getQF().executeQuerySQL(sql);
		logger.info("v size is " + v.size());
		for (BizObject b : v) {
			String houseid = b.getString("storehouseid");
			if (storehouse.get(houseid) == null) {
				result = new HashMap<String, Double>();
				storehouse.put(houseid, result);
			} else
				result = storehouse.get(houseid);

			BizObject exdetail = new BizObject("exbilldetail");
			List<BizObject> list2 = b.getList("exbilldetail");
			logger.info("list2 size " + list2.size());
			for (BizObject b2 : list2) {
				String key = b2.getBizObj("materialid").getString("coding");
				if (result.get(key) == null) {

					logger.info("amount" + b2.getDouble("amount"));
					result.put(key, b2.getDouble("amount", 0));
				} else {
					result.put(key, b2.getDouble("amount") + result.get(key));
				}

			}

		}
		// List vll = new ArrayList();
		int i = 0;
		List vl = new ArrayList();
		Iterator iterator1 = storehouse.keySet().iterator();// .listIterator();
		// for(int index=0;index<5;index++)
		while (iterator1.hasNext()) {
			String sid = iterator1.next().toString();
			Map<String, Double> m = storehouse.get(sid);

			Iterator iterator = result.keySet().iterator();// .listIterator();
			// for(int index=0;index<5;index++)
			while (iterator.hasNext()) {
				BizObject abc = new BizObject();
				String mid = iterator.next().toString();
				abc.set("mid", mid);
				abc.set("mcount", m.get(mid));
				abc.set("sid", SystemKit.getCacheParamById("storehouse", sid));
				vl.add(abc);
				// System.out.print( iterator.next() );
				// int index=iterator.nextIndex();
				// System.out.print(Linkedlist.get(index)+" ");
			}

		}

		this.setAttribute("objList", vl);

		this._nextUrl = "/oa/mail/objList.jsp";
	}

	public void makeamount2() throws SQLException {
		// String
		// sql="select batch,inbillamount,amount from manufacture manu where manu.batch like 'TS%' and manu.inbillamount < manu.amount and manu.status in (1,2,3)";
		String sql = "select *  from exbill e where  e.statusid=3 and e.billno like 'XN%'   and e.expatch in (select id from manufacture manu where manu.batch like 'TS%'  and manu.inbillamount < manu.amount and manu.status in (1, 2, 3))";

		Map<String, Double> result = new HashMap();
		BizObject exbill = new BizObject("exbill");
		// String sql =
		// "select id,amount, amount-inbillamount as ddd from manufacture where status in (1,2,3) and batch like 'TS%'";
		List<BizObject> v = exbill.getQF().executeQuerySQL(sql);
		for (BizObject b : v) {

			BizObject exdetail = new BizObject("exbilldetail");
			List<BizObject> list2 = b.getList("exbilldetail");
			logger.info("list2 size " + list2.size());
			for (BizObject b2 : list2) {
				String key = b2.getBizObj("materialid").getString("coding");
				if (result.get(key) == null) {

					logger.info("amount" + b2.getDouble("amount"));
					result.put(key, b2.getDouble("amount", 0));
				} else {
					result.put(key, b2.getDouble("amount") + result.get(key));
				}

			}

		}
		List vl = new ArrayList();
		Iterator iterator = result.keySet().iterator();// .listIterator();
		// for(int index=0;index<5;index++)
		while (iterator.hasNext()) {
			BizObject abc = new BizObject();
			String mid = iterator.next().toString();
			abc.set("mid", mid);
			abc.set("mcount", result.get(mid));
			vl.add(abc);
			// System.out.print( iterator.next() );
			// int index=iterator.nextIndex();
			// System.out.print(Linkedlist.get(index)+" ");
		}

		this.setAttribute("objList", vl);
		this._nextUrl = "/oa/mail/objList.jsp";
	}

	public void makeamount1021() throws SQLException {
		Map<String, Double> result = new HashMap();
		BizObject manu = new BizObject("manufacture");
		String sql = "select id,amount, amount-inbillamount as ddd from manufacture where status in (1,2,3,5) and batch like 'TS%' and hexiao is null";
		List<BizObject> v = manu.getQF().executeQuerySQL(sql);
		for (BizObject b : v) {
			BizObject manudetail = new BizObject("manu_material_detail");
			List<BizObject> list2 = b.getList("manu_material_detail");
			logger.info("list2 size " + list2.size());
			for (BizObject b2 : list2) {
				String key = b2.getBizObj("materialid").getString("coding");
				if (result.get(key) == null) {
					// logger.info("ddd"+b.getDouble("ddd"));
					logger.info("plancount" + b2.getDouble("plancount"));
					// logger.info("amount"+b.getDouble("amount"));

					result.put(key, b2.getDouble("plancount"));
				} else {
					result.put(key, b2.getDouble("plancount") + result.get(key));
					// result.put(key,
					// b.getDouble("ddd")*b2.getDouble("plancount")/b.getDouble("amount")+result.get(key));
				}

			}
		}
		List vl = new ArrayList();
		Iterator iterator = result.keySet().iterator();// .listIterator();
		// for(int index=0;index<5;index++)
		while (iterator.hasNext()) {
			BizObject abc = new BizObject();
			String mid = iterator.next().toString();
			abc.set("mid", mid);
			abc.set("mcount", result.get(mid));
			vl.add(abc);
			// System.out.print( iterator.next() );
			// int index=iterator.nextIndex();
			// System.out.print(Linkedlist.get(index)+" ");
		}

		this.setAttribute("objList", vl);
		this._nextUrl = "/oa/mail/objList.jsp";

	}

	public void makeamount() throws SQLException {
		Map<String, Double> result = new HashMap();
		BizObject manu = new BizObject("manufacture");
		String sql = "select id,amount, amount-inbillamount as ddd from manufacture where status in (1,2,3,5) and batch like 'TS%' and hexiao is null";
		List<BizObject> v = manu.getQF().executeQuerySQL(sql);
		for (BizObject b : v) {
			BizObject manudetail = new BizObject("manu_material_detail");
			List<BizObject> list2 = b.getList("manu_material_detail");
			logger.info("list2 size " + list2.size());
			for (BizObject b2 : list2) {
				String key = b2.getBizObj("materialid").getString("coding");
				if (result.get(key) == null) {
					logger.info("ddd" + b.getDouble("ddd"));
					logger.info("plancount" + b2.getDouble("plancount"));
					logger.info("amount" + b.getDouble("amount"));
					result.put(
							key,
							b.getDouble("ddd") * b2.getDouble("plancount")
									/ b.getDouble("amount"));
				} else {
					result.put(
							key,
							b.getDouble("ddd") * b2.getDouble("plancount")
									/ b.getDouble("amount") + result.get(key));
				}

			}
		}
		List vl = new ArrayList();
		Iterator iterator = result.keySet().iterator();// .listIterator();
		// for(int index=0;index<5;index++)
		while (iterator.hasNext()) {
			BizObject abc = new BizObject();
			String mid = iterator.next().toString();
			abc.set("mid", mid);
			abc.set("mcount", result.get(mid));
			vl.add(abc);
			// System.out.print( iterator.next() );
			// int index=iterator.nextIndex();
			// System.out.print(Linkedlist.get(index)+" ");
		}

		this.setAttribute("objList", vl);
		this._nextUrl = "/oa/mail/objList.jsp";

	}

	private void showMail(String objId) throws SQLException {
		// logger.info("user is "+this.currentUser());
		DBObject obj = new BasicDBObject();
		if (!objId.equals("")) {
			obj = new MongoDB(MongoDB.T_MAIL).findOne(objId);
			// logger.info(objId+" obj is "+obj);
			BizObject annex = new BizObject("annex");
			// annex.set("type", '0');
			annex.set("bizid", objId);

			// 察看图片列表

			List annexV = annex.getQF().query(annex);
			// 图片列表
			this.setAttribute("annexList", annexV);

		}

		this._request.setAttribute("obj", obj);

		this._nextUrl = "/oa/mail/mailOper.jsp";

	}

	public void showMail() throws SQLException {

		String objId = this.getParameter("objId");
		this.showMail(objId);
	}

	@Ajax
	public String listOrder() {
		String s = "{    \"sign\": \"b83083c14cdc8b2262d0ccd041ba9477\",    \"message\": \"\",    \"username\": \"bobo\",    \"resultCode\": \"00\",    \"memberId\": \"402881413b8dea0d013b8df08cec0003\",    \"orderId\": \"ff8080813b8e63af013b8e64feaa0003\",    \"orderDetails\": {        \"deliveryFee\": 0,        \"deliveryTypeName\": \"撒旦撒\",        \"memo\": \"\",        \"orderSn\": \"100003\",        \"orderStatus\": \"unprocessed\",        \"paidAmount\": 0,        \"paymentConfigName\": \"易生卡支付\",        \"paymentFee\": 0,        \"paymentStatus\": \"unpaid\",        \"productTotalPrice\": 150,        \"shipAddress\": \"酒仙桥\",        \"shipArea\": \"北京朝阳区\",        \"shipAreaPath\": \"402881882ba8753a012ba8bf474d001c,402881882ba8753a012ba8c088690021\",        \"shipMobile\": \"13800138999\",        \"shipName\": \"向应飞\",        \"shipPhone\": \"\",        \"shipZipCode\": \"100001\",        \"shippingStatus\": \"unshipped\",        \"totalAmount\": 150    },    \"orderItem\": [        {            \"deliveryQuantity\": 0,            \"deposit\": 0,            \"id\": \"ff8080813b8e63af013b8e64febc0004\",            \"isBook\": false,            \"processContent\": \"\",            \"productCategory\": \"\",            \"productHtmlFilePath\": \"/html/product_content/201212/a96ad3625d054a88bd24238e01dc020a.html\",            \"productName\": \"庐山景点门票\",            \"productPrice\": 50,            \"productQuantity\": 1,            \"productSn\": \"SN_ECB126D2A348\",            \"subProductName\": \"成人票\",            \"suppliersId\": \"402881413b8934be013b8989ac920002\",            \"totalDeliveryQuantity\": 0        },        {            \"deliveryQuantity\": 0,            \"deposit\": 10,            \"id\": \"ff8080813b8e63af013b8e64fedc0005\",            \"isBook\": true,            \"processContent\": \"\",            \"productCategory\": \"\",            \"productHtmlFilePath\": \"/html/product_content/201212/a96ad3625d054a88bd24238e01dc020a.html\",            \"productName\": \"庐山景点门票\",            \"productPrice\": 50,            \"productQuantity\": 2,            \"productSn\": \"SN_ECB126D2A348\",            \"subProductName\": \"成人票\",            \"suppliersId\": \"402881413b8934be013b8989ac920002\",            \"totalDeliveryQuantity\": 0        }    ]}";
		return s;
	}

	//@Ajax
//	public String listOrderClient() throws JSONException {
//		String s = GmyouClient.exec("listOrder", "");
//		BizObject b = GmyouClient.changeit(s);
//		List<BizObject> v = (List) b.get("orderitem");
//		String ss = "";
//		for (BizObject bb : v) {
//			ss = ss + bb;
//		}
//		return ss;
//	}

	/**
	 * 發送短信
	 * 
	 * @throws Exception
	 */
//	public void sendSMS() throws Exception {
//		// Endpoint.publish("http://localhost:9988/helloService",new
//		// HelloImpl());
//		BizObject sms = this.getBizObjectFromMap("sms");
//		String privateKeyPath = ActionHandler._context.getRealPath("/")
//				+ "/certs/EAIInform_PrivateKey.dat";
//		String ret = SmsClient.ESBSmsInvoke("13917792530", "什么呀，小样儿",
//				privateKeyPath);
//		this._tipInfo = "webservice 运行結果！" + ret;
//		this._nextUrl = "/template/basic/msg.jsp";
//
//	}

	
//	@Ajax
//	public String sendsms() throws IOException, ServiceException, SQLException {
//		String tel_address = SystemKit.getParamById("system_core",
//				"tel_address");// 服务器地址
//		// String privateKeyPath
//		// =ActionHandler._context.getRealPath("/")+"/certs/EAIInform_PrivateKey.dat";
//		boolean ret = SmsClient
//				.sendSmsSyn("13917792530", "什么呀，小样儿", tel_address);
//		return ret+"";
//	}
	static long sold ,i;
//    public void sendSms() {
//    	
//    //	System.out.println(request.getDeal().getSum()+"--------------------------------------------");
//    	SMSRequest request = new SMSRequest();
//    	
//    	long s = Calendar.getInstance().getTimeInMillis();
//    	
//    	BooleanResponse response = new BooleanResponse();
//    	response.setSuccessful(false);
//    	System.out.println((s-sold)+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    "+i);
//    	/**
//    	 * 5秒之内不能重发
//    	 */
//    	if((s-sold)<10000){
//    		System.out.print(".");
//    		response.setSuccessful(false);
//    		
//    	}
//    	else{    		
//        	InformationService  is = new InformationService(null);
//        	boolean result=is.sendMessage(new Deal());
//            response.setSuccessful(result);
//    		System.out.print("-");    		
//    		sold=s;
//    		i++;
//    	}
//    	
//    	
//       // return response;
//    }		
   // @Ajax
//    public String sendSMS2() throws IOException, ServiceException, SQLException{
//		String message = "您卡号为"+"123"+"于"+"2013-09-09"+"消费了"+"100"+"！(该短信不作为入账凭据)";
//		String tel_address =SystemKit.getParamById("system_core", "tel_address");//服务器地址
//		boolean s=SmsClient.sendSmsSyn("18600185193",message,tel_address);//发送手机验证
//		return s+"";
//		//this._nextUrl="/template/basic/msg.jsp";
//
//    }
}
