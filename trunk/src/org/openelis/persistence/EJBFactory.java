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
                File propFile = null;
                InputStream is = null;
                props = new Properties();
                try{
                    propFile = new File("/usr/pub/http/var/jndi/jndi.properties");
                    is = new FileInputStream(propFile);
                    props.load(is);
                }catch(Exception e){
                    props.setProperty("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
                    props.setProperty("java.naming.provider.url","localhost");
                }
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
                String pass = "";
                for(int i = 1; i < usrPass.length; i++){
                    if(i > 1)
                        pass += " ";
                    pass += usrPass[i].trim();
                }
                props.setProperty(InitialContext.SECURITY_CREDENTIALS,
                                  pass);
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
