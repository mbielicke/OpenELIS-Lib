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
package org.openelis.gwt.screen.deprecated;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.deprecated.AbstractField;
//import org.openelis.gwt.common.data.TabField;
//import org.openelis.gwt.common.data.TabRPC;
import org.openelis.gwt.widget.ScrollableTabBar;

@Deprecated
public class ScreenScrollableTabBar extends ScreenInputWidget{
    public static String TAG_NAME = "ScrollTabBar";
    //public TabRPC tabRPC;
    private ScrollableTabBar scrollableTabBar;
    
    public ScreenScrollableTabBar(){
        
    }
    
    public ScreenScrollableTabBar(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            scrollableTabBar = (ScrollableTabBar)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            scrollableTabBar = new ScrollableTabBar();
        scrollableTabBar.setStyleName("ScreenTab");        
        initWidget(scrollableTabBar);        
        String width = "";
        if (node.getAttributes().getNamedItem("width") != null){
            width = node.getAttributes()
                                  .getNamedItem("width")
                                  .getNodeValue();
            scrollableTabBar.setWidth(width);
        }
        NodeList tabs = ((Element)node).getElementsByTagName("tab");           
        if(tabs.getLength()>0){
         for (int k = 0; k < tabs.getLength(); k++) {                       
            scrollableTabBar.addTab(tabs.item(k).getAttributes()
                                    .getNamedItem("text")
                                    .getNodeValue());             
         }
         scrollableTabBar.selectTab(0);
        }        
        
       // scrollableTabBar.addTabListener(screen);
        setDefaults(node, screen);
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenScrollableTabBar(node, screen);
    }
    
    public void destroy(){
        scrollableTabBar = null;
        super.destroy();
    }
    
    @Override
    public void load(AbstractField field) {
       // tabRPC = ((TabField)field).getValue();
      //  scrollableTabBar.setTabRPC(tabRPC);
    }
    
    @Override
    public void submit(AbstractField field) {
       // field.setValue(scrollableTabBar.getTabRPC());
    }
    
    
}
