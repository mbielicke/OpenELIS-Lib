package org.openelis.gwt.client.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.client.widget.AutoCompleteTextBox;
import org.openelis.gwt.client.widget.table.small.TableCellWidget;
import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.OptionField;
import org.openelis.gwt.common.OptionItem;
import org.openelis.gwt.common.data.NumberField;
import org.openelis.gwt.common.data.StringField;

/**
 * ScreenAuto wraps an AutoComplete widget to be displayed on a Screen
 * @author tschmidt
 *
 */
public class ScreenAuto extends ScreenInputWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "auto";
    public String fieldCase = "mixed";
    public boolean loadFromModel = false;
	/**
	 * Widget wrapped by this class.
	 */
    private AutoCompleteTextBox auto;

	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenAuto() {
    }

    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &;t;auto key="string" serviceUrl="string" cat="string" shortcut="char"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenAuto(Node node, final ScreenBase screen) {
        super(node);
        String cat = node.getAttributes().getNamedItem("cat").getNodeValue();
        String url = node.getAttributes()
                         .getNamedItem("serviceUrl")
                         .getNodeValue();
        
        boolean dropDown = false;
        boolean fromModel = false;
        String textBoxDefault = null;
        String width = null;
        
        
        if (node.getAttributes().getNamedItem("dropdown") != null)
        	dropDown = true;
        
        if (node.getAttributes().getNamedItem("fromModel") != null)
        	fromModel = true;
        
        if (node.getAttributes().getNamedItem("text") != null)
        	textBoxDefault = node.getAttributes()
                                  .getNamedItem("text")
                                  .getNodeValue();
        
        if (node.getAttributes().getNamedItem("width") != null)
        	width = node.getAttributes()
                                  .getNamedItem("width")
                                  .getNodeValue();
        
        Node widthsNode = ((Element)node).getElementsByTagName("widths").item(0);
        Node headersNode = ((Element)node).getElementsByTagName("headers").item(0);
        Node editorsNode = ((Element)node).getElementsByTagName("editors").item(0);
        Node fieldsNode = ((Element)node).getElementsByTagName("fields").item(0);
        //Node filtersNode = ((Element)node).getElementsByTagName("filters").item(0);
        //Node sortsNode = ((Element)node).getElementsByTagName("sorts").item(0);
        //Node alignNode = ((Element)node).getElementsByTagName("colAligns").item(0);
        //Node statFilter = ((Element)node).getElementsByTagName("statFilters").item(0);

        auto = new AutoCompleteTextBox(cat, url, dropDown, textBoxDefault, width) {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        
        if(widthsNode != null) {
        	auto.setWidths(getWidths(widthsNode));
        }
        if(headersNode != null) {
        	auto.setHeaders(getHeaders(headersNode));
        }
        if(editorsNode != null) {
        	auto.setEditors(getEditors(editorsNode));
        }
        if(fieldsNode != null) {
        	auto.setFields(getFields(fieldsNode));
        }
        if (node.getAttributes().getNamedItem("popupHeight") != null){
            auto.setPopupHeight(node.getAttributes().getNamedItem("popupHeight").getNodeValue());
        }
        
        if (node.getAttributes().getNamedItem("type") != null){
            auto.setType(node.getAttributes().getNamedItem("type").getNodeValue());
        }
            
    /*    if (node.getAttributes().getNamedItem("shortcut") != null)
            auto.setAccessKey(node.getAttributes()
                                  .getNamedItem("shortcut")
                                  .getNodeValue()
                                  .charAt(0));*/
        /*OptionField option = (OptionField)screen.rpc.getField("state");
        if(fromModel){
        	
        }*/
        //auto.setStyleName("ScreenAuto");
        if (node.getAttributes().getNamedItem("case") != null){
            fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
            if(fieldCase.equals("upper"))
                auto.textBox.addStyleName("Upper");
            else if(fieldCase.equals("lower"))
                auto.textBox.addStyleName("Lower");
            auto.setCase(fieldCase);
        }
        initWidget(auto);
        displayWidget = auto;
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenAuto(node, screen);
    }

    public void load(AbstractField field) {
    	if(queryMode){
    		queryWidget.load(field);
    	}else{
	    	if(field instanceof StringField){
	    		auto.setValue((String)((StringField)field).getValue());
	    	}else if(field instanceof NumberField){
	    		auto.setValue((Integer)((NumberField)field).getValue());
	    	}else if(queryMode)
	            queryWidget.load(field);
	    	else{
	            auto.clear();
	            OptionField optField = (OptionField)field;
	            List optMap = optField.getOptions();
	            Iterator optIt = optMap.iterator();
	            while (optIt.hasNext()) {
	                OptionItem item = (OptionItem)optIt.next();
	                auto.addItem(item.akey.toString(), item.display);
	                if (item.selected || item.akey.equals(optField.getValue())) {
	                    auto.setItemSelected(auto.getItemCount() - 1, true);
	                }
	            }
	        }
    	}
    }

    public void submit(AbstractField field) {
        if(queryMode){
            queryWidget.submit(field);
        }else{
            if (((String)field.getKey()).endsWith("Id"))
                field.setValue(auto.value);
            if ((((String)field.getKey()).endsWith("Text"))) {
                String text = auto.textBox.getText();
                if(fieldCase.equals("upper"))
                    text = text.toUpperCase();
                else if (fieldCase.equals("lower"))
                    text = text.toLowerCase();
                field.setValue(text);
            }
        }
    }

    public void setFocus(boolean focused) {
        // TODO Auto-generated method stub
        if(queryMode)
            queryWidget.setFocus(focused);
        else
            auto.textBox.setFocus(focused);
    }

    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else
            auto.textBox.setReadOnly(!enabled);     
    }
    
    public void destroy() {
        auto = null;
        super.destroy();
    }
    
    public int[] getWidths(Node node){
    	 String[] widths = node.getFirstChild()
         .getNodeValue()
         .split(",");
    	int[] width = new int[widths.length];
    	for (int i = 0; i < widths.length; i++) {
    		width[i] = Integer.parseInt(widths[i]);
    	}
    	return width;
    }
    
    public AbstractField[] getFields(Node node) {
        NodeList fieldList = node.getChildNodes();
        ArrayList list = new ArrayList();
        for (int i = 0; i < fieldList.getLength(); i++) {
            if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                list.add(ScreenBase.getWidgetMap().getField(fieldList.item(i)));
            }
        }
        AbstractField[] fields = new AbstractField[list.size()];
        for (int i = 0; i < list.size(); i++) {
            fields[i] = (AbstractField)list.get(i);
        }
        return fields;
    }
    
    public TableCellWidget[] getEditors(Node node) {
        NodeList editors = node.getChildNodes();
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
        return cells;
    }
    
    public String[] getHeaders(Node node){
    	return node.getFirstChild()
                .getNodeValue()
                .split(",");
    }
}
