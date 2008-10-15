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

import com.google.gwt.xml.client.Node;

public class DropDownField extends AbstractField {

    private static final long serialVersionUID = 1L;
    
    private ArrayList<DataSet> selections = new ArrayList<DataSet>();
    
    private DataModel model = new DataModel();
    
    public static final String TAG_NAME = "rpc-dropdown";
    
    public DropDownField() {
        
    }
    
    public DropDownField(Object val) {
        setValue(val);
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
            if(val instanceof DataSet) {
                selections.clear();
                selections.add((DataSet)val);
            }else{
                selections.clear();
                for(DataSet set : (ArrayList<DataSet>)val)
                    selections.add(set);
            }
        }else
            selections.clear();
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
            return selections.get(0).get(0).getValue();
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
        obj.setModel((DataModel)model.getInstance());
        obj.setKey(key);
        
        //need to create a new selections array list by hand to avoid a shallow copy
        ArrayList<DataSet> cloneSelections = new ArrayList<DataSet>();
        for(int i=0; i<selections.size(); i++)
            cloneSelections.add(selections.get(i).getInstance());
        obj.setValue(cloneSelections);
        
        return obj;
    }
    
    public DropDownField getInstance(Node node){
        return new DropDownField(node);
    }
    
    public void validate() {
        if (required) {
            //if there are no selections or there is one selection but it is "" then it is empty and we need to throw an error
            if (selections.size() == 0 || (selections.size() == 1 && selections.get(0).getKey() == null)) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        valid = true;
    }

    public DataModel getModel() {
        return model;
    }

    public void setModel(DataModel model) {
        this.model = model;
    }
}
