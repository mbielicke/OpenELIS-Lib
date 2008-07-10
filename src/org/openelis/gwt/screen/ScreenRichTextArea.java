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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.richtext.RichTextWidget;

public class ScreenRichTextArea extends ScreenInputWidget {
    /**
     * Default XML Tag Name in XML Definition
     */
    public static String TAG_NAME = "richtext";
    /**
     * Widget wrapped by this class
     */
    private RichTextWidget textarea;
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenRichTextArea() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;textarea key="string" shortcut="char" tab="string,string"/&gt;
     * 
     * @param node
     * @param screen
     */ 
    public ScreenRichTextArea(Node node, final ScreenBase screen) {
        super(node);
        final ScreenRichTextArea st = this;
        boolean tools = true;
        if(node.getAttributes().getNamedItem("tools") != null){
        	if(node.getAttributes().getNamedItem("tools").getNodeValue().equals("false")){
        		tools = false;
        	}
        }
        textarea = new RichTextWidget(tools) {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, st);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        textarea.setSize("100%", "300px");
        initWidget(textarea);
        displayWidget = textarea;
        textarea.setStyleName("ScreenTextArea");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTextArea(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else
            textarea.setText(field.toString());

    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else
            field.setValue(textarea.getText());
    }
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else{
            //textarea.setReadOnly(!enabled);
            if(enabled)
                textarea.addFocusListener(this);
            else
                textarea.removeFocusListener(this);
            super.enable(enabled);
        }
    }
    
    public void setFocus(boolean focus){
        if(queryMode)
            queryWidget.setFocus(focus);
        else
            textarea.setFocus(focus);
    }
    
    public void destroy() {
        textarea = null;
        super.destroy();
    }

    public void onFocus(Widget sender) {
        if(!textarea.isEnabled()){
            if(sender == textarea){
                super.hp.addStyleName("Focus");
            }
        }
        super.onFocus(sender);
    }
    public void onLostFocus(Widget sender) {
        if(!textarea.isEnabled()){
            if(sender == textarea){
                super.hp.removeStyleName("Focus");
            }
        }
        super.onLostFocus(sender);
    }    

}
