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
import java.util.Set;

import org.openelis.gwt.common.FieldErrorException;
import org.openelis.gwt.common.FormErrorException;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.TableFieldErrorException;
import org.openelis.gwt.common.ValidationErrorsList;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.StateChangeEvent;
import org.openelis.gwt.event.StateChangeHandler;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.HasExceptions;
import org.openelis.gwt.widget.Queryable;
import org.openelis.gwt.widget.ScreenWidgetInt;
import org.openelis.gwt.widget.WindowInt;
import org.openelis.gwt.widget.table.Table;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

/**
 *  This class holds all widgets for a screen into a single panel and has logic to handle
 *  set focus to widgets and well as keyboard shortcuts to widgets.
 */
public abstract class ViewPanel extends AbsolutePanel implements FocusHandler, ScreenViewInt, HasResizeHandlers {
	
	Focusable focused;
	private int width,height;
	
	public ViewPanel() {
		addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB && focused != null) {
					
					if(focused instanceof ScreenWidgetInt && !((ScreenWidgetInt)focused).isEnabled()) {
						event.preventDefault();
						event.stopPropagation();
						return;
					}
					
					Focusable next = focused;
					do{
						next = tab(next,event.isShiftKeyDown());
					}while(next != null && !((ScreenWidgetInt)next).isEnabled());
					if(next != null)
						next.setFocus(true);
					
					return;
				}
				
				if(event.isAnyModifierKeyDown()) {
				
				    boolean ctrl,alt,shift;
				    char key;
				
				/*
				 * If no modifier is pressed then return out
				 */
				
									
				    ctrl = event.isControlKeyDown();
				    alt = event.isAltKeyDown();
				    shift = event.isShiftKeyDown();
				    key = (char)event.getNativeKeyCode();
					final Focusable target = shortcut(ctrl,alt,shift, Character.toUpperCase(key)); 
					
					if(target != null) {
						if(target instanceof Button) {
							if(((Button)target).isEnabled() && !((Button)target).isLocked()){
								((Focusable)target).setFocus(true);
								Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
									public void execute() {
										NativeEvent clickEvent = Document.get().createClickEvent(0, 
												-1, 
												-1, 
												-1, 
												-1, 
												false, 
												false, 
												false, 
												false);
									    
										ClickEvent.fireNativeEvent(clickEvent, (Button)target);
									}
								});

								event.stopPropagation();
							}
							event.preventDefault();
							event.stopPropagation();
						}else if(((ScreenWidgetInt)target).isEnabled()){ 
							((Focusable)target).setFocus(true);
							event.preventDefault();
							event.stopPropagation();
						}
					}
				}
			}
		},KeyDownEvent.getType());
		
	}
	
	public void setWidth(int width) {
		int diff;
		
		diff = width - this.width;
		this.width = width;
		super.setWidth(width+"px");
		if(diff != 0)
			ResizeEvent.fire(this, diff, 0);
	}
	
	public void setHeight(int height) {
		int diff;
		
		diff = height - this.height;
		this.height = height;
		super.setHeight(height+"px");
		if(diff != 0)
			ResizeEvent.fire(this,0,diff);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	@Override
	public void setWidth(String width) {
		this.width = Integer.parseInt(width.replace("px",""));
		super.setWidth(width);
	}
	
	@Override
	public void setHeight(String height) {
		this.height = Integer.parseInt(height.replace("px",""));
		super.setHeight(height);
	}
	
	public void onFocus(FocusEvent event) {
		focused = (Focusable)event.getSource();
	}
    
    public void finishEditing() {
    	if(focused != null && focused instanceof ScreenWidgetInt) 
    		((ScreenWidgetInt)focused).finishEditing();
    }
    
    public boolean validate() {
        boolean valid = true;        
        
        for(Widget wid : getWidgets().values()) {
          if(wid instanceof HasExceptions) {
        	  if ( ((HasExceptions)wid).hasExceptions())
        		  valid = false;
          }
        }
        
        return valid;
    }
    
    public void showErrors(ValidationErrorsList errors, WindowInt window) {
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
                tableWid = (Table)getWidget(tableE.getTableKey());
                tableWid.addException(tableE.getRowIndex(),tableWid.getColumnByName(tableE.getFieldName()),tableE);
            } else if (ex instanceof FormErrorException) {
                formE = (FormErrorException)ex;
                formErrors.add(formE);
            } else if (ex instanceof FieldErrorException) {
                fieldE = (FieldErrorException)ex;
                
                field = (HasExceptions)getWidget(fieldE.getFieldName());
                                
                if(field != null)
                 field.addException(fieldE);
                
            }
        }
        
        if (formErrors.size() == 0)
            window.setError("Please correct the errors indicated, then press Commit");
        else if (formErrors.size() == 1)
            window.setError(formErrors.get(0).getMessage());
        else {
            window.setError("(Error 1 of " + formErrors.size() + ") " +
                            formErrors.get(0).getMessage());
            window.setMessagePopup(formErrors, "ErrorPanel");
        }
    }
    
    public void clearErrors(WindowInt window) {
        for (Widget wid : getWidgets().values()) {
            if (wid instanceof HasExceptions)
                ((HasExceptions)wid).clearExceptions();
        }
        window.clearStatus();
        window.clearMessagePopup("");
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
        keys = getWidgets().keySet();
        for (String key : keys) {
            if (getWidget(key) instanceof Queryable) {
            	if(((Queryable)getWidget(key)).isQueryMode()) {
            		Object query = ((Queryable)getWidget(key)).getQuery();
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
        }
        return list;
    }
    
    public <T> void addScreenHandler(Widget wid, ScreenEventHandler<T> screenHandler) {
        assert wid != null : "addScreenHandler received a null widget";

        screenHandler.target = wid;
        addDataChangeHandler(screenHandler);
        addStateChangeHandler(screenHandler);
        if (wid instanceof HasClickHandlers)
            ((HasClickHandlers)wid).addClickHandler(screenHandler);
        if (wid instanceof HasValueChangeHandlers) {
            ((HasValueChangeHandlers<T>)wid).addValueChangeHandler(screenHandler);
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
    public HandlerRegistration addStateChangeHandler(StateChangeHandler handler) {
        return addHandler(handler, StateChangeEvent.getType());
    }
    
    public HandlerRegistration addResizeHandler(ResizeHandler handler) {
    	return addHandler(handler,ResizeEvent.getType());
    }


} 

