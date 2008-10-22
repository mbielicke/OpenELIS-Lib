package org.openelis.gwt.common.data;

import java.util.ArrayList;

public class DataSet extends ArrayList<DataObject> implements Data,Comparable{
    
    private static final long serialVersionUID = 1L;
    
    protected Data key;
    
    protected Data data;
    
    public boolean shown = true;
    
    public boolean enabled = true;
    
    public DataSet() {
        
    }
    
    public DataSet(Data key) {
        setKey(key);
    }
    
    public DataSet(Data key, DataObject value){
        setKey(key);
        add(value);
    }
    
    public DataSet(Data key, DataObject[] values){
        setKey(key);
        for(DataObject val : values){
            add(val);
        }
    }
    
    public void setKey(Data key){
        this.key = key;
    }
    
    public Data getKey() {
        return key;
    }
    
    public Object clone() {
        DataSet clone = new DataSet();
        for(int i=0; i < size(); i++){
            clone.add((DataObject)get(i).clone());
        }
        if(key != null)
            clone.key = (Data)key.clone();
        if(data != null)
            clone.data = (Data)data.clone();
        return clone;
    }

    public int compareTo(Object obj) {
        if(!(obj instanceof DataSet))
            return -1;
        DataSet comp = (DataSet)obj;
        if(comp.key != null){
            if(comp.key.equals(key))
                return 0;
            return -1;
        }
        if(comp.size() != size())
            return -1;
        for(int i=0; i < size(); i++){
            if(!comp.get(i).equals(get(i)))
                return -1;
        }
        return 0;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
