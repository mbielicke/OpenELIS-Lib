package org.openelis.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.client.services.AutoCompleteServiceInt;
import org.openelis.gwt.client.services.AutoCompleteServiceIntAsync;
import org.openelis.gwt.common.AutoCompleteRPC;
/**
 * AutoCompleteTextBox extends a GWT TextBox to add auto suggestion features that can be 
 * requested from a RemoteServiceServlet implementing the AutoCompleteServiceInt.  Suggestions
 * will appear in a pop up box below the textbox and the user can arrow down or click on an
 * item to select it.
 * 
 * @author tschmidt
 *
 */
public class AutoCompleteTextBox extends TextBox implements
                                                KeyboardListener,
                                                ChangeListener {
	/**
	 * Panel to display returned suggestions
	 */
    protected PopupPanel choicesPopup = new PopupPanel(true);
    /**
     * Widget used to display the suggestions and register
     * click events.
     */
    protected ListBox choices = new ListBox() {
        public void onBrowserEvent(Event event) {
            if (Event.ONCLICK == DOM.eventGetType(event)) {
                complete();
            }
        }
    };
    protected boolean popupAdded = false;
    protected boolean visible = false;
    protected String popupWidth = "";
    protected String fieldCase = "mixed";
    /**
     * RPC class for returning data from the server.
     */
    private AutoCompleteRPC rpc;
    /**
     * Category for which suggestions we are trying to match
     */
    private String cat;
    /**
     * Callback class for handling returning matches
     */
    private GetMatches matchCallback = new GetMatches();
    /**
     * Callback class for handing display call
     */
    private GetDisplay displayCallback = new GetDisplay();
    public Integer value;
    private ChangeListenerCollection callback;
    private AutoCompleteServiceIntAsync autoService = (AutoCompleteServiceIntAsync)GWT.create(AutoCompleteServiceInt.class);
    private ServiceDefTarget target = (ServiceDefTarget)autoService;

    /**
     * This Method will set the url for the AutoCompleteService.
     * 
     * @param url
     */
    public void initService(String url) {
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }
    
    public void setCase(String fieldCase){
        this.fieldCase = fieldCase;
    }

    /**
     * This inner class is used to add a delay to the before calling the 
     * back end to limit the number of calls made to the server.  If a
     * second key is hit within a certain amount of time the call for
     * suggestions is cancelled
     * 
     * @author tschmidt
     *
     */
    private class Delay extends Timer {
        public String text;

        public Delay(String text, int time) {
            this.text = text;
            this.schedule(time);
        }

        public void run() {
            if (getText().equals(text)) {
                if(fieldCase.equals("upper"))
                    text = text.toUpperCase();
                else if(fieldCase.equals("lower"))
                    text = text.toLowerCase();
                callForMatches(text);
            }
        }
    };


    /**
     * Contstructor for AutoCompleteTextBox
     * @param cat
     * Category is used to know which values to match the entered text to.
     * @param serviceUrl
     * This is the url to the RemoteServiceServlet that handles calls for 
     * this widget
     */
    public AutoCompleteTextBox(String cat, String serviceUrl) {
        super();
        initService(serviceUrl);
        this.cat = cat;
        this.addKeyboardListener(this);
        choices.addChangeListener(this);
        choices.sinkEvents(Event.ONCLICK);
        this.addStyleName("AutoCompleteTextBox");
        choicesPopup.add(choices);
        choicesPopup.addStyleName("AutoCompleteChoices");
        choices.addStyleName("list");
    }

    /**
     * Not used at all
     */
    public void onKeyDown(Widget arg0, char arg1, int arg2) {
    }

    /**
     * Not used at all
     */
    public void onKeyPress(Widget arg0, char arg1, int arg2) {
    }

    /**
     * A key was released, start autocompletion
     */
    public void onKeyUp(Widget arg0, char arg1, int arg2) {
        if (arg1 == KEY_DOWN) {
            int selectedIndex = choices.getSelectedIndex();
            selectedIndex++;
            if (selectedIndex > choices.getItemCount()) {
                selectedIndex = 0;
            }
            choices.setSelectedIndex(selectedIndex);
            return;
        }
        if (arg1 == KEY_UP) {
            int selectedIndex = choices.getSelectedIndex();
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = choices.getItemCount();
            }
            choices.setSelectedIndex(selectedIndex);
            return;
        }
        if (arg1 == KEY_ENTER) {
            if (visible) {
                complete();
            }
            return;
        }
        if (arg1 == KEY_ESCAPE) {
            choices.clear();
            choicesPopup.hide();
            visible = false;
            return;
        }
        String text = this.getText();
        value = null;
        if (text.length() > 0 && !text.endsWith("*")) {
            new Delay(text, 350);
        } else {
            choices.clear();
            choicesPopup.hide();
            visible = false;
        }
    }

    /**
     * Displays the suggestion returned from the server.
     */
    public void showMatches() {
        if (rpc.display.length > 0) {
            choices.clear();
            for (int i = 0; i < rpc.display.length; i++) {
                choices.addItem((String)rpc.display[i]);
            }
            // if there is only one match and it is what is in the
            // text field anyways there is no need to show autocompletion
            if (rpc.display.length == 1 && (rpc.display[0].compareTo(rpc.match) == 0 || rpc.textboxValue[0].compareTo(rpc.match) == 0)) {
                choicesPopup.hide();
            } else {
                choices.setSelectedIndex(0);
                choices.setVisibleItemCount(rpc.display.length + 1);
                visible = true;
                
                choicesPopup.setPopupPosition(this.getAbsoluteLeft(),
                                              this.getAbsoluteTop() + this.getOffsetHeight());

                if("".equals(popupWidth)){
                	choices.setWidth((this.getOffsetWidth() + 10) + "px");
                }else{
                	choices.setWidth(popupWidth);
                }
                choicesPopup.show();
            }
        } else {
            visible = false;
            choicesPopup.hide();
        }
    }

    /**
     * A mouseclick in the list of items
     */
    public void onChange(Widget arg0) {
        complete();
    }

    /**
     * Set the selection that the user made.
     */
    protected void complete() {
        if (choices.getItemCount() > 0) {
        	if(rpc.textboxValue != null){
        		this.setText(rpc.textboxValue[choices.getSelectedIndex()]);
        		this.value = rpc.id[choices.getSelectedIndex()];
        	}else{
        		this.setText(choices.getItemText(choices.getSelectedIndex()));
                this.value = rpc.id[choices.getSelectedIndex()];
        	}
        }
        choices.clear();
        choicesPopup.hide();
        if (callback != null)
            callback.fireChange(this);
    }

    /**
     * Method that calls the service to retrieve the suggestions
     * @param text
     */
    protected void callForMatches(final String text) {
        autoService.getMatches(cat, text, matchCallback);
    }

    /**
     * Sets the value for the widget and calls the server to retrieve the display
     * value
     * @param value
     */
    public void setValue(Integer value) {
        this.value = value;
        if (value != null && value.intValue() > 0)
            autoService.getDisplay(cat, value, displayCallback);
        else
            setText("");
    }

    /**
     * GetDisplay Calls the RemoteServlet to get the display for the set value
     * @author tschmidt
     *
     */
    private class GetDisplay implements AsyncCallback {
        public void onSuccess(Object result) {
            rpc = (AutoCompleteRPC)result;
            setText(rpc.dict_value);
        }

        public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
        }
    }

    /**
     * GetMatches call the RemoteServlet to get the suggestions to display
     * @author tschmidt
     *
     */
    private class GetMatches implements AsyncCallback {
        public void onSuccess(Object result) {
            rpc = (AutoCompleteRPC)result;
            showMatches();
        }

        public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
        }
    }

    /**
     * I think is deprecated and should be deleted
     * @param callback
     */
    public void setCallback(ChangeListenerCollection callback) {
        this.callback = callback;
    }

    /**
     * This method will wipe the textbox and set the selection value to null
     */
    public void reset() {
        this.value = null;
        setText("");
    }

	public String getPopupWidth() {
		return popupWidth;
	}

	public void setPopupWidth(String popupWidth) {
		this.popupWidth = popupWidth;
	}
}