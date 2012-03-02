/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.Util;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Grid;

/**
 * This class is used by OpenELIS Screens to display and input values in forms
 * and in table cells as a Drop down list selector. 
 * 
 * @param <T>
 */
public class DropSelection<T> extends SelectionBox<T> {

    /**
     * Used for Dropdown display
     */
	
	protected Grid 					                display;
    protected Button  					            button;

    /**
     * Default no-arg constructor
     */
    public DropSelection() {
    	super();
    
        display = new Grid(1,2);
        display.setCellPadding(0);
        display.setCellSpacing(0);
    
        /*
         * New constructor in Button to drop the border and a div with the
         * passed style.
         */
        button = new Button();
        AbsolutePanel image = new AbsolutePanel();
        image.setStyleName("SelectButton");
        button.setDisplay(image, false);
        
        display.setWidget(0,1,button);
        display.getCellFormatter().setWidth(0,1,"16px");

        display.setStyleName("SelectBox");
        
        outer.setWidget(display);
        
        button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(enabled)
					showPopup();
			}
		});
        
        setDisplay(new Display<T>() {
        	public void setDisplay(ArrayList<Item<T>> items) {
                int selected = 0;
                StringBuffer sb;
                
                sb = new StringBuffer();
                
                if(items != null) {
                	for (int i = 0; i < items.size(); i++ ) {
                		if(!multiSelect || (multiSelect && "Y".equals(items.get(i).getCell(0)))) {
                			selected++;
                			if (sb.length() > 0)
                				sb.append(", ");
                			sb.append(renderer.getDisplay(items.get(i)));
                		}
                	}
                	if(selected > maxDisplay)
                		sb = new StringBuffer().append(selected+" options selected");
                }

                display.setText(0, 0, sb.toString());
        	}
		});
    }

    @Override
    public void setWidth(String w) {    
        width = Util.stripUnits(w) - 5;

        /*
         * Set the outer panel to full width;
         */
        if (display != null)
            display.setWidth(width+"px");

        /*
         * set the Textbox to width - 16 to account for button.
         */
        
        display.getCellFormatter().setWidth(0,0,(width - 16) + "px");
        
        if(table != null) 
            table.setWidth(width+"px");

    }
}
