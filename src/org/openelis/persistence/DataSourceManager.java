/*
 * Created on Mar 11, 2003
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code Template
 */
package org.openelis.persistence;

import org.apache.log4j.Category;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Manage DataSource.
 * 
 * @author fyu
 */
public class DataSourceManager {
    private static Category log = Category.getInstance(DataSourceManager.class.getName());
    private static DataSource ds = null;
    private static Hashtable connections = null;
    private static Connection con = null;
    private static boolean is_ds = true;

    /**
     * Get database connection.
     * 
     * @return Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (is_ds) {
            String tName = Thread.currentThread().getName();
            if (connections.containsKey(tName)) {
                return (Connection)connections.get(tName);
            } else {
                Connection conn = ds.getConnection();
                if (conn == null) {
                    log.error("Connection remains null after attempted retrieval");
                    throw new SQLException("Connection remains null after attempted retrieval.");
                }
                conn.setAutoCommit(false);
                connections.put(tName, conn);
                return conn;
            }
        } else {
            return con;
        }
    }

    /**
     * Close database connection.
     * 
     * @param conn
     * @throws SQLException
     */
    public static void close(Connection conn) {
        if (is_ds) {
            try {
                String tName = Thread.currentThread().getName();
                if (connections.containsKey(tName))
                    connections.remove(tName);
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            try {
                con.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static void close() {
        if (is_ds) {
            try {
                String tName = Thread.currentThread().getName();
                if (connections.containsKey(tName)) {
                    Connection conn = (Connection)connections.get(tName);
                    connections.remove(tName);
                    if (conn != null) {
                        conn.close();
                        conn = null;
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            try {
                con.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Initialize datasource.
     * 
     * @throws NamingException
     */
    public static void init(String dataSource) {
        // initialize datasource
        try {
            Context ctx = new InitialContext();
            ds = (DataSource)ctx.lookup(dataSource);
            connections = new Hashtable();
        } catch (NamingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void init(String dburl,
                            String user,
                            String pass,
                            String driver) {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(dburl, user, pass);
            con.setAutoCommit(false);
            is_ds = false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void rollback() {
        if (is_ds) {
            try {
                getConnection().rollback();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            try {
                con.rollback();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static void destroy() {
        Iterator connIt = connections.values().iterator();
        while (connIt.hasNext()) {
            Connection con = (Connection)connIt.next();
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            connections = new Hashtable();
        }
    }
}
