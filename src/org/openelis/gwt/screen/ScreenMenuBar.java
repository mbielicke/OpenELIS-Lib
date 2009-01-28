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
package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.MenuCommands;

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
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            menuBar = (MenuBar)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else {
            if(node.getAttributes().getNamedItem("vertical") != null){
                if(node.getAttributes().getNamedItem("vertical").getNodeValue().equals("true"))
                    menuBar = new MenuBar(true);
                else 
                    menuBar = new MenuBar();
            }
        }
		if(node.getAttributes().getNamedItem("autoOpen") != null){
			if(node.getAttributes().getNamedItem("autoOpen").getNodeValue().equals("true"))
				menuBar.setAutoOpen(true);
		}
		mcs = (MenuCommands)ClassFactory.forName(node.getAttributes().getNamedItem("commands").getNodeValue());
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
