package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.Form;
import org.openelis.gwt.common.data.AbstractField;

public class ScreenTab extends ScreenInputWidget {
    
    public static final String TAG_NAME = "tab";
    
    public Widget wid;
    
    public ScreenTab() {

    }

    public ScreenTab(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        NodeList widgets = node.getChildNodes();
        for (int l = 0; l < widgets.getLength(); l++) {
            if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                break;
            }
        }
        initWidget(wid);
        setDefaults(node,screen);
    }
    
    @Override
    public void load(AbstractField field) {
        this.field = field;
        if(field instanceof Form)
            screen.load((Form)field);
    }
    
    @Override
    public void submit(AbstractField field) {
       // if(field instanceof Form) 
           // ((Form)field).unload(screen.widgets);
    }
}
