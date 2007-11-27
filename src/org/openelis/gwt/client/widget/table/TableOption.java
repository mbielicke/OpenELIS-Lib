package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.client.widget.OptionList;
import org.openelis.gwt.common.OptionField;
import org.openelis.gwt.common.OptionItem;

import java.util.Iterator;

/**
 * An OpptionList that implements CellWidget so that it can be used in a Table.
 * 
 * @author tschmidt
 * 
 */
public class TableOption extends OptionList implements TableCellWidget, EventPreview {
    public boolean loadFromModel;
    public String loadFromHidden = null;

    public void onBrowser(Event event) {
        if (DOM.eventGetType(event) == Event.ONKEYDOWN || DOM.eventGetType(event) == Event.ONKEYUP) {
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
        }
    }

    public TableOption() {
        super();
        setStyleName("OptionList");
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return new TableOption();
    }

    public Widget getWidget() {
        OptionList ol = new OptionList();
        return ol;
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

    public Widget getEditor() {
        return this;
    }

    public Widget getEditor(OptionField field) {
        TableOption ret = new TableOption();
        Iterator fieldIt = field.getOptions().iterator();
        while (fieldIt.hasNext()) {
            OptionItem item = (OptionItem)fieldIt.next();
            ret.addItem(item.akey, item.display);
        }
        ret.setValue(field.getValue());
        return ret;
    }

    public Object getDisplay() {
        Label tl = new Label();
        tl.setText(getItemText(getSelectedIndex()));
        tl.setWordWrap(false);
        
        return tl;
    }

    public Object getDisplay(OptionField field) {
        Label tl = new Label();
        tl.setText(field.getDisplay());
        tl.setWordWrap(false);
        
        return tl;
    }

    public boolean onEventPreview(Event event) {
        if (DOM.eventGetType(event) == Event.ONKEYDOWN || DOM.eventGetType(event) == Event.ONKEYUP) {
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
            return false;
        }
        return true;
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        TableOption to = new TableOption();
        if (node.getAttributes().getNamedItem("fromModel") != null) {
            to.loadFromModel = true;
        } else if (node.getAttributes().getNamedItem("fromHidden") != null) {
            to.loadFromHidden = node.getAttributes()
                                    .getNamedItem("fromHidden")
                                    .getNodeValue();
        } else {
            NodeList items = ((Element)node).getElementsByTagName("item");
            for (int j = 0; j < items.getLength(); j++) {
                to.addItem(items.item(j)
                                .getAttributes()
                                .getNamedItem("value")
                                .getNodeValue(), (items.item(j).getFirstChild() == null ? " " : items.item(j).getFirstChild().getNodeValue()));
            }
        }
        return to;
    }

    public void onChange(Widget sender) {
        // TODO Auto-generated method stub
        
    }
}
