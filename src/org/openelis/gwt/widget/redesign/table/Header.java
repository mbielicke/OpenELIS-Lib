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
package org.openelis.gwt.widget.redesign.table;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

public class Header extends Composite {
    
    protected FlexTable  flexTable;
    protected Table      table;
    
    
    public Header(Table table) {
        this.table = table;
        flexTable = new FlexTable();
        flexTable.setStyleName("Header");
        initWidget(flexTable);
        layout();
    }
    
    public void layout() {
        int numCols;
        Column column;
        
        
        numCols = table.getColumnCount();
        
        if(flexTable.getRowCount() <  1)
            flexTable.insertRow(0);
        
        
        for(int i = 0; i < numCols; i++) {
            column = table.getColumnAt(i);
            flexTable.setText(0,i,column.getLabel());
            flexTable.getColumnFormatter().setWidth(i,column.getWidth()+"px");            
        }
        flexTable.setWidth(table.getTableWidth()+"px");
        flexTable.getCellFormatter().setHeight(0, 0, table.getRowHeight()+"px");
        
    }

}
