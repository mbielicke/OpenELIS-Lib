package org.openelis.gwt.common.data.deprecated;

import java.io.Serializable;
@Deprecated
public interface Field<Type> extends Comparable, Serializable {
    
    public void setValue(Object obj);
    
    public Type getValue();

}
