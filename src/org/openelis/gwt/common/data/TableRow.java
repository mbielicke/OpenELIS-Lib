package org.openelis.gwt.common.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TableRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<AbstractField> columns = new ArrayList<AbstractField>();
    private HashMap<String,AbstractField> hidden = new HashMap<String,AbstractField>();
    protected boolean show = true;

    public void addColumn(AbstractField field) {
        columns.add(field);
    }

    public void setColumn(int index, AbstractField field) {
        columns.set(index, field);
    }

    public AbstractField getColumn(int index) {
        return columns.get(index);
    }

    public void removeColumn(int index) {
        columns.remove(index);
    }

    public int numColumns() {
        return columns.size();
    }

    public void addHidden(String name, AbstractField field) {
        hidden.put(name, field);
    }

    public AbstractField getHidden(String name) {
        return hidden.get(name);
    }

    public boolean show() {
        return show;
    }
}
