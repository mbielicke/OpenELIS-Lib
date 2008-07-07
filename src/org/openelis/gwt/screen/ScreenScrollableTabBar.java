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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.widget.ScrollableTabBar;

public class ScreenScrollableTabBar extends ScreenWidget{
    public static String TAG_NAME = "scrolltabBar";
    
    private ScrollableTabBar scrollableTabBar;
    
    public ScreenScrollableTabBar(){
        
    }
    
    public ScreenScrollableTabBar(Node node, ScreenBase screen) {
        super(node);
        scrollableTabBar = new ScrollableTabBar();
        scrollableTabBar.setStyleName("ScreenTab");        
        initWidget(scrollableTabBar);        
        NodeList tabs = ((Element)node).getElementsByTagName("tab");           
        if(tabs.getLength()>0){
         for (int k = 0; k < tabs.getLength(); k++) {                       
            scrollableTabBar.addTab(tabs.item(k).getAttributes()
                                    .getNamedItem("text")
                                    .getNodeValue());             
         }
         scrollableTabBar.selectTab(0);
        }        
        
        scrollableTabBar.addTabListener(screen);
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
}
