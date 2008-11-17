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


import java.util.ArrayList;

import com.google.gwt.xml.client.Node;

public class CollectionField extends AbstractField  {

    private static final long serialVersionUID = 1L;
    private ArrayList<Data> coll = new ArrayList<Data>();
    private String type = "";
    public static final String TAG_NAME = "rpc-collection";

    public CollectionField() {
        
    }
    
    public CollectionField(Node node){
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("type") != null)
            setType(node.getAttributes()
                               .getNamedItem("type")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
    }
    
    public void validate() {
        if (required) {
            if (coll.size() == 0) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        valid = true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        coll = (ArrayList)val;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return coll;
    }

    public void addItem(Data item) {
        coll.add(item);
    }

    public Object clone() {
        CollectionField obj = new CollectionField();
        obj.setRequired(required);
        obj.setType(type);
        obj.setValue(coll);
        obj.setKey(key);
        return obj;
    }
    
    public CollectionField getInstance(Node node) {
        return new CollectionField(node);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < coll.size(); i++){
            if(i > 0)
                sb.append(",");
            sb.append(((StringObject)coll.get(i)).getValue());
        }
        return sb.toString();
    }
}
