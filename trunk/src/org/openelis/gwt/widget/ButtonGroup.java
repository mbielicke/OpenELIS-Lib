package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.Iterator;

import org.openelis.gwt.widget.AppButton.ButtonState;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget class implements a a panel of buttons where only one button can be clicked at a time.  An object that handles
 * clicks for all the buttons in the group only needs to register once to this widget.  Used Mostly for AZButtons
 * @author tschmidt
 *
 */
public class ButtonGroup extends Composite implements HasClickHandlers {

    private ArrayList<AppButton> buttons = new ArrayList<AppButton>();

    public ButtonGroup() {
    }

    /**
     * Sets the panel of buttons to the group. The widget will walk through the Panel finding all the buttons registering
     * them to the group.
     * @param panel
     */
    public void setButtons(Panel panel) {
        initWidget(panel);
        findButtons(panel);
    }

    private void findButtons(HasWidgets hw) {
        Iterator<Widget> widsIt = hw.iterator();
        while (widsIt.hasNext()) {
            Widget wids = widsIt.next();
            if (wids instanceof AppButton) {
                buttons.add((AppButton)wids);
            } else if (wids instanceof HasWidgets) {
                findButtons((HasWidgets)wids);
            }
        }
    }

    /**
     * Registers all buttons in this group to handler that is passed to the method
     */
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        for (AppButton button : buttons) {
            button.addClickHandler(handler);
        }
        return null;
    }

    /**
     * enables or disables the buttons in this group based on the boolean passed
     * @param enabled
     */
    public void enable(boolean enabled) {
        for (AppButton button : buttons) {
            if ( !enabled)
                button.setState(ButtonState.DISABLED);
            else
                button.setState(ButtonState.UNPRESSED);
        }
    }
}
