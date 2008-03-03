package org.openelis.gwt.widget;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
/**
 * AppMesage is a widget that will display a styled message on a
 * screen for a user.  It can be hidden when not needed and can be
 * set to display for a number of seconds.
 * 
 * @author tschmidt
 *
 */
public class AppMessage extends Composite{
    
    public  HTML message = new HTML();
    private Timer timer = new Timer() {
        public void run() {
            hide();
        }
    };

    /**
     * Default no-arg constructor
     */
    public AppMessage(){
        setMessage("A");
        hide();
        initWidget(message);
    }
    
    /**
     * Pass a message to be displayed on the screen.  The message will not be seen until 
     * show() is called.
     * 
     * @param text
     */
    public void setMessage(String text) {
        message.setHTML("<span class='AppMessage'>&nbsp;&nbsp;" + text
                        + "&nbsp;&nbsp;</span>");
    }

    /**
     * Getter for returning HTML message that is displayed
     * @return
     */
    public HTML getMessage() {
        return message;
    }

    /**
     * Call this method to hide the current message
     */
    public void hide() {
        message.addStyleName("invisible");
    }

    /**
     * Call this method to show the currently set message
     */
    public void show() {
        message.removeStyleName("invisible");
    }

    /**
     * Call this method passing in the number of seconds to  display
     * the currently set message before hidding it.
     * @param seconds
     */
    public void show(int seconds) {
        timer.schedule(seconds * 1000);
        show();
    }
}
