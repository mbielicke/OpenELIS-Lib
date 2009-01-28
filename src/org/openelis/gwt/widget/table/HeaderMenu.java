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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.DataSorterInt;
import org.openelis.gwt.common.Filter;

import java.util.ArrayList;

/**
 * This class displays the PopupMenu for the Table Header columns Displays
 * options for sorting and filtering the table.
 * 
 * @author tschmidt
 * 
 */
public class HeaderMenu extends PopupPanel implements
                                          PopupListener,
                                          MouseListener,
                                          TableListener {
    private Grid sortMenu = new Grid(2, 2);
    private FlexTable filterMenu = new FlexTable();
    private VerticalPanel menuPanel = new VerticalPanel();
    public int col;
    public Filter[] filter;
    public DataSorterInt.SortDirection sortDirection;
    private String widget;
    private boolean doFilter;
    private TableWidget controller;

    /**
     * Constructor called from TableController.
     * 
     * @param col
     * @param sort
     * @param filter
     * @param controller
     */
    public HeaderMenu(int col,
                      boolean sort,
                      Filter[] filter,
                      TableWidget controller) {
        super(true);
        addPopupListener(this);
        this.col = col;
        this.filter = filter;
        this.controller = controller;
        if (sort) {
            Image sortUp = new Image("Images/go-up.png");
            Image sortDown = new Image("Images/go-down.png");
            sortMenu.setWidget(0, 0, sortUp);
            HTML label0 = new HTML("&nbsp;&nbsp;Sort Up");
            label0.addMouseListener(this);
            label0.setWidth("100%");
            sortMenu.setWidget(0, 1, label0);
            sortMenu.setWidget(1, 0, sortDown);
            HTML label1 = new HTML("&nbsp;&nbsp;Sort Down");
            label1.addMouseListener(this);
            label1.setWidth("100%");
            sortMenu.setWidget(1, 1, label1);
            sortMenu.addTableListener(this);
            sortMenu.setWidth("95%");
            menuPanel.add(sortMenu);
        }
        if (sort && filter != null)
            menuPanel.add(new HTML("<hr/>"));
        if (filter != null) {
            for (int i = 0; i < filter.length; i++) {
                CheckBox check = new CheckBox();
                filterMenu.setWidget(i, 0, check);
                check.setChecked(filter[i].filtered);
                String theText = filter[i].obj.getValue().toString();
                if (filter[i].display != null)
                    theText = filter[i].display;
                check.setText(theText);
            }
            filterMenu.setWidth("98%");
            filterMenu.addTableListener(this);
            menuPanel.add(filterMenu);
        }
        menuPanel.setWidth("100%");
        add(menuPanel);
        setStyleName("menuDiv");
    }
    

    /**
     * Cathes menu closures and decides if action needs to be taken.
     */
    public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        if (doFilter) {
            ((TableColumnInt)controller.columns.get(col)).setFilter(filter);
            for(TableColumnInt column : (ArrayList<TableColumnInt>)controller.columns){
                column.applyFilter();
            }
            controller.model.refresh();
        }
    }

    /**
     * Catches Mouse Events
     */
    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
    }

    /**
     * Catches Mouse events
     */
    public void onMouseEnter(Widget sender) {
        sender.addStyleName("hover");
    }

    /**
     * Catches mouse Events
     */
    public void onMouseLeave(Widget sender) {
        sender.removeStyleName("hover");
    }

    /**
     * Catches mouse events
     */
    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
    }

    /**
     * Catches Mouse events
     */
    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
    }

    /**
     * Catches click events in menu and sets options.
     */
    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        if (sender == filterMenu) {
            doFilter = true;
            filter[row].filtered = !filter[row].filtered;
            if (filter[row].filtered) {
                ((CheckBox)filterMenu.getWidget(row, 0)).setChecked(true);
                if (row != 0 && filter[0].filtered) {
                    ((CheckBox)filterMenu.getWidget(0, 0)).setChecked(false);
                    filter[0].filtered = false;
                }
            } else {
                ((CheckBox)filterMenu.getWidget(row, 0)).setChecked(false);
            }
            if (row == 0) {
                for (int i = 1; i < filter.length; i++) {
                    if (filter[i].filtered) {
                        filter[i].filtered = false;
                        ((CheckBox)filterMenu.getWidget(i, 0)).setChecked(false);
                    }
                }
                filter[0].filtered = true;
                ((CheckBox)filterMenu.getWidget(0, 0)).setChecked(true);
            }
        } else {
            if (row == 0)
                sortDirection = DataSorterInt.SortDirection.UP;
            if (row == 1)
                sortDirection = DataSorterInt.SortDirection.DOWN;
            controller.model.sort(col, sortDirection);
            hide();
        }
    }
}
