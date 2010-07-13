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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

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

    /**
     * Computed first and last model indexes displayed in the table
     */
    protected int             firstVisibleRow, lastVisibleRow,prevFirstVisibleRow,prevLastVisibleRow;

    /**
     * Flag used to determine if the table has been attached to know when to do
     * layout for the first time.
     */
    protected boolean         attached,firstAttach;

    /**
     * Computed Row Height used to calculate ScrollHeight and ScrollPosition
     * since all browsers don't seem to draw rows to the same height
     */
    protected int             rowHeight;

    /**
     * Constructor that takes a reference to the table that will use this view
     * 
     * @param table
     */
    public View(Table tbl) {
        this.table = tbl;
        /*
         * Create and set outer once here as the composite widget instead of in
         * layout() because it can only be called once.
         */
        outer = new HorizontalPanel();
        initWidget(outer);

        /*
         * Set MouseWheelHandler to the view and then adjus the scrollBar
         * accordingly
         */
        addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(MouseWheelEvent event) {
                int pos, delta;

                pos = scrollBar.getScrollPosition();
                delta = event.getDeltaY();

                if (delta < 0 && delta > -rowHeight)
                    delta = -rowHeight;
                if (delta > 0 && delta < rowHeight)
                    delta = rowHeight;
                scrollBar.setScrollPosition(pos + delta);
            }
        }, MouseWheelEvent.getType());
    }

    protected void layout() {
        /*
         * This panel will be used only if the table contains a header. This is
         * used to glue the Header and flexTable table together because
         * ScrollPanel extends SimplePanel and can only have one widget.
         */
        VerticalPanel vp = null;

        /*
         * If View is not attached to DOM yet get out. onAttach will call
         * layout() the first time this widget attached.
         */

        if ( !attached)
            return;

        // ******** Set layout of view ***************
        outer.clear();
        scrollView = new ScrollPanel();

        // ********* Create the Cell body *********
        flexTable = new FlexTable();
        flexTable.setStyleName(table.TABLE_STYLE);
        flexTable.setWidth(table.getTableWidth() + "px");

       
        flexTable.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Cell cell = flexTable.getCellForEvent(event);
                table.startEditing(firstVisibleRow + cell.getRowIndex(), cell.getCellIndex());
            }
        });

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

        
        //**** Vertical ScrollBar **************
        if (table.getVerticalScroll() != Scrolling.NEVER) {
            scrollBar = new ScrollBar();
            scrollBar.addScrollBarHandler(new ScrollBarHandler() {
                public void onScroll(ScrollBarEvent event) {
                   computeVisibleRows();
                   renderView(-1,-1);
                }
            });
            outer.add(scrollBar);    
            scrollView.setWidth(Math.max(table.getWidth() - scrollBar.getOffsetWidth(), 0) + "px");
        }else 
            scrollView.setWidth(table.getWidth() + "px");

       
        /*
         * Setup so Horizontal scrollbar is include in the offsetHeight when drawn
         */
        scrollView.setAlwaysShowScrollBars(true);
        scrollView.setHeight("100%");

        
        if (firstAttach) {
            firstAttach = false;
            
            for (int i = 0; i < table.getVisibleRows(); i++ ) {
                createRow(i);
            }

            scrollView.setHeight(scrollView.getOffsetHeight() + "px");

            if (scrollBar != null) {
                rowHeight = flexTable.getOffsetHeight() / table.getVisibleRows();
                scrollBar.setHeight(flexTable.getOffsetHeight() + "px");
                if (table.hasHeader)
                    DOM.setStyleAttribute(scrollBar.getElement(), "top", header.getOffsetHeight() +
                                                                         "px");
                DOM.setStyleAttribute(scrollBar.getElement(), "left", "-2px");
                adjustScrollBarHeight();
            }

            for (int i = 0; i < table.getVisibleRows(); i++ ) {
                flexTable.removeRow(0);
            }   
            
            computeVisibleRows();
        }
        
        //***  Horizontal ScrollBar *****************
        if(table.getHorizontalScroll() == Scrolling.NEVER)
            DOM.setStyleAttribute(scrollView.getElement(),"overflowX","hidden");
        else if(table.getHorizontalScroll() == Scrolling.AS_NEEDED){
            if(table.getTableWidth() > table.getWidth())
                DOM.setStyleAttribute(scrollView.getElement(),"overflowX","scroll");
            else
                DOM.setStyleAttribute(scrollView.getElement(),"overflowX","hidden");
        } 
        DOM.setStyleAttribute(scrollView.getElement(),"overflowY","hidden");
        
        renderView(-1,-1);
        
        adjustScrollBarHeight();

    }
    
    protected void resize() {
        header.resize();
        flexTable.setWidth(table.getTableWidth() + "px");
        for (int c = 0; c < table.getColumnCount(); c++ ) {
            flexTable.getColumnFormatter().setWidth(c, table.getColumnAt(c).getWidth() + "px");
        }
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
    private void createRow(int r) {

        flexTable.insertRow(r);
        for (int c = 0; c < table.getColumnCount(); c++ ) {
            flexTable.getColumnFormatter().setWidth(c, table.getColumnAt(c).getWidth() + "px");
        }
        flexTable.getCellFormatter().setHeight(r,0, table.getRowHeight()+"px");

    }

    protected void renderView(int startR, int endR) {
        int rc, fr, lr, delta, i;

        fr = firstVisibleRow;
        lr = lastVisibleRow;
        delta = fr - prevFirstVisibleRow;
        rc = 0;
        
        if(startR >=0) {
            if(startR > lr)
                fr = lr+1;
            else if(startR >= fr)
                fr = startR;
            delta = (table.getVisibleRows() / 3) +1;
            rc = fr - firstVisibleRow;
        }
        if(endR >=0) {
            if(endR < fr)
                lr = fr -1;
            else if(endR <= lr)
                lr = endR;
        }
        
        if (Math.abs(delta) <= table.getVisibleRows() / 3) {
            i = delta;
            if (delta > 0) {
                fr = lr - delta;
                rc = table.getVisibleRows() - 1 - delta;
                while (i-- > 0)
                    flexTable.removeRow(0);

            } else if (delta < 0) {
                lr = fr - delta;
                while (i++ < 0)
                    flexTable.removeRow(flexTable.getRowCount() - 1);
                i = delta;
                while (i++ < 0)
                    createRow(0);

            }

        }

        /*
         * Create/Load Rows in the flexTable table
         */

        for (int r = fr; r <= lr; r++ , rc++ ) {
            /*
             * Create table row if needed
             */
            if (rc >= flexTable.getRowCount())
                createRow(flexTable.getRowCount());

            for (int c = 0; c < table.getColumnCount(); c++ ) {
                renderCell(rc, c, r);
            }
            
            applyRowStyle(r,rc);
        }
        /*
         * Remove extras at the end of the view if necessary
         */
        if (table.getRowCount() < flexTable.getRowCount()) {
            for (i = 0; i < flexTable.getRowCount() - rc; i++ )
                flexTable.removeRow(i);
        }
        
        if(table.getVerticalScroll() == Scrolling.AS_NEEDED) {
            if(table.getRowCount() > table.getVisibleRows()) 
                scrollBar.setVisible(true);
            else
                scrollBar.setVisible(false);
        }

    }
    
    protected void applyRowStyle(int r,int rc) {
        String style;
        
        style = table.getModel().get(r).getStyle(r);
        if (style != null)
            flexTable.getRowFormatter().setStyleName(rc, style);
            
        if(table.isRowSelected(r))
            applySelectionStyle(r);//flexTable.getRowFormatter().addStyleName(rc, "Selection");
        else
            applyUnselectionStyle(r);//flexTable.getRowFormatter().removeStyleName(rc,"Selection");
   }
    
    protected void applySelectionStyle(int r) {
        int rc;
        
        rc = getFlexTableIndex(r);
        if(rc > -1)
            flexTable.getRowFormatter().addStyleName(rc,"Selection");
    }
    
    protected void applyUnselectionStyle(int r) {
        int rc;
        
        rc = getFlexTableIndex(r);
        if(rc > -1)
            flexTable.getRowFormatter().removeStyleName(rc, "Selection");
        
    }

    protected void renderCell(int row, int col) {
        int rc;
        
        rc = getFlexTableIndex(row);
        if(rc > -1)
            renderCell(getFlexTableIndex(row), col, row);
    }

    @SuppressWarnings("unchecked")
    private void renderCell(int flexTableIndex, int col, int modelIndex) {
        table.getColumnAt(col).getCellRenderer().render(table, flexTable, flexTableIndex, col,
                                                        table.getValueAt(modelIndex, col));
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
        if(modelIndex >= firstVisibleRow && modelIndex <= lastVisibleRow)
            return modelIndex - firstVisibleRow;
        return -1;
    }

    public void adjustScrollBarHeight() {

        if (scrollBar == null)
            return;

        scrollBar.adjustScrollMax(table.getRowCount() * rowHeight);
    }

    private void computeVisibleRows() {
        
        prevFirstVisibleRow = firstVisibleRow;
        prevLastVisibleRow  = lastVisibleRow;
        
        if (scrollBar != null) {
            firstVisibleRow = (int) (scrollBar.getScrollPosition() / rowHeight);
            lastVisibleRow = Math.min(firstVisibleRow + table.getVisibleRows() - 1,
                                      table.getRowCount() - 1);
        } else {
            firstVisibleRow = 0;
            lastVisibleRow = Math.min(table.getVisibleRows() - 1, table.getRowCount() - 1);
        }

    }

    protected boolean scrollToVisible(int r) {
        int fr;
        
        if(isRowVisible(r))
            return false;
        
        if (r < firstVisibleRow)
            fr = r;
        else
            fr = r - table.getVisibleRows() + 1;

        scrollBar.setScrollPosition(fr * rowHeight);

        return true;
    }
    
    protected boolean isRowVisible(int r) {
        return r >= firstVisibleRow && r <= lastVisibleRow;
    }
    

    @Override
    protected void onAttach() {

        if ( !isOrWasAttached()) {
            attached = true;
            firstAttach = true;
        }
        super.onAttach();
        layout();

    }
}
