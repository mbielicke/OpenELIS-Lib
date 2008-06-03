package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CheckBox.CheckType;


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
    private FocusPanel panel;
    private CheckType type = CheckType.TWO_STATE;
    private int width;
    public static final String TAG_NAME = "table-check";
   
	public TableCheck() {
		editor = new CheckBox();
        editor.enable(enabled);
        panel = new FocusPanel();
        panel.add(editor);
        setWidget(panel);
        panel.addFocusListener(this);
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
        ch.screen = screen;
        return ch;
    }

    public TableCheck(Node node, ScreenBase screen) {
        this();
        this.screen = screen;
        if(node.getAttributes().getNamedItem("threeState") != null){
            type = CheckType.THREE_STATE;
            editor.setType(CheckType.THREE_STATE);
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
        panel.setWidth(width+"px");
    }
}
