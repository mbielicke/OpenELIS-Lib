package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.widget.table.event.SourcesTableWidgetEvents;
import org.openelis.gwt.widget.table.event.TableWidgetListener;

public class QueryTableRenderer implements TableRendererInt, TableWidgetListener {
    
    private QueryTable controller;
    
    public QueryTableRenderer(QueryTable controller){
        this.controller = controller;
    }

    public void createRow(int i) {
        int j = 0;
        for(TableColumnInt column : controller.columns) {
            controller.view.table.setWidget(0,j,column.getWidgetInstance());
            controller.view.table.getFlexCellFormatter().addStyleName(0,
                                                  j,
                                                  TableView.cellStyle);
            controller.view.table.getFlexCellFormatter()
                          .setHorizontalAlignment(0, j, column.getAlign());

            controller.view.table.getFlexCellFormatter().setWidth(0, j, column.getCurrentWidth() + "px");
            controller.view.table.getFlexCellFormatter().setHeight(0, j, controller.cellHeight+"px");
            j++;
        }        
    }
    
    private void loadRow(int index, int modelIndex) {
        //controller.modelIndexList[index] = modelIndex;     
        //DataSet row = controller.model.getRow(modelIndex);

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
/*            if(controller.model.isSelected(modelIndex))
                controller.view.table.getRowFormatter().addStyleName(index, controller.view.selectedStyle);
            else
                controller.view.table.getRowFormatter().removeStyleName(index,controller.view.selectedStyle);
*/
        
    }

    public void load(int pos) {
       if(controller.view.table.getRowCount() == 0)
           createRow(0);
       for (int i = 0; i < controller.columns.size(); i++) 
           controller.columns.get(i).loadWidget(controller.view.table.getWidget(0, i),controller.rpc.getField(controller.columns.get(i).getKey()));
        
    }

    public void scrollLoad(int scrollPos) {
        // TODO Auto-generated method stub
        
    }

    public void setCellDisplay(int row, int col) {
        controller.columns.get(col).setWidgetDisplay(controller.view.table.getWidget(row, col));  
        
    }

    public void setCellEditor(int row, int col) {
        TableCellWidget cell =  (TableCellWidget)controller.view.table.getWidget(row, col);
        controller.editingCell = cell;
        controller.columns.get(col).setWidgetEditor((Widget)cell);
        ((SimplePanel)cell).getWidget().addStyleName(controller.view.widgetStyle);
        ((SimplePanel)cell).getWidget().addStyleName("Enabled");
    }

    public void finishedEditing(SourcesTableWidgetEvents sender, int row, int col) {
        if(controller.editingCell != null){
            controller.columns.get(controller.activeCell).saveValue((Widget)controller.editingCell);
            setCellDisplay(row,col);
            controller.editingCell = null;
        }
    }

    public void startedEditing(SourcesTableWidgetEvents sender, int row, int col) {
        setCellEditor(0,col);
        
    }

}
