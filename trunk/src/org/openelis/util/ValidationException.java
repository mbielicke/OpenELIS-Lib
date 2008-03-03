package org.openelis.util;

/**
 * Exception signalling that data is invalid. Classes throw this exception when
 * a change in data would render the underlying information base (database
 * record) invalid or inconsistent.
 */
public class ValidationException extends Exception {
    public ValidationException() {
        super();
    }

    public ValidationException(String msg) {
        super(msg);
    }
}
