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
package org.openelis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UTFResource {
    
    private ArrayList<PropertyResourceBundle> bundles;
    private static final String OPENELIS_CONSTANTS = "org.openelis.constants.OpenELISConstants";
    public static String DEFAULT_LANG = "en";
    
    private static HashMap<String, UTFResource> cache = new HashMap<String,UTFResource>();
    
    public static UTFResource getBundle(String constantClass, Locale locale){
        UTFResource ret;
        
        ret = cache.get(constantClass+locale.toString());
        
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
        	for(PropertyResourceBundle bundle : bundles) {
        		if(bundle.containsKey(key))
        			return new String(bundle.getString(key).getBytes("ISO-8859-1"),"UTF-8");
        	}
            return "EXCEPTION";
        }catch(Exception e) {
        	e.printStackTrace();
        	return "EXCEPTION";
        }
    }
    
    private static UTFResource buildBundle(String name, Locale locale){
        UTFResource ret = new UTFResource();
        ret.bundles = new ArrayList<PropertyResourceBundle>();
        
        ret.bundles.add((PropertyResourceBundle)ResourceBundle.getBundle(name,locale));
        
        if(ret.bundles.get(0).containsKey("include")) {
        	String[] includes = ret.bundles.get(0).getString("include").split(",");
        	for(String include : includes) {
        		ret.bundles.add((PropertyResourceBundle)ResourceBundle.getBundle(include,locale));
        	}
        }
        
        cache.put(name+locale.toString(), ret);
        
        return ret;
    }
    
    public HashMap<String,String> getMap() {
    	HashMap<String,String> map;
    	
    	map = new HashMap<String,String>();
    	
    	for(int i = bundles.size() -1; i > -1; i--) {
    		for(String key : bundles.get(i).keySet())
    			map.put(key, bundles.get(i).getString(key));
    	}
    	
    	return map;
    }
   
}
