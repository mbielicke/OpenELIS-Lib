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

import org.openelis.gwt.event.ScrollBarEvent;
import org.openelis.gwt.event.ScrollBarHandler;
import org.openelis.gwt.widget.ScrollBar;
import org.openelis.gwt.widget.redesign.table.Table.Scrolling;

import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
    protected Header          header;
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
        int scrollHeight;

        // ******** Set layout of view ***************
        outer.clear();
        scrollView = new ScrollPanel();
        
        flexTable = new FlexTable();
        flexTable.setStyleName("Table");
        flexTable.setWidth(table.getTableWidth()+"px");
        
        if (table.hasHeader()) {
            vp = new VerticalPanel();
            header = createHeader();
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
            outer.setCellVerticalAlignment(scrollBar, HasVerticalAlignment.ALIGN_MIDDLE);
        }
        
        scrollView.setWidth(table.getWidth()+"px");
        
        scrollHeight = table.hasHeader ? (table.getVisibleRows() + 1) * table.getRowHeight() : table.getVisibleRows() * table.getRowHeight(); 
        scrollView.setHeight(table.getWidth() < table.getTableWidth() ? scrollHeight + 18 +"px" : scrollHeight+"px");

        renderView();
    }

    /**
     * Method will create the Header table to be displayed by this view using
     * information found in the Column list in the Table
     */
    private Header createHeader() {
        return new Header(table);
    }

    /**
     * Will create the the necessary visible rows for the flexTable table
     * depending on what is needed at the time. If model.size() < visibleRows
     * then the number of rows created will equal model.size() else the number
     * visibleRows will be created for the flexTable table.
     */
    private void createRow() {
        
        flexTable.insertRow(flexTable.getRowCount());
        for (int c = 0; c < table.getColumnCount(); c++ ) {
            flexTable.getColumnFormatter().setWidth(c, table.getColumnAt(c).getWidth() + "px");
        }
        flexTable.getCellFormatter().setHeight(flexTable.getRowCount()-1, 0, table.getRowHeight()+"px");

    }

    protected void renderView() {
        int rc;

        computeVisibleRows();
        /*
         * Create/Load Rows in the flexTable table
         */
        rc = 0;
        for (int r = firstVisibleRow; r <= lastVisibleRow; r++ , rc++ ) {
            /*
             * Create table row if needed
             */
            if (rc >= flexTable.getRowCount())
                createRow();

            for (int c = 0; c < table.getColumnCount(); c++ ) { 
                renderCell(rc,c,r);
            }
            
        }

        /*
         * Remove extras at the end of the view if necessary
         */
        //for (int i = flexTable.getRowCount() - 1; i >= rc; i-- ) {
            //flexTable.removeRow(i);
        for(int i = 0; i < flexTable.getRowCount()-rc; i++)
            flexTable.getRowFormatter().setVisible(i, false);
        
        
        adjustScrollBar();

    }
    
    @SuppressWarnings("unchecked")
    private void renderCell(int flexTableIndex, int col, int modelIndex) {
        table.getColumnAt(col).getCellRenderer().render(table, flexTable, flexTableIndex, col,table.getValueAt(modelIndex, col));
    }

    @SuppressWarnings("unchecked")
    private void renderRow(int flexTableIndex, int modelIndex) {
       
        for (int col = 0; col < table.getColumnCount(); col++ ) 
            renderCell(flexTableIndex, col, modelIndex);
    }


    @SuppressWarnings("unchecked")
    public void startEditing(int row, int col, Object value, Event event) {
        int r;

        r = getFlexTableIndex(row);

        table.getColumnAt(col).getCellEditor().startEditing(table, flexTable, r, col,
                                                         table.getValueAt(row, col), event);
        
    }

    protected Object finishEditing(int row, int col) {
        
        return table.getColumnAt(col).getCellEditor().finishEditing();

    }

    private int getFlexTableIndex(int modelIndex) {
        computeVisibleRows();
        return modelIndex - firstVisibleRow;
    }

    public void adjustScrollBar() {
        int height;

        if(scrollBar == null)
            return;

        height = table.getRowHeight();

        scrollBar.adjust(table.getVisibleRows() * height, table.getRowCount() * height);
    }

    private void computeVisibleRows() {
        if(scrollBar != null) {
            firstVisibleRow = scrollBar.getScrollPosition() / table.getRowHeight();
            lastVisibleRow = Math.min(firstVisibleRow + table.getVisibleRows()-1, table.getRowCount() -1);
        }else{
            firstVisibleRow = 0;
            lastVisibleRow = Math.min(table.getVisibleRows() - 1,table.getRowCount());
        }
        
    
    }

    protected boolean scrollToVisible(int modelIndex) {
        int newFirst = -1;
        computeVisibleRows();

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
