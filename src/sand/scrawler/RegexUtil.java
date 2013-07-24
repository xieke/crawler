package sand.scrawler;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtil {
public static void main(String[] args) {


       String input = "<p align=\"center\"><img alt=\"\" WIDTH=\"1000\" height=\"1000\" src=\"http://media.people.com.cn/mediafile/200806/30/F200806300819101721101382.jpg\" /></p><p class=\"pictext\" align=\"center\">倪萍（资料图）</p><p>　　推荐阅读：</p><p>　　●主持人资料库&mdash;&mdash;倪萍、曹颖</p><p>　　●出走央视三年 文清碌碌无为当演员沦为影视配角</p><p>　　●川籍主持人李佳明回归央视 不再主持《开心辞典》</p><p>　　●从&ldquo;解说门&rdquo;到&ldquo;走穴&rdquo; 当央视名嘴遭遇尴尬</p><p>　　●那些从央视离开的主持人</p><p>　　●央视综艺主持人纷纷出走的三个原因</p><p>　　●毅然挥手告别　离开央视的&ldquo;名嘴&rdquo;们</p><p>　　文清从央视的这兴奋一跳，并没有跳到另一个高峰，相反落到一个低洼的谷地，那么在她之前先后离开主持岗位的曹颖、赵琳、倪萍又是怎样一幅景象呢？现在也是她们该晒一晒的时候了，不过一眼望过去并非惨不忍睹，而是有喜也有忧。</p><p>　　<strong>倪萍 影视圈也当大姐</strong></p><p>　　倪萍是中国观众最熟悉不过的央视主持人了，如果没有她的眼泪，中国的电视屏幕将会缺少很多真情。但是后来，有一天她突然说出了自己的肺腑之言：&ldquo;我曾经拿着话筒跑在最前面，但这样持续跑下去，终究会是最后一名，我已经感觉跑不动了。&rdquo;</p><p>　　于是，几年前，在央视规定很严的情况下，她被破例批准在外参演影视作品，而倪萍也的确没让央视的领导们失望，在第２２届金鸡百花奖颁奖礼上，她凭借《美丽的大脚》，一举拿下了金鸡奖的影后桂冠。</p><p>　　如今，倪萍早已将关系转到中国电视剧制作中心，全心拍摄影视剧，《两个人的芭蕾》、《泥鳅也是鱼》等影响之作陆续诞生。</p><p>　　从杨澜手中接过《综艺大观》的话筒，倪萍走向了主持舞台的辉煌，而如今离开那个位置，她依旧没有迷失，在影视圈还是保持着大姐的风范。</p><!--分页--><p align=\"center\"><img alt=\"\" width=1000 height=\"1000\" src=\"http://media.people.com.cn/mediafile/200806/30/F200806300819112389715921.jpg\" /></p><p class=\"pictext\" align=\"center\">赵琳（资料图）</p><p>　　<strong>倪萍 影视圈也当大姐</strong></p><p>　　倪萍是中国观众最熟悉不过的央视主持人了，如果没有她的眼泪，中国的电视屏幕将会缺少很多真情。但是后来，有一天她突然说出了自己的肺腑之言：&ldquo;我曾经拿着话筒跑在最前面，但这样持续跑下去，终究会是最后一名，我已经感觉跑不动了。&rdquo;</p><p>　　于是，几年前，在央视规定很严的情况下，她被破例批准在外参演影视作品，而倪萍也的确没让央视的领导们失望，在第２２届金鸡百花奖颁奖礼上，她凭借《美丽的大脚》，一举拿下了金鸡奖的影后桂冠。</p><p>　　如今，倪萍早已将关系转到中国电视剧制作中心，全心拍摄影视剧，《两个人的芭蕾》、《泥鳅也是鱼》等影响之作陆续诞生。</p><p>　　从杨澜手中接过《综艺大观》的话筒，倪萍走向了主持舞台的辉煌，而如今离开那个位置，她依旧没有迷失，在影视圈还是保持着大姐的风范。</p><p>　　<strong>赵琳 开门红后没起色</strong></p><p>　　当年，赵琳因主持央视《生活》栏目，曾荣获&ldquo;华鹤杯&rdquo;全国电视&ldquo;十佳&rdquo;经济节目主持人。２００２年，赵琳辞去央视主持人职位后，受赵宝刚导演邀约出演荧屏处女作《别了，温哥华》，饰演剧中女主角&ldquo;任小雪&rdquo;，自此踏上影视之路。</p><p>　　《别了，温哥华》在观众中反响热烈，也算是给赵琳的转型之路来了一个开门红，还凭借在该剧中出色表现，荣获中国电视&ldquo;双十佳&rdquo;女演员奖。</p><p>　　就在大家纷纷猜测赵琳从此将大红大紫的时候，她接下来主演的《重返上海滩》、《基因之战》、《录像带》、《危情２４小时》、《血色残阳》等作品，虽然有一定知名度，可她本人的表现并不突出，并未给观众继续带来惊喜。而且在网络还流传，赵琳的感情生活也犯迷糊，被一个假军人骗钱骗色。</p><p>　　如今，在镜头前沉沉浮浮的赵琳又找到了赵宝刚，加盟了有陆毅、朱雨辰、王珞丹等出演的《我的青春谁做主》，我们都期待她能借赵宝刚的功力再次冒头。</p><!--分页--><p align=\"center\"><img alt=\"\" width = 1000  height=\"1000\" src=\"http://media.people.com.cn/mediafile/200806/30/F200806300819101426445281.jpg\" /></p><p class=\"pictext\" align=\"center\">曹颖（资料图）</p><p>　　<strong>曹颖 拍戏主持两不误</strong></p><p>　　在央视主持《万家灯火》、《综艺大观》的曹颖，是观众公认的实力派主持人，可是她在央视眼里却是一名不安分的员工，因为就在她拿着央视薪水的同时，也在外面疯狂地接拍影视剧，于是，忍无可忍的央视给她下了最后通牒，要她在拍戏和央视两者之间做出选择。始终无法忽略自己对电视剧的喜爱，曹颖最终做了艰难的选择&mdash;&mdash;为演戏而离开央视。</p><p>　　看来曹颖的选择是明智的，在影视圈里，她在《日落紫禁城》里可爱的封库伦公主、《我想嫁给你》里美丽多病的白雪、《文成公主》里高贵大方的文成公主、《凤在江湖》里冷艳高贵的蜻蜓、《乌龙闯情关》里可爱调皮犹如仙子般的水仙、《律政佳人》里干练又气质十足的女律师钱小美等，都让观众记忆深刻。而她还凭借《大雪无痕》里任性的丁洁而获得了金鹰奖观众最喜欢的女演员奖。</p><p>　　拍戏是自己的最爱，可&ldquo;贪心&rdquo;的曹颖也离不开主持舞台，在一番谈判之后，她成功&ldquo;改嫁&rdquo;到湖南卫视，和湖南台当红主持人主持大型活动、享受同等待遇。更奇妙的是，湖南卫视给她的政策很宽松，既允许拍戏，还可以接拍广告。（记者 李平）</p>";
       String input2 = "<tr><td height=\"24\" class=\"font14px\"><div align=\"left\">·<a target=\"_blank\" class=\"blackul\" href=\"{url=NO\"}\">{title=NO<}</a>";
     
       String font="<font face=\"Arial,Serif\" size=\"+2\" color =\"red\">";
     //  System.out.println(parseFont(font));
     //  System.out.println(findImg(input));
     System.out.println(parseRule(input2));
   }
private static String parseFont2(String font){
	   String result = "";
	       
	    //   String regex = "<\\s*font\\s+([^>]*)\\s*>"; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
	       String regex2 = "([a-z]+)\\S*=\\s*\"([^\"]+)\""; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
	     //  Pattern p1 = Pattern.compile(regex);
	       Pattern p2 = Pattern.compile(regex2);
	       Matcher m2 = p2.matcher(font);
	       while (m2.find()) {
	          result = m2.group();
	          
	           System.out.println("it2 is "+result);
	       
	       }
	       //String replacement = pocessImgWidth(img);
	       //restult = restult.replaceAll(img, replacement);       
	       return result;   

	}
private static String parseFont(String font){
   String result = "";
       
       String regex = "<\\s*font\\s+([^>]*)\\s*>"; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
     //  String regex2 = "([a-z]+)\\S*=\\s*\"([^\"]+)\""; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
       Pattern p1 = Pattern.compile(regex);
     //  Pattern p2 = Pattern.compile(regex2);
       Matcher m1 = p1.matcher(font);
       while (m1.find()) {
          result = m1.group(1);
          parseFont2(result);
           System.out.println("it is "+result);
       
       }
       //String replacement = pocessImgWidth(img);
       //restult = restult.replaceAll(img, replacement);       
       return result;   

}


   private static float rate = 1;
   
   
   private static String parse3(String img){
   String restult = "";
       
       String regex = "[a-z]+"; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
       Pattern p1 = Pattern.compile(regex);
       Matcher m1 = p1.matcher(img);
       while (m1.find()) {
           img = m1.group();
           System.out.println("it3 is "+img);
       
       }
       //String replacement = pocessImgWidth(img);
       //restult = restult.replaceAll(img, replacement);       
return img;   
   
   }
   private static String parse2(String img){
   
   String restult = "";
           
           String regex = "([a-z]+)\\s*=([^\\}]*)"; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
           Pattern p1 = Pattern.compile(regex);
           Matcher m1 = p1.matcher(img);
           while (m1.find()) {
               img = m1.group(1);
               System.out.println("it2 is "+img+"  "+m1.group(2));
               //parse3(img);
           
           }
           //String replacement = pocessImgWidth(img);
           //restult = restult.replaceAll(img, replacement);       
   return img;   
   
   }
   /**
    * 对规则进行预处理，形成新的regex
    * @param rule
    * @return
    */
   public static String getRuleRegex(String rule){
	   
	   rule= rule.replaceAll("\\\\","\\\\\\\\");
	   rule= rule.replaceAll("\\(","\\\\(");
	   rule= rule.replaceAll("\\)","\\\\)");
	   rule= rule.replaceAll("\\[","\\\\[");
	   rule= rule.replaceAll("\\]","\\\\]");
	   rule= rule.replaceAll("\\]","\\\\]");
	   rule= rule.replaceAll("\"","\\\\\"");
	   rule= rule.replaceAll("\\.","\\\\.");
	   rule= rule.replaceAll("\\?","\\\\?");
	   rule= rule.replaceAll("\\$","\\\\$");
	   rule= rule.replaceAll("\\^","\\\\^");
	   rule= rule.replaceAll("\\/","\\\\/");
	   rule= rule.replaceAll("\\+","\\\\+");
	//   rule= rule.replaceAll("\\{([a-z]+)\\s*=([^\\}]*)\\}","(.*?)");
	//   {(.*?)
		rule= rule.replaceAll("\\{(.*?)\\}","(.*?)");
	  // }
	   
	return rule;
	   
   }

   
   /**
    * 解析content列表
    * @return
    */
   public static List<Map> parseContent(String ruleregex,String content,Map map){
	   List<Map> v=new ArrayList();
	   
       Pattern p = Pattern.compile(ruleregex);
       Matcher m = p.matcher(content);
       String img="";
       
       int count=m.groupCount();
       //System.out.println("count is "+count);
       while (m.find()) {
    	   //Map<String ,String> map = new HashMap();
    	   map.put("content",m.group(1));
           //parse2(img);
       }
       

	   
	return v;
	   
   }
   
   /**
    * 根据可变页码返回
    * @param regex
    * @return
    */
   public static List<String> parsePageUrl(String url,int min,int max){
		//preg_match("/([\d]+)/",substr($second_page,$i),$array);
	   List<String> v= new ArrayList();
	   for(int i=min;i<=max;i++){
		   v.add(url.replaceAll("\\[page\\]", i+""));
	   }

	return v;
	   
   }
   /**
    * 解析 title列表
    * @return
    */
   public static List<Map> parseTitle(String url,String ruleregex,String content,List<String> titleV){
	   List<Map> v=new ArrayList();
	   
       Pattern p = Pattern.compile(ruleregex);
       Matcher m = p.matcher(content);
       String img="";
       
       int count=m.groupCount();
       //System.out.println("count is "+count);
       while (m.find()) {
    	   Map<String ,String> map = new HashMap();
    	   for(int i=1;i<=count;i++){
    		   
    		//   System.out.println(i);
               img = m.group(1);
               map.put("url", url+img);
               img = m.group(2);
               map.put("title", img);

               //               System.out.println("it2 is "+img+"  "+m.group(i));
//               v.add(img);    		   
    	   }
    	   v.add(map);
           //parse2(img);
       }
       

	   
	return v;
	   
   }
   
   public static String  getUrl(String titleUrl){
       String regex = "http://([^/]+)(.*)"; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
       Pattern p = Pattern.compile(regex);
       Matcher m = p.matcher(titleUrl);
       String img="";
       while (m.find()) {
    	   System.out.println(m.group(1)+"   "+m.group(2));
    	   return "http://"+m.group(1);
       }
	return "";
       
	   
   }
   /**
    * 解析规则
    * @param input
    * @return
    */
   public static List<String> parseRule(String input){
	   List<String> v=new ArrayList();
   String restult = input;
       String regex = "\\{([a-z]+)\\s*=([^\\}]*)\\}"; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾
       Pattern p = Pattern.compile(regex);
       Matcher m = p.matcher(input);
       String img="";
       while (m.find()) {

           img = m.group(1);
           System.out.println("it is "+img+"  "+m.group(2));
           v.add(img);
           //parse2(img);
       }
       
       String rule = getRuleRegex(input);
       System.out.println("rule is "+rule);

   return v;
   
   }


   private static String findImg(String input) {
       String restult = input;
       String regex = "<img\\s*([^>]*)>"; //以<img开头，接着一个或多个空格，接着是一些非 > 的字符，最后以>结尾


       Pattern p = Pattern.compile(regex);
       Matcher m = p.matcher(input);


       while (m.find()) {


           String img = m.group();
           System.out.println("img is "+img);
           String replacement = processImgWidth(img);
           restult = restult.replaceAll(img, replacement);
       }


       return restult;
   }


   private static String processImgWidth(String input) {


       String result = input;


       String regexWidth = "((w|w)(i|I)(d|D)(t|T)(h|H))\\s*=\\s*(\"?)\\d*(\"?)";


       Pattern p1 = Pattern.compile(regexWidth);
       Matcher m1 = p1.matcher(input);


       if (m1.find()) {
           String width = m1.group();
           System.out.println("width is "+width);
           String replacement = processWidthValue(width);
           System.out.println("Pre: " + result);
           result = result.replaceAll(width, replacement);
           System.out.println("Post: " + result);
       }
       return result;
   }


   private static String processWidthValue(String width) {
       String result = "";


       String regexWidthNo = "\\d+";


       Pattern p2 = Pattern.compile(regexWidthNo);
       Matcher m2 = p2.matcher(width);


       if (m2.find()) {
           String widthNo = m2.group();


           try {
               int widthValue = Integer.parseInt(widthNo);


               rate = 500 / widthValue;


               if (rate < 1) {
                   result = "width=\"400\"";
               }
           } catch (Exception e) {
           }
       }


       return result;
   }
}




