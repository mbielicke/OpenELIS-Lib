package org.openelis.gwt.client.widget;

import org.openelis.gwt.client.screen.ScreenBase;
import org.openelis.gwt.client.screen.ScreenLabel;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * ButtonPanel is a widget that combines 9 buttons for controlling a widget that 
 * implements the FormInt interface.  Clicks to buttons in this widget will call
 * methods defined in FormInt.  The Form that this widget controls must be set
 * by calling setForm(Formint for) method.  The state for the form is held in this 
 * widget. For styling purposes labels are used to represent the buttons.
 *   
 * @author tschmidt
 *
 */
public class ButtonPanel extends Composite implements ClickListener {

	/**
	 * Panel used to display buttons
	 */
    private HorizontalPanel hp = new HorizontalPanel();
    /**
     * Label used to display Query button. 
     */
    public ScreenLabel query = new ScreenLabel("Query",null);
    /**
     * Label used to display Next button. 
     */
    public ScreenLabel next = new ScreenLabel("Next",null);
    /**
     * Label used to display Previous button. 
     */
    public ScreenLabel prev = new ScreenLabel("Previous",null);
    /**
     * Label used to display Add button. 
     */
    public ScreenLabel add = new ScreenLabel("Add",null);
    /**
     * Label used to display Update button. 
     */
    public ScreenLabel up = new ScreenLabel("Update",null);
    /**
     * Label used to display Delete button. 
     */
    public ScreenLabel delete = new ScreenLabel("Delete",null);
    /**
     * Label used to display Commit button. 
     */
    public ScreenLabel comm = new ScreenLabel("Commit",null);
    /**
     * Label used to display Abort button. 
     */
    public ScreenLabel abort = new ScreenLabel("Abort",null);
    /**
     * Label used to display Reload button. 
     */
    public ScreenLabel reload = new ScreenLabel("Reload",null);
    /**
     * Label used to display Select button.
     */
    public ScreenLabel select = new ScreenLabel("Select",null);
    /**
     * The Form this widget controls
     */
    protected FormInt form;
    /**
     * The current state of the Form
     */
    public int state = FormInt.DISPLAY;

    private ScreenLabel pressed;
    /**
     * Constuctor for creating ButtonPanel
     * @param buttons
     * <pre>
     * buttons attribute represents which buttons to display
     *   q - Query
     *   u - Update
     *   n - Next
     *   p - Previous
     *   b - Abort
     *   d - Delete
     *   c - Commit
     *   r - Reload
     *   a - Add
     *   s = Select
     * </pre>
     */
    public ButtonPanel(String buttons) {
        query.setStyleName("ScreenPanelButton");
        query.sinkEvents(Event.MOUSEEVENTS);
        query.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        next.setStyleName("ScreenPanelButton");
        next.sinkEvents(Event.MOUSEEVENTS);
        next.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        prev.setStyleName("ScreenPanelButton");
        prev.sinkEvents(Event.MOUSEEVENTS);
        prev.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        add.setStyleName("ScreenPanelButton");
        add.sinkEvents(Event.MOUSEEVENTS);
        add.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        up.setStyleName("ScreenPanelButton");
        up.sinkEvents(Event.MOUSEEVENTS);
        up.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        delete.setStyleName("ScreenPanelButton");
        delete.sinkEvents(Event.MOUSEEVENTS);
        delete.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        comm.setStyleName("ScreenPanelButton");
        comm.sinkEvents(Event.MOUSEEVENTS);
        comm.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        abort.setStyleName("ScreenPanelButton");
        abort.sinkEvents(Event.MOUSEEVENTS);
        abort.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        reload.setStyleName("ScreenPanelButton");
        reload.sinkEvents(Event.MOUSEEVENTS);
        reload.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        select.setStyleName("ScreenPanelButton");
        select.sinkEvents(Event.MOUSEEVENTS);
        select.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        int numbuttons = 1;
        if(!buttons.equals("all"))
            numbuttons = buttons.length();
        for(int i = 0; i < numbuttons; i++){
            if (buttons.equals("all") || buttons.charAt(i) == 'q') {
                hp.add(query);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 'n') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }
                hp.add(next);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 'p') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }
                hp.add(prev);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 'a') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }
                hp.add(add);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 'u') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }
                hp.add(up);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 'd') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }
                hp.add(delete);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 'c') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }
                hp.add(comm);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 'b') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }   
                hp.add(abort);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 'r') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }
                hp.add(reload);
            }
            if (buttons.equals("all") || buttons.charAt(i) == 's') {
                if(hp.getWidgetCount() > 0){
                    addSeparator();
                }
                hp.add(select);
            }
        }
        initWidget(hp);
        enable("all",true);
        enable("npdcbrs", false);
    }
    /**
     * Method used to add a blank td between buttons for styling
     */
    private void addSeparator() {
        HTML sep = new HTML();
        sep.addStyleDependentName("separator");
        hp.add(sep);
        hp.setCellWidth(sep, "1px");
    }

    /**
     * This method sets the Form that this ButtonPanel controls
     * @param form
     */
    public void setForm(FormInt form) {
        this.form = form;
    }

    /**
     * Handler for button clicks
     */
    public void onClick(Widget sender) {
        if (sender == query) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = query;
            pressed.addStyleName("Pressed");
            form.query(state);
        }
        if (sender == next) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = null;
            form.next(state);
        }
        if (sender == prev) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = null;
            form.prev(state);
        }
        if (sender == add) {
            if(pressed != null) {
                pressed.removeStyleName("Pressed");
            }
            pressed = add;
            pressed.addStyleName("Pressed");
            form.add(state);
        }
        if (sender == up) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = up;
            pressed.addStyleName("Pressed");
            form.up(state);
        }
        if (sender == delete) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = null;
            form.delete(state);
        }
        if (sender == comm) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = null;
            form.commit(state);
        }
        if (sender == abort) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = null;
            form.abort(state);
        }
        if (sender == reload) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = null;
            form.reload(state);
        }
        if (sender == select) {
            if(pressed != null)
                pressed.removeStyleName("Pressed");
            pressed = null;
            form.select(state);
        }
    }
    
    /**
     * Returns the current state of the form
     * @return state
     */
    public int getState() {
        return state;
    }
    
    /**
     * Sets the ButtonPanel to the state passed in.
     * @param state
     */
    public void setState(int state) {
        if(state == FormInt.ADD || state == FormInt.QUERY || state == FormInt.UPDATE){
            enable("quanpdrs",false);
            enable("cb",true);
        }
        if(state == FormInt.DISPLAY){
            enable("quadrs",true);
            enable("cb",false);
            //ttb 11/28/07 took out np so that we can control it from the screen instead
        }
        if(state == FormInt.BROWSE){
            enable("quadnprs",true);
            enable("cb",false);
        }
        this.state = state;
        
    }
    /**
     * This method will either enable or disable the buttons that are passed in through the buttons 
     * parameter 
     * 
     * @param buttons
     * @param enabled
     */
    public void enable(String buttons, boolean enabled){
        if (buttons.equals("all") || buttons.indexOf("q") > -1) {
            enable(query,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("n") > -1) {
            enable(next,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("p") > -1) {
            enable(prev,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("a") > -1) {
            enable(add,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("u") > -1) {
            enable(up,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("d") > -1) {
            enable(delete,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("c") > -1) {
            enable(comm,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("b") > -1) {
            enable(abort,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("r") > -1) {
            enable(reload,enabled);
        }
        if (buttons.equals("all") || buttons.indexOf("s") > -1) {
            enable(select,enabled);
        }
    }
    
    /**
     * Private method to enable/disable a specific button.
     * @param wid
     * @param enabled
     */
    private void enable(ScreenLabel wid, boolean enabled){
        if(!enabled){
            wid.addStyleName("disabled");
            wid.removeClickListener(this);
        }else{
            wid.removeStyleName("disabled");
            wid.addClickListener(this);
        }
    }
    
    public void removeButtons(String buttons){
        if (buttons.indexOf("q") > -1) {
           removeButton(query);
        }
        if (buttons.indexOf("n") > -1) {
            removeButton(next);
        }
        if (buttons.indexOf("p") > -1) {
            removeButton(prev);
        }
        if (buttons.indexOf("a") > -1) {
            removeButton(add);
        }
        if (buttons.indexOf("u") > -1) {
            removeButton(up);
        }
        if (buttons.indexOf("d") > -1) {
            removeButton(delete);
        }
        if (buttons.indexOf("c") > -1) {
            removeButton(comm);
        }
        if (buttons.indexOf("b") > -1) {
            removeButton(abort);
        }
        if (buttons.indexOf("r") > -1) {
            removeButton(reload);
        }
        if (buttons.indexOf("s") > -1) {
            removeButton(select);
        }
    }
    
    private void removeButton(Widget wid){
        int index = hp.getWidgetIndex(wid);
        hp.remove(index);
        if(hp.getWidgetCount() > 1)
            hp.remove(index);
    }

}
