package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import org.openelis.gwt.client.screen.Screen;
/** 
 * Deprecated class, probably will be deleted.
 * @author tschmidt
 *
 */
public abstract class AppWidgetBase extends Composite {
    Screen form;

    public AppWidgetBase(Screen form) {
        this.form = form;
    }

    public void onBrowserEvent(Event event) {
        form.doTab(event, this);
    }

}
