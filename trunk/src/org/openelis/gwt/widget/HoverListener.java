package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.ScreenWidget;
/**
 * This class is used to provide the default hover logic for 
 * screen widgets.  It must be included in your WidgetMap for 
 * your application to be available.
 * @author tschmidt
 *
 */
public class HoverListener extends MouseListenerAdapter {
    
   public void onMouseEnter(Widget arg0) {
       arg0.addStyleName(((ScreenWidget)arg0).hoverStyle);
   }
   
   public void onMouseLeave(Widget arg0) {
       arg0.removeStyleName(((ScreenWidget)arg0).hoverStyle);   
   }

}
