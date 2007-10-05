package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.client.screen.ScreenWindow;

import java.util.HashMap;

import org.openelis.gwt.client.screen.Screen;
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
    public int index ;
    public int limit;
    
    public static native void setIndex(Element elem, int index) /*-{
        elem.style.zIndex = index;
    }-*/;
    
    public static native int getIndex(Element elem) /*-{
        return Number(elem.style.zIndex);
    }-*/;
    
    
    public WindowBrowser(boolean size, int limit) {
        this.limit = limit;
        initWidget(browser);
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
                    index = getIndex(browser.getElement());
                }
            });
        }
    }
    
    public void addScreen(final Screen screen, final String text, String category) {
        if(windows.size() == limit){
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        if (windows.containsKey(text)) {
            return;
        }
        index++;
        ScreenWindow window = new ScreenWindow(this, text, category);
        window.setContent(screen);
        setIndex(window.getElement(),index);
        browser.add(window,(windows.size()*25),(windows.size()*25));
        windows.put(text,window);
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                screen.getXML(screen.xmlUrl);
            }
        });
    }
    
    public boolean selectScreen(String text) {
        if (windows.containsKey(text)) {
            if(index != ((ScreenWindow)windows.get(text)).zIndex){
                index++;
                setIndex(((Widget)windows.get(text)).getElement(),index);
                ((ScreenWindow)windows.get(text)).zIndex = index;
            }
            return true;
        }
        return false;
    }
    
    private void setBrowserHeight() {
        if (browser.isVisible()) {
            browser.setHeight((Window.getClientHeight() - browser
                                                                 .getAbsoluteTop() - 10) + "px");
            browser.setWidth((Window.getClientWidth() - browser
                                                               .getAbsoluteLeft() - 10) + "px");
        }
    }


 
}
