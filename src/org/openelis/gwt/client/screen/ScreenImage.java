package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.xml.client.Node;
/**
 * ScreenImage wraps a GWT Image widget to be displayed on a Screen.
 * @author tschmidt
 *
 */
public class ScreenImage extends ScreenWidget implements SourcesClickEvents {
	/**
	 * Widget wrapped by this class
	 */
    private Image image;
    private DelegatingClickListenerCollection clickListeners;
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "image";
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenImage() {
        
    }
    
    public ScreenImage(String url) {
        image = new Image(url);
        initWidget(image);
        addMouseListener((MouseListener)Screen.getWidgetMap().get("HoverListener"));
        sinkEvents(Event.MOUSEEVENTS);
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;image key="string" url="string" onclick="string"/&gt;
     *  
     * @param node
     * @param screen
     */
    public ScreenImage(Node node, final Screen screen) {
        super(node);
        image = new Image() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        image.setUrl(node.getAttributes().getNamedItem("url").getNodeValue());
        if (node.getAttributes().getNamedItem("onclick") != null) {
        	String listener = node.getAttributes().getNamedItem("onclick").getNodeValue();
        	if(listener.equals("this"))
        		addClickListener(screen);
        	else
        		addClickListener((ClickListener)Screen.getWidgetMap().get(listener));
        }
        initWidget(image);
        image.setStyleName("ScreenImage");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        return new ScreenImage(node, screen);
    }

    public void addClickListener(ClickListener listener) {
        if(clickListeners == null){
            clickListeners = new DelegatingClickListenerCollection(this,image);
        }
        clickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null)
            clickListeners.remove(listener);
    }
    
    public void destroy(){
        image = null;
        clickListeners = null;
        super.destroy();
    }
}
