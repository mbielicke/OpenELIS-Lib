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
package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.ScrollBarEvent;
import org.openelis.gwt.event.ScrollBarHandler;
import org.openelis.gwt.widget.DragItem;
import org.openelis.gwt.widget.ExceptionHelper;
import org.openelis.gwt.widget.ScrollBar;
import org.openelis.gwt.widget.table.Table.Scrolling;

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
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
    protected int             rowHeight, lastRow = -1, lastCol = -1, lastX = -1, lastY = -1;

    /**
     * Timer used to determine if over cell should try and display errors
     */
    protected Timer           timer;
    
    /**
     * Container to hold the widget for formatting and spacing
     */
    private Container         container;

    /**
     * Constructor that takes a reference to the table that will use this view
     * 
     * @param tree
     */
    public View(Table tbl) {
        FocusPanel fp;

        this.table = tbl;
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
            @SuppressWarnings({ "rawtypes" })
            public void onClick(ClickEvent event) {
                Cell cell = flexTable.getCellForEvent(event);
                if(table.fireCellClickedEvent(firstVisibleRow+cell.getRowIndex(), cell.getCellIndex()))
                		table.startEditing(firstVisibleRow + cell.getRowIndex(),
                				cell.getCellIndex(), (GwtEvent)event);
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
                c = table.getColumnForX(event.getX());
                mr = firstVisibleRow + (event.getY() / rowHeight);

                if (mr == lastRow && c == lastCol)
                    return;

                ExceptionHelper.closePopup();

                timer.cancel();

                lastRow = mr;
                lastCol = c;
                
                if(lastRow < table.getRowCount())
                	timer.schedule(250);
            }
        });

        inner = new VerticalPanel();
        scrollView.setWidget(inner);
        inner.add(fp);
        inner.setSpacing(0);

        timer = new Timer() {
            public void run() {
                table.drawExceptions(lastRow, lastCol, lastX, lastY);
            }
        };
        
        container = new Container();
        
        /*
        addVisibleHandler(new VisibleEvent.Handler() {
			
			@Override
			public void onVisibleOrInvisible(VisibleEvent event) {
				Logger.getLogger("OpenELISClient").info("Is Table now visible = "+event.isVisible());
			}
		});
		*/
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

        flexTable.setStyleName(table.TABLE_STYLE);
        flexTable.setWidth(table.getTotalColumnWidth() + "px");

        // ********** Create and attach Header **************
        if (table.hasHeader() && header == null) {
            header = new Header(table);
            inner.insert(header, 0);
        } else if ( !table.hasHeader() && header != null) {
            inner.remove(0);
            header = null;
        } else if( table.hasHeader() && header != null)
        	header.layout();

        // **** Vertical ScrollBar **************
        if (table.getVerticalScroll() != Scrolling.NEVER && vertScrollBar == null) {
            vertScrollBar = new ScrollBar();
            vertScrollBar.addScrollBarHandler(new ScrollBarHandler() {
                public void onScroll(ScrollBarEvent event) {
                    visibleChanged = true;
                    renderView( -1, -1);
                }
            });
            outer.add(vertScrollBar);
        } else if (table.getVerticalScroll() == Scrolling.NEVER && vertScrollBar != null) {
            outer.remove(1);
            vertScrollBar = null;
        }

        scrollView.setWidth(Math.max(table.getWidthWithoutScrollbar()+2, 0) + "px");

        // *** Horizontal ScrollBar *****************
        if (table.getHorizontalScroll() == Scrolling.NEVER)
            DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "hidden");
        else if (table.getHorizontalScroll() == Scrolling.AS_NEEDED) {
            if (table.getTotalColumnWidth() > table.getWidthWithoutScrollbar())
                DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "scroll");
            else
                DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "hidden");
        }
        DOM.setStyleAttribute(scrollView.getElement(), "overflowY", "hidden");
        
        /*
         * This code is executed the first time the Table is attached
         */
        if (firstAttach) {

            firstAttach = false;

            flexTable.removeAllRows();
            for (int i = 0; i < table.getVisibleRows(); i++ )
                createRow(i);

            // rowHeight = table.getRowHeight() + rowHeightAdj;
            rowHeight = flexTable.getOffsetHeight() / table.getVisibleRows();
            
            if (table.getFixScrollbar())
                scrollView.setHeight(scrollView.getOffsetHeight() + "px");
            else
                scrollView.setHeight("100%");

            if (vertScrollBar != null) {
                vertScrollBar.setHeight(flexTable.getOffsetHeight() + "px");
                if (table.hasHeader)
                    DOM.setStyleAttribute(vertScrollBar.getElement(), "top",
                                          header.getOffsetHeight() + "px");
                DOM.setStyleAttribute(vertScrollBar.getElement(), "left", "-2px");
                adjustScrollBarHeight();
            }

            for (int i = 0; i < table.getVisibleRows(); i++ )
                flexTable.removeRow(0);

            visibleChanged = true;
        }



        renderView( -1, -1);

        adjustScrollBarHeight();

    }

    /**
     * This method is called when a column width is changed. It will resize the
     * columns to there currently set width.
     */
    protected void resize() {
        for (int c = 0; c < table.getColumnCount(); c++ )
            flexTable.getColumnFormatter().setWidth(c, table.getColumnAt(c).getWidth() + "px");
        flexTable.setWidth(table.getTotalColumnWidth() + "px");
    }

    /**
     * Will create the the necessary visible rows for the flexTable table
     * depending on what is needed at the time. If model.size() < visibleRows
     * then the number of rows created will equal model.size() else the number
     * visibleRows will be created for the flexTable table.
     */
    private void createRow(int rc) {
        flexTable.insertRow(rc);
        flexTable.getCellFormatter().setHeight(rc, 0, table.getRowHeight() + "px");

        if (table.getDragController() != null)
            table.dragController.makeDraggable(new DragItem(table, flexTable.getRowFormatter()
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

        table.finishEditing();

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
        if (delta != 0 && Math.abs(delta) <= table.getVisibleRows() / 3) {
            i = delta;

            if (delta > 0) {
                fvr = lvr - delta;
                rc = table.getVisibleRows() - 1 - delta;
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

            for (int c = 0; c < table.getColumnCount(); c++ )
                renderCell(rc, c, vr);

            applyRowStyle(vr, rc);
        }

        /*
         * Remove extra rows at the end of the view if necessary
         */
        if (table.getRowCount() < flexTable.getRowCount()) {
            int remove = flexTable.getRowCount() - rc;
            while (remove-- > 0)
                flexTable.removeRow(rc);
        }

        /*
         * Check if scrollbar needs to be made visible or hidden
         */
        if (table.getVerticalScroll() == Scrolling.AS_NEEDED) {
            if (table.getRowCount() > table.getVisibleRows())
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
        
        if(r >= table.getRowCount())
        	return;
        
        style = table.getRowAt(r).getStyle(r);
        if (style != null)
            flexTable.getRowFormatter().setStyleName(rc, style);

        if (table.isRowSelected(r))
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
    protected void renderCell(int r, int c) {
        int rc;

        rc = getFlexTableIndex(r);

        if (rc > -1)
            renderCell(rc, c, r);
    }

    
    @SuppressWarnings("unchecked")
    private void renderCell(int rc, int c, int r) {

        if (table.getQueryMode())
            table.getColumnAt(c).getCellRenderer(r).renderQuery(flexTable, rc, c,
                                                               (QueryData)table.getValueAt(r, c));
        else
            table.getColumnAt(c).getCellRenderer(r).render(flexTable, rc, c,
                                                          table.getValueAt(r, c));

        if (table.hasExceptions(r, c))
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

        rc = getFlexTableIndex(r);
        /*
         * Get X coord of the column in the table
         */
        x1 = table.getXForColumn(c);
        x2 = x1 + table.getColumnAt(c).getWidth();

        /*
         * Get the currently viewed portion of the table
         */
        v1 = scrollView.getHorizontalScrollPosition();
        v2 = v1 + table.getWidthWithoutScrollbar();

        /*
         * Make sure the cell is completely visible
         */
        if (x1 < v1)
            scrollView.setHorizontalScrollPosition(x1);
        else if (x2 > v2)
            scrollView.setHorizontalScrollPosition(x2 - table.getWidthWithoutScrollbar());
        
        container.setWidth( (table.getColumnAt(c).getWidth() - 3));
        container.setHeight( (table.getRowHeight() - 3));
        flexTable.setWidget(rc, c, container);

        if (table.getQueryMode())
            table.getColumnAt(c)
                 .getCellEditor(r)
                 .startEditingQuery((QueryData)table.getValueAt(r, c),container,
                                    event);
        else
            table.getColumnAt(c).getCellEditor(r).startEditing(table.getValueAt(r, c), container, event);
    }

    /**
     * Returns the value of the CellEditor
     * 
     * @param r
     * @param c
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object finishEditing(int r, int c) {
        CellEditor cellEditor;

        cellEditor = table.getColumnAt(c).getCellEditor(r);

        table.setValidateException(r, c, cellEditor.validate());

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

        vertScrollBar.adjustScrollMax(table.getRowCount() * rowHeight + 1);
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
                lastVisibleRow = Math.min(firstVisibleRow + table.getVisibleRows() - 1,
                                          table.getRowCount() - 1);
            } else {
                firstVisibleRow = 0;
                lastVisibleRow = Math.min(table.getVisibleRows() - 1, table.getRowCount() - 1);
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
            r = r - table.getVisibleRows() + 1;

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
        else if (fr >= table.getRowCount())
            fr = table.getRowCount() - table.getVisibleRows() + 1;

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

}
