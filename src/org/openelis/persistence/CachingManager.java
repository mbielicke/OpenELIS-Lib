/*
 * Created on Mar 11, 2003
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code Template
 */
package org.openelis.persistence;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import org.apache.log4j.Category;
import java.io.Serializable;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
 * Manage DataSource.
 * 
 * @author fyu
 */
public class CachingManager {
    private static Category log = Category.getInstance(CachingManager.class.getName());
    private static CacheManager cacheManager;

    /**
     * Get database connection.
     * 
     * @return Connection
     * @throws CacheException
     * @throws IllegalStateException
     * @throws SQLException
     */
    public static Object getElement(String cacheName, Serializable key) {
        Cache cache = null;
        Object value;
        Element element;
        if (!cacheManager.getStatus().equals(Status.STATUS_ALIVE))
            return null;
        try {
            cache = cacheManager.getCache(cacheName);
            value = null;
            if (cache == null) {
                return null;
            }
            if (cache.getStatus().equals(Status.STATUS_ALIVE)) {
                try {
                    element = cache.get(key);
                    if (element != null) {
                        value = element.getValue();
                    }
                } catch (IllegalStateException e) {
                    log.debug("IllegalStateException");
                } catch (CacheException e) {
                    log.debug("CacheException");
                }
            }
            return value;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static void putElement(String cacheName,
                                  Serializable key,
                                  Serializable value) {
        Cache cache;
        if (!cacheManager.getStatus().equals(Status.STATUS_ALIVE))
            return;
        try {
            cache = cacheManager.getCache(cacheName);
            if (cache == null)
                try {
                    cacheManager.addCache(cacheName);
                } catch (Exception ignE) {
                    log.warn("Unable to use cache: " + ignE.getMessage());
                }
            cache = cacheManager.getCache(cacheName);
            if (cache.getStatus().equals(Status.STATUS_ALIVE)) {
                cache.put(new Element(key, value));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
    }

    /**
     * Initialize datasource.
     * 
     * @throws NamingException
     */
    public static void init(String appRoot) {
        try {
            cacheManager = new CacheManager(appRoot + "/META-INF/ehcache.xml");
            // fillTests();
            // fillMethods();
        } catch (CacheException cacheE) {
            log.warn("Unable to initialize cache manager: " + cacheE.getMessage());
            // } catch (SQLException sqlE) {
            // log.warn("Unable to prefil method and test cache: " +
            // sqlE.getMessage());
        }
        log.debug("CachingManager: " + cacheManager + " initialized.");
    }

    /**
     * Perform clean up tasks when this servlet is been shutting down.
     * 
     * @see javax.servlet.Servlet#destroy()
     */
    public static void destroy() {
        log.debug("CachingManager: " + cacheManager + " shutdown.");
        cacheManager.shutdown();
    }
}
