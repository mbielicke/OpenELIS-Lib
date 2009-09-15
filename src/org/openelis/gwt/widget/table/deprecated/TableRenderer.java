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
package org.openelis.gwt.widget.table.deprecated;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.deprecated.FieldType;
import org.openelis.gwt.common.data.deprecated.TableDataRow;
import org.openelis.gwt.widget.table.deprecated.event.SourcesTableModelEvents;
import org.openelis.gwt.widget.table.deprecated.event.SourcesTableWidgetEvents;
import org.openelis.gwt.widget.table.deprecated.event.TableModelListener;
import org.openelis.gwt.widget.table.deprecated.event.TableWidgetListener;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class TableRenderer implements TableRendererInt, TableModelListener, TableWidgetListener, ClickListener {
    
    private TableWidget controller;
    public ArrayList<TableRow> rows = new ArrayList<TableRow>();
    
    public TableRenderer(TableWidget controller){
        this.controller = controller;
    }
    
    public void createRow(int i) {
        int j = 0;
        for(TableColumnInt column : controller.columns) {
            Widget wid = column.getWidgetInstance();
            if(wid instanceof SourcesClickEvents) {
                ((SourcesClickEvents)wid).addClickListener(this);
            }
            controller.view.table.setWidget(i,j,wid);
            if(wid instanceof TableCellInputWidget) {
                ((TableCellInputWidget)wid).rowIndex = i;
            }
            
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
        row.addMouseOverHandler(controller.mouseHandler);
        row.addMouseOutHandler(controller.mouseHandler);
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
        int ScrollHeight = (controller.model.shownRows()*controller.cellHeight)+(controller.maxRows*2);
        if(controller.model.isAutoAdd()){
            ScrollHeight += controller.cellHeight;
        }
        int testStart = new Double(Math.ceil(((double)(controller.maxRows*controller.cellHeight+(controller.maxRows*controller.cellSpacing)+(controller.maxRows*2)+controller.cellSpacing))/(controller.cellHeight))).intValue();
        if(testStart < controller.model.shownRows() - controller.maxRows)
            ScrollHeight += controller.cellHeight;
        controller.view.setScrollHeight(ScrollHeight);
        controller.view.scrollBar.setScrollPosition(pos);
        int tRows = controller.maxRows;
        if(controller.model.shownRows() == 0){
            int count = controller.view.table.getRowCount();
            for(int i = 0; i < count; i++){
                controller.view.table.removeRow(0);
                rows.remove(0);
            }
            if(!controller.model.getAutoAdd())
                return;
        }
        if(controller.model.shownRows() < controller.maxRows){
            tRows = controller.model.shownRows();
            if(controller.model.getAutoAdd()){
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
        if(controller.model.getAutoAdd() && controller.view.table.getRowCount() == 0){
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
        org.openelis.gwt.common.data.deprecated.TableDataRow<Object> row = controller.model.getRow(modelIndex);
        rows.get(index).modelIndex = modelIndex;
        rows.get(index).row = row;
        List<FieldType> cells = row.getCells();
        for (int i = 0; i < cells.size(); i++) {
            controller.columns.get(i).loadWidget(controller.view.table.getWidget(index, i),cells.get(i));
            
            //if(tCell instanceof TableMultiple && manager != null){
              //  manager.setMultiple(model.indexOf(row),i,this);
            //}
            /*if(tCell instanceof TableCheck){
                if(controller.model.canEdit(index,i)){
                    tCell.enable(true);
                }else{
                    tCell.enable(false);
                }
            }
            */
            //tCell.setField(row.getObject(i));
           // setCellDisplay(index,i);
            //if(controller.showRows){
              //  ((Label)controller.view.rows.getWidget(index,0)).setText(String.valueOf(controller.model.indexOf(row)+1));
           // }
        }
        rows.get(index).setStyleName("");
        if(index % 2 == 1 && !controller.isDropdown){
            rows.get(index).addStyleName("AltTableRow");
        }
        if(controller.model.isSelected(modelIndex))
            rows.get(index).addStyleName(controller.view.selectedStyle);
        if(!controller.model.isEnabled(modelIndex)) 
            rows.get(index).addStyleName(controller.view.disabledStyle);
        if(row.style != null && !row.style.equals("")){
         	rows.get(index).addStyleName(row.style);
        }
            
    }
    
    public void scrollLoad(int scrollPos){
        if(controller.editingCell != null){
            controller.columns.get(controller.activeCell).saveValue((Widget)controller.editingCell);
            stopEditing(null,controller.activeRow,controller.activeCell);
        }
        int rowsPer = controller.maxRows;
        if(controller.maxRows > controller.model.shownRows()){
            rowsPer = controller.model.shownRows();
            if(controller.model.getAutoAdd())
                rowsPer++;
        }
        int loadStart = new Double(Math.ceil(((double)scrollPos)/(controller.cellHeight))).intValue();
        if(controller.model.numRows() != controller.model.shownRows()){
            int start = 0;
            int i = 0;
            while(start < loadStart && i < controller.model.numRows() -1){
                if(controller.model.getRow(i).shown)
                    start++;
                i++;
            }
             loadStart = i;   
        }
        int numRows = controller.model.numRows();
        if(controller.model.getAutoAdd())
            numRows++;
        if(loadStart+rowsPer > numRows){
            loadStart = loadStart - ((loadStart+rowsPer) - numRows);
            if(controller.model.getAutoAdd())
                loadStart++;
        }
        for(int i = 0; i < rowsPer; i++){
            while(loadStart+i < controller.model.numRows() && !controller.model.getRow(loadStart+i).shown)
                loadStart++;
            if(loadStart+i < controller.model.numRows())
                loadRow(i,loadStart+i);
            else{
                controller.model.setAutoAddRow((TableDataRow<Object>)controller.model.createRow());
                loadRow(i,controller.model.numRows());
            }
        }

    }
    
    public void setCellEditor(int row, int col) {
            TableCellWidget cell =  (TableCellWidget)controller.view.table.getWidget(row, col);
            controller.editingCell = cell;
            controller.columns.get(col).setWidgetEditor((Widget)cell);
            ((SimplePanel)cell).getWidget().addStyleName(controller.view.widgetStyle);
            ((SimplePanel)cell).getWidget().addStyleName("Enabled");
            
    }
    
    public void setCellDisplay(int row, int col) {
        controller.columns.get(col).setWidgetDisplay(controller.view.table.getWidget(row, col));        
    }

    public void stopEditing(SourcesTableWidgetEvents sender, int row, int col) {
        if(controller.editingCell != null){
            controller.columns.get(controller.activeCell).saveValue((Widget)controller.editingCell);
            setCellDisplay(row,col);
            controller.editingCell = null;
        }
    }

    public void hideRows(SourcesTableWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void removeRows(SourcesTableWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void selected(SourcesTableWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void startEditing(SourcesTableWidgetEvents sender, int row, int col) {
        setCellEditor(row,col);
    }

    public void rowUnselected(SourcesTableModelEvents sender, int row) {
        if(row == -1){
            for(int i = 0; i < controller.view.table.getRowCount() ; i++) {
                controller.view.table.getRowFormatter().removeStyleName(i,controller.view.selectedStyle);
            }
        }else
            controller.view.table.getRowFormatter().removeStyleName(row, controller.view.selectedStyle);
        
    }

    public void cellUpdated(SourcesTableModelEvents sender, int row, int cell) {
            for(TableRow trow : rows) {
                if(trow.modelIndex == row){
                    setCellDisplay(trow.index,cell);
                    break;
                }
            }
           
    }

    public void dataChanged(SourcesTableModelEvents sender) {
        load(0);
    }

    public void rowDeleted(SourcesTableModelEvents sender, int row) {
        load(controller.view.scrollBar.getScrollPosition());        
    }

    public void rowAdded(SourcesTableModelEvents sender, int row) {
        load(controller.view.scrollBar.getScrollPosition());        
    }

    public void rowUpdated(SourcesTableModelEvents sender, int row) {
        load(controller.view.scrollBar.getScrollPosition());
    }

    public void rowSelected(SourcesTableModelEvents sender, int row) {
        if(controller.activeRow < 0)
            return;
        controller.view.table.getRowFormatter().addStyleName(controller.activeRow, controller.view.selectedStyle);
        for(int i = 0; i < controller.view.table.getCellCount(controller.activeRow); i++){
            if(controller.view.table.getCellFormatter().getStyleName(controller.activeRow,i).indexOf("disabled") > -1){
                controller.view.table.getWidget(controller.activeRow,i).addStyleName("disabled");
            }
        }
        
    }

    public void selected(SourcesTableWidgetEvents sender, int rows) {
        // TODO Auto-generated method stub
        
    }

    public void unselected(SourcesTableWidgetEvents sender, int rows) {
        // TODO Auto-generated method stub
        
    }

    public void unload(SourcesTableModelEvents sender) {
        // TODO Auto-generated method stub
        
    }

    public void finishedEditing(SourcesTableWidgetEvents sender, int row, int col) {
        // TODO Auto-generated method stub
        
    }
    
    public ArrayList<TableRow> getRows() {
        return rows;
    }

    public void onClick(Widget sender) {
        controller.finishEditing();
    }

}