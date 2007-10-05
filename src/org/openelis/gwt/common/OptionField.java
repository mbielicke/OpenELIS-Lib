/*
 * Created on Mar 27, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OptionField extends AbstractField implements Serializable {
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.OptionItem>
     */
    private ArrayList options = new ArrayList();
    private boolean multi;
    private String value;

    public boolean isValid() {
        if (required) {
            if (!multi) {
                if (value == null) {
                    addError("Field is required");
                    return false;
                } else if (((String)value).equals("") || ((String)value).equals("0")) {
                    addError("Field is Required");
                    return false;
                }
            } else {
                for (int i = 0; i < options.size(); i++) {
                    OptionItem item = (OptionItem)options.get(i);
                    if (item.selected)
                        return true;
                }
                addError("Selection is required");
                return false;
            }
        }
        return true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        if (val != null) {
            if (val instanceof String) {
                if (multi) {
                    OptionItem temp = new OptionItem();
                    temp.akey = (String)val;
                    ((OptionItem)options.get(options.indexOf(temp))).selected = true;
                } else {
                    value = (String)val;
                }
            }
            if (val instanceof Integer) {
                if (multi) {
                    OptionItem temp = new OptionItem();
                    temp.akey = ((Integer)val).toString();
                    ((OptionItem)options.get(options.indexOf(temp))).selected = true;
                } else {
                    value = val.toString();
                }
            }
            if (val instanceof OptionItem) {
                if (multi) {
                    ((OptionItem)options.get(options.indexOf(val))).selected = true;
                } else {
                    value = ((OptionItem)val).akey;
                }
            }
        } else {
            clearValues();
        }
    }

    public void addValue(Object val) {
        setValue(val);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IField#getValue()
     */
    public Object getValue() {
        // TODO Auto-generated method stub
        if (value == null)
            return "";
        return value;
    }

    public Vector getValues() {
        ArrayList sels = getSelections();
        Vector retVector = new Vector();
        for (int i = 0; i < sels.size(); i++) {
            retVector.add(sels.get(i));
        }
        return retVector;
    }

    public ArrayList getSelections() {
        ArrayList retList = new ArrayList();
        for (int i = 0; i < options.size(); i++) {
            if (((OptionItem)options.get(i)).selected) {
                retList.add(options.get(i));
            }
        }
        return retList;
    }

    public String toString() {
        return "";
    }

    public void addOption(Object key, Object display) {
        OptionItem newItem = new OptionItem();
        newItem.akey = (String)key;
        newItem.display = (String)display;
        options.add(newItem);
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public ArrayList getOptions() {
        return options;
    }

    public void clearValues() {
        if (multi) {
            for (int i = 0; i < options.size(); i++) {
                ((OptionItem)options.get(i)).selected = false;
            }
        } else {
            value = null;
        }
    }

    public void removeOptions() {
        options.clear();
    }

    public String getDisplay() {
        if (value == null)
            return "";
        for (int i = 0; i < options.size(); i++) {
            OptionItem item = (OptionItem)options.get(i);
            if (item.akey.equals(value))
                return item.display;
        }
        return "";
    }

    public Object getInstance() {
        OptionField obj = new OptionField();
        obj.setRequired(required);
        obj.setMulti(multi);
        for (int i = 0; i < options.size(); i++) {
            OptionItem item = (OptionItem)options.get(i);
            obj.addOption(item.akey, item.display);
        }
        obj.setValue(value);
        return obj;
    }

    public Object getInstance(Node field) {
        OptionField option = new OptionField();
        if (field.getAttributes().getNamedItem("key") != null)
            option.setKey(field.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (field.getAttributes().getNamedItem("required") != null)
            option.setRequired(new Boolean(field.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (field.getAttributes().getNamedItem("multi") != null)
            option.setMulti(new Boolean(field.getAttributes()
                                             .getNamedItem("multi")
                                             .getNodeValue()).booleanValue());
        NodeList items = ((Element)field).getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            option.addOption(item.getAttributes()
                                 .getNamedItem("value")
                                 .getNodeValue(), item.getFirstChild()
                                                      .getNodeValue());
        }
        return option;
    }
}
