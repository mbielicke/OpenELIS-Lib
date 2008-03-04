package org.openelis.gwt.widget.table;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.ui.Composite;

import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.TableRow;
import org.openelis.gwt.widget.table.TableCellWidget;

import java.util.ArrayList;

public class TableWidget extends Composite {

    public TableController controller = new TableController();
    public ConstantsWithLookup constants = null;
    
    public TableWidget() {
        initWidget(controller.view);
    }
    /**
     * Sets the height of the table.
     */
    public void setHeight(String height) {
        controller.view.setHeight(height);
    }
    
    public void setMaxRows(int maxRows){
        controller.setMaxRows(maxRows);
    }

    /**
     * Sets the width of the table.
     */
    public void setWidth(String width) {
        controller.view.setWidth(width);
    }

    /**
     * Sets the TableManager for this table
     * 
     * @param manager
     */
    public void setManager(TableManager manager) {
        controller.setManager(manager);
    }

    /**
     * This method creates the editors that the table will use.
     * 
     * @param node
     */
    public void setEditors(TableCellWidget[] cells) {
        controller.setEditors(cells);
    }

    /**
     * This method will set the Model fields to be used by the table.
     * 
     * @param node
     */
    public void setFields(AbstractField[] fields) {

        controller.model.setFields(fields);
    }

    /**
     * This table will set the Column Aligns of the table.
     * 
     * @param node
     */
    public void setColAlign(HorizontalAlignmentConstant[] alignments) {

        controller.setColAlign(alignments);
    }

    /**
     * This Table will set any static filters defined for the table.
     * 
     * @param node
     */
    public void setStatFilter(ArrayList filters) {

        controller.setStatFilterable(filters);
    }

    public void setTableTitle(String title){
        controller.view.setTitle(title);
    }
    
    public void setAutoAdd(boolean autoAdd){
        controller.setAutoAdd(autoAdd);
    }
    
    public void setShowRows(boolean showRows) {
        controller.setShowRows(showRows);
    }
    
    public void setColWidths(int[] width){
        controller.setColWidths(width);
    }
    
    public void setHeaders(String[] headers){
        controller.view.setHeaders(headers);
    }
    
    public void setFilterable(boolean[] filterable){
        controller.setFilterable(filterable);
    }
    
    public void setSortable(boolean[] sort){
        controller.setSortable(sort);
    }
    
    public void initService(String url) {
        controller.initService(url);
    }
    
    public void setCellHeight(int height){
        controller.setCellHeight(height);
    }
    
    /**
     * This method sets the model in the screen. rows = -1 means a serviceUrl
     * has been provided and the getModel method will be called to populate the
     * table.
     * 
     * @param rows
     */
    public void init(int rows) {
        controller.view.initTable(controller);
        controller.setView(controller.view);
        if (rows < 0)
            controller.getModel();
        else {
            for (int m = 0; m < rows; m++) {
                TableRow row = controller.model.createRow();
                controller.model.addRow(row);
            }
        }
        controller.sizeTable();
    }
    
    public void enable(boolean enabled){
        controller.enabled(enabled);
    }
    
    public void setEnable(boolean enabled){
        controller.enabled = true;
    }
    
    
    public boolean isVisible() {
        // TODO Auto-generated method stub
        return super.isVisible();
    }
}
