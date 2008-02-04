package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.OptionField;
import org.openelis.gwt.common.data.OptionItem;
import org.openelis.gwt.widget.DragSelectWidget;
import org.openelis.gwt.widget.DragWidget;

import java.util.Iterator;
import java.util.List;
/**
 * ScreenDragSelect wraps a DragSelectWidget to be displayed on the screen.
 * @author tschmidt
 *
 */
public class ScreenDragSelect extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "dragselect";
	/**
	 * Widget wrapped by this class
	 */
    private DragSelectWidget dragselect;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenDragSelect() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;dragselect key="string" shortcut="char"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenDragSelect(Node node, final ScreenBase screen) {
        super(node);
        dragselect = new DragSelectWidget() {
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
        if (node.getAttributes().getNamedItem("shortcut") != null)
            dragselect.setAccessKey(node.getAttributes()
                                        .getNamedItem("shortcut")
                                        .getNodeValue()
                                        .charAt(0));
        initWidget(dragselect);
        dragselect.setStyleName("ScreenDragSelect");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenDragSelect(node, screen);
    }

    public void load(AbstractField field) {
        OptionField recField = (OptionField)field;
        List recMap = recField.getOptions();
        Iterator recIt = recMap.iterator();
        DragWidget[] recs = new DragWidget[recMap.size()];
        for (int i = 0; i < recMap.size(); i++) {
            OptionItem item = (OptionItem)recIt.next();
            recs[i] = new DragWidget(item.display, item.akey, item.selected);
        }
        dragselect.setDragWidgets(recs);
    }

    public void submit(AbstractField field) {
        if (dragselect.changed) {
            DragWidget[] selections = dragselect.getDragWidgets();
            OptionField opts = (OptionField)field;
            opts.clearValues();
            for (int i = 0; i < selections.length; i++) {
                if (selections[i].selected) {
                    opts.setValue(selections[i].value);
                }
            }
        }
    }
    
    public void enable(boolean enabled){
        if(enabled)
            dragselect.removeStyleName("disabled");
        else
            dragselect.addStyleName("disabled");
        dragselect.setEnabled(enabled);
    }
    
    public void destroy() {
        dragselect = null;
        super.destroy();
    }

}
