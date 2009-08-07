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

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

import org.openelis.gwt.common.DataFilterer;
import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.Field;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.widget.table.TableCellWidget;

import java.util.HashMap;
@Deprecated
public class TreeColumn implements TreeColumnInt {

    public String header;
    public boolean sortable;
    public boolean filterable;
    public HashMap<String,TableCellWidget> cellMap = new HashMap<String,TableCellWidget>();
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
    
    
    public Widget getWidgetInstance(String leafType) {
        TableCellWidget tcell = cellMap.get(leafType).getNewInstance();
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
        for(TableCellWidget cellWidget : cellMap.values())
            cellWidget.enable(enable);
    }
    
    public HorizontalAlignmentConstant getAlign() {
        return alignment;
    }

    public Widget getColumnWidget(String leafType) {
        return (Widget)cellMap.get(leafType);
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

    public void setColumnWidget(Widget widget, String leafType) {
        cellMap.put(leafType,(TableCellWidget)widget);
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
        for(TableCellWidget cellWidget : cellMap.values()){
            if(cellWidget instanceof TableTree)
                ((TableTree)cellWidget).addCommandListener(controller);
        }
    }
    
    public TreeWidget getTreeWidget() {
        return controller;
    }
    
    public Filter[] getFilter() {
        return null;
    }
    
    public void setFilter(Filter[] filter) {
        
    }
    
    public void applyFilter() {
        
    }

    public void applyQueryFilter() {
       
    }
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        
    }

    
}
