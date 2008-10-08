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

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;

import java.util.ArrayList;
/**
 * ScreenTab wraps a GWT TabPanel for displaying widgets 
 * on a Screen in Tab Layout.
 * @author tschmidt
 *
 */
public class ScreenTab extends ScreenWidget implements ScreenPanel, TabListener {
    
    private ArrayList<String> tabList = new ArrayList<String>();
    private AbstractField field;
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "TabPanel";
	/**
	 * Widget wrapped by this class
	 */
	private TabPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTab() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="tab" key="string"&gt;
     *   &lt;tab text="string"&gt;
     *    ....
     *   &lt;tab&gt;
     *   &lt;tab text="string"&gt;
     *    ....
     *   &lt;/tab&gt;
     * &lt;/panel&gt;
     *     
     * @param node
     * @param screen
     */	
    public ScreenTab(Node node, ScreenBase screen) {
        super(node);
        panel = new TabPanel();
        panel.setStyleName("ScreenTab");
        initWidget(panel);
        NodeList tabs = ((Element)node).getElementsByTagName("tab");
        for (int k = 0; k < tabs.getLength(); k++) {
            NodeList widgets = tabs.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    ScrollPanel scroll = new ScrollPanel();
                    scroll.add(wid);
                    scroll.setWidth("100%");
                    //tabs can not have a constant or hard coded text
                    panel.add(scroll, tabs.item(k).getAttributes()
                                             .getNamedItem("text")
                                             .getNodeValue());
                }
            }
            if(tabs.item(k).getAttributes().getNamedItem("key") != null)
                tabList.add(tabs.item(k).getAttributes().getNamedItem("key").getNodeValue());
            else
                tabList.add(null);
        }
        panel.selectTab(0);
        panel.addTabListener((TabListener)screen);
        panel.addTabListener(this);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTab(node, screen);
    }
    
    public void destroy(){
        panel = null;
        super.destroy();
    }
    public Panel getPanel() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void submit(AbstractField field) {
        if(field != null){
            field.setValue(tabList.get(panel.getTabBar().getSelectedTab()));
        }
    }
    
    @Override
    public void load(AbstractField field) {
        // TODO Auto-generated method stub
        this.field = field;
        if(field.getValue() != null && !field.getValue().equals("")){
            panel.selectTab(tabList.indexOf(field.getValue()));
        }
    }
    public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
        // TODO Auto-generated method stub
        return true;
    }
    public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
        if(field != null)
            field.setValue(tabList.get(panel.getTabBar().getSelectedTab()));
    }

}
