package org.openelis.gwt.common;

/**
 * DatabaseException denotes a generic runtime data access (SQL) exception.
 */
public class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatabaseException() {
        super("Unknown database ERROR");
    }

    public DatabaseException(String msg) {
        super(msg);
    }
    
    public DatabaseException(Exception e) {
        super(e.getMessage());
    }
}
