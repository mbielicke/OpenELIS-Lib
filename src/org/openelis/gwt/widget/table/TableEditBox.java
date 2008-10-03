package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.EditBox;

public class TableEditBox extends TableCellInputWidget {
    
    protected EditBox editor;
    protected Label display;
    protected int width;
    protected boolean enabled;
    public static final String TAG_NAME = "table-editbox";
    
    public TableEditBox() {
        
    }
    
    public TableEditBox(Node node, ScreenBase screen){
        this.screen = screen;
        editor = new EditBox();
    }
    
    public TableCellWidget getNewInstance() {
        TableEditBox editBox = new TableEditBox();
        editBox.screen = this.screen;
        return editBox;
    }
    
    public void setDisplay() {
        if(display == null){
            display = new Label();
            display.setWordWrap(false);
            display.setWidth(width+"px");
        }
        display.setText((String)field.getValue());
        setWidget(display);
    }
    
    public void setEditor() {
        if(!enabled)
            return;
        if(editor == null){
            editor = new EditBox();
            editor.setWidth((width-15)+"px");
        }
        editor.setText((String)field.getValue());
        setWidget(editor);
    }
    
    public void saveValue() {
        if(!enabled)
            return;
        if(editor.getText() != null)
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
            editor.setWidth((width-15)+"px");
        if(display != null)
            display.setWidth(width+"px");
    }
    
    public void setFocus(boolean focused) {
        editor.setFocus(focused);
    }
    
}
