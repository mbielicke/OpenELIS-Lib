package org.openelis.gwt.common.data;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.xml.client.Node;

public class DoubleField extends AbstractField<Double> implements FieldType {

    private static final long serialVersionUID = 1L;

    private Double max;
    private Double min;
    private String pattern;
    protected boolean invalid;
    
    public static final String TAG_NAME = "rpc-double";

    public DoubleField() {
        super();
    }
    
    public DoubleField(Double value) {
        super(value);
    }
    
    public DoubleField(Node node) {
        this();
        setAttributes(node);
    }
    
    @Override
    public void setAttributes(Node node) {
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("max") != null)
            setMax(new Double(node.getAttributes()
                                          .getNamedItem("max")
                                          .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            setMin(new Double(node.getAttributes()
                                          .getNamedItem("min")
                                          .getNodeValue()));
        if (node.getAttributes().getNamedItem("reset") != null)
            setAllowReset(new Boolean(node.getAttributes()
                                                .getNamedItem("reset")
                                                .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            setValue(new Integer(node.getFirstChild().getNodeValue()));
        }
        if (node.getAttributes().getNamedItem("pattern") != null){
            setFormat(node.getAttributes().getNamedItem("pattern").getNodeValue());
        }   
    }
    
    public void setMax(Double max) {
        this.max = max;
    }
    
    public void setMin(Double min) {
        this.min = min;
    }
    
    public void validate() {
        if (invalid){
            valid = false;
            addError("Field must be numeric");
            return;
        }
        if (required) {
            if (value == null) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        if (value != null && !isInRange()) {
            valid = false;
            return;
        }
        valid = true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (value == null)
            return true;
        if (max != null && value > max) {
            addError("Field exceeded maximum value");
            return false;
        }
        if (min != null && value < min) {
            addError("Field is below minimum value");
            return false;
        }
        return true;
    }

    public String toString() {
        return format();
    }
    
    public Object clone() {
        DoubleField obj = new DoubleField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setRequired(required);
        obj.setValue(value);
        obj.setKey(key);
        obj.setAllowReset(allowReset);
        return obj;
    }
    
    public String format() {
        if(value == null)
            return "";
        if(pattern != null)
            return NumberFormat.getFormat(pattern).format(value);
        return String.valueOf(value);
    }
    
    public void setFormat(String pattern) {
        this.pattern = pattern;
    }
    
    public void setValue(String val) {
        invalid = false;
        if(pattern == null) {
            try {
                if (val != null && !"".equals(val)) {
                    if (val instanceof String && !((String)val).equals(""))
                        value = Double.valueOf((String)val);
                } else {
                    value = null;
                }
            } catch (Exception e) {
                invalid = true;
            }
        }else{
            try {
                setValue(new Integer((String)val));
            }catch(Exception e){
                setValue(Integer.valueOf(Double.toString(NumberFormat.getFormat(pattern).parse((String)val))));
            }
        }
   
    }

}
