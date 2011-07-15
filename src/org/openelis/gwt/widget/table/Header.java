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

import java.util.ArrayList;

import org.openelis.gwt.widget.CheckMenuItem;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.PopupMenuPanel;

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
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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
    protected PopupPanel popResize, popFilter,popMenu;
    
    /**
     * Position where the resize started.
     */
    protected int        startX, resizeColumn, showingFilterFor,headerHeight = 20;
    
    /**
     * Widget that used to display a then position due to resizing.
     */
    protected FocusPanel bar, filterButton;
    
    /**
     * The column that is being resized.
     */
    protected boolean    resizeColStyle, showingFilter;
        
    /**
     * Reference to this object to be used in anonymous handlers
     */
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

        /*
         * Mouse handler for determining to allow resizing or filter based on 
         * mouse position
         */
        addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {
                checkForResizeFilter(event.getX());
            }
        });

        /*
         * MouseDown handler for doing resize of columns in a table
         */
        addHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                showFilter( -1);
                /*
                 * Initial popResize and bar the first time a resize request
                 * is received
                 */
                if (popResize == null) {
                    popResize = new PopupPanel();
                    bar = new FocusPanel();
                    bar.setWidth("1px");
                    bar.setHeight((table.view.flexTable.getOffsetHeight() + getOffsetHeight())+"px");
                    DOM.setStyleAttribute(bar.getElement(), "background", "red");
                    popResize.add(bar);
                    /*
                     * Move resize bar if mouse moved
                     */
                    bar.addMouseMoveHandler(new MouseMoveHandler() {
                        public void onMouseMove(MouseMoveEvent event) {
                            popResize.setPopupPosition(popResize.getAbsoluteLeft() + event.getX(),
                                                       popResize.getAbsoluteTop());
                        }
                    });

                    /*
                     * Calculate new column size by comparing the startX to the last position of the 
                     * resize bar.
                     */
                    bar.addMouseUpHandler(new MouseUpHandler() {
                        public void onMouseUp(MouseUpEvent event) {
                            Column column;

                            column = table.getColumnAt(resizeColumn);
                            
                            /*
                             * Column will call table.resize() in the call to setWidth
                             */
                            column.setWidth(column.getWidth() +
                                            (popResize.getAbsoluteLeft() - startX));

                            if (popResize != null)
                                popResize.hide();
                            
                            DOM.releaseCapture(bar.getElement());

                        }
                    });
                }

                /*
                 * Calc the start position of the resize bar
                 */
                startX = table.getXForColumn(resizeColumn) +
                         table.getColumnAt(resizeColumn).getWidth() - 1 + getAbsoluteLeft();
                
                popResize.setPopupPosition(startX, ((Widget)event.getSource()).getAbsoluteTop());
                popResize.show();
                /*
                 * We set the capture of mouse events now to the resize bar itself.  This allows us
                 * to simplify the logic of dragging the bar, as well as provide smoother dragging and 
                 * allows the mouse to float outside of the header and still move the resize bar
                 */
                DOM.setCapture(bar.getElement());
               
            }
        }, MouseDownEvent.getType());

        /*
         * Handler to remove filter buttons from header if the mouse leaves the header
         */
        addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                int relX, relY;

                if (showingFilter) {
                    /*
                     * Moving the mouse over a filter button will cause this event to be fired.
                     * We want to determine if the mouse is still in the header and to return out
                     * if so
                     */
                    relX = event.getRelativeX(getElement());
                    relY = event.getRelativeY(getElement());
                    if (relX > -1 && relX < getOffsetWidth() && relY > -1 &&
                        relY < getOffsetHeight())
                        return;
                    else{ 
                        if(popMenu == null || !popMenu.isShowing())
                            showFilter( -1);
                    }
                }
            }
        });
        
        layout();
    }

    /**
     * Method to draw the Header based on values set in the Columns of the
     * table.
     */
    protected void layout() {
        
        if (flexTable.getRowCount() < 1)
            flexTable.insertRow(0);
        
        renderView(-1,-1);
        
        flexTable.setWidth(table.getTotalColumnWidth() + "px");
        flexTable.getCellFormatter().setHeight(0, 0, headerHeight + "px");
        
        flexTable.getCellFormatter().addStyleName(0, 0, "First");
    }
    
    /**
     * This method will render each Header cell in the header.  Pass a range of Header cells to render or pass -1,-1 
     * render all Header cells
     * @param start
     * @param end
     */
    protected void renderView(int start, int end) {
        Column column;
        String header;
        
        if(start < 0)
            start = 0;
        
        if(end < 0)
            end = table.getColumnCount()-1;
        
        for (int i = start; i <= end; i++ ) {
            column = table.getColumnAt(i);
            header = column.getLabel().replaceAll("\\n", "<br/>");
            
            flexTable.setHTML(0, i, header);
            flexTable.getColumnFormatter().setWidth(i, column.getWidth() + "px");
            flexTable.getCellFormatter().setVerticalAlignment(0, i, HasVerticalAlignment.ALIGN_BOTTOM);
            
            if(column.isFiltered())
                flexTable.getCellFormatter().addStyleName(0,i,"Filtered");
            else
                flexTable.getCellFormatter().removeStyleName(0, i, "Filtered");
            
            if(column.isSorted()) 
                flexTable.getCellFormatter().addStyleName(0,i,"Sorted");
            else
                flexTable.getCellFormatter().removeStyleName(0, i, "Sorted");
        }
        
        while(flexTable.getCellCount(0) > table.getColumnCount())
        	flexTable.removeCell(0, flexTable.getCellCount(0) -1);
    }

    /**
     * Method to set the height of the header.
     * @param height
     */
    protected void setHeaderHeight(int height) {
        headerHeight = height;
    }
    
    /**
     * Resizes the header to the new column widths
     */
    protected void resize() {
        Column col;

        for (int i = 0; i < table.getColumnCount(); i++ ) {
            col = table.getColumnAt(i);
            flexTable.getColumnFormatter().setWidth(i, col.getWidth() + "px");
        }

        flexTable.setWidth(table.getTotalColumnWidth() + "px");
    }

    /**
     * Method to show the filter button to allow the user to click to show the filter menu
     * 
     */
    private void showFilter(int column) {
        int x;

        if (showingFilter) {
            if (showingFilterFor != column) {
                popFilter.hide();
            } else
                return;
        }

        if (column < 0)
            return;

        if (table.getColumnAt(column).isFilterable() || table.getColumnAt(column).isSortable) {
            x = table.getXForColumn(column) + table.getColumnAt(column).getWidth();
            /*
             * if the position of the filter button is off the currently scrolled view
             * then just return
             */
            if (x > table.getWidthWithoutScrollbar() +
                    table.view.scrollView.getHorizontalScrollPosition())
                return;
            x -= 17;
            /*
             * Initialize the popFilter the first time.
             */
            if (popFilter == null) {
                popFilter = new PopupPanel(true);
                popFilter.setWidth("16px");
                filterButton = new FocusPanel();
                filterButton.setStyleName("FilterButton");
                
                /*
                 * Show filter menu relative to filterButton if clicked
                 */
                filterButton.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        if(popMenu == null || !popMenu.isShowing()) {
                            table.finishEditing();
                            popMenu = getMenuForColumn(showingFilterFor);
                            popMenu.showRelativeTo(filterButton);
                        }
                    }
                });
                
                /*
                 * This is added so that when the user mouses out of header directly form 
                 * the button we will hide the filter button if not currently showing a menu
                 */
                filterButton.addMouseOutHandler(new MouseOutHandler() {
                    public void onMouseOut(MouseOutEvent event) {
                        if(popMenu == null || !popMenu.isShowing())
                            popFilter.hide();
                    }
                });
                
                /*
                 * When popFilter is closed reset the showingFilter and the column being shown for
                 */
                popFilter.addCloseHandler(new CloseHandler<PopupPanel>() {
                   public void onClose(CloseEvent<PopupPanel> event) {
                       showingFilter = false;
                       showingFilterFor = -1;
                    } 
                });
                popFilter.add(filterButton);
            }
            
            /*
             * Position and show filter button
             */
            popFilter.setPopupPosition(x + getAbsoluteLeft(), getAbsoluteTop()+getOffsetHeight()-20);
            popFilter.show();
            showingFilter = true;
            showingFilterFor = column;
            
            /*
             * If filterMenu is currently being shown, hide it, and show the menu for the current column
             */
            if(popMenu != null && popMenu.isShowing()) {
                popMenu.hide();
                table.finishEditing();
                popMenu = getMenuForColumn(showingFilterFor);
                popMenu.showRelativeTo(filterButton);
            }
                
        }
    }

    /**
     * Method to determine if the resize cursor or filter button should be shown based on
     * the cursor position in the header 
     * @param x
     */
    private void checkForResizeFilter(int x) {
        int col1, col2;

        col1 = table.getColumnForX(x - 4);
        col2 = table.getColumnForX(x + 4);

        if (col1 != col2 && col1 >= 0) {
            if (table.getColumnAt(col1).isResizable()) {
                flexTable.getCellFormatter().addStyleName(0, col1, "ResizeCol");
                if(col2 > -1)
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
    
    /**
     * This method will create a Menu panel for a table column in order to do 
     * sorting and filtering.
     * @param col
     * @return
     */
    protected PopupMenuPanel getMenuForColumn(final int col) {
        final PopupMenuPanel panel;
        MenuItem  item;
        CheckMenuItem filterItem;        
        final Column column;
        final ArrayList<FilterChoice> choices;
        Filter filter;
               
        panel = new PopupMenuPanel();
        panel.setStyleName("MenuPanel");
        
        column = table.getColumnAt(col);
        /*
         * Set the Sort Options if column is sortable.
         */
        if(column.isSortable()) {
            /*
             * Create Item for Ascending sort.  
             */
            item = new MenuItem("Ascending", "Ascending","");
            item.addCommand(new Command() {
                public void execute() {
                    doSort(col,Table.SORT_ASCENDING);
                    popFilter.hide();
                }
            });
            panel.addItem(item);
            
            /*
             * Create Sort Descending menu Item. 
             */
            item = new MenuItem("Descending", "Descending", "");
            item.addCommand(new Command() {
                public void execute() {
                    doSort(col,Table.SORT_DESCENDING);
                    popFilter.hide();
                }
            });
            panel.addItem(item);
            
            /*
             * if Column is filterable add separator between sort and filter items
             */
            if(column.isFilterable())
                panel.addMenuSeparator();
        }
        
        if(column.isFilterable()) {
            if(column.getFilter() == null) {
                filter = table.new UniqueFilter();
                filter.setColumn(col);
                column.setFilter(filter);
            }
            choices = column.getFilter().getChoices(table.getModel());
            for(final FilterChoice choice : choices) {
                filterItem = new CheckMenuItem(choice.getDisplay(),"",false);
                
                /*
                 * Listen for Filter Value change and set the change in the choices list 
                 * and set the filterSChanged flag to true;
                 */
                filterItem.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    public void onValueChange(ValueChangeEvent<Boolean> event) {
                        choice.setSelected(event.getValue());
                        doFilter(column, choices);
                    } 
                });
                
                /*
                 * If fileter is in force already set the filterMenu to checked.
                 */
                filterItem.setCheck(choice.selected);
                
                panel.addItem(filterItem);
            }
        }
        return panel;
    }
    
    /**
     * This method will apply the currently selected filter for the table
     * @param column
     * @param choices
     */
    private void doFilter(Column column, ArrayList<FilterChoice> choices) {
        
        column.isFiltered = false;
  
        /*
         * Check if all filters were removed or if a new filter was applied
         */
        for(int i = 0; i < choices.size(); i++){
            if(choices.get(i).isSelected()) {
                column.isFiltered = true;
                break;
            }       
        }
        
        /*
         * Reset all columns to not sorted since the filter will remove it
         */
        for(int i = 0; i < table.getColumnCount(); i++)
            table.getColumnAt(i).isSorted = false;
        
        /*
         * Changed to call for all columns so sorts and filter indicators will be synced
         */
        renderView(-1, -1);
        
        table.applyFilters();
    }
    
    /**
     * This method will execute the sort for the column selected on the table
     * @param col
     * @param dir
     */
    private void doSort(int col, int dir) {
        
        table.applySort(col,dir,table.getColumnAt(col).sort);
        
        /*
         * We only sort for one column so remove the isSorted for any column that is currently sorted
         */
        for(int i = 0; i < table.getColumnCount(); i++)
            table.getColumnAt(i).isSorted = false;
        
        /*
         * Set sorted state for column
         */
        table.getColumnAt(col).isSorted = true;
        
        /*
         * 
         * Changed to call for all columns so sorts and filter indicators will be synced
         */
        renderView(-1, -1);
    }

}
