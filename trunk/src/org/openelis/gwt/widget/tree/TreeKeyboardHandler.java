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

import org.openelis.gwt.widget.CheckBox;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Label;

public class TreeKeyboardHandler implements KeyDownHandler, KeyUpHandler {
    
    private TreeWidget controller;
    
    public TreeKeyboardHandler(TreeWidget controller) {
        this.controller = controller;
    }
        
    public void onKeyDown(KeyDownEvent event) {

        if(controller.activeWidget instanceof CheckBox && KeyCodes.KEY_ENTER == event.getNativeKeyCode()){
        	return;
        }
        
        if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
            controller.ctrlKey = true;
        if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
            controller.shiftKey = true;

        boolean shift = event.isShiftKeyDown();
        if (KeyCodes.KEY_DOWN == event.getNativeKeyCode()) {
            if (controller.selectedRow >= 0 && controller.selectedRow < controller.numRows() - 1) {
            	final int row = findNextActive(controller.selectedRow);
            	int col = controller.selectedCol;
            	while (col > controller.getColumns().get(controller.getRow(row).leafType).size() -1)
            		col--;
            	final int fcol = col;
            	if(!controller.isRowDrawn(row)){
            		controller.view.setScrollPosition(controller.view.top+(controller.cellHeight*(row-controller.selectedRow)));
            		controller.unselect(-1);
            	}
            	DeferredCommand.addCommand(new Command() {
            		public void execute() {
            			controller.select(row, fcol);
            		}
            	});

            }
        }
        if (KeyCodes.KEY_UP == event.getNativeKeyCode()) {
            if (controller.selectedRow > 0) {
                final int row = findPrevActive(controller.selectedRow);
            	int col = controller.selectedCol;
            	while (col > controller.getColumns().get(controller.getRow(row).leafType).size() -1)
            		col--;
            	final int fcol = col;
            	if(!controller.isRowDrawn(row)){
            		controller.view.setScrollPosition(controller.view.top-(controller.cellHeight*(controller.selectedRow-row)));
            		controller.unselect(-1);
            	}
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        controller.select(row, fcol);
                    }
                });
            }
        }
        if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
            if(controller.activeWidget != null) {
                controller.finishEditing();
            }else if(controller.selectedRow < 0) {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        tabToNextRow(controller.selectedRow);
                    }
                });
            }else if(controller.selectedCol > -1){
                if(controller.columns.get(controller.getRow(controller.selectedRow).leafType).get(controller.selectedCol).getColumnWidget() instanceof CheckBox) {
                    ((CheckBox)controller.view.table.getWidget(controller.selectedRow,controller.selectedCol)).setState("CHECKED");
                    controller.finishEditing();
            }
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
            if(controller.selectedRow < 0){
                controller.selectedRow = 0;
                controller.selectedCol = -1;
            }
            if((controller.selectedRow >= controller.shownRows()-1 || controller.selectedRow >= controller.numRows()-1) &&
                controller.selectedCol + 1 >= controller.columns.get(controller.getRow(controller.selectedRow).leafType).size()) {
                  controller.finishEditing();
                  controller.selectedCol = -1;
                  controller.setFocus(true);
                  return;
            }
            if (controller.selectedCol + 1 >= controller.columns.get(controller.getRow(controller.selectedRow).leafType).size()) {
                tabToNextRow(controller.selectedRow);
            } else {
                int col = controller.selectedCol + 1;
                while (col < controller.columns.get(controller.getRow(controller.selectedRow).leafType).size() && (controller.columns.get(controller.getRow(controller.selectedRow).leafType).get(col).getColumnWidget() instanceof Label ||
                       (!controller.canEditCell(controller.selectedRow,col))))
                    col++;
                if(col == controller.columns.get(controller.getRow(controller.selectedRow).leafType).size()){
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
            if (controller.selectedCol == 0 && controller.selectedRow == 0){
                    controller.finishEditing();
                    controller.setFocus(true);
                    controller.selectedCol = -1;
                    return;

            }
            if (controller.selectedCol - 1 < 0) {
                tabToPrevRow(controller.selectedRow);
            } else {
                int col = controller.selectedCol - 1;
                while (col > -1 && ((controller.columns.get(controller.getRow(controller.selectedRow).leafType).get(col).getColumnWidget() instanceof Label) ||
                                    (!controller.canEditCell(controller.selectedRow, col))))
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
        if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
            controller.ctrlKey = false;
        if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
            controller.shiftKey = false;
	}
    
    private int findNextActive(int current) {
        int next = current + 1;
        while(next < controller.numRows() && !controller.isEnabled(next))
            next++;
        if(next <= controller.numRows())
            return next;
        return findNextActive(next);
    }
    
    private int findPrevActive(int current) {
        int prev = current - 1;
        while(prev > -1 && !controller.isEnabled(prev))
            prev--;
        if(prev >=  -1)
            return prev;
        return findPrevActive(1);
    }
    
    private void tabToNextRow(int row) {
        row = findNextActive(row);
        int col = 0;
        if(row >= controller.numRows()){
        	controller.finishEditing();
        	return;
        }
        while (col < controller.columns.get(controller.getRow(row).leafType).size() && (controller.columns.get(controller.getRow(row).leafType).get(col).getColumnWidget() instanceof Label) || (!controller.canEditCell(row, col)))
            col++;
        if(col == controller.columns.get(controller.getRow(row).leafType).size()){
        	tabToNextRow(row);
        	return;
        }
        if(row < controller.numRows()){
            final int fRow = row;
            final int fCol = col;
            if(!controller.isRowDrawn(fRow)){
            	controller.view.setScrollPosition(controller.view.top+(controller.cellHeight*(row-controller.selectedRow)));
        		controller.unselect(-1);
            }
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    controller.select(fRow, fCol);
                }
            });
        }
    }
    
    private void tabToPrevRow(int row) {
    	final int frow = findPrevActive(row);
    	if(frow == -1){
    		controller.finishEditing();
    		return;
    	}
    	int col = controller.getColumns().get(controller.getRow(frow).leafType).size() - 1;
    	while (col > -1 && (controller.columns.get(controller.getRow(frow).leafType).get(col).getColumnWidget() instanceof Label) || (!controller.canEditCell(frow, col)))
    		col--;
    	if(col == -1){
    		tabToPrevRow(frow);
    		return;
    	}
    	final int fCol = col;
    	if(!controller.isRowDrawn(row)){
    		controller.view.setScrollPosition(controller.view.top-(controller.cellHeight*(controller.selectedRow-frow)));
    		controller.unselect(-1);
    	}
    	DeferredCommand.addCommand(new Command() {
    		public void execute() {
    			controller.select(frow, fCol);
    		}
    	});
	}

}
