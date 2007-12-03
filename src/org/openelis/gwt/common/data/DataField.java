package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;


import java.util.Vector;

public interface DataField extends DataObject {
    
    public void setRequired(boolean required);

    public boolean isRequired();

    public void addError(String err);

    public String[] getErrors();

    public Vector getValues();

    public void setMin(Object min);

    public void setMax(Object max);

    public void setKey(Object key);

    public Object getKey();

    public void addOption(Object key, Object val);

    public void clearErrors();

    public Object getInstance(Node node);

    public boolean isValid();

    public boolean isInRange();

    public Object getInstance();
    
    public String getTip();

    public void setTip(String tip);

}
