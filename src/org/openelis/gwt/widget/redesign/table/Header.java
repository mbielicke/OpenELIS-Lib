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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class will create a Header that is used for the Table Widget.
 * 
 * @author tschmidt
 * 
 */
public class Header extends FocusPanel {

    /**
     * Contains Header widgets and is the wrapped widget for this composite.
     */
    protected FlexTable flexTable;
    /**
     * Reference to the Table this header is used for.
     */
    protected Table     table;
    /**
     * Popuppanel used to display the resize bar.
     */
    protected PopupPanel popResize, popFilter;
    /**
     * Position where the resize started.
     */
    protected int        startX, resizeColumn, showingFilterFor;
    /**
     * Widget that used to display a then position due to resizing.
     */
    protected FocusPanel bar, filterButton;
    /**
     * The column that is being resized.
     */
    protected boolean    resizeColStyle, showingFilter;
    protected Header     source = this;

    /**
     * Constructor that takes the containing table as a parameter
     * 
     * @param table
     */
    public Header(final Table table) {
        this.table = table;
        flexTable = new FlexTable();
        flexTable.setStyleName("Header");
        setWidget(flexTable);

        addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {
                checkForResizeFilter(event.getX());
            }
        });

        addHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                showFilter(-1);
                if (popResize == null) {
                    popResize = new PopupPanel();
                    bar = new FocusPanel();
                    bar.setWidth("1px");
                    bar.setHeight(table.getOffsetHeight() + "px");
                    DOM.setStyleAttribute(bar.getElement(), "background", "red");
                    popResize.add(bar);
                    bar.addMouseMoveHandler(new MouseMoveHandler() {
                        public void onMouseMove(MouseMoveEvent event) {
                            popResize.setPopupPosition(
                                                       popResize.getAbsoluteLeft() + (event.getX()),
                                                       popResize.getAbsoluteTop());
                        }
                    });

                    bar.addMouseUpHandler(new MouseUpHandler() {
                        public void onMouseUp(MouseUpEvent event) {
                            Column column;

                            column = table.getColumnAt(resizeColumn);

                            column.setWidth(Math.max(column.getWidth() +
                                                     (popResize.getAbsoluteLeft() - startX),
                                                     column.getMinWidth()));

                            if (popResize != null)
                                popResize.hide();

                        }
                    });
                }

                startX = table.getXForColumn(resizeColumn) +
                         table.getColumnAt(resizeColumn).getWidth() - 1;
                popResize.setPopupPosition(startX, ((Widget)event.getSource()).getAbsoluteTop());
                popResize.show();
                DOM.setCapture(bar.getElement());
                source.unsinkEvents(Event.ONMOUSEDOWN);
            }
        }, MouseDownEvent.getType());

        addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                int relX, relY;

                if (showingFilter) {
                    relX = event.getRelativeX(getElement());
                    relY = event.getRelativeY(getElement());
                    if (relX > -1 && relX < getOffsetWidth() &&
                        relY > -1 && relY < getOffsetHeight())
                        return;
                    else
                        showFilter(-1);
                }
            }
        });

        layout();
    }

    /**
     * Method to draw the Header based on values set in the Columns of the
     * table.
     */
    public void layout() {
        int numCols;
        Column column;

        numCols = table.getColumnCount();

        if (flexTable.getRowCount() < 1)
            flexTable.insertRow(0);

        for (int i = 0; i < numCols; i++ ) {
            column = table.getColumnAt(i);
            flexTable.setText(0, i, column.getLabel());
            flexTable.getColumnFormatter().setWidth(i, column.getWidth() + "px");
        }

        flexTable.setWidth(table.getTableWidth() + "px");
        flexTable.getCellFormatter().setHeight(0, 0, table.getRowHeight() + "px");
    }

    /**
     * Resizes the header to the new column
     */
    protected void resize() {
        Column col;

        for (int i = 0; i < table.getColumnCount(); i++ ) {
            col = table.getColumnAt(i);
            flexTable.getColumnFormatter().setWidth(i, col.getWidth() + "px");
        }

        flexTable.setWidth(table.getTableWidth() + "px");
    }

    private void showFilter(int column) {
        int x;
        
        if (showingFilter) {
            if (showingFilterFor != column) {
                popFilter.hide();
                showingFilter = false;
                showingFilterFor = -1;
            } else
                return;
        }

        if (column < 0)
            return;

        if (table.getColumnAt(column).isFilterable()) {
            x = table.getXForColumn(column) + table.getColumnAt(column).getWidth() - 17;
            if(x > table.getWidth()) 
                return;
            if (popFilter == null) {
                popFilter = new PopupPanel();
                popFilter.setWidth("16px");
                filterButton = new FocusPanel();
                filterButton.setStyleName("FilterButton");
                filterButton.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        // TODO Auto-generated method stub
                    }
                });
                popFilter.add(filterButton);
            }
            popFilter.setPopupPosition(x,getAbsoluteTop());
            popFilter.show();
            showingFilter = true;
            showingFilterFor = column;
        }
    }

    private void checkForResizeFilter(int x) {
        int col1, col2;

        col1 = table.getColumnForX(x - 4);
        col2 = table.getColumnForX(x + 4);

        if (col1 != col2 && col1 >= 0) {
            if (table.getColumnAt(col1).isResizable()) {
                flexTable.getCellFormatter().addStyleName(0, col1, "ResizeCol");
                flexTable.getCellFormatter().addStyleName(0, col2, "ResizeCol");
                resizeColStyle = true;
                resizeColumn = col1;
                sinkEvents(Event.ONMOUSEDOWN);
                return;
            }
        } else if (col1 >= 0) {
            showFilter(col1);
        }

        if (resizeColStyle) {
            flexTable.getCellFormatter().removeStyleName(0, col1, "ResizeCol");
            flexTable.getCellFormatter().removeStyleName(0, col2, "ResizeCol");
            resizeColStyle = false;
            unsinkEvents(Event.ONMOUSEDOWN);
        }

    }

}
