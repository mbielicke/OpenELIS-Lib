package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;

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
    public ScreenButtonPanel(Node node, Screen screen) {
        super(node);
        bPanel = null;
        if (node.getAttributes().getNamedItem("buttons") != null)
            bPanel = new ButtonPanel(node.getAttributes()
                                         .getNamedItem("buttons")
                                         .getNodeValue());
        else
            bPanel = new ButtonPanel("all");
        
        if(screen.constants != null){
        	((Label)bPanel.abort.getWidget()).setText(screen.constants.getString("abort"));
        	((Label)bPanel.add.getWidget()).setText(screen.constants.getString("add"));
        	((Label)bPanel.comm.getWidget()).setText(screen.constants.getString("commit"));
        	((Label)bPanel.delete.getWidget()).setText(screen.constants.getString("delete"));
        	((Label)bPanel.next.getWidget()).setText(screen.constants.getString("next"));
        	((Label)bPanel.prev.getWidget()).setText(screen.constants.getString("previous"));
        	((Label)bPanel.query.getWidget()).setText(screen.constants.getString("query"));
        	((Label)bPanel.reload.getWidget()).setText(screen.constants.getString("reload"));
        	((Label)bPanel.up.getWidget()).setText(screen.constants.getString("update"));
        }
        initWidget(bPanel);
        bPanel.setStyleName("ScreenButtonPanel");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenButtonPanel(node, screen);
    }
    
    public void destroy(){
        bPanel = null;
        super.destroy();
    }
    

}
