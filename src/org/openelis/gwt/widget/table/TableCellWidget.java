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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.DataObject;

public interface TableCellWidget {
    public void clear();

    public void setDisplay();

    public void setEditor();

    public Widget getInstance(Node node);
    
    public TableCellWidget getNewInstance();
    
    public void saveValue();
    
    public void setField(DataObject field);
    
    public Widget getWidget();
    
    public void enable(boolean enabled);
    
    public void setCellWidth(int width);
    
    public void setFocus(boolean focus);
    
    public int getRowIndex();
    
    public void setRowIndex(int row);
}
