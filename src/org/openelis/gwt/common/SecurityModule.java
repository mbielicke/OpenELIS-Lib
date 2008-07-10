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

public class SecurityModule extends SecurityObject implements Serializable {
    
    
    protected boolean hasSelect,hasAdd,hasUpdate,hasDelete;
    protected String clause;

    private static final long serialVersionUID = 1L;
    
    public SecurityModule(Integer id, String name, boolean select, boolean add, boolean update, boolean delete, String clause){
        moduleId = id;
        moduleName = name;
        hasSelect = select;
        hasAdd = add;
        hasUpdate = update;
        hasDelete = delete;
        this.clause = clause;
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
   
   public boolean has(Flags fl){
       if(fl == Flags.ADD)
           return hasAdd;
       else if(fl == Flags.DELETE)
           return hasDelete;
       else if(fl == Flags.SELECT)
           return hasSelect;
       else if(fl == Flags.UPDATE)
           return hasUpdate;
       else
           return false;
   }
    

}
