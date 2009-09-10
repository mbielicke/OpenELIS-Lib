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

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.deprecated.AbstractField;
import org.openelis.gwt.screen.deprecated.AppScreenForm.State;
import org.openelis.gwt.widget.deprecated.MaskedTextBox;

/**
 * ScreenMaskedBox wraps a MaskedTextBox widget for display on a Screen.
 * @author tschmidt
 *
 */
@Deprecated public class ScreenMaskedBox extends ScreenInputWidget implements FocusListener{
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
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            maskbox = (MaskedTextBox)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            maskbox = new MaskedTextBox();
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
        else{
            maskbox.setText(field.toString());
            super.load(field);
        }

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
    
    public void setForm(State state) {
        if(queryWidget == null){
            if(state == State.QUERY)
                maskbox.noMask = true;
            else
                maskbox.noMask = false;
        }else{
            super.setForm(state);
        }
    }
   
    public void onFocus(Widget sender) {
        if(!maskbox.isReadOnly()){
            if(sender == maskbox){
                super.inner.addStyleName("Focus");
            }
        }   
        super.onFocus(sender);
    }
    public void onLostFocus(Widget sender) {
        if(!maskbox.isReadOnly()){
            if(sender == maskbox){
                super.inner.removeStyleName("Focus");
            }
        }
        super.onLostFocus(sender);
    }    
}
