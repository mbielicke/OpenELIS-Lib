/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.FormErrorException;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.services.AutoCompleteServiceInt;
import org.openelis.gwt.services.AutoCompleteServiceIntAsync;
import org.openelis.gwt.widget.table.TableAutoDropdown;
import org.openelis.gwt.widget.table.TableCellWidget;

import java.util.ArrayList;
import java.util.HashMap;

public class AutoCompleteDropdown extends Composite implements
		KeyboardListener, ChangeListener, ClickListener, MouseListener,
		PopupListener, FocusListener, HasFocus, SourcesChangeEvents {
	public HorizontalPanel mainHP = new HorizontalPanel();
    
    private Request lastRequest;

	public TextBox textBox = new TextBox();

	public FocusPanel focusPanel = new FocusPanel();

	/**
	 * Widget used to display the suggestions and register click events.
	 */
	public ScrollList scrollList;

	HashMap idHashMap = new HashMap();

	public final PopupPanel choicesPopup = new PopupPanel(true) {
        
        public boolean onEventPreview(Event event){
            if(DOM.eventGetType(event) == Event.ONKEYDOWN){
                if(DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB || DOM.eventGetKeyCode(event) == KeyboardListener.KEY_ENTER){
                    if(insideTable){  
                        //complete();
                        ((AppScreen)screen).onEventPreview(event);
                        return true;
                    }
                }
            }
            return super.onEventPreview(event);
            
        }
	    public boolean onKeyDownPreview(char key, int modifiers) {
	        // TODO Auto-generated method stub
            scrollList.onKeyDown(this, key, modifiers);      
	        return super.onKeyDownPreview(key, modifiers);
	    }   
    };

	protected String textBoxDefault = "";

	protected boolean popupAdded = false;

	public boolean visible = false;

	protected String popupHeight = "";

	private ScreenBase screen;

	protected Widget comp;

	protected String width = "";
    
    protected String popWidth = "";

	protected String fieldCase = "mixed";

	protected int currentCursorPos = 0;

	protected String type = "integer";

	protected int startPos = 0;

	private boolean clickedArrow = false;

	private int currentStart = -1;

	private int currentActive = -1;

	protected boolean multiSelect = false;

	protected boolean fromModel = false;
	
	protected boolean insideTable = false;
	
	protected int numberOfRows = 10;
    
    protected AutoCompleteParamsInt autoParams = null;
    
    protected boolean linear = false;

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

	private ChangeListenerCollection changeListeners;

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
        init(cat,serviceUrl,multi,textBoxDefault,width,popWidth);
    }
    
    public void init(String cat, 
                     String serviceUrl,
                     boolean multi, 
                     String textBoxDefault,
                     String width,
                     String popWidth){ 
        if(serviceUrl != null)
            initService(serviceUrl);
		this.cat = cat;
		this.textBoxDefault = textBoxDefault;
		this.width = width;
        this.popWidth = popWidth;
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
			if (arg1 == KEY_DOWN || arg1 == KEY_UP || arg1 == KEY_TAB || arg1 == KEY_ENTER
					|| arg1 == KEY_LEFT || arg1 == KEY_RIGHT || arg1 == KEY_ALT || 
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
                scrollList.active = true;
               
                scrollList.sizeTable();
                
                //we need to set the popup width if it is a dropdown and the width is auto.
                //we leave the width auto if it is an auto complete
                if(cat == null && "auto".equals(popWidth)){
                    if(scrollList.getDataModel().size() > numberOfRows){  //we need a scrollbar
                        int textBoxWidth = new Integer((width.substring(0, width.indexOf("px")))).intValue();
                        if(getParent() instanceof TableAutoDropdown)
                            scrollList.setCellWidths(new int[] {(textBoxWidth-24)});
                        else
                            scrollList.setCellWidths(new int[] {(textBoxWidth-8)});
                    }else{  //we dont need a scroll bar
                        int textBoxWidth = new Integer((width.substring(0, width.indexOf("px")))).intValue();
                        if(getParent() instanceof TableAutoDropdown)
                            scrollList.setCellWidths(new int[] {(textBoxWidth-6)});
                        else
                            scrollList.setCellWidths(new int[] {(textBoxWidth+10)});
                    }
                }

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
                    }else{
                        scrollList.unselectAll();
                        scrollList.setActive(0);
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
			
        if(scrollList.getSelected().size() == 0 && cat!=null && startPos == 0)
        	scrollList.setActive(0);
			
        visible = true;

		} else {
			visible = false;
			
            focusPanel.addStyleName("Selected");
            
            HorizontalPanel hp = new HorizontalPanel();
            hp.setStyleName("Form");
            
            Label text = new Label();
            text.setStyleName("Prompt");
            text.setText("No records found");
            
            hp.add(text);

            choicesPopup.clear();
            choicesPopup.addStyleName("AutoCompletePopup");
            choicesPopup.addPopupListener(this);
            choicesPopup.setPopupPosition(this.getAbsoluteLeft(), this
                                          .getAbsoluteTop()
                                          + this.getOffsetHeight() - 1);
            choicesPopup.setWidget(hp);
            choicesPopup.show();
            scrollList.active = true;
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
	public void complete() {
		// if the textbox is filled out with nothing selected we need to clear the textbox
		// the selected list will always have something in it if it is valid text
		if (!"".equals(textBox.getText())
				&& scrollList.getSelected().size() == 0){
			textBox.setText("");
			return;
		}

		if (scrollList == null) {// || textBox.getText().length() == 0) {
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
		if (changeListeners != null)
			changeListeners.fireChange(this);
		clickedArrow = false;
	}

	/**
	 * Method that calls the service to retrieve the suggestions
	 * 
	 * @param text
	 */
	protected void callForMatches(final String text) {
        if(cat != null){
            if(lastRequest != null && lastRequest.isPending())
                lastRequest.cancel();
          
            if(screen != null)
                ((AppScreen)screen).window.setStatus("", "spinnerIcon");
        
            try {
                HashMap params = null;
                if(autoParams != null){
                    params = autoParams.getParams(screen.rpc);
                }
                lastRequest = autoService.getMatches(cat, scrollList.getDataModel(), text, params, new AsyncCallback() {
                    public void onSuccess(Object result){
          
                        scrollList.setDataModel((DataModel)result);
                        currentActive = 0;

                        showMatches(0);
                        
                        if(screen != null)
                            ((AppScreen)screen).window.setStatus("", "");
                    }
                    
                    public void onFailure(Throwable caught) {
                        if(caught instanceof FormErrorException){
                            ((AppScreen)screen).window.setStatus(caught.getMessage(), "ErrorPanel");
                        }else
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
            if(cat == null)
                index = getIndexByTextValue(textBox.getText()); 

            if (index > -1 && index < model.size()) {
                tempStartPos = index;
                this.startPos = index;
            }else{
                textBox.setText("");
                tempStartPos = 0;
                return;
            }
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
    
    public void setSelectedIndex(int index) {
        scrollList.setSelected(index);
    }
    
    public Object getSelectedValue() {
        ArrayList<DataSet> set = scrollList.getSelected();
        if(set == null)
            return null;
        return set.get(0).getKey().getValue();
    }
    
	public DataModel getModel() {
		return scrollList.getDataModel();
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
                ((HasFocus)sender).setFocus(false);
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
        scrollList.active = false;
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

				//complete();
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
    
    public void setLinear(boolean linear){
        this.linear = linear;
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
        if(textValue.equals(""))
            return -1;
		DataModel model = scrollList.getDataModel();
		int low = 0;
		int high = model.size() - 1;
		int mid = -1;
		int length = textValue.length();
        
        if(linear){
            for(int i = 0; i < model.size(); i++){
                if(((String) model.get(i).getObject(0).getValue()).substring(0,length).toUpperCase().compareTo(textValue.toUpperCase()) == 0)
                    return i;
            }
            return -1;
        }else{
            //we first need to do a binary search to 
            while (low <= high) {
                mid = (low + high) / 2;

                if (((String) model.get(mid).getObject(0).getValue()).substring(0,length).toUpperCase().compareTo(textValue.toUpperCase()) < 0)
                    low = mid + 1;
                else if (((String) model.get(mid).getObject(0).getValue()).substring(0,length).toUpperCase().compareTo(textValue.toUpperCase()) > 0)
                    high = mid - 1;
                else
                    break;
            }
		
            if(low > high)
                return -1; // NOT FOUND
            else{
                //we need to do a linear search backwards to find the first entry that matches our search
                while(mid > -1 && ((String) model.get(mid).getObject(0).getValue()).substring(0,length).toUpperCase().compareTo(textValue.toUpperCase()) == 0)
                    mid--;
			
                return (mid+1);
            }
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

	public void setInsideTable(boolean insideTable) {
		this.insideTable = insideTable;
	}

    public void addChangeListener(ChangeListener listener) {
        if(changeListeners == null){
            changeListeners = new ChangeListenerCollection();
        }
        changeListeners.add(listener);
        
    }

    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null){
            changeListeners.remove(listener);
        }
        
    }
    
    public void setAutoParams(AutoCompleteParamsInt autoParams){
        this.autoParams = autoParams;
    }
}
