package org.openelis.gwt.common.data;

import org.openelis.gwt.common.RPC;

public class QueryData implements RPC {

    private static final long serialVersionUID = 1L;

    public enum Type {
        STRING, INTEGER, DOUBLE, DATE
    }

    public Type   type;
    public String query;
    public String key;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
