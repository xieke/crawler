package sand.scrawler;

import java.util.ArrayList;  
import java.util.List;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
  
/** 
 * @use 获取指定HTML标签的指定属性的值 
 * @ProjectName stuff 
 * @Author <a href="mailto:mhmyqn@qq.com">mikan</a></br> 
 * @Date 2012-11-19 下午08:27:24 </br> 
 * @FullName com.mmq.regex.MatchHtmlElementAttrValue.java </br> 
 * @JDK 1.6.0 </br> 
 * @Version 1.0 </br> 
 */  
public class MatchHtmlElementAttrValue {  
      
    /** 
     * 获取指定HTML标签的指定属性的值 
     * @param source 要匹配的源文本 
     * @param element 标签名称 
     * @param attr 标签的属性名称 
     * @return 属性值列表 
     */  
    public static List<String> match(String source, String element, String attr) {  
        List<String> result = new ArrayList<String>();  
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";  
        Matcher m = Pattern.compile(reg).matcher(source);  
        while (m.find()) {  
            String r = m.group(1);  
            result.add(r);  
        }  
        return result;  
    }  
      
    public static void main(String[] args) {  
        String source = "<a title=中国体育报 href=''>aaa</a><a title='北京日报' href=''>bbb</a>";  
        List<String> list = match(source, "a", "title");  
        System.out.println(list);  
    }  
}  
