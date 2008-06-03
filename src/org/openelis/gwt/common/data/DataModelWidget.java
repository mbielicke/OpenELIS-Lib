package org.openelis.gwt.common.data;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SourcesChangeEvents;


public class DataModelWidget extends Composite implements SourcesChangeEvents {

    public enum Action {SELECTION,REFRESH,GETPAGE,ADD,DELETE}
    
    private ChangeListenerCollection changeListeners;
    
    private DataModel model = new DataModel();
    
    public Action action;
    
    public void addChangeListener(ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new ChangeListenerCollection();
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if (changeListeners != null)
            changeListeners.remove(listener);
    }
    
    private void fireChange(Action action) {
        this.action = action;
        if(changeListeners != null)
        	changeListeners.fireChange(this);
    }
    
    public void setModel(DataModel model){
        this.model = model;
        fireChange(Action.REFRESH);
    }
    
    public DataModel getModel() {
        return model;
    }
    
    public void next() {
        try {
            model.select(model.getSelectedIndex()+1);
            fireChange(Action.SELECTION);
        }catch(IndexOutOfBoundsException e){
            model.setPage(model.getPage()+1);
            model.selecttLast(false);
            fireChange(Action.GETPAGE);
        }
    }
    
    public void previous() {
        try {
            model.select(model.getSelectedIndex()-1);
            fireChange(Action.SELECTION);
        }catch(IndexOutOfBoundsException e){
            model.setPage(model.getPage()-1);
            model.selecttLast(true);
            fireChange(Action.GETPAGE);
        }
    }
    
    public void select(int selection) throws IndexOutOfBoundsException {
        model.select(selection);
        fireChange(Action.SELECTION);
    }
    
    public void add(DataSet set){
        model.add(set);
        fireChange(Action.ADD);
    }
    
    public DataSet getSelected() {
        return model.getSelected();
    }
    
    public void delete(int index){
        model.delete(index);
        fireChange(Action.DELETE);
    }
    
    public int getSelectedIndex() {
        return model.getSelectedIndex();
    }
    
    public int getPage(){
        return model.getPage();
    }
    
    public void setPage(int page) {    	
        model.setPage(page);
        fireChange(Action.GETPAGE);
    }

}
