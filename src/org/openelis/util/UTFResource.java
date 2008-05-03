package org.openelis.util;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UTFResource {
    
    private PropertyResourceBundle bundle;
    
    public static UTFResource getBundle(String name){
        UTFResource ret = new UTFResource();
        ret.bundle = (PropertyResourceBundle)ResourceBundle.getBundle(name);
        return ret;
    }
    
    public static UTFResource getBundle(String name, Locale locale){
        UTFResource ret = new UTFResource();
        ret.bundle = (PropertyResourceBundle)ResourceBundle.getBundle(name,locale);
        return ret;
    }
    
    public String getString(String key){
        try {
            return new String(this.bundle.getString(key).getBytes("ISO-8859-1"),"UTF-8");
        }catch(Exception e){
            return "EXCEPTION";
        }
    }
    

}
