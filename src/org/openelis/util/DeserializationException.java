package org.openelis.util;

public class DeserializationException extends Exception {
    int lineNumber = -1;

    /** Constructs an empty DeserializationException.
      */
    private DeserializationException() {
        super();
    }

    /** Constructs a DeserializationException with the descriptive message
      * <b>string</b> and <b>lineNumber</b>, the line number on which
      * the error occurred.
      */
    public DeserializationException(String string, int lineNumber) {
        super(string);
        this.lineNumber = lineNumber;
    }

    /** Returns the line number at which the DeserializationException
      * occurred.
      */
    public int lineNumber() {
        return lineNumber;
    }
}
