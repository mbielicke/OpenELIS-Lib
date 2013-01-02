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


/**
 *  Class represents a system user object with the fields in the database 
 *  security for table system_user. 
 */

public class SystemUserVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    protected Integer         id;
    protected String          externalId, loginName, lastName, firstName, initials,
                              isEmployee, isActive, isTemplate;

    public SystemUserVO() {
    }

    public SystemUserVO(Integer id, String externalId, String loginName, String lastName,
                        String firstName, String initials, String isEmployee, String isActive,
                        String isTemplate) {
        this.id = id;
        this.externalId = DataBaseUtil.trim(externalId);
        this.loginName = DataBaseUtil.trim(loginName);
        this.lastName = DataBaseUtil.trim(lastName);
        this.firstName = DataBaseUtil.trim(firstName);
        this.initials = DataBaseUtil.trim(initials);
        this.isEmployee = DataBaseUtil.trim(isEmployee);
        this.isActive = DataBaseUtil.trim(isActive);
        this.isTemplate = DataBaseUtil.trim(isTemplate);
    }
    
    public Integer getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getInitials() {
        return initials;
    }

    public String getIsEmployee() {
        return isEmployee;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getIsTemplate() {
        return isTemplate;
    }
}
