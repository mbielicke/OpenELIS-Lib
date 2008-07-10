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

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenTablePanel wraps a GWT FlexTable to display 
 * widgets on a Screen in table layout.
 * @author tschmidt
 *
 */
public class ScreenTablePanel extends ScreenWidget {
	/**
	 * Reference String for Widget Map
	 */
	public static String TAG_NAME = "TablePanel";
	/**
	 * Widget wrapped by this class
	 */
	private FlexTable panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTablePanel() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;panel layout="table" id="string"&gt;
     *   &lt;row&gt;
     *     &lt;widget/&gt;
     *   &lt;/row&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenTablePanel(Node node, ScreenBase screen) {
        super(node);
        panel = new FlexTable();
        panel.setStyleName("ScreenTablePanel");
        initWidget(panel);
        
        if (node.getAttributes().getNamedItem("visible") != null && node.getAttributes().getNamedItem("visible").getNodeValue().equals("false")){
            panel.setVisible(false);
        }
        if (node.getAttributes().getNamedItem("spacing") != null)
            panel.setCellSpacing(Integer.parseInt(node.getAttributes()
                                                      .getNamedItem("spacing")
                                                      .getNodeValue()));
        if (node.getAttributes().getNamedItem("padding") != null)
            panel.setCellSpacing(Integer.parseInt(node.getAttributes()
                                                      .getNamedItem("padding")
                                                      .getNodeValue()));
        NodeList rows = ((Element)node).getElementsByTagName("row");
        for (int k = 0; k < rows.getLength(); k++) {
        	
            NodeList widgets = rows.item(k).getChildNodes();
            int w = -1;
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    w++;
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    panel.setWidget(k, w, wid);
                    if (widgets.item(l).getAttributes().getNamedItem("colspan") != null)
                        panel.getFlexCellFormatter()
                             .setColSpan(k,
                                         w,
                                         Integer.parseInt(widgets.item(l)
                                                                 .getAttributes()
                                                                 .getNamedItem("colspan")
                                                                 .getNodeValue()));
                    if (widgets.item(l).getAttributes().getNamedItem("rowspan") != null)
                        panel.getFlexCellFormatter()
                             .setRowSpan(k,
                                         w,
                                         Integer.parseInt(widgets.item(l)
                                                                 .getAttributes()
                                                                 .getNamedItem("rowspan")
                                                                 .getNodeValue()));
                    if (widgets.item(l).getAttributes().getNamedItem("style") != null)
                        panel.getFlexCellFormatter()
                             .addStyleName(k,
                                           w,
                                           widgets.item(l)
                                                  .getAttributes()
                                                  .getNamedItem("style")
                                                  .getNodeValue());
                    if (widgets.item(l).getAttributes().getNamedItem("align") != null) {
                        String align = widgets.item(l)
                                              .getAttributes()
                                              .getNamedItem("align")
                                              .getNodeValue();
                        if (align.equals("right"))
                            panel.getFlexCellFormatter()
                                 .setHorizontalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_RIGHT);
                        if (align.equals("left"))
                            panel.getFlexCellFormatter()
                                 .setHorizontalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_LEFT);
                        if (align.equals("center"))
                            panel.getFlexCellFormatter()
                                 .setHorizontalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_CENTER);
                    }
                    if (widgets.item(l).getAttributes().getNamedItem("valign") != null) {
                        String align = widgets.item(l)
                                              .getAttributes()
                                              .getNamedItem("valign")
                                              .getNodeValue();
                        if (align.equals("top"))
                            panel.getFlexCellFormatter()
                                 .setVerticalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_TOP);
                        if (align.equals("bottom"))
                            panel.getFlexCellFormatter()
                                 .setVerticalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_BOTTOM);
                        if (align.equals("middle"))
                            panel.getFlexCellFormatter()
                                 .setVerticalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_MIDDLE);
                    }
                }
            }
            
            if (rows.item(k).getAttributes().getNamedItem("style") != null) {
            	panel.getRowFormatter().addStyleName(k, rows.item(k).getAttributes().getNamedItem("style").getNodeValue());
            }
        }
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTablePanel(node, screen);
    }
    
    public void destroy() {
        panel = null;
        super.destroy();
    }
    
    public void onMouseEnter(int rowNumber) {
    	panel.getRowFormatter().removeStyleName(rowNumber, "topMenuPanel");
    	panel.getRowFormatter().addStyleName(rowNumber, "topMenuPanelHover");
    	
    	//
    	
    }
    
    public void onMouseLeave(int rowNumber) {
    	panel.getRowFormatter().removeStyleName(rowNumber, "topMenuPanelHover");
    	panel.getRowFormatter().addStyleName(rowNumber, "topMenuPanel");   
    }

}
