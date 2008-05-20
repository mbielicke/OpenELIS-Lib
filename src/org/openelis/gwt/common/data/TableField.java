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
        valid = true;
        
        if(value == null)
            return;
        
        for(int i = 0; i < value.numRows(); i++){
            for(int j = 0; j < value.getRow(i).numColumns(); j++){
                if(!value.getFieldAt(i,j).isValid()){
                    valid = false;
                    break;
                }
            }
            if(!valid){
                break;
            }
        }
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
