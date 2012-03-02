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
import org.openelis.gwt.widget.table.CheckBoxCell;
import org.openelis.gwt.widget.table.Column;
import org.openelis.gwt.widget.table.Row;
import org.openelis.gwt.widget.table.Table;
import org.openelis.gwt.widget.table.event.CellClickedEvent;
import org.openelis.gwt.widget.table.event.CellClickedHandler;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is used by OpenELIS Screens to display and input values in forms
 * and in table cells as a Drop down list selector. 
 * 
 * @param <T>
 */
public class SelectionBox<T> extends Composite implements ScreenWidgetInt, 
													      Queryable, 
													      Focusable,
													      HasBlurHandlers, 
													      HasFocusHandlers,
													      HasValue<T>, 
													      HasExceptions {

    /**
     * Used for Dropdown display
     */
	
	protected FocusPanel            				outer;
	protected Grid 					                multiHeader;
    protected VerticalPanel         				vp;
    protected Button  					            checkAll,uncheckAll,close;
    protected Table                 				table;
    protected PopupPanel      					    popup;
    protected int                   			    cellHeight = 19, itemCount = 10, width, maxDisplay = 3;
    protected boolean 					            required,queryMode,showingOptions,multiSelect,enabled;
    protected ArrayList<T>          				value;
    protected Widget                                displayWidget;
    
    /**
     * Sorted list of display values for search
     */
    protected ArrayList<SearchPair> 				searchText;
    protected HashMap<T, Integer>					searchHash;
    
    protected SearchPair 							searchPair;
    
    /**
     * Exceptions list
     */
    protected ArrayList<LocalizedException>         endUserExceptions, validateExceptions;

    /**
     * HashMap to set selections by key;
     */
    protected HashMap<T, Integer>   				keyHash;

    /**
     * Instance of the Renderer interface. Initially set to the DefaultRenderer
     * implementation.
     */
    protected Renderer              			    renderer  = new DefaultRenderer();
    
    protected Display<T>                            display = new DefaultDisplay();
    
    /**
     * Public Interface used to provide rendering logic for the Selection display
     * 
     */
    public interface Renderer {
        public String getDisplay(Row row);
    }
    
    public interface Display<T> {
    	public void setDisplay(ArrayList<Item<T>> values);
    }

    /**
     * Default no-arg constructor
     */
    public SelectionBox() {
    	init();
    }

    /**
     * Init() method overrriden from TextBox to draw the Selection correctly.
     * Also set up handlers for click and key handling
     */
    public void init() {
        /*
         * Final instance used in Anonymous handlers.
         */
        final SelectionBox<T> source = this;

        /*
         * Final instance of the private class KeyboardHandler
         */
        final KeyboardHandler keyHandler = new KeyboardHandler();
        
        outer = new FocusPanel();
        
        DOM.setStyleAttribute(outer.getElement(), "borderWidth", "0");
        /*
         * Sets the panel as the wrapped widget
         */
        initWidget(outer);

        /*
         * Since HorizontalPanel is not a Focusable widget we need to listen to
         * the textbox focus and blur events and pass them through to the
         * handlers registered to source.
         */
        outer.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                FocusEvent.fireNativeEvent(event.getNativeEvent(), source);
            }
        });

        outer.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                ArrayList<T> values;

                BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
                if(!showingOptions && isEnabled() && !queryMode) {
                	values = getValues();

                		if(multiSelect)
                			setValues(values, true);
                		else
                			setValue(values != null ? values.get(0) : null,true);

               	}
            }
        });

        /*
         * Registers the keyboard handling this widget
         */
        addHandler(keyHandler, KeyDownEvent.getType());
        addHandler(keyHandler, KeyPressEvent.getType());
        
    }
    
    public void setDisplay(Widget widget) {
    	assert widget instanceof HasClickHandlers;
    	
    	displayWidget = widget;
    	outer.setWidget(widget);
    	
    	((HasClickHandlers)widget).addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(enabled)
					showPopup();
			}
		});
    }


    /**
     * This method will display the table set as the PopupContext for this
     * Select. Will create the Popup and initialize the first time if null. We
     * also call scrollToVisible() on the table to make sure the selected value
     * is in the current table view.
     */
    protected void showPopup() {
    	showingOptions = true;
        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setStyleName("DropdownPopup");
            if(multiSelect) {
                uncheckAll = new Button("Unchecked","All",false);
                checkAll = new Button("Checked","All",false);
                close = new Button("CloseButton","",false);
                multiHeader = new Grid(1,3);
                multiHeader.setCellSpacing(0);
                multiHeader.setCellPadding(0);
                multiHeader.setWidget(0,0,checkAll);
                multiHeader.setWidget(0,1,uncheckAll);
                multiHeader.setWidget(0,2,close);
                multiHeader.getCellFormatter().setHorizontalAlignment(0, 1, HasAlignment.ALIGN_CENTER);
                multiHeader.getCellFormatter().setHorizontalAlignment(0, 2, HasAlignment.ALIGN_RIGHT);
                multiHeader.setWidth("100%");
                vp = new VerticalPanel();
                vp.add(multiHeader);
                vp.add(table);
                popup.setWidget(vp);
                checkAll.addClickHandler(new ClickHandler() {
    				public void onClick(ClickEvent event) {
    					for(Item<T> item : getModel()) {
    						if(item.enabled)
    							item.setCell(0, "Y");
    					}
    					display.setDisplay(getSelectedItems());
    					table.setModel(getModel());
    				}
    			});
                checkAll.setEnabled(true);
                
                uncheckAll.addClickHandler(new ClickHandler() {
    				public void onClick(ClickEvent event) {
    					for(Item<T> item : getModel())
    						item.setCell(0, null);
    					display.setDisplay(null);
    					table.setModel(getModel());
    				}
    			});
                uncheckAll.setEnabled(true);
                
                close.addClickHandler(new ClickHandler() {
                	public void onClick(ClickEvent event) {
                		popup.hide();
                	}
                });
                close.setEnabled(true);
            }else
            	popup.setWidget(table);
            popup.setPreviewingAllNativeEvents(false);
            popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                public void onClose(CloseEvent<PopupPanel> event) {
                	showingOptions = false;
                	setFocus(true);
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


    @Override
    public void setWidth(String w) {    
    	width = Util.stripUnits(w);
        
        if(table != null) 
            table.setWidth(width+"px");

    }
    
    public int getWidth() {
        return width;
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
    
    public void setDisplay(Display<T> display) {
    	this.display = display;
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
                if (!((Item)table.getModel().get(event.getItem())).isEnabled()) {
                    event.cancel();
                    return;
                }
                /*
                 * Never select the table row in Multiselect,  Switch the checkbox 
                 * and move on.
                 */
         		if(multiSelect) {
     				if("Y".equals(table.getValueAt(event.getItem(), 0)))
     					table.setValueAt(event.getItem(),0,"N");
     				else
     					table.setValueAt(event.getItem(),0,"Y");
     				display.setDisplay(getSelectedItems());
     				event.cancel();
         		}
            }
        });

        /*
         * This handler will catch the events when the user clicks on rows in
         * the table.
         */
        table.addSelectionHandler(new SelectionHandler<Integer>() {
            public void onSelection(SelectionEvent<Integer> event) {
            	display.setDisplay(getSelectedItems());
            }
        });
            
       table.addCellClickedHandler(new CellClickedHandler() {
         	public void onCellClicked(CellClickedEvent event) {
         		/*
         		 * Keep options up if multiple selection occurs 
         		 */
         		if(multiSelect) 
         			return;
         			         
         		popup.hide();
           	}
       });
    }
    
    /**
     * Method Returns the Table widget used to display the Select Options 
     */
    public Table getPopupContext() {
        return table;
    }
    
    /**
     * Sets the number of visible rows in the Table that shows the options
     * @param itemCount
     */
    public void setVisibleItemCount(int itemCount) {
        this.itemCount = itemCount;
        
        if(table != null) 
        	table.setVisibleRows(itemCount);
    }

    /**
     * Sets the data model for the PopupContext of this widget.
     * 
     * @param model
     */
    public void setModel(ArrayList<Item<T>> model) {
        assert table != null;

        /*
         * If already in MultiSelect we need to add the check column
         * to the model.
         */
        if(multiSelect) {
        	/*
        	 * Insert column for checkbox
        	 */
        	for(Item<T> item : model) 
        		item.getCells().add(0,null);
        }
        
        table.setModel(model);
        
        if(model.size() < itemCount) {
            table.setVisibleRows(model.size());
        }else
            table.setVisibleRows(itemCount);
        

        createKeyHash(model);
        
        searchText = null;

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
    	if(multiSelect) {
            clearSelections();
            
            if(index > -1) 
            	getModel().get(index).setCell(0, "Y");
            
            display.setDisplay(getSelectedItems());
            
    	}else {
    		if(index > -1) {
    			table.selectRowAt(index);
    			display.setDisplay(getSelectedItems());
    		}else{
    			table.clearRowSelection();
    			display.setDisplay(null);
    		}
    	}
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
        
        if(!multiSelect && !table.isAnyRowSelected())
            return null;
        
        items = new ArrayList<Item<T>>();
        if(multiSelect) {
            for(Item<T> item : getModel()) {
            	if("Y".equals(item.getCell(0)))
            		items.add(item);
            }
        }else {
        	for(int i = 0; i < table.getSelectedRows().length; i++) 
        		items.add((Item<T>)table.getModel().get(table.getSelectedRows()[i]));
        }
        return items.size() > 0 ? items : null;
    }
    
    /**
     * Method will set the widget into MultiSelection mode
     * @param multi
     */
    public void setMultiSelect(boolean multi) {
    	if(multiSelect != multi) {
    		multiSelect = multi;
    		popup = null;
    		if(multi){
    			/*
    			 * If switching to multi select and checkbox column at position 0
    			 * Table will add the value to te model correctly
    			 */
    			Column col = new Column();
    			col.setCellRenderer(new CheckBoxCell(new CheckBox()));
    			table.addColumnAt(0, col);
    		}else{
    			/*
    			 * Remove Checkbox column if switching to single select
    			 * The table will remove the column from the model
    			 */
    			table.removeColumnAt(0);
    			display.setDisplay(null);
    		}
    	}
    }

    // ********** Methods Overridden in the ScreenWidetInt ****************

    /**
     * Method overridden from TextBox to enable the button and table as well as
     * the textbox.
     */
    @Override
    public void setEnabled(boolean enabled) {
       // if (isEnabled() == enabled)
         //   return;
    	
    	this.enabled = enabled;
    	
        table.setEnabled(enabled);
        
        if (enabled)
            sinkEvents(Event.ONKEYDOWN | Event.ONKEYPRESS);
        else
            unsinkEvents(Event.ONKEYDOWN | Event.ONKEYPRESS);
        
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
        ArrayList<T> values;

        if(!Util.isDifferent(this.value == null ? null : this.value.get(0), value)) 
            return;
        
    	values = new ArrayList<T>();
    	values.add(value);

        if(multiSelect) {
        	setValues(values,fireEvents);
        	return;
        }

        if (value != null) {
            validKey = keyHash.containsKey(value);
           // assert validKey : "Key not found in Item list";
            if(!validKey)
            	value = null;
            table.selectRowAt(keyHash.get(value));
            
            display.setDisplay(getSelectedItems());
            
        } else {
            table.selectRowAt( -1);
            display.setDisplay(null);
        }

        this.value = values;

        if (fireEvents) 
            ValueChangeEvent.fire(this, value);

    };

    /**
     * Check if the Dropdown is valid
     */
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
    
    /**
     * Method used to return the values of the Selection
     */
	public T getValue() {
		return value == null ? null : value.get(0);
	}
    
    /**
     * Method used to retrieve selected values when in MultiSelect mode
     * @return
     */
    public ArrayList<T> getValues() {
    	ArrayList<T> values = null;

		values = new ArrayList<T>();
    	if(multiSelect) {
    		for(Item<T> item : getModel())
    			if("Y".equals(item.getCell(0)))
    				values.add(item.getKey());
    	}else if(getSelectedItem() != null) 
    		values.add(getSelectedItem().key);
    	
    	
    	return values.size() > 0 ? values : null;
    }
    
    /**
     * Method used to set the value of the Selection
     * @param value
     */
	public void setValue(T value) {
		setValue(value, false);
	}
	
    /**
     * Method used to set the selections when Multiple Selection is allowed
     * @param values
     */
    public void setValues(ArrayList<T> values) {
    	setValues(values,false);
    }
    
    /**
     * Method used to set the selections when Multiple Selection is Allowed and can 
     * specify to fire ValueChangeEvent.
     * @param values
     * @param fireEvents
     */
    public void setValues(ArrayList<T> values, boolean fireEvents) {
        if(!Util.isDifferent(this.value, values)) 
            return;


        clearSelections();

        if (values != null) {
        	
        	for(T key : values)
        		getModel().get(keyHash.get(key)).setCell(0, "Y");        
        	
        	display.setDisplay(getSelectedItems());
        } else {
        	display.setDisplay(null);
        }

        this.value = values;

        if (fireEvents) 
            ValueChangeEvent.fire(this, value != null ? value.get(0) : null);
    }
    
    /**
     * Method called to check for valid Selection
     */
	public void validateValue() {
		validateValue(false);
	}
    
    /**
     * Method used in MultiSelect to clear all checkboxes in MultiSelect mode
     */
    protected void clearSelections() {
    	if(!multiSelect)
    		return;
    	
    	for(Item<T> item : getModel())
    		item.setCell(0, null);
    }
    
    /**
     * Method used to set the max number of comma delimited options to show
     * @param maxDisplay
     */
    public void setMaxDisplay(int maxDisplay) {
    	this.maxDisplay = maxDisplay;
    }

    //************  Implementation of Queryable ***********************
    /**
     * Method for putting the Select into query mode.
     */
    @Override
    public void setQueryMode(boolean query) {
        if (query == queryMode)
            return;
        queryMode = query;
        setMultiSelect(query);
    }

    /**
     * Method for creating a QueryData object for this
     * widget
     */
    @Override
    public Object getQuery() {
        QueryData qd;
        StringBuffer sb;
        ArrayList<T> values;

        values = getValues();
        /*
         * Return null if nothing selected
         */
        if (values == null || (values.size() == 1 && values.get(0) == null))
            return null;

        qd = new QueryData();

        /*
         * Create the query from the selected values
         */
        sb = new StringBuffer();

        for (int i = 0; i < values.size(); i++ ) {
            if (i > 0)
                sb.append(" | ");
            sb.append(values.get(i));
            if(values.get(i) != null)
           
                /*
                 * Since there is no helper we need to do an instance check here
                 */
                if (values.get(i) instanceof Integer)
                    qd.setType(QueryData.Type.INTEGER);
                else
                    qd.setType(QueryData.Type.STRING);

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
        
        setValues(null);
        
        if(qd == null)
            return;
        
        table.clearRowSelection();
        if(qd.getQuery() != null && !qd.getQuery().equals("")) {
        	params = qd.getQuery().split(" \\| ");
        	for(int i = 0; i < params.length; i++) {
        		if(qd.getType() == QueryData.Type.INTEGER) 
        			key = (T)new Integer(params[i]);  
        		else
        			key = (T)params[i];
           
        		getModel().get(keyHash.get(key)).setCell(0, "Y");
        	}
        }
        display.setDisplay(getSelectedItems());
        
        table.setModel(getModel());
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
            searchHash = new HashMap<T,Integer>();
            for (int i = 0; i < getModel().size(); i++ ) {
                if (getModel().get(i).enabled) {
                    searchText.add(new SearchPair(i, renderer.getDisplay(getModel().get(i))
                                                             .toUpperCase()));
                }
                
            }
            Collections.sort(searchText);
            
            for(int i = 0; i < searchText.size(); i++) 
            	searchHash.put(getModel().get(searchText.get(i).modelIndex).key, i);
            
        }
        
        index = Collections.binarySearch(searchText, new SearchPair( -1, textValue),
                                         new MatchComparator());
        
        if (index < 0)
            return -1;
        else {
            // we need to do a linear search backwards to find the first entry
            // that matches our search
            index-- ;
            while (index >= 0 &&
                   compareValue((String)searchText.get(index).display, textValue,textValue.length()) == 0)
                index-- ;

            return searchText.get(index + 1).modelIndex;
        }

    }
    


    public void closePopup() {
    	popup.hide();
    }
    
    /**
     * Compares two values by adjusting for length first.
     * @param value
     * @param textValue
     * @param length
     * @return
     */
    private int compareValue(String value, String textValue, int length) {
        if (value.length() < length) {
        	if(textValue.startsWith(value))
        		return -1;
            return value.compareTo(textValue.substring(0, value.length()));
        }
        return value.substring(0, length).compareTo(textValue);
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
     * Private Default implementation of the Renderer interface.
     * 
     */
    protected class DefaultRenderer implements Renderer {
        public String getDisplay(Row row) {
        	int index;
        
        	if(multiSelect)
        		index = 1;
        	else
        		index = 0;
        	
            return row != null ? row.getCells().get(index).toString() : "";
        }
    }    
    
    protected class DefaultDisplay implements Display<T> {

		@Override
		public void setDisplay(ArrayList<Item<T>> values) {
			//Do nothing
		}
    	
    }
    
    // ********** Table Keyboard Handling ****************************

    protected class KeyboardHandler implements KeyDownHandler, KeyPressHandler {
    	
    	String searchString = "";
    	
        /**
         * This method handles all key down events for this table
         */
        public void onKeyDown(KeyDownEvent event) {

            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_TAB:
                    if (popup != null && popup.isShowing())
                        popup.hide();
                    //event.stopPropagation();
                    break;
                case KeyCodes.KEY_BACKSPACE :
                	this.searchString = "";
                	break;
            }

        }
        
        /**
         * This method handles all keyup events for the dropdown widget.
         */
        public void onKeyPress(KeyPressEvent event) {
            int index;
            
            switch (event.getNativeEvent().getKeyCode()) {
                case KeyCodes.KEY_DOWN:
                    table.selectRowAt(findNextActive(table.getSelectedRow()));
                    break;
                case KeyCodes.KEY_UP:
                    table.selectRowAt(findPrevActive(table.getSelectedRow()));
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
                case KeyCodes.KEY_BACKSPACE:
                	setSelectedIndex(-1);
                	break;
                default:
                    searchString += String.valueOf(event.getCharCode());                    

                    index = findIndexByTextValue(searchString);
                    
                    if (index > -1)
                        setSelectedIndex(index);
                    else
                    	searchString = searchString.substring(0,searchString.length()-1);
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
     * Adds a manual Exception to the widgets exception list.
     */
    public void addException(LocalizedException error) {
        if (endUserExceptions == null)
            endUserExceptions = new ArrayList<LocalizedException>();
        endUserExceptions.add(error);
        ExceptionHelper.checkExceptionHandlers(this);
    }

    protected void addValidateException(LocalizedException error) {
        if (validateExceptions == null)
            validateExceptions = new ArrayList<LocalizedException>();
        validateExceptions.add(error);
    }

	
    /**
     * Convenience method to check if a widget has exceptions so we do not need
     * to go through the cost of merging the logical and validation exceptions
     * in the getExceptions method.
     * 
     * @return
     */
    public boolean hasExceptions() {
        return endUserExceptions != null || validateExceptions != null;
    }

    /**
     * Combines both exceptions list into a single list to be displayed on the
     * screen.
     */
    public ArrayList<LocalizedException> getValidateExceptions() {
        return validateExceptions;
    }

    public ArrayList<LocalizedException> getEndUserExceptions() {
        return endUserExceptions;
    }

    /**
     * Clears all manual and validate exceptions from the widget.
     */
    public void clearExceptions() {
        endUserExceptions = null;
        validateExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    public void clearEndUserExceptions() {
        endUserExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    public void clearValidateExceptions() {
        validateExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }

    
    public void addExceptionStyle(String style) {
    	outer.addStyleName(style);
    }
    
    public void removeExceptionStyle(String style) {
    	outer.removeStyleName(style);
    }


    /**
     * Method only implemented to satisfy Focusable interface.
     */
    public int getTabIndex() {
        return -1;
    }

    /**
     * Method only implemented to satisfy Focusable interface.
     */
    public void setTabIndex(int index) {

    }

    /**
     * Method only implemented to satisfy Focusable interface.
     */
    public void setAccessKey(char key) {

    }
	
	public void setFocus(boolean focused) {
		//if(display instanceof Focusable)
		//	((Focusable) display).setFocus(true);
		//else
			outer.setFocus(focused);
	}

    /**
     * Method used to validate the inputed query string by the user.
     */
    public void validateQuery() {
    	//Stubbed since user can not type query in
    }

    /**
     * Method used to determine if widget is currently in Query mode
     */
    public boolean isQueryMode() {
    	return queryMode;
    }

	@Override
	public boolean isEnabled() {
		return enabled;
	}

    public void addFocusStyle(String style) {
        addStyleName(style);
    }

    public void removeFocusStyle(String style) {
        removeStyleName(style);
    }
    
    public void setRequired(boolean required) {
    	this.required = required;
    }
    
    public boolean isMultSelect() {
    	return multiSelect;
    }
	
    // ************ Handler Registration methods *********************

    /**
     * The Screen will add its screenHandler here to register for the
     * onValueChangeEvent
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * This Method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return addDomHandler(handler, BlurEvent.getType());
    }

    /**
     * This method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return addDomHandler(handler, FocusEvent.getType());
    }

    /**
     * Adds a mouseover handler to the textbox for displaying Exceptions
     */
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    /**
     * Adds a MouseOut handler for hiding exceptions display
     */
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }
}
