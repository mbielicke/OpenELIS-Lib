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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * ScreenAbsolute is used to layout widgets on the Screen using absolute X and Y coordinates
 * it Wraps GWT's AbsolutePanel.
 * @author tschmidt
 *
 */
public class ScreenAbsolute extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "AbsolutePanel";
	/** 
	 * Widget wrapped by this class
	 */
	private AbsolutePanel panel;
	
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenAbsolute() {
    }

    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="absolute" key="string"&gt;
     *   &lt;widget x="int" y="int"&gt;
     *   ...
     *   &lt;/widget&gt;
     * &lt;/panel&gt;
     *
     * @param node
     * @param screen
     */
    public ScreenAbsolute(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen){
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            panel = (AbsolutePanel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            panel = new AbsolutePanel();
        panel.setStyleName("ScreenAbsolute");
        if(node.getAttributes().getNamedItem("overflow") != null)
            DOM.setStyleAttribute(panel.getElement(),"overflow",node.getAttributes().getNamedItem("overflow").getNodeValue());
        initWidget(panel);
        NodeList widgets = node.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                int x = -1;
                if (widgets.item(k).getAttributes().getNamedItem("x") != null)
                    x = Integer.parseInt(widgets.item(k)
                                                .getAttributes()
                                                .getNamedItem("x")
                                                .getNodeValue());
                int y = -1;
                if (widgets.item(k).getAttributes().getNamedItem("y") != null)
                    y = Integer.parseInt(widgets.item(k)
                                                .getAttributes()
                                                .getNamedItem("y")
                                                .getNodeValue());
                if(node.getAttributes().getNamedItem("align") != null)
                    DOM.setElementProperty(panel.getElement(),"align",node.getAttributes().getNamedItem("align").getNodeValue());
                panel.add(wid, x, y);
            }
        }
        setDefaults(node, screen);
    }
    
    /**
     * Method called to return a specific instance of this widget defined in the 
     * XML node.
     */
    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenAbsolute(node, screen);
    }
    
    public void destroy() {
        panel = null;
        super.destroy();
    }

}
