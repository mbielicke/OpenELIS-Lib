package org.openelis.gwt.client.screen;

import org.openelis.gwt.client.widget.ButtonPanel;
import org.openelis.gwt.client.widget.TitledPanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class ScreenTitledPanel extends ScreenWidget{

	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "titledPanel";
	/**
	 * Widget wrapped by this class
	 */
	private TitledPanel titledPanel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTitledPanel() {
    }
    
    public ScreenTitledPanel(Node node, ScreenBase screen) {
        super(node);
        titledPanel = new TitledPanel();
        
        Element legend = (Element) ((Element)node).getElementsByTagName("legend").item(0);
        NodeList widgets = legend.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Node input = null;
                if (widgets.item(k).getNodeName().equals("widget")) {
                    NodeList inputList = widgets.item(k).getChildNodes();
                    for (int m = 0; m < inputList.getLength(); m++) {
                        if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                            input = inputList.item(m);
                            m = 100;
                        }
                    }
                } else
                    input = widgets.item(k);
                Widget wid = Screen.getWidgetMap().getWidget(input, screen);
                titledPanel.setTitle(wid);
            }
        }
        
        legend = (Element) ((Element)node).getElementsByTagName("content").item(0);
        widgets = legend.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Node input = null;
                if (widgets.item(k).getNodeName().equals("widget")) {
                    NodeList inputList = widgets.item(k).getChildNodes();
                    for (int m = 0; m < inputList.getLength(); m++) {
                        if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                            input = inputList.item(m);
                            m = 100;
                        }
                    }
                } else
                    input = widgets.item(k);
                Widget wid = Screen.getWidgetMap().getWidget(input, screen);
                titledPanel.setWidget(wid);
            }
        }
       
        initWidget(titledPanel);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTitledPanel(node, screen);
    }
}
