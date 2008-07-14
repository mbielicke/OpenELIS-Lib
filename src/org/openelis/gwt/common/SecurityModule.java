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
package org.openelis.gwt.common;

import java.io.Serializable;

public class SecurityModule implements Serializable {
    
    public enum ModuleFlags {SELECT,ADD,UPDATE,DELETE}
    
    protected boolean hasSelect,hasAdd,hasUpdate,hasDelete;
    protected String clause;
    protected String name;

    private static final long serialVersionUID = 1L;
    
    public SecurityModule() {
        
    }
    
    public SecurityModule(String name, String select, String add, String update, String delete, String clause){
        this.name = name;
        if("Y".equals(select))
            hasSelect = true;
        if("Y".equals(add))
            hasAdd = true;
        if("Y".equals(update))
            hasUpdate = true;
        if("Y".equals(delete))
            hasDelete = true;
        this.clause = clause;
    }
    
    public SecurityModule(String name, boolean select, boolean add, boolean update, boolean delete, String clause){
        this.name = name;
        hasSelect = select;
        hasAdd = add;
        hasUpdate = update;
        hasDelete = delete;
        this.clause = clause;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * Returns true if user has select permission on this module
     */
   public boolean hasSelectPermission() {
       return hasSelect;
   }

   /**
     * Returns true if user has add permission on this module
     */
   public boolean hasAddPermission() {
       return hasAdd;
   }

   /**
     * Returns true if user has update permission on this module
     */
   public boolean hasUpdatePermission() {
       return hasUpdate;
   }

   /**
     * Returns true if user has delete permission on this module
     */
   public boolean hasDeletePermission() {
       return hasDelete;
   }
   
   public String getClause() {
       return clause;
   }
   
   public boolean has(ModuleFlags fl){
       if(fl == ModuleFlags.ADD)
           return hasAdd;
       else if(fl == ModuleFlags.DELETE)
           return hasDelete;
       else if(fl == ModuleFlags.SELECT)
           return hasSelect;
       else if(fl == ModuleFlags.UPDATE)
           return hasUpdate;
       else
           return false;
   }
    

}
