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

import com.google.gwt.user.client.Window;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

public class AutoComplete extends DropdownWidget {
    
    public AutoCompleteListener listener = new AutoCompleteListener(this);
    public boolean queryMode;
    

    AutoCompleteCallInt autoCall;
    public String cat;

    
    public AutoComplete(ArrayList<TableColumnInt> columns, int maxRows, String width, String title, boolean showHeader, VerticalScroll showScroll) {
        super(columns, maxRows, width, title, showHeader, showScroll);
        lookUp.icon.addMouseListener(listener);
        lookUp.icon.addClickListener(listener);
        lookUp.textbox.addKeyboardListener(listener);
        lookUp.setIconStyle("");
        
        this.showAltRowColors = false;
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
            lookUp.textbox.setText(textBoxDefault);

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
