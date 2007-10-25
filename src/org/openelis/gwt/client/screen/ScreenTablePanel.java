package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenTablePanel wraps a GWT FlexTable to display 
 * widgets on a Screen in table layout.
 * @author tschmidt
 *
 */
public class ScreenTablePanel extends ScreenWidget {
	/**
	 * Reference String for Widget Map
	 */
	public static String TAG_NAME = "panel-table";
	/**
	 * Widget wrapped by this class
	 */
	private FlexTable panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTablePanel() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;panel layout="table" id="string"&gt;
     *   &lt;row&gt;
     *     &lt;widget/&gt;
     *   &lt;/row&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenTablePanel(Node node, Screen screen) {
        super(node);
        panel = new FlexTable();
        panel.setStyleName("ScreenTablePanel");
        initWidget(panel);
        String tableId = node.getAttributes().getNamedItem("key").getNodeValue();
        if (node.getAttributes().getNamedItem("spacing") != null)
            panel.setCellSpacing(Integer.parseInt(node.getAttributes()
                                                      .getNamedItem("spacing")
                                                      .getNodeValue()));
        if (node.getAttributes().getNamedItem("padding") != null)
            panel.setCellSpacing(Integer.parseInt(node.getAttributes()
                                                      .getNamedItem("padding")
                                                      .getNodeValue()));
        NodeList rows = ((Element)node).getElementsByTagName("row");
        for (int k = 0; k < rows.getLength(); k++) {
            NodeList widgets = rows.item(k).getChildNodes();
            int w = -1;
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    w++;
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
                    Widget wid = null;
                    if (input != null)
                        wid = Screen.getWidgetMap().getWidget(input, screen);
                    else
                        wid = new Label("");
                    panel.setWidget(k, w, wid);
                    if (widgets.item(l).getAttributes().getNamedItem("colspan") != null)
                        panel.getFlexCellFormatter()
                             .setColSpan(k,
                                         w,
                                         Integer.parseInt(widgets.item(l)
                                                                 .getAttributes()
                                                                 .getNamedItem("colspan")
                                                                 .getNodeValue()));
                    if (widgets.item(l).getAttributes().getNamedItem("rowspan") != null)
                        panel.getFlexCellFormatter()
                             .setRowSpan(k,
                                         w,
                                         Integer.parseInt(widgets.item(l)
                                                                 .getAttributes()
                                                                 .getNamedItem("rowspan")
                                                                 .getNodeValue()));
                    if (widgets.item(l).getAttributes().getNamedItem("style") != null)
                        panel.getFlexCellFormatter()
                             .addStyleName(k,
                                           w,
                                           widgets.item(l)
                                                  .getAttributes()
                                                  .getNamedItem("style")
                                                  .getNodeValue());
                    if (widgets.item(l).getAttributes().getNamedItem("align") != null) {
                        String align = widgets.item(l)
                                              .getAttributes()
                                              .getNamedItem("align")
                                              .getNodeValue();
                        if (align.equals("right"))
                            panel.getFlexCellFormatter()
                                 .setHorizontalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_RIGHT);
                        if (align.equals("left"))
                            panel.getFlexCellFormatter()
                                 .setHorizontalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_LEFT);
                        if (align.equals("center"))
                            panel.getFlexCellFormatter()
                                 .setHorizontalAlignment(k,
                                                         w,
                                                         HasAlignment.ALIGN_CENTER);
                    }
                }
            }
            if (rows.item(k).getAttributes().getNamedItem("type") != null) {
                panel.setText(k, 0, " ");
                panel.getFlexCellFormatter().addStyleName(k, 0, "Prompt");
                panel.getRowFormatter().setVisible(k, false);
                com.google.gwt.user.client.Element el = panel.getRowFormatter()
                                                             .getElement(k);
                DOM.setIntAttribute(el, "row", k);
                DOM.setAttribute(el, "table", tableId);
                screen.errors.put(rows.item(k)
                                      .getAttributes()
                                      .getNamedItem("id")
                                      .getNodeValue(), el);
            } else {
                if (rows.item(k).getAttributes().getNamedItem("id") != null)
                    screen.widgets.put(rows.item(k)
                                          .getAttributes()
                                          .getNamedItem("id")
                                          .getNodeValue(),
                                      panel.getRowFormatter().getElement(k));
            }
        }
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenTablePanel(node, screen);
    }
    
    public void destroy() {
        panel = null;
        super.destroy();
    }

}
