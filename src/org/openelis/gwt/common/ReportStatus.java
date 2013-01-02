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


/**
 * This class is used to update/send the status of report to the client (GWT and/or servlet).
 */
public class ReportStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected String message, path;   
    protected int percentComplete;    
    protected Status status;
        
    public enum Status {
        RUNNING, SAVED, PRINTED
    };
    
    public ReportStatus() {
        status = Status.RUNNING;
        percentComplete = 0;
        message = null;
    }
    
    /**
     * Gets/sets the message that is displayed to the user
     */
    public String getMessage() {
        return message;
    }

    public ReportStatus setMessage(String message) {
        this.message = DataBaseUtil.trim(message);
        return this;
    }

    /**
     * Gets/sets the percentage of the completed report 
     */
    public int getPercentComplete() {
        return percentComplete;
    }

    public ReportStatus setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
        return this;
    }
    
    /**
     * Gets/sets the current state of the report.
     */
    public Status getStatus() {
        return status;        
    }
    
    public ReportStatus setStatus(Status status) {
        this.status = status;
        return this;
    }
    
    /**
     * Gets/sets the file path for the servlet.
     */
    public String getPath() {
        return path;
    }
    
    public ReportStatus setPath(String path) {
        this.path = path;
        return this;
    }
}
