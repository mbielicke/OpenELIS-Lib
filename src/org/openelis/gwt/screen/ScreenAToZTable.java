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
import com.google.gwt.user.client.dnd.DropListener;
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
import org.openelis.gwt.event.DragManager;
import org.openelis.gwt.event.DropManager;
import org.openelis.gwt.event.HasDropController;
import org.openelis.gwt.widget.AToZTable;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableDragController;
import org.openelis.gwt.widget.table.TableIndexDropController;
import org.openelis.gwt.widget.table.TableKeyboardHandler;
import org.openelis.gwt.widget.table.TableLabel;
import org.openelis.gwt.widget.table.TableManager;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;
import java.util.Vector;

public class ScreenAToZTable extends ScreenInputWidget {
    public AToZTable azTable;
    public static final String TAG_NAME = "azTable";
    public Vector<String> dropTargets;
    public boolean dropInited;
    
    public ScreenAToZTable() {
    }

    public ScreenAToZTable(Node node, ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            azTable = (AToZTable)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            azTable = new AToZTable();
        NodeList children = node.getChildNodes();
        Node bpanel = null;
        Node table = null;
        Node query = null;
        for(int i = 0; i < children.getLength(); i++){
            if(children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if(children.item(i).getNodeName().equals("buttonPanel"))
                    bpanel = children.item(i);
                else if(children.item(i).getNodeName().equals("table"))
                    table = children.item(i);
                else if(children.item(i).getNodeName().equals("query"))
                    query = children.item(i);
            }
        }
        if(table == null) {
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
            azTable.init(columns,maxRows,width,title,showHeader,VerticalScroll.NEVER);
        }else{
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
                if (table.getAttributes().getNamedItem("manager") != null) {
                    String appClass = table.getAttributes()
                                          .getNamedItem("manager")
                                          .getNodeValue();
                   
                    if("this".equals(appClass))
                        manager = (TableManager)screen;
                    else{
                        manager = (TableManager)ClassFactory.forName(appClass);
                    }
                }
                Node widthsNode = ((Element)table).getElementsByTagName("widths")
                                                 .item(0);
                Node headersNode = null;
                if(((Element)table).getElementsByTagName("headers").getLength() > 0)
                    headersNode = ((Element)table).getElementsByTagName("headers")
                                                  .item(0);
                Node editorsNode = ((Element)table).getElementsByTagName("editors")
                                                  .item(0);
                Node fieldsNode = ((Element)table).getElementsByTagName("fields")
                                                 .item(0);
                Node filtersNode = ((Element)table).getElementsByTagName("filters")
                                                  .item(0);
                Node sortsNode = ((Element)table).getElementsByTagName("sorts")
                                                .item(0);
                Node queryNode = ((Element)node).getElementsByTagName("query").item(0);
                Node alignNode = ((Element)table).getElementsByTagName("colAligns")
                                                .item(0);
                Node colFixed = ((Element)table).getElementsByTagName("fixed").item(0);
                
                if(table.getAttributes().getNamedItem("cellHeight") != null){
                    cellHeight = (Integer.parseInt(table.getAttributes().getNamedItem("cellHeight").getNodeValue()));
                }
                
                width = table.getAttributes().getNamedItem("width").getNodeValue();
                maxRows = Integer.parseInt(table.getAttributes().getNamedItem("maxRows").getNodeValue());
                //if constants = true we want to get the title from the properties file
                if(table.getAttributes().getNamedItem("title") != null){
                        title =table.getAttributes().getNamedItem("title").getNodeValue();
                }
                if(table.getAttributes().getNamedItem("autoAdd") != null){
                    if(table.getAttributes().getNamedItem("autoAdd").getNodeValue().equals("true"))
                        autoAdd = true;
                }
                if(table.getAttributes().getNamedItem("showRows") != null){
                    if(table.getAttributes().getNamedItem("showRows").getNodeValue().equals("true"))
                        showRows = true;
                }
                if(table.getAttributes().getNamedItem("enable") != null){
                    if(table.getAttributes().getNamedItem("enable").getNodeValue().equals("true"))
                        enable = true;
                }
                if(table.getAttributes().getNamedItem("showScroll") != null){
                    showScroll = VerticalScroll.valueOf((table.getAttributes().getNamedItem("showScroll").getNodeValue()));
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
                    String[] queryCols = queryNode.getFirstChild().getNodeValue().split(",");
                    for(int i = 0; i < queryCols.length; i++) {
                        columns.get(i).setQuerayable(Boolean.valueOf(queryCols[i]).booleanValue());
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
                azTable.init(columns,maxRows,width,title,showHeader,showScroll);
                if(table.getAttributes().getNamedItem("multiSelect") != null){
                    if(table.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
                        azTable.model.enableMultiSelect(true);
                }
                if (node.getAttributes().getNamedItem("targets") != null) {
                    dropTargets = new Vector<String>();
                    String targets[] = node.getAttributes()
                                          .getNamedItem("targets")
                                          .getNodeValue().split(",");
                    for(int i = 0; i < targets.length; i++){
                            dropTargets.add(targets[i]);
                    }
                    azTable.dragController = new TableDragController(RootPanel.get());
                }
                if (node.getAttributes().getNamedItem("dropManager") != null){
                    azTable.dropController = new TableIndexDropController(azTable);
                    String appClass = node.getAttributes()
                    .getNamedItem("dropManager")
                    .getNodeValue();

                    if("this".equals(appClass))
                        azTable.dropController.manager =(DropManager)screen;
                    else{
                        azTable.dropController.manager = (DropManager)ClassFactory.forName(appClass);
                    }
                }
                if (node.getAttributes().getNamedItem("dragManager") != null){
                    getDragController();
                    String appClass = node.getAttributes()
                    .getNamedItem("dragManager")
                    .getNodeValue();

                    if("this".equals(appClass))
                        azTable.dragController.manager =(DragManager)screen;
                    else{
                        azTable.dragController.manager = (DragManager)ClassFactory.forName(appClass);
                    }
                }
                azTable.enabled(enable);
                azTable.model.setManager(manager);
                int rows = 0;
                if (node.getAttributes().getNamedItem("rows") != null) {
                    rows = Integer.parseInt(node.getAttributes()
                                                .getNamedItem("rows")
                                                .getNodeValue());
                    for(int i = 0; i < rows; i++){
                        data.addDefault();
                    }
                }
            azTable.model.load(data);
            
            azTable.setStyleName("ScreenTable");
            ((TableKeyboardHandler)azTable.keyboardHandler).setScreen(this);
            azTable.screenWidget = this;
        }
        if(query != null){
            NodeList inputList = query.getChildNodes();
            Node input = null;
            for (int m = 0; m < inputList.getLength(); m++) {
                if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                    input = inputList.item(m);
                    m = 100;
                }
            }
            Widget queryWid = ScreenBase.createWidget(input, screen);
            setQueryWidget((ScreenInputWidget)queryWid);
        }
        displayWidget = azTable;

        azTable.setButtonPanel(ScreenWidget.loadWidget(bpanel, screen));
        
        ((AppScreen)screen).addKeyboardListener(azTable);
        ((AppScreen)screen).addClickListener(azTable);
        initWidget(azTable);        
        setDefaults(node, screen);
    }
    

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenAToZTable(node, screen);
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
        azTable.enabled(enabled);
        super.enable(enabled);
    }
    
    public void setFocus(boolean focus){
        azTable.setFocus(focus);
    }
    
    @Override
    public void drawError() {
        azTable.model.refresh();
    }
    
    public TableIndexDropController getDropController() {
        if(azTable.dropController == null)
            azTable.dropController = new TableIndexDropController(azTable);
        return azTable.dropController;
        
    }

    public TableDragController getDragController() {
        if(azTable.dragController == null)
            azTable.dragController = new TableDragController(RootPanel.get());
        return azTable.dragController;
    }

    public void setDragController(DragController controller) {
        azTable.dragController = (TableDragController)controller;
        
    }

    public void setDropController(DropController controller) {
        azTable.dropController = (TableIndexDropController)controller;
        
    }

}
