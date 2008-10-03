package org.openelis.gwt.widget;

import com.google.gwt.user.client.Window;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

public class AutoComplete extends DropdownWidget {
    
    public AutoCompleteListener listener = new AutoCompleteListener(this);
    

    AutoCompleteCallInt autoCall;
    String cat;

    
    public AutoComplete(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super(columns, maxRows, width, title, showHeader, showScroll);
        focusPanel.addMouseListener(listener);
        focusPanel.addClickListener(listener);
        textBox.addKeyboardListener(listener);
    }
    
    public void init(String cat, 
                     boolean multi, 
                     String textBoxDefault,
                     String width
                     ){ 
        this.cat = cat;
        this.textBoxDefault = textBoxDefault;
        
        this.multiSelect = multi;
        model.enableMultiSelect(multi);
        if (textBoxDefault != null)
            textBox.setText(textBoxDefault);

        setWidth(width);

    }
    
    public void getMatches(final String text) {
        if(screen != null && ((AppScreen)screen).window != null)
            ((AppScreen)screen).window.setStatus("", "spinnerIcon");
         try {
            autoCall.callForMatches(this, model.getData(), text);

          } catch (Exception e) {
            Window.alert(e.getMessage());
         }
    }
    
    public void showAutoMatches(DataModel data){
        data.multiSelect = multiSelect;
        activeRow = -1;
        activeCell = -1;
        model.load(data);
        showTable(0);
        if(screen != null && ((AppScreen)screen).window != null)
            ((AppScreen)screen).window.setStatus("", "");
    }
    
    public void setAutoCall(AutoCompleteCallInt autoCall) {
        this.autoCall = autoCall;
    }
    

}
