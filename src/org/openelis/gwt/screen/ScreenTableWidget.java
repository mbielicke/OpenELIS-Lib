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

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.common.data.TableField;
import org.openelis.gwt.event.DragManager;
import org.openelis.gwt.event.DropManager;
import org.openelis.gwt.event.HasDragController;
import org.openelis.gwt.event.HasDropController;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableDragController;
import org.openelis.gwt.widget.table.TableIndexDropController;
import org.openelis.gwt.widget.table.TableKeyboardHandler;
import org.openelis.gwt.widget.table.TableManager;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;
import java.util.Vector;

/**
 * ScreenTable wraps the FormTable widget to be displayed
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenTableWidget extends ScreenInputWidget implements HasDragController, HasDropController {
    

        /**
         * Default XML Tag Name for XML definition and WidgetMap
         */
        public static String TAG_NAME = "table";
        /**
         * Widget wrapped by this class
         */
        private TableWidget table;
        private Vector<String> dropTargets;
        private boolean dropInited;
        /**
         * Default no-arg constructor used to create reference in the WidgetMap class
         */
        public ScreenTableWidget() {
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
        public ScreenTableWidget(Node node, ScreenBase screen) {
            super(node);
            init(node,screen);
        }
        
        public void init(Node node, ScreenBase screen) {
            if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
                table = (TableWidget)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
            else
                table = new TableWidget();
            
           // try {
            TableManager manager = null;
            int cellHeight;
            String width;
            int maxRows;
            String title = "";
            boolean autoAdd = false;
            boolean showRows = false;
            boolean enable = false;
            VerticalScroll showScroll = VerticalScroll.NEEDED;
            boolean showHeader = false;
            ArrayList<TableColumnInt> columns = new ArrayList<TableColumnInt>();
            DataModel data = new DataModel();
                if (node.getAttributes().getNamedItem("manager") != null) {
                    String appClass = node.getAttributes()
                                          .getNamedItem("manager")
                                          .getNodeValue();
                   
                    if("this".equals(appClass))
                        manager = (TableManager)screen;
                    else{
                        manager = (TableManager)ClassFactory.forName(appClass);
                    }
                }
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
                Node queryNode = ((Element)node).getElementsByTagName("query").item(0);
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
                if(node.getAttributes().getNamedItem("autoAdd") != null){
                    if(node.getAttributes().getNamedItem("autoAdd").getNodeValue().equals("true"))
                        autoAdd = true;
                }
                if(node.getAttributes().getNamedItem("showRows") != null){
                    if(node.getAttributes().getNamedItem("showRows").getNodeValue().equals("true"))
                        showRows = true;
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
                    if(((Element)headersNode).getElementsByTagName("menuItem").getLength() > 0) {
                        NodeList menus = headersNode.getChildNodes();
                        int j = 0;
                        for(int i = 0; i < menus.getLength(); i++) {
                            if(menus.item(i).getNodeType() == Node.ELEMENT_NODE && menus.item(i).getNodeName().equals("menuItem")) {
                                columns.get(j).setHeaderMenu((ScreenMenuItem)ScreenWidget.loadWidget(menus.item(i), screen));
                                j++;
                            }
                        }
                    }else{
                        String[] headerNames = headersNode.getFirstChild().getNodeValue().split(",");
                        for(int i = 0; i < headerNames.length; i++){
                            columns.get(i).setHeader(headerNames[i].trim());
                        }
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
                if (queryNode != null) {
                    String[] query = queryNode.getFirstChild().getNodeValue().split(",");
                    for(int i = 0; i < query.length; i++) {
                        columns.get(i).setQuerayable(Boolean.valueOf(query[i]).booleanValue());
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
                NodeList fieldList = fieldsNode.getChildNodes();
                DataSet<Object> set = new DataSet<Object>();
                for (int i = 0; i < fieldList.getLength(); i++) {
                    if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        AbstractField field = (ScreenBase.createField(fieldList.item(i)));
                        set.add((FieldType)field);
                        columns.get(i).setKey(field.key);
                    }
                }
                data.setDefaultSet(set);
                table.init(columns,maxRows,width,title,showHeader,showScroll);
                if(node.getAttributes().getNamedItem("multiSelect") != null){
                    if(node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
                        table.model.enableMultiSelect(true);
                }
                int rows = 0;
                if (node.getAttributes().getNamedItem("rows") != null) {
                    rows = Integer.parseInt(node.getAttributes()
                                                .getNamedItem("rows")
                                                .getNodeValue());
                    for(int i = 0; i < rows; i++){
                        data.addDefault();
                    }
                }
                if (node.getAttributes().getNamedItem("targets") != null) {
                    dropTargets = new Vector<String>();
                    String targets[] = node.getAttributes()
                                          .getNamedItem("targets")
                                          .getNodeValue().split(",");
                    for(int i = 0; i < targets.length; i++){
                            dropTargets.add(targets[i]);
                    }
                    table.dragController = new TableDragController(RootPanel.get());
                }
                if (node.getAttributes().getNamedItem("dropManager") != null){
                    table.dropController = new TableIndexDropController(table);
                    String appClass = node.getAttributes()
                    .getNamedItem("dropManager")
                    .getNodeValue();

                    if("this".equals(appClass))
                        table.dropController.manager =(DropManager)screen;
                    else{
                        table.dropController.manager = (DropManager)ClassFactory.forName(appClass);
                    }
                }
                if (node.getAttributes().getNamedItem("dragManager") != null){
                    getDragController();
                    String appClass = node.getAttributes()
                    .getNamedItem("dragManager")
                    .getNodeValue();

                    if("this".equals(appClass))
                        table.dragController.manager =(DragManager)screen;
                    else{
                        table.dragController.manager = (DragManager)ClassFactory.forName(appClass);
                    }
                }
               // enable(enable);
                table.model.setManager(manager);
                
                
            ((AppScreen)screen).addKeyboardListener(table.keyboardHandler);
            ((AppScreen)screen).addClickListener(table.mouseHandler);
            initWidget(table);
            table.model.load(data);
            displayWidget = table;
            table.setStyleName("ScreenTable");
            ((TableKeyboardHandler)table.keyboardHandler).setScreen(this);
            setDefaults(node, screen);
            table.screenWidget = this;
        }

        public ScreenWidget getInstance(Node node, ScreenBase screen) {
            // TODO Auto-generated method stub
            return new ScreenTableWidget(node, screen);
        }

        public void load(AbstractField field) {
            if(!queryMode){
                if (field.getValue() != null)
                    table.model.load((DataModel)field.getValue());
                else{
                    table.model.clear();
                    field.setValue(table.model.getData());
                }
            }else {
                if(queryWidget instanceof ScreenTableWidget){
                    if(field.getValue() != null){
                        if (field.getValue() != null)
                            ((ScreenTableWidget)queryWidget).table.model.load((DataModel)field.getValue());
                    }
                }else{
                    queryWidget.load(field);
                }
            }
        }

        public void submit(AbstractField field) {
            if(queryMode)
                queryWidget.submit(field);
            else{
                field.setValue(table.model.unload());
                ArrayList<String> fieldIndex = new ArrayList<String>();
                for(TableColumnInt col : (ArrayList<TableColumnInt>)table.columns)
                    fieldIndex.add(col.getKey());
                ((TableField)field).setFieldIndex(fieldIndex);
            }
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
            if(enabled && !dropInited){
                if(dropTargets.size() > 0){
                    for(String target : dropTargets) {         
                        getDragController().registerDropController(((HasDropController)screen.widgets.get(target)).getDropController());
                    }
                }
                dropInited = true;    
            }
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
        
        public TableIndexDropController getDropController() {
            if(table.dropController == null)
                table.dropController = new TableIndexDropController(table);
            return table.dropController;
            
        }

        public TableDragController getDragController() {
            if(table.dragController == null)
                table.dragController = new TableDragController(RootPanel.get());
            return table.dragController;
        }

        public void setDragController(DragController controller) {
            table.dragController = (TableDragController)controller;
            
        }

        public void setDropController(DropController controller) {
            table.dropController = (TableIndexDropController)controller;
            
        }
}
        
       
