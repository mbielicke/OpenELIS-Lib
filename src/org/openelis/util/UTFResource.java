package org.openelis.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UTFResource {
    
    private PropertyResourceBundle bundle;
    private static final String OPENELIS_CONSTANTS = "org.openelis.modules.main.server.constants.OpenELISConstants";
    public static String DEFAULT_LANG = "en";
    private static HashMap<Locale, UTFResource> cache = new HashMap();
    
    public static UTFResource getBundle(String constantClass, Locale locale){
        UTFResource ret;
        
        ret = cache.get(locale);
        
        if(ret == null)
            ret = buildBundle(constantClass, locale);
            
        return ret;
    }
    
    public static UTFResource getBundle(String localeString){
        UTFResource res;
        Locale locale;
        
        if(localeString != null)
            locale = new Locale(localeString);
        else
            locale = new Locale(DEFAULT_LANG);            
        
        res = cache.get(locale);
        
        if(res == null)
            res = buildBundle(OPENELIS_CONSTANTS, locale);       
        
        return res;
    }
    
    public String getString(String key){
        try {
            return new String(this.bundle.getString(key).getBytes("ISO-8859-1"),"UTF-8");
        }catch(Exception e){
            return "EXCEPTION";
        }
    }
    
    private static UTFResource buildBundle(String name, Locale locale){
        UTFResource ret = new UTFResource();
        
        ret.bundle = (PropertyResourceBundle)ResourceBundle.getBundle(name,locale);
        cache.put(locale, ret);
        
        return ret;
    }
}
