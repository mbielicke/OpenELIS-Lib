package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.ui.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

public class TextBox<T> extends com.google.gwt.user.client.ui.TextBox implements HasField<T> {

	public enum Case {MIXED,UPPER,LOWER};
    public Case textCase = Case.MIXED;
    
    public boolean enforceMask = false;
    public boolean enforceLength = true;
    public boolean autoNext = false;
    public int length = 255;
    public String mask,picture;
    public TextAlignConstant alignment = TextBox.ALIGN_LEFT;
    private Field<T> field;
    private boolean enabled;
    private NewMaskListener maskListener;
    private com.google.gwt.user.client.ui.TextBox source = this;
    
    public TextBox() {

    }
    
    public void setCase(Case textCase){
        this.textCase = textCase;
        if (textCase == Case.UPPER){
            addStyleName("Upper");
            removeStyleName("Lower");
        }
        else if (textCase == Case.LOWER){
            addStyleName("Lower");
            removeStyleName("Upper");
        }else{
        	removeStyleName("Upper");
        	removeStyleName("Lower");
        }
        	
    }
    
    public void setLength(int length) {
        this.length = length;
        setMaxLength(length);
    }
    
    public String getText() {
        if(enforceMask && picture != null && picture.equals(super.getText())) 
            return "";
        
        
        if (textCase == Case.UPPER)
            return super.getText().toUpperCase();
        else if(textCase == Case.LOWER)
            return super.getText().toLowerCase();
        else
            return super.getText();
    }
    
    public void setText(String text) {
        if(text == null || textCase == Case.MIXED)
            super.setText(text);
        else if(textCase == Case.UPPER)
            super.setText(text.toUpperCase());
        else 
            super.setText(text.toLowerCase());
    }
    
    public void setMask(String mask) {
    	StringBuffer pic;
    	
        this.mask = mask;
    	pic = new StringBuffer();
    	
    	for(char mc : mask.toCharArray()) {
    		switch (mc) {
    			case '9' :
    			case 'X' :
    				pic.append(" ");
    				break;
    			default :
    				pic.append(mc);
    		}
    	}
    	picture = pic.toString();
    	
    	if(maskListener == null) {
    		new NewMaskListener(this,mask);
    		addBlurHandler(new BlurHandler() {
				public void onBlur(BlurEvent event) {
					ValueChangeEvent.fire(source, getText());
				}
			});
    	}else
    		maskListener.mask = mask;
    	
        setLength(mask.length()); 
        
        enforceMask = true;
    }
    
    public void setTextAlignment(TextAlignConstant alignment){
        this.alignment = alignment;
        super.setTextAlignment(alignment);
    }

	public Field getField() {
		return field;
	}

	
	public void setField(Field<T> field) {
		this.field = field;
		addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}
	
	public void addException(Exception error) {
		field.addException(error);
		field.drawExceptions(this);
	}
	
	public void clearExceptions() {
		field.clearExceptions(this);
	}
	
	public void addTabHandler(TabHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
	}

	public void setQueryMode(boolean query) {
		if(field.queryMode == query)
			return;
		field.setQueryMode(query);
		enforceLength = !query;
		enforceMask = !query;
		if(query)
			setMaxLength(255);
		else
			setMaxLength(length);
		//if(query)
			//changeReg.removeHandler();
		//else
			//addValueChangeHandler(handler);
	}

	public void checkValue() {
		field.checkValue(this);
		
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		if(!field.queryMode)
			return;
		if(field.queryString != null && !"".equals(field.queryString)) {
			QueryData qd = new QueryData();
			qd.setQuery(field.queryString);
			qd.setKey(key);
			if(field instanceof StringField)
				qd.setType(QueryData.Type.STRING);
			else if(field instanceof IntegerField)
				qd.setType(QueryData.Type.INTEGER);
			else if(field instanceof DoubleField)
				qd.setType(QueryData.Type.DOUBLE);
			else if(field instanceof DateField)
				qd.setType(QueryData.Type.DATE);
			list.add(qd);
		}
		
	}

	public ArrayList<Exception> getExceptions() {
		return field.exceptions;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
		setReadOnly(!enabled);
		enforceMask = enabled;
		if(!enabled){
			unsinkEvents(Event.KEYEVENTS);
		}else
			sinkEvents(Event.KEYEVENTS);
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public T getFieldValue() {
		// TODO Auto-generated method stub
		return field.getValue();
	}
	
	public void setFieldValue(T value) {
		if(enforceMask && picture != null && picture.equals(value)) {
			value = null;
			setText("");
		}
		
		field.setValue(value);
		if(value != null)
			setText(field.format());
		else 
			setText("");
	}

	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return field.addValueChangeHandler(handler);
	}
	
	public void setValue(String value) {
		setFieldValue((T)value);
	}

	public void addExceptionStyle(String style) {
		addStyleName(style);
	}

	public Object getWidgetValue() {
	    if(enforceMask && picture != null && picture.equals(getText())) {
	        setText("");
            return null;
	    }else
            return getText();
	}

	public void removeExceptionStyle(String style) {
		removeStyleName(style);
	}
	
	
}
