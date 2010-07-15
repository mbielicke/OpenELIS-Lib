/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget.redesign.table;

import java.util.ArrayList;

import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.widget.Queryable;
import org.openelis.gwt.widget.ScreenWidgetInt;
import org.openelis.gwt.widget.redesign.table.event.BeforeRowAddedEvent;
import org.openelis.gwt.widget.redesign.table.event.BeforeRowAddedHandler;
import org.openelis.gwt.widget.redesign.table.event.BeforeRowDeletedEvent;
import org.openelis.gwt.widget.redesign.table.event.BeforeRowDeletedHandler;
import org.openelis.gwt.widget.redesign.table.event.CellEditedEvent;
import org.openelis.gwt.widget.redesign.table.event.CellEditedHandler;
import org.openelis.gwt.widget.redesign.table.event.HasBeforeRowAddedHandlers;
import org.openelis.gwt.widget.redesign.table.event.HasBeforeRowDeletedHandlers;
import org.openelis.gwt.widget.redesign.table.event.HasCellEditedHandlers;
import org.openelis.gwt.widget.redesign.table.event.HasRowAddedHandlers;
import org.openelis.gwt.widget.redesign.table.event.HasRowDeletedHandlers;
import org.openelis.gwt.widget.redesign.table.event.RowAddedEvent;
import org.openelis.gwt.widget.redesign.table.event.RowAddedHandler;
import org.openelis.gwt.widget.redesign.table.event.RowDeletedEvent;
import org.openelis.gwt.widget.redesign.table.event.RowDeletedHandler;
import org.openelis.gwt.widget.table.event.BeforeCellEditedEvent;
import org.openelis.gwt.widget.table.event.BeforeCellEditedHandler;
import org.openelis.gwt.widget.table.event.HasBeforeCellEditedHandlers;
import org.openelis.gwt.widget.table.event.HasUnselectionHandlers;
import org.openelis.gwt.widget.table.event.UnselectionEvent;
import org.openelis.gwt.widget.table.event.UnselectionHandler;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is used by screens and widgets such as AutoComplete and Dropdown to display 
 * information in a Table grid format
 * @author tschmidt
 *
 */
public class Table extends FocusPanel implements ScreenWidgetInt, Queryable,
                                     HasBeforeSelectionHandlers<Integer>,
                                     HasSelectionHandlers<Integer>,
                                     HasUnselectionHandlers<Integer>, HasBeforeCellEditedHandlers,
                                     HasCellEditedHandlers, HasBeforeRowAddedHandlers,
                                     HasRowAddedHandlers, HasBeforeRowDeletedHandlers,
                                     HasRowDeletedHandlers {

    /**
     * Cell that is currently being edited.
     */
    protected int                editingRow, editingCol;

    /**
     * Table dimensions
     */
    protected int                rowHeight, visibleRows, viewWidth = -1, totalColumnWidth;
    
    /**
     * Model used by the Table
     */
    protected ArrayList<Row>     rows;
    
    /**
     * Columns used by the Table
     */
    protected ArrayList<Column>  columns;
    
    /**
     * List of selected Rows by index in the table
     */
    protected ArrayList<Integer> selections;

    /**
     * Table state values
     */
    protected boolean            enabled, multiSelect, editing, hasFocus, queryMode, hasHeader;

    /**
     * Enum representing the state of when the scroll bar should be shown.
     */
    public enum Scrolling {
        ALWAYS, AS_NEEDED, NEVER
    };

    /**
     * Fields to hold state of whether the scroll bars are shown
     */
    protected Scrolling verticalScroll, horizontalScroll;

    /**
     * Reference to the View composite for this widget.
     */
    protected View      view;

    /**
     * Primary CSS style to be applied to this table.
     */
    protected String    TABLE_STYLE = "Table";

    /**
     * Arrays for determining relative X positions for columns
     */
    protected short[]   xForColumn, columnForX;

    /**
     * Default no-arg constructor that initializes all needed fields so the
     * layout of the table can succeed.
     */
    public Table() {
        editingRow = -1;
        editingCol = -1;
        rowHeight = 20;
        visibleRows = 0;
        enabled = false;
        multiSelect = false;
        editing = false;
        hasFocus = false;
        queryMode = false;
        verticalScroll = Scrolling.ALWAYS;
        horizontalScroll = Scrolling.ALWAYS;
        selections = new ArrayList<Integer>(5);
        columns = new ArrayList<Column>(5);
        rows = null;
        view = new View(this);
        setWidget(view);
        layout();

        /*
         * This Handler takes care of all key events on the table when editing
         * and when only selection is on
         */
        addDomHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                int row, col;

                if ( !isEnabled())
                    return;

                row = editingRow;
                col = editingCol;

                switch (event.getNativeEvent().getKeyCode()) {
                    case (KeyCodes.KEY_TAB):

                        if (col < 0)
                            break;

                        if ( !event.isShiftKeyDown()) {
                            while (true) {
                                col++ ;
                                if (col >= getColumnCount()) {
                                    col = 0;
                                    row++ ;
                                    if (row >= getRowCount())
                                        break;

                                }
                                if (startEditing(row, col))
                                    break;
                            }
                        } else {
                            while (true) {
                                col-- ;
                                if (col < 0) {
                                    col = getColumnCount() - 1;
                                    row-- ;
                                    if (row < 0)
                                        break;
                                }
                                if (startEditing(row, col))
                                    break;
                            }
                        }

                        break;
                    case (KeyCodes.KEY_DOWN):

                        if ( !isEditing()) {
                            if (isAnyRowSelected()) {
                                row = getSelectedRow();
                                while (true) {
                                    row++ ;
                                    if (row >= getRowCount())
                                        break;
                                    if (selectRowAt(row))
                                        break;
                                }
                            }
                            break;
                        }
                        while (true) {
                            row++ ;
                            if (row >= getRowCount())
                                break;
                            if (startEditing(row, col))
                                break;
                        }
                        break;
                    case (KeyCodes.KEY_UP):
                        if ( !isEditing()) {
                            if (isAnyRowSelected()) {
                                row = getSelectedRow();
                                while (true) {
                                    row-- ;
                                    if (row < 0)
                                        break;
                                    if (selectRowAt(row))
                                        break;
                                }
                            }
                            break;
                        }
                        while (true) {
                            row-- ;
                            if (row < 0)
                                break;
                            if (startEditing(row, col))
                                break;
                        }
                        break;
                    case (KeyCodes.KEY_ENTER):

                        if (isEditing()) {
                            finishEditing();
                            return;
                        }

                        if (isAnyRowSelected()) {
                            row = getSelectedRow();
                            startEditing(row, 0);
                        }

                        break;
                }

            }
        }, KeyDownEvent.getType());

    }

    // ********* Table Definition Methods *************
    /**
     * Returns the currently used Row Height for the table layout
     */
    public int getRowHeight() {
        return rowHeight;
    }

    /**
     * Sets the Row Height to be used in the table layout.
     * 
     * @param rowHeight
     */
    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
        layout();
    }

    /**
     * Returns how many physical rows are used in the table layout.
     * 
     * @return
     */
    public int getVisibleRows() {
        return visibleRows;
    }

    /**
     * Sets how many physical rows are used in the table layout.
     * 
     * @param visibleRows
     */
    public void setVisibleRows(int visibleRows) {
        this.visibleRows = visibleRows;
        layout();
    }

    /**
     * Returns the data model currently being displayed by this table. The
     * return value is parameterized so specific models can be used that extend
     * the basic Row such as Item in AutoCompete and Dropdown
     * 
     * @return
     */
    public ArrayList<? extends Row> getModel() {
        return rows;
    }

    /**
     * Sets the data model to be displayed by this table. The model parameter is
     * parameterized so specific models can be used that extend the basic Row
     * such as Item in AutoCompete and Dropdown
     * 
     * @param model
     */
    @SuppressWarnings("unchecked")
    public void setModel(ArrayList<? extends Row> model) {
        this.rows = (ArrayList<Row>)model;
        renderView( -1, -1);
    }

    /**
     * Returns the current size of the held model. Returns zero if a model has
     * not been set.
     * 
     * @return
     */
    public int getRowCount() {
        if (rows != null)
            return rows.size();
        return 0;
    }

    /**
     * Used to determine the table has more than one row currently selected.
     * 
     * @return
     */
    public boolean isMultipleRowsSelected() {
        return selections.size() > 1;
    }

    /**
     * Used to determine if the table currently allows multiple selection.
     * 
     * @return
     */
    public boolean isMultipleSelectionAllowed() {
        return multiSelect;
    }

    /**
     * Used to put the table into Multiple Selection mode.
     * 
     * @param multiSelect
     */
    public void setAllowMultipleSelection(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    /**
     * Returns the current Vertical Scrollbar view rule set.
     * 
     * @return
     */
    public Scrolling getVerticalScroll() {
        return verticalScroll;
    }

    /**
     * Sets the current Vertical Scrollbar view rule.
     * 
     * @param verticalScroll
     */
    public void setVerticalScroll(Scrolling verticalScroll) {
        this.verticalScroll = verticalScroll;
        layout();
    }

    /**
     * Returns the current Horizontal Scrollbar view rule set
     * 
     * @return
     */
    public Scrolling getHorizontalScroll() {
        return horizontalScroll;
    }

    /**
     * Sets the current Horizontal Scrollbar view rule.
     * 
     * @param horizontalScroll
     */
    public void setHorizontalScroll(Scrolling horizontalScroll) {
        this.horizontalScroll = horizontalScroll;
        layout();
    }

    /**
     * Sets the width of the table view
     * 
     * @param width
     */
    public void setWidth(int width) {
        this.viewWidth = width;
        layout();

    }

    /**
     * Method overridden from Composite to call setWidth(int) so that the width
     * can be adjusted.
     */
    @Override
    public void setWidth(String width) {
        setWidth(Util.stripUnits(width));
    }

    /**
     * Returns the currently set view width for the Table
     * 
     * @return
     */
    public int getWidth() {
        return viewWidth;
    }
    
    /**
     * Returns the view width of the table minus the the width of the 
     * scrollbar if the scrollbar is visible or if space has been 
     * reserved for it
     * 
     * @return
     */
    protected int getWidthWithoutScrollbar() {
        if (verticalScroll != Scrolling.NEVER)
            return viewWidth - 18;

        return viewWidth;
    }
    
    /**
     * Returns the width of the all the column widths added together
     * which is the physical width of the table
     * 
     * @return
     */
    protected int getTotalColumnWidth() {
        return totalColumnWidth;
    }

    /**
     * Sets the Primary CSS style to be used by this table
     * 
     * @param style
     */
    public void setTableStyle(String style) {
        TABLE_STYLE = style;
    }

    /**
     * Returns the number of columns used in this Table
     * 
     * @return
     */
    public int getColumnCount() {
        if (columns != null)
            return columns.size();
        return 0;
    }

    /**
     * Returns the column at the passed index
     * 
     * @param index
     * @return
     */
    public Column getColumnAt(int index) {
        return columns.get(index);
    }

    /**
     * Returns column by the name passed
     * 
     * @param index
     * @return
     */
    public int getColumnByName(String name) {
        for (int i = 0; i < columns.size(); i++ ) {
            if (columns.get(i).name.equals(name))
                return i;
        }
        return -1;
    }

    /**
     * Returns the X coordinate on the Screen of the Column passed.
     * 
     * @param index
     * @return
     */
    protected int getXForColumn(int index) {
        if (xForColumn != null && index >= 0 && index < xForColumn.length)
            return xForColumn[index];
        return -1;
    }

    /**
     * Returns the Column for the current mouse x position passed in the header
     * 
     * @param x
     * @return
     */
    protected int getColumnForX(int x) {
        if (columnForX != null && x >= 0 && x < columnForX.length)
            return columnForX[x];
        return -1;
    }



    /**
     * Sets whether the table as a header or not.
     */
    public void setHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    /**
     * Used to determine if table has header
     * 
     * @return
     */
    public boolean hasHeader() {
        return hasHeader;
    }

    /**
     * Sets the list columns to be used by this Table
     * 
     * @param columns
     */
    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;

        for (Column column : columns)
            column.setTable(this);

        layout();
    }

    /**
     * Creates and Adds a Column at the end of the column list with passed name
     * and header label in the params.
     * 
     * @param name
     *        Name of the column for reference
     * @param label
     *        Label used in Table header.
     * @return The newly created and added column
     */
    public Column addColumn(String name, String label) {
        return addColumnAt(columns.size(), name, label);
    }

    /**
     * Creates and adds a new column to the table.
     * 
     * @return
     */
    public Column addColumn() {
        return addColumn("", "");
    }

    /**
     * Creates and inserts a new Column int the table at the specified index
     * using the name and label passed.
     * 
     * @param index
     *        Index in the Column list where to insert the new Column
     * @param name
     *        Name used in the Column as a reference to the Column.
     * @param label
     *        Label used in the Table header.
     * @return The newly created and added Column.
     */
    public Column addColumnAt(int index, String name, String label) {
        Column column;

        column = new Column(this, name, label);
        columns.add(index, column);
        layout();

        return column;
    }

    /**
     * Creates and adds a new Column at passed index
     * 
     * @param index
     *        Index in the Column list where to insert the new Column.
     * @return The newly created and added column.
     */
    public Column addColumnAt(int index) {
        return addColumnAt(index, "", "");
    }

    /**
     * Removes the column in the table and passed index.
     * 
     * @param index
     */
    public Column removeColumnAt(int index) {
        Column col;

        col = columns.remove(index);
        layout();

        return col;
    }

    /**
     * Removes all columns from the table.
     */
    public void removeAllColumns() {
        columns.clear();
        layout();
    }

    /**
     * Creates a new blank Row and adds it to the bottom of the Table model.
     * 
     * @return
     */
    public Row addRow() {
        return addRow(rows.size(), null);
    }

    /**
     * Creates a new blank Row and inserts it in the table model at the passed
     * index.
     * 
     * @param index
     * @return
     */
    public Row addRowAt(int index) {
        return addRow(index, null);
    }

    /**
     * Adds the passed Row to the end of the Table model.
     * 
     * @param row
     * @return
     */
    public Row addRow(Row row) {
        return addRow(rows.size(), row);
    }

    /**
     * Adds the passed Row into the Table model at the passed index.
     * 
     * @param index
     * @param row
     * @return
     */
    public Row addRowAt(int index, Row row) {
        return addRow(index, row);
    }

    /**
     * Private method called by all public addRow methods to handle event firing
     * and add the new row to the model.
     * 
     * @param index
     *        Index where the new row is to be added.
     * @param row
     *        Will be null if a Table should create a new blank Row to add
     *        otherwise the passed Row will be added.
     * @return Will return null if this action is canceled by a
     *         BeforeRowAddedHandler, otherwise the newly created Row will be
     *         returned or if a Row is passed to the method it will echoed back.
     */
    private Row addRow(int index, Row row) {
        finishEditing();

        if (row == null)
            row = new Row(columns.size());

        if ( !fireBeforeRowAddedEvent(index, row))
            return null;

        rows.add(index, row);
        view.renderView(index, -1);
        view.adjustScrollBarHeight();

        fireRowAddedEvent(index, row);

        return row;

    }

    /**
     * Method will delete a row from the model at the specified index and
     * refersh the view.
     * 
     * @param index
     * @return
     */
    public Row removeRowAt(int index) {
        Row row;

        finishEditing();

        row = rows.get(index);

        if ( !fireBeforeRowDeletedEvent(index, row))
            return null;

        rows.remove(index);

        renderView(-1,-1);

        fireRowDeletedEvent(index, row);

        return row;
    }

    /**
     * Set the model for this table to null and redraws
     */
    public void removeAllRows() {
        finishEditing();
        clearRowSelection();
        rows = null;
        renderView( -1, -1);
    }

    // ************ Selection Methods ***************

    /**
     * Returns an array of indexes of the currently selected row
     */
    public Integer[] getSelectedRows() {
        return selections.toArray(new Integer[] {});
    }

    /**
     * Selects the row at the passed index. Selection can be canceled by a
     * BeforeSelecionHandler. If selection is allowed, then a SelectionEvent
     * will be fired to all registered handlers, and the selected row will be
     * scrolled in the visible view.
     * 
     * @param index
     */
    public boolean selectRowAt(int index) {

        if ( !multiSelect)
            clearRowSelection();

        if (index > -1 && index < rows.size()) {
            if ( !selections.contains(index)) {
                if ( !fireBeforeSelectionEvent(index))
                    return false;

                finishEditing();
                selections.add(index);
                scrollToVisible(index);
                view.applySelectionStyle(index);

                fireSelectionEvent(index);

            }
        }
        return true;
    }

    /**
     * Unselects the row from the selection list. This method does nothing if
     * the passed index is not currently a selected row, otherwise the row will
     * be unselected and an UnselectEvent will be fired to all registered
     * handlers
     * 
     * @param index
     */
    public void unselectRowAt(int index) {
        Integer i;
        i = new Integer(index);
        if (selections.contains(i)) {
            finishEditing();
            if ( !fireUnselectEvent(index))
                return;
            selections.remove(i);
            view.applyUnselectionStyle(index);
        }
    }

    /**
     * Returns the selected index of the first row selected
     * 
     * @return
     */
    public int getSelectedRow() {
        return selections.size() > 0 ? selections.get(0) : -1;
    }

    /**
     * Used to determine if the passed row index is currently in the selection
     * list.
     * 
     * @param index
     * @return
     */
    public boolean isRowSelected(int index) {
        return selections.contains(index);
    }

    /**
     * Used to determine if any row in the table is selected
     * 
     * @return
     */
    public boolean isAnyRowSelected() {
        return selections.size() > 0;
    }

    /**
     * Clears all selections from the table.
     */
    public void clearRowSelection() {
        int index;

        if ( !isAnyRowSelected())
            return;

        finishEditing();

        index = selections.get(0);
        if (isMultipleRowsSelected())
            index = -1;

        if ( !fireUnselectEvent(index))
            return;

        for (int i = 0; i < selections.size(); i++ )
            view.applyUnselectionStyle(selections.get(i));

        selections.clear();
    }

    // ********* Event Firing Methods ********************

    /**
     * Private method that will fire a BeforeSelectionEvent for the passed
     * index. Returns false if the selection is canceled by registered handler
     * and true if the selection is allowed.
     */
    private boolean fireBeforeSelectionEvent(int index) {
        BeforeSelectionEvent<Integer> event = null;

        if ( !queryMode)
            event = BeforeSelectionEvent.fire(this, index);

        return event == null || !event.isCanceled();
    }

    /**
     * Private method that will fire a SelectionEvent for the passed index to
     * notify all registered handlers that row at the passed index was selected.
     * Returns true as a default.
     * 
     * @param index
     * @return
     */
    private boolean fireSelectionEvent(int index) {

        if ( !queryMode)
            SelectionEvent.fire(this, index);

        return true;
    }

    /**
     * Private method that will fire an UnselectionEvent for the passed index.
     * Returns false if the unselection was canceled by a registered handler and
     * true if the unselection is allowed.
     * 
     * @param index
     * @return
     */
    public boolean fireUnselectEvent(int index) {
        UnselectionEvent<Integer> event = null;

        if ( !queryMode)
            event = UnselectionEvent.fire(this, index, -1);

        return event == null || !event.isCanceled();
    }

    /**
     * Private method that will fire a BeforeCellEditedEvent for a cell in the
     * table. Returns false if the cell editing is canceled by a registered
     * handler and true if the user is allowed to edit the cell.
     * 
     * @param row
     * @param col
     * @param val
     * @return
     */
    private boolean fireBeforeCellEditedEvent(int row, int col, Object val) {
        BeforeCellEditedEvent event = null;

        if ( !queryMode)
            event = BeforeCellEditedEvent.fire(this, row, col, val);

        return event == null || !event.isCancelled();
    }

    /**
     * Private method that will fire a CellEditedEvent after the value of a cell
     * is changed by a user input. Returns true as default.
     * 
     * @param index
     * @return
     */
    private boolean fireCellEditedEvent(int row, int col) {

        if ( !queryMode)
            CellEditedEvent.fire(this, row, col);

        return true;
    }

    /**
     * Private method that fires a BeforeRowAddedEvent for the passed index and
     * Row. Returns false if the addition is canceled by a registered handler
     * and true if the addition is allowed.
     * 
     * @param index
     * @param row
     * @return
     */
    private boolean fireBeforeRowAddedEvent(int index, Row row) {
        BeforeRowAddedEvent event = null;

        if ( !queryMode)
            BeforeRowAddedEvent.fire(this, index, row);

        return event == null || !event.isCancelled();
    }

    /**
     * Private method that fires a RowAddedEvent for the passed index and Row to
     * all registered handlers. Returns true as a default.
     * 
     * @param index
     * @param row
     * @return
     */
    private boolean fireRowAddedEvent(int index, Row row) {

        if ( !queryMode)
            RowAddedEvent.fire(this, index, row);

        return true;

    }

    /**
     * Private method that fires a BeforeRowDeletedEvent for the passed index
     * and Row. Returns false if the deletion is canceled by a registered
     * handler and true if the deletion is allowed.
     * 
     * @param index
     * @param row
     * @return
     */
    private boolean fireBeforeRowDeletedEvent(int index, Row row) {
        BeforeRowAddedEvent event = null;

        if ( !queryMode)
            BeforeRowDeletedEvent.fire(this, index, row);

        return event == null || event.isCancelled();
    }

    /**
     * Private method that fires a RowDeletedEvent for the passed index and Row
     * to all registered handlers. Returns true as a default.
     * 
     * @param index
     * @param row
     * @return
     */
    private boolean fireRowDeletedEvent(int index, Row row) {

        if ( !queryMode)
            RowDeletedEvent.fire(this, index, row);

        return true;

    }

    // ********* Edit Table Methods *******************
    /**
     * Used to determine if a cell is currently being edited in the Table
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * Sets the value of a cell in Table model.
     * 
     * @param row
     * @param col
     * @param value
     */
    public void setValueAt(int row, int col, Object value) {
        finishEditing();
        rows.get(row).setCell(col, value);
        refreshCell(row, col);
    }

    /**
     * Sets a row in the model at the passed index and refreshes the view.
     * 
     * @param index
     * @param row
     */
    public void setRowAt(int index, Row row) {
        finishEditing();
        rows.set(index, row);
        renderView(index,index);
    }

    /**
     * Returns the value of a cell in the model.
     * 
     * @param row
     * @param col
     * @return
     */
    public Object getValueAt(int row, int col) {
        if (rows == null)
            return null;
        return rows.get(row).getCell(col);
    }

    /**
     * Method to put a cell into edit mode.  If a cell can not be edited than false will be returned
     * @param row
     * @param col
     * @return
     */
    public boolean startEditing(int row, int col) {
        return startEditing(row, col, null);
    }

    /**
     * Method that sets focus to a cell in the Table and readies it for user
     * input.
     * 
     * @param row
     * @param col
     * @return
     */
    private boolean startEditing(final int row, final int col, final Event event) {
        boolean willScroll;

        if ( !isEnabled())
            return false;

        finishEditing();

        willScroll = view.isRowVisible(row);

        if ( !isRowSelected(row))
            selectRowAt(row);

        if ( !isRowSelected(row))
            return false;

        if ( !fireBeforeCellEditedEvent(row, col, getValueAt(row, col)))
            return false;

        editingRow = row;
        editingCol = col;
        editing = true;

        view.scrollToVisible(row);
        if ( !willScroll)
            view.startEditing(row, col, getValueAt(row, col), event);
        else {
            /*
             * Call start editing in Deferred Command to make sure the table is scrolled
             *  before setting the cell into edit mode
             */
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    view.startEditing(row, col, getValueAt(row, col), event);
                }
            });
        }
        return true;
    }

    /**
     * Method called to complete editing of any cell in the table. Method does
     * nothing a cell is not currently being edited.
     */
    public void finishEditing() {
        Object newValue, oldValue;
        int row, col;

        if ( !editing)
            return;

        editing = false;
        row = editingRow;
        col = editingCol;

        editingRow = -1;
        editingCol = -1;

        newValue = view.finishEditing(row, col);
        oldValue = getValueAt(row, col);
        rows.get(row).setCell(col, newValue);
        refreshCell(row, col);

        if (Util.isDifferent(newValue, oldValue)) {
            fireCellEditedEvent(row, col);
        }
        /*
         * Call setFocus(true) so that the KeyHandler will recieve
         * events when no cell is being edited
         */
        setFocus(true);
    }

    /**
     * Returns the current row where cell is being edited
     * @return
     */
    public int getEditingRow() {
        return editingRow;
    }

    /**
     * Returns the current column where cell is being edited
     * @return
     */
    public int getEditingCol() {
        return editingCol;
    }

    // ********* Draw Scroll Methods ****************
    /**
     * Scrolls the table in the required direction to make sure the passed index
     * is visible. Or if the index passed is in the view range refresh the row
     * to make sure that the latest data is shown (i.e. row Added before scroll
     * size is hit).
     */
    public boolean scrollToVisible(int index) {
        return view.scrollToVisible(index);
    }

    /**
     * Redraws the table when any part of its physical definition is changed.
     */
    protected void layout() {
        computeColumnsWidth();
        view.layout();
    }

    /**
     * Method called when a column width has been set to resize the table columns
     */
    protected void resize() {
        if ( !isAttached())
            return;

        computeColumnsWidth();

        if (hasHeader)
            view.getHeader().resize();

        view.resize();
    }

    /**
     * Method will have the view re-compute its visible rows and refresh the view
     * @param startR
     * @param endR
     */
    protected void renderView(int startR, int endR) {
        view.setVisibleChanged(true);
        view.renderView(startR, endR);
    }

    /**
     * Method computes the XForColumn and ColumForX arrays and set the totoalColumnWidth
     */
    private void computeColumnsWidth() {
        int from, to;

        //
        // compute total width
        //
        totalColumnWidth = 0;
        xForColumn = new short[getColumnCount()];
        for (int i = 0; i < getColumnCount(); i++ ) {
            xForColumn[i] = (short)totalColumnWidth;
            totalColumnWidth += getColumnAt(i).getWidth();
        }
        //
        // mark the array
        //
        from = 0;
        columnForX = new short[totalColumnWidth];
        for (int i = 0; i < getColumnCount(); i++ ) {
            to = from + getColumnAt(i).getWidth();
            while (from < to)
                columnForX[from++ ] = (short)i;
        }
    }

    /**
     * redraws data in the cell passed
     * 
     * @param row
     * @param col
     */
    protected void refreshCell(int row, int col) {
        view.renderCell(row, col);
    }

    // ************* Implementation of ScreenWidgetInt *************

    /**
     * Sets whether this table allows selection
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

    }

    /**
     * Used to determine if the table is enabled for selection.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets a TabHandler to this widget to be used in the containing Screen tab
     * order.
     */
    public void addTabHandler(TabHandler handler) {
        addDomHandler(handler, KeyDownEvent.getType());
    }

    /**
     * Sets the Focus style to the Table
     */
    public void addFocusStyle(String style) {
        addStyleName(style);
    }

    /**
     * Removes the Focus style from the Table
     */
    public void removeFocusStyle(String style) {
        removeStyleName(style);
    }

    // ********** Implementation of Queryable *******************
    /**
     * Returns a list of QueryData objects for all Columns in the table that
     * have values and will participate in the query.
     */
    public Object getQuery() {
        ArrayList<Object> qds;
        QueryData qd;
        Widget editor;

        if ( !queryMode)
            return null;

        qds = new ArrayList<Object>();

        for (int i = 0; i < getColumnCount(); i++ ) {
            editor = getColumnAt(i).getCellEditor().getWidget();
            if (editor instanceof Queryable) {
                qd = (QueryData) ((Queryable)editor).getQuery();
                if (qd != null) {
                    qd.key = getColumnAt(i).name;
                    qds.add(qd);
                }
            }
        }
        return qds.toArray();
    }

    /**
     * Puts the table into and out of query mode.
     */
    public void setQueryMode(boolean query) {
        ArrayList<Row> model;
        Row row;

        if (query == queryMode)
            return;

        this.queryMode = query;
        if (query) {
            model = new ArrayList<Row>();
            row = new Row(getColumnCount());
            model.add(row);
            setModel(model);
        } else
            setModel(null);
    }

    /**
     * Method to determine if Table is in QueryMode
     * @return
     */
    public boolean getQueryMode() {
        return queryMode;
    }

    // ********* Registration of Handlers ******************
    /**
     * Registers a BeforeSelectionHandler to this Table
     */
    public HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<Integer> handler) {
        return addHandler(handler, BeforeSelectionEvent.getType());
    }

    /**
     * Registers a SelectionHandler to this Table
     */
    public HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    /**
     * Registers an UnselectionHandler to this Table
     */
    public HandlerRegistration addUnselectionHandler(UnselectionHandler<Integer> handler) {
        return addHandler(handler, UnselectionEvent.getType());
    }

    /**
     * Registers a BeforeCellEditedHandler to this Table
     */
    public HandlerRegistration addBeforeCellEditedHandler(BeforeCellEditedHandler handler) {
        return addHandler(handler, BeforeCellEditedEvent.getType());
    }

    /**
     * Registers a CellEditedHandler to this Table
     */
    public HandlerRegistration addCellEditedHandler(CellEditedHandler handler) {
        return addHandler(handler, CellEditedEvent.getType());
    }

    /**
     * Registers a BeforeRowAddedHandler to this Table
     */
    public HandlerRegistration addBeforeRowAddedHandler(BeforeRowAddedHandler handler) {
        return addHandler(handler, BeforeRowAddedEvent.getType());
    }

    /**
     * Registers a RowAddedHandler to this Table
     */
    public HandlerRegistration addRowAddedHandler(RowAddedHandler handler) {
        return addHandler(handler, RowAddedEvent.getType());
    }

    /**
     * Registers a BeforeRowDeletedHandler to this Table
     */
    public HandlerRegistration addBeforeRowDeletedHandler(BeforeRowDeletedHandler handler) {
        return addHandler(handler, BeforeRowDeletedEvent.getType());
    }

    /**
     * Registers a RowDeletedHandler to this Table
     */
    public HandlerRegistration addRowDeletedHandler(RowDeletedHandler handler) {
        return addHandler(handler, RowDeletedEvent.getType());
    }

}
