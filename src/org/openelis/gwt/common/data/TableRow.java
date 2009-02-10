package org.openelis.gwt.common.data;

import java.util.ArrayList;

public class TableRow extends ArrayList<Field> {
    
    private static final long serialVersionUID = 1L;

    /**
     * Flag letting Widgets know if this DataSet should be shown on screen or 
     * if it is currently hidden.  Used when filtering.
     */
    public boolean shown = true;
    
    /**
     * Flag letting Widgets know if this DataSet is enabled and is available for
     * selection by users.
     */
    public boolean enabled = true;
    
    /**
     * Default constructor
     *
     */
    public TableRow() {
        
    }
    
    /**
     * Constructor that creates an empty DataSet with the passed key value
     * @param key
     */
    //public TableR(Key key) {
      //  setKey(key);
    //}
    
    /**
     * Constructor that creates a DataSet with the passed key value and the single
     * value passed.
     * @param key
     * @param value
     */
    public TableRow(Field val){
        add(val);
    }
    
    /**
     * Constructor that creates a DataSet with passed key vlaue and all the data values
     * passed int the DataObject[]
     * @param key
     * @param values
     */
    public TableRow(Field[] values){
        for(Field val : values){
            add(val);
        }
    }
    
    /**
     * This method will set the key value of the DataSet
     * @param key
     */
    //public void setKey(Key key){
      //  this.key = key;
    //}
    
    /**
     * This method will return the key value of the DataSet
     * @return
     */
    //public Key getKey() {
      //  return key;
    //}
    
    /**
     * This method will create a new DataSet and set the values of it
     * to be the same as this one.
     */
    public Object clone() {
        TableRow clone = new TableRow();
        for(int i=0; i < size(); i++){
            clone.add(get(i));
        }
        //if(key != null)
           // clone.key = key;
        //if(data != null)
          //  clone.data = (Data)data.clone();
        clone.enabled = enabled;
        return clone;
    }

    /**
     * This method is implemented for the Comparable interface so that 
     * DataSets can be compared when sorting 
     *
     */
    public int compareTo(Object obj) {
    /*    if(!(obj instanceof DataSet<ArrayList<DO>>))
            return -1;
        DataSet comp = (DataSet)obj;
        if(comp.key != null){
            if(comp.key.equals(key))
                return 0;
            return -1;
        }
        if(comp.value.size() != size())
            return -1;
        for(int i=0; i < size(); i++){
            if(!comp.get(i).equals(get(i)))
                return -1;
        }
     */
        return 0;
    }

    /**
     * This method will return the data member that is used by programs to attach
     * info to this dataset but is not accessible to end users
     * @return
     */
   // public UserData getData() {
     //  return data;
   // }

    /**
     * This method will set the data member that is used by programs to attach
     * info to this dataset but is not accessible to end users
     * @param data
     */
   // public void setData(UserData data) {
     //  this.data = data;
  // }
    
    /**
     * Override of the .equals(Object obj) method used when sorting and filtering 
     * datasets
     */
    //public boolean equals(Object object) {
      //  if(! (object instanceof DataSet)) 
        //    return false;
        //return ((DataSet)object).getKey().equals(key);
    //}
    
    //@Override
    //public int hashCode() {
        // TODO Auto-generated method stub
      //  return key.hashCode();
   // }

}
