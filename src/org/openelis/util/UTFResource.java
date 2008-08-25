/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.util;

import java.util.Enumeration;
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
    
    public  Enumeration<String> getKeys() {
        return bundle.getKeys();
    }
}
