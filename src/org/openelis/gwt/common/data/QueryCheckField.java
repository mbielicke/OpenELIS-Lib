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

public class QueryCheckField extends CheckField {

    private static final long serialVersionUID = 1L;
    
    public static final String TAG_NAME = "rpc-queryCheck";
    
    public QueryCheckField() {
        
    }
    
    public QueryCheckField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }

    public QueryCheckField getInstance(Node node) {
        return new QueryCheckField(node);
    }

    public QueryCheckField getInstance() {
        QueryCheckField obj = new QueryCheckField();
        obj.setRequired(required);
        obj.setValue(getValue());
        return obj;
    }
}
