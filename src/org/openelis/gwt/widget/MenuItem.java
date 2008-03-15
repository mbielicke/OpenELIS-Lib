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
        Label description = new Label(descriptionText);
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
