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
package org.openelis.gwt.common.data;

public class BooleanObject extends DataObject<Boolean> implements FieldType {

    private static final long serialVersionUID = 1L;

    public BooleanObject() {       
    }
    
    public BooleanObject(Boolean val){
        super(val);
    }
    
    public BooleanObject(boolean val) {
        this(new Boolean(val));
    }
    
    public BooleanObject(String val) {
        if(val.equals("false") || val.equals("true"))
            setValue(new Boolean(val));
        else if(val.equalsIgnoreCase("Y"))
            setValue(new Boolean(true));
        else
            setValue(new Boolean(false));
    }
    
    public Object clone() {
        return new BooleanObject(value);
    }


}
