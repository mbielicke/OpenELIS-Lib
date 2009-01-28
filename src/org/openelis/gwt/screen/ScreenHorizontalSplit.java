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
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * ScreenSplit wraps a GWT HorizontalSplitPanel for display on a Screen.    
 * @author tschmidt
 *
 */
public class ScreenHorizontalSplit extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "HorizontalSplitPanel";
	/**
	 * Widget wrapped by this class
	 */
    private HorizontalSplitPanel hp;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenHorizontalSplit() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;panel layout="hsplit" splitpos="int" sizeToWindow="boolean"&gt;
     *   &lt;section&gt;
     *     WIDGETS FOR LEFT PANEL
     *   &lt;/section&gt;
     *   &lt;section&gt;
     *     WIDGETS FOR RIGHT PANEL
     *   &lt;/section&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenHorizontalSplit(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            hp = (HorizontalSplitPanel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            hp = new HorizontalSplitPanel();
        
        hp.setStyleName("ScreenSplit");
        initWidget(hp);
        NodeList sections = ((Element)node).getElementsByTagName("section");
        for (int k = 0; k < sections.getLength(); k++) {
            NodeList widgets = sections.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    if (k == 0) {
                            hp.setLeftWidget(wid);
                    }
                    if (k == 1) {
                            hp.setRightWidget(wid);
                    }
                }
            }
        }
        if (node.getAttributes().getNamedItem("splitpos") != null) {
            String splitpos = node.getAttributes()
                                  .getNamedItem("splitpos")
                                  .getNodeValue();
                hp.setSplitPosition(splitpos);
        }
        if (node.getAttributes().getNamedItem("sizeToWindow") != null){
        	if(node.getAttributes().getNamedItem("sizeToWindow").getNodeValue().equals("true")){
                Window.addWindowResizeListener(new WindowResizeListener() {

                    public void onWindowResized(int width, int height) {
                            setBrowserHeight(hp);
                    }

                });
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                            setBrowserHeight(hp);
                    }
                });
        	}
        }
                
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenHorizontalSplit(node, screen);
    }
 
    private void setBrowserHeight(Widget wid) {
        if (wid.isVisible()) {
            wid.setHeight((Window.getClientHeight() - wid.getAbsoluteTop() - 10) + "px");
            wid.setWidth((Window.getClientWidth() - wid.getAbsoluteLeft() - 10) + "px");
        }
    }
    
    public void destroy(){
        hp = null;
        super.destroy();
    }
}
