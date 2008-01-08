package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.client.widget.AppButton;

public class ScreenAppButton extends ScreenWidget implements SourcesClickEvents{
    
    private DelegatingClickListenerCollection clickListeners;

    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
    public static String TAG_NAME = "appButton";
    /**
     * Widget wrapped by this class
     */
    private AppButton button;
    
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenAppButton() {
    }

    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget<br/>
     * <br/>
     * &lt;button key="string" html="escaped HTML" text="string" constant="boolean"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenAppButton(Node node, final ScreenBase screen) {
        super(node);
        button = new AppButton();
        button.action = node.getAttributes().getNamedItem("action").getNodeValue();
        if(node.getAttributes().getNamedItem("toggle") != null){
            if(node.getAttributes().getNamedItem("toggle").getNodeValue().equals("true"))
                button.toggle = true;
        }
        NodeList widgets = node.getChildNodes();
        for (int l = 0; l < widgets.getLength(); l++) {
            if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {                       
                Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                button.setWidget(wid);
            }
        }
        initWidget(button);
        setDefaults(node, screen);
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen){
        return new ScreenAppButton(node,screen);
    }

    public void addClickListener(ClickListener listener) {
        if(clickListeners == null)
            clickListeners = new DelegatingClickListenerCollection(this,button);
        clickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null)
            clickListeners.remove(listener);
    }
    
    public void destroy() {
        clickListeners = null;
        button = null;
        super.destroy();
    }

}
