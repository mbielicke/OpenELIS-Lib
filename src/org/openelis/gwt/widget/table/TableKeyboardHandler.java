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

import org.openelis.gwt.widget.CheckBox;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Label;


public class TableKeyboardHandler implements TableKeyboardHandlerInt {
    
    private TableWidget controller;
    
    public TableKeyboardHandler(TableWidget controller) {
        this.controller = controller;
    }
    
    private int findNextActive(int current) {
        int next = current + 1;
        while(next < controller.numRows() && !controller.isEnabled(next))
            next++;
        if(next < controller.numRows())
            return next;
        controller.finishEditing();
        controller.selectedCol = -1;
        controller.setFocus(true);
        return -1;
    }
    
    private int findPrevActive(int current) {
        int prev = current - 1;
        while(prev > -1 && !controller.isEnabled(prev))
            prev--;
        if(prev >  -1)
            return prev;
        controller.finishEditing();
        controller.selectedCol = -1;
        controller.setFocus(true);
        return -1;
    }
    
    private void tabToNextRow(int curRow) {
        int row = findNextActive(curRow);
        if(row < 0)
            return;
        int col = 0;
       
        while ((controller.columns.get(col).getColumnWidget() instanceof Label) || (!controller.canEditCell(row,col))) {
            col++;
            if(col == controller.columns.size()) {
                tabToNextRow(row);
                return;
            } 
        }
       
        if(row < controller.numRows()){
            final int fRow = row;
            final int fCol = col;
            if(!controller.isRowDrawn(fRow)){
                controller.view.setScrollPosition(controller.view.top+(controller.cellHeight*(row-controller.selectedRow)));
                controller.unselect(-1);
            }
            //DeferredCommand.addCommand(new Command() {
              //  public void execute() {
                    controller.select(fRow, fCol);
               // }
            //});
        }
    }
    
    private void tabToPrevRow(int curRow) {
        final int row = findPrevActive(curRow);
        if(row < 0)
            return;
        int col = controller.columns.size() - 1;
        while ((controller.columns.get(col).getColumnWidget() instanceof Label) || (!controller.canEditCell(row,col))) {
            col--;
            if(col < 0) {
                tabToPrevRow(row);
                return;
            }
        }
            
        final int fCol = col;
        if(!controller.isRowDrawn(row)){
            controller.view.setScrollPosition(controller.view.top-(controller.cellHeight*(controller.selectedRow-row)));
            controller.unselect(-1);
        }
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                controller.select(row, fCol);
            }
        });
    }

    public void onKeyDown(KeyDownEvent event) {
        if(controller.activeWidget instanceof CheckBox && KeyCodes.KEY_ENTER == event.getNativeKeyCode()){
            return;
        }
        /*
        if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
            controller.ctrlKey = true;
        if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
            controller.shiftKey = true;
        */
        boolean shift = event.isShiftKeyDown();
        if (KeyCodes.KEY_DOWN == event.getNativeKeyCode()) {
            if (controller.selectedRow >= 0 && controller.selectedRow < controller.numRows() - 1) {
                controller.finishEditing();
                final int row = findNextActive(controller.selectedRow);
                final int col = controller.selectedCol;
                if(!controller.isRowDrawn(row)){
                    controller.view.setScrollPosition(controller.view.top+(controller.cellHeight*(row-controller.selectedRow)));
                    controller.unselect(-1);
                }
                DeferredCommand.addCommand(new Command() {
                     public void execute() {
                         controller.select(row, col);
                     }
                 });
            }
        }
        if (KeyCodes.KEY_UP == event.getNativeKeyCode()) {
            if (controller.selectedRow > 0) {
                controller.finishEditing();
                final int row = findPrevActive(controller.selectedRow);
                final int col = controller.selectedCol;
                if(!controller.isRowDrawn(row)){
                    controller.view.setScrollPosition(controller.view.top-(controller.cellHeight*(controller.selectedRow-row)));
                    controller.unselect(-1);
                }
                DeferredCommand.addCommand(new Command() {
                     public void execute() {
                         controller.select(row, col);
                     }
                 });
            }
        }
        if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
            if(controller.activeWidget != null) {
                controller.finishEditing();
            }else if(controller.selectedCol > -1){
                if(controller.columns.get(controller.selectedCol).getColumnWidget() instanceof CheckBox) {
                    ((CheckBox)controller.view.table.getWidget(controller.tableIndex(controller.selectedRow),controller.selectedCol)).setState("CHECKED");
                    controller.finishEditing();
            }
            }else if(controller.selectedRow < 0) {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        tabToNextRow(controller.selectedRow);
                    }
                });
            }else {
                controller.selectedRow--;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        tabToNextRow(controller.selectedRow);
                    }
                });
            }
        }
        if (KeyCodes.KEY_TAB == event.getNativeKeyCode() && controller.selectedCol > -1 && !shift) {
            event.preventDefault();
            int col = controller.selectedCol;
            controller.finishEditing();
            if(controller.selectedRow < 0){
                controller.selectedRow = 0;
                //controller.selectedCol = -1;
            }
            if((controller.selectedRow >= controller.shownRows()-1 || controller.selectedRow >= controller.numRows()-1) &&
                col + 1 >= controller.columns.size()) {
                  //controller.selectedCol = -1;
                  controller.setFocus(true);
                  return;
            }
            if (col + 1 >= controller.columns.size()) {
                tabToNextRow(controller.selectedRow);
            } else {
                col++;
                while (col < controller.columns.size() && (controller.columns.get(col).getColumnWidget() instanceof Label ||
                       (!controller.canEditCell(controller.selectedRow, col)))) 
                    col++;
                if(col == controller.columns.size()){
                    tabToNextRow(controller.selectedRow);
                }else{
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            controller.select(controller.selectedRow, fCol);

                        }
                    });
                }
            }
        }
        if (KeyCodes.KEY_TAB == event.getNativeKeyCode() && controller.selectedCol > -1 && shift) {
            event.preventDefault();
            int col = controller.selectedCol;
            controller.finishEditing();
            if (col == 0 && controller.selectedRow == 0){
               //controller.selectedCol = -1;
               controller.setFocus(true);
               return;
            }
            if (col - 1 < 0) {
                tabToPrevRow(controller.selectedRow);
            } else {
                col--;
                while (col > -1 && ((controller.columns.get(col).getColumnWidget() instanceof Label) ||
                                    (!controller.canEditCell(controller.selectedRow,col))))
                    col--;
                if(col < 0){
                    tabToPrevRow(controller.selectedRow);
                }else{
                    final int fCol = col;
                    controller.select(controller.selectedRow, fCol);
                }
            }
        }
        
    }

    public void onKeyUp(KeyUpEvent event) {
        /*
        if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
            controller.ctrlKey = false;
        if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
            controller.shiftKey = false;
        */
    }
}
