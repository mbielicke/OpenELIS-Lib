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

import com.google.gwt.xml.client.Node;


import java.util.ArrayList;
import java.util.Vector;

public interface DataField extends DataObject {
    
    public void setRequired(boolean required);

    public boolean isRequired();

    public void addError(String err);

    public ArrayList<String> getErrors();

    public Vector getValues();

    public void setMin(Object min);

    public void setMax(Object max);

    public void setKey(Object key);

    public Object getKey();

    public void addOption(Object key, Object val);

    public void clearErrors();

    public <T extends DataField> T getInstance(Node node);

    public boolean isValid();

    public boolean isInRange();
    
    public String getTip();

    public void setTip(String tip);

}
