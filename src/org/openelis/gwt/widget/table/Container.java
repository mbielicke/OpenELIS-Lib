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
package org.openelis.gwt.widget.table;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

public class Container extends AbsolutePanel {
    
    protected int width;
    protected int height;
    protected Scheduler.ScheduledCommand command;
    protected Widget editor;
    
    public Container() {
        setStyleName("CellContainer");
        command = new Command() {
            public void execute() {
                ((Focusable)editor).setFocus(true);
            }
        };
    }
    
    public void setWidth(int width) {
        this.width = width;
        setWidth(width+"px");
    }
    
    public void setHeight(int height) {
        this.height = height;
        setHeight(height+"px");
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
   
    public void setEditor(Widget editor) {
        this.editor = editor;
        clear();
        add(editor);
        focusWidget();
    }
    
    public void focusWidget() {
        Scheduler.get().scheduleDeferred(command);
    }
   
}
