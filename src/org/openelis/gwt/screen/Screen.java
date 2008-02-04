package org.openelis.gwt.screen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.services.ScreenServiceInt;
import org.openelis.gwt.services.ScreenServiceIntAsync;
import org.openelis.gwt.widget.WidgetMap;

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
public class Screen extends ScreenBase {

    public String xmlUrl;
    public ConstantsWithLookup constants;
    private ScreenServiceIntAsync screenService = (ScreenServiceIntAsync)GWT.create(ScreenServiceInt.class);
    private ServiceDefTarget target = (ServiceDefTarget)screenService;
    /**
     * This field contains all widgets available to this application
     */
    private static WidgetMap WIDGET_MAP;

    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public Screen() {
        super();
    }

    /**
     * This constructor takes a file name as a string which points to 
     * the xml definition of this screen on the server.
     * @param xmlUrl
     */
    public Screen(String xmlUrl) {
        this();
        this.xmlUrl = xmlUrl;
    }

    /**
     * This method is used to init the serviceUrl to be used by the form.
     * 
     * @param url
     */
    public void initService(String url) {
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }

    /**
     * This method will call the defined ScreenServiceURL and on 
     * return load the screen from the RPC returned.  It will then
     * call the afterSubmit method or the afterQuery method.  
     * 
     * @param method
     */
    protected void callService(final String method) {
        if(method.equals("query")){
            screenService.query(rpc, new AsyncCallback() {
                public void onSuccess(Object result) {
                    afterQuery(result,true);
                }
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                    afterQuery(null,false);
                }
            });
        }else{
            screenService.action(rpc, new AsyncCallback() {
                public void onSuccess(Object result) {
                    rpc = (FormRPC)result;
                    load();
                    afterSubmit(method, true);
                }
                
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                    afterSubmit(method, false);
                }
            });
        }
    }

    /**
     * This method is called after a call to the servlet.  It should be 
     * overridden by the extending class to provide functionality for a 
     * screen.
     * 
     * @param method
     * @param success
     */
    public void afterSubmit(String method, boolean success) {

    }

    /**
     * This method is called after a call to the servlet for a form query.
     * It should be overridden by the extending class to provide functionality
     * for a screen.
     * 
     * @param result
     * @param success
     */
    public void afterQuery(Object result, boolean success){
        
    }

    /** 
     * this method is called when the screen is to be drawn using the xmlUrl defined by the 
     * constructor call
     */
    public void getXML(){
        getXML(xmlUrl);
    }
    
    /**
     * This method will retrieve the XML definition of this form from the
     * server. All forms are expected to be in a Forms directory under the
     * public path.  It will then call the draw() method after parsing the 
     * xml to display the screen.  It will create the FormRPC fields form
     * the parsed XML's RPC section. It also loads any default values for fields 
     * defined in the xml.  afterSubmit("draw",true) is called at the end of this
     * method so any custom initialization can be done by an extending class.
     * 
     * @param name
     */
    public void getXML(String name) {
       HTTPRequest.asyncGet("Forms/"+name, new ResponseTextHandler() {
           public void onCompletion(String response) {
               xml = XMLParser.parse(response);
               drawScreen();
            }
       });   
    }           
                
    private void drawScreen() {
         draw();
         try {
             NodeList rpcList = xml.getDocumentElement().getElementsByTagName("rpc");
             Element rpcEl = (Element)rpcList.item(0);
             NodeList fieldList = rpcEl.getChildNodes();
             HashMap map = new HashMap();
             for (int i = 0; i < fieldList.getLength(); i++) {
                if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                   AbstractField field = ScreenBase.getWidgetMap().getField(fieldList.item(i));
                   map.put((String)field.getKey(), field);
                }
             }
             rpc.setFieldMap(map);
         } catch (Exception e) {
             Window.alert("FormUtil: " + e.getMessage());
         }
         load();
         if (xml.getDocumentElement().getAttribute("serviceUrl") != null) {
             String url = xml.getDocumentElement().getAttribute("serviceUrl");
             initService(url);
         }
        afterSubmit("draw", true);
    }

}
