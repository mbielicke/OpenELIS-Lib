package org.openelis.gwt.common.data;

public class DoubleObject extends DataObject<Double> implements FieldType {
    
    private static final long serialVersionUID = 1L;

    public DoubleObject() {}
    
    public DoubleObject(Double val){
        setValue(val);
    }
    
    public DoubleObject(double val) {
        setValue(new Double(val));
    }
    
    public Object clone() {
        return new DoubleObject(value);
    }
    

}
