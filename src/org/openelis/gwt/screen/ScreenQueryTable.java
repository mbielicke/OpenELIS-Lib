/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.TableField;
import org.openelis.gwt.widget.table.QueryTable;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableKeyboardHandler;
import org.openelis.gwt.widget.table.TableManager;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

/**
 * ScreenTable wraps the FormTable widget to be displayed
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenQueryTable extends ScreenInputWidget {
    

        /**
         * Default XML Tag Name for XML definition and WidgetMap
         */
        public static String TAG_NAME = "queryTable";
        /**
         * Widget wrapped by this class
         */
        private QueryTable table;
        /**
         * Default no-arg constructor used to create reference in the WidgetMap class
         */
        public ScreenQueryTable() {
        }
        
        @Override
        public void onBrowserEvent(Event event) {
            if(table.editingCell != null)
                return;
            super.onBrowserEvent(event);
        }
        /**
         * Constructor called from getInstance to return a specific instance of this class
         * to be displayed on the screen.  It uses the XML Node to create it's widget.
         * 
         * 
         * @param node
         * @param screen
         */ 
        public ScreenQueryTable(Node node, ScreenBase screen) {
            super(node);
            
           // try {
            int cellHeight;
            String width;
            int maxRows;
            String title = "";
            boolean enable = false;
            VerticalScroll showScroll = VerticalScroll.NEEDED;
            boolean showHeader = false;
            ArrayList<TableColumnInt> columns = new ArrayList<TableColumnInt>();

                Node widthsNode = ((Element)node).getElementsByTagName("widths")
                                                 .item(0);
                Node headersNode = null;
                if(((Element)node).getElementsByTagName("headers").getLength() > 0)
                    headersNode = ((Element)node).getElementsByTagName("headers")
                                                  .item(0);
                Node editorsNode = ((Element)node).getElementsByTagName("editors")
                                                  .item(0);
                Node fieldsNode = ((Element)node).getElementsByTagName("fields")
                                                 .item(0);
                Node filtersNode = ((Element)node).getElementsByTagName("filters")
                                                  .item(0);
                Node sortsNode = ((Element)node).getElementsByTagName("sorts")
                                                .item(0);
                Node alignNode = ((Element)node).getElementsByTagName("colAligns")
                                                .item(0);
                Node colFixed = ((Element)node).getElementsByTagName("fixed").item(0);
                
                if(node.getAttributes().getNamedItem("cellHeight") != null){
                    cellHeight = (Integer.parseInt(node.getAttributes().getNamedItem("cellHeight").getNodeValue()));
                }
                
                width = node.getAttributes().getNamedItem("width").getNodeValue();
                maxRows = Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue());
                //if constants = true we want to get the title from the properties file
                if(node.getAttributes().getNamedItem("title") != null){
                        title =node.getAttributes().getNamedItem("title").getNodeValue();
                }
                if(node.getAttributes().getNamedItem("enable") != null){
                    if(node.getAttributes().getNamedItem("enable").getNodeValue().equals("true"))
                        enable = true;
                }
                if(node.getAttributes().getNamedItem("showScroll") != null){
                    showScroll = VerticalScroll.valueOf((node.getAttributes().getNamedItem("showScroll").getNodeValue()));
                }
                if (widthsNode != null) {
                    String[] widths = widthsNode.getFirstChild()
                                                .getNodeValue()
                                                .split(",");
                    for (String wid : widths) {
                        TableColumn col = new TableColumn();
                        col.setCurrentWidth(Integer.parseInt(wid));
                        columns.add(col);
                    }
                }
                if(headersNode != null){
                    showHeader = true;
                    String[] headerNames = headersNode.getFirstChild()
                    .getNodeValue()
                    .split(",");
                    for(int i = 0; i < headerNames.length; i++){
                        columns.get(i).setHeader(headerNames[i].trim());
                    }                    
                }
                if (filtersNode != null) {
                    String[] filters = filtersNode.getFirstChild()
                                                  .getNodeValue()
                                                  .split(",");
                    for (int i = 0; i < filters.length; i++) {
                        columns.get(i).setFilterable(Boolean.valueOf(filters[i]).booleanValue());
                    }
                }
                if (sortsNode != null) {
                    String[] sorts = sortsNode.getFirstChild()
                                              .getNodeValue()
                                              .split(",");
                    for (int i = 0; i < sorts.length; i++) {
                        columns.get(i).setSortable(Boolean.valueOf(sorts[i]).booleanValue());
                    }
                }
                if (colFixed != null) {
                    String[] fixeds = colFixed.getFirstChild()
                                              .getNodeValue()
                                              .split(",");
                    for (int i = 0; i < fixeds.length; i++) {
                        columns.get(i).setFixedWidth(Boolean.valueOf(fixeds[i]).booleanValue());
                    }
                }
                if (alignNode != null){
                    String[] aligns = alignNode.getFirstChild().getNodeValue().split(",");
                    for (int i = 0; i < aligns.length; i++) {
                        if (aligns[i].equals("left"))
                            columns.get(i).setAlign(HasAlignment.ALIGN_LEFT);
                        if (aligns[i].equals("center"))
                            columns.get(i).setAlign(HasAlignment.ALIGN_CENTER);
                        if (aligns[i].equals("right"))
                            columns.get(i).setAlign(HasAlignment.ALIGN_RIGHT);
                    }
                }
                NodeList editors = editorsNode.getChildNodes();
                int j = 0; 
                for (int i = 0; i < editors.getLength(); i++) {
                    if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        columns.get(j).setColumnWidget((Widget)ScreenBase.createCellWidget(editors.item(i),screen));
                        j++;
                    }
                }
                if (fieldsNode != null) {
                    String[] fields = fieldsNode.getFirstChild()
                                              .getNodeValue()
                                              .split(",");
                    for (int i = 0; i < fields.length; i++) {
                        columns.get(i).setKey(fields[i].trim());
                    }
                }
                table = new QueryTable(columns,maxRows,width,title,showHeader,showScroll);
                table.enabled(enable);
            
            ((AppScreen)screen).addKeyboardListener(table.keyboardHandler);
            ((AppScreen)screen).addClickListener(table.mouseHandler);
            initWidget(table);
            displayWidget = table;
            table.setStyleName("ScreenTable");
            table.screen = this;
            setDefaults(node, screen);
        }

        public ScreenWidget getInstance(Node node, ScreenBase screen) {
            // TODO Auto-generated method stub
            return new ScreenQueryTable(node, screen);
        }

        public void load(AbstractField field) {
            table.enabled(true);
            table.activeCell = -1;
            table.activeRow = -1;
            table.load(screen.rpc);
            
        }

        public void submit(AbstractField field) {
            table.unload();
            for(TableColumnInt column : table.columns){
                screen.rpc.setFieldValue(column.getKey(), table.rpc.getFieldValue(column.getKey()));
            }
            //((FormRPC)field).setFieldMap(((FormRPC)table.unload()).getFieldMap());
        }

        public Widget getWidget() {
            return table;
        }
        
        public void destroy(){
            table = null;
            super.destroy();
        }
        
        public void setQueryWidget(ScreenInputWidget qWid){
            queryWidget = qWid;
            
        }
        
        public ScreenInputWidget getQueryWidget(){
            return queryWidget;
        }
        
        public void enable(boolean enabled){
            table.enabled(enabled);
            super.enable(enabled);
        }
        
        public void setFocus(boolean focus){
            table.setFocus(focus);
        }
        
        @Override
        public void drawError() {
        	table.model.refresh();
        }

}