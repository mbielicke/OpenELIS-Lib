package org.openelis.gwt.widget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
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

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.screen.ScreenWindow;

import java.util.Date;

public class CalendarLookUp extends LookUp implements KeyboardListener, 
                                                      FocusListener, 
                                                      ClickListener, 
                                                      SourcesFocusEvents, 
                                                      ChangeListener,
                                                      SourcesChangeEvents,
                                                      MouseListener,
                                                      HasValue<Date>,
                                                      ValueChangeHandler<String>{

    protected byte begin;
    protected byte end;
    protected boolean week;
    protected Date weekDate;
    protected ChangeListenerCollection changeListeners;
    protected PopupPanel pop;

    public CalendarLookUp() {
        super();
        setIconStyle("CalendarButton");
        textbox.addKeyboardListener(this);
        textbox.addFocusListener(this);
        addClickListener(this);
        icon.addMouseListener(this);
        textbox.addStyleName("TextboxUnselected");
        hp.addStyleName("Calendar");
        textbox.addValueChangeHandler(this);
    }
    
    public CalendarLookUp(byte begin,byte end,boolean week) {
        this();
        init(begin,end,week);
    }
    
    public void init(byte begin,byte end,boolean week){
        this.begin = begin;
        this.end = end;
        this.week = week;
    }
    
    
    public void setDate(DatetimeRPC date) {
        if (week) {
            date.add(-(date.getDate().getDay()));
            DatetimeRPC endDate = DatetimeRPC.getInstance(DatetimeRPC.YEAR,
                                                          DatetimeRPC.DAY,
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
        CalendarWidget cal = new CalendarWidget(textbox.getText());
        cal.addChangeListener(this);
        //pop = new (null,"","","",true);
        pop = new PopupPanel(true, false);
        
        pop.setWidth("150px");
        pop.setWidget(cal);
        pop.setPopupPosition(textbox.getAbsoluteLeft(),
                             textbox.getAbsoluteTop() + textbox.getOffsetHeight());
        pop.show();
    }
    
    public Date getValue() {
        if (getText().equals(""))
            return null;
        Date date = new Date(getText().replaceAll("-", "/"));
        return DatetimeRPC.getInstance(begin, end, date).getDate();
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
    
    public void onChange(Widget sender) {
        pop.hide();
        Date date = new Date((String)((ScreenWidget)sender).getUserObject());
        setDate(DatetimeRPC.getInstance(begin, end, date));
        
        if (changeListeners != null){
            changeListeners.fireChange(this);
        }
        setFocus(true);
    }

    public void addChangeListener(ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new ChangeListenerCollection();
        changeListeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null)
            changeListeners.remove(listener);
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

	public void setValue(Date value) {
		setValue(value,false);
		
	}

	public void setValue(Date value, boolean fireEvents) {
        if (value != null)
            setText(DatetimeRPC.getInstance(begin, end, value).toString());
        else
            setText("");
        if(fireEvents) {
        	ValueChangeEvent.fire(this, value);
        }
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Date> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getValue().equals(""))
            setValue(null,true);
        else{
        	Date date = new Date(getText().replaceAll("-", "/"));
        	setValue(DatetimeRPC.getInstance(begin, end, date).getDate(),true);
        }
	}
    
}
