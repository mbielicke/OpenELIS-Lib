package org.openelis.gwt.common;

public class QueryNotFoundException extends RPCException {

	private static final long serialVersionUID = -7562477330849839330L;
	private String msg;
    public String appMsg;

    public QueryNotFoundException() {
        super();
    }

    public QueryNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}
