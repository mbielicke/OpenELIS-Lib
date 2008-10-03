package org.openelis.gwt.widget.tree;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.tree.TreeView;

import java.util.Stack;

public class TableTree extends SimplePanel implements TableCellWidget , SourcesCommandEvents {
    
    private class ItemGrid extends Grid implements TableListener{
        
        public int clickCell;
        
        public ItemGrid(int rows, int cols) {
            super(rows,cols);
            setCellPadding(0);
            setCellSpacing(0);
            addTableListener(this);
        }

        public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
            if(cell == clickCell){
                commandListeners.fireCommand(Action.TOGGLE, new Integer(rowIndex));
            }
        }
    }
    
    private ItemGrid editor;
    private CommandListenerCollection commandListeners;
    private DataObject field;
    private int width;
    private NumberFormat displayMask;
    public static final String TAG_NAME = "table-tree";
    public enum Action {TOGGLE};
    public int rowIndex;

    
    public TableTree() {

    }

    public void clear() {
        
    }

    public TableCellWidget getNewInstance() {
        TableTree treeItem = new TableTree();
        treeItem.width = width;
        return treeItem;
    }

    public Widget getInstance(Node node) {
        return new TableTree();
    }
    
    public TableTree(Node node){
    }

    public void setDisplay() {
        setEditor();        
    }

    public void setEditor() {
        createItem((TreeDataItem)field);
        setWidget(editor);
    }

    public void saveValue() {
        // TODO Auto-generated method stub
        
    }

    public void setField(DataObject field) {
        this.field = field;
    }

    public void enable(boolean enabled) {
        // TODO Auto-generated method stub   
    }

    public void setCellWidth(int width) {
        this.width = width;
        if(editor != null)
            editor.setWidth(width+"px");
    }
    
    public void setFocus(boolean focused) {

    }
    
    public void createItem(final TreeDataItem drow) {
        editor = new ItemGrid(1,2+drow.depth);    
        editor.setWidth(width+"px");
        for(int j = 0; j < editor.getColumnCount(); j++) {
            if(j < editor.getColumnCount() -1)
                editor.getCellFormatter().setWidth(0,j,"18px");
            editor.getCellFormatter().setHeight(0,j,"18px");
            if(j == editor.getColumnCount() -2){
                if(drow.open && drow.getItems().size() > 0)
                    DOM.setStyleAttribute(editor.getCellFormatter().getElement(0,j), "background", "url('Images/tree-.gif') no-repeat center");
                else if(drow.getItems().size() > 0)
                    DOM.setStyleAttribute(editor.getCellFormatter().getElement(0,j), "background", "url('Images/tree+.gif') no-repeat center");
                else if(j > 0){
                    if(drow.parent.getItems().indexOf(drow) == drow.parent.getItems().size()-1)
                        DOM.setStyleAttribute(editor.getCellFormatter().getElement(0,j), "background", "url('Images/treedotsL.gif') no-repeat");
                    else
                        DOM.setStyleAttribute(editor.getCellFormatter().getElement(0,j), "background", "url('Images/treedotsT.gif') no-repeat");
                }
                if(drow.getItems().size() > 0){
                    editor.clickCell = j;
                }
            }
        }
        if(drow.depth > 1) {
            Stack<TreeDataItem> levels = new Stack<TreeDataItem>();
            levels.push(drow.parent);
            while(levels.peek().depth > 1){
                levels.push(levels.peek().parent);
            }
            for(TreeDataItem item : levels){
                if(item.parent.getItems().indexOf(item) < item.parent.getItems().size() -1){
                    DOM.setStyleAttribute(editor.getCellFormatter().getElement(0,item.depth), "background", "url('Images/treedotsI.gif') no-repeat center");
                }
            }
            
        }
       // if(i % 2 == 1){
       //     DOM.setStyleAttribute(grid.getRowFormatter().getElement(0), "background", "#f8f8f9");
       // }
        
        editor.setWidget(0, editor.getColumnCount() - 1, new Label((String)drow.getLabel().getValue()));
        /*
       if(i % 2 == 1){
             DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,grid.getColumnCount()-1), "background", "#f8f8f9");
       }
       */
       editor.addStyleName(TreeView.cellStyle);
       DOM.setStyleAttribute(editor.getWidget(0,editor.getColumnCount() - 1).getElement(),"padding","2px");
    }



    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int row) {
        rowIndex = row;
        
    }
    
    public void addCommandListener(CommandListener listener) {
        if(commandListeners == null)
            commandListeners = new CommandListenerCollection();
        commandListeners.add(listener);   
     }

     public void removeCommandListener(CommandListener listener) {
         if(commandListeners != null) 
             commandListeners.remove(listener);
         
     }
    

}
