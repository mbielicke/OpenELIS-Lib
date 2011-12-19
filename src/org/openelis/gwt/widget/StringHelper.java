package org.openelis.gwt.widget;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

/**
 * This class is used by ScreenWidgets that implement HasValue<String> to
 * provide methods for formatting, validating and query by String values.
 * 
 * @author tschmidt
 * 
 */
public class StringHelper implements WidgetHelper<String> {


	protected String mask;
	
    /**
     * Public no arg constructor
     */
    public StringHelper() {

    }

    /**
     * Will return a new QueryData object using the passed query string and and
     * the setting the type to QueryData.Type.STRING.
     */
    public QueryData getQuery(String input) {
        // Do nothing and return if passed null or empty string
        if (input == null || "".equals(input))
            return null;

        return new QueryData(QueryData.Type.STRING,input);
    }

    /**
     * Returns null if empty string or null value passed. Otherwise will echo
     * back the input parameter.
     */
    public String getValue(String input) throws LocalizedException {

        // If null or empty string return value as null;
        if (input == null || "".equals(input)) 
            return null;

        return input;
    }

    /**
     * This method will ensure the passed query string is in the correct format
     * and that the query params are all valid double values.
     */
    public void validateQuery(String input) throws LocalizedException {
        QueryFieldUtil qField;

        // If null or empty string do nothing and return.
        if (input == null || input.equals(""))
            return;

        // Parse query and if invalid set exception and return right away.
        qField = new QueryFieldUtil();

        qField.parse(input);
    }

    /**
     * This method will return a string value for display applying any
     * formatting if needed.
     */
    public String format(String value) {

        if (value == null)
            return "";

        return value;
    }
    
    public void setMask(String mask) {
    	this.mask = mask;
    }
    
    public String applyMask(String input) {
		StringBuffer applied;
		char mc;
		int pos;
		boolean loop;
		
		if(mask == null || mask.equals(""))
			return input;
		
		applied = new StringBuffer();
		pos = 0;
		/*
		 * Loop through input applying mask chars when needed
		 */
		for(char in : input.toCharArray()) {
			if(pos >= mask.length())
				break;
			
			mc = mask.charAt(pos);
		   
			do {
		    	loop = false;
		    	switch(mc) {
		    		case '9' :					
		    			if(Character.isDigit(in)) {  
		    				applied.append(in);
		    				pos++;
		    			}
		    			break;
		    		case 'X' :
		    			if(Character.isLetterOrDigit(in)) {  
		    				applied.append(in);
		    				pos++;
		    			}
		    			break;
		    		default :
		    			applied.append(mc);
		    			pos++;
		    			if(mc != in) {
		    				mc = mask.charAt(pos);
		    				loop = true;
		    			}
		    	}
			} while(loop && pos < mask.length());
		}
		
		return applied.toString();
    }

}
