package sand.actionhandler.news;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import sand.actionhandler.system.ActionHandler;
import sand.annotation.Ajax;
import sand.depot.tool.system.ControllableException;
import sand.depot.tool.system.ErrorException;
import sand.depot.tool.system.InfoException;
import sand.mail.MailServer;
import tool.dao.BizObject;
import tool.util.CachedResponseWrapper;

public class GpMailAH extends ActionHandler {
	private Configuration cfg; 

	public GpMailAH(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
        cfg = new Configuration();
        // - Templates are stoted in the WEB-INF/templates directory of the Web app.
        cfg.setServletContextForTemplateLoading(this._context  , "WEB-INF/templates");
		// TODO Auto-generated constructor stub
	}

	public void listPost() throws SQLException{
							
					this.setOrderBy("posttime desc");
					this.listObj("post");
					
					List<BizObject> v= (List)this._request.getAttribute("objList");

					for(BizObject b:v){
						String s= b.getString("newsids");
						String news[]=s.split(",");
						b.set("newsids", news);
					}
				this._nextUrl="/template/news/postList.jsp";

	}
	
	public void upload(){
		this._nextUrl="/template/news/upload.jsp";
	}
	public void doUpload() throws IOException{
		File f =this.getUploadFile();
		//String path=ActionHandler._context.getRealPath("/")+"WEB-INF/templates/news/watermarks.png";
		String path ="/root/erp.upload/maillist";
		//log(path);
		//log(this._context.getContextPath());
		File newf=new File(path);
		if(newf.exists()){
			File nnewf=new File(path+Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
			if(nnewf.exists()) nnewf.delete();
			FileUtils.moveFile(newf, nnewf);
		}
			
		FileUtils.moveFile(f, newf);
		//FileUtils.move
		throw new InfoException("上传成功");
		//this._nextUrl="/template/news/upload.jsp";
	}
	public void comon() throws IOException{
		
		this._dispatched=true;
		// Build the data-model
        Map root = new HashMap();
        root.put("message", "Hello World!");
        
        // Get the templat object
        Template t = cfg.getTemplate("test.ftl");
        
        // Prepare the HTTP response:
        // - Use the charset of template for the output
        // - Use text/html MIME-type
        _response.setContentType("text/html; charset=" + t.getEncoding());
        Writer out = _response.getWriter();
        
        // Merge the data-model and the template
        try {
            t.process(root, out);
        } catch (TemplateException e) {
            throw new ErrorException(
                    "Error while processing FreeMarker template"+e.getMessage());
        }
	}

	
	

	
	

}
