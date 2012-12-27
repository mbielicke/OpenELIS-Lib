package org.openelis.gwt.common;

import java.util.ArrayList;

public class ValidationErrorsList extends Exception {
    
    private static final long serialVersionUID = 1L;
    private ArrayList<Exception> errors;
    private boolean hasErrors, hasWarnings;
    
    public ValidationErrorsList() {
        super();
        errors = new ArrayList<Exception>();
        hasErrors = false;
        hasWarnings = false;
    }

    public ValidationErrorsList(String msg) {
        super(msg);
        hasErrors = false;
        hasWarnings = false;
    }
    
    public void add(Exception ex){
        errors.add(ex);

        if(ex instanceof Warning)
            hasWarnings = true;
        else
            hasErrors = true;
    }
    
    public int size(){
        return errors.size();
    }
    
    public ArrayList<Exception> getErrorList(){
        return errors;
    }
    
    public boolean hasErrors(){
        return hasErrors;
    }
    
    public boolean hasWarnings(){
        return hasWarnings;
    }
    
}
