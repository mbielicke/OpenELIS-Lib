package org.openelis.gwt.widget;

/**
 * This interface is used by Screens that want to use a ButtonPanel 
 * to control the operations and state of the form.
 * @author tschmidt
 *
 */
public interface FormInt {
    
    public static final int DISPLAY = 0;
    public static final int UPDATE = 1;
    public static final int ADD = 2;
    public static final int QUERY = 3;
    public static final int BROWSE = 4;
    public static final int DELETE = 5;

    public void query(int state);

    public void next(int state);

    public void prev(int state);

    public void add(int state);

    public void up(int state);

    public void delete(int state);

    public void commit(int state);

    public void abort(int state);
    
    public void reload(int state);
    
    public void select(int state);
    
    public boolean hasChanges();
    
    public void option(String action, int state);
}
