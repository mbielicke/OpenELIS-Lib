package org.openelis.gwt.common;

import org.openelis.gwt.common.SecurityModule.ModuleFlags;
import org.openelis.gwt.common.SecuritySection.SectionFlags;

import java.io.Serializable;
import java.util.HashMap;

public class SecurityUtil implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected HashMap<String,SecuritySection> sections = new HashMap<String,SecuritySection>();
    protected HashMap<String,SecurityModule> modules = new HashMap<String,SecurityModule>();
    
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
    
    public boolean hasModule(String name){
        if(modules.containsKey(name))
            return modules.get(name).hasSelect;
        else
            return false;
    }
    
    public boolean hasSection(String name){
        if(sections.containsKey(name))
            return sections.get(name).hasView;
        else
            return false;
    }
   
}
