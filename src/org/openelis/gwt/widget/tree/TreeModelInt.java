package org.openelis.gwt.widget.tree;

import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.common.data.TreeDataModel;
import org.openelis.gwt.widget.tree.event.SourcesTreeModelEvents;

import java.util.ArrayList;

public interface TreeModelInt extends SourcesTreeModelEvents {
    
    public void addRow(TreeDataItem row);
    
    public void addRow(int index, TreeDataItem row);
    
    public void deleteRow(int row);
        
    public void hideRow(int row);
    
    public void showRow(int row);
    
    public TreeDataItem getRow(int row);
    
    public int numRows();
    
    public DataObject getObject(int row, int col);
    
    public void clear();
    
    public TreeDataItem setRow(int index, TreeDataItem row);
    
    public int shownRows();
    
    public void load(TreeDataModel data);
    
    public void selectRow(int index);
    
    public void selectRow(DataObject key);
    
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
        
    public TreeDataModel unload();
        
}
