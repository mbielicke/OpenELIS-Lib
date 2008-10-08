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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.ScreenBase;

import java.util.ArrayList;
/**
 * TabBrowser is a widget that will display screens separated into tabs
 * @author tschmidt
 *
 */
public class TabBrowser extends Composite {

    private TabPanel browser = new TabPanel();
    private ArrayList tabs = new ArrayList();
    private int tabLimit = 3;
    private int heightAdj = 8;
    private Widget sizeWidget;

    public TabBrowser(boolean size, int tabLimit) {
        this.tabLimit = tabLimit;
        initWidget(browser);
        setStyleName("TabBrowser");
        browser.setVisible(false);
        DOM.setStyleAttribute(browser.getDeckPanel().getElement(),
                              "overflow",
                              "auto");
        if(size){
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
        }
    }

    public void addScreen(ScreenBase screen, final String text, String category) {
        if (!browser.isVisible()) {
            browser.setVisible(true);
        }
        if (tabs.contains(text)) {
            int index = tabs.indexOf(text);
            browser.selectTab(index);
            return;
        }
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);
        hp.add(new MenuLabel(category, text));
        Image close = new Image("Images/close.png");
        hp.add(close);
        hp.setCellHorizontalAlignment(hp.getWidget(0), HasAlignment.ALIGN_LEFT);
        hp.setCellHorizontalAlignment(hp.getWidget(1), HasAlignment.ALIGN_RIGHT);
        hp.setWidth("100%");
        browser.add(screen, hp);
        int index = browser.getTabBar().getTabCount() - 1;
        tabs.add(text);
        browser.selectTab(index);
        close.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                closeScreen(text);
            }
        });
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                setBrowserHeight();
            }
        });
    }

    public void closeScreen(String text) {
        closeScreen(tabs.indexOf(text));
    }

    public void closeScreen(int index) {
        browser.remove(index);
        tabs.remove(index);
        if (browser.getDeckPanel().getVisibleWidget() == -1 && tabs.size() > 0) {
            if (index > 0)
                browser.selectTab(index - 1);
            else
                browser.selectTab(0);
        }
        if (tabs.size() == 0)
            browser.setVisible(false);
    }

    public boolean selectScreen(String text) {
        if (tabs.contains(text)) {
            int index = tabs.indexOf(text);
            browser.selectTab(index);
            return true;
        }
        if (tabs.size() == tabLimit) {
            Window.alert("Please close at least one Screen before opening another.");
            return true;
        }
        return false;
    }

    private void setBrowserHeight() {
        if (browser.isVisible()) {
            browser.getDeckPanel()
                   .setHeight((Window.getClientHeight() - browser.getDeckPanel()
                                                                 .getAbsoluteTop() - 10) + "px");
            browser.getDeckPanel()
                   .setWidth((Window.getClientWidth() - browser.getDeckPanel()
                                                               .getAbsoluteLeft() - 10) + "px");
        }

    }

}
