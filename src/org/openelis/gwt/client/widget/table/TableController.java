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
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.client.services.TableServiceInt;
import org.openelis.gwt.client.services.TableServiceIntAsync;
import org.openelis.gwt.client.widget.OptionList;
import org.openelis.gwt.client.widget.table.TableCellWidget;
import org.openelis.gwt.client.widget.table.TableCheck;
import org.openelis.gwt.client.widget.table.TableLabel;
import org.openelis.gwt.client.widget.table.TableOption;
import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataModelWidget;
import org.openelis.gwt.common.data.OptionField;
import org.openelis.gwt.common.data.TableModel;
import org.openelis.gwt.common.data.TableRow;

import java.util.ArrayList;
import java.util.Iterator;

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
    public TableCellWidget[] editors;
    public int[] colwidth;
    public int[] curColWidth;
    public boolean[] sortable;
    public boolean[] filterable;
    private ArrayList statFilters = new ArrayList();
    private ArrayList filters = new ArrayList();
    public TableCallback callback = new TableCallback();
    public HasHorizontalAlignment.HorizontalAlignmentConstant[] colAlign;
    private boolean resizing;
    private int startx;
    private int resizeColumn = -1;
    private int tableCol = -1;
    public TableServiceIntAsync tableService = (TableServiceIntAsync)GWT.create(TableServiceInt.class);
    private ServiceDefTarget target = (ServiceDefTarget)tableService;
    private ChangeListenerCollection changeListeners;
    private int start = 0;
    private boolean autoAdd;
    public boolean showRows;
    public boolean modelSet;
    public int maxRows;
    public int cellHeight = 18;
    public int cellSpacing = 1;
    public boolean enabled;
    
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
        //DOM.addEventPreview(this);
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
        this.view.table.setVisible(false);
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
    
    public void loadModel(TableModel model){
        this.model = model;
        load();
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
        for(int i = 0; i < editors.length; i++){
            if(editors[i] instanceof TableOption){
                ((TableOption)editors[i]).setListener(this);
            }
        }
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
    
    public boolean getAutoAdd() {
        return autoAdd;
    }
    
    public void setMaxRows(int rows){
        maxRows = rows;
        view.setHeight((rows*cellHeight+(rows*cellSpacing)+cellSpacing)+"px");
    }
    
    public void setCellHeight(int height){
        cellHeight = height;
    }
    
    public void setShowRows(boolean showRows){
        this.showRows = showRows;
    }
    /**
     * This method will add a new row to the model and the table view.
     * 
     */
    public void addRow() {
        model.addRow(null);
        if(view.table.getRowCount() < maxRows){
            createRow(model.numRows()-1);
            loadRow(model.numRows()-1);
        }else{
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    int sel = selected;
                    int selCell = selectedCell;
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                    select(sel,selCell);
                }
            });
        }
        view.setScrollHeight((model.numRows()*cellHeight)+(maxRows*cellSpacing)+cellSpacing);
        if(manager != null){
            manager.rowAdded(model.numRows()-1,this);
        }
    }

    public void addRow(TableRow row) {
        model.addRow(row);
        if(view.table.getRowCount() < maxRows){
            createRow(model.numRows()-1);
            loadRow(model.numRows()-1);
        }else{
            view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
        }
        view.setScrollHeight((model.numRows()*cellHeight)+(maxRows*cellSpacing)+cellSpacing);
        if(manager != null){
            manager.rowAdded(model.numRows()-1,this);
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
          
            model.insertRow(start+index, null);
            if(view.table.getRowCount() != maxRows){
                createRow(model.numRows() -1);
                for(int j = index; j < model.numRows(); j++){
                    loadRow(j);
                }
            }else
                view.scrollBar.setScrollPosition(index*cellHeight);
            if(manager != null){
                manager.rowAdded(index,this);
            }
            view.setScrollHeight((model.numRows()*cellHeight)+(maxRows*cellSpacing)+cellSpacing);
        }
    }

    public void insertRow(int index, TableRow row) {
        if (manager == null || (manager != null && manager.canInsert(index,
                                                                     this))) {
      
            model.insertRow(start+index, row);
            if(view.table.getRowCount() != maxRows){
                createRow(model.numRows() -1);
                for(int j = index; j < model.numRows(); j++){
                    loadRow(j);
                }
            }else
                view.scrollBar.setScrollPosition(index*cellHeight);
            if(manager != null){
                manager.rowAdded(index,this);
            }
            view.setScrollHeight((model.numRows()*cellHeight)+(maxRows*cellSpacing)+cellSpacing);
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
        	TableCellWidget tCell = (TableCellWidget)view.table.getWidget(index, i);
        	tCell.setField(model.getFieldAt(start+index, i));
        	setCellDisplay(index,i);
            if(showRows){
                ((Label)view.rows.getWidget(index,0)).setText(String.valueOf(start+index+1));
            }
        }
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
    
            model.deleteRow(start+row);
            if(view.table.getRowCount() != maxRows && model.numRows() < maxRows){
                view.table.removeRow(row);
            }else
                scrollLoad(view.scrollBar.getScrollPosition());
        }
        view.setScrollHeight((model.numRows()*cellHeight)+(maxRows*cellSpacing)+cellSpacing);

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
        if(enabled){
            for (int i = 0; i < view.table.getCellCount(row); i++) {
                if (selectedCell == i) {
                    saveValue(row, i);
                    if(autoAdd){
                        if(manager != null && manager.doAutoAdd(start+row,i,this))
                            addRow();
                        else if(manager == null && start+row == model.numRows() -1)
                            addRow();
                    }       
                }
                setCellDisplay(row, i);
            }
        }
        selectedCell = -1;
        selected = -1;
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
        if (manager == null || (manager != null && manager.canSelect(start+row, this))){
            if(selected > -1)
                unselect(selected);
            view.table.getRowFormatter().addStyleName(row, view.selectedStyle);
            for(int i = 0; i < view.table.getCellCount(row); i++){
                if(view.table.getCellFormatter().getStyleName(row,i).indexOf("disabled") > -1){
                    view.table.getWidget(row,i).addStyleName("disabled");
                }
            }
        }
        else
            return;
        if(enabled){
            if (selectedCell != col) {
                if (selectedCell > -1) {
                    saveValue(row, selectedCell);
                    if(autoAdd){
                        if(manager != null && manager.doAutoAdd(start+row,selectedCell,this))
                            addRow();
                        else if(manager == null && start+row == model.numRows() -1)
                            addRow();
                    }
                    setCellDisplay(row, selectedCell);
                }
                if (col > -1 && (manager == null || (manager != null && manager.canEdit(start+row, col, this))))
                    setCellEditor(row, col);
                else
                    selectedCell = -1;
            }
        }
        selected = row;
        if (manager != null && col > -1)
            manager.action(start+row, col, this);
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
            if(selected > -1){
            	view.table.getRowFormatter().removeStyleName(selected, view.selectedStyle);
            	unselect(selected);                
            }    
            view.table.getRowFormatter().addStyleName(row, view.selectedStyle);
            for(int i = 0; i < view.table.getCellCount(row); i++){
                if(view.table.getCellFormatter().getStyleName(row,i).indexOf("disabled") > -1){
                    view.table.getWidget(row,i).addStyleName("disabled");
                }
            }
            selectedCell = -1;
            selected = row;
        }catch(Exception e){
            Window.alert("select "+e.getMessage());
        }
    }

    /**
     * This method can be overridden to handle on change events
     */
    public void onChange(Widget sender) {
        if(sender instanceof OptionList){
            int sel = selected;
            unselect(sel);
            select(sel);
        }   
        if(sender instanceof DataModelWidget){
        	DataModelWidget modelWidget = (DataModelWidget)sender;
        	if(modelWidget.event == DataModelWidget.SELECTION){
        		select(modelWidget.getSelectedIndex());
        	}
        	if(modelWidget.event == DataModelWidget.REFRESH){
        		manager.setModel(this,modelWidget.getModel());
        	}
        }
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
        	TableCellWidget cell = 	(TableCellWidget)view.table.getWidget(row, col);
        	if(cell instanceof TableLabel){
        		selectedCell = -1;
        	    return;
        	}
        	cell.setEditor();
            if(!(cell instanceof TableCheck)){
                ((SimplePanel)cell).getWidget().addStyleName(view.widgetStyle);
                ((SimplePanel)cell).getWidget().addStyleName("Enabled");
            }
            if (((SimplePanel)cell).getWidget() instanceof HasFocus){
            	((HasFocus)((SimplePanel)cell).getWidget()).setFocus(true);
                /*
                 * Even though we set focus above we need to do it again in a 
                 * deferred command for that POS browser IE.
                */ 
                final int rowF = row;
                final int colF = col;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        try {
                            ((HasFocus)((SimplePanel)view.table.getWidget(rowF, colF)).getWidget()).setFocus(true);
                        }catch(Exception e){
                            
                        }
                    }
                });
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
        	TableCellWidget cell = (TableCellWidget)view.table.getWidget(row, col);
        	cell.setDisplay();
            DOM.setStyleAttribute(((SimplePanel)cell).getWidget().getElement(), "overflowX", "hidden");
        }catch(Exception e){
            Window.alert("setCell Display "+e.getMessage());
        }
           
     }

    /**
     * This method will clear and redraw the table
     */
    public void reset() {
        start = 0;
        view.cellView.setScrollPosition(0);
        if(selected > -1)
            view.table.getRowFormatter().removeStyleName(selected, view.selectedStyle);
        selected = -1;
        selectedCell = -1;
        view.controller = this;
        if(autoAdd){
            model.addRow(null);
        }
        view.reset();
        if(model.numRows() > 0){
            if(model.numRows() > maxRows){
                for(int i = 0; i < maxRows; i++){
                    createRow(i);
                }
            }else{
                for(int i = 0; i < model.numRows(); i++){
                    createRow(i);
                }
            }
        }        
        view.cellView.setWidget(view.table);
        load();
        sizeTable();
        modelSet = true;
        if(enabled)
            enabled(true);
    }
    
    public void load() {
        start = 0;
        selected = -1;
        selectedCell = -1;
        int tRows = maxRows;
        if(model.numRows() < maxRows)
            tRows = model.numRows();
        if(view.table.getRowCount() > tRows){
            int count = view.table.getRowCount();
            for(int i = count -1; i > tRows -1; i--){
                view.table.removeRow(i);
            }
        }else if(view.table.getRowCount() < tRows){
            int count = view.table.getRowCount();
            for(int i = count; i < tRows; i++){
                createRow(i);
            }
        }
        if(model.numRows() > maxRows)
            view.setScrollHeight((model.numRows()*cellHeight)+(maxRows*cellSpacing)+cellSpacing);
        else
            view.setScrollHeight((model.numRows()*cellHeight)+(model.numRows()*cellSpacing)+cellSpacing);
        view.scrollBar.setScrollPosition(0);
        if(model.numRows() > 0)
            scrollLoad(0);
        if (model.paged)
            view.setNavPanel(model.pageIndex, model.totalPages, model.showIndex);
    }
    
    public void createRow(int i) {
        for(int j= 0; j < model.getRow(i).numColumns(); j++){
            TableCellWidget tcell = editors[j].getNewInstance();
            if(tcell instanceof TableOption){
                if(((TableOption)tcell).loadFromHidden != null){
                    ((TableOption)tcell).fromHidden = (OptionField)model.hidden.get(((TableOption)tcell).loadFromHidden);
                }
            }
            ((SimplePanel)tcell).setWidth((curColWidth[j])+ "px");
            view.table.setWidget(i,j,(Widget)tcell);
            view.table.getFlexCellFormatter().addStyleName(i,
                                                  j,
                                                  TableView.cellStyle);
            if (colAlign != null && colAlign[j] != null) {
                view.table.getFlexCellFormatter()
                          .setHorizontalAlignment(i, j, colAlign[j]);
            }
            if(i % 2 == 1){
                DOM.setStyleAttribute(view.table.getRowFormatter().getElement(i), "background", "#f8f8f9");
            }
            view.table.getFlexCellFormatter().setWidth(i, j, curColWidth[j] + "px");
            view.table.getFlexCellFormatter().setHeight(i, j, cellHeight+"px");
            view.table.getRowFormatter().addStyleName(i, TableView.rowStyle);
            if(showRows){
                Label rowNum = new Label(String.valueOf(i+1));
                view.rows.setWidget(i,0,rowNum);
                view.rows.getFlexCellFormatter().setStyleName(i, 0, "RowNum");
                view.rows.getFlexCellFormatter().setHeight(i,0,cellHeight+"px");
            }
        }
    }
    
    public void scrollLoad(int scrollPos){
        try{
        	if(selected > -1)
        		unselect(-1);
        	int rowsPer = maxRows;
        	if(maxRows > model.numRows())
        		rowsPer = model.numRows();
        	start = (scrollPos)/(cellHeight);
        	if(start+rowsPer > model.numRows())
        		start = start - ((start+rowsPer) - model.numRows());
        	for(int i = 0; i < rowsPer; i++){
        		loadRow(i);
        	}
        }catch(Exception e){
            Window.alert("scrollLoad "+e.getMessage());
        }
    }

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
        view.table.setWidth(width+"px");
        if(view.header != null){
            view.header.setWidth((width+curColWidth.length*2)+"px");  
            for(int i = 0; i < curColWidth.length; i++){
                if( i > 0 && i < curColWidth.length - 1){
                    view.header.getFlexCellFormatter().setWidth(0, i*2,(curColWidth[i]-4)+"px");
                }else{
                    view.header.getFlexCellFormatter().setWidth(0, i*2,(curColWidth[i]-1)+"px");
                }
            }
        }           
        if(model.numRows() > 0)
            view.header.setWidth(view.table.getOffsetWidth()+"px");
        else
            view.header.setWidth(width+"px");
        int viewWidth = -1;
        if(!view.width.equals("auto"))
            viewWidth = Integer.parseInt(view.width.substring(0,view.width.indexOf("px")));
        else if(!GWT.isScript()){
            view.cellView.setWidth(view.table.getOffsetWidth()+"px");
            view.titlePanel.setWidth(view.table.getOffsetWidth()+"px");
        }
        if(viewWidth > -1 && view.table.getOffsetWidth() > viewWidth){
        	view.setHeight((maxRows*cellHeight+maxRows+18+"px"));
            view.setScrollHeight(model.numRows()*cellHeight+maxRows+18);
        }
        if(showRows){
            if(viewWidth > -1 && viewWidth < view.table.getOffsetWidth()){
                view.rowsView.setHeight((view.cellView.getOffsetHeight()-17)+"px");
            }
        }
        view.titlePanel.setWidth(view.cellView.getOffsetWidth()+"px");
           }
        });
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
    	TableCellWidget wid = (TableCellWidget)view.table.getWidget(row,col);
    	wid.saveValue();
        if (manager != null) {
            manager.finishedEditing(start+row, col, this);
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
            if (selected >= 0 && selected < model.numRows() - 1) {
                if(selected < view.table.getRowCount() -1){
                    final int row = selected + 1;
                    final int col = selectedCell;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, row, col);
                        }
                    });
                }else{
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                    final int col = selectedCell;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, maxRows-1, col);
                        }
                    });
                }
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
            }else{
                view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                final int col = selectedCell;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        onCellClicked(view.table, 0, col);
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
                    if(!(view.table.getWidget(selected, selectedCell) instanceof TableCheck)){
                        saveValue(selected, selectedCell);
                        setCellDisplay(selected, selectedCell);
                        if(model.autoAdd || autoAdd){
                            if(manager != null && manager.doAutoAdd(selected,selectedCell,this))
                                addRow();
                            else if(manager == null && start+selected == model.numRows() -1)
                                addRow();
                        }
                        selectedCell = -1;
                    } else {
                        if (manager != null)
                            return manager.action(selected, -1, this);
                    }
                }
            }
        }
        if (KeyboardListener.KEY_RIGHT == code && model.paged) {
            if (model.pageIndex != model.totalPages - 1)
                manager.getNextPage(this);
        }
        if (KeyboardListener.KEY_LEFT == code && model.paged) {
            if (model.pageIndex != 0)
                manager.getPreviousPage(this);
        }
        if (KeyboardListener.KEY_TAB == code && selectedCell > -1 && !shift) {
            if (selectedCell + 1 >= model.getRow(start+selected).numColumns()) {
                int row = selected + 1;
                int col = 0;
                if (start+row == model.numRows()){
                    row = 0;
                    view.scrollBar.setScrollPosition(0);
                }
                while ((editors[col] instanceof TableLabel))
                    col++;
                if(row < maxRows - 1){
                    final int fRow = row;
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, fRow, fCol);
                        }
                    });
                }else{
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table,maxRows -1,fCol);
                        }
                    });
                }
            } else {
                int col = selectedCell + 1;
                while ((editors[col] instanceof TableLabel))
                    col++;
                final int fCol = col;
                DeferredCommand.addCommand(new Command() {
                   public void execute() {
                       onCellClicked(view.table, selected, fCol);
                       if(((TableCellWidget)view.table.getWidget(selected, fCol)).getWidget() instanceof FocusWidget)
                           ((FocusWidget)((TableCellWidget)view.table.getWidget(selected, fCol)).getWidget()).setFocus(true);
                   }
                });
                //((FocusWidget)view.table.getWidget(selected, selectedCell)).setFocus(true);
            }
            DOM.eventCancelBubble(event, true);
            DOM.eventPreventDefault(event);
            return false;
        }
        if (KeyboardListener.KEY_TAB == code && selectedCell > -1 && shift) {
            if (selectedCell - 1 < 0) {
                if(selected == 0) {
                    if(start == 0){
                        final int row = maxRows -1;
                        int col = model.getRow(model.numRows() -1).numColumns() - 1;
                        while ((editors[col] instanceof TableLabel))
                            col--;
                        final int fCol = col;
                        view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+model.numRows()*cellHeight);
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                onCellClicked(view.table, row, fCol);
                            }
                        });
                    }else{
                        int col = model.getRow(model.numRows() -1).numColumns() - 1;
                        while ((editors[col] instanceof TableLabel))
                            col--;
                        final int fCol = col;
                        view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                onCellClicked(view.table, 0, fCol);
                            }
                        });
                    }
                }else{
                    final int row = selected - 1;
                    int col = model.getRow(model.numRows() -1).numColumns() - 1;
                    while ((editors[col] instanceof TableLabel))
                        col--;
                    final int fCol = col;
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            onCellClicked(view.table, row, fCol);
                        }
                    });
                }
            } else {
                int col = selectedCell - 1;
                while (col > -1 && (editors[col] instanceof TableLabel))
                    col--;
                if(col < 0){
                    selectedCell = 0;
                    return onKeyPress(event);
                }
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
            if (DOM.eventGetType(event) == Event.ONCLICK){
                if(!DOM.isOrHasChild(view.getElement(), DOM.eventGetTarget(event))){
                    DOM.removeEventPreview(this);
                  //  unselect(-1);
                }
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
        HTML nav = (HTML)sender;
        String styleNames = nav.getStyleName();
        String htmlString = nav.getHTML();
        String page = "";
        
        int start = htmlString.indexOf("value=\"") + 7;
        int end = htmlString.indexOf("\"", start);
        
        if(start > 6)
        	page = htmlString.substring(start, end);
        
        if (styleNames.indexOf("nextNavIndex")>-1)
            manager.getNextPage(this);
        else if (styleNames.indexOf("prevNavIndex")>-1)
            manager.getPreviousPage(this);
        else if(!"".equals(page))
            manager.getPage(Integer.parseInt(page));
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
                if(modelSet)
                    loadModel((TableModel)result);
                else
                    setModel((TableModel)result);
                if(setRow > -1)
                    select(setRow,0);
                setRow = -1;
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
        if(sender instanceof FocusPanel){
            resizing = true;
            startx = x;
            for (int i = 0; i < view.header.getCellCount(0); i++) {
                if (sender == view.header.getWidget(0, i)) {
                    resizeColumn = i - 1;
                    tableCol = resizeColumn / 2;
                }
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
            int colA = curColWidth[tableCol] + (x - startx);
            int colB = curColWidth[(tableCol)+1] - (x - startx);
            if(colA <= 16 || colB <= 16) 
                return;
            curColWidth[tableCol] = colA;
            curColWidth[(tableCol)+1] = colB;
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
                            ((SimplePanel)view.table.getWidget(j, i)).setWidth((curColWidth[i]) + "px");
                            ((SimplePanel)view.table.getWidget(j,i)).getWidget().setWidth(curColWidth[i]+"px");
                        }
                    }
                }
            });
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
    
    public void enabled(boolean enabled){
        this.enabled = enabled;
        for(int i = 0; i < editors.length; i++){
            editors[i].enable(enabled);
        }
        Iterator widIt = view.table.iterator();
        while(widIt.hasNext()){
            ((TableCellWidget)widIt.next()).enable(enabled);
        }
    }
    
}
