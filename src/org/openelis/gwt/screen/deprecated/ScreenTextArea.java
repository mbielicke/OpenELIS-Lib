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

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.deprecated.AbstractField;
import org.openelis.gwt.screen.deprecated.AppScreenForm.State;

/** 
 * ScreenTextArea wraps a GWT TextArea widget to be displayed on 
 * a Screen.
 * @author tschmidt
 *
 */
@Deprecated
public class ScreenTextArea extends ScreenInputWidget implements FocusListener{
    /**
     * Default XML Tag Name in XML Definition
     */
    public static String TAG_NAME = "textarea";
    /**
     * Widget wrapped by this class
     */
    private TextArea textarea;
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenTextArea() {
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
    public ScreenTextArea(Node node, final ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            textarea = (TextArea)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            textarea = new TextArea();
        if (node.getAttributes().getNamedItem("shortcut") != null)
            textarea.setAccessKey(node.getAttributes()
                                      .getNamedItem("shortcut")
                                      .getNodeValue()
                                      .charAt(0));
        if (node.getAttributes().getNamedItem("tab") != null) {
            screen.addTab(this, node.getAttributes()
                                       .getNamedItem("tab")
                                       .getNodeValue()
                                       .split(","));
            textarea.sinkEvents(Event.KEYEVENTS);
        }
        
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
        if(queryMode && queryWidget != null)
            queryWidget.load(field);
        else{
            textarea.setText(field.toString());
            super.load(field);
        }

    }

    public void submit(AbstractField field) {
        if(queryMode && queryWidget != null)
            queryWidget.submit(field);
        else
            field.setValue(textarea.getText());
    }
    
    
    public void enable(boolean enabled){
        if(queryMode && queryWidget != null)
            queryWidget.enable(enabled);
        else{
            if(!alwaysEnabled){
                if(alwaysDisabled)
                    enabled = false;
                textarea.setReadOnly(!enabled);
                if(enabled){
                    textarea.addFocusListener(this);
                }else
                    textarea.removeFocusListener(this);
                super.enable(enabled);
            }else
                super.enable(true);
        }
    }
    
    public void setFocus(boolean focus){
        if(queryMode && queryWidget != null)
            queryWidget.setFocus(focus);
        else
            textarea.setFocus(focus);
    }
    
    public void destroy() {
        textarea = null;
        super.destroy();
    }

    public void onFocus(Widget sender) {
        if(!textarea.isReadOnly()){
            if(sender == textarea){
                super.inner.addStyleName("Focus");
            }
        }
        super.onFocus(sender);
    }
    public void onLostFocus(Widget sender) {
        if(!textarea.isReadOnly()){
            if(sender == textarea){
                super.inner.removeStyleName("Focus");
            }
        }
        super.onLostFocus(sender);
    }    
    
    public void setForm(State state) {
        if(state == State.QUERY) {
            if(queryWidget == null) {
                queryField = field.getQueryField();
            }
        }
        super.setForm(state);
    }
}
