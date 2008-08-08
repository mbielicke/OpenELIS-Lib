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
package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;

/**
 * ScreenPassword wraps a GWT Password widget for Display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenPassword extends ScreenInputWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "password";
	/**
	 * Widget wrapped by this class
	 */
    private PasswordTextBox textbox;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenPassword() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;password key="string" shortcut="char"/&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenPassword(Node node, final ScreenBase screen) {
        super(node);
        final ScreenPassword sp = this;
        textbox = new PasswordTextBox() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, sp);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        if (node.getAttributes().getNamedItem("shortcut") != null)
            textbox.setAccessKey(node.getAttributes()
                                     .getNamedItem("shortcut")
                                     .getNodeValue()
                                     .charAt(0));
        initWidget(textbox);
        displayWidget = textbox;
        textbox.setStyleName("ScreenPassword");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenPassword(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else{
            textbox.setText(field.toString());
            super.load(field);
        }
    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else
            field.setValue(textbox.getText());
    }
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else{
            textbox.setReadOnly(!enabled);
            if(enabled)
                textbox.addFocusListener(this);
            else
                textbox.removeFocusListener(this);
            super.enable(enabled);
        }
    }
    
    public void setFocus(boolean focus){
        if(queryMode)
            queryWidget.setFocus(focus);
        else
            textbox.setFocus(focus);
    }
    
    public void destroy(){
        textbox = null;
        super.destroy();
    }
}
