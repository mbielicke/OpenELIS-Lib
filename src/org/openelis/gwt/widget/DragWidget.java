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
package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.Label;
/**
 * This widget is used by the DragSelectWidget to 
 * drag items between the two selection boxes.
 * @author tschmidt
 *
 */
public class DragWidget extends Label {
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
