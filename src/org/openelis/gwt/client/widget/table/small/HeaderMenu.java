package org.openelis.gwt.client.widget.table.small;

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

import org.openelis.gwt.common.Filter;

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
    public boolean sortDirection;
    private String widget;
    private boolean doFilter;
    private TableController controller;

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
                      TableController controller) {
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
                String theText = filter[i].value;
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
            controller.filter(col, filter);
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
                sortDirection = false;
            if (row == 1)
                sortDirection = true;
            controller.sort(col, sortDirection);
            hide();
        }
    }
}
