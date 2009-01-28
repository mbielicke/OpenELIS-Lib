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
/**
 * DataModel implements the Data interface and extends ArrayList<DataSet> in order
 * send and recieve data for widgets such as Tables, Dropdown and Autocomplete.   
 * @author tschmidt
 *
 */
public class DataModel<D extends DataSet> extends ArrayList<D> implements Data {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * This list will be returned to the server containing all entries from the 
     * model that have been deleted on the client end.
     */
    private ArrayList<D> deleted = new ArrayList<D>();
    
    /**
     * This list will hold the index to all entries that have marked as selected.
     */
    public ArrayList<Integer> selections = new ArrayList<Integer>();
    
    /**
     * This map is used to access the entries in the model randomly using
     * the key value set in the DataSet for each entry 
     */
    private HashMap<Data,D> keyMap = new HashMap<Data,D>(); 
    
    /**
     * This DataSet represents the default data for each entry in the model.
     * It is used to create new entries for the model.
     */
    private D defaultSet;
    
    /**
     * The index of the selected entry
     */
    public int selected = -1;
    /**
     * The page number of the model if the model is paged
     */
    private int page = 0;
    /**
     * This flag is used when walking through a paged model such as AZ query reuslts.
     * if SelectLast is set then on return of the new page the last entry should be selected
     * and displayed.  Used primarily when walkign through the list using the previous button.
     */
    private boolean selectLast;
    /**
     * This flag is used to mark when the model can seelct multiple selections.
     */
    public boolean multiSelect;
    
    /**
     * Adds the passed DataSet to the end of the Model list.
     */
    public boolean add(D set){
        return add(set.getKey(),set);
    }
    
    /**
     * This method will create a new DataSet consisting of the key, and data object passesd and 
     * and that new DataSet to the end of the Model List.
     * @param key
     * @param value
     * @return
     */
    public boolean add(Data key, DataObject value){
        if(value instanceof DataSet)
            return add(key,(D)value);
        DataSet set = new DataSet();
        set.setKey(key);
        set.add(value);
        return add(key,(D)set);
    }
    
    /**
     * This Method will create a new DataSet consisting of the key and adding each DataObject in
     * the passed array.  This DataSet will then be added to the end of the Model List.
     * @param key
     * @param objects
     * @return
     */
    public boolean add(Data key, DataObject[] objects) {
        DataSet set = new DataSet();
        for(int i = 0; i < objects.length; i++){
            set.add(objects[i]);
        }
        set.setKey(key);
        return add(key,(D)set);
    }
    
    /**
     * This method will add the passed DataSet to the end of the model list and also sets the the 
     * DataSet in the Key map using the passed key so that it can be access randomly by this key value.
     * @param key
     * @param set
     * @return
     */
    public boolean add(Data key, D set){
        keyMap.put(key,set);
        return super.add(set);
    }
    
    /**
     * This method will return the DataSet from the keyMap using the passed key.
     * @param key
     * @return
     */
    public D getByKey(Data key) {
        return keyMap.get(key);
    }
    
    /**
     * Deletes the DataSet from the model at the passed index and also 
     * removes the link to the DataSet from the keyMap.
     * @param index
     */
    public void delete(int index) {
        keyMap.remove(get(index).key);
        deleted.add(remove(index));
    }
    
    /**
     * Deletes the passed DataSet from the model and also removes the entry
     * from the keyMap.
     * @param set
     */
    public void delete(D set){
        keyMap.remove(set.getKey());
        deleted.add(set);
        remove(set);
    }
    
    /**
     * Sets the default DataSet for this model that will be used by the createNewSet() 
     * method to add new entries to the Model. 
     * @param set
     */
    public void setDefaultSet(D set) {
        defaultSet = set;
    }
    
    /**
     * Clones the the DefaultSet for this model and returns it so that data can be set
     * and the entry added to the model.
     * @return
     */
    public D createNewSet() {
        return (D)defaultSet.clone();
    }
    
    /**
     * This method will create a new empty default entry by calling createNewSet() and then
     * adding the new default entry to the end of the model list.
     *
     */
    public void addDefault() {
        add(createNewSet());
    }
            
    /**
     * Selects an entry in the model by the passed index
     * @param selection
     * @throws IndexOutOfBoundsException
     */
    public void select(int selection) throws IndexOutOfBoundsException {
        if(selection > size())
            throw new IndexOutOfBoundsException();
        selected = selection;
        if(!multiSelect)
            selections.clear();
        selections.add(selection);
    }
         
    /**
     * Unselects the entry from the model by the passed index
     * @param index
     */
    public void unselect(int index) {
        if(index == -1){
            selections.clear();
            selected = -1;
        }else{
            selections.remove(new Integer(index));
            if(selected == index)
                selected = -1;
        }
    }
    
    /**
     * This method will return the selected index for the model.  If the model allows
     * multiple selection then this method will return the entry that was first
     * selected.
     * @return
     */
    public int getSelectedIndex() {
        return selections.get(0);
    }
    
    /**
     * Returns the selected entry in the model.
     * @return
     */
    public D getSelected() {
        return get(selected);
    }
    
    /**
     * Returns the selected entries as an ArrayList<DataSet> 
     * @return
     */
    public ArrayList<D> getSelections() {
        ArrayList<D> selectionSets = new ArrayList<D>();
        for(int i : selections) 
            selectionSets.add(get(i));
        return selectionSets;
    }
   
    /**
     * Returns the current page this model represents if it is from a paged
     * model set.
     * @return
     */
    public int getPage(){
        return page;
    }
    
    /**
     * Sets the current page that this model represents
     * @param page
     */
    public void setPage(int page) {
        this.page = page;
    }
    
    /**
     * This method will empty the model of all entries and removes all selections
     * and delettions along with the key mapping.
     */
    public void clear() {
        super.clear();
        selected = -1;
        page = 0;
        selections.clear();
        deleted.clear();
        keyMap.clear();
    }
    
    /**
     * Clears all selections from the model leaving all data and deletions in tact.
     *
     */
    public void clearSelections() {
        selected = -1;
        selections.clear();
    }
    
    /**
     * This method will set the selectLast flag 
     * @param last
     */
    public void selecttLast(boolean last){
        this.selectLast = last;    
    }
    
    /**
     * This method returns the value of the selectLast flag
     * @return
     */
    public boolean isSelectLast(){
        return selectLast;
    }
    
    /**
     * This method will clone this model to a new DataModel object and return the 
     * new model.
     * 
     */
    public Object clone() {
        DataModel<D> clone = new DataModel<D>();
        clone.page = page;
        clone.selected = selected;
        clone.selectLast = selectLast;
        if(defaultSet != null)
            clone.defaultSet = (D)defaultSet.clone();
        
        for(int i = 0; i < size(); i++){
            clone.add((D)get(i).clone());
        }
        return clone;
    }
    
    /**
     * Returns the ArrayList<DataSet> of all deleted entries from this model.
     * @return
     */
    public ArrayList<D> getDeletions() {
        return deleted;
    }

}
