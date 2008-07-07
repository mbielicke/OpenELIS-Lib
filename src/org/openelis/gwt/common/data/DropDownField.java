/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import java.util.ArrayList;

public class DropDownField extends AbstractField {

    private static final long serialVersionUID = 1L;
    
    private ArrayList<DataSet> selections = new ArrayList<DataSet>();
    
    public static final String TAG_NAME = "rpc-dropdown";
    
    public DropDownField() {
        
    }
    
    public DropDownField(Node node) {
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (node.getFirstChild() != null) {
            String dflt = node.getFirstChild().getNodeValue();
            setValue(dflt);
        }
    }
    
    public void setValue(Object val) {
        if(val instanceof ArrayList){
            selections = (ArrayList<DataSet>)val;
            return;
        }
        selections = new ArrayList<DataSet>();
        if(val instanceof Integer)
            add((Integer)val);
        else if(val instanceof Double)
            add((Double)val);
        else if(val instanceof String)
            add((String)val);
        else if(val instanceof DataSet)
            add((DataSet)val);
    }

    public Object getValue() {
        if(selections.size() == 1)
            return selections.get(0).getKey().getValue();
        else if(selections.size() > 1)
            return selections;
        else
            return null;       
    }
    
    public Object getTextValue(){
    	if(selections.size() == 1)
            return selections.get(0).getObject(0).getValue();
        else if(selections.size() > 1)
            return selections;
        else
            return null;
    }
    
    public ArrayList<DataSet> getSelections() {
        return selections;
    }

    public void add(Integer key) {
        DataSet set = new DataSet();
        NumberObject no = new NumberObject(key);
        set.setKey(no);
        add(set);
    }
    
    public void add(Double key){
        DataSet set = new DataSet();
        NumberObject no = new NumberObject(key);
        set.setKey(no);
        add(set);
    }
    
    public void add(String key){
        DataSet set = new DataSet();
        StringObject so = new StringObject(key);
        set.setKey(so);
        add(set);
    }
    
    public void add(DataSet set) {
        selections.add(set);
    }
    
    public void remove(DataSet set){
        selections.remove(set);
    }
    
    public void clear() {
        selections = new ArrayList<DataSet>();
    } 
    
    public DropDownField getInstance() {
        DropDownField obj = new DropDownField();
        obj.setRequired(required);
        obj.setValue(selections);
        return obj;
    }
    
    public DropDownField getInstance(Node node){
        return new DropDownField(node);
    }
    
    public void validate() {
    	if (required) {
            //if there are no selections or there is one selection but it is "" then it is empty and we need to throw an error
            if (selections.size() == 0 || (selections.size() == 1 && ((StringObject)selections.get(0).getObject(0)).getValue().equals(""))) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        valid = true;
    }
}
