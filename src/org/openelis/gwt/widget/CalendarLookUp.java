package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.Date;

import org.openelis.ui.common.Datetime;
import org.openelis.ui.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;

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
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * This widget class displays a textbox and a button for entering dates into this system.  A date can be typed in or
 * the button clicked which will open the CalendarWidget in a popup panel for date selection.
 * 
 * @author tschmidt
 *
 */
public class CalendarLookUp extends FocusPanel implements HasValue<Datetime>, 
														  HasField<Datetime>,
														  ValueChangeHandler<Datetime>,
														  HasMouseOverHandlers,
														  HasMouseOutHandlers,
														  HasBlurHandlers,
														  HasFocusHandlers {

    protected byte begin;
    protected byte end;
    protected boolean week;
    protected Date weekDate;
    protected PopupPanel pop;
    private DateField field;
    private boolean enabled;
    protected TextBox<String> textbox = new TextBox<String>();
    private IconContainer icon = new IconContainer();
    
    
    /**
     * Inner class to handle the Calendar Button events for the widget
     * @author tschmidt
     *
     */
    private class IconMouseHandler implements MouseDownHandler,
    										  MouseOverHandler,
    										  MouseOutHandler,
    										  MouseUpHandler,
    										  ClickHandler {
    	
        /**
         * Opens CalendarWidget if button is clicked 
         */
        public void onClick(ClickEvent event) {
            doCalendar((Widget)event.getSource(), begin, end);
        }

    	/**
    	 * Adds Pressed style to the calendar button
    	 */
    	public void onMouseDown(MouseDownEvent event) {
           	icon.addStyleName("Pressed");
        }

    	/**
    	 * Adds Hover Style to the Calendar button
    	 */
        public void onMouseOver(MouseOverEvent event) {
            icon.addStyleName("Hover");
        }

        /**
         * Removes Hover style from calendar button
         */
        public void onMouseOut(MouseOutEvent event) {
            icon.removeStyleName("Hover");
        }

        /**
         * Removes Pressed style from calendar button
         */
        public void onMouseUp(MouseUpEvent event) {
            icon.removeStyleName("Pressed");
        }
    }
    
    /**
     * This inner class handles focus, blur and mouse over functions for the widget.  We use the textbox of the 
     * composite as the focusable widget for CalendarLookup, catch thos events here then refire them to other
     * listeners so the receive the full widget as the source of the event.
     * 
     * @author tschmidt
     *
     */
    private class CalendarHandler implements FocusHandler,
    										 BlurHandler,
    										 MouseOverHandler,
    										 MouseOutHandler,
    										 KeyUpHandler,
    										 ValueChangeHandler<String>{

    	/**
    	 * Reference to outer instance.
    	 */
    	private CalendarLookUp source;
    	
    	/**
    	 * Contstructor that sets the source reference to the outer class.
    	 * @param source
    	 */
    	public CalendarHandler(CalendarLookUp source) {
    		this.source = source;
    	}
    	
    	/**
    	 * Sets focus style to widget and fires focus event for the source.
    	 */
		public void onFocus(FocusEvent event) {
	        if (isEnabled()) {
	            if (event.getSource() == textbox) {
	                // we need to set the selected style name to the textbox
	                textbox.addStyleName("Focus");
//	                textbox.removeStyleName("TextboxUnselected");
	                textbox.setFocus(true);
	                icon.addStyleName("Selected");
	            }
	        }
			FocusEvent.fireNativeEvent(event.getNativeEvent(), source);
		}

		/**
		 * Removes focus styles from widget and fires the Bluer event for the source
		 */
		public void onBlur(BlurEvent event) {
	        if (isEnabled()) {
	            if (event.getSource() == textbox) {
	                // we need to set the unselected style name to the textbox
//	                textbox.addStyleName("TextboxUnselected");
	                textbox.removeStyleName("Focus");
	                icon.removeStyleName("Selected");
	            	if("".equals(textbox.getValue()) || (textbox.enforceMask && textbox.picture.equals(textbox.getValue()))){
	            		textbox.setText("");
	        	    	if(field.getValue() != null){
	            			setValue(null,true);
	        	    	}
	            	}
	                if(field.queryMode){
	                	field.setStringValue(textbox.getText());
	                }else
	                	setValue(getValue(),true);
	            }
	        }
        	BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
//			if(field.exceptions != null){
//				field.drawExceptions(source);
//			}
			
		}

		public void onMouseOver(MouseOverEvent event) {
			MouseOverEvent.fireNativeEvent(event.getNativeEvent(), source);	
		}

		public void onMouseOut(MouseOutEvent event) {
			MouseOutEvent.fireNativeEvent(event.getNativeEvent(), source);
		}
		
	    /**
	     * Handler to open Calendar widget if the Enter key is pressed.
	     */
	    public void onKeyUp(KeyUpEvent event) {
	        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	            ((TextBox)event.getSource()).setFocus(false);
	            doCalendar((Widget)event.getSource(), begin, end);
	        }
	    }

		public void onValueChange(ValueChangeEvent<String> event) {
				String value;
				
				if(textbox.enforceMask && textbox.picture.equals(event.getValue()))
					value = "";
				else
					value = event.getValue();
				
				setValue(field.getValueFromString(value),true);
				
				if(field.valid)
					textbox.setText(field.format());
		}
    	
    }

    /**
     * No Arg constructor
     */
    public CalendarLookUp() {
        HorizontalPanel hp = new HorizontalPanel();
        hp.addStyleName("Calendar");
        hp.add(textbox);
        hp.add(icon);
        setWidget(hp);
  
        CalendarHandler handler = new CalendarHandler(this);
        textbox.addFocusHandler(handler);
        textbox.addBlurHandler(handler);
        textbox.addMouseOutHandler(handler);
        textbox.addMouseOverHandler(handler);
        textbox.setStyleName("TextboxUnselected");
        textbox.addKeyUpHandler(handler);
  
        IconMouseHandler iconHandler = new IconMouseHandler();
        icon.setStyleName("CalendarButton");
        icon.addClickHandler(iconHandler);
        icon.addMouseOutHandler(iconHandler);
        icon.addMouseOverHandler(iconHandler);
        icon.addMouseDownHandler(iconHandler);
        icon.addMouseUpHandler(iconHandler);
        icon.setTabIndex(-1);
        textbox.addStyleName("TextboxUnselected");
        
        //textbox.addValueChangeHandler(handler);
        
    }
    
    /**
     * Constructor that accepts a beginning and end precision and a boolean if the calendar selection is for week or single day
     * 
     * @param begin
     *        Datetime beginning byte precision 
     * @param end
     *        Datetime end byte precision
     * @param week
     *        if true the calendar selection will always go to the beginning of the week
     */
    public CalendarLookUp(byte begin,byte end,boolean week) {
        this();
        init(begin,end,week);
    }
    
    /**
     * Sinks a KeyPressEvent for this widget attaching a TabHandler that will override the default
     * browser tab order for the tab order defined by the screen for this widget.
     * 
     * @param handler
     *        Instance of TabHandler that controls tabing logic for widget.
     */
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    
    /**
     * Sets the begin, end and week flag for the widget.
     * @param begin
     * @param end
     * @param week
     */
    public void init(byte begin,byte end,boolean week){
        this.begin = begin;
        this.end = end;
        this.week = week;
        if(begin > Datetime.DAY)
        	setMask("99:99");
        else if(end < Datetime.HOUR)
        	setMask("9999-99-99");
        else
        	setMask("9999-99-99 99:99");
    }
    
    /**
     * creates a new  CalendarWidget and assigns this widget as ValueCahngeHandler 
     * @param sender
     * @param begin
     * @param end
     */
    private void doCalendar(Widget sender, final byte begin, final byte end) {
    	try  {
    		CalendarWidget cal = null;
    		if(field.queryMode)
    			cal = new CalendarWidget(Datetime.getInstance(begin,end),begin,end);
    		else{
    			Datetime date = getValue();
    			if(date == null)
    				date = Datetime.getInstance(begin,end);
    		    cal = new CalendarWidget(date,begin,end);
    		}
    		cal.addValueChangeHandler(this);
    		pop = new PopupPanel(true, false);
    		pop.setWidget(cal);
    		pop.setPopupPosition(textbox.getAbsoluteLeft(),
    				textbox.getAbsoluteTop() + textbox.getOffsetHeight());
    		pop.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    		Window.alert(e.getMessage());
    	}
    }
    
    /**
     * Returns the current Datetime value of this widget.
     */
    public Datetime getValue() {
        if (textbox.getText().equals(""))
            return null;
        Date date;
        try {
        	date = new Date(textbox.getText().replaceAll("-", "/"));
        }catch(Exception e){
        	return null;
        }
        return Datetime.getInstance(begin, end, date);
    }

    public String getStringValue() {
    	return textbox.getText();
    }
    
    /**
     * This method will be called by the CalendarWidget once a Date has been chosen by the user
     * and the popup has closed.
     */
    public void onValueChange(ValueChangeEvent<Datetime> event) {
    	if(field.queryMode){
    		textbox.setText(textbox.getText()+DateTimeFormat.getFormat(field.pattern).format(event.getValue().getDate()));
    	}else
    		setValue(event.getValue(),true);
    	if(pop != null){
    		pop.hide();
    	}
    	textbox.setFocus(true);
    }

    /**
     * Sets the current value of the widget and does not Fire ValueChangeEvent.
     */
	public void setValue(Datetime value) {
		setValue(value,false);
		
	}

	/**
	 * Sets the current value of the widget and will fire a ValueChangeEvent only
	 * if fireEvents flag is passed as true.
	 */
	public void setValue(Datetime value, boolean fireEvents) {
		field.setValue((Datetime)value,fireEvents);
		
        if (value != null)
            textbox.setText(field.format());
        else
           textbox.setText("");
        //if(fireEvents) {
        //	ValueChangeEvent.fire(field, value);
       // }
	}

	/**
	 * Adds a ValueChangeHandler for this widget.
	 */
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * Adds a BlurHandler for this widget.
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler,BlurEvent.getType());
	}
	
	/**
	 * Adds MouseOverHandler for this widget.
	 */
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
    	return addDomHandler(handler,MouseOverEvent.getType());
    }
    
    /**
     * Adds a MouesOutHandler for this widget.
     */
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
    	return addDomHandler(handler, MouseOutEvent.getType());
    }

    /**
     * Adds a an error to the widget.  An error style is added to the widget and on MouseOver a popup will 
     * be displayed with errors.
     */
	public void addException(Exception error) {
		field.addException(error);
		field.drawExceptions(this);
	}

	/**
	 * Clears the error list for this widget and removes the error style from it.
	 */
	public void clearExceptions() {
		field.clearExceptions(this);
	}
	
    /**
     * Returns the field used to validate and format this widget. 
     */
	public Field<Datetime> getField() {
		return field;
	}

	/**
	 * Sets the field to be used by this widget.
	 */
	public void setField(Field<Datetime> field) {
		this.field = (DateField)field;
		//addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}

	/**
	 * Adds FocusHandler to this widget.
	 */
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler,FocusEvent.getType());
	}

	/**
	 * Puts this widget into or out of query mode based on the boolean passed.
	 */
	public void setQueryMode(boolean query) {
		if(query == field.queryMode)
			return;
		
		field.setQueryMode(query);
		textbox.enforceMask = !query && enabled;
		textbox.enforceLength = !query;
		if(query)
			textbox.setLength(255);
		else
			textbox.setLength(textbox.length);
	
	}

	/**
	 * Validates the value for this widget and will display or remove any errors
	 * if necessary.
	 */
	public void checkValue() {
		field.checkValue(this);
		
	}

	/**
	 * If the widget is in Query mode and the queryString value is not null, a QueryData object will be created 
	 * and added to the list passed into the method.  
	 */
	public void getQuery(ArrayList<QueryData> list, String key) {
		if(!field.queryMode)
			return;
		if(field.queryString != null) {
			QueryData qd = new QueryData();
			qd.setQuery(field.queryString);
			qd.setKey(key);
			qd.setType(QueryData.Type.DATE);
			list.add(qd);
		}
	}
	
	public void setQuery(QueryData qd) {
		if(qd != null) {
			textbox.setText(qd.getQuery());
			field.queryString = qd.getQuery();
		}else
			textbox.setText("");
	}

	/**
	 * The current list of error for this widget.
	 */
	public ArrayList<Exception> getExceptions() {
		return field.exceptions;
	}

	/**
	 * Will enable or disable the widget for input based on the boolean passed.
	 */
	public void enable(boolean enabled) {
		this.enabled = enabled;
		textbox.setReadOnly(!enabled);
		textbox.enforceMask = enabled;
		if(enabled) {
			textbox.sinkEvents(Event.ONFOCUS | Event.ONBLUR | Event.ONMOUSEOUT | Event.ONMOUSEOVER | Event.ONKEYUP);
			icon.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEUP);
		}else{
			textbox.unsinkEvents(Event.ONFOCUS | Event.ONBLUR | Event.ONMOUSEOUT | Event.ONMOUSEOVER | Event.ONKEYUP);
			icon.unsinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEUP);
		}
	}
	
	/**
	 * Method used to check if the widget is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Returns the current value in the field for this widget.
	 */
	public Datetime getFieldValue() {
		// TODO Auto-generated method stub
		return field.getValue();
	}
	
	/**
	 * Sets the passed value into the field for this widget.
	 */
	public void setFieldValue(Datetime value){
		field.setValue(value);
		setValue(value);
	}
	
	/**
	 * Sets the width of this widget by setting the width to the textbox and adjusting for the size
	 * of the calendar button.
	 */
	public void setWidth(String width) {
		int wid = 0;
		if(width.indexOf("px") > 0)
			wid = Integer.parseInt(width.substring(0,width.indexOf("px"))) - 18;
		else
			wid = Integer.parseInt(width) - 18;
		textbox.setWidth(wid+"px");
		super.setWidth(width);
	}

	public void setFocus(boolean focused) {
		textbox.setFocus(focused);	
	}
	
	public void selectText() {
		textbox.selectAll();
	}
	
	/**
	 * Adds a ValueChangeHandler to the widget.
	 */
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<Datetime> handler) {
		return field.addValueChangeHandler(handler);
	}

	public void addExceptionStyle(String style) {
		textbox.addStyleName(style);
	}

	public Object getWidgetValue() {
		return textbox.getText();
	}

	public void removeExceptionStyle(String style) {
		textbox.removeStyleName(style);
	}
	
	public void setMask(String mask) {
		textbox.setMask(mask);
	}
    
}
