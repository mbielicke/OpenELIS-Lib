package org.openelis.gwt.common;

import org.openelis.gwt.common.data.Data;

public class RPC<Display extends Form,Key extends Data> implements Data {
    
    private static final long serialVersionUID = 1L;
    
    public String xml;
    public Key key;
    public Display form;
    
    public Object clone() {
        return null;
    }

}
