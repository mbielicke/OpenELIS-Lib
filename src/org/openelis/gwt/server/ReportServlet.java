package org.openelis.gwt.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ReportServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String service = req.getParameter("service");
		String method = req.getParameter("method");
		Class[] paramTypes = {HttpServletRequest.class, HttpServletResponse.class};
		Object[] params = {req,resp};
		try {
			Object serviceInst = Class.forName(service).newInstance();
		    serviceInst.getClass().getMethod(method, paramTypes).invoke(serviceInst, params);
		
		} catch(InvocationTargetException e){
			if(e.getCause() != null)
				throw (ServletException)e.getCause();
			else
				throw (ServletException)e.getTargetException();
		} catch (NoSuchMethodException e) {
            throw new ServletException("NoSuchMethodException: "+e.getMessage());
		} catch(Exception e){
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
		//super.doGet(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
		//super.doPost(req, resp);
	}
	
}
