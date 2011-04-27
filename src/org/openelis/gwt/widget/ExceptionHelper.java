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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * This is a Singleton class that will handle displaying exceptions that have
 * been added to a widget when moused over by user.
 * 
 * @author tschmidt
 * 
 */
public class ExceptionHelper {

    /**
     * Final widgets used for displaying Exceptions in Popup
     */
    protected static final VerticalPanel                                exceptionPanel;
    protected static final DecoratorPanel                               dp;
    protected static final PopupPanel                                   popPanel;
    protected static final HashMap<HasExceptions,HandlerRegistration>   overHandlers;
    protected static final HashMap<HasExceptions,HandlerRegistration>   outHandlers;
    protected static final Timer                                        timer;
    
    protected static final ExceptionHandlers                            handlers;

    /**
     * Private constructor used to delay creating final widgets until needed
     */
    static {
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
        
        handlers = new ExceptionHandlers();
    }

    /**
     * Clears Exception Styles form widget
     * 
     * @param widget
     */
    private static void clearExceptionStyle(HasExceptions widget) {
        widget.removeExceptionStyle("InputError");
        widget.removeExceptionStyle("InputWarning");
    }
    
    /**
     * Checks the exceptions on the widget and sets the appropiate style
     * 
     * @param widget
     */
    private static void setExceptionStyle(HasExceptions widget) {
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
    public static void checkExceptionHandlers(HasExceptions widget) {
        clearExceptionStyle(widget);
        if (widget.hasExceptions()) {
            if (!overHandlers.containsKey(widget)) { 
                overHandlers.put(widget, widget.addMouseOverHandler(handlers));
                outHandlers.put(widget, widget.addMouseOutHandler(handlers));
            }
            setExceptionStyle(widget);
        } else {
            if (overHandlers.containsKey(widget)){ 
                overHandlers.remove(widget).removeHandler();
                outHandlers.remove(widget).removeHandler();
            }
        }
        
    }

    public static void drawExceptions(ArrayList<LocalizedException> endUser, ArrayList<LocalizedException> valid,final int x, final int y) {
        ArrayList<LocalizedException> exceptions = null;
        Grid grid;

        // Clear panel 
        exceptionPanel.clear();
        grid = new Grid(0,2);
        exceptionPanel.add(grid);
        
       
        // Get out if widget has no exceptions to display;
        if (endUser == null && valid == null)
            return;

        for (int i = 0; i < 2; i++ ) {
            switch (i) {
                /*
                 * First iteration check EndUserException if no
                 * EndUserExceptions do i++ and fall through to check
                 * validatExceptions
                 */
                case 0:
                    exceptions = endUser;
                    if (exceptions == null)
                        continue;
                    break;
                    /*
                     * If no validation exceptions continue out of loop
                     */
                case 1:
                    exceptions = valid;
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
        
        popPanel.setPopupPositionAndShow(new PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int offset = x;
                if(x+offsetWidth > Window.getClientWidth())  
                    offset -= x + offsetWidth - Window.getClientWidth() - 10;

                popPanel.setPopupPosition(offset,y+10);
            }
        });

        timer.schedule(5000); 
    }
    
    protected static class ExceptionHandlers  implements MouseOverHandler, MouseOutHandler {
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
            HasExceptions source;
            
            source = (HasExceptions)event.getSource();
            
            if (source.hasExceptions()) 
                drawExceptions(source.getEndUserExceptions(),
                               source.getValidateExceptions(),
                               event.getClientX(),event.getClientY());
            
        }
    }
    
    public static void closePopup() {
        popPanel.hide();
    }

}
