package org.openelis.gwt.client.widget;

import org.openelis.gwt.client.services.AutoCompleteServiceInt;
import org.openelis.gwt.client.services.AutoCompleteServiceIntAsync;
import org.openelis.gwt.client.widget.table.TableCellWidget;
import org.openelis.gwt.client.widget.table.TableController;
import org.openelis.gwt.client.widget.table.TableManager;
import org.openelis.gwt.client.widget.table.TableWidget;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataModelWidget;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberField;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.common.data.TableRow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
/**
 * AutoCompleteTextBox extends a GWT TextBox to add auto suggestion features that can be 
 * requested from a RemoteServiceServlet implementing the AutoCompleteServiceInt.  Suggestions
 * will appear in a pop up box below the textbox and the user can arrow down or click on an
 * item to select it.
 * 
 * @author tschmidt
 *
 */
public class AutoCompleteTextBox extends Composite implements
                                                KeyboardListener,
                                                ChangeListener,
                                                ClickListener, 
                                                MouseListener,
                                                PopupListener,
                                                FocusListener,
                                                TableManager{
	
	//-------
	private HorizontalPanel mainHP = new HorizontalPanel();
	public TextBox textBox = new TextBox();
	private FocusPanel focusPanel = new FocusPanel();
	//-------
    /**
     * Widget used to display the suggestions and register
     * click events.
     */
    /*protected ListBox choices = new ListBox() {
        public void onBrowserEvent(Event event) {
            if (Event.ONCLICK == DOM.eventGetType(event)) {
                complete();
            }
        }
    };*/
	TableWidget tableWidget;
	final PopupPanel choicesPopup = new PopupPanel(true);
	//protected VerticalPanel popupPanel = new VerticalPanel();
    protected boolean dropdown = false;
    protected String textBoxDefault = "";
    protected boolean popupAdded = false;
    protected boolean visible = false;
    protected String popupHeight = "";
    protected String width = "";
    protected String fieldCase = "mixed";
    protected int currentCursorPos = 0;
    protected String type = "integer";
    protected Integer startPos = new Integer(0);
    
    //table values
    AbstractField[] fields;
    TableCellWidget[] editors;
    String[] headers;
    int[] widths;
    
    /**
     * RPC class for returning data from the server.
     */
	private DataModelWidget modelWidget = new DataModelWidget();
    /**
     * Category for which suggestions we are trying to match
     */
    private String cat;
    /**
     * Callback class for handling returning matches
     */
    private GetMatches matchCallback = new GetMatches();
    /**
     * Callback class for handing display call
     */
    private GetDisplay displayCallback = new GetDisplay();
    public Object value;
    private ChangeListenerCollection callback;
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
    
    public void setCase(String fieldCase){
        this.fieldCase = fieldCase;
    }

    /**
     * This inner class is used to add a delay to the before calling the 
     * back end to limit the number of calls made to the server.  If a
     * second key is hit within a certain amount of time the call for
     * suggestions is cancelled
     * 
     * @author tschmidt
     *
     */
    private class Delay extends Timer {
        public String text;

        public Delay(String text, int time) {
            this.text = text;
            this.schedule(time);
        }

        public void run() {
            if (textBox.getText().equals(text)) {
                if(fieldCase.equals("upper"))
                    text = text.toUpperCase();
                else if(fieldCase.equals("lower"))
                    text = text.toLowerCase();
                callForMatches(text);
            }
        }
    };


    /**
     * Contstructor for AutoCompleteTextBox
     * @param cat
     * Category is used to know which values to match the entered text to.
     * @param serviceUrl
     * This is the url to the RemoteServiceServlet that handles calls for 
     * this widget
     */
    /*public AutoCompleteTextBox(String cat, String serviceUrl) {        
        initService(serviceUrl);
        this.cat = cat;
        if(textBoxDefault != null)
    		textBox.setText(textBoxDefault);
        
        if(width != null)
        		textBox.setWidth(width);	

    	initWidget(mainHP);
    	mainHP.add(textBox);
    	//textBox.setWidth("100px");
    	textBox.addFocusListener(this);
    	focusPanel.addMouseListener(this);
    	focusPanel.addClickListener(this);
    	textBox.addKeyboardListener(this);
    	textBox.addStyleName("TextboxUnselected");
    	mainHP.add(focusPanel);
    	mainHP.setSpacing(0);
    	focusPanel.addStyleName("AutoDropdownButton");
    	mainHP.addStyleName("AutoDropdown");
       // choicesPopup.addStyleName("AutoCompleteChoices");
    }*/
    
    /**
     * Contstructor for AutoCompleteTextBox
     * @param cat
     * Category is used to know which values to match the entered text to.
     * @param serviceUrl
     * This is the url to the RemoteServiceServlet that handles calls for 
     * this widget
     */
    public AutoCompleteTextBox(String cat, String serviceUrl, boolean dropDown, String textBoxDefault, String width) {        
        initService(serviceUrl);
        this.cat = cat;
        this.dropdown = dropDown;
        this.textBoxDefault = textBoxDefault;
        this.width = width;
        if(textBoxDefault != null)
    		textBox.setText(textBoxDefault);
        
        if(width != null)
        		textBox.setWidth(width);	

    	initWidget(mainHP);
    	mainHP.add(textBox);
    	textBox.addFocusListener(this);    	
    	textBox.addKeyboardListener(this);
    	textBox.addStyleName("TextboxUnselected");    	
    	mainHP.setSpacing(0);    	
    	mainHP.addStyleName("AutoDropdown");
    	
    	if(dropdown){
    		focusPanel.addMouseListener(this);
        	focusPanel.addClickListener(this);
        	mainHP.add(focusPanel);
        	focusPanel.addStyleName("AutoDropdownButton");
    	}
    }

    /**
     * Not used at all
     */
    public void onKeyDown(Widget arg0, char arg1, int arg2) {
    }

    /**
     * Not used at all
     */
    public void onKeyPress(Widget arg0, char arg1, int arg2) {
    }

    /**
     * A key was released, start autocompletion
     */
    public void onKeyUp(Widget arg0, char arg1, int arg2) {
    	if(!textBox.isReadOnly() && choicesPopup.isVisible()){
	        if (arg1 == KEY_DOWN) {
	            int selectedIndex = tableWidget.controller.selected;
	            selectedIndex++;
	            if (selectedIndex > tableWidget.controller.model.numRows()-1) {
	               // selectedIndex = 0;
	            	//do nothing
	            	return;
	            }
	            tableWidget.controller.select(selectedIndex);
	            //need to set the textbox to the text of the selected item
	            String firstRowDisplayText = ((StringField)tableWidget.controller.model.getRow(selectedIndex).getHidden("displayText")).toString();
		        textBox.setText(firstRowDisplayText);
		        textBox.setSelectionRange(currentCursorPos, firstRowDisplayText.length() - currentCursorPos);
	            return;
	        }
	        if (arg1 == KEY_UP) {
	            int selectedIndex = tableWidget.controller.selected;
	            selectedIndex--;
	            if (selectedIndex < 0) {
	            	selectedIndex = -1;
	                //selectedIndex = tableWidget.controller.model.numRows()-1;
	            	tableWidget.controller.unselect(0);
	            	//clear the text box
	            	String currentText = textBox.getText();
	            	if(!currentText.equals(""))
	            		textBox.setText(currentText.substring(0, currentCursorPos));
	            	return;
	            }
	            tableWidget.controller.select(selectedIndex);
	            //need to set the textbox to the text of the selected item
	            String firstRowDisplayText = ((StringField)tableWidget.controller.model.getRow(selectedIndex).getHidden("displayText")).toString();
		        textBox.setText(firstRowDisplayText);
		        textBox.setSelectionRange(currentCursorPos, firstRowDisplayText.length() - currentCursorPos);
	            return;
	        }
	        if (arg1 == KEY_ENTER || arg1 == KEY_TAB) {
	        	System.out.println("key enter or tab");
	            if (visible) {
	            	choicesPopup.hide();
	            	//textBox.setSelectionRange(0,0);
	                complete();
	            }
	            return;
	        }
	        if (arg1 == KEY_ESCAPE) {
	            //choices.clear();
	            choicesPopup.hide();
	            visible = false;
	            return;
	        }
	        String text = textBox.getText();
	        value = null;
	        if (text.length() > 0 && !text.endsWith("*")) {
	            new Delay(text, 350);
	        } else {
	            //choices.clear();
	            choicesPopup.hide();
	            visible = false;
	        }
    	}
    }

    /**
     * Displays the suggestion returned from the server.
     */
    public void showMatches(int startPos) {
        if (modelWidget.getModel().size() > 0) {
        	focusPanel.addStyleName("Selected");
        	//popupPanel.addStyleName("Dropdown");
        	//popupPanel.setSpacing(0);
        	
        	choicesPopup.clear();
			choicesPopup.addStyleName("AutoCompletePopup");
	        choicesPopup.addPopupListener(this);
			choicesPopup.setPopupPosition(this.getAbsoluteLeft(),
                    this.getAbsoluteTop() + this.getOffsetHeight());

			
			choicesPopup.show();
			
			tableWidget = new TableWidget();
			tableWidget.setManager(this);
			tableWidget.setHeight(popupHeight);
			tableWidget.addStyleName("Dropdown");
			tableWidget.setMaxRows(10);
			//if(popupPanel.getWidgetCount() == 0)
			//	popupPanel.add(tableWidget);
			
			tableWidget.setWidth("auto");
			
	        tableWidget.setFields(fields);
	       
	        tableWidget.setEditors(editors);
	        
	        tableWidget.setColWidths(widths);
	       
	        if(headers != null)
	        	tableWidget.setHeaders(headers);
	        
	        //false array
	        boolean[] falseArray = new boolean[fields.length];
	        for(int k=0;k<fields.length; k++)
	        	falseArray[k] = false;
	        //always set filters to false
	        tableWidget.setSortable(falseArray);
	        //always set sorts to false
	        tableWidget.setFilterable(falseArray);
	        
	        //need to use the size of the dataset to determine how to fill the table
	        DataSet data;
	        for (int i = startPos; i < modelWidget.getModel().size(); i++) {
	        data = modelWidget.getModel().get(i);
	        TableRow row = new TableRow();
	        if(type.equals("string")){
	        	StringField hiddenId = new StringField();
	        	hiddenId.setDataObject((StringObject)data.getObject(0));
	        	row.addHidden("id",hiddenId);
	        }else if(type.equals("integer")){
	        	NumberField hiddenId = new NumberField();
	        	hiddenId.setType("integer");
	        	hiddenId.setDataObject((NumberObject)data.getObject(0));
	        	row.addHidden("id",hiddenId);
	        }
	        //we need to iterate through the columns to build the rest of the row
	        for(int j=1; j<(data.size()-2);j++){
	        	StringField field = new StringField();
	        	field.setDataObject((StringObject)data.getObject(j));
	        	row.addColumn(field);
	        }
	        
	        //we need to set the first row display text if this is the first row
	        StringField displayText = new StringField();
	        displayText.setDataObject(data.getObject(data.size()-2));
	        row.addHidden("displayText", displayText);

	        //we need to add the selected hidden flag
	        StringField selected = new StringField();
	        selected.setDataObject((StringObject)data.getObject(data.size()-1));
	        row.addHidden("selected", selected);
	        tableWidget.controller.model.addRow(row);
	        }
	        tableWidget.init(0);
	        //choicesPopup.setWidth(tableWidget.controller.view.table.getOffsetWidth()+"px");
	        choicesPopup.setWidget(tableWidget);
	        
	        //we need to select the text in the textbox
	        //textBox.selectAll();
	        
	        //we need to set the selected style name to the textbox
	        textBox.removeStyleName("TextboxUnselected");
	        textBox.addStyleName("TextboxSelected");
	        
	        //we need to put the text of the first item in the textbox and do a selection
	        currentCursorPos = textBox.getText().length();
	        String firstRowDisplayText = ((StringField)tableWidget.controller.model.getRow(0).getHidden("displayText")).toString();
	        if(firstRowDisplayText.indexOf(textBox.getText().toUpperCase()) == 0){	   
	        	textBox.setText(firstRowDisplayText);
	        	textBox.setSelectionRange(currentCursorPos, firstRowDisplayText.length() - currentCursorPos);
	        }
           
            // if there is only one match and it is what is in the
            // text field anyways there is no need to show autocompletion
         //FIXME note sure if we should complete it for the user...
	        //FIXME if (modelWidget.getModel().size() == 1 && (((String)((StringObject)modelWidget.getModel().get(0).getObject(modelWidget.getModel().get(0).size()-2)).getValue())
        	//FIXME 	   			.compareTo(textBox.getText()) == 0)) {
        	//FIXME    choicesPopup.hide();
          //FIXME } else {
            	tableWidget.controller.select(0);
              //  choices.setSelectedIndex(0);
              //  choices.setVisibleItemCount(rpc.display.length + 1);
                visible = true;         
         //FIXME }
        } else {
            visible = false;
            choicesPopup.hide();
        }
    }

    /**
     * A mouseclick in the list of items
     */
    public void onChange(Widget arg0) {
        complete();
    }

    /**
     * Set the selection that the user made.
     */
    protected void complete() {
    	if (tableWidget.controller.model.numRows() > 0) {
    		String displayText = ((StringField)tableWidget.controller.model.getRow(tableWidget.controller.selected).
    								getHidden("displayText")).toString();
    		String firstRowDisplayText = ((StringField)tableWidget.controller.model.getRow(tableWidget.controller.selected).getHidden("displayText")).toString();
	        
    		textBox.setText(displayText);
            
    		if(type.equals("string")){
    			String id = (String)((StringField)tableWidget.controller.model.getRow(tableWidget.controller.selected).
    					getHidden("id")).getValue();
    			this.value = id;
    		}else if(type.equals("integer")){
        		Integer id = (Integer)((NumberField)tableWidget.controller.model.getRow(tableWidget.controller.selected).
    					getHidden("id")).getValue();
        		this.value = id;
    		}
        }
    	visible = false;
        choicesPopup.hide();
        textBox.selectAll();
        if (callback != null)
            callback.fireChange(this);
        
        tableWidget = null;
    }

    /**
     * Method that calls the service to retrieve the suggestions
     * @param text
     */
    protected void callForMatches(final String text) {
        autoService.getMatches(cat, modelWidget.getModel(), text, matchCallback);
    }

    /**
     * Sets the value for the widget and calls the server to retrieve the display
     * value
     * @param value
     */
    public void setValue(Object value) {
        if(type.equals("string")){
        	String val = (String)value;
        	this.value = value;
        	if (value != null && !val.equals("")){
        		StringField stringField = new StringField();
        		stringField.setValue(val);
        		DataModel model = null;
        		if(dropdown)
        			model = modelWidget.getModel();
                autoService.getDisplay(cat, model, stringField, displayCallback);
        	}else
                textBox.setText("");
        }else if(type.equals("integer")){
        	Integer val = (Integer)value;
        	this.value = value;
        	if (value != null && val.intValue() > 0){
                NumberField numberField = new NumberField();
                numberField.setType("integer");
                numberField.setValue(val);
                DataModel model = null;
        		if(dropdown)
        			model = modelWidget.getModel();
        		autoService.getDisplay(cat, model, numberField, displayCallback);
        	}else
                textBox.setText("");
        }
    }

    /**
     * GetDisplay Calls the RemoteServlet to get the display for the set value
     * @author tschmidt
     *
     */
    private class GetDisplay implements AsyncCallback {
        public void onSuccess(Object result) {
        	DataModel model = (DataModel)result;
        	//modelWidget.setModel((DataModel)result);
            //FIXME note sure what to do here yet...
        	//setText(rpc.dict_value);
        	textBox.setText((String)((StringObject)model.get(0).getObject(1)).getValue());
        }

        public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
        }
    }

    /**
     * GetMatches call the RemoteServlet to get the suggestions to display
     * @author tschmidt
     *
     */
    private class GetMatches implements AsyncCallback {
        public void onSuccess(Object result) {
        	DataModel model = (DataModel)result;
        	
        	if(model.size() == 0){
        		//set textbox text back to what it was before
        		textBox.setText(textBox.getText().substring(0,currentCursorPos));
        		
        	} else if(model.get(0).size() > 1) {
        		modelWidget.setModel((DataModel)result);
        		startPos = new Integer(0);
        	} else
        		startPos = (Integer)((NumberObject)model.get(0).getObject(0)).getValue();
            showMatches(startPos.intValue());
        }

        public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
        }
    }

    /**
     * I think is deprecated and should be deleted
     * @param callback
     */
    public void setCallback(ChangeListenerCollection callback) {
        this.callback = callback;
    }

    /**
     * This method will wipe the textbox and set the selection value to null
     */
    public void reset() {
        this.value = null;
        textBox.setText("");
    }

	public String getPopupHeight() {
		return popupHeight;
	}

	public void setPopupHeight(String popupHeight) {
		this.popupHeight = popupHeight;
	}

	public void onClick(Widget sender) {
		if(!textBox.isReadOnly()){
			if(sender == focusPanel && tableWidget == null){
				//FIXME we need to fill the model if it is null
				if(modelWidget.getModel().size() == 0)
					System.out.println("model null");
				
				showMatches(0);
			}else{
				tableWidget = null;
			}
		}		
	}

	public void onMouseDown(Widget sender, int x, int y) {
		if(!textBox.isReadOnly()){
			if(sender == focusPanel){
				focusPanel.addStyleName("Pressed");
			}
		}
	}

	public void onMouseEnter(Widget sender) {
		if(!textBox.isReadOnly()){
			if(sender == focusPanel){
				focusPanel.addStyleName("Hover");
			}
		}
	}

	public void onMouseLeave(Widget sender) {
		if(!textBox.isReadOnly()){
			if(sender == focusPanel){
				focusPanel.removeStyleName("Hover");
			}
		}
	}

	public void onMouseMove(Widget sender, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseUp(Widget sender, int x, int y) {
		if(!textBox.isReadOnly()){
			if(sender == focusPanel){
			focusPanel.removeStyleName("Pressed");
			}
		}
	}

	public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
		focusPanel.removeStyleName("Selected");
		
        //we need to set the unselected style name to the textbox
        textBox.addStyleName("TextboxUnselected");
        textBox.removeStyleName("TextboxSelected");	
	}

	public void onFocus(Widget sender) {
		if(!textBox.isReadOnly()){
			if(sender == textBox){
//				we need to set the unselected style name to the textbox
				textBox.addStyleName("TextboxSelected");
				textBox.removeStyleName("TextboxUnselected");
				// textBox.setText("");
				focusPanel.addStyleName("Selected");
			}
		}
	}

	public void onLostFocus(Widget sender) {
		if(!textBox.isReadOnly()){
			if(sender == textBox){
				//we need to set the unselected style name to the textbox
				textBox.addStyleName("TextboxUnselected");
				textBox.removeStyleName("TextboxSelected");
				if(textBoxDefault != null)
					textBox.setText(textBoxDefault);
				//else
				//	textBox.setText("");
				focusPanel.removeStyleName("Selected");
				complete();
			}
		}
	}

	public void setDropdown(boolean dropdown) {
		this.dropdown = dropdown;
	}

	public void setText(String text) {
		this.textBoxDefault = text;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setEditors(TableCellWidget[] editors) {
		this.editors = editors;
	}

	public void setFields(AbstractField[] fields) {
		this.fields = fields;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	public void setWidths(int[] widths) {
		this.widths = widths;
	}
	public void clear(){
		modelWidget.getModel().clear();
	}
	public void addItem(String key, String display){
		DataSet data = new DataSet();
		
		StringObject id = new StringObject();
		id.setValue(key);
		data.addObject(id);
		
		StringObject col1 = new StringObject();
		col1.setValue(display.trim());
		data.addObject(col1);
		
		StringObject displayValue = new StringObject();
		displayValue.setValue(display.trim());
		data.addObject(displayValue);
		
		StringObject selected = new StringObject();
		selected.setValue("N");
		data.addObject(selected);
		
		modelWidget.getModel().add(data);		
	}
	
	public int getItemCount(){
		return modelWidget.getModel().size();
	}
	
	public void setItemSelected(int selected, boolean isSelected){
		String value = (isSelected ? "Y" : "N");
		
		((StringObject)modelWidget.getModel().get(selected).getObject(modelWidget.getModel().get(selected).size()-1)).setValue(value);
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean action(int row, int col, TableController controller) {
		String value = (String)((StringObject)modelWidget.getModel().get(row).getObject(modelWidget.getModel().get(row).size()-1)).getValue();
		setItemSelected(row, (value.equals("Y")));
		tableWidget.controller.selected = row;
		complete();
		return false;
	}
	public boolean canDelete(int row, TableController controller) {
		return false;
	}
	public boolean canEdit(int row, int col, TableController controller) {
		return false;
	}
	public boolean canInsert(int row, TableController controller) {
		return false;
	}
	public boolean canSelect(int row, TableController controller) {
		return true;
	}
	public boolean doAutoAdd(int row, int col, TableController controller) {
		return false;
	}
	public void finishedEditing(int row, int col, TableController controller) {}
	public void getNextPage(TableController controller) {}
	public void getPage(int page) {}
	public void getPreviousPage(TableController controller) {}
	public void rowAdded(int row, TableController controller) {}
	public void setModel(TableController controller, DataModel model) {}
	public void getNextPage() {}
	public void getPreviousPage() {}
}