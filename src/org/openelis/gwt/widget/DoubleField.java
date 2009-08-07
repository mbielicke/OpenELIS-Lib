package org.openelis.gwt.widget;

import org.openelis.gwt.screen.Screen;

import com.google.gwt.i18n.client.NumberFormat;

public class DoubleField extends Field<Double> {

    private static final long  serialVersionUID = 1L;

    private Double             max;
    private Double             min;
    private String             pattern;
    protected boolean          invalid;
    
    public void setMax(Double max) {
        this.max = max;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public void validate() {
        if (invalid) {
            valid = false;
            addError(Screen.consts.get("fieldNumericException"));
        }
        if (required) {
            if (value == null) {
            	valid = false;
                addError(Screen.consts.get("fieldRequiredException"));
            }
        }
        if (value != null && !isInRange()) {
            valid = false;
            return;
        }
        valid = true;
    }
    
    public void validateQuery() {
    	valid = true;
    	if(queryString == null || queryString.equals("")){
    		queryString = null;
    		return;
    	}
    	QueryFieldUtil qField = new QueryFieldUtil();
    	qField.parse(queryString);
        for(String param : qField.parameter){
            try {
                Double.parseDouble(param);
            } catch (Exception e) {
                addError("Param is not a valid double");
                valid = false;
                return;
            }
        }
    }
   
    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (value == null)
            return true;
        if (max != null && value > max) {
        	valid = false;
            addError(Screen.consts.get("fieldMaxException"));
        }
        if (min != null && value < min) {
        	valid = false;
            addError(Screen.consts.get("fieldMinException"));
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
        obj.required = required;
        obj.value = value;
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
    

    public void setStringValue(String val) {
        valid = true;
        if (pattern == null) {
            try {
                if (val != null && !"".equals(val)) {
                    setValue(Double.valueOf(val.toString()));
                } else {
                    value = null;
                }
            } catch (Exception e) {
                valid = false;
                addError("Field must a valid Double value");
            }
        } else {
            try {
                if (val != null && !"".equals(val))
                    setValue(Double.valueOf(NumberFormat.getFormat(pattern)
                                                       .parse(val.toString())));

            } catch (Exception e) {
                valid = false;
                addError("Field must be a valid Double value");
            }
        }
    }

}
