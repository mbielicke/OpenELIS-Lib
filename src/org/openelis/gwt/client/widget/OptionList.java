package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Node;
import org.openelis.gwt.client.screen.ScreenBase;
/**
 * OptionList extends a GWT ListBox for adding functionality for 
 * adding and selecting items.
 * @author tschmidt
 *
 */
public class OptionList extends ListBox {

    private ScreenBase screen;

    /**
     * Creates an empty list box.
     */
    public OptionList() {
        super();
        setStyleName(".gwt-ListBox");
    }

    public OptionList(Node node, ScreenBase screen) {
        this.screen = screen;
        screen.widgets.put(node.getAttributes()
                              .getNamedItem("key")
                              .getNodeValue(), this);
        if (node.getAttributes().getNamedItem("onChange") != null)
            addChangeListener(screen);
        if (node.getAttributes().getNamedItem("shortcut") != null)
            setAccessKey(node.getAttributes()
                             .getNamedItem("shortcut")
                             .getNodeValue()
                             .charAt(0));
        if (node.getAttributes().getNamedItem("multi") != null) {
            setMultipleSelect(true);
            if (node.getAttributes().getNamedItem("size") != null) {
                int size = Integer.parseInt(node.getAttributes()
                                                .getNamedItem("size")
                                                .getNodeValue());
                setVisibleItemCount(size);
            }
        }
    }

    /**
     * Adds an item to the list box.
     * 
     * @param item
     *        the text of the item to be added
     */
    public void addItem(String value, String item) {
        insertItem(value, item, getItemCount());
    }

    /**
     * Inserts an item into the list box.
     * 
     * @param item
     *        the text of the item to be inserted
     * @param idx
     *        the index at which to insert it
     */
    public void insertItem(String value, String item, int idx) {
        Element option = DOM.createElement("OPTION");
        DOM.setAttribute(option, "value", value);
        DOM.setInnerText(option, item);
        DOM.insertChild(getElement(), option, idx);
    }

    public Object getValue() {
        Element child = DOM.getChild(getElement(), getSelectedIndex());
        return DOM.getAttribute(child, "value");
    }

    public void setValue(Object value) {
        if (value == null)
            setSelectedIndex(0);
        else {
            for (int i = 0; i < DOM.getChildCount(getElement()); i++) {
                if (((String)value).equals(DOM.getAttribute(DOM.getChild(getElement(),
                                                                         i),
                                                            "value"))) {
                    setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public Object getDisplay() {
        return getItemText(getSelectedIndex());
    }

}
