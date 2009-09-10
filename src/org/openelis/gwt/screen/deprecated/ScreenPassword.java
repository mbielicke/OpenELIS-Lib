/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen.deprecated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.deprecated.AbstractField;

/**
 * ScreenPassword wraps a GWT Password widget for Display on a Screen.
 * @author tschmidt
 *
 */
@Deprecated
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
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            textbox = (PasswordTextBox)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            textbox = new PasswordTextBox();
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
