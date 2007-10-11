package org.openelis.gwt.client.screen;

import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.TabBrowser;
/**
 * ScreenTabBrowser wraps a TabBrowser widget to be displayed
 * on a screen.
 * @author tschmidt
 *
 */
public class ScreenTabBrowser extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "tabbrowser";
	/**
	 * Widget wrapped by this class
	 */
    private TabBrowser tb;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTabBrowser() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;tabbrowser key="string" tabLimit="int" sizeToWindow="boolean"/&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenTabBrowser(Node node, Screen screen) {
        super(node);
        int tabLimit = Integer.parseInt(node.getAttributes()
                                            .getNamedItem("tabLimit")
                                            .getNodeValue());
        if(node.getAttributes().getNamedItem("sizeToWindow") != null){
        	if(node.getAttributes().getNamedItem("sizeToWindow").getNodeValue().equals("true"))
        		tb = new TabBrowser(true, tabLimit);
        	else
        		tb = new TabBrowser(false, tabLimit);
        }
        initWidget(tb);
        tb.setStyleName("ScreenTabBrowser");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenTabBrowser(node, screen);
    }
    
    public void destroy() {
        tb = null;
        super.destroy();
    }
}
