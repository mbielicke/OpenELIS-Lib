package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.CheckBox;
/**
 * ScreenCheck wraps a GWT CheckBox to be displayed on a Screen.
 * @author tschmidt
 *
 */
public class ScreenCheck extends ScreenInputWidget implements SourcesClickEvents{
	private DelegatingClickListenerCollection clickListeners;
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "check";
	/**
	 * Widget wrapped by this class
	 */
    private CheckBox check;
    private int defaultType = CheckBox.TWO_STATE;
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
    public ScreenCheck(Node node, final ScreenBase screen) {
        super(node);
        check = new CheckBox(){
                public void onBrowserEvent(Event event){
                    if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                        if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                            screen.doTab(event, this);
                        }
                    } else {
                        super.onBrowserEvent(event);
                    }
                }
        };

        if(node.getAttributes().getNamedItem("threeState") != null){
            check.setType(CheckBox.THREE_STATE);
            defaultType = CheckBox.THREE_STATE;
        }
        if (node.getFirstChild() != null){
        	 check.setText(node.getFirstChild().getNodeValue());
        }
        if (node.getChildNodes().getLength() > 0){
            NodeList widgets = node.getChildNodes();
            for (int k = 0; k < widgets.getLength(); k++) {
                if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                    check.setWidget(wid);
                }
            }
        }
        if (node.getAttributes().getNamedItem("onClick") != null){
        	String listener = node.getAttributes().getNamedItem("onClick").getNodeValue();
        	if(listener.equals("this"))
        		addClickListener((ClickListener)screen);
        	else
        		addClickListener((ClickListener)ClassFactory.forName(listener));
        }
        initWidget(check);
        displayWidget = check;
        check.setStyleName("ScreenCheck");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenCheck(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else
            check.setState((String)field.getValue());
    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else
            field.setValue(check.getState());
    }
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(true);
        else{
            check.enable(enabled);
            if(enabled)
                check.addFocusListener(this);
            else
                check.removeFocusListener(this);
        }
    }
    
    public void setFocus(boolean focus){
        if(queryMode)
            queryWidget.setFocus(focus);
        else
            check.setFocus(focus);
            
    }
	public void addClickListener(ClickListener listener) {
		if(clickListeners == null){
			clickListeners = new DelegatingClickListenerCollection(this,check.panel);
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
    
    public void setForm(boolean mode) {
        if(queryWidget == null){
            if(mode){
                check.setType(CheckBox.THREE_STATE);
                check.setState(CheckBox.UNKNOWN);
            }else
                check.setType(defaultType);
        }else
            super.setForm(mode);
    }

}
