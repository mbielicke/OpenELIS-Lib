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
