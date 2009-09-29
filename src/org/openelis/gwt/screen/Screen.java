package org.openelis.gwt.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.openelis.gwt.common.FieldErrorException;
import org.openelis.gwt.common.FormErrorException;
import org.openelis.gwt.common.TableFieldErrorException;
import org.openelis.gwt.common.ValidationErrorsList;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.HasDataChangeHandlers;
import org.openelis.gwt.event.HasStateChangeHandlers;
import org.openelis.gwt.event.StateChangeEvent;
import org.openelis.gwt.event.StateChangeHandler;
import org.openelis.gwt.widget.ScreenWindow;
import org.openelis.gwt.services.ScreenService;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.table.TableWidget;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Screen extends Composite implements HasStateChangeHandlers<Screen.State>,
                                     HasDataChangeHandlers {

    public enum State {
        DEFAULT, DISPLAY, UPDATE, ADD, QUERY, DELETE
    };

    public String                         name;
    public State                          state        = State.DEFAULT;
    public ScreenWindow                   window;
    protected ScreenDefInt                def;
    protected ScreenService               service;
    public static UIFocusHandler          focusHandler = new UIFocusHandler();
    public static HashMap<String, String> consts;
    public final AbsolutePanel            panel        = new AbsolutePanel();

    /**
     * No arg constructor will initiate a blank panel and new FormRPC
     */
    public Screen() {
        initWidget(panel);
        sinkEvents(Event.ONKEYPRESS);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONKEYPRESS)
            // event.preventDefault();
            super.onBrowserEvent(event);
    }

    public Screen(String url) throws Exception {
        initWidget(panel);
        service = new ScreenService(url);
        def = new ScreenDef();
        ((ScreenDef)def).loadURL = url;
        UIUtil.createWidgets((ScreenDef)def);
        panel.add(def.getPanel());
    }

    public Screen(ScreenDefInt def) {
        initWidget(panel);
        this.def = def;
        panel.add(def.getPanel());
    }

    public void drawScreen(ScreenDefInt def) throws Exception {
        this.def = def;
        panel.clear();
        panel.add(def.getPanel());
    }

    public void setWindow(ScreenWindow window) {
        this.window = window;
    }

    public ScreenDefInt getDefinition() {
        return def;
    }

    public ScreenWindow getWindow() {
        return window;
    }

    public void setDef(ScreenDefInt def) {
        this.def = def;
    }

    public void addScreenHandler(Widget wid, ScreenEventHandler<?> screenHandler) {
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

    public void setState(Screen.State state) {
        this.state = state;
        StateChangeEvent.fire(this, state);
    }

    protected ArrayList<QueryData> getQueryFields() {
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

    protected boolean validate() {
        boolean valid = true;
        for (Widget wid : def.getWidgets().values()) {
            if (wid instanceof HasField) {
                ((HasField)wid).checkValue();
                if ( ((HasField)wid).getErrors() != null) {
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
            } else if (ex instanceof FormErrorException) {
                FormErrorException fe = (FormErrorException)ex;
                formErrors.add(consts.get(fe.getMessage()));

            } else {
                FieldErrorException fe = (FieldErrorException)ex;
                ((HasField)def.getWidget(fe.getFieldName())).addError(consts.get(fe.getMessage()));
            }
        }

        if (formErrors.size() == 0)
            window.setError(consts.get("correctErrors"));
        else if (formErrors.size() == 1)
            window.setError(formErrors.get(0));
        else {
            window.setError("(Error 1 of " + formErrors.size() + ") " + formErrors.get(0));
            window.setMessagePopup(formErrors, "ErrorPanel");
        }
    }

    protected String getString(Object obj) {
        if (obj == null)
            return "";

        return obj.toString();
    }


    private static class UIFocusHandler implements FocusHandler, BlurHandler {
        public void onFocus(FocusEvent event) {
            if ( ((HasField)event.getSource()).isEnabled()) {
                ((Widget)event.getSource()).addStyleName("Focus");
            }
        }
        public void onBlur(BlurEvent event) {
            if ( ((HasField)event.getSource()).isEnabled()) {
                ((Widget)event.getSource()).removeStyleName("Focus");
            }
        }
    }

}
