package org.openelis.gwt.client.widget.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.client.services.TableServiceInt;
import org.openelis.gwt.client.services.TableServiceIntAsync;
import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.OptionField;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.TableModel;
import org.openelis.gwt.common.TableRow;

import java.util.ArrayList;

/**
 * This class is the main controller for the Table widget. It hooks the model to
 * the view and also controls when row selection and calls to the manager for
 * permissions and actions set by the a developer.
 * 
 * @author tschmidt
 * 
 */
public class TableController implements
                            TableListener,
                            ChangeListener,
                            EventPreview,
                            ClickListener,
                            MouseListener,
                            SourcesChangeEvents{
    
    public TableModel model;
    public TableView view;
    public TableManager manager;
    public int selected = -1;
    public int selectedCell = -1;
    public int setRow = -1;
    private TableCellWidget[] editors;
    private int[] colwidth;
    private int[] curColWidth;
    private String[] staticTitles;
    private String[] dynamicTitles;
    private boolean[] sortable;
    private boolean[] filterable;
    private ArrayList statFilters = new ArrayList();
    private ArrayList filters = new ArrayList();
    private TableCallback callback = new TableCallback();
    private HasHorizontalAlignment.HorizontalAlignmentConstant[] colAlign;
    private boolean resizing;
    private int startx;
    private int resizeColumn = -1;
    private int tableCol = -1;
    private TableServiceIntAsync tableService = (TableServiceIntAsync)GWT.create(TableServiceInt.class);
    private ServiceDefTarget target = (ServiceDefTarget)tableService;
    private ChangeListenerCollection changeListeners;
    private int start = 0;
    private int end = 0;
    private boolean autoAdd;
    /**
     * This Method will set the url for the TableService.
     * 
     * @param url
     */
    public void initService(String url) {
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }

    /**
     * Default constructor, puts table on top of the event stack.
     * 
     */
    public TableController() {
        view = new TableView();
        model = new TableModel();
        DOM.addEventPreview(this);
    }

    /**
     * Constructor to set predefined model, view and manager.
     * 
     * @param model
     * @param view
     * @param manager
     */
    public TableController(TableModel model,
                           TableView view,
                           TableManager manager) {
        this.model = model;
        this.view = view;
        if (manager != null) {
            this.manager = manager;
        }
        DOM.addEventPreview(this);
        reset();
    }

    /**
     * Method to set model for table. Table will be redrawn when this method is
     * called.
     * 
     * @param model
     */
    public void setModel(TableModel model) {
        this.model = model;
        reset();
    }

    /**
     * This method sets the TableManager that will handle permissions and
     * actions for this table.
     * 
     * @param manager
     */
    public void setManager(TableManager manager) {
        this.manager = manager;
    }

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
     * This method sets the editors definition that table will use
     * 
     * @param editors
     */
    public void setEditors(TableCellWidget[] editors) {
        this.editors = editors;
    }

    /**
     * This method will set the definition that will be used for setting static
     * titles for fields in a table.
     * 
     * @param titles
     */
    public void setStaticTitles(String[] titles) {
        this.staticTitles = titles;
    }

    /**
     * This method will set the definition that will be used for setting dynamic
     * titles for fields in a table.
     * 
     * @param titles
     */
    public void setDynamicTitles(String[] titles) {
        this.dynamicTitles = titles;
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

    public void setAutoAdd(boolean auto){
        this.autoAdd = auto;
    }
    /**
     * This method will add a new row to the model and the table view.
     * 
     */
    public void addRow() {
        adjustScroll();
        model.addRow(null);
        view.table.resizeRows(model.numRows());
        int rowIndex = model.numRows() - 1;
        if (rowIndex > -1) {
            loadRow(rowIndex);
        }
        if(manager != null){
            manager.rowAdded(rowIndex,this);
        }
    }

    public void addRow(TableRow row) {
        adjustScroll();
        model.addRow(row);
        view.table.resizeRows(model.numRows());
        int rowIndex = model.numRows() - 1;
        if (rowIndex > -1) {
            loadRow(rowIndex);
        }
        if(manager != null){
            manager.rowAdded(rowIndex,this);
        }
    }

    /**
     * This method will insert a new row before the specified row in the model
     * and the view.
     * 
     * @param row
     */
    public void insertRow(int index) {
        if (manager == null || (manager != null && manager.canInsert(index,
                                                                     this))) {
            adjustScroll();
            model.insertRow(index, null);
            view.table.resizeRows(model.numRows());
            start = 0;
            end = 0;
            scrollLoad(view.cellView.getScrollPosition());
        }
        if(manager != null){
            manager.rowAdded(index,this);
        }
    }

    public void insertRow(int index, TableRow row) {
        if (manager == null || (manager != null && manager.canInsert(index,
                                                                     this))) {
            adjustScroll();
            model.insertRow(index, row);
            view.table.resizeRows(model.numRows());
            start = 0;
            end = 0;
            scrollLoad(view.cellView.getScrollPosition());
        }
        if(manager != null){
            manager.rowAdded(index,this);
        }
    }

    /**
     * This method loads the row form the model specified by the passed index
     * into the table view.
     * 
     * @param index
     */
    private void loadRow(int index) {
        for (int i = 0; i < model.getRow(0).numColumns(); i++) {
            setCellDisplay(index, i);
            view.table.getCellFormatter().addStyleName(index,
                                                           i,
                                                           view.cellStyle);
            if (colAlign != null && colAlign[i] != null) {
                view.table.getCellFormatter()
                          .setHorizontalAlignment(index, i, colAlign[i]);
            }
            view.table.getCellFormatter().setWidth(index, i, curColWidth[i] + "px");
        }
        view.table.getRowFormatter().addStyleName(index, view.rowStyle);
        //Label rowNum = new Label(String.valueOf(index+1));
        //view.rows.setWidget(index,0,rowNum);
        //view.rows.getFlexCellFormatter().setStyleName(index, 0, "RowNum");
        if(index % 2 == 1){
            view.table.getRowFormatter().addStyleName(index, "AltTableRow");
        }
        if (!model.getRow(index).show())
            view.table.getRowFormatter().addStyleName(index, "hide");
    }

    /**
     * This method will delete the specified row from the model and the view. if
     * row = -1 the currently selected row will be deleted.
     * 
     * @param row
     */
    public void deleteRow(int row) {
        if (row < 0) {
            row = selected;
            selected = -1;
        }
        if (manager == null || (manager != null && manager.canDelete(row, this))) {
            adjustScroll();
            model.deleteRow(row);
            view.table.resizeRows(model.numRows());
            start = 0;
            end = 0;
            scrollLoad(view.cellView.getScrollPosition());
        }

    }

    /**
     * This method handles all click events on the body of the table and the
     * header of the table.
     */
    public void onCellClicked(SourcesTableEvents sender, int row, int col) {
        DOM.removeEventPreview(this);
        DOM.addEventPreview(this);
        if (resizeColumn > -1) {
            resizeColumn = -1;
            return;
        }
        if(selected == row && selectedCell == col)
            return;
        if (sender == view.table) {
            if (manager != null && manager.canSelect(row, this)) {
                if (selected > -1)
                    unselect(selected);
            }
            if (manager == null && selected > -1 && row != selected) {
                unselect(selected);
            }
            select(row, col);
        }
        if (sender == view.header) {
            if (sortable[col / 2] || filterable[col / 2]) {
                Filter[] colFilters = null;
                if (filterable[col / 2])
                    colFilters = setFilter(col);
                if (!model.paged)
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
        if(row < 0 && selected < 0)
            return;
        else
            row = selected;
        view.table.getRowFormatter().removeStyleName(row, view.selectedStyle);
        for (int i = 0; i < view.table.getCellCount(row); i++) {
            if (selectedCell == i) {
                saveValue(row, i);
                if(model.autoAdd || autoAdd){
                    if(manager != null && manager.doAutoAdd(row,i,this))
                        addRow();
                    else if(manager == null && row == model.numRows() -1)
                        addRow();
                }       
            }
            setCellDisplay(row, i);
        }
        selectedCell = -1;
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
        if (manager == null || (manager != null && manager.canSelect(row, this))){
            view.table.getRowFormatter().addStyleName(row, view.selectedStyle);
            for(int i = 0; i < view.table.getCellCount(row); i++){
                if(view.table.getCellFormatter().getStyleName(row,i).indexOf("disabled") > -1){
                    view.table.getWidget(row,i).addStyleName("disabled");
                }
            }
        }
        else
            return;
        //if (row == selected) {
            if (selectedCell != col) {
                if (selectedCell > -1) {
                    saveValue(row, selectedCell);
                    if(model.autoAdd || autoAdd){
                        if(manager != null && manager.doAutoAdd(row,col,this))
                            addRow();
                        else if(manager == null && row == model.numRows() -1)
                            addRow();
                    }
                    setCellDisplay(row, selectedCell);
                }
                if (manager == null || (manager != null && manager.canEdit(row,
                                                                           col,
                                                                           this)))
                    setCellEditor(row, col);
                else
                    selectedCell = -1;
            }
        //}
        selected = row;
        if (manager != null && col > -1)
            manager.action(row, col, this);
        }catch(Exception e){
            Window.alert("select "+e.getMessage());
        }
    }

    /**
     * This method can be overridden to handle on change events
     */
    public void onChange(Widget sender) {
        // TODO Auto-generated method stub
    }

    /**
     * This method will get the correct CellWidget for editing this cell based
     * on the definition of editors in controller.
     * 
     * @param row
     * @param col
     */
    public void setCellEditor(int row, int col) {
        try{
        TableCellWidget cell = editors[col];
        if (cell instanceof TableLabel) {
            selectedCell = -1;
            return;
        }
        Widget wid = null;
        if (cell instanceof TableOption && ((TableOption)cell).loadFromModel) {
            wid = ((TableOption)cell).getEditor((OptionField)model.getFieldAt(row,
                                                                              col));
        } else if (cell instanceof TableOption && ((TableOption)cell).loadFromHidden != null) {
            wid = ((TableOption)cell).getEditor((OptionField)model.hidden.get(((TableOption)cell).loadFromHidden));
            ((TableOption)wid).setValue(model.getFieldAt(row, col).getValue());
        } else {
            cell.setValue(model.getFieldAt(row, col).getValue());
            wid = cell.getEditor();
        }
        wid.addStyleName(view.widgetStyle);
        wid.addStyleName("Enabled");
        if (wid instanceof TableCalendar) {
            wid.setWidth((curColWidth[col] - 18) + "px");
            ((TableCalendar)wid).setEnabled(true);
        }
        if(wid instanceof TableCheck){
            SimplePanel simp = new SimplePanel();
            simp.setWidget(wid);
            simp.setWidth((curColWidth[col] -4)+ "px");
            DOM.setElementProperty(simp.getElement(),"align","center");
            view.table.setWidget(row, col, simp);
        }else{
            wid.setWidth((curColWidth[col] -4)+ "px");
            view.table.setWidget(row, col, wid);
        }
        if (wid instanceof FocusWidget) {
            ((FocusWidget)wid).setFocus(true);
        }
        selectedCell = col;
        }catch(Exception e){
            Window.alert("set Editor "+e.getMessage());
        }
    }

    /**
     * This method retrieves the CellWidget for displaying the data from the
     * model based on the definition in editors of the controller.
     * 
     * @param row
     * @param col
     */
    public void setCellDisplay(int row, int col) {
        try{
        TableCellWidget cell = editors[col];
        if (!(cell instanceof TableOption) || !(((TableOption)cell).loadFromModel || ((TableOption)cell).loadFromHidden != null))
            cell.setValue(model.getFieldAt(row, col).getValue());
                
        Object display;
        if (staticTitles != null && staticTitles[col] != null) {
            if (cell instanceof TableOption && ((TableOption)cell).loadFromModel)
                display = ((TableOption)cell).getDisplay(staticTitles[col],
                                                         (OptionField)model.getFieldAt(row,
                                                                                       col));
            else if (cell instanceof TableOption && ((TableOption)cell).loadFromHidden != null) {
                OptionField field = (OptionField)model.hidden.get(((TableOption)cell).loadFromHidden);
                field.setValue(model.getFieldAt(row, col).getValue());
                display = ((TableOption)cell).getDisplay(staticTitles[col],
                                                         field);
            } else
                display = cell.getDisplay(staticTitles[col]);
        } else if (dynamicTitles != null && dynamicTitles[col] != null) {
            if (cell instanceof TableOption && ((TableOption)cell).loadFromModel)
                display = ((TableOption)cell).getDisplay((String)model.getRow(row)
                                                                      .getHidden(dynamicTitles[col])
                                                                      .getValue(),
                                                         (OptionField)model.getFieldAt(row,
                                                                                       col));
            else if (cell instanceof TableOption && ((TableOption)cell).loadFromHidden != null) {
                OptionField field = (OptionField)model.hidden.get(((TableOption)cell).loadFromHidden);
                field.setValue(model.getFieldAt(row, col).getValue());
                display = ((TableOption)cell).getDisplay((String)model.getRow(row)
                                                                      .getHidden(dynamicTitles[col])
                                                                      .getValue(),
                                                         field);
            } else
                display = cell.getDisplay((String)model.getRow(row)
                                                       .getHidden(dynamicTitles[col])
                                                       .getValue());
        } else {
            if (cell instanceof TableOption && ((TableOption)cell).loadFromModel)
                display = ((TableOption)cell).getDisplay(null,
                                                         (OptionField)model.getFieldAt(row,
                                                                                       col));
            else if (cell instanceof TableOption && ((TableOption)cell).loadFromHidden != null) {
                OptionField field = (OptionField)model.hidden.get(((TableOption)cell).loadFromHidden);
                field.setValue(model.getFieldAt(row, col).getValue());
                display = ((TableOption)cell).getDisplay(null, field);
            } else
                display = cell.getDisplay(null);
        }
        if (display instanceof CheckBox){
            if(manager == null || manager.canEdit(row,col,this)){
                ((CheckBox)display).addClickListener(this);
                ((CheckBox)display).setName(row+":"+col);
            }else{
                ((CheckBox)display).setEnabled(false);
            }
            SimplePanel simp = new SimplePanel();
            simp.setWidget((Widget)display);
            simp.setWidth((curColWidth[col] -4)+ "px");
            DOM.setElementProperty(simp.getElement(),"align","center");
            view.table.setWidget(row, col, simp);
            return;
        }
        ((Widget)display).addStyleName(view.widgetStyle);
        DOM.setStyleAttribute(((Widget)display).getElement(), "overflowX", "hidden");
        ((Widget)display).setWidth((curColWidth[col] -4)+ "px");
        view.table.setWidget(row, col, (Widget)display);
        }catch(Exception e){
            Window.alert("setCell Display "+e.getMessage());
        }
           
     }

    /**
     * This method will clear and redraw the table
     */
    public void reset() {
        view.controller = this;
        if(model.autoAdd || autoAdd){
            model.addRow(null);
        }
        if(model.numRows() > 0){
            view.reset(model.numRows(),model.getRow(0).numColumns());
            view.table.addTableListener(this);
        }
        if (view.header != null) {
            view.header.removeTableListener(this);
            view.header.addTableListener(this);
        }
        for (int i = 0; i < view.header.getCellCount(0); i++) {
            if (i % 2 == 0) {
                ((Image)((HorizontalPanel)((SimplePanel)view.header.getWidget(0, i)).getWidget()).getWidget(1)).removeStyleName("hide");
                if (!sortable[i / 2] && !filterable[i / 2]) {
                    ((Image)((HorizontalPanel)((SimplePanel)view.header.getWidget(0, i)).getWidget()).getWidget(1)).addStyleName("hide");
                }
            } else {
                FocusPanel img = (FocusPanel)view.header.getWidget(0, i);
                img.removeMouseListener(this);
                img.addMouseListener(this);
            }
        }
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                scrollLoad(0);
                selected = -1;
                selectedCell = -1;
                sizeTable();
                if (model.paged)
                    view.setNavPanel(model.pageIndex, model.totalPages, model.showIndex);
            }
        });
    }
    
    public void scrollLoad(int scrollPos){
        try{
        int newStart = 0;
        int newEnd = 0;
        int rowsPer = (view.cellView.getOffsetHeight()/(view.table.getOffsetHeight()/model.numRows()));
        newStart = (scrollPos)/(view.table.getOffsetHeight()/model.numRows()) - rowsPer * 2;
        if(newStart < 0){
            newStart = 0;
        }
        newEnd = (scrollPos)/(view.table.getOffsetHeight()/model.numRows()) + rowsPer  + rowsPer * 2;
        if(newEnd > model.numRows()){
            newEnd = model.numRows();
        }
        if(newEnd < start || newStart > end){
            for(int i = newStart; i < newEnd; i++){
                loadRow(i);
            }
            clearRows(start,end);
        }else{
            if(start < newStart){
                clearRows(start,newStart);
            }
            if(end > newEnd){
                clearRows(newEnd,end);
            }
            int i = 0;
            int stop = 0;
            if(newEnd > end && view.table.getWidget(newEnd -1,0) == null){
                 i = end;
                 stop = newEnd;
            }else{
                i = newStart;
                stop = start;
            }
            for(;i < stop; i++){
                loadRow(i);
            }
        }
        start = newStart;
        end = newEnd;
        }catch(Exception e){
            Window.alert("scrollLoad "+e.getMessage());
        }
        //view.setShown("("+ (scrollPos)/(view.table.getOffsetHeight()/model.numRows()) +":"+start+":"+end);
    }
    
    private void clearRows(final int start, final int stop){
        //DeferredCommand.addCommand(new Command() {
         //   public void execute(){
                if(selected >= start && selected <= stop)
                    unselect(selected);
                for(int i = start; i < stop; i++){
                    for(int j = 0; j < model.getRow(i).numColumns(); j++)
                        view.table.clearCell(i,j);
                }
         // }
       // });
    }

    /**
     * This method will size the table and the columns on the screen.
     * 
     */
    private void sizeTable() {
            if (model.numRows() == 0){
                int width = 0;
                for(int i = 0; i < curColWidth.length; i++){
                    width += curColWidth[i];
                }
                view.table.setWidth(width+"px");
                view.table.setHeight("17px");
            }
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    for(int i = 0; i < curColWidth.length; i++){
                        if( i > 0){
                            view.header.getFlexCellFormatter().setWidth(0, i*2,(curColWidth[i]-2)+"px");
                            view.header.getWidget(0,i*2).setWidth((curColWidth[i]-10)+"px");
                        }else{
                            view.header.getFlexCellFormatter().setWidth(0, i*2,(curColWidth[i])+"px");
                            view.header.getWidget(0,i*2).setWidth((curColWidth[i]-6)+"px");
                        }
                            
                    }
                    if(view.width.equals("auto") && view.table.getOffsetWidth() > 0){
                        view.cellView.setWidth((view.table.getOffsetWidth()+17)+"px");
                        view.headerView.setWidth(view.table.getOffsetWidth()+"px");
                    }else if(view.cellView.getOffsetHeight()-17 < view.table.getOffsetHeight() && view.table.getOffsetHeight() > 0){
                    	final int width = view.headerView.getOffsetWidth();
                    	DeferredCommand.addCommand(new Command() {
                    		public void execute() {
                    			view.headerView.setWidth((width-17)+"px");
                    		}
                    	});
                    }
                }
            });
    }
    
    public void adjustScroll() {
        if(!view.width.equals("auto") && view.cellView.getOffsetHeight()-17 > view.table.getOffsetHeight() ){
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    if(view.cellView.getOffsetHeight()-17 < view.table.getOffsetHeight()){
                        final int width = view.headerView.getOffsetWidth();
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                view.headerView.setWidth((width-17)+"px");
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * This method will pull the value from the cell editor and put it in the
     * model.
     * 
     * @param row
     * @param col
     */
    public void saveValue(int row, int col) {
        try{
    	TableCellWidget wid = null;
        if (view.table.getWidget(row,col) instanceof SimplePanel){
        	wid = (TableCellWidget)((SimplePanel)view.table.getWidget(row,col)).getWidget();
        }else{
        	wid = (TableCellWidget)view.table.getWidget(row, col);
        }
        if (wid instanceof TableLabel)
            return;
        if (wid instanceof TableMaskedTextBox)
            ((TableMaskedTextBox)wid).format();

        model.getFieldAt(row, col).setValue(wid.getValue());
        if (manager != null) {
            manager.finishedEditing(row, col, this);
        }
        if(changeListeners != null){
            changeListeners.fireChange(view);
        }
        }catch(Exception e){
            Window.alert("save Value "+e.getMessage());
        }
    }

    /**
     * This method will handle the keyboard events caught by the onEventPreview
     * method.
     * 
     * @param event
     * @return
     */
    public boolean onKeyPress(Event event) {
        int code = DOM.eventGetKeyCode(event);
        boolean shift = DOM.eventGetShiftKey(event);
        if (KeyboardListener.KEY_DOWN == code) {
            if (selected >= 0 && selected != view.table.getRowCount() - 1) {
                final int row = selected + 1;
                final int col = selectedCell;
                DeferredCommand.addCommand(new Command() {
                   public void execute() {
                      onCellClicked(view.table, row, col);
                   }
                 });
            }
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
            return false;
        }
        if (KeyboardListener.KEY_UP == code) {
            if (selected >= 0 && selected != 0) {
                final int row = selected - 1;
                final int col = selectedCell;
                //unselect(selected);
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        onCellClicked(view.table, row, col);
                    }
                });
            }
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
            return false;
        }
        if (KeyboardListener.KEY_ENTER == code) {
            if (selected >= 0) {
                if (selectedCell > -1) {
                    saveValue(selected, selectedCell);
                    setCellDisplay(selected, selectedCell);
                    if(model.autoAdd || autoAdd){
                        if(manager != null && manager.doAutoAdd(selected,selectedCell,this))
                            addRow();
                        else if(manager == null && selected == model.numRows() -1)
                            addRow();
                    }
                    selectedCell = -1;
                } else {
                    if (manager != null)
                        return manager.action(selected, -1, this);
                }
            }
        }
        if (KeyboardListener.KEY_RIGHT == code && model.paged) {
            if (model.pageIndex != model.totalPages - 1)
                getPage(++model.pageIndex, -1);
        }
        if (KeyboardListener.KEY_LEFT == code && model.paged) {
            if (model.pageIndex != 0)
                getPage(--model.pageIndex, -1);
        }
        if (KeyboardListener.KEY_TAB == code && selectedCell > -1 && !shift) {
            if (selectedCell + 1 >= model.getRow(selected).numColumns()) {
                int row = selected + 1;
                int col = 0;
                if (row == view.table.getRowCount())
                    row = 0;
                while ((editors[col] instanceof TableLabel))
                    col++;
                final int fRow = row;
                final int fCol = col;
                onCellClicked(view.table, fRow, fCol);
                onCellClicked(view.table,fRow,fCol);
            } else {
                int col = selectedCell + 1;
                while ((editors[col] instanceof TableLabel))
                    col++;
                final int fCol = col;
                onCellClicked(view.table, selected, fCol);
            }
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
            return false;
        }
        if (KeyboardListener.KEY_TAB == code && selectedCell > -1 && shift) {
            if (selectedCell - 1 < 0) {
                int row = selected - 1;
                if (row < 0)
                    row = model.numRows() - 1;
                int col = model.getRow(row).numColumns() - 1;
                while ((editors[col] instanceof TableLabel))
                    col--;
                final int fRow = row;
                final int fCol = col;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        onCellClicked(view.table, fRow, fCol);
                    }
                });
            } else {
                int col = selectedCell - 1;
                while ((editors[col] instanceof TableLabel))
                    col--;
                final int fCol = col;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        onCellClicked(view.table, selected, fCol);
                    }
                });
            }
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
            return false;
        }
        return true;
    }

    /**
     * EventPreview for catching Keyboard events for the table.
     */
    public boolean onEventPreview(Event event) {
        // TODO Auto-generated method stub
        if (view.table.isAttached()) {
            if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                return onKeyPress(event);
            }
        }
        return true;
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
        if (!model.paged) {
            model.sort(col, down);
            reset();
            return;
        }
        try {
            tableService.sort(col, down, model.pageIndex, selected, callback);
        } catch (Exception e) {
            Window.alert(e.getMessage());
        }
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
        if (!model.paged) {
            model.applyFilters(filters);
            reset();
            return;
        }
        try {
            tableService.filter(col,
                                filter,
                                model.pageIndex,
                                selected,
                                callback);
        } catch (RPCException e) {
            Window.alert(e.getMessage());
        }
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
        if (!model.paged) {
            Filter[] filter = model.getFilterValues(col / 2);
            if (filters.get(col / 2) != null) {
                Filter[] filtered = (Filter[])filters.get(col / 2);
                for (int j = 0; j < filter.length; j++) {
                    for (int k = 0; k < filtered.length; k++) {
                        if (filter[j].value.equals(filtered[k].value)) {
                            filter[j].filtered = filtered[k].filtered;
                            k = filtered.length;
                        }
                    }
                }
            }
            filters.set(col / 2, filter);
            return filter;
        }
        final int colF = col;
        tableService.getFilter(col / 2, new AsyncCallback() {
            public void onSuccess(Object result) {
                showMenu(1, colF, (Filter[])result);
            }

            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
        });
        return null;
    }

    /**
     * This method will call back to the server to retrieve the selected page.
     * 
     * @param page
     * @param selected
     */
    public void getPage(int page, int selected) {
        try {
            tableService.getPage(page, selected, callback);
        } catch (RPCException e) {
        }
    }

    /**
     * This method will call back to the server to sort a paged model.
     * 
     * @param col
     * @param down
     */
    public void setSort(int col, boolean down) {
        try {
            tableService.sort(col, down, model.pageIndex, selected, callback);
        } catch (RPCException e) {
        }
    }

    /**
     * This method will call back to the server to retrieve the model initially.
     * If the model is paged it will be page 1, otherwise it will be the entire
     * model.
     */
    public void getModel() {
        tableService.getModel(model, callback);
    }
    
    public void getModel(int setRow){
        this.setRow = setRow;
        getModel();
    }

    /**
     * This method will send the model back to the server to be persisted.
     * 
     */
    public void saveModel() {
        if(selected > -1){
            unselect(selected);
        }
        tableService.saveModel(model, callback);
    }

    /**
     * This method is catches click events on page index for paged tables.
     */
    public void onClick(Widget sender) {
        if(sender instanceof CheckBox){
            String cell[] = ((CheckBox)sender).getName().split(":");
            int row = Integer.parseInt(cell[0]);
            int col = Integer.parseInt(cell[1]);
            saveValue(row,col);
            if (selected > -1 && row != selected) {
                unselect(selected);
            }
            select(row,col);
            return;
        }
        HTML nav = (HTML)sender;
        String htmlString = nav.getHTML();
        int start = htmlString.indexOf("value=\"") + 7;
        int end = htmlString.indexOf("\"", start);
        String page = htmlString.substring(start, end);
        if (page.equals("-1"))
            getPage(--model.pageIndex, -1);
        else if (page.equals("+1"))
            getPage(++model.pageIndex, -1);
        else
            getPage(Integer.parseInt(page), -1);
    }

    /**
     * Private callback class for retrieving model from the server.
     * 
     * @author tschmidt
     * 
     */
    private class TableCallback implements AsyncCallback {
        public void onSuccess(Object result) {
            if (result != null) {
                model = (TableModel)result;
                reset();
                if(setRow > -1)
                    select(setRow,0);
            }
        }

        public void onFailure(Throwable caught) {
            Window.alert("Failed Table :" + caught.toString());
        }
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        if (sender instanceof FocusPanel) {
            resizing = true;
            startx = x;
            for (int i = 0; i < view.header.getCellCount(0); i++) {
                if (sender == view.header.getWidget(0, i)) {
                    resizeColumn = i - 1;
                    tableCol = resizeColumn / 2;
                    i = view.header.getCellCount(0);
                }
            }
            DOM.setCapture(sender.getElement());
            ((FocusPanel)sender).setFocus(false);
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
        if (resizing) {
            int colA = curColWidth[tableCol] + (x - startx);
            int colB = curColWidth[(tableCol)+1] - (x - startx);
            if(colA <= 16 || colB <= 16) 
                return;
            curColWidth[tableCol] = colA;
            curColWidth[(tableCol)+1] = colB;
            if( resizeColumn == 0 ){
                view.header.getFlexCellFormatter().setWidth(0, resizeColumn,(curColWidth[tableCol])+"px");
                view.header.getWidget(0,resizeColumn).setWidth((curColWidth[tableCol]-6)+"px");
                view.header.getFlexCellFormatter().setWidth(0, resizeColumn+2,(curColWidth[(tableCol)+1] -2)+"px");
                view.header.getWidget(0,resizeColumn+2).setWidth((curColWidth[(tableCol)+1]-10)+"px");
            }else{
                view.header.getFlexCellFormatter().setWidth(0, resizeColumn,(curColWidth[tableCol] -2)+"px");
                view.header.getWidget(0,resizeColumn).setWidth((curColWidth[tableCol]-10)+"px");
                view.header.getFlexCellFormatter().setWidth(0, resizeColumn+2,(curColWidth[(tableCol)+1] -2)+"px");
                view.header.getWidget(0,resizeColumn+2).setWidth((curColWidth[(tableCol)+1]-10)+"px");
            }                    
        }
    }

    /**
     * Catches mouses Events for resizing columns.
     */
    public void onMouseUp(Widget sender, int x, int y) {
        if (resizing) {
            DOM.releaseCapture(sender.getElement());
            resizing = false;
            for (int j = 0; j < model.numRows(); j++) {
                for (int i = 0; i < curColWidth.length; i++) {
                    if (curColWidth[i] > 0) {
                        view.table.getCellFormatter()
                                  .setWidth(j, i, curColWidth[i] + "px");
                        view.table.getWidget(j, i).setWidth((curColWidth[i] -4) + "px");
                       
                    }
                }
            }
        }
    }

    public void addChangeListener(ChangeListener listener) {
        if(changeListeners == null)
            changeListeners = new ChangeListenerCollection(); 
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null)
            changeListeners.remove(listener);
    }
}
