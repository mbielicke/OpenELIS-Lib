package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.TableModel;
import org.openelis.gwt.screen.AppScreen;

import java.util.ArrayList;

/**
 * This class is the main controller for the Table widget. It hooks the model to
 * the view and also controls when row selection and calls to the manager for
 * permissions and actions set by the a developer.
 * 
 * @author tschmidt
 * 
 */
public abstract class TableController extends Composite implements
                            TableListener,
                            KeyboardListener,
                            ClickListener,
                            MouseListener,
                            ChangeListener{
    
    public TableView view;
    public TableModel model;
    public int selectedRow = -1;
    public int selectedCell = -1;
    public int[] colwidth;
    public int[] curColWidth;
    public boolean[] sortable;
    public boolean[] filterable;
    public boolean[] colFixed;
    protected ArrayList statFilters = new ArrayList();
    protected ArrayList filters = new ArrayList();
    public HasHorizontalAlignment.HorizontalAlignmentConstant[] colAlign;
    protected boolean resizing;
    protected int startx;
    protected int resizeColumn1 = -1;
    protected int tableCol1 = -1;
    protected int resizeColumn2 = -1;
    protected int tableCol2 = -1;
    public int start = 0;
    public boolean showRows;
    public int maxRows;
    public int cellHeight = 18;
    public int cellSpacing = 1;
    public boolean showScrolls;
    public boolean active;

    /**
     * This method will set the view for this table.
     * 
     * @param view
     */
    public void setView(TableView view) {
        this.view = view;
        view.table.addTableListener(this);
    }

    /**
     * This method will set the initial and minimum widths for columns in a
     * table. A width of -1 relates to auto.
     * 
     * @param widths
     */
    public void setColWidths(int[] widths) {
        colwidth = widths;
        curColWidth = new int[widths.length];
        colFixed = new boolean[widths.length];
        for (int i = 0; i < colwidth.length; i++) {
            curColWidth[i] = colwidth[i];
        }
    }

    /**
     * This method sets the definition for which columns can be sorted in the
     * table.
     * 
     * @param sortable
     */
    public void setSortable(boolean[] sortable) {
        this.sortable = sortable;
    }
    
    public void setColFixed(boolean[] fixed){
        this.colFixed = fixed;
    }

    /**
     * This method sets the definition for which columns can be filtered in the
     * table.
     * 
     * @param filterable
     */
    public void setFilterable(boolean[] filterable) {
        this.filterable = filterable;
        for (int i = 0; i < filterable.length; i++) {
            filters.add(null);
        }
    }

    /**
     * This method will set the definition for which columns contain static
     * filter values.
     * 
     * @param filters
     */
    public void setStatFilterable(ArrayList filters) {
        this.statFilters = filters;
    }

    /**
     * This method will set the definition for the alignment of feilds in the
     * cells of the table. The default alignment is left.
     * 
     * @param align
     */
    public void setColAlign(HasHorizontalAlignment.HorizontalAlignmentConstant[] align) {
        colAlign = align;
    }
    
    public void setMaxRows(int rows){
        maxRows = rows;
        view.setHeight((rows*cellHeight+(rows*cellSpacing)+cellSpacing));
    }
    
    public void setCellHeight(int height){
        cellHeight = height;
    }
    
    public void setShowRows(boolean showRows){
        this.showRows = showRows;
    }
    
    public void setShowScroll(boolean showScrolls){
        this.showScrolls = showScrolls;
        if(showScrolls){
            view.scrollBar.setAlwaysShowScrollBars(true);
            DOM.setStyleAttribute(view.scrollBar.getElement(), "display", "block");
        }
    }

    /**
     * This method handles all click events on the body of the table and the
     * header of the table.
     */
    public void onCellClicked(SourcesTableEvents sender, int row, int col) {
        if (resizeColumn1 > -1) {
            resizeColumn1 = -1;
            return;
        }
        active = true;
        if(selectedRow == row && selectedCell == col)
            return;
        if (sender == view.table) {
            select(row, col);
        }
        if (sender == view.header) {
            if (sortable[col / 2] || filterable[col / 2]) {
                Filter[] colFilters = null;
                if (filterable[col / 2])
                    colFilters = setFilter(col);
                showMenu(row, col, colFilters);
            }
        }
    }

    /**
     * This method will make available the sort and filter options for the
     * column in the table.
     * 
     * @param row
     * @param col
     * @param colFilters
     */
    private void showMenu(int row, int col, Filter[] colFilters) {
        HeaderMenu menu = new HeaderMenu(col / 2,
                                         sortable[col / 2],
                                         colFilters,
                                         this);
        int left = DOM.getAbsoluteLeft(view.header.getFlexCellFormatter()
                                                  .getElement(row, col));
        int top = DOM.getAbsoluteTop(view.header.getFlexCellFormatter()
                                                .getElement(row, col)) + DOM.getIntAttribute(view.header.getFlexCellFormatter()
                                                                                                        .getElement(row,
                                                                                                                    col),
                                                                                             "offsetHeight");
        menu.setPopupPosition(left, top);
        int width = DOM.getIntAttribute(view.header.getFlexCellFormatter()
                                                   .getElement(row, col),
                                        "offsetWidth");
        if (width < 150)
            width = 150;
        menu.setWidth(width + "px");
        menu.show();
    }

    /**
     * This method will unselect the row specified. Unselecting will save any
     * datat that has been changed in the row to the model.
     * 
     * @param row
     */
    public void unselect(int row) {
        try{
        if(row < 0 || selectedRow < 0)
            return;
        else
            row = selectedRow;
        view.table.getRowFormatter().removeStyleName(row, view.selectedStyle);
        selectedCell = -1;
        selectedRow = -1;
        }catch(Exception e){
            Window.alert("unselect "+e.getMessage());
        }
    }

    /**
     * This method will cause the table row passed to be selected. If the row is
     * already selected, the column clicked will be opened for editing if the
     * cell is editable and the user has the correct permissions.
     * 
     * @param row
     * @param col
     */
    public void select(int row, int col) {
        try{
            view.table.getRowFormatter().addStyleName(row, view.selectedStyle);
            for(int i = 0; i < view.table.getCellCount(row); i++){
                if(view.table.getCellFormatter().getStyleName(row,i).indexOf("disabled") > -1){
                    view.table.getWidget(row,i).addStyleName("disabled");
                }
            }
            selectedRow = row;
        }catch(Exception e){
            Window.alert("select "+e.getMessage());
        }
    }
   
    /**
     * Method added to handle selection from Model callbacks.
     * @param row
     */
    public void select(int row) {
        try{
            if(selectedRow > -1){
            	view.table.getRowFormatter().removeStyleName(selectedRow, TableView.selectedStyle);
            	unselect(selectedRow);                
            }    
            view.table.getRowFormatter().addStyleName(row, TableView.selectedStyle);
            for(int i = 0; i < view.table.getCellCount(row); i++){
                if(view.table.getCellFormatter().getStyleName(row,i).indexOf("disabled") > -1){
                    view.table.getWidget(row,i).addStyleName("disabled");
                }
            }
            selectedCell = -1;
            selectedRow = row;
        }catch(Exception e){
            Window.alert("select "+e.getMessage());
        }
    }
    
    /**
     * This method will clear and redraw the table
     */
    public void reset() {
        start = 0;
        view.cellView.setScrollPosition(0);
        if(selectedRow > -1)
            view.table.getRowFormatter().removeStyleName(selectedRow, view.selectedStyle);
        selectedRow = -1;
        selectedCell = -1;
        view.controller = this;
        view.cellView.setWidget(view.table);
    }
    
    public abstract void scrollLoad(int scrollPos);

    /**
     * This method will size the table and the columns on the screen.
     * 
     */
    public void sizeTable() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                int width = 0;
                for(int i = 0; i < curColWidth.length; i++){
                    width += curColWidth[i];
                }
                int displayWidth = width + (curColWidth.length*4) - (curColWidth.length -1);
                if(view.headers != null){
                    view.header.setWidth(displayWidth+"px");  
                    for(int i = 0; i < curColWidth.length; i++){
                        if( i > 0 && i < curColWidth.length - 1){
                            view.header.getFlexCellFormatter().setWidth(0, i*2,(curColWidth[i]-4)+"px");
                        }else{
                            view.header.getFlexCellFormatter().setWidth(0, i*2,(curColWidth[i]-1)+"px");
                        }
                    }
                }           
                view.header.setWidth(displayWidth+"px");
                view.cellView.setScrollWidth(displayWidth+"px");
                if(showRows){
                    view.rowsView.setHeight((view.cellView.getOffsetHeight()-17)+"px");
                }
                view.titlePanel.setWidth(displayWidth+"px");
            }
        });
    }

    /**
     * Method for calling the Sort method in the model for the column selected.
     * 
     * @param col
     * @param down
     */
    public void sort(int col, boolean down) {
        Image img = (Image)((HorizontalPanel)view.header.getWidget(1, col)).getWidget(1);
        if (down)
            img.setUrl("Images/go-down.png");
        else
            img.setUrl("Images/go-up.png");
    }

    /**
     * This method will call the filter method for the column selected.
     * 
     * @param col
     * @param filter
     */
    public void filter(int col, Filter[] filter) {
        filters.set(col, filter);
        Image img = (Image)((HorizontalPanel)((SimplePanel)view.header.getWidget(0, col * 2)).getWidget()).getWidget(1);
        if (!filter[0].filtered)
            img.setUrl("Images/apply.png");
        else
            img.setUrl("Images/unapply.png");
    }

    /**
     * This method will set the filters to be displayed in the menu for the
     * column selected.
     * 
     * @param col
     * @return
     */
    public Filter[] setFilter(int col) {
        if (statFilters.size() > col / 2 && statFilters.get(col / 2) != null) {
            if (filters.get(col / 2) == null)
                filters.set(col / 2, statFilters.get(col / 2));
            return (Filter[])filters.get(col / 2);
        }
        return null;
    }

    /**
     * This method is catches click events on page index for paged tables.
     */
    public void onClick(Widget sender) {
        if(view.table.isAttached()){
            if(sender instanceof AppScreen){
                if(active && !DOM.isOrHasChild(view.getElement(), ((AppScreen)sender).clickTarget)){
                    active = false;
                    switchSelectedRow();
                }
                return;
            }
            if(sender instanceof HTML){
                HTML nav = (HTML)sender;
                String styleNames = nav.getStyleName();
                String htmlString = nav.getHTML();
                String page = "";
        
                int start = htmlString.indexOf("value=\"") + 7;
                int end = htmlString.indexOf("\"", start);
        
                if(start > 6)
                    page = htmlString.substring(start, end);
                return;
            }
        }
 
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        if(DOM.isOrHasChild(view.header.getElement(), sender.getElement())){
            resizing = true;
            startx = x;
            for (int i = 0; i < view.header.getCellCount(0); i++) {
                if (sender == view.header.getWidget(0, i)) {
                    resizeColumn1 = i - 1;
                    tableCol1 = resizeColumn1 / 2;
                    if(colFixed[tableCol1] && colFixed[tableCol1+1]){
                        resizing = false;
                        resizeColumn1 = -1;
                        tableCol1 = -1;
                        tableCol2 = -1;
                        return;
                    }  
                    if(colFixed[tableCol1]){
                        tableCol1 = -1;
                        int j = tableCol1 -1; 
                        while(tableCol1 < 0 && j > -1){
                            if(!colFixed[tableCol1])
                                tableCol1 = j;
                            j--;
                        }
                    }
                    if(tableCol1 < 0){
                        int j = tableCol1 +1;
                        while(tableCol1 < 0 && j < colFixed.length){
                            if(!colFixed[j])
                                tableCol1 = j;
                            j++;
                        }
                    }
                }
            }
            if(tableCol1 < 0){
                resizing = false;
                resizeColumn1 = -1;
                tableCol1 = -1;
                tableCol2 = -1;
                return;
            }
            tableCol2 = -1;
            int i = tableCol1 +1;
            while(tableCol2 < 0 && i < colFixed.length){
                if(!colFixed[i])
                    tableCol2 = i;
                i++;
            }
            if(tableCol2 < 0){
                i = 0;
                while(tableCol2 < 0 && i < tableCol1){
                    if(!colFixed[i])
                        tableCol2 = i;
                    i++;
                }
            }
            if(tableCol2 < 0){
                resizing = false;
                resizeColumn1 = -1;
                tableCol1 = -1;
                tableCol2 = -1;
                return;
            }
            FocusPanel bar = new FocusPanel();
            bar.addMouseListener(this);
            bar.setHeight((view.table.getOffsetHeight()+17)+"px");
            bar.setWidth("1px");
            DOM.setStyleAttribute(bar.getElement(), "background", "red");
            DOM.setStyleAttribute(bar.getElement(), "position", "absolute");
            DOM.setStyleAttribute(bar.getElement(),"left",sender.getAbsoluteLeft()+"px");
            DOM.setStyleAttribute(bar.getElement(),"top",sender.getAbsoluteTop()+"px");
            RootPanel.get().add(bar);   
            DOM.setCapture(bar.getElement());
        }
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseLeave(Widget sender) {

    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseMove(Widget sender, int x, int y) {
            if(resizing) {
                int colA = curColWidth[tableCol1] + (x - startx);
                int colB = curColWidth[tableCol2] - (x - startx);
                if(colA <= 16 || colB <= 16) 
                    return;
                curColWidth[tableCol1] = colA;
                curColWidth[tableCol2] = colB;
                DOM.setStyleAttribute(sender.getElement(),"left",(DOM.getAbsoluteLeft(sender.getElement())+(x-startx))+"px");
            }
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseUp(Widget sender, int x, int y) {
            if (resizing) {
                DOM.releaseCapture(sender.getElement());
                RootPanel.get().remove(sender);
                resizing = false;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        for(int i = 0; i < curColWidth.length; i++){
                            if( i > 0 && i < curColWidth.length - 1){
                                view.header.getFlexCellFormatter().setWidth(0, i*2,(curColWidth[i]-4)+"px");
                                view.header.getWidget(0,i*2).setWidth((curColWidth[i]-4)+"px");
                            }else{
                                view.header.getFlexCellFormatter().setWidth(0, i*2,(curColWidth[i]-1)+"px");
                                view.header.getWidget(0,i*2).setWidth((curColWidth[i]-1)+"px");
                            }   
                        }
                        for (int j = 0; j < view.table.getRowCount(); j++) {
                            for (int i = 0; i < curColWidth.length; i++) {
                                view.table.getFlexCellFormatter().setWidth(j, i, curColWidth[i] +  "px");
                                ((TableCellWidget)view.table.getWidget(j,i)).setCellWidth(curColWidth[i]);
                                ((SimplePanel)view.table.getWidget(j, i)).setWidth((curColWidth[i]) + "px");
                                //((SimplePanel)view.table.getWidget(j,i)).getWidget().setWidth(curColWidth[i]+"px");
                            }
                        }
                    }
                });
            }
    }
    
    /**
     * EventPreview for catching Keyboard events for the table.
     */
    public boolean onEventPreview(Event event) {
        // TODO Auto-generated method stub
        if (view.table.isAttached()) {
            if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                
            }
            if (DOM.eventGetType(event) == Event.ONCLICK){
                if(!DOM.isOrHasChild(view.getElement(), DOM.eventGetTarget(event))){
                  
                   switchSelectedRow();
                }
            }
        }
        return true;
    }
   
    public void onChange(Widget sender){
        
    }
    
    public void switchSelectedRow(){
        
    }
    
}
