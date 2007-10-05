package org.openelis.gwt.common;

import java.io.Serializable;

public class OptionItem implements Serializable {
    public String display;
    public String akey;
    public boolean selected;

    public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return akey.equals(akey.toString());
        } else {
            return ((OptionItem)obj).akey.equals(akey);
        }
    }
}
