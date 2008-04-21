package org.openelis.gwt.common;

public class TableFieldErrorException extends FieldErrorException{

	private static final long serialVersionUID = 1L;
	int rowIndex = -1;

	public TableFieldErrorException() {
        super();
    }

    public TableFieldErrorException(String msg) {
        super(msg);
    }
    
    public TableFieldErrorException(String msg, String fieldName) {
        super(msg, fieldName);
    }
    
    public TableFieldErrorException(String msg, int rowIndex, String fieldName) {
        super(msg, fieldName);
        
        this.rowIndex = rowIndex;
    }
    
    public int getRowIndex(){
    	return rowIndex;
    }
}
