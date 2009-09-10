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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesKeyboardEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.deprecated.Form;
import org.openelis.gwt.services.deprecated.AppScreenServiceInt;
import org.openelis.gwt.services.deprecated.AppScreenServiceIntAsync;
import org.openelis.gwt.widget.deprecated.AppButton;

import java.util.HashMap;
/**
 * The Screen class is the base class for displaying a screen 
 * drawn on the client machine.  It can also validate input and
 * and return data to the servlet.  This class is compiled into 
 * javascript and run on the client.
 * 
 * @author tschmidt
 *
 */
@Deprecated
public class AppScreen<ScreenRPC extends Form> extends ScreenBase<ScreenRPC> implements EventPreview, SourcesKeyboardEvents, SourcesClickEvents {

    public AppScreenServiceIntAsync<ScreenRPC> service;
    //public HashMap<String,Form> forms = new HashMap<String,Form>();
    //public HashMap<String,FieldType> initData;
    private KeyboardListenerCollection keyListeners;
    private ClickListenerCollection clickListeners;
    public Element clickTarget;
    public ScreenWindow window;
    public static HashMap<String,String> consts;
    protected ScreenWidget focused;
    
    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public AppScreen() {
        super();
    }
    
    public AppScreen(AppScreenServiceIntAsync<ScreenRPC> service){
        this();
        this.service = service;
        //getXML();
    }
    
    public AppScreen(String serviceClass) {
        super();         
        service = (AppScreenServiceIntAsync)GWT.create(AppScreenServiceInt.class);
        ServiceDefTarget target = (ServiceDefTarget)service;
        target.setServiceEntryPoint(target.getServiceEntryPoint()+"?service="+serviceClass);
    }
    
   /*
    @Deprecated public void getXML(ScreenRPC rpc) {
        this.form = rpc;
        service.getXML(new AsyncCallback<String>() {
           public void onSuccess(String result){
               drawScreen(result);
               afterDraw(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDraw(false);
           }
        });
    }
    
    @Deprecated public void getXMLData(ScreenRPC rpc) {
        this.form = rpc;
        service.getXMLData(new AsyncCallback<HashMap<String,FieldType>>() {
           public void onSuccess(HashMap<String,FieldType> result){
               initData = result;
               drawScreen((String)((StringObject)initData.get("xml")).getValue());
               afterDraw(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDraw(false);
           }
        });
    }
    */
    public void getScreen(ScreenRPC screen) {
        form = screen;
        service.getScreen(form,new AsyncCallback<ScreenRPC>() {
            public void onSuccess(ScreenRPC result){
                form = result;
                drawScreen(form.xml);
                afterDraw(true);
            }
            
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                afterDraw(false);
            }
        });
    }
    
    /*
    @Deprecated public void getXMLData(HashMap<String, FieldType> args, ScreenRPC rpc) {
        this.form = rpc;
        service.getXMLData(args, new AsyncCallback<HashMap<String,FieldType>>() {
           public void onSuccess(HashMap<String,FieldType> result){
               //try {
                   initData = result;
                   drawScreen((String)((StringObject)initData.get("xml")).getValue());
                   afterDraw(true);
               //}catch(Exception e){
                 //  Window.alert("error "+e.getMessage() + e.getStackTrace()[0]);
              // }
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDraw(false);
           }
        });
    }
    */
    
    public void afterDraw(boolean sucess) {

        //this.form = forms.get("display");
        //load();
        DOM.addEventPreview(this);
        if(window != null){
            window.setName(name);
            window.setVisible(true);
            RootPanel.get().removeStyleName("ScreenLoad");
            window.setStatus(consts.get("loadCompleteMessage"),"");
        }
        form.xml = null;
    }
    
    public void redrawScreen(String xmlDef){
        panel.clear();
        tabOrder = new HashMap();
        tabBack = new HashMap();
        widgets = new HashMap();
        //forms = new HashMap();
        drawScreen(xmlDef);
        afterDraw(true);
    }
    
    public void drawScreen(String xmlDef) {
         xml = XMLParser.parse(xmlDef);
             NodeList rpcList = xml.getDocumentElement().getChildNodes();
             for(int i = 0; i < rpcList.getLength(); i++){
                 if(rpcList.item(i).getNodeType() == Node.ELEMENT_NODE && rpcList.item(i).getNodeName().equals("rpc")){
                     String key = rpcList.item(i).getAttributes().getNamedItem("key").getNodeValue();
                     //if(forms.containsKey(key)){
                         form.createFields(rpcList.item(i));
                         form.load = true;
                         break;
                     /*}else{
                         Form form = (Form)ScreenBase.createField(rpcList.item(i));
                         form.load = true;
                         forms.put(form.key, form);
                         
                     }
                     */
                 }
             }
             draw();
    }


    public boolean onEventPreview(Event event) {
        if(DOM.eventGetType(event) == Event.ONKEYPRESS){
            if(DOM.eventGetCtrlKey(event)){
                String key = String.valueOf((char)DOM.eventGetKeyCode(event));
                if(shortcut.containsKey(key)){
                    Widget wid = (Widget)shortcut.get(key);
                    if(wid instanceof AppButton){
                        ((AppButton)wid).fireClick();
                    }else if(wid instanceof HasFocus){
                        ((HasFocus)wid).setFocus(true);
                    }
                    DOM.eventPreventDefault(event);
                    return false;
                }
                return true;
            }
        }
        switch(DOM.eventGetType(event)){
            case Event.ONKEYDOWN:
            case Event.ONKEYUP:
            case Event.ONKEYPRESS:
                if(keyListeners != null)
                    keyListeners.fireKeyboardEvent(this, event);
                break;
            case Event.ONCLICK:
                clickTarget = DOM.eventGetTarget(event);
                if(clickListeners != null)
                    clickListeners.fireClick(this);
                break;
        }
        return true;   
    }

    public void addKeyboardListener(KeyboardListener listener) {
        if(keyListeners == null){
            keyListeners = new KeyboardListenerCollection();
        }
        keyListeners.add(listener);
        
    }

    public void removeKeyboardListener(KeyboardListener listener) {
        if(keyListeners != null){
            keyListeners.remove(listener);
        }
        
    }

    public void addClickListener(ClickListener listener) {
        if(clickListeners == null){
            clickListeners = new ClickListenerCollection();
        }
        clickListeners.add(listener);
        
    }

    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null){
            clickListeners.remove(listener);
        }
        
    }
}
