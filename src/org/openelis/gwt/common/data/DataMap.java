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

import java.util.HashMap;
import java.util.Iterator;

/**
 * DataMap extends HashMap<String, Data> and implements the Data interface for passing 
 * maps of Data objects between a client and the Server. 
 * @author tschmidt
 *
 */
@ Deprecated public class DataMap extends HashMap<String,Field> implements Field {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method will create a new object and set it's member fields a
     * and values to the same as the object being called.
     */
    public Object clone() {
        DataMap dataMap = new DataMap();
        Iterator keyIt = keySet().iterator();
        while(keyIt.hasNext()){
            String key = (String)keyIt.next();
            dataMap.put(key, (Field)((DataObject)get(key)).clone());
        }
        return dataMap;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object obj) {
        // TODO Auto-generated method stub
        
    }

    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

}
