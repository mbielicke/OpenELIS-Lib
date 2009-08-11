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
package org.openelis.gwt.widget.table.rewrite;

import java.util.ArrayList;

import org.openelis.gwt.widget.rewrite.AutoComplete;
import org.openelis.gwt.widget.HasField;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TableRenderer  {
    
    private TableWidget controller;
    public ArrayList<TableRow> rows = new ArrayList<TableRow>();
    
    public TableRenderer(TableWidget controller){
        this.controller = controller;
    }
    
    public void createRow(int i) {
        int j = 0;
        for(TableColumn column : controller.columns) {
            Widget wid = column.getDisplayWidget(new TableDataCell(null));
            controller.view.table.setWidget(i,j,wid);            
            if(controller.isDropdown)
                controller.view.table.getFlexCellFormatter().addStyleName(i,
                                                                          j,
                                                                          TableView.dropdownCellStyle);
            else
                controller.view.table.getFlexCellFormatter().addStyleName(i,
                                                                          j,
                                                                          TableView.cellStyle);
            
            controller.view.table.getFlexCellFormatter()
                          .setHorizontalAlignment(i, j, column.getAlign());

           
            controller.view.table.getFlexCellFormatter().setWidth(i, j, column.getCurrentWidth() + "px");
            controller.view.table.getFlexCellFormatter().setHeight(i, j, controller.cellHeight+"px");
            //view.table.getRowFormatter().addStyleName(i, TableView.rowStyle);
            if(controller.showRows){
                Label rowNum = new Label(String.valueOf(i+1));
                controller.view.rows.setWidget(i,0,rowNum);
                controller.view.rows.getFlexCellFormatter().setStyleName(i, 0, "RowNum");
                controller.view.rows.getFlexCellFormatter().setHeight(i,0,controller.cellHeight+"px");
            }
            j++;
        }
        TableRow  row = new TableRow(controller.view.table.getRowFormatter().getElement(i));
        row.addMouseOutHandler(controller);
        row.addMouseOverHandler(controller);
        if(controller.dragController != null)
            controller.dragController.makeDraggable(row);
        row.index = i;
        if(i % 2 == 1 && !controller.isDropdown){
            row.addStyleName("AltTableRow");
        }
        rows.add(row);
    }

    public void load(int pos) {
        controller.modelIndexList = new int[controller.maxRows];
        int ScrollHeight = (controller.shownRows()*controller.cellHeight)+(controller.maxRows*2);
        if(controller.isAutoAdd()){
            ScrollHeight += controller.cellHeight;
        }
        int testStart = new Double(Math.ceil(((double)(controller.maxRows*controller.cellHeight+(controller.maxRows*controller.cellSpacing)+(controller.maxRows*2)+controller.cellSpacing))/(controller.cellHeight))).intValue();
        if(testStart < controller.shownRows() - controller.maxRows)
            ScrollHeight += controller.cellHeight;
        controller.view.setScrollHeight(ScrollHeight);
        controller.view.scrollBar.setScrollPosition(pos);
        int tRows = controller.maxRows;
        if(controller.shownRows() == 0){
            int count = controller.view.table.getRowCount();
            for(int i = 0; i < count; i++){
                controller.view.table.removeRow(0);
                rows.remove(0);
            }
            if(!controller.getAutoAdd())
                return;
        }
        if(controller.shownRows() < controller.maxRows){
            tRows = controller.shownRows();
            if(controller.getAutoAdd()){
                tRows++;
            }
        }
        if(controller.view.table.getRowCount() > tRows){
            int count = controller.view.table.getRowCount();
            for(int i = count -1; i > tRows -1; i--){
                controller.view.table.removeRow(i);
                if(i < rows.size())
                    rows.remove(i);
            }
        }else if(controller.view.table.getRowCount() < tRows){
            int count = controller.view.table.getRowCount();
            for(int i = count; i < tRows; i++){
                createRow(i);
            }
        }
        if(controller.getAutoAdd() && controller.view.table.getRowCount() == 0){
            createRow(0);
        }
        scrollLoad(pos);

    }
    
    /**
     * This method loads the row form the model specified by the passed index
     * into the table view.
     * 
     * @param index
     */
    private void loadRow(int index, int modelIndex) {
        controller.modelIndexList[index] = modelIndex;     
        TableDataRow row = controller.getRow(modelIndex);
        rows.get(index).modelIndex = modelIndex;
        rows.get(index).row = row;
        for (int i = 0; i < row.cells.size(); i++) {
            controller.columns.get(i).loadWidget(controller.view.table.getWidget(index, i),row.cells.get(i));
        }
        rows.get(index).setStyleName("");
        if(index % 2 == 1 && !controller.isDropdown){
            rows.get(index).addStyleName("AltTableRow");
        }
        if(controller.isSelected(modelIndex))
            rows.get(index).addStyleName(controller.view.selectedStyle);
        if(!controller.isEnabled(modelIndex)) 
            rows.get(index).addStyleName(controller.view.disabledStyle);
        if(row.style != null && !row.style.equals(""))
        	rows.get(index).addStyleName(row.style);
    }
    
    public void loadRow(int index) {
    	loadRow(index,controller.modelIndexList[index]);
    }
    
    public void scrollLoad(int scrollPos){
        if(controller.editingCell != null){
            stopEditing();
            controller.activeCell = -1;
            controller.activeRow--;
        }
        int rowsPer = controller.maxRows;
        if(controller.maxRows > controller.shownRows()){
            rowsPer = controller.shownRows();
            if(controller.getAutoAdd())
                rowsPer++;
        }
        int loadStart = new Double(Math.ceil(((double)scrollPos)/(controller.cellHeight))).intValue();
        if(controller.numRows() != controller.shownRows()){
            int start = 0;
            int i = 0;
            while(start < loadStart && i < controller.numRows() -1){
                if(controller.getRow(i).shown)
                    start++;
                i++;
            }
             loadStart = i;   
        }
        int numRows = controller.numRows();
        if(controller.getAutoAdd())
            numRows++;
        if(loadStart+rowsPer > numRows){
            loadStart = loadStart - ((loadStart+rowsPer) - numRows);
            if(controller.getAutoAdd())
                loadStart++;
        }
        for(int i = 0; i < rowsPer; i++){
            while(loadStart+i < controller.numRows() && !controller.getRow(loadStart+i).shown)
                loadStart++;
            if(loadStart+i < controller.numRows())
                loadRow(i,loadStart+i);
            else{
                controller.setAutoAddRow((TableDataRow)controller.createRow());
                loadRow(i,controller.numRows());
            }
        }

    }
    
    public void setCellEditor(int row, int col) {
        controller.editingCell = (Widget)controller.columns.get(col).getWidgetEditor((TableDataCell)controller.getCell(controller.modelIndexList[row],col));
        controller.view.table.setWidget(row, col, controller.editingCell);
        if(controller.editingCell instanceof Focusable)
        	((Focusable)controller.editingCell).setFocus(true);
        //controller.editingCell.getWidget().addStyleName(controller.view.widgetStyle);
        //((SimplePanel)cell).getWidget().addStyleName("Enabled");
            
    }
    
    public void setCellDisplay(int row, int col) {
    	controller.view.table.setWidget(row, col, controller.columns.get(col).getDisplayWidget((TableDataCell)controller.getCell(controller.modelIndexList[row],col)));
    }

    public void stopEditing() {
        if(controller.editingCell != null){
        	if(controller.editingCell instanceof Focusable)
        		((Focusable)controller.editingCell).setFocus(false);
        	if(controller.editingCell instanceof AutoComplete){
        		Object[] idName = new Object[2];
        		idName[0] = ((AutoComplete)controller.editingCell).getValue();
        		idName[1] = ((AutoComplete)controller.editingCell).getTextBoxDisplay();
        		controller.setCell(controller.modelIndexList[controller.activeRow], controller.activeCell, idName);
        	}else
        		controller.setCell(controller.modelIndexList[controller.activeRow], controller.activeCell, ((HasField)controller.editingCell).getFieldValue());
        	if(controller.editingCell instanceof HasField)
        		controller.getRow(controller.modelIndexList[controller.activeRow]).cells.get(controller.activeCell).errors = ((HasField)controller.editingCell).getErrors();
            setCellDisplay(controller.activeRow,controller.activeCell);
            controller.editingCell = null;
            
        }
    }


    public void rowUnselected(int row) {
        if(row == -1){
            for(int i = 0; i < controller.view.table.getRowCount() ; i++) {
                controller.view.table.getRowFormatter().removeStyleName(i,controller.view.selectedStyle);
            }
        }else
            controller.view.table.getRowFormatter().removeStyleName(row, controller.view.selectedStyle);
    }

    public void cellUpdated(int row, int cell) {
    	 setCellDisplay(row,cell);
           
    }

    public void dataChanged(boolean keepPosition) {
    	if(keepPosition)
    		load(controller.view.scrollBar.getScrollPosition());
    	else
    		load(0);
    }


    public void rowSelected(int row) {
    	rows.get(row).addStyleName(controller.view.selectedStyle);
    }
    
    public ArrayList<TableRow> getRows() {
        return rows;
    }

}
