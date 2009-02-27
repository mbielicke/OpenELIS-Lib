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


public class TableField<Key> extends AbstractField<DataModel<Key>> implements FieldType {

    private static final long serialVersionUID = 1L;
    public static final String TAG_NAME = "rpc-table";
    private ArrayList<String> fieldIndex = new ArrayList<String>();
    
    public TableField() {
        
    }
    
    public TableField(Node node){
        setAttributes(node);
    }
    
    public void setAttributes(HashMap<String,String> attribs) {
        setKey(attribs.get("key"));
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(DataModel<Key> val) {
        // TODO Auto-generated method stub
        if(val == null){
            if(value != null)
                ((DataModel)value).clear();
            else
                value = new DataModel<Key>();
        }else
            value = val;
    }
    
    public void setValue(Object val) {
        if(val == null){
            if(value == null)
                value = new DataModel<Key>();
            else
                value.clear();
        }else
           value = (DataModel<Key>)val;
    }

    public DataModel<Key> getValue() {
        return value;
    }

    public Object clone() {
        TableField obj = new TableField();
        obj.setKey(key);
        obj.setRequired(required);
        obj.setTip(tip);
        obj.setValue((DataModel<Key>)value.clone());
        
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
        for(DataSet<Key> row : value){
            if(row.shown){
                for (FieldType obj : row.list){
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
        for(DataSet<Key> row : value){
            for(FieldType obj : row.list){
                if(obj instanceof AbstractField)
                    ((AbstractField)obj).clearErrors();
            }
        }
    }
    
    public void setFieldIndex(ArrayList<String> fieldIndex) {
        this.fieldIndex = fieldIndex;
    }
    
    public ArrayList<String> getFieldIndex() {
        return fieldIndex;
    }
    
    public AbstractField getField(int row, String field) {
        return (AbstractField)value.get(row).get(fieldIndex.indexOf(field));
    }
    
    public void setFieldError(int row,String fieldName,String error){
        getField(row,fieldName).addError(error);
    }
    
    public void clearFieldError(int row, String fieldName) {
        getField(row,fieldName).clearErrors();
    }
    
}
