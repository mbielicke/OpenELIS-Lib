/*
 * Created on Mar 22, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;

import java.util.Vector;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface IField {
    public boolean isValid();

    public boolean isRequired();

    public boolean isInRange();

    public void addError(String err);

    public String[] getErrors();

    public void setValue(Object val);

    public Object getValue();

    public Vector getValues();

    public void setMin(Object min);

    public void setMax(Object max);

    public void setKey(Object key);

    public Object getKey();

    public void addOption(Object key, Object val);

    public void clearErrors();

    public String toString();

    public Object getInstance();
}
