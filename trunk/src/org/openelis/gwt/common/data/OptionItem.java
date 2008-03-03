package org.openelis.gwt.common.data;

import java.io.Serializable;

public class OptionItem implements Serializable {

    private static final long serialVersionUID = 1L;
    public String display;
    public String akey;
    public boolean selected;
    public String tip;

    public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return akey.equals(akey.toString());
        } else {
            return ((OptionItem)obj).akey.equals(akey);
        }
    }
}
