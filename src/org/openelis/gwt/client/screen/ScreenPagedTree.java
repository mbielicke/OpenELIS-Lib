package org.openelis.gwt.client.screen;

import org.openelis.gwt.client.widget.pagedtree.TreeController;
import org.openelis.gwt.common.AbstractField;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

public class ScreenPagedTree extends ScreenWidget {
   public TreeController controller = new TreeController();
    /**
     * Default XML Tag Name used in XML Definition
     */
    public static final String TAG_NAME = "pagedtree";
    
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenPagedTree() {
    }
   
    public ScreenPagedTree(Node node, Screen screen) {
        super(node);
        this.screen = screen;
        
        
        
        createTree(node);
        initWidget(controller.view);
        
        
        setDefaults(node, screen);
    }
    
    /**
     * This method will initialize the TreeView member of the TreeController
     * @param node
     */
    public void createTree(Node node) {
     try {
                     
        if (node.getAttributes().getNamedItem("title") != null) {
            controller.view.setTitle(node.getAttributes()
                                     .getNamedItem("title")
                                     .getNodeValue());        
        
         }
     }catch (Exception e) {
         Window.alert(e.getMessage());
     }
    }
    
        

    public void setHeight(String height) {
        controller.view.setHeight(height);
    }

    /**
     * Sets the width of the table.
     */
    public void setWidth(String width) {
        controller.view.setWidth(width);
    }
    
    public ScreenWidget getInstance(Node node, Screen screen) {

        return new ScreenPagedTree(node, screen);
    }

    public void load(AbstractField field) {

        Document doc = XMLParser.parse((String)field.getValue());
        createTree(doc.getDocumentElement());
    }

    /**
     * Loads the Tree of widgets from an XML stored in String
     * @param xml
     */
    public void load(String xml) {
        Document doc = XMLParser.parse(xml);
        createTree(doc.getDocumentElement());
    }
    
    public void destroy() {
       // tree = null;
        super.destroy();
    }
    
    
    
    
}
