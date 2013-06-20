package sand.actionhandler.basic;

import java.io.File;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import sand.actionhandler.basic.GlobalAH;
import sand.actionhandler.system.ActionHandler;

import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.depot.business.document.Document;
import sand.depot.business.oa.Job;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.SystemKit;
import sand.image.ImageOperation;
import tool.dao.BizObject;
import tool.dao.InitialConn;
import tool.dao.JDO;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;
import tool.util.Instrument;

@AccessControl("no")
// 不需要访问控制
public class DocumentAH extends ActionHandler {

	private static Logger logger = Logger.getLogger(DocumentAH.class);
	// private Document doc = null;

	static List _cols = new ArrayList();

	/**
	 * 构造函数
	 * 
	 * @param req
	 * @param res
	 */
	public DocumentAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "document"; // 本操作针对表manufactureL204
		this._moduleId = "OA"; // 本模块为基本设置
	}

	int _type = 0;
	int _index = 0;
	boolean isadmin = true;
	public final static String[] titleArry = { "uda" };
	public static String[][] strArry = null;

	private static  void initialCols(){
		if (strArry == null)
			strArry = new String[1][1];
		
		if(_cols.size()==0){
			try {
				List<BizObject> v = SystemKit.getCachePickList("channel");
				String strv[] = new String[v.size()];
				int i=0;
				for (BizObject b : v) {
					int index = b.getIntegerValue("id");
					strv[i] = b.getString("name");
					i++;
				}
				strArry[0] = strv;

				String ss[] = strArry[0];
				for (String s : ss) {
					_cols.add(s);
				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	// 初始化
	protected void initial() {
		super.initial();

		initialCols();
		this.pushLocation("资讯中心", "/oa.DocumentAH.list");
		// doc = new Document(this._curuser, this.getJdo());
		if (this.getParameter("isadmin").equals("1"))
			isadmin = true;
		try {
			_type = Integer.parseInt(this.getParameter("type"));
			_index = Integer.parseInt(this.getParameter("index"));

		} catch (java.lang.NumberFormatException e) {
			//e.printStackTrace();
			//logger.error("",e);

			// logger.error("parse index type error");
		}

		this.setAttribute("type", _type);
		this.setAttribute("index", _index);

		// logger.info("type is "+_type);
		// logger.info("index is "+_index);

		this.setAttribute("title", strArry[_type][_index]);

		// this.setAttribute("cols", _cols);
	}

	/**
	 * 分树状显示所有annex
	 * 
	 * @throws SQLException
	 */
	public void listAll() throws SQLException {
		List top = new ArrayList();

		int i = 0;
		for (String sss : titleArry) {

			List m = new ArrayList();
			// BizObject b = new BizObject("annex");
			// if (sss.indexOf("20")>=0){
			// b.set("year", sss);
			// }
			for (String ss : strArry[i]) {
				// b.set("bizid", ss);
				// List v = new QueryFactory("annex").mquery(b);
				// logger.info("b "+b );
				m.add(ss);
			}
			top.add(m);
			i++;
		}
		this.setAttribute("top", top);
		this._nextUrl = "/template/basic/docHome.jsp";
	}

	/**
	 * 取栏目列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List getChannel() throws SQLException {
		//initialCols();
		return SystemKit.getCachePickList("channel");
	}

	public static List list(int type, int index) throws SQLException {
		initialCols();
		BizObject docment = new BizObject("document");

		// if(!annex.getString("deptid").equals("")){
		// //String[] depts={annex.getString("deptid"),department.getId()};
		// annex.set("bizid", _cols.get(_index).toString());
		// //annex.set("deptid", depts);
		// }

		docment.set("bizid", 0);
		docment.set("bizid", _cols.get(index).toString());
//		docment.setOrderBy("modifydate desc");
		docment.setOrderBy("createdate desc");

		QueryFactory qf = new QueryFactory("document");
		qf.setHardcoreFilter("deleted is null");

		List<BizObject> v = qf.query(docment, new PageVariable(10));

		return v;

	}
	
	
	public static List lists(int type, int index) throws SQLException {
		initialCols();
		BizObject docment = new BizObject("document");

		// if(!annex.getString("deptid").equals("")){
		// //String[] depts={annex.getString("deptid"),department.getId()};
		// annex.set("bizid", _cols.get(_index).toString());
		// //annex.set("deptid", depts);
		// }

		docment.set("bizid", 0);
		docment.set("bizid", _cols.get(index).toString());
//		docment.setOrderBy("modifydate desc");
		docment.setOrderBy("createdate desc");

		QueryFactory qf = new QueryFactory("document");
		qf.setHardcoreFilter("deleted is null");

		List<BizObject> v = qf.query(docment, new PageVariable(13));

		return v;

	}

	/**
	 * 公用list
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws ServletException
	 * @throws SQLException
	 */

	public void list() throws UnsupportedEncodingException, ServletException,
			SQLException {
		// String BizId = this.getParameter("BizId");
		// String type = this.getParameter("type");
		this.pushLocation(_cols.get(_index).toString(),
				"/oa.DocumentAH.list?index=" + _index);
		this.setAttribute("channels", this.getChannel());
		BizObject docment = this.getBizObjectFromMap("document");
		// logger.info("annex is "+annex);
		if (docment == null)
			docment = new BizObject("document");

		// if(!annex.getString("deptid").equals("")){
		// //String[] depts={annex.getString("deptid"),department.getId()};
		// annex.set("bizid", _cols.get(_index).toString());
		// //annex.set("deptid", depts);
		// }

		docment.set("bizid", _cols.get(_index).toString());

		this.clearEmptyParam(docment);
		// _cols.get(_index).toString()

		// }

		// annex.set("bizid", new String(
		// _cols.get(0).toString().getBytes("ISO-8859-1"),"UTF-8"));
		// this.setHardcoreFilter("BizId = '" + BizId + "'");
		// if (_type==2){
		// annex.setOrderBy("title");
		// this.setPageSize(-1);
		// }
		//
		// else
		// docment.setOrderBy("modifydate desc");
		docment.setOrderBy("createdate desc");

		// if(!this.getParameter("orderBy").equals("")){
		// annex.setOrderBy(this.getParameter("orderBy"));
		// this.setAttribute("orderBy", this.getParameter("orderBy"));
		// }

		// this.setAttribute("cols", _cols);
		this.setAttribute("obj", docment);
		// logger.info("annex "+docment);

		// this.setHardcoreFilter("bizid='"+_cols.get(_index).toString()+"'");
		QueryFactory qf = new QueryFactory("document");
		qf.setHardcoreFilter("deleted is null");

		List<BizObject> v = qf.query(docment, preparePageVar());

		this.setAttribute("docList", v);
		// logger.info("sql is "+_sql);
		this._nextUrl = "/artic/artic_list.jsp";
		if (isadmin)
			this._nextUrl = "/template/basic/docList.jsp";

	}

	/**
	 * 人事
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	// public void listRenShi() throws UnsupportedEncodingException,
	// ServletException, SQLException{
	// this.setAttribute("title", "人事管理");
	// this.setAttribute("method", "listRenShi");
	// _cols.add("08年度会议纪要");
	// _cols.add("08年度红头文件");
	// _cols.add("08年度业务文件");
	// _cols.add("09年度会议纪要");
	// _cols.add("09年度红头文件");
	// _cols.add("09年度业务文件");
	// _cols.add("信息管理部帮助文档");
	// _cols.add("其他类别");
	// list();
	//
	// }
	/**
	 * 行政
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 *             //
	 */
	// public void listXingZheng() throws UnsupportedEncodingException,
	// ServletException, SQLException{
	// this.setAttribute("title", "行政管理");
	// this.setAttribute("method", "listXingZheng");
	// _cols.add("人事规章制度");
	// _cols.add("人事常用表单");
	// _cols.add("部门组织结构");
	// _cols.add("部门主要职责");
	// _cols.add("岗位说明书");
	// list();
	//
	//
	// }

	/**
	 * 添加体系文件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	@CandoCheck("editdocument")
	public void add() throws ServletException, SQLException {
		this.setAttribute("obj", this.getBizObjectFromMap("document"));
		this.setAttribute("channel", _cols.get(_index).toString());
		this._nextUrl = "/artic/artic_bencandy.jsp";
		if (isadmin)
			this._nextUrl = "/template/basic/docOper.jsp";
	}

	public static long forJava(File f1, File f2) throws IOException {
		// long time=new Date().getTime();
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);
		byte[] buffer = new byte[length];
		while (true) {
			int ins = in.read(buffer);
			if (ins == -1) {
				in.close();
				out.flush();
				out.close();
				// return new Date().getTime()-time;
			} else
				out.write(buffer, 0, ins);
		}
	}

	public void subrecover(File dir) throws SQLException, IOException {
	}

	public void staticRecover() throws SQLException {
		QueryFactory qf = new QueryFactory("annex");
		qf.setOrderBy("annexurl");
		List<BizObject> v = qf.query();
		for (BizObject a : v) {
			String filepath = "c:/" + a.getString("annexurl") + a.getId()
					+ a.getString("suffix");
			System.out.println(filepath);
			if (!new File(filepath).exists()) {
				String name = "   ";
				if (a.getBizObj("creator") != null)
					name = a.getBizObj("creator").getString("username");
				logger.info(a.getString("annexname") + "  " + name + "  "
						+ a.getString("annexurl") + "  " + a.getString("bizid"));
			}
		}

	}

	/**
	 * 恢复数据文件
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void recover() throws SQLException, IOException {

		String dir = "c://files";
		if (!this.getParameter("dir", "").equals("")) {
			dir = dir + "/" + this.getParameter("dir");

		}
		this.subrecover(new File(dir));
	}

	/**
	 * 添加体系文件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void view() throws ServletException, SQLException {
		// this.setAttribute("obj", this.getBizObjectFromMap("annex"));
		super.showObj("document", _objId);
		this.setAttribute("channel", _cols.get(_index).toString());
		this._nextUrl = "/artic/artic_bencandy.jsp";
		if (isadmin)
			this._nextUrl = "/template/basic/docView.jsp";

	}

	@CandoCheck("editdocument")
	public void view_sx() throws ServletException, SQLException {
		// this.setAttribute("obj", this.getBizObjectFromMap("annex"));
		super.showObj("document", _objId);
		this._nextUrl = "/artic/artic_bencandy.jsp";
	}

	/**
	 * 编辑体系文件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void show() throws ServletException, SQLException {
		// this.setAttribute("obj", this.getBizObjectFromMap("annex"));
		this.pushLocation(_cols.get(_index).toString(),
				"/oa.DocumentAH.list?index=" + _index);
		// this.pushLocation( _cols.get(_index).toString(),
		// "/oa.DocumentAH.list?index="+_index);
		this.setAttribute("channels", this.getChannel());
		super.showObj("document", _objId);
		this._nextUrl = "/artic/artic_bencandy.jsp";
		this.setAttribute("channel", _cols.get(_index).toString());
		if (isadmin)
			this._nextUrl = "/template/basic/docOper.jsp";
	}

	@Ajax
	public String upload4Editor() throws SQLException, ServletException,
			IOException, JSONException {

		JSONObject obj = new JSONObject();
		try {
			
			File file = getFile();
//			String path = file.getAbsolutePath();
//			//File file2 = new File(path+)
//			file =ImageOperation.Imagese(_image, destFile,  (float)0.9);
			BizObject annex = this.getBizObjectFromMap("annex");
			//annex.set("type", Document.TYPE_CLUBPIC);
			String annexid = upload(file,true, false,5);
			
			annex.setID(annexid);
			annex.refresh();

			obj.put("error", 0);
			obj.put("url", "/" + annex.getString("url"));

		} catch (ControllableException e) {
			obj.put("error", 1);

			if (e.getMessage().equals("请选择要上传的文件类型"))
				obj.put("message", "请先保存文档");
			else
				obj.put("message", e.getMessage());

			// obj.put("error", e.getMessage());
		}

		// out.println(obj.toJSONString());

		return obj.toString();//.toJSONString();
	}

	private File getFile(){
		List<File> files = this.getUploadFiles();
		/**
		 * 如果是新建文档，那么需要上传一个附件
		 */
		if (files.size() > 1)
			throw new ControllableException("只允許上傳一個文件");

		File file = null;
		if (files.size() > 0)
			file = files.get(0);
		return file;
	}
	@Ajax
	public String upload() throws SQLException, ServletException, IOException {
		
		File file = getFile();
		
		return upload(file,true, true, 2);
	}

	public String upload(File f,boolean checkuser, boolean checkbizid, int size)
			throws SQLException, ServletException, IOException {
		String sessionid = this.getParameter("jsessionid");
		logger.info("jsessionid " + sessionid);
		logger.info(_request.getSession().getId() + "  " + this._curuser);

		BizObject annex = this.getBizObjectFromMap("annex");

		if (annex == null)
			annex = new BizObject("annex");

		if (checkuser && this.getCurUser() == null) {
			if (annex.getBizObj("creator") != null) {
				this._curuser = annex.getBizObj("creator");
			} else
				throw new ControllableException("请指定 annex$creator 创建人");

		}

		// 自定义BizId
		String BizId = annex.getString("bizid");
		if (checkbizid && (BizId == null || BizId.equals(""))) {
			throw new ControllableException("请选择要上传的文件类型");
		}
		logger.info("udpate annex " + annex);
		Document.updateAnnex(annex, f, size);
		return annex.getId();
	}



	/**
	 * 体系上传
	 * 
	 * @param BizId
	 *            附件所属文件ID
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	@CandoCheck("editdocument")
	public void update() throws SQLException, ServletException, IOException {
		// if (this.getParameter("JSESSIONID")!=null) {
		// Cookie userCookie = new Cookie("JSESSIONID",
		// this.getParameter("JSESSIONID"));
		// _response.addCookie(userCookie);
		// }

		BizObject document = this.getBizObjectFromMap("document");

		if (document == null)
			document = new BizObject("document");
		if (document.getString("createdate").equals("")) {
			document.set("createdate", new Date());
			Calendar c = Calendar.getInstance();
			// c.setTime(d);
			document.set("year", c.get(Calendar.YEAR));

		}

		if (!document.getString("createdate").equals("")) {
			Date d = document.getDate("createdate");
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			document.set("year", c.get(Calendar.YEAR));

		}
		//this.modify(document);
		// 自定义BizId
		// String BizId = annex.getString("bizid");
		// if (BizId == null || BizId.equals("")) {
		// throw new ControllableException("请选择要上传的文件类型");
		// }
		List<File> files = this.getUploadFiles();

		/**
		 * 如果是新建文档，那么需要上传一个附件
		 */
		// if (files.size()==0&&annex.getId().equals("")){
		// throw new ControllableException("請上傳一個文件");
		// }
		if (files.size() > 1)
			throw new ControllableException("只允許上傳一個文件");

		// String pathStr = Instrument.getUpPath(ActionHandler.currentUser()
		// .getEmployee().getID());

		File file = null;
		if (files.size() > 0)
			file = files.get(0);
		// BizObject annex = this.getBizObjectFromMap("annex");
		// annex.set("bizId", BizId);
		// document.set("bizid", document.get)
		logger.info("udpate annex " + document);
		logger.info("document is " + document);
		this.getJdo().addOrUpdate(document);

		BizObject annex = this.getBizObjectFromMap("annex");
		if (annex != null) {
			annex.set("bizid", document.getId());
			Document.updateAnnex(annex, file);

		}

		// Document.updateFiles(BizId, files);
		// this.clearEmptyParam(p_BizO);
		this.clearQueryParam();
		// this.setQueryParam("bizid", BizId);
		this.list();
	}

	/**
	 * 删除一个文件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	@CandoCheck("editdocument")
	public void delete() throws SQLException, UnsupportedEncodingException,
			ServletException {
		// String annexId = this.getParameter("annexId");
		//
		// if (annexId==null||annexId.equals("")){
		// annexId =_objId;
		// }
		System.out.println("annexId " + _objId);
		// this._dispatched =true;
		BizObject doc = new BizObject("document");
		doc.setID(_objId);
		this.getJdo().delete(doc);
		// Document.delete(_objId);
		// this.setQueryParam("bizid", this.getParameter("bizid"));
		this.clearQueryParam();
		this.list();
	}

	public static void download(String annexId, HttpServletResponse _response)
			throws SQLException {
		// String filename = _request.getParameter("filename");
		// String path = Document.getFilePath(_objId);
		// String annexId = this.getParameter("annexId");

		System.out.println("annexId " + annexId);

		QueryFactory qf = new QueryFactory("annex");
		BizObject a = qf.getByID(annexId);
		if (a == null || a.getString("deleted").equals("1"))
			throw new ControllableException("该文件已被删除！");
		if (a == null)
			throw new ControllableException("文件找不到，请重新上传！");
		String path = a.getString("annexurl");
		String filename = a.getString("annexname");

		// _response.setContentType("text/x-msdownload");
		_response.addHeader("Content-Disposition", "attachment; filename=\""
				+ a.getId() + a.getString("suffix") + "\"");
		java.io.OutputStream os = null;
		java.io.FileInputStream fis = null;

		try {
			os = _response.getOutputStream();
			fis = new java.io.FileInputStream(path + a.getId()
					+ a.getString("suffix"));
			logger.info("file path is " + path + a.getId()
					+ a.getString("suffix"));
			byte[] b = new byte[1024 * 10];
			int i = 0;

			while ((i = fis.read(b)) > 0) {
				os.write(b, 0, i);
			}

			fis.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 下载文件
	 * 
	 */
	public void download() throws SQLException {
		this._dispatched = true;
		String annexId = this.getParameter("annexId");
		if (annexId == null || annexId.equals("")) {
			annexId = _objId;
		}
		this.download(annexId, _response);

	}

	/**
	 * 查看行政公文文档
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void listAdministrationMainDepot() throws ServletException,
			SQLException, UnsupportedEncodingException {
		this._objType = "document";
		String BizId = this.getParameter("BizId");
		String type = this.getParameter("type");
		this.setAttribute("BizId", BizId);
		if (BizId == null || BizId.equals("")) {
			if (type == null || type.equals(""))
				BizId = "08会议";
			else
				BizId = type;

		} else {
			BizId = new String(BizId.getBytes("ISO-8859-1"), "UTF-8");
		}
		this.setHardcoreFilter("BizId = '" + BizId + "'");
		this.listObj();

		this._nextUrl = "/template/basic/administrationMainDepotList.jsp";
	}

	/**
	 * 查看人事公文文档
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void listPersonnelMainDepot() throws ServletException, SQLException,
			UnsupportedEncodingException {
		String BizId = this.getParameter("BizId");
		String type = this.getParameter("type");
		this.setAttribute("BizId", BizId);
		if (BizId == null || BizId.equals("")) {
			if (type == null || type.equals(""))
				BizId = "人事规章";
			else
				BizId = type;
		} else {
			BizId = new String(BizId.getBytes("ISO-8859-1"), "UTF-8");
		}

		this.setHardcoreFilter("BizId = '" + BizId + "'");
		this.listObj();
		this._nextUrl = "/template/basic/personnelMainDepotList.jsp";
	}

	/**
	 * 添加体系文件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void addSystemFile() throws ServletException, SQLException {
		this._nextUrl = "/template/basic/SystemFileOper.jsp";
	}

	/**
	 * 查看体系文件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void listSystemFile() throws ServletException, SQLException,
			UnsupportedEncodingException {
		this.listSystemFile("");
	}

	/**
	 * 查看体系文件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void listSystemFile(String bizid) throws ServletException,
			SQLException, UnsupportedEncodingException {
		this._objType = "annex";
		String BizId = this.getParameter("BizId");
		if (bizid != null && !bizid.equals(""))
			BizId = bizid;
		String type = this.getParameter("type");
		this.setAttribute("BizId", BizId);

		if (BizId == null || BizId.equals("")) {
			if (type == null || type.equals(""))
				BizId = "管理手册";
			else
				BizId = type;

		} else {
			BizId = new String(BizId.getBytes("ISO-8859-1"), "UTF-8");

		}

		String annexname = this.getParameter("annexname");

		if (annexname == null)
			annexname = "";
		else
			this.setAttribute("annexname", annexname);

		this.setHardcoreFilter("BizId = '" + BizId + "'"
				+ " and annexname  like  '%" + annexname + "%'");
		this.setOrderBy("annexname");

		this.listObj();
		// System.out.println (this._sql);
		List<BizObject> ary = (List) this._request.getAttribute("objList");

		// 是表单模板就显示下载按钮
		for (BizObject ar : ary) {
			String bizidd = ar.getString("bizid");
			if (bizidd.indexOf("表单模板") != -1) {
				ar.set("down", "1");
			}
		}

		this._nextUrl = "/template/basic/SystemFileList.jsp";
	}

	/**
	 * 查看系统更新信息文件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */

	public void listUpdateFile() throws ServletException, SQLException,
			UnsupportedEncodingException {
		this._objType = "annex";
		String BizId = this.getParameter("BizId");
		String type = this.getParameter("type");
		this.setAttribute("BizId", BizId);
		if (BizId == null || BizId.equals("")) {
			if (type == null || type.equals(""))
				BizId = "系统更新信息文件";
			else
				BizId = type;

		} else {
			BizId = new String(BizId.getBytes("ISO-8859-1"), "UTF-8");
		}
		this.setHardcoreFilter("BizId = '" + BizId + "'");
		this.listObj();

		this._nextUrl = "/template/basic/systemUpdateList.jsp";
	}

	/**
	 * 上传公共文件
	 * 
	 * @param BizId
	 *            附件所属文件ID
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateMainFiles() throws SQLException, ServletException,
			IOException {
		// 因为是公文文档自定义BizId(公文ID)为MainDepot
		String BizId = this.getParameter("type");
		if (BizId == null || BizId.equals("")) {
			throw new ControllableException("请选择要上传的文件类型");
		}
		List<File> files = this.getUploadFiles();
		Document.updateFiles(BizId, Job.MainFilePath, files);
	}

	/**
	 * 删除附件
	 * 
	 * @author xiangtiesha
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void delMainAnnex() throws SQLException, ServletException,
			UnsupportedEncodingException {
		String id = this.getParameter("ANNEXID");
		// doc.delAnnex(id, Job.MainFilePath);
		this.delete(id);
	}

	/**
	 * 下载公共文档
	 * 
	 * @author xiangtiesha
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void downloadMaindepot() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.MainFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载体系文件,不加权限控制的
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	public void downloadSystemFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		// String suffix = Document.getFileSuffix(annexname);
		// String rFileBody = pathStr + annex.getId()+suffix;
		//
		// newf = new File(rFileBody);
		// // 把上传文件该名，并移动到指定目录
		// File oldfile = new File(pathStr+annexname);
		// files.get(i).renameTo(newf);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载研发部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("research")
	public void downloadResearchFile() throws IOException, ServletException,
			SQLException {
		// String userid = this._curuser.getString("userid");
		// QueryFactory qf=new QueryFactory("re_user_dept");
		// List<BizObject> list =qf.query("userid", userid);
		// List <String > list2;
		// for (int i=0;i<list.size();i++)
		//
		// {
		// BizObject obj =list.get(i);
		// String deptid= obj.getString("");
		//
		//
		//
		//
		// List aryList = SystemKit.getCachePickList("");
		//
		//
		// }

		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载制造部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("produce")
	public void downloadProduceFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载销售部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("sell")
	public void downloadSellFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载采购部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("stock")
	public void downloadStockFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载质量部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("quality")
	public void downloadQualityFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载人力部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("manpower")
	public void downloadManPowerFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载行政部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("adm")
	public void downloadAdmFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载pos应用部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("pos")
	public void downloadPosFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 下载应用软件部门文档
	 * 
	 * @author jiang
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 */
	@CandoCheck("application")
	public void downloadApplicationFile() throws IOException, ServletException,
			SQLException {
		this._dispatched = true;
		// 取得附件信息的中URL字段
		String annexurl = this.getParameter("fileUrl");
		String annexname = this.getParameter("filename");

		// 根据操作类型，和附件的URL，父类文件夹生成完整的URL信息
		String pathStr = Instrument.getDownPath(Job.SystemFilePath, annexurl);

		Instrument.download(pathStr, annexname, _response);
	}

	/**
	 * 行政上传公共文件
	 * 
	 * @param BizId
	 *            附件所属文件ID
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateAdministrationMainFiles() throws SQLException,
			ServletException, IOException {
		this.updateMainFiles();
		this.listAdministrationMainDepot();
	}

	/**
	 * 行政删除附件
	 * 
	 * @author xiangtiesha
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void delAdministrationMainAnnex() throws SQLException,
			ServletException, UnsupportedEncodingException {
		this.delMainAnnex();
		this.listAdministrationMainDepot();
	}

	/**
	 * 体系文件附件删除
	 * 
	 * @author xiangtiesha
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void delASystemFile() throws SQLException, ServletException,
			UnsupportedEncodingException {
		String id = this.getParameter("ANNEXID");
		// doc.delAnnex(id, Job.SystemFilePath);
		this.delete(id);
		this.listSystemFile("");
	}

	/**
	 * 人事上传公共文件
	 * 
	 * @param BizId
	 *            附件所属文件ID
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updatePersonnelMainFiles() throws SQLException,
			ServletException, IOException {
		this.updateMainFiles();
		this.listPersonnelMainDepot();
	}

	/**
	 * 上传系统更新文件
	 * 
	 * @param BizId
	 *            附件所属文件ID
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	@CandoCheck("updateSysteminfo")
	public void systemUpdateMainFiles() throws SQLException, ServletException,
			IOException {
		String BizId = this.getParameter("type");
		String remarks = this.getParameter("annex$remarks");
		if (remarks == null || remarks.equals("")) {
			throw new ControllableException("请添加更新内容！");
		}
		if (BizId == null || BizId.equals("")) {
			throw new ControllableException("请选择要上传的文件类型");
		}
		File file = this.getUploadFile();
		if (file == null) {
			throw new ControllableException("请选择要上传的文件！");
		}
		Document.updateFile(BizId, file, remarks);
		this.listUpdateFile();
	}

	/**
	 * 体系上传
	 * 
	 * @param BizId
	 *            附件所属文件ID
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void viewSystemFiles() throws SQLException, ServletException,
			UnsupportedEncodingException {
		this.showObj();
		this._nextUrl = "/template/basic/docArchReader.jsp";
		// this._nextUrl="/template/basic/pdfViewer.jsp";
	}

	/**
	 * 体系上传
	 * 
	 * @param BizId
	 *            附件所属文件ID
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateSystemFiles() throws SQLException, ServletException,
			IOException {

		// 自定义BizId
		String BizId = this.getParameter("type");
		if (BizId == null || BizId.equals("")) {
			throw new ControllableException("请选择要上传的文件类型");
		}
		List<File> files = this.getUploadFiles();

		if (files.size() == 0) {
			throw new ControllableException("請上傳一個文件");
		}
		if (files.size() > 1)
			throw new ControllableException("只允許上傳一個文件");

		String pathStr = Instrument.getUpPath(ActionHandler.currentUser()
				.getEmployee().getID());

		File file = files.get(0);
		BizObject annex = this.getBizObjectFromMap("annex");
		annex.set("bizId", BizId);
		Document.updateAnnex(annex, file);

		// Document.updateFiles(BizId, files);
		this.listSystemFile(BizId);
	}

	/**
	 * 人事删除附件
	 * 
	 * @author xiangtiesha
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void delPersonnelMainAnnex() throws SQLException, ServletException,
			UnsupportedEncodingException {
		this.delMainAnnex();
		this.listPersonnelMainDepot();
	}

	/**
	 * 删除系统更新文件附件
	 * 
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */
	public void delSystemUpdateMainAnnex() throws SQLException,
			ServletException, UnsupportedEncodingException {
		this.delMainAnnex();
		this.listUpdateFile();
	}

	/**
	 * 插入普通用户看体系文件的权限
	 * 
	 * @author xiangtiesha
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */

	public void update2() throws SQLException, ServletException,
			UnsupportedEncodingException {
		QueryFactory qf = new QueryFactory("annex");
		List<BizObject> v = qf.query();
		getJdo().beginTrans();
		System.out.println("v size " + v.size());

		for (BizObject b : v) {

			if (b.getString("annexurl").indexOf("/root") >= 0)// 如果是新加的，继续
			{
				System.out.println("alread process continue");
				String suffix = Document
						.getFileSuffix(b.getString("annexname"));
				b.set("suffix", suffix);
				getJdo().update(b);
				continue;

			}
			if (b.getString("suffix").equals("")) {
				String suffix = Document
						.getFileSuffix(b.getString("annexname"));
				b.set("suffix", suffix);
				getJdo().update(b);

			}
			String suffix = Document.getFileSuffix(b.getString("annexname"));
			String pathStr = "/root/orderform.depot/" + b.getString("annexurl")
					+ b.getString("annexname");
			// Instrument.getUpPath("SystemFile.depot",
			// b.getString("annexurl"));
			File f = new File(pathStr);
			if (!f.exists())// 如果文件不存在，继续
			{
				System.out.println("not exists continue ");

				continue;
			}
			File f3 = new File("/root/erp.upload/" + b.getString("annexurl"));
			if (!f3.exists()) {
				f3.mkdirs();
			}
			String pathStr2 = "/root/erp.upload/" + b.getString("annexurl")
					+ b.getId() + suffix;
			File f2 = new File(pathStr2);
			f.renameTo(f2);
			b.set("annexurl", "/root/erp.upload/" + b.getString("annexurl"));
			b.set("suffix", suffix);
			getJdo().update(b);

			System.out.println("OK!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
	}

	/**
	 * 插入普通用户看体系文件的权限
	 * 
	 * @author xiangtiesha
	 * @throws SQLException
	 * @throws ServletException
	 * @throws UnsupportedEncodingException
	 */

	public static void main2(String[] args) throws SQLException,
			ServletException, UnsupportedEncodingException {

		InitialConn.initial();

		JDO jdo = new JDO("re_user_role");

		// 查处所有人
		String sql1 = "select distinct userid from  basic.employee";
		List<BizObject> arylist = QueryFactory.executeQuerySQL(sql1);
		// 循环每个人,查出他所属于的部门并给他加 上相应部门的下载体系文件的权限
		for (BizObject b : arylist) {
			String userid = b.getString("userid");

			// //用户循环
			//
			// {
			// 找到一个用户，然后查处部门，根据用户部门再循环
			// {
			//
			// 取出部门id == 角色备注
			// 设置权限
			// }
			//
			// }

			String sql2 = " select * from basic.re_user_dept t  where  t.userid='"
					+ userid + "'";

			List<BizObject> arylist2 = QueryFactory.executeQuerySQL(sql2);
			for (BizObject g : arylist2) { // 取出一个部门
				String deptid = g.getString("deptid");

				// 看这个部门是不是研发,是研发就添加研发体系文件角色给该用户
				String sql3 = "select  *  from  basic.department  t where   t.deptname like '%研发%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist3 = QueryFactory.executeQuerySQL(sql3);

				if (arylist3 != null && arylist3.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "研发体系文件");

					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}

					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);
					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

				// 看这个部门是不是制造,是制造就添加制造体系文件角色给该用户
				String sql4 = "select  *  from  basic.department  t where   t.deptname like '%制造%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist4 = QueryFactory.executeQuerySQL(sql4);

				if (arylist4 != null && arylist4.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "制造体系文件");
					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}

					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);

					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

				// 看这个部门是不是销售,是销售就添加销售体系文件角色给该用户
				String sql5 = "select  *  from  basic.department  t where   t.deptname like '%销售%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist5 = QueryFactory.executeQuerySQL(sql5);

				if (arylist5 != null && arylist5.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "销售体系文件");
					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}
					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);

					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

				// 看这个部门是不是采购,是采购就添加采购体系文件角色给该用户
				String sql6 = "select  *  from  basic.department  t where   t.deptname like '%采购%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist6 = QueryFactory.executeQuerySQL(sql6);

				if (arylist6 != null && arylist6.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "采购体系文件");
					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}
					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);

					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

				// 看这个部门是不是质量,是质量就添加质量体系文件角色给该用户
				String sql7 = "select  *  from  basic.department  t where   t.deptname like '%质量%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist7 = QueryFactory.executeQuerySQL(sql7);

				if (arylist7 != null && arylist7.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "质量体系文件");
					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}
					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);

					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

				// 看这个部门是不是人力,是人力就添加人力体系文件角色给该用户
				String sql8 = "select  *  from  basic.department  t where   t.deptname like '%人力%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist8 = QueryFactory.executeQuerySQL(sql8);

				if (arylist8 != null && arylist8.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "人力体系文件");
					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}

					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);

					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

				// 看这个部门是不是行政,是行政就添加行政体系文件角色给该用户
				String sql9 = "select  *  from  basic.department  t where   t.deptname like '%行政%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist9 = QueryFactory.executeQuerySQL(sql9);

				if (arylist9 != null && arylist9.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "行政体系文件");
					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}

					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);

					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

				// 看这个部门是不是pos应用,是pos应用就添加pos应用体系文件角色给该用户
				String sql10 = "select  *  from  basic.department  t where   t.deptname like '%pos应用%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist10 = QueryFactory.executeQuerySQL(sql10);

				if (arylist10 != null && arylist10.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "pos应用体系文件");
					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}
					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);

					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

				// 看这个部门是不是应用软件,是应用软件就添加应用软件体系文件角色给该用户
				String sql11 = "select  *  from  basic.department  t where   t.deptname like '%应用软件%' and t.deptid='"
						+ deptid + "'";

				List<BizObject> arylist11 = QueryFactory.executeQuerySQL(sql11);

				if (arylist11 != null && arylist11.size() > 0)

				{

					BizObject obj = new BizObject("re_user_role");
					obj.set("userid", userid);
					obj.set("roleid", "应用软件体系文件");
					// 看是添加还是更新
					QueryFactory qf = new QueryFactory("re_user_role");
					List<BizObject> ary = qf.query(obj);
					if (ary.size() > 0) {
						String id = ary.get(0).getID();
						obj.set("id", id);

					}
					obj.set("CREATEDATE", "2009-05-04");
					obj.set("CREATOR", "jiangbao");

					// ActionHandler.currentSession().addOrUpdate(obj);

					jdo.beginTrans();

					try {
						jdo.addOrUpdate(obj);
						jdo.commit();
					} catch (Exception e) {
						jdo.rollbackAll();
					}

				}

			}

		}

	}

}
