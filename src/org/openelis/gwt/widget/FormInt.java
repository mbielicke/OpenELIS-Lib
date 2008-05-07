package org.openelis.gwt.widget;

/**
 * This interface is used by Screens that want to use a ButtonPanel 
 * to control the operations and state of the form.
 * @author tschmidt
 *
 */
public interface FormInt {
    
	public static final int DEFAULT = 1;
    public static final int DISPLAY = 2;
    public static final int UPDATE = 4;
    public static final int ADD = 8;
    public static final int QUERY = 16;
    public static final int BROWSE = 32;
    public static final int DELETE = 64;

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
