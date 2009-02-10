package org.openelis.gwt.common.data;

public class IntegerObject extends DataObject<Integer> {
    
    private static final long serialVersionUID = 1L;

    public IntegerObject() {}
    
    public IntegerObject(Integer val){
        setValue(val);
    }
    
    public IntegerObject(int val){
        setValue(new Integer(val));
    }

}
