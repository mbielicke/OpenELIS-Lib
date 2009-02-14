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

import java.util.HashMap;

import org.openelis.gwt.common.Form;
import org.openelis.gwt.common.RPC;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.services.AppScreenServiceIntAsync;
import org.openelis.gwt.widget.AppButton;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
/**
 * The Screen class is the base class for displaying a screen 
 * drawn on the client machine.  It can also validate input and
 * and return data to the servlet.  This class is compiled into 
 * javascript and run on the client.
 * 
 * @author tschmidt
 *
 */
public class AppScreen<ScreenRPC extends RPC> extends ScreenBase implements KeyPressHandler {

    public AppScreenServiceIntAsync<ScreenRPC> service;
    public HashMap<String,Form> forms = new HashMap<String,Form>();
    @Deprecated public HashMap<String,FieldType> initData;
    public ScreenRPC rpc;
    public ScreenWindow window;
    protected AppConstants consts = (AppConstants)ClassFactory.forName("AppConstants");
    
    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public AppScreen() {
        super();
    }
    
    public AppScreen(AppScreenServiceIntAsync<ScreenRPC> service){
        this();
        this.service = service;
        addDomHandler(this,KeyPressEvent.getType());
    }
    
    public void getScreen(ScreenRPC screen) {
        rpc = screen;
        service.getScreen(rpc,new AsyncCallback<ScreenRPC>() {
            public void onSuccess(ScreenRPC result){
                rpc = result;
                drawScreen(rpc.xml);
                afterDraw(true);
            }
            
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                afterDraw(false);
            }
        });
    }
   
    @Deprecated 
    public void getXML(ScreenRPC rpc) {
        this.rpc = rpc;
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
    
    @Deprecated 
    public void getXMLData(ScreenRPC rpc) {
        this.rpc = rpc;
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
    
    @Deprecated 
    public void getXMLData(HashMap<String, FieldType> args, ScreenRPC rpc) {
        this.rpc = rpc;
        service.getXMLData(args, new AsyncCallback<HashMap<String,FieldType>>() {
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
    
    public void afterDraw(boolean sucess) {
        this.form = forms.get("display");
        if(window != null){
            window.setName(name);
            window.setVisible(true);
            RootPanel.get().removeStyleName("ScreenLoad");
            window.setStatus(consts.get("loadCompleteMessage"),"");
        }
        rpc.xml = null;
    }
    
    public void drawScreen(String xmlDef) {
         xml = XMLParser.parse(xmlDef);
         
         NodeList rpcList = xml.getDocumentElement().getChildNodes();
         for(int i = 0; i < rpcList.getLength(); i++){
             if(rpcList.item(i).getNodeType() == Node.ELEMENT_NODE && rpcList.item(i).getNodeName().equals("rpc")){
                 String key = rpcList.item(i).getAttributes().getNamedItem("key").getNodeValue();
                 if(forms.containsKey(key)){
                     forms.get(key).createFields(rpcList.item(i));
                     forms.get(key).load = true;
                 }else{
                     Form form = (Form)ScreenBase.createField(rpcList.item(i));
                     form.load = true;
                     forms.put(form.key, form);
                 }
             }
         }
         rpc.form = forms.get("display");
         draw();
    }

	public void onKeyPress(KeyPressEvent event) {
		 if(window.isActiveWindow() && event.isControlKeyDown()){
			 String key = String.valueOf(event.getCharCode());
			 if(shortcut.containsKey(key)){
				 Widget wid = (Widget)shortcut.get(key);
				 if(wid instanceof AppButton){
					 ((AppButton)wid).fireCommand();
				 }else if(wid instanceof Focusable){
					 ((Focusable)wid).setFocus(true);
				 }
				 event.preventDefault();
			 }
		 }
	}
}
