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
/**
 * DropDownField is an implementation of AbstractField that is 
 * used to send and recieve data for Dropdown and AutoComplete 
 * widgets.
 * @author tschmidt
 *
 */
public class DropDownField extends AbstractField {

    private static final long serialVersionUID = 1L;
    
    /**
     * Member to hold all current selections for this field
     */
    private ArrayList<DataSet> selections = new ArrayList<DataSet>();
    
    /**
     * Member to hold the DataModel that represents the Options for this 
     * field.  If this model is not empty then the widget will display 
     * this model for its options overriding what options it has stored in 
     * the widget itself
     */
    private DataModel model = new DataModel();
    
    /**
     * Tag name used in XML defintion of an rpc
     */
    public static final String TAG_NAME = "rpc-dropdown";
    
    /**
     * Default Constructor
     *
     */
    public DropDownField() {
        
    }
    
    /**
     * Constructor that accpets a defualt value as the passed parameter.
     * @param val
     */
    public DropDownField(Object val) {
        setValue(val);
    }
    
    /**
     * Contructor that accepts an XML node definition for this fields options
     * @param node
     */
    public DropDownField(Node node) {
        setAttributes(node);
    }
    
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
            setValue(dflt);
        }
    }
    
    /**
     * This method will set this fields current value
     */
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

    /**
     * Method that returns the current selected entry for this field
     * as a DataSet.
     */
    public Object getValue() {
        if(selections.size() == 1)
            return (DataSet)selections.get(0);
        else if(selections.size() > 1)
            return selections;
        else
            return null;       
    }
    
    /**
     * Method that returns the the selected entrys display value as a string
     * @return
     */
    public Object getTextValue(){
        if(selections.size() == 1)
            return (String)((DataObject)selections.get(0).get(0)).getValue();
        else if(selections.size() > 1)
            return selections;
        else
            return null;
    }
    
    /**
     * Returns all current selections as a ArrayList<DataSet>
     * @return
     */
    public ArrayList<DataSet> getSelections() {
        return selections;
    }
    
    /**
     * Returns the key for the currently selected entry
     * @return
     */
    public Object getSelectedKey() {
        if(selections.size() == 1)
            return ((DataObject)selections.get(0).getKey()).getValue();
        else if(selections.size() > 1)
            return selections;
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
        selections.add(set);
    }
    
    /**
     * Removes the passed entry from the selections list
     * @param set
     */
    public void remove(DataSet set){
        selections.remove(set);
    }
    
    /**
     * Clears all selections form this field.
     *
     */
    public void clear() {
        selections = new ArrayList<DataSet>();
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
        ArrayList<DataSet> cloneSelections = new ArrayList<DataSet>();
        for(int i=0; i<selections.size(); i++)
            cloneSelections.add((DataSet)selections.get(i).clone());
        obj.setValue(cloneSelections);
        
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
            if (selections.size() == 0 || (selections.size() == 1 && selections.get(0).getKey() == null)) {
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
