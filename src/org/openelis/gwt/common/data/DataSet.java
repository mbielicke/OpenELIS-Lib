package org.openelis.gwt.common.data;

import java.util.ArrayList;

/**
 * DataSet is a class that extends ArrayList<DataObject> and implements the Data
 * interface so that it can be used to send and recieve data from the client to the 
 * server.  DataSet is the building block for the DataModel and represents entries into the 
 * model.
 * @author tschmidt
 *
 */
/*
 * This class has been marked as a Field for backwards compatibility only.  Remove once 
 * all screens have been upgraded to create smaller code.
 */
public class DataSet<Key> implements FieldType, Comparable{
    
    private static final long serialVersionUID = 1L;
    
    public ArrayList<FieldType> list;
    /**
     * Key value for this DataSet and is expected to be unique to the DataSet
     * when grouped in a model.
     */
    protected Key key;
    /**
     * This member is used to attach some set of data to the dataset useful to the 
     * program but not accessed or seen  by the user.
     */
    protected FieldType data;
    
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
    public DataSet() {
        list = new ArrayList<FieldType>();
    }
    
    /**
     * Constructor that creates an empty DataSet with the passed key value
     * @param key
     */
    public DataSet(Key key) {
        setKey(key);
        list = new ArrayList<FieldType>();
    }
    
    /**
     * Constructor that creates a DataSet with the passed key value and the single
     * value passed.
     * @param key
     * @param value
     */
    public DataSet(Key key, FieldType val){
        setKey(key);
        list = new ArrayList<FieldType>();
        list.add(val);
    }
    
    /**
     * Constructor that creates a DataSet with passed key vlaue and all the data values
     * passed int the DataObject[]
     * @param key
     * @param values
     */
    public DataSet(Key key, FieldType[] values){
        setKey(key);
        list = new ArrayList<FieldType>();
        for(FieldType val : values){
            list.add(val);
        }
    }
    
    /**
     * This method will set the key value of the DataSet
     * @param key
     */
    public void setKey(Key key){
        this.key = key;
    }
    
    /**
     * This method will return the key value of the DataSet
     * @return
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * This method will create a new DataSet and set the values of it
     * to be the same as this one.
     */
    public Object clone() {
        DataSet<Key> clone = new DataSet<Key>();
        for(int i=0; i < list.size(); i++){
            clone.list.add((FieldType)list.get(i).clone());
        }
        if(key != null)
            clone.key = key;
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
    public FieldType getData() {
       return data;
    }

    /**
     * This method will set the data member that is used by programs to attach
     * info to this dataset but is not accessible to end users
     * @param data
     */
    public void setData(FieldType data) {
       this.data = data;
    }
    
    /**
     * Override of the .equals(Object obj) method used when sorting and filtering 
     * datasets
     */
    public boolean equals(Object object) {
        if(! (object instanceof DataSet)) 
            return false;
        return ((DataSet)object).getKey().equals(key);
    }
    
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return key.hashCode();
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object obj) {
        // TODO Auto-generated method stub
        
    }
    
    public void add(FieldType obj) {
        list.add(obj);
    }
    
    public FieldType get(int index) {
        return list.get(index);
    }
    
    public int size() {
        return list.size();
    }
}
