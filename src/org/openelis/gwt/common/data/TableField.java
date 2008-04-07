package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;


public class TableField extends AbstractField {

    private static final long serialVersionUID = 1L;
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.TableModel>
     */
    private TableModel value;
    public static final String TAG_NAME = "rpc-table";
    
    public TableField() {
        
    }
    
    public TableField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }

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

    public Object getInstance(Node node) {
        return new TableField(node);
    }
}
