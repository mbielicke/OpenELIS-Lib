package org.openelis.gwt.common;

public class RPCDeleteException extends RPCException {

	private static final long serialVersionUID = 1L;
	private String msg;
    public String appMsg;

    public RPCDeleteException() {
        super();
    }

    public RPCDeleteException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
    
}
