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
package org.openelis.gwt.common;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.Field;
import org.openelis.gwt.common.data.FieldType;

public class DataSorter implements DataSorterInt {
    
    DataModel<?> data;
    SortDirection direction;
    
    public void sort(DataModel<?> data, int col, SortDirection direction) {
        this.data = data;
        this.direction = direction;
        quicksort(0, data.size() - 1, col);
    }

    public void quicksort(int p, int r, int col) {
        if (p < r) {
            int q = partition(p, r, col);
            quicksort(p, q, col);
            quicksort(q + 1, r, col);
        }
    }

    public int partition(int s, int t, int col) {
        FieldType x = data.get(s).get(col);
        int i = s - 1;
        int j = t + 1;
        while (true) {
            if (direction == SortDirection.DOWN) {
                while (((Field)data.get(--j).get(col)).compareTo(x) > 0)
                    ;
                while (((Field)data.get(++i).get(col)).compareTo(x) < 0)
                    ;
            } else {
                while (((Field)data.get(--j).get(col)).compareTo(x) < 0)
                    ;
                while (((Field)data.get(++i).get(col)).compareTo(x) > 0)
                    ;
            }
            if (i < j) {
                data.set(j, data.set(i, data.get(j)));
            } else {
                return j;
            }
        }
    }

}
