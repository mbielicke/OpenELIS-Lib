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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.xml.client.Node;

@Deprecated
public class TableField<Key extends TableDataRow> extends AbstractField<TableDataModel<Key>> implements FieldType {

    private static final long serialVersionUID = 1L;
    public static final String TAG_NAME = "rpc-table";
    private ArrayList<String> fieldIndex = new ArrayList<String>();
    public TableDataRow defaultRow;
    
    public TableField() {
        
    }
    
    public TableField(Node node){
        setAttributes(node);
    }
    
    public TableField(String key) {
        this.key = key;
    }
    
    public void setAttributes(HashMap<String,String> attribs) {
        setKey(attribs.get("key"));
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    /*
    public void setValue(TableDataModel val) {
        // TODO Auto-generated method stub
        if(val == null){
            if(value != null){
                ((TableDataModel)value).clear();
            }else{
                value = new TableDataModel();
            }
        }else
            value = val;
        ((TableDataModel)value).setDefaultSet(defaultRow);
    }
    */
    public void setValue(Object val) {
        if(val == null){
            if(value == null)
                value = new TableDataModel<Key>();
            else
                value.clear();
        }else
           value = (TableDataModel<Key>)val;
        if(defaultRow != null)
        	((TableDataModel)value).setDefaultSet(defaultRow);
    }

    public TableDataModel<Key> getValue() {
        return value;
    }

    public Object clone() {
        TableFieldRPC obj = new TableFieldRPC();
        obj.setKey(key);
        obj.setRequired(required);
        obj.setTip(tip);
        obj.setValue((TableDataModel<Key>)value.clone());
        
        return obj;
    }

    public TableFieldRPC getInstance(Node node) {
        return new TableFieldRPC(node);
    }
    
    public void validate() {
        valid = validateModel();
    }
    
    public boolean validateModel() {
        boolean valid = true;
        for(TableDataRow row : value.list){
            if(row.shown){
                for (AbstractField obj : (List<AbstractField>)row.getCells()){
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
        for(TableDataRow row : value.list){
            for(AbstractField obj : (List<AbstractField>)row.getCells()){
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
        return (AbstractField)value.get(row).getCells().get(fieldIndex.indexOf(field));
    }
    
    public void setFieldError(int row,String fieldName,String error){
        getField(row,fieldName).addError(error);
    }
    
    public void clearFieldError(int row, String fieldName) {
        getField(row,fieldName).clearErrors();
    }
    
}
