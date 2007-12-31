package org.openelis.gwt.client.widget.table;

import org.openelis.gwt.common.data.AbstractField;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * Checkbox Widget that implements CellWidget interface so that it can
 * participate in a Table.
 * 
 * @author tschmidt
 * 
 */
public class TableCheck extends SimplePanel implements TableCellWidget, ClickListener, KeyboardListener, FocusListener {

	private Image editor;
    private FocusPanel panel;
	private AbstractField field;
	
	public TableCheck() {
        panel = new FocusPanel();
		editor = new Image();
        panel.setWidget(editor);
        panel.addClickListener(this);
        panel.addKeyboardListener(this);
        panel.addFocusListener(this);
		setWidget(panel);
        DOM.setElementProperty(getElement(), "align", "center");
	}
    /**
     * Clears value of cell to default.
     */
    public void clear() {
    	if(editor != null)
    		editor.setUrl("Images/unapply.png");
    }

    /**
     * Returns a new TableCheck widget.
     * 
     * @return
     */
    public TableCellWidget getNewInstance() {
        return new TableCheck();
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableCheck();
    }

	public void setDisplay() {
		//setEditor();
	}

	public void setEditor() {
        /*if(field.getValue() != null)
        	editor.setChecked(((Boolean)field.getValue()).booleanValue());
        else
        	editor.setChecked(false);
        */
	}

	public void saveValue() {
		//field.setValue(new Boolean(editor.isChecked()));
		
	}

	public void setField(AbstractField field) {
		this.field = field;
		if(field.getValue() != null){
               if(((Boolean)field.getValue()).booleanValue())
                   editor.setUrl("Images/apply.png");
               else
                   editor.setUrl("Images/unapply.png");
        }else{
            editor.setUrl("Images/unapply.png");
            field.setValue(new Boolean(false));
        }
	}
    public void onClick(Widget sender) {
       field.setValue(new Boolean(!((Boolean)field.getValue()).booleanValue()));
       if(((Boolean)field.getValue()).booleanValue())
           editor.setUrl("Images/apply.png");
       else
           editor.setUrl("Images/unapply.png");
    }
    
    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        
    }
    
    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }
    
    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        if(keyCode == KeyboardListener.KEY_ENTER){
            onClick(this);
        }
    }
    public void onFocus(Widget sender) {
        DOM.setStyleAttribute(sender.getElement(), "background", "white");
        
    }
    public void onLostFocus(Widget sender) {
        DOM.setStyleAttribute(sender.getElement(), "background", "none");
        
    }
    
    public void enable(boolean enabled){
        panel.removeClickListener(this);
        if(enabled){
            panel.addClickListener(this);
        }
    }
}
