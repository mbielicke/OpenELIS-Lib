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
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.common.data.TreeDataModel;
import org.openelis.gwt.common.data.TreeField;
import org.openelis.gwt.event.DragManager;
import org.openelis.gwt.event.DropManager;
import org.openelis.gwt.event.HasDragController;
import org.openelis.gwt.event.HasDropController;
import org.openelis.gwt.widget.table.TableIndexDropController;
import org.openelis.gwt.widget.tree.TreeColumn;
import org.openelis.gwt.widget.tree.TreeColumnInt;
import org.openelis.gwt.widget.tree.TreeDragController;
import org.openelis.gwt.widget.tree.TreeIndexDropController;
import org.openelis.gwt.widget.tree.TreeKeyboardHandler;
import org.openelis.gwt.widget.tree.TreeManager;
import org.openelis.gwt.widget.tree.TreeServiceCall;
import org.openelis.gwt.widget.tree.TreeServiceCallInt;
import org.openelis.gwt.widget.tree.TreeWidget;
import org.openelis.gwt.widget.tree.TreeViewInt.VerticalScroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * ScreenTable wraps the FormTable widget to be displayed
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenTreeWidget extends ScreenInputWidget implements HasDragController, HasDropController{
    

        /**
         * Default XML Tag Name for XML definition and WidgetMap
         */
        public static String TAG_NAME = "tree-table";
        /**
         * Widget wrapped by this class
         */
        private TreeWidget tree ;
        private Vector<String> dropTargets = new Vector<String>();
        private boolean dropInited;
        /**
         * Default no-arg constructor used to create reference in the WidgetMap class
         */
        public ScreenTreeWidget() {
        }
        /**
         * Constructor called from getInstance to return a specific instance of this class
         * to be displayed on the screen.  It uses the XML Node to create it's widget.
         * 
         * 
         * @param node
         * @param screen
         */ 
        public ScreenTreeWidget(Node node, ScreenBase screen) {
            super(node);
            init(node,screen);
        }
        
        public void init(Node node, ScreenBase screen) {
            if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
                tree = (TreeWidget)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
            else
                tree = new TreeWidget();
            // try {
             TreeManager manager = null;
             int cellHeight;
             String width;
             int maxRows;
             String title = "";
             boolean autoAdd = false;
             boolean showRows = false;
             boolean enable = false;
             VerticalScroll showScroll = VerticalScroll.NEEDED;
             boolean showHeader = false;
             ArrayList<TreeColumnInt> columns = new ArrayList<TreeColumnInt>();
             TreeDataModel data = new TreeDataModel();
                 if (node.getAttributes().getNamedItem("manager") != null) {
                     String appClass = node.getAttributes()
                                           .getNamedItem("manager")
                                           .getNodeValue();
                    
                     if("this".equals(appClass))
                         manager = (TreeManager)screen;
                     else{
                         manager = (TreeManager)ClassFactory.forName(appClass);
                     }
                 }
                 Node widthsNode = ((Element)node).getElementsByTagName("widths")
                                                  .item(0);
                 Node headersNode = null;
                 if(((Element)node).getElementsByTagName("headers").getLength() > 0)
                     headersNode = ((Element)node).getElementsByTagName("headers")
                                                   .item(0);
                 Node filtersNode = ((Element)node).getElementsByTagName("filters")
                 .item(0);
                 Node sortsNode = ((Element)node).getElementsByTagName("sorts")
                 .item(0);
                
                 Node alignNode = ((Element)node).getElementsByTagName("colAligns")
                                                 .item(0);
                 Node colFixed = ((Element)node).getElementsByTagName("fixed").item(0);
                 
                 
                 if (widthsNode != null) {
                     String[] widths = widthsNode.getFirstChild()
                     .getNodeValue()
                     .split(",");
                     for (String wid : widths) {
                         TreeColumn col = new TreeColumn();
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

                 
                 NodeList leafNodes = ((Element)node).getElementsByTagName("leaf");
                 
                 HashMap<String,TreeDataItem> items = new HashMap<String,TreeDataItem>();
                 for(int i = 0; i < leafNodes.getLength(); i++) {
                 
                     Node editorsNode = ((Element)leafNodes.item(i)).getElementsByTagName("editors")
                                                       .item(0);
                     Node fieldsNode = ((Element)leafNodes.item(i)).getElementsByTagName("fields")
                     .item(0);

                     String leafType = leafNodes.item(i).getAttributes().getNamedItem("type").getNodeValue();
                     NodeList editors = editorsNode.getChildNodes();
                     int j = 0; 
                     for (int k = 0; k < editors.getLength(); k++) {
                         if (editors.item(k).getNodeType() == Node.ELEMENT_NODE) {
                             columns.get(j).setColumnWidget((Widget)ScreenBase.createCellWidget(editors.item(k),screen),leafType);
                             j++;
                         }
                     }
                     NodeList fieldList = fieldsNode.getChildNodes();
                     TreeDataItem item = new TreeDataItem(fieldList.getLength());
                     item.leafType = leafType;
                     int l = 0;
                     for (int k = 0; k < fieldList.getLength(); k++) {
                         if (fieldList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                             AbstractField field = (ScreenBase.createField(fieldList.item(k)));
                             item.cells[l] = ((FieldType)field);
                             columns.get(k).setKey(field.key);
                             l++;
                         }
                     }
                     items.put(leafType,item);
                 }
                 //data.setDefaultSet(set);
                 tree.init(columns,maxRows,width,title,showHeader,showScroll);
                 if(node.getAttributes().getNamedItem("multiSelect") != null){
                     if(node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true")){
                         tree.model.enableMultiSelect(true);
                     }
                 }
                 if(node.getAttributes().getNamedItem("treeCall") != null) {
                     String treeCall = node.getAttributes().getNamedItem("treeCall").getNodeValue();
                     if(treeCall.equals("this")){
                         tree.model.treeService = (TreeServiceCallInt)screen;
                     }else{
                         tree.model.treeService = (TreeServiceCallInt)ClassFactory.forName(treeCall);
                     }
                 }else if(node.getAttributes().getNamedItem("serviceUrl") != null) {
                     tree.model.treeService = new TreeServiceCall(node.getAttributes().getNamedItem("serviceUrl").getNodeValue());
                 }
                 if(node.getAttributes().getNamedItem("drag") != null) {
                     String drag = node.getAttributes().getNamedItem("drag").getNodeValue();
                     if(drag.equals("default")){
                         //tree.drag = new TreeDragHandler(tree);
                     }
                 }
                 if(node.getAttributes().getNamedItem("drop") != null) {
                     String drop = node.getAttributes().getNamedItem("drop").getNodeValue();
                     if(drop.equals("default")){
                    /*     if(tree.drag != null)
                             tree.drop = (DropListener)tree.drag;
                         else
                             tree.drop = new TreeDragHandler(tree);
                         super.addDropListener(tree.drop);
                     */
                     }
                 }
                 //int rows = 0;
                 /*if (node.getAttributes().getNamedItem("rows") != null) {
                     rows = Integer.parseInt(node.getAttributes()
                                                 .getNamedItem("rows")
                                                 .getNodeValue());
                     for(int i = 0; i < rows; i++){
                         data.addDefualt();
                     }
                 }
                 */
                 if (node.getAttributes().getNamedItem("targets") != null) {
                     dropTargets = new Vector<String>();
                     String targets[] = node.getAttributes()
                                           .getNamedItem("targets")
                                           .getNodeValue().split(",");
                     for(int i = 0; i < targets.length; i++){
                             dropTargets.add(targets[i]);
                     }
                     tree.dragController = new TreeDragController(RootPanel.get());
                 }
                 if (node.getAttributes().getNamedItem("dropManager") != null){
                     tree.dropController = new TreeIndexDropController(tree);
                     String appClass = node.getAttributes()
                     .getNamedItem("dropManager")
                     .getNodeValue();

                     if("this".equals(appClass))
                         tree.dropController.manager =(DropManager)screen;
                     else{
                         tree.dropController.manager = (DropManager)ClassFactory.forName(appClass);
                     }
                 }
                 if (node.getAttributes().getNamedItem("dragManager") != null){
                     getDragController();
                     String appClass = node.getAttributes()
                     .getNamedItem("dragManager")
                     .getNodeValue();

                     if("this".equals(appClass))
                         tree.dragController.manager =(DragManager)screen;
                     else{
                         tree.dragController.manager = (DragManager)ClassFactory.forName(appClass);
                     }
                 }
                // enable(enable);
                 tree.model.setManager(manager);
                 
                 
             ((AppScreen)screen).addKeyboardListener(tree.keyboardHandler);
             ((AppScreen)screen).addClickListener(tree.mouseHandler);
             initWidget(tree);
             tree.model.load(data);
             tree.model.setLeaves(items);
             displayWidget = tree;
             tree.setStyleName("ScreenTable");
             ((TreeKeyboardHandler)tree.keyboardHandler).setScreen(this);
             setDefaults(node, screen);
             tree.screenWidget = this;
        }

        public ScreenWidget getInstance(Node node, ScreenBase screen) {
            // TODO Auto-generated method stub
            return new ScreenTreeWidget(node, screen);
        }

        public void load(AbstractField field) {
            if(!queryMode){
                if (field.getValue() != null)
                    tree.model.load((TreeDataModel)field.getValue());
                else{
                    tree.model.clear();
                    field.setValue(tree.model.getData());
                }
            }else {
                if(queryWidget instanceof ScreenTableWidget){
                    if(field.getValue() != null){
                        if (field.getValue() != null)
                            ((ScreenTreeWidget)queryWidget).tree.model.load((TreeDataModel)field.getValue());
                    }
                }else{
                    if(queryWidget != null)
                        queryWidget.load(field);
                }
            }
        }

        public void submit(AbstractField field) {
            if(queryMode)
                queryWidget.submit(field);
            else{
                field.setValue(tree.model.unload());
                ArrayList<String> fieldIndex = new ArrayList<String>();
                for(TreeColumnInt col : tree.columns)
                    fieldIndex.add(col.getKey());
                ((TreeField)field).setFieldIndex(fieldIndex);
            }
        }

        public Widget getWidget() {
            return tree;
        }
        
        public void destroy(){
            tree = null;
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
                if(dropTargets != null && dropTargets.size() > 0){
                    for(String target : dropTargets) {         
                        getDragController().registerDropController(((HasDropController)screen.widgets.get(target)).getDropController());
                    }
                }
                dropInited = true;
            }
            tree.enabled(enabled);
            super.enable(enabled);
        }
        
        public void setFocus(boolean focus){
            tree.setFocus(focus);
        }
        
        @Override
        public void drawError() {
            tree.model.refresh();
        }
        public TreeDragController getDragController() {
            if(tree.dragController == null)
                tree.dragController = new TreeDragController(RootPanel.get());
            return tree.dragController;
        }
        public void setDragController(DragController controller) {
            tree.dragController = (TreeDragController)controller;
            
        }
        public TreeIndexDropController getDropController() {
            if(tree.dropController == null)
                tree.dropController = new TreeIndexDropController(tree);
            return tree.dropController;
        }
        public void setDropController(DropController controller) {
           tree.dropController = (TreeIndexDropController)controller;
            
        }
        
}
