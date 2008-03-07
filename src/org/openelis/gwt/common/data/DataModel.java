package org.openelis.gwt.common.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DataModel implements DataObject, Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.DataSet>
     */
    private ArrayList entries = new ArrayList();
    
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.DataObject, org.openelis.gwt.common.data.DataSet>
     */
    private HashMap keyMap = new HashMap(); 
    
    private int selected = -1;
    private int page = 0;
    private boolean selectLast;
    
    public int size() {
        return entries.size();
    }
    
    public void add(DataSet set) {
        entries.add(set);
        if(set.getKey() != null){
            keyMap.put(set.getKey(), set);
        }
    }

    public void delete(int index) {
        entries.remove(index);
    }
    
    public void delete(DataObject key){
        entries.remove(keyMap.get(key));
        keyMap.remove(key);
    }
    
    public void delete(DataSet set){
        if(set.getKey() != null){
            keyMap.remove(set.getKey());
        }
        entries.remove(set);
    }

    public DataSet get(int index) {
        return (DataSet)entries.get(index);
    }
    
    public DataSet get(DataObject key){
        return (DataSet)keyMap.get(key);
    }
    
    public void select(int selection) throws IndexOutOfBoundsException {
        if(selection > entries.size())
            throw new IndexOutOfBoundsException();
        selected = selection;
    }
    
    public void select(DataObject key) {
        selected = entries.indexOf(keyMap.get(key));
    }
    
    public void select(DataSet set){
        selected = entries.indexOf(set);
    }
    
    public int getSelectedIndex() {
        return selected;
    }
    
    public DataSet getSelected() {
        return get(selected);
    }
    
    public int indexOf(DataObject key){
        return entries.indexOf(keyMap.get(key));
    }
    
    public int indexOf(DataSet set){
        return entries.indexOf(set);
    }
    
    public int getPage(){
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public void clear() {
        entries = new ArrayList();
        keyMap = new HashMap();
        selected = -1;
    }
    
    public void selecttLast(boolean last){
        this.selectLast = last;    
    }
    
    public boolean isSelectLast(){
        return selectLast;
    }
    
    public Object getInstance() {
        DataModel clone = new DataModel();
        clone.page = page;
        clone.selected = selected;
        clone.selectLast = selectLast;
        for(int i = 0; i < size(); i++){
            clone.add((DataSet)get(i).getInstance());
        }
        return clone;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object object) {
        // TODO Auto-generated method stub
        
    }
    
    

}
