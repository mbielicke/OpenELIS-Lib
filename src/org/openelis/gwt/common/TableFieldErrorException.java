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

public class TableFieldErrorException extends FieldErrorException{

	private static final long serialVersionUID = 1L;
	int rowIndex = -1;

	public TableFieldErrorException() {
        super();
    }

    public TableFieldErrorException(String msg) {
        super(msg);
    }
    
    public TableFieldErrorException(String msg, String fieldName) {
        super(msg, fieldName);
    }
    
    public TableFieldErrorException(String msg, int rowIndex, String fieldName) {
        super(msg, fieldName);
        
        this.rowIndex = rowIndex;
    }
    
    public int getRowIndex(){
    	return rowIndex;
    }
}
