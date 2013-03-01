/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openelis.ui.common.PermissionException;
import org.openelis.util.SessionManager;

/**
 * This class provides a framework for a default user login to web applications.
 * The class is replacement for StaticFiler.java and allows the developers to
 * automatically login to an application using a default user id and password.
 * This filter chain is only used in GWT development mode (hosted mode).
 */

public abstract class HostedFilter implements Filter {
    protected static final long serialVersionUID = 1L;
    protected Logger            log              = Logger.getLogger(HostedFilter.class.getName());
    protected String            user, password, serverJNDIProperties;

    public void init(FilterConfig config) throws ServletException {
        user = getStringParameter(config, "User", "demo");
        password = getStringParameter(config, "Pass", "demo");
        serverJNDIProperties = getStringParameter(config, "ServerJNDIProperties", "");
    }

    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest hreq = (HttpServletRequest)req;

        //
        // register this session with SessionManager so we can access it
        // statically in gwt code
        //
        SessionManager.setSession(hreq.getSession());

        //
        // pass-through for images and if we are logged-in
        //
        if (hreq.getRequestURI().endsWith(".jpg") || hreq.getRequestURI().endsWith(".gif") ||
            hreq.getSession().getAttribute("USER_NAME") != null) {
            try {
                chain.doFilter(req, response);
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
            return;
        }
        //
        // used for language binding
        //
        if (hreq.getParameter("locale") != null)
            hreq.getSession().setAttribute("locale", req.getParameter("locale"));

        //
        // check to see if we have logged in session
        //
        if (hreq.getSession().getAttribute("USER_NAME") == null) {
            try {
                login(hreq, user, password, hreq.getRemoteAddr());
                try {
                    chain.doFilter(req, response);
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                }
                return;
            } catch (Exception e) {
                ((HttpServletResponse)response).sendRedirect("BadUserIdOrPasswordInHostedMode.html");
            }
        }
    }

    @Override
    public void destroy() {
    }

    /*
     * log the user into the system by sending its credentials to JBOSS for
     * authentication
     */
    protected void login(HttpServletRequest req, String name, String password, String ipAddress) throws Exception {
        File propFile;
        String parts, locale;
        Properties props;

        try {
            /*
             * JBOSS dependent! Build the security principal by combining
             * username;session-id;locale and then parse it in the back to get
             * all the parts. see UserCacheBean. see OpenELISLDAPModule see
             * OpenELISRolesModule
             */
            locale = (String)req.getSession().getAttribute("locale");
            parts = name + ";" + req.getSession().getId() + ";" + (locale == null ? "en" : locale);

            propFile = new File(serverJNDIProperties);
            props = new Properties();
            props.load(new FileInputStream(propFile));
            props.setProperty(InitialContext.INITIAL_CONTEXT_FACTORY,
                              "org.jboss.security.jndi.LoginInitialContextFactory");
            props.setProperty(InitialContext.SECURITY_PROTOCOL, "jboss-standard");
            props.setProperty(Context.SECURITY_PRINCIPAL, parts);
            props.setProperty(InitialContext.SECURITY_CREDENTIALS, password);

            login(props);

            req.getSession().setAttribute("jndiProps", props);
            req.getSession().setAttribute("USER_NAME", name);
        } catch (PermissionException p) {
            log.info("Permission for " + name + " failed ");
            throw p;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }

    /*
     * Each application will override and implement this method similar to the
     * following code:
     * 
     * remotectx = new InitialContext(props); remote = (UserCacheRemote)remotectx.lookup("openelis/openelis.jar/UserCacheBean!org.openelis.remote.UserCacheRemote");
     * perm = remote.login(); 
     * //
     * // check to see if she has connect permission
     * //
     * if (!perm.hasConnectPermission())
     *   throw new PermissionException("NoPermission.html");
     */
    protected abstract void login(Properties props) throws Exception;

    protected String getStringParameter(FilterConfig config, String key, String def) {
        String value;

        value = config.getInitParameter(key);
        value = (value != null ? value : def);

        log.fine("Config "+key+"='"+value+"'");
        return value; 
    }
}
