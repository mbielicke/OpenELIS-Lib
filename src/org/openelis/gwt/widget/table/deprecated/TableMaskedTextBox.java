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
package org.openelis.gwt.widget.table.deprecated;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.deprecated.AbstractField;
import org.openelis.gwt.screen.deprecated.ScreenBase;
import org.openelis.gwt.widget.deprecated.MaskedTextBox;

/**
 * A MaskedTextBox that implements a CellWidget so that it can be used in a
 * Table.
 * 
 * @author tschmidt
 * 
 */
@Deprecated
public class TableMaskedTextBox extends TableCellInputWidget {
    
    private MaskedTextBox editor;
    private Label display;
    private String mask;
    private boolean enabled;
    private int width;
    public static final String TAG_NAME = "table-maskedbox";
    
    public TableMaskedTextBox() {
    }

    public void clear() {
        if(editor != null)
            editor.setText("");
        if(display != null)
            display.setText("");
    }

    public TableCellWidget getNewInstance() {
        TableMaskedTextBox tb = new TableMaskedTextBox();
        tb.mask = mask;
        tb.enabled = enabled;
        tb.screen = screen;
        return tb;
    }

    public TableMaskedTextBox(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        this.screen = screen;
        mask = (node.getAttributes().getNamedItem("mask").getNodeValue());
    }

    public void setDisplay() {
        if(display == null){
            display = new Label();
            display.setWordWrap(false);
            display.setWidth(width+"px");
        }
        display.setText((String)field.getValue());
        setWidget(display);
        super.setDisplay();
    }

    public void setEditor() {
        if(!enabled)
            return;
        if(editor == null){
            editor = new MaskedTextBox();
            editor.addFocusListener(this);
            editor.setMask(mask);
            editor.setWidth(width+"px");
        }
        editor.setText((String)field.getValue());
        setWidget(editor);
    }

    public void saveValue() {
        editor.format();
        field.setValue(editor.getText());
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
    }
}
