package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.Exceptions;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.constants.Constants;
import org.openelis.gwt.resources.GeneralCSS;
import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.resources.TextCSS;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class PassWordTextBox extends Composite implements ScreenWidgetInt, 
														  Focusable,
														  HasBlurHandlers, 
														  HasFocusHandlers,
														  HasValue<String>, 
														  HasHelper<String>,
														  HasExceptions {


	/**
	 * Wrapped GWT TextBox
	 */
	public PasswordTextBox                          textbox;


	/**
	 * Exceptions list
	 */
	protected Exceptions                            exceptions;

	/**
	 * Data moved from Field to the widget
	 */
	protected int                                   maxLength;
	protected boolean                               required;
	protected String                                value;

	/**
	 * This class replaces the functionality that Field used to provide but now
	 * in a static way.
	 */
	protected WidgetHelper<String>                  helper    = new StringHelper();

	protected PassWordTextBox                       source = this;
	
	protected TextCSS                               css;

	/**
	 * The Constructor now sets the wrapped GWT TextBox as the element widget of
	 * this composite and adds an anonymous ValueCahngeHandler to handle input
	 * from the user.
	 */
	public PassWordTextBox() {
		init();
	}

	public void init() {
		css = OpenELISResources.INSTANCE.text();
		css.ensureInjected();
		
		textbox = new PasswordTextBox();

		setEnabled(false);

		addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				textbox.addStyleName(css.Focus());
				if(isEnabled());
					textbox.selectAll();
			}
		});

		addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				textbox.removeStyleName(css.Focus());
				textbox.setSelectionRange(0, 0);

				if(isEnabled()) 
					finishEditing();
			}
		});

		initWidget(textbox);
		
		exceptions = new Exceptions();
	}

	public String getText() {
		return textbox.getText();
	}

	public void setText(String text) {
		textbox.setText(text);
	}

	/**
	 * This method is overridden to make sure the Case style is applied to the widget  
	 */
	@Override
	public void setStyleName(String style) {
		textbox.setStyleName(style);
	}


	/**
	 * Sets the maximum input characters allowed for this text field.
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
		textbox.setMaxLength(maxLength);
	}


	/**
	 * Method used to set if this widget is required to have a value inputed.
	 * @param required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	// ************** Implementation of ScreenWidgetInt ********************

	/**
	 * Enables or disables the textbox for editing.
	 */
	public void setEnabled(boolean enabled) {
		textbox.setReadOnly( !enabled);
	}

	/**
	 * Returns whether the text is enabled for editing
	 */
	public boolean isEnabled() {
		return !textbox.isReadOnly();
	}

	// ********** Implementation of HasHelper ***************************
	/**
	 * Sets the implentation of the WidgetHelper<T> interface to be used by this
	 * widget.
	 */
	public void setHelper(WidgetHelper<String> helper) {
		this.helper = helper;
	}

	/**
	 * Returns the helper set for this widget
	 */
	public WidgetHelper<String> getHelper() {
		return helper;
	}

	// ************** Implementation of HasValue<T> interface ***************

	/**
	 * Returns the current value for this widget.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the current value of this widget without firing the
	 * ValueChangeEvent.
	 */
	public void setValue(String value) {
		setValue(value, false);
	}

	/**
	 * Sets the current value of this widget and will fire a ValueChangeEvent if
	 * the value is different than what is currently stored.
	 */
	public void setValue(String value, boolean fireEvents) {

		if(!Util.isDifferent(this.value, value)) {
			if(value != null)
				textbox.setText(helper.format(value));
			return;
		}

		this.value = value;
		if (value != null) {
			textbox.setText(helper.format(value));
		} else {
			textbox.setText("");
		}

		if (fireEvents) 
			ValueChangeEvent.fire(this, value);
	}

	/**
	 * This method takes raw input from widget and will call helper to get the 
	 * appropriate value to set and also set any validation exceptions if present
	 */
	public void finishEditing() {
		String text;

		text = textbox.getText();

		clearValidateExceptions();

		try {
			setValue(helper.getValue(text), true);
			if (required && getValue() == null) 
				throw new LocalizedException(Constants.get().fieldRequired());
		} catch (LocalizedException e) {
			addValidateException(e);
		}
		ExceptionHelper.checkExceptionHandlers(this);
	}

	/**
	 * Method used to validate the inputed query string by the user.
	 */
	public void validateQuery() {
		try {
			clearValidateExceptions();
			helper.validateQuery(textbox.getText());
		} catch (LocalizedException e) {
			addValidateException(e);
		}
		ExceptionHelper.checkExceptionHandlers(this);
	}

	// ********** Implementation of HasException interface ***************
	/**
	 * Convenience method to check if a widget has exceptions so we do not need
	 * to go through the cost of merging the logical and validation exceptions
	 * in the getExceptions method.
	 * 
	 * @return
	 */
	public boolean hasExceptions() {
		if(getValidateExceptions() != null)
			return true;

		if (required && getValue() == null) {
			addValidateException(new LocalizedException(Constants.get().fieldRequired()));
			ExceptionHelper.checkExceptionHandlers(this);
		}

		return getEndUserExceptions() != null || getValidateExceptions() != null;
	}

	/**
	 * Adds a manual Exception to the widgets exception list.
	 */
	public void addException(LocalizedException error) {
		exceptions.addException(error);
		ExceptionHelper.checkExceptionHandlers(this);
	}

	protected void addValidateException(LocalizedException error) {
		exceptions.addValidateException(error);

	}

	/**
	 * Combines both exceptions list into a single list to be displayed on the
	 * screen.
	 */
	public ArrayList<LocalizedException> getValidateExceptions() {
		return exceptions.getValidateExceptions();
	}

	public ArrayList<LocalizedException> getEndUserExceptions() {
		return exceptions.getEndUserExceptions();
	}

	/**
	 * Clears all manual and validate exceptions from the widget.
	 */
	public void clearExceptions() {
		exceptions.clearExceptions();
		removeExceptionStyle();
		ExceptionHelper.clearExceptionHandlers(this);
	}

	public void clearEndUserExceptions() {
		exceptions.clearEndUserExceptions();
		ExceptionHelper.checkExceptionHandlers(this);
	}

	public void clearValidateExceptions() {
		exceptions.clearValidateExceptions();
		ExceptionHelper.checkExceptionHandlers(this);
	}


	/**
	 * Will add the style to the widget.
	 */
	public void addExceptionStyle() {
		if(ExceptionHelper.isWarning(this))
			addStyleName(css.InputWarning());
		else
			addStyleName(css.InputError());
	}

	/**
	 * will remove the style from the widget
	 */
	public void removeExceptionStyle() {
		removeStyleName(css.InputError());
		removeStyleName(css.InputWarning());
	}

	// ************* Implementation of Focusable ******************

	/**
	 * Method only implemented to satisfy Focusable interface.
	 */
	public int getTabIndex() {
		return -1;
	}

	/**
	 * Method only implemented to satisfy Focusable interface.
	 */
	public void setTabIndex(int index) {

	}

	/**
	 * Method only implemented to satisfy Focusable interface.
	 */
	public void setAccessKey(char key) {

	}

	/**
	 * Exposing this method on the wrapped widget
	 */
	public void selectAll() {
		textbox.selectAll();
	}

	/**
	 * Exposing this method on the wrapped widget
	 */
	public void setSelectionRange(int pos, int length) {
		textbox.setSelectionRange(pos, length);
	}

	/**
	 * Exposing this method on the wrapped widget
	 */
	public void unselectAll() {
		textbox.setSelectionRange(0, 0);
	}

	/**
	 * This is need for Focusable interface and to allow programmatic setting of
	 * focus to this widget. We use the wrapped TextBox to make this work.
	 */
	public void setFocus(boolean focused) {
		textbox.setFocus(true);
	}

	// ************ Handler Registration methods *********************

	/**
	 * The Screen will add its screenHandler here to register for the
	 * onValueChangeEvent
	 */
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * This Method is here so the Focus logic of ScreenPanel can be notified
	 */
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler, BlurEvent.getType());
	}

	/**
	 * This method is here so the Focus logic of ScreenPanel can be notified
	 */
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler, FocusEvent.getType());
	}

	/**
	 * Adds a mouseover handler to the textbox for displaying Exceptions
	 */
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	/**
	 * Adds a MouseOut handler for hiding exceptions display
	 */
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}
}
