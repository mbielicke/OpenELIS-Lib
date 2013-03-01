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

import java.util.ArrayList;
import java.util.Stack;

import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Field;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.table.TableDataCell;
import org.openelis.gwt.widget.table.TableDataRow;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class TreeRenderer {
    
    private TreeWidget controller;
    public ArrayList<TreeRow> rows = new ArrayList<TreeRow>();
    
    public TreeRenderer(TreeWidget controller){
        this.controller = controller;
    }
    
    public void createRow(int i) {
    	if(controller.columns.size() == 0)
    		return;
        controller.view.table.setWidget(i, 0, new Label());
        controller.view.table.getFlexCellFormatter().setHeight(i, 0, controller.cellHeight+"px");
        TreeRow row = new TreeRow(controller.view.table.getRowFormatter().getElement(i));
        row.controller = controller;
        row.addMouseOverHandler(controller);
        row.addMouseOutHandler(controller);
        row.index = i;
        if(controller.dragController != null)
            controller.dragController.makeDraggable(row);
        rows.add(row);
    }
    
    public void load(int pos) {
        controller.rowIndexList = new int[controller.maxRows];
        int ScrollHeight = (controller.shownRows*controller.cellHeight);
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
            return;
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
        controller.rowIndexList[index] = modelIndex;     
        TreeDataItem row = controller.getRow(modelIndex);
        rows.get(index).modelIndex = modelIndex;
        rows.get(index).item = row;
        if(controller.view.table.getRowCount() -1 >= index){
            int numCells = controller.view.table.getCellCount(index);
            controller.view.table.removeCells(index, 1, numCells - 1);
        }
        ArrayList<TreeColumn> columns = controller.columns.get(controller.getRow(modelIndex).leafType);
        for (int i = 0; i < row.cells.size(); i++) {
        	Widget wid = columns.get(i).getDisplayWidget(row);
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
        
            rows.get(index).removeStyleName(controller.view.disabledStyle);            
            if(!controller.isEnabled(modelIndex))  {
                rows.get(index).addStyleName(controller.view.disabledStyle);
            }
        }
    }
    
    public void loadRow(int index) {
    	loadRow(controller.treeIndex(index),index);
    }
    
    public void scrollLoad(int scrollPos){
        if(controller.activeWidget != null){
        	controller.finishEditing();
            controller.selectedCol = -1;
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
    	TreeColumn column = controller.columns.get(controller.getRow(row).leafType).get(col);
        controller.activeWidget = (Widget)column.getWidgetEditor(controller.getRow(row));
        if(controller.activeWidget instanceof AbsolutePanel)
        	controller.activeWidget = ((AbsolutePanel)controller.activeWidget).getWidget(0);
        if(col == 0)
        	((ItemGrid)controller.view.table.getWidget(controller.treeIndex(row), col)).setWidget(controller.activeWidget);
        else
        	controller.view.table.setWidget(controller.treeIndex(row), col, controller.activeWidget);
        if(controller.activeWidget instanceof Focusable)
        	((Focusable)controller.activeWidget).setFocus(true);
    }
    
    public void setCellDisplay(int row, int col) {
    	TreeColumn column = controller.columns.get(controller.getRow(row).leafType).get(col);
    	if(col == 0)
    		((ItemGrid)controller.view.table.getWidget(controller.treeIndex(row), col)).setWidget(column.getDisplayWidget(controller.getRow(row)));
    	else
    		controller.view.table.setWidget(controller.treeIndex(row), col, column.getDisplayWidget(controller.getRow(row)));
    }

    @SuppressWarnings("unchecked")
    public boolean stopEditing() {
        if(controller.activeWidget != null && !(controller.activeWidget instanceof Label)){
        	Object currVal = controller.rows.get(controller.selectedRow).cells.get(controller.selectedCol).getValue();
	        if(controller.activeWidget instanceof Focusable)
    	    	((Focusable)controller.activeWidget).setFocus(false);
	        Object newVal = null;
        	if(controller.activeWidget instanceof Dropdown) {
        		if(((Dropdown)controller.activeWidget).popup.isShowing()){
        			((Dropdown)controller.activeWidget).popup.hide(true);	
        		}
        		newVal = ((Dropdown)controller.activeWidget).getSelectionKeys();
        	}
        	if(controller.activeWidget instanceof AutoComplete){
        		if(((AutoComplete)controller.activeWidget).popup.isShowing())
        			((AutoComplete)controller.activeWidget).popup.hide(true);
        		if(((AutoComplete)controller.activeWidget).getField().queryMode)
        			newVal = ((AutoComplete)controller.activeWidget).textbox.getText();
        		else
        			newVal = ((AutoComplete)controller.activeWidget).getSelection();
        	}else if(controller.queryMode && !(controller.activeWidget instanceof Dropdown)){
        		newVal = ((HasField)controller.activeWidget).getField().queryString;
        	}else if(!controller.queryMode && controller.activeWidget instanceof TextBox){
        		if(//((HasField)controller.activeWidget).getFieldValue() == null &&
        		   !((TextBox)controller.activeWidget).getText().equals("") && !((HasField)controller.activeWidget).getField().valid)
        		     newVal = ((TextBox)controller.activeWidget).getText();
        	}
        	if(newVal == null){
        		Field field = ((HasField)controller.activeWidget).getField();
        		if(field.queryMode)
        			newVal = field.queryString;
        		else
        			newVal = field.getValue();
        	}
        	controller.getRow(controller.selectedRow).cells.get(controller.selectedCol).setValue(newVal);
        	if(newVal instanceof TableDataRow)
        		newVal = ((TableDataRow)newVal).key;
        	if(currVal instanceof TableDataRow)
        		currVal = ((TableDataRow)currVal).key;
        	boolean changed = (currVal == null && newVal != null) || (currVal != null && !currVal.equals(newVal));
        	//if(changed) {
        		Widget wid = controller.activeWidget;
        		ArrayList<Exception> exceps = null;
				if(controller.getRow(controller.selectedRow).cells.get(controller.selectedCol).exceptions != null)
					exceps = controller.getRow(controller.selectedRow).cells.get(controller.selectedCol).exceptions;
				else
					exceps =  new ArrayList<Exception>(); 
        		if(wid instanceof HasField){
        			if(((HasField)wid).getExceptions() != null){	
        				for(Exception exc : (ArrayList<Exception>)((HasField)wid).getExceptions()){
        					if(!exceps.contains(exc))
        						exceps.add(new Exception(exc.getMessage()));
        				}
        				
        				controller.getRow(controller.selectedRow).cells.get(controller.selectedCol).exceptions = exceps;
        			}else if(changed)
        				controller.getRow(controller.selectedRow).cells.get(controller.selectedCol).exceptions = null;
        		}
        	//}
        	setCellDisplay(controller.selectedRow,controller.selectedCol);
        	controller.activeWidget = null;
            return changed;
        }
        return false;
    }

    public void rowUnselected(int row) {
        if(row == -1){
            for(int i = 0; i < controller.view.table.getRowCount() ; i++) {
                controller.view.table.getRowFormatter().removeStyleName(i,controller.view.selectedStyle);
            }
        }else
            controller.view.table.getRowFormatter().removeStyleName(controller.treeIndex(row), controller.view.selectedStyle);
        
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

    protected void rowSelected(int row) {
    	if(row > -1 && row < rows.size())
    		rows.get(controller.treeIndex(row)).addStyleName(controller.view.selectedStyle);
    }

    public ArrayList<TreeRow> getRows() {
        return rows;
    }

    public class ItemGrid extends Grid implements ClickHandler {
        
        public int clickCell;
        public int rowIndex;
        public int widgetIndex;
        public int width;
        private Widget wid;
        
        public ItemGrid(int rows, int cols) {
            super(rows,cols);
            setCellPadding(0);
            setCellSpacing(0);
            addClickHandler(this);
            DOM.setStyleAttribute(getElement(), "overflow", "hidden");
        }

        public void onClick(ClickEvent event) {
            if(((Grid)event.getSource()).getCellForEvent(event).getCellIndex() == clickCell){
                controller.toggle(controller.rowIndexList[rowIndex]);
                event.stopPropagation();
            }
        }
        
        public void setWidget(Widget wid){
        	this.wid = wid;
        	int widAdj = width - (widgetIndex)*18;
        	wid.setWidth(widAdj+"px");
        	setWidget(0, widgetIndex, wid);
        }
        
        public void setWidth(int width) {
        	this.width = width;
        	setWidth(width+"px");
        	if(wid != null) {
        		int widAdj = width - (widgetIndex)*18;
            	wid.setWidth(widAdj+"px");
        	}
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
