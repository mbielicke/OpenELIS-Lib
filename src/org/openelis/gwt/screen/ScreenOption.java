package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.OptionField;
import org.openelis.gwt.common.data.OptionItem;
import org.openelis.gwt.widget.OptionList;

import java.util.Iterator;
import java.util.List;
/**
 *  ScreenOption wraps the OptionList for display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenOption extends ScreenInputWidget implements FocusListener {
	/**
	 * Default XML Tag Name for xml Definition and WidgetMap
	 */
	public static String TAG_NAME = "optionlist";
	/**
	 * Widget wrapped by this class
	 */
    private OptionList optionlist;
    private boolean multi;
    private int size;
    private boolean enabled;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenOption() {
    };
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;optionlist key="string" onChange="string" mult="boolean" size="int"/&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenOption(Node node, final ScreenBase screen) {
        super(node);
        optionlist = new OptionList() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                    }
                }else if(DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
                    if(!enabled){
                        DOM.eventCancelBubble(event,true);
                        DOM.eventPreventDefault(event);
                        setFocus(false);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        if (node.getAttributes().getNamedItem("onChange") != null){
        	String listener = node.getAttributes().getNamedItem("onChange").getNodeValue();
        	if (listener.equals("this"))
        		optionlist.addChangeListener(screen);
        	else
        		optionlist.addChangeListener((ChangeListener)ClassFactory.forName(listener));
        }
        if (node.getAttributes().getNamedItem("multi") != null) {
        	if(node.getAttributes().getNamedItem("multi").getNodeValue().equals("true")){
                multi = true;
        		optionlist.setMultipleSelect(true);
        		if (node.getAttributes().getNamedItem("size") != null) {
        			size = Integer.parseInt(node.getAttributes()
                                                	.getNamedItem("size")
                                                	.getNodeValue());
        			optionlist.setVisibleItemCount(size);
        		}
            }
        }
        initWidget(optionlist);
        displayWidget = optionlist;
        optionlist.setStyleName("ScreenOption");
        setDefaults(node, screen);
        optionlist.sinkEvents(Event.ONMOUSEDOWN);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenOption(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode){
            queryWidget.load(field);
        }else{
            optionlist.clear();
            OptionField optField = (OptionField)field;
            List optMap = optField.getOptions();
            Iterator optIt = optMap.iterator();
            while (optIt.hasNext()) {
                OptionItem item = (OptionItem)optIt.next();
                optionlist.addItem(item.akey.toString(), item.display);
                if (item.selected || item.akey.equals(optField.getValue())) {
                    optionlist.setItemSelected(optionlist.getItemCount() - 1, true);
                }
            }
        }

    }

    public void submit(AbstractField field) {
        if(queryMode){
            queryWidget.submit(field);
        }else{
            OptionField oField = (OptionField)field;
            if (optionlist.isMultipleSelect()) {
                for (int i = 0; i < optionlist.getItemCount(); i++) {
                    if (optionlist.isItemSelected(i))
                        oField.addValue(optionlist.getValue(i));
                }
            } else {
                if (optionlist.getValue() != null) {
                    field.setValue(optionlist.getValue());
                }
            }
        }
    }
    
    public void enable(boolean enabled){
        this.enabled = enabled;
        optionlist.setEnabled(enabled);
    }

    public void onFocus(Widget sender) {
        if(!enabled)
            optionlist.setFocus(false);
        super.onFocus(sender);
    }

    public void onLostFocus(Widget sender) {
       // TODO Auto-generated method stub
        super.onLostFocus(sender);
    }
    
    public void setFocus(boolean focus){
        if(queryMode)
            queryWidget.setFocus(focus);
        else
            optionlist.setFocus(focus);
    }
    
    public void destroy() {
        optionlist = null;
        super.destroy();
    }
}
