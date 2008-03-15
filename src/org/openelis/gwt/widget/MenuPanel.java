package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuPanel extends Composite {
    
    String layout;
    private CellPanel panel;

    
    public MenuPanel(String layout) {
        if(layout.equals("vertical"))
            panel = new VerticalPanel();
        else
            panel = new HorizontalPanel();
        initWidget(panel);
    }
    
    public void add(Widget wid){
        panel.add(wid);
    }
    
    public void clear(){
        panel.clear();
    }
    

}
