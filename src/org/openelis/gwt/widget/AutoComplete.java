/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.ui.common.data.QueryData;
import org.openelis.gwt.event.BeforeGetMatchesEvent;
import org.openelis.gwt.event.BeforeGetMatchesHandler;
import org.openelis.gwt.event.GetMatchesEvent;
import org.openelis.gwt.event.GetMatchesHandler;
import org.openelis.gwt.event.HasBeforeGetMatchesHandlers;
import org.openelis.gwt.event.HasGetMatchesHandlers;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRenderer;
import org.openelis.gwt.widget.table.TableView;
import org.openelis.gwt.widget.table.TableView.VerticalScroll;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;


/**
 * This widget class implements a textbox that listens to keystrokes and will fire a GetMatchesEvent to a
 * registered handler that will return a list of suggestions that are displayed in a dropdown window for the
 * user to choose from.
 * 
 * @author tschmidt
 *
 * @param <T>
 *        generic parameter used to define the Type of Key to used by the widget.  
 */
public class AutoComplete<T> extends DropdownWidget implements FocusHandler, BlurHandler, HasValue<T>, HasBeforeGetMatchesHandlers, HasGetMatchesHandlers, HasMouseOutHandlers, HasMouseOverHandlers {
    
    private AutoCompleteListener listener = new AutoCompleteListener(this);
    private Field<T> field;
    private boolean enabled;
    IconContainer icon = new IconContainer();
    public String dropwidth;
    public int minWidth;
    HorizontalPanel hp; 
    private int delay = 350;
    
    private class AutoCompleteListener implements KeyUpHandler, ClickHandler {
        
        private AutoComplete widget;
       
        
        public AutoCompleteListener(AutoComplete widget){
            this.widget = widget;
        }

        /**
         * Catches key events from the AutoComplete widget and determines if getMatches should be called.  We set a delay so
         * that if multiple key strokes are entered quickly we don't call getMathces with calls that will most likely be never seen by the 
         * user. 
         */
        public void onKeyUp(KeyUpEvent event) {
            if(widget.queryMode)
                return;
            if (!widget.textbox.isReadOnly()) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN || event.getNativeKeyCode() == KeyCodes.KEY_UP ||  event.getNativeKeyCode() == KeyCodes.KEY_TAB 
                        || event.getNativeKeyCode() == KeyCodes.KEY_LEFT || event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || event.getNativeKeyCode() == KeyCodes.KEY_ALT || 
                        event.getNativeKeyCode() == KeyCodes.KEY_CTRL || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE)
                    return;
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !widget.popup.isShowing() && !widget.itemSelected && widget.focused){
                     widget.showTable();
                    return;
                }
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && widget.itemSelected){
                    widget.itemSelected = false;
                    return;
                }
                String text = widget.textbox.getText();
                if (text.length() > 0 && !text.endsWith("*")) {
                    widget.setDelay(text,delay);
                } else {
                    widget.hideTable();
                }
            }
        }

        /**
         * Call getMatches with whatever is in the the textbox to show the model
         * If empty then will return the top of the listed sorted alphabetically
         */
		public void onClick(ClickEvent event) {
			try {
				getMatches(textbox.getText());
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}

    }

    private class Handler implements MouseOutHandler, MouseOverHandler {
    	
    	AutoComplete<T> source;
    	
    	public Handler(AutoComplete<T> source) {
    		this.source = source;
    	}

		public void onMouseOut(MouseOutEvent event) {
			//MouseOutEvent.fireNativeEvent(event.getNativeEvent(), source);
			
		}

		public void onMouseOver(MouseOverEvent event) {
			//MouseOverEvent.fireNativeEvent(event.getNativeEvent(), source);
			
		}
    	
    }
    
    Handler mouseHandler = new Handler(this);
    
    /**
     * Default No-Arg constructor
     */
    public AutoComplete() {
    
    }
    
    /**
     * This method is called to setup the widget after all options are set into the widget
     * That is why this code is not located in a constructor.
     */
    public void init() {
    	if(maxRows == 0)
    		maxRows = 10;
        renderer = new TableRenderer(this);
        view = new TableView(this,showScroll);
        view.setWidth(width);
        keyboardHandler = this;
        hp = new HorizontalPanel();
        hp.add(textbox);
        hp.add(icon);
        setWidget(hp);
        hp.setWidth(width);
        setStyleName("AutoDropdown");
        textbox.setStyleName("TextboxUnselected");
        textbox.addFocusHandler(this);
        textbox.addBlurHandler(this);
        popup.setStyleName("DropdownPopup");
        popup.setWidget(view);
        popup.addCloseHandler(this);
        textbox.addKeyUpHandler(listener);
        textbox.setReadOnly(!enabled);
        textbox.addMouseOutHandler(mouseHandler);
        textbox.addMouseOverHandler(mouseHandler);
        icon.setStyleName("AutoDropdownButton");
        icon.addClickHandler(listener);
        icon.addFocusHandler(this);
        addDomHandler(keyboardHandler,KeyDownEvent.getType());
        addDomHandler(keyboardHandler,KeyUpEvent.getType());
    }
    
    /**
     * Sinks a KeyPressEvent for this widget attaching a TabHandler that will override the default
     * browser tab order for the tab order defined by the screen for this widget.
     * 
     * @param handler
     *        Instance of TabHandler that controls tabing logic for widget.
     */
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    /**
     * Call this function with true to enable the widget and false to disable.
     * 
     * @param enabled
     */
    public void enable(boolean enabled) {
        this.enabled = enabled;
        textbox.setReadOnly(!enabled);
        icon.enable(enabled);
        //super.enable(enabled);
    }
    
    /**
     * Method that returns true if the widget is currently enabled for use.
     */
    public boolean isEnabled() {
    	return enabled;
    }
    
    /**
     *  Overrides the getMatches method from the super DropdownWidget to fire the getMatches events.
     *  
     *  If a handler is registered, a BeforeGetMatchesEvent will be fired and the the cancel flag will
     *  be checked before firing the GetMatchesEvent.
     *  
     *  Will throw an exception if no handler is registered to the widget.
     */
    protected void getMatches(final String text) throws Exception {
        if(!field.queryMode) {
            try {
            	if(getHandlerCount(BeforeGetMatchesEvent.getType()) > 0) {
            		BeforeGetMatchesEvent event = BeforeGetMatchesEvent.fire(this, text);
            		if(event.isCancelled())
            			return;
            	}
            	if(getHandlerCount(GetMatchesEvent.getType()) > 0)
            		GetMatchesEvent.fire(this, text);
            	else
            		throw new Exception("No GetMatchesHandler registered to AutoComplete Widget");

            } catch (Exception e) {
            	e.printStackTrace();
                Window.alert(e.getMessage());
            }
        }
    }
    
    /**
     * Displays the suggestions supplied from a GetMatchesEvent Handler.
     * 
     * @param data
     *        A model for the DropdownWidget to display for matching suggestions.
     */
    public void showAutoMatches(ArrayList<TableDataRow> data){
    	selectedRow = -1;
    	selectedCol = -1;
    	if(data == null || data.size() == 0){
    		data = new ArrayList<TableDataRow>();
    		data.add(new TableDataRow(null,""));
    	}
    	load(data);
    	if(textbox.getStyleName().indexOf("Focus") > -1){
    		selectRow(0);
    		view.setHeight(Math.min(maxRows, data.size())*cellHeight);
    		showTable();
    	}else if(data != null && data.size() > 0){
    		setValue((T)data.get(0).key,true);
    		field.checkValue(this);
    		field.drawExceptions(this);
    	}else{
    		setSelection(null,"");
    		field.checkValue(this);
    		field.drawExceptions(this);
    		if(fireEvents)
    			ValueChangeEvent.fire(this, null);
    	}
    	
    }
        
    /**
     * Loads a default model to the widget to be used for displaying an initial value before the user
     * has used the widget.
     * @param model
     */
    public void setModel(ArrayList<TableDataRow> model){
        this.load(model);
    }
    
    /**
     * Returns the key value for the selected option in the current model of suggestions for the widget.
     */
    public T getValue() {
        if(getSelectedRow() > -1)
            return (T)getRow(getSelectedRow()).key;
        else
            return null;
    }

    /**
     * Sets the current value of the widget.  Does not fire ValueChangeEvent.
     */
    public void setValue(T value) {
        setValue(value,false);
    }

    /**
     * Sets the current value of the widget and fires a ValueChangeEvent to any registered handlers
     * if the fireEvents param is passed as true.
     */
    public void setValue(T value, boolean fireEvents) {
        T old = field.getValue();
        setSelection(value);
        field.setValue(value);
        if(fireEvents) 
           ValueChangeEvent.fireIfNotEqual(this, old, value);
    }

    /**
     * This method takes a key, value pair and will create a default model with a TableDataRow with those values
     * and sets the value the widget to that row.  Used when displaying data returned from a fetch call.
     * 
     * @param key
     *       Key value for the selected row 
     * @param display
     *       Display text for the selected row
     */
    public void setSelection(T key, String display) {
    	ArrayList<TableDataRow> model = new ArrayList<TableDataRow>();
    	model.add(new TableDataRow(key,display));
    	setModel(model);
    	setSelection(key);
    	field.setValue(key);
    }
    
    /**
     * This method takes a TableDataRow and will create a default model with that TableDataRow 
     * and sets the value of he widget to that row.  Used mostly when displaying values when the widget 
     * is used in a table.
     * 
     */
    public void setSelection(TableDataRow row) {
    	ArrayList<TableDataRow> model = new ArrayList<TableDataRow>();
    	model.add(row);
    	setModel(model);
    	setSelection(row.key);
    	field.setValue((T)row.key);
    }
    
    /**
     * Registers ValueChangeHandler to the widget
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler,ValueChangeEvent.getType());
    }
    
    /**
     * Registers a handler to widgets field to get notified when its field value changes.
     */
    public HandlerRegistration addFieldValueChangeHandler(
    		ValueChangeHandler handler) {
    	return addValueChangeHandler(handler);
    }

    /**
     * Adds a an error to the widget.  An error style is added to the widget and on MouseOver a popup will 
     * be displayed with errors.
     */
    public void addException(Exception error) {
        field.addException(error);
        field.drawExceptions(this);
    }
	
	/**
	 * Clears the error list and removes the error style from the widget
	 */
	public void clearExceptions() {
		field.clearExceptions(this);
	}

	/**
	 * Returns the Field used by this widget to hold errors and validate data
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Sets the Field to be used by this widget to display erorros, validate data, and format the
	 * output of the widget if necessary.
	 */
	public void setField(Field field) {
		this.field = field;
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}
	
	/**
	 * Receives notice when this widget is focused and adds the Focus style.
	 */
    public void onFocus(FocusEvent event) {
    	if(isEnabled())
    		textbox.addStyleName("Focus");
    }

    /**
     * Receives notice when this widget loses focus.  Removes the focus style and fires ValuChangeEvent to handlers.
     */
    public void onBlur(BlurEvent event) {
    	textbox.removeStyleName("Focus");
    	if(!showingOptions && isEnabled() && !field.queryMode){
	    	if("".equals(textbox.getText()) && field.getValue() != null){
    			setSelection(null,"");
    			ValueChangeEvent.fire(this, null);
    		}else{
    			setValue(getValue(),true);
    		}
    		checkValue();
    	}
    }
    
    @Override
    public void setFocus(boolean focus) {
    	textbox.setFocus(focus);
    	if(getSelection() != null) {
    		if(getSelection().display != null) {
    			if(columns.get(0).getColumnWidget() instanceof Dropdown){
        			((Dropdown)columns.get(0).getColumnWidget()).setValue(getSelection().getCells().get(0));
        			textbox.setText(((Dropdown)columns.get(0).getColumnWidget()).getTextBoxDisplay());
        		}else {
        			String textValue = "";
        			textValue = (String)getSelection().getCells().get(0)
        			+ (!"".equals(textValue) ? "|" : "") + textValue;
        			textbox.setText(textValue);
        		}
    		}
    	}
    }
    
	@Override
	public void setWidth(String width) {
		if(hp != null && width != null)
			hp.setWidth(width);
		int index = width.indexOf("px");
		int wid = 0;
		if(index > 0)
			wid = Integer.parseInt(width.substring(0,index)) - 16;
		else
			wid = Integer.parseInt(width) - 16;
		if(wid+16 > minWidth)
			dropwidth = (wid+16)+"px";
		else
			dropwidth = minWidth+"px";
		//view.setWidth(dropwidth);
		super.setWidth(wid+"px");
	}

    /**
     * Puts the field into Query Mode to be used to accept query strings instead of normal data.
     */
	public void setQueryMode(boolean query) {
		if(query == field.queryMode)
			return;
		load(null);
		field.setQueryMode(query);	
			
	}
	
	/**
	 * called to validate the current value set to the widget and will display error if necessary.
	 */
	@Override
	public void checkValue() {
		if(!field.queryMode)
			field.checkValue(this);
	}
	
	/**
	 * If the widget is in Query mode and the queryString value is not null, a QueryData object will be created 
	 * and added to the list passed into the method.  
	 */
	public void getQuery(ArrayList list, String key) {
		if(!field.queryMode)
			return;
		if(textbox.getText() != null && !textbox.getText().equals("")){
			QueryData qd = new QueryData();
			qd.setKey(key);
			qd.setQuery(textbox.getText());
			qd.setType(QueryData.Type.STRING);
			list.add(qd);
		}
	}
	
	/**
	 * The current list of error for this widget.
	 */
	@Override
	public ArrayList<Exception> getExceptions() {
		return field.exceptions;
	}
    
	/**
	 * Returns the curent value in the field for this widget.
	 */
	public Object getFieldValue() {
		return getValue();
	}
	
	/**
	 * Sets the value of the field to the object passed in the method.
	 */
	public void setFieldValue(Object value) {
		if(field.queryMode)
			field.setStringValue((String)value);
		else
			setValue((T)value);
	}
	
	@Override
	/**
	 * Sets the value of the widget to the chosen value by the user in the dropdown 
	 * and closes the popup.
	 */
	public void complete() {
		super.complete();
		//field.setValue(getValue());
		clearExceptions();
		//ValueChangeEvent.fire(this, getValue());
		//checkValue();
		textbox.setFocus(true);
		
	}
	
	/**
	 * Registers a BeforeGetMatchesHandler to the widget.
	 */
	public HandlerRegistration addBeforeGetMatchesHandler(BeforeGetMatchesHandler handler) {
		return addHandler(handler, BeforeGetMatchesEvent.getType());
	}
	
	/**
	 * Registers a GetMatchesHandler to the widget.
	 */
	public HandlerRegistration addGetMatchesHandler(GetMatchesHandler handler) {
		return addHandler(handler,GetMatchesEvent.getType());
	}
	
    @Override
    public void addExceptionStyle(String style) {
    	textbox.addStyleName(style);
    }
	
    @Override
    public void removeExceptionStyle(String style) {
    	textbox.removeStyleName(style);
    }
    
    @Override
    public Object getWidgetValue() {
    	return getValue();
    }
    
    public void setDelay(int delay) {
    	this.delay = delay;
    }
}
