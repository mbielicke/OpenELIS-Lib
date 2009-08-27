package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.rewrite.DateField;
import org.openelis.gwt.widget.rewrite.DoubleField;
import org.openelis.gwt.widget.rewrite.Field;
import org.openelis.gwt.widget.rewrite.IntegerField;
import org.openelis.gwt.widget.rewrite.StringField;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

public class TextBox<T> extends com.google.gwt.user.client.ui.TextBox implements HasField<T> {

	public enum Case {MIXED,UPPER,LOWER};
    public Case textCase = Case.MIXED;
    
    public boolean enforceMask = true;
    public boolean enforceLength = true;
    public boolean autoNext = false;
    public int length = 255;
    public String mask;
    public TextAlignConstant alignment = TextBox.ALIGN_LEFT;
    private Field<T> field;
    public boolean queryMode;
    private boolean enabled;
    
    public TextBox() {
        
    }
    
    public void setCase(Case textCase){
        this.textCase = textCase;
        if (textCase == Case.UPPER){
            addStyleName("Upper");
            removeStyleName("Lower");
        }
        if (textCase == Case.LOWER){
            addStyleName("Lower");
            removeStyleName("Upper");
        }
    }
    
    public void setLength(int length) {
        this.length = length;
        setMaxLength(length);
    }
    
    public String getText() {
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
        this.mask = mask;
        new MaskListener(this,mask);
        setLength(mask.length()); 
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
	
	public void addError(String error) {
		field.addError(error);
		field.drawError(this);
	}
	
	public void clearErrors() {
		field.clearError(this);
	}
	
	public void addTabHandler(UIUtil.TabHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		enforceLength = !query;
		enforceMask = !query;
		//if(query)
			//changeReg.removeHandler();
		//else
			//addValueChangeHandler(handler);
	}

	public void checkValue() {
		field.checkValue(this);
		
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		if(field.queryString != null) {
			QueryData qd = new QueryData();
			qd.query = field.queryString;
			qd.key = key;
			if(field instanceof StringField)
				qd.type = QueryData.Type.STRING;
			else if(field instanceof IntegerField)
				qd.type = QueryData.Type.INTEGER;
			else if(field instanceof DoubleField)
				qd.type = QueryData.Type.DOUBLE;
			else if(field instanceof DateField)
				qd.type = QueryData.Type.DATE;
			list.add(qd);
		}
		
	}

	public ArrayList<String> getErrors() {
		return field.errors;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
		setReadOnly(!enabled);
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
		field.setValue(value);
		if(value != null)
			setText(value.toString());
		else 
			setText("");
	}

	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return field.addValueChangeHandler(handler);
	}
	
	
	
}
