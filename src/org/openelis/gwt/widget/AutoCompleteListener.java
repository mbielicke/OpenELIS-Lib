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

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;


/**
 * Implements a Key Listener for the AutoComplete widget to determine when a GetMatchesEvent should be fired.  Implemented in separate
 * class because DropdownWidget which is the super of AutoComplete implements the same handler for Keyboard navigation of the PopupTable
 *  
 * @author tschmidt
 *
 */
public class AutoCompleteListener implements KeyUpHandler {
    
    private AutoComplete widget;
   
    
    public AutoCompleteListener(AutoComplete widget){
        this.widget = widget;
    }

    /**
     * Catches key events from the AutoComplete widget and determines if getMatches should be called.  We set a delay so
     * that if multiple key strokes are entered quickly we don't call getMathces with calls that will most likely be never seen by the 
     * user. 
     */
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
