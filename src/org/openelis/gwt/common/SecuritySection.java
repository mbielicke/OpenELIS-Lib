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

import java.io.Serializable;

public class SecuritySection implements Serializable {
    
    public enum SectionFlags {VIEW,ASSIGN,COMPLETE,RELEASE,CANCEL}
    
    private static final long serialVersionUID = 1L;
    protected boolean hasView, hasAssign, hasComplete, hasRelease, hasCancel;
    protected String name;
    
    public SecuritySection() {
        
    }
    
    public SecuritySection(String name, String view, String assign, String complete, String release, String cancel){
        this.name = name;
        if("Y".equals("view"))
            hasView = true;
        if("Y".equals(assign))
            hasAssign = true;
        if("Y".equals(complete))
            hasComplete = true;
        if("Y".equals(release))
            hasRelease = true;
        if("Y".equals(cancel))
            hasCancel = true;
    }
    
    public SecuritySection(String name, boolean view, boolean assign, boolean complete, boolean release, boolean cancel){
       this.name = name;
       hasView = view;
       hasAssign = assign;
       hasComplete = complete;
       hasRelease = release;
       hasCancel = cancel;
    }
    
    public String getName() {
        return name;
    }
    /**
     * Returns true if user has select permission on this group
     */
    public boolean hasViewPermission() {
       return hasView;
    }

    /**
     * Returns true if user has add permission on this group
     */
    public boolean hasAssignPermission() {
       return hasAssign;
    }

    /**
     * Returns true if user has complete permission on this group
     */
    public boolean hasCompletePermission() {
       return hasComplete;
    }

    /**
     * Returns true if user has cancel permission on this group
     */
    public boolean hasCancelPermission() {
       return hasCancel;
    }

    /**
     * Returns true if user has release permission on this group
     */
    public boolean hasReleasePermission() {
       return hasRelease;
    }
    
    public boolean has(SectionFlags fl) {
        if(fl == SectionFlags.ASSIGN)
            return hasAssign;
        else if(fl == SectionFlags.CANCEL)
            return hasCancel;
        else if(fl == SectionFlags.COMPLETE)
            return hasComplete;
        else if(fl == SectionFlags.RELEASE)
            return hasRelease;
        else if(fl == SectionFlags.VIEW)
            return hasView;
        else
            return false;
    }

}
