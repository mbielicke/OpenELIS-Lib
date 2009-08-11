package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;
import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.IconContainer;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CalendarLookUp extends Composite implements HasValue<Datetime>, 
														 HasField<Datetime>,
														 FocusHandler,
														 BlurHandler,
														 ClickHandler,
														 KeyUpHandler,
														 ValueChangeHandler<Datetime> {

                                                

    protected byte begin;
    protected byte end;
    protected boolean week;
    protected Date weekDate;
    protected PopupPanel pop;
    private Field<Datetime> field;
    private boolean queryMode;
    private boolean enabled;
    private TextBox textbox = new TextBox();
    private IconContainer icon = new IconContainer();
    
    private class IconMouseHandler implements MouseDownHandler,
    										  MouseOverHandler,
    										  MouseOutHandler,
    										  MouseUpHandler {

    	public void onMouseDown(MouseDownEvent event) {
           	icon.addStyleName("Pressed");
        }

        public void onMouseOver(MouseOverEvent event) {
            icon.addStyleName("Hover");
        }

        public void onMouseOut(MouseOutEvent event) {
            icon.removeStyleName("Hover");
        }

        public void onMouseUp(MouseUpEvent event) {
            icon.removeStyleName("Pressed");
        }
    }

    public CalendarLookUp() {
        HorizontalPanel hp = new HorizontalPanel();
        hp.addStyleName("Calendar");
        hp.add(textbox);
        hp.add(icon);
        initWidget(hp);
        icon.setStyleName("CalendarButton");
        textbox.setStyleName("TextboxUnselected");
        textbox.addFocusHandler(this);
        textbox.addBlurHandler(this);
        textbox.addKeyUpHandler(this);
        icon.addClickHandler(this);
        IconMouseHandler iconHandler = new IconMouseHandler();
        icon.addMouseOutHandler(iconHandler);
        icon.addMouseOverHandler(iconHandler);
        icon.addMouseDownHandler(iconHandler);
        icon.addMouseUpHandler(iconHandler);
        textbox.addStyleName("TextboxUnselected");
    }
    
    public CalendarLookUp(byte begin,byte end,boolean week) {
        this();
        init(begin,end,week);
    }
    
    public void addTabHandler(UIUtil.TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void init(byte begin,byte end,boolean week){
        this.begin = begin;
        this.end = end;
        this.week = week;
    }
    
    
    public void setDate(Datetime date) {
        if (week) {
            date.add(-(date.getDate().getDay()));
            Datetime endDate = Datetime.getInstance(Datetime.YEAR,
                                                          Datetime.DAY,
                                                          date.getDate());
            endDate.add(6);
            String from = DateTimeFormat.getFormat("EEEE MMM d, yyyy")
                                        .format(date.getDate());
            String to = DateTimeFormat.getFormat("EEEE MMM d, yyyy")
                                      .format(endDate.getDate());
            textbox.setText(from + " - " + to);
            weekDate = date.getDate();
        } else
            textbox.setText(date.toString());
    }
    
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            ((TextBox)event.getSource()).setFocus(false);
            doCalendar((Widget)event.getSource(), begin, end);
        }
    }

    public void onFocus(FocusEvent event) {
        if (!textbox.isReadOnly()) {
            if (event.getSource() == textbox) {
                // we need to set the selected style name to the textbox
                textbox.addStyleName("TextboxSelected");
                textbox.removeStyleName("TextboxUnselected");
                textbox.setFocus(true);
                // textBox.setText("");
                icon.addStyleName("Selected");
            }
        }
        
    }

    public void onBlur(BlurEvent event) {
        if (!textbox.isReadOnly()) {
            if (event.getSource() == textbox) {
                // we need to set the unselected style name to the textbox
                textbox.addStyleName("TextboxUnselected");
                textbox.removeStyleName("TextboxSelected");
                icon.removeStyleName("Selected");
                setValue(getValue(),true);
            }
        }
    }
        
    public void onClick(ClickEvent event) {
        textbox.addStyleName("TextboxSelected");
        textbox.removeStyleName("TextboxUnselected");
        textbox.setFocus(true);
        icon.addStyleName("Selected");
        doCalendar((Widget)event.getSource(), begin, end);
    }
    
    protected void doCalendar(Widget sender, final byte begin, final byte end) {
    	try  {
    		CalendarWidget cal = new CalendarWidget(getValue());
    		cal.addValueChangeHandler(this);
        //	pop = new (null,"","","",true);
    		pop = new PopupPanel(true, false);
        
    		pop.setWidth("150px");
    		pop.setWidget(cal);
    		pop.setPopupPosition(textbox.getAbsoluteLeft(),
    				textbox.getAbsoluteTop() + textbox.getOffsetHeight());
    		pop.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    		Window.alert(e.getMessage());
    	}
    }
    
    public Datetime getValue() {
        if (textbox.getText().equals(""))
            return null;
        Date date = new Date(textbox.getText().replaceAll("-", "/"));
        return Datetime.getInstance(begin, end, date);
    }

    public Object getDisplay(String title) {
        Label tl = new Label();
        tl.setText(textbox.getText());
        tl.setWordWrap(false);
        if (title != null)
            tl.setTitle(title);
        return tl;
    }
    
    public Date getWeekDate() {
        return weekDate;
    }
    

    
    public void onValueChange(ValueChangeEvent<Datetime> event) {
    	setValue(event.getValue(),true);
    	if(pop != null){
    		pop.hide();
    	}
    }

	public void setValue(Datetime value) {
		setValue(value,false);
		
	}

	public void setValue(Datetime value, boolean fireEvents) {
        if (value != null)
            textbox.setValue(value.toString(),fireEvents);
        else
           textbox.setValue("",fireEvents);
        if(fireEvents) {
        	ValueChangeEvent.fire(this, value);
        }
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Datetime> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return textbox.addBlurHandler(handler);
	}
	
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return textbox.addMouseOutHandler(handler);
	}
	
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return textbox.addMouseOverHandler(handler);
	}

	public void addError(String error) {
		field.addError(error);
		field.drawError(this);
	}

	public void clearErrors() {
		field.clearError(this);
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
		textbox.addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler,FocusEvent.getType());
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		
	}

	public void checkValue() {
		field.checkValue(this);
		
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		if(field.queryString != null) {
			QueryData qd = new QueryData();
			qd.query = field.queryString;
			qd.key = key;
			qd.type = QueryData.Type.DATE;
			list.add(qd);
		}
		
	}

	public ArrayList<String> getErrors() {
		return field.errors;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public Datetime getFieldValue() {
		// TODO Auto-generated method stub
		return field.getValue();
	}
	
	public void setFieldValue(Datetime value){
		field.setValue(value);
		setValue(value);
	}
    
}
