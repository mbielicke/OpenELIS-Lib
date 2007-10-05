package org.openelis.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Category;
import org.openelis.util.SessionManager;

public class EJBFactory {
    private static Category log = Category.getInstance(EJBFactory.class);

    public static Object lookup(String bean){
        try{
            Properties props = (Properties)SessionManager.getSession().getAttribute("jndiProps");
            InitialContext ctx = null;
            if (props == null){
                File propFile = new File("/usr/pub/http/var/jndi/jndi.properties");
                InputStream is = new FileInputStream(propFile);
                props = new Properties();
                props.load(is);
                props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.jboss.security.jndi.LoginInitialContextFactory");
                props.setProperty(Context.SECURITY_PROTOCOL, "other");
                HttpClient httpclient = new HttpClient();
                String url = ("https://www.uhl.uiowa.edu/cas/uhlcasapter");
                PostMethod post = new PostMethod(url);
                post.addParameter("action", "fetchUSRPWD");
                post.addParameter("CASTGC",
                                  (String)SessionManager.getSession()
                                                        .getAttribute("castgc"));
                httpclient.executeMethod(post);
                String response = post.getResponseBodyAsString();
                String[] usrPass = response.split(" ");
                props.setProperty(InitialContext.SECURITY_CREDENTIALS,
                                  usrPass[1].trim());
                props.setProperty(Context.SECURITY_PRINCIPAL, usrPass[0].trim());
                ctx = new InitialContext(props);
                SessionManager.getSession().setAttribute("jndiProps", props);
            }else{
                ctx = new InitialContext(props);
            }
            return ctx.lookup(bean);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
