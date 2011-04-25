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
package org.openelis.gwt.widget.tree;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.ScrollBarEvent;
import org.openelis.gwt.event.ScrollBarHandler;
import org.openelis.gwt.widget.DragItem;
import org.openelis.gwt.widget.ExceptionHelper;
import org.openelis.gwt.widget.ScrollBar;
import org.openelis.gwt.widget.table.CellEditor;
import org.openelis.gwt.widget.table.CellRenderer;
import org.openelis.gwt.widget.table.Container;
import org.openelis.gwt.widget.tree.Tree.Scrolling;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.VisibleEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

/**
 * Composite GWT widget to draw and handle logic for displaying a Table. All
 * methods are protected and only used by the Table widget itself.
 * 
 * @author tschmidt
 * 
 */
public class View extends Composite {
    /**
     * Reference to Table this View is used in
     */
    protected Tree            tree;

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
    protected ScrollBar       vertScrollBar;

    /**
     * Panel to hold Scrollable view area and ScrollBar together.
     */
    protected HorizontalPanel outer;
    protected VerticalPanel   inner;

    /**
     * Computed first and last model indexes displayed in the table
     */
    protected int             firstVisibleRow, lastVisibleRow, prevFirstVisibleRow,
                    prevLastVisibleRow;

    /**
     * Flag used to determine if the table has been attached to know when to do
     * layout for the first time.
     */
    protected boolean         attached, firstAttach, visibleChanged;

    /**
     * Computed Row Height used to calculate ScrollHeight and ScrollPosition
     * since all browsers don't seem to draw rows to the same height
     */
    protected int             rowHeight, lastRow = -1, lastCol = -1, lastX = -1, lastY = -1,
                    indent = 18;

    /**
     * Timer used to determine if over cell should try and diplay errors
     */
    protected Timer           timer;

    protected ClickHandler    toggleHandler;

    /**
     * Container to hold the widget for formatting and spacing
     */
    private Container         container;

    /**
     * Constructor that takes a reference to the table that will use this view
     * 
     * @param tree
     */
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
                vertScrollBar.setVerticalScrollPosition(pos + delta);
            }
        }, MouseWheelEvent.getType());

        flexTable = new FlexTable();
        flexTable.addClickHandler(new ClickHandler() {
            @SuppressWarnings("rawtypes")
			public void onClick(ClickEvent event) {
                Cell cell = flexTable.getCellForEvent(event);
                if(tree.fireCellClickedEvent(firstVisibleRow+cell.getRowIndex(), cell.getCellIndex()))
                	tree.startEditing(firstVisibleRow + cell.getRowIndex(), cell.getCellIndex(),
                			(GwtEvent)event);
            }
        });

        /*
         * The FlexTable is placed in a FocusPanel to provide MouseMove events
         * so mousing in and out of cells can be detected.
         */
        fp = new FocusPanel();
        fp.setWidget(flexTable);

        fp.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {

                int mr, c;

                lastX = event.getClientX();
                lastY = event.getClientY();
                c = tree.getColumnForX(event.getX());
                mr = firstVisibleRow + (event.getY() / rowHeight);

                if (mr == lastRow && c == lastCol)
                    return;

                ExceptionHelper.closePopup();

                timer.cancel();

                lastRow = mr;
                lastCol = c;

                timer.schedule(250);
            }
        });

        inner = new VerticalPanel();
        scrollView.setWidget(inner);
        inner.add(fp);
        inner.setSpacing(0);

        timer = new Timer() {
            public void run() {
                tree.drawExceptions(lastRow, lastCol, lastX, lastY);
            }
        };
        
        toggleHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if ( ((Grid)event.getSource()).getCellForEvent(event).getCellIndex() == 0) {
                    tree.toggle(lastRow);
                    event.stopPropagation();
                }
            }
        };
        
        container = new Container();
        
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

        if(!isWidgetVisible()) {
        	addVisibleHandler(new VisibleEvent.Handler() {
        		public void onVisibleOrInvisible(VisibleEvent event) {
        			if(event.isVisible() && isWidgetVisible()){
        				firstAttach = true;
        				layout();
        				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
							public void execute() {
								removeVisibleHandler();
							}
						});
        			}
        		}
        	});
        	return;
        }
        
        flexTable.setStyleName(tree.TREE_STYLE);
        flexTable.setWidth(tree.getTotalColumnWidth() + "px");

        // ********** Create and attach Header **************
        if (tree.hasHeader() && header == null) {
            header = new Header(tree);
            inner.insert(header, 0);
        } else if ( !tree.hasHeader() && header != null) {
            inner.remove(0);
            header = null;
        } else if ( tree.hasHeader() && header != null)
        	header.layout();

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

        scrollView.setWidth(Math.max(tree.getWidthWithoutScrollbar()+2, 0) + "px");

        /*
         * This code is executed the first time the Table is attached
         */
        if (firstAttach) {

            firstAttach = false;

            for (int i = 0; i < tree.getVisibleRows(); i++ )
                createRow(i);

            // rowHeight = table.getRowHeight() + rowHeightAdj;
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
    private void createRow(int rc) {
        flexTable.insertRow(rc);
        flexTable.getCellFormatter().setHeight(rc, 0, tree.getRowHeight() + "px");

        if (tree.getDragController() != null)
            tree.dragController.makeDraggable(new DragItem(tree, flexTable.getRowFormatter()
                                                                          .getElement(rc)));
    }

    /**
     * This method will redraw the table from the startRow to the endRow that
     * are passed in as params. Rows are passed as -1,-1 the entire view will be
     * drawn.
     * 
     * @param smr
     * @param emr
     */
    protected void renderView(int smr, int emr) {
        int rc, fvr, lvr, delta, i;

        if ( !attached)
            return;

        tree.finishEditing();

        computeVisibleRows();

        fvr = firstVisibleRow;
        lvr = lastVisibleRow;
        delta = fvr - prevFirstVisibleRow;
        rc = 0;

        /*
         * Determine new fr and rc if startR is set
         */

        if (smr >= 0) {
            if (smr > lvr)
                fvr = lvr + 1;
            else if (smr >= fvr)
                fvr = smr;
            delta = 0;
            rc = fvr - firstVisibleRow;
        }
        /*
         * Determine new lr if endR is set
         */

        if (emr >= 0) {
            if (emr < fvr)
                lvr = fvr - 1;
            else if (emr <= lvr)
                lvr = emr;
        }

        /*
         * If delta is 1/3 or less of the table view we will delete and add new
         * rows instead of rendering all rows over.
         */
        if (delta != 0 && Math.abs(delta) <= tree.getVisibleRows() / 3) {
            i = delta;

            if (delta > 0) {
                fvr = lvr - delta;
                rc = tree.getVisibleRows() - 1 - delta;
                while (i-- > 0)
                    flexTable.removeRow(0);

            } else if (delta < 0) {
                lvr = fvr - delta;
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
        for (int vr = fvr; vr <= lvr; vr++ , rc++ ) {
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

            for (int c = 0; c < tree.getColumnCount(); c++ )
                renderCell(rc, c, vr);

            applyRowStyle(vr, rc);
        }

        /*
         * Remove extra rows at the end of the view if necessary
         */
        if (tree.getRowCount() < flexTable.getRowCount()) {
            int remove = flexTable.getRowCount() - rc;
            while (remove-- > 0)
                flexTable.removeRow(rc);
        }

        /*
         * Check if scrollbar needs to be made visible or hidden
         */
        if (tree.getVerticalScroll() == Scrolling.AS_NEEDED) {
            if (tree.getRowCount() > tree.getVisibleRows())
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

        style = tree.getNodeAt(r).getStyle(r);
        if (style != null)
            flexTable.getRowFormatter().setStyleName(rc, style);

        if (tree.isNodeSelected(r))
            flexTable.getRowFormatter().addStyleName(rc, "Selection");
        else
            flexTable.getRowFormatter().removeStyleName(rc, "Selection");
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
    protected void renderCell(String def, int r, int c) {
        int rc;

        rc = getFlexTableIndex(r);

        if (rc > -1)
            renderCell(rc, c, r);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void renderCell(int rc, int c, int r) {
        CellRenderer cellRenderer;
        HTMLTable table;
        int row, col;
        Node node;

        node = tree.getNodeAt(r);
        
        if (c < tree.getNodeDefintion(node.getType()).size())
            cellRenderer = tree.getCellRenderer(r, c);
        else {
            flexTable.setText(rc, c, "");
            flexTable.getCellFormatter().removeStyleName(rc, c, "InputError");
            return;
        }

        table = flexTable;
        row = rc;
        col = c;

        if (c == 0) {
            table = getTreeCell(node, rc, c);
            row = 0;
            col = 2;
        }

        if (tree.getQueryMode())
            cellRenderer.renderQuery(table, row, col, (QueryData)tree.getValueAt(r, c));
        else
            cellRenderer.render(table, row, col, tree.getValueAt(r, c));

        if (tree.hasExceptions(r, c))
            flexTable.getCellFormatter().addStyleName(rc, c, "InputError");
        else
            flexTable.getCellFormatter().removeStyleName(rc, c, "InputError");

       
    }

    /**
     * Will put the passed cell into edit mode making sure the the cell is
     * compeltely visible first
     * 
     * @param r
     * @param c
     * @param value
     * @param event
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void startEditing(int r, final int c, Object value, GwtEvent event) {
        int rc, x1, x2, v1, v2;
        CellEditor cellEditor;

        rc = getFlexTableIndex(r);
        /*
         * Get X coord of the column in the table
         */
        x1 = tree.getXForColumn(c);
        x2 = x1 + tree.getColumnAt(c).getWidth();

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

        cellEditor = tree.getCellEditor(r,c);
        
        container.setWidth( (tree.getColumnAt(c).getWidth() - 3));
        container.setHeight( (tree.getRowHeight() - 3));
        flexTable.setWidget(rc, c, container);
        
        DOM.removeElementAttribute(flexTable.getCellFormatter().getElement(rc,c),"style");

        if (tree.getQueryMode())
            cellEditor.startEditingQuery((QueryData)tree.getValueAt(r, c), container, event);
        else
            cellEditor.startEditing(tree.getValueAt(r, c), container, event);
    }

    /**
     * Returns the value of the CellEditor
     * 
     * @param r
     * @param c
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object finishEditing(int r, int c) {
        CellEditor cellEditor;

        cellEditor = tree.getCellEditor(r,c);

        tree.setValidateException(r, c, cellEditor.validate());

        return cellEditor.finishEditing();
    }

    /**
     * This method will translate a view index to a physical table index Will
     * return -1 if the row is not in the current view.
     * 
     * @param viewIndex
     * @return
     */
    private int getFlexTableIndex(int r) {
        if (isRowVisible(r))
            return r - firstVisibleRow;
        return -1;
    }

    /**
     * This method will re-adjust the scrollbar height based on number of rows
     * in the model
     */
    protected void adjustScrollBarHeight() {

        if (vertScrollBar == null)
            return;

        vertScrollBar.adjustScrollMax(tree.getRowCount() * rowHeight);
    }

    /**
     * Method will compute the first and last model rows that are visible in the
     * table.
     */
    protected void computeVisibleRows() {
        if (visibleChanged) {
            visibleChanged = false;
            prevFirstVisibleRow = firstVisibleRow;
            prevLastVisibleRow = lastVisibleRow;

            if (vertScrollBar != null) {
                firstVisibleRow = (int) (vertScrollBar.getScrollPosition() / rowHeight);
                lastVisibleRow = Math.min(firstVisibleRow + tree.getVisibleRows() - 1,
                                          tree.getRowCount() - 1);
            } else {
                firstVisibleRow = 0;
                lastVisibleRow = Math.min(tree.getVisibleRows() - 1, tree.getRowCount() - 1);
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

        computeVisibleRows();

        if (vertScrollBar == null)
            return false;

        if (isRowVisible(r))
            return false;

        if (r >= firstVisibleRow)
            r -= tree.getVisibleRows() + 1;

        vertScrollBar.setVerticalScrollPosition(r * rowHeight);

        return true;
    }

    /**
     * Method will scroll the view up or down by the passed number of rows. Pass
     * negative value to scroll up.
     * 
     * @param n
     */
    protected void scrollBy(int n) {
        int fr;

        computeVisibleRows();

        if (vertScrollBar == null)
            return;

        fr = firstVisibleRow + n;

        if (fr < 0)
            fr = 0;
        else if (fr >= tree.getRowCount())
            fr = tree.getRowCount() - tree.getVisibleRows() + 1;

        vertScrollBar.setVerticalScrollPosition(fr * rowHeight);

    }

    /**
     * Returns true if the passed row is drawn in the current view
     * 
     * @param r
     * @return
     */
    protected boolean isRowVisible(int r) {
        return r >= firstVisibleRow && r <= lastVisibleRow;
    }

    /**
     * Method is overridden from Composite so that layout() can be deferred
     * until it is attached to the DOM
     */
    @Override
    protected void onAttach() {

        if ( !isOrWasAttached() || firstAttach) {
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

    private TreeGrid getTreeCell(Node node, int row, int col) {
        TreeGrid grid = null;
        int level;
        String image;
        Widget widget;

        image = node.getImage();
        level = tree.showRoot() ? node.getLevel() : node.getLevel() - 1;
        
        DOM.setStyleAttribute(flexTable.getCellFormatter().getElement(row, col), "paddingLeft", (level * indent) + "px");

        grid = (widget = flexTable.getWidget(row, col)) instanceof TreeGrid ? (TreeGrid)widget : new TreeGrid();
        
        if(widget != grid)   
            flexTable.setWidget(row, col, grid);
         
        if ( !node.isLeaf()) {
            if (node.isOpen)
                grid.getCellFormatter().setStyleName(0, 0, "treeOpenImage");
            else
                grid.getCellFormatter().setStyleName(0, 0, "treeClosedImage");
            grid.getCellFormatter().setVisible(0, 0, true);
        } else
            grid.getCellFormatter().setVisible(0, 0, false);

        if (image == null)
            grid.getCellFormatter().setVisible(0, 1, false);
        else {
            grid.getCellFormatter().setVisible(0, 1, true);
            grid.getCellFormatter().setStyleName(0, 1, image);
        }
        
        return grid;
    }

    private class TreeGrid extends Grid {
        public TreeGrid() {
            super(1,3);
            addStyleName("TreeCell");
            setWidth("100%");
            getCellFormatter().setWidth(0, 0, "18px");
            getCellFormatter().setWidth(0, 1, "18px");
            getCellFormatter().setWidth(0, 2, "100%");
            addClickHandler(toggleHandler);
            setCellPadding(0);
            setCellSpacing(0);
        }
    }
    


}
