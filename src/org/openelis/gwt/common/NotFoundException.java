package org.openelis.gwt.common;

public class NotFoundException extends LocalizedException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(){
        super("noRecordsFound");
    }
    
    public NotFoundException(String msg){
        super(msg);
    }
}
