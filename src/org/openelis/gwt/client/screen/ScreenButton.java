package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.xml.client.Node;

/**
 * ScreenButton wraps either a Button or ToggleButton from the GWT library.
 * 
 * @author tschmidt
 * 
 */
public class ScreenButton extends ScreenWidget implements SourcesClickEvents{
	
	private DelegatingClickListenerCollection clickListeners;

	/**
	 * Default XML Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "button";
	/**
	 * Widget wrapped by this class
	 */
	private Button button;
	
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
	public ScreenButton() {
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
	public ScreenButton(Node node, final ScreenBase screen) {
		super(node);
		button = new Button() {
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
		button.setStyleName("ScreenButton");
		addClickListener(screen);
        if(node.getAttributes().getNamedItem("text") != null) {
            button.setText(node.getAttributes().getNamedItem("text").getNodeValue());
        }
		if (node.getAttributes().getNamedItem("html") != null) {
				button.setHTML(node.getAttributes().getNamedItem("html")
						.getNodeValue());
		}
		initWidget(button);
		setDefaults(node, screen);
	}

	public ScreenWidget getInstance(Node node, ScreenBase screen) {
		// TODO Auto-generated method stub
		return new ScreenButton(node, screen);
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
