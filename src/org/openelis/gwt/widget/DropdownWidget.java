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

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.table.PopupTable;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableKeyboardHandlerInt;
import org.openelis.gwt.widget.table.TableModel;
import org.openelis.gwt.widget.table.TableMouseHandler;
import org.openelis.gwt.widget.table.TableRenderer;
import org.openelis.gwt.widget.table.TableView;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;
import org.openelis.gwt.widget.table.event.TableWidgetListener;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.PopupPanel;

public class DropdownWidget extends PopupTable implements TableKeyboardHandlerInt, 
CloseHandler<PopupPanel>, FocusHandler, BlurHandler, Focusable, HasValueChangeHandlers<ArrayList<DataSet<Object>>> {
    
    public ScreenBase screen;
    
    protected String textBoxDefault = "";
    
    public boolean multiSelect;
    
    public boolean itemSelected;
    
    int currentCursorPos;
    
    public LookUp lookUp = new LookUp();
    
    public class Delay extends Timer {
        public String text;

        public Delay(String text, int time) {
            this.text = text;
            this.schedule(time);
        }

        public void run() {
            if (lookUp.getText().equals(text)) {
                currentCursorPos = lookUp.getText().length();
                getMatches(text);
            }
        }
    };
    
    public DropdownWidget(){
        
    }
    
    public DropdownWidget(ArrayList<TableColumnInt> columns,int maxRows,String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super();
        init(columns,maxRows,width,title,showHeader,showScroll);
    }
    
    public void init(ArrayList<TableColumnInt> columns,int maxRows,String width, String title, boolean showHeader, VerticalScroll showScroll) {
        for(TableColumnInt column : columns) {
            column.setTableWidget(this);
        }
        this.columns = columns;
        this.maxRows = maxRows;
        this.title = title;
        this.showHeader = showHeader;
        renderer = new TableRenderer(this);
        model = new TableModel(this);
        view = new TableView(this,showScroll);
        view.setWidth(width);
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        keyboardHandler = this;
        mouseHandler = new TableMouseHandler(this);
        addTableWidgetListener((TableWidgetListener)renderer);
        setWidget(lookUp);
        lookUp.setStyleName("AutoDropDown");
        lookUp.setIconStyle("AutoDropDownButton");
        lookUp.textbox.setStyleName("TextboxUnselected");
        lookUp.textbox.addFocusHandler(this);
        lookUp.textbox.addBlurHandler(this);
        
        popup.setStyleName("DropdownPopup");
        popup.setWidget(view);
        popup.addCloseHandler(this);
        
        model.setModel(new DataModel());
    }
    
    public void onClick(ClickEvent event) {
    	int row = view.table.getCellForEvent(event).getRowIndex();
        if(!model.canSelect(modelIndexList[row]))
            return;
        if(activeRow > -1 && ((multiSelect && !ctrlKey) || !multiSelect)){
            model.unselectRow(-1);
        }
        if(multiSelect && ctrlKey && model.isSelected(modelIndexList[row])){
            model.unselectRow(modelIndexList[row]);
        }else {
            activeRow = row;
            model.selectRow(modelIndexList[row]);
        }
        if(!multiSelect || (!ctrlKey && multiSelect))
            complete();
    }
    
    public void onKeyDown(KeyDownEvent event) {
        if(!popup.isShowing())
            return;
        if(event.isControlKeyDown())
            ctrlKey = true;
        if(event.isShiftKeyDown())
            shiftKey = true;
        if (event.isDownArrow()) {
            if(activeRow < 0){
                activeRow = findNextActive(0);
                model.selectRow(activeRow);    
            }else{
                if(activeRow == view.table.getRowCount() -1){
                    if(modelIndexList[activeRow]+1 < model.numRows()){                        
                        view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                        renderer.scrollLoad(view.scrollBar.getScrollPosition());
                        findNextActive(activeRow-1);
                        model.unselectRow(activeRow);
                        activeRow = view.table.getRowCount() -1;
                        model.selectRow(modelIndexList[view.table.getRowCount() -1]);
                    }
                }else{
                    int row = findNextActive(activeRow);
                    model.unselectRow(activeRow);
                    activeRow = row;
                    model.selectRow(modelIndexList[activeRow]);
                }
            }
        }
        if (event.isUpArrow()) {
            if(activeRow == 0){
                if(modelIndexList[activeRow] - 1 > -1){
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                    renderer.scrollLoad(view.scrollBar.getScrollPosition());
                    findPrevActive(1);
                    model.unselectRow(activeRow);
                    activeRow = 0;
                    model.selectRow(modelIndexList[0]);
                }
            }else if (activeRow > 0){
                int row = findPrevActive(activeRow);
                model.unselectRow(activeRow);
                activeRow = row;
                model.selectRow(modelIndexList[activeRow]);
            }
        }
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || KeyCodes.KEY_TAB == event.getNativeKeyCode()) {
            if(activeRow > -1){
                itemSelected = true;
                complete();
            }
        }
        if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE){
            complete();
        }
    }
    
    private int findNextActive(int current) {
        int next = current + 1;
        while(next < modelIndexList.length && !model.isEnabled(modelIndexList[next]))
            next++;
        if(next < modelIndexList.length)
            return next;
        view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
        renderer.scrollLoad(view.scrollBar.getScrollPosition());
        return findNextActive(modelIndexList.length-2);
    }
    
    private int findPrevActive(int current) {
        int prev = current - 1;
        while(prev > -1 && !model.isEnabled(modelIndexList[prev]))
            prev--;
        if(prev >  -1)
            return prev;
        view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
        renderer.scrollLoad(view.scrollBar.getScrollPosition());
        return findPrevActive(1);
    }
    
    public void complete() {
        String textValue = "";
         
        textValue = getTextBoxDisplay();

        lookUp.setText(textValue.trim());
        textBoxDefault = textValue;

        /**
         * This was commented out to fix a problem with IE.  
         * If you need this back than try commenting out complete() in onLostFocus
         * and restest.
         */
        //textBox.setFocus(true);
        
        hideTable();
        ValueChangeEvent.fire(this, getSelections());

    }

    public void onKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
            ctrlKey = false;
        if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
            shiftKey = false;
        
    }

    public void onClose(CloseEvent<PopupPanel> event) {
        if(multiSelect && event.isAutoClosed()){
            complete();
        }
    }
    
    public String getTextBoxDisplay(){
        String textValue = "";
        ArrayList<DataSet<Object>> selected = model.getSelections();
        
        for(int i=0;i<selected.size();i++){
            if(selected.get(i) instanceof DataSet){
                 DataSet<Object> select = selected.get(i);
                 textValue = (String)select.get(0).getValue()
                                + (!"".equals(textValue) ? "|" : "") + textValue;
            }else{
                Object select = ((AbstractField)selected.get(i).get(i)).getValue();
                
                String tempTextValue = (String)((DataModel<Object>)model.getData()).getByKey(select).get(0).getValue();
                
                textValue = tempTextValue + (!"".equals(textValue) ? "|" : "") + textValue;
            }
               
        }   
        return textValue;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
        model.enableMultiSelect(multiSelect);
    }
    
    public void getMatches(String text) {
        
    }
    
    public void setDelay(String text, int delay) {
        new Delay(text, delay);
    }

    public void setForm(ScreenBase screen) {
        this.screen = screen;
    }
    
    public void setSelections(ArrayList<DataSet<Object>> selections){
        model.clearSelections();
        if(selections != null) {
            for(DataSet<Object> set : selections)
                model.selectRow(set.getKey());
            lookUp.setText(getTextBoxDisplay());
        }else
            lookUp.setText("");
    }
    
    public ArrayList<DataSet<Object>> getSelections() {
        return model.getSelections();
    }
    
    public void setFocus(boolean focus) {
        lookUp.setFocus(focus);
    }
    
    public void onFocus(FocusEvent event) {
        if (!lookUp.textbox.isReadOnly()) {
            if (event.getSource() == lookUp.textbox) {
                // we need to set the selected style name to the textbox
                lookUp.textbox.addStyleName("TextboxSelected");
                lookUp.textbox.removeStyleName("TextboxUnselected");
                lookUp.textbox.setFocus(true);

                lookUp.icon.addStyleName("Selected");

                //setCurrentValues();
                    
            }
        }
    }

    public void onBlur(BlurEvent event) {
        if (!lookUp.textbox.isReadOnly()) {
            if (event.getSource() == lookUp.textbox) {
                // we need to set the unselected style name to the textbox
                lookUp.textbox.addStyleName("TextboxUnselected");
                lookUp.textbox.removeStyleName("TextboxSelected");

                lookUp.icon.removeStyleName("Selected");

                complete();
            }
        }
    }

    public int getTabIndex() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public void setWidth(String width) {
        lookUp.setWidth(width);
        
    }
    
    public void setWidth(int width){
        setWidth(width+"px");
    }

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<ArrayList<DataSet<Object>>> handler) {
		// TODO Auto-generated method stub
		return addHandler(handler, ValueChangeEvent.getType());
	}

    
}
