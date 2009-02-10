package org.openelis.gwt.common.data;

public class DoubleObject extends DataObject<Double> {
    
    private static final long serialVersionUID = 1L;

    public DoubleObject() {}
    
    public DoubleObject(Double val){
        setValue(val);
    }
    
    public DoubleObject(double val) {
        setValue(new Double(val));
    }
    

}
