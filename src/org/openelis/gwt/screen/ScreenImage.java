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
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.xml.client.Node;
/**
 * ScreenImage wraps a GWT Image widget to be displayed on a Screen.
 * @author tschmidt
 *
 */
public class ScreenImage extends ScreenWidget implements SourcesClickEvents {
	/**
	 * Widget wrapped by this class
	 */
    private Image image;
    private DelegatingClickListenerCollection clickListeners;
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "image";
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenImage() {
        
    }
    
    public ScreenImage(String url) {
        image = new Image(url);
        initWidget(image);
        addMouseListener((MouseListener)ClassFactory.forName("HoverListener"));
        
        sinkEvents(Event.MOUSEEVENTS);
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;image key="string" url="string" onclick="string"/&gt;
     *  
     * @param node
     * @param screen
     */
    public ScreenImage(Node node, final ScreenBase screen) {
        super(node);
        final ScreenImage si = this;
        image = new Image() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, si);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        image.setUrl(node.getAttributes().getNamedItem("url").getNodeValue());
        if (node.getAttributes().getNamedItem("onclick") != null) {
        	String listener = node.getAttributes().getNamedItem("onclick").getNodeValue();
        	if(listener.equals("this"))
        		addClickListener((ClickListener)screen);
        	else
        		addClickListener((ClickListener)ClassFactory.forName(listener));
        }
    }

    public void addClickListener(ClickListener listener) {
        if(clickListeners == null){
            clickListeners = new DelegatingClickListenerCollection(this,image);
        }
        clickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null)
            clickListeners.remove(listener);
    }
    
    public void destroy(){
        image = null;
        clickListeners = null;
        super.destroy();
    }
}
