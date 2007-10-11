package org.openelis.gwt.client.screen;

import org.openelis.gwt.common.AbstractField;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
/**
 * ScreenTree wraps a GWT Tree to display widgets on a Screen.
 * @author tschmidt
 *
 */
public class ScreenTree extends ScreenWidget {
	/**
	 * Default XML Tag Name used in XML Definition
	 */
	public static String TAG_NAME = "tree";
	/**
	 * Widget wrapped by this class
	 */
    private Tree tree;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTree() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;tree key="string"&gt;
     *   &lt;widget/&gt;
     *   &lt;widget&gt;
     *     &lt;widget/&gt;
     *     &lt;widget/&gt;
     *   &lt;/widget&gt;
     * &lt;/tree&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenTree(Node node, Screen screen) {
        super(node);
        this.screen = screen;
        int height = -1;
        tree = new Tree();
        tree.addTreeListener(screen);
        createTree(node);
        initWidget(tree);
        tree.setStyleName("ScreenTree");
        DOM.setStyleAttribute(tree.getElement(),"overflow","auto");
        setDefaults(node, screen);
    }
    
    /**
     * This method will construct the tree of widgets from an XML
     * @param node
     */
    public void createTree(Node node) {
        NodeList items = node.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
                TreeItem treeItem = createTreeItem(items.item(i), screen);
                tree.addItem(treeItem);
            }
        }

    }

    private TreeItem createTreeItem(Node node, Screen screen) {
        TreeItem item = new TreeItem(Screen.getWidgetMap().getWidget(node, screen));
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

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenTree(node, screen);
    }

    public void load(AbstractField field) {
        // TODO Auto-generated method stub
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
        tree = null;
        super.destroy();
    }

}
