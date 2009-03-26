package org.openelis.gwt.common.data;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.xml.client.Node;

import java.util.HashMap;


public class IntegerField extends AbstractField<Integer> implements FieldType {

    private static final long  serialVersionUID = 1L;

    private Integer            max;
    private Integer            min;
    private String             pattern;
    protected boolean          invalid;

    public static final String TAG_NAME         = "rpc-integer";

    public IntegerField() {
        super();
    }
    
    public IntegerField(String key) {
        this.key = key;
    }

    public IntegerField(Integer value) {
        super(value);
    }

    public IntegerField(Node node) {
        this();
        setAttributes(node);
    }

    /*
    public void setAttributes(Node node) {
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes().getNamedItem("key").getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                        .getNamedItem("required")
                                        .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("max") != null)
            setMax(new Integer(node.getAttributes()
                                   .getNamedItem("max")
                                   .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            setMin(new Integer(node.getAttributes()
                                   .getNamedItem("min")
                                   .getNodeValue()));
        if (node.getAttributes().getNamedItem("reset") != null)
            setAllowReset(new Boolean(node.getAttributes()
                                          .getNamedItem("reset")
                                          .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            setValue(new Integer(node.getFirstChild().getNodeValue()));
        }
        if (node.getAttributes().getNamedItem("pattern") != null) {
            setFormat(node.getAttributes()
                          .getNamedItem("pattern")
                          .getNodeValue());
        }
    }
    */
    
    public void setAttributes(HashMap<String,String> attribs) {
        if (attribs.containsKey("key"))
            setKey(attribs.get("key"));
        if (attribs.containsKey("required"))
            setRequired(new Boolean(attribs.get("required")));
        if (attribs.containsKey("max"))
            setMax(new Integer(attribs.get("max")));
        if (attribs.containsKey("min"))
            setMin(new Integer(attribs.get("min")));
        if (attribs.containsKey("reset"))
            setAllowReset(new Boolean(attribs.get("reset")));
        if (attribs.containsKey("value"))
            setValue(new Integer(attribs.get("value")));
        if (attribs.containsKey("pattern"))
            setFormat(attribs.get("pattern"));
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public void validate() {
        if (invalid) {
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
        IntegerField obj = new IntegerField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setRequired(required);
        obj.setValue(value);
        obj.setKey(key);
        obj.setAllowReset(allowReset);
        return obj;
    }

    public String format() {
        if (value == null)
            return "";
        if (pattern != null)
            return NumberFormat.getFormat(pattern).format(value);
        return String.valueOf(value);
    }

    public void setFormat(String pattern) {
        this.pattern = pattern;
    }

    public void setValue(Object val) {
        invalid = false;
        if (pattern == null) {
            try {
                if (val instanceof Integer) {
                    value = (Integer)val;
                } else if (val != null && !"".equals(val)) {
                    value = new Double(val.toString()).intValue();
                } else {
                    value = null;
                }
            } catch (Exception e) {
                invalid = true;
            }
        } else {
            try {
                if (val != null && !"".equals(val))
                    value = Integer.valueOf((int)NumberFormat.getFormat(pattern)
                                                             .parse(val.toString()));

            } catch (Exception e) {
                invalid = true;
            }
        }
    }
    
    public AbstractField getQueryField() {
        QueryIntegerField qField = new QueryIntegerField(key);
        qField.setMax(max);
        qField.setMin(min);
        return qField;
    }
}
