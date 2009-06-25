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

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.screen.ScreenTableWidget;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.table.rewrite.TableViewInt.VerticalScroll;
import org.openelis.gwt.widget.table.rewrite.event.SourcesTableModelEvents;
import org.openelis.gwt.widget.table.rewrite.event.SourcesTableWidgetEvents;
import org.openelis.gwt.widget.table.rewrite.event.TableModelListener;
import org.openelis.gwt.widget.table.rewrite.event.TableWidgetListener;
import org.openelis.gwt.widget.table.rewrite.event.TableWidgetListenerCollection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
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
public class TableWidget extends FocusPanel implements
                            SourcesChangeEvents,
                            SourcesTableWidgetEvents,
                            TableModelListener,
                            TableWidgetListener,
                            FocusListener,
                            ClickHandler
                            {
    

    public ArrayList<TableColumn> columns;
    public ChangeListenerCollection changeListeners;
    public TableWidgetListenerCollection tableWidgetListeners; 
    public boolean enabled;
    public boolean focused;
    public int activeRow = -1;
    public int activeCell = -1;
    public TableModelInt model;
    public TableView view;
    public TableRendererInt renderer;
    public TableKeyboardHandlerInt keyboardHandler;
    public TableMouseHandlerInt mouseHandler;
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
    public ArrayList<Filter[]> filters;
    public ScreenTableWidget screenWidget;
    public boolean isDropdown = false;
    public TableDragController dragController;
    public TableIndexDropController dropController;
    public boolean selectedByClick;
    public VerticalScroll showScroll = VerticalScroll.NEEDED;
    public String width;
    
    public TableWidget() {
        
    }
    
    
    public void init(){
        renderer = new TableRenderer(this);
        model = new TableModel(this);
        view = new TableView(this,showScroll);
        view.setWidth(width);
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        keyboardHandler = new TableKeyboardHandler(this);
        mouseHandler = new TableMouseHandler(this);
        addTableWidgetListener((TableWidgetListener)renderer);
        setWidget(view);
        addFocusListener(this);
        
    }
    
    public void setTableWidth(String width) {
    	this.width = width;
    }

    /**
     * This method handles all click events on the body of the table
     */
    public void onClick(ClickEvent event) {
    	Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);
        focused = true;
        if(!(columns.get(cell.getCellIndex()).getColumnWidget() instanceof CheckBox) && activeRow == cell.getRowIndex() && activeCell == cell.getCellIndex())
            return;
        selectedByClick = true;
        select(cell.getRowIndex(), cell.getCellIndex());
        selectedByClick = false;
    }

    /**
     * This method will unselect the row specified. Unselecting will save any
     * datat that has been changed in the row to the model.
     * 
     * @param row
     */
    public void unselect(int row) {
        if(editingCell != null){
            tableWidgetListeners.fireFinishedEditing(this, activeRow, activeCell);
        }
        model.unselectRow(modelIndexList[row]);
    }
    
    /**
     * This method will cause the table row passed to be selected. If the row is
     * already selected, the column clicked will be opened for editing if the
     * cell is editable and the user has the correct permissions.
     * 
     * @param row
     * @param col
     */
    public void select(final int row, final int col) {
        if(finishEditing()){
            if(model.numRows() >= maxRows){
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
        if(model.canSelect(modelIndexList[row])){
            focused = true;
            if(activeRow != row){
                if(activeRow > -1 && !ctrlKey){
                    model.unselectRow(-1);
                }
                activeRow = row;
                model.selectRow(modelIndexList[row]);
            }
            if(activeCell > -1 && activeCell != col) {
                if(columns.get(activeCell).getColumnWidget() instanceof CheckBox) {
                    //((CheckBox)view.table.getWidget(activeRow,activeCell)).onLostFocus(this);
                }
            }
            if(model.canEdit(modelIndexList[row],col)){
                activeCell = col;
                tableWidgetListeners.fireStartedEditing(this, row, col);
                if(columns.get(col).getColumnWidget() instanceof CheckBox) {
                    if(selectedByClick){
                        ((CheckBox)view.table.getWidget(row,col)).setState("CHECKED");
                        if(finishEditing()){
                            view.table.getRowFormatter().addStyleName(activeRow, view.selectedStyle);
                        }
                    }
                    //((Checkbox)view.table.getWidget(row,col)).onFocus(this);
                    editingCell = null;
                }
            }else
                activeCell = -1;
        }else{
            return;
        }
    }
    
    public boolean finishEditing() {
        if(editingCell != null) {
            tableWidgetListeners.fireStopEditing(this, activeRow, activeCell);
            if(model.isAutoAdd() && modelIndexList[activeRow] == model.numRows()){
                if(model.canAutoAdd(model.getAutoAddRow())){
                    model.addRow(model.getAutoAddRow());
                    tableWidgetListeners.fireFinishedEditing(this, modelIndexList[activeRow], activeCell);
                    return true;
                }
            }
            tableWidgetListeners.fireFinishedEditing(this, modelIndexList[activeRow], activeCell);
        }
        return false;
        
    }
    
    public void startEditing(int row, int col) {
        select(row,col);
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(changeListeners == null)
            changeListeners = new ChangeListenerCollection(); 
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null)
            changeListeners.remove(listener);
    }
    
    public void enabled(boolean enabled){
        this.enabled = enabled;
        if(dragController != null)
            dragController.setEnable(enabled);
        for(TableColumn column : columns) {
            column.enable(enabled);
        }
        //Iterator widIt = view.table.iterator();
        //while(widIt.hasNext()){
          //  ((ScreenWidget)widIt.next()).enable(enabled);
       // }
    }

    public void addTableWidgetListener(TableWidgetListener listener) {
        if(tableWidgetListeners == null)
            tableWidgetListeners = new TableWidgetListenerCollection();
        tableWidgetListeners.add(listener); 
    }

    public void removeTableWidgetListener(TableWidgetListener listener) {
        if(tableWidgetListeners != null)
            tableWidgetListeners.remove(listener);
    }
    
    public void setFocus(boolean focused) {
        this.focused = focused;
        super.setFocus(focused);
    }

    public void onFocus(Widget sender) {
       this.focused = true;
        
    }

    public void onLostFocus(Widget sender) {
        
    }

    public void cellUpdated(SourcesTableModelEvents sender, int row, int cell) {
        
    }

    public void dataChanged(SourcesTableModelEvents sender) {
        
    }

    public void rowAdded(SourcesTableModelEvents sender, int rows) {
        
    }

    public void rowDeleted(SourcesTableModelEvents sender, int row) {
        
    }

    public void rowSelected(SourcesTableModelEvents sender, int row) {
        
    }

    public void rowUnselected(SourcesTableModelEvents sender, int row) {
        
    }

    public void rowUpdated(SourcesTableModelEvents sender, int row) {
        
    }

    public void unload(SourcesTableModelEvents sender) {
       if(editingCell != null) {
           finishEditing();
       }
    }
    
    public void scrollToSelection(){
        if(model.numRows() == model.shownRows()){
            view.scrollBar.setScrollPosition(cellHeight*model.getSelectedIndex());
        }else{
            int shownIndex = 0;
            for(int i = 0; i < model.getSelectedIndex(); i++){
                if(model.getRow(i).shown)
                    shownIndex++;
            }
            view.scrollBar.setScrollPosition(cellHeight*shownIndex);
        }
    }

    public void finishedEditing(SourcesTableWidgetEvents sender, int row, int col) {
        if(columns.get(col).getColumnWidget() instanceof CheckBox) {
            //((SimplePanel)(TableCellWidget)view.table.getWidget(row, col)).addStyleName(view.widgetStyle);
            //((SimplePanel)(TableCellWidget)view.table.getWidget(row, col)).addStyleName("Enabled");
        }
        
    }

    public void startEditing(SourcesTableWidgetEvents sender, int row, int col) {
        if(columns.get(col).getColumnWidget() instanceof CheckBox) {
            if(selectedByClick){
                ((CheckBox)view.table.getWidget(row,col)).setState("CHECKED");
                finishEditing();
            }
        }
        
    }

    public void stopEditing(SourcesTableWidgetEvents sender, int row, int col) {
        // TODO Auto-generated method stub
        
    }
    
    
}