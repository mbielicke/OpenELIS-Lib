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

import java.io.Serializable;

public class ModelObject implements DataObject, Serializable {

    private static final long serialVersionUID = 1L;
    
    protected DataModel value;

    public ModelObject(){
        
    }
    
    public ModelObject(DataModel val){
        setValue(val);
    }
    
    public Object getInstance() {
        ModelObject clone = new ModelObject();
        clone.setValue(value);
        return clone;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = (DataModel)value;
    }

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

}
