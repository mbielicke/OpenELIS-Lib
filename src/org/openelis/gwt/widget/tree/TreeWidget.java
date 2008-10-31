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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DragListenerCollection;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.dnd.DropListenerCollection;
import com.google.gwt.user.client.dnd.MouseDragGestureRecognizer;
import com.google.gwt.user.client.dnd.SourcesDragEvents;
import com.google.gwt.user.client.dnd.SourcesDropEvents;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.screen.ScreenTreeWidget;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.screen.ScreenWindow;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.tree.TreeViewInt.VerticalScroll;
import org.openelis.gwt.widget.tree.event.SourcesTreeModelEvents;
import org.openelis.gwt.widget.tree.event.SourcesTreeWidgetEvents;
import org.openelis.gwt.widget.tree.event.TreeModelListener;
import org.openelis.gwt.widget.tree.event.TreeWidgetListener;
import org.openelis.gwt.widget.tree.event.TreeWidgetListenerCollection;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class TreeWidget extends FocusPanel implements
                                          TableListener,
                                          SourcesChangeEvents,
                                          SourcesTreeWidgetEvents,
                                          TreeModelListener,
                                          FocusListener,
                                          CommandListener{

    public ArrayList<TreeColumnInt> columns;
    public ChangeListenerCollection changeListeners;
    public TreeWidgetListenerCollection treeWidgetListeners;
    public boolean enabled;
    public boolean focused;
    public int activeRow = -1;
    public int activeCell = -1;
    public TreeModel model;
    public TreeView view;
    public TreeRendererInt renderer;
    public TreeKeyboardHandlerInt keyboardHandler;
    public TreeMouseHandlerInt mouseHandler;
    public boolean shiftKey;
    public boolean ctrlKey;
    public int maxRows;
    public int cellHeight = 18;
    public int cellSpacing = 0;
    public TableCellWidget editingCell = null;
    public int[] modelIndexList;
    public boolean showRows;
    public String title;
    public boolean showHeader;
    public ArrayList<Filter[]> filters;
    public DragListener drag;
    public DropListener drop;
    public ScreenTreeWidget screenWidget;

    public TreeWidget() {

    }

    public TreeWidget(ArrayList<TreeColumnInt> columns,
                       int maxRows,
                       String width,
                       String title,
                       boolean showHeader,
                       VerticalScroll showScroll) {
        for (TreeColumnInt column : columns) {
            column.setTreeWidget(this);
        }
        this.columns = columns;
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
        renderer = new TreeRenderer(this);
        model = new TreeModel(this);
        view = new TreeView(this, showScroll);
        view.setWidth("auto");
        view.setHeight((maxRows * cellHeight
                        + (maxRows * cellSpacing)
                        + (maxRows * 2) + cellSpacing));
        keyboardHandler = new TreeKeyboardHandler(this);
        mouseHandler = new TreeMouseHandler(this);
        addTreeWidgetListener((TreeWidgetListener)renderer);
        setWidget(view);
        addFocusListener(this);
    }
    
    /**
     * This method handles all click events on the body of the table
     */
    public void onCellClicked(SourcesTableEvents sender, int row, int col) {
        focused = true;
        if (activeRow == row && activeCell == col)
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
        if (editingCell != null) {
            treeWidgetListeners.fireStopEditing(this,
                                                     activeRow,
                                                     activeCell);
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
        if (finishEditing()) {
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    renderer.scrollLoad(view.scrollBar.getScrollPosition());
                    select(row - 1, col);
                }
            });
        }
        if (model.canSelect(modelIndexList[row])) {
            if (activeRow > -1 && !ctrlKey) {
                model.unselectRow(-1);
            }
            activeRow = row;
            model.selectRow(modelIndexList[row]);
            if (model.canEdit(modelIndexList[row], col - 1 )) {
                activeCell = col;
                treeWidgetListeners.fireStartedEditing(this, row, col);
            } else
                activeCell = -1;
        } else {
            return;
        }
    }

    public boolean finishEditing() {
        if (editingCell != null) {
            treeWidgetListeners.fireStopEditing(this,
                                                     activeRow,
                                                     activeCell);
            /*if (model.isAutoAdd() && modelIndexList[activeRow] == model.numRows()) {
                if (model.canAutoAdd(model.getAutoAddRow())) {
                    model.addRow(model.getAutoAddRow());
                    if (model.numRows() >= maxRows) {
                        view.scrollBar.scrollToBottom();
                        return true;
                    }
                }
            }
            */
            treeWidgetListeners.fireFinishedEditing(this, activeRow, activeCell);
        }
        return false;
    }

    public void startEditing(int row, int col) {
        select(row, col);
    }

    public void addChangeListener(ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new ChangeListenerCollection();
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if (changeListeners != null)
            changeListeners.remove(listener);
    }

    public void enabled(boolean enabled) {
        this.enabled = enabled;
        for (TreeColumnInt column : columns) {
            column.enable(enabled);
        }
        Iterator widIt = view.table.iterator();
        while (widIt.hasNext()) {
            ((TableCellWidget)widIt.next()).enable(enabled);
        }
    }

    public void addTreeWidgetListener(TreeWidgetListener listener) {
        if (treeWidgetListeners == null)
            treeWidgetListeners = new TreeWidgetListenerCollection();
        treeWidgetListeners.add(listener);
    }

    public void removeTreeWidgetListener(TreeWidgetListener listener) {
        if (treeWidgetListeners != null)
            treeWidgetListeners.remove(listener);
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

    public void cellUpdated(SourcesTreeModelEvents sender, int row, int cell) {

    }

    public void dataChanged(SourcesTreeModelEvents sender) {

    }

    public void rowAdded(SourcesTreeModelEvents sender, int rows) {

    }

    public void rowDeleted(SourcesTreeModelEvents sender, int row) {

    }

    public void rowSelectd(SourcesTreeModelEvents sender, int row) {

    }

    public void rowUnselected(SourcesTreeModelEvents sender, int row) {

    }

    public void rowUpdated(SourcesTreeModelEvents sender, int row) {

    }

    public void unload(SourcesTreeModelEvents sender) {
        if (editingCell != null) {
            treeWidgetListeners.fireStopEditing(this,
                                                     activeRow,
                                                     activeCell);
        }
    }

    public void scrollToSelection() {
        if (model.numRows() == model.shownRows()) {
            view.scrollBar.setScrollPosition(cellHeight * model.getData()
                                                               .getSelectedIndex());
        } else {
            int shownIndex = 0;
            for (int i = 0; i < model.getData().getSelectedIndex(); i++) {
                if (model.getRow(i).shown)
                    shownIndex++;
            }
            view.scrollBar.setScrollPosition(cellHeight * shownIndex);
        }
    }

    public boolean canPerformCommand(Enum action, Object obj) {
        // TODO Auto-generated method stub
        return true;
    }

    public void performCommand(Enum action, Object obj) {
        if(model.canToggle(modelIndexList[(Integer)obj])){
            model.toggle(modelIndexList[(Integer)obj]);
        }
        
    }

    public void rowClosed(SourcesTreeModelEvents sender, int row, TreeDataItem item) {
        // TODO Auto-generated method stub
        
    }

    public void rowOpened(SourcesTreeModelEvents sender, int row, TreeDataItem item) {
        // TODO Auto-generated method stub
        
    }
    
}
