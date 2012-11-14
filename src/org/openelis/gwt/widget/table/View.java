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
import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.resources.TableCSS;
import org.openelis.gwt.widget.DragItem;
import org.openelis.gwt.widget.ExceptionHelper;
import org.openelis.gwt.widget.VerticalScrollbar;
import org.openelis.gwt.widget.table.Table.Scrolling;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.VisibleEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Composite GWT widget to draw and handle logic for displaying a Table. All
 * methods are protected and only used by the Table widget itself.
 * 
 * @author tschmidt
 * 
 */
public class View extends Composite {
	@UiTemplate("view.ui.xml")
	interface ViewUiBinder extends UiBinder<Widget, View>{};
	public static final ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
	
    /**
     * Reference to Table this View is used in
     */
    protected Table           table;

    /**
     * Table used to draw Table flexTable
     */
    @UiField
    protected FlexTable       flexTable;

    /**
     * Table used to draw Header flexTable for the table
     */
    @UiField(provided=true)
    protected Header          header;

    /**
     * Scrollable area that contains flexTable and possibly header for
     * horizontal scroll.
     */
    @UiField
    protected ScrollPanel     scrollView;

    /**
     * Vertical ScrollBar
     */
    @UiField
    protected VerticalScrollbar       vertScrollBar;

    /**
     * Panel to hold Scrollable view area and ScrollBar together.
     */
    @UiField
    protected HorizontalPanel outer;
    @UiField
    protected VerticalPanel   inner;

    @UiField
    protected FocusPanel      fp;
    /**
     * Computed first and last model indexes displayed in the table
     */
    protected int             firstVisibleRow, lastVisibleRow;

    /**
     * Flag used to determine if the table has been attached to know when to do
     * layout for the first time.
     */
    protected boolean         attached, firstAttach;

    /**
     * Computed Row Height used to calculate ScrollHeight and ScrollPosition
     * since all browsers don't seem to draw rows to the same height
     */
    protected int             rowHeight, scrollBarHeight, lastRow = -1, lastCol = -1, lastX = -1, lastY = -1;

    /**
     * Timer used to determine if over cell should try and display errors
     */
    protected Timer           timer;
    
    /**
     * Container to hold the widget for formatting and spacing
     */
    private Container         container;
    
    protected TableCSS        css;

    protected View            source = this;
    /**
     * Constructor that takes a reference to the table that will use this view
     * 
     * @param tree
     */
    public View(Table tbl) {
    	header = new Header(tbl);
    	initWidget(uiBinder.createAndBindUi(this));

        this.table = tbl;

        /*
         * Setup so Horizontal scrollbar is include in the offsetHeight when
         * drawn
         */
        scrollView.setAlwaysShowScrollBars(true);


        timer = new Timer() {
            public void run() {
                table.drawExceptions(lastRow, lastCol, lastX, lastY);
            }
        };
        
        container = new Container();
        
        setCSS(OpenELISResources.INSTANCE.table());
    }

    @UiHandler("flexTable")
    protected void handleClick(ClickEvent event) {
    	int r,c;
    	
    	// if x < 0 the user moused out of table before letting up button
    	// ignore event in this case
    	if(event.getX() < 0)
    		return;
    	
        c = table.getColumnForX(event.getX());
        r = firstVisibleRow + (event.getY() / rowHeight);
        
        if(table.fireCellClickedEvent(r, c, event.isControlKeyDown(),event.isShiftKeyDown()))
        		table.startEditing(r,c, (GwtEvent)event);
    }
    
    @UiHandler("fp")
    protected void handleMouseMove(MouseMoveEvent event) {
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
        
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

        	@Override
        	public void execute() {
        		flexTable.removeAllRows();
        		for (int c = 0; c < table.getColumnCount(); c++ ) {
        			flexTable.getColumnFormatter().setWidth(c, table.getColumnAt(c).getWidth() + "px");
        			if(table.getColumnAt(c).getStyle() != null)
        				flexTable.getColumnFormatter().setStyleName(c, table.getColumnAt(c).getStyle());
        		}

        		flexTable.setWidth(table.getTotalColumnWidth() + "px");

        		// ********** Create and attach Header **************
        		if (table.hasHeader()) {
        			header.setVisible(true);
        			header.layout();
        		} else {
        			header.setVisible(false);
        		}
        		
        		DOM.setStyleAttribute(scrollView.getElement(), "overflowY", "hidden");

        		/*
        		 * This code is executed the first time the Table is attached
        		 */
        		if (firstAttach) {

        			firstAttach = false;

        			DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "scroll");

        			scrollView.setWidth("100%");

        			flexTable.removeAllRows();
        			for (int i = 0; i < table.getVisibleRows(); i++ )
        				createRow(i);
        			
        			rowHeight = flexTable.getOffsetHeight() / table.getVisibleRows();

        			scrollBarHeight = scrollView.getOffsetHeight() - ((table.getVisibleRows()*rowHeight) + (table.hasHeader() ? header.getOffsetHeight() :0));

        			if(table.viewWidth == -1)
        				table.viewWidth = scrollView.getOffsetWidth();

        			for (int i = 0; i < table.getVisibleRows(); i++ )
        				flexTable.removeRow(0);
        			
         			vertScrollBar.addScrollHandler(new ScrollHandler() {
        				public void onScroll(ScrollEvent event) {
        					renderView( -1, -1);
        				}
        			});
        			vertScrollBar.addMouseWheelHandler(source, rowHeight);
        		}

        		scrollView.setWidth(table.viewWidth+"px");


        		// **** Vertical ScrollBar **************
        		if (table.getVerticalScroll() != Scrolling.NEVER) {
        			vertScrollBar.setVisible(true);
        		} else if (table.getVerticalScroll() == Scrolling.NEVER) {
        			vertScrollBar.setVisible(false);
        		}

        		// *** Horizontal ScrollBar *****************
        		if (table.getHorizontalScroll() == Scrolling.NEVER)
        			DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "hidden");
        		else if (table.getHorizontalScroll() == Scrolling.AS_NEEDED) {
        			if (table.getTotalColumnWidth() > table.getWidthWithoutScrollbar())
        				DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "scroll");
        			else
        				DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "hidden");
        		}else if(table.getHorizontalScroll() == Scrolling.ALWAYS)
        			DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "scroll");

        		if (table.getFixScrollbar())
        			scrollView.setHeight((table.getVisibleRows()*rowHeight)+
        					(table.hasHeader() ? header.getOffsetHeight() : 0) + 
        					(table.getHorizontalScroll() == Scrolling.ALWAYS ||
        					(table.getHorizontalScroll() == Scrolling.AS_NEEDED && 
        					table.getTotalColumnWidth() > table.getWidthWithoutScrollbar()) ? scrollBarHeight : 0) + "px");	 
        		else
        			scrollView.setHeight("100%");

        		if (vertScrollBar.isVisible()) {
        			vertScrollBar.setHeight(table.getVisibleRows()*rowHeight+"px");
        			if (table.hasHeader) {
        				DOM.setStyleAttribute(vertScrollBar.getElement(), "top",
        						header.getOffsetHeight() + "px");
        			}
        			DOM.setStyleAttribute(vertScrollBar.getElement(), "left", "-2px");
        			adjustScrollBarHeight();
        		}

        		renderView( -1, -1);

        		adjustScrollBarHeight();

        	}
        });
    }

    /**
     * This method is called when a column width is changed. It will resize the
     * columns to there currently set width.
     */
    protected void resize() {
        for (int c = 0; c < table.getColumnCount(); c++ )
            flexTable.getColumnFormatter().setWidth(c, table.getColumnAt(c).getWidth() + "px");
        flexTable.setWidth(table.getTotalColumnWidth() + "px");
        
        /*
         * Determine if Scrollbar needs to be added or removed
         */
        if (table.getHorizontalScroll() == Scrolling.AS_NEEDED) {
            if (table.getTotalColumnWidth() > table.getWidthWithoutScrollbar())
                DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "scroll");
            else
                DOM.setStyleAttribute(scrollView.getElement(), "overflowX", "hidden");
        }
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
        int rc, fvr, lvr, delta, i, pfr;

        if ( !attached || firstAttach)
            return;

        table.finishEditing();
        
        pfr = firstVisibleRow;

        computeVisibleRows();

        fvr = firstVisibleRow;
        lvr = lastVisibleRow;
        delta = fvr - pfr;
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
            flexTable.getRowFormatter().addStyleName(rc, css.Selection());
        else
            flexTable.getRowFormatter().removeStyleName(rc, css.Selection());
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
            flexTable.getRowFormatter().addStyleName(rc, css.Selection());
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
            flexTable.getRowFormatter().removeStyleName(rc, css.Selection());
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
    	CellRenderer renderer;
    	
    	renderer = table.getColumnAt(c).getCellRenderer(r);

    	if (table.getQueryMode())
    		renderer.renderQuery(flexTable, rc, c,(QueryData)table.getValueAt(r, c));
    	else
    		renderer.render(flexTable, rc, c, table.getValueAt(r, c));
    	
    	if (table.hasExceptions(r, c))
    		flexTable.getCellFormatter().addStyleName(rc, c, css.InputError());
    	else
    		flexTable.getCellFormatter().removeStyleName(rc, c, css.InputError());
    	
    	flexTable.getCellFormatter().setVisible(rc, c, table.getColumnAt(c).isDisplayed());
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

        vertScrollBar.setScrollHeight(table.getRowCount() * rowHeight + 1);
    }

    /**
     * Method will compute the first and last model rows that are visible in the
     * table.
     */
    protected void computeVisibleRows() {
        if (rowHeight > 0 && vertScrollBar.isVisible()) {
            firstVisibleRow = (int) (vertScrollBar.getScrollPosition() / rowHeight);
            lastVisibleRow = Math.min(firstVisibleRow + table.getVisibleRows() - 1,
                                      table.getRowCount() - 1);
        } else {
            firstVisibleRow = 0;
            lastVisibleRow = Math.min(table.getVisibleRows() - 1, table.getRowCount() - 1);
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

        if (vertScrollBar.isVisible())
            return false;

        if (isRowVisible(r))
            return false;

        if (r >= firstVisibleRow)     
            r = r - table.getVisibleRows() + 1;

        vertScrollBar.updateScrollPosition(r * rowHeight);
        
        renderView(-1,-1);
        
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

        if (vertScrollBar.isVisible())
            return;

        fr = firstVisibleRow + n;

        if (fr < 0)
            fr = 0;
        else if (fr >= table.getRowCount())
            fr = table.getRowCount() - table.getVisibleRows() + 1;

        vertScrollBar.updateScrollPosition(fr * rowHeight);
        
        renderView(-1,-1);

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
    
    public void setCSS(TableCSS css) {
    	css.ensureInjected();
    	
    	for(int i = 0; i < flexTable.getRowCount(); i++) {
    		if(flexTable.getRowFormatter().getStyleName(i).contains(this.css.Selection())) {
    			flexTable.getRowFormatter().removeStyleName(i, this.css.Selection());
    			flexTable.getRowFormatter().addStyleName(i,css.Selection());
    		}
    		for(int j = 0; j < flexTable.getCellCount(i); j++) {
    			if(flexTable.getCellFormatter().getStyleName(i, j).contains(this.css.InputError())) {
    				flexTable.getCellFormatter().removeStyleName(i, j, this.css.InputError());
    				flexTable.getCellFormatter().addStyleName(i,j, css.InputError());
    			}
    			if(flexTable.getCellFormatter().getStyleName(i, j).contains(this.css.InputWarning())) {
    				flexTable.getCellFormatter().removeStyleName(i, j, this.css.InputWarning());
    				flexTable.getCellFormatter().addStyleName(i,j, css.InputWarning());
    			}
    		}
    	}
    	
    	this.css = css;
    	
    	flexTable.setStyleName(css.Table());
    	
    }
}
