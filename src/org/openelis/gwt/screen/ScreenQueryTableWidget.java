package org.openelis.gwt.screen;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.table.QueryTable;
import org.openelis.gwt.widget.table.TableCellWidget;

import java.util.ArrayList;

/**
 * ScreenTable wraps the FormTable widget to be displayed
 * on a Screen.
 * @author tschmidt
 *
 */
public class ScreenQueryTableWidget extends ScreenInputWidget {
    

        /**
         * Default XML Tag Name for XML definition and WidgetMap
         */
        public static String TAG_NAME = "qtable";
        /**
         * Widget wrapped by this class
         */
        private QueryTable table;
        /**
         * Default no-arg constructor used to create reference in the WidgetMap class
         */
        
        private String[] fields;
        public ScreenQueryTableWidget() {
        }
        /**
         * Constructor called from getInstance to return a specific instance of this class
         * to be displayed on the screen.  It uses the XML Node to create it's widget.
         * 
         * 
         * @param node
         * @param screen
         */ 
        public ScreenQueryTableWidget(Node node, ScreenBase screen) {
            super(node);
            table = new QueryTable();
           // try {
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
                Node alignNode = ((Element)node).getElementsByTagName("colAligns")
                                                .item(0);
                if(node.getAttributes().getNamedItem("cellHeight") != null){
                    table.setCellHeight(Integer.parseInt(node.getAttributes().getNamedItem("cellHeight").getNodeValue()));
                }
                table.view.setWidth(node.getAttributes().getNamedItem("width").getNodeValue());
                table.setMaxRows(Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue()));
                if(node.getAttributes().getNamedItem("title") != null){
                        table.setTitle(node.getAttributes()
                                            .getNamedItem("title")
                                            .getNodeValue());
                }else
                    table.setTitle("");
                if(node.getAttributes().getNamedItem("showRows") != null){
                    if(node.getAttributes().getNamedItem("showRows").getNodeValue().equals("true"))
                        table.setShowRows(true);
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
                        String[] headerNames = headersNode.getFirstChild()
                                                          .getNodeValue()
                                                          .split(",");
                        for(int i=0; i<headerNames.length;i++){
                            headerNames[i] = headerNames[i].trim();
                        }
                        table.view.setHeaders(headerNames);
                    
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
                }
                NodeList editors = editorsNode.getChildNodes();
                ArrayList list = new ArrayList();
                for (int i = 0; i < editors.getLength(); i++) {
                    if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        list.add(ScreenBase.getWidgetMap().getCellWidget(editors.item(i)));
                    }
                }
                TableCellWidget[] cells = new TableCellWidget[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    cells[i] = (TableCellWidget)list.get(i);
                }
                table.setEditors(cells);
                fields = fieldsNode.getFirstChild().getNodeValue().split(",");
                AbstractField[] afields = new AbstractField[fields.length];
                for(int i = 0; i < fields.length; i++){
                    afields[i] = ((FormRPC)((AppScreenForm)screen).forms.get("query")).getField(fields[i].trim());
                }
                table.setFields(afields);
                table.view.initTable(table);
                table.reset();
                table.enabled(true);
                    
            //} catch (Exception e) {
            //    Window.alert("create Table from node" +e.getMessage());
           // }
            initWidget(table);
            displayWidget = table;
            table.setStyleName("ScreenTable");
            setDefaults(node, screen);
        }

        public ScreenWidget getInstance(Node node, ScreenBase screen) {
            // TODO Auto-generated method stub
            return new ScreenQueryTableWidget(node, screen);
        }

        public void load(AbstractField field) {

        }

        public void submit(AbstractField field) {

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
            
        }

}
