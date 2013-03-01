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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.openelis.ui.common.Util;
import org.openelis.gwt.event.BeforeDragStartEvent;
import org.openelis.gwt.event.BeforeDragStartHandler;
import org.openelis.gwt.event.BeforeDropHandler;
import org.openelis.gwt.event.DragStartHandler;
import org.openelis.gwt.event.DropHandler;
import org.openelis.gwt.event.HasDropController;
import org.openelis.gwt.event.NavigationSelectionEvent;
import org.openelis.gwt.event.NavigationSelectionHandler;
import org.openelis.gwt.screen.ScreenPanel;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.widget.CalendarLookUp;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.Field;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.NavigationWidget;
import org.openelis.gwt.widget.PercentBar;
import org.openelis.gwt.widget.table.TableView.VerticalScroll;
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
import org.openelis.gwt.widget.table.event.FilterEvent;
import org.openelis.gwt.widget.table.event.FilterHandler;
import org.openelis.gwt.widget.table.event.HasBeforeCellEditedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeRowAddedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeRowDeletedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeRowMovedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeSortHandlers;
import org.openelis.gwt.widget.table.event.HasCellEditedHandlers;
import org.openelis.gwt.widget.table.event.HasFilterHandlers;
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
import org.openelis.gwt.widget.table.event.SortEvent.SortDirection;

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
import com.google.gwt.user.client.DOM;
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
													   HasBeforeRowMovedHandlers,
													   HasRowMovedHandlers,
													   HasBeforeSortHandlers,
													   HasSortHandlers,
													   HasDropController,
													   HasContextMenuHandlers,
													   FocusHandler,
													   HasFocusHandlers,
													   HasFilterHandlers,
													   NavigationWidget<TableDataRow>
													   {
                            
    protected ArrayList<TableColumn> columns;
    protected boolean enabled;
    protected boolean focused;
    protected int selectedRow = -1;
    protected int selectedCol = -1;
    public TableView view;
    public TableRenderer renderer;
    protected TableKeyboardHandlerInt keyboardHandler;
    protected boolean shiftKey;
    protected boolean ctrlKey;
    protected int maxRows;
    protected int cellHeight = 21;
    protected Widget activeWidget = null;
    protected int[] modelIndexList;
    protected String title;
    protected boolean showHeader;
    protected boolean isDropdown = false;
    protected TableDragController dragController;
    protected TableIndexDropController dropController;
    protected boolean selectedByClick;
    protected VerticalScroll showScroll = VerticalScroll.NEEDED;
    protected String width;
    protected ArrayList<TableDataRow> model = new ArrayList<TableDataRow>();
    protected int shownRows; 
    protected ArrayList<Integer> selections = new ArrayList<Integer>(1);
    protected ArrayList<Exception> exceptions;
    protected boolean queryMode;
    protected boolean mouseOver;
    protected boolean fireEvents = true;    
    protected boolean multiSelect;
    protected boolean byClick;
    public HashMap<Object,Integer> searchKey;
    
    /**
     * Table has too many configuration options to pass to a constructor. 
     * We use a no-arg constructor to create the widget, set option fields
     * then call init to realize the widget.
     */
    public TableWidget() {
        
    }
    
    /**
     * Call to realize the widget for use.
     */
    public void init(){
        renderer = new TableRenderer(this);
        keyboardHandler = new TableKeyboardHandler(this);
        view = new TableView(this,showScroll);
        view.setWidth(width);
        view.setHeight(maxRows*cellHeight);
        setWidget(view);
        //addDomHandler(keyboardHandler,KeyUpEvent.getType());
        addDomHandler(keyboardHandler,KeyDownEvent.getType());
    }
        
    /**
     * Sets the width of the cellView which is not the width of the overall widget.  When set to "auto"
     * the width of the cellView will the sum of the column widths and no Horizontal Scrollbar will appear.
     * @param width
     */
    public void setTableWidth(String width) {
    	this.width = width;
    	
    }
    
    public void setMaxRows(int maxRows) {
    	this.maxRows = maxRows;
    }
    
    public int getMaxRows() {
    	return maxRows;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public void setShowScroll(VerticalScroll scroll){
    	this.showScroll = scroll;
    }
    
    public void showHeader(boolean showHeader) {
    	this.showHeader = showHeader;
    }
    
    public boolean showHeader() {
    	return showHeader;
    }
    
    public void multiSelect(boolean multiSelect){
    	this.multiSelect = multiSelect;
    }
    
    public boolean multiSelect() {
    	return multiSelect;
    }
    
    public int getTableWidth() {
    	int tw = 0;
    	
    	if(width.equals("auto")){
    		for(TableColumn column : columns)
    			tw += column.getCurrentWidth()+3;
    	}else
    		tw = Util.stripUnits(width,"px");    	
    	return tw;
    }

    /**
     * This method handles all click events on the body of the table.  
     */
    public void onClick(ClickEvent event) {
    	event.preventDefault();
    	if(event.getNativeEvent().getCtrlKey())
    		ctrlKey = true;
    	else
    		ctrlKey = false;
    	if(event.getNativeEvent().getShiftKey())
    		shiftKey = true;
    	else
    		shiftKey = false;
    	if(event.getSource() == view.table){
    		Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);

    		if(tableIndex(selectedRow) == cell.getRowIndex() && selectedCol == cell.getCellIndex())
    			return;
    		byClick = true;
    		select(modelIndexList[cell.getRowIndex()], cell.getCellIndex(),true);
    		byClick = false;
    	}
    	ctrlKey = false;
    	shiftKey = false;
    }
    
    /**
     * Adds the highlighting of the rows when the mouse is moved over the table.
     */
    public void onMouseOver(MouseOverEvent event) {
        ((Widget)event.getSource()).addStyleName("TableHighlighted");
        mouseOver = true;
         
     }

     /**
      * Removes the highlight on the table rows whent the mouse is moved over the table. 
      */
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
            renderer.rowUnselected(-1);
            selectedRow = -1;
            return;
        }else {
            selections.remove(new Integer(row));
        }
        selectedRow = -1;
        if(isRowDrawn(row))
        	renderer.rowUnselected(row);
    }
    
    /**
     * Method used to determine if a row in the table model is currently drawn in the table
     * @param row
     * @return
     *    true if the row is currently displayed
     */
    protected boolean isRowDrawn(int row){
    	return row >= modelIndexList[0] && row <= modelIndexList[view.table.getRowCount()-1];
    }
    
    /**
     * This method is used to find the display index of a row in the model.
     * @param row
     *      the index of a row in the table model
     * @return
     *      the index of the table where the row is currently displayed.  If
     *      the row is currently scrolled off the table this method will return -1.
     */
    protected int tableIndex(int row) {
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
    protected void select(final int row, final int col, boolean byClick) {
    	if(getHandlerCount(NavigationSelectionEvent.getType()) > 0 && !queryMode && selectedRow != row) {
    		NavigationSelectionEvent.fire(this,row);
    		return;
    	}
    	// Remove selections if not multiselect and no key is held
    	if(selectedRow != row) {
    		if(!multiSelect || (multiSelect && !shiftKey && !ctrlKey)) {
    			while(selections.size() > 0) {
    				int index = selections.get(0);
    				if(fireEvents){
    					UnselectionEvent event = UnselectionEvent.fire(this,model.get(index),model.get(row));
    					if(event != null && event.isCanceled())
    						return;	
    				}
    				unselect(index);	
    			}
    		}
    	
    		//Fire Before Selection
    		if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
    			BeforeSelectionEvent<TableRow> event = BeforeSelectionEvent.fire(this, renderer.rows.get(tableIndex(row)));
    			if(event.isCanceled())
    				return;
    		}else if(!isEnabled()) 
    			return;
    	}
        finishEditing();
        if(multiSelect && ctrlKey && isSelected(row)){
        	unselect(row);
        	selectedCol = -1;
       		//sinkEvents(Event.KEYEVENTS);
        	return;
        }
        if(selectedRow != row){
            selectRow(row);
            if(fireEvents)
            	SelectionEvent.fire(this, renderer.rows.get(tableIndex(row)));
        }
        if(isEnabled() && canEditCell(row,col)){
        	if(byClick && columns.get(col).getColumnWidget() instanceof CheckBox && !shiftKey && !ctrlKey){
        		clearCellExceptions(row, col);
        		if(CheckBox.CHECKED.equals(getCell(row,col).getValue())){
        			setCell(row,col,CheckBox.UNCHECKED);
        			if(fireEvents)
        				CellEditedEvent.fire(this, row, col, CheckBox.UNCHECKED);
        		}else if(queryMode && CheckBox.UNCHECKED.equals(getCell(row,col).getValue())){
        			setCell(row,col,CheckBox.UNKNOWN);
        			if(fireEvents)
        				CellEditedEvent.fire(this, row, col, CheckBox.UNKNOWN);
        		}else{
        			setCell(row,col,CheckBox.CHECKED);
        			if(fireEvents)
        				CellEditedEvent.fire(this, row, col, CheckBox.CHECKED);
        		}
           		selectedCol = -1;
           		//sinkEvents(Event.KEYEVENTS);
        	}else{
        		selectedCol = col;
        		renderer.setCellEditor(row, col);
        		//unsinkEvents(Event.KEYEVENTS);
        	}
        }else{
       		selectedCol = -1;
       		//sinkEvents(Event.KEYEVENTS);
       	}
    }
    
    public void select(int row, int col) {
    	select(row,col,false);
    }
    
    /**
     *  This method will select all rows in the model, but only if the table is in 
     *  MultiSelect mode.
     */
    public void selectAll() {
    	if(multiSelect) {
    		selections = new ArrayList<Integer>();
    		for(int i = 0; i < model.size(); i++)
    			selections.add(i);
    		renderer.dataChanged(true);
    	}
    }
    
    /**
     * This method is called to save the currently edited cell into the model and to 
     * switch the table cell to display mode.
     */
    public void finishEditing() {
        if(activeWidget != null) {
        	if(renderer.stopEditing() && fireEvents)
        		CellEditedEvent.fire(this, selectedRow, selectedCol, getRow(selectedRow).cells.get(selectedCol).value);
            selectedCol = -1;
            //sinkEvents(Event.KEYEVENTS);
        }
    }
    
    /**
     * This method is used to put a cell into edit mode by code.  Model index is passed in and if the
     * row is currently not drawn the method will do nothing.    
     * @param row
     *    Model Index of the row to be edited.
     * @param col
     *    col index to be edited
     */
    public void startEditing(int row, int col) {
    	if(isRowDrawn(row))
    		select(row,col);
    }
    
    public boolean isEditing() {
    	return activeWidget != null;
    }
    
    /**
     * Scrolls the table to the first selected index in the selected list.  The Row will be at the top of
     * the view regardless of the current position.
     * 
     */
    public void scrollToSelection(){
    	finishEditing();
        if(numRows() == shownRows()){
            view.scrollBar.setScrollPosition(cellHeight*getSelectedRow());
        }else{
            int shownIndex = 0;
            for(int i = 0; i < getSelectedRow(); i++){
                if(getRow(i).shown)
                    shownIndex++;
            }
            view.scrollBar.setScrollPosition(cellHeight*shownIndex);
        }
    }
    
    /**
     * Scrolls the table to the first selected index in the selected list. If selectedRow is off the bottom of the view the
     * selected row will be at the bootom of the view,else it will be at the top.
     */
    public void scrollToVisisble(){
    	if(isRowDrawn(selectedRow))
    		return;
    	finishEditing();
        if(numRows() == shownRows()){
        	if(selectedRow > modelIndexList[maxRows-1])
        		view.scrollBar.setScrollPosition((cellHeight*selectedRow)-(cellHeight*(maxRows-1)));
        	else
        		view.scrollBar.setScrollPosition(cellHeight*getSelectedRow());
        }else{
            int shownIndex = 0;
            for(int i = 0; i < getSelectedRow(); i++){
                if(getRow(i).shown)
                    shownIndex++;
            }
            if(shownIndex > modelIndexList[maxRows-1])
            	view.scrollBar.setScrollPosition((cellHeight*shownIndex)-(cellHeight*(maxRows-1)));
            else
            	view.scrollBar.setScrollPosition(cellHeight*shownIndex);
        }
    }

    /**
     * Adds a new default row to the end of the model and scrolls the table to the bottom so 
     * that it can be seen.
     */
    public void addRow() {
    	finishEditing();
        addRow(createRow());
        view.scrollBar.scrollToBottom();
    }
    
    /**
     * 
     * @param index
     */
    public void addRow(int index) {
    	finishEditing();
        addRow(index,createRow());
    }
    
    public void addRow(TableDataRow row) {
    	addRow(model.size(),row);
    }
    
    public void addRow(int index, TableDataRow row) {
    	finishEditing();
    	if(fireEvents){
    		BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, index, row);
    		if(event != null && event.isCancelled())
    			return;
    	}
        model.add(index, row);
        if(row.shown)
            shownRows++;
    	renderer.dataChanged(true);
        if(fireEvents)
        	RowAddedEvent.fire(this, index, row);
    }

    public void deleteRow(int row) {
    	unselect(row);
    	if(fireEvents){
    		BeforeRowDeletedEvent event = BeforeRowDeletedEvent.fire(this, row, getRow(row));
    		if(event != null && event.isCancelled())
    			return;
    	}
        if(model.get(row).shown)
            shownRows--;
        if(row < model.size()){
        	if(fireEvents)
        		UnselectionEvent.fire(this, model.get(row),null);
            TableDataRow tmp = model.remove(row);
            if(fireEvents)
            	RowDeletedEvent.fire(this, row, tmp);
        }
        renderer.dataChanged(true);
    }
    
    public void moveRow(int curIndex, int newIndex) {
    	if(fireEvents) {
    		if(getHandlerCount(BeforeRowMovedEvent.getType()) > 0){
    			BeforeRowMovedEvent<TableDataRow> event = BeforeRowMovedEvent.fire(this, curIndex, newIndex, model.get(curIndex));
    			if(event != null && event.isCancelled())
    				return;
    		}
    	}
    	TableDataRow row = model.remove(curIndex);
    	int insert = newIndex;
    	if(newIndex > curIndex) 
    		insert--;
    	if(insert < 0)
    		insert = 0;
    	if(insert >= model.size())
    		model.add(row);
    	else
    		model.add(insert, row);
    	if(fireEvents) 
    		RowMovedEvent.fire(this, curIndex, newIndex, row);
    	renderer.dataChanged(true);
    }
        
    public TableDataRow getRow(int row) {
        if(row < numRows())
            return model.get(row);
        return null;
    }
    
    public TableDataRow getRowByKey(Object key) {
        Integer n;

        if(model == null)
    		return null;

        if (key == null) {
    		n = 0;
        } else {
            n = searchKey.get(key);
            if (n == null)
                n = 0;
        }
        return model.get(n.intValue());
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
    
    public Widget getColumnWidget(String key) {
    	for(TableColumn col : columns){
    		if(key.equals(col.key))
    			return col.getColumnWidget();
    	}
    	return null;
    }
    
    private void resetTable() {
    	if(view == null) {
    		return;
    	}
    	view.header = new TableHeaderBar();
    	view.header.init(this);
    	view.table.clear();
    	renderer.dataChanged(false);
    }

    public int numRows() {
        if(model == null)
            return 0;
        return model.size();
    }

    public Object getObject(int row, int col) {
    	Object obj = model.get(row).cells.get(col).getValue();
    	if(obj instanceof ArrayList) {
    		if(((ArrayList)obj).size() == 1)
    			return ((ArrayList)obj).get(0);
    		else if(((ArrayList)obj).size() == 0)
    			return null;
    	}
    	return obj;
    
    }

    public void clear() {
    	if(model != null)
    		model.clear();
    	clearSelections();
    	selectedRow = -1;
    	selectedCol = -1;
        shownRows = 0;
        renderer.dataChanged(false);
    }
    
    public TableDataRow setRow(int index, TableDataRow row){
    	finishEditing();
        TableDataRow set =  model.set(index, row);
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
    
	public void load(ArrayList<TableDataRow> model) {
        selections.clear();
        if(isAttached())
        	renderer.rowUnselected(-1);
        this.model = model;
        shownRows = 0;
        selectedRow = -1;
        selectedCol = -1;
        activeWidget = null;
        
        if(model == null)
        	model = new ArrayList<TableDataRow>();
        
        searchKey = new HashMap<Object,Integer>();
        
        for(int i = 0; i < model.size(); i++){
            if(model.get(i).shown)
                shownRows++;
            searchKey.put(model.get(i).key,i);
        }
        
        for(TableColumn col : columns)
        	col.clearFilter();
        
        //if(isAttached())
        renderer.dataChanged(false);        
    }
	
	public void navSelect(int index) {
        if(multiSelect && ctrlKey && isSelected(index)){
        	unselect(index);
        	selectedCol = -1;
       		//sinkEvents(Event.ONKEYPRESS);
        	return;
        }
		selectRow(index,true);
	}

	public void selectRow(final int index) {
		selectRow(index,false);
	}
	
	public void selectRows(Integer... selections) {
		selectRows(Arrays.asList(selections));
	}
	
	public void selectRows(TableDataRow... selections){
		List<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < selections.length; i++) {
			indexes.add(model.indexOf(selections[i]));
		}
		selectRows(indexes);
	}
	
	public void selectRows(ArrayList<TableDataRow> selections) {
		selectRows((TableDataRow[])selections.toArray());
	}
	
	public void selectRows(List<Integer> selections) {
		if(multiSelect)
			ctrlKey = true;
		for(int i : selections)
			selectRow(i,false);
		ctrlKey = false;
	}
	
    public void selectRow(final int index,boolean fire) {
        if(index > model.size())
            throw new IndexOutOfBoundsException();
        selectedRow = index;
        if(multiSelect && shiftKey){
        	if(selections.size() == 0)
        		selections.add(index);
        	else{
        		Integer max = Collections.max(selections);
        		Integer min = Collections.min(selections);
        		if(index < min) {
        			selections.clear();
        			for(int i = index; i <= min; i++) {
        				selections.add(i);
        			}
        		}else if(index > max) {
        			selections.clear();
        			for(int i = max; i <= index; i++) {
        				selections.add(i);
        			}
        		}
        	}
        }else{
        	if(!multiSelect || (multiSelect && !ctrlKey))
               	selections.clear();
        	selections.add(index);
        	selectedRow = index;
        }
        if(view.isVisible())
        	renderer.dataChanged(true);
        if(fire)
        	SelectionEvent.fire(this,renderer.getRows().get(tableIndex(index)));
    }
    
    public void clearSelections() {
        selections.clear();
        selectedRow = -1;
    }
    
    public ArrayList<TableDataRow> getSelections() {
    	ArrayList<TableDataRow>  sels = new ArrayList<TableDataRow>();
    	for(int index : selections) {
    		sels.add(getRow(index));
    	}
        return sels;
    }

    public ArrayList<TableDataRow> getData() {
        return model;
    }
    
    public void setCell(int row, int col, Object value) {
        getRow(row).cells.get(col).setValue(value);
        if(isRowDrawn(row))
        	renderer.cellUpdated(row, col);
    }
    
    public TableDataCell getCell(int row, int col) {
        return getRow(row).cells.get(col);
    }
    
    public void hideRow(int row) {
        model.get(row).shown = false;
        shownRows--;
        renderer.dataChanged(true);
    }
    
    public void showRow(int row) {
        model.get(row).shown = true;
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
    		Collections.sort(model,new ColumnComparator(col,direction));
    		renderer.dataChanged(false);
    	}
    }
    
    public void refresh() {
        shownRows = 0;
        for(int i = 0; i < model.size(); i++){
            if(model.get(i).shown)
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
            return model.get(index).enabled;
        return false;
    }

    public TableDataRow getSelection() {
        if(selectedRow == -1)
            return null;
        return model.get(selectedRow);
    }

    public ArrayList<TableDataRow> unload() {
        finishEditing();
        return model;
    }
    
    public int getSelectedRow() {
        return selectedRow;
    }
    
    public int getSelectedCol() {
    	return selectedCol;
    }

    public int[] getSelectedRows() {
        int[] ret = new int[selections.size()];
        for(int i = 0; i < selections.size(); i++)
            ret[i] = (Integer)selections.get(i);
        return ret;
    }
   
    
    public void setCellException(int row, int col, Exception ex) {
        model.get(row).cells.get(col).addException(ex);
        if(isRowDrawn(row))
        	renderer.cellUpdated(row, col);
    }
    
    public void setCellException(int row, String col, Exception ex) {
    	for(TableColumn column : columns) {
    		if(col.equals(column.key)){
    			setCellException(row,columns.indexOf(column),ex);
    			break;
    		}
    	}
    }
    
    public void removeCellException(int row, int col, Exception ex) {
    	if(model.get(row).cells.get(col).exceptions != null) {
    		model.get(row).cells.get(col).exceptions.remove(ex);
    		if(model.get(row).cells.get(col).exceptions.size() == 0)
    			model.get(row).cells.get(col).exceptions = null;
    		if(isRowDrawn(row))
    			renderer.cellUpdated(row,col);
    	}
    	
    }
    
    public void clearCellExceptions(int row, int col) {
    	 model.get(row).cells.get(col).clearExceptions();
    	 if(isRowDrawn(row))
    		 renderer.cellUpdated(row, col);
    }
    
    
 
    public void selectRow(Object key) {
        Integer n;

        if(model == null)
    		return;

        if (key == null) {
    		n = 0;
        } else {
            n = searchKey.get(key);
            if (n == null)
                n = 0;
        }
    	selectRow(n.intValue());
    }

	public void addException(Exception exception) {
		// TODO Auto-generated method stub
		
	}

	public void clearExceptions() {
		exceptions = null;
	}
	
	public void clearCellExceptions() {
		for(int i = 0; i < model.size(); i++) {
			for(int j = 0; j < model.get(i).size(); j++)
				((TableDataCell)model.get(i).cells.get(j)).clearExceptions();
		}
		refresh();
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
		if(columns.size() == 0){
			renderer.clear();
			return;
		}
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
				if(model != null && model.size() > 0 && model.get(0).cells.get(columns.indexOf(col)).value != null){
					if(col.colWidget instanceof Dropdown) {
						((Dropdown)col.colWidget).setSelectionKeys((ArrayList<Object>)model.get(0).cells.get(columns.indexOf(col)).value);
					}else if(!(col.colWidget instanceof CalendarLookUp)){
						((HasField)col.getColumnWidget()).setFieldValue(model.get(0).cells.get(columns.indexOf(col)).value);
					}
					((HasField)col.getColumnWidget()).getQuery(list, col.key);
					
				}
			}
		}
	}
	
	public ArrayList<Exception> getExceptions() {
		return exceptions;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
        for(TableColumn column : columns) {
            column.enable(enabled);
        }
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void checkValue() {
		exceptions = null;
		if(model == null)
			return;
		finishEditing();
		for(int i = 0; i < numRows(); i++) {
			for(int j = 0; j < model.get(i).cells.size(); j++){
				Widget wid = columns.get(j).getWidgetEditor(model.get(i));
				if(wid instanceof HasField){
					((HasField)wid).checkValue();
					if(model.get(i).cells.get(j).exceptions != null){
						exceptions = model.get(i).cells.get(j).exceptions;
					}
					if(((HasField)wid).getExceptions() != null){
						exceptions = ((HasField)wid).getExceptions();
	        			ArrayList<Exception> exceps =  new ArrayList<Exception>(); 
	        			for(Exception exc : (ArrayList<Exception>)((HasField)wid).getExceptions()){
	        				exceps.add(new Exception(exc.getMessage()));
	        				if(model.get(i).cells.get(j).exceptions != null){
	        					if(!model.get(i).cells.get(j).getExceptions().contains(exc))
	        						model.get(i).cells.get(j).exceptions.add(exc);
	        				}
	        			}
	        			if(model.get(i).cells.get(j).exceptions == null)
	        				model.get(i).cells.get(j).exceptions = exceps;
	        		}
				}
			}
		}
		if(exceptions != null)
			refresh();
		else if(fireEvents)
			TableValueChangeEvent.fire(this, model);
	}
	
	public Object getFieldValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected boolean canEditCell(int row, int col) {
		if(col < 0)
			return false;
		
		if(columns.get(col).colWidget instanceof PercentBar || (columns.get(col).colWidget instanceof TableImage && !byClick) ) {
			return false;
		}
        if(getHandlerCount(BeforeCellEditedEvent.getType()) > 0) {
        	if(fireEvents) {
        		BeforeCellEditedEvent bce = BeforeCellEditedEvent.fire(this, row, col, getRow(row).cells.get(col).value);
        		if(bce.isCancelled()){
        			return false;
        		}else
        		    return (isEnabled() && columns.get(col).isEnabled());
        	}
        }
       
        if(columns.get(col).colWidget instanceof Label || columns.get(col).colWidget instanceof TableImage)
        	return false;
       
        return (isEnabled() && columns.get(col).isEnabled());
	}

	public void setFieldValue(Object value) {
		// TODO Auto-generated method stub
		
	}
	
    public void enableDrag(boolean drag) {
    	if(drag){
    		if(dragController == null) {
    			dragController = new TableDragController(RootPanel.get());  	
    			dragController.addBeforeStartHandler(new BeforeDragStartHandler<TableRow>() {
					public void onBeforeDragStart(
							BeforeDragStartEvent<TableRow> event) {
						if(isEditing())
							event.cancel();
					}
    			});
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
	
	public void fireEvents(boolean fire) {
		fireEvents = fire;
	}
	
	public void onFocus(FocusEvent event) {
		if(((ScreenPanel)event.getSource()).focused == null || !DOM.isOrHasChild(getElement(),((ScreenPanel)event.getSource()).focused.getElement())){
			finishEditing();
		}
	}
	
	public Object getWidgetValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeExceptionStyle(String style) {
		// TODO Auto-generated method stub
	}
	
	protected void fireFilterEvent() {
		FilterEvent.fire(this);
	}
	
	// Methods for registering Event Handlers to the table.
	
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

	public HandlerRegistration addTableValueChangeHandler(
			TableValueChangeHandler handler) {
		return addHandler(handler, TableValueChangeEvent.getType());
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
	
	public HandlerRegistration addFilterHandler(FilterHandler handler) {
		return addHandler(handler, FilterEvent.getType());
	}
	
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }

	public void addExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}
	
	public HandlerRegistration addBeforeDragStartHandler(BeforeDragStartHandler<TableRow> handler) {
		assert(dragController != null) : new Exception("Enable Dragging first before registering handlers");
		return dragController.addBeforeStartHandler(handler);	
	}
	
	public HandlerRegistration addDagStartHandler(DragStartHandler<TableRow> handler) {
		assert(dragController != null) : new Exception("Enable Dragging first before registerning handlers");
		return dragController.addStartHandler(handler);
	}
	
	public HandlerRegistration addBeforeDropHandler(BeforeDropHandler<TableRow> handler) {
		assert(dropController != null) : new Exception("Enable Dropping first before registering handlers");
		return dropController.addBeforeDropHandler(handler);
	}
	
	public HandlerRegistration addDropHandler(DropHandler<TableRow> handler) {
		assert(dropController != null) : new Exception("Enable Dropping first before registering handlers");
		return dropController.addDropHandler(handler);
	}

	public HandlerRegistration addNavigationSelectionHandler(
			NavigationSelectionHandler handler) {
		return addHandler(handler,NavigationSelectionEvent.getType());
	}
 	
	public boolean getCtrlKey() {
		return ctrlKey;
	}
	
	public boolean getShiftKey(){
		return shiftKey;
	}
}
