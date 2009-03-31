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

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.TableDataModel;
import org.openelis.gwt.common.data.TableDataRow;
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

import java.util.ArrayList;

public class DropdownWidget extends PopupTable implements TableKeyboardHandlerInt, PopupListener, FocusListener, HasFocus {
    
    //public HorizontalPanel mainHP = new HorizontalPanel();

   // public TextBox textBox = new TextBox();

    //public FocusPanel focusPanel = new FocusPanel();
    
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
        lookUp.textbox.addFocusListener(this);
        
        /*
        setWidget(mainHP);
        mainHP.add(textBox);
        textBox.setStyleName("TextboxUnselected");
        textBox.addFocusListener(this);
        textBox.setWidth("auto");
        mainHP.setSpacing(0);
        mainHP.setStyleName("AutoDropdown");
        
        mainHP.add(focusPanel);
        mainHP.setCellHorizontalAlignment(focusPanel, HasAlignment.ALIGN_LEFT);
        focusPanel.setStyleName("AutoDropdownButton");
        */
        popup.setStyleName("DropdownPopup");
        popup.setWidget(view);
        popup.addPopupListener(this);
        
        model.setModel(new TableDataModel<TableDataRow<Object>>());
    }
    
    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
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
    
    public void onKeyDown(Widget sender, char code, int modifiers) {
        if(!popup.isShowing())
            return;
        if(code == KeyboardListener.KEY_CTRL)
            ctrlKey = true;
        if(code == KeyboardListener.KEY_SHIFT)
            shiftKey = true;
        if (KeyboardListener.KEY_DOWN == code) {
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
        if (KeyboardListener.KEY_UP == code) {
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
        if (KeyboardListener.KEY_ENTER == code || KeyboardListener.KEY_TAB == code) {
            if(activeRow > -1){
                itemSelected = true;
                complete();
            }
        }
        if (KeyboardListener.KEY_ESCAPE == code){
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

        if (changeListeners != null)
            changeListeners.fireChange(this);
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        if(keyCode == KeyboardListener.KEY_CTRL)
            ctrlKey = false;
        if(keyCode == KeyboardListener.KEY_SHIFT)
            shiftKey = false;
        
    }

    public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        if(multiSelect && autoClosed){
            complete();
        }
    }
    
    public String getTextBoxDisplay(){
        String textValue = "";
        ArrayList<TableDataRow<Object>> selected = model.getSelections();
        
        for(int i=0;i<selected.size();i++){
            if(selected.get(i) instanceof TableDataRow){
                 TableDataRow<? extends Object> select = selected.get(i);
                 textValue = (String)select.getCells().get(0).getValue()
                                + (!"".equals(textValue) ? "|" : "") + textValue;
            }/*else{
                Object select = ((AbstractField)selected.get(i).getCells().get(i)).getValue();
                
                String tempTextValue = (String)((TableDataModel<TableDataRow<? extends Object>>)model.getData()).getByKey(select).get(0).getValue();
                
                textValue = tempTextValue + (!"".equals(textValue) ? "|" : "") + textValue;
            }
             */  
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
    
    public void setSelections(ArrayList<Object> selections){
        model.clearSelections();
        if(selections != null) {
            for(Object key : selections)
                model.selectRow(key);
            lookUp.setText(getTextBoxDisplay());
        }else
            lookUp.setText("");
    }
    
    public void setSelection(Object key) {
        model.clearSelections();
        model.selectRow(key);
        lookUp.setText(getTextBoxDisplay());
    }
    
    public <T extends TableDataRow> ArrayList<T> getSelections() {
        return (ArrayList<T>)model.getSelections();
    }
    
    public void setFocus(boolean focus) {
        lookUp.setFocus(focus);
    }
    
    public void onFocus(Widget sender) {
        if (!lookUp.textbox.isReadOnly()) {
            if (sender == lookUp.textbox) {
                // we need to set the selected style name to the textbox
                lookUp.textbox.addStyleName("TextboxSelected");
                lookUp.textbox.removeStyleName("TextboxUnselected");
                lookUp.textbox.setFocus(true);

                lookUp.icon.addStyleName("Selected");

                //setCurrentValues();
                    
            }
        }
    }

    public void onLostFocus(Widget sender) {
        if (!lookUp.textbox.isReadOnly()) {
            if (sender == lookUp.textbox) {
                // we need to set the unselected style name to the textbox
                lookUp.textbox.addStyleName("TextboxUnselected");
                lookUp.textbox.removeStyleName("TextboxSelected");

                lookUp.icon.removeStyleName("Selected");

                complete();
            }
        }
    }

    public void addFocusListener(FocusListener listener) {
        lookUp.textbox.addFocusListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        lookUp.textbox.removeFocusListener(listener);

    }

    public int getTabIndex() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setAccessKey(char key) {
        // TODO Auto-generated method stub
        
    }

    public void setTabIndex(int index) {
        // TODO Auto-generated method stub
        
    }

    public void addKeyboardListener(KeyboardListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void removeKeyboardListener(KeyboardListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    public void setWidth(String width) {
        lookUp.setWidth(width);
        
    }
    
    public void setWidth(int width){
        setWidth(width+"px");
    }

    
}
