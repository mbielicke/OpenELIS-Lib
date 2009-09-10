package org.openelis.gwt.common.data.deprecated;

@Deprecated
public class IntegerObject extends DataObject<Integer> implements FieldType{
    
    private static final long serialVersionUID = 1L;

    public IntegerObject() {}
    
    public IntegerObject(Integer val){
        setValue(val);
    }
    
    public IntegerObject(int val){
        setValue(new Integer(val));
    }
    
    public Object clone() {
        return new IntegerObject(value);
    }

}
