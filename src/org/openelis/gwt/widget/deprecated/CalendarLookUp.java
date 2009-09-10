package org.openelis.gwt.widget.deprecated;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesFocusEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.screen.UIUtil;
import org.openelis.gwt.screen.deprecated.ScreenWidget;
import org.openelis.gwt.screen.deprecated.ScreenWindow;
import org.openelis.gwt.widget.Field;
import org.openelis.gwt.widget.HasField;

import java.util.ArrayList;
import java.util.Date;

public class CalendarLookUp extends LookUp implements KeyboardListener, 
                                                      FocusListener, 
                                                      ClickListener, 
                                                      SourcesFocusEvents, 
                                                      ChangeListener,
                                                      SourcesChangeEvents,
                                                      MouseListener,
                                                      HasValue<Datetime>,
                                                      ValueChangeHandler<Datetime>,
                                                      HasFocusHandlers,
                                                      HasField<Datetime> {
                                                

    protected byte begin;
    protected byte end;
    protected boolean week;
    protected Date weekDate;
    protected ChangeListenerCollection changeListeners;
    protected PopupPanel pop;
    private Field<Datetime> field;
    private boolean queryMode;
    private boolean enabled;

    public CalendarLookUp() {
        super();
        setIconStyle("CalendarButton");
        textbox.addKeyboardListener(this);
        textbox.addFocusListener(this);
        addClickListener(this);
        icon.addMouseListener(this);
        textbox.addStyleName("TextboxUnselected");
        hp.addStyleName("Calendar");
    }
    
    public CalendarLookUp(byte begin,byte end,boolean week) {
        this();
        init(begin,end,week);
    }
    
    public void addTabHandler(TabHandler handler) {
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
            setText(from + " - " + to);
            weekDate = date.getDate();
        } else
            setText(date.toString());
    }
    

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
        
    }

    public void onKeyUp(Widget sender, char key, int modifiers) {
        if (KeyboardListener.KEY_ENTER == (int)key) {
            ((TextBox)sender).setFocus(false);
            doCalendar(sender, begin, end);
        }
    }

    public void onFocus(Widget sender) {
        if (!textbox.isReadOnly()) {
            if (sender == textbox) {
                // we need to set the selected style name to the textbox
                textbox.addStyleName("TextboxSelected");
                textbox.removeStyleName("TextboxUnselected");
                textbox.setFocus(true);
                // textBox.setText("");
                icon.addStyleName("Selected");
            }
        }
        
    }

    public void onLostFocus(Widget sender) {
        if (!textbox.isReadOnly()) {
            if (sender == textbox) {
                // we need to set the unselected style name to the textbox
                textbox.addStyleName("TextboxUnselected");
                textbox.removeStyleName("TextboxSelected");

                icon.removeStyleName("Selected");
            }
        }
    }
    
    public void addFocusListener(FocusListener listener) {
        textbox.removeFocusListener(listener);
        textbox.addFocusListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        textbox.removeFocusListener(listener);
    }
    
    public void onClick(Widget sender) {
        textbox.addStyleName("TextboxSelected");
        textbox.removeStyleName("TextboxUnselected");
        textbox.setFocus(true);
        icon.addStyleName("Selected");
        doCalendar(sender, begin, end);
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
        if (getText().equals(""))
            return null;
        Date date = new Date(getText().replaceAll("-", "/"));
        return Datetime.getInstance(begin, end, date);
    }

    public Object getDisplay(String title) {
        Label tl = new Label();
        tl.setText(getText());
        tl.setWordWrap(false);
        if (title != null)
            tl.setTitle(title);
        return tl;
    }
    
    public Date getWeekDate() {
        return weekDate;
    }
    
    public void onMouseDown(Widget sender, int x, int y) {
        if (!textbox.isReadOnly()) {
            if (sender == icon) {
                icon.addStyleName("Pressed");
            }
        }
    }

    public void onMouseEnter(Widget sender) {
        if (!textbox.isReadOnly()) {
            if (sender == icon) {
                icon.addStyleName("Hover");
            }
        }
    }

    public void onMouseLeave(Widget sender) {
        if (!textbox.isReadOnly()) {
            if (sender == icon) {
                icon.removeStyleName("Hover");
            }
        }
    }

    public void onMouseMove(Widget sender, int x, int y) {
    }

    public void onMouseUp(Widget sender, int x, int y) {
        if (!textbox.isReadOnly()) {
            if (sender == icon) {
                icon.removeStyleName("Pressed");
            }
        }
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

	public void onChange(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void addChangeListener(ChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void removeChangeListener(ChangeListener listener) {
		// TODO Auto-generated method stub
		
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
		super.enable(enabled);
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
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<Datetime> handler) {
		return field.addValueChangeHandler(handler);
	}
    
}
