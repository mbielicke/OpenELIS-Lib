package org.openelis.gwt.common.data.deprecated;

import java.io.Serializable;

@Deprecated
public class Selection<Key> implements Serializable {

    private static final long serialVersionUID = 1L;
    public Key key;
    public String display;

    public Selection() {

    }

    public Selection(Key key, String display) {
        this.key = key;
        this.display = display;
    }

    public Object clone() {
        return new Selection(key,display);
    }

}
