/**
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * Copyright (C) The University of Iowa. All Rights Reserved.
 */
package org.openelis.gwt.server;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.RPC;
import org.openelis.gwt.services.ScreenServiceInt;

import com.google.gwt.user.client.rpc.SerializationException;

public class ScreenControllerServlet extends AppServlet implements ScreenServiceInt {

    private static final long serialVersionUID = 1L;

    private Object invoke(String service, String method) throws Exception {
        return invoke(service, method, new Class[] {}, new Object[] {});
    }

    @SuppressWarnings("rawtypes")
	private Object invoke(String service, String method, Class[] paramTypes, Object[] params) throws Exception {
        Object serviceInst;
        //HashMap<String, Object> busyPool;

        try {
            serviceInst = Class.forName(service).newInstance();
            return serviceInst.getClass().getMethod(method, paramTypes).invoke(serviceInst, params);
        } catch (InvocationTargetException e) {
            if (e.getCause() != null) {
                try {
                    sPolicy.validateSerialize(e.getCause().getClass());
                    throw (Exception)e.getCause();
                } catch (SerializationException se) {
                    throw new Exception(e.getCause().toString());
                }
            } else {
                try {
                    sPolicy.validateSerialize(e.getTargetException().getClass());
                    throw (Exception)e.getTargetException();
                } catch (SerializationException se) {
                    throw new Exception(e.getTargetException().toString());
                }
            }
        } catch (NoSuchMethodException e) {
            throw new Exception("NoSuchMethodException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            /*
             * Part of remote service caching -- commented for now 
             *
            if(SessionManager.getSession() != null) {
        		busyPool = (HashMap)SessionManager.getSession().getAttribute("busyBeanPool");
        		if (busyPool != null)
        			busyPool.clear();
        	}
        	*/
        }
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends RPC> T call(String method, Integer param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends RPC> T call(String method, RPC param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends RPC> T call(String method, Double param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends RPC> T call(String method, String param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends RPC> T call(String method, Datetime param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

    public Boolean callBoolean(String method) throws Exception {
        return (Boolean)invoke(getThreadLocalRequest().getParameter("service"), method);
    }

    public Datetime callDatetime(String method, byte begin, byte end) throws Exception {
        return (Datetime)invoke(getThreadLocalRequest().getParameter("service"),
                                method,
                                new Class[] {byte.class, byte.class},
                                new Object[] {begin, end});
    }

    public Double callDouble(String method) throws Exception {
        return (Double)invoke(getThreadLocalRequest().getParameter("service"), method);
    }
    
    public Long callLong(String method, RPC param) throws Exception {
    	return (Long)invoke(getThreadLocalRequest().getParameter("service"), 
    					    method,
    					    new Class[] {param.getClass()},
    					    new Object[] {param});
    }

    public Integer callInteger(String method) throws Exception {
        return (Integer)invoke(getThreadLocalRequest().getParameter("service"), method);
    }

    public String callString(String method) throws Exception {
        return (String)invoke(getThreadLocalRequest().getParameter("service"), method);
    }

    public String callString(String method, String param) throws Exception {
        return (String)invoke(getThreadLocalRequest().getParameter("service"),
                              method,
                              new Class[] {param.getClass()},
                              new Object[] {param});
    }

    @SuppressWarnings("unchecked")
    public <T extends RPC> T call(String method) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"), method);
    }

    public void callVoid(String method) throws Exception {
        invoke(getThreadLocalRequest().getParameter("service"), method);

    }

    @SuppressWarnings("unchecked")
    public <T extends RPC> T call(String method, Long param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

    @SuppressWarnings("unchecked")
    public <T extends ArrayList<? extends RPC>> T callList(String method, RPC param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

    @SuppressWarnings("unchecked")
    public <T extends ArrayList<? extends RPC>> T callList(String method) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"), method);
    }

    @SuppressWarnings("unchecked")
    public <T extends ArrayList<? extends RPC>> T callList(String method, String param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

    @SuppressWarnings("unchecked")
    public <T extends ArrayList<? extends RPC>> T callList(String method, Integer param) throws Exception {
        return (T)invoke(getThreadLocalRequest().getParameter("service"),
                         method,
                         new Class[] {param.getClass()},
                         new Object[] {param});
    }

}
