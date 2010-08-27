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
package org.openelis.gwt.widget.redesign.tree;

import java.util.Date;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.ScrollBarEvent;
import org.openelis.gwt.event.ScrollBarHandler;
import org.openelis.gwt.widget.DragItem;
import org.openelis.gwt.widget.ExceptionHelper;
import org.openelis.gwt.widget.ScrollBar;
import org.openelis.gwt.widget.redesign.tree.Tree.Scrolling;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class View extends Composite {

    protected Tree  tree;
    protected FlexTable flexTable;
    protected Header header;
    protected ScrollPanel scrollView;
    protected ScrollBar vertScrollBar;
    protected HorizontalPanel outer;
    protected VerticalPanel inner;
    
    protected int firstVisibleItem, lastVisibleItem, prevFirstVisibleItem, prevLastVisibleItem;
    
    protected boolean attached,firstAttach,visibleChanged;
    
    protected int rowHeight, lastItem = -1, lastCol = -1, lastX = -1, lastY = -1;
    
    protected Timer timer;
    
    public View(Tree tre) {
        FocusPanel fp;
        
        this.tree = tre;
        /*
         * Create and set outer once here as the composite widget instead of in
         * layout() because it can only be called once.
         */
        outer = new HorizontalPanel();
        initWidget(outer);
        
        scrollView = new ScrollPanel();
        
        /*
         * Setup so Horizontal scrollbar is include in the offsetHeight when
         * drawn
         */
        scrollView.setAlwaysShowScrollBars(true);

        outer.add(scrollView);
        
        /*
         * Set MouseWheelHandler to the view and then adjust the scrollBar
         * accordingly
         */
        addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(MouseWheelEvent event) {
                int pos, delta;

                pos = vertScrollBar.getScrollPosition();
                delta = event.getDeltaY();

                if (delta < 0 && delta > -rowHeight)
                    delta = -rowHeight;
                if (delta > 0 && delta < rowHeight)
                    delta = rowHeight;
                vertScrollBar.setScrollPosition(pos + delta);
            }
        }, MouseWheelEvent.getType());
        
        /*
         * The FlexTable is placed in a FocusPanel to provide MouseMove events
         * so mousing in and out of cells can be detected.
         */
        fp = new FocusPanel();
        fp.setWidget(flexTable);

        fp.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {

                int row, col;

                lastX = event.getClientX();
                lastY = event.getClientY();
                col = tree.getColumnForX(event.getX());
                row = firstVisibleItem + (event.getY() / rowHeight);

                if (row == lastItem && col == lastCol)
                    return;

                ExceptionHelper.closePopup();

                timer.cancel();

                lastItem = row;
                lastCol = col;

                timer.schedule(250);
            }
        });

        inner = new VerticalPanel();
        scrollView.setWidget(inner);
        inner.add(fp);
        inner.setSpacing(0);

        timer = new Timer() {
            public void run() {
                tree.drawExceptions(lastItem, lastCol, lastX, lastY);
            }
        };
    }
    
    /**
     * Method that will layout the table view and is called on first time
     * attached and when attributes affecting layout are changed in the table
     */
    protected void layout() {

        /*
         * If View is not attached to DOM yet get out. onAttach will call
         * layout() the first time this widget attached.
         */

        if ( !attached)
            return;

        flexTable.setStyleName(tree.TREE_STYLE);
        flexTable.setWidth(tree.getTotalColumnWidth() + "px");

        // ********** Create and attach Header **************
        if (tree.hasHeader() && header == null) {
            header = new Header(tree);
            inner.insert(header, 0);
        } else if ( !tree.hasHeader() && header != null) {
            inner.remove(0);
            header = null;
        }

        // **** Vertical ScrollBar **************
        if (tree.getVerticalScroll() != Scrolling.NEVER && vertScrollBar == null) {
            vertScrollBar = new ScrollBar();
            vertScrollBar.addScrollBarHandler(new ScrollBarHandler() {
                public void onScroll(ScrollBarEvent event) {
                    visibleChanged = true;
                    renderView( -1, -1);
                }
            });
            outer.add(vertScrollBar);
        } else if (tree.getVerticalScroll() == Scrolling.NEVER && vertScrollBar != null) {
            outer.remove(1);
            vertScrollBar = null;
        }

        scrollView.setWidth(Math.max(tree.getWidthWithoutScrollbar(), 0) + "px");

        /*
         * This code is executed the first time the Table is attached
         */
        if (firstAttach) {

            firstAttach = false;

            for (int i = 0; i < tree.getVisibleRows(); i++ )
                createRow(i);
            
            rowHeight = flexTable.getOffsetHeight() / tree.getVisibleRows();

            if (tree.getFixScrollbar())
                scrollView.setHeight(scrollView.getOffsetHeight() + "px");
            else
                scrollView.setHeight("100%");

            if (vertScrollBar != null) {
                vertScrollBar.setHeight(flexTable.getOffsetHeight() + "px");
                if (tree.hasHeader)
                    DOM.setStyleAttribute(vertScrollBar.getElement(), "top",
                                          header.getOffsetHeight() + "px");
                DOM.setStyleAttribute(vertScrollBar.getElement(), "left", "-2px");
                adjustScrollBarHeight();
            }

            for (int i = 0; i < tree.getVisibleRows(); i++ )
                flexTable.removeRow(0);

            visibleChanged = true;
        }

        // *** Horizontal ScrollBar *****************
        if (tree.getHorizontalScroll() == Scrolling.NEVER)
            DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "hidden");
        else if (tree.getHorizontalScroll() == Scrolling.AS_NEEDED) {
            if (tree.getTotalColumnWidth() > tree.getWidthWithoutScrollbar())
                DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "scroll");
            else
                DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "hidden");
        }
        DOM.setStyleAttribute(scrollView.getElement(), "overflowY", "hidden");

        renderView( -1, -1);

        adjustScrollBarHeight();

    }
    
    /**
     * This method is called when a column width is changed. It will resize the
     * columns to there currently set width.
     */
    protected void resize() {
        for (int c = 0; c < tree.getColumnCount(); c++ )
            flexTable.getColumnFormatter().setWidth(c, tree.getColumnAt(c).getWidth() + "px");
        flexTable.setWidth(tree.getTotalColumnWidth() + "px");
    }
    
    /**
     * Will create the the necessary visible rows for the flexTable table
     * depending on what is needed at the time. If model.size() < visibleRows
     * then the number of rows created will equal model.size() else the number
     * visibleRows will be created for the flexTable table.
     */
    private void createRow(int r) {
        flexTable.insertRow(r);
        flexTable.getCellFormatter().setHeight(r, 0, tree.getRowHeight() + "px");

        if (tree.getDragController() != null)
            tree.dragController.makeDraggable(new DragItem(tree, flexTable.getRowFormatter()
                                                                            .getElement(r)));
    }
    
    /**
     * This method will redraw the table from the startRow to the endRow that
     * are passed in as params. Rows are passed as -1,-1 the entire view will be
     * drawn.
     * 
     * @param startR
     * @param endR
     */
    protected void renderView(int startR, int endR) {
        int rc, fr, lr, delta, i;
        Date time;

        if ( !attached)
            return;

        tree.finishEditing();

        computeVisibleRows();

        fr = firstVisibleItem;
        lr = lastVisibleItem;
        delta = fr - prevFirstVisibleItem;
        rc = 0;

        /*
         * Determine new fr and rc if startR is set
         */
        if (startR >= 0) {
            if (startR > lr)
                fr = lr + 1;
            else if (startR >= fr)
                fr = startR;
            delta = 0;
            rc = fr - firstVisibleItem;
        }
        /*
         * Determine new lr if endR is set
         */
        if (endR >= 0) {
            if (endR < fr)
                lr = fr - 1;
            else if (endR <= lr)
                lr = endR;
        }

        /*
         * If delta is 1/3 or less of the table view we will delete and add new
         * rows instead of rendering all rows over.
         */
        if (delta != 0 && Math.abs(delta) <= tree.getVisibleRows() / 3) {
            i = delta;

            if (delta > 0) {
                fr = lr - delta;
                rc = tree.getVisibleRows() - 1 - delta;
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
        int totalCells = 0;
        long totalTime = 0;
        // System.out.println("fr = "+ fr+ " : lr = "+lr);
        for (int r = fr; r <= lr; r++ , rc++ ) {
            /*
             * Create table row if needed
             */
            if (rc >= flexTable.getRowCount()) {
                createRow(flexTable.getRowCount());
                /*
                 * ColumnFormatter is not available until first row is inserted
                 * so call resize after that
                 */
                if (rc == 0)
                    resize();
            }

            for (int c = 0; c < tree.getColumnCount(); c++ ) {
                time = new Date();
                renderCell(rc, c, r);
                totalTime += new Date().getTime() - time.getTime();
                totalCells++ ;
            }

            applyRowStyle(r, rc);
        }

        /*
         * Remove extra rows at the end of the view if necessary
         */
        if (tree.getItemCount() < flexTable.getRowCount()) {
            int remove = flexTable.getRowCount() - rc;
            while (remove-- > 0)
                flexTable.removeRow(rc);
        }

        /*
         * Check if scrollbar needs to be made visible or hidden
         */
        if (tree.getVerticalScroll() == Scrolling.AS_NEEDED) {
            if (tree.getItemCount() > tree.getVisibleRows())
                vertScrollBar.setVisible(true);
            else
                vertScrollBar.setVisible(false);
        }

    }

    /**
     * This method will apply either a style that is set in the Row getStyle
     * method or the selection style if the row is selected
     */
    protected void applyRowStyle(int r, int rc) {
        String style;

        style = tree.getItemAt(r).getStyle(r);
        if (style != null)
            flexTable.getRowFormatter().setStyleName(rc, style);

        if (tree.isItemSelected(r))
            applySelectionStyle(r);
        else
            applyUnselectionStyle(r);
    }

    /**
     * Applies the selection style to a table row
     * 
     * @param r
     */
    protected void applySelectionStyle(int r) {
        int rc;

        rc = getFlexTableIndex(r);
        if (rc > -1)
            flexTable.getRowFormatter().addStyleName(rc, "Selection");
    }

    /**
     * Removes the Selection style from a table row
     * 
     * @param r
     */
    protected void applyUnselectionStyle(int r) {
        int rc;

        rc = getFlexTableIndex(r);
        if (rc > -1)
            flexTable.getRowFormatter().removeStyleName(rc, "Selection");
    }

    /**
     * Method will get the columns cell renderer for the passed row and col and
     * redraw the cell based on the value in the tabel model for that cell.
     * 
     * @param row
     * @param col
     */
    protected void renderCell(int row, int col) {
        int rc;

        rc = getFlexTableIndex(row);
        if (rc > -1)
            renderCell(getFlexTableIndex(row), col, row);
    }

    /**
     * Method will get the columns cell renderer for the passed row and col and
     * redraw the cell based on the value in the tabel model for that cell.
     * 
     * @param r
     * @param col
     * @param row
     */
    @SuppressWarnings("unchecked")
    private void renderCell(int r, int col, int row) {

        if (tree.getQueryMode())
            tree.getColumnAt(col).getCellRenderer().renderQuery(
                                                                 tree,
                                                                 flexTable,
                                                                 r,
                                                                 col,
                                                                 (QueryData)tree.getValueAt(row,
                                                                                             col));
        else
            tree.getColumnAt(col).getCellRenderer().render(tree, flexTable, r, col,
                                                            tree.getValueAt(row, col));

        if (tree.hasExceptions(row, col))
            flexTable.getCellFormatter().addStyleName(r, col, "InputError");
        else
            flexTable.getCellFormatter().removeStyleName(r, col, "InputError");
    }

    /**
     * Will put the passed cell into edit mode making sure the the cell is
     * compeltely visible first
     * 
     * @param row
     * @param col
     * @param value
     * @param event
     */
    @SuppressWarnings("unchecked")
    public void startEditing(int row, final int col, Object value, GwtEvent event) {
        int r, x1, x2, v1, v2;

        r = getFlexTableIndex(row);
        /*
         * Get X coord of the column in the table
         */
        x1 = tree.getXForColumn(col);
        x2 = x1 + tree.getColumnAt(col).getWidth();

        /*
         * Get the currently viewed portion of the table
         */
        v1 = scrollView.getHorizontalScrollPosition();
        v2 = v1 + tree.getWidthWithoutScrollbar();

        /*
         * Make sure the cell is completely visible
         */
        if (x1 < v1)
            scrollView.setHorizontalScrollPosition(x1);
        else if (x2 > v2)
            scrollView.setHorizontalScrollPosition(x2 - tree.getWidthWithoutScrollbar());

        if (tree.getQueryMode())
            tree.getColumnAt(col)
                 .getCellEditor()
                 .startQueryEditing(tree, flexTable, r, col,
                                    (QueryData)tree.getValueAt(row, col), event);
        else
            tree.getColumnAt(col).getCellEditor().startEditing(tree, flexTable, r, col,
                                                                tree.getValueAt(row, col), event);
    }

    /**
     * Returns the value of the CellEditor
     * 
     * @param row
     * @param col
     * @return
     */
    protected Object finishEditing(int row, int col) {
        return tree.getColumnAt(col).getCellEditor().finishEditing(tree, flexTable, row, col);
    }

    /**
     * This method will translate a model index to a physical table index Will
     * return -1 if the row is not in the current view.
     * 
     * @param modelIndex
     * @return
     */
    private int getFlexTableIndex(int row) {
        if (isRowVisible(row))
            return row - firstVisibleItem;
        return -1;
    }

    /**
     * This method will re-adjust the scrollbar height based on number of rows
     * in the model
     */
    protected void adjustScrollBarHeight() {

        if (vertScrollBar == null)
            return;

        vertScrollBar.adjustScrollMax(tree.getItemCount() * rowHeight);
    }

    /**
     * Method will compute the first and last model rows that are visible in the
     * table.
     */
    protected void computeVisibleRows() {
        if (visibleChanged) {
            visibleChanged = false;
            prevFirstVisibleItem = firstVisibleItem;
            prevLastVisibleItem = lastVisibleItem;

            if (vertScrollBar != null) {
                firstVisibleItem = (int) (vertScrollBar.getScrollPosition() / rowHeight);
                lastVisibleItem = Math.min(firstVisibleItem + tree.getVisibleRows() - 1,
                                          tree.getItemCount() - 1);
            } else {
                firstVisibleItem = 0;
                lastVisibleItem = Math.min(tree.getVisibleRows() - 1, tree.getItemCount() - 1);
            }
        }

    }

    /**
     * Method will scroll the table to make sure the passed row is included in
     * the view
     * 
     * @param r
     * @return
     */
    protected boolean scrollToVisible(int r) {
        int fr;

        computeVisibleRows();

        if (vertScrollBar == null)
            return false;

        if (isRowVisible(r))
            return false;

        if (r < firstVisibleItem)
            fr = r;
        else
            fr = r - tree.getVisibleRows() + 1;

        vertScrollBar.setScrollPosition(fr * rowHeight);

        return true;
    }

    /**
     * Method will scroll the view up or down by the passed number of rows.
     * Pass negative value to scroll up.
     * @param rows
     */
    protected void scrollBy(int rows) {
        int fr;

        computeVisibleRows();

        if (vertScrollBar == null)
            return;

        fr = firstVisibleItem + rows;

        if (fr < 0)
            fr = 0;
        else if (fr >= tree.getItemCount())
            fr = tree.getItemCount() - tree.getVisibleRows() + 1;

        vertScrollBar.setScrollPosition(fr * rowHeight);

    }

    /**
     * Returns true if the passed row is drawn in the current view
     * 
     * @param r
     * @return
     */
    protected boolean isRowVisible(int r) {
        return r >= firstVisibleItem && r <= lastVisibleItem;
    }

    /**
     * Method is overridden from Composite so that layout() can be deferred
     * until it is attached to the DOM
     */
    @Override
    protected void onAttach() {

        if ( !isOrWasAttached()) {
            attached = true;
            firstAttach = true;
            layout();
        }
        super.onAttach();

    }

    /**
     * Returns the Header for this view
     * 
     * @return
     */
    protected Header getHeader() {
        return header;
    }

    /**
     * Returns the actual drawn row height on the screen 
     */
    protected int getRowHeight() {
        return rowHeight;
    }

    /**
     * Sets the visible changed to true so that computeVisibleRows() will
     * perform the last and first row calculation
     * 
     * @param changed
     */
    protected void setVisibleChanged(boolean changed) {
        visibleChanged = changed;
    }    
 
}
