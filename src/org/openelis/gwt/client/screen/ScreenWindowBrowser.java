package org.openelis.gwt.client.screen;

import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.WindowBrowser;

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
    public ScreenWindowBrowser(Node node, Screen screen){
        super(node);
        int limit = 10;
        if(node.getAttributes().getNamedItem("winLimit") != null){
            limit = Integer.parseInt(node.getAttributes().getNamedItem("winLimit").getNodeValue());
        }
        if(node.getAttributes().getNamedItem("sizeToWindow") != null)
            browser = new WindowBrowser(true,limit);
        else
            browser = new WindowBrowser(false,limit);
        initWidget(browser);
        browser.setStyleName("ScreenWindowBrowser");
        setDefaults(node,screen);
    }
    
    public ScreenWidget getInstance(Node node, Screen screen){
        return new ScreenWindowBrowser(node,screen);
    }
    
    public void destroy() {
        browser = null;
        super.destroy();
    }
    

}
