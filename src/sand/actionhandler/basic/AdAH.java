package sand.actionhandler.basic;

import java.math.BigDecimal;


import java.net.InetAddress;

import java.net.URL;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;



import org.apache.log4j.*;


import sand.actionhandler.basic.AjaxAH;

import sand.actionhandler.system.*;
import sand.depot.servlet.system.GeneralHandleSvt;
import sand.depot.tool.system.*;

import sand.depot.business.system.Employee;
import tool.crypto.Crypto;
import tool.crypto.DiscuzPassport;
import tool.crypto.Encryption;
import tool.dao.*;
import tool.handlestring.StringFormat;
import tool.taglib.html.TokenTag;
//import tool.uda.task.inter.imp.UTaskServiceImpl;
import sand.actionhandler.*;
import sand.annotation.AccessControl; //import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import sand.annotation.CandoCheck;
import sand.annotation.export;

import javax.servlet.*;
import javax.servlet.http.*;

import sand.image.RandomGraphic;
import sand.mail.MailServer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import sand.annotation.Log2DB;

//import org.json.*;
//import net.sf.json.*;
/**
 * <p>
 * Title: 广告处理类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SAND
 * </p>
 * 
 * @author liustone
 * @version 1.0
 */
@AccessControl("no")
// 不需要访问控制
public class AdAH extends ActionHandler {

	public static final int STATE_EMPIRED = -1;
	public static final int STATE_INUSE = 1;
	public static final int STATE_WAIT = 0;

	/**
	 * 构造方法
	 * 
	 * @param p_Req
	 * @param p_Res
	 */

	public AdAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		this._objType = "MODULE"; // 随便写一个了，没有还不行
		this._moduleId = "utility"; // 本模块暂时未定
		
		//SystemKit.re
	}

	static Logger logger = Logger.getLogger(AdAH.class);

	// 初始化
	protected void initial() {
	}

	/**
	 * 列表
	 * 
	 * @throws SQLException
	 */
	@CandoCheck("editad")
	public void list() throws SQLException {
		this.listObj("advertising");
		this._nextUrl = "/udaspace.admin/ad/adList.jsp";
	}

	/**
	 * 列表
	 * 
	 * @throws SQLException
	 */
	@CandoCheck("editad")
	public void listFlash() throws SQLException {
		this.setHardcoreFilter(" location>4 ");
		this.listObj("advertising");
		this._nextUrl = "/udaspace.admin/ad/adList.jsp";
	}	
	

	
	public static List<BizObject> getFlashList() throws SQLException{
		
		//
		//return this.listObj("advertising");
		BizObject b = new BizObject("advertising");
		b.setMinValue("location", 5);
		b.setOrderBy("location");
		List<BizObject> v = b.getQF().query(b,new PageVariable(5));		
		return v;
	}
	/**
	 * 列表
	 * 
	 * @throws SQLException
	 */
	@CandoCheck("editad")
	public void show() throws SQLException {
		//this.setQueryParam(aso)
		this.showObj("advertising", this.getParameter("adid"));
		this._nextUrl = "/udaspace.admin/ad/adOper.jsp";
	}

	@CandoCheck("editad")
	public void save() throws SQLException {
		BizObject obj = this.getBizObjectFromMap("advertising");
		if (obj.getDate("begindate") == null || obj.getDate("enddate") == null)
			throw new ControllableException("必须设置结束日期和开始日期");
		if (obj.getDate("enddate").before(obj.getDate("begindate")))
			throw new ControllableException("结束日期必须晚于开始日期");

		if (!obj.getString("empired").equals("-1")) {
			if (obj.getDate("begindate").before(new Date())
					&& obj.getDate("enddate").after(new Date())) {
				obj.set("empired", this.STATE_INUSE);
			} else if (obj.getDate("begindate").after(new Date())) {
				obj.set("empired", this.STATE_WAIT);
			} else
				obj.set("empired", this.STATE_EMPIRED);
		}
		super.addOrUpdate(obj);
		this.showObj("advertising", obj.getId());
		this._nextUrl = "/udaspace.admin/ad/adOper.jsp";

	}

	static int curIndex[] = {-1,-1,-1,-1,-1};

	public void test(){
		this._nextUrl="/basic.UserActionHandler.search?employee$username=谢";
	}
	 
	/**
	 * 取广告内容
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Ajax
	public String getAd() throws SQLException {
		// String s = "\n   iii \n 44\r4";

		String location = this.getParameter("location");
		int index = Integer.parseInt(location)-1;
		BizObject ad = new BizObject("advertising");
		ad.set("location", location);
		String empired[] = { "" + this.STATE_INUSE, this.STATE_WAIT + "" };
		// ad.setMinValue("begindate", value);
		ad.set("empired", empired);
		List<BizObject> v = ad.getQF().query(ad);
		String head = "<SCRIPT charset='gb2312' LANGUAGE='JavaScript'>	parent.document.getElementById('ad_"
				+ location + "').innerHTML='";
		String content = "";
		String footer = "';</script>";
		int i=0;
		for (BizObject b : v) {
			i++;
			/*
			 * 如果过期了，设置过期
			 */
			if (b.getDate("enddate").before(new Date())) {
				b.set("empired", this.STATE_EMPIRED);
				this.getJdo().update(b);
				continue;
			}
			/**
			 * 如果在时间段内，设置使用中，返回广告内容
			 */
			if (b.getDate("begindate").before(new Date())
					&& b.getDate("enddate").after(new Date())) {
				if (b.getInt("empired", 0) == this.STATE_WAIT) {
					b.set("empired", this.STATE_INUSE);
					this.getJdo().update(b);
				}
				
				//logger.info("before orderno is "+b.getInt("orderno", 0)+"  curIndex is "+curIndex+"  "+location);
				
				/**
				 * 切换，返回广告内容
				 * 
				 */
				if (i > curIndex[index]) {
					
					curIndex[index] = i;
					content = b.getString("content");
					logger.info("after orderno is "+b.getInt("orderno", 0)+"  curIndex is "+curIndex+"  "+location);
					break;
					// return b.getString("content");
				} else{
					content = b.getString("content");
					logger.info("continue");
				}
					

			}
			else
				content="该广告位已经过期";
		}
		
		if(curIndex[index]>=v.size())
			curIndex[index] = -1;
		String reg = "[\n-\r]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(content);
		content = m.replaceAll("");

		return head+content + footer;
	}

}