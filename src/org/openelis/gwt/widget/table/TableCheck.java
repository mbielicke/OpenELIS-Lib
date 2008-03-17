package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.CheckBox;


/**
 * Checkbox Widget that implements CellWidget interface so that it can
 * participate in a Table.
 * 
 * @author tschmidt
 * 
 */
public class TableCheck extends TableCellInputWidget implements FocusListener {

	private CheckBox editor;
    private boolean enabled;
    private int type = CheckBox.TWO_STATE;
    private int width;
   
	public TableCheck() {
		editor = new CheckBox();
        DOM.setElementProperty(getElement(), "align", "center");
	}
    /**
     * Clears value of cell to default.
     */
    public void clear() {
    	if(editor != null)
    		editor.setState(CheckBox.UNKNOWN);
    }

    /**
     * Returns a new TableCheck widget.
     * 
     * @return
     */
    public TableCellWidget getNewInstance() {
        TableCheck ch = new TableCheck();
        ch.enable(enabled);
        ch.type = type;
        ch.editor.setType(type);
        return ch;
    }

    public TableCheck(Node node) {
        this();
        if(node.getAttributes().getNamedItem("threeState") != null){
            type = CheckBox.THREE_STATE;
            editor.setType(CheckBox.THREE_STATE);
        }
    }

	public void saveValue() {
		field.setValue(editor.getState());
		super.saveValue();
	}

	public void setField(AbstractField field) {
		this.field = field;
		editor.setState((String)field.getValue());
	}
    
    public void onFocus(Widget sender) {
        DOM.setStyleAttribute(sender.getElement(), "background", "white");
        
    }
    public void onLostFocus(Widget sender) {
        DOM.setStyleAttribute(sender.getElement(), "background", "none");
    }
    
    public void enable(boolean enabled){
        this.enabled = enabled;
        editor.enable(enabled);
    }
    
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null)
            editor.setWidth(width+"px");
    }
}
