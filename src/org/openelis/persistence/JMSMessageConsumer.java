package org.openelis.persistence;

import org.openelis.persistence.MessageHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

public class JMSMessageConsumer  implements MessageListener {
    
    static Connection connect;
    static Session session;
    static MessageConsumer consumer;
    
    public JMSMessageConsumer() {
    }
    
        public void onMessage(Message arg0) {
            try {
                org.openelis.persistence.Message msg = (org.openelis.persistence.Message)((ObjectMessage)arg0).getObject();
                String handler = msg.getHandler();
                ((MessageHandler<org.openelis.persistence.Message>)Class.forName(handler).newInstance()).handle(msg);
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    
    public static void startListener(String destination) {
        try {
            InitialContext jndiContext = getInitialContext();
            ConnectionFactory factory = (ConnectionFactory)jndiContext.lookup("ConnectionFactory");
            Topic topic = (Topic)jndiContext.lookup(destination);
            connect = factory.createConnection();
            session = connect.createSession(false,Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(new JMSMessageConsumer());
            connect.start();            
        }catch(Exception e) {
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    
    private static InitialContext getInitialContext() throws Exception {
        InitialContext initCtx = new InitialContext();
        File propFile = new File((String)initCtx.lookup(("java:comp/env/openelisJNDI")));
        InputStream is = new FileInputStream(propFile);
        Properties props = new Properties();
        props.load(is);
        return new InitialContext(props);
    }
    
    

}
