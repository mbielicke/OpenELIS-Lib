package org.openelis.gwt.common;

/**
 * DatabaseException denotes a generic runtime data access (SQL) exception.
 */
public class DatabaseException extends Exception {

    private static final long serialVersionUID = 1L;

    public DatabaseException() {
        super();
    }

    public DatabaseException(String msg) {
        super(msg);
    }
    
    public DatabaseException(Exception e) {
        super(e.getMessage());
    }
}
