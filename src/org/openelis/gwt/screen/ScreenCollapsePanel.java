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

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.widget.CollapsePanel;

public class ScreenCollapsePanel extends ScreenWidget {
    
   protected CollapsePanel panel;
   public static final String TAG_NAME = "CollapsePanel";
   
   public ScreenCollapsePanel() {
       
   }
   
   public ScreenCollapsePanel(Node node, ScreenBase screen){
       super(node);
       panel = new CollapsePanel();
       if (node.getChildNodes().getLength() > 0){
           NodeList widgets = node.getChildNodes();
           for (int k = 0; k < widgets.getLength(); k++) {
               if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                   Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                   ((ScreenWidget)wid).setHeight("100%");
                   ((ScreenWidget)wid).setWidth("auto");
                   panel.setContent(wid);
                   if(wid instanceof ChangeListener){
                       panel.addChangeListener((ChangeListener)wid);
                   }
               }
           }
       }
       
       if(node.getAttributes().getNamedItem("height") != null)
           setHeight(node.getAttributes().getNamedItem("height").getNodeValue());
       else
           setHeight("100%");
       
       initWidget(panel);        
       setDefaults(node, screen);
       setWidth("auto");;
   }
   
   public ScreenWidget getInstance(Node node, ScreenBase scree) {
       return new ScreenCollapsePanel(node,screen);
   }

}
