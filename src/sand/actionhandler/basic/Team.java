package sand.actionhandler.basic;

import java.sql.SQLException;
import java.util.List;

import sand.actionhandler.system.ActionHandler;
import sand.depot.tool.system.ControllableException;
import tool.dao.BizObject;
import tool.dao.PageVariable;
import tool.dao.QueryFactory;

public class Team {

	String teamid; //团队id
	BizObject team;
	List<BizObject> team_members;
//	public void setID(String teamid){
//		teamid=teamid;
//	};
	/**
	 * 创建一个临时群
	 * @param users
	 * @throws SQLException 
	 */
	public static String createTempTeam(BizObject task,List<BizObject> users,String teamid) throws SQLException{
		BizObject team = new BizObject("team");
		team.set("taskid", task.getId());
		team.set("teamname", task.getString("name")+"临时团队");
		team.set("originteamid",teamid);
		ActionHandler.currentSession().addOrUpdate(team);
		for(BizObject user:users){
			BizObject re= new BizObject("re_team_user");
			re.set("teamid", team.getId());
			re.set("userid", user.getString("userid"));
			re.set("state", 1);
			ActionHandler.currentSession().addOrUpdate(re);			
		}
		return team.getId();
	}
	
	public static List<BizObject> getTeamList() throws SQLException{
		BizObject b = new BizObject("team");
	//	b.setMinValue("location", 5);
		b.setOrderBy("teamscore desc");
		List<BizObject> v = b.getQF().query(b,new PageVariable(5));		
		return v;

	}
	public BizObject getTeamBiz(){
		return team;
	}
	public List<BizObject> getTeamApplyMembers() throws SQLException{
		//List<BizObject> v = team.getList("re_team_user");
		BizObject re=new BizObject("re_team_user");

		re.set("teamid", team.getId());
		re.set("state", 0);
		List v = re.getQF().query(re);
		return v;
	}
	public List<BizObject> getTeamMembers() throws SQLException{
		//List<BizObject> v = team.getList("re_team_user");
		BizObject re=new BizObject("re_team_user");

		re.set("teamid", team.getId());
		re.set("state", 1);
		List v = re.getQF().query(re);
		return v;
	}	
	public Team() throws SQLException{
		super();
	}
	public Team(String teamid) throws SQLException{
		this.teamid=teamid;
		this.team = this.getTeamById(teamid);
		if(team==null)
			throw new ControllableException(teamid+" team 不存在");
	}
	/**
	 * 返回 1 表示 正式团队成员  0表示 申请待审核,2标示邀请待确认，-1标示不是
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public  int hasTeamMember(String userid) throws SQLException{
		team_members=team.getList("re_team_user");
		for(BizObject b:team_members){
			if (b.getString("userid").equals(userid)){
				return b.getInt("state",0);
			}			
		}
		
		return -1;
		
	}
	public BizObject getTeamById(String teamId) throws SQLException{
		QueryFactory qf = new QueryFactory("team");
		return qf.getByID(teamId);
	}

}
