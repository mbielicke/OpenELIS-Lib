package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget class implements a a panel of buttons where only one button can be clicked at a time.  An object that handles
 * clicks for all the buttons in the group only needs to register once to this widget.  Used Mostly for AZButtons
 * @author tschmidt
 *
 */
public class ButtonGroup extends SimplePanel implements HasClickHandlers {

    private ArrayList<Button> buttons = new ArrayList<Button>();

    public ButtonGroup() {
    }

    /**
     * Sets the panel of buttons to the group. The widget will walk through the Panel finding all the buttons registering
     * them to the group.
     * @param panel
     */
    public void setButtons(Panel panel) {
    	super.setWidget(panel);
        findButtons(panel);
    }

    private void findButtons(HasWidgets hw) {
        Iterator<Widget> widsIt = hw.iterator();
        while (widsIt.hasNext()) {
            Widget wids = widsIt.next();
            if (wids instanceof Button) {
                buttons.add((Button)wids);
            } else if (wids instanceof HasWidgets) {
                findButtons((HasWidgets)wids);
            }
        }
    }

    /**
     * Registers all buttons in this group to handler that is passed to the method
     */
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        for (Button button : buttons) {
            button.addClickHandler(handler);
        }
        return null;
    }

    /**
     * enables or disables the buttons in this group based on the boolean passed
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        for (Button button : buttons) {
            button.setEnabled(enabled);
        }
    }

	@Override
	public void setWidget(IsWidget w) {
		setButtons((Panel)w);
	}
	
	@Override
	public void setWidget(Widget w) {
		setButtons((Panel)w);
	}

}
