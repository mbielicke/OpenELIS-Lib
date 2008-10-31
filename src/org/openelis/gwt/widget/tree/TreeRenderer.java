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
package org.openelis.gwt.widget.tree;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.table.TableView;
import org.openelis.gwt.widget.tree.event.SourcesTreeModelEvents;
import org.openelis.gwt.widget.tree.event.SourcesTreeWidgetEvents;
import org.openelis.gwt.widget.tree.event.TreeModelListener;
import org.openelis.gwt.widget.tree.event.TreeWidgetListener;

import java.util.ArrayList;

public class TreeRenderer implements TreeRendererInt, TreeModelListener, TreeWidgetListener {
    
    private TreeWidget controller;
    public ArrayList<TreeRow> rows = new ArrayList<TreeRow>();
    
    public TreeRenderer(TreeWidget controller){
        this.controller = controller;
    }
    
    public void createRow(int i) {
        TreeColumnInt column = controller.columns.get(0);
        TableTree item = new TableTree();
        //item.addDropListener(controller.drag);
        item.enabled = controller.enabled;
        ((SimplePanel)item).setWidth((column.getCurrentWidth())+ "px");
        ((SimplePanel)item).setHeight((controller.cellHeight+"px"));
        item.setCellWidth(column.getCurrentWidth());
        item.setRowIndex(i);
        item.addCommandListener(controller);
        controller.view.table.setWidget(i, 0, item);
        TreeRow row = new TreeRow(controller.view.table.getRowFormatter().getElement(i),true);
        row.addDropListener(controller.drop);
        row.addMouseListener(controller.mouseHandler);
        row.index = i;
        rows.add(row);
    }
    
    public void load(int pos) {
        controller.modelIndexList = new int[controller.maxRows];
        int ScrollHeight = (controller.model.shownRows()*controller.cellHeight)+controller.maxRows*2;
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
        }
        if(controller.model.shownRows() < controller.maxRows){
            tRows = controller.model.shownRows();
        }
        if(controller.view.table.getRowCount() > tRows){
            int count = controller.view.table.getRowCount();
            for(int i = count -1; i > tRows -1; i--){
                controller.view.table.removeRow(i);
            }
        }else if(controller.view.table.getRowCount() < tRows){
            int count = controller.view.table.getRowCount();
            for(int i = count; i < tRows; i++){
                createRow(i);
            }
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
        TreeDataItem row = (TreeDataItem)controller.model.getRow(modelIndex);
        rows.get(index).item = row;
        rows.get(index).modelIndex = modelIndex;
        if(controller.view.table.getRowCount() -1 >= index){
            int numCells = controller.view.table.getCellCount(index);
            controller.view.table.removeCells(index, 1, numCells - 1);
        }
        for (int i = 0; i < row.size(); i++) {
            TreeColumnInt column = controller.columns.get(i);
            if(i == 0){
                TableTree item = (TableTree)controller.view.table.getWidget(index, i);
                item.editor = (TableCellWidget)column.getWidgetInstance(row.leafType);
                
                item.setField(row);
                item.setDisplay();
            }else{
                TableCellWidget wid = (TableCellWidget)column.getWidgetInstance(row.leafType);
                controller.view.table.setWidget(index,i,(Widget)wid);
                controller.columns.get(i).loadWidget(controller.view.table.getWidget(index, i),row.get(i));
                controller.view.table.getFlexCellFormatter().addStyleName(index,
                                                                          i,
                                                                          TableView.cellStyle);
            }
            controller.view.table.getFlexCellFormatter().addStyleName(index,
                                                  i,
                                                  TableView.cellStyle);
            controller.view.table.getFlexCellFormatter()
                          .setHorizontalAlignment(index, i, column.getAlign());

            controller.view.table.getFlexCellFormatter().setWidth(index, i, column.getCurrentWidth() + "px");
            controller.view.table.getFlexCellFormatter().setHeight(index, i, controller.cellHeight+"px");
            
        }
        if(controller.model.isSelected(modelIndex))
            controller.view.table.getRowFormatter().addStyleName(index, controller.view.selectedStyle);
        else
            controller.view.table.getRowFormatter().removeStyleName(index,controller.view.selectedStyle);
        if(controller.model.isEnabled(modelIndex)) 
            controller.view.table.getRowFormatter().removeStyleName(index, controller.view.disabledStyle);
        else
            controller.view.table.getRowFormatter().addStyleName(index,controller.view.disabledStyle);
                
    }
    

    
    public void scrollLoad(int scrollPos){
        if(controller.editingCell != null){
            controller.columns.get(controller.activeCell).saveValue((Widget)controller.editingCell);
            stopEditing(null,controller.activeRow,controller.activeCell);
        }
        int rowsPer = controller.maxRows;
        if(controller.maxRows > controller.model.shownRows()){
            rowsPer = controller.model.shownRows();
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
        if(loadStart+rowsPer > numRows){
            loadStart = loadStart - ((loadStart+rowsPer) - numRows);
        }
        for(int i = 0; i < rowsPer; i++){
            while(loadStart+i < controller.model.numRows() && !controller.model.getRow(loadStart+i).shown)
                loadStart++;
            if(loadStart+i < controller.model.numRows()){
                loadRow(i,loadStart+i);
            }
        }

    }
    
    public void setCellEditor(int row, int col) {
            TableCellWidget cell =  (TableCellWidget)controller.view.table.getWidget(row, col);
            if(col > 0){
                controller.columns.get(col).setWidgetEditor((Widget)cell);
                ((SimplePanel)cell).getWidget().addStyleName(controller.view.widgetStyle);
                ((SimplePanel)cell).getWidget().addStyleName("Enabled");
                controller.editingCell = cell;
            }else{
                ((TableTree)cell).editor.setEditor();
                ((TableTree)cell).editor.setFocus(true);
                ((SimplePanel)((TableTree)cell).editor).getWidget().addStyleName(controller.view.widgetStyle);
                ((SimplePanel)((TableTree)cell).editor).getWidget().addStyleName("Enabled");
                controller.editingCell = ((TableTree)cell).editor;
            }
    }
    
    public void setCellDisplay(int row, int col) {
        if(col > 0)
            controller.columns.get(col).setWidgetDisplay(controller.view.table.getWidget(row, col));
        else
            ((TableTree)controller.view.table.getWidget(row, col)).editor.setDisplay();
    }

    public void stopEditing(SourcesTreeWidgetEvents sender, int row, int col) {
        if(controller.editingCell != null){
            if(col > 0)
                controller.columns.get(controller.activeCell).saveValue((Widget)controller.editingCell);
            else
                controller.editingCell.saveValue();
            setCellDisplay(row,col);
            controller.editingCell = null;
        }
    }

    public void hideRows(SourcesTreeWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void removeRows(SourcesTreeWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void selected(SourcesTreeWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void startedEditing(SourcesTreeWidgetEvents sender, int row, int col) {
        setCellEditor(row,col);
    }

    public void rowUnselected(SourcesTreeModelEvents sender, int row) {
        if(row == -1){
            for(int i = 0; i < controller.view.table.getRowCount() ; i++) {
                controller.view.table.getRowFormatter().removeStyleName(i,controller.view.selectedStyle);
            }
        }else
            controller.view.table.getRowFormatter().removeStyleName(controller.activeRow, controller.view.selectedStyle);
        
    }

    public void cellUpdated(SourcesTreeModelEvents sender, int row, int cell) {
        setCellDisplay(row,cell);
    }

    public void dataChanged(SourcesTreeModelEvents sender) {
        load(controller.view.scrollBar.getScrollPosition());
    }

    public void rowDeleted(SourcesTreeModelEvents sender, int row) {
        load(0);        
    }

    public void rowAdded(SourcesTreeModelEvents sender, int row) {
        load(0);        
    }

    public void rowUpdated(SourcesTreeModelEvents sender, int row) {
        load(0);
    }

    public void rowSelectd(SourcesTreeModelEvents sender, int row) {
        if(controller.activeRow < 0)
            return;
        controller.view.table.getRowFormatter().addStyleName(controller.activeRow, controller.view.selectedStyle);
        for(int i = 0; i < controller.view.table.getCellCount(controller.activeRow); i++){
            if(controller.view.table.getCellFormatter().getStyleName(controller.activeRow,i).indexOf("disabled") > -1){
                controller.view.table.getWidget(controller.activeRow,i).addStyleName("disabled");
            }
        }
        
    }

    public void selected(SourcesTreeWidgetEvents sender, int rows) {
        // TODO Auto-generated method stub
        
    }

    public void unselected(SourcesTreeWidgetEvents sender, int rows) {
        // TODO Auto-generated method stub
        
    }

    public void unload(SourcesTreeModelEvents sender) {
        // TODO Auto-generated method stub
        
    }

    public void rowClosed(SourcesTreeModelEvents sender, int row, TreeDataItem item) {
        // TODO Auto-generated method stub
        
    }

    public void rowOpened(SourcesTreeModelEvents sender, int row, TreeDataItem item) {
        // TODO Auto-generated method stub
        
    }

    public void finishedEditing(SourcesTreeWidgetEvents sender, int row, int col) {
        // TODO Auto-generated method stub
        
    }

    public ArrayList<TreeRow> getRows() {
        return rows;
    }


}
