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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenWindow;

import java.util.HashMap;

/**
 * WindowBrowser will display Screen widgets in draggable Windows
 * in a certain portion of the screen.  It uses the ScreenWindow
 * widget to wrap the contents of a screen.  It also control the 
 * z-index of the windows displayed.
 * 
 * @author tschmidt
 *
 */
public class WindowBrowser extends Composite{
    
    public AbsolutePanel browser = new AbsolutePanel();
    public HashMap<String,ScreenWindow> windows = new HashMap<String,ScreenWindow>();
    public int index;
    public int limit ;
    
    public static native void setIndex(Element elem, int index) /*-{
        elem.style.zIndex = index;
    }-*/;
    
    public static native int getIndex(Element elem) /*-{
        return Number(elem.style.zIndex);
    }-*/;
    
    
    public WindowBrowser(boolean size, int limit) {
        this.limit = limit;
        initWidget(browser);
        //setIndex(getElement(),index);
        DOM.setStyleAttribute(browser.getElement(),
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
                    //index = getIndex(browser.getElement());
                }
            });
        }
    }
    
    public void addScreen(final ScreenBase screen, final String text, final String category, final String loadingText) {
        if(windows.size() == limit){
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        if (windows.containsKey(text)) {
            return;
        }
        RootPanel.get().addStyleName("ScreenLoad");
        final WindowBrowser brws = this;
        index++;
        ScreenWindow window = new ScreenWindow(brws, text, category, loadingText);
        window.setContent(screen);
        browser.add(window,(windows.size()*25),(windows.size()*25));
        windows.put(text,window);
        if(screen instanceof AppScreen){
            DOM.addEventPreview((AppScreen)screen);
        }
    }
    
    public void addScreen(AppScreen screen) {
        addScreen(screen,null);
    }
    
    public void addScreen(AppScreen screen, String key) {
        if(key == null)
           key = GWT.getTypeName(screen);
        if(windows.size() == limit){
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        if (windows.containsKey(key)) {
            selectScreen(key);
            return;
        }
        RootPanel.get().addStyleName("ScreenLoad");
        index++;
        ScreenWindow window = new ScreenWindow(this, key);
        window.setContent(screen);
        browser.add(window,(windows.size()*25),(windows.size()*25));
        windows.put(key,window);
    }
    
    public boolean selectScreen(String text) {
        if (windows.containsKey(text)) {
            if(index != windows.get(text).zIndex){
                ScreenWindow wid = windows.get(text);
                index++;
                //setIndex(((Widget)windows.get(text)).getElement(),index);
                windows.get(text).zIndex = index;
                int top = browser.getWidgetTop(wid);
                int left = browser.getWidgetLeft(wid);
                wid.setKeep(true);
                browser.add(wid, left, top);
                wid.setKeep(false);
            }
            return true;
        }
        return false;
    }
    
    public void setBrowserHeight() {
        if (browser.isVisible()) {
            browser.setHeight((Window.getClientHeight() - browser.getAbsoluteTop()) + "px");
            browser.setWidth((Window.getClientWidth() - browser.getAbsoluteLeft())+ "px");
        }
    }


 
}
