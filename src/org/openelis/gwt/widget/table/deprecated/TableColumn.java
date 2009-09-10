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
package org.openelis.gwt.widget.table.deprecated;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

import org.openelis.gwt.common.data.deprecated.FieldType;
import org.openelis.gwt.common.deprecated.DataFilterer;
import org.openelis.gwt.common.deprecated.Filter;
import org.openelis.gwt.screen.deprecated.ScreenMenuItem;

@Deprecated
public class TableColumn implements TableColumnInt {

    public String header;
    public ScreenMenuItem headerMenu;
    public boolean sortable;
    public boolean filterable;
    public boolean queryable;
    public TableCellWidget cellWidget;
    public int preferredWidth;
    public int currentWidth;
    public int minWidth;
    public boolean fixedWidth;
    public HasHorizontalAlignment.HorizontalAlignmentConstant alignment = HasHorizontalAlignment.ALIGN_LEFT;
    public TableWidget controller;
    public DataFilterer dataFilterer = new DataFilterer();
    public int columnIndex;
    public Filter[] filters;
    public String key;
    public boolean filterDisplayed = false;
    public String query;
    
    
    public Widget getWidgetInstance() {
        TableCellWidget tcell = cellWidget.getNewInstance();
        tcell.setCellWidth(currentWidth);
        ((SimplePanel)tcell).setWidth((currentWidth)+ "px");
        ((SimplePanel)tcell).setHeight((controller.cellHeight+"px"));
        return (Widget)tcell;
    }
    
    public void loadWidget(Widget widget, FieldType object) {
        ((TableCellWidget)widget).setField(object);
        ((TableCellWidget)widget).setDisplay();
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
    
    public void setTableWidget(TableWidget controller){
        this.controller = controller;
    }
    
    public TableWidget getTableWidget() {
        return controller;
    }
    
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
    
    public void applyQueryFilter() {
        dataFilterer.applyQueryFilter(controller.model.getData(),query,controller.columns.indexOf(this));
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        
    }
    
    public void setHeaderMenu(ScreenMenuItem menu) {
        this.headerMenu = menu;
    }

    public ScreenMenuItem getHeaderMenu() {
        return headerMenu;
    }

    public boolean queryable() {
        return queryable;
    }

    public void setQuerayable(boolean queryable) {
        this.queryable = queryable;
    }
    
}
