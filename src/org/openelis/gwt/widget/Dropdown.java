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

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

public class Dropdown extends DropdownWidget {
    
    private int startPos;
    boolean linear;
    /*
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
*/
    public DropDownListener listener = new DropDownListener(this);
    
    public Dropdown() {
        
    }
    
    private boolean enabled;
    
    public Dropdown(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll) {
        setup(columns,maxRows,width,title,showHeader,showScroll);
    }
    
    public void setup(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super.init(columns,maxRows,width,title,showHeader,showScroll);
        lookUp.addMouseListener(listener);
        lookUp.addClickListener(listener);
        lookUp.textbox.addKeyboardListener(listener);
        lookUp.textbox.setReadOnly(!enabled);
        lookUp.textbox.removeFocusListener(this);
        if(enabled)
            lookUp.textbox.addFocusListener(this);
        
        this.showAltRowColors = false;
    }
    
    public void init(boolean multi, 
                     String width
                     ){ 
        
        this.multiSelect = multi;
        model.enableMultiSelect(multi);
        if (textBoxDefault != null)
            lookUp.textbox.setText(textBoxDefault);

       setWidth(width);
    }
    
    public void getMatches(String match) {
        DataModel<Object> model = (DataModel<Object>)this.model.getData();
        int tempStartPos = -1;
        int index = getIndexByTextValue(match);
        

        if (index > -1 && index < model.size()) {
            tempStartPos = index;
            this.startPos = index;
        }

        if (tempStartPos == -1 && !lookUp.textbox.getText().equals("")) {
            // set textbox text back to what it was before
            lookUp.textbox.setText(lookUp.textbox.getText().substring(0, currentCursorPos));
            this.startPos = 0;
            index = getIndexByTextValue(lookUp.textbox.getText()); 

            if (index > -1 && index < model.size()) {
                tempStartPos = index;
                this.startPos = index;
            }else{
                lookUp.textbox.setText("");
                tempStartPos = 0;
                return;
            }
        }
        showTable(this.startPos);
    }
    
    private int getIndexByTextValue(String textValue) {
        if(textValue.equals(""))
            return -1;
        DataModel<Object> model = (DataModel<Object>)this.model.getData();
        int low = 0;
        int high = model.size() - 1;
        int mid = -1;
        int length = textValue.length();
        
        if(linear){
            for(int i = 0; i < model.size(); i++){
                if(((String) model.get(i).get(0).getValue()).substring(0,length).toUpperCase().compareTo(textValue.toUpperCase()) == 0)
                    return i;
            }
            return -1;
        }else{
            //we first need to do a binary search to 
            while (low <= high) {
                mid = (low + high) / 2;

                if (compareValue((String)model.get(mid).get(0).getValue(),textValue,length) < 0)
                    low = mid + 1;
                else if (compareValue((String)model.get(mid).get(0).getValue(),textValue,length) > 0)
                    high = mid - 1;
                else
                    break;
            }
        
            if(low > high)
                return -1; // NOT FOUND
            else{
                //we need to do a linear search backwards to find the first entry that matches our search
                while(mid > -1 && compareValue((String)model.get(mid).get(0).getValue(),textValue,length) == 0)
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
    
    public void setModel(DataModel<? extends Object> model){
        this.model.load((DataModel<Object>)model.clone());
    }
    
    public void enabled(boolean enabled) {
        this.enabled = enabled;
        lookUp.textbox.setReadOnly(!enabled);
        lookUp.textbox.removeFocusListener(this);
        if(enabled)
            lookUp.textbox.addFocusListener(this);
        lookUp.icon.enable(enabled);
        super.enabled(enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
