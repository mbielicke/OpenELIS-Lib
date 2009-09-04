package org.openelis.gwt.screen.rewrite;

import java.util.ArrayList;
import java.util.HashMap;

import org.openelis.gwt.common.FieldErrorException;
import org.openelis.gwt.common.FormErrorException;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.TableFieldErrorException;
import org.openelis.gwt.common.ValidationErrorsList;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.HasDataChangeHandlers;
import org.openelis.gwt.event.HasStateChangeHandlers;
import org.openelis.gwt.event.StateChangeEvent;
import org.openelis.gwt.event.StateChangeHandler;
import org.openelis.gwt.screen.ScreenWindow;
import org.openelis.gwt.services.ScreenService;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.table.rewrite.TableWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.DefaultHandlerRegistration;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class Screen extends Composite implements HasStateChangeHandlers<Screen.State>, HasDataChangeHandlers {
	
	public final AbsolutePanel panel = new AbsolutePanel();
    public String name;
    public enum State {DEFAULT,DISPLAY,UPDATE,ADD,QUERY,DELETE};
    public State state = State.DEFAULT;
    protected ScreenDef def;
    public ScreenWindow window;
    public static HashMap<String,String> consts;
    protected ScreenService service;
    
    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public Screen() {
        initWidget(panel);
        sinkEvents(Event.ONKEYPRESS);
    }
    
    @Override
    public void onBrowserEvent(Event event) {
    	if(DOM.eventGetType(event) == Event.ONKEYPRESS)
    		event.preventDefault();
    	super.onBrowserEvent(event);
    }
    
    public Screen(String url) throws Exception {
    	initWidget(panel);
    	service = new ScreenService(url);
    	def = new ScreenDef();
    	def.loadURL = url;
    	UIUtil.createWidgets(def);
    	panel.add(def.panel);
    }
    
    public Screen(ScreenDef def) {
    	initWidget(panel);
    	this.def = def;
    	panel.add(def.panel);
    }
    
    public void drawScreen(ScreenDef def) throws Exception {
    	this.def = def;
    	UIUtil.createWidgets(def,false);
    	panel.clear();
    	panel.add(def.panel);
    }
    
    public void setWindow(ScreenWindow window) {
        this.window = window;
    }
    
    public ScreenDef getDefinition() {
        return def;
    }
    
    public ScreenWindow getWindow() {
        return window;
    }
    
        
    public void setDef(ScreenDef def) {
    	this.def = def;
    }
    
    public void addScreenHandler(Widget wid, ScreenEventHandler<?> screenHandler) {
    	screenHandler.target = wid;
    	addDataChangeHandler(screenHandler);
    	addStateChangeHandler(screenHandler);
    	if(wid instanceof HasField)
    		((HasField)wid).addFieldValueChangeHandler(screenHandler);
    	if(wid instanceof HasClickHandlers) 
    		((HasClickHandlers)wid).addClickHandler(screenHandler);
    }

	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		return addHandler(handler, DataChangeEvent.getType());
	}

	public HandlerRegistration addStateChangeHandler(
			StateChangeHandler<org.openelis.gwt.screen.rewrite.Screen.State> handler) {
		return addHandler(handler, StateChangeEvent.getType());
	}
	
	public void removeFocus() {
		for(Widget wid : def.getWidgets().values()){
			if(wid instanceof Focusable) {
				((Focusable)wid).setFocus(false);
			}
		}
	}
	
	public void setState(Screen.State state){
        this.state = state;
        StateChangeEvent.fire(this, state);
    }
	
	protected boolean validate() {
        boolean valid = true;
        for (Widget wid : def.getWidgets().values()) {
            if (wid instanceof HasField) {
                ((HasField)wid).checkValue();
                if (((HasField)wid).getErrors() != null) {
                    valid = false;
                }
            }
        }
        return valid;
    }
    
    protected void clearErrors() {
        for (Widget wid : def.getWidgets().values()) {
            if (wid instanceof HasField)
                ((HasField)wid).clearErrors();
        }
    }
    
    protected void showErrors(ValidationErrorsList errors) {
        ArrayList<String> formErrors = new ArrayList<String>();
        
        for (Exception ex : errors.getErrorList()) {
            if (ex instanceof TableFieldErrorException) {
                TableFieldErrorException tfe = (TableFieldErrorException)ex;
                ((TableWidget)def.getWidget(tfe.getTableKey())).setCellError(tfe.getRowIndex(),
                                                                             tfe.getFieldName(),
                                                                             consts.get(tfe.getMessage()));
            } else if(ex instanceof FormErrorException){
                FormErrorException fe = (FormErrorException)ex;
                formErrors.add(consts.get(fe.getMessage()));
                
            } else {
                FieldErrorException fe = (FieldErrorException)ex;
                ((HasField)def.getWidget(fe.getFieldName())).addError(consts.get(fe.getMessage()));
            }
        }
        
        if(formErrors.size() == 0)
            window.setError(consts.get("correctErrors"));
        else if(formErrors.size() == 1)
            window.setError(formErrors.get(0));
        else{
            window.setError("(Error 1 of "+formErrors.size()+") " + formErrors.get(0));
            window.setMessagePopup((String[])formErrors.toArray(), "ErrorPanel");
        }
    }
    
    protected String getString(Object obj) {
        if (obj == null)
            return "";

        return obj.toString();
    }
}
