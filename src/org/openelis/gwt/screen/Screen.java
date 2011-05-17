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
package org.openelis.gwt.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.openelis.gwt.common.FieldErrorException;
import org.openelis.gwt.common.FormErrorException;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.TableFieldErrorException;
import org.openelis.gwt.common.ValidationErrorsList;
import org.openelis.gwt.common.Warning;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.HasDataChangeHandlers;
import org.openelis.gwt.event.HasStateChangeHandlers;
import org.openelis.gwt.event.StateChangeEvent;
import org.openelis.gwt.event.StateChangeHandler;
import org.openelis.gwt.services.ScreenService;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.DeckPanel;
import org.openelis.gwt.widget.HasExceptions;
import org.openelis.gwt.widget.HasValue;
import org.openelis.gwt.widget.Queryable;
import org.openelis.gwt.widget.ScreenWidgetInt;
import org.openelis.gwt.widget.TabPanel;
import org.openelis.gwt.widget.WindowInt;
import org.openelis.gwt.widget.table.Table;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is used to bring together widgets into a logical unit of work that is presented 
 * to the user. 
 *
 */
public class Screen extends SimplePanel implements HasStateChangeHandlers<Screen.State>,
                                     HasDataChangeHandlers, HasResizeHandlers {

	/**
	 * Enumeration of states for a Screen
	 */
    public enum State {
        DEFAULT, DISPLAY, UPDATE, ADD, QUERY, DELETE
    };

    /**
     * Current state of the screen
     */
    protected State                          state        = null;
    
    /**
     * Reference to the window this screen is displayed in
     */
    protected WindowInt                      window;

    /**
     * Definition of the screen containing the layout and a hash of all widgets
     */
    protected ScreenDefInt                def;
    
    /**
     * Default service for the screen
     */
    protected ScreenService               service;
    
    protected String                      fatalError;

    public static HashMap<String, String> consts;

    /**
     * No arg constructor will initiate a blank panel and new FormRPC
     */
    public Screen() {
		addDomHandler(new KeyDownHandler() {
			public void onKeyDown(final KeyDownEvent event) {
				boolean ctrl,alt,shift;
				char key;
				
				/*
				 * If no modifier is pressed then return out
				 */
				if(!event.isAnyModifierKeyDown())
					return;
				
				ctrl = event.isControlKeyDown();
				alt = event.isAltKeyDown();
				shift = event.isShiftKeyDown();
				key = (char)event.getNativeKeyCode();
				
				for(Shortcut handler : def.getShortcuts()) {
					if(handler.ctrl == ctrl && handler.alt == alt && handler.shift == shift && String.valueOf(handler.key).toUpperCase().equals(String.valueOf(key).toUpperCase())){
						if(handler.wid instanceof Button) {
							if(((Button)handler.wid).isEnabled() && !((Button)handler.wid).isLocked()){
								((Focusable)handler.wid).setFocus(true);
								NativeEvent clickEvent = Document.get().createClickEvent(0, 
										handler.wid.getAbsoluteLeft(), 
										handler.wid.getAbsoluteTop(), 
										-1, 
										-1, 
										ctrl, 
										alt, 
										shift, 
										event.isMetaKeyDown());
							    
								ClickEvent.fireNativeEvent(clickEvent, (Button)handler.wid);
								event.stopPropagation();
							}
							event.preventDefault();
							event.stopPropagation();		
						}else if(((ScreenWidgetInt)handler.wid).isEnabled()){ 
							((Focusable)handler.wid).setFocus(true);
							event.preventDefault();
							event.stopPropagation();
						}
					}
				}
			}
		},KeyDownEvent.getType());
		
		addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				Widget focused;
				Tab tab;
				Widget nextWid = null;
				boolean shift;
				
				focused = def.getPanel().getFocused();
				shift = event.isShiftKeyDown();
				
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB && focused != null){
					tab = def.getTabs().get(focused);

					while(true) {
						if(shift){
							nextWid = def.getWidget(tab.getPrev());
							if(nextWid instanceof TabPanel)
								nextWid = def.getWidget(((TabPanel)nextWid).getPrevTabWidget());
							else if(nextWid instanceof DeckPanel)
								nextWid = def.getWidget(((DeckPanel)nextWid).getPrevTabWidget());
						}else{
							nextWid = def.getWidget(tab.getNext());
							if(nextWid instanceof TabPanel)
								nextWid = def.getWidget(((TabPanel)nextWid).getNextTabWidget());
							else if(nextWid instanceof DeckPanel) 
								nextWid = def.getWidget(((DeckPanel)nextWid).getNextTabWidget());	
						}
						if(((ScreenWidgetInt)nextWid).isEnabled()) { 
							def.getPanel().setFocusWidget((Widget)nextWid);
							break;
						}else{
							tab = def.getTabs().get(nextWid);
							if(shift){
								if(def.getWidget(tab.getPrev()) == focused)
									break;
							}else {
								if(def.getWidget(tab.getNext()) == focused)
									break;
							}
						}
					}
					event.preventDefault();
					event.stopPropagation();
				}
			}
		},KeyDownEvent.getType());
    }

    /**
     * Constructor that creates the Screen with the passed Defintion
     * @param def
     */
    public Screen(final ScreenDefInt definition) {
    	this();
    	try {
    		drawScreen(definition);
    	}catch(Exception e) {
    		e.printStackTrace();
    		com.google.gwt.user.client.Window.alert(e.getMessage());
    	}
    }
    
    /**
     * This method will set the screen definition to the screen passed and also 
     * redraw the screen from the new definition.
     * @param def
     * @throws Exception
     */
    public void drawScreen(ScreenDefInt def) throws Exception {
        this.def = def;
        setWidget(def.getPanel());
    }


    /**
     * Sets a reference to the window this screen is contained in
     * @param window
     */
    public void setWindow(WindowInt window) {
        this.window = window;
    }

    /**
     * Method returns a reference to the Window this screen is in
     * @return
     */
    public WindowInt getWindow() {
        return window;
    }

    /**
     * Sets the ScreenDefintion that this screen uses
     * @param def
     */
    public void setDefinition(ScreenDefInt def) {
        this.def = def;
    }

    /**
     * Returns the ScreenDefintion used by this screen
     * @return
     */
    public ScreenDefInt getDefinition() {
        return def;
    }

    /**
     * Sets the name that this screen displays in the Window caption
     * @param name
     */
    public void setName(String name) {
        def.setName(name);
    }

    /**
     * Returns the name used by this screen
     * @return
     */
    public String getName() {
        return def.getName();
    }

    /**
     * Sets the current state of this screen to the passed param and will fire a StateChangeEvent if the
     * state has changed
     * @param state
     */
    public void setState(Screen.State state) {
        if (state != this.state) {
            this.state = state;
            StateChangeEvent.fire(this, state);
        }
    }
    
    /**
     * Returns the current State of the screen.
     * @return
     */
    public State getState() {
    	return state;
    }

    /**
     * This method will ask all widgets in the screen to validate themselves and to
     * display any errors.  If any widget goes into error then the method will return 
     * false.
     * @return
     */
    @SuppressWarnings("rawtypes")
	public boolean validate() {
        boolean valid = true;

        for (Widget wid : def.getWidgets().values()) {
            if(state == State.QUERY) {
                if(wid instanceof Queryable) {
                    ((Queryable)wid).validateQuery();
                    if ( ((HasExceptions)wid).hasExceptions())
                        valid = false;
                }
            }else {
                if(wid instanceof HasValue) {
                    ((HasValue)wid).validateValue();
                    if ( ((HasExceptions)wid).hasExceptions())
                        valid = false;
                }
            }
        }
        return valid;
    }

    /**
     * This method will ask all widgets for any Query values that were entered by the user,
     * and will return an ArrayList of QueryData objects to send back to the server to 
     * execute the query.
     * 
     * @return
     */
    public ArrayList<QueryData> getQueryFields() {
        Set<String> keys;
        ArrayList<QueryData> list;

        list = new ArrayList<QueryData>();
        keys = def.getWidgets().keySet();
        for (String key : keys) {
            if (def.getWidget(key) instanceof Queryable) {
                Object query = ((Queryable)def.getWidget(key)).getQuery();
                if(query instanceof Object[]){
                    QueryData[] qds = (QueryData[])query;
                    for(int i = 0; i < qds.length; i++) 
                        list.add(qds[i]);                    
                }else if(query != null) {
                    ((QueryData)query).setKey(key);
                    list.add((QueryData)query);
                }       
            }
        }
        return list;
    }

    /**
     * This method is used to display a list of errors returned from the server on the widgets
     * they belonfg to. 
     * @param errors
     */
    public void showErrors(ValidationErrorsList errors) {
        ArrayList<LocalizedException> formErrors;
        TableFieldErrorException tableE;
        FormErrorException formE;
        FieldErrorException fieldE;
        Table tableWid;
        HasExceptions field;

        formErrors = new ArrayList<LocalizedException>();
        for (Exception ex : errors.getErrorList()) {
            if (ex instanceof TableFieldErrorException) {
                tableE = (TableFieldErrorException) ex;
                tableWid = (Table)def.getWidget(tableE.getTableKey());
                tableWid.addException(tableE.getRowIndex(),tableWid.getColumnByName(tableE.getFieldName()),tableE);
            } else if (ex instanceof FormErrorException) {
                formE = (FormErrorException)ex;
                formErrors.add(formE);
            } else if (ex instanceof FieldErrorException) {
                fieldE = (FieldErrorException)ex;
                
                field = (HasExceptions)def.getWidget(fieldE.getFieldName());
                                
                if(field != null)
                 field.addException(fieldE);
                
            }
        }

        if (formErrors.size() == 0)
            window.setError(consts.get("gen.correctErrors"));
        else if (formErrors.size() == 1)
            window.setError(formErrors.get(0).getMessage());
        else {
            window.setError("(Error 1 of " + formErrors.size() + ") " +
                            formErrors.get(0).getMessage());
            window.setMessagePopup(formErrors, "ErrorPanel");
        }
    }

    /**
     * This method presents the user with a confirm dialog with a list of warnings on the screen 
     * and asks to confirm if they want to commit the current action.
     * @param warnings
     */
    protected void showWarningsDialog(ValidationErrorsList warnings) {
        String warningText = consts.get("warningDialogLine1") + "\n";

        for (Exception ex : warnings.getErrorList()) {
            if (ex instanceof Warning)
                warningText += " * " + ex.getMessage() + "\n";
        }
        warningText += "\n" + consts.get("warningDialogLastLine");

        if (com.google.gwt.user.client.Window.confirm(warningText))
            commitWithWarnings();
    }

    /**
     *by default this method does nothing
     *but it can be overridden by screens to do screen
     *specific actions 
     */
    protected void commitWithWarnings() {

    }

    /**
     * This method will clear all errors on the screen.
     */
    public void clearErrors() {
        for (Widget wid : def.getWidgets().values()) {
            if (wid instanceof HasExceptions)
                ((HasExceptions)wid).clearExceptions();
            if (wid instanceof HasExceptions)
                ((HasExceptions)wid).clearExceptions();
        }
        window.clearStatus();
        window.clearMessagePopup("");
    }
    
    /**
     * Registers the widgets event handlers to the screen.
     * @param wid
     * @param screenHandler
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addScreenHandler(Widget wid, ScreenEventHandler<?> screenHandler) {
        assert wid != null : "addScreenHandler received a null widget";

        screenHandler.target = wid;
        addDataChangeHandler(screenHandler);
        addStateChangeHandler(screenHandler);
        if (wid instanceof HasClickHandlers)
            ((HasClickHandlers)wid).addClickHandler(screenHandler);
        if (wid instanceof HasValueChangeHandlers) {
            ((HasValueChangeHandlers)wid).addValueChangeHandler(screenHandler);
        }
    }

    /**
     * Registers a DataChangeHandler to the Screen.
     */
    public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
        return addHandler(handler, DataChangeEvent.getType());
    }

    /**
     * Registers a StateChangeHandler to the Screen.
     */
    public HandlerRegistration addStateChangeHandler(StateChangeHandler<org.openelis.gwt.screen.Screen.State> handler) {
        return addHandler(handler, StateChangeEvent.getType());
    }

    /**
     * Registers a ResizeHandler to the Screen.
     */
    public HandlerRegistration addResizeHandler(ResizeHandler handler) {
        return addHandler(handler, ResizeEvent.getType());
    }

    /**
     * This method will set the Focus of the screen to the passed widget or
     * remove focus altogether if passed null.
     * @param widget
     */
    protected void setFocus(Widget widget) {
        def.getPanel().setFocusWidget(widget);
    }
}
