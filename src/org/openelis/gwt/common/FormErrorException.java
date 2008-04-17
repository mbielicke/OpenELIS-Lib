package org.openelis.gwt.common;

public class FormErrorException extends RPCException{

	private static final long serialVersionUID = 1L;

	private String msg;
    public String appMsg;

    public FormErrorException() {
        super();
    }

    public FormErrorException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
    
    public void setMessage(String message){
    	this.msg = message;
    }
}
