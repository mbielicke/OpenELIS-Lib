package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.client.widget.AppButton;
import org.openelis.gwt.client.widget.ButtonPanel;

/**
 * ScreenButtonPanel wraps a ButtonPanel widget to be displayed on a screen
 * @author tschmidt
 *
 */
public class ScreenButtonPanel extends ScreenWidget {
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "buttonPanel";
	/**
	 * Widget wrapped by this class
	 */
	private ButtonPanel bPanel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenButtonPanel() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;buttonPanel key="string" buttons="qunpbdcra"/&gt;
     * <br/>
     * <pre>
     * buttons attribute represents which buttons to display
     *   q - Query
     *   u - Update
     *   n - Next
     *   p - Previous
     *   b - Abort
     *   d - Delete
     *   c - Commit
     *   r - Reload
     *   a - Add
     * </pre>
     * @param node
     * @param screen
     */
    public ScreenButtonPanel(Node node, ScreenBase screen) {
        super(node);
        bPanel = new ButtonPanel();
        NodeList buttons = node.getChildNodes();
        for (int k = 0; k < buttons.getLength(); k++) {
            if(buttons.item(k).getNodeType() == Node.ELEMENT_NODE){
               Widget wid = ScreenWidget.loadWidget(buttons.item(k), screen);
               bPanel.addWidget(wid);
            }
        }
        initWidget(bPanel);
        bPanel.setStyleName("ScreenButtonPanel");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenButtonPanel(node, screen);
    }
    
    public void destroy(){
        bPanel = null;
        super.destroy();
    }
    

}
