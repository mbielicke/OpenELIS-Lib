/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.DataObject;


/**
 * A HTML Widget that implements CellWidget so it can be used in a table
 * 
 * @author tschmidt
 * 
 */
public class TableLink extends SimplePanel implements TableCellWidget {
    
    private HTML editor;
    private DataObject field;
    private boolean enabled;
    public static final String TAG_NAME = "table-link";
    
    public TableLink() {
        editor = new HTML();
        editor.setWordWrap(false);
    }

    public void clear() {
        // TODO Auto-generated method stub
    }

    public TableLink(Node node){
        this();
    }
    
    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableLink();
    }

    public TableCellWidget getNewInstance() {
        TableLink tl = new TableLink();
        tl.enabled = enabled;
        return tl;
    }

    public void saveValue() {
        // TODO Auto-generated method stub
        
    }

    public void setDisplay() {
        setEditor();
    }

    public void setEditor() {
        editor.setHTML("<div><a>" + (String)field.getValue() + "</a></div>");
        addStyleName("encounters");
        
    }

    public void setField(DataObject field) {
        this.field = field;
        
    }

    public void enable(boolean enabled) {
      this.enabled = enabled;
        
    }

    public void setCellWidth(int width) {
        // TODO Auto-generated method stub
        
    }
    
    public void setFocus(boolean focused) {

    }
}
