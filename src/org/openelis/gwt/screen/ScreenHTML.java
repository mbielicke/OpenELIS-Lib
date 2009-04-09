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
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.xml.client.Node;
/**
 * ScreenHTML wraps a GWT HMTL widget for display HTML content on 
 * a Screen.
 * @author tschmidt
 *
 */
public class ScreenHTML extends ScreenWidget implements SourcesClickEvents {
    
    private DelegatingClickListenerCollection clickListeners;
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "html";
	/**
	 * Widget wrapped by this class
	 */
    public HTML html;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenHTML() {
    }
    
    public ScreenHTML(String htmlText, Object value) {
        html = new HTML(htmlText);
        setUserObject(value);
        initWidget(html);
        html.setStyleName("ScreenHTML");
        
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * 
     * &lt;html key="string" onclick="string"&gt;ESCAPED HTML&lt;/html&gt;
     * @param node
     * @param screen
     */
    public ScreenHTML(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            html = (HTML)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            html = new HTML();
        
        if(node.getFirstChild() != null)
            html.setHTML(node.getFirstChild().getNodeValue());
        
        if (node.getAttributes().getNamedItem("onclick") != null) {
        	String[] listeners = node.getAttributes().getNamedItem("onclick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    addClickListener((ClickListener)screen);
                else
                    addClickListener((ClickListener)ClassFactory.forName(listeners[i]));
            }
        }
        initWidget(html);
        html.setStyleName("ScreenHTML");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenHTML(node, screen);
    }
    
    public void addClickListener(ClickListener arg0) {
        if(clickListeners == null)
            clickListeners = new DelegatingClickListenerCollection(this,html);
        if(clickListeners.contains(arg0))
            return;
        clickListeners.add(arg0);
         
     }

     public void removeClickListener(ClickListener arg0) {
         if(clickListeners != null)
             clickListeners.remove(arg0);
     }
     
     public void destroy(){
         DOM.setEventListener(html.getElement(), null);
         html = null;
         super.destroy();
     }

}
