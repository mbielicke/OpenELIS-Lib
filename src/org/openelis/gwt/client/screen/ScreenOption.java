package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.OptionList;
import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.OptionField;
import org.openelis.gwt.common.OptionItem;

import java.util.Iterator;
import java.util.List;
/**
 * ScreenOption wraps the OptionList for display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenOption extends ScreenWidget implements FocusListener {
	/**
	 * Default XML Tag Name for xml Definition and WidgetMap
	 */
	public static String TAG_NAME = "optionlist";
	/**
	 * Widget wrapped by this class
	 */
    private OptionList optionlist;
    
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
    public ScreenOption(Node node, final Screen screen) {
        super(node);
        optionlist = new OptionList() {
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
        optionlist.addFocusListener(this);
        if (node.getAttributes().getNamedItem("onChange") != null){
        	String listener = node.getAttributes().getNamedItem("onChange").getNodeValue();
        	if (listener.equals("this"))
        		optionlist.addChangeListener(screen);
        	else
        		optionlist.addChangeListener((ChangeListener)Screen.getWidgetMap().get(listener));
        }
        if (node.getAttributes().getNamedItem("multi") != null) {
        	if(node.getAttributes().getNamedItem("multi").getNodeValue().equals("true")){
        		optionlist.setMultipleSelect(true);
        		if (node.getAttributes().getNamedItem("size") != null) {
        			int size = Integer.parseInt(node.getAttributes()
                                                	.getNamedItem("size")
                                                	.getNodeValue());
        			optionlist.setVisibleItemCount(size);
        		}
            }
        }
        initWidget(optionlist);
        optionlist.setStyleName("ScreenOption");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenOption(node, screen);
    }

    public void load(AbstractField field) {
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

    public void submit(AbstractField field) {
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
    
    public void enable(boolean enabled){
        this.enabled = enabled;
    }

    public void onFocus(Widget sender) {
        if(!enabled)
            optionlist.setFocus(false);
        
    }

    public void onLostFocus(Widget sender) {
       // TODO Auto-generated method stub
    }
    
    public void setFocus(boolean focus){
        optionlist.setFocus(focus);
    }
    
    public void destroy() {
        optionlist = null;
        super.destroy();
    }
}
