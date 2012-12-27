package org.openelis.gwt.common;

import javax.ejb.ApplicationException;

@ApplicationException
public class PermissionException extends LocalizedException {

    private static final long serialVersionUID = 1L;

    public PermissionException() {
        super();
    }

    public PermissionException(String key) {
        super(key);
    }

    public PermissionException(String key, String... params) {
        super(key, params);
    }
}
