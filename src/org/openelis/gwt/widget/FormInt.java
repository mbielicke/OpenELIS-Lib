package org.openelis.gwt.widget;

/**
 * This interface is used by Screens that want to use a ButtonPanel 
 * to control the operations and state of the form.
 * @author tschmidt
 *
 */
public interface FormInt {
    
    public enum State {DEFAULT,DISPLAY,UPDATE,ADD,QUERY,BROWSE,DELETE}

    public void query();

    public void next();

    public void prev();

    public void add();

    public void update();

    public void delete();

    public void commit();

    public void abort();
    
    public void reload();
    
    public void select();
    
    public boolean hasChanges();
    
}
