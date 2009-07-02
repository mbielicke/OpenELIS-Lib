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
package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;

import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.table.rewrite.PopupTable;
import org.openelis.gwt.widget.table.rewrite.TableDataRow;
import org.openelis.gwt.widget.table.rewrite.TableKeyboardHandlerInt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

public class DropdownWidget extends PopupTable implements TableKeyboardHandlerInt, PopupListener {
    
    //public HorizontalPanel mainHP = new HorizontalPanel();

   // public TextBox textBox = new TextBox();

    //public FocusPanel focusPanel = new FocusPanel();
    
    public String textBoxDefault = "";
    
    protected String curValue = null;
    
    public boolean multiSelect;
    
    public boolean itemSelected;
    
    int currentCursorPos;
    
    public String popWidth;
    
    public TextBox textbox = new TextBox();
    
    public class Delay extends Timer {
        public String text;

        public Delay(String text, int time) {
            this.text = text;
            this.schedule(time);
        }

        public void run() {
            if (textbox.getText().equals(text)) {
                currentCursorPos = textbox.getText().length();
                getMatches(text);
            }
        }
    };
    
    public DropdownWidget(){
    	super();
    }
    
    public void init() {

      
    }
    
    public void onClick(ClickEvent event) {
    	Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);
    	int row = cell.getRowIndex();
    	int col = cell.getCellIndex();
        if(!canSelect(modelIndexList[row]))
            return;
        if(activeRow > -1 && ((multiSelect && !ctrlKey) || !multiSelect)){
            unselect(-1);
        }
        if(multiSelect && ctrlKey && isSelected(modelIndexList[row])){
            unselect(modelIndexList[row]);
        }else {
            activeRow = row;
            selectRow(modelIndexList[row]);
        }
        if(!multiSelect || (!ctrlKey && multiSelect))
            complete();
    }
    
    private int findNextActive(int current) {
        int next = current + 1;
        while(next < modelIndexList.length && !isEnabled(modelIndexList[next]))
            next++;
        if(next < modelIndexList.length)
            return next;
        view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
        renderer.scrollLoad(view.scrollBar.getScrollPosition());
        return findNextActive(modelIndexList.length-2);
    }
    
    private int findPrevActive(int current) {
        int prev = current - 1;
        while(prev > -1 && !isEnabled(modelIndexList[prev]))
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

        textbox.setText(textValue.trim());
       

        /**
         * This was commented out to fix a problem with IE.  
         * If you need this back than try commenting out complete() in onLostFocus
         * and restest.
         */
        //textBox.setFocus(true);
        
        hideTable();
        
        /**
        if (changeListeners != null){
            if(!textBoxDefault.equals(textValue)){
                textBoxDefault = textValue;
                changeListeners.fireChange(this);
            }
        }
        **/
    }

    public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        if(multiSelect && autoClosed){
            complete();
        }
    }
    
    public String getTextBoxDisplay(){
        String textValue = "";
        ArrayList<TableDataRow> selected = getSelections();
        
        for(int i=0;i<selected.size();i++){
            if(selected.get(i) instanceof TableDataRow){
                 TableDataRow select = selected.get(i);
                 textValue = (String)select.getCells().get(0)
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
        enableMultiSelect(multiSelect);
    }
    
    public void getMatches(String text) {
        
    }
    
    public void setDelay(String text, int delay) {
        new Delay(text, delay);
    }
    
    public void setSelections(ArrayList<Object> selections){
        clearSelections();
        if(selections != null) {
            for(Object key : selections)
                selectRow(key);
            textbox.setText(getTextBoxDisplay());
        }else
            textbox.setText("");
        
        textBoxDefault = textbox.getText();
    }
    
    public void setSelection(Object key) {
        clearSelections();
        selectRow(key);
        textbox.setText(getTextBoxDisplay());
        textBoxDefault = textbox.getText();
    }
    
    public void setFocus(boolean focus) {
        textbox.setFocus(focus);
    }
    


    public void addFocusListener(FocusListener listener) {
        textbox.addFocusListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        textbox.removeFocusListener(listener);

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
        textbox.setWidth(width);
        
    }
    
    public void setWidth(int width){
        setWidth(width+"px");
    }

	public void onKeyDown(KeyDownEvent event) {
        if(!popup.isShowing())
            return;
        if(event.getNativeKeyCode() == KeyboardHandler.KEY_CTRL)
            ctrlKey = true;
        if(event.getNativeKeyCode() == KeyboardHandler.KEY_SHIFT)
            shiftKey = true;
        if (event.getNativeKeyCode()== KeyboardHandler.KEY_DOWN) {
            if(activeRow < 0){
                activeRow = findNextActive(0);
                selectRow(activeRow);    
            }else{
                if(activeRow == view.table.getRowCount() -1){
                    if(modelIndexList[activeRow]+1 < numRows()){                        
                        view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()+cellHeight);
                        renderer.scrollLoad(view.scrollBar.getScrollPosition());
                        findNextActive(activeRow-1);
                        unselect(activeRow);
                        activeRow = view.table.getRowCount() -1;
                        selectRow(modelIndexList[view.table.getRowCount() -1]);
                    }
                }else{
                    int row = findNextActive(activeRow);
                    unselect(activeRow);
                    activeRow = row;
                    selectRow(modelIndexList[activeRow]);
                }
            }
        }
        if (KeyboardHandler.KEY_UP == event.getNativeKeyCode()) {
            if(activeRow == 0){
                if(modelIndexList[activeRow] - 1 > -1){
                    view.scrollBar.setScrollPosition(view.scrollBar.getScrollPosition()-cellHeight);
                    renderer.scrollLoad(view.scrollBar.getScrollPosition());
                    findPrevActive(1);
                    unselect(activeRow);
                    activeRow = 0;
                    selectRow(modelIndexList[0]);
                }
            }else if (activeRow > 0){
                int row = findPrevActive(activeRow);
                unselect(activeRow);
                activeRow = row;
                selectRow(modelIndexList[activeRow]);
            }
        }
        if (KeyboardHandler.KEY_ENTER == event.getNativeKeyCode() || KeyboardHandler.KEY_TAB == event.getNativeKeyCode()) {
            if(activeRow > -1){
                itemSelected = true;
                complete();
            }
        }
        if (KeyboardHandler.KEY_ESCAPE == event.getNativeKeyCode()){
            complete();
        }
		
	}

	public void onKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyboardHandler.KEY_CTRL)
            ctrlKey = false;
        if(event.getNativeKeyCode() == KeyboardHandler.KEY_SHIFT)
            shiftKey = false;
	}
	
}
