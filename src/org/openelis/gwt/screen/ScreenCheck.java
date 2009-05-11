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

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CheckBox.CheckType;
/**
 * ScreenCheck wraps a GWT CheckBox to be displayed on a Screen.
 * @author tschmidt
 *
 */
public class ScreenCheck extends ScreenInputWidget implements SourcesClickEvents,FocusListener{
    private DelegatingClickListenerCollection clickListeners;
    /**
     * Default Tag Name for XML Definition and WidgetMap
     */
    public static String TAG_NAME = "check";
    /**
     * Widget wrapped by this class
     */
    private CheckBox check;
    private CheckType defaultType = CheckBox.CheckType.TWO_STATE;
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenCheck() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;check key="string" shortcut="char" onClick="string"/&gt;
     *  
     * @param node
     * @param screen
     */
    public ScreenCheck(Node node, final ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            check = (CheckBox)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            check = new CheckBox();
        if(node.getAttributes().getNamedItem("threeState") != null){
            check.setType(CheckBox.CheckType.THREE_STATE);
            defaultType = CheckBox.CheckType.THREE_STATE;
        }
        if (node.getFirstChild() != null){
             check.setText(node.getFirstChild().getNodeValue());
        }
        if (node.getChildNodes().getLength() > 0){
            NodeList widgets = node.getChildNodes();
            for (int k = 0; k < widgets.getLength(); k++) {
                if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                    check.setWidget(wid);
                }
            }
        }
        if (node.getAttributes().getNamedItem("onClick") != null){
            String listener = node.getAttributes().getNamedItem("onClick").getNodeValue();
            if(listener.equals("this"))
                addClickListener((ClickListener)screen);
            else
                addClickListener((ClickListener)ClassFactory.forName(listener));
        }
        initWidget(check);
        displayWidget = check;
        check.setStyleName("ScreenCheck");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenCheck(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode && queryWidget != null)
            queryWidget.load(field);
        else{
            check.setState((String)field.getValue());
            super.load(field);
        }
    }

    public void submit(AbstractField field) {
        if(queryMode && queryWidget != null)
            queryWidget.submit(field);
        else
            field.setValue(check.getState());
    }
    
    public void enable(boolean enabled){
        if(queryMode && queryWidget != null)
            queryWidget.enable(true);
        else{
            check.enable(enabled);
            if(enabled)
                check.addFocusListener(this);
            else
                check.removeFocusListener(this);
            super.enable(enabled);
        }
    }
    
    public void setFocus(boolean focus){
        if(queryMode && queryWidget != null)
            queryWidget.setFocus(focus);
        else
            check.setFocus(focus);
            
    }
    public void addClickListener(ClickListener listener) {
        if(clickListeners == null){
            clickListeners = new DelegatingClickListenerCollection(this,check);
        }
        clickListeners.add(listener);
    }
    
    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null){
            clickListeners.remove(listener);
        }
    }
    
    public void destroy(){
        clickListeners = null;
        check = null;
        super.destroy();
    }
    
    public void setForm(State state) {
        if(queryWidget == null){
            if(state == State.QUERY){
                check.setType(CheckBox.CheckType.THREE_STATE);
                check.setState(CheckBox.UNKNOWN);
                queryField = field.getQueryField();
            }else
                check.setType(defaultType);
        }
        super.setForm(state);
    }
    
    public void onFocus(Widget sender) {
        if(enabled){
            super.inner.addStyleName("Focus");
        }   
        super.onFocus(sender);
    }
    public void onLostFocus(Widget sender) {
        if(enabled){
            super.inner.removeStyleName("Focus");
        }
        super.onLostFocus(sender);
    }
    

}
