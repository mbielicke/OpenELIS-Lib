/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.widget.AToZTable;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableLabel;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

public class ScreenAToZTable extends ScreenWidget {
    public AToZTable azTable;
    public static final String TAG_NAME = "azTable";
    
    public ScreenAToZTable() {
    }

    public ScreenAToZTable(Node node, ScreenBase screen) {
        super(node);
        

        String width = "auto";
        if (node.getAttributes().getNamedItem("tablewidth") != null) {
            width = (node.getAttributes().getNamedItem("tablewidth").getNodeValue());
        }
        String title = "";
        if (node.getAttributes().getNamedItem("title") != null){
            title = (node.getAttributes().getNamedItem("title").getNodeValue());
        }
        int maxRows = ((Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue())));
        String[] widths = node.getAttributes().getNamedItem("colwidths").getNodeValue().split(",");
        ArrayList<TableColumnInt> columns = new ArrayList<TableColumnInt>(); 
        if(widths != null) {
            for (String wid : widths) {
                TableColumn col = new TableColumn();
                col.setCurrentWidth(Integer.parseInt(wid));
                col.setColumnWidget(new TableLabel());
                columns.add(col);
            }
        }
        boolean showHeader = false;
        if(node.getAttributes().getNamedItem("headers") != null){
            String[] headers = node.getAttributes().getNamedItem("headers").getNodeValue().split(",");
            if(headers != null){
                showHeader = true;
                for(int i = 0; i < headers.length; i++){
                    columns.get(i).setHeader(headers[i].trim());
                }
            }
        }

        //azPanel.sizeTable();
        
        azTable = new AToZTable(columns,maxRows,width,title,showHeader,VerticalScroll.NEVER);
        Node bpanel = ((Element)node).getElementsByTagName("buttonPanel").item(0);
        azTable.setButtonPanel(ScreenWidget.loadWidget(bpanel, screen));
        
        ((AppScreen)screen).addKeyboardListener(azTable);
        ((AppScreen)screen).addClickListener(azTable);
        initWidget(azTable);        
        setDefaults(node, screen);
    }
    

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenAToZTable(node, screen);
    }


}
