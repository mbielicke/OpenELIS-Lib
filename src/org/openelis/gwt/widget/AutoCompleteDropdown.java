package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.Vector;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.BooleanObject;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberField;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.services.AutoCompleteServiceInt;
import org.openelis.gwt.services.AutoCompleteServiceIntAsync;
import org.openelis.gwt.widget.table.TableCellWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
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
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AutoCompleteDropdown extends Composite implements
KeyboardListener,
ChangeListener,
ClickListener, 
MouseListener,
PopupListener,
FocusListener,
HasFocus{ 
	private HorizontalPanel mainHP = new HorizontalPanel();
	public TextBox textBox = new TextBox() {
        public void onBrowserEvent(Event event) {
            if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB)
                    screen.doTab(event, comp);
            } else {
                super.onBrowserEvent(event);
            }
        }
    };
    
	private FocusPanel focusPanel = new FocusPanel();

	
    /**
     * Callback class for handling returning matches
     */
    private GetInitialModel getModelCallback = new GetInitialModel();
    
    /**
     * Widget used to display the suggestions and register
     * click events.
     */
	ScrollList scrollList;
	//TableWidget tableWidget;
	final PopupPanel choicesPopup = new PopupPanel(true);
    protected String textBoxDefault = "";
    protected boolean popupAdded = false;
    protected boolean visible = false;
    protected String popupHeight = "";
    private ScreenBase screen;
    protected Widget comp;
    protected String width = "";
    protected String fieldCase = "mixed";
    protected int currentCursorPos = 0;
    protected String type = "integer";
    protected int startPos = 0;
    private boolean clickedArrow = false;
    private int currentStart = -1;
    private int currentActive = -1;
    protected boolean multiSelect = false;
    protected boolean fromModel = false;
    protected Vector multiSelected = new Vector();
    
    //table values
    AbstractField[] fields;
    TableCellWidget[] editors;
    String[] headers;
    int[] widths;
    
    public AutoCompleteDropdown(){
    }
    /**
     * Category for which suggestions we are trying to match
     */
    private String cat;

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
    public AutoCompleteDropdown(String cat, String serviceUrl, boolean fromModel, boolean multi, String textBoxDefault, String width) {        
        initService(serviceUrl);
        this.cat = cat;
        this.textBoxDefault = textBoxDefault;
        this.width = width;
        this.fromModel = fromModel;
        this.multiSelect = multi;
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
    	
   		focusPanel.addMouseListener(this);
       	focusPanel.addClickListener(this);
       	mainHP.add(focusPanel);
       	focusPanel.addStyleName("AutoDropdownButton");
       	
       	comp = this;
       	
       	scrollList = new ScrollList();
       	scrollList.setMulti(multiSelect);       	
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
    		if(arg1 == KEY_DOWN || arg1 == KEY_UP || arg1 == KEY_ENTER || arg1 == KEY_TAB)
    			return;
    		//if(arg1 == KEY_TAB){
    		//	screen.doTab(event, comp);
    		//}
	        if (arg1 == KEY_ESCAPE) {
	            choicesPopup.hide();
	            visible = false;
	            return;
	        }
	        
	        String text = textBox.getText();
	        value = null;
	        if(multiSelect){
	        	if(text.length() == 0){
	        		multiSelected.clear();
	        		scrollList.unselectAll();
	        		choicesPopup.hide();
		            visible = false;
	        	}else{
	        		if(text.length() < textBoxDefault.length()){
	        			textBox.setText(textBoxDefault);
	        			choicesPopup.hide();
			            visible = false;
	        		}
	        	}
	        }else if (text.length() > 0 && !text.endsWith("*")) {
	            new Delay(text, 350);
	            currentStart = -1;
	            currentActive = -1;
	        } else {
	            choicesPopup.hide();
	            visible = false;
	        }
    	}
    }

    /**
     * Displays the suggestion returned from the server.
     */
    public void showMatches(int startPos) {
        if (scrollList.getDataModel().size() > 0) {
        	focusPanel.addStyleName("Selected");
        	
        	choicesPopup.clear();
			choicesPopup.addStyleName("AutoCompletePopup");
	        choicesPopup.addPopupListener(this);
			choicesPopup.setPopupPosition(this.getAbsoluteLeft(),
                    this.getAbsoluteTop() + this.getOffsetHeight()-1);
			//choicesPopup.setWidth("1px");
			
			choicesPopup.show();
//			we need to load the model if it is coming from the database here
		       //	if(fromModel)
		      // 		autoService.getInitialModel(cat, getModelCallback);
			scrollList.addChangeListener(this);

			scrollList.addStyleName("Dropdown");
			
			//FIXME currently the hardcoded max rows is 10...might need to change this
			if(scrollList.getDataModel().size()<10){
				scrollList.setMaxRows(scrollList.getDataModel().size());
				scrollList.setWidth(textBox.getOffsetWidth()+14+"px");
			} else {
				scrollList.setMaxRows(10);
				scrollList.setWidth(textBox.getOffsetWidth()-4+"px");
			}
			
	        choicesPopup.setWidget(scrollList);
	        DOM.addEventPreview(scrollList);
	        
	        if(!multiSelect)
	        	scrollList.unselectAll();
	        
	        if(clickedArrow && scrollList.getActive() > -1 && currentStart > -1 && currentActive > -1)
	        	startPos = currentStart + currentActive;
	        
	        //Window.alert(String.valueOf(startPos));
	        
	        if(!multiSelect)
	        	scrollList.getDataModel().get(startPos).getObject(2).setValue(new Boolean(true));
	        
	        //Window.alert("after");
	       //FIXME this could be hosing the selection...look into this
	        
	        //then the active might not be the first one...
	       if(startPos > scrollList.getDataModel().size()-scrollList.getMaxRows() && !multiSelect){
	    	   scrollList.setActive(scrollList.getMaxRows() - ((scrollList.getDataModel().size()-1) - startPos) - 1);
	       }else
	        	scrollList.setActive(0);
	       

	        scrollList.setDataModel(scrollList.getDataModel());
	        
	       if(!multiSelect){
	    	   scrollList.scrollBar.setScrollPosition(startPos*scrollList.getCellHeight());
	    	   scrollList.scrollLoad(scrollList.scrollBar.getScrollPosition());
	       }else
	        	scrollList.scrollLoad(0);

	        //we need to set the selected style name to the textbox
	        textBox.removeStyleName("TextboxUnselected");
	        textBox.addStyleName("TextboxSelected");
	        
	       if(!multiSelect){
	        	//we need to put the text of the first item in the textbox and do a selection
	    	   currentCursorPos = textBox.getText().length();
	    	   String firstRowDisplayText = (String)((StringObject)scrollList.getDataModel().get(startPos).getObject(0)).getValue();
	    	   if(firstRowDisplayText.indexOf(textBox.getText().toUpperCase()) == 0){	   
	    		   textBox.setText(firstRowDisplayText);
	    		   textBox.setSelectionRange(currentCursorPos, firstRowDisplayText.length() - currentCursorPos);
	    	   }
	       }
           
	        //FIXME not sure if this is necessary...
         //   scrollList.getDataModel().select(0);
            visible = true;         
            
        } else {
            visible = false;
            choicesPopup.hide();
        }
        clickedArrow = false;
    }

    /**
     * A mouseclick in the list of items
     */
    public void onChange(Widget arg0) {
    	int index = -1;
    	Vector selected = new Vector();
    	String textValue = "";
    	int selectionLength = -1;
    	if(!multiSelect){
    		index = scrollList.getStart() + scrollList.getActive();
    		textValue = (String)((StringObject)scrollList.getDataModel().get(index).getObject(0)).getValue();
    	}else{
    		selected = scrollList.getSelected();  //vector if indexes
    		
    		for (int i = 0; i < selected.size(); i++) {
    			if(i+1 == selected.size())
    				selectionLength = ((String)((StringObject)scrollList.getDataModel().get(((Integer)selected.get(i)).intValue()).getObject(0)).getValue()).length();
    			
        		textValue = (String)((StringObject)scrollList.getDataModel().get(((Integer)selected.get(i)).intValue()).getObject(0)).getValue()+(!"".equals(textValue)?"|":"")+textValue;
    		}
    		
    		multiSelected = selected;
    	}
    	
    	DOM.removeEventPreview(scrollList);
    	
		textBox.setText(textValue);
		textBoxDefault = textValue;
		
		if(multiSelect && selectionLength>-1)
			textBox.setSelectionRange(0, selectionLength);
		textBox.setFocus(true);
        complete();
    }
    
    private class GetInitialModel implements AsyncCallback {
        public void onSuccess(Object result) {
        	scrollList.setDataModel((DataModel)result);
        }

        public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
        }
    }

    /**
     * Set the selection that the user made.
     */
    protected void complete() {
    	if(scrollList == null || textBox.getText().length() == 0){
    		if(textBox.getText().equals("")){
    			textBox.setText("");
    			this.value = null;
    		}
    		//else do nothing
    	}else if (scrollList.getDataModel().size() > 0) {
    		//get the index of the selected
    		int index = -1;
    		String displayText = "";
    		if(!multiSelect){
    			index = scrollList.getStart() + scrollList.getActive();
    			displayText = (String)((StringObject)scrollList.getDataModel().get(index).getObject(0)).getValue();
    		} else {
    			for (int i = 0; i < multiSelected.size(); i++) {        			
    				displayText = (String)((StringObject)scrollList.getDataModel().get(((Integer)multiSelected.get(i)).intValue()).getObject(0)).getValue()+(!"".equals(displayText)?"|":"")+displayText;
        		}
    		}
	        
    		textBox.setText(displayText);
            
    		if(type.equals("string")){
    			if(multiSelect){
    				
    			}else{
    				String id = (String)((DataObject)scrollList.getDataModel().get(index).getObject(1)).getValue();
    				this.value = id;
    			}
    		}else if(type.equals("integer")){
    			if(multiSelect){
    				
    			}else{
    				Integer id = (Integer)((DataObject)scrollList.getDataModel().get(index).getObject(1)).getValue();
        			this.value = id;
    			}
    		}
        }
    	currentStart = -1;
    	currentActive = -1;
    	if(!multiSelect){
    		visible = false;
    		choicesPopup.hide();
        }
        if (callback != null)
            callback.fireChange(this);
    }

    /**
     * Method that calls the service to retrieve the suggestions
     * @param text
     */
    protected void callForMatches(final String text) {
        getMatches(text);
    }

    /**
     * Sets the value for the widget and calls the server to retrieve the display
     * value
     * @param value
     */
    public void setValue(Object value) {
    	if(value instanceof ArrayList){
    		this.value = value;
    	}else{
	        if(type.equals("string")){
	        	String val = (String)value;
	        	this.value = value;
	        	if (value != null && !val.equals("")){
	        		StringField stringField = new StringField();
	        		stringField.setValue(val);
	     
	                getDisplay(stringField);
	        	}else
	                textBox.setText("");
	        }else if(type.equals("integer")){
	        	Integer val = (Integer)value;
	        	this.value = value;
	        	if (value != null && val.intValue() > 0){
	                NumberField numberField = new NumberField();
	                numberField.setType("integer");
	                numberField.setValue(val);

	        		getDisplay(numberField);
	        	}else
	                textBox.setText("");
	        }
    	}
    }
    
    private void getDisplay(StringField value){
        DataModel model = scrollList.getDataModel();
    	for (int i = 0; i < model.size(); i++) {
    		if(((StringObject)model.get(i).getObject(1)).getValue().equals(value.getValue())){
    			textBox.setText((String)((StringObject)model.get(i).getObject(0)).getValue());
    			break;
    		}			
    	}
    }
    
    private void getDisplay(NumberField value){
        DataModel model = scrollList.getDataModel();
    	for (int i = 0; i < model.size(); i++) {
    		if(((NumberObject)model.get(i).getObject(1)).getValue().equals(value.getValue())){
    			textBox.setText((String)((StringObject)model.get(i).getObject(0)).getValue());
    			break;
    		}			
    	}
    }
    
    private void getMatches(String match){
    	DataModel model = scrollList.getDataModel();
    	int tempStartPos = -1;
		int i=0;
		while(i<model.size() && ((String)model.get(i).getObject(0).getValue()).indexOf(match) != 0){
			i++;
		}
		if(i < model.size()){
			tempStartPos = i;
			this.startPos = i;
		}
    
		//FIXME not sure what this is for...taking it out for now...
		if(tempStartPos == -1 && !textBox.getText().equals("")){
    		//set textbox text back to what it was before
    		textBox.setText(textBox.getText().substring(0,currentCursorPos));	
    	}
		
        showMatches(this.startPos);
    
    }
    
    public void setModel(DataModel model){
    	scrollList.setDataModel(model);
    }
    
    public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public DataModel getModel(){
    	return scrollList.getDataModel();
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
		textBox.setText("");
		scrollList.unselectAll();
		scrollList.setStart(0);
		scrollList.setActive(0);
		currentStart = -1;
		currentActive = -1;
    }

	public String getPopupHeight() {
		return popupHeight;
	}

	public void setPopupHeight(String popupHeight) {
		this.popupHeight = popupHeight;
	}

	public void onClick(Widget sender) {
		if(!textBox.isReadOnly()){
			if(sender == focusPanel){
				if(currentStart == -1)
					currentStart = scrollList.getStart();
			
				if(currentActive == -1)
					currentActive = scrollList.getActive();
				
				//if this is true then we need to find these values manually
				if(currentActive == -1 && currentStart == 0 && value != null && !"".equals(value) && value != new Integer(-1)){
					if(type.equals("string")){
						for(int i=0; i<scrollList.getDataModel().size(); i++){
							if(((String)((StringObject)scrollList.getDataModel().get(i).getObject(1)).getValue()).equals(value)){
								currentStart = i;
								currentActive = 0;
								scrollList.setActive(0);
								break;
							}
						}
					}else if(type.equals("integer")){
						for(int j=0; j<scrollList.getDataModel().size(); j++){
							if(((Integer)((NumberObject)scrollList.getDataModel().get(j).getObject(1)).getValue()).equals(value)){
								currentStart = j;
								currentActive = 0;
								scrollList.setActive(0);
								break;
							}
						}
					}
				}
				
				clickedArrow = true;
				//FIXME we need to fill the model if it is null
				if(scrollList.getDataModel().size() == 0)
					System.out.println("model null");
				
				showMatches(0);
			}else{
				//do nothing for now    popupShowing = true;
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
	//	focusPanel.removeStyleName("Selected");
		
        //we need to set the unselected style name to the textbox
       // textBox.addStyleName("TextboxUnselected");
       // textBox.removeStyleName("TextboxSelected");	
	}

	public void onFocus(Widget sender) {
		if(!textBox.isReadOnly()){
			if(sender == textBox){
//				we need to set the selected style name to the textbox
				textBox.addStyleName("TextboxSelected");
				textBox.removeStyleName("TextboxUnselected");
				textBox.setFocus(true);
				//delete  textBox.setText("");
				focusPanel.addStyleName("Selected");				

				if(currentStart == -1)
					currentStart = scrollList.getStart();
			
				if(currentActive == -1)
					currentActive = scrollList.getActive();
				
				//if this is true then we need to find these values manually
				if(currentActive == -1 && currentStart == 0 && value != null && !"".equals(value) && value != new Integer(-1)){
					if(type.equals("string")){
						for(int i=0; i<scrollList.getDataModel().size(); i++){
							if(((String)((StringObject)scrollList.getDataModel().get(i).getObject(1)).getValue()).equals(value)){
								currentStart = i;
								currentActive = 0;
								scrollList.setActive(0);
								break;
							}
						}
					}else if(type.equals("integer")){
						for(int j=0; j<scrollList.getDataModel().size(); j++){
							if(((Integer)((NumberObject)scrollList.getDataModel().get(j).getObject(1)).getValue()).equals(value)){
								currentStart = j;
								currentActive = 0;
								scrollList.setActive(0);
								break;
							}
						}
					}
				}
				
				//clickedArrow = true;
				
				//we need to open the popup like a normal dropdown
				//showMatches(0);
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
				
				focusPanel.removeStyleName("Selected");
				
				complete();
			}
		}
	}
	
	private void onTextBoxTab(){
		
	}
	
	public ArrayList getSelectedList(){
		ArrayList returnList = new ArrayList();
		
		if(type.equals("integer")){
			for (int i = 0; i < multiSelected.size(); i++) {
				returnList.add((Integer)((NumberObject)scrollList.getDataModel().get(((Integer)multiSelected.get(i)).intValue()).getObject(1)).getValue());
			}
		}else if(type.equals("string")){
			for (int i = 0; i < multiSelected.size(); i++) {
				returnList.add((String)((StringObject)scrollList.getDataModel().get(((Integer)multiSelected.get(i)).intValue()).getObject(1)).getValue());
			}			
		}
			
		return returnList;
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
		scrollList.getDataModel().clear();
	}
	
	public void addItem(String key, String display){
		DataSet data = new DataSet();
		
		StringObject text = new StringObject();
		text.setValue(display.trim());
		data.addObject(text);
		
		StringObject id = new StringObject();
		id.setValue(key);
		data.addObject(id);
		
		BooleanObject selected = new BooleanObject();
		selected.setValue(new Boolean(false));
		data.addObject(selected);
		
		scrollList.getDataModel().add(data);		
	}
	
	public int getItemCount(){
		return scrollList.getDataModel().size();
	}
	
	public void setItemSelected(int selected, boolean isSelected){
		String value = (isSelected ? "Y" : "N");
		
		((StringObject)scrollList.getDataModel().get(selected).getObject(scrollList.getDataModel().get(selected).size()-1)).setValue(value);
	}

	public void setType(String type) {
		this.type = type;
	}
	
	 public void setForm(ScreenBase screen) {
	        this.screen = screen;
	    }

	public int getTabIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setAccessKey(char key) {
		// TODO Auto-generated method stub
		
	}

	public void setFocus(boolean focused) {
		textBox.setFocus(focused);
		
	}

	public void setTabIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	public void addFocusListener(FocusListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void removeFocusListener(FocusListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void addKeyboardListener(KeyboardListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void removeKeyboardListener(KeyboardListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setFromModel(boolean fromModel) {
		this.fromModel = fromModel;
	}

	public boolean isFromModel() {
		return fromModel;
	}
}
