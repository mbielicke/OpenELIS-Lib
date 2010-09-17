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
 */
public class JMSMessageConsumer implements MessageListener {

    static Connection      connect;
    static Session         session;
    static MessageConsumer consumer;

    public JMSMessageConsumer() {
    }

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

    public static void startListener(String destination) {
        try {
            InitialContext jndiContext = getInitialContext();
            ConnectionFactory factory = (ConnectionFactory)jndiContext.lookup("ConnectionFactory");
            Topic topic = (Topic)jndiContext.lookup(destination);
            connect = factory.createConnection();
            session = connect.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(new JMSMessageConsumer());
            connect.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopListener() {
        try {
            connect.stop();
            session.close();
            consumer.close();
            connect.close();
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