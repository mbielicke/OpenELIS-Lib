/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Category;

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
