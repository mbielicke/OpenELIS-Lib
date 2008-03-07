package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.BooleanObject;
import org.openelis.gwt.common.data.CollectionField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.widget.AutoCompleteDropdown;
import org.openelis.gwt.widget.table.TableCellWidget;

import java.util.ArrayList;

public class ScreenAutoDropdown extends ScreenInputWidget implements FocusListener {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "autoDropdown";
    public String fieldCase = "mixed";
    public boolean loadFromModel = false;
	/**
	 * Widget wrapped by this class.
	 */
    private AutoCompleteDropdown auto;
    
    private String type = "";

	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenAutoDropdown() {
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
    public ScreenAutoDropdown(Node node, final ScreenBase screen) {
        super(node);
        String cat = node.getAttributes().getNamedItem("cat").getNodeValue();
        String url = node.getAttributes()
                         .getNamedItem("serviceUrl")
                         .getNodeValue();
        
        boolean fromModel = false;
        boolean multiSelect = false;
        String textBoxDefault = null;
        String width = null;
        
        if (node.getAttributes().getNamedItem("fromModel") != null)
        	fromModel = true;
        
        if (node.getAttributes().getNamedItem("multiSelect") != null)
        	multiSelect = true;
        
        if (node.getAttributes().getNamedItem("text") != null)
        	textBoxDefault = node.getAttributes()
                                  .getNamedItem("text")
                                  .getNodeValue();
        
        if (node.getAttributes().getNamedItem("width") != null)
        	width = node.getAttributes()
                                  .getNamedItem("width")
                                  .getNodeValue();
        
        Node widthsNode = ((Element)node).getElementsByTagName("autoWidths").item(0);
        Node headersNode = ((Element)node).getElementsByTagName("autoHeaders").item(0);
        Node editorsNode = ((Element)node).getElementsByTagName("autoEditors").item(0);
        Node fieldsNode = ((Element)node).getElementsByTagName("autoFields").item(0);
        Node optionsNode = ((Element)node).getElementsByTagName("autoItems").item(0);

        auto = new AutoCompleteDropdown(cat, url, fromModel, multiSelect, textBoxDefault, width);
        /*{
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                        return;
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };*/
        auto.setForm(screen);
        
        if(widthsNode != null) 
        	auto.setWidths(getWidths(widthsNode));
        
        if(headersNode != null) 
        	auto.setHeaders(getHeaders(headersNode));
        
        if(editorsNode != null) 
        	auto.setEditors(getEditors(editorsNode));
        
        if(fieldsNode != null) 
        	auto.setFields(getFields(fieldsNode));
        
        if (node.getAttributes().getNamedItem("type") != null){
        	type = node.getAttributes().getNamedItem("type").getNodeValue();
            auto.setType(type);
        } 
        
        if(!fromModel && optionsNode != null)
        	auto.setModel(getDropDownOptions(optionsNode));      

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
        return new ScreenAutoDropdown(node, screen);
    }

    public void load(AbstractField field) {
    	if(queryMode){
    		queryWidget.load(field);
    	}else{
            auto.setSelected((ArrayList)field.getValue());
        }
    }

    public void submit(AbstractField field) {
        if(queryMode){
            queryWidget.submit(field);
        }else if(field instanceof CollectionField){
        	field.setValue(auto.getSelected());
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
        else{
            auto.textBox.setReadOnly(!enabled);
            if(enabled)
                auto.addFocusListener(this);
            else
                auto.removeFocusListener(this);
        }
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
    
    private DataModel getDropDownOptions(Node itemsNode){
		DataModel dataModel = new DataModel();		
		
    	NodeList items = ((Element)itemsNode).getElementsByTagName("item");
    	for (int i = 0; i < items.getLength(); i++) {
    	DataSet set = new DataSet();
        Node item = items.item(i);

		//display text
        StringObject display = new StringObject();
		display.setValue((item.getFirstChild() == null ? "" : item.getFirstChild().getNodeValue()));
		set.addObject(display);

        //id
        if(type.equals("integer")){
        	NumberObject id = new NumberObject();
        	id.setType("integer");
        	id.setValue(new Integer(item.getAttributes().getNamedItem("value").getNodeValue()));
        	set.addObject(id);
        }else if(type.equals("string")){
        	StringObject id = new StringObject();
        	id.setValue(item.getAttributes().getNamedItem("value").getNodeValue());
        	set.addObject(id);
        }
        
        //selected flag
		BooleanObject selected = new BooleanObject();
		selected.setValue(new Boolean(false));
		set.addObject(selected);
		
		dataModel.add(set);
    	}
        return dataModel;
	}
    
    public ScreenAutoDropdown getQueryWidget(){
    	return (ScreenAutoDropdown)queryWidget;
    }
    
    public void onLostFocus(Widget sender) {
    	auto.onLostFocus(sender);
    	if(key != null)
    		super.onLostFocus(sender);
    }
    
   public void onFocus(Widget sender) {
	   auto.onFocus(sender);
	   super.onFocus(sender);
   }
}
