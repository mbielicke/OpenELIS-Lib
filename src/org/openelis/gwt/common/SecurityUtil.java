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

import java.util.HashMap;

import org.openelis.gwt.common.SecurityModule.ModuleFlags;
import org.openelis.gwt.common.SecuritySection.SectionFlags;

/**
 * This class contains a basic set of information for a system user including
 * all the user's security permissions.
 */
public class SecurityUtil implements RPC {

    private static final long                  serialVersionUID = 1L;

    protected Integer                          systemUserId;
    protected String                           systemUserName, firstName, lastName, initials;
    protected HashMap<String, SecurityModule>  modules;
    protected HashMap<String, SecuritySection> sections;

    public SecurityUtil() {
        modules = new HashMap<String, SecurityModule>();
        sections = new HashMap<String, SecuritySection>();
    }

    /**
     * Get/Set basic user information
     */
    public Integer getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Integer userId) {
        systemUserId = userId;
    }

    public String getSystemUserName() {
        return systemUserName;
    }

    public void setSystemUserName(String userName) {
        systemUserName = userName.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials.trim();
    }

    /**
     * Adds SecurityModule to the user's module list.  
     */
    public void add(SecurityModule module) {
        modules.put(module.getName().trim(), module);
    }

    /**
     * Adds SecuritySection to the user's section list.  
     */
    public void add(SecuritySection section) {
        sections.put(section.getName().trim(), section);
    }

    /**
     * Finds SecurityModule using specified name 
     * @param name of module 
     * @return SecurityModule object or null if it could not be found.
     */
    public SecurityModule getModule(String name) {
        return modules.get(name);
    }

    /**
     * Finds SecuritySection using specified name 
     * @param name of section 
     * @return SecuritySection object or null if it could not be found.
     */
    public SecuritySection getSection(String name) {
        return sections.get(name);
    }

    /**
     * Checks to see if the user has the requested permission for a module
     * @param name is the module name
     * @param perm is the requested module permission
     * @return true if user has permission; otherwise false
     */
    public boolean has(String name, ModuleFlags perm) {
        SecurityModule m;
        
        m = modules.get(name);
        if (m != null)
            return m.has(perm);
        return false;
    }

    /**
     * Checks to see if the user has the requested permission for a section
     * @param name is the section name
     * @param perm is the requested section permission
     * @return true if user has permission; otherwise false
     */
    public boolean has(String name, SectionFlags perm) {
        SecuritySection s;
        
        s = sections.get(name);
        if (s != null)
            return s.has(perm);
        return false;
    }

    /**
     * Checks to see if the user has the [string version] of requested permission
     * for a module
     * @private
     */
    public boolean hasModule(String name, String perm){
        SecurityModule m;

        m = modules.get(name);
        if (m != null)
            return m.has(ModuleFlags.valueOf(perm));
        return false;
    }
    
    /**
     * Checks to see if the user has the [string version] of requested permission
     * for a section
     * @private
     */
    public boolean hasSection(String name, String perm){
        SecuritySection s;

        s = sections.get(name);
        if (s != null)
            return s.has(SectionFlags.valueOf(perm));
        return false;
    }
}
