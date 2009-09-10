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
package org.openelis.gwt.common.data.deprecated;

import com.google.gwt.xml.client.Node;

@Deprecated
public class QueryDoubleField extends QueryField {

    private static final long serialVersionUID = 1L;
    private Double max;
    private Double min;
    public static final String TAG_NAME = "rpc-queryDouble";
    
    public QueryDoubleField() {
        
    }
    
    public QueryDoubleField(Node node){
        setAttributes(node);
    }
    
    public QueryDoubleField(String key) {
        this.key = key;
    }
    
    public void setAttributes(Node node) {
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
        if (node.getAttributes().getNamedItem("max") != null)
            setMax(new Integer(node.getAttributes()
                                          .getNamedItem("max")
                                          .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            setMin(new Integer(node.getAttributes()
                                          .getNamedItem("min")
                                          .getNodeValue()));
    }

    public void validate() {
        for(String param : parameter){
            try {
                Double.parseDouble(param);
            } catch (Exception e) {
                addError("Param is not a valid double");
                valid = false;
                return;
            }
        }
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (value == null)
            return true;
        for (String param : parameter) {
            double pVal = Double.parseDouble(param);
            if (max != null && pVal > max) {
                addError("Field exceeded maximum value");
                return false;
            }
            if (min != null && pVal < min) {
                addError("Field is below minimum value");
                return false;
            }
        }
        return true;
    }

    public void setMin(Object min) {
        this.min = (Double)min;
    }

    public void setMax(Object max) {
        this.max = (Double)max;
    }

    public Object clone() {
        QueryDoubleField obj = new QueryDoubleField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setRequired(required);
        obj.setValue(value);
        obj.setKey(key);
        return obj;
    }

    public QueryDoubleField getInstance(Node node) {
        return new QueryDoubleField(node);
    }
}
