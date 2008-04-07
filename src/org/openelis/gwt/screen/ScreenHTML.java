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
        html = new HTML(node.getFirstChild().getNodeValue());
        if (node.getAttributes().getNamedItem("onclick") != null) {
        	String[] listeners = node.getAttributes().getNamedItem("onclick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    addClickListener(screen);
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
