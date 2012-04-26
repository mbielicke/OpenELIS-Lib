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
package org.openelis.gwt.widget.table;

import java.util.ArrayList;

/**
 *  This class represents a Row in a table.
 */
public class Row {
    /**
     * ArrayList of values representing column values.
     */
    protected ArrayList<Object> cells;
    
    /**
     * Additional data can be attached to the row that is not 
     * displayed in the table columns
     */
    protected Object data;
    
    /**
     * Default constructor
     *
     */
    public Row() {
        cells = new ArrayList<Object>();
    }
    
    /**
     * Constructor that sets the column values to the passed ArrayList
     * @param cells
     */
    public Row(ArrayList<Object> cells) {
        this.cells = cells;
    }
    
    /**
     * Constructor that creates an empty Row of the size passed
     * @param size
     */
    public Row(int size) {
        cells = new ArrayList<Object>(size);
        for(int i = 0; i < size; i++) {
            cells.add(null);
        }
    }
    
    /**
     * Constructor that creates a Row form the passed objects
     * @param objs
     */
    public Row(Object... objs) {
        cells = new ArrayList<Object>(objs.length);
        for (int i= 0; i < objs.length; i++)
            cells.add(objs[i]);
    }
    
    /**
     * Returns the number of values in this row
     * @return
     */
    public int size() {
        return cells.size();
    }
    
    /**
     * Returns the ArrayList of values contained in this Row
     * @return
     */
    public ArrayList<Object> getCells() {
        return cells;
    }
    
    /**
     * Method called to set the value of a column in the Row.
     * @param index
     * @param value
     */
    public void setCell(int index, Object value) {
        cells.set(index,value);
    }

    /**
     * Method called return the value of a column in the Row.
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> T getCell(int index) {
        return (T)cells.get(index);
    }
    
    /**
     * Method called to attach a data object to this Row.
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }
    
    /**
     * Method called to retrieve the data object attached to this Row
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> T getData() {
        return (T)data;
    }
    
    /**
     * Method that can be overridden by classes that extend Row
     * to apply a specific style to each Row when drawn. 
     * @param index
     * @return
     */
    public String getStyle(int index) {
        return null;
    }
    
    public Object clone() {
    	Row clone;
    
    	clone = new Row(cells.size());
    	for(int i = 0; i < cells.size(); i++) {
    		clone.setCell(i,cells.get(i));
    	}
    	clone.setData(data);
    	
    	return clone;
    }
}
