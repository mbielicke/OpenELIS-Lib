package org.openelis.gwt.common.data;

import java.io.Serializable;

public interface Field<Type> extends Comparable, Serializable {
    
    public void setValue(Type obj);
    
    public Type getValue();

}
