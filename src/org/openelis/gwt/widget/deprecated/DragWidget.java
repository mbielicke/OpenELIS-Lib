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
package org.openelis.gwt.widget.deprecated;

import com.google.gwt.user.client.ui.Label;
/**
 * This widget is used by the DragSelectWidget to 
 * drag items between the two selection boxes.
 * @author tschmidt
 *
 */
@Deprecated public class DragWidget extends Label {
    public boolean selected;
    public Object value;

    public DragWidget(String label, Object value, boolean selected) {
        super(label);
        setWordWrap(false);
        setStyleName("DragItem");
        this.value = value;
        this.selected = selected;
    }

    public DragWidget(DragWidget drag) {
        super(drag.getText());
        setWordWrap(false);
        setStyleName("DragItem");
        this.selected = drag.selected;
        this.value = drag.value;
    }
}