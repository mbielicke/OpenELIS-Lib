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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.AbstractField;

/**
 * ScreenVertical wraps a GWT VerticalPanel to display widgets on a 
 * Screen in a vertical Column.
 * @author tschmidt
 *
 */

public class ScreenVertical extends ScreenWidget implements ScreenPanel {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "VerticalPanel";
	/**
	 * Widget wrapped by this class
	 */
    private VerticalPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenVertical() {
        panel = new VerticalPanel();
        initWidget(panel);
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;panel layout="vertical" key="string" spacing="int"*gt;
     *   &lt;widget&gt;
     *     ...
     *   &lt;/widget&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */
    
    public ScreenVertical(ScreenBase screen, String key) {
    	panel = new VerticalPanel();
    	initWidget(panel);
    	screen.widgets.put(key, this);
    	
    }
    public ScreenVertical(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            panel = (VerticalPanel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            panel = new VerticalPanel();
        if (node.getAttributes().getNamedItem("spacing") != null){
            panel.setSpacing(Integer.parseInt(node.getAttributes()
                                                  .getNamedItem("spacing")
                                                  .getNodeValue()));
        }
        
        NodeList widgets = node.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                addWidget(widgets.item(k), wid);
            }
        }
        if(node.getAttributes().getNamedItem("sizeToWindow") != null){
            Window.addWindowResizeListener(new WindowResizeListener() {

                public void onWindowResized(int width, int height) {
                    setBrowserHeight();
                }
                
            });
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    setBrowserHeight();
                    //index = getIndex(browser.getElement());
                }
            });
        }
        if(node.getAttributes().getNamedItem("overflow") != null ||
           node.getAttributes().getNamedItem("overflowX") != null || 
           node.getAttributes().getNamedItem("overflowY") != null){
        	AbsolutePanel ap = new AbsolutePanel();
        	ap.add(panel);
        	initWidget(ap);
            if(node.getAttributes().getNamedItem("overflow") != null)
                DOM.setStyleAttribute(ap.getElement(),"overflow",node.getAttributes().getNamedItem("overflow").getNodeValue());
            if(node.getAttributes().getNamedItem("overflowX") != null){
//            	if we want to seperate out the overflows we have to remove the overflow tag
            	DOM.setStyleAttribute(ap.getElement(),"overflow","");
                DOM.setStyleAttribute(ap.getElement(),"overflowX",node.getAttributes().getNamedItem("overflowX").getNodeValue());
            }
            if(node.getAttributes().getNamedItem("overflowY") != null){
            	//if we want to seperate out the overflows we have to remove the overflow tag
            	DOM.setStyleAttribute(ap.getElement(),"overflow","");
                DOM.setStyleAttribute(ap.getElement(),"overflowY",node.getAttributes().getNamedItem("overflowY").getNodeValue());
            }
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
        /*if (widget.getAttributes().getNamedItem("width") != null) {
            panel.setCellWidth(wid, widget.getAttributes().getNamedItem("width").getNodeValue());
        }
        if (widget.getAttributes().getNamedItem("height") != null) {
            panel.setCellWidth(wid, widget.getAttributes().getNamedItem("height").getNodeValue());
        }
        */
    }
    
    public void add(Widget w) {
        if(panel == null)
            super.add(w);
        else
            panel.add(w);
    }
    
    /**
     * createPanel creates the vertical panel from an XML node 
     * @param node
     */
    public void createPanel(Node node){
        NodeList widgets = node.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                addWidget(widgets.item(k), wid);
            }
        }
    }

    /**
     * This method loads the Widget from a String
     * @param xml
     */
    public void load(AbstractField xml) {
        load((String)xml.getValue());
    }
    
    public void load(String xml){
        clear();
        if(xml == null || xml.equals(""))
            return;
        Document doc = XMLParser.parse(xml);
        createPanel(doc.getDocumentElement());
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenVertical(node, screen);
    }
    
    public void clear(){
    	panel.clear();
    }
    
    public void destroy() {
        panel = null;
    }
    
    public VerticalPanel getPanel(){
    	return panel;
    }
    
    public void setBrowserHeight() {
        if (panel.isVisible()) {
            panel.setHeight((Window.getClientHeight() - panel
                                                                 .getAbsoluteTop()) + "px");
            panel.setWidth((Window.getClientWidth() - panel
                                                               .getAbsoluteLeft()) + "px");
            
        }
    }
    public void submit() {
        // TODO Auto-generated method stub
        
    }

}
