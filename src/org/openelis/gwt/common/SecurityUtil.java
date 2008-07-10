package org.openelis.gwt.common;

import org.openelis.gwt.common.SecurityObject.Flags;

import java.io.Serializable;
import java.util.HashMap;

public class SecurityUtil implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected HashMap<Integer,SecurityObject> mapById = new HashMap<Integer,SecurityObject>();
    protected HashMap<String,SecurityObject> mapByName = new HashMap<String,SecurityObject>();
    
    public void add(SecurityObject so){
        mapById.put(so.getId(), so);
        mapByName.put(so.getName(), so);
    }
    
    public SecurityObject get(Integer id){
        return mapById.get(id);
    }
    
    public SecurityObject get(String name){
        return mapById.get(name);
    }
    
    public boolean has(Integer id, Flags fl){
        if(mapById.containsKey(id))
            return get(id).has(fl);
        else
            return false;
    }
    
    public boolean has(String name, Flags fl){
        if(mapById.containsKey(name))
            return get(name).has(fl);
        else
            return false;
    }

}
