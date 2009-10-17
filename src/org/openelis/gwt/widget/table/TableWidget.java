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
import java.util.List;

import org.openelis.gwt.event.HasDropController;
import org.openelis.gwt.screen.ScreenPanel;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Field;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.table.TableSorterInt.SortDirection;
import org.openelis.gwt.widget.table.TableView.VerticalScroll;
import org.openelis.gwt.widget.table.event.BeforeAutoAddEvent;
import org.openelis.gwt.widget.table.event.BeforeAutoAddHandler;
import org.openelis.gwt.widget.table.event.BeforeCellEditedEvent;
import org.openelis.gwt.widget.table.event.BeforeCellEditedHandler;
import org.openelis.gwt.widget.table.event.BeforeRowAddedEvent;
import org.openelis.gwt.widget.table.event.BeforeRowAddedHandler;
import org.openelis.gwt.widget.table.event.BeforeRowDeletedEvent;
import org.openelis.gwt.widget.table.event.BeforeRowDeletedHandler;
import org.openelis.gwt.widget.table.event.BeforeRowMovedEvent;
import org.openelis.gwt.widget.table.event.BeforeRowMovedHandler;
import org.openelis.gwt.widget.table.event.BeforeSortEvent;
import org.openelis.gwt.widget.table.event.BeforeSortHandler;
import org.openelis.gwt.widget.table.event.CellEditedEvent;
import org.openelis.gwt.widget.table.event.CellEditedHandler;
import org.openelis.gwt.widget.table.event.HasBeforeAutoAddHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeCellEditedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeRowAddedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeRowDeletedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeRowMovedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeSortHandlers;
import org.openelis.gwt.widget.table.event.HasCellEditedHandlers;
import org.openelis.gwt.widget.table.event.HasRowAddedHandlers;
import org.openelis.gwt.widget.table.event.HasRowDeletedHandlers;
import org.openelis.gwt.widget.table.event.HasRowMovedHandlers;
import org.openelis.gwt.widget.table.event.HasSortHandlers;
import org.openelis.gwt.widget.table.event.HasTableValueChangeHandlers;
import org.openelis.gwt.widget.table.event.HasUnselectionHandlers;
import org.openelis.gwt.widget.table.event.RowAddedEvent;
import org.openelis.gwt.widget.table.event.RowAddedHandler;
import org.openelis.gwt.widget.table.event.RowDeletedEvent;
import org.openelis.gwt.widget.table.event.RowDeletedHandler;
import org.openelis.gwt.widget.table.event.RowMovedEvent;
import org.openelis.gwt.widget.table.event.RowMovedHandler;
import org.openelis.gwt.widget.table.event.SortEvent;
import org.openelis.gwt.widget.table.event.SortHandler;
import org.openelis.gwt.widget.table.event.TableValueChangeEvent;
import org.openelis.gwt.widget.table.event.TableValueChangeHandler;
import org.openelis.gwt.widget.table.event.UnselectionEvent;
import org.openelis.gwt.widget.table.event.UnselectionHandler;

import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasContextMenuHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

/**
 * This class is the main controller for the Table widget. It hooks the model to
 * the view and also controls when row selection and calls to the manager for
 * permissions and actions set by the a developer.
 * 
 * @author tschmidt
 * 
 */
public class TableWidget extends FocusPanel implements ClickHandler, 
													   HasField, 
													   MouseOverHandler,
													   MouseOutHandler,
													   HasTableValueChangeHandlers,
													   HasBeforeSelectionHandlers<TableRow>,
													   HasSelectionHandlers<TableRow>,
													   HasUnselectionHandlers<TableDataRow>,
													   HasBeforeCellEditedHandlers,
													   HasCellEditedHandlers, 
													   HasBeforeRowAddedHandlers,
													   HasRowAddedHandlers, 
													   HasBeforeRowDeletedHandlers,
													   HasRowDeletedHandlers,
													   HasBeforeAutoAddHandlers,
													   HasBeforeRowMovedHandlers,
													   HasRowMovedHandlers,
													   HasBeforeSortHandlers,
													   HasSortHandlers,
													   HasDropController,
													   HasContextMenuHandlers,
													   FocusHandler,
													   HasFocusHandlers
													   {
                            
    public ArrayList<TableColumn> columns;
    public boolean enabled;
    public boolean focused;
    public int activeRow = -1;
    public int activeCell = -1;
    public TableView view;
    public TableRenderer renderer;
    public TableKeyboardHandlerInt keyboardHandler;
    public boolean shiftKey;
    public boolean ctrlKey;
    public int maxRows;
    public int cellHeight = 21;
    public int cellSpacing = 2;
    public Widget editingCell = null;
    public int[] modelIndexList;
    public boolean showRows;
    public String title;
    public boolean showHeader;
    public boolean isDropdown = false;
    public TableDragController dragController;
    public TableIndexDropController dropController;
    public boolean selectedByClick;
    public VerticalScroll showScroll = VerticalScroll.NEEDED;
    public String width;
    private ArrayList<TableDataRow> data = new ArrayList<TableDataRow>();
    public TableSorterInt sorter = new TableSorter();
    public int shownRows; 
    public ArrayList<Integer> selections = new ArrayList<Integer>(1);
    private int selected = -1;
    private ArrayList<String> errors;
    public boolean queryMode;
    public boolean addIcon;
    public boolean deleteIcon;
    public boolean mouseOver;
    public boolean fireEvents = true;
    
    public boolean autoAdd;
    
    public TableDataRow autoAddRow;
    
    public boolean multiSelect;
    
    public TableWidget() {
        
    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void init(){
        renderer = new TableRenderer(this);
        keyboardHandler = new TableKeyboardHandler(this);
        view = new TableView(this,showScroll);
        view.setWidth(width);
        view.setHeight(maxRows*cellHeight);
        setWidget(view);
        addDomHandler(keyboardHandler,KeyUpEvent.getType());
        addDomHandler(keyboardHandler,KeyDownEvent.getType());
    }
        
    public void setTableWidth(String width) {
    	this.width = width;
    }

    /**
     * This method handles all click events on the body of the table
     */
    public void onClick(ClickEvent event) {
    	if(event.getSource() == view.table){
    		Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);
    		if(isEnabled()){
    			if(columns.get(cell.getCellIndex()).getColumnWidget() instanceof CheckBox){
    				if(CheckBox.CHECKED.equals(getCell(modelIndexList[cell.getRowIndex()],cell.getCellIndex()).getValue())){
    					setCell(modelIndexList[cell.getRowIndex()],cell.getCellIndex(),CheckBox.UNCHECKED);
    					if(fireEvents)
    						CellEditedEvent.fire(this,  modelIndexList[cell.getRowIndex()], cell.getCellIndex(), CheckBox.UNCHECKED);
    				}else{
    					setCell(modelIndexList[cell.getRowIndex()],cell.getCellIndex(),CheckBox.CHECKED);
    					if(fireEvents)
    						CellEditedEvent.fire(this, modelIndexList[cell.getRowIndex()], cell.getCellIndex(), CheckBox.CHECKED);
    				}
    			}
    		}
    		if(activeRow == cell.getRowIndex() && activeCell == cell.getCellIndex())
    			return;
    		selectedByClick = true;
    		select(cell.getRowIndex(), cell.getCellIndex());
    		selectedByClick = false;
    	}
    }
    
    public void onMouseOver(MouseOverEvent event) {
        ((Widget)event.getSource()).addStyleName("TableHighlighted");
        mouseOver = true;
         
     }

     public void onMouseOut(MouseOutEvent event) {
         ((Widget)event.getSource()).removeStyleName("TableHighlighted");
        mouseOver = false;   
     }

    /**
     * This method will unselect the row specified. Unselecting will save any
     * datat that has been changed in the row to the model.
     * 
     * @param row
     */
    public void unselect(int row) {
        finishEditing();
        if(row == -1){
            selections.clear();
            selected = -1;
            renderer.rowUnselected(-1);
        }else {
            selections.remove(new Integer(row));
            if(selected == row)
                selected = -1;
        }
        activeRow = -1;
        if(isRowDrawn(row))
        	renderer.rowUnselected(tableIndex(row));
    }
    
    private boolean isRowDrawn(int row){
    	return row >= modelIndexList[0] && row <= modelIndexList[view.table.getRowCount()-1];
    }
    
    private int tableIndex(int row) {
    	for(int i = 0; i < view.table.getRowCount(); i++){
    		if(modelIndexList[i] == row)
    			return i;
    	}
    	return -1;
    }
    
    /**
     * This method will cause the table row passed to be selected. If the row is
     * already selected, the column clicked will be opened for editing if the
     * cell is editable and the user has the correct permissions.
     * 
     * @param row
     * @param col
     */
    protected void select(final int row, final int col) {
    	if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
    		BeforeSelectionEvent<TableRow> event = BeforeSelectionEvent.fire(this, renderer.rows.get(row));
    		if(event.isCanceled())
    			return;
    	}else if(!isEnabled()) 
    		return;
        if(finishEditing()){
            if(numRows() >= maxRows){
                view.scrollBar.scrollToBottom();
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        renderer.scrollLoad(view.scrollBar.getScrollPosition());
                        select(row-1,col);
                    }
                });
            }
            view.table.getRowFormatter().addStyleName(activeRow, view.selectedStyle);
        }
        if(activeRow != row){
            if(activeRow > -1 && !ctrlKey){
            	if(fireEvents)
            		UnselectionEvent.fire(this,data.get(selected));
                unselect(-1);
                
            }
            activeRow = row;
            selectRow(modelIndexList[row]);
            if(fireEvents)
            	SelectionEvent.fire(this, renderer.rows.get(row));
        }
        if(canEditCell(row,col)){
            activeCell = col;
            renderer.setCellEditor(row, col);
            unsinkEvents(Event.ONKEYPRESS);
        }else{
       		activeCell = -1;
       		sinkEvents(Event.ONKEYPRESS);
       	}
    }
    
    public void selectAll() {
    	if(multiSelect) {
    		selections = new ArrayList<Integer>();
    		for(int i = 0; i < data.size(); i++)
    			selections.add(i);
    		renderer.dataChanged(true);
    	}
    }
    
    public boolean finishEditing() {
        if(editingCell != null) {
        	if(renderer.stopEditing() && fireEvents)
        		CellEditedEvent.fire(this, modelIndexList[activeRow], activeCell, getRow(activeRow).cells.get(activeCell).value);
            if(isAutoAdd() && modelIndexList[activeRow] == numRows()){
            	if(fireEvents){
            		BeforeAutoAddEvent event = BeforeAutoAddEvent.fire(this, getAutoAddRow());
                	if(event != null && !event.isCancelled()){
                		addRow(getAutoAddRow());
                		//tableWidgetListeners.fireFinishedEditing(this, modelIndexList[activeRow], activeCell);
                		return true;
                	}
            	}else{
            		addRow(getAutoAddRow());
            		return true;
            	}
            }
            activeCell = -1;
            sinkEvents(Event.KEYEVENTS);
            //tableWidgetListeners.fireFinishedEditing(this, modelIndexList[activeRow], activeCell);
        }
        return false;
        
    }
    
    public void startEditing(int row, int col) {
    	if(isRowDrawn(row))
    		select(tableIndex(row),col);
    }
    
    public void scrollToSelection(){
    	finishEditing();
        if(numRows() == shownRows()){
            view.scrollBar.setScrollPosition(cellHeight*getSelectedIndex());
        }else{
            int shownIndex = 0;
            for(int i = 0; i < getSelectedIndex(); i++){
                if(getRow(i).shown)
                    shownIndex++;
            }
            view.scrollBar.setScrollPosition(cellHeight*shownIndex);
        }
    }

    public void addRow() {
    	finishEditing();
        addRow(createRow());
        view.scrollBar.scrollToBottom();
    }
    
    public void addRow(int index) {
    	finishEditing();
        addRow(index,createRow());
    }
    
    public void addRow(TableDataRow row) {
    	finishEditing();
    	if(fireEvents){
    		BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, data.size(), row);
    		if(event != null && event.isCancelled())
    			return;
    	}
        data.add(row);
        if(row.shown)
            shownRows++;
        if(fireEvents)
        	RowAddedEvent.fire(this, data.size()-1, row);
        view.scrollBar.scrollToBottom();
       	renderer.dataChanged(true);
       	
    }
    
    public void addRow(int index, TableDataRow row) {
    	finishEditing();
    	if(fireEvents){
    		BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, index, row);
    		if(event != null && event.isCancelled())
    			return;
    	}
        data.add(index, row);
        if(row.shown)
            shownRows++;
        if(fireEvents)
        	RowAddedEvent.fire(this, index, row);
       	renderer.dataChanged(true);
    }

    public void deleteRow(int row) {
    	unselect(row);
    	if(fireEvents){
    		BeforeRowDeletedEvent event = BeforeRowDeletedEvent.fire(this, row, getRow(row));
    		if(event != null && event.isCancelled())
    			return;
    	}
        if(data.get(row).shown)
            shownRows--;
        if(row < data.size()){
        	if(fireEvents)
        		UnselectionEvent.fire(this, data.get(row));
            TableDataRow tmp = data.remove(row);
            if(fireEvents)
            	RowDeletedEvent.fire(this, row, tmp);
        }
        renderer.dataChanged(true);
    }
    
    public void MoveRow(int curIndex, int newIndex) {
    	if(fireEvents) {
    		if(getHandlerCount(BeforeRowMovedEvent.getType()) > 0){
    			BeforeRowMovedEvent event = BeforeRowMovedEvent.fire(this, curIndex, newIndex, data.get(curIndex));
    			if(event != null && event.isCancelled())
    				return;
    		}
    	}
    	TableDataRow row = data.remove(curIndex);
    	int insert = newIndex;
    	if(newIndex > curIndex) 
    		insert--;
    	if(newIndex >= data.size())
    		data.add(row);
    	else
    		data.add(insert, row);
    	if(fireEvents) 
    		RowMovedEvent.fire(this, curIndex, newIndex, row);
    	renderer.dataChanged(true);
    }
        
    public TableDataRow getRow(int row) {
        //if(data == null)
          //  return null;
        if(row < numRows())
            return data.get(row);
        if(autoAdd)
            return autoAddRow;
        return null;
    }
    
    public void addColumn(TableColumn column) {
    	columns.add(column);
    	resetTable();
    }
    
    public void addColumn(int index, TableColumn column) {
    	columns.add(index,column);
    	resetTable();
    }
    
    public void removeColumn(int index) {
    	columns.remove(index);
    	resetTable();
    }
    
    public ArrayList<TableColumn> getColumns(){
    	return columns;
    }
    
    public void setColumns(ArrayList<TableColumn> columns) {
    	this.columns = columns;
    	resetTable();
    }
    
    private void resetTable() {
    	view.header = new TableHeaderBar();
    	view.header.init(this);
    	view.table.clear();
    	renderer.dataChanged(false);
    }

    public int numRows() {
        if(data == null)
            return 0;
        return data.size();
    }

    public Object getObject(int row, int col) {
        return data.get(row).cells.get(col).getValue();
    }

    public void clear() {
    	if(data != null)
    		data.clear();
    	clearSelections();
    	activeRow = -1;
    	activeCell = -1;
        shownRows = 0;
        renderer.dataChanged(false);
    }
    
    public TableDataRow setRow(int index, TableDataRow row){
    	finishEditing();
        TableDataRow set =  data.set(index, row);
        if(isRowDrawn(index))
        	renderer.loadRow(index);
        return set;
    }
    
    public boolean tableRowEmpty(int index){
        return tableRowEmpty((TableDataRow)getRow(index));
    }
        
    private boolean tableRowEmpty(TableDataRow row){ 
        boolean empty = true;
        List cells = row.getCells();
        for(Object field : cells) {
            if(field != null && !"".equals(field)){
                empty = false;
                break;
            }
        }
        return empty;
    }
    
    public int shownRows() {
        return shownRows;
    }
    
    public TableDataRow createRow() {
        TableDataRow row = new TableDataRow(columns.size());
        return row;
    }
    
    public void load(ArrayList<TableDataRow> data) {
        selections.clear();
        selected = -1;
        renderer.rowUnselected(-1);
        this.data = data;
        shownRows = 0;
        for(int i = 0; i < data.size(); i++){
            if(((TableDataRow)data.get(i)).shown)
                shownRows++;
        }
        editingCell = null;
        activeRow = -1;
        activeCell = -1;
        renderer.dataChanged(false);
    }
    
    /* Remove method when ResultsTable can be committed
     * 
     */
    public void selectRow(int index, boolean dummy) {
    	selectRow(index);
    }

    public void selectRow(final int index) throws IndexOutOfBoundsException{
        if(index > data.size())
            throw new IndexOutOfBoundsException();
        selected = index;
        if(!multiSelect)
            selections.clear();
        selections.add(index);
        renderer.dataChanged(true);
    }
    
    public void clearSelections() {
        selections.clear();
        selected = -1;
    }
    
    public ArrayList<TableDataRow> getSelections() {
    	ArrayList<TableDataRow>  sels = new ArrayList<TableDataRow>();
    	for(int index : selections) {
    		sels.add(getRow(index));
    	}
        return sels;
    }


    public boolean getAutoAdd() {
        return autoAdd;
    }


    public TableDataRow getAutoAddRow() {
        return autoAddRow;
    }


    public void setAutoAddRow(TableDataRow row) {
        autoAddRow = row;
    }

    public ArrayList<TableDataRow> getData() {
        return data;
    }
    
    public void setCell(int row, int col, Object value) {
        getRow(row).cells.get(col).setValue(value);
        if(isRowDrawn(row))
        	renderer.cellUpdated(tableIndex(row), col);
        //CellEditedEvent.fire(this, row, col, value);
    }
    
    public TableDataCell getCell(int row, int col) {
        return getRow(row).cells.get(col);
    }
    
    public void hideRow(int row) {
        data.get(row).shown = false;
        shownRows--;
        renderer.dataChanged(true);
    }
    
    public void showRow(int row) {
        data.get(row).shown = true;
        shownRows++;
        renderer.dataChanged(true);
    }

    public void sort(int col, SortDirection direction) {
    	if(fireEvents){
    		if(getHandlerCount(BeforeSortEvent.getType()) > 0){
    			BeforeSortEvent event = BeforeSortEvent.fire(this, col, columns.get(col).key, direction);
    			if(event != null && event.isCancelled())
    				return;
    		}
    	}
    	unselect(-1);
    	if(fireEvents && getHandlerCount(SortEvent.getType()) > 0) {
    		SortEvent.fire(this, col, columns.get(col).key, direction);
    	}else{
    		sorter.sort(data, col, direction);
    		renderer.dataChanged(false);
    	}
    }
    
    public void refresh() {
        shownRows = 0;
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).shown)
                shownRows++;
        }
        renderer.dataChanged(false);
    }
    
    public boolean isSelected(int index) {
        return selections.contains(index);
    }

    public void enableMultiSelect(boolean multi) {
        this.multiSelect = multi;
    }
    
    public boolean isEnabled(int index) {
        if(index < numRows())
            return data.get(index).enabled;
        return autoAddRow.enabled;
    }

    public TableDataRow getSelection() {
        if(selected == -1)
            return null;
        
        return data.get(selected);
    }

    public ArrayList<TableDataRow> unload() {
        finishEditing();
        return data;
    }
    
    public boolean isAutoAdd() {
        return autoAdd;
    }

    public void enableAutoAdd(boolean autoAdd) {
        this.autoAdd = autoAdd;
        autoAddRow = new TableDataRow(columns.size());
        renderer.dataChanged(true);   
    }

    public int getSelectedIndex() {
        return selected;
    }

    public int[] getSelectedIndexes() {
        int[] ret = new int[selections.size()];
        for(int i = 0; i < selections.size(); i++)
            ret[i] = (Integer)selections.get(i);
        return ret;
    }
   
    
    public void setCellError(int row, int col, String error) {
        data.get(row).cells.get(col).addError(error);
        if(isRowDrawn(row))
        	renderer.cellUpdated(tableIndex(row), col);
    }
    
    public void setCellError(int row, String col, String error) {
    	for(TableColumn column : columns) {
    		if(column.key.equals(col)){
    			setCellError(row,columns.indexOf(column),error);
    			break;
    		}
    	}
    }
    
    public void clearCellError(int row, int col) {
    	 data.get(row).cells.get(col).clearErrors();
    	 if(isRowDrawn(row))
    		 renderer.cellUpdated(tableIndex(row), col);
    }
    
    
 
    public void selectRow(Object key) {
    	if(data == null && autoAdd){
    		selectRow(0);
    		return;
    	}
    	for(int i = 0; i < data.size(); i++) {
    		if(data.get(i).key == key || key.equals(data.get(i).key)){
        		selectRow(i);
    			break;
    		}
    	}
    }

	public void addError(String Error) {
		// TODO Auto-generated method stub
		
	}

	public void clearErrors() {
		errors = null;
		
	}

	public Field getField() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setField(Field field) {
		// TODO Auto-generated method stub
		
	}

	public void setQueryMode(boolean query) {
		if(query == queryMode)
			return;
		if(query)
			fireEvents = false;
		else
			fireEvents = true;
		queryMode = query;
		for(TableColumn col : columns) {
			((HasField)col.getColumnWidget()).setQueryMode(query);
		}
		ArrayList<TableDataRow> qModel = new ArrayList<TableDataRow>();
		if(query) {
			qModel.add(new TableDataRow(columns.size()));
		}
		load(qModel);
	}

	public void getQuery(ArrayList list, String key) {
		if(queryMode){
			for(TableColumn col : columns) {
				((HasField)col.getColumnWidget()).getQuery(list, col.key);
			}
		}
	}
	
	public ArrayList<String> getErrors() {
		return errors;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
        //if(dragController != null)
          //  dragController.setEnable(enabled);
        for(TableColumn column : columns) {
            column.enable(enabled);
        }
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void checkValue() {
		finishEditing();
		errors = null;
		if(data == null)
			return;
		for(int i = 0; i < numRows(); i++) {
			for(int j = 0; j < data.get(i).cells.size(); j++){
				Widget wid = columns.get(j).getWidgetEditor(data.get(i).cells.get(j));
				if(wid instanceof HasField){
					((HasField)wid).checkValue();
					data.get(i).cells.get(j).errors = ((HasField)wid).getErrors();
					if(data.get(i).cells.get(j).errors != null){
						errors = data.get(i).cells.get(j).errors;
					}
				}
			}
		}
		if(errors != null)
			refresh();
		else if(fireEvents)
			TableValueChangeEvent.fire(this, data);
	}
	
	public HandlerRegistration addCellEditedHandler(CellEditedHandler handler) {
		return addHandler(handler, CellEditedEvent.getType());
	}
	
	public HandlerRegistration addRowAddedHandler(RowAddedHandler handler) {
		return addHandler(handler, RowAddedEvent.getType());
	}

	public HandlerRegistration addRowDeletedHandler(RowDeletedHandler handler) {
		return addHandler(handler, RowDeletedEvent.getType());
	}

	public HandlerRegistration addBeforeSelectionHandler(
			BeforeSelectionHandler<TableRow> handler) {
		return addHandler(handler, BeforeSelectionEvent.getType());
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<TableRow> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	public HandlerRegistration addBeforeCellEditedHandler(
			BeforeCellEditedHandler handler) {
		return addHandler(handler,BeforeCellEditedEvent.getType());
	}

	public HandlerRegistration addBeforeRowAddedHandler(
			BeforeRowAddedHandler handler) {
		return addHandler(handler, BeforeRowAddedEvent.getType());
	}

	public HandlerRegistration addBeforeRowDeletedHandler(
			BeforeRowDeletedHandler handler) {
		return addHandler(handler, BeforeRowDeletedEvent.getType());
	}

	public HandlerRegistration addBeforeAutoddHandler(
			BeforeAutoAddHandler handler) {
		return addHandler(handler, BeforeAutoAddEvent.getType());
	}

	public Object getFieldValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected boolean canEditCell(int row, int col) {
        if(getHandlerCount(BeforeCellEditedEvent.getType()) > 0 && fireEvents) {
        	BeforeCellEditedEvent bce = BeforeCellEditedEvent.fire(this, modelIndexList[row], col, getRow(modelIndexList[row]).cells.get(col).value);
        	if(bce.isCancelled()){
        		return false;
        	}
        }
        return (isEnabled());
	}

	public void setFieldValue(Object value) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addTableValueChangeHandler(
			TableValueChangeHandler handler) {
		return addHandler(handler, TableValueChangeEvent.getType());
	}

    public void enableDrag(boolean drag) {
    	if(drag){
    		if(dragController == null) {
    			dragController = new TableDragController(RootPanel.get());  		
      			for(TableRow row : renderer.rows) 
      				dragController.makeDraggable(row);
    		}
    		dragController.setEnable(true);
    	}else{
    		dragController.setEnable(false);
    	}
    	
    }
    
    public void enableDrop(boolean drop) {
    	if(drop)
    		dropController = new TableIndexDropController(this);
    	else
    		dropController = null;
    }
    
    public void addTarget(HasDropController drop){
    	assert(dragController != null);
    	dragController.registerDropController(drop.getDropController());
    }
    
    public void removeTarget(HasDropController drop) {
    	assert(dragController != null);
    	dragController.unregisterDropController(drop.getDropController());
    }

	public DropController getDropController() {
		return dropController;
	}

	public void setDropController(DropController controller) {
		// TODO Auto-generated method stub
		
	}
	
	public void addDragHandler(DragHandler handler) {
		dragController.addDragHandler(handler);
	}
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler handler) {
		return null;
	}

	public HandlerRegistration addUnselectionHandler(
			UnselectionHandler<TableDataRow> handler) {
		return addHandler(handler,UnselectionEvent.getType());
	}

	public HandlerRegistration addContextMenuHandler(ContextMenuHandler handler) {
		return addDomHandler(handler, ContextMenuEvent.getType());
	}

	public void onFocus(FocusEvent event) {
		if(!DOM.isOrHasChild(getElement(),((ScreenPanel)event.getSource()).focused.getElement())){
			finishEditing();
		}
	}
	
	public void fireEvents(boolean fire) {
		fireEvents = fire;
	}

	public HandlerRegistration addBeforeRowMovedHandler(
			BeforeRowMovedHandler handler) {
		return addHandler(handler, BeforeRowMovedEvent.getType());
	}

	public HandlerRegistration addRowMovedHandler(RowMovedHandler handler) {
		return addHandler(handler, RowMovedEvent.getType());
	}

	public HandlerRegistration addBeforeSortHandler(BeforeSortHandler handler) {
		return addHandler(handler, BeforeSortEvent.getType());
	}

	public HandlerRegistration addSortHandler(SortHandler handler) {
		return addHandler(handler, SortEvent.getType());
	}
	
}
