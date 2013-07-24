package sand.actionhandler.basic;

import java.io.File;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;


import sand.depot.business.document.Document;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;
import tool.dao.QueryFactory;

public class ComputeTime {

	public static String pattern_hour = "yyyy-MM-dd HH:mm:ss";

	public static String pattern_day = "yyyy-MM-dd";

	public static String pattern_month = "yyyy-MM";
	
	public static String pattern_year = "yyyy";

	/**
	 * 根据日期对象,格式化成字符串
	 * 
	 * @author pengfei
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date) {
		if (date == null) {
			throw new ControllableException("日期对象不能为空!");
		}
		return ComputeTime.formatDate(date, pattern_day);
	}

	/**
	 * 根据传进来的字符串,按照指定的格式进行格式化,如果传进来的是空或者是null对象,则默认为当前日期
	 * 
	 * @author pengfei
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDateString(String date, String pattern)
			throws ParseException {
		SimpleDateFormat sim = new SimpleDateFormat(pattern);
		if (StringUtils.isBlank(date)) {
			return ComputeTime.getDate(ComputeTime.getDateString(new Date()));
		} else {
			return sim.parse(date);
		}
	}

	/**
	 * 根据当前日期,得到当前周的第一天或者是最后一天
	 * 
	 * @author pengfei
	 * @param date是指某个星期一
	 * @param days
	 *            (如:-7表示date的上个星期一,如:7表示date的下个星期一)]
	 * @param num
	 *            是指当前日期后的几天(比如6,则是指date这个星期的星期天)
	 * @return 返回日期对象
	 * @throws ServletException
	 * @throws SQLException
	 */
	public static Date getWeekDate(Date date, int days, int num)
			throws ServletException, SQLException {
		Calendar now = Calendar.getInstance();
		if (date == null) {
			now.setTime(new Date());
		} else {
			now.setTime(date);
		}
		now.add(Calendar.DAY_OF_YEAR, days);
		int today = now.get(Calendar.DAY_OF_WEEK);
		int first_day_of_week = now.get(Calendar.DATE) + 2 - today;
		now.set(now.DATE, first_day_of_week + num);// 星期一
		Date firstDate = now.getTime();
		return firstDate;
	}

	/**
	 * 根据当前日期,得到当前月的第一天或者是最后一天
	 * 
	 * @author pengfei
	 * @param date
	 * @param days
	 *            (1:当月的第一天 31:当月的最后一天,days的值为几就代表是当月的第几天)
	 * @return
	 * @throws ServletException
	 * @throws SQLException
	 */
	public static Date getMonthDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date dateOfMonth;

		if (days == 31) {
			// 如果要昨天最后一天,则先得到当月的第一天,再得到最后一天
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.add(Calendar.DAY_OF_MONTH, -(dayOfMonth - 1));
			// 得到最后一天,因为calendar中第一天是从0开始,所以最后一天,要减去1
			// calendar.getActualMaximum(Calendar.DAY_OF_MONTH)得到这个月有几天
			calendar.add(Calendar.DAY_OF_MONTH, calendar
					.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
			dateOfMonth = calendar.getTime();
		} else {
			// 得到当天是这月的第几天
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			// 减去dayOfMonth,得到想得到的日期
			calendar.add(Calendar.DAY_OF_MONTH, -(dayOfMonth) + days);
			dateOfMonth = calendar.getTime();
		}
		return dateOfMonth;
	}

	/**
	 * 根据指定日期，往前或往后推指定的天数
	 * 
	 * @author pengfei
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date getDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	/**
	 * 根据日期字符串,格式化成日期对象
	 * 
	 * @author pengfei
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getDate(String date) throws ParseException {
		if (StringUtils.isBlank(date)) {
			throw new ControllableException("日期对象不能为空!");
		}
		SimpleDateFormat sim = new SimpleDateFormat(pattern_day);
		return sim.parse(date);
	}

	/**
	 * 传进来一个日期对象，按照格式化后返回的还是一个日期对象，主要是为了去掉时分秒(时分秒全为零)
	 * 
	 * @author pengfei
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate2(Date date, String pattern)
			throws ParseException {
		if (date == null) {
			throw new ControllableException("日期对象不能为空!");
		}
		SimpleDateFormat sim = new SimpleDateFormat(pattern);
		String sdate = sim.format(date);
		return sim.parse(sdate);
	}

	/**
	 * 格式化日期字符串
	 * 
	 * @author pengfei
	 * @param datetime
	 * @return
	 * @throws ServletException
	 * @throws SQLException
	 */
	public static String formatDate(String datetime) throws SQLException {
		return formatDate(datetime, pattern_hour);
	}

	/**
	 * 按照规定的格式进行格式化字符串
	 * 
	 * @author pengfei
	 * @param datetime
	 * @param pattern
	 * @return
	 */
	public static String formatDate(String datetime, String pattern) {
		if (StringUtils.isBlank(datetime)) {
			throw new ControllableException("日期对象不能为空!");
		}
		SimpleDateFormat sim = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sim.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sim.format(date);
	}

	/**
	 * 计算工时
	 * 
	 * @param startTime
	 * @param endTime
	 * @throws ServletException
	 * @throws SQLException
	 */
	public static String computeWorkHour(Date startTime, Date endTime)
			throws ServletException, SQLException {
		long between = (endTime.getTime() - startTime.getTime()) / 60 / 60 / 100;
		BigDecimal hour = new BigDecimal("0.0");
		hour = hour.add(new BigDecimal(between));
		hour = hour.divide(new BigDecimal(10));
		return hour.toString();
	}

	/**
	 * 计算工时(默认是8小时)
	 * 
	 * @author pengfei
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static int computeHour(Date startDate, Date endDate)
			throws SQLException, ParseException {
		return computeHour(startDate, endDate, 8);
	}

	/**
	 * 计算工时
	 * 
	 * @author pengfei
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param hours
	 *            工时制(一天工作8小时)
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static int computeHour(Date startDate, Date endDate, int hours)
			throws SQLException, ParseException {
		String startD = ComputeTime.getDateString(startDate);
		String endD = ComputeTime.getDateString(endDate);

		String sql = "select count(*) count from oa.HOLIDAY where to_char(DATES,'yyyy-MM-dd')>='"
				+ startD
				+ "' and to_char(DATES,'yyyy-MM-dd')<='"
				+ endD
				+ "' and STATE=1 order by dates";
		List<BizObject> aryList = QueryFactory.executeQuerySQL(sql);
		if (aryList == null) {
			throw new ControllableException("请检查开始日期及结束日期是否正确!");
		}
		int dates = Integer.parseInt(aryList.get(0).getString("count"));
		return hours * dates;
	}

	/**
	 * 根据开始时间及工时,计算出结束时间(默认工作8小时制)
	 * 
	 * @author pengfei
	 * @param startDate
	 * @param workHours
	 * @return
	 * @throws SQLException
	 */
	public static Date computeEndDate(Date startDate, String workHours)
			throws SQLException {
		return computeEndDate(startDate, workHours, 8, "");
	}

	/**
	 * 根据开始时间及工时,计算出结束时间(默认工作8小时制)
	 * 
	 * @author pengfei
	 * @param startDate
	 * @param workHours
	 * @return
	 * @throws SQLException
	 */
	public static Date computeEndDate(Date startDate, String workHours,
			String taskId) throws SQLException {
		return computeEndDate(startDate, workHours, 8, taskId);
	}

	/**
	 * 根据开始时间,工时及工时制(8小时制或者是12小时制),计算出结束时间
	 * 
	 * @param startDate
	 * @param workHours
	 * @param hours
	 * @return
	 * @throws SQLException
	 */
	public static Date computeEndDate(Date startDate, String workHours,
			int hours, String taskId) throws SQLException {
		if (!StringUtils.isBlank(taskId)) {
			Double d = ComputeTime.computeEndDateHour(taskId);
			if (d > 0) {
				Double workH = Double.parseDouble(workHours);
				workH = workH + d;
				System.out.println("ddddd啊" + workHours + "哎" + workH);
				workHours = workH + "";
			}
		}
		return ComputeTime.computeDate(startDate, workHours, hours);
	}

	/**
	 * 根据上个任务的结束时间,及任务ID,计划开始时间(默认工作8小时制)
	 * 
	 * @author pengfei
	 * @param startDate
	 * @param workHours
	 * @return
	 * @throws SQLException
	 */
	public static Date computeStartDate(Date startDate, String workHours,
			String taskId) throws SQLException {
		return computeStartDate(startDate, workHours, 8, taskId);
	}

	/**
	 * 根据上个任务的时间,工时及工时制(8小时制或者是12小时制),及任务ID,计算出开始时间
	 * 
	 * @author pengfei
	 * @param startDate
	 * @param workHours
	 * @param hours
	 * @return
	 * @throws SQLException
	 */
	public static Date computeStartDate(Date startDate, String workHours,
			int hours, String taskId) throws SQLException {
		Double endhours = ComputeTime.computeEndDateHour(taskId);
		if (endhours == 0) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			startDate = cal.getTime();
		}
		return ComputeTime.computeDate(startDate, workHours, hours);
	}

	public static Date computeDate(Date startDate, String workHours, int hours)
			throws SQLException {
		int dates = 0;

		Double workH = Double.parseDouble(workHours);
		if (workH % hours > 0) {
			dates = dates + 1;
		}
		dates = (int) (dates + workH / hours);
		if (dates == 0)
			dates = 1;
		String startD = ComputeTime.getDateString(startDate);
		String sql = "select * from (select * from oa.HOLIDAY where to_char(DATES,'yyyy-MM-dd')>='"
				+ startD
				+ "' and STATE=1 order by DATES) where rownum<='"
				+ dates + "'";
		List<BizObject> aryList = QueryFactory.executeQuerySQL(sql);
		if (aryList.size() <= 0) {
			throw new ControllableException("请检查开始时间是否正确!");
		}
		return aryList.get(aryList.size() - 1).getDate("DATES");
	}

	/**
	 * 根据任务查找,当前使用的最后一天是否是占满一天满的,如果只占其中几个小时,则当前的任务还可以用那天 (默认8小时制)
	 * 
	 * @author pengfei
	 * @param taskId任务ID
	 * @return
	 * @throws SQLException
	 */
	public static Double computeEndDateHour(String taskId) throws SQLException {
		return ComputeTime.computeEndDateHour(taskId, 8);
	}

	/**
	 * 根据任务查找,当前使用的最后一天是否是占满一天满的,如果只占其中几个小时,则当前的任务还可以用那天
	 * 
	 * @author pengfei
	 * @param taskId任务ID
	 * @param hour
	 *            指工时及工时制(如:8,12)
	 * @return
	 * @throws SQLException
	 */
	public static Double computeEndDateHour(String taskId, int hour)
			throws SQLException {
		// 得到当前任务以及前置任务的工时
		Double d = 0.0;
		// 循环得到当前任务以及前置任务,以及前置任务的前置任务的所有这一条线的总工时
		while (1 == 1) {
			// 得到任务对象
			BizObject task = ComputeTime.getTask(taskId);
			// 总工时+当前任务的工时
			d = d + task.getDouble("PLANHOUR", 0)
					+ task.getDouble("JOINDATE", 0);
			// 如果当前任务的前置任务为空,则退出循环
			if (StringUtils.isBlank(task.getString("PREFIXTASK"))) {
				break;
			}
			// 把进入下一次循环的任务ID设为当前任务的前置任务
			taskId = task.getString("PREFIXTASK");
		}
		// 求余工时,得到最后一天占用了几个小时
		return d % hour;
	}

	/**
	 * 根据任务ID,得到任务对象
	 * 
	 * @author pengfei
	 * @param taskId
	 * @return
	 * @throws SQLException
	 */
	public static BizObject getTask(String taskId) throws SQLException {
		QueryFactory qf = new QueryFactory("PROJECTPLANTASK");
		BizObject task = qf.getByID(taskId);
		Validate.notNull(task, "任务ID不正确,请核对!");
		return task;
	}

	/**
	 * 解析字符串,一组数据中用分号隔开,同一组数据中的每个元素用逗号隔开
	 * 
	 * @author pengfei
	 * @param str
	 * @return
	 */
	public static List<BizObject> parseString(String str) {
		StringTokenizer st = new StringTokenizer(str, ";");

		List<BizObject> aryList = new ArrayList<BizObject>();

		while (st.hasMoreTokens()) {
			String deptAndUser = (String) st.nextElement();
			StringTokenizer stk = new StringTokenizer(deptAndUser, ",");
			while (stk.hasMoreTokens()) {
				BizObject biz = new BizObject();
				biz.set("ELEMENT1", (String) stk.nextElement());
				biz.set("ELEMENT2", (String) stk.nextElement());
				aryList.add(biz);
			}
		}
		return aryList;
	}

	/**
	 * 解析字符串,一组数据中用分号隔开,同一组数据中的每个元素用|隔开
	 * 
	 * @author pengfei
	 * @param str
	 * @return
	 */
	public static List<BizObject> parseString2(String str) {
		StringTokenizer st = new StringTokenizer(str, ";");

		List<BizObject> aryList = new ArrayList<BizObject>();

		while (st.hasMoreTokens()) {
			String deptAndUser = (String) st.nextElement();
			StringTokenizer stk = new StringTokenizer(deptAndUser, "|");
			while (stk.hasMoreTokens()) {
				BizObject biz = new BizObject();
				biz.set("ELEMENT1", (String) stk.nextElement());
				biz.set("ELEMENT2", (String) stk.nextElement());
				aryList.add(biz);
			}
		}
		return aryList;
	}

	/**
	 * 解析字符串,一组数据中用分号隔开,同一组数据中的每个元素用分号隔开
	 * 
	 * @author pengfei
	 * @param str
	 * @return
	 */
	public static List<BizObject> parseColonString(String str) {
		StringTokenizer st = new StringTokenizer(str, ";");

		List<BizObject> aryList = new ArrayList<BizObject>();

		while (st.hasMoreTokens()) {
			String deptAndUser = (String) st.nextElement();
			StringTokenizer stk = new StringTokenizer(deptAndUser, ":");
			while (stk.hasMoreTokens()) {
				BizObject biz = new BizObject();
				biz.set("ELEMENT1", (String) stk.nextElement());
				biz.set("ELEMENT2", (String) stk.nextElement());
				aryList.add(biz);
			}
		}
		return aryList;
	}

	/**
	 * 把JSON中的字符串的换行,又引号进行转义
	 * 
	 * @author pengfei
	 * @param str
	 * @return
	 */
	public static String replace(String str) {
		str = str.replace("\r\n", "\\n");
		str = str.replace("\n", "\\n");
		str = str.replace("\"", "\\\"");
		// str = str.replace("\\", "\\\\");
		// str = StringUtils.replace(str, "\\", "\\\\");
		// System.out.println("str is "+str);
		return str;
	}

	/**
	 * 根据日期,得到当前日期是星期几的字符串
	 * 
	 * @author pengfei
	 * @param date
	 * @return
	 */
	public static String getWeeks(Date date) {
		return ComputeTime.formatDate(date, "E");
	}

	/**
	 * 根据日期,格式化字符串
	 * 
	 * @author pengfei
	 * @param dayForWeek
	 * @return
	 * @throws ServletException
	 * @throws SQLException
	 */
	public static String formatDate(Date workTime, String pattern) {
		SimpleDateFormat sim = new SimpleDateFormat(pattern);
		return sim.format(workTime);
	}

	/**
	 * 根据传进来的一个日期对象与当前的日期作对比,如果当前的日期大于传进来的日期,则返回一个延期
	 * 
	 * @author pengfei
	 * @param planEndDate
	 * @return 0:代表延期,1代表未延期
	 * @throws ServletException
	 * @throws SQLException
	 */
	public static String isPostPoned(Date planEndDate) throws ServletException,
			SQLException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(planEndDate);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		if (calendar.getTimeInMillis() < calendar2.getTimeInMillis()) {
			return "1";
		}
		return "0";
	}

	/**
	 * 根据两个数计算百分比
	 * 
	 * @author pengfei
	 * @param divisor
	 *            除数
	 * @param dividend
	 *            被除数
	 * @return 返回一个四舍五入后保留两位小数的百分比
	 */
	public static String computePercent(int divisor, int dividend) {
		float f = divisor * 100000 / dividend;
		BigDecimal big = new BigDecimal("0.00000");
		big = new BigDecimal(f).divide(new BigDecimal(100000));
		float f1 = big.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
		DecimalFormat de = new DecimalFormat("0.00%");
		return de.format(f1);
	}

	/**
	 * 根据两个数值,得到两个数值的之商,并四舍五入保留四位小数
	 * 
	 * @author pengfei
	 * @param divisor
	 *            除数
	 * @param dividend
	 *            被除数
	 * @return 四舍五入后保留四位小数的Double类型
	 */
	public static Double computeFourNumerical(int divisor, int dividend) {
		float f = divisor * 100000 / dividend;
		BigDecimal big = new BigDecimal("0.00000");
		big = new BigDecimal(f).divide(new BigDecimal(100000));
		float f1 = big.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
		DecimalFormat de = new DecimalFormat("0.0000");
		return Double.parseDouble(de.format(f1));
	}

	/**
	 * 根据两个数值,得到两个数值的之商,并四舍五入保留两位小数
	 * 
	 * @author pengfei
	 * @param divisor
	 *            除数
	 * @param dividend
	 *            被除数
	 * @return 四舍五入后保留两位小数的Double类型
	 */
	public static Double computeTwoNumerical(Double divisor, Double dividend) {
		if (dividend == 0) {
			return 0.0;
		}
		Double f = divisor * 1000 / dividend;
		BigDecimal big = new BigDecimal("0.000");
		big = new BigDecimal(f).divide(new BigDecimal(1000));
		Double f1 = big.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		DecimalFormat de = new DecimalFormat("0.00");
		return Double.parseDouble(de.format(f1));
	}

	/**
	 * 根据两个数值,得到两个数值的之商,并四舍五入保留两位小数,如:计算出来的结果55%,即0.55,而最后返回的结果却是55
	 * 
	 * @author pengfei
	 * @param divisor
	 *            除数
	 * @param dividend
	 *            被除数
	 * @return 四舍五入后保留两位小数的Double类型
	 */
	public static Double computeNumerical(Double divisor, Double dividend) {
		if (dividend == 0) {
			return 0.0;
		}
		Double f = divisor * 1000 / dividend;
		BigDecimal big = new BigDecimal("0.000");
		big = new BigDecimal(f).divide(new BigDecimal(10));
		Double f1 = big.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		DecimalFormat de = new DecimalFormat("0.00");
		return Double.parseDouble(de.format(f1));
	}
	
	/**
	 * 计划单价,根据总金额和总数量计算出单价,如果数量小于等于零,则直接返回零(按照财务的标准是价格必须是6位小数位)

	 * @param total
	 * @param amount
	 * @return
	 */
	public static BigDecimal computePrice(BigDecimal total,BigDecimal amount){
		if(amount.compareTo(new BigDecimal("0"))<=0){
			return new BigDecimal("0").setScale(6,RoundingMode.HALF_UP);
		}
		BigDecimal price = total.divide(amount,8,RoundingMode.HALF_UP).setScale(6,RoundingMode.HALF_UP);
		return price;
	}
	
	/**
	 * 计划总金额,根据单价和数量计算出总金额(按照财务的标准,金额是两位小数位)
	 * 
	 * @param price
	 * @param amount
	 * @return
	 */
	public static BigDecimal computeTotal(BigDecimal price,BigDecimal amount){
		BigDecimal total = price.multiply(amount).setScale(2,RoundingMode.HALF_UP);
		return total;
	}

	/**
	 * 根据传进来的double类型格式化保留六位小数点
	 * 
	 * @author pengfei
	 * @param num
	 * @return
	 */
	public static Double numericalFormat(Double num) {
		if (num > 0) {
			DecimalFormat de = new DecimalFormat("0.000000");
			return Double.parseDouble(de.format(num));
		}
		return 0.000000;
	}

	public static Double numbericalSixFormat(BigDecimal bigd) {
		// DecimalFormat de = new DecimalFormat("0.000000");
		return bigd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 保存多个附件
	 * 
	 * @author pengfei
	 * @param annex
	 * @param files
	 * @throws SQLException
	 */
	public void saveAnnex(BizObject annex, List<File> files)
			throws SQLException {
		for (File f : files) {
			annex.setID("");
			annex.set("type", Document.TYPE_TASKPIC);
			Document.updateAnnex(annex, f);
		}
	}

	/**
	 * 删除附件
	 * 
	 * @author pengfei
	 * @param bizId
	 * @throws SQLException
	 */
	public void deleteAnnexList(String bizId) throws SQLException {
		QueryFactory qf = new QueryFactory("annex");
		List<BizObject> annexList = qf.query("bizid", bizId);
		for (BizObject annex : annexList) {
			Document.delete(annex.getId());
		}
	}

	/**
	 * @throws SQLException
	 * @param args
	 * @throws ParseException
	 * @throws SQLException
	 * @throws
	 */
	public static void main1(String[] args) throws ParseException, SQLException {

		Double d = 0.0;
		if (d == 0) {
			System.out.println("哎呀");
		} else {
			System.out.println("原来还能这样");
		}

		System.out.println(ComputeTime.computePercent(2, 3));

		System.out.println(ComputeTime.getWeeks(new Date()));

		System.out
				.println(NumberFormat.getPercentInstance().format(0.52256255));

		System.out.println(ComputeTime.computeNumerical(2.0, 3.0));

		// List tableNames = new ArrayList();
		// tableNames.add("PROJECTPLANTASK");
		// List schemas = new ArrayList();
		// schemas.add("DEPOT");
		// schemas.add("BASIC");
		// schemas.add("OA");
		// InitialConn.initial(tableNames, schemas);
		// System.out.println(ComputeTime.computeEndDateHour("PROJ8779310"));
		//
		// Integer i = 10;
		// Integer x = 10;
		// System.out.println(i + x);

		// double i = 230.2;
		// if (i % 8 > 0) {
		// System.out.println("大于" + 1);
		// }
		// System.out.println("i % 8" + (0 % 8));
		// System.out.println((int) (0 / 8 + 20));
		//
		// BigDecimal big = new BigDecimal(8);
		// big = big.multiply(new BigDecimal("8.4"));
		// System.out.println(big);
	}

	public static void main(String[] args) throws ServletException,
			SQLException, ParseException {
		// System.out.println("u005C ");
		// double i=1200;
		// double j=2500.23;
		// System.out.println(j%i);
		// System.out.println(Math.ceil(j));
		// System.out.println(j%i);
		// if(j%i>0){
		// j=j-(j%i)+i;
		// }
		// System.out.println(j);
		// System.out.println(ComputeTime.numbericalSixFormat(new
		// BigDecimal(0.25487563841687)));
		// System.out.println(Double.parseDouble("15795")/Double.parseDouble("20.26644885"));
		// System.out.println(new BigDecimal("15795").multiply(new
		// BigDecimal(100)).divide(new
		// BigDecimal("34315135.537014"),2,RoundingMode.HALF_UP));
		Date d = ComputeTime.getMonthDate(new Date(), 14);
		System.out.println(d);
		
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM");
		System.out.println(sim.format(new Date()));
//		String date = "2010-04";
//		System.out.println(ComputeTime.getDate(date+"-15"));
	}

}
