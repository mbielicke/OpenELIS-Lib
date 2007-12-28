package org.openelis.gwt.client.screen;

import org.openelis.gwt.client.widget.pagedtree.TreeController;
import org.openelis.gwt.client.widget.pagedtree.TreeModel;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.TableModel;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

public class ScreenPagedTree extends ScreenWidget {
   public TreeController controller = new TreeController();
   private int itemsPerPage = 0;
    /**
     * Default XML Tag Name used in XML Definition
     */
    public static final String TAG_NAME = "pagedtree";
    
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenPagedTree() {
    }
   
    public ScreenPagedTree(Node node, ScreenBase screen) {
        super(node);
        this.screen = screen;
        
        
        
        createTree(node);
        initWidget(controller.view);
        
        
        setDefaults(node, screen);
        //screen.widgets.put(node.getAttributes().getNamedItem("key").getNodeValue(),this );
    }
    
    /**
     * This method will initialize the TreeView member of the TreeController
     * @param node
     */
    public void createTree(Node node) {
     
                     
        if (node.getAttributes().getNamedItem("title") != null) {
            controller.view.setTitle(node.getAttributes()
                                     .getNamedItem("title")
                                     .getNodeValue());        
        
         }
        if (node.getAttributes().getNamedItem("itemsPerPage") != null) {
            String ipp  = node.getAttributes().getNamedItem("itemsPerPage").getNodeValue();
            Integer itemsPerPageVal = new Integer(ipp);
            controller.model.itemsPerPage =  itemsPerPageVal.intValue();
            itemsPerPage =  itemsPerPageVal.intValue();
         }else{
             controller.model.itemsPerPage = 20;
             itemsPerPage = 20;
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
    
    public ScreenWidget getInstance(Node node, ScreenBase screen) {

        return new ScreenPagedTree(node, screen);
    }

    public void load(AbstractField field) {            
              if (field.getValue() != null)
                  controller.setModel((TreeModel)field.getValue());
              else{
                  controller.model.reset();
                  controller.reset();
                  field.setValue(controller.model);
              }
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

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
    
    
    
    
}
