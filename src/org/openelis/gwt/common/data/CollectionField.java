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
package org.openelis.gwt.common.data;


import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.xml.client.Node;

public class CollectionField extends AbstractField implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<DataObject> coll = new ArrayList<DataObject>();
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

    public void addItem(DataObject item) {
        coll.add(item);
    }

    public CollectionField getInstance() {
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
