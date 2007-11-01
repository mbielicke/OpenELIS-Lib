package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.client.widget.MenuCommands;
import org.openelis.gwt.common.AbstractField;

/**
 * ScreenMenuBar wraps a GWT MenuBar to be displayed on a Screen.  
 * @author tschmidt
 *
 */
public class ScreenMenuBar extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "menubar";
	/**
	 * Widget wrapped by this class
	 */
	private MenuBar menuBar;
	private MenuCommands mcs;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
	public ScreenMenuBar() {
		
	}
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;menubar key="string" vertical="boolean" autoOpen="boolean" commands="string"&gt;
     *   &lt;item text="string" cmd="string"/&gt;
     *   &lt;item text="string" vertical="boolean" autoOpen="boolean"&gt;
     *     &lt;item text="string" cmd="string"/&gt;
     *     &lt;item text="string" cmd="string"/&gt;
     *   &lt;/item&gt;
     * &lt;/menubar&gt;
     * 
     * @param node
     * @param screen
     */	
	public ScreenMenuBar(Node node, ScreenBase screen){
		super(node);
		if(node.getAttributes().getNamedItem("vertical") != null){
			if(node.getAttributes().getNamedItem("vertical").getNodeValue().equals("true"))
				menuBar = new MenuBar(true);
			else 
				menuBar = new MenuBar();
		}
		if(node.getAttributes().getNamedItem("autoOpen") != null){
			if(node.getAttributes().getNamedItem("autoOpen").getNodeValue().equals("true"))
				menuBar.setAutoOpen(true);
		}
		mcs = (MenuCommands)Screen.getWidgetMap().get(node.getAttributes().getNamedItem("commands").getNodeValue());
		createMenu(node,menuBar);
		initWidget(menuBar);
		setDefaults(node, screen);
	}
	
	public ScreenWidget getInstance(Node node, ScreenBase screen){
		return new ScreenMenuBar(node,screen);
	}
	
    public void load(AbstractField field) {
        // TODO Auto-generated method stub
        Document doc = XMLParser.parse((String)field.getValue());
        createMenu(doc.getDocumentElement(),menuBar);
    }

    public void load(String xml) {
        Document doc = XMLParser.parse(xml);
        createMenu(doc.getDocumentElement(),menuBar);
    }
    
    private void createMenu(Node node, MenuBar mb){
        NodeList items = node.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
            	if(items.item(i).hasChildNodes()){
            		MenuBar subMenu = null;
            		if(items.item(i).getAttributes().getNamedItem("vertical") != null){
            			if(items.item(i).getAttributes().getNamedItem("vertical").getNodeValue().equals("true"))
            				subMenu = new MenuBar(true);
            			else
            				subMenu = new MenuBar();
            		}
            		if(items.item(i).getAttributes().getNamedItem("autoOpen") != null){
            			if(items.item(i).getAttributes().getNamedItem("autoOpen").getNodeValue().equals("true"))
            				subMenu.setAutoOpen(true);
            		}

            		createMenu(items.item(i),subMenu);
            		mb.addItem(items.item(i).getAttributes().getNamedItem("text").getNodeValue(),subMenu);
            	}else{
            		MenuItem mi = new MenuItem(items.item(i).getAttributes().getNamedItem("text").getNodeValue(),
            				mcs.getCommand(items.item(i).getAttributes().getNamedItem("cmd").getNodeValue()));
            		mb.addItem(mi);
            	}
            }
        }
    }
    
    public void destroy() {
        menuBar = null;
        mcs = null;
        super.destroy();
    }
	

}
