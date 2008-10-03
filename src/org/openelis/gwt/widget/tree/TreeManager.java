/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.widget.tree;

import org.openelis.gwt.common.data.TreeDataItem;


public interface TreeManager {
    
    public boolean canSelect(TreeDataItem set, int row);

    public boolean canEdit(TreeDataItem set, int row, int col);

    public boolean canDelete(TreeDataItem set, int row);

    public boolean canAdd(TreeDataItem set, int row);
    
    public boolean canOpen(TreeDataItem addRow, int row);
    
    public boolean canClose(TreeDataItem set, int row);
    
}
