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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
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
