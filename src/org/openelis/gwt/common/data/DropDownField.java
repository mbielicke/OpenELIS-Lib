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

import org.openelis.gwt.screen.AppScreen;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Node;
/**
 * DropDownField is an implementation of AbstractField that is 
 * used to send and recieve data for Dropdown and AutoComplete 
 * widgets.
 * @author tschmidt
 *
 */
@Deprecated
public class DropDownField<Key> extends AbstractField<ArrayList<TableDataRow<Key>>> implements FieldType {
    


    private static final long serialVersionUID = 1L;
    
    /**
     * Member to hold the DataModel that represents the Options for this 
     * field.  If this model is not empty then the widget will display 
     * this model for its options overriding what options it has stored in 
     * the widget itself
     */
    private TableDataModel<TableDataRow<Key>> model;
    
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
    
    public DropDownField(String key){
        this.key = key;
    }
    
    /**
     * Constructor that accpets a defualt value as the passed parameter.
     * @param val
     */
    public DropDownField(TableDataRow<Key> val) {
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
            TableDataRow<Key> ds = new TableDataRow<Key>();
            ds.key = ((Key)dflt);
            setValue(ds);
        }
    }
    
    public void setValue(Object val) {
        if(val instanceof TableDataRow){
            if(value == null)
                value = new ArrayList<TableDataRow<Key>>(1);
            else
                value.clear();

            value.add((TableDataRow<Key>)val);
        }else if(val instanceof ArrayList){
            if(value == null)
                value = new ArrayList<TableDataRow<Key>>(1);
            else
                value.clear();
            if(((ArrayList)val).size() > 0){
 			    if(((ArrayList)val).get(0) instanceof TableDataRow){
		             for(TableDataRow<Key> sel : (ArrayList<TableDataRow<Key>>)val)
        		        value.add(sel);
				}                    
            }
       }else {
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
            return "";
        
        if(value.size() == 1)
            return value.get(0).cells[0].getValue();
        else if(value.size() > 1)
            return value;
        else
            return "";
    }
    
    /**
     * Returns the key for the currently selected entry
     * @return
     */
    public Object getSelectedKey() {
        if(value == null)
            return null;
        
        if(value.size() == 1)
            return value.get(0).key;
        else if(value.size() > 1)
            return value;
        else
            return null;       
    }

    public void add(Key key) {
        TableDataRow<Key> set = new TableDataRow<Key>();
        set.key = key;
        add(set);
    }
    
    /**
     * Adds the passed entry to the selections list.
     * @param key
     */
    public void add(TableDataRow<Key> set) {
        value.add(set);//new Selection<Key>(set.key,(String)set.get(0).getValue()));
    }
    
    /**
     * Removes the passed entry from the selections list
     * @param set
     */
    public void remove(TableDataRow<Key> set){
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
        DropDownField<Key> obj = new DropDownField<Key>();
        obj.setRequired(required);
        if(model != null)
            obj.setModel((TableDataModel<TableDataRow<Key>>)model.clone());
        obj.setKey(key);
        
        //need to create a new selections array list by hand to avoid a shallow copy
        if(value != null){
            ArrayList<TableDataRow<Key>> cloneSelections = new ArrayList<TableDataRow<Key>>();
            for(int i=0; i < value.size(); i++)
                cloneSelections.add((TableDataRow<Key>)value.get(i).clone());
            
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
        valid = true;
        if (required) {
            //if there are no selections or there is one selection but it is "" then it is empty and we need to throw an error
            if (value == null || value.size() == 0 || (value.size() == 1 && value.get(0).key == null)) {
                
                addError(AppScreen.consts.get("fieldRequiredException"));
                valid = false;
                return;
            }
        }
    }

    /**
     * This method returns the DataModel for this field that holds the options that the 
     *  widget will display if not empty
     * @return
     */
    public TableDataModel<TableDataRow<Key>> getModel() {
        return model;
    }

    /**
     * Pass a DataModel to this method when you want to override the options that have been
     * set in the widget for this specific field 
     * @param model
     */
    public void setModel(TableDataModel<TableDataRow<Key>> model) {
        this.model = model;
    }
    
    public AbstractField getQueryField() {
        return this;
    }
    
    public ArrayList<Key> getKeyValues() {
        ArrayList<Key> keys = new ArrayList<Key>();
        if(getValue() != null){
            for(TableDataRow<Key> sel : getValue()) {
                keys.add(sel.key);
            }
        }
        return keys;
    
    }
    
    public String format() {
        return (String)getTextValue();
    }
}
