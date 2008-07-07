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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.widget.table.TableController;

public interface TableManager {
    public boolean canSelect(int row, TableController controller);

    public boolean canEdit(int row, int col, TableController controller);

    public boolean canDelete(int row, TableController controller);

    public boolean action(int row, int col, TableController controller);

    public boolean canInsert(int row, TableController controller);

    public void finishedEditing(int row, int col, TableController controller);
    
    public boolean doAutoAdd(int row, int col, TableController controller);
    
    public void rowAdded(int row, TableController controller);
    
    public void getPage(int page);
    
    public void getNextPage(TableController controller);
    
    public void getPreviousPage(TableController controller);
    
    public void setModel(TableController controller, DataModel model);
    
    public void setMultiple(int row, int col, TableController controller);
    
}
