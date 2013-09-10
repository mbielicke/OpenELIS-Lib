/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget;

import java.util.Date;

import org.openelis.gwt.common.DataBaseUtil;
import org.openelis.ui.common.Datetime;
import org.openelis.ui.messages.Messages;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * DateField is an implementation of AbstractField that represents data
 * used for Date fields
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DateField extends Field<Datetime> {
    
    private byte begin;
    private byte end;
    
    /**
     * Number of days in the future from current date that this field is 
     * allowed to represent. 
     */
    private Integer max;
    /**
     * Number of days in the past from current date that this field is 
     * allowed to represent.
     */
    private Integer min;

    /**
     * String that represents the Date format to be used when displaying this field
     */
    protected String pattern;

    public DateField() {
    	
    }
    
    /**
     * Constructor that takes values for the begin and end precsion and the value of the 
     * date in a DatetimeRPC object
     * @param begin
     * @param end
     * @param val
     */
    public DateField(byte begin, byte end, Date val){
        setBegin(begin);
        setEnd(end);
        setValue(Datetime.getInstance(begin,end,val));
    }
    
    /**
     * This method is called by the FormRPC and will check to see if the value set is valid.
     */
    public void validate() {
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
    	StringBuffer sb = new StringBuffer();
    	for (int j = 0; j < qField.parameter.size(); j++) {
    		if(j > 0)
    			sb.append(" ");
    		if(!qField.getComparator().get(j).equals("=") && !qField.getComparator().get(j).equals("between ") || qField.getParameter().get(j).equalsIgnoreCase("null"))
    			sb.append(qField.getComparator().get(j)+" ");

    		String param = qField.getParameter().get(j);
    		String[] dates = param.split("\\.\\.");
    		for(int i = 0; i < dates.length; i++) {

    			Date date = null;
    			if(!dates[i].equalsIgnoreCase("null")){
    				try {
    					if(begin > 2){
    						String[] time = dates[i].split(":");
    						if(time.length == 3)
    							date = new Date(0,11,31,Integer.parseInt(time[0]),Integer.parseInt(time[1]),Integer.parseInt(time[2])); 	
    						else
    							date = new Date(0,11,31,Integer.parseInt(time[0]),Integer.parseInt(time[1]));
    					}else{
    						dates[i] = dates[i].replaceAll("-", "/");
    						date = new Date(dates[i]);
    					}
    					sb.append(DateTimeFormat.getFormat(pattern).format(date));
    					removeException(Messages.get().exc_invalidDate());
    				}catch(Exception e) {
    					valid = false;
    					addException(new Exception(Messages.get().exc_invalidDate()));
    					return;
    				}
    			}
    			if(i == 0  && ((dates.length == 1 && param.indexOf("..") > -1) || dates.length == 2))
    				sb.append("..");
    		}
    		if(qField.getLogical() != null && j < qField.getLogical().size())
    			sb.append(" "+qField.getLogical().get(j)+" ");
    	}
    	if (value != null && !isInRange()) {
    		valid = false;
    	}
    	if(valid) {
    		queryString = sb.toString();
    	}
    }

   /**
    * This method will return the value of the required flag
    */
    public boolean isRequired() {
        // TODO Auto-generated method stub
        return required;
    }

   /**
    * This mehtod will determine if the date set is in the specified Date range for this field
    */
    public boolean isInRange()  {
        // TODO Auto-generated method stub
    	Date today = new Date();
        if (min != null && value.before(Datetime.getInstance().add(-min.intValue()).getDate())) {
        	valid = false;
            addException(new Exception(Messages.get().exc_fieldPast()));
        }else
        	removeException(Messages.get().exc_fieldPast());
        if (max != null && value.after(Datetime.getInstance()
                                                  .add(max.intValue()).getDate())) {
        	valid = false;
            addException(new Exception(Messages.get().exc_fieldFuture()));
        }else{
        	removeException(Messages.get().exc_fieldFuture());
        }
        return true;
    }

    /**
     * Returns the value of this field as a String using the specified date pattern
     */
    public String toString() {
        return format();
    }

    /**
     * Sets the begin precision of this date field
     * @param begin
     */
    public void setBegin(byte begin) {
        this.begin = begin;
    }

    /**
     * Sets the end precision of this date field
     * @param end
     */
    public void setEnd(byte end) {
       this.end = end;
    }
    
    /**
     * Sets the minimum allowed date for this field represented by 
     * days from current date
     */
    public void setMin(Object min) {
        this.min = (Integer)min;
    }

    /**
     * Sets the maximum allowed date for this field represented by 
     * days from current date. 
     */
    public void setMax(Object max) {
        this.max = (Integer)max;
    }

    /**
     * Returns the begin precision value for this field.
     * @return
     */
    public byte getBegin() {
        return begin;
    }

    /**
     * Returns the end precision value for this field.
     * @return
     */
    public byte getEnd() {
        return end;
    }

    /**
     * Creates a new DataField object and set it values from this
     * object 
     */
    public Object clone() {
        DateField obj = new DateField();
        obj.setMax(max);
        obj.setMin(min);
        obj.value = value;
        obj.setBegin(begin);
        obj.setEnd(end);
        return obj;
    }
    
    /**
     * Formats the string representation of this fields value using the set pattern
     */
    public String format() {
    	if(queryMode){
    		if(queryString == null)
    			return "";
    		return queryString;
    	}
        if(value == null)
            return "";
        return DateTimeFormat.getFormat(getPattern()).format(value.getDate());
    }
    
    private String getPattern() {
        if(pattern != null)	
            return pattern;
        else if(begin == Datetime.YEAR && end == Datetime.DAY) 
        	return "yyyy-MM-dd";
        else if(begin == Datetime.YEAR && end == Datetime.MINUTE)
        	return "yyyy-MM-dd HH:mm";
        else if(begin == Datetime.YEAR && end == Datetime.SECOND)
        	return "yyyy-MM-dd HH:mm:ss";
        else if(begin == Datetime.HOUR && end == Datetime.MINUTE)
        	return "HH:mm";
        else
        	return "yyyy-MM-dd";
    }
    
    /**
     * Sets the string that represents the pattern to be used when formatting this fields value
     * for display
     */
    public void setFormat(String pattern) {
        this.pattern = pattern;
    }
        
    /**
     * Sets the value of the field from the passed string representation of this fields value.
     * If a pattern is set then the date must be passed in the pattern format to be valid.
     */
    public void setStringValue(String val) {
        if(queryMode) {
        	queryString = val;
        	validateQuery();
        }else
        	setValue(getValueFromString(val));
    }
    
    public Datetime getValueFromString(String val) {
        valid = true;
        if(queryMode) {
        	queryString = val;
        	validateQuery();
        }
        Date date = null;
        if(val != null && !val.equals("")) {
        	try {
        		if(begin > 2){
        			String[] time = val.split(":");
        			if(time.length == 3)
        				date = new Date(0,11,31,Integer.parseInt(time[0]),Integer.parseInt(time[1]),Integer.parseInt(time[2]));
        			else
        				date = new Date(0,11,31,Integer.parseInt(time[0]),Integer.parseInt(time[1]));
        		}else
        			date = DateTimeFormat.getFormat(getPattern()).parseStrict(val);
        	}catch(Exception e) {
        		valid = false;
        		addException(new Exception(Messages.get().exc_invalidDate()));
        	}
        }
        return Datetime.getInstance(begin, end, date);
    }
    
	public void setValue(Datetime value, boolean fireEvents) {
		if(queryMode){
			queryString = getString(value);
			validateQuery();
		}else {
		    if(DataBaseUtil.isDifferent(this.value, value)) {
		        this.value = value;
		        if(fireEvents)
		            ValueChangeEvent.fire(this, getValue());
		    }
		}
		
	}
    
	public void checkValue(HasField wid) {
		clearExceptions(wid);
		if(queryMode){
			if(wid instanceof CalendarLookUp) {
				queryString = ((CalendarLookUp)wid).getStringValue();
			}else if(wid instanceof TextBox) {
				queryString = ((TextBox)wid).getText();
			}
			if(queryString != null && !queryString.equals(""))
				validateQuery();
			else
				queryString = null;
			if(wid instanceof CalendarLookUp) {
				((CalendarLookUp)wid).textbox.setText(queryString);
			}else if(wid instanceof TextBox) {
				((TextBox)wid).setText(queryString);
			}
		}else{
			if(wid instanceof CalendarLookUp) 
				setStringValue(((CalendarLookUp)wid).getStringValue());
			else if(wid instanceof TextBox) {
				setStringValue(((TextBox)wid).getText());
			}
			validate();
		}
		if(!valid){
			drawExceptions(wid);
		}
	}
    
}
