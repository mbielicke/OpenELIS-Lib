package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenDeck wraps a GWT DeckPanel to layout widgets in a Decks on a Screen.
 * @author tschmidt
 *
 */
public class ScreenDeck extends ScreenWidget {
	/**
	 * Default XML Tag Name in XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "DeckPanel";
	/**
	 * Widget that is wrapped by this class
	 */
	private DeckPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenDeck() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="deck" key="string"&gt:
     *   &lt;deck&gt;
     *     &lt;widget/&gt;...
     *   &lt;/deck&gt; 
     * &gt;/panel&lt;
     * 
     * @param node
     * @param screen
     */
    public ScreenDeck(Node node, ScreenBase screen) {
        super(node);
        panel = new DeckPanel();
        panel.setStyleName("ScreenDeck");
        initWidget(panel);
        NodeList decks = ((Element)node).getElementsByTagName("deck");
        for (int k = 0; k < decks.getLength(); k++) {
            NodeList widgets = decks.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    panel.add(wid);
                }
            }
        }
        panel.showWidget(0);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenDeck(node, screen);
    }
    
    public void destroy(){
        panel = null;
        super.destroy();
    }

}
