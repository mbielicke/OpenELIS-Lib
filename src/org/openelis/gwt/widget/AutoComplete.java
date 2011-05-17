/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.event.BeforeGetMatchesEvent;
import org.openelis.gwt.event.BeforeGetMatchesHandler;
import org.openelis.gwt.event.GetMatchesEvent;
import org.openelis.gwt.event.GetMatchesHandler;
import org.openelis.gwt.event.HasBeforeGetMatchesHandlers;
import org.openelis.gwt.event.HasGetMatchesHandlers;
import org.openelis.gwt.widget.table.Row;
import org.openelis.gwt.widget.table.Table;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * This class is used by OpenELIS Screens to display and input values in forms
 * and in table cells as an AutoComplete list selector. This class exteds TextBox
 * which implements the ScreenWidgetInt and we override this implementation
 * where needed.
 * 
 * @param <T>
 */
public class AutoComplete extends TextBox<AutoCompleteValue> implements HasGetMatchesHandlers,HasBeforeGetMatchesHandlers {
    /**
     * Used for AutoComplete display
     */
    protected HorizontalPanel hp;
    protected Button          button;
    protected Table           table;
    protected PopupPanel      popup;
    protected int             cellHeight = 21, delay = 350, itemCount = 10, width;
    protected Timer           timer;
    protected boolean         keepPopup;
    protected String          prevText;

    final AutoComplete        source;

    /**
     * Instance of the Renderer interface. Initially set to the DefaultRenderer
     * implementation.
     */
    protected Renderer        renderer   = new DefaultRenderer();

    /**
     * Public Interface used to provide rendering logic for the Dropdown display
     * 
     */
    public interface Renderer {
        public String getDisplay(Row row);
    }

    /**
     * Default no-arg constructor
     */
    public AutoComplete() {
        /*
         * Final instance used in Anonymous handlers.
         */
        source = this;
    }

    /**
     * Init() method overrriden from TextBox to draw the Dropdown correctly.
     * Also set up handlers for click and key handling
     */
    @Override
    public void init() {

        /*
         * Final instance of the private class KeyboardHandler
         */
        final KeyboardHandler keyHandler = new KeyboardHandler();

        hp = new HorizontalPanel();
        textbox = new com.google.gwt.user.client.ui.TextBox();
        /*
         * New constructor in Button to drop the border and a div with the
         * passed style.
         */
        button = new Button();
        AbsolutePanel image = new AbsolutePanel();
        image.setStyleName("AutoDropdownButton");
        button.setDisplay(image, false);

        hp.add(textbox);
        hp.add(button);

        /*
         * Sets the panel as the wrapped widget
         */
        initWidget(hp);

        setStyleName("AutoDropDown");
        textbox.setStyleName("TextboxUnselected");

        /*
         * Since HorizontalPanel is not a Focusable widget we need to listen to
         * the textbox focus and blur events and pass them through to the
         * handlers registered to source.
         */
        textbox.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                FocusEvent.fireNativeEvent(event.getNativeEvent(), source);
            }
        });

        textbox.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                Item<Integer> item;

                BlurEvent.fireNativeEvent(event.getNativeEvent(), source);

                item = getSelectedItem();

                if (item != null)
                    setValue(item.key, renderer.getDisplay(item));
            }
        });

        /*
         * Register click handler to button to show the popup table
         */
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            	BeforeGetMatchesEvent bgme;
            	
            	bgme = BeforeGetMatchesEvent.fire(source, getText());
            	if(bgme != null && bgme.isCancelled())
            		return;
            	
                GetMatchesEvent.fire(source, getText());
                /*
                 * Call showPopup because the textbox will have lost focus so
                 * showMatches will not call.
                 */
                showPopup();
                textbox.setFocus(true);
            }
        });

        /*
         * Registers the keyboard handling this widget
         */
        addHandler(keyHandler, KeyDownEvent.getType());
        addHandler(keyHandler, KeyUpEvent.getType());
        
        timer = new Timer() {
            public void run() {
            	BeforeGetMatchesEvent bgme;
                int cursorPos;
                String text;
                
                text = getText();
                
            	bgme = BeforeGetMatchesEvent.fire(source, getText());
            	if(bgme != null && bgme.isCancelled())
            		return;
                
                GetMatchesEvent.fire(source, text);

                cursorPos = text.length();

                setSelectedIndex(0);

                textbox.setSelectionRange(cursorPos, getText().length() - cursorPos);
                
            }
        };

    }

    /**
     * This method will display the table set as the PopupContext for this
     * Dropdown. Will create the Popup and initialize the first time if null. We
     * also call scrollToVisible() on the table to make sure the selected value
     * is in the current table view.
     */
    protected void showPopup() {

        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setStyleName("DropdownPopup");
            popup.setWidget(table);
            popup.setPreviewingAllNativeEvents(false);
            popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                public void onClose(CloseEvent<PopupPanel> event) {
                    Item<Integer> item;
                    /*
                     * Call set value if user arrowed down to select and clicked
                     * to another widget to close the Popup.
                     */
                    item = getSelectedItem();
                    if (event.isAutoClosed() && item != null) {
                        setValue(item.key, renderer.getDisplay(item));
                    }
                }
            });
        }

        if (popup.isShowing())
            return;

        popup.showRelativeTo(this);

        /*
         * Scroll if needed to make selection visible
         */
        if (getSelectedIndex() > 0)
            table.scrollToVisible(getSelectedIndex());
    }

    /**
     * Method called by various event handlers to set the displayed text for the
     * selected row in the table without firing value change events to the end
     * user.
     */
    protected void setDisplay() {
        textbox.setText(renderer.getDisplay(getSelectedItem()));
    }

    @Override
    public void setWidth(String w) {        
        width = Util.stripUnits(w);

        /*
         * Set the outer panel to full width;
         */
        if (hp != null)
            hp.setWidth(width+"px");

        /*
         * set the Textbox to width - 16 to account for button.
         */
        
        textbox.setWidth((width - 16) + "px");
        
        //if(table != null) 
        //    table.setWidth(width+"px");

    }
    
    public int getWidth() {
        return width;
    }
    
    @Override
    public void setHeight(String height) {
        textbox.setHeight(height);
        button.setHeight(height);
    }
    
    public void setVisibleItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    

    // ******* End User Dropdown methods ***********************
    /**
     * Allows the end user to override the DefaultRenderer with a custom
     * Renderer.
     * 
     * @param renderer
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Sets the Table definition to be used as the PopupContext for this
     * Dropdown. Will set the isDropdown flag in the Table so the correct
     * styling is used.
     * 
     * @param tree
     */
    public void setPopupContext(Table tableDef) {
        this.table = tableDef;
        table.setVisibleRows(itemCount);
        table.setFixScrollbar(false);
        
        /*
         * This handler will will cancel the selection if the item has been
         * disabled.
         */
        table.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @SuppressWarnings("unchecked")
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                if ( !((Item<Integer>)table.getModel().get(event.getItem())).isEnabled())
                    event.cancel();
            }
        });

        /*
         * This handler will catch the events when the user clicks on rows in
         * the table.
         */
        table.addSelectionHandler(new SelectionHandler<Integer>() {
            public void onSelection(SelectionEvent<Integer> event) {
                if(!keepPopup)
                    popup.hide();
                
                setDisplay();

                /*
                 * Set the focus back to the Textbox after closing or nothing
                 * will be focused
                 */
                textbox.setFocus(true);
            }
        });
    }
    
    /**
     * Returns the Table being used by this Autocomplete to show matches. Note
     * that Autocomplete always uses a table even for one column.
     * @return
     */
    public Table getPopupContext() {
        return table;
    }

    /**
     * Sets the data model for the PopupContext of this widget.
     * 
     * @param model
     */
    public void setModel(ArrayList<Item<Integer>> model) {
        assert table != null;
        
        table.setVisibleRows(Math.min(model.size(),itemCount));        
        table.setModel(model);
        
    }

    /**
     * Returns the model used in the table
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Item<Integer>> getModel() {
        return (ArrayList<Item<Integer>>)table.getModel();
    }

    /**
     * Sets the selected row using its overall index in the model. This method
     * will also cause a ValueChangeEvent to be fired.
     * 
     * @param index
     */
    public void setSelectedIndex(int index) {
        keepPopup = true;
        table.selectRowAt(index);
        keepPopup = false;
        if(getSelectedIndex() > -1)
            textbox.setText(renderer.getDisplay(getSelectedItem()));
        else
            textbox.setText("");
    }

    /**
     * Returns the overall index of the selected row in the model
     * 
     * @return
     */
    public int getSelectedIndex() {
        return table.getSelectedRow();
    }

    /**
     * Returns the currently selected TableDataRow in the Table
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Item<Integer> getSelectedItem() {
        if(table == null || table.getModel() == null || getSelectedIndex() < 0)
            return null;
        return (Item<Integer>)table.getModel().get(getSelectedIndex());
    }

    /**
     * Returns the string currently displayed in the textbox portion of the
     * widget.
     * 
     * @return
     */
    public String getDisplay() {
        return getText();
    }

    /**
     * Sets the amount of time the widget should wait in milliseconds before
     * firing a a GetMatchesEvent.
     * 
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    // ********** Methods Overridden in the ScreenWidetInt ****************

    /**
     * Method overridden from TextBox to enable the button and table as well as
     * the textbox.
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() == enabled)
            return;
        button.setEnabled(enabled);
        table.setEnabled(enabled);
        if (enabled)
            sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        else
            unsinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        super.setEnabled(enabled);
    }
    
    @Override
    public void setQueryMode(boolean query) {
    	if(query == queryMode)
    		return;
    	super.setQueryMode(query);
    	if(query)
    		unsinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
    	else if(isEnabled())
    		sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
    }

    /**
     * Method used to set an Autocomplete from OnDataChangeEvents from the
     * screen.
     * 
     * @param value
     * @param display
     */
    public void setValue(Integer value, String display) {
        setValue(new AutoCompleteValue(value,display),false);
    }
    
    public void setValue(AutoCompleteValue av) {
        setValue(av,false);
    }

    /**
     * Overridden method to set the T value of this widget. Will fire a value
     * change event if fireEvents is true and the value is changed from its
     * current value
     */
    @Override
    public void setValue(AutoCompleteValue value, boolean fireEvents) {

        if(!Util.isDifferent(this.value, value))
            return;

        if (value != null) {
            textbox.setText(value.getDisplay());
        } else {
            table.selectRowAt( -1);
            textbox.setText("");
        }

        this.value = value;
        
        table.setModel(null);

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    };

    /**
     * Overridden method of TextBox to check if the Dropdown is valid
     */
    @Override
    protected void validateValue(boolean fireEvents) {
        Item<Integer> item;
        validateExceptions = null;
        
        item = getSelectedItem();
        if (item != null)
            setValue(new AutoCompleteValue(item.key, renderer.getDisplay(item)),fireEvents);
        
        if (required && value == null) {
            addValidateException(new LocalizedException("gen.fieldRequiredException"));
        }
        ExceptionHelper.checkExceptionHandlers(this);
    }

    /**
     * This method is called by the GetMatchesHandler to show the results of the
     * matching
     * 
     * @param model
     */
    public void showAutoMatches(ArrayList<Item<Integer>> model) {
        setModel(model);
        /*
         * Call showPopup only if the textbox still has focus. Otherwise the
         * user has moved on from the widget so the first entry in the results
         * will be selected and displayed
         */
        if (textbox.getStyleName().indexOf("Focus") > -1)
            showPopup();
    }
    
	/**
	 * This method will register the passed BeforeGetMatchesHandler to this widget.
	 * @param handler
	 * @return
	 */
    public HandlerRegistration addBeforeGetMatchesHandler(BeforeGetMatchesHandler handler) {
		return addHandler(handler,BeforeGetMatchesEvent.getType());
	}

    /**
     * This method will register the passed GetMatchesHandler to this widget.
     */
    public HandlerRegistration addGetMatchesHandler(GetMatchesHandler handler) {
        return addHandler(handler, GetMatchesEvent.getType());
    }

    // ********** Table Keyboard Handling ****************************

    protected class KeyboardHandler implements KeyDownHandler, KeyUpHandler {
        /**
         * This method handles all key down events for this table
         */
        public void onKeyDown(KeyDownEvent event) {
            prevText = getText();
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_TAB:
                    if (popup != null && popup.isShowing())
                        popup.hide();
                    //event.stopPropagation();
                    break;
                case KeyCodes.KEY_BACKSPACE:
                    int selectLength;
                    /*
                     * If text is selected we want to select one more position back so the correct 
                     * text will be received in the key up event.
                     */
                    selectLength = textbox.getSelectionLength();
                    if (selectLength > 0) {
                        selectLength++ ;
                        textbox.setSelectionRange(getText().length() - selectLength, selectLength);
                    }
            }
        }
        
        /**
         * This method handles all keyup events for the dropdown widget.
         */
        public void onKeyUp(KeyUpEvent event) {
            String text;

            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_DOWN:
                    if (popup != null && popup.isShowing()) {
                        keepPopup = true;
                        table.selectRowAt(findNextActive(table.getSelectedRow()));
                        keepPopup = false;
                    }
                    break;
                case KeyCodes.KEY_UP:
                    if (popup != null && popup.isShowing()) {
                        keepPopup = true;
                        table.selectRowAt(findPrevActive(table.getSelectedRow()));
                        keepPopup = false;
                    }
                    break;
                case KeyCodes.KEY_ENTER:
                    if (popup == null || !popup.isShowing()) {
                        text = getText();
                        GetMatchesEvent.fire(source, text);
                    } else
                        popup.hide();
                    event.stopPropagation();
                    break;
                case KeyCodes.KEY_TAB:
                    break;
                default:
                    
                    text = getText();

                    timer.cancel();
                    /*
                     * Will hit this if backspaced to clear textbox. Call
                     * setSelected 0 so that if user tabs off the value is
                     * selected correctly
                     */
                    if (text.equals("")){    
                        setSelectedIndex( -1);
                        popup.hide();
                    }else if(!text.equals(prevText))
                        timer.schedule(delay);
                    
             }
        }

        /**
         * Method to find the next selectable item in the Dropdown
         * 
         * @param index
         * @return
         */
        @SuppressWarnings("rawtypes")
		private int findNextActive(int index) {
            int next;

            next = index + 1;
            while (next < table.getRowCount() && !((Item)table.getModel().get(next)).isEnabled())
                next++ ;

            if (next < table.getRowCount())
                return next;

            return index;

        }

        /**
         * Method to find the previous selectable item in the Dropdown
         * 
         * @param index
         * @return
         */
        @SuppressWarnings("rawtypes")
		private int findPrevActive(int index) {
            int prev;

            prev = index - 1;
            while (prev > -1 && !((Item)table.getModel().get(prev)).isEnabled())
                prev-- ;

            if (prev > -1)
                return prev;

            return index;
        }


    }

    /**
     * Private Default implementation of the Renderer interface.
     * 
     */
    protected class DefaultRenderer implements Renderer {
        public String getDisplay(Row row) {
            return row.getCells().get(0).toString();
        }
    }



}