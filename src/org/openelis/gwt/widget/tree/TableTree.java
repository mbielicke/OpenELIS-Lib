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
package org.openelis.gwt.widget.tree;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.Field;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.table.TableCellWidget;

import java.util.ArrayList;
import java.util.Stack;

public class TableTree extends SimplePanel implements TableCellWidget , SourcesCommandEvents {
    
    public class ItemGrid extends Grid implements TableListener{
        
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
    
    public ArrayList<TableCellWidget> cells = new ArrayList<TableCellWidget>();
    public ItemGrid editorGrid;
    public TableCellWidget editor;
    private CommandListenerCollection commandListeners;
    private FieldType field;
    private int width;
    private NumberFormat displayMask;
    public static final String TAG_NAME = "table-tree";
    public enum Action {TOGGLE};
    public int rowIndex;
    public boolean enabled;

    
    public TableTree() {

    }

    public void clear() {
        
    }

    public TableCellWidget getNewInstance() {
        TableTree treeItem = new TableTree();
        treeItem.width = width;
        treeItem.enabled = enabled;
        treeItem.cells = cells;
        return treeItem;
    }

    public Widget getInstance(Node node) {
        return (Widget)getNewInstance();
    }
    
    public TableTree(Node node, ScreenBase screen){
        NodeList editors = node.getChildNodes();
        for (int i = 0; i < editors.getLength(); i++) {
            if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                cells.add((TableCellWidget)ScreenBase.createCellWidget(editors.item(i),screen));
            }
        }
    }

    public void setDisplay() {
        editor.setDisplay();
        createItem((TreeDataItem)field);
        setWidget(editorGrid);
    }

    public void setEditor() {
        editor.setEditor();
        createItem((TreeDataItem)field);
        setWidget(editorGrid);
    }

    public void saveValue() {
        // TODO Auto-generated method stub
        
    }

    public void setField(FieldType field) {
        this.field = field;
        TreeDataItem item = (TreeDataItem)field;
        //editor = cells.get(item.leafCell).getNewInstance();
        editor.setField(item.get(0));
        editor.enable(enabled);
        editor.setCellWidth(width - ((item.depth+1)*18));
        ((SimplePanel)editor).setWidth(width - ((item.depth+1)*18)+"px");
    }

    public void enable(boolean enabled) {
       this.enabled = enabled;
       if(editor != null)
           editor.enable(enabled);
    }

    public void setCellWidth(int width) {
        this.width = width;
        if(editorGrid != null)
            editorGrid.setWidth(width+"px");
        if(editor != null && field != null) {
            editor.setCellWidth(width - ((((TreeDataItem)field).depth+1)*18));
            ((SimplePanel)editor).setWidth(width -((((TreeDataItem)field).depth+1)*18)+"px");
        }
    }
    
    public void setFocus(boolean focused) {
        if(editor != null)
            editor.setFocus(focused);
    }
    
    public void createItem(final TreeDataItem drow) {
        editorGrid = new ItemGrid(1,2+drow.depth);    
        editorGrid.setWidth(width+"px");
        for(int j = 0; j < editorGrid.getColumnCount(); j++) {
            if(j < editorGrid.getColumnCount() -1)
                editorGrid.getCellFormatter().setWidth(0,j,"18px");
            if(j == 0)
                editorGrid.getCellFormatter().setHeight(0,j,"18px");
            
            if(j == editorGrid.getColumnCount() -2){
                if(drow.open && drow.hasChildren())
                    editorGrid.getCellFormatter().setStyleName(0,j,"treeOpenImage");
//                    DOM.setStyleAttribute(editorGrid.getCellFormatter().getElement(0,j), "background", "url('Images/tree-.gif') no-repeat center");
                else if(drow.hasChildren())
                    editorGrid.getCellFormatter().setStyleName(0,j,"treeClosedImage");
                    //DOM.setStyleAttribute(editorGrid.getCellFormatter().getElement(0,j), "background", "url('Images/tree+.gif') no-repeat center");
                else if(j > 0){
                    
                    if(drow.parent.getItems().indexOf(drow) == drow.parent.getItems().size()-1)
                        editorGrid.getCellFormatter().setStyleName(0,j,"treeLImage");
                    //    DOM.setStyleAttribute(editorGrid.getCellFormatter().getElement(0,j), "background", "url('Images/treedotsL.gif') no-repeat center");
                    else
                        editorGrid.getCellFormatter().setStyleName(0,j,"treeTImage");
                        //DOM.setStyleAttribute(editorGrid.getCellFormatter().getElement(0,j), "background", "url('Images/treedotsT.gif') no-repeat center");
                }
                if(drow.hasChildren()){
                    editorGrid.clickCell = j;
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
                    editorGrid.getCellFormatter().setStyleName(0,item.depth,"treeIImage");
                    //DOM.setStyleAttribute(editorGrid.getCellFormatter().getElement(0,item.depth), "background", "url('Images/treedotsI.gif') no-repeat center");
                }
            }
            
        }
       // if(i % 2 == 1){
       //     DOM.setStyleAttribute(grid.getRowFormatter().getElement(0), "background", "#f8f8f9");
       // }
        
        editorGrid.setWidget(0, editorGrid.getColumnCount() - 1, (Widget)editor);
        /*
       if(i % 2 == 1){
             DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,grid.getColumnCount()-1), "background", "#f8f8f9");
       }
       */
       editorGrid.addStyleName("TreeTableLeftTree");
       DOM.setStyleAttribute(editorGrid.getRowFormatter().getElement(0), "background", "none");
       //DOM.setStyleAttribute(editorGrid.getWidget(0,editorGrid.getColumnCount() - 1).getElement(),"padding","2px");
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
