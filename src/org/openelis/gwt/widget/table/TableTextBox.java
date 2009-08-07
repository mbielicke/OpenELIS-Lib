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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenTextBox;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.TextBox;


/**
 * A TextBox that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
@Deprecated
public class TableTextBox extends TableCellInputWidget {
    
    public TextBox editor;
    private Label display;
    private boolean enabled;
    private int width;
    public static final String TAG_NAME = "table-textbox";
    
    public TableTextBox() {
        editor = new TextBox();  
    }
    
    public void clear() {
        if(editor != null)
            editor.setText("");
        if(display != null)
            display.setText("");
    }

    public TableCellWidget getNewInstance() {
        TableTextBox  textbox = new TableTextBox();
        textbox.editor.setCase(editor.textCase);
        textbox.enabled = enabled;
        textbox.editor.setLength(editor.length);
        if(editor.mask != null)
            textbox.editor.setMask(editor.mask);
        textbox.editor.setTextAlignment(editor.alignment);
        return textbox;
    }

    public void setDisplay() {
        if(display == null){
            display = new Label();
            display.setWordWrap(false);
            display.setWidth(width+"px");
        }
        if(field.getValue() != null){
            String val = field.getValue().toString();
            display.setText(val);
        } else
            display.setText("");
        
        setWidget(display);
        super.setDisplay();
    }

    public void setEditor() {
        if(!enabled)
            return;
        if(field.getValue() != null)
            editor.setText(field.getValue().toString());
        else
            editor.setText("");
        setWidget(editor);
    }

    public TableTextBox(Node node, ScreenBase screen) {
        this.screen = screen;
        ScreenTextBox sbox = new ScreenTextBox(node,screen);
        editor = (TextBox)sbox.getWidget();  
    }

    public void saveValue() {
        String val = editor.getText();
        field.setValue(val);
        super.saveValue();
    }

    public void setField(AbstractField field) {
        this.field = field;
    }

    public void enable(boolean enabled) {
       this.enabled = enabled;
        
    }
    
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null)
            editor.setWidth(width+"px");
        if(display != null)
            display.setWidth(width+"px");
    }
    
    public void setFocus(boolean focused) {
        editor.setFocus(focused);
    }
    
    public void setForm(State state) {
        if(state == State.QUERY){
            editor.setMaxLength(255);
            editor.enforceLength = false;
            editor.enforceMask = false;
            editor.setTextAlignment(TextBox.ALIGN_LEFT);
        }else{
            editor.setMaxLength(editor.length);
            editor.enforceLength = true;
            editor.enforceMask = true;
            editor.setTextAlignment(editor.alignment);
        }
    }

}
