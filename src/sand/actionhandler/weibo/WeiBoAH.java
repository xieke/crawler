package sand.actionhandler.weibo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.AccessControl;
import sand.annotation.Ajax;
import tool.dao.BizObject;
import tool.dao.QueryFactory;
import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.http.AccessToken;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

@AccessControl("no")
public class WeiBoAH extends ActionHandler {

	public static AccessToken  accesstoken;
	
	public WeiBoAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}
	 /**
	  * 
	  * @return
	 * @throws IOException 
	 * @throws ServletException 
	  */
	 public void weiboAuth() throws WeiboException, ServletException, IOException{
		 


			Oauth oauth = new Oauth();
			String url=oauth.authorize("code","","");
			System.out.println("url "+url);
			this.dispatch(url);
			//BareBonesBrowserLaunch.openURL(url);
			System.out.println(oauth.authorize("code","",""));
			System.out.print("Hit enter when it's done.[Enter]:");
		
		 
	 }
		
		@Ajax
	 public String testV() throws SQLException{
		   QueryFactory qf = new QueryFactory("v_dictionary2");
		   return qf.query().size()+"";
	 }
	 /**
	  * 接受微博授权
	 * @throws WeiboException 
	 * @throws IOException 
	 * @throws ServletException 
	  */
	 //@Ajax
	 public void weibo() throws WeiboException, ServletException, IOException{
		 
		 	Oauth oauth = new Oauth();
		 	String code=this.getParameter("code");
		 	if(code.equals("")) 
		 		
		 		{
		 		
				//Oauth oauth = new Oauth();
				String url=oauth.authorize("code","","");
				System.out.println("url "+url);
				this.dispatch(url);

		 		}
		 	else{
		 		accesstoken =oauth.getAccessTokenByCode(code);	
		 		this.dispatch("/weibo.WeiBoAH.readWeibo");
		 	}
		 	
		 	//System.out.println();
		 	
		 	//return accesstoken.getAccessToken();
	 }
	 
	// @Ajax
	 public void autoLogin() throws Exception{
		 accesstoken =UdaClient.getToken("tarzon@21cn.com", "qjdble597969");
		this.log("accesstoken "+accesstoken);
		//return accesstoken.getAccessToken();
		 this.dispatch("/weibo.WeiBoAH.readWeibo");
	 }
		// @Ajax
	 public void autoLogin2() throws Exception{
		 accesstoken =UdaClient.getToken("13508222@qq.com", "qjdble597969");
		this.log("accesstoken "+accesstoken);
		//return accesstoken.getAccessToken();
		 this.dispatch("/weibo.WeiBoAH.readWeibo");
	 }
	 
	 /**
	  * 读取关注好友的微博
	  */
	// @Ajax
	 public void readWeibo(){
			String access_token = accesstoken.getAccessToken();
			Timeline tm = new Timeline();
			tm.client.setToken(access_token);
			String result="";
			try {
				//StatusWapper status = tm.getStatus2();
				StatusWapper status = tm.getFriendsTimeline();
				result = status.getStatuses().size()+"  ";
				List v=new ArrayList();
				for(Status s : status.getStatuses()){
					BizObject b = new BizObject();
					b.set("name", s.getUser().getName());
					b.set("text",s.getText());
					b.set("pic", s.getOriginalPic());
//					s.getUser().getavatarLarge();
					//s.getCreatedAt()
					b.set("createdat", s.getCreatedAt());
					//b.set("posttimestr", b.getFormatedDate("posttime"));
					b.set("CommentsCount", s.getCommentsCount());
					v.add(b);
		//			s.getText();
					Log.logInfo(s.toString());
					//s.getCommentsCount();
					result = result + s.getUser().getName()+":"+s.getText()+"<br>";
				}
				this.setAttribute("objList",v);
//				System.out.println(status.getNextCursor());
//				System.out.println(status.getPreviousCursor());
//				System.out.println(status.getTotalNumber());
//				System.out.println(status.getHasvisible());
			} catch (WeiboException e) {
				super.log("",e);
				e.printStackTrace();
			}
			this._nextUrl="/weibolist.jsp";
			//return result;

	 }
	 
	 /**
	  * 读取关注好友的微博
	 * @throws SQLException 
	  */
	// @Ajax
	 public void listWeibo() throws SQLException{

				//StatusWapper status = tm.getFriendsTimeline();
				//result = status.getStatuses().size()+"  ";
//				BizObject s = new BizObject("weibo_status");
//				List<BizObject> v=s.getQF().query(this.preparePageVar());
//				this.setAttribute("objList",v);
				
				this.setOrderBy("createdat desc");
				this.listObj("weibo_status");
//				System.out.println(status.getNextCursor());
//				System.out.println(status.getPreviousCursor());
//				System.out.println(status.getTotalNumber());
//				System.out.println(status.getHasvisible());

			this._nextUrl="/weibolist.jsp";
			//return result;

	 }
}
