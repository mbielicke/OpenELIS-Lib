package org.openelis.gwt.widget;

import org.openelis.ui.messages.Messages;

import com.google.gwt.i18n.client.NumberFormat;


public class LongField extends Field<Long> {

    private static final long  serialVersionUID = 1L;

    private Long            max;
    private Long            min;
    private String             pattern;
    protected boolean          invalid;

    public void setMax(Long max) {
        this.max = max;
    }

    public void setMin(Long min) {
        this.min = min;
    }

    public void validate() {
    	valid = true;
        if (invalid) {
            valid = false;
            addException(new Exception(Messages.get().exc_invalidNumeric()));
        }else
        	removeException(Messages.get().exc_invalidNumeric());
        if (required) {
            if (value == null) {
            	valid = false;
                addException(new Exception(Messages.get().exc_fieldRequired()));
            }else
            	removeException(Messages.get().exc_fieldRequired());
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
        		if(!vals[i].equalsIgnoreCase("null")){
        			try {
        				Double.parseDouble(param);
        				removeException(Messages.get().exc_invalidNumeric());
        			} catch (Exception e) {
        				addException(new Exception(Messages.get().exc_invalidNumeric()));
        				valid = false;
        				return;
        			}
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
            addException(new Exception(Messages.get().exc_fieldMaxValue()));
        }else
        	removeException(Messages.get().exc_fieldMaxValue());
        if (min != null && value < min) {
        	valid = false;
            addException(new Exception(Messages.get().exc_fieldMinValue()));
        }else
        	removeException(Messages.get().exc_fieldMinValue());
        return true;
    }

    public String toString() {
        return format();
    }

    public Object clone() {
        LongField obj = new LongField();
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
        invalid = false;
        if (pattern == null) {
            try {
                if (val != null && !"".equals(val)) {
                    setValue(new Double(val.toString()).longValue());
                } else {
                    value = null;
                }
                removeException(Messages.get().exc_invalidNumeric());
            } catch (Exception e) {
                valid = false;
                invalid=true;
                addException(new Exception(Messages.get().exc_invalidNumeric()));
            }
        } else {
            try {
                if (val != null && !"".equals(val))
                    setValue(Long.valueOf((int)NumberFormat.getFormat(pattern)
                                                             .parse(val.toString())));
                else if(val.equals(""))
                	setValue(null);
                removeException(Messages.get().exc_invalidNumeric());
            } catch (Exception e) {
                valid = false;
                invalid = true;
                addException(new Exception(Messages.get().exc_invalidNumeric()));
            }
        }
    }
}
