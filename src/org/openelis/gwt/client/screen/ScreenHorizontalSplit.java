package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * ScreenSplit wraps a GWT HorizontalSplitPanel for display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenHorizontalSplit extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "panel-hsplit";
	/**
	 * Widget wrapped by this class
	 */
    private HorizontalSplitPanel hp;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenHorizontalSplit() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;panel layout="hsplit" splitpos="int" sizeToWindow="boolean"&gt;
     *   &lt;section&gt;
     *     WIDGETS FOR LEFT PANEL
     *   &lt;/section&gt;
     *   &lt;section&gt;
     *     WIDGETS FOR RIGHT PANEL
     *   &lt;/section&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenHorizontalSplit(Node node, Screen screen) {
        super(node);
        hp = new HorizontalSplitPanel();
        hp.setStyleName("ScreenSplit");
        initWidget(hp);
        NodeList sections = ((Element)node).getElementsByTagName("section");
        for (int k = 0; k < sections.getLength(); k++) {
            NodeList widgets = sections.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Node input = null;
                    if (widgets.item(l).getNodeName().equals("widget")) {
                        NodeList inputList = widgets.item(l).getChildNodes();
                        for (int m = 0; m < inputList.getLength(); m++) {
                            if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                                input = inputList.item(m);
                                m = 100;
                            }
                        }
                    } else
                        input = widgets.item(l);
                    Widget wid = Screen.getWidgetMap().getWidget(input, screen);
                    if (k == 0) {
                            hp.setLeftWidget(wid);
                    }
                    if (k == 1) {
                            hp.setRightWidget(wid);
                    }
                }
            }
        }
        if (node.getAttributes().getNamedItem("splitpos") != null) {
            String splitpos = node.getAttributes()
                                  .getNamedItem("splitpos")
                                  .getNodeValue();
                hp.setSplitPosition(splitpos);
        }
        if (node.getAttributes().getNamedItem("sizeToWindow") != null){
        	if(node.getAttributes().getNamedItem("sizeToWindow").getNodeValue().equals("true")){
                Window.addWindowResizeListener(new WindowResizeListener() {

                    public void onWindowResized(int width, int height) {
                            setBrowserHeight(hp);
                    }

                });
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                            setBrowserHeight(hp);
                    }
                });
        	}
        }
                
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        return new ScreenHorizontalSplit(node, screen);
    }
 
    private void setBrowserHeight(Widget wid) {
        if (wid.isVisible()) {
            wid.setHeight((Window.getClientHeight() - wid.getAbsoluteTop() - 10) + "px");
            wid.setWidth((Window.getClientWidth() - wid.getAbsoluteLeft() - 10) + "px");
        }
    }
    
    public void destroy(){
        hp = null;
        super.destroy();
    }
}
