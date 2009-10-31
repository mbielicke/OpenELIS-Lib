package org.openelis.gwt.widget;

import org.openelis.gwt.common.LocalizedException;
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
        valid = true;
        
        if (invalid) {
            valid = false;
            addException(new LocalizedException("fieldNumericException"));
        }else
        	removeException(new LocalizedException("fieldNumericException"));
        if (required) {
            if (value == null) {
            	valid = false;
                addException(new LocalizedException("fieldRequiredException"));
            }else
            	removeException(new LocalizedException("fieldRequiredException"));
        }
        if (value != null && !isInRange()) {
            valid = false;
        }
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
        	String[] vals = param.split("\\.\\.");
        	for(int i = 0; i < vals.length; i++){
        		try {
        			Double.parseDouble(vals[i]);
        			removeException(new LocalizedException("invalidDouble"));
        		} catch (Exception e) {
        			addException(new LocalizedException("invalidDouble"));
        			valid = false;
        			return;
        		}
        	}
        }
    }
   
    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (value == null)
            return true;
        if (max != null && value > max) {
        	valid = false;
            addException(new LocalizedException("fieldMaxException"));
        }else
        	removeException(new LocalizedException("fieldMaxException"));
        if (min != null && value < min) {
        	valid = false;
            addException(new LocalizedException("fieldMinException"));
        }else
        	removeException(new LocalizedException("fieldMinException"));
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
    	if(queryMode)
    		return super.format();
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
                removeException(new LocalizedException("invalidDouble"));
            } catch (Exception e) {
                valid = false;
                addException(new LocalizedException("invalidDouble"));
            }
        } else {
            try {
                if (val != null && !"".equals(val))
                    setValue(Double.valueOf(NumberFormat.getFormat(pattern)
                                                       .parse(val.toString())));
                removeException(new LocalizedException("invalidDouble"));
            } catch (Exception e) {
                valid = false;
                addException(new LocalizedException("invalidDouble"));
            }
        }
    }

}
