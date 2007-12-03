package org.openelis.gwt.common.data;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SourcesChangeEvents;


public class DataModelWidget extends Composite implements SourcesChangeEvents {

    public final static int SELECTION = 0,
                            REFRESH = 1,
                            GETPAGE = 2,
                            ADD = 3,
                            DELETE = 4;
    
    private ChangeListenerCollection changeListeners;
    
    private DataModel model;
    
    public int event;
    
    public void addChangeListener(ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new ChangeListenerCollection();
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if (changeListeners != null)
            changeListeners.remove(listener);
    }
    
    private void fireChange(int event) {
        this.event = event;
        changeListeners.fireChange(this);
    }
    
    public void setModel(DataModel model){
        this.model = model;
        fireChange(REFRESH);
    }
    
    public DataModel getModel() {
        return model;
    }
    
    public void next() {
        try {
            model.select(model.getSelectedIndex()+1);
            fireChange(SELECTION);
        }catch(IndexOutOfBoundsException e){
            model.setPage(model.getPage()+1);
            fireChange(GETPAGE);
        }
    }
    
    public void previous() {
        try {
            model.select(model.getSelectedIndex()-1);
            fireChange(SELECTION);
        }catch(IndexOutOfBoundsException e){
            model.setPage(model.getPage()-1);
            fireChange(GETPAGE);
        }
    }
    
    public void select(int selection) throws IndexOutOfBoundsException {
        model.select(selection);
        fireChange(SELECTION);
    }
    
    public void add(DataSet set){
        model.add(set);
        fireChange(ADD);
    }
    
    public DataSet getSelected() {
        return model.getSelected();
    }
    
    public void delete(int index){
        model.delete(index);
        fireChange(DELETE);
    }
    
    public int getSelectedIndex() {
        return model.getSelectedIndex();
    }
    
    public int getPage(){
        return model.getPage();
    }
    
    public void setPage(int page) {
        model.setPage(page);
        fireChange(GETPAGE);
    }

}
