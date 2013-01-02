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
import java.util.HashMap;

import org.openelis.gwt.common.ModulePermission.ModuleFlags;
import org.openelis.gwt.common.SectionPermission.SectionFlags;

/**
 * This class contains a basic set of information for a system user including
 * all the user's security permissions.
 */
public class SystemUserPermission implements Serializable {

    private static final long                    serialVersionUID = 1L;

    protected SystemUserVO                       user;
    protected HashMap<String, ModulePermission>  modules;
    protected HashMap<String, SectionPermission> sections;

    public SystemUserPermission() {
        modules = new HashMap<String, ModulePermission>();
        sections = new HashMap<String, SectionPermission>();
    }

    /**
     * Get basic user information
     */
    public Integer getSystemUserId() {
        return user.getId();
    }

    public String getLoginName() {
        return user.getLoginName();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getInitials() {
        return user.getInitials();
    }
    
    public SystemUserVO getUser() {
        return user;
    }

    public void setUser(SystemUserVO user) {
        this.user = user;
    }

    /**
     * Adds ModulePermission to the user's module list.
     */
    public void add(ModulePermission module) {
        modules.put(module.getName().trim(), module);
    }

    /**
     * Adds SectionPermission to the user's section list.
     */
    public void add(SectionPermission section) {
        sections.put(section.getName().trim(), section);
    }

    /**
     * Finds ModulePermission using specified name
     * 
     * @param name
     *        of module
     * @return ModulePermission object or null if it could not be found.
     */
    public ModulePermission getModule(String name) {
        return modules.get(name);
    }

    /**
     * Finds SectionPermission using specified name
     * 
     * @param name
     *        of section
     * @return SectionPermission object or null if it could not be found.
     */
    public SectionPermission getSection(String name) {
        return sections.get(name);
    }

    /**
     * Checks to see if the user has the requested permission for a module
     * 
     * @param name
     *        is the module name
     * @param perm
     *        is the requested module permission
     * @return true if user has permission; otherwise false
     */
    public boolean has(String name, ModuleFlags perm) {
        ModulePermission m;

        m = modules.get(name);
        if (m != null)
            return m.has(perm);
        return false;
    }

    /**
     * Checks to see if the user has the requested permission for a section
     * 
     * @param name
     *        is the section name
     * @param perm
     *        is the requested section permission
     * @return true if user has permission; otherwise false
     */
    public boolean has(String name, SectionFlags perm) {
        SectionPermission s;

        s = sections.get(name);
        if (s != null)
            return s.has(perm);
        return false;
    }

    /**
     * Checks to see if the user has the [string version] of requested
     * permission for a module
     */
    public boolean hasModule(String name, String perm) {
        ModulePermission m;

        m = modules.get(name);
        if (m != null)
            return m.has(perm);
        return false;
    }

    /**
     * Checks to see if the user has the [string version] of requested
     * permission for a section
     */
    public boolean hasSection(String name, String perm) {
        SectionPermission s;

        s = sections.get(name);
        if (s != null)
            return s.has(perm);
        return false;
    }

    public boolean hasConnectPermission() {
        return !modules.isEmpty() || !sections.isEmpty();
    }
}