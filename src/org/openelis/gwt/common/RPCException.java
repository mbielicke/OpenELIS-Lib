package org.openelis.gwt.common;

import java.io.Serializable;

public class RPCException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    private String msg;
    public String appMsg;

    public RPCException() {
        super();
    }

    public RPCException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}
