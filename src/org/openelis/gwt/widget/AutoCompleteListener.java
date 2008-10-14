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
package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class AutoCompleteListener implements
                                 ClickListener,
                                 MouseListener,
                                 KeyboardListener {
    
    private AutoComplete widget;
   
    
    public AutoCompleteListener(AutoComplete widget){
        this.widget = widget;
    }

    public void onClick(Widget sender) {
        if(sender == widget.focusPanel){
            if(widget.activeRow < 0)
                widget.showTable(0);
            else
                widget.showTable(widget.modelIndexList[widget.activeRow]);
        }

    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub

    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub

    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub

    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub

    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        if (!widget.textBox.isReadOnly()) {
            if (keyCode == KEY_DOWN || keyCode == KEY_UP ||  keyCode == KEY_TAB 
                    || keyCode == KEY_LEFT || keyCode == KEY_RIGHT || keyCode == KEY_ALT || 
                    keyCode == KEY_CTRL || keyCode == KEY_SHIFT || keyCode == KEY_ESCAPE)
                return;
            if(keyCode == KEY_ENTER && !widget.popup.isShowing() && !widget.itemSelected && widget.focused){
                if(widget.activeRow < 0)
                    widget.showTable(0);
                else
                    widget.showTable(widget.modelIndexList[widget.activeRow]);
                return;
            }
            if(keyCode == KEY_ENTER && widget.itemSelected){
                widget.itemSelected = false;
                return;
            }
            String text = widget.textBox.getText();
            if (text.length() > 0 && !text.endsWith("*")) {
                widget.setDelay(text, 350);
            } else if(text.length() == 0) {
                widget.model.clear();
            } else {
                widget.hideTable();
            }
        }
    }

}
