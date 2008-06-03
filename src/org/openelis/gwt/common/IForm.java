/*
 * Created on Mar 22, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;

import org.openelis.gwt.common.data.AbstractField;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface IForm {
     
    public enum Status {valid,invalid}
    
    public Object getFieldValue(String key);

    public Vector getFieldValues(String Key);

    public boolean validate();

    // public void populate(IRequest req);
    public void setFieldValue(String key, Object value);

    public void setFieldMap(HashMap<String,AbstractField> fields);

    public void reset();
}
