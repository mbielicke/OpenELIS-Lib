package org.openelis.gwt.widget;

import com.google.gwt.user.client.Timer;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

public class Dropdown extends DropdownWidget {
    
    private int startPos;
    boolean linear;
    
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

    public DropDownListener listener = new DropDownListener(this);
    
    public Dropdown() {
        
    }
    
    public Dropdown(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super(columns, maxRows, width, title, showHeader, showScroll);
        focusPanel.addMouseListener(listener);
        focusPanel.addClickListener(listener);
        textBox.addKeyboardListener(listener);
    }
    
    public void init(boolean multi, 
                     String width
                     ){ 
        
        this.multiSelect = multi;
        model.enableMultiSelect(multi);
        if (textBoxDefault != null)
            textBox.setText(textBoxDefault);

       setWidth(width);
    }
    
    public void getMatches(String match) {
        DataModel model = this.model.getData();
        int tempStartPos = -1;
        int index = getIndexByTextValue(match);
        

        if (index > -1 && index < model.size()) {
            tempStartPos = index;
            this.startPos = index;
        }

        if (tempStartPos == -1 && !textBox.getText().equals("")) {
            // set textbox text back to what it was before
            textBox.setText(textBox.getText().substring(0, currentCursorPos));
            this.startPos = 0;
            index = getIndexByTextValue(textBox.getText()); 

            if (index > -1 && index < model.size()) {
                tempStartPos = index;
                this.startPos = index;
            }else{
                textBox.setText("");
                tempStartPos = 0;
                return;
            }
        }
        showTable(this.startPos);
    }
    
    private int getIndexByTextValue(String textValue) {
        if(textValue.equals(""))
            return -1;
        DataModel model = this.model.getData();
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
    
    public void setModel(DataModel model){
        this.model.load(model);
    }

}
