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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Class is deprecated should be deleted
 * @author tschmidt
 *
 */
@Deprecated public class MenuTree extends Composite {

    public Tree menu = new Tree();
    private VerticalPanel vp = new VerticalPanel();
    private ScrollPanel scroll = new ScrollPanel();

    public MenuTree(boolean size) {
        vp.add(menu);
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

    private void setBrowserHeight() {
        if (menu.isVisible()) {
            menu.setHeight((Window.getClientHeight() - menu.getAbsoluteTop() -10) + "px");
        }
    }

}
