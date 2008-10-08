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
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * ScreenHorizontal wraps a GWT HorizontalPanel for displaying widgets 
 * on a screen in a Horizontal row.
 * @author tschmidt
 *
 */
public class ScreenHorizontal extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "HorizontalPanel";
	/**
	 * Widget wrapped by this class
	 */
    private HorizontalPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenHorizontal() {
    }
    
    public ScreenHorizontal(ScreenBase screen, String key){
    	panel = new HorizontalPanel();
    	initWidget(panel);
    	screen.widgets.put(key, this);
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="horizontal" key="string" spacing="int"&gt;
     *   &lt;widget&gt;
     *     ...
     *   &lt;/widget&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenHorizontal(Node node, ScreenBase screen) {
        super(node);
        panel = new HorizontalPanel();
        if (node.getAttributes().getNamedItem("spacing") != null)
                panel.setSpacing(Integer.parseInt(node.getAttributes()
                                                      .getNamedItem("spacing")
                                                      .getNodeValue()));
        NodeList widgets = node.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                addWidget(widgets.item(k), wid);
            }
        }
        if(node.getAttributes().getNamedItem("overflow") != null){
        	AbsolutePanel ap = new AbsolutePanel();
        	ap.add(panel);
        	initWidget(ap);
        	DOM.setStyleAttribute(ap.getElement(),"overflow",node.getAttributes().getNamedItem("overflow").getNodeValue());
        }else
        	initWidget(panel);
        panel.setStyleName("ScreenPanel");
        setDefaults(node, screen);
    }

    public void addWidget(Node widget, Widget wid) {
        panel.add(wid);
        if (widget.getAttributes().getNamedItem("halign") != null) {
            String align = widget.getAttributes()
                                 .getNamedItem("halign")
                                 .getNodeValue();
            if (align.equals("right"))
                panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_RIGHT);
            if (align.equals("left"))
                panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_LEFT);
            if (align.equals("center"))
                panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_CENTER);
        }
        if (widget.getAttributes().getNamedItem("valign") != null) {
            String align = widget.getAttributes()
                                 .getNamedItem("valign")
                                 .getNodeValue();
            if (align.equals("top"))
                panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_TOP);
            if (align.equals("middle"))
                panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_MIDDLE);
            if (align.equals("bottom"))
                panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_BOTTOM);
        }
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenHorizontal(node, screen);
    }
    
    public void destroy(){
        panel = null;
        super.destroy();
    }
   
}
