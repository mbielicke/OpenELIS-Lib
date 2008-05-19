package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.AbstractField;

import java.util.Iterator;

/**
 * This class is the main controller for the Table widget. It hooks the model to
 * the view and also controls when row selection and calls to the manager for
 * permissions and actions set by the a developer.
 * 
 * @author tschmidt
 * 
 */
public class QueryTable extends TableController {
    
    //public TableModel model;
    //public TableView view;
    public TableManager manager;
    public int selected = -1;
    public int selectedCell = -1;
    public int setRow = -1;
    public TableCellWidget[] editors;
    //public int[] colwidth;
    //public int[] curColWidth;
    public boolean enabled = true;
    public AbstractField[] fields;

    /**
     * Default constructor, puts table on top of the event stack.
     * 
     */
    public QueryTable() {
        view = new TableView();
        initWidget(view);
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
            if(editors[i] instanceof TableMultiple){
                ((TableMultiple)editors[i]).initCells(this);
            }
        }
    }
    
    public void setFields(AbstractField[] fields){
        this.fields = fields;
    }
    
    /**
     * This method loads the row form the model specified by the passed index
     * into the table view.
     * 
     * @param index
     */
    public void loadRow() {
        for (int i = 0; i < fields.length; i++) {
        	TableCellWidget tCell = (TableCellWidget)view.table.getWidget(0, i);
            if(tCell instanceof TableMultiple && manager != null){
                manager.setMultiple(0,i,this);
            }
        	tCell.setField(fields[i]);
        	setCellDisplay(0,i);
            if(showRows){
                ((Label)view.rows.getWidget(0,0)).setText("1");
            }
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
        if(selected == row && selectedCell == col)
            return;
        if (sender == view.table) {
            select(row, col);
        }
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
            view.table.getRowFormatter().addStyleName(row, view.selectedStyle);
            for(int i = 0; i < view.table.getCellCount(row); i++){
                if(view.table.getCellFormatter().getStyleName(row,i).indexOf("disabled") > -1){
                    view.table.getWidget(row,i).addStyleName("disabled");
                }
            }
            if(enabled){
                if (selectedCell != col) {
                    if (selectedCell > -1) {
                        saveValue(row, selectedCell);
                        setCellDisplay(row, selectedCell);
                    }
                    if (col > -1 )
                        setCellEditor(row, col);
                    else
                        selectedCell = -1;
                }
            }
            selected = row;
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
            DOM.setStyleAttribute(cell.getWidget().getElement(), "overflowX", "hidden");
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
        //view.reset();
        createRow();
        loadRow();
        view.cellView.setWidget(view.table);
        sizeTable();
        if(enabled)
            enabled(true);
    }
    
    public void createRow() {
        for(int j= 0; j < editors.length; j++){
            TableCellWidget tcell = editors[j].getNewInstance();
            tcell.setCellWidth(curColWidth[j]);
            if(tcell instanceof TableMultiple){
                ((TableMultiple)tcell).initCells(model);
            }
            ((SimplePanel)tcell).setWidth((curColWidth[j])+ "px");
            view.table.setWidget(0,j,(Widget)tcell);
            view.table.getFlexCellFormatter().addStyleName(0,
                                                  j,
                                                  TableView.cellStyle);
            if (colAlign != null && colAlign[j] != null) {
                view.table.getFlexCellFormatter()
                          .setHorizontalAlignment(0, j, colAlign[j]);
            }
            view.table.getFlexCellFormatter().setWidth(0, j, curColWidth[j] + "px");
            view.table.getFlexCellFormatter().setHeight(0, j, cellHeight+"px");
            view.table.getRowFormatter().addStyleName(0, TableView.rowStyle);
            if(showRows){
                Label rowNum = new Label("1");
                view.rows.setWidget(0,0,rowNum);
                view.rows.getFlexCellFormatter().setStyleName(0, 0, "RowNum");
                view.rows.getFlexCellFormatter().setHeight(0,0,cellHeight+"px");
            }
        }
    }
    
    public void scrollLoad(int scrollPos){

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
        }catch(Exception e){
            Window.alert("save Value "+e.getMessage());
        }
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

    public void onKeyDown(Widget sender, char code, int modifiers) {
        boolean shift = modifiers == KeyboardListener.MODIFIER_SHIFT;
        if (KeyboardListener.KEY_ENTER == code) {
            if (selected >= 0) {
                if (selectedCell > -1) {
                    if(!(view.table.getWidget(selected, selectedCell) instanceof TableCheck)){
                        saveValue(selected, selectedCell);
                        setCellDisplay(selected, selectedCell);
                        selectedCell = -1;
                    }
                }
            }
        }
        if (KeyboardListener.KEY_TAB == code && selectedCell > -1 && !shift) {
            if (selectedCell + 1 >= fields.length) {
                int col = 0;
                while ((editors[col] instanceof TableLabel))
                    col++;
                final int fCol = col;
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        onCellClicked(view.table, 0, fCol);
                    }
                });
            } else {
                int col = selectedCell + 1;
                while ((editors[col] instanceof TableLabel))
                    col++;
                final int fCol = col;
                DeferredCommand.addCommand(new Command() {
                   public void execute() {
                       onCellClicked(view.table, selected, fCol);
                       if(((TableCellWidget)view.table.getWidget(0, fCol)).getWidget() instanceof FocusWidget)
                           ((FocusWidget)((TableCellWidget)view.table.getWidget(0, fCol)).getWidget()).setFocus(true);
                   }
                });
            }
        }
        if (KeyboardListener.KEY_TAB == code && selectedCell > -1 && shift) {
            int col;
            if (selectedCell - 1 < 0) 
                col = fields.length - 1;
            else
                col = selectedCell -1;
             while (col > -1 && (editors[col] instanceof TableLabel))
                col--;
             if(col < 0){
                selectedCell = 0;
                onKeyDown(sender,code,modifiers);
             }
             final int fCol = col;
             DeferredCommand.addCommand(new Command() {
                public void execute() {
                    onCellClicked(view.table, selected, fCol);
                }
             });
        }
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }
    
    public void switchSelectedRow() {
        if(selected > -1 && selectedCell > -1){
            saveValue(selected, selectedCell);
            setCellDisplay(selected, selectedCell);
        }
    }
    
}
