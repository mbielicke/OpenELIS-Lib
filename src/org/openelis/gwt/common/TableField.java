package org.openelis.gwt.common;

import com.google.gwt.xml.client.Node;

public class TableField extends AbstractField {

    /**
     * @gwt.typeArgs <org.openelis.gwt.common.TableModel>
     */
    private TableModel value;

    public boolean isValid() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        if(val == null)
            value = null;
        else
            value = (TableModel)val;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return value;
    }

    public Object getInstance() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getInstance(Node field) {
        TableField table = new TableField();
        table.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
        return table;
    }
}
