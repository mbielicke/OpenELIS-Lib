/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This extends HTTPServlet and is mapped in the web.xml for downloading reports in formats
 * that can not be passed through RPC
 *
 */
public class ReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("rawtypes")
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
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}
	
}
