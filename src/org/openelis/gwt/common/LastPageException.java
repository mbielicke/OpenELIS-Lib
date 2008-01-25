package org.openelis.gwt.common;


public class LastPageException extends RPCException {

	private static final long serialVersionUID = 1L;
	private String msg;
	public String appMsg;

	public LastPageException() {
	    super();
	}

	public LastPageException(String msg) {
	    super(msg);
	    this.msg = msg;
	}

	public String getMessage() {
	    return this.msg;
	}
}
