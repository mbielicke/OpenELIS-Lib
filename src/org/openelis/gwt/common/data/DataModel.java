package org.openelis.gwt.common.data;


import java.io.Serializable;
import java.util.ArrayList;

public class DataModel implements Serializable {
    
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.DataSet>
     */
    private ArrayList entries = new ArrayList();
    
    private int selected = -1;
    private int page = -1;
    private boolean selectLast;
    
    public int size() {
        return entries.size();
    }
    
    public void add(DataSet set) {
        entries.add(set);
    }

    public void delete(int index) {
        entries.remove(index);
    }

    public DataSet get(int index) {
        return (DataSet)entries.get(index);
    }
    
    public void select(int selection) throws IndexOutOfBoundsException {
        if(selection > entries.size())
            throw new IndexOutOfBoundsException();
        selected = selection;
    }
    
    public int getSelectedIndex() {
        return selected;
    }
    
    public DataSet getSelected() {
        return get(selected);
    }
    
    public int getPage(){
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public void clear() {
        entries = new ArrayList();
        selected = -1;
    }
    
    public void selecttLast(boolean last){
        this.selectLast = last;    
    }
    
    public boolean isSelectLast(){
        return selectLast;
    }
    
    

}
