package org.openelis.gwt.screen;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.services.AppScreenServiceIntAsync;

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
    public DataObject[] initData;

    
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
    
    public void afterDraw(boolean sucess) {
        load((FormRPC)forms.get("display"));
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
             NodeList rpcList = xml.getDocumentElement().getElementsByTagName("rpc");
             for(int i = 0; i < rpcList.getLength(); i++){
                 Element rpcEl = (Element)rpcList.item(i);
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
         //} catch (Exception e) {
           //  Window.alert("FormUtil: " + e.getMessage());
         //}
        
         //load((FormRPC)forms.get("query"));
    }
    
    protected void load(FormRPC rpc){
        this.rpc = rpc;
        load();
    }

}