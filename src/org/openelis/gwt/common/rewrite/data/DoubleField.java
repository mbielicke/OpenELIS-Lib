package org.openelis.gwt.common.rewrite.data;

import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.widget.HandlesEvents;
import org.openelis.util.ValidationException;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasValue;

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

    public void validate() throws ValidationException {
        if (invalid) {
            valid = false;
            throw new ValidationException(AppScreen.consts.get("fieldNumericException"));
        }
        if (required) {
            if (value == null) {
            	valid = false;
                throw new ValidationException(AppScreen.consts.get("fieldRequiredException"));
            }
        }
        if (value != null && !isInRange()) {
            valid = false;
            return;
        }
        valid = true;
    }

    public boolean isInRange() throws ValidationException{
        // TODO Auto-generated method stub
        if (value == null)
            return true;
        if (max != null && value > max) {
        	valid = false;
            throw new ValidationException(AppScreen.consts.get("fieldMaxException"));
        }
        if (min != null && value < min) {
        	valid = false;
            throw new ValidationException(AppScreen.consts.get("fieldMinException"));
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
    

    public void setValue(String val) {
        invalid = false;
        if (pattern == null) {
            try {
                if (val != null && !"".equals(val)) {
                    setValue(Double.valueOf(val.toString()));
                } else {
                    value = null;
                }
            } catch (Exception e) {
                invalid = true;
            }
        } else {
            try {
                if (val != null && !"".equals(val))
                    setValue(Double.valueOf(NumberFormat.getFormat(pattern)
                                                       .parse(val.toString())));

            } catch (Exception e) {
                invalid = true;
            }
        }
    }

}
