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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Stack;

import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.table.TableDataCell;

public class TreeRenderer {
    
    private TreeWidget controller;
    public ArrayList<TreeRow> rows = new ArrayList<TreeRow>();
    
    public TreeRenderer(TreeWidget controller){
        this.controller = controller;
    }
    
    public void createRow(int i) {
        controller.view.table.setWidget(i, 0, new Label());
        controller.view.table.getFlexCellFormatter().setHeight(i, 0, controller.cellHeight+"px");
        TreeRow row = new TreeRow(controller.view.table.getRowFormatter().getElement(i));
        row.addMouseOverHandler(controller);
        row.addMouseOutHandler(controller);
        row.index = i;
        if(controller.dragController != null)
            controller.dragController.makeDraggable(row);
        rows.add(row);
    }
    
    public void load(int pos) {
        controller.modelIndexList = new int[controller.maxRows];
        int ScrollHeight = (controller.shownRows()*controller.cellHeight)+controller.maxRows*3;
        int testStart = new Double(Math.ceil(((double)(controller.maxRows*controller.cellHeight+(controller.maxRows*controller.cellSpacing)+(controller.maxRows*3)+controller.cellSpacing))/(controller.cellHeight))).intValue();
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
        }
        if(controller.shownRows() < controller.maxRows){
            tRows = controller.shownRows();
        }
        if(controller.view.table.getRowCount() > tRows){
            int count = controller.view.table.getRowCount();
            for(int i = count -1; i > tRows -1; i--){
                controller.view.table.removeRow(i);
                rows.remove(i);
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
        TreeDataItem row = controller.getRow(modelIndex);
        rows.get(index).modelIndex = modelIndex;
        rows.get(index).item = row;
        if(controller.view.table.getRowCount() -1 >= index){
            int numCells = controller.view.table.getCellCount(index);
            controller.view.table.removeCells(index, 1, numCells - 1);
        }
        ArrayList<TreeColumn> columns = controller.columns.get(controller.getRow(modelIndex).leafType);
        for (int i = 0; i < row.cells.size(); i++) {
        	Widget wid = columns.get(i).getDisplayWidget(row.cells.get(i));
        	if(i == 0){
        		ItemGrid ig =  createItem(row);
        		ig.setWidth(columns.get(i).getCurrentWidth());
        		ig.setWidget(wid);
        		ig.rowIndex = index;
        		controller.view.table.setWidget(index, i, ig);
        	}else{
        		controller.view.table.setWidget(index, i, wid);
        	}
        	controller.view.table.getCellFormatter().setStyleName(index,i, "TreeCell");
            if(controller.isSelected(modelIndex))
                rows.get(index).addStyleName(controller.view.selectedStyle);
            else
                rows.get(index).removeStyleName(controller.view.selectedStyle);
            if(controller.isEnabled(modelIndex)) 
                rows.get(index).removeStyleName(controller.view.disabledStyle);
            else
                rows.get(index).addStyleName(controller.view.disabledStyle);
        }
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
        if(loadStart+rowsPer > numRows){
            loadStart = loadStart - ((loadStart+rowsPer) - numRows);
        }
        for(int i = 0; i < rowsPer; i++){
            while(loadStart+i < controller.numRows() && !controller.getRow(loadStart+i).shown)
                loadStart++;
            if(loadStart+i < controller.numRows()){
                loadRow(i,loadStart+i);
            }
        }

    }
    
    public void setCellEditor(int row, int col) {
    	TreeColumn column = controller.columns.get(controller.getRow(controller.modelIndexList[row]).leafType).get(col);
        controller.editingCell = (Widget)column.getWidgetEditor((TableDataCell)controller.getCell(controller.modelIndexList[row],col));
        if(col == 0)
        	((ItemGrid)controller.view.table.getWidget(row, col)).setWidget(controller.editingCell);
        else
        	controller.view.table.setWidget(row, col, controller.editingCell);
        if(controller.editingCell instanceof Focusable)
        	((Focusable)controller.editingCell).setFocus(true);
    }
    
    public void setCellDisplay(int row, int col) {
    	TreeColumn column = controller.columns.get(controller.getRow(controller.modelIndexList[row]).leafType).get(col);
    	if(col == 0)
    		((ItemGrid)controller.view.table.getWidget(row, col)).setWidget(column.getDisplayWidget(((TableDataCell)controller.getCell(controller.modelIndexList[row],col))));
    	else
    		controller.view.table.setWidget(row, col, column.getDisplayWidget((TableDataCell)controller.getCell(row,col)));
    }

    public boolean stopEditing() {
        if(controller.editingCell != null){
        	Object currVal = controller.rows.get(controller.activeRow).cells.get(controller.activeCell).getValue();
	        if(controller.editingCell instanceof Focusable)
    	    	((Focusable)controller.editingCell).setFocus(false);
	        Object newVal = null;
        	if(controller.editingCell instanceof AutoComplete){
        		newVal = ((AutoComplete)controller.editingCell).getSelection();
        	}else
        		newVal = ((HasField)controller.editingCell).getFieldValue();

        	if(controller.editingCell instanceof HasField)
	        	controller.getRow(controller.modelIndexList[controller.activeRow]).cells.get(controller.activeCell).errors = ((HasField)controller.editingCell).getErrors();
        	controller.rows.get(controller.activeRow).cells.get(controller.activeCell).setValue(newVal);
        	setCellDisplay(controller.activeRow,controller.activeCell);
        	controller.editingCell = null;
            return (currVal == null && newVal != null) || (currVal != null && !currVal.equals(newVal));
        }
        return false;
    }

    public void rowUnselected(int row) {
        if(row == -1){
            for(int i = 0; i < controller.view.table.getRowCount() ; i++) {
                controller.view.table.getRowFormatter().removeStyleName(i,controller.view.selectedStyle);
            }
        }else
            controller.view.table.getRowFormatter().removeStyleName(controller.activeRow, controller.view.selectedStyle);
        
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
    	if(row > -1 && row < rows.size())
    		rows.get(row).addStyleName(controller.view.selectedStyle);
    }

    public ArrayList<TreeRow> getRows() {
        return rows;
    }

    public class ItemGrid extends Grid implements ClickHandler {
        
        public int clickCell;
        public int rowIndex;
        public int widgetIndex;
        public int width;
        
        public ItemGrid(int rows, int cols) {
            super(rows,cols);
            setCellPadding(0);
            setCellSpacing(0);
            addClickHandler(this);
            DOM.setStyleAttribute(getElement(), "overflow", "hidden");
        }

        public void onClick(ClickEvent event) {
            if(((Grid)event.getSource()).getCellForEvent(event).getCellIndex() == clickCell){
                controller.toggle(controller.modelIndexList[rowIndex]);
                event.stopPropagation();
            }
        }
        
        public void setWidget(Widget wid){
        	int widAdj = width - (widgetIndex)*18;
        	wid.setWidth(widAdj+"px");
        	setWidget(0, widgetIndex, wid);
        }
        
        public void setWidth(int width) {
        	this.width = width;
        	setWidth(width+"px");
        }
        
    }
    
    public ItemGrid createItem(final TreeDataItem drow) {
    	ItemGrid editorGrid = new ItemGrid(1,2+drow.depth);    
    	try {
    		for(int j = 0; j < editorGrid.getColumnCount(); j++) {
    			if(j < editorGrid.getColumnCount() -1)
    				editorGrid.getCellFormatter().setWidth(0,j,"18px");
    			if(j == 0)
    				editorGrid.getCellFormatter().setHeight(0,j,"18px");

    			editorGrid.getCellFormatter().addStyleName(0,j,"treeungrouped");
    			if(j == editorGrid.getColumnCount() -2){
    				if(!drow.mightHaveChildren()) {
    					editorGrid.getCellFormatter().setStyleName(0,j,"");
    					editorGrid.clickCell = -1;
    				}
    				if(drow.open && drow.mightHaveChildren())
    					editorGrid.getCellFormatter().setStyleName(0,j,"treeOpenImage");
    				else if(drow.mightHaveChildren())
    					editorGrid.getCellFormatter().setStyleName(0,j,"treeClosedImage");
    				else if(j > 0){

    					if(drow.childIndex == drow.parent.getItems().size()-1)
    						editorGrid.getCellFormatter().setStyleName(0,j,"treeLImage");
    					else
    						editorGrid.getCellFormatter().setStyleName(0,j,"treeTImage");
    				}
    				if(drow.mightHaveChildren()){
    					editorGrid.clickCell = j;
    				}
    			}
    		}
    	}catch(Exception e){
    		Window.alert(e.getMessage());
    	}
    	try {
    		if(drow.depth > 1) {
    			Stack<TreeDataItem> levels = new Stack<TreeDataItem>();
    			levels.push(drow.parent);
    			while(levels.peek().depth > 1){
    				levels.push(levels.peek().parent);
    			}
    			for(TreeDataItem item : levels){
    				if(item.childIndex < item.parent.getItems().size() -1){//item.parent.getItems().indexOf(item) < item.parent.getItems().size() -1){
    					editorGrid.getCellFormatter().setStyleName(0,item.depth,"treeIImage");
    				}
    			}

    		}
    	}catch(Exception  e){
    		Window.alert("parent stack "+e.getMessage());
    	}
    	editorGrid.widgetIndex = editorGrid.getColumnCount() - 1;
    	editorGrid.addStyleName("TreeTableLeftTree");
    	DOM.setStyleAttribute(editorGrid.getRowFormatter().getElement(0), "background", "none");
    	return editorGrid;
    }

}
