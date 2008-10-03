package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.DataObject;

public interface TableColumnInt {
    
    public void  setHeader(String header);
    
    public String getHeader();
    
    public void setSortable(boolean sortable);
    
    public boolean getSortable();
    
    public void setFilterable(boolean filterable);
    
    public boolean getFilterable();
    
    public void setColumnWidget(Widget widget);
    
    public Widget getColumnWidget();
        
    public void setPreferredWidth(int width);
    
    public int getPreferredWidth();
    
    public void setCurrentWidth(int width);
    
    public int getCurrentWidth();
    
    public void setMinWidth(int width);
    
    public int getMinWidth();
    
    public void setFixedWidth(boolean fixed);
    
    public boolean getFixedWidth();
    
    public void setAlign(HasHorizontalAlignment.HorizontalAlignmentConstant align);
    
    public HasHorizontalAlignment.HorizontalAlignmentConstant getAlign();
    
    public Widget getWidgetInstance();
    
    public void loadWidget(Widget widget, DataObject object);
    
    public void setWidgetDisplay(Widget widget);
    
    public void saveValue(Widget widget);
    
    public void setWidgetEditor(Widget widget);
    
    public void setTableWidget(TableWidget controller);
    
    public TableWidget getTableWidget();
    
    public void enable(boolean enable);
    
    public Filter[] getFilter();
    
    public void setFilter(Filter[] filter);
    
    public void applyFilter();
    
    public void setKey(String key);
    
    public String getKey();
    
}
