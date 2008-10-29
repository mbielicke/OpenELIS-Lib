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

import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.DataSorterInt;
import org.openelis.gwt.common.data.Data;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.widget.table.event.SourcesTableModelEvents;

import java.util.ArrayList;

public interface TableModelInt extends SourcesTableModelEvents {

    public void addRow();
    
    public void addRow(int index);
    
    public void addRow(DataSet row);
    
    public void addRow(int index, DataSet row);
    
    public void deleteRow(int row);
        
    public void hideRow(int row);
    
    public void showRow(int row);
    
    public DataSet getRow(int row);
    
    public int numRows();
    
    public Data getObject(int row, int col);
    
    public void clear();
    
    public DataSet setRow(int index, DataSet row);
    
    public int shownRows();
    
    public DataSet createRow();
    
    public void load(DataModel data);
    
    public void selectRow(int index);
    
    public void selectRow(Data key);
    
    public void unselectRow(int index);
    
    public void clearSelections();
    
    public ArrayList<DataSet> getSelections();
    
    public DataSet getSelection();
    
    public boolean getAutoAdd();
    
    public DataSet getAutoAddRow();
    
    public void setAutoAddRow(DataSet row);
    
    public DataModel getData();
    
    public void setCell(int row, int col,Object obj);
    
    public Object getCell(int row, int col);
    
    public void sort(int col, DataSorterInt.SortDirection direction);
    
    public void refresh();
    
    public boolean isSelected(int index);
    
    public void enableMultiSelect(boolean multi);
    
    public void setModel(DataModel data);
    
    public boolean isEnabled(int index);
    
    public void setManager(TableManager manager);
    
    public boolean canSelect( int row);

    public boolean canEdit(int row, int col);

    public boolean canDelete(int row);

    public boolean canAdd(int row);
    
    public boolean canAutoAdd(DataSet autoAddRow);
    
    public boolean canDrag(int row);
    
    public boolean canDrop(Widget dropWidget,  int row);
    
    public void drop(Widget dropWidget, int targetRow);
    
    public DataModel unload();
    
    public boolean isAutoAdd();
    
    public void enableAutoAdd(boolean autoAdd);
    
    public int getSelectedIndex();
    
    public int[] getSelectedIndexes();
    
    public void setCellError(int row, int col, String Error);
    
    public void clearCellError(int row, int col);
    
    
    
    
}
