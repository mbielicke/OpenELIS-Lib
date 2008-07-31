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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.services.AppScreenServiceIntAsync;
import org.openelis.gwt.widget.AppButton;

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
public class AppScreen extends ScreenBase implements EventPreview, SourcesKeyboardEvents, SourcesClickEvents {

    public AppScreenServiceIntAsync service;
    public HashMap<String,FormRPC> forms = new HashMap<String,FormRPC>();
    public HashMap<String,DataObject> initData;
    private KeyboardListenerCollection keyListeners;
    private ClickListenerCollection clickListeners;
    public Element clickTarget;
    public ScreenWindow window;
    protected AppConstants consts = (AppConstants)ClassFactory.forName("AppConstants");
    
    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public AppScreen() {
        super();
    }
    
    public AppScreen(AppScreenServiceIntAsync service){
        this();
        this.service = service;
        getXML();
    }

    public void getXML() {
        service.getXML(new AsyncCallback() {
           public void onSuccess(Object result){
               drawScreen((String)result);
               afterDraw(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDraw(false);
           }
        });
    }
    
    public void getXMLData() {
        service.getXMLData(new AsyncCallback() {
           public void onSuccess(Object result){
               initData = (HashMap<String,DataObject>)result;
               drawScreen((String)((StringObject)initData.get("xml")).getValue());
               afterDraw(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDraw(false);
           }
        });
    }
    
    public void getXMLData(HashMap<String,DataObject> args) {
        service.getXMLData(args, new AsyncCallback() {
           public void onSuccess(Object result){
               initData = (HashMap)result;
               drawScreen((String)((StringObject)initData.get("xml")).getValue());
               afterDraw(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDraw(false);
           }
        });
    }
    
    public void afterDraw(boolean sucess) {
       // try {
        this.rpc = (FormRPC)forms.get("display");
        load();
        DOM.addEventPreview(this);
        if(window != null){
            window.setName(name);
            window.setVisible(true);
            RootPanel.get().removeStyleName("ScreenLoad");
            window.setStatus(consts.get("loadCompleteMessage"),"");
        }
        //}catch(Exception e){
        //    Window.alert("after draw " +e.getMessage());
        //}
    }
    
    public void redrawScreen(String xmlDef){
        panel.clear();
        tabOrder = new HashMap();
        tabBack = new HashMap();
        widgets = new HashMap();
        forms = new HashMap();
        drawScreen(xmlDef);
        afterDraw(true);
    }
    
    public void drawScreen(String xmlDef) {
         xml = XMLParser.parse(xmlDef);
         
         //try {
             NodeList rpcList = xml.getDocumentElement().getChildNodes();
             for(int i = 0; i < rpcList.getLength(); i++){
                 if(rpcList.item(i).getNodeType() == Node.ELEMENT_NODE && rpcList.item(i).getNodeName().equals("rpc")){
                     FormRPC form = (FormRPC)ScreenBase.createField(rpcList.item(i));
                     form.load = true;
                     forms.put(form.key, form);
                 }
             }
             draw();
        //} catch (Exception e) {
         //  Window.alert("FormUtil: " + e.getMessage());
        // }
        
         //load((FormRPC)forms.get("query"));
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
