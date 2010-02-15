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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.openelis.gwt.common.LocalizedException;
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
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Field;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.NavigationWidget;
import org.openelis.gwt.widget.table.ColumnComparator;
import org.openelis.gwt.widget.table.TableDataCell;
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
													  HasRowMovedHandlers,
													  NavigationWidget<TreeDataItem>{

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
    protected int[] modelIndexList;
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
    
    public TreeWidget() {

    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
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
        //addFocusHandler(this);
        //addBlurHandler(this);
        
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
    
    public int getTreeWidth() {
    	int tw = 0;
    	
    	if(width.equals("auto")){
    		for(TreeColumn column : headers)
    			tw += column.getCurrentWidth();
    	}else if(width.indexOf("px") > -1){
    		tw = Integer.parseInt(width.substring(0,width.length()-2));
    	}else
    		tw = Integer.parseInt(width);
    	
    	return tw;
    }
    
    /**
     * This method handles all click events on the body of the table
     */
    public void onClick(ClickEvent event) {
    	Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);
		if(isEnabled() && columns.get(getRow(modelIndexList[cell.getRowIndex()]).leafType).get(cell.getCellIndex()).getColumnWidget() instanceof CheckBox && !shiftKey && !ctrlKey){
			if(CheckBox.CHECKED.equals(getCell(modelIndexList[cell.getRowIndex()],cell.getCellIndex()).getValue())){
				setCell(modelIndexList[cell.getRowIndex()],cell.getCellIndex(),CheckBox.UNCHECKED);
				if(fireEvents)
					CellEditedEvent.fire(this,  modelIndexList[cell.getRowIndex()], cell.getCellIndex(), CheckBox.UNCHECKED);
			}else if(queryMode && CheckBox.UNCHECKED.equals(getCell(modelIndexList[cell.getRowIndex()],cell.getCellIndex()).getValue())){
				setCell(modelIndexList[cell.getRowIndex()],cell.getCellIndex(),CheckBox.UNKNOWN);
				if(fireEvents)
					CellEditedEvent.fire(this, modelIndexList[cell.getRowIndex()], cell.getCellIndex(), CheckBox.UNKNOWN);
			}else{
				setCell(modelIndexList[cell.getRowIndex()],cell.getCellIndex(),CheckBox.CHECKED);
				if(fireEvents)
					CellEditedEvent.fire(this, modelIndexList[cell.getRowIndex()], cell.getCellIndex(), CheckBox.CHECKED);
			}
		}
        if(treeIndex(selectedRow) == cell.getRowIndex() && selectedCol == cell.getCellIndex())
            return;
        //selectedByClick = true;
        select(modelIndexList[cell.getRowIndex()], cell.getCellIndex());
        //selectedByClick = false;
    }


    public void unselect(int row) {
        finishEditing();
        if(row == -1){
        	for(int i : selections) {
        		rows.get(i).selected = false;
        	}
            selections.clear();
            renderer.rowUnselected(-1);
        }else if(row < selections.size()){
            selections.remove(new Integer(row));
        }
        if(selectedRow > -1)
        	rows.get(selectedRow).selected = false;
        selectedRow = -1;
        if(isRowDrawn(row))
        	renderer.rowUnselected(row);
    }
    
    protected boolean isRowDrawn(int row){
    	return row >= modelIndexList[0] && row <= modelIndexList[view.table.getRowCount()-1];
    }
    
    protected int treeIndex(int modelIndex) {
    	for(int i = 0; i < view.table.getRowCount(); i++){
    		if(modelIndexList[i] == modelIndex)
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
    	if(getHandlerCount(NavigationSelectionEvent.getType()) > 0) {
    		if(rows.get(row).parent == null){
    			NavigationSelectionEvent.fire(this,rows.get(row).childIndex);
    			return;
    		}
    	}
    	if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
    		BeforeSelectionEvent<TreeDataItem> event = BeforeSelectionEvent.fire(this, rows.get(row));
    		if(event.isCanceled())
    			return;
    	}else if(!isEnabled())
    		return;
        finishEditing();
        if(selectedRow != row){
            if(selectedRow > -1 && !ctrlKey && !shiftKey){
            	if(fireEvents)
            		UnselectionEvent.fire(this, rows.get(selectedRow));
                unselect(-1);
            }
            if(multiSelect && ctrlKey && isSelected(row)){
            	unselect(row);
            	selectedCol = -1;
           		sinkEvents(Event.ONKEYPRESS);
            	return;
            }
            //selectedRow = row;
            selectRow(row);
            if(fireEvents)
            	SelectionEvent.fire(this,rows.get(row));
        }
        if(canEditCell(row,col)){
            selectedCol = col;
            renderer.setCellEditor(row, col);
            unsinkEvents(Event.ONKEYPRESS);
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
    
    public void select(int index) {
    	selectRow(index);
    	if(fireEvents)
    		SelectionEvent.fire(this, getSelection());
    }
    
    
    public void select(TreeDataItem item) {
    	unselect(-1);
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

    public void finishEditing() {
        if (activeWidget != null) {
        	if(renderer.stopEditing() && fireEvents)
        		CellEditedEvent.fire(this,selectedRow, selectedCol, getRow(selectedRow).cells.get(selectedCol).value);
        	selectedCol = -1;
            sinkEvents(Event.KEYEVENTS);
        }
    }

    public void startEditing(int row, int col) {
    	if(isRowDrawn(row))
    		select(row, col);
    }


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

	public void onFocus(FocusEvent event) {
		if(!DOM.isOrHasChild(getElement(),((ScreenPanel)event.getSource()).focused.getElement())){
			finishEditing();
		}
	}


	public void onBlur(BlurEvent event) {

	}

    public void scrollToSelection() {
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
    
    public void addRow(TreeDataItem row) {
    	finishEditing();
    	if(fireEvents) {
    		BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, data.size(), row);
    		if(event != null && event.isCancelled())
    			return;
    	}
        data.add(row);
        checkChildItems(row,rows);
        renderer.dataChanged(true);
        row.childIndex = data.size()-1;
        if(fireEvents)
        	RowAddedEvent.fire(this, data.size()-1, row);
    }

    public void addRow(int index, TreeDataItem row) {
    	finishEditing();
    	if(fireEvents){
    		BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, index, row);
    		if(event != null && event.isCancelled())
    			return;
    	}
    	int rowindex = rows.indexOf(data.get(index));
        data.add(index,row);
        ArrayList<TreeDataItem> added = new ArrayList<TreeDataItem>();
        checkChildItems(row,added);
       	rows.addAll(rowindex, added);
       	for(int i = index; i < data.size(); i++) {
       		data.get(i).childIndex = i;
       	}
       	if(fireEvents)
       		RowAddedEvent.fire(this, index, row);  	
    	renderer.dataChanged(true);
    }
    
    public void addChildItem(TreeDataItem parent, TreeDataItem child) {
    	parent.addItem(child);
    	if(parent.open)
    		refreshRow(parent);
    	if(fireEvents)
    		RowAddedEvent.fire(this,-1, child);
    }
    
    public void addChildItem(TreeDataItem parent, TreeDataItem child, int index) {
    	parent.addItem(index, child);
    	if(parent.open)
    		refreshRow(parent);
    	if(fireEvents)
    		RowAddedEvent.fire(this, -1, child);
    }
    
    public void removeChild(TreeDataItem parent, TreeDataItem child) {
    	unselect(-1);
    	parent.removeItem(parent.getItems().indexOf(child));
    	if(parent.open)
    		refreshRow(parent);
    }
    
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

    public void clearSelections() {
        for(int i : selections){
            rows.get(i).selected = false;
        }
        selections.clear();
        selectedRow = -1;
        selectedCol = -1;
    }

    public void deleteRow(TreeDataItem item) {
    	deleteRow(rows.indexOf(item));
    }

    public void deleteRow(int row) {
    	if(fireEvents)
    		UnselectionEvent.fire(this,rows.get(selectedRow));
    	unselect(row);
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
        for(int i = row; i < data.size(); i++) {
        	data.get(i).childIndex = i;
        }
        renderer.dataChanged(true);
        if(fireEvents)
        	RowDeletedEvent.fire(this, row, item);
    }
    
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

    public ArrayList<TreeDataItem> getData() {
        return data;
    }

    public Object getObject(int row, int col) {
        return rows.get(row).cells.get(col).getValue();
    }


    public TreeDataItem getRow(int row) {
        return rows.get(row);
    }

    public TreeDataItem getSelection() {
    	if(selections == null || selections.size() == 0)
    		return null;
        return rows.get(selections.get(0));
    }

    public ArrayList<TreeDataItem> getSelections() {
        ArrayList<TreeDataItem> selected = new ArrayList<TreeDataItem>();
        for(int i : selections) {
            selected.add(rows.get(i));
        }
        return selected;
    }
    
    public int getSelectedRow() {
        return selectedRow;
    }

    public void hideRow(int row) {
        rows.get(row).shown = false;
        shownRows--;
        renderer.dataChanged(true);
    }

    public boolean isEnabled(int index) {
        if(index < numRows())
            return rows.get(index).enabled;
        return false;
    }

    public boolean isSelected(int index) {
        return selections.contains(index);
    }

    public void load(ArrayList<TreeDataItem> data) {
        this.data = data;
        shownRows = 0;
        getVisibleRows();
        selections.clear();
        selectedRow = -1;
        selectedCol = -1;
        renderer.dataChanged(false);
    }

    public int numRows() {
        return rows.size();
    }
    
    public void navSelect(int index) {
    	selectRow(index = rows.indexOf(data.get(index)));
    }
    
    public void selectRow(int index){
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
        }
        if(isRowDrawn(index))
        	renderer.dataChanged(true);
        else
        	scrollToSelection();
    }

    public void setModel(ArrayList<TreeDataItem> data) {
        this.data = data;
        
    }

    public void showRow(int row) {
        rows.get(row).shown = true;
        shownRows++;
        renderer.dataChanged(true);
    }

    public int shownRows() {
        return shownRows;
    }

    public ArrayList<TreeDataItem> unload() {
    	finishEditing();
        return data;
    }

    public void unselectRow(int index){
        if(index < 0) {
            clearSelections();
        }else if(index < numRows()){
            rows.get(index).selected = false;
            selections.remove(new Integer(index));
        }
        renderer.rowUnselected(-1);
    }

    public void setCell(int row, int col,Object value) {
        rows.get(row).cells.get(col).setValue(value);
        if(isRowDrawn(row))
        	renderer.cellUpdated(row, col);
    }

    public TableDataCell getCell(int row, int col) {
        return rows.get(row).cells.get(col);
    }

    public void setRow(int index, TreeDataItem row) {
    	int rowindex = rows.indexOf(data.get(index));
        data.set(index, row);
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
        checkChildItems(row,added);
        rows.addAll(rowindex,added);
        row.childIndex = index;
        renderer.dataChanged(true);
    }
    
    public void toggle(TreeDataItem item) {
    	toggle(rows.indexOf(item));
    }
    
    public void toggle(int row) {
    	if(getHandlerCount(NavigationSelectionEvent.getType()) > 0 && rows.get(row).parent == null && rows.get(row) != getSelection()) {
    		NavigationSelectionEvent event = NavigationSelectionEvent.fire(this, rows.get(row).childIndex);
    		if(event != null && event.isCancelled())
    			return;
    	}else {
    		if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
    			BeforeSelectionEvent<TreeDataItem> event = BeforeSelectionEvent.fire(this, rows.get(row));
    			if(!event.isCanceled()){
    				unselect(-1);
    				selections.add(row);
    				rows.get(row).selected = true;
    				renderer.rowSelected(row);
    				selectedRow = row;
    				SelectionEvent.fire(this, rows.get(row));
    			}
    		}else if(isEnabled()){
    			unselect(-1);
    			selections.add(row);
    			rows.get(row).selected = true;
    			renderer.rowSelected(row);
    			selectedRow = row;
    			if(fireEvents)
    				SelectionEvent.fire(this, rows.get(row));
    		}
    	}
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
    
    public int getSelectedRowIndex() {
        if(selections.size() == 0)
            return -1;
        return selections.get(0);
    }
    
    public int[] getSelectedRowIndexes() {
        int[] ret = new int[selections.size()];
        for(int i = 0;  i < selections.size(); i++)
            ret[i] = selections.get(i);
        return ret;
    }

    public ArrayList<Integer> getSelectedRowList() {
        return selections;
    }
    
    public void clearCellExceptions(int row, int col) {
        rows.get(row).cells.get(col).clearExceptions();
        renderer.cellUpdated(row, col);
    }

    public void setCellException(int row, int col, LocalizedException ex) {
        rows.get(row).cells.get(col).addException(ex);
        renderer.cellUpdated(row, col);
        
    }
    
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

    public void setCellException(int row, String col, LocalizedException ex) {
    	int index = -1;
    	for(TreeColumn column : columns.get(getRow(row).leafType)) {
    		if(column.key.equals(col)){
    			index = columns.get(getRow(row).leafType).indexOf(column);
    			rows.get(row).cells.get(index).addException(ex);
    			break;
    		}
    	}
        renderer.cellUpdated(row, index);
        
    }
    
    public TreeDataItem createTreeItem(String leafType) {
    	TreeDataItem td = new TreeDataItem(columns.get(leafType).size());
    	td.leafType = leafType;
    	return td;
    }
    
    public void setLeaves(HashMap<String,TreeDataItem> leaves){
        this.leaves = leaves;
    }
    
    public void expand() {
    	unselect(-1);
        rows = new ArrayList<TreeDataItem>();
        if(data == null)
        	return;
        for(int i = 0; i < data.size(); i++) {
        	TreeDataItem item = data.get(i);
        	item.childIndex = i;
        	openChildItems(item,rows);
        }
        refresh(false);
    }
    
    public void collapse() {
    	unselect(-1);
        rows = new ArrayList<TreeDataItem>();
        if(data == null)
        	return;
        for(int i = 0; i < data.size(); i++) {
        	TreeDataItem item = data.get(i);
        	item.childIndex = i;
        	rows.add(item);
        	closeChildItems(item,rows);
        }
        refresh(false);
    }
    
    public void collapse(TreeDataItem item) {
    	unselect(-1);
    	collapse(rows.indexOf(item));
    }
    
    public void collapse(int index) {
    	unselect(-1);
    	closeChildItems(rows.get(index),rows);
		refreshRow(index);
    }
    
    private void getVisibleRows() {
        rows = new ArrayList<TreeDataItem>();
        if(data == null)
        	return;
        for(int i = 0; i < data.size(); i++) {
        	TreeDataItem item = data.get(i);
        	item.childIndex = i;
        	checkChildItems(item,rows);
        }
    }
    
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
    
    private void closeChildItems(TreeDataItem item, ArrayList<TreeDataItem> rows) {
   	 item.open = false;
     if(item.shown)
       	shownRows++;
     Iterator<TreeDataItem> it = item.getItems().iterator();   
     while(it.hasNext())
         closeChildItems(it.next(),rows);
   }
    
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
    
    public void refreshRow(TreeDataItem item) {
    	refreshRow(rows.indexOf(item));
    }

    protected boolean canEditCell(int row, int col) {
        if(getHandlerCount(BeforeCellEditedEvent.getType()) > 0 && fireEvents) {
        	BeforeCellEditedEvent bce = BeforeCellEditedEvent.fire(this, row, col, getRow(row).cells.get(col).value);
        	if(bce.isCancelled()){
        		return false;
        	}else
        		return true;
        }
        return isEnabled();
    }
    
	public void addException(LocalizedException exception) {
		// TODO Auto-generated method stub
		
	}

	ArrayList<LocalizedException> exceptions;
	
	public void checkValue() {
		finishEditing();
		exceptions = null;
		if(data == null)
			return;
		for(int i = 0; i < rows.size(); i++) {
			for(int j = 0; j < rows.get(i).cells.size(); j++){
				Widget wid = columns.get(rows.get(i).leafType).get(j).getWidgetEditor(rows.get(i).cells.get(j));
				if(wid instanceof HasField){
					((HasField)wid).checkValue();
					rows.get(i).cells.get(j).exceptions = ((HasField)wid).getExceptions();
					if(rows.get(i).cells.get(j).exceptions != null){
						exceptions = rows.get(i).cells.get(j).exceptions;
					}
				}
			}
		}
		if(exceptions != null)
			renderer.dataChanged(false);
	}

	public void clearExceptions() {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<LocalizedException> getExceptions() {
		// TODO Auto-generated method stub
		return exceptions;
	}

	public Field getField() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getQuery(ArrayList list, String key) {
		// TODO Auto-generated method stub
		
	}

	public void setField(Field field) {
		// TODO Auto-generated method stub
		
	}

	public Object getFieldValue() {
		return null;
	}
	
	public void setQueryMode(boolean query) {
		// TODO Auto-generated method stub
		
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

	public void setFieldValue(Object value) {
		// TODO Auto-generated method stub
		
	}
	
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
    	getVisibleRows();
    	renderer.dataChanged(false);
    }
    
    private void sortChildItems(TreeDataItem item, String leaf, int col, SortDirection dir){
    	if(item.leafType.equals(leaf)){
    		Collections.sort(item.getItems(),new ColumnComparator(col,dir));
    	}
        Iterator<TreeDataItem> it = item.getItems().iterator();   
        while(it.hasNext())
            sortChildItems(it.next(),leaf,col,dir);
    }
    
    public void enableDrag(boolean drag) {
    	if(drag) {
    		if(dragController == null) {
    			dragController = new TreeDragController(RootPanel.get());
    			for(TreeRow row : renderer.rows)
    				dragController.makeDraggable(row);
    		}
    		dragController.setEnable(true);
    	}else{
    		dragController.setEnable(false);
    	}
    }
    
    public void enableDrop(boolean drop) {
    	if(drop){
    		dropController = new TreeIndexDropController(this);
    	}else
    		dropController = null;
    }
    
    public void addTarget(HasDropController drop) {
    	assert dragController != null;
    	dragController.registerDropController(drop.getDropController());
    }
    
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
	
	public void refresh(boolean keepPosition) {
		renderer.dataChanged(keepPosition);
	}
	
	public void refresh(int row, int col) {
		renderer.cellUpdated(row, col);
	}
	
	public void fireEvents(boolean fire) {
		fireEvents = fire;
	}
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler handler) {
		return null;
	}

	public HandlerRegistration addUnselectionHandler(
			UnselectionHandler<TreeDataItem> handler) {
		return addHandler(handler,UnselectionEvent.getType());
	}

	public void addExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	public Object getWidgetValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addBeforeSortHandler(BeforeSortHandler handler) {
		return addHandler(handler,BeforeSortEvent.getType());
	}

	public HandlerRegistration addSortHandler(SortHandler handler) {
		return addHandler(handler,SortEvent.getType());
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
    
}
