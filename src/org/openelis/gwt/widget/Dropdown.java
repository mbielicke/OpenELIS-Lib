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

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.ScreenPanel;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.widget.deprecated.IconContainer;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRenderer;
import org.openelis.gwt.widget.table.TableView;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

public class Dropdown<T> extends DropdownWidget implements FocusHandler, BlurHandler, HasValue<T>, HasField {
    
    private int startPos;
    public enum Search {LINEAR,BINARY}
    protected Search searchMode = Search.BINARY;
    private Field<T> field;
    IconContainer icon = new IconContainer();
    public String dropwidth;
    public int minWidth;
    HorizontalPanel hp; 
    public boolean queryMode;
    
    private class DropDownListener implements ClickHandler, KeyUpHandler {
        
        private Dropdown widget;
        
        public DropDownListener(Dropdown widget){
            this.widget = widget;
        }

        public void onClick(ClickEvent event) {
            if(!widget.isEnabled())
                return;
            if(event.getSource() == widget.icon){
                if(widget.selectedRow < 0)
                    if(widget.getSelections().size() > 0)
                        widget.showTable((Integer)widget.getSelectedRows()[0]);
                    else
                        widget.showTable(0);
                else
                    widget.showTable(widget.modelIndexList[widget.selectedRow]);
            }

        }

        public void onKeyUp(KeyUpEvent event) {
            if(!widget.isEnabled())
                return;
            if (!widget.textbox.isReadOnly()) {
            	int keyCode = event.getNativeKeyCode();
                if (keyCode == KeyboardHandler.KEY_DOWN || keyCode == KeyboardHandler.KEY_UP ||  keyCode == KeyboardHandler.KEY_TAB 
                        || keyCode == KeyboardHandler.KEY_LEFT || keyCode == KeyboardHandler.KEY_RIGHT || keyCode == KeyboardHandler.KEY_ALT || 
                        keyCode == KeyboardHandler.KEY_CTRL || keyCode == KeyboardHandler.KEY_SHIFT || keyCode == KeyboardHandler.KEY_ESCAPE)
                    return;
                if(keyCode == KeyboardHandler.KEY_ENTER && !widget.popup.isShowing() && !widget.itemSelected){
                    if(widget.selectedRow < 0)
                        widget.showTable(0);
                    else
                        widget.showTable(widget.modelIndexList[widget.selectedRow]);
                    return;
                }
                if(keyCode == KeyboardHandler.KEY_ENTER && widget.itemSelected){
                    widget.itemSelected = false;
                    return;
                }
                String text = widget.textbox.getText();
                if (text.length() > 0 && !text.endsWith("*")) {
                    widget.setDelay(text, 350);
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
        setStyleName("AutoDropDown");
        icon.setStyleName("AutoDropDownButton");
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
    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void getMatches(String match) {
        ArrayList<TableDataRow> model = this.getData();
        int tempStartPos = -1;
        int index = getIndexByTextValue(match);
        

        if (index > -1 && index < model.size()) {
            tempStartPos = index;
            this.startPos = index;
        }

        if (tempStartPos == -1 && !textbox.getText().equals("")) {
            // set textbox text back to what it was before
            textbox.setText(textbox.getText().substring(0, currentCursorPos));
            this.startPos = 0;
            index = getIndexByTextValue(textbox.getText()); 

            if (index > -1 && index < model.size()) {
                tempStartPos = index;
                this.startPos = index;
            }else{
                textbox.setText("");
                tempStartPos = 0;
                return;
            }
        }
        if(textbox.getStyleName().indexOf("Focus") > -1)
        	showTable(this.startPos);
        else
        	setValue((T)model.get(this.startPos).key,true);
    }
    
    private int getIndexByTextValue(String textValue) {
        if(textValue.equals(""))
            return -1;
        ArrayList<TableDataRow> model = this.getData();
        int low = 0;
        int high = model.size() - 1;
        int mid = -1;
        int length = textValue.length();
        
        if(searchMode == Search.LINEAR){
            for(int i = 0; i < model.size(); i++){
                if(compareValue((String)model.get(i).getCells().get(0),textValue,length) == 0)
                    return i;
            }
            return -1;
        }else{
            //we first need to do a binary search to 
            while (low <= high) {
                mid = (low + high) / 2;

                if (compareValue((String)model.get(mid).getCells().get(0),textValue,length) < 0)
                    low = mid + 1;
                else if (compareValue((String)model.get(mid).getCells().get(0),textValue,length) > 0)
                    high = mid - 1;
                else
                    break;
            }
        
            if(low > high)
                return -1; // NOT FOUND
            else{
                //we need to do a linear search backwards to find the first entry that matches our search
                while(mid > -1 && compareValue((String)model.get(mid).getCells().get(0),textValue,length) == 0)
                    mid--;
            
                return (mid+1);
            }
        }
    }
    
    private int compareValue(String value, String textValue, int length) {
        if(value.length() < length)
            return -1;
        return value.substring(0,length).toUpperCase().compareTo(textValue.toUpperCase());
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

    public T getValue() {
        if(getSelectedRow() > -1)
            return (T)getRow(getSelectedRow()).key;
        else
            return null;
    }

    public void setValue(T value) {
        setValue(value,false);
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


	public void addException(LocalizedException error) {
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
		if(!queryMode)
			field.checkValue(this);
	}
	
	public void getQuery(ArrayList list, String key) {
		if(!queryMode)
			return;
		ArrayList<TableDataRow> selections = getSelections();
		if(selections.size() == 1 && selections.get(0).key ==  null)
			return;
		if(selections.size() > 0) {
			QueryData qd = new QueryData();
			qd.key = key;
			if(field instanceof StringField)
				qd.type = QueryData.Type.STRING;
			else if(field instanceof IntegerField)
				qd.type = QueryData.Type.INTEGER;
			qd.query = "";
			for(TableDataRow row : selections) {
				if(selections.indexOf(row) > 0)
					qd.query += "|";
				qd.query += row.key.toString();
			}
			list.add(qd);
		}
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
	public ArrayList<LocalizedException> getExceptions() {
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
	
	public void setSearchMode(Search mode) {
		searchMode = mode;
	}

}

