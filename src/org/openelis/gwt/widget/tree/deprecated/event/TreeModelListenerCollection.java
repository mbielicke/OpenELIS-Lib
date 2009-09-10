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
package org.openelis.gwt.widget.tree.deprecated.event;

import org.openelis.gwt.common.data.deprecated.TreeDataItem;

import java.util.ArrayList;
@Deprecated
public class TreeModelListenerCollection extends ArrayList<TreeModelListener> {
    
    public void fireCellUpdated(SourcesTreeModelEvents sender, int row, int col){
        for(TreeModelListener listener : this)
            listener.cellUpdated(sender,row,col);
    }
    
    public void fireRowUpdated(SourcesTreeModelEvents sender, int row){
        for(TreeModelListener listener : this) 
            listener.rowUpdated(sender, row);
    }

    public void fireRowDeleted(SourcesTreeModelEvents sender, int row) {
        for(TreeModelListener listener : this)
            listener.rowDeleted(sender, row);
    }
    
    public void fireRowAdded(SourcesTreeModelEvents sender, int row) {
        for(TreeModelListener listener : this)
            listener.rowAdded(sender, row);
    }
    
    public void fireDataChanged(SourcesTreeModelEvents sender) {
        for(TreeModelListener listener : this)
            listener.dataChanged(sender);
    }
    
    public void fireRowSelected(SourcesTreeModelEvents sender, int row){
        for(TreeModelListener listener : this)
            listener.rowSelectd(sender, row);
    }
    
    public void fireRowUnselected(SourcesTreeModelEvents sender, int row) {
        for(TreeModelListener listener : this)
            listener.rowUnselected(sender, row);
    }
    
    public void fireUnload(SourcesTreeModelEvents sender) {
        for(TreeModelListener listener : this)
            listener.unload(sender);
    }
    
    public void fireRowOpened(SourcesTreeModelEvents sender, int row, TreeDataItem item) {
        for(TreeModelListener listener : this)
            listener.rowOpened(sender,row,item);
    }
    
    public void fireRowClosed(SourcesTreeModelEvents sender, int row, TreeDataItem item) {
        for(TreeModelListener listener : this)
            listener.rowClosed(sender,row,item);
    }
}
