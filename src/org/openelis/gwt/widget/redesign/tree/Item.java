/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget.redesign.tree;

import java.util.ArrayList;

public class Item {
    
    protected ArrayList<Item> children = new ArrayList<Item>();
    
    protected Item parent;
    
    protected ArrayList<Object> cells;
    
    protected Object data;
    
    public Item() {
        cells = new ArrayList<Object>();
    }
    
    public Item(ArrayList<Object> cells) {
        this.cells = cells;
    }
    
    public Item(int size) {
        cells = new ArrayList<Object>(size);
        for(int i = 0; i < size; i++) 
            cells.add(null);
    }
    
    public Item(Object... objs) {
        cells = new ArrayList<Object>(objs.length);
        for(int i = 0; i < objs.length; i++)
            cells.add(objs[i]);
    }
    
    public int size() {
        return cells.size();
    }
    
    public ArrayList<Object> getCells() {
        return cells;
    }
    
    public void setCell(int index, Object value) {
        cells.set(index, value);
    }
    
    public Object getCell(int index) {
        return cells.get(index);
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public String getStyle(int idex) {
        return null;
    }
  
}
