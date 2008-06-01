package org.openelis.gwt.common;

import java.io.Serializable;
import java.util.HashMap;

public class Preferences implements Serializable{
    
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
