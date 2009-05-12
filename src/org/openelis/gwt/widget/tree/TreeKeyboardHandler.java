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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.table.TableLabel;

public class TreeKeyboardHandler implements TreeKeyboardHandlerInt {
    
    private TreeWidget controller;
    private ScreenWidget screen;
    
    public TreeKeyboardHandler(TreeWidget controller) {
        this.controller = controller;
    }
    
    public void setScreen(ScreenWidget screen) {
        this.screen = screen;
    }
    
    public void onKeyDown(Widget sender, char code, int modifiers) {

        if(!controller.focused)
            return;
        
        if(code == KeyboardListener.KEY_CTRL)
            controller.ctrlKey = true;
        if(code == KeyboardListener.KEY_SHIFT)
            controller.shiftKey = true;

        boolean shift = modifiers == KeyboardListener.MODIFIER_SHIFT;
        if (KeyboardListener.KEY_DOWN == code) {
            if (controller.activeRow >= 0 && controller.activeRow < controller.model.shownRows() - 1) {
                //if(rowList[selected] == autoAddRow)
                //    return;
                if(controller.activeRow < controller.view.table.getRowCount() -1){
                    final int row = findNextActive(controller.activeRow);
                    final int col = controller.activeCell;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            controller.onCellClicked(controller.view.table, row, col);
                        }
                    });
                }else{
                    controller.view.scrollBar.setScrollPosition(controller.view.scrollBar.getScrollPosition()+18);
                    controller.renderer.scrollLoad(controller.view.scrollBar.getScrollPosition());
                    findNextActive(controller.activeRow -1);
                    final int col = controller.activeCell;
                    controller.activeCell = -1;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            controller.onCellClicked(controller.view.table, controller.view.table.getRowCount()-1, col);
                        }
                    });
                }
            }
        }
        if (KeyboardListener.KEY_UP == code) {
            if (controller.activeRow >= 0 && controller.activeRow != 0) {
                final int row = findPrevActive(controller.activeRow);
                final int col = controller.activeCell;
                //unselect(selected);
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        controller.onCellClicked(controller.view.table, row, col);
                    }
                });
            }else{
                controller.view.scrollBar.setScrollPosition(controller.view.scrollBar.getScrollPosition()-18);
                controller.renderer.scrollLoad(controller.view.scrollBar.getScrollPosition());
                findPrevActive(1);
                final int col = controller.activeCell;
                controller.activeCell = -1;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        controller.onCellClicked(controller.view.table, 0, col);
                    }
                });
            }
        }
        if (KeyboardListener.KEY_ENTER == code) {
            if(controller.editingCell != null) {
                if(controller.finishEditing()){
                    if(controller.model.numRows() >= controller.maxRows){
                        controller.view.scrollBar.scrollToBottom();
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                controller.activeRow--;
                                controller.model.selectRow(controller.modelIndexList[controller.activeRow]);
                                
                            }
                        });
                    }else{
                        controller.model.selectRow(controller.modelIndexList[controller.activeRow]);
                    }
                }else{
                   // controller.model.selectRow(controller.modelIndexList[controller.activeRow]);
                }
            }else if(controller.activeRow < 0) {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        tabToNextRow();
                    }
                });
            }else {
                controller.activeRow--;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        tabToNextRow();
                    }
                });
            }
        }
        /*
        if (KeyboardListener.KEY_RIGHT == code && model.paged) {
            if (model.pageIndex != model.totalPages - 1)
                manager.getNextPage(this);
        }
        if (KeyboardListener.KEY_LEFT == code && model.paged) {
            if (model.pageIndex != 0)
                manager.getPreviousPage(this);
        }
        */
        if (KeyboardListener.KEY_TAB == code && controller.activeCell > -1 && !shift) {
            if(controller.activeRow < 0){
                controller.activeRow = 0;
                controller.activeCell = -1;
            }
            if((controller.modelIndexList[controller.activeRow] > controller.model.shownRows() || controller.modelIndexList[controller.activeRow] == controller.model.numRows()-1) &&
                controller.activeCell + 1 >= controller.columns.size()) {
                if(screen != null){
                    controller.treeWidgetListeners.fireStopEditing(controller, controller.activeRow,controller.activeCell);
                    controller.activeCell = -1;
                    controller.editingCell = null;
                    controller.setFocus(true);
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            screen.screen.doTab(false, screen);
                        }
                    });
                    return;
                }
            }
            if (controller.activeCell + 1 >= controller.columns.size()) {
                tabToNextRow();
            } else {
                int col = controller.activeCell + 1;
                while (col < controller.columns.size() && (controller.columns.get(col).getColumnWidget(controller.model.getRow(controller.activeRow).leafType) instanceof TableLabel ||
                       (!controller.model.canEdit(controller.activeRow, col))))
                    col++;
                if(col == controller.columns.size()){
                    tabToNextRow();
                }else{
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            controller.onCellClicked(controller.view.table, controller.activeRow, fCol);
                            if(((TableCellWidget)controller.view.table.getWidget(controller.activeRow, fCol)).getWidget() instanceof FocusWidget)
                                ((FocusWidget)((TableCellWidget)controller.view.table.getWidget(controller.activeRow, fCol)).getWidget()).setFocus(true);
                        }
                    });
                }
                //((FocusWidget)view.table.getWidget(selected, selectedCell)).setFocus(true);
            }
        }
        if (KeyboardListener.KEY_TAB == code && controller.activeCell > -1 && shift) {
            if (controller.activeCell == 0 && controller.activeRow == 0){
                if(screen != null){
                    controller.treeWidgetListeners.fireStopEditing(controller, controller.activeRow,controller.activeCell);
                    controller.activeCell = -1;
                    controller.editingCell = null;
                    controller.setFocus(true);
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            screen.screen.doTab(true, screen);
                        }
                    });
                    return;
                }
            }
            if (controller.activeCell - 1 < 0) {
                tabToPrevRow();
            } else {
                int col = controller.activeCell - 1;
                while (col > -1 && ((controller.columns.get(col).getColumnWidget(controller.model.getRow(controller.activeRow).leafType) instanceof TableLabel) ||
                                    (!controller.model.canEdit(controller.activeRow, col))))
                    col--;
                if(col < 0){
                    tabToPrevRow();
                }else{
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            controller.onCellClicked(controller.view.table, controller.activeRow, fCol);
                        }
                    });
                }
            }
        }
    }
    
    private int findNextActive(int current) {
        int next = current + 1;
        while(next < controller.modelIndexList.length && !controller.model.isEnabled(controller.modelIndexList[next]))
            next++;
        if(next < controller.modelIndexList.length)
            return next;
        controller.view.scrollBar.setScrollPosition(controller.view.scrollBar.getScrollPosition()+controller.cellHeight);
        controller.renderer.scrollLoad(controller.view.scrollBar.getScrollPosition());
        return findNextActive(controller.modelIndexList.length-2);
    }
    
    private int findPrevActive(int current) {
        int prev = current - 1;
        while(prev > -1 && !controller.model.isEnabled(controller.modelIndexList[prev]))
            prev--;
        if(prev >  -1)
            return prev;
        controller.view.scrollBar.setScrollPosition(controller.view.scrollBar.getScrollPosition()-controller.cellHeight);
        controller.renderer.scrollLoad(controller.view.scrollBar.getScrollPosition());
        return findPrevActive(1);
    }
    
    private void tabToNextRow() {
        int row = findNextActive(controller.activeRow);
        int col = 0;
       
        while ((controller.columns.get(col).getColumnWidget(controller.model.getRow(row).leafType) instanceof TableLabel) || (!controller.model.canEdit(row, col)))
            col++;
        if(row < controller.view.table.getRowCount() - 1){
            final int fRow = row;
            final int fCol = col;
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    controller.onCellClicked(controller.view.table, fRow, fCol);
                }
            });
        }else{
            controller.view.scrollBar.setScrollPosition(controller.view.scrollBar.getScrollPosition()+18);
            controller.renderer.scrollLoad(controller.view.scrollBar.getScrollPosition());
            findNextActive(controller.view.table.getRowCount() -2);
            final int fCol = col;
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    controller.onCellClicked(controller.view.table,controller.view.table.getRowCount() -1,fCol);
                }
            });
        }
    }
    
    private void tabToPrevRow() {
        if(controller.activeRow == 0) {
                int col = controller.model.getRow(controller.model.shownRows() -1).size() - 1;
                while ((controller.columns.get(col).getColumnWidget(controller.model.getRow(0).leafType) instanceof TableLabel) || (!controller.model.canEdit(controller.activeRow,col)))
                    col--;
                final int fCol = col;
                controller.view.scrollBar.setScrollPosition(controller.view.scrollBar.getScrollPosition()-18);
                controller.renderer.scrollLoad(controller.view.scrollBar.getScrollPosition());
                findPrevActive(1);
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        controller.onCellClicked(controller.view.table, 0, fCol);
                    }
                });
        }else{
            final int row = findPrevActive(controller.activeRow);
            int col = controller.model.getRow(controller.model.shownRows() -1).size() - 1;
            while ((controller.columns.get(col).getColumnWidget(controller.model.getRow(row).leafType) instanceof TableLabel) || (!controller.model.canEdit(row,col)))
                col--;
            final int fCol = col;
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    controller.onCellClicked(controller.view.table, row, fCol);
                }
            });
        }
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {

        
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        if(keyCode == KeyboardListener.KEY_CTRL)
            controller.ctrlKey = false;
        if(keyCode == KeyboardListener.KEY_SHIFT)
            controller.shiftKey = false;
        
    }
}
