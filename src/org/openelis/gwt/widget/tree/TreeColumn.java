package org.openelis.gwt.widget.tree;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

import org.openelis.gwt.common.DataFilterer;
import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.table.TableWidget;

public class TreeColumn implements TreeColumnInt {

    public String header;
    public boolean sortable;
    public boolean filterable;
    public TableCellWidget cellWidget;
    public int preferredWidth;
    public int currentWidth;
    public int minWidth;
    public boolean fixedWidth;
    public HasHorizontalAlignment.HorizontalAlignmentConstant alignment = HasHorizontalAlignment.ALIGN_LEFT;
    public TreeWidget controller;
    public DataFilterer dataFilterer = new DataFilterer();
    public int columnIndex;
    public Filter[] filters;
    public String key;
    
    
    public Widget getWidgetInstance() {
        TableCellWidget tcell = cellWidget.getNewInstance();
        tcell.setCellWidth(currentWidth);
        ((SimplePanel)tcell).setWidth((currentWidth)+ "px");
        ((SimplePanel)tcell).setHeight((controller.cellHeight+"px"));
        return (Widget)tcell;
    }
    
    public void loadWidget(Widget widget, DataObject object) {
        ((TableCellWidget)widget).setField(object);
        ((TableCellWidget)widget).setDisplay();
        if(widget instanceof TableTree) {
            ((TableTree)widget).removeCommandListener(controller);
            ((TableTree)widget).addCommandListener(controller);
        }
    }
    
    public void setWidgetDisplay(Widget widget) {
        ((TableCellWidget)widget).setDisplay();
    }
    
    public void saveValue(Widget widget) {
        ((TableCellWidget)widget).saveValue();
    }
    
    public void setWidgetEditor(Widget widget) {
        ((TableCellWidget)widget).setEditor();
        ((TableCellWidget)widget).setFocus(true);
    }

    public void enable(boolean enable) {
        cellWidget.enable(enable);
    }
    
    public HorizontalAlignmentConstant getAlign() {
        return alignment;
    }

    public Widget getColumnWidget() {
        return (Widget)cellWidget;
    }

    public int getCurrentWidth() {
        return currentWidth;
    }

    public boolean getFilterable() {
        return filterable;
    }

    public boolean getFixedWidth() {
        return fixedWidth;
    }

    public String getHeader() {
        return header;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public boolean getSortable() {
        return sortable;
    }

    public void setAlign(HorizontalAlignmentConstant align) {
        alignment = align;
    }

    public void setColumnWidget(Widget widget) {
        cellWidget = (TableCellWidget)widget;
    }

    public void setCurrentWidth(int width) {
        currentWidth = width;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    public void setFixedWidth(boolean fixed) {
        fixedWidth = fixed;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setMinWidth(int width) {
        minWidth = width;
    }

    public void setPreferredWidth(int width) {
        preferredWidth = width;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }
    
    public void setTreeWidget(TreeWidget controller){
        this.controller = controller;
        if(cellWidget instanceof TableTree)
            ((TableTree)cellWidget).addCommandListener(controller);
    }
    
    public TreeWidget getTreeWidget() {
        return controller;
    }
    /*
    public Filter[] getFilter() {
        Filter[] filter = dataFilterer.getFilterValues(controller.model.getData(),controller.columns.indexOf(this));
        if (filters != null) {
            for (int j = 0; j < filter.length; j++) {
                for (int k = 0; k < filters.length; k++) {
                    if (filter[j].obj.equals(filters[k].obj)) {
                        filter[j].filtered = filters[k].filtered;
                        k = filters.length;
                    }
                }
            }
        }
        return filter;
    }
    
    public void setFilter(Filter[] filter) {
        filters = filter;
    }
    
    public void applyFilter() {
        dataFilterer.applyFilter(controller.model.getData(), filters, controller.columns.indexOf(this));
    }
*/
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        
    }

    public void applyFilter() {
        // TODO Auto-generated method stub
        
    }

    public Filter[] getFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setFilter(Filter[] filter) {
        // TODO Auto-generated method stub
        
    }
    
}
