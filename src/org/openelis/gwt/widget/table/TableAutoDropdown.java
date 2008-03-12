package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.CollectionField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.OptionField;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.AutoCompleteDropdown;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class TableAutoDropdown extends TableCellInputWidget implements EventPreview {

	public AutoCompleteDropdown editor;
	private Label display;
	
	//this should be the id of the selected element
	private StringField textValue = new StringField();
    private ChangeListener listener;
	
    public boolean loadFromModel;
    public String loadFromHidden = null;
    private boolean multi;
    private int visible = 1;
    private String fieldCase = "mixed";
    private boolean fromModel = false;
    private String type = "";
    
    public OptionField fromHidden;
    
    public TableAutoDropdown(){
    	sinkEvents(Event.KEYEVENTS);
    }
    
    public TableCellWidget getNewInstance() {
    	TableAutoDropdown ta = new TableAutoDropdown();
        ta.multi = multi;
        ta.fromHidden = fromHidden;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listener = listener;
        ta.type = type;
        return ta;
    }
    
    public TableAutoDropdown getNewTableAuto() {
    	TableAutoDropdown ta = new TableAutoDropdown();
        ta.multi = multi;
        ta.fromHidden = fromHidden;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listener = listener;
        ta.type = type;
        return ta;
    }
    
	public Widget getInstance(Node node) {
		AutoCompleteDropdown auto = new AutoCompleteDropdown();
		String cat = node.getAttributes().getNamedItem("cat").getNodeValue();
        String url = node.getAttributes()
                         .getNamedItem("serviceUrl")
                         .getNodeValue();
        
        boolean multiSelect = false;
        String textBoxDefault = null;
        String width = null;

        if (node.getAttributes().getNamedItem("fromModel") != null && node.getAttributes().getNamedItem("fromModel").getNodeValue().equals("true"))
        	fromModel = true;
        
        if (node.getAttributes().getNamedItem("multiSelect") != null && node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
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
        auto.mainHP.removeStyleName("AutoDropdown");
        auto.mainHP.addStyleName("TableAutoDropdown");
        auto.focusPanel.removeStyleName("AutoDropdownButton");
        auto.focusPanel.addStyleName("TableAutoDropdownButton");
        
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
        	type = node.getAttributes().getNamedItem("type").getNodeValue();
            auto.setType(type);
        }
            
        if (node.getAttributes().getNamedItem("case") != null){
            fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
            if(fieldCase.equals("upper"))
                auto.textBox.addStyleName("Upper");
            else if(fieldCase.equals("lower"))
                auto.textBox.addStyleName("Lower");
            auto.setCase(fieldCase);
        }
        editor = auto;
        textValue.setValue("");
        if(!fromModel && optionsNode != null)
        	editor.setModel(getDropDownOptions(optionsNode));
        	
        return getNewTableAuto();
	}

	public void saveValue() {
		if(field instanceof CollectionField){
			((CollectionField)field).setValue(editor.getSelected());
			textValue.setValue(editor.textBox.getText());
		}else{
			if(type != null && type.equals("string")){
			//	((StringField)field).setValue(editor.value);
				textValue.setValue(editor.textBox.getText());
			}else if(type != null && type.equals("integer")){
				//((NumberField)field).setValue(editor.value);
				textValue.setValue(editor.textBox.getText());
			}		
		}
		editor.closePopup();
        super.saveValue();
	}

	//the editor is shared so we need to set the textvalue
	public void setDisplay() {
		if(display == null){
			display = new Label();
			display.setWordWrap(false);
//            display.setWidth(width+"px");
		}
		
		display.setText((String)textValue.getValue());
		setWidget(display);		
        super.setDisplay();
	}

	public void setEditor() {
		if(editor == null){
			editor = new AutoCompleteDropdown();
         //   editor.setWidth(width+"px");
        }
		editor.setSelected((ArrayList)field.getValue());

		setWidget(editor);			
	}

	public void setField(AbstractField field) {
		this.field = field;
		
		//if(!(field instanceof CollectionField))
		if(((ArrayList)field.getValue()).size() > 0){
			if(((ArrayList)field.getValue()).get(0) instanceof DataSet){
				DataSet set = (DataSet) ((ArrayList)field.getValue()).get(0);
				this.textValue.setValue((String)((StringObject)set.getObject(0)).getValue());
				
			}else{
				this.textValue.setValue((String)editor.getModel().get((DataObject)((ArrayList)field.getValue()).get(0)).getObject(0).getValue());	
			}
		}
			
	}
	
	private String getTextValueFromId(AbstractField field){
		DataModel model = editor.getModel();
		String textValue = "";
		if(field.getValue() != null){
			for (int i = 0; i < model.size(); i++) {
				DataSet set = model.get(i);
					if(set.getObject(1).getValue().equals(field.getValue())){
						textValue = (String)((StringObject)set.getObject(0)).getValue();
					}	
			}
		}
		return textValue;
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

	public boolean onEventPreview(Event event) {
		if (DOM.eventGetType(event) == Event.ONKEYDOWN || DOM.eventGetType(event) == Event.ONKEYUP) {
	        DOM.eventCancelBubble(event, true);
	        DOM.eventPreventDefault(event);
	        return false;
	    }
	    return true;
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
        	set.setKey(id);
        }else if(type.equals("string")){
        	StringObject id = new StringObject();
        	id.setValue(item.getAttributes().getNamedItem("value").getNodeValue());
        	set.setKey(id);
        }
        
		dataModel.add(set);
    	}
        return dataModel;
	}

	public void enable(boolean enabled) {}
	
	public void setModel(DataModel model){
		editor.setModel(model);
	}
    
    public void setCellWidth(int width){
    /*    this.width = width;
        if(editor != null){
            editor.setWidth(width+"px");
        }
        if(display != null)
            display.setWidth(width+"px");*/
    }
}

