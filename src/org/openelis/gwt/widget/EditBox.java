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
package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.ui.common.data.QueryData;
import org.openelis.gwt.screen.ScreenDef;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.xml.client.XMLParser;

public class EditBox extends Composite implements ClickHandler, 
												  HasValue<String>, 
												  HasField<String>, 
												  HasBlurHandlers, 
												  HasMouseOverHandlers, 
												  HasMouseOutHandlers,
												  HasFocusHandlers,
												  Focusable{
	
	private TextBox text = new TextBox();
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
	
	private class EditHandler implements FocusHandler,BlurHandler,MouseOutHandler,MouseOverHandler {

		private EditBox source;
		
		public EditHandler(EditBox source) {
			this.source = source;
		}
		
		public void onFocus(FocusEvent event) {
			//FocusEvent.fireNativeEvent(event.getNativeEvent(),source);
		}

		public void onBlur(BlurEvent event) {
			BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
		}

		public void onMouseOut(MouseOutEvent event) {
			MouseOutEvent.fireNativeEvent(event.getNativeEvent(), source);
		}

		public void onMouseOver(MouseOverEvent event) {
			MouseOverEvent.fireNativeEvent(event.getNativeEvent(), source);
		}
		
	}

	public EditBox() {
		initWidget(hp);
		text.setStyleName("ScreenTextBox");
		text.setHeight("18px");
		EditHandler handler = new EditHandler(this);
		text.addFocusHandler(handler);
		text.addBlurHandler(handler);
		text.addMouseOutHandler(handler);
		text.addMouseOverHandler(handler);
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
				win = new ScreenWindow(ScreenWindow.Mode.DIALOG);
				if(editorDef == null) {
					editorDef = new ScreenDef();
					//editorDef.getPanel().add(UIUtil.createWidget(XMLParser.parse(editorScreen).getDocumentElement(),editorDef));
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

    public void onFocus(FocusEvent event) {
        if (!text.isReadOnly()) {
            if (event.getSource() == text) {
                // we need to set the selected style name to the textbox
                text.addStyleName("TextboxSelected");
                text.removeStyleName("TextboxUnselected");

            }
        }
        
    }
    
    

    public void onBlur(BlurEvent event) {
        if (!text.isReadOnly()) {
            if (event.getSource() == text) {
                // we need to set the unselected style name to the textbox
                text.addStyleName("TextboxUnselected");
                text.removeStyleName("TextboxSelected");
            }
        }
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

	public void addException(Exception error) {
		field.addException(error);
	}

	public void checkValue() {
		field.checkValue(this);
	}

	public void clearExceptions() {
		field.clearExceptions(this);
		
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
		text.setReadOnly(!enabled);		
	}

	public ArrayList<Exception> getExceptions() {
		return field.exceptions;
	}

	public Field<String> getField() {
		return field;
	}

	public String getFieldValue() {
		return field.getValue();
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		if(!field.queryMode)
			return;
		if(field.queryString != null) {
			QueryData qd = new QueryData();
			qd.setQuery(field.queryString);
			qd.setKey(key);
			qd.setType(QueryData.Type.STRING);
			list.add(qd);
		}
		
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setField(Field<String> field) {
		this.field = field;
		text.addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
		
	}

	public void setFieldValue(String value) {
		field.setValue(value);
		setText(value);
		
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler,BlurEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler,MouseOverEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler,MouseOutEvent.getType());
	}

	public int getTabIndex() {
		// TODO Auto-generated method stub
		return -1;
	}

	public void setAccessKey(char key) {
		
		
	}

	public void setTabIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler,FocusEvent.getType());
	}
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return field.addValueChangeHandler(handler);
	}

	public void addExceptionStyle(String style) {
		text.addStyleName(style);	
	}

	public Object getWidgetValue() {
		return text.getText();
	}

	public void removeExceptionStyle(String style) {
		text.removeStyleName(style);
	}

}
