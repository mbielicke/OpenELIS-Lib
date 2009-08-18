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
package org.openelis.gwt.widget.table.rewrite;

import java.util.ArrayList;
import java.util.List;

import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.rewrite.CheckBox;
import org.openelis.gwt.widget.rewrite.Field;
import org.openelis.gwt.widget.table.rewrite.TableSorterInt.SortDirection;
import org.openelis.gwt.widget.table.rewrite.TableView.VerticalScroll;
import org.openelis.gwt.widget.table.rewrite.event.BeforeAutoAddEvent;
import org.openelis.gwt.widget.table.rewrite.event.BeforeAutoAddHandler;
import org.openelis.gwt.widget.table.rewrite.event.BeforeCellEditedEvent;
import org.openelis.gwt.widget.table.rewrite.event.BeforeCellEditedHandler;
import org.openelis.gwt.widget.table.rewrite.event.BeforeRowAddedEvent;
import org.openelis.gwt.widget.table.rewrite.event.BeforeRowAddedHandler;
import org.openelis.gwt.widget.table.rewrite.event.BeforeRowDeletedEvent;
import org.openelis.gwt.widget.table.rewrite.event.BeforeRowDeletedHandler;
import org.openelis.gwt.widget.table.rewrite.event.CellEditedEvent;
import org.openelis.gwt.widget.table.rewrite.event.CellEditedHandler;
import org.openelis.gwt.widget.table.rewrite.event.HasBeforeAutoAddHandlers;
import org.openelis.gwt.widget.table.rewrite.event.HasBeforeCellEditedHandlers;
import org.openelis.gwt.widget.table.rewrite.event.HasBeforeRowAddedHandlers;
import org.openelis.gwt.widget.table.rewrite.event.HasBeforeRowDeletedHandlers;
import org.openelis.gwt.widget.table.rewrite.event.HasCellEditedHandlers;
import org.openelis.gwt.widget.table.rewrite.event.HasRowAddedHandlers;
import org.openelis.gwt.widget.table.rewrite.event.HasRowDeletedHandlers;
import org.openelis.gwt.widget.table.rewrite.event.HasTableValueChangeHandlers;
import org.openelis.gwt.widget.table.rewrite.event.RowAddedEvent;
import org.openelis.gwt.widget.table.rewrite.event.RowAddedHandler;
import org.openelis.gwt.widget.table.rewrite.event.RowDeletedEvent;
import org.openelis.gwt.widget.table.rewrite.event.RowDeletedHandler;
import org.openelis.gwt.widget.table.rewrite.event.TableValueChangeEvent;
import org.openelis.gwt.widget.table.rewrite.event.TableValueChangeHandler;

import com.google.gwt.dom.client.Document;
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
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
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
public class TableWidget extends FocusPanel implements FocusHandler, 
													   BlurHandler, 
													   ClickHandler, 
													   HasField, 
													   HasTableValueChangeHandlers,
													   MouseOverHandler, 
													   MouseOutHandler,
													   HasBeforeSelectionHandlers<TableRow>,
													   HasSelectionHandlers<TableRow>,
													   HasBeforeCellEditedHandlers,
													   HasCellEditedHandlers, 
													   HasBeforeRowAddedHandlers,
													   HasRowAddedHandlers, 
													   HasBeforeRowDeletedHandlers,
													   HasRowDeletedHandlers,
													   HasBeforeAutoAddHandlers
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
    public int cellHeight = 18;
    public int cellSpacing = 1;
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
    private ArrayList<TableDataRow> data;
    public TableSorterInt sorter = new TableSorter();
    public int shownRows; 
    public ArrayList<Integer> selections = new ArrayList<Integer>(1);
    private int selected = -1;
    private ArrayList<String> errors;
    private boolean queryMode;
    public boolean addIcon;
    public boolean deleteIcon;
    
    public boolean autoAdd;
    
    public TableDataRow autoAddRow;
    
    public boolean multiSelect;
    
    public TableWidget() {
        
    }
    
    public void addTabHandler(UIUtil.TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void init(){
        renderer = new TableRenderer(this);
        keyboardHandler = new TableKeyboardHandler(this);
        view = new TableView(this,showScroll);
        view.setWidth(width);
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        setWidget(view);
        addDomHandler(keyboardHandler,KeyUpEvent.getType());
        addDomHandler(keyboardHandler,KeyDownEvent.getType());
       // addFocusHandler(this);
       // addBlurHandler(this);
        //Event.setEventListener(Document.get().getDocumentElement(), this);
        //DOM.sinkEvents((Element)Document.get().getDocumentElement(), Event.ONCLICK);
        
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
    		if(columns.get(cell.getCellIndex()).getColumnWidget() instanceof CheckBox){
    			if(CheckBox.CHECKED.equals(getCell(cell.getRowIndex(),cell.getCellIndex()).getValue()))
        			setCell(cell.getRowIndex(),cell.getCellIndex(),CheckBox.UNCHECKED);
        		else
        			setCell(cell.getRowIndex(),cell.getCellIndex(),CheckBox.CHECKED);
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
         
     }

     public void onMouseOut(MouseOutEvent event) {
         ((Widget)event.getSource()).removeStyleName("TableHighlighted");
         
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
        }else if(row < selections.size()){
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
    	if(getHandlerCount(BeforeSelectionEvent.getType()) > 0) {
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
                unselect(-1);
            }
            activeRow = row;
            selectRow(modelIndexList[row]);
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
    
    public boolean finishEditing() {
        if(editingCell != null) {
        	renderer.stopEditing();
        	CellEditedEvent.fire(this, activeRow, activeCell, getRow(activeRow).cells.get(activeCell).value);
            if(isAutoAdd() && modelIndexList[activeRow] == numRows()){
            	BeforeAutoAddEvent event = BeforeAutoAddEvent.fire(this, getAutoAddRow());
                if(event != null && !event.isCancelled()){
                    addRow(getAutoAddRow());
                    //tableWidgetListeners.fireFinishedEditing(this, modelIndexList[activeRow], activeCell);
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
        addRow(createRow());
    }
    
    public void addRow(int index) {
        addRow(index,createRow());
    }
    
    public void addRow(TableDataRow row) {
    	BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, data.size(), row);
    	if(event != null && event.isCancelled())
    		return;
        data.add(row);
        if(row.shown)
            shownRows++;
        RowAddedEvent.fire(this, data.size()-1, row);
       	renderer.dataChanged(true);
    }
    
    public void addRow(int index, TableDataRow row) {
    	BeforeRowAddedEvent event = BeforeRowAddedEvent.fire(this, index, row);
    	if(event != null && event.isCancelled())
    		return;
        data.add(index, row);
        if(row.shown)
            shownRows++;
        RowAddedEvent.fire(this, index, row);
       	renderer.dataChanged(true);
    }

    public void deleteRow(int row) {
    	finishEditing();
    	BeforeRowDeletedEvent event = BeforeRowDeletedEvent.fire(this, row, getRow(row));
    	if(event != null && event.isCancelled())
    		return;
        if(data.get(row).shown)
            shownRows--;
        if(row < data.size()){
            TableDataRow tmp = data.remove(row);
            RowDeletedEvent.fire(this, row, tmp);
        }
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
        if(isRowDrawn(index))
        	renderer.rowSelected(tableIndex(index));
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
        CellEditedEvent.fire(this, row, col, value);
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
    	unselect(-1);
        sorter.sort(data, col, direction);
    	renderer.dataChanged(false);
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


	public void onFocus(FocusEvent event) {
	
	}


	public void onBlur(BlurEvent event) {
		//System.out.println("Blur Table");
		//if(!DOM.isOrHasChild(this.getElement(), ((Widget)event.getSource()).getElement())){
			//finishEditing();
		//if(event.getSource() != this && editingCell != null){// && editingCell.getElement() != ((Widget)event.getSource()).getElement()){
			//finishEditing();
	//	}
		
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
		for(TableColumn col : columns) {
			((HasField)col.getColumnWidget()).getQuery(list, col.key);
		}	
	}
	
	public ArrayList<String> getErrors() {
		return errors;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
        if(dragController != null)
            dragController.setEnable(enabled);
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
		else
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
        if(getHandlerCount(BeforeCellEditedEvent.getType()) > 0) {
        	BeforeCellEditedEvent bce = BeforeCellEditedEvent.fire(this, modelIndexList[row], col, getRow(row).cells.get(col).value);
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

	
}
