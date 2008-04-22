package org.openelis.gwt.screen;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.TableModel;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.table.TableManager;
import org.openelis.gwt.widget.table.TableWidget;

import java.util.ArrayList;

/**
 * ScreenTable wraps the FormTable widget to be displayed
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenTableWidget extends ScreenInputWidget {
    

        /**
         * Default XML Tag Name for XML definition and WidgetMap
         */
        public static String TAG_NAME = "table";
        /**
         * Widget wrapped by this class
         */
        private TableWidget table;
        /**
         * Default no-arg constructor used to create reference in the WidgetMap class
         */
        public ScreenTableWidget() {
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
            table = new TableWidget();
           // try {
                if (node.getAttributes().getNamedItem("manager") != null) {
                    String appClass = node.getAttributes()
                                          .getNamedItem("manager")
                                          .getNodeValue();
                    TableManager manager = (TableManager)ClassFactory.forName(appClass);
                    
                    table.setManager(manager);
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
                Node statFilter = ((Element)node).getElementsByTagName("statFilters")
                                                 .item(0);
                if(node.getAttributes().getNamedItem("cellHeight") != null){
                    table.setCellHeight(Integer.parseInt(node.getAttributes().getNamedItem("cellHeight").getNodeValue()));
                }
                table.setWidth(node.getAttributes().getNamedItem("width").getNodeValue());
                table.setMaxRows(Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue()));
                //if constants = true we want to get the title from the properties file
                if(node.getAttributes().getNamedItem("title") != null){
                    if (node.getAttributes().getNamedItem("constant") != null && node.getAttributes().getNamedItem("constant").getNodeValue().equals("true")) {
                        table.setTableTitle(table.constants.getString(node.getAttributes().getNamedItem("title").getNodeValue()));
                    }else{
                        table.setTableTitle(node.getAttributes()
                                            .getNamedItem("title")
                                            .getNodeValue());
                    }
                }else
                    table.setTableTitle("");
                if(node.getAttributes().getNamedItem("autoAdd") != null){
                    if(node.getAttributes().getNamedItem("autoAdd").getNodeValue().equals("true"))
                        table.setAutoAdd(true);
                }
                if(node.getAttributes().getNamedItem("showRows") != null){
                    if(node.getAttributes().getNamedItem("showRows").getNodeValue().equals("true"))
                        table.setShowRows(true);
                }
                if(node.getAttributes().getNamedItem("enable") != null){
                    if(node.getAttributes().getNamedItem("enable").getNodeValue().equals("true"))
                        table.setEnable(true);
                }
                if (widthsNode != null) {
                    String[] widths = widthsNode.getFirstChild()
                                                .getNodeValue()
                                                .split(",");
                    int[] width = new int[widths.length];
                    for (int i = 0; i < widths.length; i++) {
                        width[i] = Integer.parseInt(widths[i]);
                    }
                    table.setColWidths(width);
                }
                if(headersNode != null){
                if (headersNode.getAttributes().getNamedItem("constants") != null) {
                    String[] headerNames = headersNode.getFirstChild()
                                                      .getNodeValue()
                                                      .split(",");
                    String[] headerVals = new String[headerNames.length];
                    for (int i = 0; i < headerVals.length; i++) {
                        headerVals[i] = table.constants.getString(headerNames[i]).trim();
                    }
                    table.setHeaders(headerVals);
                } else {
                    String[] headerNames = headersNode.getFirstChild()
                    .getNodeValue()
                    .split(",");
                    for(int i=0; i<headerNames.length;i++){
                        headerNames[i] = headerNames[i].trim();
                    }
                    table.setHeaders(headerNames);
                    
                }
                }
                if (filtersNode != null) {
                    String[] filters = filtersNode.getFirstChild()
                                                  .getNodeValue()
                                                  .split(",");
                    boolean[] filter = new boolean[filters.length];
                    for (int i = 0; i < filters.length; i++) {
                        filter[i] = Boolean.valueOf(filters[i]).booleanValue();
                    }
                    table.setFilterable(filter);
                }
                if (sortsNode != null) {
                    String[] sorts = sortsNode.getFirstChild()
                                              .getNodeValue()
                                              .split(",");
                    boolean[] sort = new boolean[sorts.length];
                    for (int i = 0; i < sorts.length; i++) {
                        sort[i] = Boolean.valueOf(sorts[i]).booleanValue();
                    }
                    table.setSortable(sort);
                }
                if (alignNode != null){
                    String[] aligns = alignNode.getFirstChild().getNodeValue().split(",");
                    HorizontalAlignmentConstant[] alignments = new HorizontalAlignmentConstant[aligns.length];
                    for (int i = 0; i < aligns.length; i++) {
                        if (aligns.equals("left"))
                            alignments[i] = HasAlignment.ALIGN_LEFT;
                        if (aligns.equals("center"))
                            alignments[i] = HasAlignment.ALIGN_CENTER;
                        if (aligns.equals("right"))
                            alignments[i] = HasAlignment.ALIGN_RIGHT;
                    }
                    table.setColAlign(alignments);
                }if (statFilter != null){
                    NodeList columns = ((Element)statFilter).getElementsByTagName("column");
                    ArrayList list = new ArrayList();
                    for (int i = 0; i < columns.getLength(); i++) {
                        NodeList filters = ((Element)columns.item(i)).getElementsByTagName("filter");
                        if (filters == null || filters.getLength() == 0)
                            list.add(null);
                        else {
                            Filter[] filt = new Filter[filters.getLength() + 1];
                            Filter filter = new Filter();
                            filter.filtered = true;
                            filter.value = "All";
                            filt[0] = filter;
                            for (int j = 0; j < filters.getLength(); j++) {
                                filter = new Filter();
                                filter.filtered = false;
                                filter.display = filters.item(j)
                                                        .getAttributes()
                                                        .getNamedItem("display")
                                                        .getNodeValue();
                                if (filters.item(j).getAttributes().getNamedItem("value") != null)
                                    filter.value = filters.item(j)
                                                          .getAttributes()
                                                          .getNamedItem("value")
                                                          .getNodeValue();
                                if (filters.item(j).getAttributes().getNamedItem("splitOn") != null)
                                    filter.splitOn = filters.item(j)
                                                            .getAttributes()
                                                            .getNamedItem("splitOn")
                                                            .getNodeValue();
                                filt[j + 1] = filter;
                            }
                            list.add(filt);
                        }
                    }
                    table.setStatFilter(list);
                }
                NodeList editors = editorsNode.getChildNodes();
                ArrayList list = new ArrayList();
                for (int i = 0; i < editors.getLength(); i++) {
                    if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        
                        list.add(ScreenBase.createCellWidget(editors.item(i)));
                    }
                }
                TableCellWidget[] cells = new TableCellWidget[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    cells[i] = (TableCellWidget)list.get(i);
                }
                table.setEditors(cells);
                NodeList fieldList = fieldsNode.getChildNodes();
                list = new ArrayList();
                for (int i = 0; i < fieldList.getLength(); i++) {
                    if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        list.add(ScreenBase.createField(fieldList.item(i)));
                    }
                }
                AbstractField[] fields = new AbstractField[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    fields[i] = (AbstractField)list.get(i);
                }
                table.setFields(fields);
                int rows = 0;
                if (node.getAttributes().getNamedItem("rows") != null) {
                    rows = Integer.parseInt(node.getAttributes()
                                                .getNamedItem("rows")
                                                .getNodeValue());
                    table.init(rows);
                } else if (node.getAttributes().getNamedItem("serviceUrl") != null) {
                    table.initService(node.getAttributes()
                                               .getNamedItem("serviceUrl")
                                               .getNodeValue());
                    table.init(-1);
                } else {
                    table.init(0);
                }
                if(node.getAttributes().getNamedItem("showScroll") != null){
                    if(node.getAttributes().getNamedItem("showScroll").getNodeValue().equals("true"))
                        table.controller.setShowScroll(true);
                }
            //} catch (Exception e) {
            //    Window.alert("create Table from node" +e.getMessage());
           // }
                
            ((AppScreen)screen).addKeyboardListener(table.controller);
            ((AppScreen)screen).addClickListener(table.controller);
            initWidget(table);
            displayWidget = table;
            table.setStyleName("ScreenTable");
            setDefaults(node, screen);
        }

        public ScreenWidget getInstance(Node node, ScreenBase screen) {
            // TODO Auto-generated method stub
            return new ScreenTableWidget(node, screen);
        }

        public void load(AbstractField field) {
            if(!queryMode){
                if (field.getValue() != null)
                    table.controller.setModel((TableModel)field.getValue());
                else{
                    table.controller.model.reset();
                    table.controller.reset();
                    field.setValue(table.controller.model);
                }
            }else {
                if(queryWidget instanceof ScreenTableWidget){
                    if(field.getValue() != null){
                        if (field.getValue() != null)
                            ((ScreenTableWidget)queryWidget).table.controller.setModel((TableModel)field.getValue());
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
                field.setValue(table.controller.model);
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
            table.enable(enabled);
        }

}
