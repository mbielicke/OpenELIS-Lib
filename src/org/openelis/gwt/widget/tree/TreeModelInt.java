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

import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.Field;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.common.data.TreeDataModel;
import org.openelis.gwt.widget.tree.event.SourcesTreeModelEvents;

import java.util.ArrayList;
import java.util.HashMap;

public interface TreeModelInt extends SourcesTreeModelEvents {
    
    public void addRow(TreeDataItem row);
    
    public void addRow(int index, TreeDataItem row);
    
    public void deleteRow(int row);
        
    public void hideRow(int row);
    
    public void showRow(int row);
    
    public TreeDataItem getRow(int row);
    
    public int numRows();
    
    public Field getObject(int row, int col);
    
    public void clear();
    
    public TreeDataItem setRow(int index, TreeDataItem row);
    
    public int shownRows();
    
    public void load(TreeDataModel data);
    
    public void selectRow(int index);
    
    public void unselectRow(int index);
    
    public void clearSelections();
    
    public ArrayList<TreeDataItem> getSelections();
    
    public TreeDataItem getSelection();
        
    public TreeDataModel getData();
    
    public void setCell(int row, int col,Object obj);
    
    public Object getCell(int row, int col);
        
    public void refresh();
    
    public boolean isSelected(int index);
    
    public void enableMultiSelect(boolean multi);
    
    public void setModel(TreeDataModel data);
    
    public boolean isEnabled(int index);
    
    public void setManager(TreeManager manager);
    
    public boolean canSelect( int row);

    public boolean canEdit(int row, int col);

    public boolean canDelete(int row);

    public boolean canAdd(int row);
    
    public boolean canOpen(int row);
    
    public boolean canClose(int row);
    
    public boolean canDrag(int row);
    
    public boolean canDrop(Widget dropWidget, int targetRow);
    
    public void drop(Widget dropWidget, int targetRow);
    
    public void drop(Widget dragWidget);
        
    public TreeDataModel unload();
    
    public int getSelectedRowIndex();
    
    public int[] getSelectedRowIndexes();
    
    public void setCellError(int row, int col, String Error);
    
    public void clearCellError(int row, int col);
    
    public TreeDataItem createTreeItem(String leafType);
    
    public void setLeaves(HashMap<String,TreeDataItem> leaves);
        
}
