package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * ScreenVerticalSplit wraps a GWT VerticalSplitPanel for displaying
 * widgets on a Screen.
 * @author tschmidt
 *
 */
public class ScreenVerticalSplit extends ScreenWidget{
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "panel-vsplit";
	/**
	 * Widget wrapped by this class
	 */
    private VerticalSplitPanel vp;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenVerticalSplit() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="vsplit" key="string" splitpos="int" sizeToWindow="boolean"&gt;
     *   &lt;section&gt;
     *     WIDGETS FOR TOP PANEL
     *   &lt;/section&lt;
     *   &lt;section&gt;
     *     WIDGETS FOR BOTTOM PANEL
     *   &lt;/section&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenVerticalSplit(Node node, ScreenBase screen) {
        super(node);
        vp = new VerticalSplitPanel();
        vp.setStyleName("ScreenSplit");
        initWidget(vp);
        NodeList sections = ((Element)node).getElementsByTagName("section");
        for (int k = 0; k < sections.getLength(); k++) {
            NodeList widgets = sections.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    if (k == 0) {
                             vp.setTopWidget(wid);
                    }
                    if (k == 1) {
                             vp.setBottomWidget(wid);
                    }
                }
            }
        }
        if (node.getAttributes().getNamedItem("splitpos") != null) {
            String splitpos = node.getAttributes()
                                  .getNamedItem("splitpos")
                                  .getNodeValue();
                 vp.setSplitPosition(splitpos);
        }
        if (node.getAttributes().getNamedItem("sizeToWindow") != null){
        	if (node.getAttributes().getNamedItem("sizeToWindow").getNodeValue().equals("true")){
                Window.addWindowResizeListener(new WindowResizeListener() {

                    public void onWindowResized(int width, int height) {
                             setBrowserHeight(vp);
                    }

                });
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                            setBrowserHeight(vp);
                    }
                });
        	}
        }
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenHorizontalSplit(node, screen);
    }

    private void setBrowserHeight(Widget wid) {
        if (wid.isVisible()) {
            wid.setHeight((Window.getClientHeight() - wid.getAbsoluteTop() - 10) + "px");
            wid.setWidth((Window.getClientWidth() - wid.getAbsoluteLeft() - 10) + "px");
        }
    }
    
    public void destroy() {
        vp = null;
        super.destroy();
    }
}
