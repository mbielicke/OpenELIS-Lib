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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.screen.ScreenBase;

import java.util.ArrayList;

public class TableMultiple extends SimplePanel implements TableCellWidget {
    
    public ArrayList<TableCellWidget> cells = new ArrayList<TableCellWidget>();
    public int active;
    DataObject field;
    public static final String TAG_NAME = "table-multiple";
    
    public TableMultiple() {
        
    }
    
    public void clear() {
        for(int i = 0; i < cells.size(); i++){
            ((TableCellWidget)cells.get(i)).clear();
        }
    }

    public void enable(boolean enabled) {
       for(int i = 0; i < cells.size(); i++){
           ((TableCellWidget)cells.get(i)).enable(enabled);
       }
        
    }

    public TableMultiple(Node node,ScreenBase screen) {
        NodeList editors = node.getChildNodes();
        for (int i = 0; i < editors.getLength(); i++) {
            if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                cells.add((TableCellWidget)ScreenBase.createCellWidget(editors.item(i),screen));
            }
        }
    }

    public TableCellWidget getNewInstance() {
        TableMultiple mult = new TableMultiple();
        for(int i = 0; i < cells.size(); i++){
            mult.cells.add(((TableCellWidget)cells.get(i)).getNewInstance());
        }
        mult.active = active;
        return mult;
    }

    public void saveValue() {
        ((TableCellWidget)cells.get(active)).setField(field);
        ((TableCellWidget)cells.get(active)).saveValue();
    }

    public void setDisplay() {
        ((TableCellWidget)cells.get(active)).setField(field);
        ((TableCellWidget)cells.get(active)).setDisplay();
        setWidget((Widget)cells.get(active));
        
    }

    public void setEditor() {
        ((TableCellWidget)cells.get(active)).setField(field);
        ((TableCellWidget)cells.get(active)).setEditor();
        setWidget((Widget)cells.get(active));
        
    }

    public void setField(DataObject field) {
       this.field = field;
        
    }
    
    public void initCells(TableModel model){

    }
    
    public Widget getWidget() {
        return ((SimplePanel)super.getWidget()).getWidget();
    }
    
    public void initCells(TableWidget controller){

    }

    public void setCellWidth(int width) {
        for(int i = 0; i < cells.size(); i++){
            ((TableCellWidget)cells.get(i)).setCellWidth(width);
        }
        
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void setFocus(boolean focused) {

    }

}
