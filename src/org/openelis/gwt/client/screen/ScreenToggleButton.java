package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.xml.client.Node;

/** 
 * ScreenToggleButton wraps a GWT ToggleButtton to be displayed on a Screen 
 * @author tschmidt
 *
 */
public class ScreenToggleButton extends ScreenWidget {
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "toggle";
	/**
	 * Widget wrapped by this class
	 */
	private ToggleButton button;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
	public ScreenToggleButton() {
	}

    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;toggle key="string" html="escaped HTML" text="string" constant = "boolean"/&gt;
     * @param node
     * @param screen
     */
	public ScreenToggleButton(Node node, final Screen screen) {
		super(node);
		button = new ToggleButton() {
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
		button.setStyleName("ScreenToggleButton");
		button.addClickListener(screen);
		boolean cons = false;
		if (node.getAttributes().getNamedItem("constant") != null)
			cons = true;
		if (node.getAttributes().getNamedItem("text") != null) {
			if (cons)
				button.setText(screen.constants.getString(node.getAttributes()
						.getNamedItem("text").getNodeValue()));
			else
				button.setText(node.getAttributes().getNamedItem("text")
						.getNodeValue());
		}
		if (node.getAttributes().getNamedItem("html") != null)
			button.setHTML(node.getAttributes().getNamedItem("html")
					.getNodeValue());
		initWidget(button);
		setDefaults(node, screen);
	}

	public ScreenWidget getInstance(Node node, Screen screen) {
		return new ScreenToggleButton(node, screen);
	}

	public void setFocus(boolean focus) {

	}
}
