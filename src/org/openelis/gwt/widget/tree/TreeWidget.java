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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.openelis.ui.common.Util;
import org.openelis.gwt.event.BeforeDragStartEvent;
import org.openelis.gwt.event.BeforeDragStartHandler;
import org.openelis.gwt.event.BeforeDropHandler;
import org.openelis.gwt.event.DragStartHandler;
import org.openelis.gwt.event.DropEnterHandler;
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
import org.openelis.gwt.widget.table.ColumnComparator;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableDataCell;
import org.openelis.gwt.widget.table.TableDataRow;
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
import org.openelis.gwt.widget.table.event.HasUnselectionHandlers;
import org.openelis.gwt.widget.table.event.RowAddedEvent;
import org.openelis.gwt.widget.table.event.RowAddedHandler;
import org.openelis.gwt.widget.table.event.RowDeletedEvent;
import org.openelis.gwt.widget.table.event.RowDeletedHandler;
import org.openelis.gwt.widget.table.event.RowMovedEvent;
import org.openelis.gwt.widget.table.event.RowMovedHandler;
import org.openelis.gwt.widget.table.event.SortEvent;
import org.openelis.gwt.widget.table.event.SortHandler;
import org.openelis.gwt.widget.table.event.UnselectionEvent;
import org.openelis.gwt.widget.table.event.UnselectionHandler;
import org.openelis.gwt.widget.table.event.SortEvent.SortDirection;
import org.openelis.gwt.widget.tree.TreeView.VerticalScroll;
import org.openelis.gwt.widget.tree.event.BeforeLeafCloseEvent;
import org.openelis.gwt.widget.tree.event.BeforeLeafCloseHandler;
import org.openelis.gwt.widget.tree.event.BeforeLeafOpenEvent;
import org.openelis.gwt.widget.tree.event.BeforeLeafOpenHandler;
import org.openelis.gwt.widget.tree.event.HasBeforeLeafCloseHandlers;
import org.openelis.gwt.widget.tree.event.HasBeforeLeafOpenHandlers;
import org.openelis.gwt.widget.tree.event.HasLeafClosedHandlers;
import org.openelis.gwt.widget.tree.event.HasLeafOpenedHandlers;
import org.openelis.gwt.widget.tree.event.LeafClosedEvent;
import org.openelis.gwt.widget.tree.event.LeafClosedHandler;
import org.openelis.gwt.widget.tree.event.LeafOpenedEvent;
import org.openelis.gwt.widget.tree.event.LeafOpenedHandler;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
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

public class TreeWidget extends FocusPanel implements FocusHandler, 
													  BlurHandler, 
													  ClickHandler, 
													  HasField, 
													  MouseOverHandler, 
													  MouseOutHandler,
													  HasBeforeSelectionHandlers<TreeDataItem>,
													  HasSelectionHandlers<TreeDataItem>,
													  HasUnselectionHandlers<TreeDataItem>,
													  HasBeforeCellEditedHandlers,
													  HasCellEditedHandlers, 
													  HasBeforeRowAddedHandlers,
													  HasRowAddedHandlers, 
													  HasBeforeRowDeletedHandlers,
													  HasRowDeletedHandlers,
													  HasBeforeLeafOpenHandlers,
													  HasBeforeLeafCloseHandlers,
													  HasLeafOpenedHandlers,
													  HasLeafClosedHandlers,
													  HasDropController,
													  HasBeforeSortHandlers,
													  HasSortHandlers,
													  HasBeforeRowMovedHandlers,
													  HasRowMovedHandlers{


    protected HashMap<String,ArrayList<TreeColumn>> columns;
    protected boolean enabled;
    protected boolean focused;
    protected int selectedRow = -1;
    protected int selectedCol = -1;
    protected TreeView view;
    protected TreeRenderer renderer;
    protected TreeKeyboardHandler keyboardHandler;
    protected boolean shiftKey;
    protected boolean ctrlKey;
    protected int maxRows;
    protected int cellHeight = 21;
    protected int cellSpacing = 0;
    protected Widget activeWidget = null;
    protected int[] rowIndexList;
    protected boolean showRows;
    protected String title;
    protected boolean showHeader;
    protected TreeDragController dragController;
    protected TreeIndexDropController dropController;
    protected boolean selectedByClick;
    protected ArrayList<TreeDataItem> data;
    protected ArrayList<TreeDataItem> rows = new ArrayList<TreeDataItem>();
    protected int shownRows; 
    protected boolean multiSelect;
    protected VerticalScroll showScroll;
   // public ArrayList<Integer> selectedRows = new ArrayList<Integer>();
    protected HashMap<String,TreeDataItem> leaves;
    protected String width;
    protected ArrayList<TreeDataItem> deleted;// = new ArrayList<DataSet<Key>>();
    protected ArrayList<Integer> selections = new ArrayList<Integer>(1);
    protected int selected = -1;
    protected ArrayList<TreeColumn> headers;
    protected boolean fireEvents = true;
    protected boolean queryMode;
    ArrayList<Exception> exceptions;
    
    public TreeWidget() {

    }
    
    
    public void init() {
        renderer = new TreeRenderer(this);
        keyboardHandler = new TreeKeyboardHandler(this);
        view = new TreeView(this, showScroll);
        view.setWidth(width);
        view.setHeight(maxRows * cellHeight);
        setWidget(view);
        addDomHandler(keyboardHandler, KeyUpEvent.getType());
        addDomHandler(keyboardHandler, KeyDownEvent.getType());        
    }
    
    public void setTreeWidth(String width) {
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
    
    public HashMap<String,ArrayList<TreeColumn>> getColumns() {
    	return columns;
    }
    
    public void setHeaders(ArrayList<TreeColumn> headers) {
    	this.headers = headers;
    }
    
    public ArrayList<TreeColumn> getHeaders() {
    	return headers;
    }
    
    public void setColumns(HashMap<String,ArrayList<TreeColumn>> columns) {
    	this.columns = columns;
    }
    
    /**
     * This method is called be TreeViewHeader for accurate resizing
     * @return
     */
    protected int getTreeWidth() {
    	int tw = 0;
    	if(width.equals("auto")){
    		for(TreeColumn column : headers)
    			tw += column.getCurrentWidth();
    	}else
    		tw = Util.stripUnits(width, "px");
    	
    	return tw;
    }
    
    /**
     * This method handles all click events on the body of the table
     */
    public void onClick(ClickEvent event) {
    	if(event.getNativeEvent().getCtrlKey())
    		ctrlKey = true;
    	else
    		ctrlKey = false;
    	if(event.getNativeEvent().getShiftKey())
    		shiftKey = true;
    	else
    		shiftKey = false;
    	if(event.getSource() == view.table) {
    		Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);
           
    		if(treeIndex(selectedRow) == cell.getRowIndex() && selectedCol == cell.getCellIndex())
                return;
            select(rowIndexList[cell.getRowIndex()], cell.getCellIndex(),true);
		}
    	ctrlKey = false;
    	shiftKey = false;
    }


    /**
     * Pass -1 to this method to unselect all rows in the tree.  This method will fire an UnselectionEvent
     * if a current item is selected and an UnselectionHandler is registered to the tree. UnselectEvent will be
     * passed a null candidate row when -1 is passed.
     */
    public void unselect(int row) {
        finishEditing();
        if(row == -1){
        	if(selections.size() > 0) {
            	if(fireEvents && getHandlerCount(UnselectionEvent.getType())> 0) {
            		UnselectionEvent event = UnselectionEvent.fire(this, rows.get(selections.get(0)), null);
            		if(event != null && event.isCanceled())
            			return;
            	}
            }
        	for(int i : selections) {
        		rows.get(i).selected = false;
        	}
            selections.clear();
            renderer.rowUnselected(-1);
        }else {
            selections.remove(new Integer(row));
        }
        if(selectedRow > -1)
        	rows.get(selectedRow).selected = false;
        selectedRow = -1;
        if(isRowDrawn(row))
        	renderer.rowUnselected(row);
    }
    
    /**
     * This method will determine if a specific TreeDataItem by its rowIndex[0..lastReachableItem] is currently visible 
     * in the tree view.
     * @param row
     * @return
     */
    protected boolean isRowDrawn(int row){
    	return row >= rowIndexList[0] && row <= rowIndexList[view.table.getRowCount()-1];
    }
    
    /**
     * This method will determine the physical tree index[0..maxRow] of a TreeDataItem by its
     * rowIndex[0..lastReachableItem].  If the the row is currently not drawn on the screen this method will return a -1 
     * @param modelIndex
     * @return
     */
    protected int treeIndex(int rowIndex) {
    	for(int i = 0; i < view.table.getRowCount(); i++){
    		if(rowIndexList[i] == rowIndex)
    			return i;
    	}
    	return -1;
    }
    
    /**
     * This method will return the rowIndex[0..lastReachableItem] of the given TreeDataItem.  
     * Will return -1 if the TreeDataItem is not currently in the visible rows.
     * 
     * @param item
     * @return
     */
    public int getRowIndex(TreeDataItem item) {
    	return rows.indexOf(item);
    }
    
    /**
     * Method used by lib classes to start editing in a cell.
     * @param row
     * @param col
     */
    protected void select(int row, int col) {
    	select(row,col,false);
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
    	if(selectedRow != row) {
    		if(!multiSelect || (multiSelect && !shiftKey && !ctrlKey)) {
    			while(selections.size() > 0) {
    				int index = selections.get(0);
    				if(fireEvents) {
    					UnselectionEvent event = UnselectionEvent.fire(this, rows.get(index), rows.get(row));
    					if(event != null && event.isCanceled())
    						return;
    				}
    				unselect(index);
    			}
    		}
    		
    		if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
    			BeforeSelectionEvent<TreeDataItem> event = BeforeSelectionEvent.fire(this, rows.get(row));
    			if(event.isCanceled())
    				return;
    		}else if(!isEnabled())
    			return;
    	}
        finishEditing();
        if(multiSelect && ctrlKey && isSelected(row)){
        	unselect(row);
        	selectedCol = -1;
       		sinkEvents(Event.ONKEYPRESS);
        	return;
        }
        if(selectedRow != row){
            selectRow(row);
            if(fireEvents)
            	SelectionEvent.fire(this,rows.get(row));
        }
        if(isEnabled() && canEditCell(row,col)){
        	if(byClick && columns.get(rows.get(row).leafType).get(col).getColumnWidget() instanceof CheckBox && !shiftKey && !ctrlKey){
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
           		sinkEvents(Event.ONKEYPRESS);
        	}else{
        		selectedCol = col;
        		renderer.setCellEditor(row, col);
        		unsinkEvents(Event.ONKEYPRESS);
        	}
        }else{
      		selectedCol = -1;
       		sinkEvents(Event.ONKEYPRESS);
       	}

    }
   
    
    /**
     *  This method will select all rows in the model, but only if the table is in 
     *  MultiSelect mode.
     */
    public void selectAll() {
    	if(multiSelect) {
    		selections = new ArrayList<Integer>();
    		for(int i = 0; i < rows.size(); i++)
    			selections.add(i);
    		renderer.dataChanged(true);
    	}
    }
    
    /** 
     * This method will select a TreeDataItem based on its rowIndex[0..lastReachableItem].  The row does not need
     * to be currently drawn in the TreeView. 
     * @param index
     */
    public void select(int rowIndex) {
    	if(selectedRow != rowIndex) {
  			while(selections.size() > 0) {
    			int index = selections.get(0);
    			if(fireEvents) {
    				UnselectionEvent event = UnselectionEvent.fire(this, rows.get(index), rows.get(rowIndex));
    				if(event != null && event.isCanceled())
    					return;
    			}
    			unselect(index);
    		}
    		
    		if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
    			BeforeSelectionEvent<TreeDataItem> event = BeforeSelectionEvent.fire(this, rows.get(rowIndex));
    			if(event.isCanceled())
    				return;
    		}else if(!isEnabled())
    			return;
    	}
    	selectRow(rowIndex);
    	if(fireEvents)
    		SelectionEvent.fire(this, getSelection());
    }
    
    /**
     * This method will select the TreeDataItem passed.  If the passed item is not currently visible
     * becuase its parent nodes are closed, the method will will recursively open all parent nodes until the 
     * item is added to the visible list of rows.  if you need to make sure this item is also drawn on the
     * screen, call scrollToSelection() after this method.
     * @param item
     */
    public void select(TreeDataItem item) {
    	if(item.parent != null && !item.parent.open) {
    		TreeDataItem parent = item.parent;
    		while(!parent.open){
    			parent.open = true;
    			if(parent.parent != null)
    				parent = parent.parent;
    		}
    		refreshRow(parent);
    	}
    	select(rows.indexOf(item));
    }

    /**
     * This method will stop editing the active cell and flip it to it display state and fire any valueChangeEvents
     * if necessary.
     */
    public void finishEditing() {
        if (activeWidget != null) {
        	if(renderer.stopEditing() && fireEvents)
        		CellEditedEvent.fire(this,selectedRow, selectedCol, getRow(selectedRow).cells.get(selectedCol).value);
        	selectedCol = -1;
            sinkEvents(Event.KEYEVENTS);
        }
    }

    /**
     * This method can be called to flip a cell to its active editing state.  Pass the rowIndex[0..lastReachableItem], if the 
     * row is not currently displayed on the screen this method will do nothing.  
     * @param row
     * @param col
     */
    public void startEditing(int row, int col) {
    	if(isRowDrawn(row))
    		select(row, col,false);
    }
    
    /**
     * This method can be called to flip a cell to its active editing state.  if the item passed is currently not 
     * displayed on the screen this method will do nothing.
     * @param item
     * @param col
     */
    public void startEditing(TreeDataItem item, int col) {
    	startEditing(getRowIndex(item),col);
    }


    /**
     * Enables the tree and columns to be edited.  Make sure column definitions are set before calling
     * this method.
     */
	public void enable(boolean enabled) {
		this.enabled = enabled;
        for(ArrayList<TreeColumn> leaf : columns.values()) {
        	for(TreeColumn column : leaf) {
        		column.enable(enabled);
        	}
        }
	}

	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * This method is coded to determine if the user has clicked outside the tree and will call finishEditing.
	 */
	public void onFocus(FocusEvent event) {
		if(((ScreenPanel)event.getSource()).focused == null || !DOM.isOrHasChild(getElement(),((ScreenPanel)event.getSource()).focused.getElement())){
			finishEditing();
		}
	}


	public void onBlur(BlurEvent event) {

	}

	/**
	 * Scrolls the tree to the first selected item in the tree.  Selection will be at the top of the tree regardless 
	 * of its current position in the tree. Will finishEditng an activeCell before scrolling.
	 */
    public void scrollToSelection() {
    	if(isRowDrawn(selectedRow))
    		return;
    	finishEditing();
        if(numRows() == shownRows()){
            view.scrollBar.setScrollPosition(cellHeight*selectedRow);
        }else{
            int shownIndex = 0;
            for(int i = 0; i < selectedRow; i++){
                if(getRow(i).shown)
                    shownIndex++;
            }
            view.scrollBar.setScrollPosition(cellHeight*shownIndex);
        }
    }
    
	/**
	 * Scrolls the tree to the first selected item in the tree.  If the selection is off the top the selected Item will 
	 * be at the top of the tree.  If the item is off the bottom the selection will the last item in the tree view. 
	 * WillfinishEditng an activeCell before scrolling.
	 */
    public void scrollToVisible() {
    	if(isRowDrawn(selectedRow))
    		return;
    	finishEditing();
        if(numRows() == shownRows()){
        	if(selectedRow > rowIndexList[maxRows-1])
        		view.scrollBar.setScrollPosition((cellHeight*selectedRow)-(cellHeight*(maxRows-1)));
        	else
        		view.scrollBar.setScrollPosition(cellHeight*selectedRow);
        }else{
            int shownIndex = 0;
            for(int i = 0; i < selectedRow; i++){
                if(getRow(i).shown)
                    shownIndex++;
            }
            if(selectedRow > rowIndexList[maxRows-1])
            	view.scrollBar.setScrollPosition((cellHeight*shownIndex)-(cellHeight*(maxRows-1)));
            else
            	view.scrollBar.setScrollPosition(cellHeight*shownIndex);
        }
    }
    
    /**
     * Moves a TreeDataItem from its currentRowIndex[0..lastReachableItem] to the newRowIndex[0..lastReachableItem]
     * @param curIndex
     * @param newIndex
     */
    public void moveRow(int curIndex, int newIndex) {
    	if(fireEvents) {
    		if(getHandlerCount(BeforeRowMovedEvent.getType()) > 0){
    			BeforeRowMovedEvent<TreeDataItem> event = BeforeRowMovedEvent.fire(this, curIndex, newIndex, rows.get(curIndex));
    			if(event != null && event.isCancelled())
    				return;
    		}
    	}
        TreeDataItem item = rows.get(curIndex);
        if(item.parent != null){
            item.parent.removeItem(item.childIndex);
        }else{
            data.remove(rows.get(curIndex).childIndex);
        }
        int index = rows.indexOf(item);
        while(index+1 < rows.size() && item.isDecendant(rows.get(index+1))){	
    		TreeDataItem itemc = rows.get(index+1);
    		rows.remove(index+1);
    		if(itemc.shown)
    			shownRows--;
        }
        if(item.shown)
        	shownRows--;
        rows.remove(index);
    	int insert = newIndex;
    	if(newIndex > curIndex) 
    		insert--;
    	if(insert >= rows.size())
    		insert = rows.size();
    	item.depth = 0;
    	item.parent = null;
    	int rowindex = rows.indexOf(data.get(insert));
        data.add(insert,item);
        ArrayList<TreeDataItem> added = new ArrayList<TreeDataItem>();
        checkChildItems(item,added);
       	rows.addAll(rowindex, added);
       	
       	for(int i = insert; i < data.size(); i++)
       		data.get(i).childIndex = i;
    	if(fireEvents) 
    		RowMovedEvent.fire(this, curIndex, newIndex, item);
    	renderer.dataChanged(true);
    }
    
    /**
     * Adds the passed item to end of the data model as a TopLevel tree node.
     * @param row
     */
    public void addRow(TreeDataItem item) {
    	finishEditing();
    	if(fireEvents) {
    		BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, data.size(), item);
    		if(event != null && event.isCancelled())
    			return;
    	}
        data.add(item);
        checkChildItems(item,rows);
        renderer.dataChanged(true);
        item.childIndex = data.size()-1;
        if(fireEvents)
        	RowAddedEvent.fire(this, data.size()-1, item);
    }

    /**
     * Inserts the passed item to the data model as a TopLevel tree node at the passed index
     * @param index
     * @param item
     */
    public void addRow(int index, TreeDataItem item) {
    	finishEditing();
    	if(fireEvents){
    		BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, index, item);
    		if(event != null && event.isCancelled())
    			return;
    	}
    	int rowindex = rows.indexOf(data.get(index));
        data.add(index,item);
        ArrayList<TreeDataItem> added = new ArrayList<TreeDataItem>();
        checkChildItems(item,added);
       	rows.addAll(rowindex, added);
       	for(int i = index; i < data.size(); i++) {
       		data.get(i).childIndex = i;
       	}
       	if(fireEvents)
       		RowAddedEvent.fire(this, index, item);  	
    	renderer.dataChanged(true);
    }
    
    /**
     * Adds the passed Child Item to the passed Parent item at the end of its children list and will refresh and render the tree 
     * for the cahnges.
     * @param parent
     * @param child
     */
    public void addChildItem(TreeDataItem parent, TreeDataItem child) {
    	parent.addItem(child);
    	if(parent.open)
    		refreshRow(parent);
    	if(fireEvents)
    		RowAddedEvent.fire(this,-1, child);
    }
    
    /**
     * Inserts the passed Child Item to the passed Parent item at the index supplied and will refresh and render the tree 
     * for the cahnges.
     * @param parent
     * @param child
     */
    public void addChildItem(TreeDataItem parent, TreeDataItem child, int index) {
    	parent.addItem(index, child);
    	if(parent.open)
    		refreshRow(parent);
    	if(fireEvents)
    		RowAddedEvent.fire(this, -1, child);
    }
    
    /**
     * Removes the passed Child Item from the passed Parent Item and will refersh and render the tree for the changes.
     * @param parent
     * @param child
     */
    public void removeChild(TreeDataItem parent, TreeDataItem child) {
    	unselect(-1);
    	parent.removeItem(parent.getItems().indexOf(child));
    	if(parent.open)
    		refreshRow(parent);
    }
    
    /**
     * Clears the tree and and its data model.
     */
    public void clear() {
    	if(data != null)
    		data.clear();
    	if(rows != null)
    		rows.clear();
    	if(selections != null)
    		selections.clear();
    	selectedRow = -1;
    	selectedCol = -1;
    	activeWidget = null;
    	shownRows = 0;
        rows = new ArrayList<TreeDataItem>();
        renderer.dataChanged(false);
    }

    /**
     * Clears all selections without firing UnselectEvents.
     */
    protected void clearSelections() {
        for(int i : selections){
            rows.get(i).selected = false;
        }
        selections.clear();
        selectedRow = -1;
        selectedCol = -1;
    }

    /**
     * Deletes the passed item from the tree and will refresh and render the tree for the change.
     * @param item
     */
    public void deleteRow(TreeDataItem item) {
    	deleteRow(rows.indexOf(item));
    }

    /**
     * Deletes the passed item from the tree by its rowIndex[0..lastReachableItem] and will refresh
     * and render the tree for the change
     * @param row
     */
    public void deleteRow(int row) {
    	unselect(-1);
    	if(fireEvents) {
    		BeforeRowDeletedEvent event = BeforeRowDeletedEvent.fire(this, row, getRow(row));
    		if(event != null && event.isCancelled())
    			return;
    	}
        TreeDataItem item = rows.get(row);
        if(item.parent != null){
            item.parent.removeItem(item.childIndex);
        }else{
            data.remove(rows.get(row).childIndex);
        }
        int index = rows.indexOf(item);
        while(index+1 < rows.size() && item.isDecendant(rows.get(index+1))){	
    		TreeDataItem itemc = rows.get(index+1);
    		rows.remove(index+1);
    		if(itemc.shown)
    			shownRows--;
        }
        if(item.shown)
        	shownRows--;
        rows.remove(index);
        for(int i = 0; i < data.size(); i++) {
        	data.get(i).childIndex = i;
        }
        renderer.dataChanged(true);
        if(fireEvents)
        	RowDeletedEvent.fire(this, row, item);
    }
    
    /**
     * Deletes the passed list of rowIndexes[0..lastReachableItem] from the tree. Will refresh and 
     * render the tree for the changes.
     * @param rowIndexes
     */
    public void deleteRows(List<Integer> rowIndexes) {
    	unselect(-1);
        Collections.sort(rowIndexes);
        Collections.reverse(rowIndexes);
        for(int row : rowIndexes) {
            TreeDataItem item = rows.get(row);
            if(item.parent != null){
                item.parent.removeItem(item.childIndex);
            }else{
                data.remove(rows.get(row).childIndex);
            }
            int index = rows.indexOf(item)+1;
            while(item.isDecendant(rows.get(index))){
        		TreeDataItem itemc = rows.get(index+1);
        		rows.remove(index+1);
        		if(itemc.shown)
        			shownRows--;
            }
            if(item.shown)
            	shownRows--;
            rows.remove(index);
        }
        renderer.dataChanged(true);
    }

    public void enableMultiSelect(boolean multi) {
        this.multiSelect = multi;
    }

    /**
     * Returns the model of the TopLevel tree nodes.
     * @return
     */
    public ArrayList<TreeDataItem> getData() {
        return data;
    }

    /**
     * Returns the value of of a given cell of TreeDataItem.  row is the rowIndex[0..lastReachableItem] of the item.
     * @param row
     * @param col
     * @return
     */
    public Object getObject(int row, int col) {
    	Object obj = rows.get(row).cells.get(col).getValue();
    	if(obj instanceof ArrayList) {
    		if(((ArrayList)obj).size() == 1)
    			return ((ArrayList)obj).get(0);
    		else if(((ArrayList)obj).size() == 0)
    			return null;
    	}
        return obj;
    }


    /**
     * Returns the TreeDataItem at the passed rowIndex[0..lastReachableItem]
     * @param row
     * @return
     */
    public TreeDataItem getRow(int row) {
        return rows.get(row);
    }

    /**
     * Returns the currently selcted TreeDataItem
     * @return
     */
    public TreeDataItem getSelection() {
    	if(selections == null || selections.size() == 0)
    		return null;
        return rows.get(selections.get(0));
    }

    /**
     * Returns a list of currently selected TreeDataItems
     * @return
     */
    public ArrayList<TreeDataItem> getSelections() {
        ArrayList<TreeDataItem> selected = new ArrayList<TreeDataItem>();
        for(int i : selections) {
            selected.add(rows.get(i));
        }
        return selected;
    }
    
    /**
     * Returns the rowIndex[0..lastReachableItem] of the currently selected item
     * @return
     */
    public int getSelectedRow() {
        return selectedRow;
    }

    /**
     * Will hide the TreeDataItem at the passed rowIndex[0..lastReachableItem]
     * @param row
     */
    public void hideRow(int row) {
        rows.get(row).shown = false;
        shownRows--;
        renderer.dataChanged(true);
    }

    /**
     * Determines if the TreeDataItem is enabled for editing at the passed
     * rowIndex[0..lastReachableItem] 
     * @param index
     * @return
     */
    public boolean isEnabled(int index) {
        if(index < numRows())
            return rows.get(index).enabled;
        return false;
    }

    /**
     * Determines if the TreeDataItem is selected at the passed rowIndex[0..lastReachableItem]
     * @param index
     * @return
     */
    public boolean isSelected(int index) {
        return selections.contains(index);
    }

    /**
     * Loads a new model of TopLevel tree nodes into the tree completly replacing any existing model
     */
    public void load(ArrayList<TreeDataItem> data) {
    	finishEditing();
        this.data = data;
        shownRows = 0;
        getReachableRows();
        selections.clear();
        selectedRow = -1;
        selectedCol = -1;
        renderer.dataChanged(false);
    }

    /**
     * returns current number of ReachableItems in the tree. 
     * @return
     */
    public int numRows() {
        return rows.size();
    }
        
    /**
     * private method to handle the selection with multiSelection and rendering of selected rows
     * @param index
     */
    private void selectRow(int index){
        if(index > rows.size())
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
        if(isRowDrawn(index) && view.isVisible())
        	renderer.dataChanged(true);
    }
    
    /**
     * MultiSelection method that will select all items in the passed parameters of rowIndexes[0..lastReachableItem]
     * Throws assertion error if tree is not in multiSelect mode;
     * @param selections
     */
	public void selectRows(Integer... selections) {
		assert multiSelect == true;
		selectRows(Arrays.asList(selections));
	}
	
	/**
     * MultiSelection method that will select all items in the passed parameters of items
     * Throws assertion error if tree is not in multiSelect mode;
	 * @param selections
	 */
	public void selectRows(TreeDataItem... selections){
		assert multiSelect == true;
		List<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < selections.length; i++) {
			indexes.add(rows.indexOf(selections[i]));
		}
		selectRows(indexes);
	}
	
	/**
     * MultiSelection method that will select all items in the passed list of items
     * Throws assertion error if tree is not in multiSelect mode;
	 * @param selections
	 */
	public void selectRows(ArrayList<TreeDataItem> selections) {
		assert multiSelect == true;
		selectRows((TreeDataItem[])selections.toArray());
	}
	
	/**
     * MultiSelection method that will select all items in the passed list of rowIndexes[0..lastReachableItem] 
     * Throws assertion error if tree is not in multiSelect mode;
	 * @param selections
	 */	
	public void selectRows(List<Integer> selections) {
		assert multiSelect == true;
		ctrlKey = true;
		for(int i : selections)
			select(i);
		ctrlKey = false;
	}

	/*
    public void setModel(ArrayList<TreeDataItem> data) {
        this.data = data;  
    }
    */

	/**
	 * Will make an item visible if currently hidden 
	 */
    public void showRow(int row) {
    	if(!rows.get(row).shown) {
    		rows.get(row).shown = true;
    		shownRows++;
    		renderer.dataChanged(true);
    	}
    }

    /**
     * Returns the number of shownRows which is a subset of reachable rows in the model
     * @return
     */
    public int shownRows() {
        return shownRows;
    }

    /**
     * This method will finishEditing the active cell and return the model of top level nodes
     * @return
     */
    public ArrayList<TreeDataItem> unload() {
    	finishEditing();
        return data;
    }

   /*
    public void unselectRow(int index){
        if(index < 0) {
            clearSelections();
        }else if(index < numRows()){
            rows.get(index).selected = false;
            selections.remove(new Integer(index));
        }
        renderer.rowUnselected(-1);
    }
    */
    
    /**
     * Sets the value of a cell in the item at the passed rowIndex[0..lastReachableItem] and column.  
     */
    public void setCell(int row, int col,Object value) {
        rows.get(row).cells.get(col).setValue(value);
        if(isRowDrawn(row))
        	renderer.cellUpdated(row, col);
    }

    /**
     * Returns the Cell at the passed rowIndex[0..lastReachableItem] and column
     * @param row
     * @param col
     * @return
     */
    public TableDataCell getCell(int row, int col) {
        return rows.get(row).cells.get(col);
    }

    /**
     * Replaces the item at the passesd rowIndex[0..lastReachableItem] with the passed item
     * @param index
     * @param newItem
     */
    public void setRow(int index, TreeDataItem newItem) {
    	int rowindex = rows.indexOf(data.get(index));
        data.set(index, newItem);
        TreeDataItem item = rows.get(rowindex);
        while(item.isDecendant(rows.get(rowindex+1))){
    		TreeDataItem itemc = rows.get(rowindex+1);
    		rows.remove(index+1);
    		if(itemc.shown)
    			shownRows--;
    	}
        if(item.shown)
        	shownRows--;
    	rows.remove(rowindex);
        ArrayList<TreeDataItem> added = new ArrayList<TreeDataItem>();
        checkChildItems(newItem,added);
        rows.addAll(rowindex,added);
        newItem.childIndex = index;
        renderer.dataChanged(true);
    }
    
    /**
     * Will toggle the passed TreeDataItem.  Will also select that item if not currently selected.
     * @param item
     */
    public void toggle(TreeDataItem item) {
    	toggle(rows.indexOf(item));
    }
    
    /**
     * Will toggle the treeDataItem at the passed in rowIndex[0..lastReachableItem].  Will also select the item if not
     * currently selected.
     * @param row
     */
    public void toggle(int row) {
    	finishEditing();
       	if(selectedRow != row) {
       		if(!multiSelect || (multiSelect && !shiftKey && !ctrlKey)) {
       			while(selections.size() > 0) {
       				int index = selections.get(0);
       				if(fireEvents) {
       					UnselectionEvent event = UnselectionEvent.fire(this, rows.get(index), rows.get(row));
       					if(event != null && event.isCanceled())
       						return;
       				}
       				unselect(index);
       			}
       		}
       		
       		if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
       			BeforeSelectionEvent<TreeDataItem> event = BeforeSelectionEvent.fire(this, rows.get(row));
       			if(event.isCanceled())
       				return;
       		}else if(!isEnabled())
       			return;
           	selections.add(row);
     		rows.get(row).selected = true;
     		renderer.rowSelected(row);
         	selectedRow = row;
       	}
       	
   		if(fireEvents)
   			SelectionEvent.fire(this, rows.get(row));

    	if(!rows.get(row).open) {
    		if(fireEvents){
    			BeforeLeafOpenEvent event = BeforeLeafOpenEvent.fire(this, row, rows.get(row));
    			if(event != null && event.isCancelled())
    				return;
    		}
    		rows.get(row).open = true;
    		refreshRow(row);
    		if(fireEvents)
    			LeafOpenedEvent.fire(this, row, rows.get(row));
    	}else{
    		if(fireEvents){
    			BeforeLeafCloseEvent event = BeforeLeafCloseEvent.fire(this, row, rows.get(row));
    			if(event != null && event.isCancelled())
    				return;
    		}
    		rows.get(row).close();
    		refreshRow(row);
    		if(fireEvents)
    			LeafClosedEvent.fire(this, row, rows.get(row));
    	}
    }
    
    /** 
     * Returns the rowIndex[0..lastReachableItem] of the first selcted row in the tree
     * @return
     */
    public int getSelectedRowIndex() {
        if(selections.size() == 0)
            return -1;
        return selections.get(0);
    }
    
    /**
     * Returns an int[] of selected rowIndexes[0..lastReachableItem] in the tree.
     * @return
     */
    public int[] getSelectedRowIndexes() {
        int[] ret = new int[selections.size()];
        for(int i = 0;  i < selections.size(); i++)
            ret[i] = selections.get(i);
        return ret;
    }

    /**
     * Returns an ArrayLis<Integer> of the selected rowIndexes[0..lastReachableItem]
     * @return
     */
    public ArrayList<Integer> getSelectedRowList() {
        return selections;
    }
    
    /**
     * Clears exceptions for the cell at the passed rowIndex[0..lastReachableItem]  and column
     * @param row
     * @param col
     */
    public void clearCellExceptions(int row, int col) {
        rows.get(row).cells.get(col).clearExceptions();
        renderer.cellUpdated(row, col);
    }

    /**
     * Adds the passed exception to the cell at the passed rowIndex[0..lastReachableItem] and column
     * @param row
     * @param col
     * @param ex
     */
    public void setCellException(int row, int col, Exception ex) {
        rows.get(row).cells.get(col).addException(ex);
        if (isRowDrawn(row))
        	renderer.cellUpdated(row, col);
        
    }
    
    /**
     * Clears exceptions for the cell at the passed rowIndex[0..lastReachableItem] and column key
     * @param row
     * @param col
     */    
    public void clearCellExceptions(int row, String col) {
    	int index = -1;
    	for(TreeColumn column : columns.get(getRow(row).leafType)) {
    		if(column.key.equals(col)){
    			index = columns.get(getRow(row).leafType).indexOf(column);
    			rows.get(row).cells.get(index).clearExceptions();
    			break;
    		}
    	}
        renderer.cellUpdated(row, index);
    }
    
    /**
     * Adds the passed exception to the cell at the passed rowIndex[0..lastReachableItem] and column key
     * @param row
     * @param col
     * @param ex
     */
    public void setCellException(int row, String col, Exception ex) {
    	int index = -1;
    	for(TreeColumn column : columns.get(getRow(row).leafType)) {
    		if(column.key.equals(col)){
    			index = columns.get(getRow(row).leafType).indexOf(column);
    			rows.get(row).cells.get(index).addException(ex);
    			break;
    		}
    	}
    	if (isRowDrawn(row))
    		renderer.cellUpdated(row, index);
        
    }
    
    /**
     * Creates and returns a TreeDataItem of the passed leaf type
     * @param leafType
     * @return
     */
    public TreeDataItem createTreeItem(String leafType) {
    	TreeDataItem td = new TreeDataItem(columns.get(leafType).size());
    	td.leafType = leafType;
    	return td;
    }
    
    /**
     * Sets the defined Hash of leaf types that defines the TreeColumns to the tree.
     * @param leaves
     */
    public void setLeaves(HashMap<String,TreeDataItem> leaves){
        this.leaves = leaves;
    }
    
    /**
     * This method will open all top level nodes in the model and also recurse through all children to open them and make
     * every item in the tree reachable and be displayed in the tree.  It will also cause all current selections to be lost
     * and the tree scrolled to the top.
     */
    public void expand() {
    	unselect(-1);
        rows = new ArrayList<TreeDataItem>();
        shownRows = 0;
        if(data == null)
        	return;
        for(int i = 0; i < data.size(); i++) {
        	TreeDataItem item = data.get(i);
        	item.childIndex = i;
        	openChildItems(item,rows);
        }
        refresh(false);
    }
    
    /**
     * This method will close all top level nodes in the model and also recurse through all children to close them. 
     * Only top level nodes will be reachable. It will also cause all current selections to be lost
     * and the tree scrolled to the top.
     */
    public void collapse() {
    	unselect(-1);
        rows = new ArrayList<TreeDataItem>();
        shownRows = 0;
        if(data == null)
        	return;
        for(int i = 0; i < data.size(); i++) {
        	TreeDataItem item = data.get(i);
        	item.childIndex = i;
        	rows.add(item);
        	shownRows++;
        	closeChildItems(item,rows);
        }
        refresh(false);
    }
    
    /**
     * Method called for recursively opening child items
     * @param item
     * @param rows
     */
    private void openChildItems(TreeDataItem item, ArrayList<TreeDataItem> rows) {
    	 item.open = true;
    	 rows.add(item);
         if(item.shown)
         	shownRows++;
         if(item.shownItems() > 0) {
            Iterator<TreeDataItem> it = item.getItems().iterator();   
            while(it.hasNext())
                openChildItems(it.next(),rows);
         }
    }
    
    /**
     * Method called for recursively closing child items
     * @param item
     * @param rows
     */
    private void closeChildItems(TreeDataItem item, ArrayList<TreeDataItem> rows) {
   	 item.open = false;
     //if(item.shown)
       //	shownRows++;
     Iterator<TreeDataItem> it = item.getItems().iterator();   
     while(it.hasNext())
         closeChildItems(it.next(),rows);
   }
    
    /*
    public void collapse(TreeDataItem item) {
    	unselect(-1);
    	collapse(rows.indexOf(item));
    }
    
    public void collapse(int index) {
    	unselect(-1);
    	closeChildItems(rows.get(index),rows);
		refreshRow(index);
    }
    */
    
    /**
     * This method will walk the tree creating the initial list of reachable rows.  This method is called once when
     * the data is initally loaded.
     */
    private void getReachableRows() {
        rows = new ArrayList<TreeDataItem>();
        if(data == null)
        	return;
        for(int i = 0; i < data.size(); i++) {
        	TreeDataItem item = data.get(i);
        	item.childIndex = i;
        	checkChildItems(item,rows);
        }
    }
    
    /**
     * Method recursively called for walking the tree to find reachable items 
     * @param item
     * @param rows
     */
    private void checkChildItems(TreeDataItem item, ArrayList<TreeDataItem> rows){
        rows.add(item);
        if(item.shown)
        	shownRows++;
        if(item.open && item.shownItems() > 0) {
           Iterator<TreeDataItem> it = item.getItems().iterator();   
           while(it.hasNext())
               checkChildItems(it.next(),rows);
        }
    }
    
    /**
     * Refreshes a the row at the rowIndex[0..lastReachableItem] making any changes to the tree and 
     * rendering them in the display
     * @param index
     */
    private void refreshRow(int index) {
    	TreeDataItem row = rows.get(index);
    	while(index+1 < rows.size() && row.isDecendant(rows.get(index+1))){
    		TreeDataItem item = rows.get(index+1);
    		rows.remove(index+1);
    		if(item.shown)
    			shownRows--;
    	}
    	rows.remove(index);
    	if(row.shown)
    		shownRows--;
    	ArrayList<TreeDataItem> middle = new ArrayList<TreeDataItem>();
    	checkChildItems(row,middle);
    	rows.addAll(index,middle);
        renderer.dataChanged(true);
    }
    
    /**
     * Refreshes the passed item into the reachable rows making any changes to the tree and 
     * rendering them in the display
     * @param index
     */
    public void refreshRow(TreeDataItem item) {
    	refreshRow(rows.indexOf(item));
    }

    /**
     * Method called to determine if a cell can be edited by its passed rowIndex[0..lastReachableItem] and column.  Fires BeforeCellEdited event if
     * registered.
     * @param row
     * @param col
     * @return
     */
    protected boolean canEditCell(int row, int col) {
        if(getHandlerCount(BeforeCellEditedEvent.getType()) > 0) {
        	if(fireEvents) {
        		BeforeCellEditedEvent bce = BeforeCellEditedEvent.fire(this, row, col, getRow(row).cells.get(col).value);
        		if(bce.isCancelled()){
        			return false;
        		}
        	}
        }else {
        	if(columns.get(rows.get(row).leafType).get(col).colWidget instanceof Label)
        		return false;
        }
        return isEnabled();
    }
    
	/**
	 * Stub method inherited from HasField interface
	 */
    public void addException(Exception exception) {
		// TODO Auto-generated method stub
	}
    
    /**
     * This method is called on commit of a screen to determine if the Tree contains any 
     * errors  
     */
	public void checkValue() {
		finishEditing();
		exceptions = null;
		if(data == null)
			return;
		for(int i = 0; i < rows.size(); i++) {
			for(int j = 0; j < rows.get(i).cells.size(); j++){
				Widget wid = columns.get(rows.get(i).leafType).get(j).getWidgetEditor(rows.get(i));
				if(wid instanceof HasField){
					((HasField)wid).checkValue();
					if(rows.get(i).cells.get(j).exceptions != null)
						exceptions = rows.get(i).cells.get(j).exceptions;
					if(((HasField)wid).getExceptions() != null) {
						exceptions = ((HasField)wid).getExceptions();
	        			ArrayList<Exception> exceps =  new ArrayList<Exception>(); 
	        			for(Exception exc : (ArrayList<Exception>)((HasField)wid).getExceptions()){
	        				exceps.add(new Exception(exc.getMessage()));
	        				if(rows.get(i).cells.get(j).exceptions != null){
	        					if(!rows.get(i).cells.get(j).getExceptions().contains(exc))
	        						rows.get(i).cells.get(j).exceptions.add(exc);
	        				}
	        			}						
						if(rows.get(i).cells.get(j).exceptions != null)
							exceptions = rows.get(i).cells.get(j).exceptions;
					}
				}
			}
		}
		if(exceptions != null)
			renderer.dataChanged(false);
	}

	/**
	 * Stub method inherited from Hasfield Interface
	 */
	public void clearExceptions() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns any exceptions that may have been found in checkValue so the screen
	 * knows the tree has errors and will cancel commit
	 */
	public ArrayList<Exception> getExceptions() {
		// TODO Auto-generated method stub
		return exceptions;
	}

	/**
	 * Stub method inherited from HasField interface
	 */
	public Field getField() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Stub method inherited from HasField interface
	 */
	public void getQuery(ArrayList list, String key) {
		/*
		ArrayList<TreeColumn> cols;
		
		if(queryMode){
			cols = columns.get(rows.get(0).leafType);
			for(TreeColumn col : cols) {
				if(rows != null && rows.size() > 0 && rows.get(0).cells.get(cols.indexOf(col)).value != null){
					if(col.colWidget instanceof Dropdown) {
						((Dropdown)col.colWidget).setSelectionKeys((ArrayList<Object>)rows.get(0).cells.get(cols.indexOf(col)).value);
					}else if(!(col.colWidget instanceof CalendarLookUp)){
						((HasField)col.getColumnWidget()).setFieldValue(rows.get(0).cells.get(cols.indexOf(col)).value);
					}
					((HasField)col.getColumnWidget()).getQuery(list, col.key);
					
				}
			}
		}
		*/
	}

	/**
	 * Stub method inherited from HasField interface
	 */
	public void setField(Field field) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Stub method inherited from HasField interface
	 */
	public Object getFieldValue() {
		return null;
	}
	
	/**
	 * Stub method inherited from HasField interface
	 */
	public void setQueryMode(boolean query) {
		if(query == queryMode)
			return;
		if(query)
			fireEvents = false;
		else
			fireEvents = true;
		queryMode = query;
		/*
		for(ArrayList<TreeColumn> cols : columns.values())
		for(TreeColumn col : cols) {
			((HasField)col.getColumnWidget()).setQueryMode(query);
		}
		*/
	}

    public void onMouseOver(MouseOverEvent event) {
        ((Widget)event.getSource()).addStyleName("TreeHighlighted");
     }

     public void onMouseOut(MouseOutEvent event) {
         ((Widget)event.getSource()).removeStyleName("TreeHighlighted");
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
			BeforeSelectionHandler<TreeDataItem> handler) {
		return addHandler(handler, BeforeSelectionEvent.getType());
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<TreeDataItem> handler) {
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

	public HandlerRegistration addBeforeLeafOpenHandler(
			BeforeLeafOpenHandler handler) {
		return addHandler(handler, BeforeLeafOpenEvent.getType());
	}

	public HandlerRegistration addBeforeLeafCloseHandler(
			BeforeLeafCloseHandler handler) {
		return addHandler(handler, BeforeLeafCloseEvent.getType());
	}

	public HandlerRegistration addLeafOpenedHandler(LeafOpenedHandler handler) {
		return addHandler(handler,LeafOpenedEvent.getType());
	}

	public HandlerRegistration addLeafClosedHandler(LeafClosedHandler handler) {
		return addHandler(handler,LeafClosedEvent.getType());
	}

	/**
	 * Stub method inherited from HasField interface
	 */
	public void setFieldValue(Object value) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Sorts the tree model by the selected column in the headers list.
	 * This will cause the tree to be walked agian to determine the reachable rows
	 * @param col
	 * @param direction
	 */
    public void sort(int col, SortDirection direction) {
    	if(fireEvents){
    		if(getHandlerCount(BeforeSortEvent.getType()) > 0){
    			BeforeSortEvent event = BeforeSortEvent.fire(this, col, headers.get(col).key, direction);
    			if(event != null && event.isCancelled())
    				return;
    		}
    	}
    	unselect(-1);
    	if(fireEvents && getHandlerCount(SortEvent.getType()) > 0) {
    		SortEvent.fire(this, col, headers.get(col).key, direction);
    	}else{
            for(String leaf : headers.get(col).sortLeaves){
            	if(leaf.equals("model")){
            		Collections.sort(data,new ColumnComparator(col,direction));
            	}else {
            		Iterator<TreeDataItem> it = data.iterator();
                    while(it.hasNext())
                        sortChildItems(it.next(),leaf, col,direction);
            	}
            	
            }
    	}
    	getReachableRows();
    	renderer.dataChanged(false);
    }
    
    /**
     * Method called recursively for sorting child items
     * @param item
     * @param leaf
     * @param col
     * @param dir
     */
    private void sortChildItems(TreeDataItem item, String leaf, int col, SortDirection dir){
    	if(item.leafType.equals(leaf)){
    		Collections.sort(item.getItems(),new ColumnComparator(col,dir));
    	}
        Iterator<TreeDataItem> it = item.getItems().iterator();   
        while(it.hasNext())
            sortChildItems(it.next(),leaf,col,dir);
    }
    
    /**
     * Enables or disables the tree items to be dragged
     * @param drag
     */
    public void enableDrag(boolean drag) {
    	if(drag) {
    		if(dragController == null) {
    			dragController = new TreeDragController(RootPanel.get());
    			dragController.addBeforeStartHandler(new BeforeDragStartHandler<TreeRow>() {
    				public void onBeforeDragStart(BeforeDragStartEvent<TreeRow> event) {
    					finishEditing();
    				}
    			});
    			for(TreeRow row : renderer.rows)
    				dragController.makeDraggable(row);
    		}
    		dragController.setEnable(true);
    	}else{
    		dragController.setEnable(false);
    	}
    }
    
    /**
     * Enables or disables the Tree from accepting drop events
     */
    public void enableDrop(boolean drop) {
    	if(drop){
    		dropController = new TreeIndexDropController(this);
    	}else
    		dropController = null;
    }
    
    /**
     * Adds a target where items from this tree can be dragged to.
     * @param drop
     */
    public void addTarget(HasDropController drop) {
    	assert dragController != null;
    	dragController.registerDropController(drop.getDropController());
    }
    
    /**
     * Removes a target that this tree can no longer be dragged to.
     * @param drop
     */
    public void removeTarget(HasDropController drop){
    	assert dragController != null;
    	dragController.unregisterDropController(drop.getDropController());
    }

	public DropController getDropController() {
		return dropController;
	}
	
	public DragController getDragController() {
		return dragController;
	}
	
	public void addDragHandler(DragHandler handler) {
		dragController.addDragHandler(handler);
	}

	public void setDropController(DropController controller) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Will redraw the displayed tree to pull in changes made to TreeDataItems directly.  
	 * if true is passed in the the tree will keep its current scroll postion, false will 
	 * cause the tree to be scrolled to the top
	 * @param keepPosition
	 */
	public void refresh(boolean keepPosition) {
		renderer.dataChanged(keepPosition);
	}
	
	/**
	 * Redraws a cell based on its rowIndex[0..lastReachableItem] and column.  Used if a TreeDataItem 
	 * cell was changed directly through code
	 * @param row
	 * @param col
	 */
	public void refresh(int row, int col) {
		renderer.cellUpdated(row, col);
	}
	
	/**
	 * Sets the flag usesd to determine if the tree should fire registered events or not
	 * @param fire
	 */
	public void fireEvents(boolean fire) {
		fireEvents = fire;
	}
	
	/**
	 * Stub method inherited from HasField interface
	 */
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler handler) {
		return null;
	}

	public HandlerRegistration addUnselectionHandler(
			UnselectionHandler<TreeDataItem> handler) {
		return addHandler(handler,UnselectionEvent.getType());
	}

	/**
	 * Stub method inherited from HasField interface
	 */
	public void addExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Stub method inherited from HasField interface
	 */
	public Object getWidgetValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Stub method inherited from HasField interface
	 */
	public void removeExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addBeforeSortHandler(BeforeSortHandler handler) {
		return addHandler(handler,BeforeSortEvent.getType());
	}

	public HandlerRegistration addSortHandler(SortHandler handler) {
		return addHandler(handler,SortEvent.getType());
	}
	
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
	
	public HandlerRegistration addBeforeDragStartHandler(BeforeDragStartHandler<TreeRow> handler) {
		assert(dragController != null) : new Exception("Enable Dragging first before registering handlers");
		return dragController.addBeforeStartHandler(handler);	
	}
	
	public HandlerRegistration addDagStartHandler(DragStartHandler<TreeRow> handler) {
		assert(dragController != null) : new Exception("Enable Dragging first before registerning handlers");
		return dragController.addStartHandler(handler);
	}
	
	public HandlerRegistration addBeforeDropHandler(BeforeDropHandler<TreeRow> handler) {
		assert(dropController != null) : new Exception("Enable Dropping first before registering handlers");
		return dropController.addBeforeDropHandler(handler);
	}
	
	public HandlerRegistration addDropHandler(DropHandler<TreeRow> handler) {
		assert(dropController != null) : new Exception("Enable Dropping first before registering handlers");
		return dropController.addDropHandler(handler);
	}
	
	public HandlerRegistration addDropEnterHandler(DropEnterHandler<TreeRow> handler) {
		assert(dropController != null) : new Exception("Enable Dropping first before registering handlers");
		return dropController.addDropEnterHandler(handler);
	}

	public HandlerRegistration addNavigationSelectionHandler(
			NavigationSelectionHandler handler) {
		return addHandler(handler,NavigationSelectionEvent.getType());
	}

	public HandlerRegistration addBeforeRowMovedHandler(
			BeforeRowMovedHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	public HandlerRegistration addRowMovedHandler(RowMovedHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean getCtrlKey() {
		return ctrlKey;
	}
	
	public boolean getShiftKey(){
		return shiftKey;
	}
    
}
