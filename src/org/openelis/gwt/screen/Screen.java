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
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.ScreenWindow;
import org.openelis.gwt.widget.table.TableWidget;

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

public class Screen extends Composite implements HasStateChangeHandlers<Screen.State>,
                                     HasDataChangeHandlers, HasResizeHandlers {

    public enum State {
        DEFAULT, DISPLAY, UPDATE, ADD, QUERY, DELETE
    };

    public State                          state        = null;
    public ScreenWindow                   window;

    protected ScreenDefInt                def;
    protected ScreenService               service;
    protected String                      fatalError;

    public final AbsolutePanel            screenpanel  = new AbsolutePanel();
    public static HashMap<String, String> consts;

    /**
     * No arg constructor will initiate a blank panel and new FormRPC
     */
    public Screen() {
        initWidget(screenpanel);
        sinkEvents(Event.ONKEYPRESS);
    }

    public Screen(ScreenDefInt def) {
        initWidget(screenpanel);
        this.def = def;
        screenpanel.add(def.getPanel());
    }

    public void setWindow(ScreenWindow window) {
        this.window = window;
    }

    public ScreenWindow getWindow() {
        return window;
    }

    public void setDefinition(ScreenDefInt def) {
        this.def = def;
    }

    public ScreenDefInt getDefinition() {
        return def;
    }

    public void setName(String name) {
        def.setName(name);
    }

    public String getName() {
        return def.getName();
    }

    public void setState(Screen.State state) {
        if (state != this.state) {
            this.state = state;
            StateChangeEvent.fire(this, state);
        }
    }

    public boolean validate() {
        boolean valid = true;

        for (Widget wid : def.getWidgets().values()) {
            if (wid instanceof HasField) {
                ((HasField)wid).checkValue();
                if ( ((HasField)wid).getExceptions() != null)
                    valid = false;
            }
        }
        return valid;
    }

    public ArrayList<QueryData> getQueryFields() {
        Set<String> keys;
        ArrayList<QueryData> list;

        list = new ArrayList<QueryData>();
        keys = def.getWidgets().keySet();
        for (String key : def.getWidgets().keySet()) {
            if (def.getWidget(key) instanceof HasField) {
                ((HasField)def.getWidget(key)).getQuery(list, key);
            }
        }
        return list;
    }

    public void showErrors(ValidationErrorsList errors) {
        ArrayList<LocalizedException> formErrors;
        TableFieldErrorException tableE;
        FormErrorException formE;
        FieldErrorException fieldE;
        TableWidget tableWid;
        HasField field;

        formErrors = new ArrayList<LocalizedException>();
        for (Exception ex : errors.getErrorList()) {
            if (ex instanceof TableFieldErrorException) {
                tableE = (TableFieldErrorException) ex;
                tableWid = (TableWidget)def.getWidget(tableE.getTableKey());
                tableWid.setCellException(tableE.getRowIndex(), tableE.getFieldName(), tableE);
            } else if (ex instanceof FormErrorException) {
                formE = (FormErrorException)ex;
                formErrors.add(formE);
            } else if (ex instanceof FieldErrorException) {
                fieldE = (FieldErrorException)ex;
                field = (HasField)def.getWidget(fieldE.getFieldName());
                
                if(field != null)
                 field.addException(fieldE);
            }
        }

        if (formErrors.size() == 0)
            window.setError(consts.get("correctErrors"));
        else if (formErrors.size() == 1)
            window.setError(formErrors.get(0).getMessage());
        else {
            window.setError("(Error 1 of " + formErrors.size() + ") " +
                            formErrors.get(0).getMessage());
            window.setMessagePopup(formErrors, "ErrorPanel");
        }
    }

    protected void showWarningsDialog(ValidationErrorsList warnings) {
        String warningText = consts.get("warningDialogLine1") + "\n";

        for (Exception ex : warnings.getErrorList()) {
            if (ex instanceof Warning)
                warningText += " * " + ex.getMessage() + "\n";
        }
        warningText += "\n" + consts.get("warningDialogLastLine");

        if (Window.confirm(warningText))
            commitWithWarnings();
    }

    protected void commitWithWarnings() {
        // by default this method does nothing
        // but it can be overridden by screens to do screen
        // specific actions
    }

    public void clearErrors() {
        for (Widget wid : def.getWidgets().values()) {
            if (wid instanceof HasField)
                ((HasField)wid).clearExceptions();
        }
        window.clearStatus();
        window.clearMessagePopup("");
    }

    public void drawScreen(ScreenDefInt def) throws Exception {
        this.def = def;
        screenpanel.clear();
        screenpanel.add(def.getPanel());
    }

    public void addScreenHandler(Widget wid, ScreenEventHandler<?> screenHandler) {
        assert wid != null : "addScreenHandler received a null widget";

        screenHandler.target = wid;
        addDataChangeHandler(screenHandler);
        addStateChangeHandler(screenHandler);
        if (wid instanceof HasField)
            ((HasField)wid).addFieldValueChangeHandler(screenHandler);
        if (wid instanceof HasClickHandlers)
            ((HasClickHandlers)wid).addClickHandler(screenHandler);
    }

    public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
        return addHandler(handler, DataChangeEvent.getType());
    }

    public HandlerRegistration addStateChangeHandler(StateChangeHandler<org.openelis.gwt.screen.Screen.State> handler) {
        return addHandler(handler, StateChangeEvent.getType());
    }

    public HandlerRegistration addResizeHandler(ResizeHandler handler) {
        return addHandler(handler, ResizeEvent.getType());
    }

    protected void setFocus(Widget widget) {
        def.getPanel().setFocusWidget(widget);
    }
}
