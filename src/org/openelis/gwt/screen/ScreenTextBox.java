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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.TextBox;
/**
 * ScreenTextBox wraps a GWT TextBox to be displayed on a Screen.
 * @author tschmidt
 *
 */
public class ScreenTextBox extends ScreenInputWidget implements ChangeListener,
                                                                FocusListener
                                                               {
    /**
     * Default XML Tag Name used in XML Definition
     */
    public static String TAG_NAME = "textbox";
    /**
     * Widget wrapped by this class
     */
    private TextBox textbox;
  
    @Override
    public void onBrowserEvent(Event event) {
        if(DOM.eventGetType(event) == Event.ONKEYUP) {
            if(textbox.enforceMask){
                if(textbox.autoNext && DOM.eventGetKeyCode(event) != KeyboardListener.KEY_TAB){
                    if(textbox.getText().length() == (textbox.length) && textbox.getCursorPos() == textbox.length){
                        screen.doTab(false, this);
                    }
                }
            }
        }
        super.onBrowserEvent(event);
    }
    
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenTextBox() {
    }
    
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;textbox key="string" tab="string,string" shortcut="char" 
     *          case="mixed,upper,lower" max="int"/&gt; 
     * @param node
     * @param screen
     */ 
    public ScreenTextBox(Node node, final ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue())){
            textbox = (TextBox)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        }else{
            textbox = new TextBox();
        }
        
        if (node.getAttributes().getNamedItem("shortcut") != null)
            textbox.setAccessKey(node.getAttributes()
                                     .getNamedItem("shortcut")
                                     .getNodeValue()
                                     .charAt(0));
        textbox.setStyleName("ScreenTextBox");
        if (node.getAttributes().getNamedItem("case") != null){
            String fieldCase = node.getAttributes().getNamedItem("case")
                                            .getNodeValue().toUpperCase();
            textbox.setCase(TextBox.Case.valueOf(fieldCase));

        }

        if (node.getAttributes().getNamedItem("max") != null) {
            int length = Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue());
            textbox.setLength(length);
        }
        
        if (node.getAttributes().getNamedItem("textAlign") != null) {
            String align = node.getAttributes().getNamedItem("textAlign").getNodeValue();
            if(align.equals("center"))
                textbox.alignment = TextBox.ALIGN_CENTER;
            if(align.equals("right"))
                textbox.alignment = TextBox.ALIGN_RIGHT;
            if(align.equals("left"))   
                textbox.alignment = TextBox.ALIGN_LEFT;
            textbox.setTextAlignment(textbox.alignment);
        }
        
        if (node.getAttributes().getNamedItem("onchange") != null){
            String[] listeners = node.getAttributes().getNamedItem("onchange").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this")){
                    textbox.addChangeListener((ChangeListener)screen);
                }else{
                    textbox.addChangeListener((ChangeListener)ClassFactory.forName(listeners[i]));
                }
            }
        }
        
        if (node.getAttributes().getNamedItem("autoNext") != null){
            if(node.getAttributes().getNamedItem("autoNext").getNodeValue().equals("true")){
                textbox.autoNext = true;
            }
        }
        
        if (node.getAttributes().getNamedItem("mask") != null) {
            String mask = node.getAttributes().getNamedItem("mask").getNodeValue();
            textbox.setMask(mask);
        }
        
        initWidget(textbox);
        displayWidget = textbox;
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTextBox(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode){
            if(queryWidget != null)
                queryWidget.load(field);
        }else{
            textbox.setText(field.format().trim());
            super.load(field);
        }
            
    }

    public void submit(AbstractField field) {
        if(queryMode){
            if(queryWidget != null)  
                queryWidget.submit(field);
        }else
            field.setValue(textbox.getText());
            
    }

    public void onChange(Widget sender) {    
    }
    
    public void enable(boolean enabled){
        if(alwaysEnabled)
            enabled = true;
        
        if(alwaysDisabled)
            enabled = false;
        
        textbox.setReadOnly(!enabled);
        
        if(enabled){
            textbox.addFocusListener(this);
        }else
            textbox.removeFocusListener(this);
            
        super.enable(enabled);
    }
    
    public void setFocus(boolean focus){
        textbox.setFocus(focus);
    }
    
    public void destroy() {
        textbox = null;
        super.destroy();
    }
    
    public void setForm(State state) {
        if(queryWidget == null){
            if(state == State.QUERY){
                textbox.setMaxLength(255);
                textbox.enforceLength = false;
                textbox.enforceMask = false;
                textbox.setTextAlignment(TextBox.ALIGN_LEFT);
                queryField = field.getQueryField();
            }else{
                textbox.setMaxLength(textbox.length);
                textbox.enforceLength = true;
                textbox.enforceMask = true;
                textbox.setTextAlignment(textbox.alignment);
            }
        }
        super.setForm(state);
    }
    
    public void onFocus(Widget sender) {
        if(!textbox.isReadOnly()){
            if(sender == textbox){
                super.inner.addStyleName("Focus");
            }
        }   
        super.onFocus(sender);
    }
    public void onLostFocus(Widget sender) {
        if(!textbox.isReadOnly()){
            if(sender == textbox){
                super.inner.removeStyleName("Focus");
            }
        }
        super.onLostFocus(sender);
    }


}
