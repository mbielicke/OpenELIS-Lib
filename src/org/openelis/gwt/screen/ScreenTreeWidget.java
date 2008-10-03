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

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.TableField;
import org.openelis.gwt.common.data.TreeDataModel;
import org.openelis.gwt.common.data.TreeField;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableKeyboardHandler;
import org.openelis.gwt.widget.table.TableManager;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.gwt.widget.tree.TreeViewInt.VerticalScroll;
import org.openelis.gwt.widget.tree.TreeColumn;
import org.openelis.gwt.widget.tree.TreeColumnInt;
import org.openelis.gwt.widget.tree.TreeKeyboardHandler;
import org.openelis.gwt.widget.tree.TreeWidget;

import java.util.ArrayList;

/**
 * ScreenTable wraps the FormTable widget to be displayed
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenTreeWidget extends ScreenInputWidget {
    

        /**
         * Default XML Tag Name for XML definition and WidgetMap
         */
        public static String TAG_NAME = "tree-table";
        /**
         * Widget wrapped by this class
         */
        private TreeWidget tree ;
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
             ArrayList<TreeColumnInt> columns = new ArrayList<TreeColumnInt>();
             TreeDataModel data = new TreeDataModel();
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
                 NodeList editors = editorsNode.getChildNodes();
                 int j = 0; 
                 for (int i = 0; i < editors.getLength(); i++) {
                     if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                         columns.get(j).setColumnWidget((Widget)ScreenBase.createCellWidget(editors.item(i),screen));
                         j++;
                     }
                 }
                 NodeList fieldList = fieldsNode.getChildNodes();
                 DataSet set = new DataSet();
                 for (int i = 0; i < fieldList.getLength(); i++) {
                     if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                         AbstractField field = (ScreenBase.createField(fieldList.item(i)));
                         set.add(field);
                         columns.get(i).setKey(field.key);
                     }
                 }
                 //data.setDefaultSet(set);
                 tree = new TreeWidget(columns,maxRows,width,title,showHeader,showScroll);
                 tree.enabled(enable);
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
                
                 
                 
             ((AppScreen)screen).addKeyboardListener(tree.keyboardHandler);
             ((AppScreen)screen).addClickListener(tree.mouseHandler);
             initWidget(tree);
             tree.model.load(data);
             displayWidget = tree;
             tree.setStyleName("ScreenTable");
             ((TreeKeyboardHandler)tree.keyboardHandler).setScreen(this);
             setDefaults(node, screen);
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
                    field.setValue(tree.model);
                }
            }else {
                if(queryWidget instanceof ScreenTableWidget){
                    if(field.getValue() != null){
                        if (field.getValue() != null)
                            ((ScreenTreeWidget)queryWidget).tree.model.load((TreeDataModel)field.getValue());
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
}
