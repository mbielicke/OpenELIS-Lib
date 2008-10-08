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

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenLabel;


/**
 * A Label that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableDragLabel extends SimplePanel implements TableCellWidget {
    
    private ScreenLabel editor;
    private DataObject field;
    private int width;
    private NumberFormat displayMask;
    public static final String TAG_NAME = "table-dragLabel";
    public int rowIndex;

    
    public TableDragLabel() {
        editor = new ScreenLabel("",null);
        editor.sinkEvents(Event.MOUSEEVENTS);
        editor.addMouseListener((MouseListener)ClassFactory.forName("ProxyListener"));
    }

    public void clear() {
        if(editor != null)
            ((Label)editor.getWidget()).setText("");
    }

    public TableCellWidget getNewInstance() {
        TableDragLabel label = new TableDragLabel();
        label.width = width;
        label.displayMask = displayMask;
        label.editor.dropTargets = editor.dropTargets;
        label.editor.dropListeners = editor.dropListeners;
        label.editor.screen = editor.screen;
        return label;
    }

    public Widget getInstance(Node node) {
        return new TableDragLabel();
    }
    
    public TableDragLabel(Node node, ScreenBase screen){
        this();
        editor.screen = screen;
        if (node.getAttributes().getNamedItem("drop") != null) {
            String listener = node.getAttributes()
                                  .getNamedItem("drop")
                                  .getNodeValue();
            if (listener.equals("this"))
                editor.addDropListener((DropListener)screen);
            else {
                editor.addDropListener((DropListener)ClassFactory.forName(listener));
            }
        }
            
        if (node.getAttributes().getNamedItem("targets") != null) {
            String targets[] = node.getAttributes()
                                  .getNamedItem("targets")
                                  .getNodeValue().split(",");
            for(int i = 0; i < targets.length; i++){
                    editor.dropTargets.add(targets[i]);
            }
        }     
        if (node.getAttributes().getNamedItem("displayMask") != null) 
            displayMask = NumberFormat.getFormat(node.getAttributes().getNamedItem("displayMask").getNodeValue());
    }

    public void setDisplay() {
        setEditor();
        
    }

    public void setEditor() {
        if(editor == null){
            editor = new ScreenLabel();
            ((Label)editor.getWidget()).setWordWrap(false);
            editor.sinkEvents(Event.MOUSEEVENTS);
            editor.addMouseListener((MouseListener)ClassFactory.forName("ProxyListener"));
            editor.setWidth(width+"px");
        }
        Object val = field.getValue();
        if (val instanceof Integer)
            ((Label)editor.getWidget()).setText(((Integer)val).toString());
        else if (val instanceof Double){
            if(displayMask != null && !"".equals(val)                            )
                ((Label)editor.getWidget()).setText(displayMask.format((Double)val));
            else
                ((Label)editor.getWidget()).setText(((Double)val).toString());
        }
        else if (val == null)
            ((Label)editor.getWidget()).setText(" ");
        else
            ((Label)editor.getWidget()).setText((String)val);
        setWidget(editor);
    }

    public void saveValue() {
        // TODO Auto-generated method stub
        
    }

    public void setField(DataObject field) {
        this.field = field;
        editor.setUserObject(field);
        
    }

    public void enable(boolean enabled) {
       
    }

    public void setCellWidth(int width) {
        this.width = width;
        if(editor != null){
            editor.setWidth(width+"px");
        }
    }
    
    public void setFocus(boolean focused) {

    }
    
    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int row) {
        rowIndex = row;
        
    }
    
    
}
