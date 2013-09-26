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
package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.CalendarLookUp;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Field;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

public class TableRenderer  {
    
    private TableWidget controller;
    public ArrayList<TableRow> rows = new ArrayList<TableRow>();
    
    public TableRenderer(TableWidget controller){
        this.controller = controller;
    }
    
    public void createRow(int i) {
    	if(controller.columns.size() == 0)
    		return;
        int j = 0;
        for(TableColumn column : controller.columns) {
            Widget wid = column.getDisplayWidget(controller.getRow(i));
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
            j++;
        }
        TableRow  row = new TableRow(controller.view.table.getRowFormatter().getElement(i));
        row.controller = controller;
        if(controller.isDropdown){
        	row.addMouseOutHandler(controller);
        	row.addMouseOverHandler(controller);
        }
        if(controller.dragController != null)
            controller.dragController.makeDraggable(row);
        row.index = i;
        /*
        if(i % 2 == 1 && !controller.isDropdown){
            row.addStyleName("AltTableRow");
        }
        */
        rows.add(row);
    }

    public void load(int pos) {
        controller.modelIndexList = new int[controller.maxRows];
        int ScrollHeight = (controller.shownRows*controller.cellHeight);
        if(controller.isDropdown) {
        	ScrollHeight += controller.cellHeight;
        }
        /*
        int testStart = new Double(Math.ceil(((double)(controller.maxRows*controller.cellHeight)/(controller.cellHeight)))).intValue();
        if(testStart < controller.shownRows() - controller.maxRows) {
            System.out.println("testStart = "+testStart+"shown Rows = "+controller.shownRows+" maxRows = "+controller.maxRows);
            ScrollHeight += controller.cellHeight;
        }
        */
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
                if(i < rows.size())
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
        TableDataRow row = controller.getRow(modelIndex);
        rows.get(index).modelIndex = modelIndex;
        rows.get(index).row = row;
        
        for (int i = 0; i < controller.columns.size(); i++) {
        	//controller.view.table.getCellFormatter().setWidth(index, i, controller.columns.get(i).getCurrentWidth()+"px");
        	TableDataCell cell = new TableDataCell(null);
        	if( i < row.cells.size())
        		cell = row.cells.get(i);
        	controller.columns.get(i).loadWidget(controller.view.table.getWidget(index, i),row,modelIndex);
        }
        rows.get(index).setStyleName("");
        /*
        if(index % 2 == 1 && !controller.isDropdown){
            rows.get(index).addStyleName("AltTableRow");
        }
        */
        if(controller.isSelected(modelIndex))
            rows.get(index).addStyleName(controller.view.selectedStyle);
        if(!controller.isEnabled(modelIndex)) 
            rows.get(index).addStyleName(controller.view.disabledStyle);
        if(row.style != null && !row.style.equals(""))
        	rows.get(index).addStyleName(row.style);
    }
    
    public void loadRow(int index) {
    	loadRow(controller.tableIndex(index),index);
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
            if(loadStart+i < controller.numRows() && controller.getRow(loadStart+i).shown)
                loadRow(i,loadStart+i);
        }

    }
    
    public void setCellEditor(int row, int col) {
        controller.activeWidget = (Widget)controller.columns.get(col).getWidgetEditor(controller.getRow(row));
        if(controller.activeWidget instanceof AbsolutePanel)
        	controller.activeWidget = ((AbsolutePanel)controller.activeWidget).getWidget(0);
        controller.view.table.setWidget(controller.tableIndex(row), col, controller.activeWidget);
        if(controller.activeWidget instanceof Focusable)
        	((Focusable)controller.activeWidget).setFocus(true);
            
    }
    
    public void setCellDisplay(int row, int col) {
    	controller.view.table.setWidget(controller.tableIndex(row), col, controller.columns.get(col).getDisplayWidget(controller.getRow(row)));
    }

    @SuppressWarnings("unchecked")
	public boolean stopEditing() {
        if(controller.activeWidget != null){
            if(controller.activeWidget instanceof CalendarLookUp && ((CalendarLookUp)controller.activeWidget).getField().queryMode) {
                setCellDisplay(controller.selectedRow,controller.selectedCol);
                controller.getData().get(controller.selectedRow).cells.get(controller.selectedCol).setValue(((HasField)controller.activeWidget).getField().queryString);
                controller.activeWidget = null;
                return true;
            }
        	Object currVal = controller.getData().get(controller.selectedRow).cells.get(controller.selectedCol).getValue();
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
        		else if(!(controller.activeWidget instanceof AutoComplete))
        			newVal = field.getValue();
        	}
        	controller.getData().get(controller.selectedRow).cells.get(controller.selectedCol).setValue(newVal);
        	
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
            controller.view.table.getRowFormatter().removeStyleName(controller.tableIndex(row), controller.view.selectedStyle);
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
    	rows.get(controller.tableIndex(row)).addStyleName(controller.view.selectedStyle);
    }
    
    public ArrayList<TableRow> getRows() {
        return rows;
    }
    
    public void clear() {
    	controller.view.table.removeAllRows();
    }

}
