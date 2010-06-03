package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.table.ColumnComparator;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRow;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.gwt.widget.table.event.SortEvent;

import com.google.gwt.dom.client.Document;
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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class UDropdown<T> extends TextBox<T> implements KeyDownHandler, KeyUpHandler {

    /**
     * Used for Dropdown display
     */
    protected HorizontalPanel         hp;
    protected AppButton               button;
    protected TableWidget             table;
    protected PopupPanel              popup;

    /**
     * Sorted list of display values for search
     */
    protected ArrayList<TableDataRow> searchText;

    /**
     * Public Interface used to provide rendering logic for the Dropdown display
     * 
     * @author tschmidt
     * 
     */
    public interface Renderer {
        public String getDisplay(TableDataRow row);
    }

    /**
     * Private Default implementation of the Renderer interface.
     * 
     * @author tschmidt
     * 
     */
    private class DefaultRenderer implements Renderer {
        public String getDisplay(TableDataRow row) {
            return row.getCells().get(0).toString();
        }
    }

    /**
     * Instance of the Renderer interface. Initially set to the DefaultRenderer
     * implementation.
     */
    protected Renderer renderer = new DefaultRenderer();

    /**
     * Default no-arg constructor
     */
    public UDropdown() {
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
        final UDropdown<T> source = this;

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
                FocusEvent.fireNativeEvent(Document.get().createFocusEvent(), source);
            }
        });

        textbox.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                BlurEvent.fireNativeEvent(Document.get().createBlurEvent(), source);
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
        addDomHandler(this, KeyDownEvent.getType());
        addDomHandler(this, KeyUpEvent.getType());

    }

    /**
     * This method will display the table set as the PopupContext for this
     * Dropdown. Will create the Popup and initialize the first time if null. We
     * also call scrollToVisible() on the table to make sure the selected value
     * is in the current table view.
     */
    private void showPopup() {
        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setStyleName("DropdownPopup");
            popup.setWidget(table);
            popup.setPreviewingAllNativeEvents(false);
        }
        popup.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + getOffsetHeight());
        popup.show();

        /*
         * Scroll if needed to make selection visible
         */
        if (getSelectedIndex() > 0)
            table.scrollToVisible();
    }

    @Override
    public void setWidth(String width) {
        /*
         * Set the outer panel to full width;
         */
        if (hp != null && width != null)
            hp.setWidth(width);
     
        /*
         * set the Textbox to width - 16 to account for button.
         */
        textbox.setWidth((Util.stripUnits(width, "px")-16)+"px");
       
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
    public void setPopupContext(TableWidget table) {
        this.table = table;
        table.isDropdown = true;
    }

    /**
     * Sets the data model for the PopupContext of this widget.
     * 
     * @param model
     */
    public void setModel(ArrayList<TableDataRow> model) {
        assert table != null;

        /*
         * If model is smaller than maxRows then we want to reset maxRows so the
         * table is the correct size.
         */
        if (table.getMaxRows() > model.size()) {
            table.setMaxRows(model.size());
            table.view.setHeight(table.getMaxRows() * 21);
        }

        table.load(model);

        /*
         * This handler will catch the events when the user clicks on rows in
         * the table.
         */
        table.addSelectionHandler(new SelectionHandler<TableRow>() {
            public void onSelection(SelectionEvent<TableRow> event) {
                if (queryMode) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < getSelectedRows().size(); i++ ) {
                        if (i > 0)
                            sb.append(" | ");
                        sb.append(renderer.getDisplay(getSelectedRows().get(i)));
                    }
                    textbox.setText(sb.toString());
                    if ( !table.ctrlKey && !table.shiftKey)
                        popup.hide();
                } else {
                    setValue((T)getSelectedRow().key, true);
                    popup.hide();
                }
            }
        });
    }

    /**
     * Returns the model used in the table
     * 
     * @return
     */
    public ArrayList<TableDataRow> getModel() {
        return table.getData();
    }

    /**
     * Sets the selected row using its overall index in the model. This method
     * will also cause a ValueChangeEvent to be fired.
     * 
     * @param index
     */
    public void setSelectedIndex(int index) {
        table.selectRow(index);
        textbox.setText(renderer.getDisplay(getSelectedRow()));
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
    public TableDataRow getSelectedRow() {
        return table.getSelection();
    }

    /**
     * Returns an ArrayList<TableDataRow> of selected rows in the table.
     * 
     * @return
     */
    public ArrayList<TableDataRow> getSelectedRows() {
        return table.getSelections();
    }

    /**
     * This method is used to set multiple selections in the widget by key when
     * the widget is in multiSelect mode.
     * 
     * @param values
     */
    public void setValues(T... values) {
        if (table.multiSelect)
            table.ctrlKey = true;
        table.clearSelections();
        if (values != null) {
            for (T key : values)
                table.selectRow(key);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < getSelectedRows().size(); i++ ) {
                if (i > 0)
                    sb.append(" | ");
                sb.append(renderer.getDisplay(getSelectedRows().get(i)));
            }
            textbox.setText(sb.toString());
        } else
            textbox.setText("");
        table.ctrlKey = false;
    }

    /**
     * Puts the widget into multiSelect mode.
     * 
     * @param multi
     */
    public void enableMultiSelect(boolean multi) {
        table.multiSelect(multi);
    }

    // ********** Methods Overridden in the ScreenWidetInt ****************

    /**
     * Method overridden from TextBox to enable the button and table as well as
     * the textbox.
     */
    @Override
    public void setEnabled(boolean enabled) {
        button.enable(enabled);
        table.enable(enabled);
        super.setEnabled(enabled);
    }

    /**
     * Overridden method to set the T value of this widget. Will fire a value
     * change event if fireEvents is true and the value is changed from its
     * current value
     */
    @Override
    public void setValue(T value, boolean fireEvents) {
        T old;

        old = this.value;
        this.value = value;
        if (value != null) {
            textbox.setText(renderer.getDisplay(getSelectedRow()));
        } else {
            textbox.setText("");
        }

        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, old, value);
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

    /**
     * Overridden method from TextBox for putting the Dropdown into query mode.
     */
    @Override
    public void setQueryMode(boolean query) {
        if (query == queryMode)
            return;
        queryMode = query;
        enableMultiSelect(query);
    }

    /**
     * Overridden method from TextBox for creating a QueryData object for this
     * widget
     */
    @Override
    public Object getQuery() {
        QueryData qd;
        StringBuffer sb;

        /*
         * Return null if nothing selected
         */
        if (getSelectedRows() == null)
            return null;

        qd = new QueryData();

        /*
         * Since there is no helper we need to do an instance check here
         */
        if (value instanceof Integer)
            qd.type = QueryData.Type.INTEGER;
        else
            qd.type = QueryData.Type.STRING;

        /*
         * Create the query from the selected values
         */
        sb = new StringBuffer();
        for (int i = 0; i < getSelectedRows().size(); i++ ) {
            if (i > 0)
                sb.append(" | ");
            sb.append(getSelectedRows().get(i).key.toString());
        }

        qd.query = sb.toString();

        return qd;
    }

    // ********** Table Keyboard Handling ****************************

    /**
     * This method handles all key bindings for the popup table
     */
    public void onKeyDown(KeyDownEvent event) {

        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_CTRL:
                table.ctrlKey = true;
                break;
            case KeyCodes.KEY_SHIFT:
                table.shiftKey = true;
                break;
            case KeyCodes.KEY_TAB:
                if (getSelectedRow() != null)
                    setValue((T)getSelectedRow().key, true);
                if (popup != null)
                    popup.hide();
                event.stopPropagation();
                break;
        }

    }

    private int findNextActive(int index) {
        index++;
        while (index < table.numRows() && !table.isEnabled(index))
            index++ ;
        if (index < table.numRows())
            return index;
        return findNextActive(index);
    }

    private int findPrevActive(int index) {
        index--;
        while (index > -1 && !table.isEnabled(index))
            index-- ;
        if (index > -1)
            return index;
        return findPrevActive(1);
    }

    public void onKeyUp(KeyUpEvent event) {
        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_CTRL:
                table.ctrlKey = false;
                break;
            case KeyCodes.KEY_SHIFT:
                table.shiftKey = false;
                break;
            case KeyCodes.KEY_DOWN:
                if (table.getSelectedRow() >= 0 && table.getSelectedRow() < table.numRows() - 1) {
                    final int row = findNextActive(table.getSelectedRow());
                    if ( !table.isRowDrawn(row)) {
                        table.view.setScrollPosition(table.view.top +
                                                     (21 * (row - table.getSelectedRow())));
                    }
                    table.selectRow(row);
                    setValue((T)getSelectedRow().key, true);
                } else if (table.getSelectedRow() < 0) {
                    table.selectRow(0);
                    setValue(null, true);
                }
                event.stopPropagation();
                break;
            case KeyCodes.KEY_UP:
                if (table.getSelectedRow() > 0) {
                    final int row = findPrevActive(table.getSelectedRow());
                    if ( !table.isRowDrawn(row)) {
                        table.view.setScrollPosition(table.view.top -
                                                     (21 * (table.getSelectedRow() - row)));
                    }
                    table.selectRow(row);
                    setValue((T)getSelectedRow().key, true);
                }
                event.stopPropagation();
                break;
            case KeyCodes.KEY_ENTER:
                if (table.getSelectedRow() > -1) {
                    if (popup == null || !popup.isShowing())
                        showPopup();
                    else
                        popup.hide();
                }
                event.stopPropagation();
                break;
            case KeyCodes.KEY_ESCAPE:
                popup.hide();
                event.stopPropagation();
                break;
            case KeyCodes.KEY_BACKSPACE :
                break;
            default:
                int cursorPos = getText().length();
                int index = getIndexByTextValue(getText());
                if (index > -1)
                    setSelectedIndex(index);
                else
                    cursorPos-- ;
                textbox.setSelectionRange(cursorPos, getText().length() - cursorPos);
        }
    }

    // *************** Search methods ******************

    private int getIndexByTextValue(String textValue) {
    	int index = -1;
        /*
         * Force to Upper case for matching 
         */
    	textValue = textValue.toUpperCase();
        
        if (textValue.equals(""))
            return -1;

        if (searchText == null) {
            searchText = new ArrayList<TableDataRow>();
            for (int i = 0; i < getModel().size(); i++ )
                searchText.add(new TableDataRow(
                                                i,
                                                ((String)getModel().get(i).cells.get(0).getValue()).toUpperCase()));
            Collections.sort(searchText, new ColumnComparator(0, SortEvent.SortDirection.ASCENDING));
        }
        index = Collections.binarySearch(searchText, new TableDataRow(null, textValue),
                                         new MatchComparator());

        if (index < 0)
            return -1;
        else {
            // we need to do a linear search backwards to find the first entry
            // that matches our search
            while (index > 0 &&
                   compareValue((String)searchText.get(index).getCells().get(0), textValue,
                                textValue.length()) == 0)
                index-- ;

            return ( ((Integer)searchText.get(index + 1).key)).intValue();
        }

    }

    private int compareValue(String value, String textValue, int length) {
        if (value.length() < length)
            return value.compareTo(textValue.substring(0, value.length()));
        return value.substring(0, length).compareTo(textValue);
    }

    private class MatchComparator implements Comparator<TableDataRow> {

        public int compare(TableDataRow o1, TableDataRow o2) {
        	String value;
        	String textValue;
        	
            value = (String)o1.cells.get(0).getValue();
            textValue = (String)o2.cells.get(0).getValue();
            
            return compareValue(value, textValue, textValue.length());
        }

    }

}
