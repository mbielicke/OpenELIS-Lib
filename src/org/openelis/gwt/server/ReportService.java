package org.openelis.gwt.server;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openelis.gwt.common.ReportProgress;
import org.openelis.util.SessionManager;

public class ReportService {
	
	public ReportProgress progress(String id) {
		ReportProgress fp = (ReportProgress)SessionManager.getSession().getAttribute(id);
		return fp;
	}
	
	public void returnPDF(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String id = req.getParameter("id");
		if(SessionManager.getSession().getAttribute(id) == null){
			ServletOutputStream outStream = resp.getOutputStream();
			resp.setContentType("application/pdf");
			outStream.println("<p/>");
			return;
		}
		ReportProgress rp = (ReportProgress)SessionManager.getSession().getAttribute(id);
		File pdfFile = new File("/tmp/"+rp.name+SessionManager.getSession().getId()+".pdf");
		FileInputStream fileStream = new FileInputStream(pdfFile);
		ServletOutputStream outStream = resp.getOutputStream();
		resp.setContentType("application/pdf");
		int next;
		long count = 0l;
		
		while((next = fileStream.read()) >= 0) {
			count++;
			outStream.write(next);
			rp.progress = count;
		}
		fileStream.close();
		outStream.close();
		pdfFile.delete();
		rp.done = true;
		
	}
	
	public ReportProgress fileProgress(String id) throws Exception{
		ReportProgress rp = (ReportProgress)SessionManager.getSession().getAttribute(id);
		if(rp != null) {
			if(rp.done)
				SessionManager.getSession().removeAttribute(id);
		}else{
			throw new Exception("No report to download");
		}
		return rp;
	}
}
