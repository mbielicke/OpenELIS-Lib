package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.client.services.AppScreenServiceIntAsync;
import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.FormRPC;

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
public class AppScreen extends ScreenBase {

    public AppScreenServiceIntAsync service;
    public HashMap forms = new HashMap();
    
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
    
    public void afterDraw(boolean sucess) {

    }
    
    public void drawScreen(String xmlDef) {
         xml = XMLParser.parse(xmlDef);
         draw();
         try {
             NodeList rpcList = xml.getDocumentElement().getElementsByTagName("rpc");
             for(int i = 0; i < rpcList.getLength(); i++){
                 Element rpcEl = (Element)rpcList.item(i);
                 NodeList fieldList = rpcEl.getChildNodes();
                 HashMap map = new HashMap();
                 for (int j = 0; j < fieldList.getLength(); j++) {
                     if (fieldList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                         AbstractField field = AppScreen.getWidgetMap().getField(fieldList.item(j));
                         map.put((String)field.getKey(), field);
                     }
                 }
                 FormRPC form = new FormRPC();
                 form.setFieldMap(map);
                 forms.put(rpcEl.getAttributes().getNamedItem("key").getNodeValue(), form);
             }
         } catch (Exception e) {
             Window.alert("FormUtil: " + e.getMessage());
         }
         load((FormRPC)forms.get("display"));
         //load((FormRPC)forms.get("query"));
    }
    
    protected void load(FormRPC rpc){
        this.rpc = rpc;
        load();
    }

}