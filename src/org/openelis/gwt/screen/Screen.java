package org.openelis.gwt.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.openelis.ui.common.FieldErrorException;
import org.openelis.ui.common.FormErrorException;
import org.openelis.ui.common.TableFieldErrorException;
import org.openelis.ui.common.ValidationErrorsList;
import org.openelis.ui.common.Warning;
import org.openelis.ui.common.data.QueryData;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.HasDataChangeHandlers;
import org.openelis.gwt.event.HasStateChangeHandlers;
import org.openelis.gwt.event.StateChangeEvent;
import org.openelis.gwt.event.StateChangeHandler;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.ScreenWindowInt;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.ui.widget.WindowInt;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the base class for every OpenELIS screen.
 */
public class Screen extends Composite implements
		HasStateChangeHandlers<Screen.State>, HasDataChangeHandlers,
		HasResizeHandlers {

	public enum State {
		DEFAULT, DISPLAY, UPDATE, ADD, QUERY, DELETE
	};

	public State state = null;
	public WindowInt window;
	

	protected ScreenDefInt def;

	public final AbsolutePanel screenpanel = new AbsolutePanel();
	//public static HashMap<String, String> consts;

	/**
	 * No arg constructor will initiate a blank panel
	 */
	public Screen() {
		initWidget(screenpanel);
		sinkEvents(Event.ONKEYPRESS);
	}

	/**
	 * Constructor will initiate a screen using the screen definition
	 */
	public Screen(ScreenDefInt def) {
		initWidget(screenpanel);
		this.def = def;
		screenpanel.add(def.getPanel());
	}

	/**
	 * ? 
	 */
	public void drawScreen(ScreenDefInt def) throws Exception {
		this.def = def;
		screenpanel.clear();
		screenpanel.add(def.getPanel());
	}

	/**
	 * Sets the parent window for this screen.
	 */
	public void setWindow(WindowInt window) {
		this.window = window;
	}

	public WindowInt getWindow() {
		return window;
	}

	/**
	 * Set the definition for this screen. Use this method to associate this
	 * screen with given definition (often used in tabs).
	 */
	public void setDefinition(ScreenDefInt def) {
		this.def = def;
	}

	public ScreenDefInt getDefinition() {
		return def;
	}

	/**
	 * Sets the screen's name that appears in the screen title
	 */
	public void setName(String name) {
		def.setName(name);
	}

	public String getName() {
		return def.getName();
	}

	/**
	 * Sets the screen's state and fires the state change event if state was
	 * changed.
	 */
	public void setState(Screen.State state) {
		if (state != this.state) {
			this.state = state;
			StateChangeEvent.fire(this, state);
		}
	}

	/**
	 * Validates all the screen widgets for required/formating/... and returns
	 * true if all the widgets have valid values, false otherwise.
	 */
	public boolean validate() {
		boolean valid = true;

		for (Widget wid : def.getWidgets().values()) {
			if (wid instanceof HasField) {
				((HasField) wid).checkValue();
				if (((HasField) wid).getExceptions() != null)
					valid = false;
			}
		}
		return valid;
	}

	/**
	 * Returns the widget's data in form of query. The list is suitable for
	 * being used as parameters in a query.
	 */
	public ArrayList<QueryData> getQueryFields() {
		Set<String> keys;
		ArrayList<QueryData> list;

		list = new ArrayList<QueryData>();
		keys = def.getWidgets().keySet();
		for (String key : def.getWidgets().keySet()) {
			if (def.getWidget(key) instanceof HasField) {
				((HasField) def.getWidget(key)).getQuery(list, key);
			}
		}
		/*
		for(QueryData qd : list) {
			if(qd.query.startsWith("|")) {
				qd.query = qd.query.substring(1);
				qd.logical = QueryData.Logical.OR;
			}
		}
		*/
		return list;
	}

	/**
	 * Shows a list of validation errors specified for individual widgets. Depending on
	 * the error type, the error is either added to a widget or the screen.
	 */
	public void showErrors(ValidationErrorsList errors) {
		ArrayList<Exception> formErrors;
		TableFieldErrorException tableE;
		FormErrorException formE;
		FieldErrorException fieldE;
		TableWidget tableWid;
		HasField field;

		formErrors = new ArrayList<Exception>();
		for (Exception ex : errors.getErrorList()) {
			if (ex instanceof TableFieldErrorException) {
				tableE = (TableFieldErrorException) ex;
				tableWid = (TableWidget) def.getWidget(tableE.getTableKey());
				tableWid.setCellException(tableE.getRowIndex(),
						tableE.getFieldName(), tableE);
			} else if (ex instanceof FormErrorException) {
				formE = (FormErrorException) ex;
				formErrors.add(formE);
			} else if (ex instanceof FieldErrorException) {
				fieldE = (FieldErrorException) ex;
				field = (HasField) def.getWidget(fieldE.getFieldName());
				if (field != null)
					field.addException(fieldE);
			}
		}
		if (formErrors.size() == 0)
			window.setError("Please correct the errors indicated, then press Commit");
		else if (formErrors.size() == 1)
			window.setError(formErrors.get(0).getMessage());
		else {
			window.setError("(Error 1 of " + formErrors.size() + ") "
					+ formErrors.get(0).getMessage());
			//window.setMessagePopup(formErrors, "ErrorPanel");
		}
	}

	/**
	 * Shows a list of warnings in the form of a confirm dialog. Specific screens
	 * need to override the commitWithWarnings() method to catch the user's response.
	 */
	protected void showWarningsDialog(ValidationErrorsList warnings) {
		String warningText = "There are warnings on the screen:" + "\n";

		for (Exception ex : warnings.getErrorList()) {
			if (ex instanceof Warning)
				warningText += " * " + ex.getMessage() + "\n";
		}
		warningText += "\n" + "Press Ok to commit anyway or cancel to fix these warnings.";
		

		if (Window.confirm(warningText))
			commitWithWarnings();
	}

	/**
	 * Override this method to handle user's confirmation for showWarningDialog.
	 */
	protected void commitWithWarnings() {
	}

	/**
	 * Clears widget errors and the screen form error area.
	 */
	public void clearErrors() {
		for (Widget wid : def.getWidgets().values()) {
			if (wid instanceof HasField)
				((HasField) wid).clearExceptions();
		}
		window.clearStatus();
		window.clearMessagePopup("");
	}

	public void addScreenHandler(Widget wid, ScreenEventHandler<?> screenHandler) {
		assert wid != null : "addScreenHandler received a null widget";

		screenHandler.target = wid;
		addDataChangeHandler(screenHandler);
		addStateChangeHandler(screenHandler);
		if (wid instanceof HasField)
			((HasField) wid).addFieldValueChangeHandler(screenHandler);
		if (wid instanceof HasClickHandlers)
			((HasClickHandlers) wid).addClickHandler(screenHandler);
	}
	
	/**
	 * Sets the screen focus to specified widget.  
	 */
	protected void setFocus(Widget widget) {
		def.getPanel().setFocusWidget(widget);
		 //
		 // sets focus back to the window to allow short-cuts to work
		 //
		if (widget == null && window != null) {
		    //if (window.getBrowser() != null)
		      //  window.getBrowser().setFocusedWindow();
		}
	}

    /**
     * Event handlers inherited methods
     */
    public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
        return addHandler(handler, DataChangeEvent.getType());
    }

    public HandlerRegistration addStateChangeHandler(StateChangeHandler<org.openelis.gwt.screen.Screen.State> handler) {
        return addHandler(handler, StateChangeEvent.getType());
    }

    public HandlerRegistration addResizeHandler(ResizeHandler handler) {
        return addHandler(handler, ResizeEvent.getType());
    }
}
