package org.openelis.gwt.common;


public class FieldErrorException extends RPCException{

	private static final long serialVersionUID = 1L;

	private String msg;
    public String appMsg;
    private String fieldName;

    public FieldErrorException() {
        super();
    }

    public FieldErrorException(String msg) {
        super(msg);
        this.msg = msg;
    }
    
    public FieldErrorException(String msg, String fieldName) {
        super(msg);
        this.msg = msg;
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return this.msg;
    }
    
    public void setMessage(String message){
    	this.msg = message;
    }
    
    public String getFieldName(){
    	return fieldName;
    }
}
