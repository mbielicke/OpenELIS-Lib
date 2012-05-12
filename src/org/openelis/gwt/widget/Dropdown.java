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
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class is used by OpenELIS Screens to display and input values in forms
 * and in table cells as a Drop down list selector. This class exteds TextBox
 * which implements the ScreenWidgetInt and we override this implementation
 * where needed.
 * 
 * @param <T>
 */
public class Dropdown<T> extends Composite implements ScreenWidgetInt, 
													  Queryable, 
													  Focusable,
													  HasBlurHandlers, 
													  HasFocusHandlers,
													  HasValue<T>, 
													  HasExceptions {

	/**
	 * Used for Dropdown display
	 */

	protected FocusPanel            				focus;
	protected AbsolutePanel                         outer;
	protected Grid 					                display,multiHeader;
	protected VerticalPanel         				vp;
	protected Button  					            button,checkAll,uncheckAll,close;
	protected Table                 				table;
	protected PopupPanel      					    popup;
	protected int                   			    cellHeight = 19, itemCount = 10, width, maxDisplay = 3;
	protected boolean 					            required,queryMode,showingOptions,multiSelect,enabled;
	protected ArrayList<T>          				value;

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

	/**
	 * Public Interface used to provide rendering logic for the Selection display
	 * 
	 */
	public interface Renderer {
		public String getDisplay(Row row);
	}

	/**
	 * Default no-arg constructor
	 */
	public Dropdown() {
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
		final Dropdown<T> source = this;

		/*
		 * Final instance of the private class KeyboardHandler
		 */
		final KeyboardHandler keyHandler = new KeyboardHandler();

		/*
		 * Focus Panel is used to catch Focus and blur events internal to the widget
		 */
		focus = new FocusPanel();
		display = new Grid(1,2);
		display.setCellPadding(0);
		display.setCellSpacing(0);

		/*
		 * New constructor in Button to drop the border and a div with the
		 * passed style.
		 */
		button = new Button();
		AbsolutePanel image = new AbsolutePanel();
		image.setStyleName("SelectButton");
		button.setDisplay(image, false);

		display.setWidget(0,1,button);
		display.getCellFormatter().setWidth(0,1,"16px");

		focus.add(display);

		/*
		 * We wrap the focus panel again so that the focus and blur events 
		 * do not directly go from inner events to being external events;
		 */
		outer = new AbsolutePanel();
		outer.add(focus);
		initWidget(outer);

		display.setStyleName("SelectBox");

		/*
		 * Set the focus style when the Focus event is fired Externally
		 */
		addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				if(enabled)
					display.addStyleName("Focus");
			}
		});

		/*
		 * Removes the focus style when the Blue event is fires externally
		 */
		addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				
				display.removeStyleName("Focus");
				
				finishEditing();

			}
		});

		/*
		 *  Receives the Focus Event internally and exposes it Externally 
		 */
		focus.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				FocusEvent.fireNativeEvent(event.getNativeEvent(),source);
			}
		});

		/*
		 *  Receives the Blur Event internally and determines if it should be
		 *  fired externally or if the widget still has focus
		 */
		focus.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				
				if(!showingOptions && isEnabled()) 
					BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
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
		 * This is a hack to ensure that showingOptions is set true before 
		 * the blur event is fired 
		 */
		button.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				showingOptions = true;
			}
		});


		/*
		 * Registers the keyboard handling this widget
		 */
		addHandler(keyHandler, KeyDownEvent.getType());
		addHandler(keyHandler, KeyPressEvent.getType());

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
						setDisplay();
						table.setModel(getModel());
					}
				});
				checkAll.setEnabled(true);

				uncheckAll.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						for(Item<T> item : getModel())
							item.setCell(0, null);
						setDisplay();
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
					setFocus(true);
					showingOptions = false;

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
		int selected = 0;

		sb = new StringBuffer();
		if(multiSelect) 
			items = getModel();
		else
			items = getSelectedItems();
		if(items != null) {
			for (int i = 0; i < items.size(); i++ ) {
				if(!multiSelect || (multiSelect && "Y".equals(items.get(i).getCell(0)))) {
					selected++;
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(renderer.getDisplay(items.get(i)));
				}
			}
			if(selected > maxDisplay)
				sb = new StringBuffer().append(selected+" options selected");
		}

		display.setText(0, 0, sb.toString());
		//label.setText(sb.toString());
	}

	@Override
	public void setWidth(String w) {    
		width = Util.stripUnits(w) - 5;

		/*
		 * Set the outer panel to full width;
		 */
		if (display != null)
			display.setWidth(width+"px");

		/*
		 * set the Textbox to width - 16 to account for button.
		 */

		display.getCellFormatter().setWidth(0,0,(width - 16) + "px");

		if(table != null) 
			table.setWidth(width+"px");

	}

	public int getWidth() {
		return width;
	}

	@Override
	public void setHeight(String height) {
		display.setHeight(height);
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
					setDisplay();
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
				setDisplay();
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
	public ArrayList<Item<T>> getModel() {
		return table.getModel();
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

			setDisplay();
		}else {
			if(index > -1) {
				table.selectRowAt(index);
				display.setText(0, 0, renderer.getDisplay(getSelectedItem()));
			}else{
				table.clearRowSelection();
				display.setText(0, 0, "");
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
	public Item<T> getSelectedItem() {
		if(table.isAnyRowSelected())
			return getModel().get(table.getSelectedRow());
		return null;
	}

	/**
	 * Returns an ArrayList<TableDataRow> of selected rows in the table.
	 * 
	 * @return
	 */
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
				items.add(getModel().get(table.getSelectedRows()[i]));
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
				Column col = new Column.Builder(15).build();
				col.setCellRenderer(new CheckBoxCell(new CheckBox()));
				table.addColumnAt(0, col);
			}else{
				/*
				 * Remove Checkbox column if switching to single select
				 * The table will remove the column from the model
				 */
				table.removeColumnAt(0);
				display.setText(0, 0, "");
			}
		}
	}

	/**
	 * Returns the string currently displayed in the textbox portion of the
	 * widget.
	 * 
	 * @return
	 */
	public String getDisplay() {
		return display.getText(0,0);
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

		button.setEnabled(enabled);
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

			setDisplay();
		} else {
			table.selectRowAt( -1);
			display.setText(0,0,"");
		}

		this.value = values;

		if (fireEvents) 
			ValueChangeEvent.fire(this, value);

	};

	/**
	 * Check if the Dropdown is valid
	 */
	public void finishEditing() {
		ArrayList<T> values;
		
		values = getValues();

		if(multiSelect)
			setValues(values, true);
		else
			setValue(values != null ? values.get(0) : null,true);

		validateExceptions = null;

		if (required && value == null) 
			addValidateException(new LocalizedException("exc.fieldRequiredException"));
		
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
		if(!Util.isDifferent(this.value, values)) { 
			return;
		}

		clearSelections();

		if (values != null) {

			for(T key : values)
				getModel().get(keyHash.get(key)).setCell(0, "Y");

			setDisplay();
		} else {
			display.setText(0,0,"");
		}

		this.value = values;

		if (fireEvents) 
			ValueChangeEvent.fire(this, value != null ? value.get(0) : null);
		
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
		setDisplay();

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
			char ch;
			
			ch = event.getUnicodeCharCode() == 0 ? (char)event.getNativeEvent().getKeyCode() : event.getCharCode();
			
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
				searchString += String.valueOf(ch);                    

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
    	if(validateExceptions != null)
    		return true;
    	  
    	if (!queryMode && required && getValue() == null) {
            addValidateException(new LocalizedException("exc.fieldRequiredException"));
            ExceptionHelper.checkExceptionHandlers(this);
    	}
    	  
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
		removeExceptionStyle("InputError");
		removeExceptionStyle("InputWarning");
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
		display.addStyleName(style);
	}

	public void removeExceptionStyle(String style) {
		display.removeStyleName(style);
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
		focus.setFocus(focused);
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
		return addHandler(handler, BlurEvent.getType());
	}

	/**
	 * This method is here so the Focus logic of ScreenPanel can be notified
	 */
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addHandler(handler, FocusEvent.getType());
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
    
    /*
    /**
     * Used for Dropdown display
     
    protected HorizontalPanel       hp;
    protected Button                button;
    protected Table                 table;
    protected PopupPanel            popup;
    protected int                   cellHeight = 19, itemCount = 10, width;
    protected boolean               showingOptions;
    /**
     * Sorted list of display values for search
     
    protected ArrayList<SearchPair> searchText;

    /**
     * HashMap to set selections by key;
     
    protected HashMap<T, Integer>   keyHash;

    /**
     * Instance of the Renderer interface. Initially set to the DefaultRenderer
     * implementation.
     
    protected Renderer              renderer   = new DefaultRenderer();

    /**
     * Public Interface used to provide rendering logic for the Dropdown display
     * 
     
    public interface Renderer {
        public String getDisplay(Row row);
    }

    /**
     * Default no-arg constructor
     
    public Dropdown() {
    }

    /**
     * Init() method overrriden from TextBox to draw the Dropdown correctly.
     * Also set up handlers for click and key handling
     
    @Override
    public void init() {
        /*
         * Final instance used in Anonymous handlers.
         
        final Dropdown<T> source = this;

        /*
         * Final instance of the private class KeyboardHandler
         
        final KeyboardHandler keyHandler = new KeyboardHandler();
        
        hp = new HorizontalPanel();
        textbox = new TextBase();
        /*
         * New constructor in Button to drop the border and a div with the
         * passed style.
         
        button = new Button();
        AbsolutePanel image = new AbsolutePanel();
        image.setStyleName("AutoDropdownButton");
        button.setDisplay(image, false);
        
        hp.add(textbox);
        hp.add(button);

        /*
         * Sets the panel as the wrapped widget
         
        initWidget(hp);

        setStyleName("AutoDropdown");
        textbox.setStyleName("TextboxUnselected");

        /*
         * Since HorizontalPanel is not a Focusable widget we need to listen to
         * the textbox focus and blur events and pass them through to the
         * handlers registered to source.
         
        textbox.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                FocusEvent.fireNativeEvent(event.getNativeEvent(), source);
            }
        });

        textbox.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {
                Item<T> item;

                BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
                if(!showingOptions && isEnabled() && !queryMode) {
                	item = getSelectedItem();

                	if (item != null)
                		setValue(item.key, true);
               	}
            }
        });

        /*
         * Register click handler to button to show the popup table
         
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                showPopup();
            }
        });

        /*
         * Registers the keyboard handling this widget
         
        addHandler(keyHandler, KeyDownEvent.getType());
        addHandler(keyHandler, KeyUpEvent.getType());
    }

    /**
     * This method will display the table set as the PopupContext for this
     * Dropdown. Will create the Popup and initialize the first time if null. We
     * also call scrollToVisible() on the table to make sure the selected value
     * is in the current table view.
     
    protected void showPopup() {
    	showingOptions = true;
        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setStyleName("DropdownPopup");
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
         
        if (getSelectedIndex() > 0)
            table.scrollToVisible(getSelectedIndex());
        
    }

    /**
     * Method called by various event handlers to set the displayed text for the
     * selected row in the table without firing value change events to the end
     * user.
     
    protected void setDisplay() {
        StringBuffer sb;
        ArrayList<Item<T>> items;

        sb = new StringBuffer();
        items = getSelectedItems();
        if(items != null) {
        	for (int i = 0; i < items.size(); i++ ) {
        		if (i > 0)
        			sb.append(" | ");
        		sb.append(renderer.getDisplay(items.get(i)));
        	}
        }

        textbox.setText(sb.toString());
    }

    @Override
    public void setWidth(String w) {    
        width = Util.stripUnits(w);

        /*
         * Set the outer panel to full width;
         
        if (hp != null)
            hp.setWidth(width+"px");

        /*
         * set the Textbox to width - 16 to account for button.
         
        
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
     
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Sets the Table definition to be used as the PopupContext for this
     * Dropdown. Will set the isDropdown flag in the Table so the correct
     * styling is used.
     * 
     * @param tree
     
    public void setPopupContext(Table tableDef) {
        this.table = tableDef;
        table.setTableStyle("DropdownTable");
        table.setFixScrollbar(false);
        table.setRowHeight(16);
        table.setEnabled(true);
        /*
         * This handler will will cancel the selection if the item has been
         * disabled.
        
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
         
        table.addSelectionHandler(new SelectionHandler<Integer>() {
            public void onSelection(SelectionEvent<Integer> event) {
                setDisplay();
            }
        });
            
       table.addCellClickedHandler(new CellClickedHandler() {
         	public void onCellClicked(CellClickedEvent event) {
         		/*
         		 * Keep options up if multiple selection occurs 
         		 
         		if(table.isMultipleSelectionAllowed() && (event.isCtrlKeyDown() || event.isShiftKeyDown()))
         			return;
         		
         		popup.hide();
           	}
       });
    }
    
    public Table getPopupContext() {
        return table;
    }
    
    public void setVisibleItemCount(int itemCount) {
        this.itemCount = itemCount;
        
        if(table != null) 
        	table.setVisibleRows(itemCount);
    }

    /**
     * Sets the data model for the PopupContext of this widget.
     * 
     * @param model
     
    public void setModel(ArrayList<Item<T>> model) {
        assert table != null;

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
     
    @SuppressWarnings("unchecked")
    public ArrayList<Item<T>> getModel() {
        return (ArrayList<Item<T>>)table.getModel();
    }

    /**
     * Sets the selected row using its overall index in the model. This method
     * will also cause a ValueChangeEvent to be fired.
     * 
     * @param index
     
    public void setSelectedIndex(int index) {
        table.selectRowAt(index);
        textbox.setText(renderer.getDisplay(getSelectedItem()));
    }

    /**
     * Returns the overall index of the selected row in the model
     * 
     * @return
     
    public int getSelectedIndex() {
        return table.getSelectedRow();
    }

    /**
     * Returns the currently selected TableDataRow in the Table
     * 
     * @return
     
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
     
    public String getDisplay() {
        return getText();
    }

    // ********** Methods Overridden in the ScreenWidetInt ****************

    /**
     * Method overridden from TextBox to enable the button and table as well as
     * the textbox.
     
    @Override
    public void setEnabled(boolean enabled) {
       // if (isEnabled() == enabled)
         //   return;
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

        if (fireEvents) 
            ValueChangeEvent.fire(this, value);

    };

    /**
     * Overridden method of TextBox to check if the Dropdown is valid
     
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
     
    @Override
    public Object getQuery() {
        QueryData qd;
        StringBuffer sb;
        ArrayList<Item<T>> items;

        items = getSelectedItems();
        /*
         * Return null if nothing selected
         
        if (items == null)
            return null;

        qd = new QueryData();

        /*
         * Since there is no helper we need to do an instance check here
         
        if (value instanceof Integer)
            qd.setType(QueryData.Type.INTEGER);
        else
            qd.setType(QueryData.Type.STRING);

        /*
         * Create the query from the selected values
         
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
        
        table.clearRowSelection();
        if(qd.getQuery() != null && !qd.getQuery().equals("")) {
        	params = qd.getQuery().split(" \\| ");
        	for(int i = 0; i < params.length; i++) {
        		if(qd.getType() == QueryData.Type.INTEGER) 
        			key = (T)new Integer(params[i]);  
        		else
        			key = (T)params[i];
           
        		table.selectRowAt(keyHash.get(key),true);
        	}
        }
        setDisplay();
    }

    // *************** Search methods ******************

    /**
     * This method will perform a binary search on a sorted version of the the
     * Dropdown model
     
    private int findIndexByTextValue(String textValue) {
        int index = -1;
        /*
         * Force to Upper case for matching
         
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
            while (index >= 0 &&
                   compareValue((String)searchText.get(index).display, textValue,
                                textValue.length()) == 0)
                index-- ;

            return searchText.get(index + 1).modelIndex;
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
    
    /**
     * Compares two values by adjusting for length first.
     * @param value
     * @param textValue
     * @param length
     * @return
     
    private int compareValue(String value, String textValue, int length) {
        if (value.length() < length)
            return value.compareTo(textValue.substring(0, value.length()));
        return value.substring(0, length).compareTo(textValue);
    }

    /**
     * Private class to implement partial match comparator when searching
     * @author tschmidt
     *
     
    private class MatchComparator implements Comparator<SearchPair> {

        public int compare(SearchPair o1, SearchPair o2) {
            return compareValue(o1.display, o2.display, o2.display.length());
        }
    }
    
    /**
     * Private Default implementation of the Renderer interface.
     * 
     
    protected class DefaultRenderer implements Renderer {
        public String getDisplay(Row row) {
            return row != null ? row.getCells().get(0).toString() : "";
        }
    }    
    
    // ********** Table Keyboard Handling ****************************

    protected class KeyboardHandler implements KeyDownHandler, KeyUpHandler {
        /**
         * This method handles all key down events for this table
        
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
                    int selectLength,start,end;
                    /*
                     * If text is selected we want to select one more position back so the correct 
                     * text will be received in the key up event.
                     
                    selectLength = textbox.getSelectionLength();
                    if(selectLength > 0){
                        selectLength++;
                        
                        start = getText().length() - selectLength;
                        if(start < 0)
                        	start = 0;
                        
                        end = selectLength;
                        if(end > getText().length())
                        	end = getText().length();
                        	
                        textbox.setSelectionRange(start,end);
                    }
            }

        }
        
        /**
         * This method handles all keyup events for the dropdown widget.
         
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
                default:
                    text = getText();

                    /*
                     * Will hit this if backspaced to clear textbox. Call
                     * setSelected 0 so that if user tabs off the value is
                     * selected correctly
                     
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
                     
                    if(getText().length() > cursorPos)
                    	textbox.setSelectionRange(cursorPos, getText().length() - cursorPos);

            }
        }

        /**
         * Method to find the next selectable item in the Dropdown
         * 
         * @param index
         * @return
         
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
    
    */
}
