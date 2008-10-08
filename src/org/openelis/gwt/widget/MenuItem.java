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
package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuItem extends Composite {
    
    
    public MenuItem(Widget wid) {
        initWidget(wid);
    }
    
    public static Widget createDefault(String icon, String labelText, String descriptionText) {
        FlexTable table = new FlexTable();
        table.setStyleName("TopMenuRowContainer");
        AbsolutePanel iconPanel = new AbsolutePanel();
        iconPanel.setStyleName(icon);
        VerticalPanel textPanel = new VerticalPanel();
        Label label = new Label(labelText);
        label.setStyleName("topMenuItemTitle");
        label.addStyleName("locked");
        
        Label description;
        if("EXCEPTION".equals(descriptionText))
        	description = new Label();
        else
        	description = new Label(descriptionText);
        
        description.setStyleName("topMenuItemDesc");
        table.setWidget(0,0,iconPanel);
        textPanel.add(label);
        textPanel.add(description);
        table.setWidget(0,1,textPanel);
        table.getFlexCellFormatter().setStyleName(0,0,"topMenuIcon");
        table.getFlexCellFormatter().setStyleName(0,1,"topMenuItemMiddle");
        return table;
    }

}
