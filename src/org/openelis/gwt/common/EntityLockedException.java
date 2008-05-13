package org.openelis.gwt.common;

public class EntityLockedException extends RPCException {

    private static final long serialVersionUID = 1L;

    public EntityLockedException() {
        super();
    }

    public EntityLockedException(String msg) {
        super(msg);
    }
}
