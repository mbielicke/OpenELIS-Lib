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

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenDeck wraps a GWT DeckPanel to layout widgets in a Decks on a Screen.
 * @author tschmidt
 *
 */
@Deprecated
public class ScreenDeck extends ScreenWidget {
	/**
	 * Default XML Tag Name in XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "DeckPanel";
	/**
	 * Widget that is wrapped by this class
	 */
	private DeckPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenDeck() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="deck" key="string"&gt:
     *   &lt;deck&gt;
     *     &lt;widget/&gt;...
     *   &lt;/deck&gt; 
     * &gt;/panel&lt;
     * 
     * @param node
     * @param screen
     */
    public ScreenDeck(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            panel = (DeckPanel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            panel = new DeckPanel();
        panel.setStyleName("ScreenDeck");
        initWidget(panel);
        NodeList decks = ((Element)node).getElementsByTagName("deck");
        for (int k = 0; k < decks.getLength(); k++) {
            NodeList widgets = decks.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    panel.add(wid);
                }
            }
        }
        panel.showWidget(0);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenDeck(node, screen);
    }
    
    public void destroy(){
        panel = null;
        super.destroy();
    }

}
