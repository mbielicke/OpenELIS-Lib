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
package org.openelis.gwt.widget.richtext;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.rewrite.Field;

public class RichTextWidget extends Composite implements FocusListener, HasValue<String>, HasField<String>, HasFocusHandlers, HasBlurHandlers{
    
    private FlexTable vp = new FlexTable();
    public RichTextArea area;
    public RichTextToolbar toolbar;
    private boolean tools;
    private boolean enabled;
    private Field<String> field;
    
    public RichTextWidget(ScreenBase screen) {
        area = new RichTextArea();
        toolbar = new RichTextToolbar(area,screen);
    }
    
    public RichTextWidget(boolean tools) {
        init(tools);
    }
    
    public void init(boolean tools){
        this.tools = tools;
        initWidget(vp);
        vp.setCellPadding(0);
        vp.setCellSpacing(0);
        
        if(tools){
        	vp.setWidget(0,0,toolbar);
           // vp.getFlexCellFormatter().setHeight(0, 0,"75px");
            vp.getFlexCellFormatter().setVerticalAlignment(0, 0, HasAlignment.ALIGN_TOP);
            vp.setWidget(1,0,area);
            vp.getFlexCellFormatter().addStyleName(1, 0, "WhiteContentPanel");
        }else{
            vp.setWidget(0,0,area);
            vp.getFlexCellFormatter().addStyleName(0, 0, "WhiteContentPanel");
        }
        area.setSize("100%","100%");
        area.addFocusListener(this);

    }
    
    public void setText(String text){
        area.setHTML(text);
    }
    
    public String getText(){
        return area.getHTML();
    }
    
    public String getClearText() {
    	return area.getText();
    }
    
    public void addFocusListener(FocusListener listener){
        area.addFocusListener(listener);
    }
    
    public void removeFocusListener(FocusListener listener){
        area.removeFocusListener(listener);
    }
    
    public void setFocus(boolean focused) {
        if(enabled)
            area.setFocus(focused);
        else
            area.setFocus(false);
    }
    
    public boolean isEnabled(){
        return area.isEnabled();
    }
    
    public void setWidth(String width){
        vp.setWidth(width);
    }
    
    public void setHeight(String height){
        vp.setHeight(height);
    }
    
    public void enable(boolean enabled) {
        this.enabled = enabled;
        if(tools) {
            toolbar.enable(enabled);
        }
        area.setEnabled(enabled);
    }

    public void onFocus(Widget sender) {
        if(!enabled)
            area.setFocus(false);
    }

    public void onLostFocus(Widget sender) {
        // TODO Auto-generated method stub
        
    }

	public String getValue() {
		return area.getHTML();
	}

	public void setValue(String value) {
		setValue(value,false);
	}

	public void setValue(String value, boolean fireEvents) {
		String old = area.getHTML();
		area.setHTML(value);
		if(fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, old, value);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}

	public void addTabHandler(UIUtil.TabHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
	}

	public void addError(String error) {
		field.addError(error);
		field.drawError(this);
	}

	public void clearErrors() {
		field.clearError(this);
	}

	public Field<String> getField() {
		return field;
	}

	public void setField(Field<String> field) {
		this.field = field;
		addValueChangeHandler(field);
		area.addBlurHandler(field);
		area.addMouseOutHandler(field);
		area.addMouseOverHandler(field);
	}

	public String getFieldValue() {
		return field.getValue();
	}
	
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return area.addFocusHandler(handler);
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return area.addBlurHandler(handler);
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		
	}

	public void checkValue() {
		field.checkValue(this);
		
	}

	public void getQuery(ArrayList list, String key) {
		if(field.queryString != null) {
			QueryData qd = new QueryData();
			qd.query = field.queryString;
			qd.key = key;
			qd.type = QueryData.Type.STRING;
			list.add(qd);
		}
		
	}

	public ArrayList<String> getErrors() {
		return field.errors;
	}
}
