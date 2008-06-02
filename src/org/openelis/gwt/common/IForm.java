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
    // Operation Values
    final static int DISPLAY_NEW = -1;
    final static int DISPLAY = 0;
    final static int INSERT = 1;
    final static int UPDATE = 2;
    final static int COMPLETE = 3;
    final static int CANCEL = 4;
    final static int DISPLAY_UPDATE = 5;
    final static int DISPLAY_VERSION = 6;
    final static int QUERY = 7;
     
    public enum Status {valid,invalid}
    // Status Values
    
    final static String[] opNames = {"Display","Insert","Update","Complete","Cancel","Display Update","Display Version","Query"};

    public Object getFieldValue(String key);

    public Vector getFieldValues(String Key);

    public boolean validate();

    // public void populate(IRequest req);
    public void setFieldValue(String key, Object value);

    public void setFieldMap(HashMap<String,AbstractField> fields);

    public void reset();
}
