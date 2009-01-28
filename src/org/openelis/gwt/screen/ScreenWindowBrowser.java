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

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.WindowBrowser;

/**
 * ScreenWindowBrowser wraps a WindowBrowser widget to be displayed 
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenWindowBrowser extends ScreenWidget {
	/** 
	 * Default XML Tag used in XML Definition 
	 */
	public static String TAG_NAME = "winbrowser";
	/**
	 * Widget wrapped by this class
	 */
    private WindowBrowser browser;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenWindowBrowser() {
        
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * 
     * <br/><br/>
     * &lt;winbrowser key="string" winLimit="int" sizeToWindow="boolean"&gt;
     *  
     * @param node
     * @param screen
     */	
    public ScreenWindowBrowser(Node node, ScreenBase screen){
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            browser = (WindowBrowser)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            browser = new WindowBrowser();
        int limit = 10;
        if(node.getAttributes().getNamedItem("winLimit") != null){
            limit = Integer.parseInt(node.getAttributes().getNamedItem("winLimit").getNodeValue());
        }
        if(node.getAttributes().getNamedItem("sizeToWindow") != null)
            browser.init(true,limit);
        else
            browser.init(false,limit);
        initWidget(browser);
        browser.setStyleName("ScreenWindowBrowser");
        setDefaults(node,screen);
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen){
        return new ScreenWindowBrowser(node,screen);
    }
    
    public void destroy() {
        browser = null;
        super.destroy();
    }
    

}
