package org.openelis.gwt.client.widget.pagedtree;

import java.io.Serializable;
import java.util.ArrayList;

import org.openelis.gwt.client.screen.ScreenBase;
import org.openelis.gwt.client.screen.ScreenLabel;

import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


public class TreeModel implements Serializable {
    
    private static final long serialVersionUID = -5412348933917626639L;
    public boolean paged;
    public int totalPages;
    public int itemsPerPage;    
    public int pageIndex;
    public int totalItems;
    public int shown;
    public boolean showIndex;
    public boolean autoAdd;
    
    /**
     * @gwt.typeArgs <java.lang.Boolean>
     */
    //private ArrayList hasDummyChild = new ArrayList();
    
    /**
     * @gwt.typeArgs <org.openelis.gwt.client.widget.pagedtree.TreeModelItem>
     */
     
    private ArrayList items = new ArrayList();
  
    //private ScreenTree tree = null;

    
    public TreeModelItem getItem(int index){
       return (TreeModelItem)items.get(index);
    } 
    
    public void addItem(TreeModelItem item){
        items.add(item);        
    }

    public int numItems(){
        return items.size();
    }
    
    public void deleteItem(int index){
        items.remove(index);
    }
    
    public void reset(){
        items = new ArrayList();
        totalItems = 0;
        shown = 0;        
    }

    
    public void addTextChildItems(TreeItem parent, String xml){
        Document doc = XMLParser.parse(xml);                   
        Node node = doc.getDocumentElement();
        
        NodeList items = node.getChildNodes();
        for (int itemIter = 0; itemIter < items.getLength(); itemIter++) {
            if (items.item(itemIter).getNodeType() == Node.ELEMENT_NODE){
              TreeItem childitem = createTreeItem(items.item(itemIter));                                                  
              parent.addItem(childitem);
            }
            
         }        
    }    
    
    
    private TreeItem createTreeItem(Node node){
        
        String itemText = null;
        Object userObject = null;
        ScreenLabel label = null;
        if (node.getAttributes().getNamedItem("text") != null) {
            itemText = node.getAttributes().getNamedItem("text").getNodeValue();
        }
        if (node.getAttributes().getNamedItem("value") != null) {
            userObject = node.getAttributes().getNamedItem("value").getNodeValue();
        }
        
        TreeItem item = null;
        if(itemText!=null){
          label = new ScreenLabel(itemText,userObject)  ;
        } 
        
        if(label!=null){
            //initWidget(label);
            item = new TreeItem(label);
           // item.setText(((Label)label.getWidget()).getText());
        }
          item.setUserObject(userObject);
          
        NodeList items = node.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
                item.addItem(createTreeItem(items.item(i)));
            }
        }
        return item;
    }   
   
    public void addChildWidgets(TreeItem parent, String xml,ScreenBase screen){
        Document doc = XMLParser.parse(xml);                   
        Node node = doc.getDocumentElement();
        
        NodeList items = node.getChildNodes();
        for (int itemIter = 0; itemIter < items.getLength(); itemIter++) {
            if (items.item(itemIter).getNodeType() == Node.ELEMENT_NODE){
              TreeItem childitem = createTreeItem(items.item(itemIter),screen);                                                  
              parent.addItem(childitem);
            }
            
         }        
    } 
    
    private TreeItem createTreeItem(Node node, ScreenBase screen) {       
        TreeItem item = new TreeItem(ScreenBase.getWidgetMap().getWidget(node, screen));
        if (node.getAttributes().getNamedItem("key") != null) {
            screen.widgets.put(node.getAttributes()
                                  .getNamedItem("key")
                                  .getNodeValue(), item);
        }
        if (node.getAttributes().getNamedItem("value") != null) {
            item.setUserObject(node.getAttributes()
                                   .getNamedItem("value")
                                   .getNodeValue());
        }
        NodeList items = node.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
                item.addItem(createTreeItem(items.item(i), screen));
            }
        }
        return item;
    }
    
 }
