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

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenDock wraps a GWT DockPanel to display widgets on the Screen
 * with Dock Layout.
 * @author tschmidt
 *
 */
@Deprecated
public class ScreenDock extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "DockPanel";
	/**
	 * Widget that is wrapped by this class
	 */
	private DockPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenDock() {

    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * <&lt;panel layout="dock" spacing="int"&gt;>
     *   &lt;widget dir="north,south,east,west,center"&gt;
     *      ....
     *   &lt;/widget&gt;
     * &lt;/panel&gt;
     *      
     * @param node
     * @param screen
     */
    public ScreenDock(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            panel = (DockPanel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            panel = new DockPanel();
        panel.addStyleName("ScreenDock");
        initWidget(panel);
        if (node.getAttributes().getNamedItem("spacing") != null)
            panel.setSpacing(Integer.parseInt(node.getAttributes()
                                                  .getNamedItem("spacing")
                                                  .getNodeValue()));
        NodeList widgets = node.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k),screen);
                String dir = widgets.item(k)
                                    .getAttributes()
                                    .getNamedItem("dir")
                                    .getNodeValue();
                if (dir.equals("north"))
                    panel.add(wid, DockPanel.NORTH);
                else if (dir.equals("south"))
                    panel.add(wid, DockPanel.SOUTH);
                else if (dir.equals("east"))
                    panel.add(wid, DockPanel.EAST);
                else if (dir.equals("west"))
                    panel.add(wid, DockPanel.WEST);
                else if (dir.equals("center"))
                    panel.add(wid, DockPanel.CENTER);
                if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
                    String align = widgets.item(k)
                                          .getAttributes()
                                          .getNamedItem("align")
                                          .getNodeValue();
                    if (align.equals("right"))
                        panel.setCellHorizontalAlignment(wid,
                                                         HasAlignment.ALIGN_RIGHT);
                    if (align.equals("left"))
                        panel.setCellHorizontalAlignment(wid,
                                                         HasAlignment.ALIGN_LEFT);
                    if (align.equals("center"))
                        panel.setCellHorizontalAlignment(wid,
                                                         HasAlignment.ALIGN_CENTER);
                }
                if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
                    String align = widgets.item(k)
                                          .getAttributes()
                                          .getNamedItem("valign")
                                          .getNodeValue();
                    if (align.equals("top"))
                        panel.setCellVerticalAlignment(wid,
                                                       HasAlignment.ALIGN_TOP);
                    if (align.equals("middle"))
                        panel.setCellVerticalAlignment(wid,
                                                       HasAlignment.ALIGN_MIDDLE);
                    if (align.equals("bottom"))
                        panel.setCellVerticalAlignment(wid,
                                                       HasAlignment.ALIGN_BOTTOM);
                }
               
            }
        }
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenDock(node, screen);
    }
    
    public void destroy() {
        panel = null;
        super.destroy();
    }

}
