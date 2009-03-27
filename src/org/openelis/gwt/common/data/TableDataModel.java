package org.openelis.gwt.common.data;

import org.openelis.gwt.common.RPC;

import java.util.ArrayList;

public class TableDataModel<Row extends TableDataRow> implements RPC,FieldType{
    
    private static final long serialVersionUID = 1L;
    
    public ArrayList<Row> list = new ArrayList<Row>();
    
    /**
     * This list will be returned to the server containing all entries from the 
     * model that have been deleted on the client end.
     */
    private ArrayList<Row> deleted;// = new ArrayList<DataSet<Key>>();
    
    /**
     * This list will hold the index to all entries that have marked as selected.
     */
    public ArrayList<Integer> selections = new ArrayList<Integer>(1);
    
    /**
     * This map is used to access the entries in the model randomly using
     * the key value set in the DataSet for each entry 
     */
    //private HashMap<Key,DataSet<Key>> keyMap = new HashMap<Key,DataSet<Key>>(); 
    
    /**
     * This DataSet represents the default data for each entry in the model.
     * It is used to create new entries for the model.
     */
    private Row defaultSet;
    
    /**
     * The index of the selected entry
     */
    public int selected = -1;
    /**
     * The page number of the model if the model is paged
     */
    private int page = 0;
    
    public boolean paged = true;
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
    //public boolean add(DS set){
    //    return add(set);
   // }
    
    /**
     * This method will create a new DataSet consisting of the key, and data object passesd and 
     * and that new DataSet to the end of the Model List.
     * @param key
     * @param value
     * @return
     */
    //public boolean add(Key key, DO value){
      //  if(value instanceof DataSet)
        //    return add(key,(Type)value);
        //DataSet<Key,Type,UserData> set = new DataSet<Key,Type,UserData>();
        //set.setKey(key);
        //set.add(new DataObject<Type>(value));
        //return add(key,set);
    //}
    
    /**
     * This Method will create a new DataSet consisting of the key and adding each DataObject in
     * the passed array.  This DataSet will then be added to the end of the Model List.
     * @param key
     * @param objects
     * @return
     */
    /*public boolean add(Key key,DO[] objects) {
       DataSet<Key,DO> set = new DataSet<Key,DO>();
        for(int i = 0; i < objects.length; i++){
            set.add(objects[i]);
        }
        set.setKey(key);
        return add(set);
    }
    */
    
    /**
     * This method will add the passed DataSet to the end of the model list and also sets the the 
     * DataSet in the Key map using the passed key so that it can be access randomly by this key value.
     * @param key
     * @param set
     * @return
     */
    public boolean add(Row set){
        //keyMap.put((Key)set.key,set);
        return list.add(set);
    }
    
    public void add(int index, Row set) {
        //keyMap.put((Key)set.key,set);
        list.add(index,set);
    }
    
    /**
     * This method will return the DataSet from the keyMap using the passed key.
     * @param key
     * @return
     */
    /*
    public Row getByKey(Object key) {
        return keyMap.get(key);
    }
    */
    /**
     * Deletes the DataSet from the model at the passed index and also 
     * removes the link to the DataSet from the keyMap.
     * @param index
     */
    public void delete(int index) {
        //keyMap.remove(list.get(index).key);
        if(deleted == null)
            deleted = new ArrayList<Row>(1);
        deleted.add(list.remove(index));
    }
    
    public Row set(int index, Row row) {
        return list.set(index, row);
    }
    
    /**
     * Deletes the passed DataSet from the model and also removes the entry
     * from the keyMap.
     * @param set
     */
    public <T extends Row> void delete(T set){
        //keyMap.remove(set.getKey());
        if(deleted == null)
            deleted = new ArrayList<Row>(1);
        deleted.add(set);
        list.remove(set);
    }
    
    /**
     * Sets the default DataSet for this model that will be used by the createNewSet() 
     * method to add new entries to the Model. 
     * @param set
     */
    public void setDefaultSet(Row set) {
        defaultSet = set;
    }
    
    public Row getDefaultSet() {
        return defaultSet;
    }
    
    /**
     * Clones the the DefaultSet for this model and returns it so that data can be set
     * and the entry added to the model.
     * @return
     */
    public Row createNewSet() {
        return (Row)defaultSet.clone();
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
        if(selection > list.size())
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
        if(selections != null && selections.size() > 0)
            return selections.get(0);
        return -1;
    }
    
    /**
     * Returns the selected entry in the model.
     * @return
     */
    public Row getSelected() {
        if(selected > -1)
            return list.get(selected);
        return null;
    }
    
    /**
     * Returns the selected entries as an ArrayList<DataSet> 
     * @return
     */
    public ArrayList<Row> getSelections() {
        ArrayList<Row> selectionSets = new ArrayList<Row>();
        for(int i : selections){ 
            if(i > -1)
                selectionSets.add(list.get(i));
        }
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
        list.clear();
        selected = -1;
        //page = 0;
        selections.clear();
        deleted = null;
        //keyMap.clear();
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
        TableDataModel<Row> clone = new TableDataModel<Row>();
        clone.page = page;
        clone.selected = selected;
        clone.selectLast = selectLast;
        if(defaultSet != null)
            clone.defaultSet = (Row)defaultSet.clone();
        
        for(int i = 0; i < list.size(); i++){
            clone.add((Row)list.get(i).clone());
        }
        return clone;
    }
    
    /**
     * Returns the ArrayList<DataSet> of all deleted entries from this model.
     * @return
     */
    public <T> ArrayList<T> getDeletions() {
        return (ArrayList<T>)deleted;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object obj) {
        // TODO Auto-generated method stub
        
    }

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public Row get(int index) {
        return list.get(index);
    }
    
    public int size() {
        return list.size();
    }
    
   

}
