package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberField;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.OptionField;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.services.AutoCompleteServiceInt;
import org.openelis.gwt.services.AutoCompleteServiceIntAsync;
import org.openelis.gwt.widget.AutoCompleteTextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class TableAuto extends TableCellInputWidget implements EventPreview {

	private AutoCompleteTextBox editor;
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
    private String url="";
    private int width;
    
    private AutoCompleteServiceIntAsync autoService = (AutoCompleteServiceIntAsync)GWT.create(AutoCompleteServiceInt.class);
    private ServiceDefTarget target = (ServiceDefTarget)autoService;

    /**
     * This Method will set the url for the AutoCompleteService.
     * 
     * @param url
     */
    public void initService(String url) {
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }
        
    public TableAuto(){
    	sinkEvents(Event.KEYEVENTS);
    }
    
    public TableCellWidget getNewInstance() {
    	TableAuto ta = new TableAuto();
        ta.multi = multi;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listener = listener;
        ta.type = type;
        ta.url = url;
        ta.initService(url);
        return ta;
    }
    
    public TableAuto getNewTableAuto() {
    	TableAuto ta = new TableAuto();
        ta.multi = multi;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listener = listener;
        ta.type = type;
        ta.url = url;
        ta.initService(url);
        
        return ta;
    }
    
	public TableAuto(Node node) {
        this();
		AutoCompleteTextBox auto = new AutoCompleteTextBox();
		String cat = node.getAttributes().getNamedItem("cat").getNodeValue();
        url = node.getAttributes()
                         .getNamedItem("serviceUrl")
                         .getNodeValue();
        
        boolean dropDown = false;

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
        
        Node widthsNode = ((Element)node).getElementsByTagName("autoWidths").item(0);
        Node headersNode = ((Element)node).getElementsByTagName("autoHeaders").item(0);
        Node editorsNode = ((Element)node).getElementsByTagName("autoEditors").item(0);
        Node fieldsNode = ((Element)node).getElementsByTagName("autoFields").item(0);
        //Node filtersNode = ((Element)node).getElementsByTagName("filters").item(0);
        //Node sortsNode = ((Element)node).getElementsByTagName("sorts").item(0);
        //Node alignNode = ((Element)node).getElementsByTagName("colAligns").item(0);
        //Node statFilter = ((Element)node).getElementsByTagName("statFilters").item(0);
        Node optionsNode = ((Element)node).getElementsByTagName("autoItems").item(0);

        auto = new AutoCompleteTextBox(cat, url, dropDown, textBoxDefault, width);
        auto.mainHP.removeStyleName("AutoDropdown");
        auto.textBox.addStyleName("TableWidget");
        auto.textBox.addStyleName("Enabled");
        initService(url);
        
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
        if(optionsNode != null)
        	editor.setModel(getDropDownOptions(optionsNode));
	}

	public void saveValue() {
		if(type != null && type.equals("string")){
			((StringField)field).setValue(editor.value);
			textValue.setValue(editor.textBox.getText());
		}else if(type != null && type.equals("integer")){
			((NumberField)field).setValue(editor.value);
			textValue.setValue(editor.textBox.getText());
		}	
        super.saveValue();
	}

	//the editor is shared so we need to set the textvalue
	public void setDisplay() {
		if(display == null){
			display = new Label();
			display.setWordWrap(false);
            display.setWidth(width+"px");
		}
		
		//if(editor.textBox.getText() != null && !"".equals(editor.textBox.getText()))
		//	display.setText(editor.textBox.getText());
		//else
		//need to get the display
		try {
			autoService.getDisplay(editor.cat, editor.getModel(), field, new AsyncCallback() {
			       public void onSuccess(Object result){
			           DataModel model = (DataModel)result;
			           if(model.size()>0)
			        	   display.setText((String)((StringObject)model.get(0).getObject(1)).getValue());
			           else
			        	   display.setText("");
			       }
			       public void onFailure(Throwable caught){
			           Window.alert(caught.getMessage());
			       }
			    });
		} catch (RPCException e) {
			Window.alert("tableAuto-getDisplay: "+e.getMessage());
		}
		
		//display.setText((String)editor.textBox.getText());
		//	display.setText((String)textValue.getValue());

		setWidget(display);	
        super.setDisplay();
	}

	public void setEditor() {
		if(editor == null){
			editor = new AutoCompleteTextBox();
            editor.setWidth(width+"px");
            editor.mainHP.removeStyleName("AutoDropdown");
          //  editor.mainHP.addStyleName("TableAutoDropdown");
		}

		editor.setTableValue(field.getValue(),display.getText());
		setWidget(editor);	
		
	}

	public void setField(AbstractField field) {
		this.field = field;
		
		//setEditor();
		
		//we need to also set the display value because we cant guarantee its the same
		//this.textValue.setValue(getTextValueFromId(field));
	}
	
	/*private String getTextValueFromId(AbstractField field){
		DataModel model = editor.getModel();
		String textValue = "";
		if(field.getValue() != null){
			for (int i = 0; i < model.size(); i++) {
				DataSet set = model.get(i);
				//if(set.getObject(0) instanceof NumberObject){
					if(set.getObject(0).getValue().equals(field.getValue())){
						textValue = (String)((StringObject)set.getObject(set.size()-2)).getValue();
					}
				//}else if(set.getObject(0) instanceof StringObject){
					
				//}			
			}
		}
		return textValue;
	}*/
	
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
        
        //cols
        StringObject name = new StringObject();
		name.setValue((item.getFirstChild() == null ? " " : item.getFirstChild().getNodeValue()));
		set.addObject(name);

		//display text
        StringObject display = new StringObject();
		display.setValue((item.getFirstChild() == null ? " " : item.getFirstChild().getNodeValue()));
		set.addObject(display);
        //selected flag
		StringObject selected = new StringObject();
		selected.setValue("N");
		set.addObject(selected);
		
		dataModel.add(set);
    	}
        return dataModel;
	}

	public void enable(boolean enabled) {
		// TODO Auto-generated method stub
	}
    
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null){
            editor.setWidth(width+"px");
        }
        if(display != null){
            display.setWidth(width+"px");
        }
    }
}
