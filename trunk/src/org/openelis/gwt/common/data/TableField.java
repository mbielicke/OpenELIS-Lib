package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;


public class TableField extends AbstractField {

    private static final long serialVersionUID = 1L;
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.TableModel>
     */
    private TableModel value;

    public void validate() {
        // TODO Auto-generated method stub
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