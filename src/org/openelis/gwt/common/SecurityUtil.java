package org.openelis.gwt.common;

import org.openelis.gwt.common.SecurityModule.ModuleFlags;
import org.openelis.gwt.common.SecuritySection.SectionFlags;
import org.openelis.gwt.common.data.DataObject;

import java.io.Serializable;
import java.util.HashMap;

public class SecurityUtil implements Serializable, DataObject {

    private static final long serialVersionUID = 1L;
    
    protected HashMap<String,SecuritySection> sections = new HashMap<String,SecuritySection>();
    protected HashMap<String,SecurityModule> modules = new HashMap<String,SecurityModule>();
    
    private Integer system_user_id;
    
    public SecurityUtil() {
        
    }
    
    public void setSystemUserId(Integer id){
        this.system_user_id = id;
    }
   
    public Integer getSystemUserId() {
        return this.system_user_id;
    }
    
    public void add(SecurityModule sm){
        modules.put(sm.getName(), sm);
    }
    
    public void add(SecuritySection ss){
        sections.put(ss.getName(), ss);
    }
    
    public SecurityModule getModule(String name){
        return modules.get(name);
    }
    
    public SecuritySection getSection(String name){
        return sections.get(name);
    }
    
    public boolean has(String name, ModuleFlags fl){
        if(modules.containsKey(name))
            return modules.get(name).has(fl);
        else
            return false;
    }
    
    public boolean has(String name, SectionFlags fl){
        if(sections.containsKey(name))
            return sections.get(name).has(fl);
        else
            return false;
    }
    
    public boolean hasModule(String name, String fl){
        if(modules.containsKey(name))
            return modules.get(name).has(ModuleFlags.valueOf(fl));
        else
            return false;
    }
    
    public boolean hasSection(String name, String fl){
        if(sections.containsKey(name))
            return sections.get(name).has(SectionFlags.valueOf(fl));
        else
            return false;
    }

    /** 
     * Not used, only here to satisfy DataObject Interface for RPC
     * @return
     */
    public Object getInstance() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object object) {
        // TODO Auto-generated method stub
        
    }

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }
   
}
