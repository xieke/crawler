package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import sand.actionhandler.system.ActionHandler;
import sand.depot.business.system.Employee;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;
import tool.dao.JDO;

public class Wealth {
	
	private static Logger logger = Logger.getLogger(Wealth.class);
	/**
	 * 增加金币
	 * @return
	 * @throws SQLException 
	 */
	public static void changeGold(BizObject user,int amount,String bizid,String remark) throws SQLException{
		JDO _jdo=ActionHandler.currentSession();
		_jdo.beginTrans();
		
		if(user.getId().equals(""))
			throw new ControllableException("错误的对象id");
		user.refresh();
		logger.info("user is "+user.getTableName());
		if(user.getName().equalsIgnoreCase("employee")){
			BizObject wl = new BizObject("wealthlog");
			wl.set("userid", user.getId());
			wl.set("goldsnap", user.getString("udagold"));
			//wl.set("scoresnap", this._employee.getString("udascore"));
			wl.set("goldchange", amount);
			wl.set("bizid", bizid);
			wl.set("remark", remark);
			//logger.info(_employee.getInt("udagold", 0) +" + "+amount+" = "+);
			logger.info(user.getInt("udagold", 0) +" + "+amount+" = ");
			user.set("udagold", user.getInt("udagold", 0)+amount);
			
			_jdo.add(wl);
			_jdo.update(user);
			if(user.getId().equals(ActionHandler.currentUser().getEmployee().getId())){
				ActionHandler.currentUser().getEmployee().refresh();
			}
			
		}
		else if(user.getName().equalsIgnoreCase("team")){
			
			List<BizObject> reusers = user.getList("re_team_user");
			int usercount = reusers.size();
			int average = amount/usercount;
			int remain =amount;
			for(BizObject b:reusers){
				Wealth.changeGold(b.getBizObj("userid"), average, bizid,remark);
				remain=remain-average;
			}
			
			/**
			 * 剩下零头给 团队领队
			 */
			BizObject leader = user.getBizObj("leader");
			if(leader!=null){
				Wealth.changeGold(leader, remain, bizid,remark);
				
			}
	}
			
	}
//	/**
//	 * 扣除金币
//	 * @return
//	 */
//	public void subGold(int amount,String bizid){
//		
//	}	
	/**
	 * 增加积分
	 * @return
	 * @throws SQLException 
	 */
	public static void changeScore(BizObject user,int amount,String bizid,String remark) throws SQLException{
		JDO _jdo=ActionHandler.currentSession();
		_jdo.beginTrans();
		if(user.getId().equals(""))
			throw new ControllableException("错误的对象id");

		user.refresh();
		BizObject wl = new BizObject("wealthlog");
		
		if(user.getName().equalsIgnoreCase("employee")){
			wl.set("userid", user.getId());
			//wl.set("goldsnap", this._employee.getString("udagold"));
			wl.set("scoresnap", user.getString("udascore"));
			wl.set("scorechange", amount);
			wl.set("bizid", bizid);
			wl.set("remark", remark);
			logger.info(user.getInt("udascore", 0) +" + "+amount+" = ");
			user.set("udascore", user.getInt("udascore", 0)+amount);
			
			_jdo.add(wl);
			_jdo.update(user);
			if(user.getId().equals(ActionHandler.currentUser().getEmployee().getId())){
				ActionHandler.currentUser().getEmployee().refresh();
			}
			
		}
		else if(user.getName().equalsIgnoreCase("team")){
				
				List<BizObject> reusers = user.getList("re_team_user");
				int usercount = reusers.size();
				int average = amount/usercount;
				int remain =amount;
				for(BizObject b:reusers){
					Wealth.changeScore(b.getBizObj("userid"), average, bizid,remark);
					remain=remain-average;
				}
				
				/**
				 * 剩下零头给 团队领队
				 */
				BizObject leader = user.getBizObj("leader");
				if(leader!=null){
					Wealth.changeScore(leader, remain, bizid,remark);
				}
		}
	}

	/**
	 * 增加积分
	 * @return
	 * @throws SQLException 
	 */
	public static void changeAbility(BizObject user,int amount,String bizid,String remark) throws SQLException{
		JDO _jdo=ActionHandler.currentSession();
		_jdo.beginTrans();
		if(user.getId().equals(""))
			throw new ControllableException("错误的对象id");

		user.refresh();
		BizObject wl = new BizObject("wealthlog");
		
		if(user.getName().equalsIgnoreCase("employee")){
			wl.set("userid", user.getId());
			//wl.set("goldsnap", this._employee.getString("udagold"));
			wl.set("abilitysnap", user.getString("udaability"));
			wl.set("abilitychange", amount);
			wl.set("bizid", bizid);
			wl.set("remark", remark);
			logger.info(user.getInt("udaability", 0) +" + "+amount+" = ");
			user.set("udaability", user.getInt("udaability", 0)+amount);
			
			_jdo.add(wl);
			_jdo.update(user);
			if(user.getId().equals(ActionHandler.currentUser().getEmployee().getId())){
				ActionHandler.currentUser().getEmployee().refresh();
			}
			
		}
		else if(user.getName().equalsIgnoreCase("team")){
				
				List<BizObject> reusers = user.getList("re_team_user");
				int usercount = reusers.size();
				int average = amount/usercount;
				int remain =amount;
				for(BizObject b:reusers){
					Wealth.changeAbility(b.getBizObj("userid"), average, bizid,remark);
					remain=remain-average;
				}
				
				/**
				 * 剩下零头给 团队领队
				 */
				BizObject leader = user.getBizObj("leader");
				if(leader!=null){
					Wealth.changeAbility(leader, remain, bizid,remark);
				}
		}
	}

}
