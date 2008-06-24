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
