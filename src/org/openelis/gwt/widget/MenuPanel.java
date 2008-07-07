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
package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuPanel extends Composite {
    
    String layout;
    private CellPanel panel;

    
    public MenuPanel(String layout) {
        if(layout.equals("vertical"))
            panel = new VerticalPanel();
        else
            panel = new HorizontalPanel();
        initWidget(panel);
    }
    
    public void add(Widget wid){
        panel.add(wid);
    }
    
    public void clear(){
        panel.clear();
    }
    

}
