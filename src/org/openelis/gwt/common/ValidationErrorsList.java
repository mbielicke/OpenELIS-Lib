package org.openelis.gwt.common;

import java.util.ArrayList;

public class ValidationErrorsList extends RPCException{
    
    private static final long serialVersionUID = 1L;
    private ArrayList<RPCException> errors = new ArrayList<RPCException>();
    
    public ValidationErrorsList() {
        super();
    }

    public ValidationErrorsList(String msg) {
        super(msg);
    }
    
    public void add(RPCException ex){
        errors.add(ex);
        
    }
    
    public int size(){
        return errors.size();
    }
    
    public ArrayList<RPCException> getErrorList(){
        return errors;
    }
    
}
