/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeField extends AbstractField<TreeDataModel> implements FieldType {

    private static final long serialVersionUID = 1L;
    public static final String TAG_NAME = "rpc-tree";
    private ArrayList<String> fieldIndex = new ArrayList<String>();
    
    public TreeField() {
        
    }
    
    public TreeField(Node node){
        setAttributes(node);
    }
    
    public void setAttributes(HashMap<String,String> attribs){
        setKey(attribs.get("key"));
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(TreeDataModel val) {
        // TODO Auto-generated method stub
        if(val == null){
            if(value != null)
                ((TreeDataModel)value).clear();
            else
                value = null;
        }else
            value = (TreeDataModel)val;
    }

    public TreeDataModel getValue() {
        return value;
    }

    public Object clone() {
        TreeField obj = new TreeField();
        obj.setKey(key);
        obj.setRequired(required);
        obj.setTip(tip);
        obj.setValue((TreeDataModel)value.clone());
        
        return obj;
    }

    public TableField getInstance(Node node) {
        return new TableField(node);
    }
    
    public void validate() {
        valid = validateModel();
    }
    
    private boolean valid;
    
    public boolean validateModel() {
        valid = true;
        for(TreeDataItem row : value.list){
            validateItem(row);
        }
        return valid;
    }
    
    public void validateItem(TreeDataItem item) {      
        if(item.hasChildren()) {
            for(TreeDataItem child : item.getItems()){
                validateItem(child);
            }
        }
        if(item.shown){
            for (FieldType obj : item.list){
                if(obj instanceof AbstractField){
                    ((AbstractField)obj).validate();
                    if(!((AbstractField)obj).valid){
                        valid = false;
                        TreeDataItem parent = item.parent;
                        while(parent != null){
                            parent.open = true;
                            parent = parent.parent;
                        }
                    }
                }
            }       
        }
    }
    
    public void clearErrors() {
        for(TreeDataItem row : value.list){
            clearItem(row);
        }
    }
    
    public void clearItem(TreeDataItem item) {
        if(item.hasChildren()) {
            for(TreeDataItem child : item.getItems()){
                clearItem(child);
            }
        }
        for(FieldType obj : item.list){
            if(obj instanceof AbstractField)
                ((AbstractField)obj).clearErrors();
        }
    }
    
    public void setFieldIndex(ArrayList<String> fieldIndex) {
        this.fieldIndex = fieldIndex;
    }
    
    public AbstractField getField(int row, String field) {
        return (AbstractField)value.get(row).get(fieldIndex.indexOf(field));
    }
    
    public boolean isValid() {
        return valid;
    }
}
