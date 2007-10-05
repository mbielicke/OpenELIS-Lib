package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.HTML;
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
    private HTML html;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenHTML() {
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
    public ScreenHTML(Node node, Screen screen) {
        super(node);
        html = new HTML(node.getFirstChild().getNodeValue());
        if (node.getAttributes().getNamedItem("onclick") != null) {
        	String listener = node.getAttributes().getNamedItem("onclick").getNodeValue();
        	if(listener.equals("this"))
        		addClickListener(screen);
        	else
        		addClickListener((ClickListener)Screen.getWidgetMap().get(listener));
        }
        initWidget(html);
        html.setStyleName("ScreenHTML");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
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

}
