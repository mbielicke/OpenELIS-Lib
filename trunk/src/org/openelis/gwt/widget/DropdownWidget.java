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

import org.openelis.gwt.widget.table.PopupTable;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableKeyboardHandlerInt;
import org.openelis.gwt.widget.table.TableRow;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;


public class DropdownWidget extends PopupTable implements TableKeyboardHandlerInt, CloseHandler<PopupPanel> {
    
    public String textBoxDefault = "";
    
    protected String curValue = null;
    
    public boolean multiSelect;
    
    public boolean itemSelected;
    
    int currentCursorPos;
    
    public String popWidth;
    
    public boolean queryMode;
    
    public TextBox textbox = new TextBox() {
    	@Override
    	protected void delegateEvent(Widget target, GwtEvent<?> event) {
    		// TODO Auto-generated method stub
    		super.delegateEvent(getParent(), event);
    	}
    };
    
    public class Delay extends Timer {
        public String text;

        public Delay(String text, int time) {
            this.text = text;
            if (time <= 0)
                run();
            else
                schedule(time);
        }

        public void run() {
            if (textbox.getText().equals(text)) {
                currentCursorPos = textbox.getText().length();
                try {
                	getMatches(text);
                }catch(Exception e) {
                	e.printStackTrace();
                }
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
    	if(getHandlerCount(BeforeSelectionEvent.getType())> 0){
    		BeforeSelectionEvent<TableRow> be = BeforeSelectionEvent.fire(this, renderer.rows.get(row)); 
    		if(be.isCanceled())
    			return;
    	}else if(!isEnabled()){
    		return;
    	}
        if(selectedRow > -1 && ((multiSelect && !ctrlKey) || !multiSelect)){
            unselect(-1);
        }
        if(multiSelect && ctrlKey && isSelected(modelIndexList[row])){
            unselect(modelIndexList[row]);
        }else {
            selectedRow = row;
            selectRow(modelIndexList[row]);
        }
        SelectionEvent.fire(this, renderer.rows.get(row));
        if(!multiSelect || (!ctrlKey && multiSelect))
            complete();
       
    }
    
    public void complete() {
        String textValue = "";
         
        textValue = getTextBoxDisplay();

        textbox.setText(textValue.trim());
        
        hideTable();
        
    }

    
    public void onClose(PopupPanel sender, boolean autoClosed) {

    }
    
    public String getTextBoxDisplay(){
        String textValue = "";
        ArrayList<TableDataRow> selected = getSelections();
        
        for(int i=0;i<selected.size();i++){
            if(selected.get(i) instanceof TableDataRow){
            	if(selected.get(i).display != null)
            		textValue = selected.get(i).display;
            	else {
            		if(columns.get(0).getColumnWidget() instanceof Dropdown){
            			((Dropdown)columns.get(0).getColumnWidget()).setValue(selected.get(i).getCells().get(0));
            			return ((Dropdown)columns.get(0).getColumnWidget()).getTextBoxDisplay();
            		}
            		TableDataRow select = selected.get(i);
            		textValue = (String)select.getCells().get(0)
            			        + (!"".equals(textValue) ? "|" : "") + textValue;
            	}
            }
        }
        if(textValue.equalsIgnoreCase("NULL"))
            return "";
        return textValue.trim();
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }
    
    protected void getMatches(String text) throws Exception {
    	
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
        enableMultiSelect(multiSelect);
    }
    
    
    public void setDelay(String text, int delay) {
        new Delay(text, delay);
    }
    
    public void setSelections(ArrayList<Object> selections){
    	if(isMultiSelect())
    		ctrlKey = true;
        clearSelections();
        if(selections != null) {
            for(Object key : selections)
                selectRow(key);
            textbox.setText(getTextBoxDisplay());
        }else
            textbox.setText("");
        ctrlKey = false;
        	
        //textBoxDefault = textbox.getText();
    }    
    
    public void setSelection(Object key) {
        unselect(-1);
        if(!queryMode && key != null)
        	selectRow(key);
        textbox.setText(getTextBoxDisplay());
        //textBoxDefault = textbox.getText();
    }
    
    public void setFocus(boolean focus) {
        textbox.setFocus(focus);
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
        event.stopPropagation();
        if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
            ctrlKey = true;
        if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
            shiftKey = true;
        if (KeyCodes.KEY_DOWN == event.getNativeKeyCode()) {
            if (selectedRow >= 0 && selectedRow < numRows() - 1) {
            	final int row = findNextActive(selectedRow);
            	if(!isRowDrawn(row)){
            		view.setScrollPosition(view.top+(cellHeight*(row-selectedRow)));
            	}
                selectRow(row);
            }else if(selectedRow < 0){
            	selectRow(0);
            }
        }
        if (KeyCodes.KEY_UP == event.getNativeKeyCode()) {
            if (selectedRow > 0) {
                final int row = findPrevActive(selectedRow);
            	if(!isRowDrawn(row)){
            		view.setScrollPosition(view.top-(cellHeight*(selectedRow-row)));
            	}
                selectRow(row);
            }
        }
        if (KeyCodes.KEY_ENTER == event.getNativeKeyCode() || KeyCodes.KEY_TAB == event.getNativeKeyCode()) {
        	if(selectedRow > -1){
                itemSelected = true;
                SelectionEvent.fire(this, renderer.rows.get(tableIndex(selectedRow)));
                complete();
            }
        }
        if (KeyCodes.KEY_ESCAPE == event.getNativeKeyCode()){
            complete();
        }
		
	}
	
    private int findNextActive(int current) {
        int next = current + 1;
        while(next < numRows() && !isEnabled(next))
            next++;
        if(next < numRows())
            return next;
        return findNextActive(next);
    }
    
    private int findPrevActive(int current) {
        int prev = current - 1;
        while(prev > -1 && !isEnabled(prev))
            prev--;
        if(prev >  -1)
            return prev;
        return findPrevActive(1);
    }

	public void onKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_CTRL)
            ctrlKey = false;
        if(event.getNativeKeyCode() == KeyCodes.KEY_SHIFT)
            shiftKey = false;
	}

	public void onClose(CloseEvent<PopupPanel> event) {
        if(event.isAutoClosed())
            complete();		
	}
	
	public void setCase(TextBox.Case textCase) {
		textbox.setCase(textCase);
	}
	
	
}
