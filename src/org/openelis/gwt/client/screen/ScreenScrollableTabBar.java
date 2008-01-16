package org.openelis.gwt.client.screen;

import org.openelis.gwt.client.widget.ScrollableTabBar;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

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
