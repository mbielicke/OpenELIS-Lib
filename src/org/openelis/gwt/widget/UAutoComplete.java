package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.event.GetMatchesEvent;
import org.openelis.gwt.event.GetMatchesHandler;
import org.openelis.gwt.event.HasGetMatchesHandlers;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRow;
import org.openelis.gwt.widget.table.TableWidget;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class UAutoComplete<T> extends TextBox<T> implements HasGetMatchesHandlers {
    /**
     * Used for AutoComplete display
     */
    protected HorizontalPanel hp;
    protected AppButton       button;
    protected TableWidget     table;
    protected PopupPanel      popup;
    protected int             cellHeight = 19,delay = 350;

    final UAutoComplete<T>    source;

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
        public String getDisplay(TableDataRow row);
    }

    /**
     * Default no-arg constructor
     */
    public UAutoComplete() {
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
        button = new AppButton("AutoDropdownButton");

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
                Item<T> item;

                BlurEvent.fireNativeEvent(event.getNativeEvent(), source);

                item = getSelectedItem();

                if (item != null)
                    setValue(item.itemKey, true);
            }
        });

        /*
         * Register click handler to button to show the popup table
         */
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                GetMatchesEvent.fire(source, "*");
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
                        setValue(item.itemKey, true);
                    }
                }
            });
        }
        
        if(popup.isShowing()) 
            return;
        
        popup.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + getOffsetHeight());
        popup.show();

        /*
         *  Scroll if needed to make selection visible
         */
        if (getSelectedIndex() > 0)
           table.scrollToVisible();
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
    public void setWidth(String width) {
        /*
         * Set the outer panel to full width;
         */
        if (hp != null)
            hp.setWidth(width);

        /*
         * set the Textbox to width - 16 to account for button.
         */
        textbox.setWidth( (Util.stripUnits(width, "px") - 16) + "px");

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
     * @param table
     */
    public void setPopupContext(TableWidget tableDef) {
        this.table = tableDef;

        /*
         * This handler will will cancel the selection if the item has been
         * disabled.
         */
        table.addBeforeSelectionHandler(new BeforeSelectionHandler<TableRow>() {
            public void onBeforeSelection(BeforeSelectionEvent<TableRow> event) {
                if ( !event.getItem().row.enabled)
                    event.cancel();
            }
        });

        /*
         * This handler will catch the events when the user clicks on rows in
         * the table.
         */
        table.addSelectionHandler(new SelectionHandler<TableRow>() {
            public void onSelection(SelectionEvent<TableRow> event) {
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
     * Sets the data model for the PopupContext of this widget.
     * 
     * @param model
     */
    public void setModel(ArrayList<Item<T>> model) {
        assert table != null;

        table.setMaxRows(10);
        /*
         * If model is smaller than maxRows then we want to reset maxRows so the
         * table is the correct size.
         */
        if (table.getMaxRows() > model.size()) {
            table.setMaxRows(model.size());
        }

        table.view.setHeight(table.getMaxRows() * cellHeight);

        table.load(model);

    }

    /**
     * Returns the model used in the table
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Item<T>> getModel() {
        return (ArrayList<Item<T>>)table.getData();
    }

    /**
     * Sets the selected row using its overall index in the model. This method
     * will also cause a ValueChangeEvent to be fired.
     * 
     * @param index
     */
    public void setSelectedIndex(int index) {
        table.selectRow(index);
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
        return (Item<T>)table.getSelection();
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
        button.enable(enabled);
        table.enable(enabled);
        if (enabled)
            sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        else
            unsinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        super.setEnabled(enabled);
    }
    
    /**
     * Method used to set an Autocomplete from OnDataChangeEvents from the screen.
     * @param value
     * @param display
     */
    public void setValue(T value, String display) {
        this.value = value;
        textbox.setText(display);
    }

    /**
     * Overridden method to set the T value of this widget. Will fire a value
     * change event if fireEvents is true and the value is changed from its
     * current value
     */
    @Override
    public void setValue(T value, boolean fireEvents) {

        if ( ! (this.value == null && value != null) ||
            (this.value != null && !this.value.equals(value)))
            return;

        if (value != null) {
            textbox.setText(renderer.getDisplay(getSelectedItem()));
        } else {
            table.selectRow( -1);
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
        validateExceptions = null;
        if (required && value == null) {
            addValidateException(new LocalizedException("fieldRequiredException"));
        }
        ExceptionHelper.getInstance().checkExceptionHandlers(this);
    }
    
    public void showAutoMatches(ArrayList<Item<T>> model) {
        setModel(model);
        if(textbox.getStyleName().indexOf("Focus") > -1)
            showPopup();
    }
    
    public HandlerRegistration addGetMatchesHandler(GetMatchesHandler handler) {
        return addHandler(handler,GetMatchesEvent.getType());
    }

    // ********** Table Keyboard Handling ****************************

    protected class KeyboardHandler implements KeyDownHandler, KeyUpHandler {
        /**
         * This method handles all key down events for this table
         */
        public void onKeyDown(KeyDownEvent event) {

            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_TAB:
                    if (popup != null && popup.isShowing())
                        popup.hide();
                    event.stopPropagation();
                    break;
            }
        }

        /**
         * Method to find the next selectable item in the Dropdown
         * 
         * @param index
         * @return
         */
        private int findNextActive(int index) {
            int next;

            next = index + 1;
            while (next < table.numRows() && !table.isEnabled(next))
                next++ ;

            if (next < table.numRows())
                return next;

            return index;

        }

        /**
         * Method to find the previous selectable item in the Dropdown
         * 
         * @param index
         * @return
         */
        private int findPrevActive(int index) {
            int prev;

            prev = index - 1;
            while (prev > -1 && !table.isEnabled(prev))
                prev-- ;

            if (prev > -1)
                return prev;

            return index;
        }

        /**
         * This method handles all keyup events for the dropdown widget.
         */
        public void onKeyUp(KeyUpEvent event) {
            int cursorPos;
            String text;
            
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_DOWN:
                    if(popup.isShowing()) {
                        table.selectRow(findNextActive(table.getSelectedRow()));
                        table.scrollToVisible();
                        setDisplay();
                        event.stopPropagation();
                    }
                    break;
                case KeyCodes.KEY_UP:
                    if(popup.isShowing()) {
                        table.selectRow(findPrevActive(table.getSelectedRow()));
                        table.scrollToVisible();
                        setDisplay();
                        event.stopPropagation();
                    }
                    break;
                case KeyCodes.KEY_ENTER:
                    if (popup == null || !popup.isShowing()){
                        text = getText();
                        if(text.equals(""))
                            text = "*";
                        GetMatchesEvent.fire(source, text);
                    }else
                        popup.hide();
                    event.stopPropagation();
                    break;
                case KeyCodes.KEY_TAB:
                    break;
                case KeyCodes.KEY_BACKSPACE:
                    text = getText();
                    if ( !text.equals(""))
                        textbox.setText(text.substring(0, text.length() - 1));
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
                    
                    new Delay(text);
            }
        }
    }

    /**
     * Private Default implementation of the Renderer interface.
     * 
     */
    protected class DefaultRenderer implements Renderer {
        public String getDisplay(TableDataRow row) {
            return row.getCells().get(0).toString();
        }
    }
    
    private class Delay {
         
        private String match;
        
        public Delay(String text) {
            this.match = text;
            
            new Timer() {
                public void run() {
                    int cursorPos; 
                    
                    if(match.equals(getText())) {
                        GetMatchesEvent.fire(source, match);
                        
                        cursorPos = match.length();
                        
                        setSelectedIndex(0);
                       
                        /*
                         * Call getText() here instead of text becaue it was changed by setSelectedIndex(0);
                         */
                        textbox.setSelectionRange(cursorPos, getText().length() - cursorPos);
                    }   
                }
            }.schedule(delay);
            
        }
    }

}
