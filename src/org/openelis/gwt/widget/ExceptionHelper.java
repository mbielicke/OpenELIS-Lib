package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.HashMap;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Warning;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is a Singleton class that will handle displaying exceptions that have
 * been added to a widget when moused over by user.
 * 
 * @author tschmidt
 * 
 */
public class ExceptionHelper implements MouseOverHandler, MouseOutHandler {

    /**
     * Final widgets used for displaying Exceptions in Popup
     */
    protected final VerticalPanel                                exceptionPanel;
    protected final DecoratorPanel                               dp;
    protected final PopupPanel                                   popPanel;
    protected final HashMap<HasExceptions,HandlerRegistration>   overHandlers;
    protected final HashMap<HasExceptions,HandlerRegistration>   outHandlers;
    protected final Timer                                        timer;

    /**
     * Static Singleton instance of ExceptionUtil.
     */
    private static ExceptionHelper   instance = new ExceptionHelper();

    /**
     * Private constructor used to delay creating final widgets until needed
     */
    private ExceptionHelper() {
        // Creation of final widgets
        exceptionPanel = new VerticalPanel();
        popPanel       = new PopupPanel(true);
        dp             = new DecoratorPanel();
        overHandlers   = new HashMap<HasExceptions,HandlerRegistration>();
        outHandlers    = new HashMap<HasExceptions,HandlerRegistration>();

        dp.setStyleName("ErrorWindow");
        dp.add(exceptionPanel);
        dp.setVisible(true);

        popPanel.setWidget(dp);
        popPanel.setStyleName("");
        
        timer = new Timer() {
            public void run() {
                popPanel.hide();
            }
        };
    }

    /**
     * Method get Singleton instance
     * 
     * @return
     */
    public static ExceptionHelper getInstance() {
        return instance;
    }

    /**
     * Clears Exception Styles form widget
     * 
     * @param widget
     */
    private void clearExceptionStyle(HasExceptions widget) {
        widget.removeExceptionStyle("InputError");
        widget.removeExceptionStyle("InputWarning");
    }
    
    /**
     * Checks the exceptions on the widget and sets the appropiate style
     * 
     * @param widget
     */
    private void setExceptionStyle(HasExceptions widget) {
        ArrayList<LocalizedException> exceptions = null;
    
        for (int i = 0; i < 2; i++ ) {
            switch (i) {
                /*
                 * First iteration check EndUserException if no
                 * EndUserExceptions do i++ and fall through to check
                 * validatExceptions
                 */
                case 0:
                    exceptions = widget.getEndUserExceptions();
                    if (exceptions == null)
                        continue;
                    break;
                    /*
                     * If no validation exceptions continue out of loop
                     */
                case 1:
                    exceptions = widget.getValidateExceptions();
                    if (exceptions == null)
                        continue;
            }

            for (LocalizedException exception : exceptions) {
                if(!(exception instanceof Warning)){
                    widget.addExceptionStyle("InputError");
                    return;
                }
            }
        }
        
        widget.addExceptionStyle("InputWarning");
    }
    
    
    
    /**
     * Adds and removes Mouse handlers for displaying Exceptions as needed
     */
    public void checkExceptionHandlers(HasExceptions widget) {
        clearExceptionStyle(widget);
        if (widget.hasExceptions()) {
            if (!overHandlers.containsKey(widget)) { 
                overHandlers.put(widget, widget.addMouseOverHandler(this));
                outHandlers.put(widget, widget.addMouseOutHandler(this));
            }
            setExceptionStyle(widget);
        } else {
            if (overHandlers.containsKey(widget)){ 
                overHandlers.remove(widget).removeHandler();
                outHandlers.remove(widget).removeHandler();
            }
        }
        
    }

    /**
     * Method called from MouseOverHandler to draw and display exceptions in the
     * PopupPanel.
     * 
     * @param widget
     */
    protected void drawExceptions(HasExceptions widget) {
        ArrayList<LocalizedException> exceptions = null;
        Grid grid;

        // Clear panel and widget exception styling
        exceptionPanel.clear();
        grid = new Grid(0,2);
        exceptionPanel.add(grid);
        
        clearExceptionStyle(widget);

        // Get out if widget has no exceptions to display;
        if ( !widget.hasExceptions())
            return;

        // Start at Warning level and override to error level if necessary
        String style = "InputWarning";
        for (int i = 0; i < 2; i++ ) {
            switch (i) {
                /*
                 * First iteration check EndUserException if no
                 * EndUserExceptions do i++ and fall through to check
                 * validatExceptions
                 */
                case 0:
                    exceptions = widget.getEndUserExceptions();
                    if (exceptions == null)
                        continue;
                    break;
                    /*
                     * If no validation exceptions continue out of loop
                     */
                case 1:
                    exceptions = widget.getValidateExceptions();
                    if (exceptions == null)
                        continue;
            }

            for (LocalizedException exception : exceptions) {
                grid.insertRow(0);
                if (exception instanceof Warning) {
                    grid.getCellFormatter().setStyleName(0, 0, "WarnIcon");
                    grid.getCellFormatter().setStyleName(0,1,"warnPopupLabel");
                } else {
                    grid.getCellFormatter().setStyleName(0, 0, "ErrorIcon");
                    grid.getCellFormatter().setStyleName(0,1,"errorPopupLabel");
                }
                grid.setText(0, 1, exception.getMessage());
            }
        }

        widget.addExceptionStyle(style);
    }

    /**
     * Handler to hide PopupPanel when user mouses off widget
     */
    public void onMouseOut(MouseOutEvent event) {
        popPanel.hide();
        timer.cancel();
    }

    /**
     * Handler to show Exceptions when a user mouses over a widget
     */
    public void onMouseOver(MouseOverEvent event) {
        if ( ((HasExceptions)event.getSource()).hasExceptions()) {
            drawExceptions((HasExceptions)event.getSource());
            
            popPanel.setPopupPosition( ((Widget)event.getSource()).getAbsoluteLeft() +
                                      ((Widget)event.getSource()).getOffsetWidth(),
                                      ((Widget)event.getSource()).getAbsoluteTop());
            
            popPanel.show();

            timer.schedule(5000);
        }
    }
}
