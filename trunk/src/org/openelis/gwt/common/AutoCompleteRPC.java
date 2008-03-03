package org.openelis.gwt.common;

import java.io.Serializable;

public class AutoCompleteRPC implements Serializable {

    private static final long serialVersionUID = 1L;
    public String category;
    public String match;
    public String[] display;
    public String[] textboxValue;
    public Integer[] id;
    public Integer value;
    public String dict_value;
    public String contact_types;
}
