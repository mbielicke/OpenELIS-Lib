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
package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.ScreenWindow;
import org.openelis.gwt.screen.rewrite.ScreenDef;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.IconContainer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.xml.client.XMLParser;

public class EditBox extends Composite implements ClickHandler, HasValue<String>, HasField<String>{
	
	private TextArea text = new TextArea();
	private IconContainer fp = new IconContainer();
	private HorizontalPanel hp = new HorizontalPanel();
	private static String editorScreen = "<VerticalPanel><textarea key='editor' tools='false' width='300px' height='200px' showError='false'/><HorizontalPanel halign='center'>"+
										"<appButton action='ok' key='ok' onclick='this' style='Button'>"+
									   		"<text>OK</text>"+
									    "</appButton>" +
									    "<appButton action='cancel' key='cancel' onclick='this' style='Button'>"+
									       "<text>Cancel</text>" +
	                                    "</appButton>" +
	                                    "</HorizontalPanel>" +
	                                    "</VerticalPanel>";
	private ScreenWindow win;
	private ScreenDef editorDef;
	private boolean enabled;
	private Field<String> field;

	public EditBox() {
		initWidget(hp);
		text.setStyleName("ScreenTextBox");
		text.setHeight("18px");
		hp.add(text);
		hp.add(fp);
		fp.setStyleName("DotsButton");
		fp.addClickHandler(this);
	}

	public void onClick(ClickEvent sender) {
		if(!isEnabled()) {
			return;
		}
		if(sender.getSource() == fp){
				win = new ScreenWindow(null,"","standardNotePicker","",true,false);
				if(editorDef == null) {
					editorDef = new ScreenDef();
					editorDef.getPanel().add(UIUtil.createWidget(XMLParser.parse(editorScreen).getDocumentElement(),editorDef));
					((AppButton)editorDef.getWidget("ok")).addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							text.setText(((TextArea)editorDef.getWidget("editor")).getText());
							win.close();
						}
					});
					((AppButton)editorDef.getWidget("cancel")).addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							win.close();
						}
					});
				}
				((TextArea)editorDef.getWidget("editor")).setText(text.getText());
				win.setContent(editorDef.getPanel());
				win.setVisible(true);
		}	
	}
	
	public void setText(String txt){
		text.setText(txt);
	}
	
	public String getText() {
		return text.getText();
	}
		
	public void setFocus(boolean focus){
		text.setFocus(focus);
	}
		
	public void setWidth(String width){
		int wid;
		if(width.indexOf("px") > -1)
			wid = Integer.parseInt(width.substring(0,width.indexOf("px"))) - 16;
		else 
			wid = Integer.parseInt(width) - 16;
		text.setWidth(wid+"px");
	}

	public String getValue() {
		return text.getText();
	}

	public void setValue(String value) {
		setValue(value,false);
		
	}

	public void setValue(String value, boolean fireEvents) {
		String old = getValue();
		text.setText(value);
		if(fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, old, value);
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}

	public void addError(String error) {
		field.addError(error);
	}

	public void checkValue() {
		field.checkValue(this);
	}

	public void clearErrors() {
		field.clearError(this);
		
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
		text.setReadOnly(!enabled);		
	}

	public ArrayList<String> getErrors() {
		return field.errors;
	}

	public Field<String> getField() {
		return field;
	}

	public String getFieldValue() {
		return field.getValue();
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		if(field.queryString != null) {
			QueryData qd = new QueryData();
			qd.query = field.queryString;
			qd.key = key;
			qd.type = QueryData.Type.STRING;
			list.add(qd);
		}
		
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setField(Field<String> field) {
		this.field = field;
		text.addValueChangeHandler(field);
		text.addBlurHandler(field);
		text.addMouseOutHandler(field);
		text.addMouseOverHandler(field);
		
	}

	public void setFieldValue(String value) {
		field.setValue(value);
		setText(value);
		
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		
	}

}
