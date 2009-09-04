package org.openelis.gwt.common;

import java.util.ArrayList;

public class ValidationErrorsList extends Exception {
    
    private static final long serialVersionUID = 1L;
    private ArrayList<Exception> errors = new ArrayList<Exception>();
    
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
    
    public ArrayList<Exception> getErrorList(){
        return errors;
    }
    
}
