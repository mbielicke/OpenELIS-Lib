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
import java.util.Date;
import java.util.HashMap;
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
import javax.servlet.http.HttpSession;

import org.openelis.ui.common.PermissionException;
import org.openelis.util.SessionManager;
import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class provides a framework for user login to web applications. The class provides
 * user login retry and lock out capability through configuration parameters in web.xml. 
 */

public abstract class StaticFilter implements Filter {

    protected static final long serialVersionUID = 1L;
    protected Logger        log              = Logger.getLogger(StaticFilter.class.getName());
    protected LoginAttempt  loginAttempt;
    protected int           loginLockoutTime,       // time in milli to lockout user
                            loginIPRetryCount,      // # of tries before ip lockout 
                            loginNameRetryCount;    // # of tries before username lockout
    protected String        loginXSL,               // full path to XSL stylesheet for login
                            loginHTML,              // wrapper HTML for application
                            constantsPath,          // fully qualified classname for constants
                            constantsAuthFailure,   // constants key for authentication failure message
                            serverJNDIProperties;   // Java comp JNDI properties variable that points to JNDI file  

    public void init(FilterConfig config) throws ServletException {
        loginAttempt = new LoginAttempt();

        loginXSL = getStringParameter(config, "LoginXSL", "");
        loginHTML = getStringParameter(config, "LoginHTML", "");
        loginLockoutTime = getIntegerParameter(config, "LoginLockoutTime", 1000 * 60 * 10);
        loginIPRetryCount = getIntegerParameter(config, "LoginIPRetryCount", 7);
        loginNameRetryCount = getIntegerParameter(config, "LoginNameRetryCount", 4);
        constantsPath = getStringParameter(config, "ConstantsPath", "");
        constantsAuthFailure = getStringParameter(config, "ConstantsAuthFailure", "");
        serverJNDIProperties = getStringParameter(config, "ServerJNDIProperties", "");

        //
        // xsl parser access to constants
        //
        ServiceUtils.props = constantsPath;
    }

    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException {
        boolean error;
        Date now;
        HttpSession session;
        HttpServletRequest hreq = (HttpServletRequest)req;

        //
        // pass-through for images and if we are logged-in
        //
        session = hreq.getSession(false);

        if (hreq.getRequestURI().endsWith(".jpg") || hreq.getRequestURI().endsWith(".gif") ||
            (session != null && session.getAttribute("USER_NAME") != null)) {
            //
            // prevent cache
            //
            if (hreq.getRequestURI().contains(".nocache.")) {
                now = new Date();
                response = (HttpServletResponse) response;
                ((HttpServletResponse)response).setDateHeader("Date", now.getTime());
                ((HttpServletResponse)response).setDateHeader("Expires", now.getTime() - 86400000L);
                ((HttpServletResponse)response).setHeader("Pragma", "no-cache");
                ((HttpServletResponse)response).setHeader("Cache-control", "no-cache, no-store, must-revalidate");
            }
            try {
                chain.doFilter(req, response);
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
            return;
        }

        //
        // check to see if we are coming from login screen
        //
        error = false;
        if (hreq.getParameter("username") != null) {
            try {
                login(hreq, hreq.getParameter("username"),
                      req.getParameter("password"), hreq.getRemoteAddr());
                try {
                    chain.doFilter(req, response);
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                }
                return;
            } catch (Exception e) {
                error = true;
            }
        }
        //
        // ask them to authenticate
        //
        try {
            Document doc;
            Element action;

            doc = XMLUtil.createNew("login");
            action = doc.createElement("action");
            action.appendChild(doc.createTextNode(loginHTML));
            doc.getDocumentElement().appendChild(action);
            if (error) {
                Element errorEL = doc.createElement("error");
                errorEL.appendChild(doc.createTextNode(constantsAuthFailure));
                doc.getDocumentElement().appendChild(errorEL);
            }
            ((HttpServletResponse)response).setContentType("text/html");
            ((HttpServletResponse)response).setCharacterEncoding("UTF-8");
            response.getWriter().write(ServiceUtils.getXML(loginXSL, doc));
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
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
        InitialContext localctx;

        try {
            if ( !loginAttempt.isValid(name, ipAddress))
                throw new PermissionException();

            /*
             * JBOSS dependent! Build the security principal by combining
             * username;session-id;locale and then parse it in the back to get
             * all the parts. see UserCacheBean. see OpenELISLDAPModule see
             * OpenELISRolesModule
             */
            locale = (String)req.getParameter("locale");
            parts = name + ";" + req.getSession().getId() + ";" + (locale == null ? "en" : locale);

            localctx = new InitialContext();
            propFile = new File((String)localctx.lookup(serverJNDIProperties));
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

            loginAttempt.success(name, ipAddress);
        } catch (PermissionException p) {
            loginAttempt.fail(name, ipAddress);
            throw p;
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            loginAttempt.fail(name, ipAddress);
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

    protected int getIntegerParameter(FilterConfig config, String key, int def) {
        int value;

        try {
            value = Integer.parseInt(config.getInitParameter(key));
        } catch (Exception e) {
            value = def;
        }

        log.fine("Config "+key+"='"+value+"'");
        return value;
    }

    /*
     * Simple class to manage login attempts
     */
    protected class LoginAttempt {
        int                                            tries;
        long                                           lastTime;

        protected HashMap<String, LoginAttempt> failed = new HashMap<String, StaticFilter.LoginAttempt>();

        /**
         * Checks to see if the user from ip address has exceeded the number of
         * attempts trying to login into the system.
         */
        public boolean isValid(String name, String ipAddress) {
            long cutoff;
            LoginAttempt la;

            cutoff = System.currentTimeMillis() - loginLockoutTime;

            la = failed.get(ipAddress);
            if (la != null && la.lastTime >= cutoff && la.tries >= loginIPRetryCount)
                return false;

            la = failed.get(name);
            if (la != null && la.lastTime >= cutoff && la.tries >= loginNameRetryCount)
                return false;

            return true;
        }

        /**
         * Clears the failed list for the user and ip address. TODO: need a
         * sliding window remove for clearing the ip address for better
         * security.
         */
        public void success(String name, String ipAddress) {
            failed.remove(ipAddress);
            failed.remove(name);

            log.info("Login attempt for '" + name + "' - " + ipAddress + " succeeded");
        }

        /**
         * Adds/increments the number of failed attempts from user and ip
         * address.
         */
        public void fail(String name, String ipAddress) {
            long now;
            LoginAttempt li, ln;

            now = System.currentTimeMillis();

            li = failed.get(ipAddress);
            if (li == null) {
                li = new LoginAttempt();
                failed.put(ipAddress, li);
            }
            li.lastTime = now;
            li.tries = Math.min(li.tries + 1, 9999);

            ln = failed.get(name);
            if (ln == null) {
                ln = new LoginAttempt();
                failed.put(name, ln);
            }
            ln.lastTime = now;
            ln.tries = Math.min(ln.tries + 1, 9999);

            log.severe("Login attempt for '" +
                       name + "' [" + ln.tries + " of "+ loginNameRetryCount + "]" + " - " +
                       ipAddress + " [" + li.tries +" of " + loginIPRetryCount + "] failed ");
        }
    }
}