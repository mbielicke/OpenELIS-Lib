package org.openelis.gwt.common;

import java.util.ArrayList;

public class ValidationErrorsList extends LocalizedException {
    
    private static final long serialVersionUID = 1L;
    private ArrayList<LocalizedException> errors = new ArrayList<LocalizedException>();
    private boolean hasErrors, hasWarnings;
    
    public ValidationErrorsList() {
        super();
        hasErrors = false;
        hasWarnings = false;
    }

    public ValidationErrorsList(String msg) {
        super(msg);
        hasErrors = false;
        hasWarnings = false;
    }
    
    public void add(LocalizedException ex){
        errors.add(ex);

        if(ex instanceof Warning)
            hasWarnings = true;
        else
            hasErrors = true;
    }
    
    public int size(){
        return errors.size();
    }
    
    public ArrayList<LocalizedException> getErrorList(){
        return errors;
    }
    
    public boolean hasErrors(){
        return hasErrors;
    }
    
    public boolean hasWarnings(){
        return hasWarnings;
    }
    
}
