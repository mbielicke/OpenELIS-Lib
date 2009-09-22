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

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.RPC;
import org.openelis.gwt.services.ScreenServiceInt;

public class ScreenControllerServlet extends AppServlet implements ScreenServiceInt {

    private static final long serialVersionUID = 1L;
    
    private Object invoke(String service, String method) throws Throwable {
    	return invoke(service,method,new Class[]{},new Object[]{});
    }
    
    @SuppressWarnings("unchecked")
	private Object invoke(String service, String method, Class[] paramTypes, Object[] params) throws Throwable {

		try{
			Object serviceInst = Class.forName(service).newInstance();
		    return serviceInst.getClass().getMethod(method, paramTypes).invoke(serviceInst, params);
		
		}catch(InvocationTargetException e){
			if(e.getCause() != null)
				throw (Exception)e.getCause();
			else
				throw (Exception)e.getTargetException();
		}catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, Integer param)	throws Throwable {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
	}
    
    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, RPC param) throws Throwable {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
	}
    
    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, Double param) throws Throwable {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
	}
    
    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, String param) throws Throwable {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
    }

    @SuppressWarnings(value = "unchecked")
	public <T extends RPC> T call(String method, Datetime param) throws Throwable {
    	return (T)invoke(getThreadLocalRequest().getParameter("service"),method, new Class[]{param.getClass()},new Object[] {param});
	}
 
	public Boolean callBoolean(String method) throws Throwable {
    	return (Boolean)invoke(getThreadLocalRequest().getParameter("service"),method);
	}
 
	public Datetime callDatetime(String method, byte begin, byte end) throws Throwable {
    	return (Datetime)invoke(getThreadLocalRequest().getParameter("service"),method,new Class[]{byte.class,byte.class},new Object[]{begin,end});
	}

	public Double callDouble(String method) throws Throwable {
    	return (Double)invoke(getThreadLocalRequest().getParameter("service"),method);
	}

	public Integer callInteger(String method) throws Throwable {
    	return (Integer)invoke(getThreadLocalRequest().getParameter("service"),method);
	}

	public String callString(String method) throws Throwable {
    	return (String)invoke(getThreadLocalRequest().getParameter("service"),method);
	}

	@SuppressWarnings("unchecked")
	public <T extends RPC> T call(String method) throws Throwable {
		return (T)invoke(getThreadLocalRequest().getParameter("service"),method);
	}

	public void callVoid(String method) throws Throwable {
		invoke(getThreadLocalRequest().getParameter("service"),method);
		
	}

	@SuppressWarnings("unchecked")
	public <T extends RPC> T call(String method, Long param) throws Throwable {
		return (T)invoke(getThreadLocalRequest().getParameter("service"),method,new Class[]{param.getClass()},new Object[] {param});
	}

}
