package org.openelis.gwt.common.data;

import java.io.Serializable;

public interface FieldType extends Serializable {
    
    public Object clone();

    public void setValue(Object obj);
    
    public Object getValue();

}
