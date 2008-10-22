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

import org.openelis.gwt.common.SecurityModule.ModuleFlags;
import org.openelis.gwt.common.SecuritySection.SectionFlags;
import org.openelis.gwt.common.data.Data;

import java.util.HashMap;

public class SecurityUtil implements Data {

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

    public Object clone() {
        // TODO Auto-generated method stub
        return null;
    }

   
}
