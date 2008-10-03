/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;
import org.openelis.gwt.widget.table.event.SourcesTableModelEvents;
import org.openelis.gwt.widget.table.event.SourcesTableWidgetEvents;
import org.openelis.gwt.widget.table.event.TableModelListener;
import org.openelis.gwt.widget.table.event.TableWidgetListener;
import org.openelis.gwt.widget.table.event.TableWidgetListenerCollection;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is the main controller for the Table widget. It hooks the model to
 * the view and also controls when row selection and calls to the manager for
 * permissions and actions set by the a developer.
 * 
 * @author tschmidt
 * 
 */
public class TableWidget extends FocusPanel implements
                            TableListener,
                            SourcesChangeEvents,
                            SourcesTableWidgetEvents,
                            TableModelListener,
                            FocusListener{
    

    public ArrayList<TableColumnInt> columns;
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
    public TableCellWidget editingCell = null;
    public int[] modelIndexList;
    public boolean showRows;
    public String title;
    public boolean showHeader;
    public ArrayList<Filter[]> filters;
    public TableWidget() {
        
    }
    
    public TableWidget(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll){
        for(TableColumnInt column : columns) {
            column.setTableWidget(this);
        }
        this.columns = columns;
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
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
    
    /**
     * Default constructor, puts table on top of the event stack.
     * 
     */
    public TableWidget(ArrayList<TableColumnInt> columns,TableModel model, TableView view, TableRenderer renderer) {
        this.columns = columns;
        this.model = model;
        this.view = view;
        this.renderer = renderer;
        model.addTableModelListener(renderer);
        addTableWidgetListener(renderer);
        view.table.addTableListener(this);
        keyboardHandler = new TableKeyboardHandler(this);
        mouseHandler = new TableMouseHandler(this);
    }

    /**
     * This method handles all click events on the body of the table
     */
    public void onCellClicked(SourcesTableEvents sender, int row, int col) {
        focused = true;
        if(activeRow == row && activeCell == col)
            return;
        select(row, col);
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
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    renderer.scrollLoad(view.scrollBar.getScrollPosition());
                    select(row-1,col);
                }
            });
        }
        if(model.canSelect(modelIndexList[row])){
            if(activeRow > -1 && !ctrlKey){
                model.unselectRow(-1);
            }
            activeRow = row;
            model.selectRow(modelIndexList[row]);           
            if(model.canEdit(modelIndexList[row],col)){
                activeCell = col;
                tableWidgetListeners.fireStartedEditing(this, row, col);
            }else
                activeCell = -1;
        }else{
            return;
        }
    }
    
    public boolean finishEditing() {
        if(editingCell != null) {
            tableWidgetListeners.fireFinishedEditing(this, activeRow, activeCell);
            if(model.isAutoAdd() && modelIndexList[activeRow] == model.numRows()){
                if(model.canAutoAdd(model.getAutoAddRow())){
                    model.addRow(model.getAutoAddRow());
                    if(model.numRows() >= maxRows){
                        view.scrollBar.scrollToBottom();
                        return true;
                    }
                }
            }
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
        for(TableColumnInt column : columns) {
            column.enable(enabled);
        }
        Iterator widIt = view.table.iterator();
        while(widIt.hasNext()){
            ((TableCellWidget)widIt.next()).enable(enabled);
        }
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

    public void rowSelectd(SourcesTableModelEvents sender, int row) {
        
    }

    public void rowUnselected(SourcesTableModelEvents sender, int row) {
        
    }

    public void rowUpdated(SourcesTableModelEvents sender, int row) {
        
    }

    public void unload(SourcesTableModelEvents sender) {
       if(editingCell != null) {
           tableWidgetListeners.fireFinishedEditing(this, activeRow, activeCell);
       }
    }
    
    public void scrollToSelection(){
        if(model.numRows() == model.shownRows()){
            view.scrollBar.setScrollPosition(cellHeight*model.getData().getSelectedIndex());
        }else{
            int shownIndex = 0;
            for(int i = 0; i < model.getData().getSelectedIndex(); i++){
                if(model.getRow(i).shown)
                    shownIndex++;
            }
            view.scrollBar.setScrollPosition(cellHeight*shownIndex);
        }
    }
    
}
