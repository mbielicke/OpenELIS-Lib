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
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesKeyboardEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataObject;
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
    public HashMap forms = new HashMap();
    public DataObject[] initData;
    private KeyboardListenerCollection keyListeners;
    private ClickListenerCollection clickListeners;
    public Element clickTarget;
    
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
               DataObject[] data = (DataObject[])result;
               drawScreen((String)data[0].getValue());
               initData = new DataObject[data.length-1];
               for(int i = 1; i < data.length; i++) {
                   initData[i-1] = data[i];
               }
               afterDraw(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDraw(false);
           }
        });
    }
    
    public void getXMLData(DataObject[] args) {
        service.getXMLData(args, new AsyncCallback() {
           public void onSuccess(Object result){
               DataObject[] data = (DataObject[])result;
               drawScreen((String)data[0].getValue());
               initData = new DataObject[data.length-1];
               for(int i = 1; i < data.length; i++) {
                   initData[i-1] = data[i];
               }
               afterDraw(true);
           }
           public void onFailure(Throwable caught){
               Window.alert(caught.getMessage());
               afterDraw(false);
           }
        });
    }
    
    public void afterDraw(boolean sucess) {
        load((FormRPC)forms.get("display"));
        DOM.addEventPreview(this);
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
         
        // try {
             NodeList rpcList = xml.getDocumentElement().getElementsByTagName("rpc");
             for(int i = 0; i < rpcList.getLength(); i++){
                 com.google.gwt.xml.client.Element rpcEl = (com.google.gwt.xml.client.Element)rpcList.item(i);
                 NodeList fieldList = rpcEl.getChildNodes();
                 HashMap map = new HashMap();
                 for (int j = 0; j < fieldList.getLength(); j++) {
                     if (fieldList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                         AbstractField field = ScreenBase.createField(fieldList.item(j));
                         map.put((String)field.getKey(), field);
                     }
                 }
                 FormRPC form = new FormRPC();
                 form.setFieldMap(map);
                 form.key = rpcEl.getAttributes().getNamedItem("key").getNodeValue();
                 forms.put(form.key, form);
             }
             draw();
       // } catch (Exception e) {
       ///     Window.alert("FormUtil: " + e.getMessage());
        // }
        
         //load((FormRPC)forms.get("query"));
    }
    
    protected void load(FormRPC rpc){
        this.rpc = rpc;
        load();
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
                }
                DOM.eventPreventDefault(event);
                return false;
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