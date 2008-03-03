package org.openelis.gwt.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

import org.openelis.gwt.screen.Screen;
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
    public HashMap windows = new HashMap();
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
        DeferredCommand.addCommand(new Command() {
            public void execute() {
        
                index++;
                ScreenWindow window = new ScreenWindow(brws, text, category, loadingText);
                window.setContent(screen);
                //  setIndex(window.getElement(),index);
                browser.add(window,(windows.size()*25),(windows.size()*25));
                windows.put(text,window);
        
                if(screen instanceof Screen){
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            ((Screen)screen).getXML(((Screen)screen).xmlUrl);
                        }
                    });
                }
            }
        });
    }
    
    public boolean selectScreen(String text) {
        if (windows.containsKey(text)) {
            if(index != ((ScreenWindow)windows.get(text)).zIndex){
                ScreenWindow wid = (ScreenWindow)windows.get(text);
                index++;
                //setIndex(((Widget)windows.get(text)).getElement(),index);
                ((ScreenWindow)windows.get(text)).zIndex = index;
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
            browser.setHeight((Window.getClientHeight() - browser
                                                                 .getAbsoluteTop()) + "px");
            browser.setWidth((Window.getClientWidth() - browser
                                                               .getAbsoluteLeft()) + "px");
        }
    }


 
}
