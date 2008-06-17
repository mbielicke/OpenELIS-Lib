package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * ScreenStack wraps a GWT StackPanel for displaying
 * widgets on Screen.
 * @author tschmidt
 *
 */
public class ScreenStack extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "StackPanel";
	/**
	 * Widget wrapped by this class
	 */
    private StackPanel stack;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenStack() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="stack" key="string"&gt;
     *   &lt;stack text="string"&gt;
     *     ....
     *   &lt;/stack&gt;
     *   &lt;stack text="string"&gt;
     *     ...
     *   &lt;/stack&gt;
     * &lt;/panel&gt;
     *   
     * @param node
     * @param screen
     */	
    public ScreenStack(Node node, ScreenBase screen) {
        super(node);
        stack = new StackPanel();
        stack.setStyleName("ScreenStack");
        //stack.setStylePrimaryName("ScreenStack");
        initWidget(stack);
        NodeList stacks = ((Element)node).getElementsByTagName("stack");
        for (int k = 0; k < stacks.getLength(); k++) {
            NodeList widgets = stacks.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    stack.add(wid, stacks.item(k)
                                         .getAttributes()
                                         .getNamedItem("text")
                                         .getNodeValue());
                }
            }
        }
        stack.showStack(0);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenStack(node, screen);
    }
    
    public void destroy() {
        stack = null;
        super.destroy();
    }
}
