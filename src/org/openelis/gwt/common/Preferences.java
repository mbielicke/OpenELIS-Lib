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
package org.openelis.gwt.common;

import java.util.HashMap;

public class Preferences implements RPC{
    
    private static final long serialVersionUID = 1L;
    
    private HashMap<String,String> map = new HashMap<String,String>();
    private String key;
    private Integer id;
    
    public Preferences() {
        
    }
    
    public String get(String key){
        return map.get(key);
    }
    
    public boolean getBoolean(String key){
        return Boolean.valueOf(map.get(key)).booleanValue();
    }
    
    public double getDouble(String key){
        return Double.parseDouble(map.get(key));
    }
    
    public int getInt(String key){
        return Integer.parseInt(map.get(key));
    }
    
    public void put(String key, String value){
        map.put(key, value);
    }
    
    public void putBoolean(String key, boolean value){
        map.put(key, Boolean.toString(value));
    }
    
    public void putDouble(String key, double value){
        map.put(key, Double.toString(value));
    }
    
    public void putInt(String key, int value){
        map.put(key,Integer.toString(value));
    }
    
    public void remove(String key){
        map.put(key, null);
    }
    
    public void removePreference(String key){
        map.remove(key);
    }
    
    public void setKey(String key){
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
    
    public HashMap getMap() {
        return map;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }
    
}
