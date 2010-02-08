package org.openelis.gwt.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openelis.gwt.common.FileProgress;
import org.openelis.util.SessionManager;

public class ReportService {
	
	public FileProgress progress(String id) {
		FileProgress fp = (FileProgress)SessionManager.getSession().getAttribute(id);
		return fp;
	}
	
	public void returnPDF(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String id = req.getParameter("id");
		//System.out.println(id);
		if(SessionManager.getSession().getAttribute(id) == null){
			ServletOutputStream outStream = resp.getOutputStream();
			resp.setContentType("application/pdf");
			outStream.println("<p/>");
			return;
		}
		File pdfFile = new File("/tmp/myfile"+SessionManager.getSession().getId()+".pdf");
		FileInputStream fileStream = new FileInputStream(pdfFile);
		ServletOutputStream outStream = resp.getOutputStream();
		resp.setContentType("application/pdf");
		int next;
		long count = 0l;
		
		while((next = fileStream.read()) >= 0) {
			count++;
			outStream.write(next);
			((FileProgress)SessionManager.getSession().getAttribute(id)).progress = count;
		}
		fileStream.close();
		outStream.close();
		pdfFile.delete();
		((FileProgress)SessionManager.getSession().getAttribute(id)).done = true;
		
	}
	
	public FileProgress fileProgress(String id) throws Exception{
		FileProgress fp = (FileProgress)SessionManager.getSession().getAttribute(id);
		if(fp != null) {
			if(fp.done)
				SessionManager.getSession().removeAttribute(id);
		}else{
			throw new Exception("No report to download");
		}
		return fp;
	}
}
