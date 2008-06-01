package org.openelis.gwt.screen;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.AToZTable;

public class ScreenAToZTable extends ScreenWidget {
    public AToZTable azTable;
    public static final String TAG_NAME = "azTable";
    
    public ScreenAToZTable() {
    }

    public ScreenAToZTable(Node node, ScreenBase screen) {
        super(node);
        
        azTable = new AToZTable();
        
        if (node.getAttributes().getNamedItem("tablewidth") != null) {
            azTable.setTableWidth(node.getAttributes().getNamedItem("tablewidth").getNodeValue());
        }
        if (node.getAttributes().getNamedItem("title") != null){
            azTable.view.setTitle(node.getAttributes().getNamedItem("title").getNodeValue());
        }
        azTable.setMaxRows((Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue())));
        String[] widths = node.getAttributes().getNamedItem("colwidths").getNodeValue().split(",");
        int[] width = new int[widths.length];
        for (int i = 0; i < widths.length; i++) {
            width[i] = Integer.parseInt(widths[i]);
        }
        azTable.setColWidths(width);
        if(node.getAttributes().getNamedItem("headers") != null){
            String[] headers = node.getAttributes().getNamedItem("headers").getNodeValue().split(",");
            azTable.view.setHeaders(headers);
        }
        Node bpanel = ((Element)node).getElementsByTagName("buttonPanel").item(0);
        azTable.setButtonPanel(ScreenWidget.loadWidget(bpanel, screen));
        azTable.view.initTable(azTable);
        //azPanel.sizeTable();
        
        ((AppScreen)screen).addKeyboardListener(azTable);
        ((AppScreen)screen).addClickListener(azTable);
        initWidget(azTable);        
        setDefaults(node, screen);
    }
    

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenAToZTable(node, screen);
    }


}
