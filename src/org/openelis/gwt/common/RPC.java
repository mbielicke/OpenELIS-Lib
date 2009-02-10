package org.openelis.gwt.common;

import java.io.Serializable;

public class RPC<Display extends Form,Key> implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public String xml;
    public Key key;
    public Display form;
    

}
