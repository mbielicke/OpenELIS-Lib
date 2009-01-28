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
package org.openelis.gwt.screen;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.EditBox;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class ScreenEditBox extends ScreenInputWidget {
	
	public static final String TAG_NAME = "editbox";
	
	private EditBox editbox;
	
	public ScreenEditBox() {
		
	}
	
	public ScreenEditBox(Node node, ScreenBase screen){
		super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            editbox = (EditBox)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
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
