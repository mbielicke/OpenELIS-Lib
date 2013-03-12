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
import java.util.Collections;
import java.util.Comparator;

import org.openelis.ui.common.data.QueryData;
import org.openelis.gwt.screen.ScreenPanel;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.widget.table.ColumnComparator;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRenderer;
import org.openelis.gwt.widget.table.TableView;
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
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Dropdown<T> extends DropdownWidget implements FocusHandler, BlurHandler, HasValue<T>, HasField {
    
    private Field<T> field;
    IconContainer icon = new IconContainer();
    public String dropwidth;
    public int minWidth;
    HorizontalPanel hp; 
    public ArrayList<TableDataRow> searchText;
    private static PartialCompare partialCompare;
    private int delay = 0;

    private boolean noBlur;
    
    static {
        partialCompare = new PartialCompare();
    }
    
    private class DropDownListener implements ClickHandler, KeyUpHandler {
        
        private Dropdown<T> widget;
        
        public DropDownListener(Dropdown<T> widget){
            this.widget = widget;
        }

        public void onClick(ClickEvent event) {
            if(!widget.isEnabled())
                return;
            if(event.getSource() == widget.icon){
                if(widget.selectedRow < 0) {
                    if(widget.getSelections().size() > 0)
                    	selectRow((Integer)widget.getSelectedRows()[0]);
                }
                widget.showTable();
                noBlur = true;
                setFocus(false);
                
            }

        }

        public void onKeyUp(KeyUpEvent event) {
            if(!widget.isEnabled())
                return;
            if (!widget.textbox.isReadOnly()) {
            	int keyCode = event.getNativeKeyCode();
                if (keyCode == KeyCodes.KEY_DOWN || keyCode == KeyCodes.KEY_UP ||  keyCode == KeyCodes.KEY_TAB 
                        || keyCode == KeyCodes.KEY_LEFT || keyCode == KeyCodes.KEY_RIGHT || keyCode == KeyCodes.KEY_ALT || 
                        keyCode == KeyCodes.KEY_CTRL || keyCode == KeyCodes.KEY_SHIFT || keyCode == KeyCodes.KEY_ESCAPE)
                    return;
                if(keyCode == KeyCodes.KEY_ENTER && !widget.popup.isShowing() && !widget.itemSelected){
                    if(widget.selectedRow < 0) {
                        if(widget.getSelections().size() > 0)
                        	selectRow((Integer)widget.getSelectedRows()[0]);
                    }
                    widget.showTable();
                    return;
                }
                if(keyCode == KeyCodes.KEY_ENTER && widget.itemSelected){
                    widget.itemSelected = false;
                    return;
                }
                String text = widget.textbox.getText();
                if (text.length() > 0 && !text.endsWith("*")) {
                    if (textbox.getText().equals(text)) {
                        currentCursorPos = textbox.getText().length();
                        try {
                        	getMatches(text);
                        }catch(Exception e) {
                        	e.printStackTrace();
                        }
                    }
                    //widget.setDelay(text, delay);
                } else if(text.length() == 0){
                    widget.selectedRow = 0;
                    widget.selectRow(0);
                    widget.scrollToSelection();
                }else{
                    widget.hideTable();
                }
            }
            
        }

    }
    

    public DropDownListener listener = new DropDownListener(this);
    
    public Dropdown() {
    	super();
    }
    
    public void setup() {
        isDropdown = true;
    	if(maxRows == 0)
    		maxRows = 10;
        renderer = new TableRenderer(this);
        view = new TableView(this,showScroll);
        view.setWidth(width);
        setWidth(dropwidth);
        view.setHeight(maxRows*cellHeight);
        keyboardHandler = this;
        hp = new HorizontalPanel();
        hp.add(textbox);
        hp.add(icon);
        setWidget(hp);
        hp.setWidth(dropwidth);
        int index = dropwidth.indexOf("px");
		if(index > 0)
			minWidth = Integer.parseInt(dropwidth.substring(0,index));
		else
			minWidth = Integer.parseInt(dropwidth);
        setStyleName("AutoDropdown");
        icon.setStyleName("AutoDropdownButton");
        textbox.setStyleName("TextboxUnselected");
        textbox.addFocusHandler(this);
        textbox.addBlurHandler(this);
        popup.setStyleName("DropdownPopup");
        popup.setWidget(view);
        popup.addCloseHandler(this);
        icon.addClickHandler(listener);
        icon.addFocusHandler(this);
        textbox.addKeyUpHandler(listener);
        textbox.setReadOnly(!enabled);
       
        addDomHandler(keyboardHandler,KeyDownEvent.getType());
        addDomHandler(keyboardHandler,KeyUpEvent.getType());
        addBlurHandler(new BlurHandler() {
			
			public void onBlur(BlurEvent event) {
				if(popup.isShowing()){
					doBlur();
				}
			}
		});        	
    }
    
    private void doBlur() {
    	if(noBlur) {
    		noBlur = false;
    		return;
    	}
    	
        String textValue = getTextBoxDisplay();

        textbox.setText(textValue.trim());
        
        hideTable();
		field.setValue(getValue());
		ValueChangeEvent.fire(this, getValue());
		field.clearExceptions(this);
		checkValue();
		activeWidget = null;
    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void getMatches(String match) {
    	//match = match.replaceAll("//","////");
        int index = getIndexByTextValue(match);

        if (index == -1 && !textbox.getText().equals("")) {
            textbox.setText(textbox.getText().substring(0, currentCursorPos-1));
        }else{
        	if(popup.isShowing()){
        		selectRow(index);
        	    scrollToSelection();
        	}else if(textbox.getStyleName().indexOf("Focus") > -1){
        		selectRow(index);
        		showTable();
        	}else{
        		setValue((T)model.get(index).key,true);
        		field.checkValue(this);
        		field.drawExceptions(this);
        	}
        }
    }
    
    private int getIndexByTextValue(String textValue) {
        int index = -1, lindex;
        ArrayList<TableDataRow> model;
        
    	if(textValue.length() < 1)
    		return index;

    	textValue = textValue.toUpperCase();
    	model = this.getData();

    	if(searchText == null) {
    		searchText = new ArrayList<TableDataRow>();
    		for(int i = 0; i < model.size(); i++) 
    			searchText.add(new TableDataRow(i,((String)model.get(i).cells.get(0).getValue()).toUpperCase()));
    		Collections.sort(searchText, new ColumnComparator(0,SortEvent.SortDirection.ASCENDING));
    	}
    	index = Collections.binarySearch(searchText,new TableDataRow(null,textValue), partialCompare);

    	if(index < 0) {
    		return -1;
    	}else{
    		//we need to do a linear search backwards to find the first entry that matches our search
    	    index--;
            while(index > -1 && partialCompare.compare(searchText.get(index),textValue) == 0)
    			index--;

    		lindex = (((Integer)searchText.get(index+1).key)).intValue();
    		
    		return isEnabled(lindex) ? lindex : -1;
    	}
    }
    
    public void setModel(ArrayList<TableDataRow> model){
        this.load((ArrayList<TableDataRow>)model);
    }
    
    public void enable(boolean enabled) {
        this.enabled = enabled;
        textbox.setReadOnly(!enabled);
        icon.enable(enabled);
        super.enable(enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public void setText(String text) {
    	textbox.setText(text);
    }

    public T getValue() {
        if(getSelectedRow() > -1)
            return (T)getRow(getSelectedRow()).key;
        else
            return null;
    }

    public void setValue(T value) {
        setValue(value,false);
    }

    public void clearSelection() {
    	unselect(-1);
    	textbox.setText("");
    }
    
    public void setValue(T value, boolean fireEvents) {
        T old = getValue();
       	setSelection(value);
        if(fireEvents)
            ValueChangeEvent.fireIfNotEqual(this, old, value);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler,ValueChangeEvent.getType());
    }
    
	@Override
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler handler) {
		return addValueChangeHandler(handler);
	}


	public void addException(Exception error) {
		field.addException(error);
		field.drawExceptions(this);
	}

	public void clearExceptions() {
		field.clearExceptions(this);
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
		//addValueChangeHandler(field);
		addBlurHandler(field);
		textbox.addMouseOutHandler(field);
		textbox.addMouseOverHandler(field);
	}
	
    @Override
    public void setFocus(boolean focus) {
    	textbox.setFocus(focus);
    }

	public void setQueryMode(boolean query) {
		if(queryMode == query)
			return;
		queryMode = query;
		if(query){
			setMultiSelect(true);
		}else
			setMultiSelect(false);
	}
	
	@Override
	public void checkValue() {
		if(!queryMode){
			field.checkValue(this);
		}
	}
	
	public void getQuery(ArrayList list, String key) {
		if(!queryMode)
			return;
		ArrayList<TableDataRow> selections = getSelections();
		if(selections.size() == 1 && selections.get(0).key == null)
			return;
		if(selections.size() > 0) {
			QueryData qd = new QueryData();
			qd.setKey(key);
			if(field instanceof StringField)
				qd.setType(QueryData.Type.STRING);
			else if(field instanceof IntegerField)
				qd.setType(QueryData.Type.INTEGER);
			qd.setQuery("");
			for(TableDataRow row : selections) {
				if(selections.indexOf(row) > 0)
					qd.setQuery(qd.getQuery() + "|");
				if(row.key == null)
					qd.setQuery(qd.getQuery() + "NULL");
				else
					qd.setQuery(qd.getQuery() + row.key.toString());
			}
			list.add(qd);
		}
	}
	
	public void setQuery(QueryData qd) {
		String[] keys;
		ArrayList<Object> selections;
		
		if(qd == null) {
			clearSelections();
			textbox.setText("");
			return;
		}
		
		keys = qd.getQuery().split("\\|");
		
		selections = new ArrayList<Object>();
		
		for(String key : keys) {
			if(qd.getType() == QueryData.Type.INTEGER) 
				selections.add(Integer.valueOf(key));
			else
				selections.add(key);
		}
		
		setSelections(selections);
	}
	
	public void onBlur(BlurEvent event) {
		textbox.removeStyleName("Focus");
		if(!queryMode)
			BlurEvent.fireNativeEvent(Document.get().createBlurEvent(), this);
	}
	
	public void onFocus(FocusEvent event) {
		if(event.getSource() instanceof ScreenPanel){
			if(((ScreenPanel)event.getSource()).focused != this){
				textbox.removeStyleName("Focus");
				return;
			}
		}
		if(isEnabled())
			textbox.addStyleName("Focus");
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
		view.setWidth(dropwidth);
		super.setWidth(wid+"px");
	}
	
	@Override
	public ArrayList<Exception> getExceptions() {
		return field.exceptions;
	}
	
	@Override
	public void complete() {
		super.complete();
		field.setValue(getValue());
		ValueChangeEvent.fire(this, getValue());
		field.clearExceptions(this);
		checkValue();
		textbox.setFocus(true);
		activeWidget = null;
	}
	@Override
	public void setFieldValue(Object value) {
		setValue((T)value);
	}
	
	@Override
	public Object getFieldValue() {
		return getValue();
	}
	
	@Override
	public void addExceptionStyle(String style) {
		textbox.addStyleName(style);
	}
	
	@Override
	public void removeExceptionStyle(String style) {
		textbox.removeStyleName(style);
	}
	
	public Object getWidgetValue() {
		return getValue();
	}
	
    private static class PartialCompare implements Comparator<TableDataRow> {
        public int compare(TableDataRow o1, TableDataRow o2) {
            String full, partial;
            
            full = (String)o1.cells.get(0).getValue();
            partial = (String)o2.cells.get(0).getValue();
            return compare(full, partial);
        }
        public int compare(TableDataRow o1, String partial) {
            String full;
            
            full = (String)o1.cells.get(0).getValue();
            return compare(full, partial);
        }
        public int compare(String full, String partial) {
            if (full.startsWith(partial))
                return 0;
            return full.compareTo(partial);
        }
    }

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public ArrayList<Object> getSelectionKeys() {
		ArrayList<Object> ret = new ArrayList<Object>();
		ArrayList<TableDataRow> selections = getSelections();
		for(TableDataRow row : selections) 
			ret.add(row.key);
		return ret;
	}
	
	public void setSelectionKeys(ArrayList<Object> selections) {
		if(multiSelect)
			ctrlKey = true;
		setSelections(selections);
		ctrlKey = false; 
		if(selections != null && selections.size() > 0)
			field.setValue((T)selections.get(0));
		else
			field.setValue(null);
	}
}

