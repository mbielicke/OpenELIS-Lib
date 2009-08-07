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
package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.CheckField;
import org.openelis.gwt.screen.AppScreenForm.State;

/**
 * ScreenRadio wraps a GWT RadioButton widget for Display on a Screen.
 * @author tschmidt
 *
 */
@Deprecated
public class ScreenRadio extends ScreenInputWidget {
    /**
     * Default XML Tag Name for XML definition and WidgetMap
     */
    public static String TAG_NAME = "radio";
    /**
     * Widget wrapped by this class
     */
    private RadioButton radio;
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenRadio() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;radio key="string" group="string" shortcut="char"&lt;TEXT&lt;/radio&gt;
     * 
     * @param node
     * @param screen
     */ 
    public ScreenRadio(Node node, final ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        final ScreenRadio sr = this;
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            radio = (RadioButton)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else{
            radio = new RadioButton(node.getAttributes()
                                    .getNamedItem("group")
                                    .getNodeValue());
        }
        if (node.getFirstChild() != null)
            radio.setText(node.getFirstChild().getNodeValue());
        if (node.getAttributes().getNamedItem("shortcut") != null)
            radio.setAccessKey(node.getAttributes()
                                   .getNamedItem("shortcut")
                                   .getNodeValue()
                                   .charAt(0));
        initWidget(radio);
        displayWidget = radio;
        radio.setStyleName("ScreenRadio");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenRadio(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else{
            radio.setChecked(((CheckField)field).isChecked());
            super.load(field);
        }
    }

    public void submit(AbstractField field) {
        if(queryMode && queryWidget != null)
            queryWidget.submit(field);
        else
            field.setValue(new Boolean(radio.isChecked()));
    }
    
    public void enable(boolean enabled){
        if(queryMode && queryWidget != null)
            queryWidget.enable(enabled);
        else{
            radio.setEnabled(enabled);
            if(enabled)
                radio.addFocusListener(this);
            else
                radio.removeFocusListener(this);
            super.enable(enabled);
        }
    }
    
    public void setFocus(boolean focus){
        if(queryMode && queryWidget != null)
            queryWidget.setFocus(focus);
        else
            radio.setFocus(focus);
    }
    
    public void destroy() {
        radio = null;
        super.destroy();
    }

    public void setForm(State state){
        if(queryWidget == null){
            if(state == State.QUERY)
                queryField = field.getQueryField();
        }
        super.setForm(state);
    }
}
