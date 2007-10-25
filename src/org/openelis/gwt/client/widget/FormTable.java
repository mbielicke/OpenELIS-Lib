package org.openelis.gwt.client.widget;

import java.util.ArrayList;

import org.openelis.gwt.client.screen.Screen;
import org.openelis.gwt.client.widget.table.TableCellWidget;
import org.openelis.gwt.client.widget.table.TableController;
import org.openelis.gwt.client.widget.table.TableManager;
import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.Filter;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * This class is used to wrap a table in a Screen.
 * 
 * @author tschmidt
 * 
 */
public class FormTable extends Composite {

    public TableController controller = new TableController();
    public ConstantsWithLookup constants = null;

    /**
     * Constructor that takes an i18n interface for setting static fields along
     * with the XML node containing the definition of the table.
     * 
     * @param node
     * @param constants
     */
    public FormTable(Node node, ConstantsWithLookup constants) {
        this.constants = constants;
        create(node);
    }

    /**
     * Constructor that takes a XML node containg the definition of the table.
     * 
     * @param node
     */
    public FormTable(Node node) {
        create(node);
    }

    /**
     * Method to create the table from the XML definition.
     * 
     * @param node
     */
    private void create(Node node) {
        try {
            initWidget(controller.view);
            if (node.getAttributes().getNamedItem("manager") != null) {
                String appClass = node.getAttributes()
                                      .getNamedItem("manager")
                                      .getNodeValue();
                TableManager manager = (TableManager)Screen.getWidgetMap()
                                                           .getTableManager(appClass);
               setManager(manager);
            }
            Node widthsNode = ((Element)node).getElementsByTagName("widths")
                                             .item(0);
            Node headersNode = ((Element)node).getElementsByTagName("headers")
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
            Node statNode = ((Element)node).getElementsByTagName("staticTitles")
                                           .item(0);
            Node dynNode = ((Element)node).getElementsByTagName("dynamicTitles")
                                          .item(0);
            Node statFilter = ((Element)node).getElementsByTagName("statFilters")
                                             .item(0);
            controller.view.setTitle(node.getAttributes()
                              .getNamedItem("title")
                              .getNodeValue());
            if(node.getAttributes().getNamedItem("autoAdd") != null){
                if(node.getAttributes().getNamedItem("autoAdd").getNodeValue().equals("true"))
                    controller.setAutoAdd(true);
            }
            if(node.getAttributes().getNamedItem("showRows") != null){
                if(node.getAttributes().getNamedItem("showRows").getNodeValue().equals("true"))
                    controller.setShowRows(true);
            }
            if (widthsNode != null) {
                String[] widths = widthsNode.getFirstChild()
                                            .getNodeValue()
                                            .split(",");
                int[] width = new int[widths.length];
                for (int i = 0; i < widths.length; i++) {
                    width[i] = Integer.parseInt(widths[i]);
                }
                controller.setColWidths(width);
            }
            if (headersNode.getAttributes().getNamedItem("constants") != null) {
                String[] headerNames = headersNode.getFirstChild()
                                                  .getNodeValue()
                                                  .split(",");
                String[] headerVals = new String[headerNames.length];
                for (int i = 0; i < headerVals.length; i++) {
                    headerVals[i] = constants.getString(headerNames[i]);
                }
                controller.view.setHeaders(headerVals);
            } else {
                controller.view.setHeaders(headersNode.getFirstChild()
                                           .getNodeValue()
                                           .split(","));
            }
            if (filtersNode != null) {
                String[] filters = filtersNode.getFirstChild()
                                              .getNodeValue()
                                              .split(",");
                boolean[] filter = new boolean[filters.length];
                for (int i = 0; i < filters.length; i++) {
                    filter[i] = Boolean.valueOf(filters[i]).booleanValue();
                }
                controller.setFilterable(filter);
            }
            if (sortsNode != null) {
                String[] sorts = sortsNode.getFirstChild()
                                          .getNodeValue()
                                          .split(",");
                boolean[] sort = new boolean[sorts.length];
                for (int i = 0; i < sorts.length; i++) {
                    sort[i] = Boolean.valueOf(sorts[i]).booleanValue();
                }
                controller.setSortable(sort);
            }
            if (alignNode != null)
                setColAlign(alignNode);
            if (statNode != null)
                controller.setStaticTitles(statNode.getFirstChild()
                                                   .getNodeValue()
                                                   .split(","));
            if (dynNode != null)
                controller.setDynamicTitles(dynNode.getFirstChild()
                                                   .getNodeValue()
                                                   .split(","));
            if (statFilter != null)
                setStatFilter(statFilter);
            setEditors(editorsNode);
            setFields(fieldsNode);
            int rows = 0;
            if (node.getAttributes().getNamedItem("rows") != null) {
                rows = Integer.parseInt(node.getAttributes()
                                            .getNamedItem("rows")
                                            .getNodeValue());
                init(rows);
            } else if (node.getAttributes().getNamedItem("serviceUrl") != null) {
                controller.initService(node.getAttributes()
                                           .getNamedItem("serviceUrl")
                                           .getNodeValue());
                init(-1);
            } else {
                init(0);
            }
        } catch (Exception e) {
            Window.alert(e.getMessage());
        }
    }

    /**
     * Sets the height of the table.
     */
    public void setHeight(String height) {
        controller.view.setHeight(height);
    }

    /**
     * Sets the width of the table.
     */
    public void setWidth(String width) {
        controller.view.setWidth(width);
    }

    /**
     * Sets the TableManager for this table
     * 
     * @param manager
     */
    public void setManager(TableManager manager) {
        controller.setManager(manager);
    }

    /**
     * This method creates the editors that the table will use.
     * 
     * @param node
     */
    public void setEditors(Node node) {
        NodeList editors = node.getChildNodes();
        ArrayList list = new ArrayList();
        for (int i = 0; i < editors.getLength(); i++) {
            if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                list.add(Screen.getWidgetMap().getCellWidget(editors.item(i)));
            }
        }
        TableCellWidget[] cells = new TableCellWidget[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cells[i] = (TableCellWidget)list.get(i);
        }
        controller.setEditors(cells);
    }

    /**
     * This method will set the Model fields to be used by the table.
     * 
     * @param node
     */
    public void setFields(Node node) {
        NodeList fieldList = node.getChildNodes();
        ArrayList list = new ArrayList();
        for (int i = 0; i < fieldList.getLength(); i++) {
            if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                list.add(Screen.getWidgetMap().getField(fieldList.item(i)));
            }
        }
        AbstractField[] fields = new AbstractField[list.size()];
        for (int i = 0; i < list.size(); i++) {
            fields[i] = (AbstractField)list.get(i);
        }
        controller.model.setFields(fields);
    }

    /**
     * This table will set the Column Aligns of the table.
     * 
     * @param node
     */
    public void setColAlign(Node node) {
        String[] aligns = node.getFirstChild().getNodeValue().split(",");
        HorizontalAlignmentConstant[] alignments = new HorizontalAlignmentConstant[aligns.length];
        for (int i = 0; i < aligns.length; i++) {
            if (aligns.equals("left"))
                alignments[i] = HasAlignment.ALIGN_LEFT;
            if (aligns.equals("center"))
                alignments[i] = HasAlignment.ALIGN_CENTER;
            if (aligns.equals("right"))
                alignments[i] = HasAlignment.ALIGN_RIGHT;
        }
        controller.setColAlign(alignments);
    }

    /**
     * This Table will set any static filters defined for the table.
     * 
     * @param node
     */
    public void setStatFilter(Node node) {
        NodeList columns = ((Element)node).getElementsByTagName("column");
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
        controller.setStatFilterable(list);
    }

    /**
     * This method sets the model in the screen. rows = -1 means a serviceUrl
     * has been provided and the getModel method will be called to populate the
     * table.
     * 
     * @param rows
     */
    public void init(int rows) {
        controller.view.initTable(controller);
        controller.setView(controller.view);
        if (rows < 0)
            controller.getModel();
        else {
            for (int m = 0; m < rows; m++) {
                controller.model.addRow(null);
            }
            controller.reset();
        }
    }
}
