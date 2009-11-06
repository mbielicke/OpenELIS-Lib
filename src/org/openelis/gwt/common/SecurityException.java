package org.openelis.gwt.common;

public class SecurityException extends LocalizedException {

    private static final long serialVersionUID = 1L;

    public SecurityException() {
        super();
    }

    public SecurityException(String key) {
        super(key);
    }

    public SecurityException(String key, String... params) {
        super(key, params);
    }
}
