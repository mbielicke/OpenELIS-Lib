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

import com.google.gwt.xml.client.Node;


import java.util.Vector;

public interface DataField extends DataObject {
    
    public void setRequired(boolean required);

    public boolean isRequired();

    public void addError(String err);

    public String[] getErrors();

    public Vector getValues();

    public void setMin(Object min);

    public void setMax(Object max);

    public void setKey(Object key);

    public Object getKey();

    public void addOption(Object key, Object val);

    public void clearErrors();

    public Object getInstance(Node node);

    public boolean isValid();

    public boolean isInRange();

    public Object getInstance();
    
    public String getTip();

    public void setTip(String tip);

}
