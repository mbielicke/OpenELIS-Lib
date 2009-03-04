/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
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
