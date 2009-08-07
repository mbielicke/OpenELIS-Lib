package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.TreeDataModel;
import org.openelis.gwt.widget.diagram.Diagram;

@Deprecated
public class ScreenDiagram extends ScreenInputWidget {
    
    /**
     * Default XML Tag Name used in XML Definition
     */
    public static String TAG_NAME = "diagram";
    /**
     * Widget wrapped by this class
     */
    private Diagram diagram;
    
    public ScreenDiagram() {
        
    }
    
    public ScreenDiagram(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        this.screen = screen;
        int height = -1;
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            diagram = (Diagram)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            diagram = new Diagram();
        initWidget(diagram);
        setDefaults(node, screen);
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenDiagram(node, screen);
    }

    public void load(AbstractField field) {
        if (field.getValue() != null)
            diagram.draw(((TreeDataModel)field.getValue()));
    }

    public void submit(AbstractField field) {
        field.setValue(diagram.model);
    }

    public Widget getWidget() {
        return diagram;
    }
    
    public void destroy(){
        diagram = null;
        super.destroy();
    }
        
    public void enable(boolean enabled){
        super.enable(enabled);
    }

}
