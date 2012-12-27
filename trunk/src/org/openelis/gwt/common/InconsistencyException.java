package org.openelis.gwt.common;

import javax.ejb.ApplicationException;

/** Error signaling that an inconsistent internal state has been
 * discovered. Classes throw this error when something that should be
 * impossible has happened. This error is usually due to incorrect program
 * logic.
 */
@ApplicationException
public class InconsistencyException extends LocalizedException {

    private static final long serialVersionUID = 1L;

    public InconsistencyException(){
        super();
    }
    
    public InconsistencyException(String msg){
        super(msg);
    }
}
