package org.openelis.gwt.screen;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.EditBox;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class ScreenEditBox extends ScreenInputWidget {
	
	public static final String TAG_NAME = "editbox";
	
	private EditBox editbox;
	
	public ScreenEditBox() {
		
	}
	
	public ScreenEditBox(Node node, ScreenBase screen){
		super(node);
		editbox = new EditBox();
		initWidget(editbox);
		setDefaults(node,screen);
	}
	
	public ScreenEditBox getInstance(Node node, ScreenBase screen){
		return new ScreenEditBox(node,screen);
	}
	
    public void load(AbstractField field) {
        if(!queryMode){
            editbox.setText(field.toString().trim());
            super.load(field);   
        }else
            queryWidget.load(field);
    }

    public void submit(AbstractField field) {
        if(!queryMode){
            String text = editbox.getText();
            field.setValue(text);
        }else
            queryWidget.submit(field);

    }

    public void onChange(Widget sender) {    
    }
    
    public void enable(boolean enabled){
    	if(!alwaysEnabled){
    		if(alwaysDisabled)
    			enabled = false;
	        editbox.setReadOnly(!enabled);
	        if(enabled){
	            editbox.addFocusListener(this);
	        }else
	            editbox.removeFocusListener(this);
            super.enable(enabled);
    	}else
            super.enable(true);
    }
    
    public void setFocus(boolean focus){
        editbox.setFocus(focus);
    }
    
    public void destroy() {
        editbox = null;
        super.destroy();
    }
    
	public void onFocus(Widget sender) {
		if(!editbox.isReadOnly()){
			if(sender == editbox){
				super.hp.addStyleName("Focus");
			}
		}	
        super.onFocus(sender);
	}
	public void onLostFocus(Widget sender) {
		if(!editbox.isReadOnly()){
			if(sender == editbox){
				super.hp.removeStyleName("Focus");
			}
		}
        super.onLostFocus(sender);
	}    

}
