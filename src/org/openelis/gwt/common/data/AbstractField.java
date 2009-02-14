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

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.xml.client.Node;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * AbstractField is an abstract superclass for objects that can be used for 
 * entering and transferring data back and forth between the GWT client and the 
 * tomcat server.  It wraps a DataObject represent the piece of data that it manages.
 * AbstratField implemets DataField which adds interfaces allowing the Screen programs
 * to load, submit, validate and handle errors for user input controls. 
 * 
 * AbstractField is declared as abstract so it can not be instantied by itself.
 * @author tschmidt
 *
 */
public abstract class AbstractField<Type> extends DataObject<Type> implements DataField, Serializable {

    private static final long serialVersionUID = 1L;

    protected ArrayList<String> errors = new ArrayList<String>();
    protected boolean required;
    public String key;
    protected String tip;
    public boolean valid = true;
    protected boolean allowReset = true;
    
    public AbstractField() {
        setValue(null);
    }
    
    public AbstractField(Type val) {
        setValue(val);
    }
    /**
     * Setting this flag to true will cause an error to 
     * be caught on Form submission if this field has not
     * been entered.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Resturns the value of the required flag.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Will add the passed error message to the list of 
     * messages to be displayed for thie field and marks the 
     * field as invalid.
     */
    public void addError(String err) {
        errors.add(err);
        valid = false;
    }

    /**
     * returns all error messages set for this field.
     */
    public ArrayList<String> getErrors() {
        return errors;
    }

    /* Don't think this is used
    public Vector getValues() {
        // TODO Auto-generated method stub
        return null;
    }
    */

    /**
     * Sets the minimum value that this field is allowed to have.
     * Method must be impelemented in extending class
     */
    public void setMin(Object min) {
    }

    /**
     * Sets the maximum value that this field is allowed to have.
     * Method must be implemented by extending class
     */
    public void setMax(Object max) {
    }

    /**
     * Sets the key value that is used by the FormRPC and the AppScreen when 
     * loading, unloading oor drawing errors on the screen.
     */
    public void setKey(Object key) {
        this.key = (String)key;
    }

    /**
     * Returns the key value that is used by the FormRPC and the AppScreen when 
     * loading, unloading oor drawing errors on the screen.
     */
    public Object getKey() {
        return key;
    }

    /*This is no longer used
    public void addOption(Object key, Object val) {
        // TODO Auto-generated method stub
    }
    */
    
    /** 
     * This method will remove all errors from the screen and 
     * sets the valid flag back to true for the field.
     */
    public void clearErrors() {
        errors = new ArrayList<String>();
        valid = true;
    }

    /*
     I think this has been replaced with clone();
    public DataField getInstance(Node node) {
        return null;
    }
    */

    /**
     * Returns the value for the Valid flag for this
     * field.
     */
    public boolean isValid() {
        // TODO Auto-generated method stub
        return valid;
    }

    /**
     * This stub will be called by FormRPC when
     * validating a screen before submission. Extending classes must
     * implement this function. 
     */
    public void validate() {
        
    }
    
    /**
     * This method will is called during validation to check
     * if the fields values is between MIN and MAX values if set.
     * It must be implemented by extending class if applicable.
     */
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * This field's value will be set to the object being 
     * passed.
     */
    //public void setValue(Object val) {
      // object.setValue(val);
    //}

    /**
     * returns the current value of this field
     */
   // public Object getValue() {
     //   return object.getValue();
   // }

    /**
     * Returns a new field with the same attributes and values
     * as the one the function was called on.
     */
    public Object clone() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Returns the text that should used as a tool tip on the 
     * screen when the mouse is hovered on the correspondigng widget.
     */
    public String getTip() {
        return tip;
    }
    
    /**
     * Sets the value that will be displayed as the tool tip when
     * the mouse is over the corresponding widget on the screen.
     */
    public void setTip(String tip) {
        this.tip = tip;
    }
    
    /**
     * This method will is used to set what type of data that this field 
     * can hold(String,Number,Date...).
     */
   // public void setDataObject(DataObject object){
   //     this.object = object;
   // }
    
    /**
     * Returns the DataObject used to store the value of this field.
     */
   // public DObject getDataObject() {
     //   return object;
   // }
    
    /**
     * This method tells the screen if this fields value should be 
     * reset when reseting the form or if it should be treated as a contstant
     * and not set to null.  Default is to allow reseting the value.  Set to false
     * if the fields value should not change.
     * @param reset
     */
    public void setAllowReset(boolean reset) {
        allowReset = reset;
    }
    
    /**
     * Reuturns the value of the allowReset flag letting the screen
     * know if it can resets this fields value.
     * @return
     */
    public boolean allowsReset() {
        return allowReset;
    }
    
    /**
     * Removes all errors from this field and resets the valid flag back to 
     * true;
     */
    public void removeErrors() {
        valid = true;
        errors.clear();
    }
    
    /**
     * This is implemented so that the Field can be used in places where Comparator functionality is
     * needed such as sorting and filtering of table columns.  Actual comparison logic will be 
     * handled by this fields DataObject.  It can be compared to another AbstractField or DataObject and -1
     * will be returned if the object passed is not one of these.  This not a very good since is can cause
     * erroneous results, should probably do something else.
     */
    public int compareTo(Object o) {
        if(o instanceof AbstractField)
            return compareTo(((AbstractField)o));
        else if(o instanceof DataObject)
            return compareTo(o);
        else
            return -1;
    }
    
    /**
     * Overrides default .equals() logic and passes it to the implementation coded in this fields
     * DataObject. 
     */
    public boolean equals(Object o) {
        if(o instanceof AbstractField)
            return value.equals(((AbstractField)o).value);
        else if (o instanceof DataObject)
            return equals(o);
        else
            return false;
    }
    
    /**
     * This method is called to format the output of this fields value if a 
     * date field or some pattern formating pattern such as been added to the field 
     * such as currency.
     * @return
     */
    public String format() {
        if(getValue() == null)
            return "";
        return String.valueOf(getValue());
    }
    
    /**
     * This method will set the formatting pattern to be used for this fields output.
     * @param pattern
     */
    public void setFormat(String pattern){
        
    }
    
    public abstract void setAttributes(Node node);
        

}
