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
package org.openelis.gwt.widget;

/**
 * Class used by AutoComplete to store its key-value pair
 * @author tschmidt
 *
 */
public class AutoCompleteValue {
    
	/**
	 * The database key for this item
	 */
    protected Integer id;
    /**
     * The string used for display in the UI
     */
    protected String  display;
    /**
     * Optional Data object that can be attached and referenced on selection
     */
    protected Object  data;
    
    /**
     * Default no-arg constructor
     */
    public AutoCompleteValue() {
        
    }
    
    /**
     * Constructor that takes an integer key and a String display value
     * @param id
     * @param display
     */
    public AutoCompleteValue(Integer id, String display){
        this.id = id;
        this.display = display;
    }

    /**
     * Returns the database row key for this item
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the key value for this item
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the display for this AutoComplete
     * @return
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Sets the diplay String for this AutoCompleteValue 
     * @param display
     */
    public void setDisplay(String display) {
        this.display = display;
    }
    
    /**
     * Returns the data object associated with this Autocompelte Value
     * @return
     */
    public Object getData() {
    	return data;
    }
    
    /**
     * Sets the data object for this Autocomplete value
     * @param data
     */
    public void setData(Object data) {
    	this.data = data;
    }
    
}
