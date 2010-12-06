package org.openelis.gwt.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openelis.gwt.common.DataBaseUtil;
import org.openelis.util.SessionManager;

public class ReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;        

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        int c;        
        String tempFileName, contentType, attachmentFileName;
        File tempFile;
        FileInputStream fileStream;
        ServletOutputStream outStream;
        
        fileStream = null;
        outStream = null;
        try {
            tempFileName = req.getParameter("tempfile");
            contentType = req.getParameter("contentType");
            attachmentFileName = req.getParameter("attachmentFileName");            
            
            if (DataBaseUtil.isEmpty(tempFileName) || DataBaseUtil.isEmpty(contentType))
                error(resp, "Missing file name or content type parameter; please report this error to your sysadmin");
            
            if (SessionManager.getSession().getAttribute(tempFileName) == null)
                error(resp, "Specified file name is not valid; please report this error to your sysadmin");
                        
            tempFile = new File("/tmp", tempFileName);
            fileStream = new FileInputStream(tempFile);
            outStream = resp.getOutputStream();
            
            resp.setContentType(contentType);    
            if (!DataBaseUtil.isEmpty(attachmentFileName))
                resp.setHeader("Content-Disposition", "attachment;filename=\""+ attachmentFileName + "\""); 
                    
            while ((c = fileStream.read()) != -1) 
                outStream.write(c);
            
            tempFile.delete();
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        } finally {
            if (fileStream != null)
                fileStream.close();
            if (outStream != null)
                outStream.close();
        }
    }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);		
	}
	
	private void error (HttpServletResponse resp, String message) {	    
	    String htmlMessage;
	    	    
	    htmlMessage = "<html>\n" +
	    		      "<header>Error in generating report:</header>" +
	    		      "<body>"+message+"</body>"+
	    		      "</html>";
	    resp.setContentType("text/html");
	    try {
	        resp.getWriter().println(htmlMessage);
	    } catch (Exception e) {
            e.printStackTrace();
        }
	} 
	
}
