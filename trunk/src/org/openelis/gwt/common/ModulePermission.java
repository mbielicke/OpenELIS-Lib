/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.common;

import java.io.Serializable;

public class ModulePermission implements Serializable {

    public enum ModuleFlags {
        SELECT, ADD, UPDATE, DELETE
    }

    protected boolean         hasSelect, hasAdd, hasUpdate, hasDelete;
    protected String          clause;
    protected String          name;

    private static final long serialVersionUID = 1L;

    public ModulePermission() {

    }

    public ModulePermission(String name, String select, String add, String update, String delete,
                          String clause) {
        this.name = name;
        hasSelect = "Y".equals(select);
        hasAdd = "Y".equals(add);
        hasUpdate = "Y".equals(update);
        hasDelete = "Y".equals(delete);
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

    public boolean has(ModuleFlags flag) {
        switch (flag) {
            case ADD:
                return hasAdd;
            case DELETE:
                return hasDelete;
            case SELECT:
                return hasSelect;
            case UPDATE:
                return hasUpdate;
            default:
                return false;
        }
    }

    public boolean has(String flag) {
        return has(ModuleFlags.valueOf(flag));
    }
}
