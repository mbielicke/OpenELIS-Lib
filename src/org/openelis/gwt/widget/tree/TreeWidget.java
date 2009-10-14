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

import org.openelis.gwt.event.HasDropController;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.screen.UIUtil;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.Field;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.table.TableDataCell;
import org.openelis.gwt.widget.table.event.BeforeAutoAddEvent;
import org.openelis.gwt.widget.table.event.BeforeAutoAddHandler;
import org.openelis.gwt.widget.table.event.BeforeCellEditedEvent;
import org.openelis.gwt.widget.table.event.BeforeCellEditedHandler;
import org.openelis.gwt.widget.table.event.BeforeRowAddedEvent;
import org.openelis.gwt.widget.table.event.BeforeRowAddedHandler;
import org.openelis.gwt.widget.table.event.BeforeRowDeletedEvent;
import org.openelis.gwt.widget.table.event.BeforeRowDeletedHandler;
import org.openelis.gwt.widget.table.event.CellEditedEvent;
import org.openelis.gwt.widget.table.event.CellEditedHandler;
import org.openelis.gwt.widget.table.event.HasBeforeAutoAddHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeCellEditedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeRowAddedHandlers;
import org.openelis.gwt.widget.table.event.HasBeforeRowDeletedHandlers;
import org.openelis.gwt.widget.table.event.HasCellEditedHandlers;
import org.openelis.gwt.widget.table.event.HasRowAddedHandlers;
import org.openelis.gwt.widget.table.event.HasRowDeletedHandlers;
import org.openelis.gwt.widget.table.event.HasUnselectionHandlers;
import org.openelis.gwt.widget.table.event.RowAddedEvent;
import org.openelis.gwt.widget.table.event.RowAddedHandler;
import org.openelis.gwt.widget.table.event.RowDeletedEvent;
import org.openelis.gwt.widget.table.event.RowDeletedHandler;
import org.openelis.gwt.widget.table.event.UnselectionEvent;
import org.openelis.gwt.widget.table.event.UnselectionHandler;
import org.openelis.gwt.widget.tree.TreeSorterInt.SortDirection;
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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
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
													  HasBeforeAutoAddHandlers,
													  HasBeforeLeafOpenHandlers,
													  HasBeforeLeafCloseHandlers,
													  HasLeafOpenedHandlers,
													  HasLeafClosedHandlers,
													  HasDropController{

    public HashMap<String,ArrayList<TreeColumn>> columns;
    public boolean enabled;
    public boolean focused;
    public int activeRow = -1;
    public int activeCell = -1;
    public TreeView view;
    public TreeRenderer renderer;
    public TreeKeyboardHandler keyboardHandler;
    public boolean shiftKey;
    public boolean ctrlKey;
    public int maxRows;
    public int cellHeight = 18;
    public int cellSpacing = 0;
    public Widget editingCell = null;
    public int[] modelIndexList;
    public boolean showRows;
    public String title;
    public boolean showHeader;
    public TreeDragController dragController;
    public TreeIndexDropController dropController;
    public boolean selectedByClick;
    private ArrayList<TreeDataItem> data;
    public ArrayList<TreeDataItem> rows = new ArrayList<TreeDataItem>();
    public int shownRows; 
    public boolean multiSelect;
    public VerticalScroll showScroll;
   // public ArrayList<Integer> selectedRows = new ArrayList<Integer>();
    protected HashMap<String,TreeDataItem> leaves;
    public String width;
    public ArrayList<TreeDataItem> deleted;// = new ArrayList<DataSet<Key>>();
    public ArrayList<Integer> selections = new ArrayList<Integer>(1);
    private int selected = -1;
    public ArrayList<TreeColumn> headers;
    public TreeSorterInt sorter = new TreeSorter();
    public boolean fireEvents = true;
    
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
        view.setHeight((maxRows * cellHeight
                        + (maxRows * cellSpacing)
                        + (maxRows * 3) + cellSpacing));        
        setWidget(view);
        addDomHandler(keyboardHandler, KeyUpEvent.getType());
        addDomHandler(keyboardHandler, KeyDownEvent.getType());
        addFocusHandler(this);
        addBlurHandler(this);
        
    }
    
    public void setTreeWidth(String width) {
    	this.width = width;
    }
    
    /**
     * This method handles all click events on the body of the table
     */
    public void onClick(ClickEvent event) {
    	Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);
		if(isEnabled()){
			if(columns.get(rows.get(modelIndexList[cell.getRowIndex()]).leafType).get(cell.getCellIndex()).getColumnWidget() instanceof CheckBox){
				if(CheckBox.CHECKED.equals(getCell(cell.getRowIndex(),cell.getCellIndex()).getValue())){
					setCell(cell.getRowIndex(),cell.getCellIndex(),CheckBox.UNCHECKED);
					if(fireEvents)
						CellEditedEvent.fire(this, cell.getRowIndex(),cell.getCellIndex(), modelIndexList[cell.getRowIndex()], CheckBox.UNCHECKED);
				}else{
					setCell(cell.getRowIndex(),cell.getCellIndex(),CheckBox.CHECKED);
					if(fireEvents)
						CellEditedEvent.fire(this, cell.getRowIndex(),cell.getCellIndex(), modelIndexList[cell.getRowIndex()], CheckBox.CHECKED);
				}
			}
		}
        if(activeRow == cell.getRowIndex() && activeCell == cell.getCellIndex())
            return;
        selectedByClick = true;
        select(cell.getRowIndex(), cell.getCellIndex());
        selectedByClick = false;
    }


    public void unselect(int row) {
        finishEditing();
        if(row == -1){
        	for(int i : selections) {
        		rows.get(i).selected = false;
        	}
            selections.clear();
            selected = -1;
            renderer.rowUnselected(-1);
        }else if(row < selections.size()){
            selections.remove(new Integer(row));
            if(selected == row)
                selected = -1;
        }
        if(activeRow > -1)
        	rows.get(activeRow).selected = false;
        activeRow = -1;
        if(isRowDrawn(row))
        	renderer.rowUnselected(treeIndex(row));
    }
    
    private boolean isRowDrawn(int row){
    	return row >= modelIndexList[0] && row <= modelIndexList[view.table.getRowCount()-1];
    }
    
    private int treeIndex(int modelIndex) {
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
    	if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
    		BeforeSelectionEvent<TreeDataItem> event = BeforeSelectionEvent.fire(this, rows.get(modelIndexList[row]));
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
            		UnselectionEvent.fire(this, rows.get(activeRow));
                unselect(-1);
            }
            activeRow = row;
            selectRow(modelIndexList[row]);
            if(fireEvents)
            	SelectionEvent.fire(this,rows.get(modelIndexList[row]));
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
    
    public void select(TreeDataItem item) {
    	selectRow(rows.indexOf(item));
    	if(fireEvents)
    		SelectionEvent.fire(this, item);
    }

    public boolean finishEditing() {
        if (editingCell != null) {
        	if(renderer.stopEditing() && fireEvents)
        		CellEditedEvent.fire(this, activeRow, activeCell, modelIndexList[activeRow], getRow(activeRow).cells.get(activeCell).value);
        	activeCell = -1;
            sinkEvents(Event.KEYEVENTS);
        }
        return false;
    }

    public void startEditing(int row, int col) {
    	if(isRowDrawn(row))
    		select(treeIndex(row), col);
    }


	public void enable(boolean enabled) {
		this.enabled = enabled;
        if(dragController != null)
            dragController.setEnable(enabled);
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
	}


	public void onBlur(BlurEvent event) {

	}

    public void scrollToSelection() {
        if (numRows() == shownRows()) {
            view.scrollBar.setScrollPosition(cellHeight * getSelectedIndex());
        } else {
            int shownIndex = 0;
            for (int i = 0; i < getSelectedIndex(); i++) {
                if (getRow(i).shown)
                    shownIndex++;
            }
            view.scrollBar.setScrollPosition(cellHeight * shownIndex);
        }
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
        if(fireEvents)
        	RowAddedEvent.fire(this, data.size()-1, row);
    }

    public void addRow(int index, TreeDataItem row) {
    	finishEditing();
    	if(fireEvents){
    		BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, data.size(), row);
    		if(event != null && event.isCancelled())
    			return;
    	}
    	int rowindex = rows.indexOf(data.get(index));
        data.add(index,row);
        ArrayList<TreeDataItem> added = new ArrayList<TreeDataItem>();
        checkChildItems(row,added);
       	rows.addAll(rowindex, added);
       	renderer.dataChanged(true);
       	if(fireEvents)
       		RowAddedEvent.fire(this, data.size()-1, row);  	
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
    	activeRow = -1;
    	activeCell = -1;
    	editingCell = null;
    	shownRows = 0;
        rows = new ArrayList<TreeDataItem>();
        renderer.dataChanged(false);
    }

    public void clearSelections() {
        for(int i : selections){
            rows.get(i).selected = false;
        }
        selections.clear();
    }


    public void deleteRow(int row) {
    	finishEditing();
    	if(fireEvents) {
    		BeforeRowDeletedEvent event = BeforeRowDeletedEvent.fire(this, row, getRow(row));
    		if(event != null && event.isCancelled())
    			return;
    	}
    	if(selections.contains(row)){
            unselectRow(row);
            if(fireEvents)
            	UnselectionEvent.fire(this, getRow(row));
        }
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
        renderer.dataChanged(true);
        if(fireEvents)
        	RowDeletedEvent.fire(this, row, item);
    }
    
    public void deleteRows(List<Integer> rowIndexes) {
    	finishEditing();
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
        return rows.get(selections.get(0));
    }

    public ArrayList<TreeDataItem> getSelections() {
        ArrayList<TreeDataItem> selected = new ArrayList<TreeDataItem>();
        for(int i : selections) {
            selected.add(rows.get(i));
        }
        return selected;
    }
    
    public int getSelectedIndex() {
        if(selections.size() == 0)
            return -1;
        return selections.get(0);
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
        activeRow = -1;
        activeCell = -1;
        renderer.dataChanged(false);
    }

    public int numRows() {
        return rows.size();
    }

    public void selectRow(int index){
        if(index < numRows()){
           if(!multiSelect && selections.size() > 0){
        	   if(fireEvents)
        		   UnselectionEvent.fire(this, rows.get(selections.get(0)));
               rows.get(selections.get(0)).selected = false;
               renderer.rowUnselected(-1);
               selections.clear();
           }
           rows.get(index).selected = true;
           selections.add(index);
           renderer.rowSelected(treeIndex(index));
        }    
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
        renderer.dataChanged(true);
    }
    
    public void toggle(int row) {
    	if(getHandlerCount(BeforeSelectionEvent.getType()) > 0 && fireEvents) {
    		BeforeSelectionEvent<TreeDataItem> event = BeforeSelectionEvent.fire(this, rows.get(modelIndexList[row]));
    		if(!event.isCanceled()){
    			unselect(-1);
    			selections.add(row);
    			rows.get(row).selected = true;
    			renderer.rowSelected(treeIndex(row));
    			activeRow = treeIndex(row);
    			SelectionEvent.fire(this, rows.get(row));
    		}
    	}else if(isEnabled()){
    		unselect(-1);
			selections.add(row);
			rows.get(row).selected = true;
			renderer.rowSelected(treeIndex(row));
			activeRow = treeIndex(row);
    		if(fireEvents)
    			SelectionEvent.fire(this, rows.get(row));
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
    
    public void clearCellError(int row, int col) {
        rows.get(row).cells.get(col).clearErrors();
        renderer.cellUpdated(row, col);
    }

    public void setCellError(int row, int col, String Error) {
        rows.get(row).cells.get(col).addError(Error);
        renderer.cellUpdated(row, col);
        
    }
    
    public void clearCellError(int row, String col) {
    	int index = -1;
    	for(TreeColumn column : columns.get(getRow(row).leafType)) {
    		if(column.key.equals(col)){
    			index = columns.get(getRow(row).leafType).indexOf(column);
    			rows.get(row).cells.get(index).clearErrors();
    			break;
    		}
    	}
        renderer.cellUpdated(row, index);
    }

    public void setCellError(int row, String col, String error) {
    	int index = -1;
    	for(TreeColumn column : columns.get(getRow(row).leafType)) {
    		if(column.key.equals(col)){
    			index = columns.get(getRow(row).leafType).indexOf(column);
    			rows.get(row).cells.get(index).addError(error);
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
    
    private void getVisibleRows() {
        rows = new ArrayList<TreeDataItem>();
        if(data == null)
        	return;
        Iterator<TreeDataItem> it = data.iterator();
        while(it.hasNext())
            checkChildItems(it.next(),rows);
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
    
    private void refreshRow(TreeDataItem item) {
    	refreshRow(rows.indexOf(item));
    }

    protected boolean canEditCell(int row, int col) {
        if(getHandlerCount(BeforeCellEditedEvent.getType()) > 0 && fireEvents) {
        	BeforeCellEditedEvent bce = BeforeCellEditedEvent.fire(this, modelIndexList[row], col, getRow(row).cells.get(col).value);
        	if(bce.isCancelled()){
        		return false;
        	}else
        		return true;
        }
        return isEnabled();
    }
    
	public void addError(String Error) {
		// TODO Auto-generated method stub
		
	}

	ArrayList<String> errors;
	
	public void checkValue() {
		finishEditing();
		errors = null;
		if(data == null)
			return;
		for(int i = 0; i < rows.size(); i++) {
			for(int j = 0; j < rows.get(i).cells.size(); j++){
				Widget wid = columns.get(rows.get(i).leafType).get(j).getWidgetEditor(rows.get(i).cells.get(j));
				if(wid instanceof HasField){
					((HasField)wid).checkValue();
					rows.get(i).cells.get(j).errors = ((HasField)wid).getErrors();
					if(rows.get(i).cells.get(j).errors != null){
						errors = rows.get(i).cells.get(j).errors;
					}
				}
			}
		}
		if(errors != null)
			renderer.dataChanged(false);
	}

	public void clearErrors() {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<String> getErrors() {
		// TODO Auto-generated method stub
		return errors;
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

	public HandlerRegistration addBeforeAutoddHandler(
			BeforeAutoAddHandler handler) {
		return addHandler(handler, BeforeAutoAddEvent.getType());
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
    	unselect(-1);
        sorter.sort(data,headers.get(col).sortLeaves, col,direction);
    	load(data);
    }
    
    public void enableDrag(boolean drag) {
    	if(drag) {
    		dragController = new TreeDragController(RootPanel.get());
    		for(TreeRow row : renderer.rows)
    			dragController.makeDraggable(row);
    	}else{
    		for(TreeRow row : renderer.rows) 
    			dragController.makeNotDraggable(row);
    		dragController = null;
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
    	dragController.registerDropController(drop.getDropController());
    }

	public DropController getDropController() {
		return dropController;
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
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler handler) {
		return null;
	}

	public HandlerRegistration addUnselectionHandler(
			UnselectionHandler<TreeDataItem> handler) {
		return addHandler(handler,UnselectionEvent.getType());
	}
    
}
