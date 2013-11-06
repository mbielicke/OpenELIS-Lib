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

import org.openelis.gwt.screen.ScreenPanel;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.widget.Field;
import org.openelis.gwt.widget.HasField;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RichTextWidget extends Composite implements FocusHandler, HasValue<String>, HasField<String>, HasFocusHandlers, HasBlurHandlers, BlurHandler, Focusable {

	private VerticalPanel vp = new VerticalPanel();
	public RichTextArea area;
	public RichTextToolbar toolbar;
	private boolean tools;
	private boolean enabled;
	private Field<String> field;
	private HandlerRegistration focReg;

	public RichTextWidget() {
		area = new RichTextArea();
		toolbar = new RichTextToolbar(area);
	}

	public RichTextWidget(boolean tools) {
		this();
		init(tools);
	}

	public void init(boolean tools){
		this.tools = tools;
		initWidget(vp);
		DOM.setStyleAttribute(vp.getElement(),"background","white");
		vp.setSpacing(0);

		if(tools){
			vp.add(toolbar);
			vp.add(area);
			vp.setCellWidth(toolbar, "100%");
		}else{
			vp.add(area);
		}
		area.setSize("100%","100%");
        area.addFocusHandler(this);
		//Font and Font size can not be set until the area recieves focus.  We set up this handler to 
		//set the font and size that we want to default then remove the handler so we don't repeat it.
		focReg = area.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				area.getFormatter().setFontName("Verdana");
				area.getFormatter().setFontSize(RichTextArea.FontSize.X_SMALL);
				focReg.removeHandler();
			}
		});
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

	public void onFocus(FocusEvent event) {
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

	public void addTabHandler(TabHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
	}

	public void addException(Exception error) {
		field.addException(error);
		field.drawExceptions(this);
	}

	public void clearExceptions() {
		field.clearExceptions(this);
	}

	public Field<String> getField() {
		return field;
	}

	public void setField(Field<String> field) {
		this.field = field;
		addValueChangeHandler(field);
		addBlurHandler(field);
		area.addMouseOutHandler(field);
		area.addMouseOverHandler(field);
	}

	public String getFieldValue() {
		return field.getValue();
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler, FocusEvent.getType());
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler, BlurEvent.getType());
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
	}

	public void checkValue() {
		field.checkValue(this);
	}

	public void getQuery(ArrayList list, String key) {

	}

	public ArrayList<Exception> getExceptions() {
		return field.exceptions;
	}


	public void setFieldValue(String value) {
		field.setValue(value);
	}

	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return field.addValueChangeHandler(handler);
	}

	public void addExceptionStyle(String style) {
		area.addStyleName(style);
	}

	public Object getWidgetValue() {
		return area.getHTML();
	}

	public void removeExceptionStyle(String style) {	
		area.removeStyleName(style);
	}

	public int getTabIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setAccessKey(char key) {
		// TODO Auto-generated method stub
		
	}

	public void setTabIndex(int index) {
		// TODO Auto-generated method stub
		
	}
	
    public void onBlur(BlurEvent event) {
	    area.removeStyleName("Focus");
        BlurEvent.fireNativeEvent(Document.get().createBlurEvent(), this);
	}
	   


}
