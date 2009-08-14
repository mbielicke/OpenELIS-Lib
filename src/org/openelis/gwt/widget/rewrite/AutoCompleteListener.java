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
package org.openelis.gwt.widget.rewrite;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

public class AutoCompleteListener implements
                                 ClickHandler,
                                 KeyUpHandler {
    
    private AutoComplete widget;
   
    
    public AutoCompleteListener(AutoComplete widget){
        this.widget = widget;
    }

    public void onClick(ClickEvent event) {

    }

    public void onKeyUp(KeyUpEvent event) {
        if(widget.queryMode)
            return;
        if (!widget.textbox.isReadOnly()) {
            if (event.getNativeKeyCode() == KeyboardHandler.KEY_DOWN || event.getNativeKeyCode() == KeyboardHandler.KEY_UP ||  event.getNativeKeyCode() == KeyboardHandler.KEY_TAB 
                    || event.getNativeKeyCode() == KeyboardHandler.KEY_LEFT || event.getNativeKeyCode() == KeyboardHandler.KEY_RIGHT || event.getNativeKeyCode() == KeyboardHandler.KEY_ALT || 
                    event.getNativeKeyCode() == KeyboardHandler.KEY_CTRL || event.getNativeKeyCode() == KeyboardHandler.KEY_SHIFT || event.getNativeKeyCode() == KeyboardHandler.KEY_ESCAPE)
                return;
            if(event.getNativeKeyCode() == KeyboardHandler.KEY_ENTER && !widget.popup.isShowing() && !widget.itemSelected && widget.focused){
                if(widget.activeRow < 0)
                    widget.showTable(0);
                else
                    widget.showTable(widget.modelIndexList[widget.activeRow]);
                return;
            }
            if(event.getNativeKeyCode() == KeyboardHandler.KEY_ENTER && widget.itemSelected){
                widget.itemSelected = false;
                return;
            }
            String text = widget.textbox.getText();
            if (text.length() > 0 && !text.endsWith("*")) {
                widget.setDelay(text, 350);
            } else {
                widget.hideTable();
            }
        }
    }

}
