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

import java.util.ArrayList;

public class TableFieldErrorException extends FieldErrorException{

	private static final long serialVersionUID = 1L;
	protected int rowIndex = -1;
	protected ArrayList<Exception> childExceptionList = null; 
	protected String tableKey = null;
    
	
	public TableFieldErrorException() {
        super();
    }
    /*
    public TableFieldErrorException(String msg) {
        super(msg);
    }
    
    public TableFieldErrorException(String msg, String fieldName) {
        super(msg, fieldName);
    }
    */
    public TableFieldErrorException(String key, int rowIndex, String fieldName) {
        super(key, fieldName);
        this.rowIndex = rowIndex;
    }
    
    public TableFieldErrorException(String key, int rowIndex, String fieldName, String[] params) {
        super(key, fieldName, params);
        this.rowIndex = rowIndex;
    }
    
    public TableFieldErrorException(String key, int rowIndex, String fieldName,String tableKey) {
        this(key, rowIndex, fieldName); 
        this.tableKey = tableKey;
    }
    
    public TableFieldErrorException(String key, int rowIndex, String fieldName,String tableKey, String[] params) {
        this(key, rowIndex, fieldName, params); 
        this.tableKey = tableKey;
    }
    
    public int getRowIndex(){
    	return rowIndex;
    }

    public ArrayList<Exception> getChildExceptionList() {
        return childExceptionList;
    }

    public void setChildExceptionList(ArrayList<Exception> childExceptionList) {
        this.childExceptionList = childExceptionList;
    }

    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }
}
