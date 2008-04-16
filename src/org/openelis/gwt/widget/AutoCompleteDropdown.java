package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.HashMap;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberObject;
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
		KeyboardListener, ChangeListener, ClickListener, MouseListener,
		PopupListener, FocusListener, HasFocus {
	public HorizontalPanel mainHP = new HorizontalPanel();

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

	public FocusPanel focusPanel = new FocusPanel() {
		public void onBrowserEvent(Event event) {
			if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
				if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB)
					onLostFocus(textBox);

				if (multiSelect) {
					visible = false;
					choicesPopup.hide();
				}
				screen.doTab(event, comp);
			} else {
				super.onBrowserEvent(event);
			}
		}
	};

	/**
	 * Widget used to display the suggestions and register click events.
	 */
	public ScrollList scrollList;

	HashMap idHashMap = new HashMap();

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
	
	protected int numberOfRows = 10;

	// table values
	AbstractField[] fields;

	TableCellWidget[] editors;

	String[] headers;

	int[] widths;

	public AutoCompleteDropdown() {
	}

	/**
	 * Category for which suggestions we are trying to match
	 */
	public String cat;

	private ChangeListenerCollection callback;

	private AutoCompleteServiceIntAsync autoService = (AutoCompleteServiceIntAsync) GWT
			.create(AutoCompleteServiceInt.class);

	private ServiceDefTarget target = (ServiceDefTarget) autoService;

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

	public void setCase(String fieldCase) {
		this.fieldCase = fieldCase;
	}

	/**
	 * This inner class is used to add a delay to the before calling the back
	 * end to limit the number of calls made to the server. If a second key is
	 * hit within a certain amount of time the call for suggestions is cancelled
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
				if (fieldCase.equals("upper"))
					text = text.toUpperCase();
				else if (fieldCase.equals("lower"))
					text = text.toLowerCase();
				callForMatches(text);
			}
		}
	};

	/**
	 * Contstructor for AutoCompleteTextBox
	 * 
	 * @param cat
	 *            Category is used to know which values to match the entered
	 *            text to.
	 * @param serviceUrl
	 *            This is the url to the RemoteServiceServlet that handles calls
	 *            for this widget
	 */
	public AutoCompleteDropdown(String cat, 
                                String serviceUrl,
                                boolean multi, 
                                String textBoxDefault,
                                String width,
                                String popWidth) {
        if(serviceUrl != null)
            initService(serviceUrl);
		this.cat = cat;
		this.textBoxDefault = textBoxDefault;
		this.width = width;
		this.fromModel = fromModel;
		this.multiSelect = multi;
		if (textBoxDefault != null)
			textBox.setText(textBoxDefault);

		if (width != null)
			textBox.setWidth(width);

		initWidget(mainHP);
		mainHP.add(textBox);
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
        scrollList.addChangeListener(this);
        scrollList.addStyleName("Dropdown");
        scrollList.setWidth(popWidth);
        scrollList.setMaxRows(numberOfRows);
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
		if (!textBox.isReadOnly() && choicesPopup.isVisible()) {
			if (arg1 == KEY_DOWN || arg1 == KEY_UP || arg1 == KEY_ENTER
					|| arg1 == KEY_TAB || arg1 == KEY_LEFT || arg1 == KEY_RIGHT || arg1 == KEY_ALT || 
					arg1 == KEY_CTRL || arg1 == KEY_SHIFT)
				return;

			if (arg1 == KEY_ESCAPE) {
				choicesPopup.hide();
				visible = false;
				return;
			}

			String text = textBox.getText();
			if (multiSelect) {
				if (text.length() == 0) {
					scrollList.setSelected(new ArrayList());
					scrollList.unselectAll();
					choicesPopup.hide();
					visible = false;
				} else {
					if (text.length() < textBoxDefault.length()) {
						textBox.setText(textBoxDefault);
						choicesPopup.hide();
						visible = false;
					}
				}
			} else if (text.length() > 0 && !text.endsWith("*")) {
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
                choicesPopup.setPopupPosition(this.getAbsoluteLeft(), this
                                              .getAbsoluteTop()
                                              + this.getOffsetHeight() - 1);
                choicesPopup.setWidget(scrollList);
                choicesPopup.show();
                scrollList.sizeTable();

			
			DOM.addEventPreview(scrollList);

			if (!multiSelect) {
				//if the user clicked the arrow and there is already a row selected, we need to scroll to that row
				if(clickedArrow && scrollList.getSelected().size() > 0)
                    startPos = scrollList.getDataModel().indexOf((DataSet)scrollList.getSelected().get(0));
				//if they didnt click the arrow we can assume they typed something.  First we need to unselect all rows so we can recalculate the values
				else if(!clickedArrow)
						scrollList.unselectAll();
					//if the row selected is at the bottom we need to calculate the scrolling differently
					if(((scrollList.getDataModel().size()+1)  > scrollList.getMaxRows()) && startPos > ((scrollList.getDataModel().size()+1) - scrollList.getMaxRows())){
						int newStartPos = (scrollList.getDataModel().size() - scrollList.getMaxRows());
						scrollList.scrollLoad(newStartPos * scrollList.getCellHeight());
	                    scrollList.view.scrollBar.setScrollPosition((newStartPos+1) * scrollList.getCellHeight());
					}else{						
						scrollList.scrollLoad(startPos * scrollList.getCellHeight());
						scrollList.view.scrollBar.setScrollPosition(startPos * scrollList.getCellHeight());
                    }
                    
//					if the row selected is at the bottom we need to calculate the selecting differently
                    if(cat == null && (scrollList.getDataModel().size() > scrollList.getMaxRows()) && startPos > (scrollList.getDataModel().size() - scrollList.getMaxRows()) 
                    		&& (startPos < scrollList.getDataModel().size())){
                    	scrollList.unselectAll();
                    	scrollList.setActive(scrollList.getMaxRows() - (scrollList.getDataModel().size() % startPos));
                    }else if(cat == null && startPos > (scrollList.getDataModel().size() - scrollList.getMaxRows()) 
                    		&& (startPos < scrollList.getDataModel().size())){
                    	scrollList.unselectAll();
                    	scrollList.setActive(startPos);
                    }
                   
			}else
				scrollList.scrollLoad(0);

			// we need to set the selected style name to the textbox
			textBox.removeStyleName("TextboxUnselected");
			textBox.addStyleName("TextboxSelected");

			if (!multiSelect) {
				// we need to put the text of the first item in the textbox and
				// do a selection
				currentCursorPos = textBox.getText().length();
				String firstRowDisplayText = ((String)((StringObject) scrollList
						.getDataModel().get(startPos).getObject(0)).getValue()).trim();
				if (firstRowDisplayText.toUpperCase()
						.indexOf(textBox.getText().toUpperCase()) == 0) {
					textBox.setText(firstRowDisplayText.trim());
					textBox.setSelectionRange(currentCursorPos,
							firstRowDisplayText.length() - currentCursorPos);
				}
			}
			
        if(scrollList.getSelected().size() == 0)
        	scrollList.setActive(0);
			
        visible = true;

		} else {
			visible = false;
			choicesPopup.hide();
		}
	}

	/**
	 * A mouseclick in the list of items
	 */
	public void onChange(Widget arg0) {
		if (scrollList.getActive() > -1) {
			int selectionLength = -1;
			String textValue = "";
			textValue = getTextBoxDisplay();

			DOM.removeEventPreview(scrollList);

			textBox.setText(textValue.trim());
			textBoxDefault = textValue;

			if (multiSelect && selectionLength > -1)
				textBox.setSelectionRange(0, selectionLength);

			complete();
		} else {
			choicesPopup.hide();
		}
	}

	/**
	 * Set the selection that the user made.
	 */
	protected void complete() {
		// if the textbox is filled out with nothing selected dont do anything
		// because the user is just tabbing through
		if (!"".equals(textBox.getText())
				&& scrollList.getSelected().size() == 0)
			return;

		if (scrollList == null || textBox.getText().length() == 0) {
			if (textBox.getText().equals("")) {
				textBox.setText("");
				scrollList.unselectAll();
			//	this.value = null;
			}
			// else do nothing
		} else if (scrollList.getSelected().size() > 0) {
			// get the index of the selected
            String textValue = "";
            int selectionLength = -1;
            
            textValue = getTextBoxDisplay();

			DOM.removeEventPreview(scrollList);

			textBox.setText(textValue.trim());
			textBoxDefault = textValue;

		}
		textBox.setFocus(true);
		
		currentStart = -1;
		currentActive = -1;
		if (!multiSelect) {
			visible = false;
			choicesPopup.hide();
		}
		if (callback != null)
			callback.fireChange(this);
		clickedArrow = false;
	}

	/**
	 * Method that calls the service to retrieve the suggestions
	 * 
	 * @param text
	 */
	protected void callForMatches(final String text) {
        if(cat != null){
            try {
                autoService.getMatches(cat, scrollList.getDataModel(), text, new AsyncCallback() {
                    public void onSuccess(Object result){
                        scrollList.setDataModel((DataModel)result);
                        currentActive = 0;

                        showMatches(0);
                    }
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }
                });
            } catch (RPCException e) {
                Window.alert(e.getMessage());
            }
        }else{
            getMatches(text);
        }
	}

	private void getMatches(String match) {
		DataModel model = scrollList.getDataModel();
		int tempStartPos = -1;
		int index = 0;
 
        if(cat == null)
            index = getIndexByTextValue(match);
        

		if (index > -1 && index < model.size()) {
			tempStartPos = index;
			this.startPos = index;
		}

		if (tempStartPos == -1 && !textBox.getText().equals("")) {
			// set textbox text back to what it was before
			textBox.setText(textBox.getText().substring(0, currentCursorPos));
			this.startPos = 0;
		}
		clickedArrow = false;
		showMatches(this.startPos);
	}

	public void setModel(DataModel model) {
		scrollList.setDataModel(model);
	}

	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
        scrollList.setMulti(multiSelect);
	}

    public void setSelected(ArrayList selections){
        if(cat != null){
            scrollList.setDataModel(new DataModel());
        }
    	if(selections != null){
        	scrollList.setSelected(selections);
        	
        	String textValue = "";
        	textValue = getTextBoxDisplay();
			textBox.setText(textValue.trim());
			textBoxDefault = textValue;
			
			//need to set the scroll list values back to default
			scrollList.setActive(-1);
			scrollList.setStart(0);
			currentActive= -1;
			currentStart = -1;
    	}
    }
    
	public DataModel getModel() {
		return scrollList.getDataModel();
	}

	/**
	 * I think is deprecated and should be deleted
	 * 
	 * @param callback
	 */
	public void setCallback(ChangeListenerCollection callback) {
		this.callback = callback;
	}

	/**
	 * This method will wipe the textbox and set the selection value to null
	 */
	public void reset() {
		textBoxDefault = null;
		textBox.setText("");
		scrollList.unselectAll();
		scrollList.setStart(0);
		scrollList.setActive(-1);
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
		if (!textBox.isReadOnly()) {
			if (sender == focusPanel) {
								
				clickedArrow = true;

				showMatches(0);
			}
		}
	}

	public void onMouseDown(Widget sender, int x, int y) {
		if (!textBox.isReadOnly()) {
			if (sender == focusPanel) {
				focusPanel.addStyleName("Pressed");
			}
		}
	}

	public void onMouseEnter(Widget sender) {
		if (!textBox.isReadOnly()) {
			if (sender == focusPanel) {
				focusPanel.addStyleName("Hover");
			}
		}
	}

	public void onMouseLeave(Widget sender) {
		if (!textBox.isReadOnly()) {
			if (sender == focusPanel) {
				focusPanel.removeStyleName("Hover");
			}
		}
	}

	public void onMouseMove(Widget sender, int x, int y) {
	}

	public void onMouseUp(Widget sender, int x, int y) {
		if (!textBox.isReadOnly()) {
			if (sender == focusPanel) {
				focusPanel.removeStyleName("Pressed");
			}
		}
	}

	public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
		if (clickedArrow) {
			focusPanel.removeStyleName("Selected");

			// we need to set the unselected style name to the textbox
			textBox.addStyleName("TextboxUnselected");
			textBox.removeStyleName("TextboxSelected");
			clickedArrow = false;
		}
		visible = false;
	}

	public void onFocus(Widget sender) {
		if (!textBox.isReadOnly()) {
			if (sender == textBox) {
				// we need to set the selected style name to the textbox
				textBox.addStyleName("TextboxSelected");
				textBox.removeStyleName("TextboxUnselected");
				textBox.setFocus(true);

				focusPanel.addStyleName("Selected");

				setCurrentValues();
					
			}
		}
	}

	public void onLostFocus(Widget sender) {
		if (!textBox.isReadOnly()) {
			if (sender == textBox) {
				// we need to set the unselected style name to the textbox
				textBox.addStyleName("TextboxUnselected");
				textBox.removeStyleName("TextboxSelected");

				focusPanel.removeStyleName("Selected");

				complete();
			}
		}
	}

	public ArrayList getSelected() {
		return scrollList.getSelected();
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
        scrollList.setHeaders(headers);
	}

	public void setWidths(int[] widths) {
		this.widths = widths;
        scrollList.setCellWidths(widths);
	}

	public void clear() {
		scrollList.clear();
	}

	public void addItem(String key, String display) {
		DataSet data = new DataSet();

		StringObject text = new StringObject();
		text.setValue(display.trim());
		data.addObject(text);

		StringObject id = new StringObject();
		id.setValue(key);
		data.addObject(id);

		scrollList.getDataModel().add(data);
	}

	public int getItemCount() {
		return scrollList.getDataModel().size();
	}

	public void setItemSelected(int selected, boolean isSelected) {
		scrollList.setSelected(selected);
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setForm(ScreenBase screen) {
		this.screen = screen;
	}

	public int getTabIndex() {
		return 0;
	}

	public void setAccessKey(char key) {
	}

	public void setFocus(boolean focused) {
		if (focused) {
			focusPanel.setFocus(true);
			focusPanel.setFocus(false);
		}
		textBox.setFocus(focused);
	}

	public void setTabIndex(int index) {
	}

	public void addFocusListener(FocusListener listener) {
		textBox.addFocusListener(listener);
	}

	public void removeFocusListener(FocusListener listener) {
		textBox.removeFocusListener(listener);

	}

	public void addKeyboardListener(KeyboardListener listener) {
	}

	public void removeKeyboardListener(KeyboardListener listener) {
	}

	public void setFromModel(boolean fromModel) {
		this.fromModel = fromModel;
	}

	public boolean isFromModel() {
		return fromModel;
	}

	public boolean isPopUpVisible() {
		return visible;
	}

	public void closePopup() {
		choicesPopup.hide();
	}
	
	public void setRows(int rows){
		numberOfRows = rows;
	}

	private int getIndexByTextValue(String textValue) {
		DataModel model = scrollList.getDataModel();
		int low = 0;
		int high = model.size() - 1;
		int mid = -1;
		int length = textValue.length();

		//we first need to do a binary search to 
		while (low <= high) {
			mid = (low + high) / 2;

			if (((String) model.get(mid).getObject(0).getValue()).substring(0,length).compareTo(textValue) < 0)
				low = mid + 1;
			else if (((String) model.get(mid).getObject(0).getValue()).substring(0,length).compareTo(textValue) > 0)
				high = mid - 1;
			else
				break;
		}
		
		if(low > high)
			return -1; // NOT FOUND
		else{
			//we need to do a linear search backwards to find the first entry that matches our search
			while(((String) model.get(mid).getObject(0).getValue()).substring(0,length).compareTo(textValue) == 0)
				mid--;
			
			return (mid+1);
		}
	}

	private String getTextBoxDisplay(){
		String textValue = "";
		ArrayList selected = scrollList.getSelected();
		
		for(int i=0;i<selected.size();i++){
			if(selected.get(i) instanceof DataSet){
				 DataSet select = (DataSet)selected.get(i);
				 textValue = (String) ((StringObject) select.getObject(0)).getValue()
				 				+ (!"".equals(textValue) ? "|" : "") + textValue;
			}else{
				NumberObject select = (NumberObject)selected.get(i);
				
				String tempTextValue = (String)((DataObject)((DataSet)scrollList.getDataModel().get(select)).getObject(0)).getValue();
				
				textValue = tempTextValue + (!"".equals(textValue) ? "|" : "") + textValue;
			}
               
		}	
		return textValue;
	}
	
	private void setCurrentValues(){
		if (currentStart == -1)
			currentStart = scrollList.getStart();

		if (currentActive == -1)
			currentActive = scrollList.getActive();

		// if this is true then we need to find these values manually
		if (currentActive == -1 && currentStart == 0 && scrollList.getSelected().size() > 0 && !multiSelect) {
       
          int index = scrollList.getDataModel().indexOf((DataSet)scrollList.getSelected().get(0));
          if(index > -1){
                currentStart = index;
                currentActive = 0;
                scrollList.setActive(0);
           }
          
		}

	}
}
