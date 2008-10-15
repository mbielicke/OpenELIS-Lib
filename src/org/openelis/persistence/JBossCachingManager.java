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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import org.apache.log4j.Category;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.NamingException;

/**
 * Manage DataSource.
 * 
 * @author fyu
 */
public class JBossCachingManager {
    private static Category log = Category.getInstance(JBossCachingManager.class.getName());
    private static HashMap<String,CacheManager> cacheManagers = new HashMap<String, CacheManager>();

    /**
     * Get database connection.
     * 
     * @return Connection
     * @throws CacheException
     * @throws IllegalStateException
     * @throws SQLException
     */
    public static Object getElement(String app, String cacheName, Serializable key) {
        Cache cache = null;
        Object value;
        Element element;
        CacheManager cacheManager = cacheManagers.get(app);
        if(cacheManager == null)
            return null;
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

    public static void putElement(String app,
                                  String cacheName,
                                  Serializable key,
                                  Serializable value) {
        Cache cache;
        CacheManager cacheManager = cacheManagers.get(app);
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

    
    public static void init(String app, InputStream stream) {
        try {
            CacheManager cacheManager = new CacheManager(stream);
            // fillTests();
            // fillMethods();
            cacheManagers.put(app, cacheManager);
        } catch (CacheException cacheE) {
            log.warn("Unable to initialize cache manager: " + cacheE.getMessage());
            System.out.println(cacheE.getMessage());
            // } catch (SQLException sqlE) {
            // log.warn("Unable to prefil method and test cache: " +
            // sqlE.getMessage());
        }
        log.debug("CachingManager: " + app + " initialized.");
    }

    /**
     * Perform clean up tasks when this servlet is been shutting down.
     * 
     * @see javax.servlet.Servlet#destroy()
     */
    public static void destroy(String app) {
        try{
            cacheManagers.get(app).shutdown();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public static boolean isAlive(String app, String cache) {
        CacheManager cacheManager = cacheManagers.get(app);
        if(cacheManager != null && cacheManager.getStatus().equals(Status.STATUS_ALIVE)){
            if(cacheManager.getCache(cache) != null){
                return true;
            }
        }
        return false; 
    }
}