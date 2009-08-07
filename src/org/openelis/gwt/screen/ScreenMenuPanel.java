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

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenMenuItem;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.MenuPanel;

/**
 * ScreenMenuPanel will create either a vertical or horizontal list of widgets
 * from an XML string and is Styled as a ScreenMenuList.
 * @author tschmidt
 *
 */
@Deprecated
public class ScreenMenuPanel extends ScreenWidget {
	/**
	 * Default XML Tag Name in XML Definition
	 */
	public static String TAG_NAME = "menuPanel";
	/**
	 * Widget wrapped by this class
	 */
    public MenuPanel panel;

	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenMenuPanel() {
        
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;menu vetical="boolean" key="string"&gt;
     *    &lt;widget/&gt;
     *    ......
     * &lt;menu/&gt;
     *   
     * @param node
     * @param screen
     */	
    public ScreenMenuPanel(Node node, ScreenBase screen){
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        this.screen = screen;
        String layout = node.getAttributes().getNamedItem("layout").getNodeValue();
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            panel = (MenuPanel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            panel = new MenuPanel();
        panel.init(layout);
        createPanel(node);
        initWidget(panel);
        setDefaults(node, screen);
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen){
        return new ScreenMenuPanel(node,screen);
    }
    
    /**
     * createPanel creates the MenuPanel from an XML node 
     * @param node
     */
    public void createPanel(Node node){
        NodeList items = node.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(items.item(i), screen);
              //  if(wid instanceof ScreenMenuItem){
              //      ((MenuItem)((ScreenMenuItem)wid).getWidget()).parent = panel;
              //      panel.menuItems.add(((MenuItem)((ScreenMenuItem)wid).getWidget()));
              //  }
                if(wid instanceof ScreenMenuItem)
                    panel.add(((ScreenMenuItem)wid).getWidget());
                else
                    panel.add(wid);
            }
        }
    }
    
    /**
     * This method loads the Widget from an AbstractField from the FormRPC
     */
    public void load(AbstractField field) {
        Document doc = XMLParser.parse((String)field.getValue());
        panel.clear();
        createPanel(doc.getDocumentElement());
    }

    /**
     * This method loads the Widget from a String
     * @param xml
     */
    public void load(String xml) {
        Document doc = XMLParser.parse(xml);
        panel.clear();
        createPanel(doc.getDocumentElement());
    }
    
    public void destroy() {
        panel = null;
        super.destroy();
    }
    


}
