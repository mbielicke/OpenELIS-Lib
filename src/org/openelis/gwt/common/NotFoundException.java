package org.openelis.gwt.common;

import javax.ejb.ApplicationException;

@ApplicationException
public class NotFoundException extends LocalizedException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(){
        super("No Records Found");
    }
    
    public NotFoundException(String msg){
        super(msg);
    }
}
