package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.MenuLabel;

/**
 * ScreenMenuLabel wraps a MenuLabel widget for display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenMenuLabel extends ScreenWidget implements SourcesClickEvents{
	/**
	 * Default XML Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "menulabel";
	/**
	 * Widget wrapped by this class
	 */
    private MenuLabel label;
    private DelegatingClickListenerCollection clickListeners;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenMenuLabel() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;menulabel key="string" onClick="string" image="string" text="string" value="string"/&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenMenuLabel(Node node, ScreenBase screen) {
        super(node);
        label = new MenuLabel();
        if (node.getAttributes().getNamedItem("text") != null){
            label.setText(node.getAttributes().getNamedItem("text").getNodeValue());
        }
        if(node.getAttributes().getNamedItem("image") !=  null)
            label.setImage(node.getAttributes().getNamedItem("image").getNodeValue());
        initWidget(label);
        label.setStyleName("ScreenLabel");
        if (node.getAttributes().getNamedItem("value") != null) {
            setUserObject(node.getAttributes().getNamedItem("value").getNodeValue());
        }
        if(node.getAttributes().getNamedItem("onClick") != null){
            String[] listeners = node.getAttributes().getNamedItem("onClick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    addClickListener((ClickListener)screen);
                else
                    addClickListener((ClickListener)ClassFactory.forName(listeners[i]));
            }
        }
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenMenuLabel(node, screen);
    }

    public void addClickListener(ClickListener listener) {
        if (clickListeners == null) {
            clickListeners = new DelegatingClickListenerCollection(this, label.getLabel());
          }
          clickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
       if(clickListeners != null){
           clickListeners.remove(listener);
       }   
    }
    
    public void destroy() {
        clickListeners = null;
        label = null;
        super.destroy();
    }

}
