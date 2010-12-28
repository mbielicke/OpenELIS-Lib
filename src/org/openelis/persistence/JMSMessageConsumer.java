package org.openelis.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

/**
 * Class creates a session within servlet context and starts listening for
 * messages. OpenELIS uses this service to update caches from backend J2EE (JBOSS)
 * to servlet (TOMCAT).
 * 
 * Please note that Tomcat will complain about several threads not being stopped even after
 * we close/stop all the listeners.
 */
public class JMSMessageConsumer {

    private static Connection      connect;
    private static Session         session;
    private static MessageConsumer consumer;

    public static void startListener(String destination) {
        InitialContext jndiContext;
        ConnectionFactory factory;
        Topic topic;
        
        try {
            jndiContext = getInitialContext();
            factory = (ConnectionFactory)jndiContext.lookup("ConnectionFactory");
            topic = (Topic)jndiContext.lookup(destination);
            connect = factory.createConnection();
            session = connect.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message arg0) {
                    String h;
                    org.openelis.persistence.Message m;
                    
                    try {
                        m = (org.openelis.persistence.Message) ((ObjectMessage)arg0).getObject();
                        h = m.getHandler();
                        ((MessageHandler<org.openelis.persistence.Message>)Class.forName(h).newInstance()).handle(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            connect.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopListener() {
        try {
            if (consumer != null)
                consumer.close();
            if (session != null)
                session.close();
            if (connect != null) {
                connect.stop();
                connect.close();
            }
            connect = null;
            session = null;
            consumer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InitialContext getInitialContext() throws Exception {
        InitialContext ctx;
        File propFile;
        Properties props;

        ctx = new InitialContext();
        propFile = new File((String)ctx.lookup( ("java:comp/env/openelisJNDI")));
        props = new Properties();
        props.load(new FileInputStream(propFile));

        return new InitialContext(props);
    }
}