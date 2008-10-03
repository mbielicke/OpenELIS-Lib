package org.openelis.gwt.widget.tree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.table.TableView;
import org.openelis.gwt.widget.tree.event.SourcesTreeModelEvents;
import org.openelis.gwt.widget.tree.event.SourcesTreeWidgetEvents;
import org.openelis.gwt.widget.tree.event.TreeModelListener;
import org.openelis.gwt.widget.tree.event.TreeWidgetListener;

import java.util.Stack;

public class TreeRenderer implements TreeRendererInt, TreeModelListener, TreeWidgetListener  {
    
    private TreeWidget controller;
    
    public TreeRenderer(TreeWidget controller){
        this.controller = controller;
    }
    
    public void createRow(int i) {
        int j = 0;
        for(TreeColumnInt column : controller.columns) {
            controller.view.table.setWidget(i,j,column.getWidgetInstance());
            controller.view.table.getFlexCellFormatter().addStyleName(i,
                                                  j,
                                                  TableView.cellStyle);
            controller.view.table.getFlexCellFormatter()
                          .setHorizontalAlignment(i, j, column.getAlign());

            if(i % 2 == 1){
                DOM.setStyleAttribute(controller.view.table.getRowFormatter().getElement(i), "background", "#f8f8f9");
            }
            controller.view.table.getFlexCellFormatter().setWidth(i, j, column.getCurrentWidth() + "px");
            controller.view.table.getFlexCellFormatter().setHeight(i, j, controller.cellHeight+"px");
            //view.table.getRowFormatter().addStyleName(i, TableView.rowStyle);
            if(controller.showRows){
                Label rowNum = new Label(String.valueOf(i+1));
                controller.view.rows.setWidget(i,0,rowNum);
                controller.view.rows.getFlexCellFormatter().setStyleName(i, 0, "RowNum");
                controller.view.rows.getFlexCellFormatter().setHeight(i,0,controller.cellHeight+"px");
            }
            j++;
        }
    }
    
    public void load(int pos) {
        controller.modelIndexList = new int[controller.maxRows];
        int ScrollHeight = (controller.model.shownRows()*controller.cellHeight)+(controller.maxRows*2);
        if(controller.model.isAutoAdd()){
            ScrollHeight += controller.cellHeight;
        }
        int testStart = new Double(Math.ceil(((double)(controller.maxRows*controller.cellHeight+(controller.maxRows*controller.cellSpacing)+(controller.maxRows*2)+controller.cellSpacing))/(controller.cellHeight))).intValue();
        if(testStart < controller.model.shownRows() - controller.maxRows)
            ScrollHeight += controller.cellHeight;
        controller.view.setScrollHeight(ScrollHeight);
        controller.view.scrollBar.setScrollPosition(pos);
        int tRows = controller.maxRows;
        if(controller.model.shownRows() == 0){
            int count = controller.view.table.getRowCount();
            for(int i = 0; i < count; i++){
                controller.view.table.removeRow(0);
            }
            if(!controller.model.getAutoAdd())
                return;
        }
        if(controller.model.shownRows() < controller.maxRows){
            tRows = controller.model.shownRows();
            if(controller.model.getAutoAdd()){
                tRows++;
            }
        }
        if(controller.view.table.getRowCount() > tRows){
            int count = controller.view.table.getRowCount();
            for(int i = count -1; i > tRows -1; i--){
                controller.view.table.removeRow(i);
            }
        }else if(controller.view.table.getRowCount() < tRows){
            int count = controller.view.table.getRowCount();
            for(int i = count; i < tRows; i++){
                createRow(i);
            }
        }
        if(controller.model.getAutoAdd() && controller.view.table.getRowCount() == 0){
            createRow(0);
        }
        scrollLoad(pos);

    }
    
    /**
     * This method loads the row form the model specified by the passed index
     * into the table view.
     * 
     * @param index
     */
    private void loadRow(int index, int modelIndex) {
        controller.modelIndexList[index] = modelIndex;     
        TreeDataItem row = (TreeDataItem)controller.model.getRow(modelIndex);
        controller.columns.get(0).loadWidget(controller.view.table.getWidget(index, 0),row);
        for (int i = 0; i < row.size(); i++) {
            controller.columns.get(i+1).loadWidget(controller.view.table.getWidget(index, i+1),row.get(i));
            //if(tCell instanceof TableMultiple && manager != null){
              //  manager.setMultiple(model.indexOf(row),i,this);
            //}
            /*if(tCell instanceof TableCheck){
                if(controller.model.canEdit(index,i)){
                    tCell.enable(true);
                }else{
                    tCell.enable(false);
                }
            }
            */
            //tCell.setField(row.getObject(i));
           // setCellDisplay(index,i);
            //if(controller.showRows){
              //  ((Label)controller.view.rows.getWidget(index,0)).setText(String.valueOf(controller.model.indexOf(row)+1));
           // }
            if(controller.model.isSelected(modelIndex))
                controller.view.table.getRowFormatter().addStyleName(index, controller.view.selectedStyle);
            else
                controller.view.table.getRowFormatter().removeStyleName(index,controller.view.selectedStyle);
            if(controller.model.isEnabled(modelIndex)) 
                controller.view.table.getRowFormatter().removeStyleName(index, controller.view.disabledStyle);
            else
                controller.view.table.getRowFormatter().addStyleName(index,controller.view.disabledStyle);
                
            
        }
    }
    
    public Grid createItem(final TreeDataItem drow, int i) {
        Grid grid = new Grid(1,2+drow.depth);
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        grid.setWidth(controller.columns.get(0).getCurrentWidth()+"px");
        for(int j = 0; j < grid.getColumnCount(); j++) {
            if(j < grid.getColumnCount() -1)
                grid.getCellFormatter().setWidth(0,j,"18px");
            grid.getCellFormatter().setHeight(0,j,controller.cellHeight+"px");
            if(j == grid.getColumnCount() -2){
                if(drow.open && drow.size() > 0)
                    DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,j), "background", "url('Images/tree-.gif') no-repeat center");
                else if(drow.size() > 0)
                    DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,j), "background", "url('Images/tree+.gif') no-repeat center");
                else if(j > 0){
                    if(drow.parent.getItems().indexOf(drow) == drow.parent.getItems().size()-1)
                        DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,j), "background", "url('Images/treedotsL.gif') no-repeat");
                    else
                        DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,j), "background", "url('Images/treedotsT.gif') no-repeat");
                }
                if(drow.size() > 0){
                    grid.addTableListener(new TableListener() {
                        public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
                            drow.toggle();
                            controller.model.getData().setRows();
                            load(controller.view.scrollBar.getScrollPosition());
                        }
                    });
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
                    DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,item.depth), "background", "url('Images/treedotsI.gif') no-repeat center");
                }
            }
            
        }
       // if(i % 2 == 1){
       //     DOM.setStyleAttribute(grid.getRowFormatter().getElement(0), "background", "#f8f8f9");
       // }
        
        grid.setWidget(0, grid.getColumnCount() - 1, new Label((String)drow.getLabel().getValue()));
       if(i % 2 == 1){
             DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,grid.getColumnCount()-1), "background", "#f8f8f9");
       }
       grid.addStyleName(TreeView.cellStyle);
       DOM.setStyleAttribute(grid.getWidget(0,grid.getColumnCount() - 1).getElement(),"padding","2px");
        return grid;
        
    }
    

    
    public void scrollLoad(int scrollPos){
        if(controller.editingCell != null){
            controller.columns.get(controller.activeCell).saveValue((Widget)controller.editingCell);
            finishedEditing(null,controller.activeRow,controller.activeCell);
        }
        int rowsPer = controller.maxRows;
        if(controller.maxRows > controller.model.shownRows()){
            rowsPer = controller.model.shownRows();
            if(controller.model.getAutoAdd())
                rowsPer++;
        }
        int loadStart = new Double(Math.ceil(((double)scrollPos)/(controller.cellHeight))).intValue();
        if(controller.model.numRows() != controller.model.shownRows()){
            int start = 0;
            int i = 0;
            while(start < loadStart && i < controller.model.numRows() -1){
                if(controller.model.getRow(i).shown)
                    start++;
                i++;
            }
             loadStart = i;   
        }
        if(loadStart+rowsPer > controller.model.numRows()){
            loadStart = loadStart - ((loadStart+rowsPer) - controller.model.numRows());
            if(controller.model.getAutoAdd())
                loadStart++;
        }
        for(int i = 0; i < rowsPer; i++){
            while(loadStart+i < controller.model.numRows() && !controller.model.getRow(loadStart+i).shown)
                loadStart++;
            if(loadStart+i < controller.model.numRows())
                loadRow(i,loadStart+i);
            else{
                controller.model.setAutoAddRow(controller.model.createRow());
                loadRow(i,controller.model.numRows());
            }
        }

    }
    
    public void setCellEditor(int row, int col) {
            TableCellWidget cell =  (TableCellWidget)controller.view.table.getWidget(row, col);
            controller.editingCell = cell;
            controller.columns.get(col).setWidgetEditor((Widget)cell);
            ((SimplePanel)cell).getWidget().addStyleName(controller.view.widgetStyle);
            ((SimplePanel)cell).getWidget().addStyleName("Enabled");
    }
    
    public void setCellDisplay(int row, int col) {
        controller.columns.get(col).setWidgetDisplay(controller.view.table.getWidget(row, col));        
    }

    public void finishedEditing(SourcesTreeWidgetEvents sender, int row, int col) {
        if(controller.editingCell != null){
            controller.columns.get(controller.activeCell).saveValue((Widget)controller.editingCell);
            setCellDisplay(row,col);
            controller.editingCell = null;
        }
    }

    public void hideRows(SourcesTreeWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void removeRows(SourcesTreeWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void selected(SourcesTreeWidgetEvents sender, int[] rows) {
        // TODO Auto-generated method stub
        
    }

    public void startedEditing(SourcesTreeWidgetEvents sender, int row, int col) {
        setCellEditor(row,col);
    }

    public void rowUnselected(SourcesTreeModelEvents sender, int row) {
        if(row == -1){
            for(int i = 0; i < controller.view.table.getRowCount() ; i++) {
                controller.view.table.getRowFormatter().removeStyleName(i,controller.view.selectedStyle);
            }
        }else
            controller.view.table.getRowFormatter().removeStyleName(controller.activeRow, controller.view.selectedStyle);
        
    }

    public void cellUpdated(SourcesTreeModelEvents sender, int row, int cell) {
        setCellDisplay(row,cell);
    }

    public void dataChanged(SourcesTreeModelEvents sender) {
        load(0);
    }

    public void rowDeleted(SourcesTreeModelEvents sender, int row) {
        load(0);        
    }

    public void rowAdded(SourcesTreeModelEvents sender, int row) {
        load(0);        
    }

    public void rowUpdated(SourcesTreeModelEvents sender, int row) {
        load(0);
    }

    public void rowSelectd(SourcesTreeModelEvents sender, int row) {
        if(controller.activeRow < 0)
            return;
        controller.view.table.getRowFormatter().addStyleName(controller.activeRow, controller.view.selectedStyle);
        for(int i = 0; i < controller.view.table.getCellCount(controller.activeRow); i++){
            if(controller.view.table.getCellFormatter().getStyleName(controller.activeRow,i).indexOf("disabled") > -1){
                controller.view.table.getWidget(controller.activeRow,i).addStyleName("disabled");
            }
        }
        
    }

    public void selected(SourcesTreeWidgetEvents sender, int rows) {
        // TODO Auto-generated method stub
        
    }

    public void unselected(SourcesTreeWidgetEvents sender, int rows) {
        // TODO Auto-generated method stub
        
    }

    public void unload(SourcesTreeModelEvents sender) {
        // TODO Auto-generated method stub
        
    }


}
