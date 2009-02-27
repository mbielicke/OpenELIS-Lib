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
/**
 * DropDownField is an implementation of AbstractField that is 
 * used to send and recieve data for Dropdown and AutoComplete 
 * widgets.
 * @author tschmidt
 *
 */
public class DropDownField<Key> extends AbstractField<ArrayList<DataSet<Key>>> implements FieldType {

    private static final long serialVersionUID = 1L;
    
    /**
     * Member to hold the DataModel that represents the Options for this 
     * field.  If this model is not empty then the widget will display 
     * this model for its options overriding what options it has stored in 
     * the widget itself
     */
    private DataModel<Key> model = new DataModel<Key>();
    
    /**
     * Tag name used in XML defintion of an rpc
     */
    public static final String TAG_NAME = "rpc-dropdown";
    
    /**
     * Default Constructor
     *
     */
     
    public DropDownField() {
        super();
    }
    
    /**
     * Constructor that accpets a defualt value as the passed parameter.
     * @param val
     */
    public DropDownField(DataSet<Key> val) {
        this();
        setValue(val);
    }
    
    /**
     * Contructor that accepts an XML node definition for this fields options
     * @param node
     */
    public DropDownField(Node node) {
        setAttributes(node);
    }
    /*
    public void setAttributes(Node node) {
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
            DataSet<Key> ds = new DataSet<Key>();
            ds.setKey((Key)dflt);
            setValue(ds);
        }
    }
    */
    
    public void setAttributes(HashMap<String,String> attribs) {
        if (attribs.containsKey("key"))
            setKey(attribs.get("key"));
        if (attribs.containsKey("required"))
            setRequired(new Boolean(attribs.get("required")));
        if (attribs.containsKey("value")){
            String dflt = attribs.get("value");
            DataSet<Key> ds = new DataSet<Key>();
            ds.setKey((Key)dflt);
            setValue(ds);
        }
    }
    
    public void setValue(Object val) {
        if(val instanceof DataSet){
            if(val == null){
                value = null;
                return;
            }
            if(value == null)
                value = new ArrayList<DataSet<Key>>();
            else
                value.clear();
            value.clear();
            if(val != null)
                value.add((DataSet<Key>)val);
        }else if(val instanceof ArrayList){
            if(val != null){
                if(value == null)
                    value = new ArrayList<DataSet<Key>>();
                
                value.clear();
                
                for(DataSet<Key> set : (ArrayList<DataSet<Key>>)val)
                    value.add(set);
            }else
                value = null;
        }else if(val == null){
                value = null; 
                return;
        }
    }
    
    /**
     * Method that returns the the selected entrys display value as a string
     * @return
     */
    public Object getTextValue(){
        if(value == null)
            return null;
        
        if(value.size() == 1)
            return value.get(0).get(0).getValue().toString();
        else if(value.size() > 1)
            return value;
        else
            return null;
    }
    
    /**
     * Returns the key for the currently selected entry
     * @return
     */
    public Object getSelectedKey() {
        if(value == null)
            return null;
        
        if(value.size() == 1)
            return value.get(0).getKey();
        else if(value.size() > 1)
            return value;
        else
            return null;       
    }

    /**
     * Adds the entry to the selections list that matches the key passed.
     * @param key
     */
    public void add(Integer key) {
        DataSet set = new DataSet();
        NumberObject no = new NumberObject(key);
        set.setKey(no);
        add(set);
    }
    
    /**
     * Adds the entry to the selections list that matches the key passed.
     * @param key
     */
    public void add(Double key){
        DataSet set = new DataSet();
        NumberObject no = new NumberObject(key);
        set.setKey(no);
        add(set);
    }
    
    /**
     * Adds the entry to the selections list that matches the key passed.
     * @param key
     */
    public void add(String key){
        DataSet set = new DataSet();
        StringObject so = new StringObject(key);
        set.setKey(so);
        add(set);
    }
    
    /**
     * Adds the passed entry to the selections list.
     * @param key
     */
    public void add(DataSet set) {
        value.add(set);
    }
    
    /**
     * Removes the passed entry from the selections list
     * @param set
     */
    public void remove(DataSet set){
        value.remove(set);
    }
    
    /**
     * Clears all selections form this field.
     *
     */
    public void clear() {
        if(value != null)
            value.clear();
    } 
    
    /**
     * Creates a new DropDownField object and sets its values to the 
     * same as the object called
     */
    public Object clone(){
        DropDownField obj = new DropDownField();
        obj.setRequired(required);
        obj.setModel((DataModel)model.clone());
        obj.setKey(key);
        
        //need to create a new selections array list by hand to avoid a shallow copy
        if(value != null){
            ArrayList<DataSet> cloneSelections = new ArrayList<DataSet>();
            for(int i=0; i < value.size(); i++)
                cloneSelections.add((DataSet)value.get(i).clone());
            
            obj.setValue(cloneSelections);
        }else
            obj.setValue(null);
        
        return obj;
    }
    
    /**
     * Returns a new DropDownField instance based on the XML definition passed in
     * @param node
     * @return
     */
    public DropDownField getInstance(Node node){
        return new DropDownField(node);
    }
    
    /**
     * Method will validate the the field's values when called.
     */
    public void validate() {
        if (required) {
            //if there are no selections or there is one selection but it is "" then it is empty and we need to throw an error
            if (value.size() == 0 || (value.size() == 1 && value.get(0).getKey() == null)) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        valid = true;
    }

    /**
     * This method returns the DataModel for this field that holds the options that the 
     *  widget will display if not empty
     * @return
     */
    public DataModel getModel() {
        return model;
    }

    /**
     * Pass a DataModel to this method when you want to override the options that have been
     * set in the widget for this specific field 
     * @param model
     */
    public void setModel(DataModel model) {
        this.model = model;
    }
}
