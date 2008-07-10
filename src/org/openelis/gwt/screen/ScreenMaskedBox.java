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
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.MaskedTextBox;

/**
 * ScreenMaskedBox wraps a MaskedTextBox widget for display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenMaskedBox extends ScreenInputWidget implements FocusListener{
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "maskedbox";
	/**
	 * Widget wrapped by this class
	 */
    private MaskedTextBox maskbox;
    private String next;
    
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenMaskedBox() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;maskedbox key="string" shortcut="char" mask="string" next="string"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenMaskedBox(Node node, final ScreenBase screen) {
        super(node);
        final ScreenMaskedBox sm = this;
        maskbox = new MaskedTextBox() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, sm);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
            
            public void complete() {
                screen.doTab(null,sm);
            }
        };
        if (node.getAttributes().getNamedItem("shortcut") != null)
            maskbox.setAccessKey(node.getAttributes()
                                     .getNamedItem("shortcut")
                                     .getNodeValue()
                                     .charAt(0));
        String mask = node.getAttributes().getNamedItem("mask").getNodeValue();
        if(node.getAttributes().getNamedItem("next") != null){
            next = node.getAttributes().getNamedItem("next").getNodeValue();
        }
        maskbox.setMask(mask);
        initWidget(maskbox);
        displayWidget = maskbox;
        maskbox.setStyleName("ScreenMaskedBox");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenMaskedBox(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else
            maskbox.setText(field.toString());

    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else
            field.setValue(maskbox.getText());

    }
    
    public void enable(boolean enabled){
        maskbox.setReadOnly(!enabled);
        if(enabled)
            maskbox.addFocusListener(this);
        else
            maskbox.removeFocusListener(this);
        super.enable(enabled);
    }
    
    public void setFocus(boolean focus){
        maskbox.setFocus(focus);
    }
    
    public void destroy(){
        maskbox = null;
        next = null;
        super.destroy();
    }
    
    public void setForm(boolean mode) {
        if(queryWidget == null){
            maskbox.noMask = mode;
        }else{
            super.setForm(mode);
        }
    }
   
    public void onFocus(Widget sender) {
		if(!maskbox.isReadOnly()){
			if(sender == maskbox){
				super.hp.addStyleName("Focus");
			}
		}	
        super.onFocus(sender);
	}
	public void onLostFocus(Widget sender) {
		if(!maskbox.isReadOnly()){
			if(sender == maskbox){
				super.hp.removeStyleName("Focus");
			}
		}
        super.onLostFocus(sender);
	}    
}
