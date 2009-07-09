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

import org.openelis.gwt.screen.rewrite.Screen;
import org.openelis.gwt.widget.rewrite.CheckBox;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

public class TableKeyboardHandler implements TableKeyboardHandlerInt {
    
    private TableWidget controller;
    private Screen screen;
    
    public TableKeyboardHandler(TableWidget controller) {
        this.controller = controller;
    }
    
    public void setScreen(Screen screen) {
        this.screen = screen;
    }
    

    
    private int findNextActive(int current) {
        int next = current + 1;
        while(next < controller.modelIndexList.length && !controller.isEnabled(controller.modelIndexList[next]))
            next++;
        if(next < controller.modelIndexList.length)
            return next;
        controller.view.setScrollPosition(controller.view.top+controller.cellHeight);
        return findNextActive(controller.modelIndexList.length-2);
    }
    
    private int findPrevActive(int current) {
        int prev = current - 1;
        while(prev > -1 && !controller.isEnabled(controller.modelIndexList[prev]))
            prev--;
        if(prev >  -1)
            return prev;
        controller.view.setScrollPosition(controller.view.top-controller.cellHeight);
        return findPrevActive(1);
    }
    
    private void tabToNextRow() {
        int row = findNextActive(controller.activeRow);
        int col = 0;
       
        while ((controller.columns.get(col).getColumnWidget() instanceof Label) || (!controller.canEdit(row, col)))
            col++;
        if(row < controller.view.table.getRowCount()){
            final int fRow = row;
            final int fCol = col;
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    controller.select(fRow, fCol);
                }
            });
        }
    }
    
    private void tabToPrevRow() {
        final int row = findPrevActive(controller.activeRow);
        int col = controller.columns.size() - 1;
        while ((controller.columns.get(col).getColumnWidget() instanceof Label) || (!controller.canEdit(row,col)))
            col--;
        final int fCol = col;
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                controller.select(row, fCol);
            }
        });
    }

	public void onKeyDown(KeyDownEvent event) {
        //if(!controller.focused)
          //  return;
        
        if(controller.editingCell instanceof CheckBox && KeyboardHandler.KEY_ENTER == event.getNativeKeyCode()){
        	return;
        }
        if(event.getNativeKeyCode() == KeyboardHandler.KEY_CTRL)
            controller.ctrlKey = true;
        if(event.getNativeKeyCode() == KeyboardHandler.KEY_SHIFT)
            controller.shiftKey = true;

        boolean shift = event.isShiftKeyDown();
        if (KeyboardHandler.KEY_DOWN == event.getNativeKeyCode()) {
            if (controller.activeRow >= 0 && controller.activeRow < controller.shownRows() - 1) {
                if(controller.activeRow < controller.view.table.getRowCount() -1){
                    final int row = findNextActive(controller.activeRow);
                    final int col = controller.activeCell;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            controller.select(row, col);
                        }
                    });
                }else{
                    controller.view.setScrollPosition(controller.view.top+controller.cellHeight);
                    findNextActive(controller.activeRow -1);
                    final int col = controller.activeCell;
                    controller.activeCell = -1;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            controller.select(controller.view.table.getRowCount()-1, col);
                        }
                    });
                }
            }
        }
        if (KeyboardHandler.KEY_UP == event.getNativeKeyCode()) {
            if (controller.activeRow >= 0 && controller.activeRow != 0) {
                final int row = findPrevActive(controller.activeRow);
                final int col = controller.activeCell;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        controller.select(row, col);
                    }
                });
            }else{
                controller.view.setScrollPosition(controller.view.top-controller.cellHeight);
                findPrevActive(1);
                final int col = controller.activeCell;
                controller.activeCell = -1;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        controller.select(0, col);
                    }
                });
            }
        }
        if (KeyboardHandler.KEY_ENTER == event.getNativeKeyCode()) {
            if(controller.editingCell != null) {
                if(controller.finishEditing()){
                    /*if(controller.columns.get(controller.activeCell).getColumnWidget() instanceof TableCheck) {
                            ((TableCheck)controller.view.table.getWidget(controller.activeRow,controller.activeCell)).check();
                            if(controller.finishEditing()){
                                controller.view.table.getRowFormatter().addStyleName(controller.activeRow, controller.view.selectedStyle);
                            }
                            ((TableCheck)controller.view.table.getWidget(controller.activeRow,controller.activeCell)).onFocus(null);
                    }*/
                    if(controller.numRows() >= controller.maxRows){
                        controller.view.scrollBar.scrollToBottom();
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                controller.activeRow--;
                                controller.selectRow(controller.modelIndexList[controller.activeRow]);
                                
                            }
                        });
                    }else{
                        controller.selectRow(controller.modelIndexList[controller.activeRow]);
                    }
                }else{
                    controller.selectRow(controller.modelIndexList[controller.activeRow]);
                }
            }else if(controller.activeCell > -1){
                if(controller.columns.get(controller.activeCell).getColumnWidget() instanceof CheckBox) {
                    ((CheckBox)controller.view.table.getWidget(controller.activeRow,controller.activeCell)).setState("CHECKED");
                    if(controller.finishEditing()){
                        controller.view.table.getRowFormatter().addStyleName(controller.activeRow, controller.view.selectedStyle);
                    }
                    //((TableCheck)controller.view.table.getWidget(controller.activeRow,controller.activeCell)).onFocus(null);
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
        if (KeyboardHandler.KEY_TAB == event.getNativeKeyCode() && controller.activeCell > -1 && !shift) {
            if(controller.activeRow < 0){
                controller.activeRow = 0;
                controller.activeCell = -1;
            }
            if((controller.modelIndexList[controller.activeRow] > controller.shownRows() || controller.modelIndexList[controller.activeRow] >= controller.numRows()) &&
                controller.activeCell + 1 >= controller.columns.size()) {
                if(screen != null){
                    if(!controller.finishEditing()){
                        controller.activeCell = -1;
                        controller.setFocus(true);
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                //screen.screen.doTab(false, screen);
                            }
                        });
                        return;
                    }
                }
            }
            if (controller.activeCell + 1 >= controller.columns.size()) {
                tabToNextRow();
            } else {
                int col = controller.activeCell + 1;
                while (col < controller.columns.size() && (controller.columns.get(col).getColumnWidget() instanceof Label ||
                       (!controller.canEdit(controller.activeRow, col))))
                    col++;
                if(col == controller.columns.size()){
                    tabToNextRow();
                }else{
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            controller.select(controller.activeRow, fCol);
                            /*controller.onCellClicked(controller.view.table, controller.activeRow, fCol);
                            if(((TableCellWidget)controller.view.table.getWidget(controller.activeRow, fCol)).getWidget() instanceof FocusWidget)
                                ((FocusWidget)((TableCellWidget)controller.view.table.getWidget(controller.activeRow, fCol)).getWidget()).setFocus(true);
                            */
                        }
                    });
                }
            }
        }
        if (KeyboardHandler.KEY_TAB == event.getNativeKeyCode() && controller.activeCell > -1 && shift) {
            if (controller.activeCell == 0 && controller.modelIndexList[controller.activeRow] == 0){
                if(screen != null){
                    controller.finishEditing();
                    controller.setFocus(true);
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            //screen.screen.doTab(true, screen);
                        }
                    });
                    return;
                }
            }
            if (controller.activeCell - 1 < 0) {
                tabToPrevRow();
            } else {
                int col = controller.activeCell - 1;
                while (col > -1 && ((controller.columns.get(col).getColumnWidget() instanceof Label) ||
                                    (!controller.canEdit(controller.activeRow, col))))
                    col--;
                if(col < 0){
                    tabToPrevRow();
                }else{
                    final int fCol = col;
                    //DeferredCommand.addCommand(new Command() {
                      //  public void execute() {
                            controller.select(controller.activeRow, fCol);
                       // }
                    //});
                }
            }
        }
		
	}

	public void onKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyboardHandler.KEY_CTRL)
            controller.ctrlKey = false;
        if(event.getNativeKeyCode() == KeyboardHandler.KEY_SHIFT)
            controller.shiftKey = false;
	}
}
