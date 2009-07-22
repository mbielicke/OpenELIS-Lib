/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.server;

import java.util.Date;

import org.openelis.gwt.common.RPC;
import org.openelis.gwt.server.AppServlet;
import org.openelis.gwt.services.ScreenServiceInt;

public class ScreenControllerServlet extends AppServlet implements ScreenServiceInt {

    private static final long serialVersionUID = 1L;
    
    private Object invoke(String service, String method) throws Exception {
    	return invoke(service,method,new Class[]{},new Object[]{});
    }
    
    @SuppressWarnings("unchecked")
	private Object invoke(String service, String method, Class[] paramTypes, Object[] params) throws Exception{
		Object serviceInst = Class.forName(getThreadLocalRequest().getParameter("service")).newInstance();
		return serviceInst.getClass().getMethod(method, paramTypes).invoke(serviceInst, params);
    }

    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, Integer param)	throws Exception {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
	}
    
    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, RPC param) throws Exception {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
	}
    
    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, Double param) throws Exception {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
	}
    
    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, String param) throws Exception {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
    }

    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, Date param) throws Exception {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
	}
 
	public Boolean callBoolean(String method) throws Exception {
    	return (Boolean)invoke(getThreadLocalRequest().getParameter("service"),method);
	}
 
	public Date callDate(String method) throws Exception {
    	return (Date)invoke(getThreadLocalRequest().getParameter("service"),method);
	}

	public Double callDouble(String method) throws Exception {
    	return (Double)invoke(getThreadLocalRequest().getParameter("service"),method);
	}

	public Integer callInteger(String method) throws Exception {
    	return (Integer)invoke(getThreadLocalRequest().getParameter("service"),method);
	}

	public String callString(String method) throws Exception {
    	return (String)invoke(getThreadLocalRequest().getParameter("service"),method);
	}

}
