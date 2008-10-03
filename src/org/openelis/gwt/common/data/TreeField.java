package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import java.util.ArrayList;

public class TreeField extends AbstractField {

    private static final long serialVersionUID = 1L;
    private TreeDataModel value = new TreeDataModel();
    public static final String TAG_NAME = "rpc-tree";
    private ArrayList<String> fieldIndex = new ArrayList<String>();
    
    public TreeField() {
        
    }
    
    public TreeField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        if(val == null){
            if(value != null)
                ((TreeDataModel)value).clear();
            else
                value = null;
        }else
            value = (TreeDataModel)val;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return value;
    }

    public TableField getInstance() {
        TableField obj = new TableField();
        obj.setKey(key);
        obj.setRequired(required);
        obj.setTip(tip);
        obj.setValue(value.getInstance());
        
        return obj;
    }

    public TableField getInstance(Node node) {
        return new TableField(node);
    }
    
    public void validate() {
        valid = validateModel();
    }
    
    public boolean validateModel() {
        boolean valid = true;
        for(DataSet row : value){
            if(row.shown){
                for (DataObject obj : row){
                    if(obj instanceof AbstractField){
                        ((AbstractField)obj).validate();
                        if(!((AbstractField)obj).valid)
                            valid = false;
                    }
                }
            }
        }
        return valid;
    }
    
    public void clearErrors() {
        for(DataSet row : value){
            for(DataObject obj : row){
                if(obj instanceof AbstractField)
                    ((AbstractField)obj).clearErrors();
            }
        }
    }
    
    public void setFieldIndex(ArrayList<String> fieldIndex) {
        this.fieldIndex = fieldIndex;
    }
    
    public AbstractField getField(int row, String field) {
        return (AbstractField)value.get(row).get(fieldIndex.indexOf(field));
    }
}
