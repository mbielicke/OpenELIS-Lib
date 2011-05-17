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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;
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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * This class is used by OpenELIS Screens to display and input values in forms
 * and in table cells as a Drop down list selector. This class exteds TextBox
 * which implements the ScreenWidgetInt and we override this implementation
 * where needed.
 * 
 * @param <T>
 */
public class Dropdown<T> extends TextBox<T> {

    /**
     * Used for Dropdown display
     */
    protected HorizontalPanel       hp;
    protected Button                button;
    protected Table                 table;
    protected PopupPanel            popup;
    protected int                   cellHeight = 19, itemCount = 10, width;
    protected boolean               keepPopup;
    /**
     * Sorted list of display values for search
     */
    protected ArrayList<SearchPair> searchText;

    /**
     * HashMap to set selections by key;
     */
    protected HashMap<T, Integer>   keyHash;

    /**
     * Instance of the Renderer interface. Initially set to the DefaultRenderer
     * implementation.
     */
    protected Renderer              renderer   = new DefaultRenderer();

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
    public Dropdown() {
    }

    /**
     * Init() method overrriden from TextBox to draw the Dropdown correctly.
     * Also set up handlers for click and key handling
     */
    @Override
    public void init() {
        /*
         * Final instance used in Anonymous handlers.
         */
        final Dropdown<T> source = this;

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

        setStyleName("AutoDropdown");
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
                Item<T> item;

                BlurEvent.fireNativeEvent(event.getNativeEvent(), source);

                item = getSelectedItem();

                if (item != null)
                    setValue(item.key, true);
            }
        });

        /*
         * Register click handler to button to show the popup table
         */
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                showPopup();
            }
        });

        /*
         * Registers the keyboard handling this widget
         */
        addHandler(keyHandler, KeyDownEvent.getType());
        addHandler(keyHandler, KeyUpEvent.getType());

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
                    Item<T> item;
                    /*
                     * Call set value if user arrowed down to select and clicked
                     * to another widget to close the Popup.
                     */
                    item = getSelectedItem();
                    if (event.isAutoClosed() && item != null) {
                        setValue(item.key, true);
                    }
                }
            });
        }
        
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
        StringBuffer sb;
        ArrayList<Item<T>> items;

        sb = new StringBuffer();
        items = getSelectedItems();
        for (int i = 0; i < items.size(); i++ ) {
            if (i > 0)
                sb.append(" | ");
            sb.append(renderer.getDisplay(items.get(i)));
        }

        textbox.setText(sb.toString());
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
        
        if(table != null) 
            table.setWidth(width+"px");

    }
    
    public int getWidth() {
        return width;
    }
    
    @Override
    public void setHeight(String height) {
        textbox.setHeight(height);
        button.setHeight(height);
    }

    /**
     * This method sets up the key hash which is used to search for the correct
     * index to select when setting value by key.
     * 
     * @param model
     */
    private void createKeyHash(ArrayList<Item<T>> model) {
        keyHash = new HashMap<T, Integer>();

        for (int i = 0; i < model.size(); i++ )
            keyHash.put(model.get(i).key, i);

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
        //table.setVisibleRows(itemCount);
        table.setTableStyle("DropdownTable");
        table.setFixScrollbar(false);
        table.setRowHeight(16);
        table.setEnabled(true);
        /*
         * This handler will will cancel the selection if the item has been
         * disabled.
         */
        table.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @SuppressWarnings("rawtypes")
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                if (!((Item)table.getModel().get(event.getItem())).isEnabled())
                    event.cancel();
            }
        });

        /*
         * This handler will catch the events when the user clicks on rows in
         * the table.
         */
        table.addSelectionHandler(new SelectionHandler<Integer>() {
            public void onSelection(SelectionEvent<Integer> event) {
                /*
                 * Close popup if not in multiSelect mode or if we are in
                 * multiSelect but the ctrl or shift is not held
                 */
                if (!keepPopup && !table.isMultipleSelectionAllowed()) {
                    if(popup != null)
                        popup.hide();
                }

                setDisplay();

                /*
                 * Set the focus back to the Textbox after closing or nothing
                 * will be focused
                 */
                textbox.setFocus(true);
            }
        });
    }
    
    public Table getPopupContext() {
        return table;
    }
    
    public void setVisibleItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * Sets the data model for the PopupContext of this widget.
     * 
     * @param model
     */
    public void setModel(ArrayList<Item<T>> model) {
        assert table != null;

        table.setModel(model);
        
        if(model.size() < itemCount) {
            table.setVisibleRows(model.size());
        }else
            table.setVisibleRows(itemCount);
        

        createKeyHash(model);

    }

    /**
     * Returns the model used in the table
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Item<T>> getModel() {
        return (ArrayList<Item<T>>)table.getModel();
    }

    /**
     * Sets the selected row using its overall index in the model. This method
     * will also cause a ValueChangeEvent to be fired.
     * 
     * @param index
     */
    public void setSelectedIndex(int index) {
        table.selectRowAt(index);
        textbox.setText(renderer.getDisplay(getSelectedItem()));
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
    public Item<T> getSelectedItem() {
        if(table.isAnyRowSelected())
            return (Item<T>)table.getModel().get(table.getSelectedRow());
        return null;
    }

    /**
     * Returns an ArrayList<TableDataRow> of selected rows in the table.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Item<T>> getSelectedItems() {
        ArrayList<Item<T>> items = null;
        
        if(!table.isAnyRowSelected())
            return null;
        
        items = new ArrayList<Item<T>>();
        for(int i = 0; i < table.getSelectedRows().length; i++) {
            items.add((Item<T>)table.getModel().get(table.getSelectedRows()[i]));
        }
        return items;
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

    //********* Implementation of HasValue *************************
    /**
     * Overridden method to set the T value of this widget. Will fire a value
     * change event if fireEvents is true and the value is changed from its
     * current value
     */
    @Override
    public void setValue(T value, boolean fireEvents) {
        boolean validKey;

        if(!Util.isDifferent(this.value, value)) 
            return;


        if (value != null) {
            validKey = keyHash.containsKey(value);
           // assert validKey : "Key not found in Item list";
            if(!validKey)
            	value = null;
            table.selectRowAt(keyHash.get(value));
            
            setDisplay();
        } else {
            table.selectRowAt( -1);
            textbox.setText("");
        }

        this.value = value;

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    };

    /**
     * Overridden method of TextBox to check if the Dropdown is valid
     */
    @Override
    protected void validateValue(boolean fireEvents) {
        Item<T> item;
        validateExceptions = null;
        
        item = getSelectedItem();
        if (item != null)
            setValue(item.key, fireEvents);

        if (required && value == null) {
            addValidateException(new LocalizedException("gen.fieldRequiredException"));
        }
        ExceptionHelper.checkExceptionHandlers(this);
    }

    //************  Implementation of Queryable ***********************
    /**
     * Overridden method from TextBox for putting the Dropdown into query mode.
     */
    @Override
    public void setQueryMode(boolean query) {
        if (query == queryMode)
            return;
        queryMode = query;
        table.setAllowMultipleSelection(query);
    }

    /**
     * Overridden method from TextBox for creating a QueryData object for this
     * widget
     */
    @Override
    public Object getQuery() {
    	if(!queryMode)
    		return null;
    	
        QueryData qd;
        StringBuffer sb;
        ArrayList<Item<T>> items;

        items = getSelectedItems();
        /*
         * Return null if nothing selected
         */
        if (items == null)
            return null;

        qd = new QueryData();

        /*
         * Since there is no helper we need to do an instance check here
         */
        if (value instanceof Integer)
            qd.setType(QueryData.Type.INTEGER);
        else
            qd.setType(QueryData.Type.STRING);

        /*
         * Create the query from the selected values
         */
        sb = new StringBuffer();

        for (int i = 0; i < items.size(); i++ ) {
            if (i > 0)
                sb.append(" | ");
            sb.append(items.get(i).key);
        }

        qd.setQuery(sb.toString());

        return qd;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void setQuery(QueryData qd) {
        String[] params;
        T key;
        
        if(!queryMode)
            return;
        
        setValue(null);
        
        if(qd == null)
            return;
        
        params = qd.getQuery().split(" \\| ");
        for(int i = 0; i < params.length; i++) {
            if(qd.getType() == QueryData.Type.INTEGER) 
                key = (T)new Integer(params[i]);  
            else
                key = (T)params[i];
           
            table.selectRowAt(keyHash.get(key),true);
           
        }
        setDisplay();
    }

    // *************** Search methods ******************

    /**
     * This method will perform a binary search on a sorted version of the the
     * Dropdown model
     */
    private int findIndexByTextValue(String textValue) {
        int index = -1;
        /*
         * Force to Upper case for matching
         */
        textValue = textValue.toUpperCase();

        if (textValue.equals(""))
            return -1;

        if (searchText == null) {
            searchText = new ArrayList<SearchPair>();
            for (int i = 0; i < getModel().size(); i++ ) {
                if (getModel().get(i).enabled)
                    searchText.add(new SearchPair(i, renderer.getDisplay(getModel().get(i))
                                                             .toUpperCase()));
            }
            Collections.sort(searchText);
        }
        index = Collections.binarySearch(searchText, new SearchPair( -1, textValue),
                                         new MatchComparator());

        if (index < 0)
            return -1;
        else {
            // we need to do a linear search backwards to find the first entry
            // that matches our search
            index-- ;
            while (index > 0 &&
                   compareValue((String)searchText.get(index).display, textValue,
                                textValue.length()) == 0)
                index-- ;

            return searchText.get(index + 1).modelIndex;
        }

    }

    /**
     * Private class to implement partial match comparator when searching
     * @author tschmidt
     *
     */
    private class MatchComparator implements Comparator<SearchPair> {

        public int compare(SearchPair o1, SearchPair o2) {
            return compareValue(o1.display, o2.display, o2.display.length());
        }

    }

    /**
     * Compares two values by adjusting for length first.
     * @param value
     * @param textValue
     * @param length
     * @return
     */
    private int compareValue(String value, String textValue, int length) {
        if (value.length() < length)
            return value.compareTo(textValue.substring(0, value.length()));
        return value.substring(0, length).compareTo(textValue);
    }

    // ********** Table Keyboard Handling ****************************

    protected class KeyboardHandler implements KeyDownHandler, KeyUpHandler {
        /**
         * This method handles all key down events for this table
         */
        public void onKeyDown(KeyDownEvent event) {

            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_CTRL:
                    //table.ctrlKey = true;
                    break;
                case KeyCodes.KEY_SHIFT:
                    //table.shiftKey = true;
                    break;
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
                    if(selectLength > 0){
                        selectLength++;
                        textbox.setSelectionRange(getText().length() - selectLength, selectLength);
                    }
            }

        }
        
        /**
         * This method handles all keyup events for the dropdown widget.
         */
        public void onKeyUp(KeyUpEvent event) {
            int cursorPos, index;
            String text;
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_CTRL:
                    //table.ctrlKey = false;
                    break;
                case KeyCodes.KEY_SHIFT:
                    //table.shiftKey = false;
                    break;
                case KeyCodes.KEY_DOWN:
                    keepPopup = true;
                    table.selectRowAt(findNextActive(table.getSelectedRow()));
                    keepPopup = false;
                    //table.scrollToVisible(table.get);
                    //setDisplay();
                    //event.stopPropagation();
                    break;
                case KeyCodes.KEY_UP:
                    keepPopup = true;
                    table.selectRowAt(findPrevActive(table.getSelectedRow()));
                    keepPopup = false;
                    //table.scrollToVisible();
                    //setDisplay();
                    //event.stopPropagation();
                    break;
                case KeyCodes.KEY_ENTER:
                    if (popup == null || !popup.isShowing())
                        showPopup();
                    else
                        popup.hide();
                    event.stopPropagation();
                    break;
                case KeyCodes.KEY_TAB:
                    break;
                default:
                    text = getText();

                    /*
                     * Will hit this if backspaced to clear textbox. Call
                     * setSelected 0 so that if user tabs off the value is
                     * selected correctly
                     */
                    if (text.equals("")) {
                        setSelectedIndex( -1);
                        return;
                    }

                    cursorPos = text.length();
                    index = findIndexByTextValue(text);

                    if (index > -1)
                        setSelectedIndex(index);
                    else
                        cursorPos-- ;

                    /*
                     * Call getText() here instead of text because it was changed
                     * by setSelectedIndex(0);
                     */
                    textbox.setSelectionRange(cursorPos, getText().length() - cursorPos);

            }
        }

        /**
         * Method to find the next selectable item in the Dropdown
         * 
         * @param index
         * @return
         */
        @SuppressWarnings("unchecked")
		private int findNextActive(int index) {
            int next;

            next = index + 1;
            while (next < table.getRowCount() && !((Item<T>)table.getModel().get(next)).isEnabled())
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
        @SuppressWarnings("unchecked")
		private int findPrevActive(int index) {
            int prev;

            prev = index - 1;
            while (prev > -1 && !((Item<T>)table.getModel().get(prev)).isEnabled())
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
            return row != null ? row.getCells().get(0).toString() : "";
        }
    }
    
    @Override
    public void addExceptionStyle(String style) {
    	textbox.addStyleName(style);
    }
    
    @Override
    public void removeExceptionStyle(String style) {
    	textbox.removeStyleName(style);
    }

    public void closePopup() {
    	popup.hide();
    }
}
