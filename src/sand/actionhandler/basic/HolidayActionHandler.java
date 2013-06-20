package sand.actionhandler.basic;

import java.sql.SQLException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


import sand.actionhandler.system.ActionHandler;
import tool.basic.BasicContext;
import tool.basic.DateUtils;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class HolidayActionHandler extends ActionHandler {
	
	private Logger logger = Logger.getLogger(HolidayActionHandler.class);
	private List<BizObject> _holidayList = new ArrayList<BizObject>();

	public HolidayActionHandler(HttpServletRequest req,
			HttpServletResponse res) {
		super(req, res);
		super._objType = "holiday";
		super._moduleId = "basic";
	}

	public void show() throws ServletException, SQLException, ParseException {
		String month_y = this.getParameter("month_y");
		String month_m = this.getParameter("month_m");
		String month  = "";
		if (StringUtils.isBlank(month_y)&&StringUtils.isBlank(month_m)){
			month = DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYYMM);
			month_y = DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYY);
			month_m = DateUtils.formatDate(new Date(), DateUtils.PATTERN_MM);
		}
		else 
			month = month_y+"-"+((month_m.length()<2)?(0+month_m):month_m);
		this.pushLocation("节假日设置", "/basic.HolidayActionHandler.show?month_y="+month_y+"&month_m="+month_m);
		
		this.refreshById(month);
		for(BizObject obj : _holidayList){
			if(obj.getString("is_holiday").equals(BasicContext.HOLIDAY_IS_HOLIDAY_YES))obj.set("checkValue", obj.getId());
		}
		this._request.setAttribute("month_y", month_y);
		this._request.setAttribute("month_m", month_m);
		this._request.setAttribute("objList", _holidayList);
		this._nextUrl = "/template/basic/holiday/holidaySetOper.jsp";
	}
	
	public void save() throws ServletException,SQLException, ParseException{
		List<BizObject> list = this.getBizObjectWithType("holiday");
		String month = this.getParameter("month");
		this.refreshById(month);
		
		this.getJdo().beginTrans();
		for(BizObject biz : this._holidayList){
			biz.set("is_holiday", BasicContext.HOLIDAY_IS_HOLIDAY_NO);
			this.update(biz);
		}
		for(BizObject biz : list){
			biz.set("is_holiday", BasicContext.HOLIDAY_IS_HOLIDAY_YES);
			this.update(biz);
		}
		this.getJdo().commit();
		logger.info("list is "+list);
		this.show();
		this._nextUrl = "/template/basic/holiday/holidaySetOper.jsp";
	}
	
	public void refreshById(String month) throws SQLException{
		if(!StringUtils.isBlank(month)){
			String sql = "select * from basic.holiday where y_month='" + month + "' order by dates";
			_holidayList = QueryFactory.executeQuerySQL(sql);
		}
	}
	
	public void addNextYear() throws SQLException,ParseException{
		QueryFactory qf = new QueryFactory("holiday");
		qf.setOrderBy("dates desc");
		List<BizObject> list = qf.query();
		String year = "";
		if(list==null || list.size()<=0){
			year = DateUtils.formatDate(new Date(), DateUtils.PATTERN_YYYY);
		}else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(list.get(0).getDate("dates"));
			cal.add(Calendar.YEAR, 1);
			year = DateUtils.formatDate(cal.getTime(), DateUtils.PATTERN_YYYY);
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtils.parse(year+"-01-01", DateUtils.PATTERN_YYYYMMDD));		
		Date endDate = DateUtils.parse(year+"-12-31", DateUtils.PATTERN_YYYYMMDD);
		BizObject biz = null;
		
		while(true){
			biz = new BizObject("HOLIDAY");
			if (c.getTime().compareTo(endDate) > 0) break;
			biz.set("DATES", c.getTime());
			
			if (c.get(Calendar.DAY_OF_WEEK) - 1 == 0 || c.get(Calendar.DAY_OF_WEEK) - 1 == 6) {
				 biz.set("is_holiday", BasicContext.HOLIDAY_IS_HOLIDAY_YES);
			} else {
				 biz.set("is_holiday", BasicContext.HOLIDAY_IS_HOLIDAY_NO);
			}
			biz.set("year",year);
			biz.set("week",c.get(Calendar.DAY_OF_WEEK)-1);
			biz.set("y_month", DateUtils.formatDate(c.getTime(), DateUtils.PATTERN_YYYYMM));
			c.add(Calendar.DAY_OF_YEAR, 1);
			this.getJdo().add(biz);
		}
		
		this._nextUrl = "GeneralHandleSvt?reqType=basic.HolidayActionHandler.show&month="+ComputeTime.formatDate(endDate, ComputeTime.pattern_month);
	}
}
