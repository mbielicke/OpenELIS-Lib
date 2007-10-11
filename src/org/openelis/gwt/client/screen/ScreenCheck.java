package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.CheckField;
/**
 * ScreenCheck wraps a GWT CheckBox to be displayed on a Screen.
 * @author tschmidt
 *
 */
public class ScreenCheck extends ScreenWidget implements SourcesClickEvents{
	private DelegatingClickListenerCollection clickListeners;
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "check";
	/**
	 * Widget wrapped by this class
	 */
    private CheckBox check;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenCheck() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;check key="string" shortcut="char" onClick="string"/&gt;
     *  
     * @param node
     * @param screen
     */
    public ScreenCheck(Node node, final Screen screen) {
        super(node);
        check = new CheckBox() {
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
        if (node.getFirstChild() != null)
            check.setText(node.getFirstChild().getNodeValue());
        if (node.getAttributes().getNamedItem("shortcut") != null)
            check.setAccessKey(node.getAttributes()
                                   .getNamedItem("shortcut")
                                   .getNodeValue()
                                   .charAt(0));
        if (node.getAttributes().getNamedItem("onClick") != null){
        	String listener = node.getAttributes().getNamedItem("onClick").getNodeValue();
        	if(listener.equals("this"))
        		addClickListener(screen);
        	else
        		addClickListener((ClickListener)Screen.getWidgetMap().get(listener));
        }
        initWidget(check);
        check.setStyleName("ScreenCheck");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenCheck(node, screen);
    }

    public void load(AbstractField field) {
        check.setChecked(((CheckField)field).isChecked());
    }

    public void submit(AbstractField field) {
        field.setValue(new Boolean(check.isChecked()));
    }
    
    public void enable(boolean enabled){
        check.setEnabled(enabled);
    }
    
    public void setFocus(boolean focus){
        check.setFocus(focus);
    }
	public void addClickListener(ClickListener listener) {
		if(clickListeners == null){
			clickListeners = new DelegatingClickListenerCollection(this,check);
		}
		clickListeners.add(listener);
	}
	
	public void removeClickListener(ClickListener listener) {
		if(clickListeners != null){
			clickListeners.remove(listener);
		}
	}
    
    public void destroy(){
        clickListeners = null;
        check = null;
        super.destroy();
    }

}
