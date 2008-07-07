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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.openelis.gwt.screen.ScreenLabel;
/**
 * Class is deprecated should be deleted
 * @author tschmidt
 *
 */
public class MenuList extends Composite{
    
    private VerticalPanel vp = new VerticalPanel();
    private ScrollPanel scroll = new ScrollPanel();
    
    public MenuList(boolean size){
        if (size) {
            scroll.add(vp);
            initWidget(scroll);
            Window.addWindowResizeListener(new WindowResizeListener() {
                public void onWindowResized(int width, int height) {
                    setBrowserHeight();
                }
            });
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    setBrowserHeight();
                }
            });
        }else{
            initWidget(vp);
        }
    }
    
    public void add(String text, String cat){
        Label label = new Label(text);
        label.addStyleName(cat);
        vp.add(label);
    }
    
    public void add(ScreenLabel label){
        vp.add(label);
    }
   
    private void setBrowserHeight() {
        if (isVisible()) {
            setHeight((Window.getClientHeight() - getAbsoluteTop() -10) + "px");
        }
    }
    
    public void clear() {
        vp.clear();
    }

}
