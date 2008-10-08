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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.screen.ScreenInputWidget;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;
import org.openelis.gwt.widget.table.event.TableWidgetListener;

import java.util.ArrayList;

/**
 * This class is the main controller for the Table widget. It hooks the model to
 * the view and also controls when row selection and calls to the manager for
 * permissions and actions set by the a developer.
 * 
 * @author tschmidt
 * 
 */
public class QueryTable extends TableWidget implements TableKeyboardHandlerInt, TableMouseHandlerInt{
    
    public FormRPC rpc;
    public ScreenInputWidget screen;
    /**
     * Default constructor, puts table on top of the event stack.
     * 
     */
    public QueryTable() {
       
    }

    public QueryTable(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll){
        for(TableColumnInt column : columns) {
            column.setTableWidget(this);
        }
        this.columns = columns;
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
        renderer = new QueryTableRenderer(this);
        view = new TableView(this,showScroll);
        view.setWidth("auto");
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        keyboardHandler = this;
        mouseHandler = this;
        addTableWidgetListener((TableWidgetListener)renderer);
        setWidget(view);
        addFocusListener(this);
    }
    
    public void select(int row, int col) {
        finishEditing();
        activeRow = 0;
        activeCell = col;
        tableWidgetListeners.fireStartedEditing(this, row, col);
    }

    public boolean finishEditing() {
        if(editingCell != null) {
            tableWidgetListeners.fireFinishedEditing(this, activeRow, activeCell);
        }
        return false;
    }

    public void onKeyDown(Widget sender, char code, int modifiers) {
        if(!focused)
            return;
        boolean shift = modifiers == KeyboardListener.MODIFIER_SHIFT;
        if (KeyboardListener.KEY_ENTER == code) {
            if(editingCell != null) {
                tableWidgetListeners.fireFinishedEditing(this, activeRow,activeCell);
                activeCell = -1;
            }else if(activeRow < 0) {
                activeRow = 0;
                int col = 0;
                while ((columns.get(col).getColumnWidget() instanceof TableLabel) )
                   col++;
                final int fCol = col;
                DeferredCommand.addCommand(new Command() {
                   public void execute() {
                      onCellClicked(view.table, 0, fCol);
                   }
                });
            }
        }
        if (KeyboardListener.KEY_TAB == code && activeCell > -1 && !shift) {
            if(activeRow < 0){
                activeRow = 0;
                activeCell = -1;
            }
            if(activeCell == columns.size()-1) {
                if(screen != null){
                    tableWidgetListeners.fireFinishedEditing(this, this.activeRow,this.activeCell);
                    activeCell = -1;
                    editingCell = null;
                    setFocus(true);
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            screen.screen.doTab(false, screen);
                        }
                    });
                    return;
                }
            } else {
                int col = activeCell + 1;
                while (col < columns.size() && (columns.get(col).getColumnWidget() instanceof TableLabel))
                    col++;
                if(col == columns.size()){
                    if(screen != null){
                        tableWidgetListeners.fireFinishedEditing(this, this.activeRow,this.activeCell);
                        activeCell = -1;
                        editingCell = null;
                        setFocus(true);
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                screen.screen.doTab(false, screen);
                            }
                        });
                        return;
                    }
                }else{
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, activeRow, fCol);
                            if(((TableCellWidget)view.table.getWidget(activeRow, fCol)).getWidget() instanceof FocusWidget)
                                ((FocusWidget)((TableCellWidget)view.table.getWidget(activeRow, fCol)).getWidget()).setFocus(true);
                        }
                    });
                }
                //((FocusWidget)view.table.getWidget(selected, selectedCell)).setFocus(true);
            }
        }
        if (KeyboardListener.KEY_TAB == code && activeCell > -1 && shift) {
            if (activeCell == 0){
                if(screen != null){
                    tableWidgetListeners.fireFinishedEditing(this, activeRow,activeCell);
                    activeCell = -1;
                    editingCell = null;
                    setFocus(true);
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            screen.screen.doTab(true, screen);
                        }
                    });
                    return;
                }
            } else {
                int col = activeCell - 1;
                while (col > -1 && ((columns.get(col).getColumnWidget() instanceof TableLabel))) 
                    col--;
                if(col < 0){
                    if(screen != null){
                        tableWidgetListeners.fireFinishedEditing(this, activeRow,activeCell);
                        activeCell = -1;
                        editingCell = null;
                        setFocus(true);
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                screen.screen.doTab(true, screen);
                            }
                        });
                        return;
                    }
                }else{
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, activeRow, fCol);
                        }
                    });
                }
            }
        }
        
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onClick(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
    public void load(FormRPC rpc) {
        this.rpc = rpc;
        renderer.load(0);
    }
    
    public FormRPC unload() {
        tableWidgetListeners.fireFinishedEditing(this, activeRow, activeCell);
        return rpc;
    }
 
    
}
