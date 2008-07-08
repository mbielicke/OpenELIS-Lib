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


public class TableField extends AbstractField {

    private static final long serialVersionUID = 1L;
    private TableModel value;
    public static final String TAG_NAME = "rpc-table";
    
    public TableField() {
        
    }
    
    public TableField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }

    public void validate() {
    	if(value != null)
    		valid = value.validate();
    	else
    		valid = true;
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
            value = (TableModel)val;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return value;
    }

    public TableField getInstance() {
        TableField obj = new TableField();
        obj.setKey(key);
        obj.setRequired(required);
        obj.setTip(tip);
        obj.setValue(value);
        
        return obj;
    }

    public TableField getInstance(Node node) {
        return new TableField(node);
    }
    
    public void clearErrors() {
        if(value != null)
            value.clearErrors();
    }
    
    
}
