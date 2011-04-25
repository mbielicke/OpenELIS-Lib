package org.openelis.gwt.common;

import javax.ejb.ApplicationException;

/**
 * DatabaseException denotes a generic runtime data access (SQL) exception.
 */
@ApplicationException(rollback=true)
public class DatabaseException extends Exception {

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
