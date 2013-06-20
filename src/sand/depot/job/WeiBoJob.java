package sand.depot.job;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sand.actionhandler.weibo.UdaClient;
import sand.depot.tool.system.SystemKit;
import tool.dao.BizObject;
import tool.dao.JDO;
import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.http.AccessToken;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class WeiBoJob extends BaseJob {

	
	public void insertUser(User user) throws SQLException{
		//if(user.get)
		BizObject b = new BizObject("weibo_user");
		b.set("id", user.getId());
		if(b.getQF().getByID(user.getId())!=null){
			this.log(user.getId()+" exist,continue ");
			return;
		}
			
		
		b.set("screenName", user.getScreenName());
		b.set("name", user.getName());
		b.set("province", user.getProvince());
		b.set("city",user.getCity());
		b.set("location", user.getLocation());
		b.set("description", user.getDescription());
		b.set("url", user.getUrl());
		b.set("profileImageUrl", user.getProfileImageUrl());
		b.set("userDomain", user.getUserDomain());
		b.set("gender", user.getGender());
		b.set("createdat", user.getCreatedAt());
		
		this._jdo.addOrUpdate(b);
	}

	public void insertStatus(Status s) throws SQLException{
		BizObject b = new BizObject("weibo_status");
		b.set("id", s.getId());
		if(b.getQF().getByID(s.getId())!=null){
			this.log(s.getId()+" exist; continue;");
			return;
		}
		b.set("createdat", s.getCreatedAt());
		b.set("userid", s.getUser().getId());	
		b.set("username", s.getUser().getName());
		this.log(" text :" +s.getText().length());
		b.set("mid", s.getMid());	
		b.set("text", s.getText());
		b.set("source", s.getSource().getName());
		b.set("favorited", s.isFavorited());
		b.set("inReplyToStatusId", s.getInReplyToStatusId());
		b.set("inReplyToUserId", s.getInReplyToUserId());
		b.set("inReplyToScreenName", s.getInReplyToScreenName());
		b.set("thumbnailPic", s.getThumbnailPic());
		b.set("bmiddlePic", s.getBmiddlePic());
		b.set("originalPic", s.getOriginalPic());
//		b.set("retweetedStatus", s.getRetweetedStatus().getId());
		String picurl = UdaClient.download(s.getOriginalPic());
		if(!picurl.equals("")){
			int i=picurl.indexOf("/erp.upload");
			b.set("picurl", picurl.substring(i));
		}
		b.set("geo", s.getGeo());
		b.set("latitude", s.getLatitude());
		b.set("longitude", s.getLongitude());
		b.set("repostsCount", s.getRepostsCount());
		b.set("commentsCount", s.getCommentsCount());
		
		this._jdo.addOrUpdate(b);
	}
	@Override
	public String run() throws Exception {
		// TODO Auto-generated method stub
		
		List<BizObject> users = SystemKit.getCachePickList("weibo_users");
		int i=0;
		for(BizObject b:users){
			
			AccessToken accesstoken =UdaClient.getToken(b.getString("id"), b.getString("name"));
			this.log("accesstoken "+accesstoken);

			String access_token = accesstoken.getAccessToken();
			Timeline tm = new Timeline();
			tm.client.setToken(access_token);
		//	String result="";
			try {
				StatusWapper status = tm.getFriendsTimeline();
				//result = status.getStatuses().size()+"  ";
				List v=new ArrayList();
				for(Status s : status.getStatuses()){

					User user = s.getUser();
					insertUser(user);
					insertStatus(s);
					i++;
					//result = result + s.getUser().getName()+":"+s.getText()+"<br>";
				}

			} catch (WeiboException e) {
				super.log("",e);
				e.printStackTrace();
				return JDO.getStackTrace(e);
			}
			
		}
 		
		return super.OK+"，共入库 "+i+"条微博";
	}

}
