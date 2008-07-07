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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;


public class ModelField extends AbstractField {

    private static final long serialVersionUID = 1L;

    private DataModel value;
    public static final String TAG_NAME = "rpc-model";
    
    public ModelField() {
        
    }
    
    public ModelField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }

    public void validate() {
        // TODO Auto-generated method stub
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        if(val == null)
            value = null;
        else
            value = (DataModel)val;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return value;
    }

    public ModelField getInstance() {
        ModelField obj = new ModelField();
       
        obj.setRequired(required);
        obj.setValue(value.getInstance());
        obj.setRequired(required);
        obj.setTip(tip);
        obj.setKey(key);
        
        return obj;
    }

    public ModelField getInstance(Node node) {
       return new ModelField(node);
    }
}
