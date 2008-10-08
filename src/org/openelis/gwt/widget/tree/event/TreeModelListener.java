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
package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.common.data.TreeDataItem;

public interface TreeModelListener {
    
    public void cellUpdated(SourcesTreeModelEvents sender, int row , int cell);
    
    public void rowUpdated(SourcesTreeModelEvents sender,  int row);
    
    public void rowDeleted(SourcesTreeModelEvents sender,  int row);
    
    public void rowAdded(SourcesTreeModelEvents sender, int rows);
    
    public void dataChanged(SourcesTreeModelEvents sender);
    
    public void rowSelectd(SourcesTreeModelEvents sender, int row);
    
    public void rowUnselected(SourcesTreeModelEvents sender, int row);
    
    public void unload(SourcesTreeModelEvents sender);
    
    public void rowClosed(SourcesTreeModelEvents sender, int row, TreeDataItem item);
    
    public void rowOpened(SourcesTreeModelEvents sender, int row, TreeDataItem item);

}
