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
       init(node,screen);
   }
   
   public void init(Node node, ScreenBase screen) {
       if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
           panel = (CollapsePanel)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
       else
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
       //else
           //setHeight("100%");
       
       initWidget(panel);        
       setDefaults(node, screen);
      // setWidth("auto");;
   }
   
   public ScreenWidget getInstance(Node node, ScreenBase scree) {
       return new ScreenCollapsePanel(node,screen);
   }

}
