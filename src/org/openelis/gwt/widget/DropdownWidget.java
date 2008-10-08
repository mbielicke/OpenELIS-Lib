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
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.StringObject;
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
    
    public HorizontalPanel mainHP = new HorizontalPanel();

    public TextBox textBox = new TextBox();

    public FocusPanel focusPanel = new FocusPanel();
    
    public ScreenBase screen;
    
    protected String textBoxDefault = "";
    
    public boolean multiSelect;
    
    public boolean itemSelected;
    
    public String fieldCase = "upper";
    int currentCursorPos;
    
    public class Delay extends Timer {
        public String text;

        public Delay(String text, int time) {
            this.text = text;
            this.schedule(time);
        }

        public void run() {
            if (textBox.getText().equals(text)) {
                if (fieldCase.equals("upper"))
                    text = text.toUpperCase();
                else if (fieldCase.equals("lower"))
                    text = text.toLowerCase();
                currentCursorPos = textBox.getText().length();
                getMatches(text);
            }
        }
    };
    
    public DropdownWidget(){
        
    }
    
    public DropdownWidget(ArrayList<TableColumnInt> columns,int maxRows,String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super();
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
        
        setWidget(mainHP);
        mainHP.add(textBox);
        textBox.setStyleName("TextboxUnselected");
        textBox.addFocusListener(this);
        textBox.setWidth("100%");
        mainHP.setSpacing(0);
        mainHP.setStyleName("AutoDropdown");
        
        mainHP.add(focusPanel);
        mainHP.setCellHorizontalAlignment(focusPanel, HasAlignment.ALIGN_LEFT);
        focusPanel.setStyleName("AutoDropdownButton");
        
        popup.setStyleName("DropdownPopup");
        popup.setWidget(view);
        popup.addPopupListener(this);
        
        model.setModel(new DataModel());
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

        textBox.setText(textValue.trim());
        textBoxDefault = textValue;

        textBox.setFocus(true);
        
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
        ArrayList selected = model.getSelections();
        
        for(int i=0;i<selected.size();i++){
            if(selected.get(i) instanceof DataSet){
                 DataSet select = (DataSet)selected.get(i);
                 textValue = (String) ((StringObject) select.get(0)).getValue()
                                + (!"".equals(textValue) ? "|" : "") + textValue;
            }else{
                NumberObject select = (NumberObject)selected.get(i);
                
                String tempTextValue = (String)((DataObject)((DataSet)model.getData().getByKey(select)).get(0)).getValue();
                
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
    }
    
    public void getMatches(String text) {
        
    }
    
    public void setDelay(String text, int delay) {
        new Delay(text, delay);
    }

    public void setForm(ScreenBase screen) {
        this.screen = screen;
    }
    
    public void setSelections(ArrayList<DataSet> selections){
        model.clearSelections();
        for(DataSet set : selections)
            model.selectRow(set.getKey());
        textBox.setText(getTextBoxDisplay());
    }
    
    public ArrayList<DataSet> getSelections() {
        return model.getSelections();
    }
    
    public void setFocus(boolean focus) {
        textBox.setFocus(focus);
    }
    
    public void onFocus(Widget sender) {
        if (!textBox.isReadOnly()) {
            if (sender == textBox) {
                // we need to set the selected style name to the textbox
                textBox.addStyleName("TextboxSelected");
                textBox.removeStyleName("TextboxUnselected");
                textBox.setFocus(true);

                focusPanel.addStyleName("Selected");

                //setCurrentValues();
                    
            }
        }
    }

    public void onLostFocus(Widget sender) {
        if (!textBox.isReadOnly()) {
            if (sender == textBox) {
                // we need to set the unselected style name to the textbox
                textBox.addStyleName("TextboxUnselected");
                textBox.removeStyleName("TextboxSelected");

                focusPanel.removeStyleName("Selected");

                //complete();
            }
        }
    }

    public void addFocusListener(FocusListener listener) {
        textBox.addFocusListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        textBox.removeFocusListener(listener);

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
        mainHP.setWidth(width);
    }
    
    public void setWidth(int width){
        setWidth(width+"px");
    }

    
}
