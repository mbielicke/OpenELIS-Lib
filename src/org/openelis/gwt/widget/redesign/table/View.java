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

import org.openelis.gwt.common.Util;
import org.openelis.gwt.event.ScrollBarEvent;
import org.openelis.gwt.event.ScrollBarHandler;
import org.openelis.gwt.widget.HasHelper;
import org.openelis.gwt.widget.ScrollBar;
import org.openelis.gwt.widget.WidgetHelper;
import org.openelis.gwt.widget.redesign.table.Table.Scrolling;

import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.gen2.table.event.client.TableEvent.Cell;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class View extends Composite {
    /**
     * Reference to Table this View is used in
     */
    protected Table           table;
    /**
     * Table used to draw Table flexTable
     */
    protected FlexTable       flexTable;
    /**
     * Table used to draw Header flexTable for the table
     */
    protected FlexTable       header;
    /**
     * Scrollable area that contains flexTable and possibly header for
     * horizontal scroll.
     */
    protected ScrollPanel     scrollView;
    /**
     * Vertical ScrollBar
     */
    protected ScrollBar       scrollBar;
    /**
     * Panel to hold Scrollable view area and ScrollBar together.
     */
    protected HorizontalPanel outer;

    protected int             firstVisibleRow, lastVisibleRow;

    /**
     * Constructor that takes a reference to the table that will use this view
     * 
     * @param table
     */
    public View(Table tbl) {
        this.table = tbl;

        outer = new HorizontalPanel();
        initWidget(outer);

        addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(MouseWheelEvent event) {
                int pos, delta, height;

                pos = scrollBar.getScrollPosition();
                delta = event.getDeltaY();
                height = table.getRowHeight();

                if (delta < 0 && delta > -height)
                    delta = -height;
                if (delta > 0 && delta < height)
                    delta = height;
                scrollBar.setScrollPosition(pos + delta);
            }
        }, MouseWheelEvent.getType());

        layout();
    }

    protected void layout() {
        /*
         * This panel will be used only if the table contains a header. This is
         * used to glue the Header and flexTable table together because
         * ScrollPanel extends SimplePanel and can only have one widget.
         */
        VerticalPanel vp = null;

        // ******** Set layout of view ***************
        outer.clear();
        scrollView = new ScrollPanel();
        flexTable = new FlexTable();

        if (table.hasHeader()) {
            vp = new VerticalPanel();
            header = createHeader();
            flexTable = new FlexTable();
            vp.add(header);
            vp.add(flexTable);
            vp.setSpacing(0);
            scrollView.setWidget(vp);
        } else
            scrollView.setWidget(flexTable);

        outer.add(scrollView);

        if (table.getVerticalScroll() != Scrolling.NEVER) {
            scrollBar = new ScrollBar();
            scrollBar.addScrollBarHandler(new ScrollBarHandler() {
                public void onScroll(ScrollBarEvent event) {
                    renderView();
                }
            });
            adjustScrollBar();
            outer.add(scrollBar);
        }

        renderView();
    }

    /**
     * Method will create the Header table to be displayed by this view using
     * information found in the Column list in the Table
     */
    private FlexTable createHeader() {
        return new FlexTable();
    }

    /**
     * Will create the the necessary visible rows for the flexTable table
     * depending on what is needed at the time. If model.size() < visibleRows
     * then the number of rows created will equal model.size() else the number
     * visibleRows will be created for the flexTable table.
     */
    private void createRow() {
        flexTable.insertRow(flexTable.getRowCount());
        for (int c = 0; c < table.getColumnCount(); c++ )
            flexTable.getColumnFormatter().setWidth(c, table.columnAt(c).getWidth() + "px");
    }

    protected void renderView() {
        int rc;

        computeVisibleRows();
        /*
         * Create/Load Rows in the flexTable table
         */
        rc = 0;
        for (int r = firstVisibleRow; r < lastVisibleRow; r++ , rc++ ) {
            /*
             * Create table row if needed
             */
            if (rc >= flexTable.getRowCount())
                createRow();

            for (int c = 0; c < table.getColumnCount(); c++ ) {
                table.columnAt(c)
                     .getCellRenderer()
                     .render(flexTable, rc, c, table.getValueAt(r, c));
            }

        }

        /*
         * Remove extras at the end of the view if necessary
         */
        for (int i = flexTable.getRowCount() - 1; i > rc; i-- ) {
            flexTable.removeRow(i);
        }

    }

    private void renderViewByDeleteAdd(int startIndex, int endIndex) {
        int rowsToScroll, newFirst = 0, modelStart, flexTableStart;
        computeVisibleRows();
        /*
         * Delete and add Rows from either Top or Bottom depending on direction
         * of scroll. Also calc and set indexes needed to render the new rows.
         */
        if (startIndex > firstVisibleRow) {
            rowsToScroll = startIndex - firstVisibleRow;
            newFirst = firstVisibleRow + rowsToScroll;
            for (int i = 0; i < rowsToScroll; i++ ) {
                flexTable.removeRow(0);
                flexTable.insertRow(flexTable.getRowCount());
            }
            modelStart = newFirst + table.getVisibleRows() - rowsToScroll;
            flexTableStart = table.getVisibleRows() - rowsToScroll;
        } else {
            rowsToScroll = firstVisibleRow - startIndex;
            newFirst = firstVisibleRow - rowsToScroll;
            for (int i = 0; i < rowsToScroll; i++ ) {
                flexTable.removeRow(flexTable.getRowCount() - 1);
                flexTable.insertRow(0);
            }
            modelStart = newFirst;
            flexTableStart = 0;
        }

        /*
         * Render the new rows with data from the model.
         */
        for (int i = 0; i < rowsToScroll; i++ ) {
            renderRow(flexTableStart + i, modelStart + i);
        }

    }

    private void renderViewBySetAll(int startIndex, int endIndex) {
        for (int i = 0; i < table.getVisibleRows(); i++ ) {

            if (i > endIndex) {
                while (flexTable.getRowCount() > i)
                    flexTable.removeRow(flexTable.getRowCount() - 1);
                break;
            }

            if (i == flexTable.getRowCount())
                createRow();

            renderRow(i, startIndex + i);
        }
    }

    protected void renderRow(int row) {
        int flexTableIndex;

        flexTableIndex = getFlexTableIndex(row);
        /*
         * If table has not reached srollable length yet make sure that a row is
         * present for the index passed such as when a row is added.
         */
        if (flexTableIndex == flexTable.getRowCount())
            flexTable.insertRow(row);

        renderRow(flexTableIndex, row);
    }

    @SuppressWarnings("unchecked")
    private void renderRow(int flexTableIndex, int modelIndex) {
        for (int col = 0; col < table.getColumnCount(); col++ ) {
            renderCell(flexTableIndex, col, modelIndex);
        }
    }

    protected void renderCell(int row, int col) {
        renderCell(getFlexTableIndex(row), col, row);
    }

    protected void refreshView(int startIndex, int endIndex) {
        int flexTableIndex;
        /*
         * Adjust drawn rows if model size drops below the current rowCount
         */
        if (table.getRowCount() < flexTable.getRowCount())
            flexTable.removeRow(flexTable.getRowCount() - 1);

        /*
         * adjust endIndex in case where last row is deleted
         */
        if (endIndex > table.getRowCount() - 1)
            endIndex = table.getRowCount() - 1;

        /*
         * Loop through refreshing the drawn rows
         */
        for (int i = startIndex; i <= endIndex; i++ ) {
            flexTableIndex = getFlexTableIndex(i);
            if (flexTableIndex > -1 && flexTableIndex < flexTable.getRowCount())
                renderRow(getFlexTableIndex(i), i);
            else
                break;
        }

    }

    @SuppressWarnings("unchecked")
    private void renderCell(int flexTableIndex, int col, int modelIndex) {

        table.columnAt(col).getCellRenderer().render(flexTable, flexTableIndex, col,
                                                     table.getValueAt(modelIndex, col));

    }

    @SuppressWarnings("unchecked")
    public void startEditing(int row, int col, Object value, Event event) {
        int r;

        r = getFlexTableIndex(row);

        table.columnAt(col).getCellEditor().startEditing(flexTable, r, col,
                                                         table.getValueAt(row, col), event);
    }

    protected Object finishEditing(int row, int col) {
        return table.columnAt(col).getCellEditor().finishEditing();
    }

    private int getFlexTableIndex(int modelIndex) {
        computeVisibleRows();
        return modelIndex - firstVisibleRow;
    }

    public void adjustScrollBar() {
        int height;

        height = table.getRowHeight();

        scrollBar.adjust(table.getVisibleRows() * height, table.getRowCount() * height);
    }

    private void computeVisibleRows() {
        firstVisibleRow = scrollBar.getScrollPosition() / table.getRowHeight();
        lastVisibleRow = Math.min(firstVisibleRow + table.getVisibleRows(), table.getRowCount());
    }

    protected boolean scrollToVisible(int modelIndex) {
        int newFirst = -1;
        computeVisibleRows();
        /*
         * We do this here if table has not hit enough rows yet to scroll to
         * make sure the latest data in the model is displayed such as when rows
         * are added.
         */
        if (table.getRowCount() < table.getVisibleRows()) {
            renderRow(modelIndex);
            return true;
        }

        if (modelIndex < firstVisibleRow)
            newFirst = modelIndex;
        else if (modelIndex > firstVisibleRow + table.getVisibleRows())
            newFirst = modelIndex - table.getVisibleRows();

        if (newFirst < 0)
            return false;
        
        scrollBar.setScrollPosition(newFirst * table.getRowHeight());
        
        return true;
    }
}
