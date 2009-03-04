/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
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
    
    public void removeHidden(String name) {
        hidden.remove(name);
    }

    public AbstractField getHidden(String name) {
        return hidden.get(name);
    }

    public boolean show() {
        return show;
    }
}
